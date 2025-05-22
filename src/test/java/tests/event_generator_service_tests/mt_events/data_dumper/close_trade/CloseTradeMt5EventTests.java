package tests.event_generator_service_tests.mt_events.data_dumper.close_trade;

import business_objects.kafka.mt_data_dumper_events.close_trade.CloseTradeMt5;
import business_objects.kafka.mt_events.CloseTradeMtEvent_NEW;
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

import static business_objects.kafka.mt_data_dumper_events.close_trade.CloseTradeFactory.generateCloseTradeDataDumperMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.instanceOf;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_CLOSE_TRADE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class CloseTradeMt5EventTests extends TestBaseKafka {

    @Test
    @Tag("CSV-1253")
    @AllureId("1160")
    @DisplayName("Generate close event with event generator service from MT5 data dumper source with all fields populated")
    void generateMt5CloseTradeEventTest() throws JsonProcessingException, InterruptedException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt5.getPayload().getLogin()), true);

        CloseTradeMtEvent_NEW retrievedCloseTradeMtEvent = objectMapper.readValue(consumedMessage.message(), CloseTradeMtEvent_NEW.class);

        Allure.step("Verify kafka message");
        assertThat("Check tradeId", retrievedCloseTradeMtEvent.tradeId, equalTo(closeTradeMt5.getPayload().getDeal()));
        assertThat("Check symbol", retrievedCloseTradeMtEvent.symbol, equalTo(closeTradeMt5.getPayload().getSymbol()));
        assertThat("Check id", retrievedCloseTradeMtEvent.id, instanceOf(String.class));
        assertThat("Check serverId", retrievedCloseTradeMtEvent.serverId, equalTo(closeTradeMt5.getHeader().getServerId()));
        assertThat("Check tradingAccount", retrievedCloseTradeMtEvent.tradingAccount, equalTo(closeTradeMt5.getPayload().getLogin()));
        assertThat("Check volume", (long) retrievedCloseTradeMtEvent.volume, equalTo(closeTradeMt5.getPayload().getVolume()));
        assertThat("Check closeTime", retrievedCloseTradeMtEvent.closeTime, startsWith(String.valueOf(closeTradeMt5.getPayload().getTime())));
        assertThat("Check closeTimeUtc", retrievedCloseTradeMtEvent.closeTimeUtc, startsWith(String.valueOf(closeTradeMt5.getPayload().getTimeUtc())));
        assertThat("Check equity", retrievedCloseTradeMtEvent.equity, equalTo(closeTradeMt5.getPayload().getEquity()));
        assertThat("Check balance", retrievedCloseTradeMtEvent.balance, equalTo(closeTradeMt5.getPayload().getBalance()));
        assertThat("Check leverage", (int) retrievedCloseTradeMtEvent.leverage, equalTo(closeTradeMt5.getPayload().getLeverage()));
        assertThat("Check margin", retrievedCloseTradeMtEvent.margin, equalTo(closeTradeMt5.getPayload().getMargin()));
        assertThat("Check freeMargin", retrievedCloseTradeMtEvent.freeMargin, equalTo(closeTradeMt5.getPayload().getFreeMargin()));
        assertThat("Check eventDate", retrievedCloseTradeMtEvent.eventDate, startsWith(String.valueOf(closeTradeMt5.getPayload().getTimeUtc())));
        assertThat("Check initialEventTime", retrievedCloseTradeMtEvent.initialEventTime, instanceOf(String.class));
        assertThat("Check metadata", retrievedCloseTradeMtEvent.metadata.created, instanceOf(String.class));
        assertThat("Check type", retrievedCloseTradeMtEvent.type, equalTo("closeTrade"));
    }

}
