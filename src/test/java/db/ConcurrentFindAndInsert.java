package db;

import com.mongodb.client.MongoCollection;
import junit.framework.TestCase;
import org.bson.Document;

public class ConcurrentFindAndInsert extends TestCase {
    public void testIndex() {
        //generate insert object with 2 files in values
        //create 4 threads
        //execute insert 10 times each
        //join threads
        //get item from db
        //expect to have 80 items in values
        //remove from db
    }
}
