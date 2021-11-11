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

        if (!isValidVehicle(vehicle) || !isAvailable(vehicle) || until.isBefore(LocalDateTime.now())) {
            return -1.0;
        }

        vehicle.setEndOfReservationPeriod(until);
        long minutes = Duration.between(LocalDateTime.now(), until).toMinutes();
        return (minutes + 1) * vehicle.getPricePerMinute();
    }

    @Override
    public Vehicle findNearestAvailableVehicleInRadius(String type, Location location, double maxDistance) {
        double minDistance = maxDistance;

        Vehicle nearestVehicle = null;
        for (Vehicle v : vehicles) {
            double currDistance = location.getDistance(v.getLocation());
            if (v.getType().equals(type) && isAvailable(v) &&
                    currDistance < minDistance) {
                nearestVehicle = v;
                minDistance = currDistance;
            }
        }

        return nearestVehicle;
    }

    private boolean isValidVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return false;
        }

        for (Vehicle v : vehicles) {
            if (v.equals(vehicle)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAvailable(Vehicle vehicle) {
        return vehicle.getEndOfReservationPeriod().isBefore(LocalDateTime.now());
    }
}
