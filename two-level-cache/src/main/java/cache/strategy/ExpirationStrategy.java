package cache.strategy;

import cache.core.DiskElementInfo;
import java.io.Serializable;
import java.util.Map;

public interface ExpirationStrategy <K, V extends Serializable> {

    //expiration rule
    public Map<K, V> createMemoryLevelCache(int maxEntries);

    //eviction rule
    public Map<K, DiskElementInfo> createDiskLevelCache();
}
