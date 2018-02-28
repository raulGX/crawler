package tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DirectoryParser {
    private List<Path> files = null;

    public DirectoryParser() {
        files = new ArrayList<>();
    }

    public List<Path> getFiles(Path dirname) {
        addFilesToList(dirname);
        return files;
    }

    private void addFilesToList(Path dirname) {
        List<Path> filesInDirectory;
        try { //add files to main list
            filesInDirectory = Files.walk(Paths.get(dirname.toString()))
                .filter(Files::isRegularFile)
                .map(Path::toAbsolutePath)
                .collect(Collectors.toList());
            files.addAll(filesInDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
