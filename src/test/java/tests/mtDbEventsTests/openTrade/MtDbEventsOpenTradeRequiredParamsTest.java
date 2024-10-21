package tests.mtDbEventsTests.openTrade;

import static helpers.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventFactory.generateOpenTradeMtDbEventMt4;
import static helpers.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventFactory.generateOpenTradeMtDbEventMt5;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.removeKeyFromJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import helpers.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventMt4;
import helpers.kafka.mtDbEvents.openTrade.OpenTradeMtDbEventMt5;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Disabled
public class MtDbEventsOpenTradeRequiredParamsTest {

    @Test
    @DisplayName(
            "Generate open event with any of the required parameters = null and verify that the Event Generator didn't produce the event")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("121")
    public void generateOpenTradeEventsWithoutMandatoryParamsTest() throws JsonProcessingException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        //        Creation of open trade events that should be filtered out by the filtration rules
        OpenTradeMtDbEventMt4 openTradeEventOpenTime = generateOpenTradeMtDbEventMt4();
        openTradeEventOpenTime.data.openTime = null;

        OpenTradeMtDbEventMt4 openTradeEventTradeId = generateOpenTradeMtDbEventMt4();
        openTradeEventTradeId.data.tradeId = null;

        OpenTradeMtDbEventMt4 openTradeEventMtAccount = generateOpenTradeMtDbEventMt4();
        openTradeEventMtAccount.data.mtAccount = null;

        OpenTradeMtDbEventMt4 openTradeEventServerId = generateOpenTradeMtDbEventMt4();
        openTradeEventServerId.data.serverId = null;

        OpenTradeMtDbEventMt4 openTradeEventMt4Cmd = generateOpenTradeMtDbEventMt4();
        openTradeEventMt4Cmd.data.cmd = null;

        OpenTradeMtDbEventMt5 openTradeEventMt5Entry = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Entry.data.entry = null;

        OpenTradeMtDbEventMt5 openTradeEventMt5Action = generateOpenTradeMtDbEventMt5();
        openTradeEventMt5Action.data.action = null;

