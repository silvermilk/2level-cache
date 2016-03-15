package cache.store;

import cache.api.CacheFactory;
import cache.core.Configuration;
import cache.core.DiskElementInfo;
import cache.core.TwoLevelCache;
import cache.core.TwoLevelCacheTest;
import cache.strategy.StrategyType;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DiskStoreTest {

    private DiskStore<String, TwoLevelCacheTest.TestEntity> instance;
    private Configuration config;
    private TwoLevelCache<String, TwoLevelCacheTest.TestEntity> cacheInstance;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() {
        config = new Configuration()
                .setExpirationStrategyType(StrategyType.LRU)
                .setMaxBytesLocalDisk(500)
                .setMaxEntriesMemoryLevel(2)
                .setPathToLocalDisk(folder.getRoot().getAbsolutePath());
        cacheInstance = (TwoLevelCache<String, TwoLevelCacheTest.TestEntity>) CacheFactory.<String, TwoLevelCacheTest.TestEntity>createCache(config);
        instance = cacheInstance.<String, TwoLevelCacheTest.TestEntity>getDiskStore();
    }

    /**
     * Test of isOverflow method, of class DiskStore.
     */
    @Test
    public void testIsOverflow() {
        //GIVEN
        String key = "someKey";
        TwoLevelCacheTest.TestEntity testEntity = new TwoLevelCacheTest.TestEntity("some test value");
        instance.put(key, testEntity);
        DiskElementInfo diskElementInfo = instance.getElementsMap().get(key);
        int diskElementPayloadSize = diskElementInfo.getPayloadSize();
        long maxTestEntriesOnDisk = instance.getMaxBytesLocalDisk() / diskElementPayloadSize;

        //WHEN
        for (int i = 0; i < (maxTestEntriesOnDisk + 1); i++) {
            instance.put(key + i, testEntity);
        }

        //THEN
//        assertTrue(instance.isOverflow());
    }

    /**
     * Test of get method, of class DiskStore.
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
        assertEquals(testEntity.getField(), result.getField());
    }

    /**
     * Test of put method, of class DiskStore.
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
        assertEquals(testEntity.getField(), result.getField());
    }

    /**
     * Test of remove method, of class DiskStore.
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
