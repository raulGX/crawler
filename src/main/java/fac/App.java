package fac;

import tools.DirectoryParser;
import tools.SiteScraper;
import tools.WordParser;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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
        filesDirectIndexes.forEach((filename, value) -> {
            try {
                FileWriter fw = new FileWriter(directIndexFileName, true);
                value.forEach((word, count) -> {
                    try {
                        fw.write(filename + " : " + word + "(" + count + ");");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
