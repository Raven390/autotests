package tests.event_generator_service_tests.mt_events.data_dumper.open_trade;

import static business_objects.kafka.mt_data_dumper_events.OpenTradeFactory.generateOpenTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_EVENT_GENERATOR_SERVICE;

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

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_OPEN_TRADE_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class OpenTradeMt4RequiredParamsTests extends TestBaseKafka {

    @Test
    @AllureId("1330")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if header=null")
    void requiredParamsMt4CloseTradeEventTest1() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.setHeader(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1331")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if operation=null")
    void requiredParamsMt4CloseTradeEventTest2() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setOperation(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1332")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if msgType=null")
    void requiredParamsMt4CloseTradeEventTest3() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgType(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1333")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if msgId=null")
    void requiredParamsMt4CloseTradeEventTest4() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setMsgId(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1334")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if serverId=null")
    void requiredParamsMt4CloseTradeEventTest5() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getHeader().setServerId(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1335")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if payload=null")
    void requiredParamsMt4CloseTradeEventTest6() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        String login = String.valueOf(openTradeMt4.getPayload().getLogin());
        openTradeMt4.setPayload(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages =
                kafka.isAnyMatchPresentInMessages(KAFKA_TOPIC_MT_EVENTS, login);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1336")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if openTime=null")
    void requiredParamsMt4CloseTradeEventTest7() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getPayload().setOpenTime(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1337")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if openTimeUtc=null")
    void requiredParamsMt4CloseTradeEventTest8() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getPayload().setOpenTimeUtc(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1338")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if order=null")
    void requiredParamsMt4CloseTradeEventTest9() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getPayload().setOrder(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1339")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if login=null")
    void requiredParamsMt4CloseTradeEventTest10() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getPayload().setLogin(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages =
                kafka.isAnyMatchPresentInMessages(KAFKA_TOPIC_MT_EVENTS, "null");

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1340")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if volume=null")
    void requiredParamsMt4CloseTradeEventTest11() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getPayload().setVolume(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1341")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if symbol=null")
    void requiredParamsMt4CloseTradeEventTest12() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getPayload().setSymbol(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1342")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if cmd=null")
    void requiredParamsMt4CloseTradeEventTest13() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getPayload().setCmd(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1343")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if mode=null")
    void requiredParamsMt4CloseTradeEventTest14() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getPayload().setMode(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1344")
    @Tag("CSV-1280")
    @DisplayName("MT4 open trade event not found if state=null")
    void requiredParamsMt4CloseTradeEventTest15() throws JsonProcessingException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        openTradeMt4.getPayload().setState(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }
}
