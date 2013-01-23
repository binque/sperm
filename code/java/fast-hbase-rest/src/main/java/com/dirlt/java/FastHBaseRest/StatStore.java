package com.dirlt.java.FastHBaseRest;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 12/8/12
 * Time: 1:42 AM
 * To change this template use File | Settings | File Templates.
 */

public class StatStore {
    // singleton.
    private Map<String, Long> counter = new TreeMap<String, Long>();
    private Map<String, String> status = new TreeMap<String, String>();
    private String serviceName = null;
    private boolean stat = true;
    private static StatStore instance = null;

    public static StatStore getInstance() {
        return instance;
    }

    public StatStore(Configuration configuration) {
        this.serviceName = configuration.getServiceName();
        this.stat = configuration.isStat();
    }

    public static void init(Configuration configuration) {
        instance = new StatStore(configuration);
    }

    public void addCounter(String name, long value) {
        if (!stat) {
            return;
        }
        synchronized (counter) {
            if (counter.containsKey(name)) {
                counter.put(name, counter.get(name) + value);
            } else {
                counter.put(name, value);
            }
        }
    }

    public void updateStatus(String name, String value) {
        if (!stat) {
            return;
        }
        synchronized (status) {
            status.put(name, value);
        }
    }

    // well a little too simple.:).
    public String toString() {
        if (!stat) {
            return "statistics off";
        }
        StringBuffer sb = new StringBuffer();
//        sb.append("----------counter----------\n");
        synchronized (counter) {
            Set<Map.Entry<String, Long>> entries = counter.entrySet();
            for (Map.Entry<String, Long> entry : entries) {
                sb.append(String.format("%s = %s\n", entry.getKey(), entry.getValue().toString()));
            }
        }
//        sb.append("----------status----------\n");
        synchronized (status) {
            Set<Map.Entry<String, String>> entries = status.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                sb.append(String.format("%s = %s\n", entry.getKey(), entry.getValue()));
            }
        }
        return sb.toString();
    }
}