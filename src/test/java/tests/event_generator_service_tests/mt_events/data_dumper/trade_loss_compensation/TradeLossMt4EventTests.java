package tests.event_generator_service_tests.mt_events.data_dumper.trade_loss_compensation;

import business_objects.kafka.mt_data_dumper_events.TradeEventMt4;
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

import static business_objects.kafka.mt_data_dumper_events.TradeLossFactory.generateTradeLossTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_LOSS_COMPENSATION_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class TradeLossMt4EventTests extends TestBaseKafka {

    @Test
    @AllureId("1214")
    @Tag("CSV-1280")
    @DisplayName("Generate trade loss event with event generator service from MT4 data dumper source with all fields populated")
    void generateMt4TradeLossTradeEventTest() throws JsonProcessingException, InterruptedException {

        TradeEventMt4 tradeLossTradeMt4 = generateTradeLossTradeDataDumperMt4();

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(tradeLossTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, String.valueOf(tradeLossTradeMt4.getPayload().getLogin()), true);

        TradeEvent retrievedTradeLossTradeMtEvent = objectMapper.readValue(consumedMessage.message(), TradeEvent.class);

        Allure.step("Verify kafka message");
        assertThat("Check tradeId", retrievedTradeLossTradeMtEvent.tradeId, equalTo(tradeLossTradeMt4.getPayload().getOrder()));
        assertThat("Check symbol", retrievedTradeLossTradeMtEvent.symbol, equalTo(tradeLossTradeMt4.getPayload().getSymbol()));
        assertThat("Check id", retrievedTradeLossTradeMtEvent.id, instanceOf(String.class));
        assertThat("Check serverId", retrievedTradeLossTradeMtEvent.serverId, equalTo(tradeLossTradeMt4.getHeader().getServerId()));
        assertThat("Check tradingAccount", retrievedTradeLossTradeMtEvent.tradingAccount, equalTo(tradeLossTradeMt4.getPayload().getLogin()));
        assertThat("Check volume", retrievedTradeLossTradeMtEvent.volume, equalTo(tradeLossTradeMt4.getPayload().getVolume()));
        assertThat("Check closeTime", retrievedTradeLossTradeMtEvent.closeTime, startsWith(String.valueOf(tradeLossTradeMt4.getPayload().getCloseTime())));
        assertThat("Check closeTimeUtc", retrievedTradeLossTradeMtEvent.closeTimeUtc, startsWith(String.valueOf(tradeLossTradeMt4.getPayload().getCloseTimeUtc())));
        assertThat("Check equity", retrievedTradeLossTradeMtEvent.equity, equalTo(tradeLossTradeMt4.getPayload().getEquity()));
        assertThat("Check balance", retrievedTradeLossTradeMtEvent.balance, equalTo(tradeLossTradeMt4.getPayload().getBalance()));
        assertThat("Check leverage", retrievedTradeLossTradeMtEvent.leverage, equalTo(tradeLossTradeMt4.getPayload().getLeverage()));
        assertThat("Check margin", retrievedTradeLossTradeMtEvent.margin, equalTo(tradeLossTradeMt4.getPayload().getMargin()));
        assertThat("Check freeMargin", retrievedTradeLossTradeMtEvent.freeMargin, equalTo(tradeLossTradeMt4.getPayload().getFreeMargin()));
        assertThat("Check eventDate", retrievedTradeLossTradeMtEvent.eventDate, startsWith(String.valueOf(tradeLossTradeMt4.getPayload().getCloseTimeUtc())));
        assertThat("Check type", retrievedTradeLossTradeMtEvent.type, equalTo("tradeLossCompensation"));
    }
}
