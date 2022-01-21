package bg.sofia.uni.fmi.mjt.boardgames.analyzer;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class BoardGamesStatisticsAnalyzer implements StatisticsAnalyzer {

    private final Collection<BoardGame> boardGames;

    public BoardGamesStatisticsAnalyzer(Collection<BoardGame> games) {
        boardGames = new LinkedList<>(games);
    }

    @Override
    public List<String> getNMostPopularCategories(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Argument cannot be a negative number");
        }
        if (n == 0) {
            return Collections.emptyList();
        }

        Map<String, Long> categoriesMap = boardGames.stream()
                .flatMap(game -> game.categories().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        if (n > categoriesMap.size()) {
            n = categoriesMap.size();
        }

        return categoriesMap
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public double getAverageMinAge() {
        return boardGames.stream()
                .mapToDouble(BoardGame::minAge)
                .average()
                .orElse(0.0);
    }

    @Override
    public double getAveragePlayingTimeByCategory(String category) {
        if (category == null) {
            throw new IllegalArgumentException("Arguments cannot be null or empty");
        }

        return boardGames.stream()
                .filter(game -> game.categories().contains(category))
                .mapToDouble(BoardGame::playingTimeMins)
                .average()
                .orElse(0.0);
    }

    @Override
    public Map<String, Double> getAveragePlayingTimeByCategory() {

        return boardGames.stream()
                .flatMap(game -> game.categories()
                        .stream()
                        .map(category -> Map.entry(category, game.playingTimeMins())))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        HashMap::new,
                        Collectors.averagingDouble(Map.Entry::getValue)));
    }
}
