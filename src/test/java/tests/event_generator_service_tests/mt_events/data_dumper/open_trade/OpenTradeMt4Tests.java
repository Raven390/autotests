package tests.event_generator_service_tests.mt_events.data_dumper.open_trade;

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

import static business_objects.kafka.mt_data_dumper_events.OpenTradeFactory.generateOpenTradeDataDumperMt4;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_EVENT_GENERATOR_SERVICE;
import static utils.Utils.convertTimestampToIsoFormat;
import static utils.Utils.getCurrentTimestampMillis;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_DATA_DUMPER_OPEN_TRADE_EVENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class OpenTradeMt4Tests extends TestBaseKafka {

    @Test
    @AllureId("1210")
    @Tag("CSV-1280")
    @DisplayName("Generate open event with event generator service from MT4 data dumper source with all fields populated")
    void generateMt4OpenTradeEventTest() throws JsonProcessingException, InterruptedException {

        TradeEventMt4 openTradeMt4 = generateOpenTradeDataDumperMt4();
        Long time = getCurrentTimestampMillis();
        String convertedTimestamp = convertTimestampToIsoFormat(time);
        openTradeMt4.getPayload().setOpenTime(time);
        openTradeMt4.getPayload().setOpenTimeUtc(time);


        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(openTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

        Allure.step("Wait for event generator do some magic and consume message from mt-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, convertedTimestamp, true);

        TradeEvent retrievedOpenTradeMtEvent = objectMapper.readValue(consumedMessage.message(), TradeEvent.class);

        Allure.step("Verify kafka message");
        assertThat("Check tradeId", retrievedOpenTradeMtEvent.tradeId, equalTo(openTradeMt4.getPayload().getOrder()));
        assertThat("Check symbol", retrievedOpenTradeMtEvent.symbol, equalTo(openTradeMt4.getPayload().getSymbol()));
        assertThat("Check id", retrievedOpenTradeMtEvent.id, instanceOf(String.class));
        assertThat("Check serverId", retrievedOpenTradeMtEvent.serverId, equalTo(openTradeMt4.getHeader().getServerId()));
        assertThat("Check tradingAccount", retrievedOpenTradeMtEvent.tradingAccount, equalTo(openTradeMt4.getPayload().getLogin()));
        assertThat("Check volume", retrievedOpenTradeMtEvent.volume, equalTo(openTradeMt4.getPayload().getVolume()));
        assertThat("Check closeTime", retrievedOpenTradeMtEvent.closeTime, equalTo(null));
        assertThat("Check closeTimeUtc", retrievedOpenTradeMtEvent.closeTimeUtc, equalTo(null));
        assertThat("Check openTime", retrievedOpenTradeMtEvent.openTime, equalTo(convertedTimestamp));
        assertThat("Check openTimeUtc", retrievedOpenTradeMtEvent.openTimeUtc, equalTo(convertedTimestamp));
        assertThat("Check equity", retrievedOpenTradeMtEvent.equity, equalTo(openTradeMt4.getPayload().getEquity()));
        assertThat("Check balance", retrievedOpenTradeMtEvent.balance, equalTo(openTradeMt4.getPayload().getBalance()));
        assertThat("Check leverage", retrievedOpenTradeMtEvent.leverage, equalTo(openTradeMt4.getPayload().getLeverage()));
        assertThat("Check margin", retrievedOpenTradeMtEvent.margin, equalTo(openTradeMt4.getPayload().getMargin()));
        assertThat("Check freeMargin", retrievedOpenTradeMtEvent.freeMargin, equalTo(openTradeMt4.getPayload().getFreeMargin()));
        assertThat("Check eventDate", retrievedOpenTradeMtEvent.eventDate, equalTo(convertedTimestamp));
        assertThat("Check type", retrievedOpenTradeMtEvent.type, equalTo("openTrade"));
    }
}
