package db;

import org.bson.Document;

import java.util.*;

public class DirectIndexCollectionBridge {
    public static final String DocumentKey = "docName";
    public static final String DocumentValue = "values";

    public static void addDirectIndexes(HashMap<String, HashMap<String, Float>> directIndexes) {
        Iterator<Map.Entry<String, HashMap<String, Float>>> iterator = directIndexes.entrySet().iterator();
        List<Document> documents = new ArrayList<>();

        while (iterator.hasNext()) {
            Map.Entry<String, HashMap<String, Float>> next = iterator.next();
            Document toInsert = new Document()
                    .append(DocumentKey, next.getKey())
                    .append(DocumentValue, next.getValue());
            documents.add(toInsert);
        }

        try {
            DbSingleton.getInstance().getDirectIndexCollection().insertMany(documents);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addDirectIndex(String key, HashMap<String, Float> value) {
        Document toInsert = new Document()
                .append(DocumentKey, key)
                .append(DocumentValue, value);
        try {
            DbSingleton.getInstance().getDirectIndexCollection().insertOne(toInsert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addDirectIndex(String key, Document map) {
        Document toInsert = new Document()
                .append(DocumentKey, key)
                .append(DocumentValue, map);
        try {
            DbSingleton.getInstance().getDirectIndexCollection().insertOne(toInsert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
