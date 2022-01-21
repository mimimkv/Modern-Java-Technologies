package bg.sofia.uni.fmi.mjt.race.track.pit;

import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Pit {

    private final int pitTeamsCount;
    private final PitTeam[] pitTeams;
    private final Queue<Car> stoppedCars;
    private final AtomicInteger pitStopsCount;
    private boolean isFinished;

    public Pit(int nPitTeams) {
        this.pitTeamsCount = nPitTeams;
        this.stoppedCars = new LinkedList<>();
        this.isFinished = false;
        this.pitStopsCount = new AtomicInteger();
        this.pitTeams = new PitTeam[pitTeamsCount];
        initPitTeams();
    }

    private void initPitTeams() {
        for (int i = 0; i < pitTeamsCount; i++) {
            pitTeams[i] = new PitTeam(i, this);
            pitTeams[i].setName("PitTeam: " + i);
            pitTeams[i].start();
        }
    }

    public synchronized void submitCar(Car car) {
        pitStopsCount.incrementAndGet();
        stoppedCars.add(car);
        notifyAll();
    }

    public synchronized Car getCar() {
        while (!isFinished && stoppedCars.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return stoppedCars.poll();
    }

    public int getPitStopsCount() {
        return pitStopsCount.get();
    }

    public List<PitTeam> getPitTeams() {
        return Arrays.stream(pitTeams).toList();
    }

    public void finishRace() {
        synchronized (this) {
            isFinished = true;
            notifyAll();
        }

        for (int i = 0; i < pitTeamsCount; i++) {
            try {
                pitTeams[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}