package tests.eventGeneratorServiceTests.mtEvents.openTrade;

import static businessObjects.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventFactory.generateOpenTradeMtDbEventMt4;
import static businessObjects.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventFactory.generateOpenTradeMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MessageWithHeaders;
import businessObjects.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventMt4;
import businessObjects.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventMt5;
import businessObjects.kafka.mtEvents.OpenTradeMtEvent;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_OPEN_TRADE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
public class MtDbEventsOpenTradeTest {

    @Test
    @DisplayName("Generate open event with event generator service from MT4 source with all fields populated")
    @AllureId("118")
    public void generateMt4OpenTradeEventTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        OpenTradeMtDbEventMt4 openTradeMtDbEventMt4 = generateOpenTradeMtDbEventMt4();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeMtDbEventMt4), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, openTradeMtDbEventMt4.data.openTime, true);
        OpenTradeMtEvent retrievedOpenTradeMtEvent = objectMapper.readValue(consumedMessage.message(), OpenTradeMtEvent.class);

        OpenTradeMtEvent expectedOpenTradeMtEvent = new OpenTradeMtEvent(
                openTradeMtDbEventMt4.data.openTime, openTradeMtDbEventMt4.data.tradeId, openTradeMtDbEventMt4.data.mtAccount, openTradeMtDbEventMt4.data.volume, openTradeMtDbEventMt4.data.symbol, openTradeMtDbEventMt4.data.serverId, "openTrade");

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedOpenTradeMtEvent.id, notNullValue());
        assertThat("Check all fields except id", retrievedOpenTradeMtEvent, equalTo(expectedOpenTradeMtEvent));
    }

    @Test
    @DisplayName("Generate open event with event generator service from MT5 source with all fields populated")
    @AllureId("119")
    public void generateMt5OpenTradeEventTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        OpenTradeMtDbEventMt5 openTradeMtDbEventMt5 = generateOpenTradeMtDbEventMt5();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeMtDbEventMt5), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, openTradeMtDbEventMt5.data.openTime, true);
        OpenTradeMtEvent retrievedOpenTradeMtEvent = objectMapper.readValue(consumedMessage.message(), OpenTradeMtEvent.class);

        OpenTradeMtEvent expectedOpenTradeMtEvent = new OpenTradeMtEvent(
                openTradeMtDbEventMt5.data.openTime, openTradeMtDbEventMt5.data.tradeId, openTradeMtDbEventMt5.data.mtAccount, openTradeMtDbEventMt5.data.volume, openTradeMtDbEventMt5.data.symbol, openTradeMtDbEventMt5.data.serverId, "openTrade");

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedOpenTradeMtEvent.id, notNullValue());
        assertThat("Check all fields except id", retrievedOpenTradeMtEvent, equalTo(expectedOpenTradeMtEvent));
    }
}
