package tests.mtDbEventsTests.rafBalanceOrder;

import static helpers.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventFactory.generateRafBalanceOrderMtDbEventMt4;
import static helpers.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventFactory.generateRafBalanceOrderMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import helpers.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventMt4;
import helpers.kafka.mtDbEvents.rafBalanceOrder.RafBalanceOrderMtDbEventMt5;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Disabled
public class MtDbEventsRafBalanceOrderFiltrationTest {

    @Test
    @DisplayName(
        "Generate RAF balance order event with event generator service that should be filtered out by the Event Generator")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("125")
    public void generateRafBalanceOrderEventsAndVerifyTheyWereFilteredOutTest() throws JsonProcessingException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        //        Creation of Raf balance order events that should be filtered out by the filtration rules
        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTestAccount1 = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventTestAccount1.data.mtAccount = 741_000;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTestAccount2 = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventTestAccount2.data.mtAccount = 749_999;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTradeIdAccountServerId1 = generateRafBalanceOrderMtDbEventMt4();
        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventTradeIdAccountServerId2 = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventTradeIdAccountServerId2.data.tradeId = rafBalanceOrderEventTradeIdAccountServerId1.data.tradeId;
        rafBalanceOrderEventTradeIdAccountServerId2.data.mtAccount = rafBalanceOrderEventTradeIdAccountServerId1.data.mtAccount;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventMt4Cmd1 = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventMt4Cmd1.data.cmd = -1;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventMt4Cmd2 = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventMt4Cmd2.data.cmd = 2;

        RafBalanceOrderMtDbEventMt4 rafBalanceOrderEventMt4Comment = generateRafBalanceOrderMtDbEventMt4();
        rafBalanceOrderEventMt4Comment.data.comment = "test_comment";

        RafBalanceOrderMtDbEventMt5 rafBalanceOrderEventMt5Action1 = generateRafBalanceOrderMtDbEventMt5();
        rafBalanceOrderEventMt5Action1.data.action = -1;

        RafBalanceOrderMtDbEventMt5 rafBalanceOrderEventMt5Action2 = generateRafBalanceOrderMtDbEventMt5();
        rafBalanceOrderEventMt5Action2.data.action = 99;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderEventTestAccount1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderEventTestAccount2), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderEventTradeIdAccountServerId1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderEventTradeIdAccountServerId2), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderEventMt4Cmd1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderEventMt4Cmd2), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderEventMt4Comment), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderEventMt5Action1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(rafBalanceOrderEventMt5Action2), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderEventTestAccount1.data.openTime, rafBalanceOrderEventTestAccount2.data.openTime, rafBalanceOrderEventTradeIdAccountServerId1.data.openTime, rafBalanceOrderEventTradeIdAccountServerId2.data.openTime, rafBalanceOrderEventMt4Cmd1.data.openTime, rafBalanceOrderEventMt4Cmd2.data.openTime, rafBalanceOrderEventMt4Comment.data.openTime, rafBalanceOrderEventMt5Action1.data.openTime, rafBalanceOrderEventMt5Action2.data.openTime);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
