/* coding:utf-8
 * Copyright (C) dirlt
 */

package com.dirlt.java;

import java.util.Date;

import org.apache.log4j.Logger;

public class Timer {
	private static final Logger LOG = Logger.getLogger(Timer.class);

	public static void main(String[] args) throws InterruptedException {
		
		// TODO(dirlt):
		LOG.info("begin timing");
		Date d=new Date();
		Thread.sleep(1000);
		Date d2=new Date();		
		long elapse=d2.getTime()-d.getTime();
		System.out.println(elapse);
		LOG.info("end timing, elapsed "+elapse);
	}
}
