package bg.sofia.uni.fmi.mjt.cache;


import bg.sofia.uni.fmi.mjt.cache.exception.ItemNotFound;
import bg.sofia.uni.fmi.mjt.cache.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeastRecentlyUsedCacheTest {

    @Mock
    private Storage<Integer, String> storage;

    private CacheBase<Integer, String> cache;

    @BeforeEach
    void setup() {
        cache = new LeastRecentlyUsedCache<>(storage, 4);
    }

    @Test
    public void testGetWhenItemIsNotFoundException() {
        assertThrows(ItemNotFound.class, () -> cache.get(2),
                "ItemNotFoundException expected when key is not found");
    }

    @Test
    public void testGetWithNullKeyException() {
        assertThrows(IllegalArgumentException.class, () -> cache.get(null),
                "IllegalArgumentException expected when key is not found");
    }

    @Test
    public void testGetWithValidKeySuccess() {
        cache.put(1, "one");
        try {
            assertEquals("one", cache.get(1), "get method does not work");
        } catch (ItemNotFound e) {
            fail("get method should not throw an exception when key is present");
        }
    }

    @Test
    public void testSizeNoElementsSuccess() {
        assertEquals(0, cache.size());
    }

    @Test
    public void testSizeElementWithRepetitionSuccess() {
        cache.put(1, "one");
        cache.put(1, "One");
        cache.put(1, "one");

        int expected = 1;
        assertEquals(expected, cache.size(), "size does not work when there are repetitions of keys");
    }

    @Test
    public void testSizeSuccess() {
        cache.put(1, "one");
        cache.put(2, "two");

        int expected = 2;
        assertEquals(expected, cache.size(), "size does not work");
    }

    @Test
    public void testClearNoElementsInCacheSuccess() {
        cache.clear();
        assertEquals(0, cache.size(), "clear does not work when there are no elements");
    }

    @Test
    public void testClearSuccess() {
        cache.put(1, "one");
        cache.put(2, "two");
        cache.clear();
        assertEquals(0, cache.size(), "clear does not work");
    }

    @Test
    public void testClearResetHitRate() throws ItemNotFound {
        cache.put(1, "one");
        cache.get(1);
        cache.clear();
        assertEquals(0.0, cache.getHitRate(), 0.0001,
                "hit rate is not reset when the cache is cleared");
    }

    @Test
    public void testGetHitRateAllHitsSuccessful() throws ItemNotFound {
        cache.put(1, "one");
        cache.get(1);
        cache.get(1);

        double expected = 1.0;
        assertEquals(expected, cache.getHitRate(), 0.0001,
                "hit rate does not work");
    }

    @Test
    public void testGetHitRateZero() {
        assertEquals(0.0, cache.getHitRate(), 0.0001,
                "getHitRate does not work when there was no hit");
    }

    @Test
    public void testGetHitRateSuccess() throws ItemNotFound {
        when(storage.retrieve(2)).thenReturn("two");
        cache.put(1, "one");
        cache.get(1);
        cache.get(2);

        double expected = 0.5;
        assertEquals(expected, cache.getHitRate(), 0.0001,
                "getHitRate does not return correct value");
    }

    @Test
    public void testValuesNoValuesInCache() {
        assertEquals(Collections.emptyList(), cache.values(),
                "values should return an empty list when there is nothing in the cache");
    }

    @Test
    public void testGetWhenEvictionIsRequired() throws ItemNotFound {
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        cache.put(4, "four");

        String expected = "five";
        when(storage.retrieve(5)).thenReturn(expected);

        String actual = cache.get(5);
        assertEquals(actual, expected);
        assertEquals(4, cache.size());
    }

    @Test
    public void testValuesSuccess() {
        cache.put(1, "one");
        cache.put(2, "two");

        List<String> expected = List.of("one", "two");
        Collection<String> actual = cache.values();

        assertIterableEquals(expected, actual);
    }

    /*@Test void testSomething() throws ItemNotFound {
        when(storage.retrieve(1)).thenReturn("one");
        when(storage.retrieve(2)).thenReturn("two");
        when(storage.retrieve(3)).thenReturn("three");
        when(storage.retrieve(4)).thenReturn("four");
        when(storage.retrieve(5)).thenReturn("five");

        assertEquals("two", cache.get(2));
        assertEquals("one", cache.get(1));
        assertEquals("three", cache.get(3));
        assertEquals("four", cache.get(4));
        assertEquals("five", cache.get(5));
        cache.get(1);
        cache.put(2, "TWO");

        assertTrue(cache.containsKey(5));
        assertTrue(cache.containsKey(1));
        assertTrue(cache.containsKey(2));
        assertTrue(cache.containsKey(4));
        assertFalse(cache.containsKey(3));

        List<String> list = List.of("four", "five", "one", "TWO");
        assertIterableEquals(list, cache.values());
    }*/
}

