package com.dirlt.java.hbase;

//import org.apache.hadoop.hbase.HBaseConfiguration;
//import org.apache.hadoop.hbase.HColumnDescriptor;
//import org.apache.hadoop.hbase.HTableDescriptor;
//import org.apache.hadoop.hbase.client.HBaseAdmin;
//import org.apache.hadoop.hbase.util.Bytes;
//
//public class Utility {
//	public static void createHTable(String tableName, String... families)
//			throws Exception {
//		HTableDescriptor tableDescriptor = new HTableDescriptor(
//				Bytes.toBytes(tableName));
//		for (int i = 0; i < families.length; i++) {
//			HColumnDescriptor descriptor = new HColumnDescriptor(
//					Bytes.toBytes(families[i]));
//			tableDescriptor.addFamily(descriptor);
//		}
//		HBaseAdmin admin = new HBaseAdmin(HBaseConfiguration.create());
//		admin.createTable(tableDescriptor);
//	}
//
//	public static void dropHTable(String tableName) throws Exception {
//		HBaseAdmin admin = new HBaseAdmin(HBaseConfiguration.create());
//		admin.disableTable(tableName);
//		admin.deleteTable(tableName);
//	}
//
//	public static boolean isHTableAvailable(String tableName) throws Exception {
//		HBaseAdmin admin = new HBaseAdmin(HBaseConfiguration.create());
//		return admin.isTableAvailable(tableName);
//	}
//}
