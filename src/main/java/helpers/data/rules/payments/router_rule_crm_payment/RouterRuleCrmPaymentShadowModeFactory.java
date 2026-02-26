package helpers.data.rules.payments.router_rule_crm_payment;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.addConnectionByEmailPhoneAttribute;
import static helpers.data.DataHelper.addFraudTypeToConnectedUser;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.FraudType.EXCHANGER;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.PAYMENT_METHOD_CODE_CRYPTO;
import static utils.Constants.PAYMENT_PROVIDER_FASAPAY;
import static utils.Utils.*;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.FraudTypeStatus;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

public class RouterRuleCrmPaymentShadowModeFactory {
    private static final ClientHelper testClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient8_1 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient9 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient10 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient10_1 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient11 = getRandomVantageClientAllFields();
    private static final ClientHelper testClient11_1 = getRandomVantageClientAllFields();

    @Description("Create data for Shadow mode Router rule on withdrawal event")
    private static DataHelper getTest(ClientHelper client) {
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
                .paymentChannelName("CRYPTO_CHANNEL")
                .paymentMethodCode("CRYPTO")
                .platform("MT4")
                .regulator(data.clientHelper.getRegulator())
                .schemaVersion("2.0")
                .type(Event.CRM_WITHDRAWAL_EVENT.getName())
                .withdrawalAmount(1.1)
                .withdrawalAmountUSD(1.2)
                .withdrawalApplicationTime(Instant.now().toString())
                .withdrawalCurrency("EUR")
                .withdrawalId(getRandomLongPositive())
                .status("Risk Audit")
                .build();
        return data;
    }

    private static DataHelper getTest1Data() {
        DataHelper data = getTest(testClient1);
        data.crmWithdrawalEventV2.setCheckName("Crypto_Risk");
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);
        return data;
    }

    private static DataHelper getTest2Data() {
        DataHelper data = getTest(testClient2);
        data.crmWithdrawalEventV2.setCheckName("Crypto_Risk");
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);
        return data;
    }

    private static DataHelper getTest3Data() {
        DataHelper data = getTest(testClient3);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);
        return data;
    }

    private static DataHelper getTest4Data() {
        DataHelper data = getTest(testClient4);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);
        return data;
    }

    private static DataHelper getTest5Data() {
        DataHelper data = getTest(testClient5);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);
        return data;
    }

    private static DataHelper getTest6Data() {
        DataHelper data = getTest(testClient6);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);
        return data;
    }

    private static DataHelper getTest7Data() {
        DataHelper data = getTest(testClient7);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);
        return data;
    }

    private static DataHelper getTest8Data() throws IOException, InterruptedException {
        DataHelper data = getTest(testClient8);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);

        String paymentProfileKey = getRandomUuidString();
        DataHelper data2 = getTest(testClient8_1);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);
        data.crmWithdrawalEventV2.setCrypto(CrmWithdrawalEventV2.Crypto.builder()
                .walletAddress(paymentProfileKey)
                .build());
        data.addDepositSumByCategory(501d);
        data.addWithdrawalSumByCategory(9000d, 5);
        data2.addWithdrawalSumByCategory(9000d, 5);
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, EXCHANGER);
        setupData(data2);

        data2.createClient(testClient8_1).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        return data;
    }

    private static DataHelper getTest9Data() {
        DataHelper data = getTest(testClient9);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);
        return data;
    }

    private static DataHelper getTest10Data() throws IOException, InterruptedException {
        DataHelper data = getTest(testClient10);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);

        String paymentProfileKey = getRandomUuidString();
        DataHelper data2 = getTest(testClient10_1);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);
        data.crmWithdrawalEventV2.setCrypto(CrmWithdrawalEventV2.Crypto.builder()
                .walletAddress(paymentProfileKey)
                .build());
        data.addDepositSumByCategory(501d);
        data.addWithdrawalSumByCategory(9000d, 5);
        data2.addWithdrawalSumByCategory(9000d, 5);
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, EXCHANGER);
        setupData(data2);

        data2.createClient(testClient10_1).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        return data;
    }

    private static DataHelper getTest11Data() throws IOException, InterruptedException {
        DataHelper data = getTest(testClient11);
        data.crmWithdrawalEventV2.setWithdrawalAmountUSD(1d);

        String paymentProfileKey = getRandomUuidString();
        DataHelper data2 = getTest(testClient11_1);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);
        data.crmWithdrawalEventV2.setCrypto(CrmWithdrawalEventV2.Crypto.builder()
                .walletAddress(paymentProfileKey)
                .build());
        data.addDepositSumByCategory(501d);
        data.addWithdrawalSumByCategory(9000d, 5);
        data2.addWithdrawalSumByCategory(9000d, 5);
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, EXCHANGER);
        setupData(data2);

        data2.createClient(testClient11_1).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        return data;
    }

    public static Map<String, DataHelper> setupRouterRuleShadowModeWithdrawalData()
            throws IOException, InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
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
        map.put("11", getTest11Data());
        return map;
    }
}
