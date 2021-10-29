package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;

public abstract class VehicleBase implements Vehicle {

    private final String id;
    private Location location;
    private LocalDateTime endOfReservationPeriod;

    public VehicleBase(String id, Location location) {
        this.id = id;
        this.location = location;
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
        return endOfReservationPeriod == null ? LocalDateTime.now().minusMinutes(10) :endOfReservationPeriod;
    }

    @Override
    public void setEndOfReservationPeriod(LocalDateTime endOfReservationPeriod) {
        this.endOfReservationPeriod = endOfReservationPeriod;
    }

    public abstract double getPricePerMinute();

    public abstract String getType();


}
