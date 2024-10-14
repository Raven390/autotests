package tests.eventGeneratorServiceTests.login;

import static helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventData.getLoginDbEventData;
import static helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventMetadata.getLoginDbEventMetadata;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;
import static utils.Utils.getRandomInt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEvent;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventData;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventMetadata;
import helpers.kafka.crmEvents.eventGeneratorOutboundEvents.LoginEvent;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class EventGeneratorLoginTests {

    @Test
    @DisplayName("Generate login event with event generator service")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE_EVENT_LOGIN)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("65")
    public void generateLoginEventTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        // Prepare data object
        String loginTime = "2024-10-02T10:34:00.058786Z";
        Integer userId = getRandomInt();
        System.out.println(userId);
        String brand = "Vantage";
        String ipAddress = "192.168.0.1";
        String ua_string = "123";
        String cookie = "83a76a4c-6hce-4b9c-a39f-3s65d3c51775";
        String type = "websiteLogin";

        // Prepare metadata object
        String timestamp = "2024-10-02T10:34:00.058786Z";
        String recordType = "data";
        String operation = "login";
        String partitionKeyType = "attribute-name";
        String schemaName = "dev_m_regulator_vfsc";
        String tableName = "tb_user_login_info";

        LoginDbEventData data = getLoginDbEventData(loginTime, userId, brand, ipAddress, ua_string, cookie);
        LoginDbEventMetadata metadata =
                getLoginDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, userId);
        LoginEvent loginCrmEvent = objectMapper.readValue(consumedMessage, LoginEvent.class);

        Allure.step("Verify that message was written correctly");
        assertThat("Check UUID", loginCrmEvent.uuid, notNullValue());
        assertThat("Check loginTime", loginCrmEvent.login_time, equalTo(loginTime));
        assertThat("Check user_id", loginCrmEvent.user_id, equalTo(userId));
        assertThat("Check brand", loginCrmEvent.brand, equalTo(brand));
        assertThat("Check ip_address", loginCrmEvent.ip_address, equalTo(ipAddress));
        assertThat("Check cid", loginCrmEvent.cid, equalTo(ua_string));
        assertThat("Check cookie", loginCrmEvent.cookie, equalTo(cookie));
        assertThat("Check type", loginCrmEvent.type, equalTo(type));
    }
}
