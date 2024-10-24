package tests.eventGeneratorServiceTests.crmEvents.registration;

import static helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEventFactory.generateRegistrationDbEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.registration.RegistrationDbEvent;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class EventGeneratorRegistrationRequiredParametersTests {

    @Test
    @DisplayName("Generate registration event with any of the required parameters = null and verify that the Event Generator didn't produce the event")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("101")
    public void generateRegistrationEventsWithoutMandatoryParamsTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        RegistrationDbEvent registrationDbEventCreateTime = generateRegistrationDbEvent();
        registrationDbEventCreateTime.data.createTime = null;

        RegistrationDbEvent registrationDbEventUserId = generateRegistrationDbEvent();
        registrationDbEventUserId.data.userId = null;

        RegistrationDbEvent registrationDbEventBrand = generateRegistrationDbEvent();
        registrationDbEventBrand.data.brand = null;

        RegistrationDbEvent registrationDbEventRegulator = generateRegistrationDbEvent();
        registrationDbEventRegulator.data.regulator = null;

        RegistrationDbEvent registrationDbEventMtAccount = generateRegistrationDbEvent();
        registrationDbEventMtAccount.data.mtAccount = null;

        RegistrationDbEvent registrationDbEventTableName = generateRegistrationDbEvent();
        registrationDbEventTableName.metadata.tableName = null;

        RegistrationDbEvent registrationDbEventData = generateRegistrationDbEvent();
        registrationDbEventData.data = null;

        RegistrationDbEvent registrationDbEventMetadata = generateRegistrationDbEvent();
        registrationDbEventMetadata.metadata = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages("13", KAFKA_TOPIC_CRM_DB_EVENTS, objectMapper.writeValueAsString(registrationDbEventCreateTime), objectMapper.writeValueAsString(registrationDbEventUserId), objectMapper.writeValueAsString(registrationDbEventBrand), objectMapper.writeValueAsString(registrationDbEventRegulator), objectMapper.writeValueAsString(registrationDbEventMtAccount), objectMapper.writeValueAsString(registrationDbEventTableName), objectMapper.writeValueAsString(registrationDbEventData), objectMapper.writeValueAsString(registrationDbEventMetadata));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_CRM_EVENTS, registrationDbEventCreateTime.data.userId.toString(), registrationDbEventUserId.data.createTime, registrationDbEventBrand.data.userId.toString(), registrationDbEventRegulator.data.userId.toString(), registrationDbEventMtAccount.data.userId.toString(), registrationDbEventTableName.data.userId.toString(), registrationDbEventData.metadata.timestamp, registrationDbEventMetadata.data.userId.toString());

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @DisplayName("Generate open event with any of the optional parameters = null and verify that the Event Generator produced the event")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("102")
    public void generateOpenTradeEventsWithoutOptionalParamsTest() throws JsonProcessingException, InterruptedException {

        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        RegistrationDbEvent registrationDbEventTimestamp = generateRegistrationDbEvent();
        registrationDbEventTimestamp.metadata.timestamp = null;

        RegistrationDbEvent registrationDbEventRecordType = generateRegistrationDbEvent();
        registrationDbEventRecordType.metadata.recordType = null;

        RegistrationDbEvent registrationDbEventOperation = generateRegistrationDbEvent();
        registrationDbEventOperation.metadata.operation = null;

        RegistrationDbEvent registrationDbEventPartitionType = generateRegistrationDbEvent();
        registrationDbEventPartitionType.metadata.partitionKeyType = null;

        RegistrationDbEvent registrationDbEventSchemaName = generateRegistrationDbEvent();
        registrationDbEventSchemaName.metadata.schemaName = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages("13", KAFKA_TOPIC_CRM_DB_EVENTS, objectMapper.writeValueAsString(registrationDbEventTimestamp), objectMapper.writeValueAsString(registrationDbEventRecordType), objectMapper.writeValueAsString(registrationDbEventOperation), objectMapper.writeValueAsString(registrationDbEventPartitionType), objectMapper.writeValueAsString(registrationDbEventSchemaName));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage areAllParamsPresentInMessages = kafka.areAllParamsPresentInMessages(
                KAFKA_TOPIC_CRM_EVENTS, registrationDbEventTimestamp.data.userId.toString(), registrationDbEventRecordType.data.userId.toString(), registrationDbEventOperation.data.userId.toString(), registrationDbEventPartitionType.data.userId.toString(), registrationDbEventSchemaName.data.userId.toString());

        Allure.step("Verify that all events without optional params were found in the messages");
        assertThat("Check if all matching events found.", areAllParamsPresentInMessages.matchResult(), equalTo(true));
    }
}
