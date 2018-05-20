package crawler;

import org.jsoup.Jsoup;
import request.Request;
import tools.ElementTools;
import tools.SiteScraper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class App {
    public static void main(String[] args) {
        int counter = 0;
        int maxCounter = 2000;
        String workdir = "/Users/raulpopovici/Desktop/facultate/riw/labprb/crawler/workdir";
        String startUrl = "http://romaniatourism.com/";
//        String startUrl = "https://medium.com";
        WebCache cache = new WebCache();
        HashSet<String> targets = new LinkedHashSet<String>(); // use hashset or smth
        targets.add(startUrl);
        long startTime = System.currentTimeMillis();
        while (counter < maxCounter && targets.size() > 0) {
            String url = targets.iterator().next();
            targets.remove(url);
            String hostUrl = null;
            try {
                hostUrl = Request.getHostFromUrl(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.out.println("malformed url " + url);
                continue;
            }
            String path = url.replaceAll("(http|https)?:\\/\\/", "").replaceAll("\\?.*$", "");
            if (path.endsWith("/") || !path.endsWith(".(html|php)")) {
                path = path + "/index.html";
            }
            File f = new File(workdir, path);
            if (f.exists())
                continue;
            Domain currentDomain = cache.getDomainObject(url);

            if (currentDomain == null) {
                boolean allowRobots = RepEnforcer.allowDomainRobots(url);
                String ip = null;
                try {
                    ip = java.net.InetAddress.getByName(hostUrl).getHostAddress();
                } catch (UnknownHostException e) {
                    System.out.println("DNS error: could not fetch ip");
                    e.printStackTrace();
                }
                currentDomain = new Domain(ip, 0, allowRobots); // replace url with ip
                cache.addDomain(currentDomain, url);
            }

            if (!currentDomain.isAllowRobots()) {
                continue;
            }
            Request req = new Request();
            String body = null;
            try {
                body = req.httpRequestFromIp(hostUrl, Request.getPathFromUrl(url), 80, currentDomain.getIp());
            } catch (Exception e) {
                System.out.println("error at host: " + url);
            }
            if (body == null) {
                continue;
            }
            SiteScraper sc = new SiteScraper(body, true);
            if (!RepEnforcer.allowPageRobots(sc.getRobots())) {
                continue;
            }

            String[] links = sc.getLinks()
                    .stream()
                    .map(ElementTools::getAbsoluteHref)
                    .filter(link -> link.length() > 0 && !link.contains("https") && !link.contains("mail"))
                    .toArray(size -> new String[size]);
            targets.addAll(Arrays.asList(links));
            System.out.println(url);
            try{
                f.getParentFile().mkdirs();
                f.createNewFile();
                Files.write(f.toPath(), sc.getText().getBytes());
            } catch (Exception e) {
                System.out.println("can't create " + f.getPath());
            }
            counter++;
        }
        long time = (new Date()).getTime() - startTime;
        System.out.println(counter);
        System.out.println(time);
    }
}
