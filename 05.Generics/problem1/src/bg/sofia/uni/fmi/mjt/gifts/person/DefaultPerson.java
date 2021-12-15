package bg.sofia.uni.fmi.mjt.gifts.person;

import bg.sofia.uni.fmi.mjt.gifts.exception.WrongReceiverException;
import bg.sofia.uni.fmi.mjt.gifts.gift.Gift;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DefaultPerson<I> implements Person<I> {
    private final I id;
    private List<Gift<?>> gifts;

    public DefaultPerson(I id) {
        this.id = id;
        gifts = new ArrayList<>();
    }

    @Override
    public Collection<Gift<?>> getNMostExpensiveReceivedGifts(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N cannot be negative");
        }

        if (n == 0) {
            return Collections.emptyList();
        }

        int size = gifts.size();
        if (n > size) {
            n = size;
        }

        Collections.sort(gifts, giftPriceComparator);

        return List.copyOf(gifts.subList(size - n, size));
    }

    @Override
    public Collection<Gift<?>> getGiftsBy(Person<?> person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }

        List<Gift<?>> giftsByPerson = new ArrayList<>();
        for (Gift<?> gift : gifts) {
            if (gift.getSender().equals(person)) {
                giftsByPerson.add(gift);
            }
        }

        return List.copyOf(giftsByPerson);
    }

    @Override
    public I getId() {
        return id;
    }

    @Override
    public void receiveGift(Gift<?> gift) {
        if (gift == null) {
            throw new IllegalArgumentException("Args cannot be null");
        }

        if (!gift.getReceiver().getId().equals(id)) {
            throw new WrongReceiverException("Wrong recipient!");
        }

        gifts.add(gift);
    }

    private static Comparator<Gift<?>> giftPriceComparator = new Comparator<Gift<?>>() {
        @Override
        public int compare(Gift<?> o1, Gift<?> o2) {
            return (int) (o1.getPrice() - o2.getPrice());
        }
    };

}
