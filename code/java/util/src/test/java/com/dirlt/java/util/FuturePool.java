package com.dirlt.java.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FuturePool<IN, OUT> {
	public static interface CallbackInterface<IN, OUT> {
		OUT onOK(IN response);

		OUT onException(Exception e);
	}

	private ExecutorService executorService;

	FuturePool(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public Future<OUT> putFutureWithCallback(final Future<IN> future,
			final CallbackInterface<IN, OUT> callback) {
		Callable<OUT> c = new Callable<OUT>() {
			public OUT call() {
				try {
					return callback.onOK(future.get());
				} catch (Exception e) {
					return callback.onException(e);
				}
			}
		};
		System.out.println("submit blocking...");
		return executorService.submit(c);
	}

	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(1);
		FuturePool<String, String> pool = new FuturePool<String, String>(
				executorService); // share a thread pool.		
		Future<String> blockTask = executorService
				.submit(new Callable<String>() {
					public String call() {
						try {
							System.out.println("start sleep...");
							Thread.sleep(5000); // 5 seconds.
							System.out.println("sleep over...");
							return "sleep over";
						} catch (InterruptedException e) {
							e.printStackTrace();
							return "sleep interrupted";
						}
					}
				});
		Future<String> blockTaskResult = pool.putFutureWithCallback(blockTask,
				new CallbackInterface<String, String>() {
					public String onOK(String in) {
						System.out.println(in);
						return "OK";
					}

					public String onException(Exception e) {
						e.printStackTrace();
						return "Caught Exception";
					}
				});
		System.out.println(blockTaskResult.get());
		executorService.shutdown();
	}
}
