package com.dirlt.java.FastHBaseRest;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 12/8/12
 * Time: 1:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class Configuration {
    private int restPort;
    private int metricPort;
    private String quorumSpec;

    public Configuration(String[] args) {
        // TODO(dirlt):
    }

    public int getRestPort() {
        return restPort;
    }

    public int getMetricPort() {
        return metricPort;
    }

    public String getQuorumSpec() {
        return quorumSpec;
    }
}
