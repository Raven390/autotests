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
class StopoutMt5RequiredParamsTests extends TestBaseKafka {

    @Test
    @AllureId("1259")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with header=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest1() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.setHeader(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1260")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with msgType=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest2() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setMsgType(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1261")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with operation=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest3() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setOperation(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1262")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with MsgId=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest4() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setMsgId(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1263")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with serverId=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest5() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setServerId(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1264")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with payload=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest6() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        String login = stopoutCloseTradeMt5.getPayload().getLogin().toString();
        stopoutCloseTradeMt5.setPayload(null);

        Allure.step("Write message to mt5DealPerform topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY,
                objectMapper.writeValueAsString(stopoutCloseTradeMt5),
                KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages =
                kafka.isAnyMatchPresentInMessages(KAFKA_TOPIC_MT_EVENTS, login);

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1265")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with time=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest7() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setTime(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1266")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with timeUtc=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest8() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setTimeUtc(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1267")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with deal=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest9() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setDeal(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1268")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with login=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest10() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setLogin(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1269")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with volume=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest11() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setVolume(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1270")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with symbol=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest12() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setSymbol(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1271")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with entry=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest13() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setEntry(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1272")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with action=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest14() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setAction(null);

        Allure.step("Write message to mt5DealPerform topic");
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
    @AllureId("1273")
    @Tag("CSV-1280")
    @DisplayName("MT5 stopout close trade event with comment=null is not processed by EG")
    void requiredParametersMt5StopoutCloseTradeEventTest15() throws JsonProcessingException, InterruptedException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateStopoutTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setComment(null);

        Allure.step("Write message to mt5DealPerform topic");
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
