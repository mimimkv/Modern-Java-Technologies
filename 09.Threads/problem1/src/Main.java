import bg.sofia.uni.fmi.mjt.race.track.Car;
import bg.sofia.uni.fmi.mjt.race.track.RaceTrack;
import bg.sofia.uni.fmi.mjt.race.track.Track;

public class Main {
    private static final int CARS_COUNT = 20;
    private static final int TEAMS_IN_PIT_COUNT = 5;
    private static final int PIT_STOPS_COUNT = 3;
    private static final int WAITING_TIME = 3000;

    public static void main(String[] args) throws InterruptedException {

        Track track = new RaceTrack(TEAMS_IN_PIT_COUNT);
        Car[] cars = new Car[CARS_COUNT];
        Thread[] threads = new Thread[CARS_COUNT];

        for (int i = 0; i < CARS_COUNT; i++) {
            cars[i] = new Car(i, PIT_STOPS_COUNT, track);
            threads[i] = new Thread(cars[i]);
            threads[i].start();
        }

        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        track.getPit().finishRace();
        System.out.println("Number of finished cars: " + track.getNumberOfFinishedCars());
        System.out.println("Total pitStops: " + track.getPit().getPitStopsCount());


    }
}
