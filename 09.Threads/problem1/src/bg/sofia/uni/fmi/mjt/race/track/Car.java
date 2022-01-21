package bg.sofia.uni.fmi.mjt.race.track;

import java.util.Random;

public class Car implements Runnable {

    private static final Random RANDOM = new Random();
    private static final int TIME_FOR_ONE_ROUND = 1000;

    private final int id;
    private final int nPitStops;
    private final Track track;

    public Car(int id, int nPitStops, Track track) {
        this.id = id;
        this.nPitStops = nPitStops;
        this.track = track;
    }

    @Override
    public void run() {
        if (this.nPitStops != 0) {
            try {
                Thread.sleep(RANDOM.nextInt(TIME_FOR_ONE_ROUND));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        track.enterPit(this);
        //System.out.println("Car " + id + " running, " + "left rounds: " + nPitStops);
    }

    public int getCarId() {
        return id;
    }

    public int getNPitStops() {
        return nPitStops;
    }

    public Track getTrack() {
        return track;
    }

}