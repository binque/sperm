package com.dirlt.java.FastHBaseRest;

import com.dirlt.java.FastHbaseRest.MessageProtos1;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.stumbleupon.async.Callback;
import com.stumbleupon.async.Deferred;
import org.hbase.async.GetRequest;
import org.hbase.async.KeyValue;
import org.hbase.async.PutRequest;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 12/8/12
 * Time: 2:03 AM
 * To change this template use File | Settings | File Templates.
 */

public class AsyncClient implements Runnable {
    public static final String kSep = String.format("%c", 0x0);

    // state of each step.
    enum Status {
        kStat,
        kHttpRequest,
        kReadRequest,
        kWriteRequest,
        kReadLocalCache,
        kReadCacheService,
        kReadHBaseService,
        kWriteHBaseService,
        kReadResponse,
        kWriteResponse,
        kHttpResponse,
    }

    public Status code = Status.kStat; // default value.
    public Channel channel;

    public HttpRequest httpRequest;
    public String path;
    public byte[] bs;
    public MessageProtos1.ReadRequest rdReq;
    public MessageProtos1.WriteRequest wrReq;

    public Message msg;
    public MessageProtos1.ReadResponse.Builder rdRes;
    public MessageProtos1.WriteResponse.Builder wrRes;

    public long sessionStartTimestamp;
    public long sessionEndTimestamp;
    public long readStartTimestamp;
    public long readEndTimestamp;
    public long readHBaseServiceStartTimestamp;
    public long readHBaseServiceEndTimestamp;
    public long writeHBaseServiceStartTimestamp;
    public long writeHBaseServiceEndTimestamp;


    public String tableName;
    public String rowKey;
    public String columnFamily;

    public String prefix; // cache key prefix.

    public static String makeCacheKeyPrefix(String tableName, String rowKey, String cf) {
        return tableName + kSep + rowKey + kSep + cf;
    }

    public static String makeCacheKey(String prefix, String column) {
        return prefix + kSep + column;
    }

    private List<String> readCacheQualifiers; // qualifiers that to be queried from cache.
    private List<String> readHBaseQualifiers; // qualifiers that to be queried from hbase.
    // if == null, then read column family.

    @Override
    public void run() {
        if (code == Status.kHttpRequest) {
            handleHttpRequest();
        } else if (code == Status.kReadRequest) {
            readRequest();
        } else if (code == Status.kWriteRequest) {
            writeRequest();
        } else if (code == Status.kReadLocalCache) {
            readLocalCache();
        } else if (code == Status.kReadCacheService) {
            readCacheService();
        } else if (code == Status.kReadHBaseService) {
            readHBaseService();
        } else if (code == Status.kWriteHBaseService) {
            writeHBaseService();
        } else if (code == Status.kReadResponse) {
            readResponse();
        } else if (code == Status.kWriteResponse) {
            writeResponse();
        } else if (code == Status.kHttpResponse) {
            handleHttpResponse();
        }
    }

    public void handleHttpRequest() {
        RestServer.logger.debug("http request");

        ChannelBuffer buffer = httpRequest.getContent();
        int size = buffer.readableBytes();
        StatStore.getInstance().addCounter("rpc.in.bytes", size);
        bs = new byte[size];
        buffer.readBytes(bs);

        if (path.equals("/write")) {
            code = Status.kWriteRequest;
        } else {
            // default is read request.
            code = Status.kReadRequest;
        }
        run();
    }

    public void readRequest() {
        // parse request.
        MessageProtos1.ReadRequest.Builder builder = MessageProtos1.ReadRequest.newBuilder();
        try {
            builder.mergeFrom(bs);
        } catch (InvalidProtocolBufferException e) {
            // just close channel.
            RestServer.logger.debug("parse message exception");
            StatStore.getInstance().addCounter("rpc.in.count.invalid", 1);
            channel.close();
        }
        rdReq = builder.build();
        readStartTimestamp = System.currentTimeMillis();

        tableName = rdReq.getTableName();
        rowKey = rdReq.getRowKey();
        columnFamily = rdReq.getColumnFamily();

        rdRes = MessageProtos1.ReadResponse.newBuilder();
        rdRes.setTableName(tableName);
        rdRes.setRowKey(rowKey);
        rdRes.setColumnFamily(columnFamily);
        prefix = makeCacheKeyPrefix(tableName, rowKey, columnFamily);

        // reset qualifiers.
        readCacheQualifiers = null;
        readHBaseQualifiers = null;
        if (rdReq.getQualifiersCount() == 0) {
            // read column family
            // then we can't do cache.
            code = Status.kReadHBaseService;
        } else {
            // raise local cache request.
            code = Status.kReadLocalCache;
        }
        StatStore.getInstance().addCounter("rpc.read.count", 1);
        run();
    }

