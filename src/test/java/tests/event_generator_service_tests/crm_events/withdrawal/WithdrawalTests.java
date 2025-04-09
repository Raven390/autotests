package tests.event_generator_service_tests.crm_events.withdrawal;

import static business_objects.kafka.crm_db_events.withdrawal.WithdrawalDbEventFactory.generateWithdrawalDbEvent;
import static business_objects.kafka.crm_db_events.withdrawal.WithdrawalDbEventFactory.generateWithdrawalDbEventCps;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.TEAM_CORE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import business_objects.kafka.crm_db_events.withdrawal.WithdrawalDbEvent;
import business_objects.kafka.crm_db_events.withdrawal.WithdrawalDbEventCps;
import business_objects.kafka.crm_events.WithdrawalEvent;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature(FEATURE_EVENT_GENERATOR_SERVICE)
@Story(STORY_EVENT_GENERATOR_SERVICE_WITHDRAWAL)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_EVENT_GENERATOR_SERVICE)
class WithdrawalTests {
    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Generate withdrawal event with event generator service for tb_payment_withdraw table")
    @AllureId("67")
    void generateWithdrawalEventTest() throws JsonProcessingException, InterruptedException {
        WithdrawalDbEvent withdrawalDbEvent = generateWithdrawalDbEvent();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(withdrawalDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, withdrawalDbEvent.data.id.toString());
        WithdrawalEvent retrievedWithdrawalEvent = objectMapper.readValue(consumedMessage, WithdrawalEvent.class);

        WithdrawalEvent expectedWithdrawalEvent = new WithdrawalEvent(
                withdrawalDbEvent.data.createTime, withdrawalDbEvent.data.id, withdrawalDbEvent.data.userId, withdrawalDbEvent.data.mt4Account, withdrawalDbEvent.data.brand, withdrawalDbEvent.data.regulator, withdrawalDbEvent.data.paymentMethodCode, withdrawalDbEvent.data.withdrawType, withdrawalDbEvent.data.withdrawAmount, withdrawalDbEvent.data.fee, withdrawalDbEvent.data.actualAmount, withdrawalDbEvent.data.paymentAmount, withdrawalDbEvent.data.cardNumber, withdrawalDbEvent.data.isDel, withdrawalDbEvent.data.updateTime, withdrawalDbEvent.data.orderNumber, withdrawalDbEvent.data.cpsMandatoryField, withdrawalDbEvent.data.isRememberInfo, withdrawalDbEvent.data.upiAccountName, withdrawalDbEvent.data.deductCredit, withdrawalDbEvent.data.userSalesId, withdrawalDbEvent.data.accountSalesId, withdrawalDbEvent.data.orderCurrency, withdrawalDbEvent.data.checkingStatus, withdrawalDbEvent.data.isTrade, withdrawalDbEvent.data.rate, withdrawalDbEvent.data.isNonApp, withdrawalDbEvent.data.toUsdRate, "withdrawal"
        );

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedWithdrawalEvent.id, notNullValue());
        assertThat("Check all fields except id", retrievedWithdrawalEvent, equalTo(expectedWithdrawalEvent));
    }

    @Test
    @DisplayName("Generate withdrawal event with event generator service for tb_payment_withdraw_cps table")
    @AllureId("66")
    void generateWithdrawalEventCpsTest() throws JsonProcessingException, InterruptedException {

        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        WithdrawalDbEventCps withdrawalDbEventCps = generateWithdrawalDbEventCps();

        Allure.step("Write message to crm-db-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(withdrawalDbEventCps), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, withdrawalDbEventCps.data.id.toString());
        WithdrawalEvent retrievedWithdrawalEvent = objectMapper.readValue(consumedMessage, WithdrawalEvent.class);

        WithdrawalEvent expectedWithdrawalEvent = new WithdrawalEvent(
                withdrawalDbEventCps.data.createTime, withdrawalDbEventCps.data.id, withdrawalDbEventCps.data.userId, withdrawalDbEventCps.data.mt4Account, withdrawalDbEventCps.data.brand, withdrawalDbEventCps.data.regulator, "UnionPay", withdrawalDbEventCps.data.withdrawType, withdrawalDbEventCps.data.withdrawAmount, withdrawalDbEventCps.data.fee, withdrawalDbEventCps.data.actualAmount, withdrawalDbEventCps.data.paymentAmount, withdrawalDbEventCps.data.cardNumber, withdrawalDbEventCps.data.isDel, withdrawalDbEventCps.data.updateTime, withdrawalDbEventCps.data.orderNumber, null, null, null, withdrawalDbEventCps.data.deductCredit, withdrawalDbEventCps.data.userSalesId, withdrawalDbEventCps.data.accountSalesId, withdrawalDbEventCps.data.orderCurrency, withdrawalDbEventCps.data.checkingStatus, null, withdrawalDbEventCps.data.rate, null, withdrawalDbEventCps.data.toUsdRate, "withdrawal"
        );

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", retrievedWithdrawalEvent.id, notNullValue());
        assertThat("Check all fields except id", retrievedWithdrawalEvent, equalTo(expectedWithdrawalEvent));
    }
}
