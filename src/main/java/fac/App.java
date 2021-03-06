package fac;

import db.DirectIndexCollectionBridge;
import db.IndirectIndexCollectionBridge;
import tools.DirectoryParser;
import tools.WordParser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class App {
    /**
     * Sequential app
     * @param args
     */
    public static void main(String[] args) {
        List<Path> paths = new DirectoryParser().getFiles(Paths.get("testfolder"));
        HashMap<String, HashMap<String, Double>> filesDirectIndexes = new HashMap<>();
        //^^^^ "filename": <"word": count>

        paths.stream().forEach(path -> { //reads files + adds to filesDirectIndexes
            String fileName = path.toAbsolutePath().toString();
            WordParser wp = new WordParser();
            wp.readFromFile(fileName);
            filesDirectIndexes.put(fileName, wp.getWordMap());
        });

        DirectIndexCollectionBridge.addDirectIndexes(filesDirectIndexes);

        HashMap<String, HashMap<String, Double>> indirectIndex = new HashMap<>();
        filesDirectIndexes.forEach((filename, value) -> {
            try {
                value.forEach((word, count) -> {

                    if (!indirectIndex.containsKey(word)) {
                        indirectIndex.put(word, new HashMap<>());
                    }

                    indirectIndex.get(word).put(filename, count);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        System.out.println("Read " + filesDirectIndexes.size() + " files");
        System.out.println("Number of words: " + indirectIndex.size());
        IndirectIndexCollectionBridge.addIndirectIndex(indirectIndex);

    }
}
