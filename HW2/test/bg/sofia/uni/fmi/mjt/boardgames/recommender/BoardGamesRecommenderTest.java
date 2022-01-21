package bg.sofia.uni.fmi.mjt.boardgames.recommender;


import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class BoardGamesRecommenderTest {

    private static BoardGamesRecommender recommender;

    @BeforeAll
    public static void setUp() {
        recommender = new BoardGamesRecommender(initGamesStream(), initStopWordsStream());

    }

    @Test
    public void testGetAllGamesUnmodifiableView() {
        Collection<BoardGame> games = recommender.getGames();

        try {
            games.clear();
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
            return;
        }
        fail("The returned collection should be unmodifiable");
    }

    @Test
    public void testGetAllGamesAreAllLoaded() {
        Collection<BoardGame> games = recommender.getGames();
        int expected = 6;
        int actual = games.size();

        assertEquals(expected, actual, "Not all games are loaded");
    }

    @Test
    public void testGetSimilarToGameNull() {
        assertThrows(IllegalArgumentException.class, () -> recommender.getSimilarTo(null, 6),
                "IllegalArgumentException expected when method called with null argument");
    }

    @Test
    public void testGetSimilarToNIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> recommender.getSimilarTo(BoardGame.of("1;2;3;4;5;6;7;8;9"), -1),
                "IllegalArgumentException expected when method called with null argument");
    }

    @Test
    public void testGetSimilarToNIsZero() {
        Collection<BoardGame> expected = Collections.emptyList();
        Collection<BoardGame> actual = recommender.getSimilarTo(BoardGame.of("1;2;3;4;5;6;7;8;9"), 0);

        assertIterableEquals(expected, actual, "Method should return an empty collection when n is 0");
    }

    @Test
    public void testGetSimilarToGameNotInTheCollection() {
        BoardGame game = BoardGame.of("101;5;10;2;Some game;90;Adventure,Fantasy,Exploration,Fighting;Dice Rolling;something");
        List<BoardGame> expected = Collections.singletonList(
                BoardGame.of("31;5;10;2;Dark World;90;Adventure,Exploration,Fantasy,Fighting;Dice Rolling,Grid Movement;The game is three-dimensional"));
        Collection<BoardGame> actual = recommender.getSimilarTo(game, 1);

        assertEquals(expected.size(), actual.size(), "Size of returned collection is not correct");
        assertIterableEquals(expected, actual, "The returned collection does not contain the correct elements");
    }

    @Test
    public void testGetSimilarToGameFromTheCollectionOneGameReturned() {
        BoardGame game = BoardGame.of("31;5;10;2;Dark World;90;Adventure,Exploration,Fantasy,Fighting;Dice Rolling,Grid Movement;The game is three-dimensional");
        List<BoardGame> expected = Collections.singletonList(
                BoardGame.of("4;8;7;2;Monopoly;120;Strategy,Adventure;Dice Rolling,Board;It is a multi-player board game"));
        Collection<BoardGame> actual = recommender.getSimilarTo(game, 1);

        assertEquals(expected.size(), actual.size(), "Size of returned collection is not correct");
        assertIterableEquals(expected, actual, "The returned collection does not contain the correct elements");
    }

    @Test
    public void testGetSimilarToSeveralGamesReturned() {
        BoardGame game = BoardGame.of("31;5;10;2;Dark World;90;Adventure,Exploration,Fantasy,Fighting;Dice Rolling,Grid Movement;The game is three-dimensional");
        List<BoardGame> expected = List.of(
                BoardGame.of("4;8;7;2;Monopoly;120;Strategy,Adventure;Dice Rolling,Board;It is a multi-player board game"),
                BoardGame.of("1;4;5;2;Ne se syrdi choveche;20;Adventure;Dice Rolling;some description"));

        Collection<BoardGame> actual = recommender.getSimilarTo(game, 2);
        assertIterableEquals(expected, actual, "The returned collection does not contain the correct elements");
    }

    @Test
    public void testBoardGamesRecommenderConstructorWithZipFile() {
        Path pathZip = Path.of("data.zip");
        String fileName = "data.csv";
        Path pathStopWords = Path.of("stopwords.txt");

        BoardGamesRecommender recommender1 = new BoardGamesRecommender(pathZip, fileName, pathStopWords);
        BoardGame game = BoardGame.of("2;10;4;2;Uno;60;Card game;Cards;some description...");

        assertEquals(recommender.getGames().size(), recommender1.getGames().size(),
                "Size of returned collection is not correct");

        assertTrue(recommender1.getGames().contains(game), "game that should be in the collection is not found");
    }

    @Test
    public void testStoreGamesIndexWriterIsNull() {
        assertThrows(IllegalArgumentException.class, () -> recommender.storeGamesIndex(null),
                "IllegalArgumentException expected when method called with null argument");
    }

    @Test
    public void testStoreGamesIndex() {
        StringWriter writer = new StringWriter();
        recommender.storeGamesIndex(writer);

        String expected =
                "some: 1" + System.lineSeparator() +
                        "game: 33,2,3,4,31" + System.lineSeparator() +
                        "dimensional: 31" + System.lineSeparator() +
                        "description: 1" + System.lineSeparator() +
                        "three: 31" + System.lineSeparator() +
                        "multi: 4" + System.lineSeparator() +
                        "cooperative: 33" + System.lineSeparator() +
                        "adventure: 33" + System.lineSeparator() +
                        "classic: 2" + System.lineSeparator() +
                        "family: 2" + System.lineSeparator() +
                        "board: 33,4" + System.lineSeparator() +
                        "card: 2,3" + System.lineSeparator() +
                        "player: 4" + System.lineSeparator();

        assertEquals(expected, writer.toString(), "Games index is not correct");

    }

    @Test
    public void testGetByDescriptionNullArgument() {
        assertThrows(IllegalArgumentException.class, () -> recommender.getByDescription(),
                "IllegalArgumentException expected when method called with no arguments");
    }

    @Test
    public void testGetByDescriptionOneGame() {
        List<BoardGame> expected = Collections.singletonList(BoardGame.of("4;8;7;2;Monopoly;120;Strategy,Adventure;Dice Rolling,Board;It is a multi-player board game"));
        List<BoardGame> actual = recommender.getByDescription("It", "is", "a", "mUlti", "player");

        assertEquals(expected.size(), actual.size(), "Method does not return list with the correct size");
        assertIterableEquals(expected, actual, "Method does not return the correct collection");
    }

    @Test
    public void testGetByDescription() {
        List<BoardGame> expected = List.of(
                BoardGame.of("2;10;4;2;Uno;60;Card game;Cards;It is the classic family card game"),
                BoardGame.of("3;4;18;4;Belot;120;Card game,Strategy;Cards;It is a card game")
        );
        List<BoardGame> actual = recommender.getByDescription("card", "family");

        assertEquals(expected.size(), actual.size(), "Method does not return list with the correct size");
        assertIterableEquals(expected, actual, "Method does not return the correct collection");
    }

    private static Reader initGamesStream() {
        String[] games = {
                "first line is not read",
                "31;5;10;2;Dark World;90;Adventure,Exploration,Fantasy,Fighting;Dice Rolling,Grid Movement;The game is three-dimensional",
                "33;8;12;1;Arkham Horror;180;Adventure,Strategy;Co-operative Play;It is a cooperative adventure board game",
                "1;4;5;2;Ne se syrdi choveche;20;Adventure;Dice Rolling;some description",
                "2;10;4;2;Uno;60;Card game;Cards;It is the classic family card game",
                "3;4;18;4;Belot;120;Card game,Strategy;Cards;It is a card game",
                "4;8;7;2;Monopoly;120;Strategy,Adventure;Dice Rolling,Board;It is a multi-player board game"
        };

        return new StringReader(Arrays.stream(games).collect(Collectors.joining(System.lineSeparator())));
    }

    private static Reader initStopWordsStream() {
        String[] stopWords = {
                "it",
                "or",
                "and",
                "a",
                "there",
                "is",
                "the"
        };

        return new StringReader(Arrays.stream(stopWords).collect(Collectors.joining(System.lineSeparator())));
    }
}