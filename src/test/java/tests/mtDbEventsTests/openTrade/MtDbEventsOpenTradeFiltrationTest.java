package tests.mtDbEventsTests.openTrade;

import static helpers.kafka.KafkaHelper.getEventTypeFromHeaders;
import static helpers.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventFactory.generateOpenTradeMtDbEventMt4;
import static helpers.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventFactory.generateOpenTradeMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import helpers.kafka.MessageWithHeaders;
import helpers.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventMt4;
import helpers.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventMt5;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class MtDbEventsOpenTradeFiltrationTest {

    @Test
    @DisplayName("Generate open event with event generator service that should be filtered out by the Event Generator")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("120")
    public void generateOpenTradeEventsAndVerifyTheyWereFilteredOutTest() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        //        Creation of open trade events that should be filtered out by the filtration rules
        OpenTradeMtDbEventMt4 openTradeEventTestAccount1 = generateOpenTradeMtDbEventMt4();
        openTradeEventTestAccount1.data.mtAccount = 741_000;

        OpenTradeMtDbEventMt4 openTradeEventTestAccount2 = generateOpenTradeMtDbEventMt4();
        openTradeEventTestAccount2.data.mtAccount = 749_999;

        OpenTradeMtDbEventMt4 openTradeEventTradeIdAccountServerId1 = generateOpenTradeMtDbEventMt4();
        OpenTradeMtDbEventMt4 openTradeEventTradeIdAccountServerId2 = generateOpenTradeMtDbEventMt4();
        openTradeEventTradeIdAccountServerId2.data.tradeId = openTradeEventTradeIdAccountServerId1.data.tradeId;
        openTradeEventTradeIdAccountServerId2.data.mtAccount = openTradeEventTradeIdAccountServerId1.data.mtAccount;

        OpenTradeMtDbEventMt4 openTradeEventTestCloseTime = generateOpenTradeMtDbEventMt4();
        openTradeEventTestCloseTime.data.closeTime = "2024-09-30T16:24:35.142706Z";

        OpenTradeMtDbEventMt4 openTradeEventMt4Cmd1 = generateOpenTradeMtDbEventMt4();
        openTradeEventMt4Cmd1.data.cmd = -1;

        OpenTradeMtDbEventMt4 openTradeEventMt4Cmd2 = generateOpenTradeMtDbEventMt4();
        openTradeEventMt4Cmd2.data.cmd = 2;

        OpenTradeMtDbEventMt5 openTradeEventMt5Entry1 = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Entry1.data.entry = -1;

        OpenTradeMtDbEventMt5 openTradeEventMt5Entry2 = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Entry2.data.entry = 2;

        OpenTradeMtDbEventMt5 openTradeEventMt5Action1 = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Action1.data.action = -1;

        OpenTradeMtDbEventMt5 openTradeEventMt5Action2 = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Action2.data.action = 99;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(openTradeEventTestAccount1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(openTradeEventTestAccount2), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(openTradeEventTradeIdAccountServerId1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(openTradeEventTradeIdAccountServerId2), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventMt4Cmd1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventMt4Cmd2), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventMt5Entry1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventMt5Entry2), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventMt5Action1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventMt5Action2), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, openTradeEventTestAccount1.data.openTime, openTradeEventTestAccount2.data.openTime, openTradeEventTradeIdAccountServerId2.data.openTime, openTradeEventMt4Cmd1.data.openTime, openTradeEventMt4Cmd2.data.openTime, openTradeEventMt5Entry1.data.openTime, openTradeEventMt5Entry2.data.openTime, openTradeEventMt5Action1.data.openTime, openTradeEventMt5Action2.data.openTime);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(openTradeEventTestCloseTime), KAFKA_TOPIC_MT_DB_EVENTS);
        Allure.step("Verify that event with close time was recognized as close trade");
        MessageWithHeaders consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_MT_EVENTS, openTradeEventTestCloseTime.data.closeTime, true);
        String eventType = getEventTypeFromHeaders(consumedMessage);
        assertThat("Check that the event was recognized as close event.", eventType, equalTo("closeTrade"));
    }
}
