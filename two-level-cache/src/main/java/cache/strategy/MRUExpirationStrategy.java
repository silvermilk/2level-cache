package cache.strategy;

import cache.core.DiskElementInfo;
import cache.core.TwoLevelCache;
import java.io.Serializable;
import java.util.LinkedHashMap;

public class MRUExpirationStrategy<K, V extends Serializable> implements ExpirationStrategy {

    static final boolean ACCESS_ORDER = true;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private final TwoLevelCache<K, V> cache;

    public MRUExpirationStrategy(TwoLevelCache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public LinkedHashMap<K, V> createMemoryLevelCache(final int maxEntries) {
        return new LinkedHashMap<K, V>(maxEntries, DEFAULT_LOAD_FACTOR, ACCESS_ORDER) {
            @Override
            public V put(K key, V value) {
                if (size() >= maxEntries) {
                    //move entry to the second level of cache
                    cache.getDiskStore().put(key, value);
                } else {
                    value = super.put(key, value);
                }
                return value;
            }
        };
    }

    @Override
    public LinkedHashMap<K, DiskElementInfo> createDiskLevelCache() {
        return new LinkedHashMap<K, DiskElementInfo>(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, ACCESS_ORDER) {
            @Override
            public DiskElementInfo put(K key, DiskElementInfo value) {
                if (cache.isDiskStorageOverflow()) {
                    cache.evictEntryFromDisk(key);
                    remove(key);
                } else {
                    value = super.put(key, value);
                }
                return value;
            }
        };
    }
}
