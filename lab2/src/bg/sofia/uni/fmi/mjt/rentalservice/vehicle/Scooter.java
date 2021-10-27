package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;

public class Scooter implements Vehicle {
    private String id;
    private Location location;
    

    public Scooter(String id, Location location) {
        this.id = id;
        this.location = location;
    }

    @Override
    public double getPricePerMinute() {
        return 0.30;
    }

    @Override
    public String getType() {
        return "Scooter";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public LocalDateTime getEndOfReservationPeriod() {

    }

    @Override
    public void setEndOfReservationPeriod(LocalDateTime until) {

    }
}
