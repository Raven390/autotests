package tests.mtDbEventsTests.closeTrade;

import static helpers.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventFactory.generateCloseTradeMtDbEventMt4;
import static helpers.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventFactory.generateCloseTradeMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import helpers.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventMt4;
import helpers.kafka.mtDbEvents.closeTrade.CloseTradeMtDbEventMt5;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class MtDbEventsCloseTradeFiltrationTest {

    @Test
    @DisplayName("Generate close event with event generator service that should be filtered out by the Event Generator")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("115")
    public void generateCloseTradeEventsAndVerifyTheyWereFilteredOutTest() throws JsonProcessingException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        //        Creation of close trade events that should be filtered out by the filtration rules
        CloseTradeMtDbEventMt4 closeTradeEventTestAccount1 = generateCloseTradeMtDbEventMt4();
        closeTradeEventTestAccount1.data.mtAccount = 741_000;

        CloseTradeMtDbEventMt4 closeTradeEventTestAccount2 = generateCloseTradeMtDbEventMt4();
        closeTradeEventTestAccount2.data.mtAccount = 749_999;

        CloseTradeMtDbEventMt4 closeTradeEventMt4CloseTimeNull = generateCloseTradeMtDbEventMt4();
        closeTradeEventMt4CloseTimeNull.data.closeTime = null;

        CloseTradeMtDbEventMt4 closeTradeEventMt4CloseTimeEmpty = generateCloseTradeMtDbEventMt4();
        closeTradeEventMt4CloseTimeEmpty.data.closeTime = "";

        CloseTradeMtDbEventMt4 closeTradeEventMt4CloseTimeZero = generateCloseTradeMtDbEventMt4();
        closeTradeEventMt4CloseTimeZero.data.closeTime = "0";

        CloseTradeMtDbEventMt4 closeTradeEventMt4Cmd1 = generateCloseTradeMtDbEventMt4();
        closeTradeEventMt4Cmd1.data.cmd = -1;

        CloseTradeMtDbEventMt4 closeTradeEventMt4Cmd2 = generateCloseTradeMtDbEventMt4();
        closeTradeEventMt4Cmd2.data.cmd = 2;

        CloseTradeMtDbEventMt5 closeTradeEventMt5Entry1 = generateCloseTradeMtDbEventMt5();
        closeTradeEventMt5Entry1.data.entry = -1;

        CloseTradeMtDbEventMt5 closeTradeEventMt5Entry2 = generateCloseTradeMtDbEventMt5();
        closeTradeEventMt5Entry2.data.entry = 2;

        CloseTradeMtDbEventMt5 closeTradeEventMt5Entry3 = generateCloseTradeMtDbEventMt5();
        closeTradeEventMt5Entry3.data.entry = 4;

        CloseTradeMtDbEventMt5 closeTradeEventMt5Action1 = generateCloseTradeMtDbEventMt5();
        closeTradeEventMt5Action1.data.action = -1;

        CloseTradeMtDbEventMt5 closeTradeEventMt5Action2 = generateCloseTradeMtDbEventMt5();
        closeTradeEventMt5Action2.data.action = 99;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(closeTradeEventTestAccount1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(closeTradeEventTestAccount2), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(closeTradeEventMt4CloseTimeNull), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(closeTradeEventMt4CloseTimeEmpty), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(closeTradeEventMt4CloseTimeZero), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(closeTradeEventMt4Cmd1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(closeTradeEventMt4Cmd2), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(closeTradeEventMt5Entry1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(closeTradeEventMt5Entry2), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(closeTradeEventMt5Entry3), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(closeTradeEventMt5Action1), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(closeTradeEventMt5Action2), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS, closeTradeEventTestAccount1.data.closeTime, closeTradeEventTestAccount2.data.closeTime, closeTradeEventMt4CloseTimeNull.metadata.timestamp, closeTradeEventMt4CloseTimeEmpty.metadata.timestamp, closeTradeEventMt4CloseTimeZero.metadata.timestamp, closeTradeEventMt4Cmd1.data.closeTime, closeTradeEventMt4Cmd2.data.closeTime, closeTradeEventMt5Entry1.data.closeTime, closeTradeEventMt5Entry2.data.closeTime, closeTradeEventMt5Entry3.data.closeTime, closeTradeEventMt5Action1.data.closeTime, closeTradeEventMt5Action2.data.closeTime);

        Allure.step("Verify that no matched results were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
