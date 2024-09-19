import static utils.Constants.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.messages.triggers.LoginEvent;
import helpers.kafka.messages.triggers.RegistrationEvent;
import helpers.kafka.messages.triggers.WithdrawalEvent;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

// JUST FOR DEMO PURPOSES
// TODO REMOVE WHOLE CLASS AFTER IMPLEMENTING FIRST TEST USING KAFKA
public class KafkaDemoTest {

    @Test
    void testProduceAndReadMessage() throws IOException {
        // Prepare producer and consumer
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaHelper kafka = new KafkaHelper();

        // Data
        int userId = 123;
        String brand = "VT";
        String regulator = "CySec";
        Date date = Date.from(Instant.ofEpochMilli(System.currentTimeMillis()));
        String paymentMethodCode = "FASAPAY";
        int mtAccount = 666;
        int ipAdress = 999;
        int transferId = 444;

        // Fill events with data
        RegistrationEvent registrationEvent =
                RegistrationEvent.registrationEvent(date, userId, brand, regulator, mtAccount);
        LoginEvent loginEvent = LoginEvent.loginEvent(date, userId, brand, ipAdress);
        WithdrawalEvent withdrawalEvent = WithdrawalEvent.withdrawalEvent(
                date, transferId, userId, mtAccount, brand, regulator, paymentMethodCode);

        // Write and check that RegistrationEvent is written
        RecordMetadata producedRegistrationTrigger = kafka.produceMessage(
                "13", objectMapper.writeValueAsString(registrationEvent), KAFKA_TOPIC_INCOMING_CORE);
        ConsumerRecord<String, String> consumedRegistrationTrigger =
                kafka.consumeMessages(KAFKA_TOPIC_INCOMING_CORE, producedRegistrationTrigger.offset());
        RegistrationEvent readRegistrationTrigger =
                objectMapper.readValue(consumedRegistrationTrigger.value(), RegistrationEvent.class);
        Assertions.assertEquals(registrationEvent.createTime, readRegistrationTrigger.createTime);
        Assertions.assertEquals(registrationEvent.userId, readRegistrationTrigger.userId);
        Assertions.assertEquals(registrationEvent.brand, readRegistrationTrigger.brand);
        Assertions.assertEquals(registrationEvent.regulator, readRegistrationTrigger.regulator);
        Assertions.assertEquals(registrationEvent.mtAccount, readRegistrationTrigger.mtAccount);

        // Write and check that loginEvent is written
        RecordMetadata producedLoginTrigger =
                kafka.produceMessage("13", objectMapper.writeValueAsString(loginEvent), KAFKA_TOPIC_INCOMING_CORE);
        ConsumerRecord<String, String> consumedLoginTrigger =
                kafka.consumeMessages(KAFKA_TOPIC_INCOMING_CORE, producedLoginTrigger.offset());
        LoginEvent readLoginTrigger = objectMapper.readValue(consumedLoginTrigger.value(), LoginEvent.class);
        Assertions.assertEquals(loginEvent.loginTime, readLoginTrigger.loginTime);
        Assertions.assertEquals(loginEvent.userId, readLoginTrigger.userId);
        Assertions.assertEquals(loginEvent.brand, readLoginTrigger.brand);
        Assertions.assertEquals(loginEvent.ipAddress, readLoginTrigger.ipAddress);

        // Write and check that withdrawalEvent is written
        RecordMetadata producedWithdrawalTrigger =
                kafka.produceMessage("13", objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_INCOMING_CORE);
        ConsumerRecord<String, String> consumedMessageOfCore =
                kafka.consumeMessages(KAFKA_TOPIC_INCOMING_CORE, producedWithdrawalTrigger.offset());
        WithdrawalEvent readWithdrawalTrigger =
                objectMapper.readValue(consumedMessageOfCore.value(), WithdrawalEvent.class);
        Assertions.assertEquals(withdrawalEvent.transferId, readWithdrawalTrigger.transferId);
        Assertions.assertEquals(withdrawalEvent.userId, readWithdrawalTrigger.userId);
        Assertions.assertEquals(withdrawalEvent.brand, readWithdrawalTrigger.brand);
        Assertions.assertEquals(withdrawalEvent.regulator, readWithdrawalTrigger.regulator);
        Assertions.assertEquals(withdrawalEvent.paymentMethodCode, readWithdrawalTrigger.paymentMethodCode);

        // Check for Alerts Topic
        RecordMetadata producedMessage =
                kafka.produceMessage("13", "I'm message for backoffice", KAFKA_TOPIC_INCOMING_BACK_OFFICE);
        ConsumerRecord<String, String> consumedMessage =
                kafka.consumeMessages(KAFKA_TOPIC_INCOMING_BACK_OFFICE, producedMessage.offset());
        Assertions.assertEquals("I'm message for backoffice", consumedMessage.value());
    }
}
