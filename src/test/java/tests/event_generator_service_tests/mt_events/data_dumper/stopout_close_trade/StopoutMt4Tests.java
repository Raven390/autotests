package tests.event_generator_service_tests.mt_events.data_dumper.stopout_close_trade;

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

import static business_objects.kafka.mt_data_dumper_events.StopoutFactory.generateStopoutTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.convertTimestampToIsoFormat;
import static utils.Utils.getCurrentTimestampMillis;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_STOP_OUT_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class StopoutMt4Tests extends TestBaseKafka {

    @Test
    @AllureId("1211")
    @Tag("CSV-1280")
    @DisplayName("Generate stopout event with event generator service from MT4 data dumper source with all fields populated")
    void generateMt4StopoutEventTest() throws JsonProcessingException, InterruptedException {

        TradeEventMt4 stopoutTradeMt4 = generateStopoutTradeDataDumperMt4();
        Long time = getCurrentTimestampMillis();
        String convertedTimestamp = convertTimestampToIsoFormat(time);
        stopoutTradeMt4.getPayload().setCloseTime(time);
        stopoutTradeMt4.getPayload().setCloseTime(time);
        stopoutTradeMt4.getPayload().setOpenTime(time);
        stopoutTradeMt4.getPayload().setOpenTimeUtc(time);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(stopoutTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, String.valueOf(stopoutTradeMt4.getPayload().getLogin()), true);

        TradeEvent retrievedStopoutTradeMtEvent = objectMapper.readValue(consumedMessage.message(), TradeEvent.class);

        Allure.step("Verify kafka message");
        assertThat("Check tradeId", retrievedStopoutTradeMtEvent.tradeId, equalTo(stopoutTradeMt4.getPayload().getOrder()));
        assertThat("Check symbol", retrievedStopoutTradeMtEvent.symbol, equalTo(stopoutTradeMt4.getPayload().getSymbol()));
        assertThat("Check id", retrievedStopoutTradeMtEvent.id, instanceOf(String.class));
        assertThat("Check serverId", retrievedStopoutTradeMtEvent.serverId, equalTo(stopoutTradeMt4.getHeader().getServerId()));
        assertThat("Check tradingAccount", retrievedStopoutTradeMtEvent.tradingAccount, equalTo(stopoutTradeMt4.getPayload().getLogin()));
        assertThat("Check volume", retrievedStopoutTradeMtEvent.volume, equalTo(stopoutTradeMt4.getPayload().getVolume()));
        assertThat("Check equity", retrievedStopoutTradeMtEvent.equity, equalTo(stopoutTradeMt4.getPayload().getEquity()));
        assertThat("Check balance", retrievedStopoutTradeMtEvent.balance, equalTo(stopoutTradeMt4.getPayload().getBalance()));
        assertThat("Check leverage", retrievedStopoutTradeMtEvent.leverage, equalTo(stopoutTradeMt4.getPayload().getLeverage()));
        assertThat("Check margin", retrievedStopoutTradeMtEvent.margin, equalTo(stopoutTradeMt4.getPayload().getMargin()));
        assertThat("Check freeMargin", retrievedStopoutTradeMtEvent.freeMargin, equalTo(stopoutTradeMt4.getPayload().getFreeMargin()));
        assertThat("Check eventDate", retrievedStopoutTradeMtEvent.eventDate, equalTo(convertedTimestamp));
        assertThat("Check closeTime", retrievedStopoutTradeMtEvent.closeTime, equalTo(convertedTimestamp));
        assertThat("Check closeTimeUtc", retrievedStopoutTradeMtEvent.closeTimeUtc, equalTo(convertedTimestamp));
        assertThat("Check type", retrievedStopoutTradeMtEvent.type, equalTo("stopoutCloseTrade"));

    }
}
