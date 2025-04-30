package tests.event_generator_service_tests.mt_events.close_trade;

import static business_objects.kafka.mt_db_events.close_trade.CloseTradeMtDbEventFactory.generateCloseTradeMtDbEventMt4;
import static business_objects.kafka.mt_db_events.close_trade.CloseTradeMtDbEventFactory.generateCloseTradeMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.removeKeyFromJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import business_objects.kafka.mt_db_events.close_trade.CloseTradeMtDbEventMt4;
import business_objects.kafka.mt_db_events.close_trade.CloseTradeMtDbEventMt5;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_CLOSE_TRADE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class MtDbEventsCloseTradeRequiredParamsTest {

    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Generate close event with any of the required parameters = null and verify that the Event Generator didn't produce the event")
    @AllureId("116")
    void generateCloseTradeEventsWithoutMandatoryParamsTest() throws JsonProcessingException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        //Creation of close trade events that should be filtered out by the filtration rules
        CloseTradeMtDbEventMt4 closeTradeEventCloseTime = generateCloseTradeMtDbEventMt4();
        closeTradeEventCloseTime.data.closeTime = null;

        CloseTradeMtDbEventMt4 closeTradeEventTradeId = generateCloseTradeMtDbEventMt4();
        closeTradeEventTradeId.data.tradeId = null;

        CloseTradeMtDbEventMt4 closeTradeEventMtAccount = generateCloseTradeMtDbEventMt4();
        closeTradeEventMtAccount.data.mtAccount = null;

        CloseTradeMtDbEventMt4 closeTradeEventServerId = generateCloseTradeMtDbEventMt4();
        closeTradeEventServerId.data.serverId = null;

        CloseTradeMtDbEventMt4 closeTradeEventMt4Cmd = generateCloseTradeMtDbEventMt4();
        closeTradeEventMt4Cmd.data.cmd = null;

        CloseTradeMtDbEventMt5 closeTradeEventMt5Entry = generateCloseTradeMtDbEventMt5();
        closeTradeEventMt5Entry.data.entry = null;

        CloseTradeMtDbEventMt5 closeTradeEventMt5Action = generateCloseTradeMtDbEventMt5();
        closeTradeEventMt5Action.data.action = null;

        CloseTradeMtDbEventMt4 closeTradeEventTableName = generateCloseTradeMtDbEventMt4();
        closeTradeEventTableName.metadata.tableName = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages("13", KAFKA_TOPIC_MT_DB_EVENTS, objectMapper.writeValueAsString(closeTradeEventCloseTime), objectMapper.writeValueAsString(closeTradeEventTradeId), objectMapper.writeValueAsString(closeTradeEventMtAccount), objectMapper.writeValueAsString(closeTradeEventServerId), objectMapper.writeValueAsString(closeTradeEventMt4Cmd), objectMapper.writeValueAsString(closeTradeEventMt5Entry), objectMapper.writeValueAsString(closeTradeEventMt5Action), objectMapper.writeValueAsString(closeTradeEventTableName));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, closeTradeEventCloseTime.metadata.timestamp, closeTradeEventTradeId.data.closeTime, closeTradeEventMtAccount.data.closeTime, closeTradeEventServerId.data.closeTime, closeTradeEventMt4Cmd.data.closeTime, closeTradeEventMt5Entry.data.closeTime, closeTradeEventMt5Action.data.closeTime, closeTradeEventTableName.data.closeTime);

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @DisplayName("Generate close event with any of the optional parameters = null and verify that the Event Generator produced the event")
    @AllureId("117")
    void generateCloseTradeEventsWithoutOptionalParamsTest() throws JsonProcessingException {
        //Creation of close trade events that should be filtered out by the filtration rules
        CloseTradeMtDbEventMt4 closeTradeEventVolume = generateCloseTradeMtDbEventMt4();
        closeTradeEventVolume.data.volume = null;

        CloseTradeMtDbEventMt4 closeTradeEventSymbol = generateCloseTradeMtDbEventMt4();
        closeTradeEventSymbol.data.symbol = null;

        CloseTradeMtDbEventMt4 closeTradeEventTimestamp = generateCloseTradeMtDbEventMt4();
        closeTradeEventTimestamp.metadata.timestamp = null;

        CloseTradeMtDbEventMt4 closeTradeEventRecordType = generateCloseTradeMtDbEventMt4();
        closeTradeEventRecordType.metadata.recordType = null;

        CloseTradeMtDbEventMt4 closeTradeEventOperation = generateCloseTradeMtDbEventMt4();
        closeTradeEventOperation.metadata.operation = null;

        CloseTradeMtDbEventMt4 closeTradeEventPartitionKeyType = generateCloseTradeMtDbEventMt4();
        closeTradeEventPartitionKeyType.metadata.partitionKeyType = null;

        CloseTradeMtDbEventMt4 closeTradeEventSchemaName = generateCloseTradeMtDbEventMt4();
        closeTradeEventSchemaName.metadata.schemaName = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages(KAFKA_MESSAGE_KEY, KAFKA_TOPIC_MT_DB_EVENTS, objectMapper.writeValueAsString(closeTradeEventVolume), objectMapper.writeValueAsString(closeTradeEventSymbol), objectMapper.writeValueAsString(closeTradeEventTimestamp), objectMapper.writeValueAsString(closeTradeEventRecordType), objectMapper.writeValueAsString(closeTradeEventOperation), objectMapper.writeValueAsString(closeTradeEventPartitionKeyType), objectMapper.writeValueAsString(closeTradeEventSchemaName));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage areAllParamsPresentInMessages = kafka.areAllParamsPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, closeTradeEventVolume.data.closeTime, closeTradeEventSymbol.data.closeTime, closeTradeEventTimestamp.data.closeTime, closeTradeEventRecordType.data.closeTime, closeTradeEventOperation.data.closeTime, closeTradeEventPartitionKeyType.data.closeTime, closeTradeEventSchemaName.data.closeTime);

        Allure.step("Verify that all events without optional params were found in the messages");
        assertThat("Check if all matching events found", areAllParamsPresentInMessages.matchResult(), equalTo(true));
    }

    @Test
    @DisplayName("Generate close event with any of the required parameters missing from Json and verify that the Event Generator didn't produce the event")
    @AllureId("129")
    void generateCloseTradeEventsWithMandatoryParamsMissingFromJsonTest() throws JsonProcessingException {
        //Creation of close trade events that should be filtered out by the filtration rules
        CloseTradeMtDbEventMt4 closeTradeEventCloseTimeObject = generateCloseTradeMtDbEventMt4();
        String closeTradeEventCloseTime = removeKeyFromJson(objectMapper.writeValueAsString(closeTradeEventCloseTimeObject), "CLOSE_TIME");

        CloseTradeMtDbEventMt4 closeTradeEventTradeIdObject = generateCloseTradeMtDbEventMt4();
        String closeTradeEventTradeId = removeKeyFromJson(objectMapper.writeValueAsString(closeTradeEventTradeIdObject), "TICKET");

        CloseTradeMtDbEventMt4 closeTradeEventMtAccountObject = generateCloseTradeMtDbEventMt4();
        String closeTradeEventMtAccount = removeKeyFromJson(objectMapper.writeValueAsString(closeTradeEventMtAccountObject), "LOGIN");

        CloseTradeMtDbEventMt4 closeTradeEventServerIdObject = generateCloseTradeMtDbEventMt4();
        String closeTradeEventServerId = removeKeyFromJson(objectMapper.writeValueAsString(closeTradeEventServerIdObject), "ServerID");

        CloseTradeMtDbEventMt4 closeTradeEventMt4CmdObject = generateCloseTradeMtDbEventMt4();
        String closeTradeEventMt4Cmd = removeKeyFromJson(objectMapper.writeValueAsString(closeTradeEventMt4CmdObject), "CMD");

        CloseTradeMtDbEventMt5 closeTradeEventMt5EntryObject = generateCloseTradeMtDbEventMt5();
        String closeTradeEventMt5Entry = removeKeyFromJson(objectMapper.writeValueAsString(closeTradeEventMt5EntryObject), "Entry");

        CloseTradeMtDbEventMt5 closeTradeEventMt5ActionObject = generateCloseTradeMtDbEventMt5();
        String closeTradeEventMt5Action = removeKeyFromJson(objectMapper.writeValueAsString(closeTradeEventMt5ActionObject), "Action");

        CloseTradeMtDbEventMt4 closeTradeEventTableNameObject = generateCloseTradeMtDbEventMt4();
        String closeTradeEventTableName = removeKeyFromJson(objectMapper.writeValueAsString(closeTradeEventTableNameObject), "table-name");

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages(KAFKA_MESSAGE_KEY, KAFKA_TOPIC_MT_DB_EVENTS, closeTradeEventCloseTime, closeTradeEventTradeId, closeTradeEventMtAccount, closeTradeEventServerId, closeTradeEventMt4Cmd, closeTradeEventMt5Entry, closeTradeEventMt5Action, closeTradeEventTableName);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, closeTradeEventCloseTimeObject.metadata.timestamp, closeTradeEventTradeIdObject.data.closeTime, closeTradeEventMtAccountObject.data.closeTime, closeTradeEventServerIdObject.data.closeTime, closeTradeEventMt4CmdObject.data.closeTime, closeTradeEventMt5EntryObject.data.closeTime, closeTradeEventMt5ActionObject.data.closeTime, closeTradeEventTableNameObject.data.closeTime);

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
