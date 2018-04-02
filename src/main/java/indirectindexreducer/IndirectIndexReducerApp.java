package indirectindexreducer;

import com.mongodb.client.MongoCollection;
import db.DbSingleton;
import db.DirectIndexCollectionBridge;
import db.IndirectIndexCollectionBridge;
import directindexreducer.DirectIndexConsumer;
import directindexreducer.DirectIndexSplitProducer;
import kafka.KafkaConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.bson.Document;

import java.util.Map;
import java.util.Set;

public class IndirectIndexReducerApp {
    public static void main(String[] args) {
        String brokerIp = "127.0.0.1:9092"; //todo replace this with ip from args
        MongoCollection<Document> indirectIndexCollection = DbSingleton.getInstance().getIndirectIndexCollection();
        KafkaConsumer<String, String> kafkaConsumer = DirectIndexSplitConsumer.createConsumer(brokerIp);
        try {
            while(true) {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    try{
                        Document doc = Document.parse(consumerRecord.value());
                        IndirectIndexCollectionBridge.addWordToIndexer(doc, indirectIndexCollection);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                kafkaConsumer.commitSync();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            kafkaConsumer.close();
        }
    }
}
