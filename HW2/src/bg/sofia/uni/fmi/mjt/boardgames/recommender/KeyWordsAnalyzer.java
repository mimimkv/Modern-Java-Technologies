package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeyWordsAnalyzer {

    private static final String SPLIT_REGEX = "[\\p{IsPunctuation}\\p{IsWhite_Space}]+";

    private static Stream<String> tokenizeDescription(BoardGame game) {
        return Stream.of(game.description().toLowerCase().split(SPLIT_REGEX));
    }

    private static Set<String> getWordsFromDescription(BoardGame game) {
        return tokenizeDescription(game)
                .collect(Collectors.toSet());
    }

    public static List<BoardGame> getByKeywords(List<BoardGame> games, Set<String> stopWords, String... keywords) {

        return games.stream()
                .map(game -> Map.entry(game, Arrays.stream(keywords)
                        .map(String::toLowerCase)
                        .filter(word -> !stopWords.contains(word) && getWordsFromDescription(game).contains(word))
                        .count()))
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(game -> game.getValue() > 0)
                .map(Map.Entry::getKey)
                .toList();

    }
}
