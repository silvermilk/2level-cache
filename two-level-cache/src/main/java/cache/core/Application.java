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
                .setExpirationStrategy(StrategyType.LRU)
                .setMaxBytesLocalDisk(1000000)
                .setMaxEntriesMemoryLevel(1000)
                .setPathToLocalDisk("D:/");
                
        Cache <String, Integer> cache = CacheFactory.createCache(config);

    }
}
