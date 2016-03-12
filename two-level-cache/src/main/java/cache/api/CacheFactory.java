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
        MemoryStore memoryLevel = new MemoryStore(config.getExpirationStrategy(), config.getMaxBytesMemoryLevel());
        DiskStore diskLevel = new DiskStore(config.getExpirationStrategy(), config.getMaxBytesLocalDisk(), config.getPathToLocalDisk());
        return new TwoLevelCache(memoryLevel, diskLevel);
    }
}
