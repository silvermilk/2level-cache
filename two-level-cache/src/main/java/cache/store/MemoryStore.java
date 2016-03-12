package cache.store;

import cache.strategy.Strategy;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Nastya
 */
public class MemoryStore <K,V> {

    private final Strategy expirationStrategy;
    private final long maxBytesMemoryLevel;
    
    private ConcurrentHashMap <K,V> memoryCache;

    public MemoryStore(Strategy expirationStrategy, long maxBytesMemoryLevel) {
        this.expirationStrategy = expirationStrategy;
        this.maxBytesMemoryLevel = maxBytesMemoryLevel;
        memoryCache = new ConcurrentHashMap();
    }

    public Strategy getExpirationStrategy() {
        return expirationStrategy;
    }

    public long getMaxBytesMemoryLevel() {
        return maxBytesMemoryLevel;
    }
    
    public V get (K key) {
        return memoryCache.get(key);
    }
    
    public void put(K key, V value) {
        memoryCache.put(key, value);
    }
    
    public void replace(K key, V newValue) {
        memoryCache.replace(key, newValue);
    }
    
    public void remove(K key) {
        memoryCache.remove(key);
    }

}
