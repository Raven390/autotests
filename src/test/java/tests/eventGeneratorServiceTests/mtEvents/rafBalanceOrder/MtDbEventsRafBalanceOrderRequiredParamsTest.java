package tests.eventGeneratorServiceTests.mtEvents.rafBalanceOrder;

import static businessObjects.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventFactory.generateRafBalanceOrderMtDbEventMt4;
import static businessObjects.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventFactory.generateRafBalanceOrderMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.removeKeyFromJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import businessObjects.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventMt4;
import businessObjects.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventMt5;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_RAF_BALANCE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
public class MtDbEventsRafBalanceOrderRequiredParamsTest {

    @Test
    @DisplayName(
        "Generate RAF balance order event with any of the required parameters = null and verify that the Event Generator didn't produce the event")
    @AllureId("126")
    public void generateRafBalanceOrderEventsWithoutMandatoryParamsTest() throws JsonProcessingException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        //        Creation of Raf balance order events that should be filtered out by the filtration rules
        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventOpenTime = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventOpenTime.data.openTime = null;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTradeId = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventTradeId.data.tradeId = null;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventMtAccount = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventMtAccount.data.mtAccount = null;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventComment = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventComment.data.comment = null;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventMt4Cmd = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventMt4Cmd.data.cmd = null;

        RafBalanceOrderMtDbEventMt5 rafBalanceOrderEventMt5Action = generateRafBalanceOrderMtDbEventMt5();
        rafBalanceOrderEventMt5Action.data.action = null;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTableName = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventTableName.metadata.tableName = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages("13", KAFKA_TOPIC_MT_DB_EVENTS, objectMapper.writeValueAsString(rafBalanceOrderEventOpenTime), objectMapper.writeValueAsString(rafBalanceOrderEventTradeId), objectMapper.writeValueAsString(rafBalanceOrderEventMtAccount), objectMapper.writeValueAsString(rafBalanceOrderEventComment), objectMapper.writeValueAsString(rafBalanceOrderEventMt4Cmd), objectMapper.writeValueAsString(rafBalanceOrderEventMt5Action), objectMapper.writeValueAsString(rafBalanceOrderEventTableName));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderEventOpenTime.metadata.timestamp, rafBalanceOrderEventTradeId.data.openTime, rafBalanceOrderEventMtAccount.data.openTime, rafBalanceOrderEventComment.data.openTime, rafBalanceOrderEventMt4Cmd.data.openTime, rafBalanceOrderEventMt5Action.data.openTime, rafBalanceOrderEventTableName.data.openTime);

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @DisplayName(
        "Generate RAF balance order event with any of the optional parameters = null and verify that the Event Generator produced the event")
    @AllureId("127")
    public void generateRafBalanceOrderEventsWithoutOptionalParamsTest() throws JsonProcessingException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        //        Creation of Raf balance order events that should be filtered out by the filtration rules

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTimestamp = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventTimestamp.metadata.timestamp = null;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventRecordType = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventRecordType.metadata.recordType = null;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventOperation = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventOperation.metadata.operation = null;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventPartitionKeyType = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventPartitionKeyType.metadata.partitionKeyType = null;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventSchemaName = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventSchemaName.metadata.schemaName = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages("13", KAFKA_TOPIC_MT_DB_EVENTS, objectMapper.writeValueAsString(rafBalanceOrderEventTimestamp), objectMapper.writeValueAsString(rafBalanceOrderEventRecordType), objectMapper.writeValueAsString(rafBalanceOrderEventOperation), objectMapper.writeValueAsString(rafBalanceOrderEventPartitionKeyType), objectMapper.writeValueAsString(rafBalanceOrderEventSchemaName));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage areAllParamsPresentInMessages = kafka.areAllParamsPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderEventTimestamp.data.openTime, rafBalanceOrderEventRecordType.data.openTime, rafBalanceOrderEventOperation.data.openTime, rafBalanceOrderEventPartitionKeyType.data.openTime, rafBalanceOrderEventSchemaName.data.openTime);

        Allure.step("Verify that all events without optional params were found in the messages");
        assertThat("Check if all matching events found", areAllParamsPresentInMessages.matchResult(), equalTo(true));
    }

    @Test
    @DisplayName(
        "Generate RAF balance order event with any of the required parameters missing from Json and verify that the Event Generator didn't produce the event")
    @AllureId("131")
    public void generateRafBalanceOrderEventsWithMandatoryParamsMissingFromJsonTest() throws JsonProcessingException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        //        Creation of close trade events that should be filtered out by the filtration rules
        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventOpenTimeObject = generateRafBalanceOrderMtDbEventMt4();
        String rafBalanceOrderEventOpenTime = removeKeyFromJson(objectMapper.writeValueAsString(rafBalanceOrderEventOpenTimeObject), "OPEN_TIME");

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTradeIdObject = generateRafBalanceOrderMtDbEventMt4();
        String rafBalanceOrderEventTradeId = removeKeyFromJson(objectMapper.writeValueAsString(rafBalanceOrderEventTradeIdObject), "TICKET");

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventMtAccountObject = generateRafBalanceOrderMtDbEventMt4();
        String rafBalanceOrderEventMtAccount = removeKeyFromJson(objectMapper.writeValueAsString(rafBalanceOrderEventMtAccountObject), "LOGIN");

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventServerIdObject = generateRafBalanceOrderMtDbEventMt4();
        String rafBalanceOrderEventServerId = removeKeyFromJson(objectMapper.writeValueAsString(rafBalanceOrderEventServerIdObject), "ServerID");

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventMt4CmdObject = generateRafBalanceOrderMtDbEventMt4();
        String rafBalanceOrderEventMt4Cmd = removeKeyFromJson(objectMapper.writeValueAsString(rafBalanceOrderEventMt4CmdObject), "CMD");

        RafBalanceOrderMtDbEventMt5 rafBalanceOrderEventMt5ActionObject = generateRafBalanceOrderMtDbEventMt5();
        String rafBalanceOrderEventMt5Action = removeKeyFromJson(objectMapper.writeValueAsString(rafBalanceOrderEventMt5ActionObject), "Action");

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTableNameObject = generateRafBalanceOrderMtDbEventMt4();
        String rafBalanceOrderEventTableName = removeKeyFromJson(objectMapper.writeValueAsString(rafBalanceOrderEventTableNameObject), "table-name");

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventCommentObject = generateRafBalanceOrderMtDbEventMt4();
        String rafBalanceOrderEventComment = removeKeyFromJson(objectMapper.writeValueAsString(rafBalanceOrderEventCommentObject), "COMMENT");

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages("13", KAFKA_TOPIC_MT_DB_EVENTS, rafBalanceOrderEventOpenTime, rafBalanceOrderEventTradeId, rafBalanceOrderEventMtAccount, rafBalanceOrderEventServerId, rafBalanceOrderEventMt4Cmd, rafBalanceOrderEventMt5Action, rafBalanceOrderEventTableName, rafBalanceOrderEventComment);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderEventOpenTimeObject.metadata.timestamp, rafBalanceOrderEventTradeIdObject.data.openTime, rafBalanceOrderEventMtAccountObject.data.openTime, rafBalanceOrderEventServerIdObject.data.openTime, rafBalanceOrderEventMt4CmdObject.data.openTime, rafBalanceOrderEventMt5ActionObject.data.openTime, rafBalanceOrderEventTableNameObject.data.openTime, rafBalanceOrderEventCommentObject.data.openTime);

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
