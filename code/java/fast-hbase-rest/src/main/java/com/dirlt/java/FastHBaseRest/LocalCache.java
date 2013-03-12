package com.dirlt.java.FastHBaseRest;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: dirlt
 * Date: 12/8/12
 * Time: 2:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class LocalCache {
    // singleton.
    private static Cache<String, byte[]> cache = null;
    private static LocalCache instance = null;

    public static void init(Configuration configuration) {
        instance = new LocalCache(configuration);
    }

    private LocalCache(Configuration configuration) {
        RestServer.logger.info("cache-expire-time=" + configuration.getCacheExpireTime() +
                "(s), cache-max-capacity=" + configuration.getCacheMaxCapacity());
        CacheBuilder builder = CacheBuilder.newBuilder();
        builder.expireAfterWrite(configuration.getCacheExpireTime(), TimeUnit.SECONDS);
        builder.maximumSize(configuration.getCacheMaxCapacity());
        cache = builder.build();
    }

    public static LocalCache getInstance() {
        return instance;
    }

    public byte[] get(String k) {
        return cache.getIfPresent(k);
    }

    public void set(String k, byte[] b) {
        cache.put(k, b);
    }

    public void clear() {
        cache.invalidateAll();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        CacheStats stats = cache.stats();
//        sb.append(String.format("cache.request.count = %s\n", stats.requestCount()));
//        sb.append(String.format("cache.hit.count = %s\n", stats.hitCount()));
//        sb.append(String.format("cache.hit.rate = %s\n", stats.hitRate()));
//        sb.append(String.format("cache.miss.count = %s\n", stats.missCount()));
//        sb.append(String.format("cache.miss.rate = %s\n", stats.missRate()));
        sb.append(String.format("cache.eviction.count = %s\n", stats.evictionCount()));
        return sb.toString();
    }
}
