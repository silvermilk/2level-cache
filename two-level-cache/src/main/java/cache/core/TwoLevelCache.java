package cache.core;

import cache.api.Cache;
import cache.store.DiskStore;
import cache.store.MemoryStore;

/**
 *
 * @author Nastya
 */
public class TwoLevelCache <K,V> implements Cache <K,V>{
    
    MemoryStore <K,V> memoryLevel;
    DiskStore <K,V> diskLevel;
    
    public TwoLevelCache (MemoryStore memoryLevel, DiskStore diskLevel) {
        this.memoryLevel = memoryLevel;
        this.diskLevel = diskLevel;
    }

    @Override
    public V get(K key) {
        V cachedEntry = memoryLevel.get(key);
        
        if(cachedEntry == null) {
            //search in 2d level
            cachedEntry = diskLevel.get(key);
        } 
        return cachedEntry;
    }

    @Override
    public void put(K key, V value) {
        memoryLevel.put(key, value);
    }

    @Override
    public void replace(K key, V value) {
        memoryLevel.replace(key, value);
    }

    @Override
    public void remove(K key) {
        memoryLevel.remove(key);
    }
    
}
