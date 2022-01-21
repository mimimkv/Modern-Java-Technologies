package bg.sofia.uni.fmi.mjt.race.track.pit;


import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class PitTeam extends Thread {

    private static final Random RANDOM = new Random();
    private static final int TIME_TO_REPAIR_CAR = 200;

    private final int id;
    private final Pit pitStop;
    private final AtomicInteger pitStoppedCars;

    public PitTeam(int id, Pit pitStop) {
        this.id = id;
        this.pitStop = pitStop;
        pitStoppedCars = new AtomicInteger();
    }

    @Override
    public void run() {
        Car car = null;

        while ((car = pitStop.getCar()) != null) {

            try {
                Thread.sleep(RANDOM.nextInt(TIME_TO_REPAIR_CAR));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            pitStoppedCars.incrementAndGet();

            Thread thread = new Thread(new Car(car.getCarId(), car.getNPitStops() - 1, car.getTrack()));
            thread.start();
        }

        //System.out.println(this.getName() + " finished work");
        System.out.println(this.getName() + " repaired " + this.pitStoppedCars + " cars.");
    }


    public int getPitStoppedCars() {
        return pitStoppedCars.get();
    }

}
