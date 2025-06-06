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

import static business_objects.kafka.mt_data_dumper_events.TradeLossFactory.generateTradeLossTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_LOSS_COMPENSATION_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class TradeLossMt4RequiredParamsTests extends TestBaseKafka {

    @Test
    @AllureId("1290")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with header=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest1() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.setHeader(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1291")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with operation=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest2() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getHeader().setOperation(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1292")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with msgType=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest3() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getHeader().setMsgType(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1293")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with msgId=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest4() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getHeader().setMsgId(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1294")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with serverId=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest5() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getHeader().setServerId(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1295")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with payload=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest6() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        String login = String.valueOf(tradeLossCompensationMt4.getPayload().getLogin());
        tradeLossCompensationMt4.setPayload(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, login);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1296")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with CloseTime=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest7() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getPayload().setCloseTime(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1297")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with CloseTimeUtc=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest8() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getPayload().setCloseTimeUtc(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1298")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with order=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest9() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getPayload().setOrder(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1299")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with login=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest10() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        String login = tradeLossCompensationMt4.getPayload().getLogin().toString();
        tradeLossCompensationMt4.getPayload().setLogin(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, login);

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1300")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with volume=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest11() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getPayload().setVolume(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1301")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with symbol=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest12() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getPayload().setSymbol(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1302")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with cmd=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest13() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getPayload().setCmd(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1303")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with mode=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest14() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getPayload().setMode(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1304")
    @Tag("CSV-1280")
    @DisplayName("MT4 trade loss compensation event with comment=null is not processed by EG")
    void requiredParametersMt4TradeLossCompensationEventTest15() throws JsonProcessingException {

        TradeEventMt4 tradeLossCompensationMt4 = generateTradeLossTradeDataDumperMt4();
        tradeLossCompensationMt4.getPayload().setComment(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossCompensationMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossCompensationMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were not found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
