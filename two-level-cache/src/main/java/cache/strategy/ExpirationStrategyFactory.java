package cache.strategy;

/**
 *
 * @author Nastya
 */
public class ExpirationStrategyFactory {

    public static ExpirationStrategy expirationStrategy(StrategyType type) {
        ExpirationStrategy strategy;
        switch (type) {
            case LRU:
                strategy = new LRUExpirationStrategy();
                break;
            case MRU:
                strategy = new MRUExpirationStrategy();
                break;
            case LFU:
                strategy = new LFUExpirationStrategy();
                break;   
            default:
               strategy = new LRUExpirationStrategy(); 
               break;
        }
        return strategy;
    }
}
