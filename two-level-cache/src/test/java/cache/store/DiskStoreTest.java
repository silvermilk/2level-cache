package cache.store;

import cache.api.CacheFactory;
import cache.core.Configuration;
import cache.core.DiskElementInfo;
import cache.core.TwoLevelCache;
import cache.strategy.StrategyType;
import cache.utils.BigTestEntity;
import cache.utils.TestEntity;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DiskStoreTest {

    private DiskStore<String, TestEntity> instance;
    private Configuration config;
    private TwoLevelCache<String, TestEntity> cacheInstance;
    private final String key = "someKey";
    private final TestEntity testEntity = new TestEntity("some test value");

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() {
        config = new Configuration()
                .setExpirationStrategyType(StrategyType.LRU)
                .setMaxBytesLocalDisk(500)
                .setMaxEntriesMemoryLevel(2)
                .setPathToLocalDisk(folder.getRoot().getAbsolutePath());
        cacheInstance = (TwoLevelCache<String, TestEntity>) CacheFactory.<String, TestEntity>createCache(config);
        instance = cacheInstance.<String, TestEntity>getDiskStore();
    }

    @Test
    public void testThereIsNoOverflow() {
        //GIVEN

        instance.put(key, testEntity);
        DiskElementInfo diskElementInfo = instance.getElementsMap().get(key);
        int diskElementPayloadSize = diskElementInfo.getPayloadSize();
        long maxTestEntriesOnDisk = instance.getMaxBytesLocalDisk() / diskElementPayloadSize;

        //WHEN
        for (int i = 0; i < (maxTestEntriesOnDisk + 10); i++) {
            instance.put(key + i, testEntity);
        }

        //THEN
        assertFalse(instance.isOverflow());
    }

    @Test
    public void testGet() {

        //WHEN
        instance.put(key, testEntity);
        TestEntity result = instance.get(key);

        //THEN
        assertEquals(testEntity.getField(), result.getField());
    }

    @Test
    public void testPut() {

        //WHEN
        instance.put(key, testEntity);
        TestEntity result = instance.get(key);

        //THEN
        assertEquals(testEntity.getField(), result.getField());
    }

    @Test
    public void shouldPutNewEntryOnFreeSpace() {
        //GIVEN
        String firstKey = "firstKey";
        BigTestEntity firstBigEntity = new BigTestEntity("test value", "long test field",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit");

        String secondKey = "secondKey";
        TestEntity secondEntity = new TestEntity("second test value");

        String thirdKey = "thirdKey";
        TestEntity thirdEntity = new TestEntity("third test value");

        //WHEN
        instance.put(firstKey, firstBigEntity);
        long firstElementPointer = instance.getElementsMap().get(firstKey).getPointer();

        instance.put(secondKey, secondEntity);
        instance.remove(firstKey);

        instance.put(thirdKey, thirdEntity);
        long thirdElementPointer = instance.getElementsMap().get(thirdKey).getPointer();

        //THEN
        assertEquals(firstElementPointer, thirdElementPointer);
    }

    @Test
    public void shouldAddBigEntryToTheEndOfFile() {
        //GIVEN
        String firstKey = "firstKey";
        String secondKey = "secondKey";
        String thirdKey = "thirdKey";

        BigTestEntity bigEntity = new BigTestEntity("test value", "long test field",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit");
        TestEntity firstEntity = new TestEntity("second test value");
        TestEntity secondEntity = new TestEntity("third test value");

        //WHEN
        instance.put(firstKey, firstEntity);
        long firstElementPointer = instance.getElementsMap().get(firstKey).getPointer();

        instance.put(secondKey, secondEntity);
        instance.remove(firstKey);

        instance.put(thirdKey, bigEntity);
        long thirdElementPointer = instance.getElementsMap().get(thirdKey).getPointer();

        //THEN
        assertNotEquals(firstElementPointer, thirdElementPointer);
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
