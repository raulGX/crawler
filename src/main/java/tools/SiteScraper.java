package tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SiteScraper {
    private Document doc = null;

    public SiteScraper(String url) {
        try {
            doc = Jsoup.connect(url).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public SiteScraper(String body, boolean fromString) {
        try {
            doc = Jsoup.parse(body);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Elements getLinks() {
        if (doc == null) {
            System.out.println("Document is null");
            return new Elements();
        }

        Elements links = doc.select("a[href]");
        links.removeIf(item -> {
            String href = item.attr("href");
            return href.length() != 0 && href.contains("#");
        } );
        return links;
    }

    public String getText() {
        if (doc == null) {
            System.out.println("Document is null");
            return "";
        }

        return doc.body().text();
    }


    public String getTitle() {
        return doc.select("title").text();
    }

    public String getKeywords() {
        return doc.select("meta[name=\"keywords\"]").text();
    }

    public String getRobots() {
        if (doc == null) {
            return null;
        }
        return doc.select("meta[name=\"robots\"]").attr("content");
    }

    public String getDescription() {
        return doc.select("meta[name=\"description\"]").attr("content");
    }
}
