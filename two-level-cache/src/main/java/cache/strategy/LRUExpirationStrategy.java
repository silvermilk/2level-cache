/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache.strategy;

import cache.store.DiskStore;
import cache.store.MemoryStore;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Nastya
 */
public class LRUExpirationStrategy implements ExpirationStrategy {

    static final boolean ACCESS_ORDER = true;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    DiskStore diskLevel;

//    public LRUExpirationStrategy (MemoryStore memoryLevel, DiskStore diskLevel) {
//        this.memoryLevel = memoryLevel;
//        this.diskLevel = diskLevel;
//    }
    
    @Override
    public LinkedHashMap createMemoryLevelCache(final int maxEntries) {
        return new LinkedHashMap(maxEntries, DEFAULT_LOAD_FACTOR, ACCESS_ORDER) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                if (size() > maxEntries) {
                    //move eldest entry to the second level of cache
//                    diskLevel.put(eldest.getKey(), eldest.getValue());

                    return true; 

                }
                return false; 

            }
        };
    }

    @Override

    public LinkedHashMap createDiskLevelCache(DiskStore diskLevel) {
        this.diskLevel = diskLevel;
        return new LinkedHashMap(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, ACCESS_ORDER) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
//                if (size() > diskLevel.isOverflow()) {
//                    //move eldest entry to the second level of cache
//                    diskLevel.remove(eldest.getKey());
//
//                    return true; 
//
//                }
                return false; 
            }
        };
    }

}
