package cache.strategy;

import cache.core.DiskElementInfo;
import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 *
 * @author Nastya
 */
public interface ExpirationStrategy <K, V extends Serializable> {

    //expiration rule
    public LinkedHashMap<K, V> createMemoryLevelCache(int maxEntries);

    //eviction rule
    public LinkedHashMap<K, DiskElementInfo> createDiskLevelCache();
}
