package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import com.sun.nio.sctp.NotificationHandler;

import java.time.LocalDateTime;
import java.util.Objects;

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
        return endOfReservationPeriod == null ? LocalDateTime.now().minusMinutes(1) :endOfReservationPeriod;
    }

    @Override
    public void setEndOfReservationPeriod(LocalDateTime endOfReservationPeriod) {
        this.endOfReservationPeriod = endOfReservationPeriod;
    }

    public abstract double getPricePerMinute();

    public abstract String getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleBase other = (VehicleBase) o;
        return getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
