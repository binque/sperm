package com.dirlt.java.playboard;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 3/12/13
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapPerformanceTest {
    private static final int NUMBER = 10000000;
    private static final String PREFIX = "s";

    public static void action(Map<String, Long> dict) {
        for (int i = 0; i < NUMBER; i++) {
            dict.put(PREFIX + i, (long) i);
        }
        for (int i = 0; i < NUMBER; i++) {
            dict.put(PREFIX + i, dict.get(PREFIX + i) + dict.get(PREFIX + (i + 1000) % NUMBER));
        }
    }

    public static void main(String[] args) {
        Map<String, Long> dict = new HashMap<String, Long>();
        long start = System.currentTimeMillis();
        action(dict);
        long end = System.currentTimeMillis();
        System.out.printf("%.2f\n", (float) (end - start));
    }
}

