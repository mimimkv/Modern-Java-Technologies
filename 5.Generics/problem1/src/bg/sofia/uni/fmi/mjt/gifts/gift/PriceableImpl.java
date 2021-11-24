package bg.sofia.uni.fmi.mjt.gifts.gift;

public class PriceableImpl implements Priceable {

    private double price;

    public PriceableImpl(double price) {
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
