package directindexreducer;

import kafka.KafkaConstants;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

public class DirectIndexConsumer {
    public static KafkaConsumer<String, String> createConsumer(String brokerIP) {
        Properties properties = new Properties();

        properties.setProperty("bootstrap.servers", brokerIP);
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());

        properties.setProperty("group.id", "test");
        properties.setProperty("enable.auto.commit", "false");
//        properties.setProperty("auto.commit.interval.ms", "1000");
        properties.setProperty("auto.offset.reset", "earliest");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(KafkaConstants.TOPIC_DIRECT_INDEX));

        return kafkaConsumer;
    }
}
