package bg.sofia.uni.fmi.mjt.gifts.gift;

import bg.sofia.uni.fmi.mjt.gifts.person.Person;

import java.util.Collection;
import java.util.List;

public class BirthdayGift<T extends Priceable> implements Gift<T> {

    private Person<?> sender;
    private Person<?> receiver;

    private Collection<T> items;

    public BirthdayGift(Person<?> sender, Person<?> receiver, Collection<T> items) {
        this.sender = sender;
        this.receiver = receiver;
        this.items = items;
    }

    @Override
    public Person<?> getSender() {
        return sender;
    }

    @Override
    public Person<?> getReceiver() {
        return receiver;
    }

    @Override
    public double getPrice() {
        double totalPrice = 0.0;
        for (T item : items) {
            totalPrice += item.getPrice();
        }

        return totalPrice;
    }

    @Override
    public void addItem(T t) {
        if (t == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        items.add(t);
    }

    @Override
    public boolean removeItem(T t) {
        if (t == null || !items.contains(t)) {
            return false;
        }

        items.remove(t);
        return true;
    }

    @Override
    public Collection<T> getItems() {
        return List.copyOf(items);
    }

    @Override
    public T getMostExpensiveItem() {
        T mostExpensive = null;
        double maxPrice = 0.0;
        for (T item : items) {
            if (item.getPrice() > maxPrice) {
                maxPrice = item.getPrice();
                mostExpensive = item;
            }
        }

        return mostExpensive;
    }
}
