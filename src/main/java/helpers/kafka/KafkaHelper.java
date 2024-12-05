package helpers.kafka;

import static utils.ConfigFactory.*;
import static utils.Constants.*;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Future;

import io.qameta.allure.Step;
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

    public static final Path filePath = Path.of("src/main/resources/config/consumer-groups");

    public static String getFreeConsumerId() {
        FileLock fileLock = null;
        FileChannel fileChannel = null;
        long startTime = System.currentTimeMillis();
        long timeout = 10_000; // 10 seconds
        boolean isLocked = false;

        try {
            // Open the file channel
            fileChannel = FileChannel.open(filePath, StandardOpenOption.READ, StandardOpenOption.WRITE);

            // Attempt to acquire the lock with retries
            while (!isLocked && (System.currentTimeMillis() - startTime) < timeout) {
                try {
                    fileLock = fileChannel.tryLock();
                    isLocked = (fileLock != null);
                } catch (Exception e) {
                    System.out.println("File is locked by another JVM. Retrying...");
                    Thread.sleep(1000); // Wait 1 second before retrying
                }
            }

            if (!isLocked) {
                System.out.println("Failed to acquire file lock within timeout.");
                return null; // Could not acquire the lock
            }

            // Lock acquired: Process the file
            List<String> lines = Files.readAllLines(filePath);

            // Iterate over each line and check if it's used or not
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                // Check if the line is marked as used
                if (!line.startsWith("Used")) {
                    // Mark this line as used and write it back to the file
                    lines.set(i, "Used - " + line);
                    Files.write(filePath, lines);

                    System.out.println("getFreeConsumerId, consumerId = " + line);
                    System.setProperty("consumerGroup", line);
                    System.out.println("getFreeConsumerId, consumerGroup = " + line);

                    return line; // Return the original line without "Used - "
                }
            }

            // If all lines are marked as used, return null
            return null;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Release the lock before closing the channel
            if (fileLock != null) {
                try {
                    fileLock.release();
                    System.out.println("File lock released.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Close the channel
            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Void cleanConsumerIdAfterUse(String target, String replacement) {
        FileLock fileLock = null;
        FileChannel fileChannel = null;
        long startTime = System.currentTimeMillis();
        long timeout = 10_000; // 10 seconds
        boolean isLocked = false;

        try {
            // Open the file channel
            fileChannel = FileChannel.open(filePath, StandardOpenOption.READ, StandardOpenOption.WRITE);

            // Attempt to acquire the lock with retries
            while (!isLocked && (System.currentTimeMillis() - startTime) < timeout) {
                try {
                    fileLock = fileChannel.tryLock();
                    isLocked = (fileLock != null);
                } catch (Exception e) {
                    System.out.println("File is locked by another JVM. Retrying...");
                    Thread.sleep(1000); // Wait 1 second before retrying
                }
            }

            if (!isLocked) {
                System.out.println("Failed to acquire file lock within timeout.");
                return null; // Could not acquire the lock
            }

            // Lock acquired: Process the file
            List<String> lines = Files.readAllLines(filePath);

            // Prepare the target string to search for
            String targetToReplace = "Used - " + target;
            System.out.println(targetToReplace);

            // Loop through each line and replace if it matches the target string
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).equals(targetToReplace)) {
                    lines.set(i, replacement);
                    System.out.println("cleanConsumerIdAfterUse LINE " + i + " replacement success");
                }
            }

            // Write the modified lines back to the file
            Files.write(filePath, lines);
            System.out.println("File updated successfully.");
            System.out.println(Files.readAllLines(filePath));

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Release the lock before closing the channel
            if (fileLock != null) {
                try {
                    fileLock.release();
                    System.out.println("File lock released.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Close the channel
            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Properties getKafkaProducerProperties() {
        Properties properties = new Properties();
        if ("GITLAB_CI".equals(System.getenv("RUNNER"))) {
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_PRIVATE);
            properties.put(
                    "sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username='kafkaclient' password='" + System.getenv("KAFKA_PASSWORD") + "';");
        } else {
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_PUBLIC);
            properties.put(
                    "sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username='kafkaclient' password='" + KAFKA_PASSWORD + "';");
        }
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "SCRAM-SHA-512");
        return properties;
    }

    public static Properties getKafkaConsumerProperties(String consumerId) {
        Properties properties = new Properties();
        if ("GITLAB_CI".equals(System.getenv("RUNNER"))) {
            // Private Kafka setup for CI environment
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_PRIVATE);
            properties.put(
                    "sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username='kafkaclient' password='" + System.getenv("KAFKA_PASSWORD") + "';");
        } else {
            // Public Kafka setup for local environment
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_PUBLIC);
            properties.put(
                    "sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username='kafkaclient' password='" + KAFKA_PASSWORD + "';");
        }

        // Deserializers for key and value
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // Kafka Consumer Group ID
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerId);

        // Auto-offset configuration: read from the earliest offset if no previous offset is found
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Security configurations
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "SCRAM-SHA-512");

        return properties;
    }

    @Step("Consume message")
    public String consumeMessage(String topic, String id) throws InterruptedException {
        ConsumerRecords<String, String> records;
        String consumerId = getFreeConsumerId();
        Properties properties = getKafkaConsumerProperties(consumerId);
        String consumerGroupId = properties.get(ConsumerConfig.GROUP_ID_CONFIG).toString();
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
                            "Consumed message from %s: key = %s, value = %s, partition = %d, offset = %d%n", topic, record.key(), record.value(), record.partition(), record.offset());

                    // If the record contains the specified id, return it
                    if (record.value() != null && record.value().contains(id)) {
                        cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
                        return record.value();
                    }
                }
            }
            // After maxAttempts, if no matching message is found, return message
            cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
            return KAFKA_NO_MESSAGE_FOUND_ERROR;
        } finally {
            cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
            consumer.close();// Ensure the consumer is closed
        }
    }

    @Step("Consume messages")
    public List<String> consumeMessages(String topic, String id) throws InterruptedException {
        ConsumerRecords<String, String> records;
        List<String> matchingMessages = new ArrayList<>();
        String consumerId = getFreeConsumerId();
        Properties properties = getKafkaConsumerProperties(consumerId);
        String consumerGroupId = properties.get(ConsumerConfig.GROUP_ID_CONFIG).toString();
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
                            "Consumed message from %s: key = %s, value = %s, partition = %d, offset = %d%n", topic, record.key(), record.value(), record.partition(), record.offset());

                    // If the record contains the specified id, add it to the list
                    if (record.value() != null && record.value().contains(id)) {
                        matchingMessages.add(record.value());
                    }
                }

                // Exit early if messages are found
                if (!matchingMessages.isEmpty()) {
                    cleanConsumerIdAfterUse(consumerGroupId, consumerId);
                    return matchingMessages;
                }
            }

            // After maxAttempts, return the list (it might be empty)
            cleanConsumerIdAfterUse(consumerGroupId, consumerId);
            return matchingMessages;
        } finally {
            cleanConsumerIdAfterUse(consumerGroupId, consumerId);
            consumer.close(); // Ensure the consumer is closed
        }
    }

    @Step("Consume message")
    public String consumeMessage(String topic, String id, Integer maxAttempts) throws InterruptedException {
        ConsumerRecords<String, String> records;
        String consumerId = getFreeConsumerId();
        Properties properties = getKafkaConsumerProperties(consumerId);
        String consumerGroupId = properties.get(ConsumerConfig.GROUP_ID_CONFIG).toString();
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
                            "Consumed message from %s: key = %s, value = %s, partition = %d, offset = %d%n", topic, record.key(), record.value(), record.partition(), record.offset());

                    // If the record contains the specified id, return it
                    if (record.value() != null && record.value().contains(id)) {
                        return record.value();
                    }
                }
            }
            // After maxAttempts, if no matching message is found, return message
            cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
            return KAFKA_NO_MESSAGE_FOUND_ERROR;
        } finally {
            cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
            consumer.close(); // Ensure the consumer is closed
        }
    }

    @Step("Consume single message from {topic}")
    public MessageWithHeaders consumeMessage(String topic, String id, boolean getHeaders) throws InterruptedException {
        if (!getHeaders) {
            return new MessageWithHeaders(consumeMessage(topic, id), new HashMap<>());
        } else {
            ConsumerRecords<String, String> records;
            String consumerId = getFreeConsumerId();
            Properties properties = getKafkaConsumerProperties(consumerId);
            String consumerGroupId = properties.get(ConsumerConfig.GROUP_ID_CONFIG).toString();
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

                    // Process each record
                    for (ConsumerRecord<String, String> record : records) {
                        System.out.printf(
                                "Consumed message from %s: key = %s, value = %s, partition = %d, offset = %d%n", topic, record.key(), record.value(), record.partition(), record.offset());

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
                cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
                return new MessageWithHeaders(
                        KAFKA_NO_MESSAGE_FOUND_ERROR, new HashMap<>());
            }
            // Ensure the consumer is closed
            finally {
                cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
                consumer.close();
            }
        }
    }

    @Step("Consume messages from {topic}")
    public Map<String, String> consumeMessages(String topic, String... idList) throws InterruptedException {
        ConsumerRecords<String, String> records;
        String consumerId = getFreeConsumerId();
        Properties properties = getKafkaConsumerProperties(consumerId);
        String consumerGroupId = properties.get(ConsumerConfig.GROUP_ID_CONFIG).toString();
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
        Map<String, String> foundMessages = new HashMap<>(); // Store the messages corresponding to each id

        try {
            while (attempts < maxAttempts && foundMessages.size() < idList.length) {
                // Poll the Kafka broker for new records (with a timeout of 1000 ms)
                records = consumer.poll(Duration.ofMillis(1000));
                attempts++; // Increment the attempt count
                Thread.sleep(500);

                // Process each record
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf(
                            "Consumed message from %s: key = %s, value = %s, partition = %d, offset = %d%n", topic, record.key(), record.value(), record.partition(), record.offset());

                    // Check each id in the idList for a match in the record value
                    for (String id : idList) {
                        if (!foundMessages.containsKey(id) && record.value() != null && record.value().contains(id)) {
                            foundMessages.put(id, record.value()); // Store the found message
                        }
                    }
                }
            }

            // If some ids are not found after maxAttempts, map them to a null
            for (String id : idList) {
                foundMessages.putIfAbsent(id, null);
            }
            cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
            return foundMessages; // Return the map of found messages
        } finally {
            cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
            consumer.close(); // Ensure the consumer is closed
        }
    }

    @Step("Produce {message} to the {topic}")
    public RecordMetadata produceMessage(String key, String message, String topic) {
        RecordMetadata metadata = null;
        // Set producer properties and create a new Kafka producer
        Properties properties = getKafkaProducerProperties();

        // Create a producer record

        try (KafkaProducer<Object, Object> producer = new KafkaProducer<>(properties)) {
            ProducerRecord<Object, Object> record = new ProducerRecord<>(topic, key, message);
            // Send the record and get the metadata about the sent record
            Future<RecordMetadata> future = producer.send(record);
            metadata = future.get();
            System.out.printf(
                    "Produced message to %s: key = %s, value = %s, partition = %d, offset = %d%n", topic, record.key(), record.value(), record.partition(), metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return metadata;
    }

    @Step("Produce multiple {messagesList} to the {topic}")
    public void produceMessages(String key, String topic, String... messagesList) {
        // Set producer properties and create a new Kafka producer
        Properties properties = getKafkaProducerProperties();

        try (KafkaProducer<Object, Object> producer = new KafkaProducer<>(properties)) {
            // Loop through all messages and send each one
            for (String message : messagesList) {
                ProducerRecord<Object, Object> record = new ProducerRecord<>(topic, key, message);
                try {
                    // Send the record and print metadata about the sent record
                    Future<RecordMetadata> future = producer.send(record);
                    RecordMetadata metadata = future.get();
                    System.out.printf(
                            "Produced message to %s: key = %s, value = %s, partition = %d, offset = %d%n", topic, record.key(), record.value(), metadata.partition(), metadata.offset());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Step("Check that {textToSearchList} presented in topic")
    public MatchResultWithMessage isAnyMatchPresentInMessages(String topic, String... textToSearchList) {
        ConsumerRecords<String, String> records;
        String consumerId = getFreeConsumerId();
        Properties properties = getKafkaConsumerProperties(consumerId);
        String consumerGroupId = properties.get(ConsumerConfig.GROUP_ID_CONFIG).toString();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe to the topic
        try (consumer) {
            consumer.subscribe(Collections.singletonList(topic));
            int maxAttempts = 25;
            int attempts = 0;
            // Ensure there are no null values in the textToSearchList
            if (textToSearchList == null || textToSearchList.length == 0) {
                cleanConsumerIdAfterUse(consumerId, consumerId);
                return new MatchResultWithMessage(false, KAFKA_NO_PARAMETERS_PROVIDED);
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
                            cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
                            return new MatchResultWithMessage(
                                    true, "Matching Record Found: " + record.value() + ". Based on search with: " + text);
                        }
                    }
                }
            }
            // After max attempts, if no matching message is found, return false
            cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
            return new MatchResultWithMessage(false, KAFKA_NO_MESSAGE_FOUND_ERROR);
        } finally {
            cleanConsumerIdAfterUse(consumerGroupId, consumerGroupId);
            consumer.close();
        }
    }

    @Step("Check that all params {textToSearchList} presented in topic")
    public MatchResultWithMessage areAllParamsPresentInMessages(String topic, String... textToSearchList) {
        ConsumerRecords<String, String> records;
        String consumerId = getFreeConsumerId();
        Properties properties = getKafkaConsumerProperties(consumerId);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe to the topic

        try (consumer) {
            consumer.subscribe(Collections.singletonList(topic));
            int maxAttempts = 25;
            int attempts = 0;
            // Track which texts are found across all messages
            Set<String> foundTexts = new HashSet<>();

            while (attempts < maxAttempts) {
                // Poll the Kafka broker for new records (with a timeout of 500 ms)
                records = consumer.poll(Duration.ofMillis(1000));
                attempts++; // Increment the attempt count

                // Process each record
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf(
                            "Consumed message from %s: key = %s, value = %s, partition = %d, offset = %d%n", topic, record.key(), record.value(), record.partition(), record.offset());
                    // Check if each textToSearch is present in the message value
                    for (String text : textToSearchList) {
                        if (record.value() != null && record.value().contains(text)) {
                            foundTexts.add(text); // Mark this text as found
                            System.out.println("Text size: " + foundTexts.size());
                            System.out.println("Search length: " + textToSearchList.length);
                        }
                    }
                    // If all texts are found, we can stop searching
                    if (foundTexts.size() == textToSearchList.length) {
                        cleanConsumerIdAfterUse(consumerId, consumerId);
                        return new MatchResultWithMessage(true, KAFKA_ALL_PARAMETERS_FOUND);
                    }
                }
            }
            // If we exit the loop, it means some parameters were not found
            cleanConsumerIdAfterUse(consumerId, consumerId);
            return new MatchResultWithMessage(false, KAFKA_SOME_PARAMETERS_FOUND);
        } finally {
            cleanConsumerIdAfterUse(consumerId, consumerId);
            consumer.close();
        }
    }

    @Step("Get event type from headers")
    public static String getEventTypeFromHeaders(MessageWithHeaders messageWithHeaders) {
        return messageWithHeaders.headers().get("__TypeId__");
    }
}
