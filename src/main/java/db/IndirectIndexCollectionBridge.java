package db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
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

            Document toInsert = new Document();
            List<Document> listOfApperances = DbUtils.fromMapToList(indirectIndex.get(key));
            Document query = new Document(DocumentKey, key);

            Document updateOnInsert = new Document(DocumentValue,
                    new Document("$each", listOfApperances)
            );
            Document update = new Document("$push", updateOnInsert);

            FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
            options.returnDocument(ReturnDocument.AFTER);
            options.upsert(true);

            indirectIndexCollection.findOneAndUpdate(query, update, options);
        }

    }
}
