package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import java.time.LocalDate;
import java.time.LocalDateTime;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

public interface Vehicle {

    double getPricePerMinute();
    String getType();
    String getId();
    Location getLocation();
    LocalDateTime getEndOfReservationPeriod();
    void setEndOfReservationPeriod(LocalDateTime until);
}

