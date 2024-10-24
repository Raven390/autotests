package tests.eventGeneratorServiceTests.crmEvents.withdrawal;

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
    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Generate withdrawal event with event generator service for tb_payment_withdraw_cps table")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("66")
    public void generateWithdrawalEventTest1() throws JsonProcessingException, InterruptedException {

        // Create test data
        int id = getRandomInt();
        System.out.println("Generated id: " + id);
        String clientId = "10076317";
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
        String cpsMandatoryField = "address,amount,birthday,card_name,card_number,city,email,first_name,last_name,notify_url,order_currency,order_id,payment_method,phone,profiles,province,state,submit_time,timestamp,transaction_type,user_id,zip";
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
        String cardHash = "nouse";
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
        String streamPosition = "mysql-bin-changelog.430137:18975760:0:18976975:1847424366774639:mysql-bin-changelog.430137:18975513";
        WithdrawalDbEventDataCpsTable data = getWithdrawalDbEventDataCpsTable(id, clientId, mt4Account, accountName, currency, status, withdrawType, withdrawAmount, fee, actualAmount, paymentAmount, cardNumber, isDel, createTime, updateTime, cpsAttachVariable, orderNumber, cpsMandatoryField, isRememberInfo, upiAccountName, deductCredit, userSalesId, accountSalesId, orderCurrency, paymentMethodCode, checkingStatus, isTrade, rate, isNonApp, toUsdRate, brand, regulator);
        WithdrawalDbEventMetadataCpsTable metadata = getWithdrawalDbEventMetadataCpsTable(timestamp, recordType, operation, partitionKeyType, schemaName, tableName, transactionId, transactionRecordId, prevTransactionId, prevTransactionRecordId, commitTimestamp, streamPosition);

        Allure.step("Write message to crm-db-events topic");
        WithdrawalDbEventCpsTable crmDbEvent = getWithdrawalDbEventCpsTable(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessage(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(id));
        WithdrawalEvent withdrawalEvent = objectMapper.readValue(consumedMessage, WithdrawalEvent.class);

        Allure.step("Verify that message was written correctly");
        assertThat("Check id", withdrawalEvent.id, notNullValue());
        assertThat("Check eventDate", withdrawalEvent.eventDate, equalTo(createTime));
        assertThat("Check withdrawalId", withdrawalEvent.withdrawalId, equalTo(id));
        assertThat("Check clientId", withdrawalEvent.clientId, equalTo(Integer.parseInt(clientId)));
        assertThat("Check metaTraderAccount", withdrawalEvent.metaTraderAccount, equalTo(mt4Account));
        assertThat("Check brand", withdrawalEvent.brand, equalTo(brand));
        assertThat("Check regulator", withdrawalEvent.regulator, equalTo(regulator));
        assertThat("Check paymentMethodCode", withdrawalEvent.paymentMethodCode, equalTo("UnionPay"));
        assertThat("Check withdrawType", withdrawalEvent.withdrawType, equalTo(withdrawType));
        assertThat("Check withdrawalAmount", withdrawalEvent.withdrawalAmount, equalTo(withdrawAmount));
        assertThat("Check fee", withdrawalEvent.fee, equalTo(fee));
        assertThat("Check actualAmount", withdrawalEvent.actualAmount, equalTo(actualAmount));
        assertThat("Check paymentAmount", withdrawalEvent.paymentAmount, equalTo(paymentAmount));
        assertThat("Check cardHash", withdrawalEvent.cardHash, equalTo(cardHash));
        assertThat("Check wdIsDel", withdrawalEvent.wdIsDel, equalTo(isDel));
        assertThat("Check updateTime", withdrawalEvent.updateTime, equalTo(updateTime));
        assertThat("Check cpsAttachVariable", withdrawalEvent.cpsAttachVariable, equalTo(cpsAttachVariable));
        assertThat("Check orderNumber", withdrawalEvent.orderNumber, equalTo(orderNumber));
        assertThat("Check cpsMandatoryField", withdrawalEvent.cpsMandatoryField, equalTo(cpsMandatoryField));
        assertThat("Check wdIsRememberInfo", withdrawalEvent.wdIsRememberInfo, equalTo(isRememberInfo));
        assertThat("Check upiAccountName", withdrawalEvent.upiAccountName, equalTo(upiAccountName));
        assertThat("Check deductCredit", withdrawalEvent.deductCredit, equalTo("0E-8"));
        assertThat("Check userSalesId", withdrawalEvent.userSalesId, equalTo(userSalesId));
        assertThat("Check accountSalesId", withdrawalEvent.accountSalesId, equalTo(accountSalesId));
        assertThat("Check withdrawalCurrency", withdrawalEvent.withdrawalCurrency, equalTo(orderCurrency));
        assertThat("Check checkingStatus", withdrawalEvent.checkingStatus, equalTo(checkingStatus));
        assertThat("Check wdIsTrade", withdrawalEvent.wdIsTrade, equalTo(isTrade));
        assertThat("Check rate", withdrawalEvent.rate, equalTo(rate));
        assertThat("Check wdIsNonApp", withdrawalEvent.wdIsNonApp, equalTo(isNonApp));
        assertThat("Check toUsdRate", withdrawalEvent.toUsdRate, equalTo(toUsdRate));
        assertThat("Check type", withdrawalEvent.type, equalTo("withdrawal"));
    }
}
