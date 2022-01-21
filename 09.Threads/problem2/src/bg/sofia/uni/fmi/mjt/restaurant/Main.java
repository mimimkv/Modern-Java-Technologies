package bg.sofia.uni.fmi.mjt.restaurant;

import bg.sofia.uni.fmi.mjt.restaurant.customer.Customer;

public class Main {

    public static void main(String[] args) {
        Restaurant restaurant = new MJTDiningPlace(5);

        Customer[] customers = new Customer[100];
        for (int i = 0; i < 100; i++) {
            customers[i] = new Customer(restaurant);
            customers[i].start();
        }

        for (int i = 0; i < 100; i++) {
            try {
                customers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        restaurant.close();
    }

    // todo ask: nqma li vsichki gotvachi da prikluchat i da ne izpylnqt nishto
}
