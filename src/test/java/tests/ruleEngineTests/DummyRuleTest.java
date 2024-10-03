package tests.ruleEngineTests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Example of event message
 *
 * {
 *   "user_id": 12345,
 *   "create_time": "2024-09-26T17:10:45",
 *   "transfer_id": 478908765,
 *   "mt_account": 768795768,
 *   "brand": "brand_468e065e73e8",
 *   "regulator": "regulator_d47904e3f720",
 *   "payment_method_code": "payment_method_code_3098b8c483bd",
 *   "type": "withdrawal"
 * }
 *
 * Example of alert message
 * {
 *   "uuid": "41d677e3-cca0-4f13-8a00-952836483deb",
 *   "alertId": "f6f4b674-f908-48d3-aeab-248c85cff631",
 *   "ucid": "brand_468e065e73e8-999-regulator_d47904e3f720",
 *   "timestamp": 1727791069.2194092,
 *   "rule": {
 *   "code": 123456789,
 *   "ver": "1.0",
 *   "name": "withdrawal",
 *   "trigger": "Withdrawal",
 *   "fraudType": "Withdrawal",
 *   "attributes": {}
 *   }
 * }
 */
public class DummyRuleTest {

    @Test
    @DisplayName("Dummy rule test")
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Feature(FEATURE_RULE_ENGINE)
    @Tag(TEAM_CORE)
    @AllureId("58")
    public void dummyRuleTest() throws JsonProcessingException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaHelper kafka = new KafkaHelper();

        Allure.step("Prepare test data");
        String uuid = UUID.randomUUID().toString();
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
                uuid, createTime, transferId, userId, mtAccount, brand, regulator, paymentMethodCode, type);

        Allure.step("Send trigger message to CRM_EVENTS");
        kafka.produceMessage("13", objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);
        ConsumerRecord<String, String> consumedMessageOfCore = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, uuid);
        WithdrawalEvent withdrawalEvent1 = objectMapper.readValue(consumedMessageOfCore.value(), WithdrawalEvent.class);

        Allure.step("Verify that message was written correctly");
        assertThat("Check brand", withdrawalEvent1.brand, equalTo(brand));
        assertThat("Check regulator", withdrawalEvent1.regulator, equalTo(regulator));
        assertThat("Check paymentMethodCode", withdrawalEvent1.paymentMethodCode, equalTo(paymentMethodCode));
        assertThat("Check createTime", withdrawalEvent1.createTime, equalTo(createTime));
        assertThat("Check userId", withdrawalEvent1.userId, equalTo(userId));
        assertThat("Check mtAccount", withdrawalEvent1.mtAccount, equalTo(mtAccount));
        assertThat("Check transferId", withdrawalEvent1.transferId, equalTo(transferId));
        assertThat("Check type", withdrawalEvent1.type, equalTo(type));

        Allure.step("Wait for rule execution. Check topic for needed message");
        ConsumerRecord<String, String> consumedAlert = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, uuid);
        AlertEvent alertEvent = objectMapper.readValue(consumedAlert.value(), AlertEvent.class);

        Allure.step("Verify that message was written correctly");
        assertThat("Check brand", alertEvent.uuid, equalTo(uuid));
        assertThat("Check alertId", alertEvent.alertId, notNullValue());
        assertThat("Check ucid", alertEvent.ucid, equalTo(brand + "-" + userId + "-" + regulator));
        assertThat("Check timestamp", alertEvent.timestamp, notNullValue());
        assertThat("Check rule.code", alertEvent.rule.code, equalTo(123_456_789));
        assertThat("Check rule.ver", alertEvent.rule.ver, equalTo("1.0"));
        assertThat("Check rule.name", alertEvent.rule.name, equalTo("withdrawal"));
        assertThat("Check rule.trigger", alertEvent.rule.trigger, equalTo("Withdrawal"));
        assertThat("Check rule.fraudType", alertEvent.rule.fraudType, equalTo("Withdrawal"));
        assertThat("Check rule.attributes", alertEvent.rule.attributes, notNullValue());
    }
}
