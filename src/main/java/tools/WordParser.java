package tools;

import opennlp.tools.stemmer.PorterStemmer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class WordParser {
    private HashMap<String, Float> wordMap;
    private List<String> stopwords = null;
    public static List<String> exceptions = Arrays.asList("SW", "GoT", "fara");
    public static final String wordSeparators = "[.,\\$:;()?!\"\\s]+";
    // mongod illegal keyNames /\. "$
    public static List<String> boolWords = Arrays.asList("and", "or", "not");
    private long wordsCount = 0;
    public WordParser() {
        wordMap = new HashMap<>();
        this.stopwords = WordParser.getStopwords("stopwords.txt");
    }

    public void readFromFile(Path filePath) {
        readFromFile(filePath.toString());
    }

    public void readFromFile(String filePath) {
        wordMap = new HashMap<>();
        wordsCount = 0;
        File file = new File(filePath);
        Scanner input = null; //remove filepath string with actual file
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file);
        }
        input.useDelimiter(wordSeparators);
        PorterStemmer stemmer = new PorterStemmer();
        while (input.hasNext()) {
            String word = input.next().toLowerCase();
            if (exceptions.contains(word)) {
                wordsCount++;
                //adds to wordMap and skips over stopwords
                if (wordMap.containsKey(word)) {
                    float count = wordMap.get(word);
                    wordMap.put(word, count+1);
                } else {
                    wordMap.put(word, 1.0f);
                }
            }
            else if (!stopwords.contains(word)) {//word = fc(word)
                wordsCount++;
                word = stemmer.stem(word);
                if (wordMap.containsKey(word)) {
                    float count = wordMap.get(word);
                    wordMap.put(word, count+1);
                } else {
                    wordMap.put(word, 1.0f);
                }
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

    public HashMap<String, Float> getWordMap() {
        Iterator<String> it = wordMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            float count = wordMap.get(key);
            wordMap.put(key, count/wordsCount);
        }
        return wordMap;
    }

    public static List<String> getWordsForBoolSearch(String s, boolean isBoolWord) {
        String[] words = s.split(" ");
        List<String> retList = new ArrayList<>();
        for (String word : words) {
            if (isBoolWord) {
                if (boolWords.contains(word)) {
                    retList.add(word);
                }
            } else {
                if (!boolWords.contains(word)) {
                    retList.add(word);
                }
            }
        }
        return retList;
    }
}
