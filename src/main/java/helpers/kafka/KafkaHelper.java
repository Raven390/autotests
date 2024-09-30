package helpers.kafka;

import static utils.ConfigFactory.*;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Future;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaHelper {

    public static Properties getKafkaProducerProperties() {
        Properties properties = new Properties();
        if ("GITLAB_CI".equals(System.getenv("RUNNER"))) {
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_PRIVATE);
            properties.put(
                    "sasl.jaas.config",
                    "org.apache.kafka.common.security.scram.ScramLoginModule required username='kafkaclient' password='"
                            + System.getenv("KAFKA_PASSWORD") + "';");
        } else {
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_PUBLIC);
            properties.put(
                    "sasl.jaas.config",
                    "org.apache.kafka.common.security.scram.ScramLoginModule required username='kafkaclient' password='"
                            + KAFKA_PASSWORD + "';");
        }
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "SCRAM-SHA-512");
        return properties;
    }

    public static Properties getKafkaConsumerProperties() {
        Properties properties = new Properties();
        if ("GITLAB_CI".equals(System.getenv("RUNNER"))) {
            // Private Kafka setup for CI environment
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_PRIVATE);
            properties.put(
                    "sasl.jaas.config",
                    "org.apache.kafka.common.security.scram.ScramLoginModule required username='kafkaclient' password='"
                            + System.getenv("KAFKA_PASSWORD") + "';");
        } else {
            // Public Kafka setup for local environment
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_PUBLIC);
            properties.put(
                    "sasl.jaas.config",
                    "org.apache.kafka.common.security.scram.ScramLoginModule required username='kafkaclient' password='"
                            + KAFKA_PASSWORD + "';");
        }

        // Deserializers for key and value
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Kafka Consumer Group ID
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "coretest");

        // Auto-offset configuration: read from the earliest offset if no previous offset is found
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Security configurations
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "SCRAM-SHA-512");

        return properties;
    }

    public ConsumerRecord<String, String> consumeMessages(String topic) {
        ConsumerRecords<String, String> records;
        // Set up the consumer properties and create a new Kafka consumer
        Properties properties = getKafkaConsumerProperties();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe to the topic
        consumer.subscribe(Collections.singletonList(topic));

        try {
            while (true) {
                // Poll the Kafka broker for new records
                records = consumer.poll(Duration.ofMillis(500));

                // Process each record
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf(
                            "Consumed message from %s: key = %s, value = %s, partition = %d, offset = %d%n",
                            topic, record.key(), record.value(), record.partition(), record.offset());
                    return record;
                }
            }
        } finally {
            consumer.close();
        }
    }

    public RecordMetadata produceMessage(String key, String message, String topic) {
        RecordMetadata metadata = null;
        // Set producer properties and create a new Kafka producer
        Properties properties = getKafkaProducerProperties();
        System.out.println(properties.get(""));
        KafkaProducer producer = new KafkaProducer<>(properties);

        // Create a producer record
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);

        try {
            // Send the record and get the metadata about the sent record
            Future<RecordMetadata> future = producer.send(record);
            metadata = future.get();
            System.out.printf(
                    "Sent message to topic:%s partition:%d offset:%d%n",
                    metadata.topic(), metadata.partition(), metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
        return metadata;
    }
}
