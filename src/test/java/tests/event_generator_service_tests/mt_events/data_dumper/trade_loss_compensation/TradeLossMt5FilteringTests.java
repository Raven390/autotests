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
class TradeLossMt5FilteringTests extends TestBaseKafka {

    /*
    MsgType = ‘DealPerform’
    Operation = 0
    Entry IN (1, 3)
    Action = 2
    Comment contains  "Trade Loss" or "TLV"
     */

    @Test
    @AllureId("1282")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss event passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 1, Action=0, Comment contains 'Trade Loss'")
    void filtrationMt5TradeLossEventTest1() throws JsonProcessingException {

        TradeEventMt5 tradeLossMt5 = generateTradeLossTradeDataDumperMt5();
        tradeLossMt5.getHeader().setOperation(0);
        tradeLossMt5.getPayload().setEntry(1);
        tradeLossMt5.getPayload().setAction(2);
        tradeLossMt5.getPayload().setComment("Trade Loss");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));
    }

    @Test
    @AllureId("1283")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss event passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 3, Action=2, Comment contains 'TLV'")
    void filtrationMt5TradeLossEventTest2() throws JsonProcessingException {

        TradeEventMt5 tradeLossMt5 = generateTradeLossTradeDataDumperMt5();
        tradeLossMt5.getHeader().setOperation(0);
        tradeLossMt5.getPayload().setEntry(3);
        tradeLossMt5.getPayload().setAction(2);
        tradeLossMt5.getPayload().setComment("TLV");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));
    }


    @Test
    @AllureId("1284")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss event not passing filtering. MsgType = ‘Deal1’, Operation = 0, Entry = 1, Action=2, Comment contains 'Trade Loss'")
    void filtrationMt5TradeLossEventTest3() throws JsonProcessingException {

        TradeEventMt5 tradeLossMt5 = generateTradeLossTradeDataDumperMt5();
        tradeLossMt5.getHeader().setMsgType("Deal1");
        tradeLossMt5.getHeader().setOperation(0);
        tradeLossMt5.getPayload().setEntry(1);
        tradeLossMt5.getPayload().setAction(2);
        tradeLossMt5.getPayload().setComment("Trade Loss");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1285")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss event not passing filtering. MsgType = ‘DealPerform’, Operation = 1, Entry = 1, Action=2, Comment contains 'Trade Loss'")
    void filtrationMt5TradeLossEventTest4() throws JsonProcessingException {

        TradeEventMt5 tradeLossMt5 = generateTradeLossTradeDataDumperMt5();
        tradeLossMt5.getHeader().setOperation(1);
        tradeLossMt5.getPayload().setEntry(1);
        tradeLossMt5.getPayload().setAction(2);
        tradeLossMt5.getPayload().setComment("Trade Loss");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1286")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss event not  passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 2, Action=2, Comment contains 'Trade Loss'")
    void filtrationMt5TradeLossEventTest5() throws JsonProcessingException {

        TradeEventMt5 tradeLossMt5 = generateTradeLossTradeDataDumperMt5();
        tradeLossMt5.getHeader().setOperation(0);
        tradeLossMt5.getPayload().setEntry(2);
        tradeLossMt5.getPayload().setAction(2);
        tradeLossMt5.getPayload().setComment("Trade Loss");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1287")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss event not passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 1, Action=3, Comment contains 'Trade Loss'")
    void filtrationMt5TradeLossEventTest6() throws JsonProcessingException {

        TradeEventMt5 tradeLossMt5 = generateTradeLossTradeDataDumperMt5();
        tradeLossMt5.getHeader().setOperation(0);
        tradeLossMt5.getPayload().setEntry(1);
        tradeLossMt5.getPayload().setAction(3);
        tradeLossMt5.getPayload().setComment("Trade Loss");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1288")
    @Tag("CSV-1280")
    @DisplayName("MT5 trade loss event not passing filtering. MsgType = ‘DealPerform’, Operation = 0, Entry = 1, Action=2, Comment contains 'Trade Los'")
    void filtrationMt5TradeLossEventTest7() throws JsonProcessingException {

        TradeEventMt5 tradeLossMt5 = generateTradeLossTradeDataDumperMt5();
        tradeLossMt5.getHeader().setOperation(0);
        tradeLossMt5.getPayload().setEntry(1);
        tradeLossMt5.getPayload().setAction(2);
        tradeLossMt5.getPayload().setComment("Trade Los");

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
