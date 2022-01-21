package bg.sofia.uni.fmi.mjt.boardgames;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public record BoardGame(int id, String name, String description, int maxPlayers, int minAge, int minPlayers,
                        int playingTimeMins, Collection<String> categories, Collection<String> mechanics) {

    private static final int ID = 0;
    private static final int MAX_PLAYERS = 1;
    private static final int MIN_AGE = 2;
    private static final int MIN_PLAYERS = 3;
    private static final int NAME = 4;
    private static final int PLAYING_TIME_MINS = 5;
    private static final int CATEGORIES = 6;
    private static final int MECHANICS = 7;
    private static final int DESCRIPTION = 8;


    public static BoardGame of(String line) {
        String[] tokens = line.split(";");

        int id = Integer.parseInt(tokens[ID]);
        int maxPlayers = Integer.parseInt(tokens[MAX_PLAYERS]);
        int minAge = Integer.parseInt(tokens[MIN_AGE]);
        int minPlayers = Integer.parseInt(tokens[MIN_PLAYERS]);
        int playingTimeMins = Integer.parseInt(tokens[PLAYING_TIME_MINS]);
        String name = tokens[NAME];
        String description = tokens[DESCRIPTION];

        String[] categoriesTokens = tokens[CATEGORIES].split(",");
        String[] mechanicsTokens = tokens[MECHANICS].split(",");

        Set<String> categories = new HashSet<>(Arrays.asList(categoriesTokens));
        Set<String> mechanics = new HashSet<>(Arrays.asList(mechanicsTokens));

        return new BoardGame(id, name, description, maxPlayers, minAge, minPlayers,
                playingTimeMins, categories, mechanics);
    }


}