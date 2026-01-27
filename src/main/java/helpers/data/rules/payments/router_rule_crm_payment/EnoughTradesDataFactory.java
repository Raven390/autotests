package helpers.data.rules.payments.router_rule_crm_payment;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnection;
import static business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObjectFactory.generateMt4TradesObject;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateMt5DealsCoercedObject;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClientZero;
import static business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObjectFactory.generatePaymentDecisionObject;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObjectFactory.generatePaymentRuleExecutionsObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.cost_payment_fee.CostPaymentFee;
import business_objects.db.clickhouse.crm_tb_deposit_channel.CrmTbDepositChannelObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_type.CrmTbDepositTypeObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntity;
import business_objects.db.clickhouse.crm_tb_withdrawal_type.CrmTbWithdrawalTypeObject;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.mt_mt4_trades.MtMt4TradesObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.db.clickhouse.s3_fact_login_metrics.S3FactLoginMetricsObject;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import helpers.database.DbName;
import io.qameta.allure.Description;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Utils;

public class EnoughTradesDataFactory {
    private static final ClientHelper enoughTradesRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient31 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient41 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient9 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient10 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient11 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient12 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient13 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient14 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient15 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient16 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient17 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient18 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient19 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient20 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient21 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient22 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient23 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient24 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClient25 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClientAlert1 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClientAlert2 = getRandomVantageClientAllFields();
    private static final ClientHelper enoughTradesRuleClientAlert3 = getRandomVantageClientAllFields();

