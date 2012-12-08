package com.dirlt.java.FastHBaseRest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 12/8/12
 * Time: 1:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class CpuWorkerPool {
    // singleton.
    private ExecutorService executorService = null;
    private static CpuWorkerPool instance = null;

    public static void init(Configuration configuration) {
        instance = new CpuWorkerPool(configuration);
    }

    private CpuWorkerPool(Configuration configuration) {
        // TODO(dirlt): some strategy.
        executorService = Executors.newCachedThreadPool();
    }

    public static CpuWorkerPool getInstance() {
        return instance;
    }

    public void submit(Runnable runnable) {
        // wait runnable.
        executorService.submit(runnable);
    }
}
