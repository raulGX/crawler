package crawler;

import org.jsoup.Jsoup;

import java.net.URI;
import java.net.URISyntaxException;

public class RepEnforcer {
    public static boolean allowDomainRobots(String url) {
        try {
            String robotsUrl = getDomainName(url) + "/robots.txt"; // replace url with ip
            String text = Jsoup.connect(robotsUrl).get().text();
            if (text.contains("Allow: /"))
                return true;
            return !text.toLowerCase().contains("disallow");
        } catch (Exception e) {
            System.out.println("no robots " + url);
        }
        return true;
    }
    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        return "http://" + uri.getHost();
    }
    public static boolean allowPageRobots(String robotsMeta) {
        if (robotsMeta == null)
            return true;
        if (robotsMeta.toLowerCase().contains("disallow"))
            return false;
        return true;
    }
}
