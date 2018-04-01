package db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public final class DbSingleton {
    private static volatile DbSingleton instance = null;
    private static volatile MongoClient mClient;
    private static final String DbName = "riw";
    private static final String UserCollection = "user";
    private static final String DirectIndexCollection = "directindex";
    private static final String IndirectIndexCollection = "indirectindex";
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

    public MongoCollection<Document> getDirectIndexCollection() {
        return getDB().getCollection(DirectIndexCollection);
    }

    public MongoCollection<Document> getIndirectIndexCollection() {
        return getDB().getCollection(IndirectIndexCollection);
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