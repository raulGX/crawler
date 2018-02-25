package fac;

import org.jsoup.select.Elements;
import tools.ElementTools;
import tools.SiteScraper;


public class App {
    public static void main(String[] args) {
        String url = "https://www.emag.ro";
        SiteScraper emagScraper = new SiteScraper(url);
        Elements links = emagScraper.getLinks();

        ElementTools.printAllLinks(links);

        System.out.println(emagScraper.getText());
    }
}
