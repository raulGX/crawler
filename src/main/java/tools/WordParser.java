package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WordParser {
    private HashMap<String, Integer> wordMap;
    private List<String> stopwords = null;
    public static List<String> exceptions = Arrays.asList("SW", "GoT", "fara");
    public static final String wordSeparators = "[.,:;()?!\"\\s]+";
    public WordParser(List<String> stopwords) {
        wordMap = new HashMap<>();
        this.stopwords = stopwords;
    }

    public void readFromFile(Path filePath) {
        readFromFile(filePath.toString());
    }

    public void readFromFile(String filePath) {
        File file = new File(filePath);
        Scanner input = null; //remove filepath string with actual file
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file);
        }
        input.useDelimiter(wordSeparators);
        while (input.hasNext()) {
            String word = input.next();
            if (exceptions != null && exceptions.contains(word)) {
                //adds to wordMap and skips over stopwords
            }
            else if (stopwords != null && stopwords.contains(word)) {
                continue;
            }
            if (wordMap.containsKey(word)) {
                int count = wordMap.get(word);
                wordMap.put(word, count+1);
            } else {
                wordMap.put(word, 1);
            }
        }
    }

    public static List<String> getStopwords(String pathToStopwords) {
        try {
            return Files.lines(Paths.get(pathToStopwords))
                    .map(Object::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashMap<String, Integer> getWordMap() {
        return wordMap;
    }
}
