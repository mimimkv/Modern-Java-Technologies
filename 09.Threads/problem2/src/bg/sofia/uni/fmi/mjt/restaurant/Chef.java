package bg.sofia.uni.fmi.mjt.restaurant;

public class Chef extends Thread {

    private final int id;
    private final Restaurant restaurant;
    private int cookedMealsCount;

    public Chef(int id, Restaurant restaurant) {
        this.id = id;
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        cookMeal();
    }

    /**
     * Gets an order from the backlog and cooks it.
     **/
    private void cookMeal() {
        Order order = null;

        while ((order = restaurant.nextOrder()) != null) {
            try {
                Thread.sleep(order.meal().getCookingTime());
                cookedMealsCount++;
            } catch (InterruptedException e) {
                System.err.print("Unexpected exception was thrown: " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.printf("Chef %d cooked %s meals.\n", id, cookedMealsCount);
    }

    /**
     * Returns the total number of meals that this chef has cooked.
     **/
    public int getTotalCookedMeals() {
        return cookedMealsCount;
    }

}