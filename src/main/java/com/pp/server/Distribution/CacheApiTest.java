package com.pp.server.Distribution;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.spi.CachingProvider;
import javax.swing.plaf.synth.SynthOptionPaneUI;

import static javax.cache.expiry.Duration.ONE_HOUR;

public class CacheApiTest {
    public static void main(String args[])
    {
        //resolve a cache manager
        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        //configure the cache
        MutableConfiguration<String, Integer> config = new MutableConfiguration<>();
        //uses store by reference
        config.setStoreByValue(false)
                .setTypes(String.class, Integer.class)
                .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(ONE_HOUR))
                .setStatisticsEnabled(true);

        //create the cache
        cacheManager.createCache("simpleOptionalCache", config);

        //... and then later to get the cache
        Cache<String, Integer> cache = Caching.getCache("simpleOptionalCache",
                String.class, Integer.class);

        //use the cache
        String key = "key";
        Integer value1 = 1;
        cache.put("key", value1);
        Integer value2 = cache.get(key);

        System.out.println("value2:"+value2);
        cache.remove("key");
        if (!cache.containsKey(key))
        {
            System.out.println("cache not contains key "+ key);
        }

    }
}

