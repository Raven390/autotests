package utils.kafka;

import static utils.ConfigFactory.KAFKA_HOST;
import static utils.ConfigFactory.KAFKA_PORT;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaMessageConsumerHelper {

  public static void consumeMessages(String topic) {
    // Set up the consumer properties
    Properties properties = new Properties();
    properties.put(
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_HOST + ":" + KAFKA_PORT); // Kafka broker URL
    properties.put(
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.put(
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // Create a new Kafka consumer
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

    // Subscribe to the topic
    consumer.subscribe(Collections.singletonList(topic));

    // Infinite loop to continuously listen for new messages
    try {
      while (true) {
        // Poll the Kafka broker for new records
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

        // Process each record
        for (ConsumerRecord<String, String> record : records) {
          System.out.printf(
              "Consumed message: key = %s, value = %s, partition = %d, offset = %d%n",
              record.key(), record.value(), record.partition(), record.offset());
        }
      }
    } finally {
      consumer.close();
    }
  }
}
