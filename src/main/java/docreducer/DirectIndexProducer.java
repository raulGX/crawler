package docreducer;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.bson.Document;

import java.util.Properties;

public class DirectIndexProducer {
    public static Producer<String, String> createProducer(String brokerIp) {
        Properties properties = new Properties();

        // kafka bootstrap server
        properties.setProperty("bootstrap.servers", brokerIp);
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        // producer acks
        properties.setProperty("acks", "1");
        properties.setProperty("retries", "3");
        properties.setProperty("linger.ms", "1");

        return new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties);
    }

}
