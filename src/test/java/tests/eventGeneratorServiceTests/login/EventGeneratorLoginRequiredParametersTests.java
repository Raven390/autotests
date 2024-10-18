package tests.eventGeneratorServiceTests.login;

import static helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventData.getLoginDbEventData;
import static helpers.kafka.crmDbEvents.eventGeneratorInbound.login.LoginDbEventMetadata.getLoginDbEventMetadata;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
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
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class EventGeneratorLoginRequiredParametersTests {
    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    // Prepare data object
    static String loginDatetime = "2024-10-02T10:34:00.058786Z";
    static Integer userId = getRandomInt();
    static String brand = "Vantage";
    static String ipAddress = "192.168.0.1";
    static String uaString = "123";
    static String cookie = "83a76a4c-6hce-4b9c-a39f-3s65d3c51775";

    // Prepare metadata object
    static String timestamp = "2024-10-02T10:34:00.058786Z";
    static String recordType = "data";
    static String operation = "login";
    static String partitionKeyType = "attribute-name";
    static String schemaName = "dev_m_regulator_vfsc";
    static String tableName = "tb_user_login_info";

    // Provide combinations of parameters for required LoginDbEventData fields
    private static Stream<Arguments> loginDbEventDataRequiredParameters() {
        return Stream.of(
                Arguments.of(null, getRandomInt(), brand, ipAddress, uaString, cookie),
                Arguments.of(loginDatetime, null, brand, ipAddress, uaString, cookie),
                Arguments.of(loginDatetime, getRandomInt(), null, ipAddress, uaString, cookie));
    }

    // Provide combinations for non-required LoginDbEventData fields
    private static Stream<Arguments> loginDbEventDataNotRequiredParameters() {
        return Stream.of(
                Arguments.of(loginDatetime, getRandomInt(), brand, null, uaString, cookie),
                Arguments.of(loginDatetime, getRandomInt(), brand, ipAddress, null, cookie),
                Arguments.of(loginDatetime, getRandomInt(), brand, ipAddress, uaString, null));
    }

    // Provide combinations of parameters for required LoginDbEventMetadata fields
    private static Stream<Arguments> loginDbEventMetadataRequiredParameters() {
        return Stream.of(Arguments.of(timestamp, recordType, operation, partitionKeyType, schemaName, null));
    }

    // Provide combinations for non-required LoginDbEventMetadata fields
    private static Stream<Arguments> loginDbEventMetadataNotRequiredParameters() {
        return Stream.of(
                Arguments.of(null, recordType, operation, partitionKeyType, schemaName, tableName),
                Arguments.of(timestamp, null, operation, partitionKeyType, schemaName, tableName),
                Arguments.of(timestamp, recordType, null, partitionKeyType, schemaName, tableName),
                Arguments.of(timestamp, recordType, operation, null, schemaName, tableName),
                Arguments.of(timestamp, recordType, operation, partitionKeyType, null, tableName));
    }

    @ParameterizedTest
    @MethodSource("loginDbEventDataRequiredParameters")
    @DisplayName("Generate login event with required data parameters = null")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("111")
    public void generateLoginEventTest1(
            String loginDatetime, Integer userId, String brand, String ipAddress, String uaString, String cookie)
            throws JsonProcessingException, InterruptedException {

        LoginDbEventData data = getLoginDbEventData(loginDatetime, userId, brand, ipAddress, uaString, cookie);
        LoginDbEventMetadata metadata =
                getLoginDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @ParameterizedTest
    @MethodSource("loginDbEventDataNotRequiredParameters")
    @DisplayName("Generate login event with not required data parameters = null")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("112")
    public void generateLoginEventTest2(
            String loginDatetime, Integer userId, String brand, String ipAddress, String uaString, String cookie)
            throws JsonProcessingException, InterruptedException {

        LoginDbEventData data = getLoginDbEventData(loginDatetime, userId, brand, ipAddress, uaString, cookie);
        LoginDbEventMetadata metadata =
                getLoginDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat("Check message", consumedMessage, containsString(String.valueOf(userId)));
    }

    @Test
    @DisplayName("Generate login event with data=null object")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("93")
    public void generateLoginEventTest3() throws JsonProcessingException, InterruptedException {

        LoginDbEventMetadata metadata =
                getLoginDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(null, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate login event with metadata=null object")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("94")
    public void generateLoginEventTest4() throws JsonProcessingException, InterruptedException {

        LoginDbEventData data = getLoginDbEventData(loginDatetime, userId, brand, ipAddress, uaString, cookie);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, null);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @ParameterizedTest
    @MethodSource("loginDbEventMetadataRequiredParameters")
    @DisplayName("Generate login event with required metadata parameters = null")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("113")
    public void generateLoginEventTest5(
            String timestamp,
            String recordType,
            String operation,
            String partitionKeyType,
            String schemaName,
            String tableName)
            throws JsonProcessingException, InterruptedException {
        Integer userId = getRandomInt();

        LoginDbEventData data = getLoginDbEventData(loginDatetime, userId, brand, ipAddress, uaString, cookie);
        LoginDbEventMetadata metadata =
                getLoginDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @ParameterizedTest
    @MethodSource("loginDbEventMetadataNotRequiredParameters")
    @DisplayName("Generate login event with not required metadata parameters = null")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("114")
    public void generateLoginEventTest6(
            String timestamp,
            String recordType,
            String operation,
            String partitionKeyType,
            String schemaName,
            String tableName)
            throws JsonProcessingException, InterruptedException {

        LoginDbEventData data = getLoginDbEventData(loginDatetime, userId, brand, ipAddress, uaString, cookie);
        LoginDbEventMetadata metadata =
                getLoginDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        LoginDbEvent crmDbEvent = LoginDbEvent.getLoginDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat("Check message", consumedMessage, containsString(String.valueOf(userId)));
    }
}
