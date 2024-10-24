package tests.eventGeneratorServiceTests.crmEvents.registration;

import static helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEventData.getRegistrationDbEventData;
import static helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEventMetadata.getRegistrationDbEventMetadata;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static utils.Constants.*;
import static utils.Utils.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEvent;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEventData;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEventMetadata;
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

public class EventGeneratorRegistrationRequiredParametersTests {
    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    // Prepare data object
    static String createTime = getCurrentDateTime();
    static Integer userId = getRandomInt();
    static String brand = "Vantage";
    static String regulator = "VFSC";
    static Integer mtAccount = getRandomInt();

    // Prepare metadata object
    static String timestamp = getCurrentDateTime();
    static String recordType = "data";
    static String operation = "registration";
    static String partitionKeyType = "attribute-name";
    static String schemaName = "dev_m_regulator_vfsc";
    static String tableName = "tb_account_mt4";

    // Provide combinations of parameters for required LoginDbEventData fields
    private static Stream<Arguments> registrationDbEventDataRequiredParameters() {
        return Stream.of(
                Arguments.of(null, userId, brand, regulator, mtAccount),
                Arguments.of(createTime, null, brand, regulator, mtAccount),
                Arguments.of(createTime, userId, null, regulator, mtAccount),
                Arguments.of(createTime, userId, brand, null, mtAccount),
                Arguments.of(createTime, userId, brand, regulator, null));
    }

    // Provide combinations for non-required LoginDbEventMetadata fields
    private static Stream<Arguments> registrationDbEventMetadataNotRequiredParameters() {
        return Stream.of(
                Arguments.of(null, recordType, operation, partitionKeyType, schemaName, tableName),
                Arguments.of(timestamp, null, operation, partitionKeyType, schemaName, tableName),
                Arguments.of(timestamp, recordType, null, partitionKeyType, schemaName, tableName),
                Arguments.of(timestamp, recordType, operation, null, schemaName, tableName),
                Arguments.of(timestamp, recordType, operation, partitionKeyType, null, tableName));
    }

    // Provide combinations for required LoginDbEventMetadata fields
    private static Stream<Arguments> registrationDbEventMetadataRequiredParameters() {
        return Stream.of(
                Arguments.of(timestamp, recordType, operation, partitionKeyType, schemaName, null));
    }

    @Test
    @DisplayName("Generate registration event with data=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("101")
    public void generateRegistrationEventTest1() throws JsonProcessingException, InterruptedException {
        RegistrationDbEventMetadata metadata = getRegistrationDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        RegistrationDbEvent crmDbEvent = RegistrationDbEvent.getRegistrationDbEvent(null, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, createTime);

        Allure.step("Verify that message was not found");
        assertThat(consumedMessage, containsString(KAFKA_NO_MESSAGE_FOUND_ERROR));
    }

    @ParameterizedTest
    @MethodSource("registrationDbEventDataRequiredParameters")
    @DisplayName("Generate registration event with data required parameters=null")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("102")
    public void generateRegistrationEventTest2(String createTime, Integer userId, String brand, String regulator, Integer mtAccount) throws JsonProcessingException, InterruptedException {

        RegistrationDbEventData data = getRegistrationDbEventData(createTime, userId, brand, regulator, mtAccount);
        RegistrationDbEventMetadata metadata = getRegistrationDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        RegistrationDbEvent crmDbEvent = RegistrationDbEvent.getRegistrationDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, timestamp);

        Allure.step("Verify that message was not found");
        assertThat(consumedMessage, containsString(KAFKA_NO_MESSAGE_FOUND_ERROR));
    }

    @Test
    @DisplayName("Generate registration event with metadata=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("103")
    public void generateRegistrationEventTest3() throws JsonProcessingException, InterruptedException {

        RegistrationDbEventData data = getRegistrationDbEventData(createTime, userId, brand, regulator, mtAccount);

        Allure.step("Write message to crm-db-events topic");
        RegistrationDbEvent crmDbEvent = RegistrationDbEvent.getRegistrationDbEvent(data, null);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, createTime);

        Allure.step("Verify that message was not found");
        assertThat(consumedMessage, containsString(KAFKA_NO_MESSAGE_FOUND_ERROR));
    }

    @ParameterizedTest
    @MethodSource("registrationDbEventMetadataRequiredParameters")
    @DisplayName("Generate registration event with data required parameters=null")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("104")
    public void generateRegistrationEventTest4(String timestamp, String recordType, String operation, String partitionKeyType, String schemaName, String tableName) throws JsonProcessingException, InterruptedException {

        RegistrationDbEventData data = getRegistrationDbEventData(createTime, userId, brand, regulator, mtAccount);
        RegistrationDbEventMetadata metadata = getRegistrationDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        RegistrationDbEvent crmDbEvent = RegistrationDbEvent.getRegistrationDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, userId.toString());

        Allure.step("Verify that message was not found");
        assertThat(consumedMessage, containsString(KAFKA_NO_MESSAGE_FOUND_ERROR));
    }

    @ParameterizedTest
    @MethodSource("registrationDbEventMetadataNotRequiredParameters")
    @DisplayName("Generate registration event with metadata not required parameters=null")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("105")
    public void generateRegistrationEventTest5(String timestamp, String recordType, String operation, String partitionKeyType, String schemaName, String tableName) throws JsonProcessingException, InterruptedException {
        Integer userId = getRandomInt();
        System.out.println(userId);

        RegistrationDbEventData data = getRegistrationDbEventData(createTime, userId, brand, regulator, mtAccount);
        RegistrationDbEventMetadata metadata = getRegistrationDbEventMetadata(timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        RegistrationDbEvent crmDbEvent = RegistrationDbEvent.getRegistrationDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat("Check message", consumedMessage, containsString(String.valueOf(userId)));
    }
}
