package bg.sofia.uni.fmi.mjt.boardgames.recommender.distance;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

import java.util.Collection;
import java.util.HashSet;


public class Distance {

    public static double getDistance(BoardGame game1, BoardGame game2) {
        int typeDist = distanceTypeAttributes(game1, game2);

        // returns a negative number if games share no common category
        if (typeDist < 0) {
            return -1.0;
        }

        return typeDist + distanceNumericalAttributes(game1, game2);
    }

    private static double distanceNumericalAttributes(BoardGame g1, BoardGame g2) {
        double dist = (double)
                (g1.playingTimeMins() - g2.playingTimeMins()) * (g1.playingTimeMins() - g2.playingTimeMins()) +
                (g1.maxPlayers() - g2.maxPlayers()) * (g1.maxPlayers() - g2.maxPlayers()) +
                (g1.minAge() - g2.minAge()) * (g1.minAge() - g2.minAge()) +
                (g1.minPlayers() - g2.minPlayers()) * (g1.minPlayers() - g2.minPlayers());

        return Math.sqrt(dist);
    }

    private static int distanceTypeAttributes(BoardGame g1, BoardGame g2) {
        int dist;
        Collection<String> union = new HashSet<>(g1.categories());
        Collection<String> intersection = new HashSet<>(g1.categories());
        union.addAll(g2.categories());
        intersection.retainAll(g2.categories());

        dist = union.size() - intersection.size();
        if (intersection.size() < 1) {
            return -1;
        }

        union.clear();
        intersection.clear();
        union.addAll(g1.mechanics());
        union.addAll(g2.mechanics());
        intersection.addAll(g1.mechanics());
        intersection.retainAll(g2.mechanics());

        dist += union.size() - intersection.size();
        return dist;
    }
}
