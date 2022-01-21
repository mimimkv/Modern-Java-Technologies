package bg.sofia.uni.fmi.mjt.restaurant.customer;

import bg.sofia.uni.fmi.mjt.restaurant.Meal;
import bg.sofia.uni.fmi.mjt.restaurant.Order;
import bg.sofia.uni.fmi.mjt.restaurant.Restaurant;

import java.util.Random;

public abstract class AbstractCustomer extends Thread {

    private static final Random RANDOM = new Random();

    private final Restaurant restaurant;

    public AbstractCustomer(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {

        try {
            Thread.sleep(RANDOM.nextInt(5000) + 1000);
        } catch (InterruptedException e) {
            System.err.println("Unexpected exception was thrown: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Customer ordered a meal");
        restaurant.submitOrder(new Order(Meal.chooseFromMenu(), this));
    }

    public abstract boolean hasVipCard();

}
