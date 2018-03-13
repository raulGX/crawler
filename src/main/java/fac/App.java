package fac;

import tools.BoolSearch;
import tools.DirectoryParser;
import tools.SiteScraper;
import tools.WordParser;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<Path> paths = new DirectoryParser().getFiles(Paths.get("testfolder"));
        List<String> stopwords = WordParser.getStopwords("stopwords.txt");
        HashMap<String, HashMap<String, Integer>> filesDirectIndexes = new HashMap<>();
        //^^^^ "filename": <"word": count>

        paths.stream()
            .forEach(path -> { //reads files + adds to filesDirectIndexes
                String fileName = path.toAbsolutePath().toString();
                WordParser wp = new WordParser(stopwords);
                wp.readFromFile(fileName);
                filesDirectIndexes.put(fileName, wp.getWordMap());
            });

        System.out.println(filesDirectIndexes.size());

        String directIndexFileName = "a1.txt";
        HashMap<String, HashMap<String, Integer>> indirectIndex = new HashMap<>();
        filesDirectIndexes.forEach((filename, value) -> {
            try {//write filename : word(count);
                FileWriter fw = new FileWriter(directIndexFileName, true);
                value.forEach((word, count) -> {

                    if (!indirectIndex.containsKey(word)) {
                        indirectIndex.put(word, new HashMap<>());
                    }

                    indirectIndex.get(word).put(filename, count);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        String searchString = "ana or mara and mere not pere";
        BoolSearch bs = new BoolSearch(indirectIndex);
        bs.boolSearch(searchString);

    }
}
