package cache.api;

import java.io.Serializable;

public interface Cache<K, V extends Serializable> {

    public V get(K key);

    public void put(K key, V value);

    public void remove(K key);
}
