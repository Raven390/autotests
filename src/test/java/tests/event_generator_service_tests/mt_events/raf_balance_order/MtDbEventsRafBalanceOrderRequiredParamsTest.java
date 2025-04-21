package tests.event_generator_service_tests.mt_events.raf_balance_order;

import static business_objects.kafka.mt_db_events.raf_balance_order.RafBalanceOrderMtDbEventFactory.generateRafBalanceOrderMtDbEventMt4;
import static business_objects.kafka.mt_db_events.raf_balance_order.RafBalanceOrderMtDbEventFactory.generateRafBalanceOrderMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.removeKeyFromJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import business_objects.kafka.mt_db_events.raf_balance_order.RafBalanceOrderMtDbEventMt4;
import business_objects.kafka.mt_db_events.raf_balance_order.RafBalanceOrderMtDbEventMt5;
import io.qameta.allure.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_RAF_BALANCE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
@Disabled
@Tag(TAG_MANUAL)
class MtDbEventsRafBalanceOrderRequiredParamsTest {

    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Generate RAF balance order event with any of the required parameters = null and verify that the Event Generator didn't produce the event")
    @AllureId("126")
    void generateRafBalanceOrderEventsWithoutMandatoryParamsTest() throws JsonProcessingException {

        //Creation of Raf balance order events that should be filtered out by the filtration rules
        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventOpenTime = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventOpenTime.getData().setComment("test_comment");
        rafBalanceOrderEventOpenTime.getData().setOpenTime(null);

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTradeId = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventTradeId.getData().setComment("test_comment");
        rafBalanceOrderEventTradeId.getData().setTradeId(null);

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventMtAccount = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventMtAccount.getData().setComment("test_comment");
        rafBalanceOrderEventMtAccount.getData().setMtAccount(null);

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventComment = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventComment.getData().setComment("test_comment");
        rafBalanceOrderEventComment.getData().setComment(null);

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventMt4Cmd = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventMt4Cmd.getData().setComment("test_comment");
        rafBalanceOrderEventMt4Cmd.getData().setCmd(null);

        RafBalanceOrderMtDbEventMt5 rafBalanceOrderEventMt5Action = generateRafBalanceOrderMtDbEventMt5();
        rafBalanceOrderEventMt5Action.getData().setComment("test_comment");
        rafBalanceOrderEventMt5Action.getData().setAction(null);

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTableName = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventTableName.getData().setComment("test_comment");
        rafBalanceOrderEventTableName.getMetadata().setTableName(null);

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages(KAFKA_MESSAGE_KEY, KAFKA_TOPIC_MT_DB_EVENTS, objectMapper.writeValueAsString(rafBalanceOrderEventOpenTime), objectMapper.writeValueAsString(rafBalanceOrderEventTradeId), objectMapper.writeValueAsString(rafBalanceOrderEventMtAccount), objectMapper.writeValueAsString(rafBalanceOrderEventComment), objectMapper.writeValueAsString(rafBalanceOrderEventMt4Cmd), objectMapper.writeValueAsString(rafBalanceOrderEventMt5Action), objectMapper.writeValueAsString(rafBalanceOrderEventTableName)
        );

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderEventOpenTime.getMetadata().getTimestamp(), rafBalanceOrderEventTradeId.getData().getOpenTime(), rafBalanceOrderEventMtAccount.getData().getOpenTime(), rafBalanceOrderEventComment.getData().getOpenTime(), rafBalanceOrderEventMt4Cmd.getData().getOpenTime(), rafBalanceOrderEventMt5Action.getData().getOpenTime(), rafBalanceOrderEventTableName.getData().getOpenTime());

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @DisplayName("Generate RAF balance order event with any of the optional parameters = null and verify that the Event Generator produced the event")
    @AllureId("127")
    void generateRafBalanceOrderEventsWithoutOptionalParamsTest() throws JsonProcessingException {

        // Creation of Raf balance order events that should be filtered out by the filtration rules
        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTimestamp = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventTimestamp.getData().setComment("test_comment");
        rafBalanceOrderEventTimestamp.getMetadata().setTimestamp(null);

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventRecordType = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventRecordType.getData().setComment("test_comment");
        rafBalanceOrderEventRecordType.getMetadata().setRecordType(null);

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventOperation = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventOperation.getData().setComment("test_comment");
        rafBalanceOrderEventOperation.getMetadata().setOperation(null);

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventPartitionKeyType = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventPartitionKeyType.getData().setComment("test_comment");
        rafBalanceOrderEventPartitionKeyType.getMetadata().setPartitionKeyType(null);

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventSchemaName = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventSchemaName.getData().setComment("test_comment");
        rafBalanceOrderEventSchemaName.getMetadata().setSchemaName(null);

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages(KAFKA_MESSAGE_KEY, KAFKA_TOPIC_MT_DB_EVENTS, objectMapper.writeValueAsString(rafBalanceOrderEventTimestamp), objectMapper.writeValueAsString(rafBalanceOrderEventRecordType), objectMapper.writeValueAsString(rafBalanceOrderEventOperation), objectMapper.writeValueAsString(rafBalanceOrderEventPartitionKeyType), objectMapper.writeValueAsString(rafBalanceOrderEventSchemaName));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage areAllParamsPresentInMessages = kafka.areAllParamsPresentInMessages(KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderEventTimestamp.getData().getOpenTime(), rafBalanceOrderEventRecordType.getData().getOpenTime(), rafBalanceOrderEventOperation.getData().getOpenTime(), rafBalanceOrderEventPartitionKeyType.getData().getOpenTime(), rafBalanceOrderEventSchemaName.getData().getOpenTime());

        Allure.step("Verify that all events without optional params were found in the messages");
        assertThat("Check if all matching events found", areAllParamsPresentInMessages.matchResult(), equalTo(true));
    }

    @Test
    @DisplayName("Generate RAF balance order event with any of the required parameters missing from Json and verify that the Event Generator didn't produce the event")
    @AllureId("131")
    void generateRafBalanceOrderEventsWithMandatoryParamsMissingFromJsonTest() throws JsonProcessingException {

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
        kafka.produceMessages(KAFKA_MESSAGE_KEY, KAFKA_TOPIC_MT_DB_EVENTS, rafBalanceOrderEventOpenTime, rafBalanceOrderEventTradeId, rafBalanceOrderEventMtAccount, rafBalanceOrderEventServerId, rafBalanceOrderEventMt4Cmd, rafBalanceOrderEventMt5Action, rafBalanceOrderEventTableName, rafBalanceOrderEventComment);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderEventOpenTimeObject.getMetadata().getTimestamp(), rafBalanceOrderEventTradeIdObject.getData().getOpenTime(), rafBalanceOrderEventMtAccountObject.getData().getOpenTime(), rafBalanceOrderEventServerIdObject.getData().getOpenTime(), rafBalanceOrderEventMt4CmdObject.getData().getOpenTime(), rafBalanceOrderEventMt5ActionObject.getData().getOpenTime(), rafBalanceOrderEventTableNameObject.getData().getOpenTime(), rafBalanceOrderEventCommentObject.getData().getOpenTime()
        );

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat("Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
