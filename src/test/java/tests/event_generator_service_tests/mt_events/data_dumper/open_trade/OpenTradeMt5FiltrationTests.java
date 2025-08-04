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
class OpenTradeMt5FiltrationTests extends TestBaseKafka {

    /*
    MsgType = ‘Deal’
    Operation = 0
    Entry = 0
    Action IN (0, 1)
     */

    @Test
    @AllureId("1220")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event passing filtering. MsgType = ‘Deal’, Operation = 0, Entry = 0, Action= 1")
    void filtrationMt5OpenTradeEventTest2() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getHeader().setMsgType("Deal");
        openTradeMt5.getHeader().setOperation(0);
        openTradeMt5.getPayload().setEntry(0);
        openTradeMt5.getPayload().setAction(1);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(true));

    }


    @Test
    @AllureId("1221")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event passing filtering. MsgType = ‘Deal1’, Operation = 0, Entry = 0, Action=1")
    void filtrationMt5OpenTradeEventTest3() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getHeader().setMsgType("Deal1");
        openTradeMt5.getHeader().setOperation(0);
        openTradeMt5.getPayload().setEntry(0);
        openTradeMt5.getPayload().setAction(1);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1222")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event passing filtering. MsgType = ‘Deal’, Operation = 1, Entry = 0, Action=1")
    void filtrationMt5OpenTradeEventTest4() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getHeader().setMsgType("Deal");
        openTradeMt5.getHeader().setOperation(1);
        openTradeMt5.getPayload().setEntry(0);
        openTradeMt5.getPayload().setAction(1);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1223")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event passing filtering. MsgType = ‘Deal’, Operation = 0, Entry = 3, Action=1")
    void filtrationMt5OpenTradeEventTest5() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getHeader().setMsgType("Deal");
        openTradeMt5.getHeader().setOperation(0);
        openTradeMt5.getPayload().setEntry(9);
        openTradeMt5.getPayload().setAction(1);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

    @Test
    @AllureId("1224")
    @Tag("CSV-1280")
    @DisplayName("MT5 open trade event passing filtering. MsgType = ‘Deal’, Operation = 0, Entry = 0, Action=2")
    void filtrationMt5OpenTradeEventTest6() throws JsonProcessingException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getHeader().setMsgType("Deal");
        openTradeMt5.getHeader().setOperation(0);
        openTradeMt5.getPayload().setEntry(0);
        openTradeMt5.getPayload().setAction(2);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()));

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }

}
