package tests.event_generator_service_tests.mt_events.close_trade_data_dumper;

import business_objects.kafka.mt_data_dumper_events.close_trade.CloseTradeMt5;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseKafka;

import static business_objects.kafka.mt_data_dumper_events.close_trade.CloseTradeFactory.generateCloseTradeDataDumperMt5;
import static utils.Constants.*;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_CLOSE_TRADE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
@Disabled
public class CloseTradeMt5EventFiltrationTests extends TestBaseKafka {

    /*
    Algorithm for filtering trades
    MsgType = ‘Deal’
    Operation = 0
    Entry IN (1, 3)
    Action IN (0, 1)
     */

    @Test
    @AllureId("1168")
    @DisplayName("MT5 close trade event passing filtering. MsgType = ‘Deal’, Operation = 0, Entry = 1, Action=0")
    void filtrationMt5CloseTradeEventTest1() throws JsonProcessingException, InterruptedException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.header.setMsgType("Deal");
        closeTradeMt5.header.setOperation(0);
        closeTradeMt5.payload.setEntry(1);
        closeTradeMt5.payload.setAction(0);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

    }

    @Test
    @AllureId("1173")
    @DisplayName("MT5 close trade event passing filtering. MsgType = ‘Deal’, Operation = 0, Entry = 3, Action=1")
    void filtrationMt5CloseTradeEventTest6() throws JsonProcessingException, InterruptedException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.header.setMsgType("Deal");
        closeTradeMt5.header.setOperation(0);
        closeTradeMt5.payload.setEntry(3);
        closeTradeMt5.payload.setAction(1);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

    }

    @Test
    @AllureId("1169")
    @DisplayName("MT5 close trade event NOT passing filtering. MsgType != ‘Deal’")
    void filtrationMt5CloseTradeEventTest2() throws JsonProcessingException, InterruptedException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.header.setMsgType("Deal1");
        closeTradeMt5.header.setOperation(0);
        closeTradeMt5.payload.setEntry(3);
        closeTradeMt5.payload.setAction(1);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

    }

    @Test
    @AllureId("1170")
    @DisplayName("MT5 close trade event NOT passing filtering. Operation != 0")
    void filtrationMt5CloseTradeEventTest3() throws JsonProcessingException, InterruptedException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.header.setMsgType("Deal");
        closeTradeMt5.header.setOperation(1);
        closeTradeMt5.payload.setEntry(3);
        closeTradeMt5.payload.setAction(1);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

    }

    @Test
    @AllureId("1171")
    @DisplayName("MT5 close trade event NOT passing filtering. Entry NOT IN (1, 3)")
    void filtrationMt5CloseTradeEventTest4() throws JsonProcessingException, InterruptedException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.header.setMsgType("Deal");
        closeTradeMt5.header.setOperation(0);
        closeTradeMt5.payload.setEntry(2);
        closeTradeMt5.payload.setAction(1);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

    }

    @Test
    @AllureId("1172")
    @DisplayName("MT5 close trade event NOT passing filtering. Action NOT IN (0, 1)")
    void filtrationMt5CloseTradeEventTest5() throws JsonProcessingException, InterruptedException {

        CloseTradeMt5 closeTradeMt5 = generateCloseTradeDataDumperMt5();
        closeTradeMt5.header.setMsgType("Deal");
        closeTradeMt5.header.setOperation(0);
        closeTradeMt5.payload.setEntry(3);
        closeTradeMt5.payload.setAction(2);

        Allure.step("Write message to Mt5_Deal topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt5), KAFKA_TOPIC_MT_5_DEAL);

    }

}
