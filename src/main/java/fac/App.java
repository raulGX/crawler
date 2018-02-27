package fac;

import tools.DirectoryParser;
import tools.SiteScraper;
import tools.WordParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<Path> paths = new DirectoryParser().getFiles(Paths.get("testfolder"));
        List<String> stopwords = WordParser.getStopwords("stopwords.txt");
        WordParser wp = new WordParser(stopwords);
        paths.forEach(wp::readFromFile);
        System.out.println(stopwords.size());

    }
}
