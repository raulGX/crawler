package db;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class IndirectIndexCollectionBridge {
    public static final String DocumentKey = "_id";
    public static final String DocumentValue = "values";

    public static void addIndirectIndex(HashMap<String, HashMap<String, Integer>> indirectIndex) {
        MongoCollection<Document> indirectIndexCollection = DbSingleton.getInstance().getIndirectIndexCollection();

        Iterator<String> iterator = indirectIndex.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            Document fromDb = indirectIndexCollection.find(eq(DocumentKey, key)).first();

            if (fromDb == null) {
                Document toInsert = new Document();
                toInsert.append(DocumentKey, key);
                List<Document> listOfApperances = DbUtils.fromMapToList(indirectIndex.get(key));
                toInsert.append(DocumentValue, listOfApperances);
                indirectIndexCollection.insertOne(toInsert);
            }
        }

    }
}
