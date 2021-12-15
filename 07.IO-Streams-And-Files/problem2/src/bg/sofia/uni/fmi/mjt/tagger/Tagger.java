package bg.sofia.uni.fmi.mjt.tagger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tagger {

    private final Map<String, String> cities;
    private final Map<String, Integer> taggedCities;
    private long taggedCitiesCount;

    /**
     * Creates a new instance of Tagger for a given list of city/country pairs
     *
     * @param citiesReader a java.io.Reader input stream containing list of cities and countries
     *                     in the specified CSV format
     */
    public Tagger(Reader citiesReader) throws IOException {
        cities = new HashMap<>();
        taggedCities = new HashMap<>();
        taggedCitiesCount = 0;
        loadCities(citiesReader);
    }

    private void loadCities(Reader citiesReader) throws IOException {
        BufferedReader reader = new BufferedReader(citiesReader);

        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(",");
            String citiNameLowerCase = tokens[0].toLowerCase();
            String cityNameLowerCaseCapitalFirst = citiNameLowerCase.substring(0, 1).toUpperCase()
                    + citiNameLowerCase.substring(1).toLowerCase();

            cities.put(cityNameLowerCaseCapitalFirst, tokens[1]);
        }
    }

    /**
     * Processes an input stream of a text file, tags any cities and outputs result
     * to a text output stream.
     *
     * @param text   a java.io.Reader input stream containing text to be processed
     * @param output a java.io.Writer output stream containing the result of tagging
     */
    public void tagCities(Reader text, Writer output) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(text);
        BufferedWriter bufferedWriter = new BufferedWriter(output);

        String inputLine;
        boolean isNotFirstLine = false;
        while ((inputLine = bufferedReader.readLine()) != null) {

            if (isNotFirstLine) {
                bufferedWriter.newLine();
            }

            String[] words = inputLine.split("\\s+");
            String outputLine = inputLine;
            for (String word : words) {
                int index = getIndexOfNonLetter(word);
                if (index != -1 && index != 0) {
                    word = word.substring(0, index);
                }
                String strippedWord = word
                        .replaceAll("[^a-zA-Z]+", "")
                        .replaceAll("\\s+", " ")
                        .trim();

                if (!strippedWord.isEmpty()) {
                    String cityNameLowerCase = strippedWord.toLowerCase();
                    String cityNameLowerCaseCapitalFirst = cityNameLowerCase.substring(0, 1).toUpperCase()
                            + cityNameLowerCase.substring(1).toLowerCase();

                    if (cities.containsKey(cityNameLowerCaseCapitalFirst)) {
                        String country = cities.get(cityNameLowerCaseCapitalFirst);
                        String replacement = String.format("<city country=\"%s\">%s</city>", country, cityNameLowerCaseCapitalFirst);
                        outputLine = outputLine.replaceAll(strippedWord, replacement);
                        Integer taggedCityCount = taggedCities.get(cityNameLowerCaseCapitalFirst);
                        taggedCityCount = (taggedCityCount != null) ? taggedCityCount + 1 : 1;
                        taggedCities.put(cityNameLowerCaseCapitalFirst, taggedCityCount);
                        taggedCitiesCount += taggedCityCount;
                    }
                }
            }

            bufferedWriter.write(outputLine);
            bufferedWriter.flush();
            isNotFirstLine = true;

        }
    }

    /**
     * Returns a collection the top @n most tagged cities' unique names
     * from the last tagCities() invocation. Note that if a particular city has been tagged
     * more than once in the text, just one occurrence of its name should appear in the result.
     * If @n exceeds the total number of cities tagged, return as many as available
     * If tagCities() has not been invoked at all, return an empty collection.
     *
     * @param n the maximum number of top tagged cities to return
     * @return a collection the top @n most tagged cities' unique names
     * from the last tagCities() invocation.
     */
    public Collection<String> getNMostTaggedCities(int n) {
        if (n > taggedCities.size()) {
            n = taggedCities.size();
        }

        List<String> result = new ArrayList<>(taggedCities.keySet());
        Collections.sort(result, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Integer count1 = taggedCities.get(o1);
                Integer count2 = taggedCities.get(o2);

                return count2.compareTo(count1);
            }
        });

        return result.subList(0, n);
    }

    /**
     * Returns a collection of all tagged cities' unique names
     * from the last tagCities() invocation. Note that if a particular city has been tagged
     * more than once in the text, just one occurrence of its name should appear in the result.
     * If tagCities() has not been invoked at all, return an empty collection.
     *
     * @return a collection of all tagged cities' unique names
     * from the last tagCities() invocation.
     */
    public Collection<String> getAllTaggedCities() {
        return taggedCities.keySet();
    }

    /**
     * Returns the total number of tagged cities in the input text
     * from the last tagCities() invocation
     * In case a particular city has been taged in several occurences, all must be counted.
     * If tagCities() has not been invoked at all, return 0.
     *
     * @return the total number of tagged cities in the input text
     */
    public long getAllTagsCount() {
        return taggedCitiesCount;
    }

    private int getIndexOfNonLetter(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (!Character.isLetter(word.charAt(i))) {
                return i;
            }
        }

        return -1;
    }
}
