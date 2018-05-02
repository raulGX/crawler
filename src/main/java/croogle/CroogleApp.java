package croogle;

import tools.VectorSearch;

import java.util.Iterator;
import java.util.Map;

public class CroogleApp {
    public static void main(String[] args) {

        String searchS = "python and wikipedia";

        Map<String, Double> searchResults = new VectorSearch().search(searchS);
        Iterator<Map.Entry<String, Double>> iterator = searchResults.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> next = iterator.next();
            System.out.println(next.getKey() + " " + next.getValue());
        }
    }
}
