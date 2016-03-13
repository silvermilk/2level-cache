package cache.api;

import cache.core.Configuration;
import cache.core.TwoLevelCache;
import cache.store.DiskStore;
import cache.store.MemoryStore;
import cache.strategy.ExpirationStrategy;
import cache.strategy.ExpirationStrategyFactory;
import java.io.Serializable;

/**
 *
 * @author Nastya
 */
public class CacheFactory {

    public static <K, V extends Serializable> Cache<K, V> createCache(Configuration config) {
        TwoLevelCache <K, V> cache = new TwoLevelCache<>();
        ExpirationStrategy expirationStrategy = ExpirationStrategyFactory.expirationStrategy(config.getExpirationStrategyType(), cache);
        MemoryStore<K, V> memoryStore = new MemoryStore<>(expirationStrategy, config.getMaxEntriesMemoryLevel());
        DiskStore<K, V> diskStore = new DiskStore<>(expirationStrategy, config.getMaxBytesLocalDisk(), config.getPathToLocalDisk());
        cache.setMemoryStore(memoryStore);
        cache.setDiskStore(diskStore);
        return cache;
    }
}
