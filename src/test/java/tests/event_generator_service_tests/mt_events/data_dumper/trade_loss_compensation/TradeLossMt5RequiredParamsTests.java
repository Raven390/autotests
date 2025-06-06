package tests.event_generator_service_tests.mt_events.data_dumper.trade_loss_compensation;

import business_objects.kafka.mt_data_dumper_events.TradeEventMt5;
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

import static business_objects.kafka.mt_data_dumper_events.TradeLossFactory.generateTradeLossTradeDataDumperMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_LOSS_COMPENSATION_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class TradeLossMt5RequiredParamsTests extends TestBaseKafka {

    @Test
    @AllureId("1305")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with header=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest1() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.setHeader(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1306")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with msgType=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest2() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setMsgType(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1307")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with operation=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest3() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setOperation(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1308")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with MsgId=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest4() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setMsgId(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1309")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with serverId=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest5() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getHeader().setServerId(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1310")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with payload=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest6() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        String login = stopoutCloseTradeMt5.getPayload().getLogin().toString();
        stopoutCloseTradeMt5.setPayload(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, login);

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1311")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with time=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest7() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setTime(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1312")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with timeUtc=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest8() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setTimeUtc(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1313")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with deal=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest9() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setDeal(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1314")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with login=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest10() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setLogin(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1315")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with volume=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest11() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setVolume(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1316")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with symbol=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest12() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setSymbol(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1317")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with entry=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest13() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setEntry(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1318")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with action=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest14() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setAction(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1319")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss compensation event with comment=null is not processed by EG")
    void requiredParametersMt5TradeLossCompensationEventTest15() throws JsonProcessingException {

        TradeEventMt5 stopoutCloseTradeMt5 = generateTradeLossTradeDataDumperMt5();
        stopoutCloseTradeMt5.getPayload().setComment(null);

        Allure.step("Write message to mt5Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutCloseTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutCloseTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
