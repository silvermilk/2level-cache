package cache.strategy;

import cache.core.DiskElementInfo;
import cache.core.TwoLevelCache;
import java.io.Serializable;
import java.util.LinkedHashMap;


public abstract class AbstractExpirationStrategy<K, V extends Serializable> implements ExpirationStrategy {

    TwoLevelCache<K, V> cache;
        
    @Override
    public LinkedHashMap<K, V> createMemoryLevelCache(final int maxEntries) {
        return null;
    }

    @Override
    public LinkedHashMap<K, DiskElementInfo> createDiskLevelCache() {
        return null;
    }
}
