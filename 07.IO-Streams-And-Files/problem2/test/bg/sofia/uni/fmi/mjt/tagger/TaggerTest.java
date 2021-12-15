package bg.sofia.uni.fmi.mjt.tagger;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaggerTest {

    private static final String SAMPLE_DICTIONARY_INPUT = """
            Barcelona,Spain
            Dubai,United Arab Emirates
            London,United Kingdom
            Madrid,Spain
            Sofia,Bulgaria
            Vienna,Austria
            """;

    private Tagger tagger;

    @BeforeEach
    public void setUp() throws IOException {
        Reader citiesCountries = new StringReader(SAMPLE_DICTIONARY_INPUT);
        tagger = new Tagger(citiesCountries);
    }

    @Test
    public void testTagCitiesEmptyFile() throws IOException {
        Reader emptyText = new StringReader("");
        Writer outputWriter = new StringWriter();

        tagger.tagCities(emptyText, outputWriter);
        assertTrue(outputWriter.toString().isEmpty(), "Empty text should remain the same after tagging");

        emptyText.close();
        outputWriter.close();
    }

    @Test
    public void testTagCitiesTextFileWithoutCities() throws IOException {
        String inputWithNoCities = "some text with no cities!@#";
        Reader inputReader = new StringReader(inputWithNoCities);
        Writer outputWriter = new StringWriter();

        tagger.tagCities(inputReader, outputWriter);

        assertEquals(inputWithNoCities, outputWriter.toString(),
                "Text with no cities should remain the same after tagging");
        inputReader.close();
        outputWriter.close();
    }

    @Test
    public void testTagCitiesTextWithOneCityNoPunctuation() throws IOException {
        final String input = "Sofia is in Bulgaria";
        final String expected = "<city country=\"Bulgaria\">Sofia</city> is in Bulgaria";

        Reader reader = new StringReader(input);
        Writer writer = new StringWriter();

        tagger.tagCities(reader, writer);

        assertEquals(expected, writer.toString(), "tagger does not work");
        reader.close();
        writer.close();
    }

    @Test
    public void testTagCitiesTextWithOneCityWithPunctuation() throws IOException {
        final String input = "Hello ,Sofia";
        final String expected = "Hello ,<city country=\"Bulgaria\">Sofia</city>";

        Reader reader = new StringReader(input);
        Writer writer = new StringWriter();

        tagger.tagCities(reader, writer);

        assertEquals(expected, writer.toString(), "tagger does not work with one city with punctuation");
        reader.close();
        writer.close();
    }

    @Test
    public void testTagCitiesTextWithMultipleLine() throws IOException {
        final String input = String.format("Hello Sofia%nHello Dubai");
        final String expected = String.format("Hello <city country=\"Bulgaria\">Sofia</city>%n" +
                "Hello <city country=\"United Arab Emirates\">Dubai</city>");

        Reader reader = new StringReader(input);
        Writer writer = new StringWriter();

        tagger.tagCities(reader, writer);
        assertEquals(expected, writer.toString(), "tagger does not work with two cities with new lines");
        reader.close();
        writer.close();
    }

    @Test
    public void testTagCitiesTextWithOneCityCapitalAndLowerCaseLetters() throws IOException {
        final String input = "sOfIa is in Bulgaria";
        final String expected = "<city country=\"Bulgaria\">Sofia</city> is in Bulgaria";

        Reader reader = new StringReader(input);
        Writer writer = new StringWriter();

        tagger.tagCities(reader, writer);

        assertEquals(expected, writer.toString(),
                "tagger does not work when city is with both upper and lower case letters");
        reader.close();
        writer.close();
    }

    @Test
    public void testGetNMostTaggedCities() throws IOException {
        final String input = "hello soFia, london SOfia";
        final List<String> expected = Collections.singletonList("Sofia");

        Reader inputReader = new StringReader(input);
        Writer outputWriter = new StringWriter();

        tagger.tagCities(inputReader, outputWriter);
        List<String> actual = new ArrayList<>(tagger.getNMostTaggedCities(1));

        assertIterableEquals(expected, actual, "Tagged cities are not sorted correctly");
        inputReader.close();
        outputWriter.close();
    }

    @Test
    public void testGetNMostTaggedCitiesAllCities() throws IOException {
        final String input = "hello sofia barcelona";
        final List<String> expected = Arrays.asList("Barcelona", "Sofia");

        Reader reader = new StringReader(input);
        Writer writer = new StringWriter();

        tagger.tagCities(reader, writer);
        List<String> actual = new ArrayList<>(tagger.getAllTaggedCities());

        assertEquals(expected, actual, "getMostTaggedCities does not work");
        reader.close();
        writer.close();
    }

    @Test
    public void testGetAllTagsCount() throws IOException {
        final String input = String.format("hello, world! ,Sofia! is a city " +
                "in Bulgaria%none more city: Vienna%none more city: London");
        final long expected = 3;

        Reader inputReader = new StringReader(input);
        Writer outputWriter = new StringWriter();

        tagger.tagCities(inputReader, outputWriter);
        long actual = tagger.getAllTagsCount();

        assertEquals(expected, actual);
        inputReader.close();
        outputWriter.close();
    }
}
