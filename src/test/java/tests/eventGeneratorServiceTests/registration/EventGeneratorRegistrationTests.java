package tests.eventGeneratorServiceTests.registration;

import static helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEventData.getRegistrationDbEventData;
import static helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEventMetadata.getRegistrationDbEventMetadata;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class EventGeneratorRegistrationTests {

    @Disabled
    @Test
    @DisplayName("Generate registration event with event generator service")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE_EVENT_REGISTRATION)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("64")
    public void generateRegistrationEventTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        int id = getRandomInt();

        // Prepare data object
        String createTime = "2024-10-02T10:34:00.058786Z";
        int userId = getRandomInt();
        String brand = "Vantage";
        String regulator = "VFSC";
        String mtAccount = "124";

        // Prepare metadata object
        String timestamp = "2024-10-02T10:34:00.058786Z";
        String recordType = "data";
        String operation = "login";
        String partitionKeyType = "attribute-name";
        String schemaName = "dev_m_regulator_vfsc";
        String tableName = "tb_login";

        RegistrationDbEventData data = getRegistrationDbEventData(createTime, userId, brand, regulator, mtAccount);
        RegistrationDbEventMetadata metadata = getRegistrationDbEventMetadata(
                timestamp, recordType, operation, partitionKeyType, schemaName, tableName);

        Allure.step("Write message to crm-db-events topic");
        RegistrationDbEvent crmDbEvent = RegistrationDbEvent.getRegistrationDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, id);
        RegistrationEvent RegistrationCrmEvent = objectMapper.readValue(consumedMessage, RegistrationEvent.class);

        Allure.step("Verify that message was written correctly");
        assertThat("Check RegistrationTime", RegistrationCrmEvent.data.UUID, notNullValue());
        assertThat("Check RegistrationTime", RegistrationCrmEvent.data.create_time, equalTo(createTime));
        assertThat("Check userId", RegistrationCrmEvent.data.user_id, equalTo(userId));
        assertThat("Check brand", RegistrationCrmEvent.data.brand, equalTo(brand));
        assertThat("Check regulator", RegistrationCrmEvent.data.regulator, equalTo(regulator));
        assertThat("Check mtAccount", RegistrationCrmEvent.data.mt_account, equalTo(mtAccount));
    }
}
