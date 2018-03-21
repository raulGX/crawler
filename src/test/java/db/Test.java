package db;

import com.mongodb.client.MongoCollection;
import junit.framework.TestCase;
import org.bson.Document;

public class Test extends TestCase {
    public void testIndex() {
        MongoCollection<Document> userCollection = DbSingleton.getInstance().getUserCollection();
        Document doc = new Document().append("_id", "wworks");
        userCollection.insertOne(doc);
    }
}
