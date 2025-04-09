package tests.event_generator_service_tests.mt_events.open_trade;

import static business_objects.kafka.mt_db_events.open_trade.OpenTradeMtDbEventFactory.generateOpenTradeMtDbEventMt4;
import static business_objects.kafka.mt_db_events.open_trade.OpenTradeMtDbEventFactory.generateOpenTradeMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.removeKeyFromJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import business_objects.kafka.mt_db_events.open_trade.OpenTradeMtDbEventMt4;
import business_objects.kafka.mt_db_events.open_trade.OpenTradeMtDbEventMt5;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_OPEN_TRADE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class MtDbEventsOpenTradeRequiredParamsTest {

    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName(
        "Generate open event with any of the required parameters = null and verify that the Event Generator didn't produce the event")
    @AllureId("121")
    void generateOpenTradeEventsWithoutMandatoryParamsTest() throws JsonProcessingException {

        //        Creation of open trade events that should be filtered out by the filtration rules
        OpenTradeMtDbEventMt4 openTradeEventOpenTime = generateOpenTradeMtDbEventMt4();
        openTradeEventOpenTime.data.openTime = null;

        OpenTradeMtDbEventMt4 openTradeEventTradeId = generateOpenTradeMtDbEventMt4();
        openTradeEventTradeId.data.tradeId = null;

        OpenTradeMtDbEventMt4 openTradeEventMtAccount = generateOpenTradeMtDbEventMt4();
        openTradeEventMtAccount.data.mtAccount = null;

        OpenTradeMtDbEventMt4 openTradeEventServerId = generateOpenTradeMtDbEventMt4();
        openTradeEventServerId.data.serverId = null;

        OpenTradeMtDbEventMt4 openTradeEventMt4Cmd = generateOpenTradeMtDbEventMt4();
        openTradeEventMt4Cmd.data.cmd = null;

        OpenTradeMtDbEventMt5 openTradeEventMt5Entry = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Entry.data.entry = null;

        OpenTradeMtDbEventMt5 openTradeEventMt5Action = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Action.data.action = null;

        OpenTradeMtDbEventMt4 openTradeEventTableName = generateOpenTradeMtDbEventMt4();
        openTradeEventTableName.metadata.tableName = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages("13", KAFKA_TOPIC_MT_DB_EVENTS, objectMapper.writeValueAsString(openTradeEventOpenTime), objectMapper.writeValueAsString(openTradeEventTradeId), objectMapper.writeValueAsString(openTradeEventMtAccount), objectMapper.writeValueAsString(openTradeEventServerId), objectMapper.writeValueAsString(openTradeEventMt4Cmd), objectMapper.writeValueAsString(openTradeEventMt5Entry), objectMapper.writeValueAsString(openTradeEventMt5Action), objectMapper.writeValueAsString(openTradeEventTableName));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, openTradeEventOpenTime.metadata.timestamp, openTradeEventTradeId.data.openTime, openTradeEventMtAccount.data.openTime, openTradeEventServerId.data.openTime, openTradeEventMt4Cmd.data.openTime, openTradeEventMt5Entry.data.openTime, openTradeEventMt5Action.data.openTime, openTradeEventTableName.data.openTime);

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @DisplayName(
        "Generate open event with any of the optional parameters = null and verify that the Event Generator produced the event")
    @AllureId("122")
    void generateOpenTradeEventsWithoutOptionalParamsTest() throws JsonProcessingException {

        //        Creation of Open trade events that should be filtered out by the filtration rules
        OpenTradeMtDbEventMt4 openTradeEventVolume = generateOpenTradeMtDbEventMt4();
        openTradeEventVolume.data.volume = null;

        OpenTradeMtDbEventMt4 openTradeEventSymbol = generateOpenTradeMtDbEventMt4();
        openTradeEventSymbol.data.symbol = null;

        OpenTradeMtDbEventMt4 openTradeEventCloseTime = generateOpenTradeMtDbEventMt4();
        openTradeEventCloseTime.data.closeTime = null;

        OpenTradeMtDbEventMt4 openTradeEventTimestamp = generateOpenTradeMtDbEventMt4();
        openTradeEventTimestamp.metadata.timestamp = null;

        OpenTradeMtDbEventMt4 openTradeEventRecordType = generateOpenTradeMtDbEventMt4();
        openTradeEventRecordType.metadata.recordType = null;

        OpenTradeMtDbEventMt4 openTradeEventOperation = generateOpenTradeMtDbEventMt4();
        openTradeEventOperation.metadata.operation = null;

        OpenTradeMtDbEventMt4 openTradeEventPartitionKeyType = generateOpenTradeMtDbEventMt4();
        openTradeEventPartitionKeyType.metadata.partitionKeyType = null;

        OpenTradeMtDbEventMt4 openTradeEventSchemaName = generateOpenTradeMtDbEventMt4();
        openTradeEventSchemaName.metadata.schemaName = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages(KAFKA_MESSAGE_KEY, KAFKA_TOPIC_MT_DB_EVENTS, objectMapper.writeValueAsString(openTradeEventVolume), objectMapper.writeValueAsString(openTradeEventSymbol), objectMapper.writeValueAsString(openTradeEventCloseTime), objectMapper.writeValueAsString(openTradeEventTimestamp), objectMapper.writeValueAsString(openTradeEventRecordType), objectMapper.writeValueAsString(openTradeEventOperation), objectMapper.writeValueAsString(openTradeEventPartitionKeyType), objectMapper.writeValueAsString(openTradeEventSchemaName));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage areAllParamsPresentInMessages = kafka.areAllParamsPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, openTradeEventVolume.data.openTime, openTradeEventSymbol.data.openTime, openTradeEventCloseTime.data.openTime, openTradeEventTimestamp.data.openTime, openTradeEventRecordType.data.openTime, openTradeEventOperation.data.openTime, openTradeEventPartitionKeyType.data.openTime, openTradeEventSchemaName.data.openTime);

        Allure.step("Verify that all events without optional params were found in the messages");
        assertThat("Check if all matching events found.", areAllParamsPresentInMessages.matchResult(), equalTo(true));
    }

    @Test
    @DisplayName(
        "Generate open event with any of the required parameters missing from Json and verify that the Event Generator didn't produce the event")
    @AllureId("130")
    void generateOpenTradeEventsWithMandatoryParamsMissingFromJsonTest() throws JsonProcessingException {

        //        Creation of close trade events that should be filtered out by the filtration rules
        OpenTradeMtDbEventMt4 openTradeEventOpenTimeObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventOpenTime = removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventOpenTimeObject), "OPEN_TIME");

        OpenTradeMtDbEventMt4 openTradeEventTradeIdObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventTradeId = removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventTradeIdObject), "TICKET");

        OpenTradeMtDbEventMt4 openTradeEventMtAccountObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventMtAccount = removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventMtAccountObject), "LOGIN");

        OpenTradeMtDbEventMt4 openTradeEventServerIdObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventServerId = removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventServerIdObject), "ServerID");

        OpenTradeMtDbEventMt4 openTradeEventMt4CmdObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventMt4Cmd = removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventMt4CmdObject), "CMD");

        OpenTradeMtDbEventMt5 openTradeEventMt5EntryObject = generateOpenTradeMtDbEventMt5();
        String openTradeEventMt5Entry = removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventMt5EntryObject), "Entry");

        OpenTradeMtDbEventMt5 openTradeEventMt5ActionObject = generateOpenTradeMtDbEventMt5();
        String openTradeEventMt5Action = removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventMt5ActionObject), "Action");

        OpenTradeMtDbEventMt4 openTradeEventTableNameObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventTableName = removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventTableNameObject), "table-name");

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages(KAFKA_MESSAGE_KEY, KAFKA_TOPIC_MT_DB_EVENTS, openTradeEventOpenTime, openTradeEventTradeId, openTradeEventMtAccount, openTradeEventServerId, openTradeEventMt4Cmd, openTradeEventMt5Entry, openTradeEventMt5Action, openTradeEventTableName);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, openTradeEventOpenTimeObject.metadata.timestamp, openTradeEventTradeIdObject.data.openTime, openTradeEventMtAccountObject.data.openTime, openTradeEventServerIdObject.data.openTime, openTradeEventMt4CmdObject.data.openTime, openTradeEventMt5EntryObject.data.openTime, openTradeEventMt5ActionObject.data.openTime, openTradeEventTableNameObject.data.openTime);

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
