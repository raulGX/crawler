package tools;

import db.DirectIndexCollectionBridge;
import org.bson.Document;
import java.util.Comparator;
import java.util.Collections;

import java.util.*;
import java.util.Map.Entry;

public class VectorSearch {
    public Map<String, Double> search(String searchString) {
        BoolSearch bs = new BoolSearch();
        HashSet<String> documents = bs.boolSearch(searchString);

        HashMap<String,HashMap<String, Double>> indirectIndex = bs.getIndex();
        HashMap<String,Document> directIndexes = new HashMap<>();
        HashMap<String,Double> queryVals = new HashMap<>();
        for(String document : documents) {
            Document currIndex = DirectIndexCollectionBridge.getIndex(document);
            directIndexes.put(document, currIndex);
        }

        int documentCount = documents.size() + 1; //including query

        double queryNorm = 0;
        List<String> queryWords = bs.getWords();
        for (String word : queryWords) {
            double val = 1.0/queryWords.size() * getIdf(word, directIndexes, documentCount);
            queryVals.put(word, val);
            queryNorm += val * val;
        }

        HashMap<String, Double> stringTreeMap = new HashMap<>();
        for(String document : documents) {
//            System.out.println(document);
            Document index = (Document) DirectIndexCollectionBridge.getIndex(document).get(DirectIndexCollectionBridge.DocumentValue);
            directIndexes.put(document, index);
            double docNorm = 0;
            Iterator<Entry<String, Object>> iterator = index.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> next = iterator.next();
                String word = next.getKey();
                double tf = (Double)next.getValue();
                double idf = getIdf(word, directIndexes, documentCount);
                double val = tf * idf;
                next.setValue(val);
                docNorm += val * val;
            }

            // calc cos
            double sum = 0;
            for (String queryWord : queryWords) {
                double no = (Double)index.getOrDefault(queryWord, 0.0);
                sum += queryVals.getOrDefault(queryWord, 0.0) * no;
            }
            stringTreeMap.put(document, sum);
        }
        return sortByComparator(stringTreeMap, false);
    }

    private double getIdf(String word, HashMap<String,Document> directIndexes, int documentCount) {
        Iterator<Entry<String, Document>> iterator = directIndexes.entrySet().iterator();
        int count = 1;
        while (iterator.hasNext()) {
            Entry<String, Document> next = iterator.next();
            Document doc = next.getValue();
            if (doc.get("word") != null) {
                count++;
            }
        }
        return Math.log(documentCount / count);
    }

    private static Map<String, Double> sortByComparator(Map<String, Double> unsortMap, final boolean order)
    {

        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Double>>()
        {
            public int compare(Entry<String, Double> o1,
                               Entry<String, Double> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Entry<String, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
}
