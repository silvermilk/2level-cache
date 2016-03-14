package cache.core;

import cache.api.CacheFactory;
import cache.store.DiskStore;
import cache.strategy.StrategyType;
import java.io.Serializable;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.atMost;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verify;

public class TwoLevelCacheTest {
    
    private Configuration config;
    private TwoLevelCache<String, TestEntity> instance;
    private DiskStore<String, TestEntity> mockDiskStore = Mockito.mock(DiskStore.class);

    @Before
    public void setUp() {
        config = new Configuration()
                .setExpirationStrategyType(StrategyType.LRU)
                .setMaxBytesLocalDisk(1000)
                .setMaxEntriesMemoryLevel(2)
                .setPathToLocalDisk("D:\\");
        instance = (TwoLevelCache<String, TestEntity>) CacheFactory.<String, TestEntity>createCache(config);    
        instance.setDiskStore(mockDiskStore);        
    }

    /**
     * Test of get method, of class TwoLevelCache.
     */
    @Test
    public void shouldGetCorrectCacheValueFromMemoryLevel() {
        //GIVEN
        String key = "someKey";
        TestEntity testEntity = new TestEntity("some test value");

        //WHEN
        instance.put(key, testEntity);
        TestEntity result = instance.get(key);

        //THEN
        assertEquals(testEntity, result);
        verify(mockDiskStore, atMost(1)).get(any(String.class));
    }

    /**
     * Test of put method, of class TwoLevelCache.
     */
    @Test
    public void testPut() {
//        System.out.println("put");
//        Object key = null;
//        Object value = null;
//        TwoLevelCache instance = new TwoLevelCache();
//        instance.put(key, value);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    private static class TestEntity implements Serializable {

        private String field;

        public TestEntity(String field) {
            this.field = field;
        }
    }

}
