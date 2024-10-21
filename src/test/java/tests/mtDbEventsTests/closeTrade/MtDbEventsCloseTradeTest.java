package tests.mtDbEventsTests.closeTrade;

import static helpers.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventFactory.generateCloseTradeMtDbEventMt4;
import static helpers.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventFactory.generateCloseTradeMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MessageWithHeaders;
import helpers.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventMt4;
import helpers.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventMt5;
import helpers.kafka.mtEvents.CloseTradeMtEvent;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class MtDbEventsCloseTradeTest {

    @Test
    @DisplayName("Generate close event with event generator service from MT4 source with all fields populated")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("109")
    public void generateMt4CloseTradeEventTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        CloseTradeMtDbEventMt4 closeTradeMtDbEventMt4 = generateCloseTradeMtDbEventMt4();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(closeTradeMtDbEventMt4), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MessageWithHeaders consumedMessage =
                kafka.consumeMessages(KAFKA_TOPIC_MT_EVENTS, closeTradeMtDbEventMt4.data.closeTime, true);
        CloseTradeMtEvent retrievedCloseTradeMtEvent =
                objectMapper.readValue(consumedMessage.message(), CloseTradeMtEvent.class);
        retrievedCloseTradeMtEvent.type = consumedMessage.headers().get("__TypeId__");

        CloseTradeMtEvent expectedCloseTradeMtEvent = new CloseTradeMtEvent(
                closeTradeMtDbEventMt4.data.closeTime,
                closeTradeMtDbEventMt4.data.tradeId,
                closeTradeMtDbEventMt4.data.mtAccount,
                closeTradeMtDbEventMt4.data.volume,
                closeTradeMtDbEventMt4.data.symbol,
                closeTradeMtDbEventMt4.data.serverId,
                "closeTrade");

        Allure.step("Verify that message was written correctly");
        assertThat("Check uuid", retrievedCloseTradeMtEvent.uuid, notNullValue());
        assertThat("Check all fields except uuid", retrievedCloseTradeMtEvent, equalTo(expectedCloseTradeMtEvent));
    }

    @Test
    @DisplayName("Generate close event with event generator service from MT5 source with all fields populated")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("110")
    public void generateMt5CloseTradeEventTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        CloseTradeMtDbEventMt5 closeTradeMtDbEventMt5 = generateCloseTradeMtDbEventMt5();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(closeTradeMtDbEventMt5), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MessageWithHeaders consumedMessage =
                kafka.consumeMessages(KAFKA_TOPIC_MT_EVENTS, closeTradeMtDbEventMt5.data.closeTime, true);
        CloseTradeMtEvent retrievedCloseTradeMtEvent =
                objectMapper.readValue(consumedMessage.message(), CloseTradeMtEvent.class);
        retrievedCloseTradeMtEvent.type = consumedMessage.headers().get("__TypeId__");

        CloseTradeMtEvent expectedCloseTradeMtEvent = new CloseTradeMtEvent(
                closeTradeMtDbEventMt5.data.closeTime,
                closeTradeMtDbEventMt5.data.tradeId,
                closeTradeMtDbEventMt5.data.mtAccount,
                closeTradeMtDbEventMt5.data.volume,
                closeTradeMtDbEventMt5.data.symbol,
                closeTradeMtDbEventMt5.data.serverId,
                "closeTrade");

        Allure.step("Verify that message was written correctly");
        assertThat("Check uuid", retrievedCloseTradeMtEvent.uuid, notNullValue());
        assertThat("Check all fields except uuid", retrievedCloseTradeMtEvent, equalTo(expectedCloseTradeMtEvent));
    }
}
