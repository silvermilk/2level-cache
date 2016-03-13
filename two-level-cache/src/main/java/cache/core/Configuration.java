package cache.core;

import cache.strategy.ExpirationStrategyFactory;
import cache.strategy.ExpirationStrategy;
import cache.strategy.StrategyType;

/**
 *
 * @author Nastya
 */
public class Configuration {
//    private StrategyType expirationStrategyType;
    private ExpirationStrategy expirationStrategy;
    private int maxEntriesMemoryLevel;
    private long maxBytesLocalDisk;
    private String pathToLocalDisk;
    
    public ExpirationStrategy getExpirationStrategy() {
        return expirationStrategy;
    }

    public Configuration setExpirationStrategy(StrategyType expirationStrategyType) {
        this.expirationStrategy = ExpirationStrategyFactory.expirationStrategy(expirationStrategyType);
        return this;
    }

    public int getMaxEntriesMemoryLevel() {
        return maxEntriesMemoryLevel;
    }

    public Configuration setMaxEntriesMemoryLevel(int maxEntriesMemoryLevel) {
        this.maxEntriesMemoryLevel = maxEntriesMemoryLevel;
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
