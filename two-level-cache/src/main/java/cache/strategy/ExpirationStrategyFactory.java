package cache.strategy;

import cache.core.TwoLevelCache;
import java.io.Serializable;

/**
 *
 * @author Nastya
 */
public class ExpirationStrategyFactory {

    public static <K, V extends Serializable> ExpirationStrategy expirationStrategy(StrategyType type, TwoLevelCache <K, V> cache) {
        ExpirationStrategy strategy;
        switch (type) {
            case LRU:
                strategy = new LRUExpirationStrategy(cache);
                break;
            case FIFO:
                strategy = new FIFOExpirationStrategy(cache);
                break;                
            case MRU:
                strategy = new MRUExpirationStrategy(cache);
                break;
            default:
               strategy = new LRUExpirationStrategy(cache); 
               break;
        }
        return strategy;
    }
}
