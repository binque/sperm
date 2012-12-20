package com.dirlt.java.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 12/20/12
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class TableManager {
    private static Configuration configuration = HBaseConfiguration.create();
    private static final String kTableName = 't1';
    private static final String kColumnFamily = 'cf';

    public static void createTable() throws IOException {
        HBaseAdmin hbase = new HBaseAdmin(conf);
        HTableDescriptor desc = new HTableDescriptor("t1");
        desc.addFamily(new HColumnDescriptor(Bytes.toBytes("personal")));
        desc.addFamily(new HColumnDescriptor(Bytes.toBytes("account")));
        hbase.createTable(desc);
    }

    public static void deleteTable() throws IOException {
        HBaseAdmin hbase = new HBaseAdmin(conf);
        if(hbase.tableExists())

    }
}
