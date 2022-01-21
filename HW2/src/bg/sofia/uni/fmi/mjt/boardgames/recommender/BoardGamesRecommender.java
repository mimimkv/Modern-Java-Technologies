package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.recommender.distance.Distance;
import bg.sofia.uni.fmi.mjt.boardgames.recommender.index.Index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BoardGamesRecommender implements Recommender {

    private final Set<String> stopWords = new HashSet<>();
    private final List<BoardGame> boardGames = new LinkedList<>();
    private final Index index;

    /**
     * Constructs an instance using the provided file names.
     *
     * @param datasetZipFile  ZIP file containing the board games dataset file
     * @param datasetFileName the name of the dataset file (inside the ZIP archive)
     * @param stopWordsFile   the stopwords file
     */
    public BoardGamesRecommender(Path datasetZipFile, String datasetFileName, Path stopWordsFile) {
        if (datasetZipFile == null || datasetFileName == null ||
                stopWordsFile == null || datasetFileName.isBlank()) {
            throw new IllegalArgumentException("Arguments cannot be null or empty");
        }

        String zipFilePath = datasetZipFile.toAbsolutePath().toString();
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {

            ZipEntry zipEntry = zipFile.getEntry(datasetFileName);
            InputStream inputStream = zipFile.getInputStream(zipEntry);

            loadBoardGames(new InputStreamReader(inputStream));
            loadStopWords(Files.newBufferedReader(stopWordsFile));

        } catch (IOException e) {
            throw new IllegalStateException("A problem occurred while opening/reading the zip file");
        }

        index = new Index(boardGames, stopWords);
    }

    /**
     * Constructs an instance using the provided Reader streams.
     *
     * @param dataset   Reader from which the dataset can be read
     * @param stopwords Reader from which the stopwords list can be read
     */
    public BoardGamesRecommender(Reader dataset, Reader stopwords) {
        if (dataset == null || stopwords == null) {
            throw new IllegalArgumentException("Reader cannot be null");
        }
        loadBoardGames(dataset);
        loadStopWords(stopwords);
        index = new Index(boardGames, stopWords);
    }

    @Override
    public Collection<BoardGame> getGames() {
        return Collections.unmodifiableCollection(boardGames);
    }

    @Override
    public List<BoardGame> getSimilarTo(BoardGame game, int n) {
        assertGameNotNull(game);
        assertNumberNotNegative(n);

        if (n == 0) {
            return Collections.emptyList();
        }

        return boardGames.stream()
                .filter(game1 -> !game1.equals(game))
                .map(game1 -> Map.entry(game1, Distance.getDistance(game1, game)))
                .sorted(Map.Entry.comparingByValue())
                .dropWhile(entry -> entry.getValue() < 0)
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public List<BoardGame> getByDescription(String... keywords) {
        if (keywords.length == 0) {
            throw new IllegalArgumentException("keywords cannot be null");
        }

        return KeyWordsAnalyzer.getByKeywords(boardGames, stopWords, keywords);
    }

    @Override
    public void storeGamesIndex(Writer writer) {
        if (writer == null) {
            throw new IllegalArgumentException("writer cannot be null");
        }

        index.print(writer);
    }


    private void loadStopWords(Reader reader) {
        BufferedReader bufferedReader = new BufferedReader(reader);
        stopWords.addAll(bufferedReader.lines()
                .collect(Collectors.toSet()));

    }

    private void loadBoardGames(Reader reader) {
        BufferedReader bufferedReader = new BufferedReader(reader);
        boardGames.addAll(bufferedReader.lines()
                .skip(1)
                .map(BoardGame::of)
                .collect(Collectors.toSet()));
    }

    private void assertGameNotNull(BoardGame game) {
        if (game == null) {
            throw new IllegalArgumentException("game cannot be null");
        }
    }

    private void assertNumberNotNegative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n cannot be a negative number");
        }
    }
}
