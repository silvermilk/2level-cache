package cache.api;

import cache.core.Configuration;
import cache.core.TwoLevelCache;
import cache.store.DiskStore;
import cache.store.MemoryStore;

/**
 *
 * @author Nastya
 */
public class CacheFactory {

    public static Cache createCache(Configuration config) {
        MemoryStore memoryStore = new MemoryStore(config.getExpirationStrategy(), config.getMaxEntriesMemoryLevel());
        DiskStore diskStore = new DiskStore(config.getExpirationStrategy(), config.getMaxBytesLocalDisk(), config.getPathToLocalDisk());
        return new TwoLevelCache(memoryStore, diskStore);
    }
}
