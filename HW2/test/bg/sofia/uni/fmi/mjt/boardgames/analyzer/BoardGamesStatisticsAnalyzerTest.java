package bg.sofia.uni.fmi.mjt.boardgames.analyzer;


import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BoardGamesStatisticsAnalyzerTest {

    private static BoardGamesStatisticsAnalyzer analyzer;


    @BeforeAll
    public static void setUp() {
        List<BoardGame> boardGames = List.of(
                BoardGame.of("31;5;10;2;Dark World;90;Adventure,Exploration,Fantasy,Fighting;Dice Rolling,Grid Movement;some descr"),
                BoardGame.of("33;8;12;1;Arkham Horror;180;Adventure,Strategy;Co-operative Play;some descr"),
                BoardGame.of("1;4;5;2;Ne se syrdi choveche;20;Adventure;Dice Rolling;some description"),
                BoardGame.of("2;10;4;2;Uno;60;Card game;Cards;some description..."),
                BoardGame.of("3;4;18;4;Belot;120;Card game,Strategy;Cards;some description"),
                BoardGame.of("4;8;7;2;Monopoly;120;Strategy,Adventure;Dice Rolling,Board;description")
        );
        analyzer = new BoardGamesStatisticsAnalyzer(boardGames);

    }

    @Test
    public void testGetAverageMinAge() {
        double expected = 9.33;
        double actual = analyzer.getAverageMinAge();

        assertEquals(expected, actual, 0.01, "Average min age is not calculated correctly");
    }

    @Test
    public void testGetAverageMinAgeNoGamesInDataSet() {
        BoardGamesStatisticsAnalyzer emptyAnalyzer = new BoardGamesStatisticsAnalyzer(Collections.emptyList());
        double expected = 0.0;
        double actual = emptyAnalyzer.getAverageMinAge();

        assertEquals(expected, actual, 0.01, "Average min age should be 0.0 when there are no games in the dataset");
    }

    @Test
    public void testGetNMostPopularCategoriesNIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getNMostPopularCategories(-1),
                "getNMOstPopularCategories should throw an exception when called with a negative number");
    }

    @Test
    public void testGetNMostPopularCategoriesNIsZero() {
        List<String> expected = Collections.emptyList();
        List<String> actual = analyzer.getNMostPopularCategories(0);

        assertIterableEquals(expected, actual,
                "getNMostPopularCategories should return an empty list when called with argument 0");
    }

    @Test
    public void testGetNMostPopularCategoriesNIsGreaterThanNumberOfAllCategories() {
        List<String> expected = List.of("Adventure", "Strategy", "Card game", "Fighting", "Fantasy", "Exploration");
        List<String> actual = analyzer.getNMostPopularCategories(10);

        boolean assertEqualLists = expected.containsAll(actual) && actual.containsAll(expected);

        assertEquals(expected.size(), actual.size(), "method does not return list with correct size");
        assertTrue(assertEqualLists, "method does not return the correct most popular categories");
    }

    @Test
    public void testGetNMostPopularCategoriesTestWithOne() {
        List<String> expected = Collections.singletonList("Adventure");
        List<String> actual = analyzer.getNMostPopularCategories(1);

        assertIterableEquals(expected, actual, "method does not return the correct most popular category");
    }

    @Test
    public void testGetNMostPopularCategoriesTwoCategories() {
        List<String> expected = List.of("Adventure", "Strategy");
        List<String> actual = analyzer.getNMostPopularCategories(2);

        assertIterableEquals(expected, actual, "method does not return the correct most popular categories");
    }

    @Test
    public void testGetNMostPopularCategoriesThreeCategories() {
        List<String> expected = List.of("Adventure", "Strategy", "Card game");
        List<String> actual = analyzer.getNMostPopularCategories(3);

        assertIterableEquals(expected, actual, "method does not return the correct most popular categories");
    }

    @Test
    public void testGetAveragePlayingTimeByCategoryNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getAveragePlayingTimeByCategory(null),
                "IllegalArgumentException expected when method called with null argument");
    }

    @Test
    public void testGetAveragePlayingTimeByCategoryNoSuchCategory() {
        double expected = 0.0;
        double actual = analyzer.getAveragePlayingTimeByCategory("random cat");

        assertEquals(expected, actual, 0.01,
                "method should return 0.0 when there is no such category in the dataset");
    }

    @Test
    public void testGetAveragePlayingTimeByCategory() {
        Map<String, Double> expected = Map.of(
                "Adventure", 102.5,
                "Exploration", 90.0,
                "Fantasy", 90.0,
                "Fighting", 90.0,
                "Card game", 90.0,
                "Strategy", 140.0);

        Map<String, Double> actual = analyzer.getAveragePlayingTimeByCategory();
        assertEquals(expected.size(), actual.size(), "method does not return a collection with the correct size");
        assertEquals(expected, actual, "method does not return the correct collection");
    }
}