package bg.sofia.uni.fmi.mjt.restaurant;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class MJTDiningPlaceTest {

    private static Restaurant restaurant;
    private static final int DEFAULT_CHEFS_COUNT = 0;

    @BeforeAll
    public static void init() {
        restaurant = new MJTDiningPlace(DEFAULT_CHEFS_COUNT);
    }

    @Test
    public void testGetOrderCount() throws InterruptedException {

    }
}