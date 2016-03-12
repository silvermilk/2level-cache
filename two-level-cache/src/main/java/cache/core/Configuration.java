package cache.core;

import cache.strategy.ExpirationPolicyFactory;
import cache.strategy.Strategy;
import cache.strategy.StrategyType;

/**
 *
 * @author Nastya
 */
public class Configuration {
//    private StrategyType expirationStrategyType;
    private Strategy expirationStrategy;
    private long maxBytesMemoryLevel;
    private long maxBytesLocalDisk;
    private String pathToLocalDisk;
    
    public Strategy getExpirationStrategy() {
        return expirationStrategy;
    }

    public Configuration setExpirationStrategy(StrategyType expirationStrategyType) {
        this.expirationStrategy = ExpirationPolicyFactory.expirationStrategy(expirationStrategyType);
        return this;
    }

    public long getMaxBytesMemoryLevel() {
        return maxBytesMemoryLevel;
    }

    public Configuration setMaxBytesMemoryLevel(long maxBytesMemoryLevel) {
        this.maxBytesMemoryLevel = maxBytesMemoryLevel;
        return this;
    }

    public long getMaxBytesLocalDisk() {
        return maxBytesLocalDisk;
    }

    public Configuration setMaxBytesLocalDisk(long maxBytesLocalDisk) {
        this.maxBytesLocalDisk = maxBytesLocalDisk;
        return this;
    }

    public String getPathToLocalDisk() {
        return pathToLocalDisk;
    }

    public Configuration setPathToLocalDisk(String pathToLocalDisk) {
        this.pathToLocalDisk = pathToLocalDisk;
        return this;
    }
}
