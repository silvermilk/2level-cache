package cache.core;

import cache.api.Cache;
import cache.store.DiskStore;
import cache.store.MemoryStore;

/**
 *
 * @author Nastya
 */
public class TwoLevelCache<K, V> implements Cache<K, V> {

    MemoryStore<K, V> memoryStore;
    DiskStore<K, V> diskStore;

    public TwoLevelCache(MemoryStore memoryStore, DiskStore diskStore) {
        this.memoryStore = memoryStore;
        this.diskStore = diskStore;
    }

    @Override
    public V get(K key) {
        return findCachedEntry(key);
    }

    @Override
    public void put(K key, V value) {
        //find on 2d level
        findCachedEntry(key);
        memoryStore.put(key, value);
    }

    @Override
    public void remove(K key) {
        V cachedEntry = memoryStore.get(key);
        if (cachedEntry == null) {
            cachedEntry = diskStore.get(key);
            if(cachedEntry != null) {
                //diskStore.remove(K key);
            }
        } else {
            memoryStore.remove(key);
        }
    }

    private V findCachedEntry(K key) {
        V cachedEntry = memoryStore.get(key);

        if (cachedEntry == null) {
            //search in 2d level
            cachedEntry = diskStore.get(key);
            if (cachedEntry != null) {
                moveDiskEntryInMemory(key, cachedEntry);
            }
        }
        return cachedEntry;
    }

    private  void moveDiskEntryInMemory(K key, V value) {
        //diskStore.remove(K key);
        memoryStore.put(key, value);
    }
//    class CacheKeyWrapper <K> implements Cache.KeyWrapper {
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
