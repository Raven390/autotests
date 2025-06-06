package tests.event_generator_service_tests.mt_events.data_dumper.trade_loss_compensation;

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

import static business_objects.kafka.mt_data_dumper_events.StopoutFactory.generateStopoutTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_LOSS_COMPENSATION_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class TradeLossMt4FilteringTests extends TestBaseKafka {

    /*
    msg_type = ‘trade_record’
    operation = 1
    mode = 2
    close_time <> 0
    cmd = 6
    comment contains "Trade Loss" or "TLV"
     */

    @Test
    @AllureId("1274")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time > 0, cmd = 6, comment contains 'Trade Loss'")
    void filtrationMt4TradeLossEventTest1() throws JsonProcessingException {

        TradeEventMt4 tradeLossMt4 = generateStopoutTradeDataDumperMt4();
        tradeLossMt4.getHeader().setMsgType("trade_record");
        tradeLossMt4.getHeader().setOperation(1);
        tradeLossMt4.getPayload().setMode(2);
        tradeLossMt4.getPayload().setCloseTime(1L);
        tradeLossMt4.getPayload().setCmd(6);
        tradeLossMt4.getPayload().setComment("Trade Loss");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));
    }

    @Test
    @AllureId("1275")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time < 0, cmd = 6, comment contains 'RLV'")
    void filtrationMt4TradeLossEventTest2() throws JsonProcessingException {

        TradeEventMt4 tradeLossMt4 = generateStopoutTradeDataDumperMt4();
        tradeLossMt4.getHeader().setMsgType("trade_record");
        tradeLossMt4.getHeader().setOperation(1);
        tradeLossMt4.getPayload().setMode(2);
        tradeLossMt4.getPayload().setCloseTime(-1L);
        tradeLossMt4.getPayload().setCmd(6);
        tradeLossMt4.getPayload().setComment("TLV");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));
    }

    @Test
    @AllureId("1276")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss event passing filtering. msg_type = ‘trade_record1’, operation = 1, mode = 2, close_time > 0, cmd = 6, comment contains 'Trade Loss'")
    void filtrationMt4TradeLossEventTest3() throws JsonProcessingException {

        TradeEventMt4 tradeLossMt4 = generateStopoutTradeDataDumperMt4();
        tradeLossMt4.getHeader().setMsgType("trade_record1");
        tradeLossMt4.getHeader().setOperation(1);
        tradeLossMt4.getPayload().setMode(2);
        tradeLossMt4.getPayload().setCloseTime(1L);
        tradeLossMt4.getPayload().setCmd(6);
        tradeLossMt4.getPayload().setComment("Trade Loss");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1277")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss event passing filtering. msg_type = ‘trade_record’, operation = 0, mode = 2, close_time > 0, cmd = 6, comment contains 'Trade Loss'")
    void filtrationMt4TradeLossEventTest4() throws JsonProcessingException {

        TradeEventMt4 tradeLossMt4 = generateStopoutTradeDataDumperMt4();
        tradeLossMt4.getHeader().setMsgType("trade_record");
        tradeLossMt4.getHeader().setOperation(0);
        tradeLossMt4.getPayload().setMode(2);
        tradeLossMt4.getPayload().setCloseTime(1L);
        tradeLossMt4.getPayload().setCmd(6);
        tradeLossMt4.getPayload().setComment("Trade Loss");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1278")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 3, close_time > 0, cmd = 6, comment contains 'Trade Loss'")
    void filtrationMt4TradeLossEventTest5() throws JsonProcessingException {

        TradeEventMt4 tradeLossMt4 = generateStopoutTradeDataDumperMt4();
        tradeLossMt4.getHeader().setMsgType("trade_record");
        tradeLossMt4.getHeader().setOperation(1);
        tradeLossMt4.getPayload().setMode(3);
        tradeLossMt4.getPayload().setCloseTime(1L);
        tradeLossMt4.getPayload().setCmd(6);
        tradeLossMt4.getPayload().setComment("Trade Loss");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1279")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time = 0, cmd = 6, comment contains 'Trade Loss'")
    void filtrationMt4TradeLossEventTest6() throws JsonProcessingException {

        TradeEventMt4 tradeLossMt4 = generateStopoutTradeDataDumperMt4();
        tradeLossMt4.getHeader().setMsgType("trade_record");
        tradeLossMt4.getHeader().setOperation(1);
        tradeLossMt4.getPayload().setMode(2);
        tradeLossMt4.getPayload().setCloseTime(0L);
        tradeLossMt4.getPayload().setCmd(6);
        tradeLossMt4.getPayload().setComment("Trade Loss");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1280")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time > 0, cmd = 7, comment contains 'Trade Loss'")
    void filtrationMt4TradeLossEventTest7() throws JsonProcessingException {

        TradeEventMt4 tradeLossMt4 = generateStopoutTradeDataDumperMt4();
        tradeLossMt4.getHeader().setMsgType("trade_record");
        tradeLossMt4.getHeader().setOperation(1);
        tradeLossMt4.getPayload().setMode(2);
        tradeLossMt4.getPayload().setCloseTime(1L);
        tradeLossMt4.getPayload().setCmd(7);
        tradeLossMt4.getPayload().setComment("Trade Loss");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1281")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time > 0, cmd = 6, comment contains 'Trade Los'")
    void filtrationMt4TradeLossEventTest8() throws JsonProcessingException {

        TradeEventMt4 tradeLossMt4 = generateStopoutTradeDataDumperMt4();
        tradeLossMt4.getHeader().setMsgType("trade_record");
        tradeLossMt4.getHeader().setOperation(1);
        tradeLossMt4.getPayload().setMode(2);
        tradeLossMt4.getPayload().setCloseTime(1L);
        tradeLossMt4.getPayload().setCmd(6);
        tradeLossMt4.getPayload().setComment("Trade Los");

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

}
