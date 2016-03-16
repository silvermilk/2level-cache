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

public class FIFOTwoLevelCacheTest {

    private Configuration config;
    private TwoLevelCache<String, TestEntity> instance;
    private DiskStore<String, TestEntity> mockedDiskStore = Mockito.mock(DiskStore.class);

    private String firstKey = "firstKey";
    private TestEntity firstEntity = new TestEntity("first test value");
    private String secondKey = "secondKey";
    private TestEntity secondEntity = new TestEntity("second test value");
    private String thirdKey = "thirdKey";
    private TestEntity thirdEntity = new TestEntity("third test value");

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() {
        config = new Configuration()
                .setExpirationStrategyType(StrategyType.FIFO)
                .setMaxBytesLocalDisk(1000)
                .setMaxEntriesMemoryLevel(2)
                .setPathToLocalDisk(folder.getRoot().getAbsolutePath());
        instance = (TwoLevelCache<String, TestEntity>) CacheFactory.<String, TestEntity>createCache(config);
    }

    @Test
    public void shouldGetCorrectCacheValueFromMemoryLevel() {
        //GIVEN
        instance.setDiskStore(mockedDiskStore);

        //WHEN
        instance.put(firstKey, firstEntity);
        TestEntity result = instance.get(firstKey);

        //THEN
        assertEquals(firstEntity, result);
        verify(mockedDiskStore, atMost(1)).get(any(String.class));
    }

    @Test
    public void shouldGetCorrectCacheValueFromDiskLevel() {
        //GIVEN

        //WHEN
        instance.put(firstKey, firstEntity);
        instance.put(secondKey, secondEntity);
        //make second item the eldest.This should not affect the order
        instance.get(firstKey);
        instance.put(thirdKey, thirdEntity);
        TestEntity result = instance.get(firstKey);

        //THEN
        assertEquals(firstEntity.getField(), result.getField());
        assertNotEquals(firstEntity, result);
    }

    @Test
    public void shouldPutCorrectCacheEntryOnMemoryLevel() {
        //GIVEN
        instance.setDiskStore(mockedDiskStore);

        //WHEN
        instance.put(firstKey, firstEntity);
        TestEntity result = instance.get(firstKey);

        //THEN
        assertEquals(firstEntity, result);
        verify(mockedDiskStore, Mockito.never()).put(firstKey, firstEntity);
    }

    @Test
    public void shouldPutCorrectCacheEntryOnDiskLevel() {
        //GIVEN
        instance.setDiskStore(mockedDiskStore);

        //WHEN
        instance.put(firstKey, firstEntity);
        instance.put(secondKey, secondEntity);
        //make second item the eldest.This should not affect the order
        instance.get(firstKey);
        instance.put(thirdKey, thirdEntity);

        //THEN
        verify(mockedDiskStore, Mockito.atLeastOnce()).put(firstKey, firstEntity);
    }

    @Test
    public void shouldRemoveEntryFromDiskLevel() {
        //GIVEN
        //WHEN
        instance.put(firstKey, firstEntity);
        instance.put(secondKey, secondEntity);
        instance.put(thirdKey, thirdEntity);
        instance.remove(firstKey);
        TestEntity result = instance.get(firstKey);

        //THEN
        assertNull(result);
    }

    @Test
    public void shouldRemoveEntryFromMemoryLevel() {
        //GIVEN
        instance.setDiskStore(mockedDiskStore);

        //WHEN
        instance.put(firstKey, firstEntity);
        instance.remove(firstKey);
        TestEntity result = instance.get(firstKey);

        //THEN
        assertNull(result);
        verify(mockedDiskStore, Mockito.never()).remove(firstKey);
    }
}
