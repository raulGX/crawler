package crawler;

import tools.ElementTools;
import tools.SiteScraper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class App {
    public static void main(String[] args) {
        int counter = 0;
        int maxCounter = 100;
        String workdir = "/Users/raulpopovici/Desktop/facultate/riw/labprb/crawler/workdir";
        String startUrl = "http://riweb.tibeica.com/crawl/front.html";

        Queue<String> targets = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        targets.add(startUrl);
        while (counter < maxCounter && targets.size() > 0) {
            String url = targets.poll();
            if (visited.contains(url)) {
                continue;
            }
            visited.add(url);
            SiteScraper sc = new SiteScraper(url);
            String[] links = sc.getLinks()
                    .stream()
                    .map(ElementTools::getAbsoluteHref)
                    .toArray(size -> new String[size]);
            targets.addAll(Arrays.asList(links));
            url = url.substring(url.indexOf("//") + 2);
            if (url.endsWith("/")) {
                url = url + "index.html";
            }
            File f = new File(workdir, url);
            try{
                System.out.println(f.toPath().toString());
                f.getParentFile().mkdirs();
                f.createNewFile();
                Files.write(f.toPath(), sc.getText().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            counter++;
        }
    }
}
