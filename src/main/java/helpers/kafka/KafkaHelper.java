package helpers.kafka;

import static utils.ConfigFactory.*;
import static utils.Constants.KAFKA_NO_MESSAGE_FOUND_ERROR;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Future;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
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

    public String consumeMessages(String topic, String id) throws InterruptedException {
        ConsumerRecords<String, String> records;
        Properties properties = getKafkaConsumerProperties();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe to the topic and poll once to assign partitions
        consumer.subscribe(Collections.singletonList(topic));
        consumer.poll(Duration.ofMillis(100)); // Initial poll to get the assignment

        // Get the partitions assigned to the consumer for this topic
        Set<TopicPartition> partitions = consumer.assignment();

        // Get the latest (end) offset for each partition
        Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);

        // Seek to the previous 50 messages for each partition
        for (TopicPartition partition : partitions) {
            long endOffset = endOffsets.get(partition);
            long startOffset = Math.max(0, endOffset - 50);
            consumer.seek(partition, startOffset);
        }

        int maxAttempts = 30;
        int attempts = 0;

        try {
            while (attempts < maxAttempts) {
                // Poll the Kafka broker for new records (with a timeout of 1000 ms)
                records = consumer.poll(Duration.ofMillis(1000));
                attempts++; // Increment the attempt count
                Thread.sleep(500);

                // Process each record
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf(
                            "Consumed message from %s: key = %s, value = %s, partition = %d, offset = %d%n",
                            topic, record.key(), record.value(), record.partition(), record.offset());

                    // If the record contains the specified id, return it
                    if (record.value() != null && record.value().contains(id)) {
                        return record.value();
                    }
                }
            }
            // After maxAttempts, if no matching message is found, return message
            return KAFKA_NO_MESSAGE_FOUND_ERROR;
        } finally {
            consumer.close(); // Ensure the consumer is closed
        }
    }

    public String consumeMessages(String topic, String id, Integer maxAttempts) throws InterruptedException {
        ConsumerRecords<String, String> records;
        Properties properties = getKafkaConsumerProperties();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe to the topic and poll once to assign partitions
        consumer.subscribe(Collections.singletonList(topic));
        consumer.poll(Duration.ofMillis(100)); // Initial poll to get the assignment

        // Get the partitions assigned to the consumer for this topic
        Set<TopicPartition> partitions = consumer.assignment();

        // Get the latest (end) offset for each partition
        Map<TopicPartition, Long> endOffsets = consumer.endOffsets(partitions);

        // Seek to the previous 50 messages for each partition
        for (TopicPartition partition : partitions) {
            long endOffset = endOffsets.get(partition);
            long startOffset = Math.max(0, endOffset - 50);
            consumer.seek(partition, startOffset);
        }

        int attempts = 0;

        try {
            while (attempts < maxAttempts) {
                // Poll the Kafka broker for new records (with a timeout of 1000 ms)
                records = consumer.poll(Duration.ofMillis(1000));
                attempts++; // Increment the attempt count
                Thread.sleep(500);

                // Process each record
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf(
                            "Consumed message from %s: key = %s, value = %s, partition = %d, offset = %d%n",
                            topic, record.key(), record.value(), record.partition(), record.offset());

                    // If the record contains the specified id, return it
                    if (record.value() != null && record.value().contains(id)) {
                        return record.value();
                    }
                }
            }
            // After maxAttempts, if no matching message is found, return message
            return KAFKA_NO_MESSAGE_FOUND_ERROR;
        } finally {
            consumer.close(); // Ensure the consumer is closed
        }
    }

    public MessageWithHeaders consumeMessages(String topic, String id, boolean getHeaders) throws InterruptedException {
        if (!getHeaders) {
            return new MessageWithHeaders(consumeMessages(topic, id), new HashMap<>());
        } else {
            ConsumerRecords<String, String> records;
            Properties properties = getKafkaConsumerProperties();
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

            // Subscribe to the topic
            consumer.subscribe(Collections.singletonList(topic));

            int maxAttempts = 25;
            int attempts = 0;

            try {
                while (attempts < maxAttempts) {
                    // Poll the Kafka broker for new records (with a timeout of 500 ms)
                    records = consumer.poll(Duration.ofMillis(1000));
                    attempts++; // Increment the attempt count

                    // Process each record
                    for (ConsumerRecord<String, String> record : records) {
                        System.out.printf(
                                "Consumed message from %s: key = %s, value = %s, partition = %d, offset = %d%n",
                                topic, record.key(), record.value(), record.partition(), record.offset());

                        // If the record contains the specified id, return it
                        if (record.value() != null && record.value().contains(id)) {
                            Headers headers = record.headers();
                            // Convert headers to a HashMap
                            Map<String, String> headersMap = new HashMap<>();
                            for (Header header : headers) {
                                headersMap.put(header.key(), new String(header.value()));
                            }
                            return new MessageWithHeaders(record.value(), headersMap);
                        }
                    }
                }
                // After X attempts, if no matching message is found, return null
                return new MessageWithHeaders(
                        "Max attempts reached without finding a matching message.", new HashMap<>());
            } finally {
                consumer.close(); // Ensure the consumer is closed
            }
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
                    "Produced message to %s: key = %s, value = %s, partition = %d, offset = %d%n",
                    topic, record.key(), record.value(), record.partition(), metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
        return metadata;
    }

    public MatchResultWithMessage isAnyMatchPresentInMessages(String topic, String... textToSearchList) {
        ConsumerRecords<String, String> records;
        Properties properties = getKafkaConsumerProperties();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe to the topic
        try (consumer) {
            consumer.subscribe(Collections.singletonList(topic));
            int maxAttempts = 25;
            int attempts = 0;
            // Ensure there are no null values in the textToSearchList
            if (textToSearchList == null || textToSearchList.length == 0) {
                return new MatchResultWithMessage(false, "No search parameters provided.");
            }
            while (attempts < maxAttempts) {
                // Poll the Kafka broker for new records (with a timeout of 1000 ms)
                records = consumer.poll(Duration.ofMillis(1000));
                attempts++; // Increment the attempt count
                // If no records are found, continue polling
                if (records.isEmpty()) {
                    continue;
                }
                // Process each record
                for (ConsumerRecord<String, String> record : records) {
                    // Ensure record.value() is not null
                    if (record.value() == null) {
                        continue; // Skip records with null value
                    }
                    // Check if any of the search text is present in the message value
                    for (String text : textToSearchList) {
                        // Ensure text is not null
                        if (text != null && record.value().contains(text)) {
                            return new MatchResultWithMessage(
                                    true,
                                    "Matching Record Found: " + record.value() + ". Based on search with: " + text);
                        }
                    }
                }
            }
            // After max attempts, if no matching message is found, return false
            return new MatchResultWithMessage(false, "Max attempts reached without finding a matching message.");
        }
    }

    public MatchResultWithMessage areAllParamsPresentInMessages(String topic, String... textToSearchList) {
        ConsumerRecords<String, String> records;
        Properties properties = getKafkaConsumerProperties();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe to the topic

        try (consumer) {
            consumer.subscribe(Collections.singletonList(topic));
            int maxAttempts = 25;
            int attempts = 0;
            while (attempts < maxAttempts) {
                // Poll the Kafka broker for new records (with a timeout of 500 ms)
                records = consumer.poll(Duration.ofMillis(1000));
                attempts++; // Increment the attempt count

                // Track which texts are found across all messages
                Set<String> foundTexts = new HashSet<>();

                // Process each record
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf(
                            "Consumed message from %s: key = %s, value = %s, partition = %d, offset = %d%n",
                            topic, record.key(), record.value(), record.partition(), record.offset());
                    // Check if each textToSearch is present in the message value
                    for (String text : textToSearchList) {
                        if (record.value() != null && record.value().contains(text)) {
                            foundTexts.add(text); // Mark this text as found
                        }
                    }
                    // If all texts are found, we can stop searching
                    if (foundTexts.size() == textToSearchList.length) {
                        return new MatchResultWithMessage(true, "All the parameters were found in messages.");
                    }
                }
            }
            // If we exit the loop, it means some parameters were not found
            return new MatchResultWithMessage(false, "Some of the parameters were not found in messages.");
        }
    }
}
