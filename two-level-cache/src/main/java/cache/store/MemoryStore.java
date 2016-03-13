package cache.store;

import cache.strategy.ExpirationStrategy;
import java.util.LinkedHashMap;

/**
 *
 * @author Nastya
 */
public class MemoryStore<K, V> {

    private final ExpirationStrategy expirationStrategy;
    private final int maxEntriesMemoryLevel;

    private final LinkedHashMap<K, V> memoryCache;

    public MemoryStore(ExpirationStrategy expirationStrategy, int maxEntriesMemoryLevel) {
        this.expirationStrategy = expirationStrategy;
        this.maxEntriesMemoryLevel = maxEntriesMemoryLevel;
        memoryCache = expirationStrategy.createMemoryLevelCache(maxEntriesMemoryLevel);
    }


    public V get(K key) {
        synchronized (memoryCache) {
            return memoryCache.get(key);
        }
    }

    public void put(K key, V value) {
        synchronized (memoryCache) {
            memoryCache.put(key, value);
        }
    }

    public void remove(K key) {
        synchronized (memoryCache) {
            memoryCache.remove(key);
        }
    }

    public ExpirationStrategy getExpirationStrategy() {
        return expirationStrategy;
    }

    public int getMaxEntriesMemoryLevel() {
        return maxEntriesMemoryLevel;
    }

//    class CacheKeyWrapper<K, V> implements Cache.KeyWrapper {
//
//        private K key;
//        
//        private int counter;
//
//        CacheKeyWrapper(K key) {
//            this.key = key;
//        }
//
//        @Override
//        public K getKey() {
//            return key;
//        }
//
//        public int getCounter() {
//            return counter;
//        }
//
//        public void setCounter(int counter) {
//            this.counter = counter;
//        }
//        
//    }
}
