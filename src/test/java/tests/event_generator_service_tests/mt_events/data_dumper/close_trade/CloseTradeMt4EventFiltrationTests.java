package tests.event_generator_service_tests.mt_events.data_dumper.close_trade;

import business_objects.kafka.mt_data_dumper_events.TradeEventMt4;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.kafka.MatchResultWithMessage;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseKafka;

import static business_objects.kafka.mt_data_dumper_events.CloseTradeFactory.generateCloseTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_EVENT_GENERATOR_SERVICE;
import static utils.Utils.writeLog;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_CLOSE_TRADE_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class CloseTradeMt4EventFiltrationTests extends TestBaseKafka {

    /*
    Algorithm for filtering trades
    msg_type = ‘trade_record’
    operation = 1
    mode = 2
    close_time <> 0
    cmd IN (0, 1)
     */

    @Test
    @Tag("CSV-1253")
    @AllureId("1161")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time > 0, cmd = 0")
    void filtrationMt4CloseTradeEventTest1() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getHeader().setMsgType("trade_record");
        closeTradeMt4.getHeader().setOperation(1);
        closeTradeMt4.getPayload().setMode(2);
        closeTradeMt4.getPayload().setCloseTime(1L);
        closeTradeMt4.getPayload().setCmd(0);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));

    }

    @Test
    @Tag("CSV-1253")
    @AllureId("1162")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time > 0, cmd = 1")
    void filtrationMt4CloseTradeEventTest2() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getHeader().setMsgType("trade_record");
        closeTradeMt4.getHeader().setOperation(1);
        closeTradeMt4.getPayload().setMode(2);
        closeTradeMt4.getPayload().setCloseTime(1L);
        closeTradeMt4.getPayload().setCmd(1);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));

    }

    @Test
    @Tag("CSV-1253")
    @AllureId("1163")
    @DisplayName("MT4 close trade event NOT passing filtering. msg_type != ‘trade_record’")
    void filtrationMt4CloseTradeEventTest3() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getHeader().setMsgType("tradeRecord");
        closeTradeMt4.getHeader().setOperation(1);
        closeTradeMt4.getPayload().setMode(2);
        closeTradeMt4.getPayload().setCloseTime(1L);
        closeTradeMt4.getPayload().setCmd(1);

        writeLog(closeTradeMt4);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @Tag("CSV-1253")
    @AllureId("1164")
    @DisplayName("MT4 close trade event NOT passing filtering. operation != 1")
    void filtrationMt4CloseTradeEventTest4() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getHeader().setMsgType("trade_record");
        closeTradeMt4.getHeader().setOperation(2);
        closeTradeMt4.getPayload().setMode(2);
        closeTradeMt4.getPayload().setCloseTime(1L);
        closeTradeMt4.getPayload().setCmd(1);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @Tag("CSV-1253")
    @AllureId("1165")
    @DisplayName("MT4 close trade event NOT passing filtering. mode != 2")
    void filtrationMt4CloseTradeEventTest5() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getHeader().setMsgType("trade_record");
        closeTradeMt4.getHeader().setOperation(1);
        closeTradeMt4.getPayload().setMode(1);
        closeTradeMt4.getPayload().setCloseTime(1L);
        closeTradeMt4.getPayload().setCmd(1);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @Tag("CSV-1253")
    @AllureId("1166")
    @DisplayName("MT4 close trade event NOT passing filtering. close_time = 0")
    void filtrationMt4CloseTradeEventTest6() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getHeader().setMsgType("trade_record");
        closeTradeMt4.getHeader().setOperation(1);
        closeTradeMt4.getPayload().setMode(2);
        closeTradeMt4.getPayload().setCloseTime(0L);
        closeTradeMt4.getPayload().setCmd(1);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @Tag("CSV-1253")
    @AllureId("1167")
    @DisplayName("MT4 close trade event NOT passing filtering. cmd NOT IN (0, 1)")
    void filtrationMt4CloseTradeEventTest7() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getHeader().setMsgType("trade_record");
        closeTradeMt4.getHeader().setOperation(1);
        closeTradeMt4.getPayload().setMode(2);
        closeTradeMt4.getPayload().setCloseTime(1L);
        closeTradeMt4.getPayload().setCmd(2);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
