package tests.event_generator_service_tests.mt_events.data_dumper.stopout_close_trade;

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

import static business_objects.kafka.mt_data_dumper_events.StopoutFactory.generateStopoutTradeDataDumperMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_STOP_OUT_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class StopoutMt5Tests extends TestBaseKafka {

    @Test
    @AllureId("1213")
    @Tag("CSV-1280")
    @DisplayName("Generate stopout event with event generator service from MT5 data dumper source with all fields populated")
    void generateMt5StopoutEventTest() throws JsonProcessingException, InterruptedException {

        TradeEventMt5 stopoutTradeMt5 = generateStopoutTradeDataDumperMt5();

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutTradeMt5.getPayload().getLogin()), true);

        TradeEvent retrievedStopoutMtEvent = objectMapper.readValue(consumedMessage.message(), TradeEvent.class);

        Allure.step("Verify kafka message");
        assertThat("Check tradeId", retrievedStopoutMtEvent.tradeId, equalTo(stopoutTradeMt5.getPayload().getDeal()));
        assertThat("Check symbol", retrievedStopoutMtEvent.symbol, equalTo(stopoutTradeMt5.getPayload().getSymbol()));
        assertThat("Check id", retrievedStopoutMtEvent.id, instanceOf(String.class));
        assertThat("Check serverId", retrievedStopoutMtEvent.serverId, equalTo(stopoutTradeMt5.getHeader().getServerId()));
        assertThat("Check tradingAccount", retrievedStopoutMtEvent.tradingAccount, equalTo(stopoutTradeMt5.getPayload().getLogin()));
        assertThat("Check volume", (long) retrievedStopoutMtEvent.volume, equalTo(stopoutTradeMt5.getPayload().getVolume()));
        assertThat("Check closeTime", retrievedStopoutMtEvent.closeTime, startsWith(String.valueOf(stopoutTradeMt5.getPayload().getTime())));
        assertThat("Check closeTimeUtc", retrievedStopoutMtEvent.closeTimeUtc, startsWith(String.valueOf(stopoutTradeMt5.getPayload().getTimeUtc())));
        assertThat("Check equity", retrievedStopoutMtEvent.equity, equalTo(stopoutTradeMt5.getPayload().getEquity()));
        assertThat("Check balance", retrievedStopoutMtEvent.balance, equalTo(stopoutTradeMt5.getPayload().getBalance()));
        assertThat("Check leverage", (int) retrievedStopoutMtEvent.leverage, equalTo(stopoutTradeMt5.getPayload().getLeverage()));
        assertThat("Check margin", retrievedStopoutMtEvent.margin, equalTo(stopoutTradeMt5.getPayload().getMargin()));
        assertThat("Check freeMargin", retrievedStopoutMtEvent.freeMargin, equalTo(stopoutTradeMt5.getPayload().getFreeMargin()));
        assertThat("Check eventDate", retrievedStopoutMtEvent.eventDate, startsWith(String.valueOf(stopoutTradeMt5.getPayload().getTimeUtc())));
        assertThat("Check initialEventTime", retrievedStopoutMtEvent.initialEventTime, instanceOf(String.class));
        assertThat("Check metadata", retrievedStopoutMtEvent.metadata.created, instanceOf(String.class));
        assertThat("Check type", retrievedStopoutMtEvent.type, equalTo("stopoutCloseTrade"));
    }
}
