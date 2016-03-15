package cache.strategy;

import cache.core.DiskElementInfo;
import cache.core.TwoLevelCache;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author anchu
 */
public class MRUExpirationStrategy<K, V extends Serializable> implements ExpirationStrategy {

    static final boolean ACCESS_ORDER = true;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private TwoLevelCache<K, V> cache;

    public MRUExpirationStrategy(TwoLevelCache<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public LinkedHashMap<K, V> createMemoryLevelCache(final int maxEntries) {
        return new LinkedHashMap<K, V>(maxEntries, DEFAULT_LOAD_FACTOR, ACCESS_ORDER) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                if (size() > maxEntries) {
                    synchronized (this) {
                        K firstEntryKey = keySet().iterator().next();
                        V firstEntryValue = get(firstEntryKey);
                        //move entry to the second level of cache
                        cache.moveMemoryEntryToDisk(firstEntryKey, firstEntryValue);
                        remove(firstEntryKey);
                    }
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
                K firstEntryKey = keySet().iterator().next();
                if (cache.isDiskStorageOverflow()) {
                    cache.evictEntryFromDisk(firstEntryKey);
                    synchronized (this) {
                        remove(firstEntryKey);
                    }
                }
                return false;
            }
        };
    }
}
