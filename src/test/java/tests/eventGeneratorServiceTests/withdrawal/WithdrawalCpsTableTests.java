package tests.eventGeneratorServiceTests.withdrawal;

import static helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal.WithdrawalDbEventCpsTable.getWithdrawalDbEventCpsTable;
import static helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal.WithdrawalDbEventDataCpsTable.getWithdrawalDbEventDataCpsTable;
import static helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal.WithdrawalDbEventMetadataCpsTable.getWithdrawalDbEventMetadataCpsTable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;
import static utils.Utils.getRandomInt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal.*;
import helpers.kafka.crmEvents.eventGeneratorOutboundEvents.WithdrawalEvent;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class WithdrawalCpsTableTests {

    @Test
    @DisplayName("Generate withdrawal event with event generator service for tb_payment_withdraw_cps table")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE_EVENT_WITHDRAWAL)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("66")
    public void generateWithdrawalEventTest1() throws JsonProcessingException, InterruptedException {
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();

        // Create test data
        int id = getRandomInt();
        System.out.println("Generated id: " + id);
        String userId = "10076317";
        int mt4Account = 804_317_687;
        String accountName = "nouse";
        String currency = "USD";
        int status = 1;
        int withdrawType = 75;
        String withdrawAmount = "50.00000000";
        String fee = "50.00000000";
        String actualAmount = "50.00000000";
        String paymentAmount = "50.00000000";
        String cardNumber = "nouse";
        int isDel = 0;
        String createTime = "2024-09-30T19:24:26Z";
        String updateTime = "2024-09-30T19:24:26Z";
        String cpsAttachVariable = "{\"card_name\":\"nouse\",\"card_number\":\"nouse\"}";
        String orderNumber = "VU80431768720240930192426";
        String cpsMandatoryField =
                "address,amount,birthday,card_name,card_number,city,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip";
        int isRememberInfo = 0;
        String upiAccountName = "nouse";
        String deductCredit = "0000000000000000000000000000.00000000";
        int userSalesId = 10_015_169;
        int accountSalesId = 10_015_169;
        String orderCurrency = "840";
        String paymentMethodCode = "F00000_094";
        int checkingStatus = 0;
        int isTrade = 0;
        String rate = "1.000000";
        int isNonApp = 1;
        String toUsdRate = "1.000000";
        String brand = "Vantage";
        String regulator = "VFSC";
        // Prepare metadata object
        String timestamp = "2024-10-02T10:34:00.058786Z";
        String recordType = "data";
        String operation = "update";
        String partitionKeyType = "attribute-name";
        String schemaName = "dev_m_regulator_vfsc";
        String tableName = "tb_payment_withdraw_cps";
        String transactionId = "1847424366774639";
        String transactionRecordId = "1";
        String prevTransactionId = "1.84742436101607e+15";
        String prevTransactionRecordId = "1";
        String commitTimestamp = "2024-10-02T09:34:20.000000Z";
        String streamPosition =
                "mysql-bin-changelog.430137:18975760:0:18976975:1847424366774639:mysql-bin-changelog.430137:18975513";
        WithdrawalDbEventDataCpsTable data = getWithdrawalDbEventDataCpsTable(
                id,
                userId,
                mt4Account,
                accountName,
                currency,
                status,
                withdrawType,
                withdrawAmount,
                fee,
                actualAmount,
                paymentAmount,
                cardNumber,
                isDel,
                createTime,
                updateTime,
                cpsAttachVariable,
                orderNumber,
                cpsMandatoryField,
                isRememberInfo,
                upiAccountName,
                deductCredit,
                userSalesId,
                accountSalesId,
                orderCurrency,
                paymentMethodCode,
                checkingStatus,
                isTrade,
                rate,
                isNonApp,
                toUsdRate,
                brand,
                regulator);
        WithdrawalDbEventMetadataCpsTable metadata = getWithdrawalDbEventMetadataCpsTable(
                timestamp,
                recordType,
                operation,
                partitionKeyType,
                schemaName,
                tableName,
                transactionId,
                transactionRecordId,
                prevTransactionId,
                prevTransactionRecordId,
                commitTimestamp,
                streamPosition);

        Allure.step("Write message to crm-db-events topic");
        WithdrawalDbEventCpsTable crmDbEvent = getWithdrawalDbEventCpsTable(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, id);
        WithdrawalEvent withdrawalEvent = objectMapper.readValue(consumedMessage, WithdrawalEvent.class);

        Allure.step("Verify that message was written correctly");
        assertThat("Check UUID", withdrawalEvent.data.UUID, notNullValue());
        assertThat("Check create_time", withdrawalEvent.data.create_time, equalTo(createTime));
        assertThat("Check transfer_id", withdrawalEvent.data.transfer_id, equalTo(id));
        assertThat("Check brand", withdrawalEvent.data.brand, equalTo(brand));
        assertThat("Check regulator", withdrawalEvent.data.regulator, equalTo(regulator));
        assertThat("Check payment_method_code", withdrawalEvent.data.payment_method_code, equalTo("UnionPay"));
        assertThat("Check withdraw_type", withdrawalEvent.data.withdraw_type, equalTo(withdrawType));
        assertThat("Check withdraw_amount", withdrawalEvent.data.withdraw_amount, equalTo(withdrawAmount));
        assertThat("Check fee", withdrawalEvent.data.fee, equalTo(fee));
        assertThat("Check actual_amount", withdrawalEvent.data.actual_amount, equalTo(actualAmount));
        assertThat("Check payment_amount", withdrawalEvent.data.payment_amount, equalTo(paymentAmount));
        assertThat("Check wd_is_del", withdrawalEvent.data.wd_is_del, equalTo(isDel));
        assertThat("Check update_time", withdrawalEvent.data.update_time, equalTo(updateTime));
        assertThat("Check cps_attach_variable", withdrawalEvent.data.cps_attach_variable, equalTo(cpsAttachVariable));
        assertThat("Check order_number", withdrawalEvent.data.order_number, equalTo(orderNumber));
        assertThat("Check cps_mandatory_field", withdrawalEvent.data.cps_mandatory_field, equalTo(cpsMandatoryField));
        assertThat("Check wd_is_remember_info", withdrawalEvent.data.wd_is_remember_info, equalTo(isRememberInfo));
        assertThat("Check upi_account_name", withdrawalEvent.data.upi_account_name, equalTo(upiAccountName));
        assertThat("Check user_sales_id", withdrawalEvent.data.user_sales_id, equalTo(userSalesId));
        assertThat("Check account_sales_id", withdrawalEvent.data.account_sales_id, equalTo(accountSalesId));
        assertThat("Check order_currency", withdrawalEvent.data.order_currency, equalTo(orderCurrency));
        assertThat("Check checking_status", withdrawalEvent.data.checking_status, equalTo(checkingStatus));
        assertThat("Check is_trade", withdrawalEvent.data.is_trade, equalTo(isTrade));
        assertThat("Check rate", withdrawalEvent.data.rate, equalTo(rate));
        assertThat("Check is_non_app", withdrawalEvent.data.is_non_app, equalTo(isNonApp));
        assertThat("Check to_usd_rate", withdrawalEvent.data.to_usd_rate, equalTo(toUsdRate));
    }
}
