package directindexreducer;

import db.DbSingleton;
import db.DbUtils;
import db.DirectIndexCollectionBridge;
import db.IndirectIndexCollectionBridge;
import docreducer.DirectIndexProducer;
import docreducer.DocumentConsumer;
import kafka.KafkaConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.bson.Document;
import tools.WordParser;

import java.util.Map;
import java.util.Set;

public class DirectIndexReducerApp {
    public static void main(String[] args) {
        String brokerIp = "127.0.0.1:9092"; //todo replace this with ip from args

        KafkaConsumer<String, String> kafkaConsumer = DirectIndexConsumer.createConsumer(brokerIp);
        Producer<String, String> kafkaProducer = DirectIndexSplitProducer.createProducer(brokerIp);
        try {
            while(true) {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    try{
                        Document doc = Document.parse(consumerRecord.value());
//                        ProducerRecord<String, String> item = new ProducerRecord<>(KafkaConstants.TOPIC_DIRECT_INDEX, "");
//                        kafkaProducer.send(item);
                        Document wordMap = (Document) doc.get("map");
                        String docName = (String) doc.get(DbUtils.DocumentPath);
                        DirectIndexCollectionBridge.addDirectIndex(docName, wordMap);
                        //for every key in map write { doc, word, no,}
                        Set<Map.Entry<String, Object>> entries = wordMap.entrySet();
                        for (Map.Entry<String, Object> entry : entries) {
                            String word = entry.getKey();
                            Object value = entry.getValue();

                            Document toSend = new Document();
                            toSend.append("word", word);
                            toSend.append(DbUtils.DocumentPath, docName);
                            toSend.append(DbUtils.DocumentAppearances, value);
                            ProducerRecord<String, String> item = new ProducerRecord<>(KafkaConstants.TOPIC_DIRECT_INDEX_SPLIT, toSend.toJson());
                            kafkaProducer.send(item);
                        }
                        kafkaProducer.flush();
                        System.out.println("Wrote to db and splitted " + docName);
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
            kafkaProducer.close();
        }
    }
}
