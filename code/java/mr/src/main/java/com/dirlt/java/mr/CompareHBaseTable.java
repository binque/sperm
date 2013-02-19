package com.dirlt.java.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 2/19/13
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompareHBaseTable {
    public static void main(String[] args) throws Exception {
        String t1 = "t1";
        String t2 = "t2";
        if (args.length >= 2) {
            t1 = args[0];
            t2 = args[1];
        }

        Configuration configuration = HBaseConfiguration.create();
        Scan scan = new Scan();
        scan.setCaching(500); // 1 is the default in Scan, which will be bad for MR
        scan.setCacheBlocks(false);

        HTable table1 = new HTable(configuration, t1);
        HTable table2 = new HTable(configuration, t2);
        ResultScanner r1 = table1.getScanner(scan);
        ResultScanner r2 = table2.getScanner(scan);
        try {
            int count = 0;
            while (true) {
                Result ra = r1.next();
                Result rb = r2.next();
                count++;
                if ((count % (64 * 1024)) == 0) {
                    System.err.println("count = " + count);
                }
                if (ra == null && rb == null) {
                    System.err.println("Congratulations! They are same!");
                    break;
                } else if (ra == null) {
                    System.err.println("BAD! table " + t2 + " has more");
                    break;
                } else if (rb == null) {
                    System.err.println("BAD! table " + t1 + " has more");
                    break;
                } else {
                    if (!Arrays.equals(ra.getRow(), rb.getRow())) {
                        System.err.println("BAD! ROW DIFF! '" + Bytes.toString(ra.getRow()) + "', '" + Bytes.toString(rb.getRow()) + "'");
                        break;
                    }
                    // damn.
                    NavigableMap<byte[], NavigableMap<byte[], byte[]>> v1 = ra.getNoVersionMap();
                    NavigableMap<byte[], NavigableMap<byte[], byte[]>> v2 = ra.getNoVersionMap();
                    Iterator<Map.Entry<byte[], NavigableMap<byte[], byte[]>>> iterator1 = v1.entrySet().iterator();
                    Iterator<Map.Entry<byte[], NavigableMap<byte[], byte[]>>> iterator2 = v2.entrySet().iterator();
                    while (iterator1.hasNext() && iterator2.hasNext()) {

                    }
                    if (iterator1.hasNext()) {
                        // TODO(dirlt):
                    } else if (iterator2.hasNext()) {
                        // TODO(dirlt):
                    }
                }
            }
        } finally {
            r1.close();
            r2.close();
            table1.close();
            table2.close();
        }
    }
}
