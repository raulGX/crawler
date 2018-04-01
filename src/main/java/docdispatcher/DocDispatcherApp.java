package docdispatcher;

import tools.DirectoryParser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DocDispatcherApp {
    public static void main(String[] args) {
        //initialize stuff
        String brokerIp = "127.0.0.1:9092"; //todo replace this with ip from args
        String line;
        Scanner sc = new Scanner(System.in);
        DocumentProducer producer = new DocumentProducer(brokerIp);
        try {
            while (true) {
                System.out.println("Enter absolute path to directory or press q to exit");
                line = sc.nextLine();
                if (line.equals("q")) {
                    break;
                }
                // /Users/raulpopovici/Desktop/facultate/riw/labprb/crawler/testfolder
                List<Path> paths;
                try{
                    File folder = new File(line);
                    paths = new DirectoryParser().getFiles(folder.toPath());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    paths = new LinkedList<>();
                }
                for (Path path : paths) {
                    producer.publish(path.toString());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            producer.close();
        }
        //todo new thread that runs getDbIndexCount
    }
}
