package cache.core;

import cache.api.Cache;
import cache.store.DiskStore;
import cache.store.MemoryStore;
import java.io.Serializable;

public class TwoLevelCache<K, V extends Serializable> implements Cache<K, V> {

    MemoryStore<K, V> memoryStore;
    DiskStore<K, V> diskStore;

    @Override
    public V get(K key) {
        return findCachedEntry(key);
    }

    @Override
    public synchronized void put(K key, V value) {
        if(key == null) {
            throw new IllegalArgumentException("Cache key cannot be null!");
        }
        if(value == null) {
            throw new IllegalArgumentException("Cache value cannot be null!");
        }
        //find on 2d level
        findCachedEntry(key);
        memoryStore.put(key, value);
    }

    @Override
    public synchronized void remove(K key) {
        V cachedEntry = memoryStore.get(key);
        if (cachedEntry == null) {
            cachedEntry = diskStore.get(key);
            if (cachedEntry != null) {
                diskStore.remove(key);
            }
        } else {
            memoryStore.remove(key);
        }
    }

    private synchronized V findCachedEntry(K key) {
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

    private synchronized void moveDiskEntryInMemory(K key, V value) {
        diskStore.remove(key);
        memoryStore.put(key, value);
    }

    public synchronized void evictEntryFromDisk(K key) {
        diskStore.remove(key);
    }

    public boolean isDiskStorageOverflow() {
        return diskStore.isOverflow();
    }

    public MemoryStore<K, V> getMemoryStore() {
        return memoryStore;
    }

    public void setMemoryStore(MemoryStore<K, V> memoryStore) {
        this.memoryStore = memoryStore;
    }

    public DiskStore<K, V> getDiskStore() {
        return diskStore;
    }

    public void setDiskStore(DiskStore<K, V> diskStore) {
        this.diskStore = diskStore;
    }
}
