import static utils.Constants.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.messages.triggers.WithdrawalEvent;
import java.io.IOException;
import java.util.Date;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// JUST FOR DEMO PURPOSES
// TODO REMOVE WHOLE CLASS AFTER IMPLEMENTING FIRST TEST USING KAFKA
public class KafkaDemo {

    @Test
    void testProduceAndReadMessage() throws IOException {
        // Prepare producer and consumer
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaHelper kafka = new KafkaHelper();

        // Fill event with data
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent();
        withdrawalEvent.createTime = new Date();
        withdrawalEvent.transferId = 123_456;
        withdrawalEvent.userId = 98_765;
        withdrawalEvent.mtAccount = 654_321;
        withdrawalEvent.brand = "BrandX";
        withdrawalEvent.regulator = "RegulatorY";
        withdrawalEvent.paymentMethodCode = "PM123";

        // Check for trigger topic
        RecordMetadata producedMessageForCore =
                kafka.produceMessage("13", objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_INCOMING_CORE);
        ConsumerRecord consumedMessageOfCore =
                kafka.consumeMessages(KAFKA_TOPIC_INCOMING_CORE, producedMessageForCore.offset());
        WithdrawalEvent eventToRead =
                objectMapper.readValue(consumedMessageOfCore.value().toString(), WithdrawalEvent.class);
        Assertions.assertEquals(withdrawalEvent.brand, eventToRead);

        // Check for Alerts Topic
        RecordMetadata producedMessage =
                kafka.produceMessage("13", "I'm message for backoffice", KAFKA_TOPIC_INCOMING_BACK_OFFICE);
        ConsumerRecord consumedMessage =
                kafka.consumeMessages(KAFKA_TOPIC_INCOMING_BACK_OFFICE, producedMessage.offset());
        Assertions.assertEquals("I'm message for backoffice", consumedMessage.value());
    }
}
