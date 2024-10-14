package tests.eventGeneratorServiceTests.login;

import static helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventData.getLoginDbEventData;
import static helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventMetadata.getLoginDbEventMetadata;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.getRandomInt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEvent;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventData;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventMetadata;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class EventGeneratorLoginRequiredParametersTests {

    // Prepare data object
    String login_datetime = "2024-10-02T10:34:00.058786Z";
    Integer userId = getRandomInt();
    String brand = "Vantage";
    String ipAddress = "192.168.0.1";
    String uaString = "123";
    String cookie = "83a76a4c-6hce-4b9c-a39f-3s65d3c51775";

    // Prepare metadata object
    String timestamp = "2024-10-02T10:34:00.058786Z";
    String recordType = "data";
    String operation = "login";
    String partitionKeyType = "attribute-name";
    String schemaName = "dev_m_regulator_vfsc";
    String tableName = "tb_user_login_info";

    @Test
    @DisplayName("Generate login event with loginTime=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE_EVENT_LOGIN)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("87")
    public void generateLoginEventTest1() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        LoginDbEventData data = getLoginDbEventData(null, userId, brand, ipAddress, uaString, cookie);
        LoginDbEventMetadata metadata =
                getLoginDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, userId);

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate login event with userId=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE_EVENT_LOGIN)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("88")
    public void generateLoginEventTest2() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        LoginDbEventData data = getLoginDbEventData(login_datetime, null, brand, ipAddress, uaString, cookie);
        LoginDbEventMetadata metadata =
                getLoginDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, userId);

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate login event with brand=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE_EVENT_LOGIN)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("89")
    public void generateLoginEventTest3() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        LoginDbEventData data = getLoginDbEventData(login_datetime, userId, null, ipAddress, uaString, cookie);
        LoginDbEventMetadata metadata =
                getLoginDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, userId);

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate login event with data=null object")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE_EVENT_LOGIN)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("93")
    public void generateLoginEventTest7() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        LoginDbEventMetadata metadata =
                getLoginDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(null, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, userId);

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate login event with metadata=null object")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE_EVENT_LOGIN)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("94")
    public void generateLoginEventTest8() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        LoginDbEventData data = getLoginDbEventData(login_datetime, userId, brand, ipAddress, uaString, cookie);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, null);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, userId);

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate login event with tableName=null object")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE_EVENT_LOGIN)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("100")
    public void generateLoginEventTest14() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        LoginDbEventData data = getLoginDbEventData(login_datetime, userId, brand, ipAddress, uaString, cookie);
        LoginDbEventMetadata metadata =
                getLoginDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, null);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, userId);

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }
}
