package cache.core;

import cache.api.CacheFactory;
import cache.store.DiskStore;
import cache.strategy.StrategyType;
import cache.utils.TestEntity;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;

public class MRUTwoLevelCacheTest {

    private Configuration config;
    private TwoLevelCache<String, TestEntity> instance;
    private DiskStore<String, TestEntity> mockedDiskStore = Mockito.mock(DiskStore.class);

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() {
        config = new Configuration()
                .setExpirationStrategyType(StrategyType.MRU)
                .setMaxBytesLocalDisk(1000)
                .setMaxEntriesMemoryLevel(2)
                .setPathToLocalDisk(folder.getRoot().getAbsolutePath());
        instance = (TwoLevelCache<String, TestEntity>) CacheFactory.<String, TestEntity>createCache(config);
    }

    @Test
    public void shouldGetCorrectCacheValueFromMemoryLevel() {
        //GIVEN
        String key = "someKey";
        TestEntity testEntity = new TestEntity("some test value");
        instance.setDiskStore(mockedDiskStore);

        //WHEN
        instance.put(key, testEntity);
        TestEntity result = instance.get(key);

        //THEN
        assertEquals(testEntity, result);
        verify(mockedDiskStore, atMost(1)).get(any(String.class));
    }

    @Test
    public void shouldGetCorrectCacheValueFromDiskLevel() {
        //GIVEN
        String firstKey = "firstKey";
        TestEntity firstEntity = new TestEntity("first test value");
        String secondKey = "secondKey";
        TestEntity secondEntity = new TestEntity("second test value");
        String thirdKey = "thirdKey";
        TestEntity thirdEntity = new TestEntity("third test value");

        //WHEN
        instance.put(firstKey, firstEntity);
        instance.put(secondKey, secondEntity);
        instance.put(thirdKey, thirdEntity);
        TestEntity result = instance.get(thirdKey);

        //THEN
        assertEquals(thirdEntity.getField(), result.getField());
        assertNotEquals(thirdEntity, result);
    }

    @Test
    public void shouldPutCorrectCacheEntryOnMemoryLevel() {
        //GIVEN
        String key = "someKey";
        TestEntity testEntity = new TestEntity("some test value");
        instance.setDiskStore(mockedDiskStore);

        //WHEN
        instance.put(key, testEntity);
        TestEntity result = instance.get(key);

        //THEN
        assertEquals(testEntity, result);
        verify(mockedDiskStore, Mockito.never()).put(key, testEntity);
    }

    @Test
    public void shouldPutCorrectCacheEntryOnDiskLevel() {
        //GIVEN
        String firstKey = "firstKey";
        TestEntity firstEntity = new TestEntity("first test value");
        String secondKey = "secondKey";
        TestEntity secondEntity = new TestEntity("second test value");
        String thirdKey = "thirdKey";
        TestEntity thirdEntity = new TestEntity("third test value");
        instance.setDiskStore(mockedDiskStore);

        //WHEN
        instance.put(firstKey, firstEntity);
        instance.put(secondKey, secondEntity);
        instance.get(firstKey);
        instance.put(thirdKey, thirdEntity);


        //THEN
        verify(mockedDiskStore, Mockito.atLeastOnce()).put(thirdKey, thirdEntity);
    }

    @Test
    public void shouldRemoveEntryFromDiskLevel() {
        //GIVEN
        String firstKey = "firstKey";
        TestEntity firstEntity = new TestEntity("first test value");
        String secondKey = "secondKey";
        TestEntity secondEntity = new TestEntity("second test value");
        String thirdKey = "thirdKey";
        TestEntity thirdEntity = new TestEntity("third test value");

        //WHEN
        instance.put(firstKey, firstEntity);
        instance.put(secondKey, secondEntity);
        instance.put(thirdKey, thirdEntity);
        instance.remove(thirdKey);
        TestEntity result = instance.get(thirdKey);

        //THEN
        assertNull(result);
    }

    @Test
    public void shouldRemoveEntryFromMemoryLevel() {
        //GIVEN
        String key = "someKey";
        TestEntity testEntity = new TestEntity("some test value");
        instance.setDiskStore(mockedDiskStore);

        //WHEN
        instance.put(key, testEntity);
        instance.remove(key);
        TestEntity result = instance.get(key);

        //THEN
        assertNull(result);
        verify(mockedDiskStore, Mockito.never()).remove(key);
    }
}
