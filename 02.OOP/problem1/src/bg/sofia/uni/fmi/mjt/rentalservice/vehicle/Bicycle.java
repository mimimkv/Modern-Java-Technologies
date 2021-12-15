package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Bicycle extends VehicleBase {
    private final double pricePerMinute = 0.20;
    private final String TYPE = "BICYCLE";

    public Bicycle(String id, Location location) {
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
