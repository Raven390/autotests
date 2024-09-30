package tests.ruleEngineTests;

import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.messages.alerts.AlertEvent;
import helpers.kafka.messages.triggers.WithdrawalEvent;
import io.qameta.allure.*;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class DummyRuleTest {

    @Test
    @DisplayName("Dummy rule test")
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Feature(FEATURE_RULE_ENGINE)
    @Tag(TEAM_CORE)
    @AllureId("58")
    public void dummyRuleTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaHelper kafka = new KafkaHelper();

        Allure.step("Prepare test data");
        String traceId = UUID.randomUUID().toString();
        Date createTime = Date.from(Instant.ofEpochMilli(System.currentTimeMillis()));
        int transferId = 478_908_765;
        int userId = 999;
        int mtAccount = 768_795_768;
        String brand = "brand_468e065e73e8";
        String regulator = "regulator_d47904e3f720";
        String paymentMethodCode = "payment_method_code_3098b8c483bd";
        String type = "withdrawal";

        Allure.step("Build withdrawal message to send to crm_events");
        WithdrawalEvent withdrawalEvent = WithdrawalEvent.withdrawalEvent(
                traceId, createTime, transferId, userId, mtAccount, brand, regulator, paymentMethodCode, type);

        Allure.step("Send trigger message to CRM_EVENTS");
        kafka.produceMessage("13", objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);
        ConsumerRecord<String, String> consumedMessageOfCore = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS);
        WithdrawalEvent withdrawalEvent1 = objectMapper.readValue(consumedMessageOfCore.value(), WithdrawalEvent.class);

        Allure.step("Verify that message was written correctly");
        Assertions.assertEquals(brand, withdrawalEvent1.brand);
        Assertions.assertEquals(regulator, withdrawalEvent1.regulator);
        Assertions.assertEquals(paymentMethodCode, withdrawalEvent1.paymentMethodCode);
        Assertions.assertEquals(createTime, withdrawalEvent1.createTime);
        Assertions.assertEquals(userId, withdrawalEvent1.userId);
        Assertions.assertEquals(mtAccount, withdrawalEvent1.mtAccount);
        Assertions.assertEquals(transferId, withdrawalEvent1.transferId);
        Assertions.assertEquals(type, withdrawalEvent1.type);

        Allure.step("Wait for rule execution. Check topic for needed message");
        ConsumerRecord<String, String> consumedAlert = kafka.consumeMessages(KAFKA_TOPIC_ALERTS);
        AlertEvent alertEvent = objectMapper.readValue(consumedAlert.value(), AlertEvent.class);

        Allure.step("Verify that message was written correctly");
        Assertions.assertEquals(traceId, alertEvent.uuid);
        Assertions.assertTrue(alertEvent.alertId != null);
        Assertions.assertEquals(String.valueOf(userId), alertEvent.ucid);
        Assertions.assertTrue(alertEvent.timestamp != null);
        Assertions.assertEquals(null, alertEvent.rule);
        Assertions.assertEquals("Withdrawal", alertEvent.trigger);
        Assertions.assertEquals(null, alertEvent.ruleAttributes);
    }
}
