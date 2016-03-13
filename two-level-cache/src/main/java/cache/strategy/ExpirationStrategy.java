package cache.strategy;

import cache.store.DiskStore;
import cache.store.MemoryStore;
import java.util.LinkedHashMap;

/**
 *
 * @author Nastya
 */
public interface ExpirationStrategy {

    //expiration rule
    public LinkedHashMap createMemoryLevelCache(int maxEntries);

    //eviction rule
    public LinkedHashMap createDiskLevelCache(DiskStore diskLevel);
}