    public void writeRequest() {
        MessageProtos1.WriteRequest.Builder builder = MessageProtos1.WriteRequest.newBuilder();
        // parse request.
        try {
            builder.mergeFrom(bs);
        } catch (InvalidProtocolBufferException e) {
            // just close channel.
            RestServer.logger.debug("parse message exception");
            StatStore.getInstance().addCounter("rpc.in.count.invalid", 1);
            channel.close();
        }
        wrReq = builder.build();

        tableName = wrReq.getTableName();
        rowKey = wrReq.getRowKey();
        columnFamily = wrReq.getColumnFamily();

        // prepare the result.
        wrRes = MessageProtos1.WriteResponse.newBuilder();

        code = Status.kWriteHBaseService;
        StatStore.getInstance().addCounter("rpc.write.count", 1);
        run();
    }

    public void readLocalCache() {
        RestServer.logger.debug("read local cache");

        readCacheQualifiers = new ArrayList<String>();

        // check local cache mean while fill the cache request.
        int readCount = 0;
        int cacheCount = 0;
        for (String q : rdReq.getQualifiersList()) {
            String cacheKey = makeCacheKey(prefix, q);
            RestServer.logger.debug("search cache with key = " + cacheKey);
            byte[] b = LocalCache.getInstance().get(cacheKey);
            readCount += 1;
            if (b != null) {
                RestServer.logger.debug("cache hit!");
                cacheCount += 1;
                MessageProtos1.ReadResponse.KeyValue.Builder bd = MessageProtos1.ReadResponse.KeyValue.newBuilder();
                bd.setQualifier(q);
                bd.setContent(ByteString.copyFrom(b));
                rdRes.addKvs(bd);
            } else {
                RestServer.logger.debug("read hbase qualifier: " + q);
                readCacheQualifiers.add(q);
            }
        }
        StatStore.getInstance().addCounter("read.count", readCount);
        StatStore.getInstance().addCounter("read.count.local-cache", cacheCount);

        if (!readCacheQualifiers.isEmpty()) {
            code = Status.kReadCacheService; // read cache service.
        } else {
            code = Status.kReadResponse; // return directly.
        }
        run();
    }

    public void readCacheService() {
        readHBaseQualifiers = readCacheQualifiers;

        // TODO(dirlt): add cache service for example redis or memcached.
        // but you also have to think about the access pattern.

        // raise hbase service request.
        code = Status.kReadHBaseService;
        run();
    }


    public void readHBaseService() {
        RestServer.logger.debug("read hbase service");
        RestServer.logger.debug("tableName = " + tableName + ", rowKey = " + rowKey + ", columnFamily = " + columnFamily);

        GetRequest getRequest = new GetRequest(tableName, rowKey);
        getRequest.family(columnFamily);
        if (readHBaseQualifiers != null) { // read qualifiers.
            // otherwise we read all qualifiers from column family.
            // a little bit tedious.
            byte[][] qualifiers = new byte[readHBaseQualifiers.size()][];
            int idx = 0;
            for (String q : readHBaseQualifiers) {
                qualifiers[idx] = q.getBytes();
                idx += 1;
            }
            getRequest.qualifiers(qualifiers);
            StatStore.getInstance().addCounter("read.count.hbase.column", qualifiers.length);
        } else {
            StatStore.getInstance().addCounter("read.count.hbase.column-family", 1);
        }

        Deferred<ArrayList<KeyValue>> deferred = HBaseService.getInstance().get(getRequest);
        final AsyncClient client = this;
        client.readHBaseServiceStartTimestamp = System.currentTimeMillis();
        // if failed, we don't return.
        deferred.addCallback(new Callback<Object, ArrayList<KeyValue>>() {
            @Override
            public Object call(ArrayList<KeyValue> keyValues) throws Exception {
                // we don't return because we put into CpuWorkerPool.
                client.code = Status.kReadResponse;
                client.readHBaseServiceEndTimestamp = System.currentTimeMillis();
                if (readHBaseQualifiers != null) { // read qualifiers.
                    // fill the cache and builder.
                    for (KeyValue kv : keyValues) {
                        String k = new String(kv.qualifier()); // not kv.key(), that's rowkey.
                        String cacheKey = makeCacheKey(prefix, k);
                        byte[] value = kv.value();
                        RestServer.logger.debug("fill cache with key = " + cacheKey);
                        LocalCache.getInstance().set(cacheKey, value);
                        MessageProtos1.ReadResponse.KeyValue.Builder bd = MessageProtos1.ReadResponse.KeyValue.newBuilder();
                        bd.setQualifier(k);
                        bd.setContent(ByteString.copyFrom(value));
                        client.rdRes.addKvs(bd);
                    }
                    StatStore.getInstance().addCounter("read.duration.hbase.column",
                            client.readHBaseServiceEndTimestamp - client.readHBaseServiceStartTimestamp);
                } else {
                    // just fill the builder. don't save them to cache.
                    for (KeyValue kv : keyValues) {
                        String k = new String(kv.qualifier());
                        byte[] value = kv.value();
                        MessageProtos1.ReadResponse.KeyValue.Builder bd = MessageProtos1.ReadResponse.KeyValue.newBuilder();
                        bd.setQualifier(k);
                        bd.setContent(ByteString.copyFrom(value));
                        client.rdRes.addKvs(bd);
                    }
                    StatStore.getInstance().addCounter("read.duration.hbase.column-family",
                            client.readHBaseServiceEndTimestamp - client.readHBaseServiceStartTimestamp);
                }
                CpuWorkerPool.getInstance().submit(client);
                return null;
            }
        });
        deferred.addErrback(new Callback<Object, Exception>() {
            @Override
            public Object call(Exception o) throws Exception {
                // we don't care.
                return null;
            }
        });
    }

