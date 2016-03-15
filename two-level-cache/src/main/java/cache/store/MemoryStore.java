package cache.store;

import cache.strategy.ExpirationStrategy;
import java.util.LinkedHashMap;

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
        return memoryCache.get(key);
    }

    public void put(K key, V value) {
        memoryCache.put(key, value);
    }

    public void remove(K key) {
        memoryCache.remove(key);
    }

    public ExpirationStrategy getExpirationStrategy() {
        return expirationStrategy;
    }

    public int getMaxEntriesMemoryLevel() {
        return maxEntriesMemoryLevel;
    }
}
