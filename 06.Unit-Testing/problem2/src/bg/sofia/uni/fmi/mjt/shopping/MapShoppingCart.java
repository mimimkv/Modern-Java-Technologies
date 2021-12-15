package bg.sofia.uni.fmi.mjt.shopping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bg.sofia.uni.fmi.mjt.shopping.item.Item;

public class MapShoppingCart extends AbstractShoppingCart {

    private Map<Item, Integer> items;

    public MapShoppingCart(ProductCatalog catalog) {
        super(catalog);
        items = new HashMap<>();
    }

    public Collection<Item> getUniqueItems() {
        return items.keySet();
    }

    @Override
    public void addItem(Item item) {
        validateItemNotNull(item);

        if (!items.containsKey(item)) {
            items.put(item, 0);
        }

        items.put(item, items.get(item) + 1);
    }

    @Override
    public void removeItem(Item item) {
        validateItemNotNull(item);

        if (!items.containsKey(item)) {
            throw new ItemNotFoundException(ITEM_NOT_FOUND_MSG);
        }

        Integer itemCounter = items.get(item);
        if (itemCounter == 1) {
            items.remove(item);
        } else {
            items.put(item, itemCounter - 1);
        }
    }

    @Override
    public double getTotal() {
        double total = 0.0;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            ProductInfo info = catalog.getProductInfo(entry.getKey().getId());
            total += info.price() * entry.getValue();
        }
        return total;
    }

    @Override
    public Collection<Item> getSortedItems() {
        List<Item> sortedItems = new ArrayList<>(items.keySet());
        Collections.sort(sortedItems, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {

                int count1 = items.get(item1);
                int count2 = items.get(item2);

                return count2 - count1;
            }
        });
        return sortedItems;
    }

}