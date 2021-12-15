package bg.sofia.uni.fmi.mjt.shopping;

import bg.sofia.uni.fmi.mjt.shopping.item.Apple;
import bg.sofia.uni.fmi.mjt.shopping.item.Chocolate;
import bg.sofia.uni.fmi.mjt.shopping.item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListShoppingCartTest {

    private static final String APPLE_ID = "apple-1";
    private static final String CHOCOLATE_ID = "chocolate-1";

    private static final Apple APPLE = new Apple(APPLE_ID);
    private static final Chocolate CHOCOLATE = new Chocolate(CHOCOLATE_ID);

    @Mock
    private ProductCatalog catalog;

    private ShoppingCart cart;

    @BeforeEach
    public void setup() {
        cart = new ListShoppingCart(catalog);
    }

    @Test
    public void testGetUniqueItemsWithDuplicatesSuccess() {
        cart.addItem(APPLE);
        cart.addItem(CHOCOLATE);
        cart.addItem(APPLE);

        int expected = 2;
        int actual = cart.getUniqueItems().size();
        assertEquals(actual, expected,
                "getUnique does not return correct number of items");
    }

    @Test
    public void testRemoveItemWithExistingItemSuccess() {
        cart.addItem(APPLE);
        cart.addItem(CHOCOLATE);
        cart.removeItem(APPLE);

        int expected = 1;
        int actual = cart.getUniqueItems().size();
        assertEquals(expected, actual, "removeItem does not remove correctly");
    }

    @Test
    public void testRemoveItemWithNullItemFail() {
        assertThrows(IllegalArgumentException.class, () -> cart.removeItem(null),
                "IllegalArgumentException expected when removing a null item");
    }

    @Test
    public void testAddItemWithNullItemFail() {
        assertThrows(IllegalArgumentException.class, () -> cart.addItem(null),
                "IllegalArgumentException expected when adding a null item");
    }

    @Test
    public void testGetTotalSuccess() {
        cart.addItem(APPLE);
        cart.addItem(CHOCOLATE);
        cart.addItem(CHOCOLATE);

        when(catalog.getProductInfo(APPLE_ID)).thenReturn(new ProductInfo("apple", "green apple", 1));
        when(catalog.getProductInfo(CHOCOLATE_ID)).thenReturn(new ProductInfo("chocolate", "black chocolate", 2));

        double expected = 5.0;
        double actual = cart.getTotal();
        assertEquals(expected, actual, 0, "getTotal does not calculate total price correctly");

        verify(catalog, times(1)).getProductInfo(APPLE_ID);
        verify(catalog, times(2)).getProductInfo(CHOCOLATE_ID);
    }

    @Test
    public void testGetSortedItems() {
        cart.addItem(CHOCOLATE);
        cart.addItem(CHOCOLATE);
        cart.addItem(APPLE);
        cart.addItem(APPLE);
        cart.addItem(APPLE);

        Collection<Item> expectedItems = List.of(APPLE, CHOCOLATE);
        Collection<Item> actualItems = new ArrayList<>(cart.getSortedItems());
        assertEquals(expectedItems, actualItems,
                "getSortedItems does not sort correctly");
    }
}