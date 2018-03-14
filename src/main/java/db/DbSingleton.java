package db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public final class DbSingleton {
    private static volatile DbSingleton instance = null;
    private static MongoClient mClient;
    private static final String DbName = "riw";
    private static final String UserCollection = "user";

    private MongoClient getMongoClient() {
        if (mClient == null) {
            mClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        }
        return mClient;
    }
    // Utility method to get database instance
    private MongoDatabase getDB() {
        return getMongoClient().getDatabase(DbName);
    }

    public MongoCollection<Document> getUserCollection() {
        return getDB().getCollection(UserCollection);
    }

    private DbSingleton() {
        getMongoClient();
    }

    public static DbSingleton getInstance() {
        if (instance == null) {
            synchronized(DbSingleton.class) {
                if (instance == null) {
                    instance = new DbSingleton();
                }
            }
        }
        return instance;
    }
}