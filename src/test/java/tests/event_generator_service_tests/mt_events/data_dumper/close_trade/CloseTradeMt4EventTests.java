package tests.event_generator_service_tests.mt_events.data_dumper.close_trade;

import business_objects.kafka.mt_data_dumper_events.close_trade.CloseTradeMt4;
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


import static business_objects.kafka.mt_data_dumper_events.close_trade.CloseTradeFactory.generateCloseTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_CLOSE_TRADE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class CloseTradeMt4EventTests extends TestBaseKafka {

    @Test
    @Tag("CSV-1253")
    @DisplayName("Generate close event with event generator service from MT4 data dumper source with all fields populated")
    @AllureId("1159")
    void generateMt4CloseTradeEventTest() throws JsonProcessingException, InterruptedException {

        CloseTradeMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, String.valueOf(closeTradeMt4.getPayload().getLogin()), true);

        CloseTradeMtEvent_NEW retrievedCloseTradeMtEvent = objectMapper.readValue(consumedMessage.message(), CloseTradeMtEvent_NEW.class);

        Allure.step("Verify kafka message");
        assertThat("Check tradeId", retrievedCloseTradeMtEvent.tradeId, equalTo(closeTradeMt4.getPayload().getOrder()));
        assertThat("Check symbol", retrievedCloseTradeMtEvent.symbol, equalTo(closeTradeMt4.getPayload().getSymbol()));
        assertThat("Check id", retrievedCloseTradeMtEvent.id, instanceOf(String.class));
        assertThat("Check serverId", retrievedCloseTradeMtEvent.serverId, equalTo(closeTradeMt4.getHeader().getServerId()));
        assertThat("Check tradingAccount", retrievedCloseTradeMtEvent.tradingAccount, equalTo(closeTradeMt4.getPayload().getLogin()));
        assertThat("Check volume", retrievedCloseTradeMtEvent.volume, equalTo(closeTradeMt4.getPayload().getVolume()));
        assertThat("Check closeTime", retrievedCloseTradeMtEvent.closeTime, startsWith(String.valueOf(closeTradeMt4.getPayload().getCloseTime())));
        assertThat("Check closeTimeUtc", retrievedCloseTradeMtEvent.closeTimeUtc, startsWith(String.valueOf(closeTradeMt4.getPayload().getCloseTimeUtc())));
        assertThat("Check equity", retrievedCloseTradeMtEvent.equity, equalTo(closeTradeMt4.getPayload().getEquity()));
        assertThat("Check balance", retrievedCloseTradeMtEvent.balance, equalTo(closeTradeMt4.getPayload().getBalance()));
        assertThat("Check leverage", retrievedCloseTradeMtEvent.leverage, equalTo(closeTradeMt4.getPayload().getLeverage()));
        assertThat("Check margin", retrievedCloseTradeMtEvent.margin, equalTo(closeTradeMt4.getPayload().getMargin()));
        assertThat("Check freeMargin", retrievedCloseTradeMtEvent.freeMargin, equalTo(closeTradeMt4.getPayload().getFreeMargin()));
        assertThat("Check eventDate", retrievedCloseTradeMtEvent.eventDate, startsWith(String.valueOf(closeTradeMt4.getPayload().getCloseTimeUtc())));
        assertThat("Check initialEventTime", retrievedCloseTradeMtEvent.initialEventTime, instanceOf(String.class));
        assertThat("Check metadata", retrievedCloseTradeMtEvent.metadata.created, instanceOf(String.class));
        assertThat("Check type", retrievedCloseTradeMtEvent.type, equalTo("closeTrade"));
    }
}
