package docdispatcher;
import kafka.KafkaConstants;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class DocumentProducer {
    private Producer<String, String> producer;
    public DocumentProducer(String brokerIp) {
        Properties properties = new Properties();

        // kafka bootstrap server
        properties.setProperty("bootstrap.servers", brokerIp);
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        // producer acks
        properties.setProperty("acks", "1");
        properties.setProperty("retries", "3");
        properties.setProperty("linger.ms", "1");

        producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties);
    }
    public void publish(String message) {
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>(KafkaConstants.TOPIC_DOCUMENT_TO_PROCESS, message);
        producer.send(producerRecord);
        producer.flush();
    }
    public void close() {
        producer.close();
    }
}
