package tests.eventGeneratorServiceTests.registration;

import static helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEventData.getRegistrationDbEventData;
import static helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEventMetadata.getRegistrationDbEventMetadata;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentDateTime;
import static utils.Utils.getRandomInt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEvent;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEventData;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEventMetadata;
import helpers.kafka.crmEvents.eventGeneratorOutboundEvents.RegistrationEvent;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class EventGeneratorRegistrationTests {
    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    // Prepare data object
    String createTime = getCurrentDateTime();
    Integer userId = getRandomInt();
    String brand = "Vantage";
    String regulator = "VFSC";
    Integer mtAccount = getRandomInt();

    // Prepare metadata object
    String timestamp = "2024-09-30T16:24:35.142706Z";
    String recordType = "data";
    String operation = "insert";
    String partitionKeyType = "attribute-name";
    String schemaName = "dev_m_regulator_vfsc";
    String tableName = "tb_account_mt4";

    @Test
    @DisplayName("Generate registration event with event generator service")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("64")
    public void generateRegistrationEventTest1() throws JsonProcessingException, InterruptedException {

        RegistrationDbEventData data = getRegistrationDbEventData(createTime, userId, brand, regulator, mtAccount);
        RegistrationDbEventMetadata metadata = getRegistrationDbEventMetadata(
                timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        RegistrationDbEvent crmDbEvent = RegistrationDbEvent.getRegistrationDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, createTime, 60);
        RegistrationEvent RegistrationCrmEvent = objectMapper.readValue(consumedMessage, RegistrationEvent.class);

        Allure.step("Verify that message was written correctly");
        assertThat("Check RegistrationTime", RegistrationCrmEvent.data.uuid, notNullValue());
        assertThat("Check RegistrationTime", RegistrationCrmEvent.data.create_time, equalTo(createTime));
        assertThat("Check userId", RegistrationCrmEvent.data.user_id, equalTo(userId));
        assertThat("Check brand", RegistrationCrmEvent.data.brand, equalTo(brand));
        assertThat("Check regulator", RegistrationCrmEvent.data.regulator, equalTo(regulator));
        assertThat("Check mtAccount", RegistrationCrmEvent.data.mt_account, equalTo(mtAccount));
    }

    @Test
    @DisplayName("Generate registration event two times for the same user")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("128")
    public void generateRegistrationEventTest2() throws JsonProcessingException, InterruptedException {
        RegistrationDbEventData data =
                getRegistrationDbEventData(createTime, getRandomInt(), brand, regulator, mtAccount);
        RegistrationDbEventMetadata metadata = getRegistrationDbEventMetadata(
                timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        RegistrationDbEvent crmDbEvent = RegistrationDbEvent.getRegistrationDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, createTime, 60);
        RegistrationEvent RegistrationCrmEvent = objectMapper.readValue(consumedMessage, RegistrationEvent.class);

        Allure.step("Verify that message was written correctly");
        assertThat("Check RegistrationTime", RegistrationCrmEvent.data.uuid, notNullValue());

        Allure.step("Write second message to crm-db-events topic");
        crmDbEvent = RegistrationDbEvent.getRegistrationDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, createTime);
        assertThat(consumedMessage, containsString("Max attempts reached without finding a matching message"));
    }
}