    @Description("Create data for Withdrawal Integrity check rule")
    private static DataHelper getEnoughTradesRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        data.createClient(client);
        data.crmWithdrawalEventV2 = CrmWithdrawalEventV2.builder()
                .accountType("MT4")
                .binNumber(Utils.getRandomIntPositive().toString())
                .brand(data.clientHelper.getBrand().toLowerCase())
                .checkName("")
                .clientId(data.clientHelper.getUserId())
                .eventDate(Instant.now().toString())
                .expMonth("4")
                .expYear("2030")
                .fullName(data.clientHelper.getFirstName())
                .id(getRandomUuidString())
                .merchantOrderId("VTSG" + getRandomIntPositive())
                .mt4Account(data.clientHelper.getTradingAccount())
                .paymentChannelCode(PAYMENT_PROVIDER_FASAPAY)
                .paymentChannelName("-")
                .paymentMethodCode("INTERNATIONAL_WIRE_TRANSFER")
                .platform("WEB")
                .regulator(data.clientHelper.getRegulator())
                .schemaVersion("1.0")
                .type(Event.CRM_WITHDRAWAL_EVENT.getName())
                .withdrawalAmount(1.0)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(Long.valueOf(getRandomIntPositive()))
                .status("Risk audit")
                .build();
        return data;
    }

    private static DataHelper getEnoughTradesTest1Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient1);
        data.crmWithdrawalEventV2.setAccountCategory("IB");
        return data;
    }

    private static DataHelper getEnoughTradesTest2Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient2);
        data.crmWithdrawalEventV2.setFundType("PAMM");

        return data;
    }

    private static DataHelper getEnoughTradesTest3Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient3);
        data.crmWithdrawalEventV2.setFundType("MAM");
        return data;
    }

    private static DataHelper getEnoughTradesTest31Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient31);
        data.crmWithdrawalEventV2.setAccountType("MTS");
        return data;
    }

    private static DataHelper getEnoughTradesTest4Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient4);
        return data;
    }

    private static DataHelper getEnoughTradesTest41Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient41);
        return data;
    }

    private static DataHelper getEnoughTradesTest5Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient5);
        return data;
    }

    private static DataHelper getEnoughTradesTest6Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient6);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1000.0);
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(100.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        return data;
    }

    private static DataHelper getEnoughTradesTest7Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient7);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1000.0);
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(100.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(175.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        return data;
    }

    private static DataHelper getEnoughTradesTest8Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient8);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1000.0);

        // set connections
        ClientHelper connectedClient1 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient1);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(100.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity connectedDeposit11 = generateCrmTbDepositEntityByClient(connectedClient1);
        CrmTbDepositEntity connectedDeposit21 = generateCrmTbDepositEntityByClient(connectedClient2);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, connectedDeposit11, connectedDeposit21);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(100.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        return data;
    }

    private static DataHelper getEnoughTradesTest9Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient9);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);

        // set connections
        ClientHelper connectedClient1 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient1);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity connectedWithdrawal11 = generateCrmTbWithdrawalEntityByClient(connectedClient1);
        CrmTbWithdrawalEntity connectedWithdrawal21 = generateCrmTbWithdrawalEntityByClient(connectedClient2);
        data.crmTbWithdrawalObjects =
                (List.of(withdrawal1, withdrawal2, withdrawal3, connectedWithdrawal11, connectedWithdrawal21));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(99.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity connectedDeposit11 = generateCrmTbDepositEntityByClient(connectedClient1);
        CrmTbDepositEntity connectedDeposit21 = generateCrmTbDepositEntityByClient(connectedClient2);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, connectedDeposit11, connectedDeposit21);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(101.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        return data;
    }

    private static DataHelper getEnoughTradesTest10Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient10);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);

        // set connections
        ClientHelper connectedClient1 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient1);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity connectedWithdrawal11 = generateCrmTbWithdrawalEntityByClient(connectedClient1);
        CrmTbWithdrawalEntity connectedWithdrawal21 = generateCrmTbWithdrawalEntityByClient(connectedClient2);
        data.crmTbWithdrawalObjects =
                (List.of(withdrawal1, withdrawal2, withdrawal3, connectedWithdrawal11, connectedWithdrawal21));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(100.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity connectedDeposit11 = generateCrmTbDepositEntityByClient(connectedClient1);
        CrmTbDepositEntity connectedDeposit21 = generateCrmTbDepositEntityByClient(connectedClient2);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, connectedDeposit11, connectedDeposit21);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(101.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        return data;
    }

    private static DataHelper getEnoughTradesTest11Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient11);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);

        // set connections
        ClientHelper connectedClient1 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient1);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity connectedWithdrawal11 = generateCrmTbWithdrawalEntityByClient(connectedClient1);
        CrmTbWithdrawalEntity connectedWithdrawal21 = generateCrmTbWithdrawalEntityByClient(connectedClient2);
        data.crmTbWithdrawalObjects =
                (List.of(withdrawal1, withdrawal2, withdrawal3, connectedWithdrawal11, connectedWithdrawal21));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(100.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity connectedDeposit11 = generateCrmTbDepositEntityByClient(connectedClient1);
        CrmTbDepositEntity connectedDeposit21 = generateCrmTbDepositEntityByClient(connectedClient2);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, connectedDeposit11, connectedDeposit21);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(101.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(250.0);
        deal.setCommissionUsd(250.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        return data;
    }

    private static DataHelper getEnoughTradesTest12Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient12);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);

        // set connections
        ClientHelper connectedClient1 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient1);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity connectedWithdrawal11 = generateCrmTbWithdrawalEntityByClient(connectedClient1);
        CrmTbWithdrawalEntity connectedWithdrawal21 = generateCrmTbWithdrawalEntityByClient(connectedClient2);
        data.crmTbWithdrawalObjects =
                (List.of(withdrawal1, withdrawal2, withdrawal3, connectedWithdrawal11, connectedWithdrawal21));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(100.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity connectedDeposit11 = generateCrmTbDepositEntityByClient(connectedClient1);
        CrmTbDepositEntity connectedDeposit21 = generateCrmTbDepositEntityByClient(connectedClient2);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, connectedDeposit11, connectedDeposit21);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(101.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        return data;
    }

    private static DataHelper getEnoughTradesTest13Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient13);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);

        // set connections
        ClientHelper connectedClient1 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient1);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity connectedWithdrawal11 = generateCrmTbWithdrawalEntityByClient(connectedClient1);
        CrmTbWithdrawalEntity connectedWithdrawal21 = generateCrmTbWithdrawalEntityByClient(connectedClient2);
        data.crmTbWithdrawalObjects =
                (List.of(withdrawal1, withdrawal2, withdrawal3, connectedWithdrawal11, connectedWithdrawal21));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(100.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity connectedDeposit11 = generateCrmTbDepositEntityByClient(connectedClient1);
        CrmTbDepositEntity connectedDeposit21 = generateCrmTbDepositEntityByClient(connectedClient2);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, connectedDeposit11, connectedDeposit21);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(101.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(6.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest14Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient14);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);

        // set connections
        ClientHelper connectedClient1 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient1);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity connectedWithdrawal11 = generateCrmTbWithdrawalEntityByClient(connectedClient1);
        CrmTbWithdrawalEntity connectedWithdrawal21 = generateCrmTbWithdrawalEntityByClient(connectedClient2);
        data.crmTbWithdrawalObjects =
                (List.of(withdrawal1, withdrawal2, withdrawal3, connectedWithdrawal11, connectedWithdrawal21));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(100.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity connectedDeposit11 = generateCrmTbDepositEntityByClient(connectedClient1);
        CrmTbDepositEntity connectedDeposit21 = generateCrmTbDepositEntityByClient(connectedClient2);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, connectedDeposit11, connectedDeposit21);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(101.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(7.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest15Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient15);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);
        data.crmWithdrawalEventV2.setPaymentMethodCode("CRYPTO");

        // set connections
        ClientHelper connectedClient1 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient1);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity connectedWithdrawal11 = generateCrmTbWithdrawalEntityByClient(connectedClient1);
        CrmTbWithdrawalEntity connectedWithdrawal21 = generateCrmTbWithdrawalEntityByClient(connectedClient2);
        data.crmTbWithdrawalObjects =
                (List.of(withdrawal1, withdrawal2, withdrawal3, connectedWithdrawal11, connectedWithdrawal21));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "CRYPTO";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(100.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity connectedDeposit11 = generateCrmTbDepositEntityByClient(connectedClient1);
        CrmTbDepositEntity connectedDeposit21 = generateCrmTbDepositEntityByClient(connectedClient2);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, connectedDeposit11, connectedDeposit21);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(101.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("CRYPTO")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(2565.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest16Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient16);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1000.0);
        data.crmWithdrawalEventV2.setPaymentMethodCode("CRYPTO");

        // set connections
        ClientHelper connectedClient1 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient1);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity connectedWithdrawal11 = generateCrmTbWithdrawalEntityByClient(connectedClient1);
        CrmTbWithdrawalEntity connectedWithdrawal21 = generateCrmTbWithdrawalEntityByClient(connectedClient2);
        data.crmTbWithdrawalObjects =
                (List.of(withdrawal1, withdrawal2, withdrawal3, connectedWithdrawal11, connectedWithdrawal21));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "CRYPTO";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(100.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity connectedDeposit11 = generateCrmTbDepositEntityByClient(connectedClient1);
        CrmTbDepositEntity connectedDeposit21 = generateCrmTbDepositEntityByClient(connectedClient2);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, connectedDeposit11, connectedDeposit21);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(101.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("CRYPTO")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(2564.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest17Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient17);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);

        // set connections
        ClientHelper connectedClient1 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient1);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(connectedClient1);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(connectedClient2);
        withdrawal1.setAmountUsd(BigDecimal.valueOf(0.31));
        withdrawal2.setAmountUsd(BigDecimal.valueOf(250.0));
        withdrawal3.setAmountUsd(BigDecimal.valueOf(250.0));
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "INTERNATIONAL_WIRE_TRANSFER";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit1.setAmountUsd(BigDecimal.valueOf(0.31));
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(connectedClient1);
        deposit2.setAmountUsd(BigDecimal.valueOf(250.0));
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(connectedClient2);
        deposit3.setAmountUsd(BigDecimal.valueOf(250.0));
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));

        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(.01);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(0.01);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(2.5);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest18Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient18);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(10.0);

        // set connections
        ClientHelper connectedClient1 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection1 = getConnection(data.clientHelper, connectedClient1);
        connection1.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "name+dateofbirth";
        connectionInfo1.connectionAttributeValue = "test";
        connectionInfo1.sourceAttributeValue = "test";
        connectionInfo1.relationType = "exact";
        connection1.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo1));
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        ConnectionTableEntry connection2 = getConnection(data.clientHelper, connectedClient2);
        connection2.connectionScore = 0.8;
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "document";
        connectionInfo2.connectionAttributeValue = "test";
        connectionInfo2.sourceAttributeValue = "test";
        connectionInfo2.relationType = "exact";
        connection2.connectionInfo =
                ConnectionTableEntry.ConnectionInfo.connectionInfoToString(List.of(connectionInfo2));
        data.connections = List.of(connection1, connection2);
        CrmTbUserObject connectedUserCrmTbUserObject1 = generateUserByClient(connectedClient1);
        CrmTbUserObject connectedUserCrmTbUserObject2 = generateUserByClient(connectedClient2);
        data.connectedUsers = List.of(connectedUserCrmTbUserObject1, connectedUserCrmTbUserObject2);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(connectedClient1);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(connectedClient2);
        withdrawal1.setAmountUsd(BigDecimal.valueOf(0.31));
        withdrawal2.setAmountUsd(BigDecimal.valueOf(250.0));
        withdrawal3.setAmountUsd(BigDecimal.valueOf(250.0));
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "INTERNATIONAL_WIRE_TRANSFER";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        deposit1.setAmountUsd(BigDecimal.valueOf(0.31));
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(connectedClient1);
        deposit2.setAmountUsd(BigDecimal.valueOf(250.0));
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(connectedClient2);
        deposit3.setAmountUsd(BigDecimal.valueOf(250.0));
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));

        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(.01);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(0.01);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(0.31);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest19Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient19);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(176.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(176.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(2000.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest20Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient20);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal4 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3, withdrawal4));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(176.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit4 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, deposit4);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(176.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(3500.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest21Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient21);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);
        data.crmWithdrawalEventV2.setPaymentChannelCode("CREDIT_CARD");

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "CREDIT_CARD";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(2)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(176.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(176.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("CREDIT_CARD")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(2000.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest22Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient22);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);
        data.crmWithdrawalEventV2.setPaymentChannelCode("E_WALLET");

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "E_WALLET";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(3)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(176.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(176.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(3)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("E_WALLET")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(2000.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest23Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient23);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);
        data.crmWithdrawalEventV2.setPaymentChannelCode("E_WALLET");
        data.crmWithdrawalEventV2.setPaymentTypeName("Neteller");

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "E_WALLET";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(3)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(176.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(176.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(3)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("E_WALLET")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(2000.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest24Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient24);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal4 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3, withdrawal4));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(176.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit4 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, deposit4);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(176.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(3500.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    private static DataHelper getEnoughTradesTest25Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClient25);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1.0);

        // set withdrawals
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal4 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3, withdrawal4));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(176.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit4 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3, deposit4);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(176.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(200.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        // set RFR risk free revenue
        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(2250.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        return data;
    }

    // ==============================================delimiter

    private static DataHelper getEnoughTradesTestAlert1Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClientAlert1);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1000.0);
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(200.0)));

        // set deposits
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(250.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper); // set credits
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper); // set profit
        deal.setProfitUsd(1000.0);
        deal.setCommissionUsd(1000.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(1000.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        MtMt4TradesObject trade = generateMt4TradesObject(data.clientHelper);
        trade.setCloseTime(null);
        trade.setCloseTimeUtc(null);
        trade.setProfitUsd(1.0);
        trade.setCommissionUsd(1.0);
        trade.setStorageUsd(1.0);
        data.MtMt4TradesObjects = List.of(trade);

        return data;
    }

    private static DataHelper getEnoughTradesTestAlert2Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClientAlert2);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1000.0);
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "International cat";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(200.0)));
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(250.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("INTERNATIONAL_WIRE_TRANSFER")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper); // set credits
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper); // set profit
        deal.setProfitUsd(1000.0);
        deal.setCommissionUsd(1000.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(1000.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        MtMt4TradesObject trade = generateMt4TradesObject(data.clientHelper);
        trade.setCloseTime(null);
        trade.setCloseTimeUtc(null);
        trade.setProfitUsd(1.0);
        trade.setCommissionUsd(1.0);
        trade.setStorageUsd(1.0);
        data.MtMt4TradesObjects = List.of(trade);

        PaymentEventsObject paymentEventsObject1 = generatePaymentEventsObject(data.clientHelper);
        PaymentDetailsObject paymentDetailsObject1 =
                generatePaymentDetailsObject(paymentEventsObject1, data.clientHelper);
        PaymentRuleExecutionsObject paymentRuleExecutionsObject1 =
                generatePaymentRuleExecutionsObject(paymentEventsObject1);
        paymentRuleExecutionsObject1.setRuleEndId(112);
        paymentRuleExecutionsObject1.setRuleId(3);
        PaymentDecisionsObject paymentDecisionsObject1 = generatePaymentDecisionObject(paymentEventsObject1);
        paymentDecisionsObject1.setDecisionCode(1);
        paymentDecisionsObject1.setDecisionType("payment");

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE, paymentRuleExecutionsObject1);
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, List.of(paymentDecisionsObject1));

        return data;
    }

    private static DataHelper getEnoughTradesTestAlert3Data() {
        DataHelper data = getEnoughTradesRuleData(enoughTradesRuleClientAlert3);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1000.0);
        data.crmWithdrawalEventV2.setPaymentChannelCode("CRYPTO");
        CrmTbWithdrawalEntity withdrawal1 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal2 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        CrmTbWithdrawalEntity withdrawal3 = generateCrmTbWithdrawalEntityByClient(data.clientHelper);
        data.crmTbWithdrawalObjects = (List.of(withdrawal1, withdrawal2, withdrawal3));
        int wdTypeId = getRandomBytePositive();
        int sourceId = getRandomBytePositive();
        int pcId = getRandomBytePositive();
        String catName = "CRYPTO";
        CrmTbWithdrawalTypeObject wdType = CrmTbWithdrawalTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .enName(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbWithdrawalTypeObjects = List.of(wdType);
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setSourceIdSt(sourceId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setPaymentTypeId(wdTypeId));
        data.crmTbWithdrawalObjects.forEach(wd -> wd.setAmountUsd(BigDecimal.valueOf(200.0)));
        CrmTbDepositEntity deposit1 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit2 = generateCrmTbDepositEntityByClient(data.clientHelper);
        CrmTbDepositEntity deposit3 = generateCrmTbDepositEntityByClient(data.clientHelper);
        data.crmTbDepositObjects = List.of(deposit1, deposit2, deposit3);
        data.crmTbDepositObjects.forEach(d -> d.setSourceIdSt(sourceId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentTypeId(wdTypeId));
        data.crmTbDepositObjects.forEach(d -> d.setPaymentChannelId(pcId));
        data.crmTbDepositObjects.forEach(d -> d.setAmountUsd(BigDecimal.valueOf(250.0)));
        CrmTbDepositTypeObject dType = CrmTbDepositTypeObject.builder()
                .id(wdTypeId)
                .sourceIdSt(sourceId)
                .category(1)
                .name(catName)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositTypeObjects = List.of(dType);
        CrmTbDepositChannelObject dChannel = CrmTbDepositChannelObject.builder()
                .id(pcId)
                .sourceIdSt(sourceId)
                .channelId(pcId)
                .typeId(wdTypeId)
                .name(catName)
                .isMobileChannel(0)
                .lastUpdated(getCurrentTimestampDbFormat())
                .build();
        data.crmTbDepositChannelObjects = List.of(dChannel);

        CostPaymentFee cpf = CostPaymentFee.builder()
                .id(getRandomIntPositive())
                .category("CRYPTO")
                .country(data.clientHelper.getCountry())
                .depositFeePrc(BigDecimal.valueOf(200))
                .withdrawalFeePrc(BigDecimal.valueOf(200))
                .lastUpdated(OffsetDateTime.now())
                .build();
        data.costPaymentFees = List.of(cpf);

        // set credits
        MtTbCreditsObject credit = generateCreditsByClient(data.clientHelper);
        credit.setAmountUsd(100.0);
        data.mtTbCreditsObjects = List.of(credit);

        // set profit
        Mt5DealsCoercedObject deal = generateMt5DealsCoercedObject(data.clientHelper);
        deal.setProfitUsd(1000.0);
        deal.setCommissionUsd(1000.0);
        data.mt5DealsCoercedObjects = List.of(deal);

        S3FactLoginMetricsObject loginMetrics = generateS3FactLoginMetricsClientZero(data.clientHelper);
        loginMetrics.setDailyTakerSpreadRevenueOz(1000.0);
        data.S3FactLoginMetricsObjects = List.of(loginMetrics);

        MtMt4TradesObject trade = generateMt4TradesObject(data.clientHelper);
        trade.setCloseTime(null);
        trade.setCloseTimeUtc(null);
        trade.setProfitUsd(1.0);
        trade.setCommissionUsd(1.0);
        trade.setStorageUsd(1.0);
        data.MtMt4TradesObjects = List.of(trade);

        PaymentEventsObject paymentEventsObject1 = generatePaymentEventsObject(data.clientHelper);
        PaymentDetailsObject paymentDetailsObject1 =
                generatePaymentDetailsObject(paymentEventsObject1, data.clientHelper);
        PaymentRuleExecutionsObject paymentRuleExecutionsObject1 =
                generatePaymentRuleExecutionsObject(paymentEventsObject1);
        paymentRuleExecutionsObject1.setRuleEndId(202);
        paymentRuleExecutionsObject1.setRuleId(3);
        paymentRuleExecutionsObject1.setDateCreated(
                Timestamp.valueOf(LocalDateTime.now().minusDays(5)));

        PaymentEventsObject paymentEventsObject2 = generatePaymentEventsObject(data.clientHelper);
        PaymentDetailsObject paymentDetailsObject2 =
                generatePaymentDetailsObject(paymentEventsObject2, data.clientHelper);
        PaymentRuleExecutionsObject paymentRuleExecutionsObject2 =
                generatePaymentRuleExecutionsObject(paymentEventsObject2);
        paymentRuleExecutionsObject2.setRuleEndId(205);
        paymentRuleExecutionsObject2.setRuleId(3);
        paymentRuleExecutionsObject2.setDateCreated(
                Timestamp.valueOf(LocalDateTime.now().minusDays(4)));
        PaymentDecisionsObject paymentDecisionsObject2 = generatePaymentDecisionObject(paymentEventsObject2);
        paymentDecisionsObject2.setDecisionCode(1);

        PaymentEventsObject paymentEventsObject3 = generatePaymentEventsObject(data.clientHelper);
        PaymentDetailsObject paymentDetailsObject3 =
                generatePaymentDetailsObject(paymentEventsObject3, data.clientHelper);
        PaymentRuleExecutionsObject paymentRuleExecutionsObject3 =
                generatePaymentRuleExecutionsObject(paymentEventsObject3);
        paymentRuleExecutionsObject3.setRuleEndId(102);
        paymentRuleExecutionsObject3.setRuleId(3);
        paymentRuleExecutionsObject3.setDateCreated(
                Timestamp.valueOf(LocalDateTime.now().minusDays(3)));

        PaymentEventsObject paymentEventsObject4 = generatePaymentEventsObject(data.clientHelper);
        PaymentDetailsObject paymentDetailsObject4 =
                generatePaymentDetailsObject(paymentEventsObject4, data.clientHelper);
        PaymentRuleExecutionsObject paymentRuleExecutionsObject4 =
                generatePaymentRuleExecutionsObject(paymentEventsObject4);
        paymentRuleExecutionsObject4.setRuleEndId(103);
        paymentRuleExecutionsObject4.setRuleId(3);
        paymentRuleExecutionsObject4.setDateCreated(
                Timestamp.valueOf(LocalDateTime.now().minusDays(2)));

        PaymentEventsObject paymentEventsObject5 = generatePaymentEventsObject(data.clientHelper);
        PaymentDetailsObject paymentDetailsObject5 =
                generatePaymentDetailsObject(paymentEventsObject5, data.clientHelper);
        PaymentRuleExecutionsObject paymentRuleExecutionsObject5 =
                generatePaymentRuleExecutionsObject(paymentEventsObject5);
        paymentRuleExecutionsObject5.setRuleEndId(108);
        paymentRuleExecutionsObject5.setRuleId(3);
        paymentRuleExecutionsObject5.setDateCreated(
                Timestamp.valueOf(LocalDateTime.now().minusDays(1)));

        insertObjectsToDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE,
                List.of(
                        paymentEventsObject1,
                        paymentEventsObject2,
                        paymentEventsObject3,
                        paymentEventsObject4,
                        paymentEventsObject5));
        insertObjectsToDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE,
                List.of(
                        paymentRuleExecutionsObject1,
                        paymentRuleExecutionsObject2,
                        paymentRuleExecutionsObject3,
                        paymentRuleExecutionsObject4,
                        paymentRuleExecutionsObject5));
        insertObjectsToDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE,
                List.of(
                        paymentDetailsObject1,
                        paymentDetailsObject2,
                        paymentDetailsObject3,
                        paymentDetailsObject4,
                        paymentDetailsObject5));

        return data;
    }

    public static Map<String, DataHelper> setupEnoughTradesRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getEnoughTradesTest1Data());
        map.put("2", getEnoughTradesTest2Data());
        map.put("3", getEnoughTradesTest3Data());
        map.put("31", getEnoughTradesTest31Data());
        map.put("4", getEnoughTradesTest4Data());
        map.put("41", getEnoughTradesTest41Data());
        map.put("5", getEnoughTradesTest5Data());
        map.put("6", getEnoughTradesTest6Data());
        map.put("7", getEnoughTradesTest7Data());
        map.put("8", getEnoughTradesTest8Data());
        map.put("9", getEnoughTradesTest9Data());
        map.put("10", getEnoughTradesTest10Data());
        map.put("11", getEnoughTradesTest11Data());
        map.put("12", getEnoughTradesTest12Data());
        map.put("13", getEnoughTradesTest13Data());
        map.put("14", getEnoughTradesTest14Data());
        map.put("15", getEnoughTradesTest15Data());
        map.put("16", getEnoughTradesTest16Data());
        map.put("17", getEnoughTradesTest17Data());
        map.put("18", getEnoughTradesTest18Data());
        map.put("19", getEnoughTradesTest19Data());
        map.put("20", getEnoughTradesTest20Data());
        map.put("21", getEnoughTradesTest21Data());
        map.put("22", getEnoughTradesTest22Data());
        map.put("23", getEnoughTradesTest23Data());
        map.put("24", getEnoughTradesTest24Data());
        map.put("25", getEnoughTradesTest25Data());
        map.put("770", getEnoughTradesTestAlert1Data());
        map.put("771", getEnoughTradesTestAlert2Data());
        map.put("772", getEnoughTradesTestAlert3Data());

        setupData(map);

        return map;
    }
}
