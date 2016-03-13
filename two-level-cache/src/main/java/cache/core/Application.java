package cache.core;

import cache.api.Cache;
import cache.api.CacheFactory;
import cache.strategy.StrategyType;

/**
 *
 * @author Nastya
 */
public class Application {
    public static void main(String[] args) {

        Configuration config = new Configuration()
                .setExpirationStrategyType(StrategyType.LRU)
                .setMaxBytesLocalDisk(1000)
                .setMaxEntriesMemoryLevel(2)
                .setPathToLocalDisk("D:\\");
                
        Cache <String, Integer> cache = CacheFactory.createCache(config);
        cache.put("erett", 123234);
        cache.put("retrt", 1268234);
        cache.put("eretrttytyt", 454657);
        cache.put("rewr", 454657);
        cache.put("eretrttretretytyt", 454657);
        
        System.out.println(cache.get("rewr"));

    }
}
