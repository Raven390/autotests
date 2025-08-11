package tests.event_generator_service_tests.mt_events.data_dumper.close_trade;

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

import static business_objects.kafka.mt_data_dumper_events.CloseTradeFactory.generateCloseTradeDataDumperMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_CLOSE_TRADE_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class CloseTradeMt5EventFiltrationTests extends TestBaseKafka {

    /*
    Algorithm for filtering trades
    MsgType = ‘DealPerform’
    Operation = 0
    Entry IN (1, 3)
    Action IN (0, 1)
     */

    @Test
    @Tag("CSV-1253")
    @AllureId("1168")
    @DisplayName("MT5 close trade event passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 1, Action=0")
    void filtrationMt5CloseTradeEventTest1() throws JsonProcessingException {

        TradeEventMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getHeader().setOperation(0);
        closeTradeMt5.getPayload().setEntry(1);
        closeTradeMt5.getPayload().setAction(0);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));

    }

    @Test
    @Tag("CSV-1253")
    @AllureId("1173")
    @DisplayName("MT5 close trade event passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 3, Action=1")
    void filtrationMt5CloseTradeEventTest6() throws JsonProcessingException {

        TradeEventMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getHeader().setOperation(0);
        closeTradeMt5.getPayload().setEntry(3);
        closeTradeMt5.getPayload().setAction(1);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));

    }

    @Test
    @Tag("CSV-1253")
    @AllureId("1169")
    @DisplayName("MT5 close trade event NOT passing filtering. MsgType != ‘DealPerform’")
    void filtrationMt5CloseTradeEventTest2() throws JsonProcessingException {

        TradeEventMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getHeader().setMsgType("DeaPerform");
        closeTradeMt5.getHeader().setOperation(0);
        closeTradeMt5.getPayload().setEntry(3);
        closeTradeMt5.getPayload().setAction(1);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @Tag("CSV-1253")
    @AllureId("1170")
    @DisplayName("MT5 close trade event NOT passing filtering. Operation != 0")
    void filtrationMt5CloseTradeEventTest3() throws JsonProcessingException {

        TradeEventMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getHeader().setOperation(1);
        closeTradeMt5.getPayload().setEntry(3);
        closeTradeMt5.getPayload().setAction(1);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @Tag("CSV-1253")
    @AllureId("1171")
    @DisplayName("MT5 close trade event NOT passing filtering. Entry NOT IN (1, 3)")
    void filtrationMt5CloseTradeEventTest4() throws JsonProcessingException {

        TradeEventMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getHeader().setOperation(0);
        closeTradeMt5.getPayload().setEntry(2);
        closeTradeMt5.getPayload().setAction(1);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }

    @Test
    @Tag("CSV-1253")
    @AllureId("1172")
    @DisplayName("MT5 close trade event NOT passing filtering. Action NOT IN (0, 1)")
    void filtrationMt5CloseTradeEventTest5() throws JsonProcessingException {

        TradeEventMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.getHeader().setOperation(0);
        closeTradeMt5.getPayload().setEntry(3);
        closeTradeMt5.getPayload().setAction(2);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

    }
}
