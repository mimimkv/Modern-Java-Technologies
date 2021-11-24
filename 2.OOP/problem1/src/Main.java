import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.service.RentalServiceAPI;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Bicycle;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Car;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Scooter;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;
import bg.sofia.uni.fmi.mjt.rentalservice.service.RentalService;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        Vehicle car1 = new Car("car1", new Location(1, 0));
        Vehicle car2 = new Car("car2", new Location(2, 2));
        Vehicle car3 = new Car("car3", new Location(10, 7));

        Vehicle bicycle1 = new Bicycle("bicycle1", new Location(1, 2));
        Vehicle bicycle2 = new Bicycle("bicycle2", new Location(5, 5));
        Vehicle bicycle3 = new Bicycle("bicycle3", new Location(3, 9));

        Vehicle scooter1 = new Scooter("scooter1", new Location(3, 4));
        Vehicle scooter2 = new Scooter("scooter2", new Location(5, 6));
        Vehicle scooter3 = new Scooter("scooter3", new Location(25, 2.5));
        Vehicle scooter4 = new Scooter("scooter4", new Location(12, 12));

        Vehicle[] vehicles = new Vehicle[]{
                car1, car2, car3, bicycle1, bicycle2, scooter1, scooter2, scooter3, scooter4};
        RentalServiceAPI rentalService = new RentalService(vehicles);

        double priceForCar = rentalService.rentUntil(car1, LocalDateTime.now().plusMinutes(20));
        double priceForBicycle = rentalService.rentUntil(bicycle1, LocalDateTime.now().plusMinutes(20));
        double priceForBicycle2 = rentalService.rentUntil(bicycle1, LocalDateTime.now().plusMinutes(5));
        double priceForBicycle3 = rentalService.rentUntil(bicycle3, LocalDateTime.now().plusMinutes(25));

        System.out.printf("%.2f\n", priceForCar); // 10.50
        System.out.printf("%.2f\n", priceForBicycle); // 4.20
        System.out.printf("%.2f\n", priceForBicycle2); // -1
        System.out.printf("%.2f\n", priceForBicycle3); // -1

        Location currentLocation = new Location(0, 0);
        System.out.println();
        Vehicle nearestVehicle =
                rentalService.findNearestAvailableVehicleInRadius("CAR", currentLocation, 10);
        System.out.println(nearestVehicle.getId()); // car2

        Vehicle nearestScooter1 =
                rentalService.findNearestAvailableVehicleInRadius("SCOOTER", currentLocation, 1.5);

        if (nearestScooter1 == null) { // true
            System.out.println("No scooter found at this distance");
        } else {
            System.out.println(nearestScooter1.getId());
        }

        Vehicle nearestScooter2 =
                rentalService.findNearestAvailableVehicleInRadius("SCOOTER", currentLocation, 50.0);
        System.out.println(nearestScooter2.getId()); // scooter1

        Vehicle nearestBicycle =
                rentalService.findNearestAvailableVehicleInRadius("BICYCLE", currentLocation, 10.0);
        System.out.println(nearestBicycle.getId()); // bicycle2

        double priceForScooter = rentalService.rentUntil(scooter3, LocalDateTime.now().plusMinutes(5));
        System.out.printf("%.2f\n", priceForScooter); // 1.80

        double priceForCar2 = rentalService.rentUntil(car2, LocalDateTime.now().plusMinutes(3));
        System.out.printf("%.2f\n", priceForCar2); // 2.00
    }
}
