package cache.api;

/**
 *
 * @author Nastya
 */
public interface Cache <K,V> {
    
    public V get(K key);
    
    public void put(K key, V value);
    
    public void replace(K key, V value);
    
    public void remove(K key);
}
