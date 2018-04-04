package db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class IndirectIndexCollectionBridge {
    public static final String DocumentKey = "_id";
    public static final String DocumentValue = "values";

    public static void addIndirectIndex(HashMap<String, HashMap<String, Double>> indirectIndex) {
        MongoCollection<Document> indirectIndexCollection = DbSingleton.getInstance().getIndirectIndexCollection();

        Iterator<String> iterator = indirectIndex.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();

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

    public static void addWordToIndexer(Document wordInfo, MongoCollection<Document> indirectIndexCollection) {
        //    "value": {
        //      "word": "prepare",
        //      "document": "/Users/raulpopovici/Desktop/facultate/riw/labprb/crawler/testfolder/4.txt",
        //      "no": 1
        //    },
        String word = (String) wordInfo.get("word");
        String document = (String) wordInfo.get(DbUtils.DocumentPath);
        Object no = wordInfo.get(DbUtils.DocumentAppearances);
        List<Document> listOfApperances = new LinkedList<>();
        Document appendDoc = new Document()
                .append(DbUtils.DocumentPath, document)
                .append(DbUtils.DocumentAppearances, no);
        listOfApperances.add(appendDoc);
        Document query = new Document(DocumentKey, word);

        Document updateOnInsert = new Document(DocumentValue,
                new Document("$each", listOfApperances)
        );
        Document update = new Document("$push", updateOnInsert);

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        options.upsert(true);

        indirectIndexCollection.findOneAndUpdate(query, update, options);
    }
    public static Document getWord(String word) {
        MongoCollection<Document> indirectIndexCollection = DbSingleton.getInstance().getIndirectIndexCollection();
        return indirectIndexCollection.find(eq("_id", word)).first();
    }
}
