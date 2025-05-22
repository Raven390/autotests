package tests.event_generator_service_tests.mt_events.data_dumper.close_trade;

import business_objects.kafka.mt_data_dumper_events.close_trade.CloseTradeMt5;
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

import static business_objects.kafka.mt_data_dumper_events.close_trade.CloseTradeFactory.generateCloseTradeDataDumperMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_CLOSE_TRADE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class CloseTradeMt5EventRequiredParametersTests extends TestBaseKafka {

    @Test
    @AllureId("1182")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with header=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest1() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.setHeader(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1181")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with payload=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest2() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        String login = String.valueOf(closeTradeMt5.getPayload().getLogin());
        closeTradeMt5.setPayload(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, login);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1194")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with header.operation=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest3() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getHeader().setOperation(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1195")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with header.MsgType=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest4() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getHeader().setMsgType(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1196")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with header.MsgId=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest5() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getHeader().setMsgId(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1197")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with header.ServerId=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest6() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getHeader().setServerId(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1198")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with payload.Time=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest7() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getPayload().setTime(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1199")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with payload.TimeUtc=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest8() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getPayload().setTimeUtc(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1200")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with payload.Deal=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest9() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getPayload().setDeal(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1201")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with payload.Login=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest10() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        String login = String.valueOf(closeTradeMt5.getPayload().getLogin());
        closeTradeMt5.getPayload().setLogin(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, login);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1202")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with payload.Volume=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest11() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getPayload().setVolume(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1203")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with payload.Symbol=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest12() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getPayload().setSymbol(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1204")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with payload.Entry=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest13() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getPayload().setEntry(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1205")
    @Tag("CSV-1253")
    @DisplayName("MT5 close trade event with payload.Action=null is not processed by EG")
    void requiredParametersMt5CloseTradeEventTest14() throws JsonProcessingException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getPayload().setAction(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }


}
