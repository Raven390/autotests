package tests.event_generator_service_tests.mt_events.data_dumper.close_trade;

import static business_objects.kafka.mt_data_dumper_events.CloseTradeFactory.generateCloseTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

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
@Story(STORY_DATA_DUMPER_CLOSE_TRADE_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class CloseTradeMt4EventRequiredParametersTests extends TestBaseKafka {

    @Test
    @AllureId("1179")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with header=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest1() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.setHeader(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1180")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with payload=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest2() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        String login = String.valueOf(closeTradeMt4.getPayload().getLogin());
        closeTradeMt4.setPayload(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

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
    @AllureId("1183")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with header.msgId=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest3() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getHeader().setMsgId(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1183")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with header.operation=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest4() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getHeader().setOperation(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1184")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with header.msg_type=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest5() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getHeader().setMsgType(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1185")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with header.server_id=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest6() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getHeader().setServerId(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1186")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with payload.close_time=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest7() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getPayload().setCloseTime(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1187")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with payload.close_time_utc=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest8() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getPayload().setCloseTimeUtc(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1188")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with payload.order=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest9() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getPayload().setOrder(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1189")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with payload.login=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest10() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getPayload().setLogin(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1190")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with payload.volume=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest11() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getPayload().setVolume(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1191")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with payload.symbol=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest12() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getPayload().setSymbol(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1192")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with payload.cmd=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest13() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getPayload().setCmd(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @AllureId("1193")
    @Tag("CSV-1253")
    @DisplayName("MT4 close trade event with payload.mode=null is not processed by EG")
    void requiredParametersMt4CloseTradeEventTest19() throws JsonProcessingException {

        TradeEventMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        closeTradeMt4.getPayload().setMode(null);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }
}
