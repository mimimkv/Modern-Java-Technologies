package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public class Scooter extends VehicleBase {
    private final double pricePerMinute = 0.30;
    private final String TYPE = "Scooter";

    public Scooter(String id, Location location) {
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
