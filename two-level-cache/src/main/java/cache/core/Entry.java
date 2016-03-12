package cache.core;

import java.io.Serializable;

/**
 *
 * @author Nastya
 */
public class Entry <K,V> implements Serializable {
    private transient long duration;
    private transient long counter;
    
    
}
