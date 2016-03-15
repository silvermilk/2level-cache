package cache.store;

import cache.api.CacheFactory;
import cache.core.Configuration;
import cache.core.TwoLevelCache;
import cache.core.TwoLevelCacheTest;
import cache.strategy.StrategyType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;


public class MemoryStoreTest {
    
    private Configuration config;
    private MemoryStore <String, TwoLevelCacheTest.TestEntity> instance;
    private TwoLevelCache<String, TwoLevelCacheTest.TestEntity> cacheInstance;
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Before
    public void setUp() {
        config = new Configuration()
                .setExpirationStrategyType(StrategyType.LRU)
                .setMaxBytesLocalDisk(1000)
                .setMaxEntriesMemoryLevel(2)
                .setPathToLocalDisk(folder.getRoot().getAbsolutePath());
        cacheInstance = (TwoLevelCache<String, TwoLevelCacheTest.TestEntity>) CacheFactory.<String, TwoLevelCacheTest.TestEntity>createCache(config);
        instance = cacheInstance.<String, TwoLevelCacheTest.TestEntity>getMemoryStore();
    }
    
    /**
     * Test of get method, of class MemoryStore.
     */
    @Test
    public void testGet() {
        //GIVEN
        String key = "someKey";
        TwoLevelCacheTest.TestEntity testEntity = new TwoLevelCacheTest.TestEntity("some test value");

        //WHEN
        instance.put(key, testEntity);
        TwoLevelCacheTest.TestEntity result = instance.get(key);

        //THEN
        assertEquals(testEntity, result);
    }

    /**
     * Test of put method, of class MemoryStore.
     */
    @Test
    public void testPut() {
        //GIVEN
        String key = "someKey";
        TwoLevelCacheTest.TestEntity testEntity = new TwoLevelCacheTest.TestEntity("some test value");

        //WHEN
        instance.put(key, testEntity);
        TwoLevelCacheTest.TestEntity result = instance.get(key);

        //THEN
        assertEquals(testEntity, result);
    }

    /**
     * Test of remove method, of class MemoryStore.
     */
    @Test
    public void testRemove() {
        //GIVEN
        String key = "someKey";
        TwoLevelCacheTest.TestEntity testEntity = new TwoLevelCacheTest.TestEntity("some test value");

        //WHEN
        instance.put(key, testEntity);
        instance.remove(key);
        TwoLevelCacheTest.TestEntity result = instance.get(key);

        //THEN
        assertNull(result);
    }
}
