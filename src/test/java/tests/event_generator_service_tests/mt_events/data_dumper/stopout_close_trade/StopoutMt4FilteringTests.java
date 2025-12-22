package tests.event_generator_service_tests.mt_events.data_dumper.stopout_close_trade;

import static business_objects.kafka.mt_data_dumper_events.CloseTradeFactory.generateCloseTradeDataDumperMt4;
import static business_objects.kafka.mt_data_dumper_events.StopoutFactory.generateStopoutTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

import business_objects.kafka.mt_data_dumper_events.TradeEventMt4;
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
class StopoutMt4FilteringTests extends TestBaseKafka {

    /*
    msg_type = ‘trade_record’
    operation = 1
    mode = 2
    close_time <> 0
    cmd IN (0, 1)
    comment contains %so%
     */

    @Test
    @AllureId("1229")
    @Tag("CSV-1280")
    @DisplayName(
            "MT4 stopout close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time > 0, cmd = 0, comment contains 'so'")
    void filtrationMt4StopoutCloseTradeEventTest1() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setMsgType("trade_record");
        stopoutCloseTradeMt4.getHeader().setOperation(1);
        stopoutCloseTradeMt4.getPayload().setMode(2);
        stopoutCloseTradeMt4.getPayload().setCloseTime(1L);
        stopoutCloseTradeMt4.getPayload().setCmd(0);
        stopoutCloseTradeMt4.getPayload().setComment("so");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt4),
                KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(true));
    }

    @Test
    @AllureId("1230")
    @Tag("CSV-1280")
    @DisplayName(
            "MT4 stopout close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time > 0, cmd = 1, comment contains 'so'")
    void filtrationMt4StopoutCloseTradeEventTest2() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateCloseTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setMsgType("trade_record");
        stopoutCloseTradeMt4.getHeader().setOperation(1);
        stopoutCloseTradeMt4.getPayload().setMode(2);
        stopoutCloseTradeMt4.getPayload().setCloseTime(1L);
        stopoutCloseTradeMt4.getPayload().setCmd(1);
        stopoutCloseTradeMt4.getPayload().setComment("so");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt4),
                KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(true));
    }

    @Test
    @AllureId("1231")
    @Tag("CSV-1280")
    @DisplayName(
            "MT4 stopout close trade event passing filtering. msg_type = ‘trade_record1’, operation = 1, mode = 2, close_time > 0, cmd = 0, comment contains 'so'")
    void filtrationMt4StopoutCloseTradeEventTest3() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateCloseTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setMsgType("trade_record1");
        stopoutCloseTradeMt4.getHeader().setOperation(1);
        stopoutCloseTradeMt4.getPayload().setMode(2);
        stopoutCloseTradeMt4.getPayload().setCloseTime(1L);
        stopoutCloseTradeMt4.getPayload().setCmd(0);
        stopoutCloseTradeMt4.getPayload().setComment("so");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt4),
                KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1232")
    @Tag("CSV-1280")
    @DisplayName(
            "MT4 stopout close trade event passing filtering. msg_type = ‘trade_record’, operation = 0, mode = 2, close_time > 0, cmd = 0, comment contains 'so'")
    void filtrationMt4StopoutCloseTradeEventTest4() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateCloseTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setMsgType("trade_record");
        stopoutCloseTradeMt4.getHeader().setOperation(0);
        stopoutCloseTradeMt4.getPayload().setMode(2);
        stopoutCloseTradeMt4.getPayload().setCloseTime(1L);
        stopoutCloseTradeMt4.getPayload().setCmd(0);
        stopoutCloseTradeMt4.getPayload().setComment("so");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt4),
                KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1233")
    @Tag("CSV-1280")
    @DisplayName(
            "MT4 stopout close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 3, close_time > 0, cmd = 0, comment contains 'so'")
    void filtrationMt4StopoutCloseTradeEventTest5() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateCloseTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setMsgType("trade_record");
        stopoutCloseTradeMt4.getHeader().setOperation(1);
        stopoutCloseTradeMt4.getPayload().setMode(3);
        stopoutCloseTradeMt4.getPayload().setCloseTime(1L);
        stopoutCloseTradeMt4.getPayload().setCmd(0);
        stopoutCloseTradeMt4.getPayload().setComment("so");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt4),
                KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1234")
    @Tag("CSV-1280")
    @DisplayName(
            "MT4 stopout close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time = 0, cmd = 0, comment contains 'so'")
    void filtrationMt4StopoutCloseTradeEventTest6() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateCloseTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setMsgType("trade_record");
        stopoutCloseTradeMt4.getHeader().setOperation(1);
        stopoutCloseTradeMt4.getPayload().setMode(2);
        stopoutCloseTradeMt4.getPayload().setCloseTime(0L);
        stopoutCloseTradeMt4.getPayload().setCmd(0);
        stopoutCloseTradeMt4.getPayload().setComment("so");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt4),
                KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1235")
    @Tag("CSV-1280")
    @DisplayName(
            "MT4 stopout close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time > 0, cmd = 2, comment contains 'so'")
    void filtrationMt4StopoutCloseTradeEventTest7() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateCloseTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setMsgType("trade_record");
        stopoutCloseTradeMt4.getHeader().setOperation(1);
        stopoutCloseTradeMt4.getPayload().setMode(2);
        stopoutCloseTradeMt4.getPayload().setCloseTime(1L);
        stopoutCloseTradeMt4.getPayload().setCmd(2);
        stopoutCloseTradeMt4.getPayload().setComment("so");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt4),
                KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1236")
    @Tag("CSV-1280")
    @DisplayName(
            "MT4 stopout close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time > 0, cmd = 0, comment contains 'so1'")
    void filtrationMt4StopoutCloseTradeEventTest8() throws JsonProcessingException, InterruptedException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateCloseTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setMsgType("trade_record");
        stopoutCloseTradeMt4.getHeader().setOperation(1);
        stopoutCloseTradeMt4.getPayload().setMode(2);
        stopoutCloseTradeMt4.getPayload().setCloseTime(1L);
        stopoutCloseTradeMt4.getPayload().setCmd(0);
        stopoutCloseTradeMt4.getPayload().setComment("so1");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt4),
                KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(
                KAFKA_TOPIC_MT_EVENTS,
                String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()),
                true);
        TradeEvent retrievedStopoutTradeMtEvent = objectMapper.readValue(consumedMessage.message(), TradeEvent.class);
        assertThat("Check type", retrievedStopoutTradeMtEvent.type, equalTo("closeTrade"));
    }
}
