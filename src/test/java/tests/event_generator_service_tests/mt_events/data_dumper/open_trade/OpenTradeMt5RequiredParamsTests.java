package tests.event_generator_service_tests.mt_events.data_dumper.open_trade;

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

import static business_objects.kafka.mt_data_dumper_events.OpenTradeFactory.generateOpenTradeDataDumperMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_EVENT_GENERATOR_SERVICE;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_OPEN_TRADE_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class OpenTradeMt5RequiredParamsTests extends TestBaseKafka {

    @Test
    @AllureId("1354")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event not coming if header = null")
    void filtrationMt5OpenTradeEventTest1() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.setHeader(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1362")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest2() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        String login = String.valueOf(openTradeMt5.getPayload().getLogin());
        openTradeMt5.setPayload(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, login);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1361")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with header.operation=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest3() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getHeader().setOperation(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1360")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with header.MsgType=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest4() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getHeader().setMsgType(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1359")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with header.MsgId=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest5() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getHeader().setMsgId(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1358")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with header.ServerId=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest6() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getHeader().setServerId(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1357")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.Time=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest7() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setTime(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1356")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.TimeUtc=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest8() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setTimeUtc(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1355")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.Deal=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest9() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setDeal(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1354")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.Login=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest10() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        String login = String.valueOf(openTradeMt5.getPayload().getLogin());
        openTradeMt5.getPayload().setLogin(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, login);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1353")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.Volume=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest11() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setVolume(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1352")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.Symbol=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest12() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setSymbol(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1351")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.Entry=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest13() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setEntry(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1350")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.Action=null is not processed by EG")
    void filtrationMt5OpenTradeEventTest14() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setAction(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if no any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1349")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.Equity=null is processed by EG")
    void filtrationMt5OpenTradeEventTest15() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setEquity(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), true, equalTo(true));
    }

    @Test
    @AllureId("1348")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.Margin=null is processed by EG")
    void filtrationMt5OpenTradeEventTest16() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setMargin(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), true, equalTo(true));
    }

    @Test
    @AllureId("1347")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.FreeMargin=null is processed by EG")
    void filtrationMt5OpenTradeEventTest17() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setFreeMargin(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), true, equalTo(true));
    }

    @Test
    @AllureId("1346")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.Balance=null is processed by EG")
    void filtrationMt5OpenTradeEventTest18() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setBalance(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), true, equalTo(true));
    }

    @Test
    @AllureId("1345")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event with payload.Leverage=null is processed by EG")
    void filtrationMt5OpenTradeEventTest19() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setLeverage(null);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), true, equalTo(true));
    }
}
