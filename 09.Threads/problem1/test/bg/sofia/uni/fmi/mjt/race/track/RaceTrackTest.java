package bg.sofia.uni.fmi.mjt.race.track;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RaceTrackTest {

    private static final int WAITING_TIME = 3000;

    private Track track;

    @BeforeEach
    public void setUp() {
        track = new RaceTrack(3);
    }

    @Test
    public void testGetNumberOfFinishedCars() throws InterruptedException {
        Car[] cars = new Car[5];
        for (int i = 0; i < 5; i++) {
            cars[i] = new Car(i, 3, track);
            track.enterPit(cars[i]);
        }

        Thread.sleep(WAITING_TIME);

        track.getPit().finishRace();
        int expected = 5;
        int actual = track.getNumberOfFinishedCars();

        assertEquals(expected, actual, "Number of finished cars is not correct");
    }

    @Test
    public void testGetPitCount() throws InterruptedException {
        Car[] cars = new Car[4];
        for (int i = 0; i < 4; i++) {
            cars[i] = new Car(i, 5, track);
            track.enterPit(cars[i]);
        }

        Thread.sleep(4000);

        track.getPit().finishRace();
        int expected = 20;
        int actual = track.getPit().getPitStopsCount();

        assertEquals(expected, actual, "Method does not return the correct total pit stops count");
    }

    @Test
    public void testGetFinishedCarsIds() throws InterruptedException {
        Car[] cars = new Car[3];
        for (int i = 0; i < 3; i++) {
            cars[i] = new Car(i, 2, track);
            track.enterPit(cars[i]);
        }

        Thread.sleep(WAITING_TIME);

        track.getPit().finishRace();
        List<Integer> actual = List.of(0, 1, 2);
        List<Integer> expected = track.getFinishedCarsIds();
        assertTrue(assertEqualCollections(expected, actual), "Method does not return the correct list of IDs");
    }

    private boolean assertEqualCollections(Collection<Integer> collection1, Collection<Integer> collection2) {
        return collection1.size() == collection2.size() &&
                collection1.containsAll(collection2) &&
                collection2.containsAll(collection1);
    }
}