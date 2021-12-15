package bg.sofia.uni.fmi.mjt.cache.factory;

import bg.sofia.uni.fmi.mjt.cache.Cache;
import bg.sofia.uni.fmi.mjt.cache.LeastFrequentlyUsedCache;
import bg.sofia.uni.fmi.mjt.cache.LeastRecentlyUsedCache;
import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class CacheFactoryTest {

    @Mock
    private Storage<Integer, String> storage;

    @Test
    public void testGetInstanceWhenCapacityIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> CacheFactory.getInstance(storage, -1, EvictionPolicy.LEAST_FREQUENTLY_USED),
                "capacity cannot be negative");
    }

    @Test
    public void testGetInstanceWithCapacitySuccess() {
        Cache<Integer, String> cache1 = CacheFactory.getInstance(storage, 2, EvictionPolicy.LEAST_FREQUENTLY_USED);
        Cache<Integer, String> cache2 = CacheFactory.getInstance(storage, 2, EvictionPolicy.LEAST_RECENTLY_USED);

        assertTrue(cache1 instanceof LeastFrequentlyUsedCache);
        assertTrue(cache2 instanceof LeastRecentlyUsedCache);
    }

    @Test
    public void testGetInstanceSuccess() {
        Cache<Integer, String> cache1 = CacheFactory.getInstance(storage, EvictionPolicy.LEAST_FREQUENTLY_USED);
        Cache<Integer, String> cache2 = CacheFactory.getInstance(storage, EvictionPolicy.LEAST_RECENTLY_USED);

        assertTrue(cache1 instanceof LeastFrequentlyUsedCache);
        assertTrue(cache2 instanceof LeastRecentlyUsedCache);
    }
}