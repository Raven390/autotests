package tests.eventGeneratorServiceTests.withdrawal;

import static helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal.WithdrawalDbEventData.getWithdrawalDbEventData;
import static helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal.WithdrawalDbEventMetadata.getWithdrawalDbEventMetadata;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;
import static utils.Utils.getRandomInt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal.WithdrawalDbEvent;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal.WithdrawalDbEventData;
import helpers.kafka.crmDbEvents.eventGeneratorInbound.withdrawal.WithdrawalDbEventMetadata;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class WithdrawalRequiredParametersTests {
    KafkaHelper kafka = new KafkaHelper();
    ObjectMapper objectMapper = new ObjectMapper();

    // Create test data
    int id = getRandomInt();
    int userId = getRandomInt();
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
    String tableName = "tb_payment_withdraw";
    String transactionId = "1847424366774639";
    String transactionRecordId = "1";
    String prevTransactionId = "1.84742436101607e+15";
    String prevTransactionRecordId = "1";
    String commitTimestamp = "2024-10-02T09:34:20.000000Z";
    String streamPosition =
            "mysql-bin-changelog.430137:18975760:0:18976975:1847424366774639:mysql-bin-changelog.430137:18975513";

    @Test
    @DisplayName("Generate withdrawal event with user_id=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("68")
    public void generateWithdrawalEventTest() throws JsonProcessingException, InterruptedException {

        WithdrawalDbEventData data = getWithdrawalDbEventData(
                id,
                null,
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
        WithdrawalDbEventMetadata metadata = getWithdrawalDbEventMetadata(
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
        WithdrawalDbEvent crmDbEvent = WithdrawalDbEvent.getWithdrawalDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(id));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate withdrawal event with create_time=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("69")
    public void generateWithdrawalEventTest1() throws JsonProcessingException, InterruptedException {

        WithdrawalDbEventData data = getWithdrawalDbEventData(
                id,
                String.valueOf(userId),
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
                null,
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
        WithdrawalDbEventMetadata metadata = getWithdrawalDbEventMetadata(
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
        WithdrawalDbEvent crmDbEvent = WithdrawalDbEvent.getWithdrawalDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(id));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate withdrawal event with transfer_id=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("70")
    public void generateWithdrawalEventTest2() throws JsonProcessingException, InterruptedException {

        WithdrawalDbEventData data = getWithdrawalDbEventData(
                null,
                String.valueOf(userId),
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
        WithdrawalDbEventMetadata metadata = getWithdrawalDbEventMetadata(
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
        WithdrawalDbEvent crmDbEvent = WithdrawalDbEvent.getWithdrawalDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate withdrawal event with brand=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("71")
    public void generateWithdrawalEventTest3() throws JsonProcessingException, InterruptedException {

        WithdrawalDbEventData data = getWithdrawalDbEventData(
                id,
                String.valueOf(userId),
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
                null,
                regulator);
        WithdrawalDbEventMetadata metadata = getWithdrawalDbEventMetadata(
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
        WithdrawalDbEvent crmDbEvent = WithdrawalDbEvent.getWithdrawalDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate withdrawal event with regulator=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("72")
    public void generateWithdrawalEventTest4() throws JsonProcessingException, InterruptedException {

        WithdrawalDbEventData data = getWithdrawalDbEventData(
                id,
                String.valueOf(userId),
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
                null);
        WithdrawalDbEventMetadata metadata = getWithdrawalDbEventMetadata(
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
        WithdrawalDbEvent crmDbEvent = WithdrawalDbEvent.getWithdrawalDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate withdrawal event with metadata=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("73")
    public void generateWithdrawalEventTest5() throws JsonProcessingException, InterruptedException {

        WithdrawalDbEventData data = getWithdrawalDbEventData(
                id,
                String.valueOf(userId),
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
                null);

        Allure.step("Write message to crm-db-events topic");
        WithdrawalDbEvent crmDbEvent = WithdrawalDbEvent.getWithdrawalDbEvent(data, null);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate withdrawal event with data=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("74")
    public void generateWithdrawalEventTest6() throws JsonProcessingException, InterruptedException {

        WithdrawalDbEventMetadata metadata = getWithdrawalDbEventMetadata(
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
        WithdrawalDbEvent crmDbEvent = WithdrawalDbEvent.getWithdrawalDbEvent(null, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }

    @Test
    @DisplayName("Generate withdrawal event with table_name=null parameter")
    @Feature(FEATURE_EVENT_GENERATOR_SERVICE)
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(LAYER_API)
    @AllureId("81")
    public void generateWithdrawalEventTest12() throws JsonProcessingException, InterruptedException {

        WithdrawalDbEventData data = getWithdrawalDbEventData(
                id,
                String.valueOf(userId),
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
        WithdrawalDbEventMetadata metadata = getWithdrawalDbEventMetadata(
                timestamp,
                recordType,
                operation,
                partitionKeyType,
                schemaName,
                null,
                transactionId,
                transactionRecordId,
                prevTransactionId,
                prevTransactionRecordId,
                commitTimestamp,
                streamPosition);

        Allure.step("Write message to crm-db-events topic");
        WithdrawalDbEvent crmDbEvent = WithdrawalDbEvent.getWithdrawalDbEvent(data, metadata);
        kafka.produceMessage("13", objectMapper.writeValueAsString(crmDbEvent), KAFKA_TOPIC_CRM_DB_EVENTS);

        Allure.step("Wait for event generator do some magic and consume message from crm-events topic");
        String consumedMessage = kafka.consumeMessages(KAFKA_TOPIC_CRM_EVENTS, String.valueOf(userId));

        Allure.step("Verify that message was not found");
        assertThat(
                "Check message", consumedMessage, equalTo("Max attempts reached without finding a matching message."));
    }
}
