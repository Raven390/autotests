package tests.event_generator_service_tests.mt_events.open_trade;

import static business_objects.kafka.mt_db_events.open_trade.OpenTradeMtDbEventFactory.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.kafka.MatchResultWithMessage;
import helpers.kafka.MessageWithHeaders;
import business_objects.kafka.mt_db_events.open_trade.OpenTradeMtDbEventMt4;
import business_objects.kafka.mt_db_events.open_trade.OpenTradeMtDbEventMt5;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseKafka;

import java.time.Instant;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_OPEN_TRADE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class MtDbEventsOpenTradeFiltrationTest extends TestBaseKafka {

    @Test
    @DisplayName("Generate open event with event generator service that should be filtered out by the Event Generator")
    @AllureId("120")
    void generateOpenTradeEventsAndVerifyTheyWereFilteredOutTest() throws JsonProcessingException,
            InterruptedException {

        //Creation of open trade events that should be filtered out by the filtration rules
        OpenTradeMtDbEventMt4 openTradeEventTestAccount1 = generateOpenTradeMtDbEventMt4();
        openTradeEventTestAccount1.data.mtAccount = 741_000;
        openTradeEventTestAccount1.data.openTime = "2025-04-30 07:59:01";

        OpenTradeMtDbEventMt4 openTradeEventTestAccount2 = generateOpenTradeMtDbEventMt4();
        openTradeEventTestAccount2.data.mtAccount = 749_999;
        openTradeEventTestAccount2.data.openTime = "2025-04-30 07:59:02";

        OpenTradeMtDbEventMt4 openTradeEventTradeIdAccountServerId1 = generateOpenTradeMtDbEventMt4();
        openTradeEventTradeIdAccountServerId1.data.openTime = "2025-04-30 07:59:03";
        OpenTradeMtDbEventMt4 openTradeEventTradeIdAccountServerId2 = generateOpenTradeMtDbEventMt4();
        openTradeEventTradeIdAccountServerId2.data.openTime = "2025-04-30 07:59:04";
        openTradeEventTradeIdAccountServerId2.data.tradeId = openTradeEventTradeIdAccountServerId1.data.tradeId;
        openTradeEventTradeIdAccountServerId2.data.mtAccount = openTradeEventTradeIdAccountServerId1.data.mtAccount;

        OpenTradeMtDbEventMt4 openTradeEventTestCloseTime = generateOpenTradeMtDbEventMt4();
        openTradeEventTestCloseTime.data.openTime = "2025-04-30 07:59:05";
        openTradeEventTestCloseTime.data.closeTime = Instant.now().toString();
        openTradeEventTestCloseTime.data.modifyTime = Instant.now().plusMillis(100_000).toString();

        OpenTradeMtDbEventMt4 openTradeEventMt4Cmd1 = generateOpenTradeMtDbEventMt4();
        openTradeEventMt4Cmd1.data.cmd = -1;
        openTradeEventMt4Cmd1.data.openTime = "2025-04-30 07:59:06";

        OpenTradeMtDbEventMt4 openTradeEventMt4Cmd2 = generateOpenTradeMtDbEventMt4();
        openTradeEventMt4Cmd2.data.cmd = 2;
        openTradeEventMt4Cmd2.data.openTime = "2025-04-30 07:59:07";

        OpenTradeMtDbEventMt5 openTradeEventMt5Entry1 = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Entry1.data.entry = -1;
        openTradeEventMt5Entry1.data.openTime = "2025-04-30 07:59:08";

        OpenTradeMtDbEventMt5 openTradeEventMt5Entry2 = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Entry2.data.entry = 2;
        openTradeEventMt5Entry2.data.openTime = "2025-04-30 07:59:09";

        OpenTradeMtDbEventMt5 openTradeEventMt5Action1 = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Action1.data.action = -1;
        openTradeEventMt5Action1.data.openTime = "2025-04-30 07:59:10";

        OpenTradeMtDbEventMt5 openTradeEventMt5Action2 = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Action2.data.action = 99;
        openTradeEventMt5Action2.data.openTime = "2025-04-30 07:59:11";

        OpenTradeMtDbEventMt4 openTradeEventTestCloseTimeNull = generateOpenTradeMtDbEventMt4();
        openTradeEventTestCloseTimeNull.data.closeTime = null;
        openTradeEventTestCloseTimeNull.data.openTime = "2025-04-30 07:59:12";

        OpenTradeMtDbEventMt4 openTradeEventTestCloseTimeZero = generateOpenTradeMtDbEventMt4();
        openTradeEventTestCloseTimeNull.data.closeTime = "0";
        openTradeEventTestCloseTimeNull.data.openTime = "2025-04-30 07:59:13";

        OpenTradeMtDbEventMt4 openTradeEventTestCloseTimeEmpty = generateOpenTradeMtDbEventMt4();
        openTradeEventTestCloseTimeNull.data.closeTime = "";
        openTradeEventTestCloseTimeNull.data.openTime = "2025-04-30 07:59:14";

        OpenTradeMtDbEventMt4 openTradeEventTestCloseTimeDefault = generateOpenTradeMtDbEventMt4();
        openTradeEventTestCloseTimeNull.data.closeTime = "1970-01-01T00:00:00Z";
        openTradeEventTestCloseTimeNull.data.openTime = "2025-04-30 07:59:15";

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages(KAFKA_MESSAGE_KEY, KAFKA_TOPIC_MT_DB_EVENTS, objectMapper.writeValueAsString(openTradeEventTestAccount1), objectMapper.writeValueAsString(openTradeEventTestAccount2), objectMapper.writeValueAsString(openTradeEventTradeIdAccountServerId1), objectMapper.writeValueAsString(openTradeEventTradeIdAccountServerId2), objectMapper.writeValueAsString(openTradeEventMt4Cmd1), objectMapper.writeValueAsString(openTradeEventMt4Cmd2), objectMapper.writeValueAsString(openTradeEventMt5Entry1), objectMapper.writeValueAsString(openTradeEventMt5Entry2), objectMapper.writeValueAsString(openTradeEventMt5Action1), objectMapper.writeValueAsString(openTradeEventMt5Action2));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, openTradeEventTestAccount1.data.openTime, openTradeEventTestAccount2.data.openTime, openTradeEventTradeIdAccountServerId2.data.openTime, openTradeEventMt4Cmd1.data.openTime, openTradeEventMt4Cmd2.data.openTime, openTradeEventMt5Entry1.data.openTime, openTradeEventMt5Entry2.data.openTime, openTradeEventMt5Action1.data.openTime, openTradeEventMt5Action2.data.openTime, openTradeEventTestCloseTimeNull.data.closeTime, openTradeEventTestCloseTimeZero.data.closeTime, openTradeEventTestCloseTimeEmpty.data.closeTime, openTradeEventTestCloseTimeDefault.data.closeTime);

        Allure.step("Verify that no matched results were found");
        assertThat("Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));

        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(openTradeEventTestCloseTime), KAFKA_TOPIC_MT_DB_EVENTS);
        Allure.step("Verify that event with close time was recognized as close trade");
        MessageWithHeaders consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_MT_EVENTS, openTradeEventTestCloseTime.data.closeTime, true);
        CloseTradeMtEvent retrievedCloseTradeMtEvent = objectMapper.readValue(consumedMessage.message(), CloseTradeMtEvent.class);
        assertThat("Check that the event was recognized as close event.", retrievedCloseTradeMtEvent.type, equalTo(EG_CLOSE_TRADE_EVENT));
    }
}
