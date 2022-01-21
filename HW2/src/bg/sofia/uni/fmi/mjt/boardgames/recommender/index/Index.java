package bg.sofia.uni.fmi.mjt.boardgames.recommender.index;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Index {

    private static final String SPLIT_REGEX = "[\\p{IsPunctuation}\\p{IsWhite_Space}]+";
    private final Map<String, Set<Integer>> wordsToIds;
    private final Set<String> stopWords;

    public Index(List<BoardGame> games, Set<String> stopWords) {
        this.stopWords = stopWords;
        wordsToIds = addWordsFromDescriptions(games);

    }

    public void print(Writer writer) {
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        try {
            for (Map.Entry<String, Set<Integer>> entry : wordsToIds.entrySet()) {
                bufferedWriter.write(entry.getKey());
                bufferedWriter.write(": ");
                bufferedWriter.write(entry.getValue()
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
                bufferedWriter.write(System.lineSeparator());
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while writing");
        }
    }

    private Stream<String> tokenizeDescription(BoardGame game) {
        return Stream.of(game.description().toLowerCase().split(SPLIT_REGEX));
    }

    private Map<String, Set<Integer>> addWordsFromDescriptions(List<BoardGame> games) {
        return games.stream()
                .flatMap(game -> tokenizeDescription(game)
                        .filter(word -> !stopWords.contains(word))
                        .map(word -> Map.entry(word, game)))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(entry -> entry.getValue().id(), Collectors.toSet())
                ));
    }
}
