package com.dirlt.java.FastHBaseRest;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 12/8/12
 * Time: 1:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class Configuration {
    private int port = 8000;
    private String quorumSpec = "localhost:2181";
    private int cpuThreadNumber = 16;
    private int cpuQueueSize = 4096;
    private int cacheExpireTime = 3600;
    private int cacheMaxCapacity = 2 * 1024 * 1024; // 2M
    private String serviceName = "unknown";
    private boolean debug = false;

    public boolean parse(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--port=")) {
                port = Integer.valueOf(arg.substring("--port=".length())).intValue();
            } else if (arg.startsWith("--hbase-quorum-spec=")) {
                quorumSpec = arg.substring("--hbase-quorum-spec=".length());
            } else if (arg.startsWith("--cpu-thread-number=")) {
                cpuThreadNumber = Integer.valueOf(arg.substring("--cpu-thread-number=".length()));
            } else if (arg.startsWith("--cpu-queue-size=")) {
                cpuQueueSize = Integer.valueOf(arg.substring("--cpu-queue-size=".length()));
            } else if (arg.startsWith("--cache-expire-time=")) {
                cacheExpireTime = Integer.valueOf(arg.substring("--cache-expire-time=".length()));
            } else if (arg.startsWith("--cache-max-capacity=")) {
                cacheMaxCapacity = Integer.valueOf(arg.substring("--cache-max-capacity=".length()));
            } else if (arg.startsWith("--service-name=")) {
                serviceName = arg.substring("--service-name=".length());
            } else if (arg.startsWith("--debug")) {
                debug = true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static void usage() {
        System.out.println("Fast HBase Rest");
        System.out.println("options:");
        System.out.println("\t--port # default 8000");
        System.out.println("\t--debug # debug mode");
        System.out.println("\t--quorum-spec # zookeeper address list. eg. \"host1,host2,host3\". default \"localhost\"");
        System.out.println("\t--cpu-thread-number # default 16");
        System.out.println("\t--cpu-queue-size # default 4096");
        System.out.println("\t--cache-expire-time # default 3600 in seconds");
        System.out.println("\t--cache-max-capacity # default 2 * 1024 * 1024");
    }

    public int getPort() {
        return port;
    }

    public String getQuorumSpec() {
        return quorumSpec;
    }

    public int getCpuThreadNumber() {
        return cpuThreadNumber;
    }

    public int getCpuQueueSize() {
        return cpuQueueSize;
    }

    public int getCacheExpireTime() {
        return cacheExpireTime;
    }

    public int getCacheMaxCapacity() {
        return cacheMaxCapacity;
    }

    public String getServiceName() {
        return serviceName;
    }

    public boolean isDebug() {
        return debug;
    }
}
