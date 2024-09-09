package helpers.kafka;

import static utils.ConfigFactory.*;

import java.util.Properties;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class BOKafkaMessageProducerHelperAlertEvent {

    public void produceMessage(String key, String message, String topic) {
        // Set producer properties
        Properties properties = new Properties();
        if ("GITLAB_CI".equals(System.getenv("RUNNER"))) {
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOKAFKAPRIVATE);
            properties.put(
                    "sasl.jaas.config",
                    "org.apache.kafka.common.security.scram.ScramLoginModule required username='kafkaclient' password='"
                            + System.getenv("BOKAFKAPASS") + "';");
        } else {
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOKAFKAPUBLIC);
            properties.put(
                    "sasl.jaas.config",
                    "org.apache.kafka.common.security.scram.ScramLoginModule required username='kafkaclient' password='"
                            + BOKAFKAPASS + "';");
        }
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put("security.protocol", "SASL_SSL");
        properties.put("sasl.mechanism", "SCRAM-SHA-512");
        //        properties.put("ssl.truststore.location", "/truststore/kafka.client.truststore.jks");
        //        properties.put("ssl.endpoint.identification.algorithm","");

        KafkaProducer producer = new KafkaProducer<>(properties);

        // Create a producer record
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);

        try {
            // Send the record and get the metadata about the sent record
            Future<RecordMetadata> future = producer.send(record);
            RecordMetadata metadata = future.get();
            System.out.printf(
                    "Sent message to topic:%s partition:%d offset:%d%n",
                    metadata.topic(), metadata.partition(), metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
