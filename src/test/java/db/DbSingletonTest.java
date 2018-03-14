package db;

import com.mongodb.client.MongoCollection;
import junit.framework.TestCase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class DbSingletonTest extends TestCase {
    public void testDb() {
        MongoCollection<Document> userCollection = DbSingleton.getInstance().getUserCollection();
        Document doc = new Document("name", "testuser")
                .append("age", 12);

        userCollection.insertOne(doc);
        Document myDoc = userCollection.find().first();
        assertNotNull(myDoc);

        userCollection.deleteOne(eq("name", "testuser"));

        Document myNullDoc = userCollection.find().first();
        assertNull(myNullDoc);
    }

    //todo implement multithreaded work with db test
}