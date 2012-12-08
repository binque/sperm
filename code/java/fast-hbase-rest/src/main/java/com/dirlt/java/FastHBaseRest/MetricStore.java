package com.dirlt.java.FastHBaseRest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 12/8/12
 * Time: 1:42 AM
 * To change this template use File | Settings | File Templates.
 */

public class MetricStore {
    // singleton.
    private Map<String, Long> counter = new HashMap<String, Long>();
    private Map<String, String> status = new HashMap<String, String>();
    private static MetricStore instance = new MetricStore();

    public static final String kRpcInCount = "rpc.in.count";
    public static final String kRpcOutCount = "rpc.out.count";
    public static final String kRpcInBytes = "rpc.in.bytes";
    public static final String kRpcOutBytes = "rpc.out.bytes";
    public static final String kConnectionCount = "connection.count";
    public static final String kProtobufInvalidCount = "protobuf.invalid.count";

    public static void incRpcInCount() {
        instance.addCounter(kRpcInCount, 1);
    }

    public static void addRpcInBytes(long bytes) {
        instance.addCounter(kRpcInBytes, bytes);
    }

    public static void incRpcOutCount() {
        instance.addCounter(kRpcOutCount, 1);
    }

    public static void addRpcOutBytes(long bytes) {
        instance.addCounter(kRpcOutBytes, bytes);
    }

    public static void incConnectionCount() {
        instance.addCounter(kConnectionCount, 1);
    }

    public static void decConnectionCount() {
        instance.addCounter(kConnectionCount, -1);
    }

    public static void incProtobufInvalidCount() {
        instance.addCounter(kProtobufInvalidCount, 1);
    }

    public static MetricStore getInstance() {
        return instance;
    }

    public void addCounter(String name, long value) {
        synchronized (counter) {
            if (counter.containsKey(name)) {
                counter.put(name, counter.get(name) + value);
            } else {
                counter.put(name, value);
            }
        }
    }

    public long getCounter(String name) {
        synchronized (counter) {
            if (!counter.containsKey(name)) {
                return 0L;
            } else {
                return counter.get(name);
            }
        }
    }

    public void updateStatus(String name, String value) {
        synchronized (status) {
            status.put(name, value);
        }
    }

    public void getStatus(String name) {
        synchronized (status) {
            status.get(name);
        }
    }

    // well a little too simple.:).
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("----------counter----------\n");
        synchronized (counter) {
            Set<Map.Entry<String, Long>> entries = counter.entrySet();
            for (Map.Entry<String, Long> entry : entries) {
                sb.append(String.format("%s = %s\n", entry.getKey(), entry.getValue().toString()));
            }
        }
        sb.append("----------status----------\n");
        synchronized (status) {
            Set<Map.Entry<String, String>> entries = status.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                sb.append(String.format("%s = %s\n", entry.getKey(), entry.getValue()));
            }
        }
        return sb.toString();
    }
}