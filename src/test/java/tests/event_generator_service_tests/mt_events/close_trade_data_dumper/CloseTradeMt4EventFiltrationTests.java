package tests.event_generator_service_tests.mt_events.close_trade_data_dumper;

import business_objects.kafka.mt_data_dumper_events.close_trade.CloseTradeMt4;
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

import static business_objects.kafka.mt_data_dumper_events.close_trade.CloseTradeFactory.generateCloseTradeDataDumperMt4;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_EVENT_GENERATOR_SERVICE;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_CLOSE_TRADE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
@Disabled
public class CloseTradeMt4EventFiltrationTests extends TestBaseKafka {

    /*
    Algorithm for filtering trades
    msg_type = ‘trade_record’
    operation = 1
    mode = 2
    close_time <> 0
    cmd IN (0, 1)
     */

    @Test
    @AllureId("1161")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time > 0, cmd = 0")
    void filtrationMt4CloseTradeEventTest1() throws JsonProcessingException, InterruptedException {

        CloseTradeMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        generateCloseTradeDataDumperMt4().header.setMsgType("trade_record");
        generateCloseTradeDataDumperMt4().header.setOperation(1);
        generateCloseTradeDataDumperMt4().payload.setMode(2);
        generateCloseTradeDataDumperMt4().payload.setCloseTime(1);
        generateCloseTradeDataDumperMt4().payload.setCmd(0);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);


    }

    @Test
    @AllureId("1162")
    @DisplayName("MT4 close trade event passing filtering. msg_type = ‘trade_record’, operation = 1, mode = 2, close_time > 0, cmd = 1")
    void filtrationMt4CloseTradeEventTest2() throws JsonProcessingException, InterruptedException {

        CloseTradeMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        generateCloseTradeDataDumperMt4().header.setMsgType("trade_record");
        generateCloseTradeDataDumperMt4().header.setOperation(1);
        generateCloseTradeDataDumperMt4().payload.setMode(2);
        generateCloseTradeDataDumperMt4().payload.setCloseTime(1);
        generateCloseTradeDataDumperMt4().payload.setCmd(1);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

    }

    @Test
    @AllureId("1163")
    @DisplayName("MT4 close trade event NOT passing filtering. msg_type != ‘trade_record’")
    void filtrationMt4CloseTradeEventTest3() throws JsonProcessingException, InterruptedException {

        CloseTradeMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        generateCloseTradeDataDumperMt4().header.setMsgType("tradeRecord");
        generateCloseTradeDataDumperMt4().header.setOperation(1);
        generateCloseTradeDataDumperMt4().payload.setMode(2);
        generateCloseTradeDataDumperMt4().payload.setCloseTime(1);
        generateCloseTradeDataDumperMt4().payload.setCmd(0);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

    }

    @Test
    @AllureId("1164")
    @DisplayName("MT4 close trade event NOT passing filtering. operation != 1")
    void filtrationMt4CloseTradeEventTest4() throws JsonProcessingException, InterruptedException {

        CloseTradeMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        generateCloseTradeDataDumperMt4().header.setMsgType("tradeRecord");
        generateCloseTradeDataDumperMt4().header.setOperation(2);
        generateCloseTradeDataDumperMt4().payload.setMode(2);
        generateCloseTradeDataDumperMt4().payload.setCloseTime(1);
        generateCloseTradeDataDumperMt4().payload.setCmd(0);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

    }

    @Test
    @AllureId("1165")
    @DisplayName("MT4 close trade event NOT passing filtering. mode != 2")
    void filtrationMt4CloseTradeEventTest5() throws JsonProcessingException, InterruptedException {

        CloseTradeMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        generateCloseTradeDataDumperMt4().header.setMsgType("tradeRecord");
        generateCloseTradeDataDumperMt4().header.setOperation(1);
        generateCloseTradeDataDumperMt4().payload.setMode(1);
        generateCloseTradeDataDumperMt4().payload.setCloseTime(1);
        generateCloseTradeDataDumperMt4().payload.setCmd(0);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

    }

    @Test
    @AllureId("1166")
    @DisplayName("MT4 close trade event NOT passing filtering. close_time = 0")
    void filtrationMt4CloseTradeEventTest6() throws JsonProcessingException, InterruptedException {

        CloseTradeMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        generateCloseTradeDataDumperMt4().header.setMsgType("tradeRecord");
        generateCloseTradeDataDumperMt4().header.setOperation(1);
        generateCloseTradeDataDumperMt4().payload.setMode(2);
        generateCloseTradeDataDumperMt4().payload.setCloseTime(0);
        generateCloseTradeDataDumperMt4().payload.setCmd(0);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);

    }

    @Test
    @AllureId("1167")
    @DisplayName("MT4 close trade event NOT passing filtering. cmd NOT IN (0, 1)")
    void filtrationMt4CloseTradeEventTest7() throws JsonProcessingException, InterruptedException {

        CloseTradeMt4 closeTradeMt4 = generateCloseTradeDataDumperMt4();
        generateCloseTradeDataDumperMt4().header.setMsgType("tradeRecord");
        generateCloseTradeDataDumperMt4().header.setOperation(1);
        generateCloseTradeDataDumperMt4().payload.setMode(2);
        generateCloseTradeDataDumperMt4().payload.setCloseTime(1);
        generateCloseTradeDataDumperMt4().payload.setCmd(2);

        Allure.step("Write message to mt4_trade_record topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(closeTradeMt4), KAFKA_TOPIC_MT_4_TRADE_RECORD);
    }
}
