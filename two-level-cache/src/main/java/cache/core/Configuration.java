package cache.core;

import cache.strategy.StrategyType;

public class Configuration {

    private StrategyType expirationStrategyType;
    private int maxEntriesMemoryLevel;
    private long maxBytesLocalDisk;
    private String pathToLocalDisk;

    public StrategyType getExpirationStrategyType() {
        return expirationStrategyType;
    }

    public Configuration setExpirationStrategyType(StrategyType expirationStrategyType) {
        this.expirationStrategyType = expirationStrategyType;
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
