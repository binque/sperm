/* coding:utf-8
 * Copyright (C) dirlt
 */

package com.dirlt.hadoop;

import junit.framework.TestCase;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
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
}