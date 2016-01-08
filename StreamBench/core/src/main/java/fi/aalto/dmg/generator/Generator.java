package fi.aalto.dmg.generator;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 * Created by jun on 08/01/16.
 */
public class Generator {
    public static KafkaProducer<String, String> createProducer(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.RETRIES_CONFIG, "3");
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "100");
        props.put(ProducerConfig.ACKS_CONFIG, "0"); // "all"
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");

        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 67108864);
        props.put(ProducerConfig.SEND_BUFFER_CONFIG, 67108864);
        props.put(ProducerConfig.RECEIVE_BUFFER_CONFIG, 67108864);

        props.put(ProducerConfig.TIMEOUT_CONFIG, 250);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 0);

        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "5000000");
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "128");


        props.put(ProducerConfig.BLOCK_ON_BUFFER_FULL_CONFIG, true);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        return producer;
    }
}
