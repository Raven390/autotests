package tests.event_generator_service_tests.mt_events.data_dumper.stopout_close_trade;

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

import static business_objects.kafka.mt_data_dumper_events.StopoutFactory.generateStopoutTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_STOP_OUT_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class StopoutMt4RequiredParamsTests extends TestBaseKafka {

    @Test
    @AllureId("1237")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with header=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest1() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.setHeader(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1238")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with operation=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest2() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setOperation(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1239")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with msgType=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest3() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setMsgType(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1240")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with msgId=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest4() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setMsgId(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1241")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with serverId=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest5() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getHeader().setServerId(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1242")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with payload=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest6() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        String login = String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin());
        stopoutCloseTradeMt4.setPayload(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, login);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1243")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with CloseTime=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest7() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getPayload().setCloseTime(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1244")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with CloseTimeUtc=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest8() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getPayload().setCloseTimeUtc(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1245")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with order=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest9() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getPayload().setOrder(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1246")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with login=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest10() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getPayload().setOrder(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1247")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with volume=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest11() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getPayload().setVolume(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1248")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with symbol=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest12() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getPayload().setSymbol(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1249")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with cmd=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest13() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getPayload().setCmd(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1250")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with mode=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest14() throws JsonProcessingException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getPayload().setMode(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1251")
    @Tag("CSV-1280")
    @DisplayName("MT4 stopout close trade event with comment=null is not processed by EG")
    void requiredParametersMt4StopoutCloseTradeEventTest15() throws JsonProcessingException, InterruptedException {

        TradeEventMt4 stopoutCloseTradeMt4 = generateStopoutTradeDataDumperMt4();
        stopoutCloseTradeMt4.getPayload().setComment(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt4.getPayload().getLogin()), true);
        TradeEvent retrievedStopoutTradeMtEvent = objectMapper.readValue(consumedMessage.message(), TradeEvent.class);
        assertThat("Check type", retrievedStopoutTradeMtEvent.type, equalTo("closeTrade"));
    }
}