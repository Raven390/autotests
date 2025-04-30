package tests.event_generator_service_tests.mt_events.close_trade;

import static business_objects.kafka.mt_db_events.close_trade.CloseTradeMtDbEventFactory.generateCloseTradeMtDbEventMt4;
import static business_objects.kafka.mt_db_events.close_trade.CloseTradeMtDbEventFactory.generateCloseTradeMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MessageWithHeaders;
import business_objects.kafka.mt_db_events.close_trade.CloseTradeMtDbEventMt4;
import business_objects.kafka.mt_db_events.close_trade.CloseTradeMtDbEventMt5;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_CLOSE_TRADE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class MtDbEventsCloseTradeTest {

    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Generate close event with event generator service from MT4 source with all fields populated")
    @AllureId("109")
    void generateMt4CloseTradeEventTest() throws JsonProcessingException, InterruptedException {

        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMtDbEventMt4), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, closeTradeMtDbEventMt4.data.closeTime, true);
        CloseTradeMtEvent retrievedCloseTradeMtEvent = objectMapper.readValue(consumedMessage.message(), CloseTradeMtEvent.class);

        CloseTradeMtEvent expectedCloseTradeMtEvent = new CloseTradeMtEvent(
                closeTradeMtDbEventMt4.data.closeTime, closeTradeMtDbEventMt4.data.tradeId.longValue(), closeTradeMtDbEventMt4.data.mtAccount, closeTradeMtDbEventMt4.data.volume, closeTradeMtDbEventMt4.data.symbol, closeTradeMtDbEventMt4.data.serverId, EG_CLOSE_TRADE_EVENT);

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedCloseTradeMtEvent.id, notNullValue());
        assertThat("Check all fields except id", retrievedCloseTradeMtEvent, equalTo(expectedCloseTradeMtEvent));
    }

    @Test
    @DisplayName("Generate close event with event generator service from MT5 source with all fields populated")
    @AllureId("110")
    void generateMt5CloseTradeEventTest() throws JsonProcessingException, InterruptedException {
        CloseTradeMtDbEventMt5 closeTradeMtDbEventMt5 = generateCloseTradeMtDbEventMt5();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMtDbEventMt5), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, closeTradeMtDbEventMt5.data.closeTime, true);
        CloseTradeMtEvent retrievedCloseTradeMtEvent = objectMapper.readValue(consumedMessage.message(), CloseTradeMtEvent.class);

        CloseTradeMtEvent expectedCloseTradeMtEvent = new CloseTradeMtEvent(
                closeTradeMtDbEventMt5.data.closeTime, closeTradeMtDbEventMt5.data.tradeId.longValue(), closeTradeMtDbEventMt5.data.mtAccount, closeTradeMtDbEventMt5.data.volume, closeTradeMtDbEventMt5.data.symbol, closeTradeMtDbEventMt5.data.serverId, EG_CLOSE_TRADE_EVENT);

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedCloseTradeMtEvent.id, notNullValue());
        assertThat("Check all fields except id", retrievedCloseTradeMtEvent, equalTo(expectedCloseTradeMtEvent));
    }
}
