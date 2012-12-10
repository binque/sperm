package com.dirlt.java.FastHBaseRest;

import com.dirlt.java.FastHbaseRest.MessageProtos1;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.stumbleupon.async.Callback;
import com.stumbleupon.async.Deferred;
import org.hbase.async.GetRequest;
import org.hbase.async.KeyValue;
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
    public static final int kDefaultVersion = 1;
    public static final String kSep = String.format("%c", 0x0);

    // state of each step.
    enum Status {
        kHttpRequest,
        kLocalCache,
        kCacheService,
        kHBaseService,
        kHttpResponse,
    }


    public Status code;
    public Channel channel;
    public HttpRequest httpRequest;
    public MessageProtos1.Request protoRequest;
    public MessageProtos1.Response.Builder protoResponseBuilder;

    public String tableName;
    public String rowKey;
    public String columnFamily;
    public String prefix; // cache key prefix.

    private List<String> queryCacheQualifiers;
    private List<String> queryHBaseQualifiers;

    public static String makeCacheKeyPrefix(String tableName, String rowKey, String cf) {
        return tableName + kSep + rowKey + kSep + cf;
    }

    public static String makeCacheKey(String prefix, String column) {
        return prefix + kSep + column;
    }

    @Override
    public void run() {
        if (code == Status.kHttpRequest) {
            RestServer.logger.debug("http request");
            ChannelBuffer buffer = httpRequest.getContent();
            int size = buffer.readableBytes();
            StatStore.addRpcInBytes(size); // rpc in bytes.
            byte[] bs = new byte[size];
            buffer.readBytes(bs);
            // parse request.
            try {
                MessageProtos1.Request.Builder builder = MessageProtos1.Request.newBuilder();
                builder.mergeFrom(bs);
                protoRequest = builder.build();
            } catch (InvalidProtocolBufferException e) {
                // just close channel.
                RestServer.logger.debug("parse protobuf exception");
                StatStore.incProtobufInvalidCount();
                channel.close();
            }

            tableName = protoRequest.getTableName();
            rowKey = protoRequest.getRowKey();
            columnFamily = protoRequest.getColumnFamily();

            protoResponseBuilder = MessageProtos1.Response.newBuilder();
            protoResponseBuilder.setTableName(tableName);
            protoResponseBuilder.setRowKey(rowKey);
            protoResponseBuilder.setColumnFamily(columnFamily);
            prefix = makeCacheKeyPrefix(tableName, rowKey, columnFamily);

            // raise local cache request.
            code = Status.kLocalCache;
            run();

        } else if (code == Status.kLocalCache) {
            RestServer.logger.debug("local cache");
            queryCacheQualifiers = new ArrayList<String>();

            // check local cache mean while fill the cache request.
            int queryCount = 0;
            int cacheCount = 0;
            for (String q : protoRequest.getQualifiersList()) {
                String cacheKey = makeCacheKey(prefix, q);
                RestServer.logger.debug("search cache with key = " + cacheKey);
                byte[] b = LocalCache.getInstance().get(cacheKey);
                queryCount += 1;
                if (b != null) {
                    RestServer.logger.debug("cache hit!");
                    cacheCount += 1;
                    MessageProtos1.Response.KeyValue.Builder bd = MessageProtos1.Response.KeyValue.newBuilder();
                    bd.setQualifier(q);
                    bd.setContent(ByteString.copyFrom(b));
                    protoResponseBuilder.addKvs(bd);
                } else {
                    RestServer.logger.debug("query hbase qualifier: " + q);
                    queryCacheQualifiers.add(q);
                }
            }
            StatStore.addQueryCount(queryCount);
            StatStore.addQueryLocalCacheCount(cacheCount);

            if (!queryCacheQualifiers.isEmpty()) {
                code = Status.kCacheService; // query cache service.
            } else {
                code = Status.kHttpResponse; // return directly.
            }
            run();

        } else if (code == Status.kCacheService) {
            queryHBaseQualifiers = queryCacheQualifiers;

            // TODO(dirlt): add cache service for example redis or memcached.
            // but you also have to think about the access pattern.

            // raise hbase service request.
            code = Status.kHBaseService;
            run();

        } else if (code == Status.kHBaseService) {
            RestServer.logger.debug("hbase service");
            RestServer.logger.debug("tableName = " + tableName + ", rowKey = " + rowKey + ", columnFamily = " + columnFamily);
            GetRequest getRequest = new GetRequest(tableName, rowKey);
            getRequest.family(columnFamily);
            // a little bit tedious.
            byte[][] qualifiers = new byte[queryHBaseQualifiers.size()][];
            int idx = 0;
            for (String q : queryHBaseQualifiers) {
                qualifiers[idx] = q.getBytes();
                idx += 1;
            }
            getRequest.qualifiers(qualifiers);
            Deferred<ArrayList<KeyValue>> deferred = HBaseService.getInstance().get(getRequest);
            final AsyncClient client = this;
            // if failed, we don't return.
            deferred.addCallback(new Callback<Object, ArrayList<KeyValue>>() {
                @Override
                public Object call(ArrayList<KeyValue> keyValues) throws Exception {
                    // we don't return because we put into CpuWorkerPool.
                    client.code = Status.kHttpResponse;
                    // fill the cache and builder.
                    for (KeyValue kv : keyValues) {
                        String k = new String(kv.qualifier()); // not kv.key(), that's rowkey.
                        String cacheKey = makeCacheKey(prefix, k);
                        byte[] value = kv.value();
                        RestServer.logger.debug("fill cache with key = " + cacheKey);
                        LocalCache.getInstance().set(cacheKey, value);
                        MessageProtos1.Response.KeyValue.Builder bd = MessageProtos1.Response.KeyValue.newBuilder();
                        bd.setQualifier(k);
                        bd.setContent(ByteString.copyFrom(value));
                        client.protoResponseBuilder.addKvs(bd);
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
        } else if (code == Status.kHttpResponse) {
            RestServer.logger.debug("http response");
            HttpResponse response = new DefaultHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            MessageProtos1.Response msg = protoResponseBuilder.build();
            int size = msg.getSerializedSize();
            StatStore.addRpcOutBytes(size);
            response.setHeader("Content-Length", size);
            ByteArrayOutputStream os = new ByteArrayOutputStream(size);
            try {
                msg.writeTo(os);
                ChannelBuffer buffer = ChannelBuffers.copiedBuffer(os.toByteArray());
                response.setContent(buffer);

                channel.write(response); // write over.
            } catch (Exception e) {
                // just igtnore it.
            }
        }
    }
}

