package tests.event_generator_service_tests.mt_events.raf_balance_order;

import static business_objects.kafka.mt_db_events.raf_balance_order.RafBalanceOrderMtDbEventFactory.generateRafBalanceOrderMtDbEventMt4;
import static business_objects.kafka.mt_db_events.raf_balance_order.RafBalanceOrderMtDbEventFactory.generateRafBalanceOrderMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import business_objects.kafka.mt_db_events.raf_balance_order.RafBalanceOrderMtDbEventMt4;
import business_objects.kafka.mt_db_events.raf_balance_order.RafBalanceOrderMtDbEventMt5;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_RAF_BALANCE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class MtDbEventsRafBalanceOrderFiltrationTest {

    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName(
        "Generate RAF balance order event with event generator service that should be filtered out by the Event Generator")
    @AllureId("125")
    void generateRafBalanceOrderEventsAndVerifyTheyWereFilteredOutTest() throws JsonProcessingException {

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
        kafka.produceMessages("13", KAFKA_TOPIC_MT_DB_EVENTS, objectMapper.writeValueAsString(rafBalanceOrderEventTestAccount1), objectMapper.writeValueAsString(rafBalanceOrderEventTestAccount2), objectMapper.writeValueAsString(rafBalanceOrderEventTradeIdAccountServerId1), objectMapper.writeValueAsString(rafBalanceOrderEventTradeIdAccountServerId2), objectMapper.writeValueAsString(rafBalanceOrderEventMt4Cmd1), objectMapper.writeValueAsString(rafBalanceOrderEventMt4Cmd2), objectMapper.writeValueAsString(rafBalanceOrderEventMt4Comment), objectMapper.writeValueAsString(rafBalanceOrderEventMt5Action1), objectMapper.writeValueAsString(rafBalanceOrderEventMt5Action2));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, rafBalanceOrderEventTestAccount1.data.openTime, rafBalanceOrderEventTestAccount2.data.openTime, rafBalanceOrderEventTradeIdAccountServerId2.data.openTime, rafBalanceOrderEventMt4Cmd1.data.openTime, rafBalanceOrderEventMt4Cmd2.data.openTime, rafBalanceOrderEventMt4Comment.data.openTime, rafBalanceOrderEventMt5Action1.data.openTime, rafBalanceOrderEventMt5Action2.data.openTime);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
