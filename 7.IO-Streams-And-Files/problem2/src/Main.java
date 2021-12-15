import bg.sofia.uni.fmi.mjt.tagger.Tagger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

public class Main {

    public static void main(String[] args) throws IOException {
        Reader reader = new FileReader("world-cities.csv");
        Tagger tagger = new Tagger(reader);

        String text = "Plovdiv's old town is a major tourist attraction. It is the second largest city\n" +
                "in Bulgaria, after the capital ,Sofia.\n";
        Reader reader1 = new StringReader(text);
        Writer writer = new FileWriter("result.txt");
        tagger.tagCities(reader1, writer);

        reader.close();
        reader1.close();
        writer.close();
    }
}
