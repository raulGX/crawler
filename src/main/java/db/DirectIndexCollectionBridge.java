package db;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

public class DirectIndexCollectionBridge {
    public static final String DocumentKey = "docName";
    public static final String DocumentValue = "values";

    public static void addDirectIndexes(HashMap<String, HashMap<String, Double>> directIndexes) {
        Iterator<Map.Entry<String, HashMap<String, Double>>> iterator = directIndexes.entrySet().iterator();
        List<Document> documents = new ArrayList<>();

        while (iterator.hasNext()) {
            Map.Entry<String, HashMap<String, Double>> next = iterator.next();
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

    public static void addDirectIndex(String key, HashMap<String, Double> value) {
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

    public static Document getWord(String word) {
        MongoCollection<Document> collection = DbSingleton.getInstance().getDirectIndexCollection();
        return collection.find(eq(DocumentKey, word)).first();
    }

    public static Document getIndex(String word) {
        MongoCollection<Document> collection = DbSingleton.getInstance().getDirectIndexCollection();
        return collection.find(eq(DocumentKey, word)).first();
    }
}
