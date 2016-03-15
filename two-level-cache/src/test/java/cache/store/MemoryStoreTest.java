package cache.store;

import cache.api.CacheFactory;
import cache.core.Configuration;
import cache.core.TwoLevelCache;
import cache.strategy.StrategyType;
import cache.utils.TestEntity;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;


public class MemoryStoreTest {
    
    private Configuration config;
    private MemoryStore <String, TestEntity> instance;
    private TwoLevelCache<String, TestEntity> cacheInstance;
    private final String key = "someKey";
    private final TestEntity testEntity = new TestEntity("some test value");
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    @Before
    public void setUp() {
        config = new Configuration()
                .setExpirationStrategyType(StrategyType.LRU)
                .setMaxBytesLocalDisk(1000)
                .setMaxEntriesMemoryLevel(2)
                .setPathToLocalDisk(folder.getRoot().getAbsolutePath());
        cacheInstance = (TwoLevelCache<String, TestEntity>) CacheFactory.<String, TestEntity>createCache(config);
        instance = cacheInstance.<String, TestEntity>getMemoryStore();
    }
    
    @Test
    public void testGet() {
        //WHEN
        instance.put(key, testEntity);
        TestEntity result = instance.get(key);

        //THEN
        assertEquals(testEntity, result);
    }

    @Test
    public void testPut() {
        //WHEN
        instance.put(key, testEntity);
        TestEntity result = instance.get(key);

        //THEN
        assertEquals(testEntity, result);
    }

    @Test
    public void testRemove() {

        //WHEN
        instance.put(key, testEntity);
        instance.remove(key);
        TestEntity result = instance.get(key);

        //THEN
        assertNull(result);
    }
}
