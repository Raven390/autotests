package tests.event_generator_service_tests.mt_events.data_dumper.trade_loss_compensation;

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

import static business_objects.kafka.mt_data_dumper_events.TradeLossFactory.generateTradeLossTradeDataDumperMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_LOSS_COMPENSATION_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class TradeLossMt5EventTests extends TestBaseKafka {
    @Test
    @AllureId("1213")
    @Tag("CSV-1280")
    @DisplayName("Generate trade loss event with event generator service from MT5 data dumper source with all fields populated")
    void generateMt5TradeLossEventTest() throws JsonProcessingException, InterruptedException {

        TradeEventMt5 tradeLossTradeMt5 = generateTradeLossTradeDataDumperMt5();

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossTradeMt5.getPayload().getLogin()), true);

        TradeEvent retrievedTradeLossMtEvent = objectMapper.readValue(consumedMessage.message(), TradeEvent.class);

        Allure.step("Verify kafka message");
        assertThat("Check tradeId", retrievedTradeLossMtEvent.tradeId, equalTo(tradeLossTradeMt5.getPayload().getDeal()));
        assertThat("Check symbol", retrievedTradeLossMtEvent.symbol, equalTo(tradeLossTradeMt5.getPayload().getSymbol()));
        assertThat("Check id", retrievedTradeLossMtEvent.id, instanceOf(String.class));
        assertThat("Check serverId", retrievedTradeLossMtEvent.serverId, equalTo(tradeLossTradeMt5.getHeader().getServerId()));
        assertThat("Check tradingAccount", retrievedTradeLossMtEvent.tradingAccount, equalTo(tradeLossTradeMt5.getPayload().getLogin()));
        assertThat("Check volume", (long) retrievedTradeLossMtEvent.volume, equalTo(tradeLossTradeMt5.getPayload().getVolume()));
        assertThat("Check closeTime", retrievedTradeLossMtEvent.closeTime, startsWith(String.valueOf(tradeLossTradeMt5.getPayload().getTime())));
        assertThat("Check closeTimeUtc", retrievedTradeLossMtEvent.closeTimeUtc, startsWith(String.valueOf(tradeLossTradeMt5.getPayload().getTimeUtc())));
        assertThat("Check equity", retrievedTradeLossMtEvent.equity, equalTo(tradeLossTradeMt5.getPayload().getEquity()));
        assertThat("Check balance", retrievedTradeLossMtEvent.balance, equalTo(tradeLossTradeMt5.getPayload().getBalance()));
        assertThat("Check leverage", (int) retrievedTradeLossMtEvent.leverage, equalTo(tradeLossTradeMt5.getPayload().getLeverage()));
        assertThat("Check margin", retrievedTradeLossMtEvent.margin, equalTo(tradeLossTradeMt5.getPayload().getMargin()));
        assertThat("Check freeMargin", retrievedTradeLossMtEvent.freeMargin, equalTo(tradeLossTradeMt5.getPayload().getFreeMargin()));
        assertThat("Check eventDate", retrievedTradeLossMtEvent.eventDate, startsWith(String.valueOf(tradeLossTradeMt5.getPayload().getTimeUtc())));
        assertThat("Check initialEventTime", retrievedTradeLossMtEvent.initialEventTime, instanceOf(String.class));
        assertThat("Check metadata", retrievedTradeLossMtEvent.metadata.created, instanceOf(String.class));
        assertThat("Check type", retrievedTradeLossMtEvent.type, equalTo("tradeLossCompensation"));
    }
}
