package tests.eventGeneratorServiceTests.mtEvents.rafBalanceOrder;

import static helpers.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventFactory.generateRafBalanceOrderMtDbEventMt4;
import static helpers.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventFactory.generateRafBalanceOrderMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MessageWithHeaders;
import helpers.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventMt4;
import helpers.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventMt5;
import helpers.kafka.mtEvents.RafBalanceOrderMtEvent;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Disabled
public class MtDbEventsRafBalanceOrderTest {

    @Test
    @DisplayName("Generate RAF balance order with event generator service from MT4 source with all fields populated")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("123")
    public void generateMt4RafBalanceOrderEventTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        RafBalanceOrderMtDbEventMt4 rafBalanceOrderMtDbEvent1 = generateRafBalanceOrderMtDbEventMt4();
        RafBalanceOrderMtDbEventMt4 rafBalanceOrderMtDbEvent2 = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderMtDbEvent2.data.comment = "Referral";

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderMtDbEvent1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderMtDbEvent2), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MessageWithHeaders consumedMessage1 = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderMtDbEvent1.data.openTime, true);
        RafBalanceOrderMtEvent retrievedRafBalanceOrderMtEvent1 = objectMapper.readValue(consumedMessage1.message(), RafBalanceOrderMtEvent.class);
        MessageWithHeaders consumedMessage2 = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderMtDbEvent2.data.openTime, true);
        RafBalanceOrderMtEvent retrievedRafBalanceOrderMtEvent2 = objectMapper.readValue(consumedMessage2.message(), RafBalanceOrderMtEvent.class);

        RafBalanceOrderMtEvent expectedRafBalanceOrderMtEvent1 = new RafBalanceOrderMtEvent(
                rafBalanceOrderMtDbEvent1.data.openTime, rafBalanceOrderMtDbEvent1.data.tradeId, rafBalanceOrderMtDbEvent1.data.mtAccount, rafBalanceOrderMtDbEvent1.data.comment, rafBalanceOrderMtDbEvent1.data.serverId, "raf");

        RafBalanceOrderMtEvent expectedRafBalanceOrderMtEvent2 = new RafBalanceOrderMtEvent(
                rafBalanceOrderMtDbEvent2.data.openTime, rafBalanceOrderMtDbEvent2.data.tradeId, rafBalanceOrderMtDbEvent2.data.mtAccount, rafBalanceOrderMtDbEvent2.data.comment, rafBalanceOrderMtDbEvent2.data.serverId, "raf");

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedRafBalanceOrderMtEvent1.id, notNullValue());
        assertThat(
                "Check all fields except id", retrievedRafBalanceOrderMtEvent1, equalTo(expectedRafBalanceOrderMtEvent1));

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedRafBalanceOrderMtEvent2.id, notNullValue());
        assertThat(
                "Check all fields except id", retrievedRafBalanceOrderMtEvent2, equalTo(expectedRafBalanceOrderMtEvent2));
    }

    @Test
    @DisplayName("Generate RAF balance order with event generator service from MT5 source with all fields populated")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("124")
    public void generateMt5RafBalanceOrderEventTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        RafBalanceOrderMtDbEventMt5 rafBalanceOrderMtDbEvent1 = generateRafBalanceOrderMtDbEventMt5();
        RafBalanceOrderMtDbEventMt5 rafBalanceOrderMtDbEvent2 = generateRafBalanceOrderMtDbEventMt5();
        rafBalanceOrderMtDbEvent2.data.comment = "Referral";

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderMtDbEvent1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderMtDbEvent2), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MessageWithHeaders consumedMessage1 = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderMtDbEvent1.data.openTime, true);
        RafBalanceOrderMtEvent retrievedRafBalanceOrderMtEvent1 = objectMapper.readValue(consumedMessage1.message(), RafBalanceOrderMtEvent.class);
        MessageWithHeaders consumedMessage2 = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderMtDbEvent2.data.openTime, true);
        RafBalanceOrderMtEvent retrievedRafBalanceOrderMtEvent2 = objectMapper.readValue(consumedMessage2.message(), RafBalanceOrderMtEvent.class);

        RafBalanceOrderMtEvent expectedRafBalanceOrderMtEvent1 = new RafBalanceOrderMtEvent(
                rafBalanceOrderMtDbEvent1.data.openTime, rafBalanceOrderMtDbEvent1.data.tradeId, rafBalanceOrderMtDbEvent1.data.mtAccount, rafBalanceOrderMtDbEvent1.data.comment, rafBalanceOrderMtDbEvent1.data.serverId, "raf");

        RafBalanceOrderMtEvent expectedRafBalanceOrderMtEvent2 = new RafBalanceOrderMtEvent(
                rafBalanceOrderMtDbEvent2.data.openTime, rafBalanceOrderMtDbEvent2.data.tradeId, rafBalanceOrderMtDbEvent2.data.mtAccount, rafBalanceOrderMtDbEvent2.data.comment, rafBalanceOrderMtDbEvent2.data.serverId, "raf");

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedRafBalanceOrderMtEvent1.id, notNullValue());
        assertThat(
                "Check all fields except id", retrievedRafBalanceOrderMtEvent1, equalTo(expectedRafBalanceOrderMtEvent1));

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedRafBalanceOrderMtEvent2.id, notNullValue());
        assertThat(
                "Check all fields except id", retrievedRafBalanceOrderMtEvent2, equalTo(expectedRafBalanceOrderMtEvent2));
    }
}
