package tests.event_generator_service_tests.mt_events.data_dumper.stopout_close_trade;

import static business_objects.kafka.mt_data_dumper_events.StopoutFactory.generateStopoutTradeDataDumperMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

import business_objects.kafka.mt_data_dumper_events.TradeEventMt5;
import business_objects.kafka.mt_events.TradeEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.kafka.MatchResultWithMessage;
import helpers.kafka.MessageWithHeaders;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseKafka;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_STOP_OUT_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class StopoutMt5FilteringTests extends TestBaseKafka {

    /*
    MsgType = ‘DealPerform’
    Operation = 0
    Entry IN (1, 3)
    Action IN (0, 1)
    comment contains %so%
     */

    @Test
    @AllureId("1252")
    @Tag("CSV-1280")
    @DisplayName(
            "MT5 close trade event passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 1, Action=0, Comment contains 'so'")
    void filtrationMt5StopoutCloseTradeEventTest1() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setOperation(0);
        stopoutCloseTradeMt5.getPayload().setEntry(1);
        stopoutCloseTradeMt5.getPayload().setAction(0);
        stopoutCloseTradeMt5.getPayload().setComment("so");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt5),
                KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(true));
    }

    @Test
    @AllureId("1253")
    @Tag("CSV-1280")
    @DisplayName(
            "MT5 close trade event passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 3, Action=1, Comment contains 'so'")
    void filtrationMt5StopoutCloseTradeEventTest2() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setOperation(0);
        stopoutCloseTradeMt5.getPayload().setEntry(3);
        stopoutCloseTradeMt5.getPayload().setAction(1);
        stopoutCloseTradeMt5.getPayload().setComment("so");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt5),
                KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(true));
    }

    @Test
    @AllureId("1254")
    @Tag("CSV-1280")
    @DisplayName(
            "MT5 close trade event passing filtering. MsgType = ‘DealPerform1’, Operation = 0, Entry = 3, Action=1, Comment contains 'so'")
    void filtrationMt5StopoutCloseTradeEventTest3() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setMsgType("DealPerform1");
        stopoutCloseTradeMt5.getHeader().setOperation(0);
        stopoutCloseTradeMt5.getPayload().setEntry(3);
        stopoutCloseTradeMt5.getPayload().setAction(1);
        stopoutCloseTradeMt5.getPayload().setComment("so");

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt5),
                KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1255")
    @Tag("CSV-1280")
    @DisplayName(
            "MT5 close trade event passing filtering. MsgType = ‘DealPerform’, Operation = 1, Entry = 3, Action=1, Comment contains 'so'")
    void filtrationMt5StopoutCloseTradeEventTest4() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setOperation(1);
        stopoutCloseTradeMt5.getPayload().setEntry(3);
        stopoutCloseTradeMt5.getPayload().setAction(1);
        stopoutCloseTradeMt5.getPayload().setComment("so");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt5),
                KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1256")
    @Tag("CSV-1280")
    @DisplayName(
            "MT5 close trade event passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 2, Action=1, Comment contains 'so'")
    void filtrationMt5StopoutCloseTradeEventTest5() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setOperation(0);
        stopoutCloseTradeMt5.getPayload().setEntry(2);
        stopoutCloseTradeMt5.getPayload().setAction(1);
        stopoutCloseTradeMt5.getPayload().setComment("so");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt5),
                KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1257")
    @Tag("CSV-1280")
    @DisplayName(
            "MT5 close trade event passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 3, Action=2, Comment contains 'so'")
    void filtrationMt5StopoutCloseTradeEventTest6() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setOperation(0);
        stopoutCloseTradeMt5.getPayload().setEntry(3);
        stopoutCloseTradeMt5.getPayload().setAction(2);
        stopoutCloseTradeMt5.getPayload().setComment("so");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt5),
                KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1258")
    @Tag("CSV-1280")
    @DisplayName(
            "MT5 close trade event passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 3, Action=2, Comment = '123'")
    void filtrationMt5StopoutCloseTradeEventTest7() throws JsonProcessingException, InterruptedException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setOperation(0);
        stopoutCloseTradeMt5.getPayload().setEntry(1);
        stopoutCloseTradeMt5.getPayload().setAction(0);
        stopoutCloseTradeMt5.getPayload().setComment("123");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt5),
                KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()),
                true);
        TradeEvent retrievedStopoutTradeMtEvent = objectMapper.readValue(consumedMessage.message(), TradeEvent.class);
        assertThat("Check type", retrievedStopoutTradeMtEvent.type, equalTo("closeTrade"));
    }
}
