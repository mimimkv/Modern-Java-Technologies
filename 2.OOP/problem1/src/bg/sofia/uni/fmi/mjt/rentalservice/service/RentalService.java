package bg.sofia.uni.fmi.mjt.rentalservice.service;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;

public class RentalService implements RentalServiceAPI {
    private Vehicle[] vehicles;

    public RentalService(Vehicle[] vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public double rentUntil(Vehicle vehicle, LocalDateTime until) {

        if (!isValidVehicle(vehicle) || !isAvailable(vehicle)) {
            return -1.0;
        }

        vehicle.setEndOfReservationPeriod(until);
        long minutes = Duration.between(LocalDateTime.now(), until).toMinutes();
        return minutes * vehicle.getPricePerMinute();
    }

    @Override
    public Vehicle findNearestAvailableVehicleInRadius(String type, Location location, double maxDistance) {
        int indexMinDistance = -1;
        double minDistance = maxDistance;

        for (int i = 0; i < vehicles.length; ++i) {
            if (indexMinDistance == -1 ||
                location.getDistance(vehicles[i].getLocation()) < minDistance) {
                indexMinDistance = i;
                minDistance = location.getDistance(vehicles[i].getLocation());
            }
        }

        return indexMinDistance == -1? null : vehicles[indexMinDistance];
    }

    private boolean isValidVehicle(Vehicle vehicle) {
        for (int i = 0; i < vehicles.length; ++i) {
            if (vehicle.getId().equals(vehicles[i].getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean isAvailable(Vehicle vehicle) {
        return vehicle.getEndOfReservationPeriod().isBefore(LocalDateTime.now());
    }
}
