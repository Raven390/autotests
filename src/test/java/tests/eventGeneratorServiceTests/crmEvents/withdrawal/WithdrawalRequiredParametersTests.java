package tests.eventGeneratorServiceTests.crmEvents.withdrawal;

import static helpers.kafka.crmDbEvents.withdrawal.WithdrawalDbEventFactory.generateWithdrawalDbEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.MatchResultWithMessage;
import helpers.kafka.crmDbEvents.withdrawal.WithdrawalDbEvent;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_WITHDRAWAL)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
public class WithdrawalRequiredParametersTests {

    @Test
    @DisplayName("Generate withdrawal event with any of the required parameters = null and verify that the Event Generator didn't produce the event")
    @AllureId("68")
    public void generateWithdrawalEventsWithoutMandatoryParamsTest() throws JsonProcessingException {

        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        WithdrawalDbEvent withdrawalDbEventCreateTime = generateWithdrawalDbEvent();
        withdrawalDbEventCreateTime.data.createTime = null;

        WithdrawalDbEvent withdrawalDbEventId = generateWithdrawalDbEvent();
        withdrawalDbEventId.data.id = null;

        WithdrawalDbEvent withdrawalDbEventUserId = generateWithdrawalDbEvent();
        withdrawalDbEventUserId.data.userId = null;

        WithdrawalDbEvent withdrawalDbEventBrand = generateWithdrawalDbEvent();
        withdrawalDbEventBrand.data.brand = null;

        WithdrawalDbEvent withdrawalDbEventRegulator = generateWithdrawalDbEvent();
        withdrawalDbEventRegulator.data.regulator = null;

        WithdrawalDbEvent withdrawalDbEventTableName = generateWithdrawalDbEvent();
        withdrawalDbEventTableName.metadata.tableName = null;

        WithdrawalDbEvent withdrawalDbEventData = generateWithdrawalDbEvent();
        withdrawalDbEventData.data = null;

        WithdrawalDbEvent withdrawalDbEventMetadata = generateWithdrawalDbEvent();
        withdrawalDbEventMetadata.metadata = null;

        Allure.step("Write messages to crm-db-events topic");
        kafka.produceMessages("13", KAFKA_TOPIC_CRM_DB_EVENTS, objectMapper.writeValueAsString(withdrawalDbEventCreateTime), objectMapper.writeValueAsString(withdrawalDbEventId), objectMapper.writeValueAsString(withdrawalDbEventUserId), objectMapper.writeValueAsString(withdrawalDbEventBrand), objectMapper.writeValueAsString(withdrawalDbEventRegulator), objectMapper.writeValueAsString(withdrawalDbEventTableName), objectMapper.writeValueAsString(withdrawalDbEventData), objectMapper.writeValueAsString(withdrawalDbEventMetadata));

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        MatchResultWithMessage isAnyMatchPresentInMessages = kafka.isAnyMatchPresentInMessages(
                KAFKA_TOPIC_CRM_EVENTS, withdrawalDbEventCreateTime.data.id.toString(), withdrawalDbEventId.data.createTime, withdrawalDbEventUserId.data.id.toString(), withdrawalDbEventBrand.data.id.toString(), withdrawalDbEventRegulator.data.id.toString(), withdrawalDbEventTableName.data.id.toString(), withdrawalDbEventData.metadata.timestamp, withdrawalDbEventMetadata.data.id.toString());

        Allure.step("Verify that no matched results for events without mandatory params were found");
        assertThat(
                "Check if any matched results found. " + isAnyMatchPresentInMessages.message(), isAnyMatchPresentInMessages.matchResult(), equalTo(false));
    }
}
