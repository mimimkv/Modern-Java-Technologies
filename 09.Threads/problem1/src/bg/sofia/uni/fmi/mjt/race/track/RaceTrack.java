package bg.sofia.uni.fmi.mjt.race.track;

import bg.sofia.uni.fmi.mjt.race.track.pit.Pit;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RaceTrack implements Track {

    private static final int DEFAULT_TEAMS_COUNT = 20;

    private static AtomicInteger numberOfFinishedCars;
    private final List<Integer> finishedCarsIds;
    private final Pit pit;

    public RaceTrack() {
        this(DEFAULT_TEAMS_COUNT);
    }

    public RaceTrack(int teamsInPitCount) {
        numberOfFinishedCars = new AtomicInteger();
        this.finishedCarsIds = new LinkedList<>();
        this.pit = new Pit(teamsInPitCount);

    }

    @Override
    public void enterPit(Car car) {
        if (car.getNPitStops() == 0) {
            finishedCarsIds.add(car.getCarId());
            numberOfFinishedCars.incrementAndGet();
        } else {
            pit.submitCar(car);

        }
    }

    @Override
    public int getNumberOfFinishedCars() {
        return numberOfFinishedCars.get();
    }

    @Override
    public List<Integer> getFinishedCarsIds() {
        return finishedCarsIds;
    }

    @Override
    public Pit getPit() {
        return pit;
    }

}
