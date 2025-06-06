package tests.event_generator_service_tests.mt_events.data_dumper.open_trade;

import business_objects.kafka.mt_data_dumper_events.TradeEventMt4;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.kafka.MatchResultWithMessage;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseKafka;

import static business_objects.kafka.mt_data_dumper_events.OpenTradeFactory.generateOpenTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_EVENT_GENERATOR_SERVICE;

@Disabled
@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_OPEN_TRADE_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class OpenTradeMt4FiltrationTests extends TestBaseKafka {

    /*
    msg_type = ‘trade_record’
    operation = 0
    mode = 0
    open_time = 0
    cmd IN (0, 1)
    state = 0
     */

    @Test
    @AllureId("1215")
    @Tag("CSV-1280")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 0, mode = 0, open_time = 0, cmd = 0, state = 0")
    void filtrationMt4CloseTradeEventTest1() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgType("trade_record");
        openTradeMt4.getHeader().setOperation(0);
        openTradeMt4.getPayload().setMode(0);
        openTradeMt4.getPayload().setCmd(0);
        openTradeMt4.getPayload().setState(0);
        openTradeMt4.getPayload().setOpenTime(0L);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));

    }

    @Test
    @AllureId("1216")
    @Tag("CSV-1280")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 0, mode = 0, open_time = 0, cmd = 1, state = 0")
    void filtrationMt4CloseTradeEventTest2() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgType("trade_record");
        openTradeMt4.getHeader().setOperation(0);
        openTradeMt4.getPayload().setMode(0);
        openTradeMt4.getPayload().setCmd(1);
        openTradeMt4.getPayload().setState(0);
        openTradeMt4.getPayload().setOpenTime(0L);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));

    }

    /*
    msg_type = ‘trade_record’
    operation = 1
    mode = 1
    open_time = 0
    cmd IN (0, 1)
    state = 0
     */

    @Test
    @AllureId("1217")
    @Tag("CSV-1280")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 1, open_time = 0, cmd = 0, state = 0")
    void filtrationMt4CloseTradeEventTest3() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgType("trade_record");
        openTradeMt4.getHeader().setOperation(1);
        openTradeMt4.getPayload().setMode(1);
        openTradeMt4.getPayload().setCmd(0);
        openTradeMt4.getPayload().setState(0);
        openTradeMt4.getPayload().setOpenTime(0L);


        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));

    }

    @Test
    @AllureId("1218")
    @Tag("CSV-1280")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 1, open_time = 0, cmd = 0, state = 0")
    void filtrationMt4CloseTradeEventTest4() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgType("trade_record");
        openTradeMt4.getHeader().setOperation(1);
        openTradeMt4.getPayload().setMode(1);
        openTradeMt4.getPayload().setCmd(0);
        openTradeMt4.getPayload().setState(0);
        openTradeMt4.getPayload().setOpenTime(0L);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));

    }

    /*
    Filtered out events
     */

    @Test
    @AllureId("1225")
    @Tag("CSV-1280")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record1’, operation = 0, mode = 0, open_time = 0, cmd = 1, state = 0")
    void filtrationMt4CloseTradeEventTest5() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgType("trade_record1");
        openTradeMt4.getHeader().setOperation(0);
        openTradeMt4.getPayload().setMode(0);
        openTradeMt4.getPayload().setCmd(1);
        openTradeMt4.getPayload().setState(0);
        openTradeMt4.getPayload().setOpenTime(0L);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @AllureId("1226")
    @Tag("CSV-1280")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 5, mode = 0, open_time = 0, cmd = 1, state = 0")
    void filtrationMt4CloseTradeEventTest6() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgType("trade_record");
        openTradeMt4.getHeader().setOperation(5);
        openTradeMt4.getPayload().setMode(0);
        openTradeMt4.getPayload().setCmd(1);
        openTradeMt4.getPayload().setState(0);
        openTradeMt4.getPayload().setOpenTime(0L);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @AllureId("1227")
    @Tag("CSV-1280")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 0, mode = 5, open_time = 0, cmd = 1, state = 0")
    void filtrationMt4CloseTradeEventTest7() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgType("trade_record");
        openTradeMt4.getHeader().setOperation(0);
        openTradeMt4.getPayload().setMode(5);
        openTradeMt4.getPayload().setCmd(1);
        openTradeMt4.getPayload().setState(0);
        openTradeMt4.getPayload().setOpenTime(0L);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @AllureId("1228")
    @Tag("CSV-1280")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 0, mode = 0, open_time = 1, cmd = 1, state = 0")
    void filtrationMt4CloseTradeEventTest8() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgType("trade_record");
        openTradeMt4.getHeader().setOperation(0);
        openTradeMt4.getPayload().setMode(0);
        openTradeMt4.getPayload().setCmd(1);
        openTradeMt4.getPayload().setState(0);
        openTradeMt4.getPayload().setOpenTime(1L);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @Tag("CSV-1280")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 0, mode = 0, open_time = 0, cmd = 2, state = 0")
    void filtrationMt4CloseTradeEventTest9() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgType("trade_record");
        openTradeMt4.getHeader().setOperation(0);
        openTradeMt4.getPayload().setMode(0);
        openTradeMt4.getPayload().setCmd(2);
        openTradeMt4.getPayload().setState(0);
        openTradeMt4.getPayload().setOpenTime(0L);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @Tag("CSV-1280")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 0, mode = 0, open_time = 0, cmd = 1, state = 1")
    void filtrationMt4CloseTradeEventTest10() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgType("trade_record");
        openTradeMt4.getHeader().setOperation(0);
        openTradeMt4.getPayload().setMode(0);
        openTradeMt4.getPayload().setCmd(1);
        openTradeMt4.getPayload().setState(1);
        openTradeMt4.getPayload().setOpenTime(0L);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }
}
