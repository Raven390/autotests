package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.data.enums.FraudType.*;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.FraudTypeStatus;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.Utils;

public class ConnectionSearchPaymentAbuseDataFactory {
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client2_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client3_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client4 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client5 = getRandomVantageClientAllFields();
    private static final ClientHelper client5_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client6 = getRandomVantageClientAllFields();
    private static final ClientHelper client6_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client7 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client8 = getRandomVantageClientAllFields();
    private static final ClientHelper client8_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client9 = getRandomVantageClientAllFields();
    private static final ClientHelper client9_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client10 = getRandomVantageClientAllFields();
    private static final ClientHelper client10_1 = getRandomVantageClientAllFields();

    @Description("Create data for Connection search rule")
    private static DataHelper getRuleData(ClientHelper client) {
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
                .paymentMethodCode("CREDIT_CARD")
                .platform("WEB")
                .regulator(data.clientHelper.getRegulator())
                .schemaVersion("1.0")
                .type(Event.CRM_WITHDRAWAL_EVENT.getName())
                .withdrawalAmount(1.0)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(Long.valueOf(getRandomIntPositive()))
                .status("Risk audit")
                .withdrawalAmountUSD(100d)
                .build();

        return data;
    }

    private static DataHelper getTest1Data() {
        DataHelper data = getRuleData(client1);
        data.createCreditCard();

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(10_001d, 4);
        return data;
    }

    private static DataHelper getTest2Data() throws IOException, InterruptedException {
        DataHelper data = getRuleData(client2);
        DataHelper data2 = getRuleData(client2_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(10_001d, 4);
        return data;
    }

    private static DataHelper getTest3Data() throws IOException, InterruptedException {
        DataHelper data = getRuleData(client3);
        DataHelper data2 = getRuleData(client3_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, HEDGING);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        // add deposit
        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(10_001d, 4);
        return data;
    }

    private static DataHelper getTest4Data() throws IOException, InterruptedException {
        DataHelper data = getRuleData(client4);
        DataHelper data2 = getRuleData(client4_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.55);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.POTENTIAL, EXCHANGER);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        // add deposit
        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(10_001d, 4);
        return data;
    }

    private static DataHelper getTest5Data() throws IOException, InterruptedException {
        DataHelper data = getRuleData(client5);
        DataHelper data2 = getRuleData(client5_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, EXCHANGER);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(10_001d, 4);
        return data;
    }

    private static DataHelper getTest6Data() throws IOException, InterruptedException {
        DataHelper data = getRuleData(client6);
        DataHelper data2 = getRuleData(client6_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.6);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, EXCHANGER);

        data.mt5DealsCoercedObjects = List.of(generateTradeByClient(data.clientHelper));
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(2001d);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(999d, 4);

        return data;
    }

    private static DataHelper getTest7Data() throws IOException, InterruptedException {
        DataHelper data = getRuleData(client7);
        DataHelper data2 = getRuleData(client7_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.8);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.POTENTIAL, EXCHANGER);

        data.mt5DealsCoercedObjects = List.of(generateTradeByClient(data.clientHelper));
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(505d);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(999d, 4);

        return data;
    }

    private static DataHelper getTest8Data() throws IOException, InterruptedException {
        DataHelper data = getRuleData(client8);
        DataHelper data2 = getRuleData(client8_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.6);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, EXCHANGER);

        data.mt5DealsCoercedObjects = List.of(generateTradeByClient(data.clientHelper));
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(100d);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(999d, 4);
        return data;
    }

    private static DataHelper getTest9Data() throws IOException, InterruptedException {
        DataHelper data = getRuleData(client9);
        DataHelper data2 = getRuleData(client9_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.8);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.POTENTIAL, EXCHANGER);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        data.mt5DealsCoercedObjects = List.of(generateTradeByClient(data.clientHelper));
        data.mt5DealsCoercedObjects.getFirst().setProfitUsd(100d);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(999d, 4);
        return data;
    }

    private static DataHelper getTest10Data() throws IOException, InterruptedException {
        DataHelper data = getRuleData(client10);
        DataHelper data2 = getRuleData(client10_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 0.6);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.POTENTIAL, EXCHANGER);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(10_001d, 4);
        return data;
    }

    public static Map<String, DataHelper> setupConnectionSearchPaymentRuleData()
            throws IOException, InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        map.put("1", getTest1Data());
        map.put("2", getTest2Data());
        map.put("3", getTest3Data());
        map.put("4", getTest4Data());
        map.put("5", getTest5Data());
        map.put("6", getTest6Data());
        map.put("7", getTest7Data());
        map.put("8", getTest8Data());
        map.put("9", getTest9Data());
        map.put("10", getTest10Data());
        return map;
    }
}