        OpenTradeMtDbEventMt4 openTradeEventTableName = generateOpenTradeMtDbEventMt4();
        openTradeEventTableName.metadata.tableName = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventOpenTime), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventTradeId), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventMtAccount), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventServerId), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventMt4Cmd), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventMt5Entry), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventMt5Action), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventTableName), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                openTradeEventOpenTime.metadata.timestamp,
                openTradeEventTradeId.data.openTime,
                openTradeEventMtAccount.data.openTime,
                openTradeEventServerId.data.openTime,
                openTradeEventMt4Cmd.data.openTime,
                openTradeEventMt5Entry.data.openTime,
                openTradeEventMt5Action.data.openTime,
                openTradeEventTableName.data.openTime);

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }

    @Test
    @DisplayName(
            "Generate open event with any of the optional parameters = null and verify that the Event Generator produced the event")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("122")
    public void generateOpenTradeEventsWithoutOptionalParamsTest() throws JsonProcessingException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        //        Creation of Open trade events that should be filtered out by the filtration rules
        OpenTradeMtDbEventMt4 openTradeEventVolume = generateOpenTradeMtDbEventMt4();
        openTradeEventVolume.data.volume = null;

        OpenTradeMtDbEventMt4 openTradeEventSymbol = generateOpenTradeMtDbEventMt4();
        openTradeEventSymbol.data.symbol = null;

        OpenTradeMtDbEventMt4 openTradeEventTimestamp = generateOpenTradeMtDbEventMt4();
        openTradeEventTimestamp.metadata.timestamp = null;

        OpenTradeMtDbEventMt4 openTradeEventRecordType = generateOpenTradeMtDbEventMt4();
        openTradeEventRecordType.metadata.recordType = null;

        OpenTradeMtDbEventMt4 openTradeEventOperation = generateOpenTradeMtDbEventMt4();
        openTradeEventOperation.metadata.operation = null;

        OpenTradeMtDbEventMt4 openTradeEventPartitionKeyType = generateOpenTradeMtDbEventMt4();
        openTradeEventPartitionKeyType.metadata.partitionKeyType = null;

        OpenTradeMtDbEventMt4 openTradeEventSchemaName = generateOpenTradeMtDbEventMt4();
        openTradeEventSchemaName.metadata.schemaName = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventVolume), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventSymbol), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventTimestamp), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventRecordType), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventOperation), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage(
                "13", objectMapper.writeValueAsString(openTradeEventPartitionKeyType), KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", objectMapper.writeValueAsString(openTradeEventSchemaName), KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage areAllParamsPresentInMessages = kafka.areAllParamsPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                openTradeEventVolume.data.openTime,
                openTradeEventSymbol.data.openTime,
                openTradeEventTimestamp.data.openTime,
                openTradeEventRecordType.data.openTime,
                openTradeEventOperation.data.openTime,
                openTradeEventPartitionKeyType.data.openTime,
                openTradeEventSchemaName.data.openTime);

        Allure.step("Verify that all events without optional params were found in the messages");
        assertThat("Check if all matching events found.", areAllParamsPresentInMessages.matchResult(), equalTo(true));
    }

    @Test
    @DisplayName(
            "Generate open event with any of the required parameters missing from Json and verify that the Event Generator didn't produce the event")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("130")
    public void generateOpenTradeEventsWithMandatoryParamsMissingFromJsonTest() throws JsonProcessingException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        //        Creation of close trade events that should be filtered out by the filtration rules
        OpenTradeMtDbEventMt4 openTradeEventOpenTimeObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventOpenTime =
                removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventOpenTimeObject), "open_time");

        OpenTradeMtDbEventMt4 openTradeEventTradeIdObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventTradeId =
                removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventTradeIdObject), "trade_id");

        OpenTradeMtDbEventMt4 openTradeEventMtAccountObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventMtAccount =
                removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventMtAccountObject), "mt_account");

        OpenTradeMtDbEventMt4 openTradeEventServerIdObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventServerId =
                removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventServerIdObject), "ServerID");

        OpenTradeMtDbEventMt4 openTradeEventMt4CmdObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventMt4Cmd =
                removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventMt4CmdObject), "cmd");

        OpenTradeMtDbEventMt5 openTradeEventMt5EntryObject = generateOpenTradeMtDbEventMt5();
        String openTradeEventMt5Entry =
                removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventMt5EntryObject), "entry");

        OpenTradeMtDbEventMt5 openTradeEventMt5ActionObject = generateOpenTradeMtDbEventMt5();
        String openTradeEventMt5Action =
                removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventMt5ActionObject), "action");

        OpenTradeMtDbEventMt4 openTradeEventCloseTimeObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventCloseTime =
                removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventCloseTimeObject), "close_time");

        OpenTradeMtDbEventMt4 openTradeEventTableNameObject = generateOpenTradeMtDbEventMt4();
        String openTradeEventTableName =
                removeKeyFromJson(objectMapper.writeValueAsString(openTradeEventTableNameObject), "table-name");

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessage("13", openTradeEventOpenTime, KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", openTradeEventTradeId, KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", openTradeEventMtAccount, KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", openTradeEventServerId, KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", openTradeEventMt4Cmd, KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", openTradeEventMt5Entry, KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", openTradeEventMt5Action, KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", openTradeEventCloseTime, KAFKA_TOPIC_MT_DB_EVENTS);
        kafka.produceMessage("13", openTradeEventTableName, KAFKA_TOPIC_MT_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_MT_EVENTS,
                openTradeEventOpenTimeObject.metadata.timestamp,
                openTradeEventTradeIdObject.data.openTime,
                openTradeEventMtAccountObject.data.openTime,
                openTradeEventServerIdObject.data.openTime,
                openTradeEventMt4CmdObject.data.openTime,
                openTradeEventMt5EntryObject.data.openTime,
                openTradeEventMt5ActionObject.data.openTime,
                openTradeEventCloseTimeObject.data.openTime,
                openTradeEventTableNameObject.data.openTime);

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(),
                isAnyMatchPresentInMessages.matchResult(),
                equalTo(false));
    }
}
