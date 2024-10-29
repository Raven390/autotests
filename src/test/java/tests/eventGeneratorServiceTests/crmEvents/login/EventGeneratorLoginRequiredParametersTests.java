package tests.eventGeneratorServiceTests.crmEvents.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import businessObjects.kafka.crmDbEvents.login.LoginDbEvent;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


import static businessObjects.kafka.crmDbEvents.login.LoginDbEventFactory.generateLoginDbEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_LOGIN)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
public class EventGeneratorLoginRequiredParametersTests {

    @Test
    @DisplayName("Generate login event with any of the required parameters = null and verify that the Event Generator didn't produce the event")
    @AllureId("111")
    public void generateLoginEventsWithoutMandatoryParamsTest() throws JsonProcessingException {

        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        LoginDbEvent loginDbEventLoginTime = generateLoginDbEvent();
        loginDbEventLoginTime.data.loginDatetime = null;

        LoginDbEvent loginDbEventUserId = generateLoginDbEvent();
        loginDbEventUserId.data.userId = null;

        LoginDbEvent loginDbEventBrand = generateLoginDbEvent();
        loginDbEventBrand.data.brand = null;

        LoginDbEvent loginDbEventTableName = generateLoginDbEvent();
        loginDbEventTableName.metadata.tableName = null;

        LoginDbEvent loginDbEventData = generateLoginDbEvent();
        loginDbEventData.data = null;

        LoginDbEvent loginDbEventMetadata = generateLoginDbEvent();
        loginDbEventMetadata.metadata = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages("13", KAFKA_TOPIC_CRM_DB_EVENTS, objectMapper.writeValueAsString(loginDbEventLoginTime), objectMapper.writeValueAsString(loginDbEventUserId), objectMapper.writeValueAsString(loginDbEventBrand), objectMapper.writeValueAsString(loginDbEventTableName), objectMapper.writeValueAsString(loginDbEventData), objectMapper.writeValueAsString(loginDbEventMetadata));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_CRM_EVENTS, loginDbEventLoginTime.data.userId.toString(), loginDbEventUserId.data.loginDatetime, loginDbEventBrand.data.userId.toString(), loginDbEventTableName.data.userId.toString(), loginDbEventData.metadata.timestamp, loginDbEventMetadata.data.userId.toString());

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @DisplayName("Generate login event with any of the optional parameters = null and verify that the Event Generator produced the event")
    @AllureId("112")
    public void generateLoginEventsWithoutOptionalParamsTest() throws JsonProcessingException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        LoginDbEvent loginDbEventIpAddress = generateLoginDbEvent();
        loginDbEventIpAddress.data.ipAddress = null;

        LoginDbEvent loginDbEventUaString = generateLoginDbEvent();
        loginDbEventUaString.data.uaString = null;

        LoginDbEvent loginDbEventCookie = generateLoginDbEvent();
        loginDbEventCookie.data.cookie = null;

        LoginDbEvent loginDbEventTimestamp = generateLoginDbEvent();
        loginDbEventTimestamp.metadata.timestamp = null;

        LoginDbEvent loginDbEventRecordType = generateLoginDbEvent();
        loginDbEventRecordType.metadata.recordType = null;

        LoginDbEvent loginDbEventOperation = generateLoginDbEvent();
        loginDbEventOperation.metadata.operation = null;

        LoginDbEvent loginDbEventPartitionType = generateLoginDbEvent();
        loginDbEventPartitionType.metadata.partitionKeyType = null;

        LoginDbEvent loginDbEventSchemaName = generateLoginDbEvent();
        loginDbEventSchemaName.metadata.schemaName = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages("13", KAFKA_TOPIC_CRM_DB_EVENTS, objectMapper.writeValueAsString(loginDbEventIpAddress), objectMapper.writeValueAsString(loginDbEventUaString), objectMapper.writeValueAsString(loginDbEventCookie), objectMapper.writeValueAsString(loginDbEventTimestamp), objectMapper.writeValueAsString(loginDbEventRecordType), objectMapper.writeValueAsString(loginDbEventOperation), objectMapper.writeValueAsString(loginDbEventPartitionType), objectMapper.writeValueAsString(loginDbEventSchemaName));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage areAllParamsPresentInMessages = kafka.areAllParamsPresentInMessages(
                KAFKA_TOPIC_CRM_EVENTS, loginDbEventIpAddress.data.userId.toString(), loginDbEventUaString.data.userId.toString(), loginDbEventCookie.data.userId.toString(), loginDbEventTimestamp.data.userId.toString(), loginDbEventRecordType.data.userId.toString(), loginDbEventOperation.data.userId.toString(), loginDbEventPartitionType.data.userId.toString(), loginDbEventSchemaName.data.userId.toString());

        Allure.step("Verify that all events without optional params were found in the messages");
        assertThat("Check if all matching events found.", areAllParamsPresentInMessages.matchResult(), equalTo(true));
    }
}
