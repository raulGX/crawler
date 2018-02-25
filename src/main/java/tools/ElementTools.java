package tools;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ElementTools {
    public static String getAbsoluteHref(Element link) {
        return link.attr("abs:href");
    }
    public static void printAllLinks(Elements links) {
        for (Element link : links ) {
            System.out.println(ElementTools.getAbsoluteHref(link));
        }
    }
}
