package tests.ruleEngineServiceTests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomInt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.alerts.AlertEvent;
import helpers.kafka.crmEvents.toRemove.WithdrawalCrmEvent;
import io.qameta.allure.*;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class DummyRuleTest {

    @Disabled("Disabled. Almost not actual now")
    @Test
    @DisplayName("Dummy rule test")
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Feature(FEATURE_RULE_ENGINE_SERVICE)
    @Tag(TEAM_CORE)
    @AllureId("58")
    public void dummyRuleTest() throws JsonProcessingException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaHelper kafka = new KafkaHelper();

        Allure.step("Prepare test data");
        int uuid = getRandomInt();
        System.out.println(uuid);
        Date createTime = Date.from(Instant.ofEpochMilli(System.currentTimeMillis()));
        int transferId = 478_908_765;
        int userId = 999;
        int mtAccount = 768_795_768;
        String brand = "brand_468e065e73e8";
        String regulator = "regulator_d47904e3f720";
        String paymentMethodCode = "payment_method_code_3098b8c483bd";
        String type = "withdrawal";

        Allure.step("Build withdrawal message to send to crm_events");
        WithdrawalCrmEvent withdrawalEvent = WithdrawalCrmEvent.withdrawalCrmEvent(String.valueOf(uuid), createTime, transferId, userId, mtAccount, brand, regulator, paymentMethodCode, type);

        Allure.step("Send trigger message to CRM_EVENTS");
        kafka.produceMessage("13", objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);
        String consumedMessageOfCore = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));
        WithdrawalCrmEvent withdrawalEvent1 = objectMapper.readValue(consumedMessageOfCore, WithdrawalCrmEvent.class);

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
        String consumedAlert = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, String.valueOf(userId));
        AlertEvent alertEvent = objectMapper.readValue(consumedAlert, AlertEvent.class);

        Allure.step("Verify that message was written correctly");
        assertThat("Check uuid", alertEvent.uuid, equalTo(String.valueOf(uuid)));
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
