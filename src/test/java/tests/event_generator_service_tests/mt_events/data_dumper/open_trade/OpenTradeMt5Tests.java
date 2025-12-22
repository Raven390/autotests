package tests.event_generator_service_tests.mt_events.data_dumper.open_trade;

import static business_objects.kafka.mt_data_dumper_events.OpenTradeFactory.generateOpenTradeDataDumperMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_EVENT_GENERATOR_SERVICE;
import static utils.Utils.convertTimestampToIsoFormat;
import static utils.Utils.getCurrentTimestampMillis;

import business_objects.kafka.mt_data_dumper_events.TradeEventMt5;
import business_objects.kafka.mt_events.TradeEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
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
@Story(STORY_DATA_DUMPER_OPEN_TRADE_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class OpenTradeMt5Tests extends TestBaseKafka {

    @Test
    @AllureId("1212")
    @Tag("CSV-1280")
    @DisplayName(
            "Generate open trade event (action = 0) with event generator service from MT5 data dumper source with all fields populated")
    void generateMt5CloseTradeEventTest() throws JsonProcessingException, InterruptedException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setAction(0);
        Long time = getCurrentTimestampMillis();
        String convertedTimestamp = convertTimestampToIsoFormat(time);
        openTradeMt5.getPayload().setTime(time);
        openTradeMt5.getPayload().setTimeUtc(time);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()), true);

        TradeEvent retrievedOpenTradeMtEvent = objectMapper.readValue(consumedMessage.message(), TradeEvent.class);

        Allure.step("Verify kafka message");
        assertThat(
                "Check tradeId",
                retrievedOpenTradeMtEvent.tradeId,
                equalTo(openTradeMt5.getPayload().getDeal()));
        assertThat(
                "Check symbol",
                retrievedOpenTradeMtEvent.symbol,
                equalTo(openTradeMt5.getPayload().getSymbol()));
        assertThat("Check id", retrievedOpenTradeMtEvent.id, instanceOf(String.class));
        assertThat(
                "Check serverId",
                retrievedOpenTradeMtEvent.serverId,
                equalTo(openTradeMt5.getHeader().getServerId()));
        assertThat(
                "Check tradingAccount",
                retrievedOpenTradeMtEvent.tradingAccount,
                equalTo(openTradeMt5.getPayload().getLogin()));
        assertThat(
                "Check volume",
                (long) retrievedOpenTradeMtEvent.volume,
                equalTo(openTradeMt5.getPayload().getVolume()));
        assertThat("Check closeTime", retrievedOpenTradeMtEvent.closeTime, equalTo(null));
        assertThat("Check closeTimeUtc", retrievedOpenTradeMtEvent.closeTimeUtc, equalTo(null));
        assertThat(
                "Check equity",
                retrievedOpenTradeMtEvent.equity,
                equalTo(openTradeMt5.getPayload().getEquity()));
        assertThat(
                "Check balance",
                retrievedOpenTradeMtEvent.balance,
                equalTo(openTradeMt5.getPayload().getBalance()));
        assertThat(
                "Check leverage",
                (int) retrievedOpenTradeMtEvent.leverage,
                equalTo(openTradeMt5.getPayload().getLeverage()));
        assertThat(
                "Check margin",
                retrievedOpenTradeMtEvent.margin,
                equalTo(openTradeMt5.getPayload().getMargin()));
        assertThat(
                "Check freeMargin",
                retrievedOpenTradeMtEvent.freeMargin,
                equalTo(openTradeMt5.getPayload().getFreeMargin()));
        assertThat("Check eventDate", retrievedOpenTradeMtEvent.eventDate, equalTo(convertedTimestamp));
        assertThat("Check type", retrievedOpenTradeMtEvent.type, equalTo("openTrade"));
        assertThat("Check openTime", retrievedOpenTradeMtEvent.openTime, equalTo(convertedTimestamp));
        assertThat("Check openTimeUtc", retrievedOpenTradeMtEvent.openTimeUtc, equalTo(convertedTimestamp));
    }

    @Test
    @AllureId("1329")
    @Tag("CSV-1280")
    @DisplayName(
            "Generate open trade event (action = 1) with event generator service from MT5 data dumper source with all fields populated")
    void generateMt5CloseTradeEventTest2() throws JsonProcessingException, InterruptedException {

        TradeEventMt5 openTradeMt5 = generateOpenTradeDataDumperMt5();
        openTradeMt5.getPayload().setAction(1);
        Long time = getCurrentTimestampMillis();
        String convertedTimestamp = convertTimestampToIsoFormat(time);
        openTradeMt5.getPayload().setTime(time);
        openTradeMt5.getPayload().setTimeUtc(time);

        Allure.step("Write message to Mt5_DealPerform topic");
        kafka.produceMessage(
                KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt5), KAFKA_TOPIC_MT_5_DEAL_PERFORM);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(
                KAFKA_TOPIC_MT_EVENTS, String.valueOf(openTradeMt5.getPayload().getLogin()), true);

        TradeEvent retrievedOpenTradeMtEvent = objectMapper.readValue(consumedMessage.message(), TradeEvent.class);

        Allure.step("Verify kafka message");
        assertThat(
                "Check tradeId",
                retrievedOpenTradeMtEvent.tradeId,
                equalTo(openTradeMt5.getPayload().getDeal()));
        assertThat(
                "Check symbol",
                retrievedOpenTradeMtEvent.symbol,
                equalTo(openTradeMt5.getPayload().getSymbol()));
        assertThat("Check id", retrievedOpenTradeMtEvent.id, instanceOf(String.class));
        assertThat(
                "Check serverId",
                retrievedOpenTradeMtEvent.serverId,
                equalTo(openTradeMt5.getHeader().getServerId()));
        assertThat(
                "Check tradingAccount",
                retrievedOpenTradeMtEvent.tradingAccount,
                equalTo(openTradeMt5.getPayload().getLogin()));
        assertThat(
                "Check volume",
                (long) retrievedOpenTradeMtEvent.volume,
                equalTo(openTradeMt5.getPayload().getVolume()));
        assertThat("Check closeTime", retrievedOpenTradeMtEvent.closeTime, equalTo(null));
        assertThat("Check closeTimeUtc", retrievedOpenTradeMtEvent.closeTimeUtc, equalTo(null));
        assertThat(
                "Check equity",
                retrievedOpenTradeMtEvent.equity,
                equalTo(openTradeMt5.getPayload().getEquity()));
        assertThat(
                "Check balance",
                retrievedOpenTradeMtEvent.balance,
                equalTo(openTradeMt5.getPayload().getBalance()));
        assertThat(
                "Check leverage",
                (int) retrievedOpenTradeMtEvent.leverage,
                equalTo(openTradeMt5.getPayload().getLeverage()));
        assertThat(
                "Check margin",
                retrievedOpenTradeMtEvent.margin,
                equalTo(openTradeMt5.getPayload().getMargin()));
        assertThat(
                "Check freeMargin",
                retrievedOpenTradeMtEvent.freeMargin,
                equalTo(openTradeMt5.getPayload().getFreeMargin()));
        assertThat("Check eventDate", retrievedOpenTradeMtEvent.eventDate, equalTo(convertedTimestamp));
        assertThat("Check type", retrievedOpenTradeMtEvent.type, equalTo("openTrade"));
        assertThat("Check openTime", retrievedOpenTradeMtEvent.openTime, equalTo(convertedTimestamp));
        assertThat("Check openTimeUtc", retrievedOpenTradeMtEvent.openTimeUtc, equalTo(convertedTimestamp));
    }
}
