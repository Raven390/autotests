package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.FraudType.*;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.FraudTypeStatus;
import io.qameta.allure.Description;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Utils;

public class ConnectionSearchPaymentAbuseDataFactory {
    private static final ClientHelper connectionSearchRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient9 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient9_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient10 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient10_1 = getRandomVantageClientAllFields();

    @Description("Create data for Connection search rule")
    private static DataHelper getConnectionSearchPaymentAbuseRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);

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
                .paymentMethodCode("CREDIT_CARD")
                .platform("WEB")
                .regulator(data.clientHelper.getRegulator())
                .schemaVersion("1.0")
                .type(CRM_WITHDRAWAL_EVENT)
                .withdrawalAmount(1.0)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(Long.valueOf(getRandomIntPositive()))
                .status("Risk audit")
                .withdrawalAmountUSD(100d)
                .build();

        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest1Data() {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient1);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest2Data() throws IOException, InterruptedException {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient2);
        DataHelper data2 = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient2_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest3Data() throws IOException, InterruptedException {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient3);
        DataHelper data2 = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient3_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, HEDGING);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest4Data() throws IOException, InterruptedException {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient4);
        DataHelper data2 = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient4_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.55);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.POTENTIAL, EXCHANGER);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest5Data() throws IOException, InterruptedException {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient5);
        DataHelper data2 = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient5_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, EXCHANGER);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest6Data() throws IOException, InterruptedException {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient6);
        DataHelper data2 = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient6_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.6);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, EXCHANGER);

        data.mt5DealsCoercedObjects = List.of(generateTradeByClient(data.clientHelper));
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(2001d);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(999));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(999));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");

        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest7Data() throws IOException, InterruptedException {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient7);
        DataHelper data2 = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient7_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.8);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.POTENTIAL, EXCHANGER);

        data.mt5DealsCoercedObjects = List.of(generateTradeByClient(data.clientHelper));
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(505d);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(999));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(999));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");

        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest8Data() throws IOException, InterruptedException {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient8);
        DataHelper data2 = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient8_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.6);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, EXCHANGER);

        data.mt5DealsCoercedObjects = List.of(generateTradeByClient(data.clientHelper));
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(100d);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(999));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(999));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest9Data() throws IOException, InterruptedException {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient9);
        DataHelper data2 = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient9_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.8);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.POTENTIAL, EXCHANGER);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        data.mt5DealsCoercedObjects = List.of(generateTradeByClient(data.clientHelper));
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(100d);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(999));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(999));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest10Data() throws IOException, InterruptedException {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient10);
        DataHelper data2 = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient10_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.6);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.POTENTIAL, EXCHANGER);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        // add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setSourceIdSt(1);
        data.crmTbDepositObjects.getFirst().setBrandUid(0);
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setStatusId(5);
        data.crmTbDepositObjects.getFirst().setStatus("Success");
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(1);
        data.crmTbDepositObjects.getFirst().setPaymentType("Credit Card");
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(1);
        data.crmTbDepositObjects.getFirst().setPaymentChannel("web");
        data.crmTbDepositObjects.getFirst().setPaymentSystemAccount("Credit card");

        // add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(1);
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(4);
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001d));
        data.crmTbWithdrawalObjects.getFirst().setStatusId(7);
        data.crmTbWithdrawalObjects.getFirst().setStatus("Complete");
        data.crmTbWithdrawalObjects.getFirst().setPaymentType("4");
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannel("web");
        return data;
    }

    public static Map<String, DataHelper> setupConnectionSearchPaymentRuleData()
            throws IOException, InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        map.put("1", getConnectionSearchPaymentAbuseTest1Data());
        map.put("2", getConnectionSearchPaymentAbuseTest2Data());
        map.put("3", getConnectionSearchPaymentAbuseTest3Data());
        map.put("4", getConnectionSearchPaymentAbuseTest4Data());
        map.put("5", getConnectionSearchPaymentAbuseTest5Data());
        map.put("6", getConnectionSearchPaymentAbuseTest6Data());
        map.put("7", getConnectionSearchPaymentAbuseTest7Data());
        map.put("8", getConnectionSearchPaymentAbuseTest8Data());
        map.put("9", getConnectionSearchPaymentAbuseTest9Data());
        map.put("10", getConnectionSearchPaymentAbuseTest10Data());

        setupData(map);

        return map;
    }
}