    public void writeHBaseService() {
        RestServer.logger.debug("write hbase service");
        RestServer.logger.debug("tableName = " + tableName + ", rowKey = " + rowKey + ", columnFamily = " + columnFamily);

        byte[][] qualifiers = new byte[wrReq.getKvsCount()][];
        byte[][] values = new byte[wrReq.getKvsCount()][];
        for (int i = 0; i < wrReq.getKvsCount(); i++) {
            qualifiers[i] = wrReq.getKvs(i).getQualifier().getBytes();
            values[i] = wrReq.getKvs(i).getContent().toByteArray();
        }
        StatStore.getInstance().addCounter("write.count", wrReq.getKvsCount());

        PutRequest putRequest = new PutRequest(tableName.getBytes(), rowKey.getBytes(), columnFamily.getBytes(), qualifiers, values);

        Deferred<Object> deferred = HBaseService.getInstance().put(putRequest);
        final AsyncClient client = this;
        client.writeHBaseServiceStartTimestamp = System.currentTimeMillis();
        // if failed, we don't return.
        deferred.addCallback(new Callback<Object, Object>() {
            @Override
            public Object call(Object obj) throws Exception {
                // we don't return because we put into CpuWorkerPool.
                client.code = Status.kWriteResponse;
                client.writeHBaseServiceEndTimestamp = System.currentTimeMillis();
                StatStore.getInstance().addCounter("write.duration",
                        client.writeHBaseServiceEndTimestamp - client.writeHBaseServiceStartTimestamp);
                CpuWorkerPool.getInstance().submit(client);
                return null;
            }
        });
        deferred.addErrback(new Callback<Object, Exception>() {
            @Override
            public Object call(Exception o) throws Exception {
                // we don't care.
                return null;
            }
        });
    }

    public void readResponse() {
        msg = rdRes.build();
        code = Status.kHttpResponse;
        readEndTimestamp = System.currentTimeMillis();
        StatStore.getInstance().addCounter("read.duration", readEndTimestamp - readStartTimestamp);
        run();
    }

    public void writeResponse() {
        msg = wrRes.build();
        code = Status.kHttpResponse;
        run();
    }

    public void handleHttpResponse() {
        RestServer.logger.debug("http response");

        HttpResponse response = new DefaultHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        int size = msg.getSerializedSize();
        response.setHeader("Content-Length", size);
        ByteArrayOutputStream os = new ByteArrayOutputStream(size);
        try {
            msg.writeTo(os);
            ChannelBuffer buffer = ChannelBuffers.copiedBuffer(os.toByteArray());
            response.setContent(buffer);
            channel.write(response); // write over.
            StatStore.getInstance().addCounter("rpc.out.bytes", size);
        } catch (Exception e) {
            // just ignore it.
        }
    }
}

