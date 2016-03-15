package cache.strategy;

import cache.core.DiskElementInfo;
import cache.core.TwoLevelCache;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class LRUExpirationStrategy<K, V extends Serializable> implements ExpirationStrategy {

    static final boolean ACCESS_ORDER = true;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    private final TwoLevelCache<K, V> cache;

    public LRUExpirationStrategy(TwoLevelCache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public LinkedHashMap<K, V> createMemoryLevelCache(final int maxEntries) {
        return new LinkedHashMap<K, V>(maxEntries, DEFAULT_LOAD_FACTOR, ACCESS_ORDER) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                if (size() > maxEntries) {
                    //move eldest entry to the second level of cache
                    cache.getDiskStore().put(eldest.getKey(), eldest.getValue());
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public LinkedHashMap<K, DiskElementInfo> createDiskLevelCache() {

        return new LinkedHashMap<K, DiskElementInfo>(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, ACCESS_ORDER) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, DiskElementInfo> eldest) {
                if (cache.isDiskStorageOverflow()) {
                    cache.evictEntryFromDisk(eldest.getKey());
                    return true;
                }
                return false;
            }
        };
    }
}
