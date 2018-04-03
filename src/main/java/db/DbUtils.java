package db;

import org.bson.Document;

import java.util.*;

public class DbUtils {
    public static final String DocumentPath = "path";
    public static final String DocumentAppearances = "no";
    public static List<Document> fromMapToList(HashMap<String, Float> documents) {
        List<Document> retList = new ArrayList<>();
        Iterator<Map.Entry<String, Float>> iterator = documents.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, Float> next = iterator.next();
            Document toInsert = new Document();
            toInsert.append(DocumentAppearances, next.getValue());
            toInsert.append(DocumentPath, next.getKey());
            retList.add(toInsert);
        }
        return retList;
    }
}
