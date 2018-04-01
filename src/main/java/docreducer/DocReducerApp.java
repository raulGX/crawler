package docreducer;

import docdispatcher.DocumentProducer;
import kafka.KafkaConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.bson.BasicBSONObject;
import org.bson.Document;
import tools.WordParser;

public class DocReducerApp {
    public static void  main(String[] args) {
        String brokerIp = "127.0.0.1:9092"; //todo replace this with ip from args

        KafkaConsumer<String, String> kafkaConsumer = DocumentConsumer.createConsumer(brokerIp);
        Producer<String, String> kafkaProducer = DirectIndexProducer.createProducer(brokerIp);
        try {
            while(true) {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
//                consumerRecord.value();
//                consumerRecord.key();
//                consumerRecord.offset();
//                consumerRecord.partition();
//                consumerRecord.topic();
//                consumerRecord.timestamp();
                    String docname = consumerRecord.value();
                    WordParser wp = new WordParser();
                    wp.readFromFile(docname);
                    Document doc = new Document();
                    doc.append("map", wp.getWordMap());
                    doc.append("document", docname);
                    ProducerRecord<String, String> item = new ProducerRecord<>(KafkaConstants.TOPIC_DIRECT_INDEX, doc.toJson());
                    kafkaProducer.send(item);
                    System.out.println("sent: " + docname);
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
