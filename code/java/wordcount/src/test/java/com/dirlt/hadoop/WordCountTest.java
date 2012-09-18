/* coding:utf-8
 * Copyright (C) dirlt
 */

package com.dirlt.hadoop;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.junit.Before;
import org.junit.Test;

public class WordCountTest extends TestCase {
    private Mapper mapper;
    private Reducer reducer;
    private MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> driver;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        mapper = new WordCount.Map();
        reducer = new WordCount.Reduce();
        driver = new MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable>(
                mapper, reducer);
    }

    // really useful.
    public static void info(String s) {
        System.out.println("[INFO]" + s);
    }

    public static void debug(String s) {
        System.err.println("[DEBUG]" + s);
    }

    @Test
    public void testCase1() {
        driver.addInput(new LongWritable(0), new Text("hello"));
        driver.addInput(new LongWritable(0), new Text("world"));
        driver.addInput(new LongWritable(0), new Text("hello"));
        driver.addInput(new LongWritable(0), new Text("world"));
        driver.addOutput(new Text("hello"), new IntWritable(2));
        driver.addOutput(new Text("world"), new IntWritable(2));
        driver.runTest();
    }

    public int writableObjectSize(Writable w) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        w.write(dos);
        return dos.size();
    }

    @Test
    public void testCase2() throws IOException {
        // writable size.
        
        // int size.
        info("int size = " + writableObjectSize(new IntWritable(10)));
        // text size.
        info("text size = " + writableObjectSize(new Text("hello")));
        // array size.
        info("array size = "
                + writableObjectSize(new ArrayWritable(IntWritable.class,
                        new Writable[] { new IntWritable(10),
                                new IntWritable(10), new IntWritable(10),
                                new IntWritable(10) })));
    }
}