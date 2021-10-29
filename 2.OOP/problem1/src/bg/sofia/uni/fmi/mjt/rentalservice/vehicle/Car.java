package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Car extends VehicleBase {
    private final double pricePerMinute = 0.50;
    private final String TYPE = "Car";

    public Car(String id, Location location) {
        super(id, location);
    }

    @Override
    public double getPricePerMinute() {
        return pricePerMinute;
    }

    @Override
    public String getType() {
        return TYPE;
    }


}
