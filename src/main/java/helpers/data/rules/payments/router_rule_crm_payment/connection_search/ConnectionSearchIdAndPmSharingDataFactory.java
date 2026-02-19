package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import static helpers.api.VerificationServiceHelper.putProfileStatus;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.data.enums.VerificationStatus.VERIFIED;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

public class ConnectionSearchIdAndPmSharingDataFactory {
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client2_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client3_2 = getRandomVantageClientAllFields();
    private static final ClientHelper client3_3 = getRandomVantageClientAllFields();
    private static final ClientHelper client3_4 = getRandomVantageClientAllFields();
    private static final ClientHelper client4 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_2 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_3 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_4 = getRandomVantageClientAllFields();
    private static final ClientHelper client6 = getRandomVantageClientAllFields();
    private static final ClientHelper client7 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_2 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_3 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_4 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_5 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_6 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_7 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_8 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_9 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_10 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_11 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_12 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_13 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_14 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_15 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_16 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_17 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_18 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_19 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_20 = getRandomVantageClientAllFields();
    private static final ClientHelper client8 = getRandomVantageClientAllFields();
    private static final ClientHelper client8_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client8_2 = getRandomVantageClientAllFields();
    private static final ClientHelper client9 = getRandomVantageClientAllFields();
    private static final ClientHelper client9_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client9_2 = getRandomVantageClientAllFields();
    private static final ClientHelper client9_3 = getRandomVantageClientAllFields();
    private static final ClientHelper client9_4 = getRandomVantageClientAllFields();
    private static final ClientHelper client9_5 = getRandomVantageClientAllFields();
    private static final ClientHelper client9_6 = getRandomVantageClientAllFields();
    private static final ClientHelper client10 = getRandomVantageClientAllFields();
    private static final ClientHelper client10_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client10_2 = getRandomVantageClientAllFields();
    private static final ClientHelper client10_3 = getRandomVantageClientAllFields();
    private static final ClientHelper client10_4 = getRandomVantageClientAllFields();
    private static final ClientHelper client10_5 = getRandomVantageClientAllFields();
    private static final ClientHelper client10_6 = getRandomVantageClientAllFields();
    private static final ClientHelper client11 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_2 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_3 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_4 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_5 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_6 = getRandomVantageClientAllFields();
    private static final String accountNumberTest2 = Utils.getRandomUuidString();
    private static final String accountNumberTest3 = Utils.getRandomUuidString();

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
                .eWallet(CrmWithdrawalEventV2.EWallet.builder().build())
                .crypto(CrmWithdrawalEventV2.Crypto.builder().build())
                .build();
        return data;
    }

    private static DataHelper getTest1Data() {
        DataHelper data = getRuleData(client1);

        data.crmWithdrawalEventV2.getEWallet().setAccountName(data.clientHelper.getEmail());
        data.crmWithdrawalEventV2.getEWallet().setAccountNumber("test14@example.com");
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawal
        data.addWithdrawalSumByCategory(9999D, 4);
        return data;
    }

    private static DataHelper getTest2Data() throws IOException {
        DataHelper data = getRuleData(client2);

        data.crmWithdrawalEventV2.getEWallet().setAccountName(data.clientHelper.getEmail());
        data.crmWithdrawalEventV2.getEWallet().setAccountNumber(accountNumberTest2);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        // add deposit
        data.addDepositSumByCategory(501D);

        data.addWithdrawalSumByCategory(9999D, 4);

        data.crmTbDepositObjects.getFirst().setPaymentProfileKey(accountNumberTest2);
        data.crmTbDepositObjects.getFirst().setPaymentProfileMasked(accountNumberTest2);
        putProfileStatus(
                VERIFIED,
                data.crmTbDepositObjects.getFirst().getPaymentProfileKey(),
                data.crmTbDepositObjects.getFirst().getPaymentProfileMasked(),
                data.clientHelper);

        return data;
    }

    private static DataHelper getTest2_1Data() throws IOException {
        DataHelper data = getRuleData(client2_1);

        data.crmWithdrawalEventV2.getEWallet().setAccountName(data.clientHelper.getEmail());
        data.crmWithdrawalEventV2.getEWallet().setAccountNumber(accountNumberTest2);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        data.addDepositSumByCategory(501D);

        data.crmTbDepositObjects.getFirst().setPaymentProfileKey(accountNumberTest2);
        data.crmTbDepositObjects.getFirst().setPaymentProfileMasked(accountNumberTest2);
        putProfileStatus(
                VERIFIED,
                data.crmTbDepositObjects.getFirst().getPaymentProfileKey(),
                data.crmTbDepositObjects.getFirst().getPaymentProfileMasked(),
                data.clientHelper);

        return data;
    }

    private static Map<String, DataHelper> getTest3Data() throws IOException {
        DataHelper data = getRuleData(client3);
        DataHelper data2 = getRuleData(client3_2);
        DataHelper data3 = getRuleData(client3_3);
        DataHelper data4 = getRuleData(client3_4);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        setupAttrConnectionDocumentAttribute(data, data3.clientHelper);

        data.crmWithdrawalEventV2.getEWallet().setAccountName(data.clientHelper.getEmail());
        data.crmWithdrawalEventV2.getEWallet().setAccountNumber(accountNumberTest3);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        // add deposit and withdrawal
        data.addDepositSumByCategory(499d);

        data.addWithdrawalSumByCategory(9999D, 4);

        // add deposit and withdrawal for connection
        data2.addDepositSumByCategory(501D);

        data2.addWithdrawalSumByCategory(9999D, 4);

        data.crmTbDepositObjects.getFirst().setPaymentProfileKey(accountNumberTest3);
        data.crmTbDepositObjects.getFirst().setPaymentProfileMasked(accountNumberTest3);
        putProfileStatus(
                VERIFIED,
                data.crmTbDepositObjects.getFirst().getPaymentProfileKey(),
                data.crmTbDepositObjects.getFirst().getPaymentProfileMasked(),
                data.clientHelper);

        data4.createClient(data4.getClientHelper()).createWithdrawal();
        data4.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(accountNumberTest3);
        data4.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        data4.getCrmTbWithdrawalObjects().getFirst().setSourceIdSt(1);

        Map<String, DataHelper> test3Map = new HashMap<>();
        test3Map.put("3", data);
        test3Map.put("3_2", data2);
        test3Map.put("3_4", data4);
        return test3Map;
    }

    private static Map<String, DataHelper> getTest4Data() {
        String paymentProfileKey = getRandomUuidString();
        DataHelper data = getRuleData(client4);
        DataHelper data2 = getRuleData(client4_1);
        DataHelper data3 = getRuleData(client4_2);
        DataHelper data4 = getRuleData(client4_3);
        DataHelper data5 = getRuleData(client4_4);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);
        data.crmWithdrawalEventV2.setEWallet(CrmWithdrawalEventV2.EWallet.builder()
                .accountNumber(paymentProfileKey)
                .accountName(paymentProfileKey)
                .build());

        data2.createClient(data2.getClientHelper()).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        data2.getCrmTbWithdrawalObjects().getFirst().setSourceIdSt(1);

        data3.createClient(data3.getClientHelper()).createWithdrawal();
        data3.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);
        data3.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        data3.getCrmTbWithdrawalObjects().getFirst().setSourceIdSt(1);

        data4.createClient(data4.getClientHelper()).createWithdrawal();
        data4.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);
        data4.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        data4.getCrmTbWithdrawalObjects().getFirst().setSourceIdSt(1);

        data5.createClient(data5.getClientHelper()).createWithdrawal();
        data5.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);
        data5.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        data5.getCrmTbWithdrawalObjects().getFirst().setSourceIdSt(1);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawal
        data.addWithdrawalSumByCategory(10_001D, 5);

        Map<String, DataHelper> test4Map = new HashMap<>();
        test4Map.put("4", data);
        test4Map.put("4_1", data2);
        test4Map.put("4_2", data3);
        test4Map.put("4_3", data4);
        test4Map.put("4_4", data5);

        return test4Map;
    }

    private static Map<String, DataHelper> getTest5Data() {
        String paymentProfileKey = getRandomUuidString();
        DataHelper data = getRuleData(client4);
        DataHelper data2 = getRuleData(client4_1);
        DataHelper data3 = getRuleData(client4_2);
        DataHelper data4 = getRuleData(client4_3);
        DataHelper data5 = getRuleData(client4_4);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);
        data.crmWithdrawalEventV2.setEWallet(CrmWithdrawalEventV2.EWallet.builder()
                .accountNumber(paymentProfileKey)
                .accountName(paymentProfileKey)
                .build());

        data2.createClient(data2.getClientHelper()).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        data2.getCrmTbWithdrawalObjects().getFirst().setSourceIdSt(1);

        data3.createClient(data3.getClientHelper()).createWithdrawal();
        data3.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);
        data3.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        data3.getCrmTbWithdrawalObjects().getFirst().setSourceIdSt(1);

        data4.createClient(data4.getClientHelper()).createWithdrawal();
        data4.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);
        data4.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        data4.getCrmTbWithdrawalObjects().getFirst().setSourceIdSt(1);

        data5.createClient(data5.getClientHelper()).createWithdrawal();
        data5.getCrmTbWithdrawalObjects().getFirst().setPaymentDetails(paymentProfileKey);
        data5.getCrmTbWithdrawalObjects().getFirst().setStatus("7");
        data5.getCrmTbWithdrawalObjects().getFirst().setSourceIdSt(1);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawal
        data.addWithdrawalSumByCategory(10_001D, 5);

        Map<String, DataHelper> test5Map = new HashMap<>();
        test5Map.put("5", data);
        test5Map.put("5_1", data2);
        test5Map.put("5_2", data3);
        test5Map.put("5_3", data4);
        test5Map.put("5_4", data5);
        return test5Map;
    }

    private static DataHelper getTest6Data() {
        DataHelper data = getRuleData(client6);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);
        data.crmWithdrawalEventV2.setEWallet(
                CrmWithdrawalEventV2.EWallet.builder().accountNumber("1111").build());

        // add deposit
        data.addDepositSumByCategory(501D);

        // add withdrawal
        data.addWithdrawalSumByCategory(10_001D, 5);

        return data;
    }

    private static Map<String, DataHelper> getTest7Data() {
        String paymentProfileKey = getRandomUuidString();
        DataHelper data = getRuleData(client7);
        DataHelper data2 = getRuleData(client7_1);
        DataHelper data3 = getRuleData(client7_2);
        DataHelper data4 = getRuleData(client7_3);
        DataHelper data5 = getRuleData(client7_4);
        DataHelper data6 = getRuleData(client7_5);
        DataHelper data7 = getRuleData(client7_6);
        DataHelper data8 = getRuleData(client7_7);
        DataHelper data9 = getRuleData(client7_8);
        DataHelper data10 = getRuleData(client7_9);
        DataHelper data11 = getRuleData(client7_10);
        DataHelper data12 = getRuleData(client7_11);
        DataHelper data13 = getRuleData(client7_12);
        DataHelper data14 = getRuleData(client7_13);
        DataHelper data15 = getRuleData(client7_14);
        DataHelper data16 = getRuleData(client7_15);
        DataHelper data17 = getRuleData(client7_16);
        DataHelper data18 = getRuleData(client7_17);
        DataHelper data19 = getRuleData(client7_18);
        DataHelper data20 = getRuleData(client7_19);
        DataHelper data21 = getRuleData(client7_20);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);
        data.crmWithdrawalEventV2.setCrypto(CrmWithdrawalEventV2.Crypto.builder()
                .walletAddress(paymentProfileKey)
                .build());

        data2.createClient(data2.getClientHelper()).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data3.createClient(data3.getClientHelper()).createWithdrawal();
        data3.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data3.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data4.createClient(data4.getClientHelper()).createWithdrawal();
        data4.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data4.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data5.createClient(data5.getClientHelper()).createWithdrawal();
        data5.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data5.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data6.createClient(data6.getClientHelper()).createWithdrawal();
        data6.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data6.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data7.createClient(data7.getClientHelper()).createWithdrawal();
        data7.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data7.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data8.createClient(data8.getClientHelper()).createWithdrawal();
        data8.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data8.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data9.createClient(data9.getClientHelper()).createWithdrawal();
        data9.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data9.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data10.createClient(data10.getClientHelper()).createWithdrawal();
        data10.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data10.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data11.createClient(data11.getClientHelper()).createWithdrawal();
        data11.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data11.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data12.createClient(data12.getClientHelper()).createWithdrawal();
        data12.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data12.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data13.createClient(data13.getClientHelper()).createWithdrawal();
        data13.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data13.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data14.createClient(data14.getClientHelper()).createWithdrawal();
        data14.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data14.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data15.createClient(data15.getClientHelper()).createWithdrawal();
        data15.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data15.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data16.createClient(data16.getClientHelper()).createWithdrawal();
        data16.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data16.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data17.createClient(data17.getClientHelper()).createWithdrawal();
        data17.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data17.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data18.createClient(data18.getClientHelper()).createWithdrawal();
        data18.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data18.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data19.createClient(data19.getClientHelper()).createWithdrawal();
        data19.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data19.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data20.createClient(data20.getClientHelper()).createWithdrawal();
        data20.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data20.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data21.createClient(data21.getClientHelper()).createWithdrawal();
        data21.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data21.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawal
        data.addWithdrawalSumByCategory(10_001D, 5);

        Map<String, DataHelper> test7Map = new HashMap<>();
        test7Map.put("7", data);
        test7Map.put("7_1", data2);
        test7Map.put("7_2", data3);
        test7Map.put("7_3", data4);
        test7Map.put("7_4", data5);
        test7Map.put("7_5", data6);
        test7Map.put("7_6", data7);
        test7Map.put("7_7", data8);
        test7Map.put("7_8", data9);
        test7Map.put("7_9", data10);
        test7Map.put("7_10", data11);
        test7Map.put("7_11", data12);
        test7Map.put("7_12", data13);
        test7Map.put("7_13", data14);
        test7Map.put("7_14", data15);
        test7Map.put("7_15", data16);
        test7Map.put("7_16", data17);
        test7Map.put("7_17", data18);
        test7Map.put("7_18", data19);
        test7Map.put("7_19", data20);
        test7Map.put("7_20", data21);

        return test7Map;
    }

    private static DataHelper getTest8Data() {
        DataHelper data = getRuleData(client8);
        DataHelper data2 = getRuleData(client8_1);
        DataHelper data3 = getRuleData(client8_2);

        data.crmWithdrawalEventV2.getCrypto().setWalletAddress(Utils.getRandomUuidString());
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add withdrawal
        data.addWithdrawalSumByCategory(10_001D, 5);
        return data;
    }

    private static Map<String, DataHelper> getTest9Data() {
        String paymentProfileKey = getRandomUuidString();
        DataHelper data = getRuleData(client9);
        DataHelper data2 = getRuleData(client9_1);
        DataHelper data3 = getRuleData(client9_2);
        DataHelper data4 = getRuleData(client9_3);
        DataHelper data5 = getRuleData(client9_4);
        DataHelper data6 = getRuleData(client9_5);
        DataHelper data7 = getRuleData(client9_6);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);
        data.crmWithdrawalEventV2.setCrypto(CrmWithdrawalEventV2.Crypto.builder()
                .walletAddress(paymentProfileKey)
                .build());

        data2.createClient(data2.getClientHelper()).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data3.createClient(data3.getClientHelper()).createWithdrawal();
        data3.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data3.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data4.createClient(data4.getClientHelper()).createWithdrawal();
        data4.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data4.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data5.createClient(data5.getClientHelper()).createWithdrawal();
        data5.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data5.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data6.createClient(data6.getClientHelper()).createWithdrawal();
        data6.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data6.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data7.createClient(data7.getClientHelper()).createWithdrawal();
        data7.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data7.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data.crmWithdrawalEventV2.getCrypto().setWalletAddress(paymentProfileKey);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawal
        data.addWithdrawalSumByCategory(10_001D, 4);

        Map<String, DataHelper> test9Map = new HashMap<>();
        test9Map.put("9", data);
        test9Map.put("9_1", data2);
        test9Map.put("9_2", data3);
        test9Map.put("9_3", data4);
        test9Map.put("9_4", data5);
        test9Map.put("9_5", data6);
        test9Map.put("9_6", data7);

        return test9Map;
    }

    private static Map<String, DataHelper> getTest10Data() {
        String paymentProfileKey = getRandomUuidString();
        DataHelper data = getRuleData(client10);
        DataHelper data2 = getRuleData(client10_1);
        DataHelper data3 = getRuleData(client10_2);
        DataHelper data4 = getRuleData(client10_3);
        DataHelper data5 = getRuleData(client10_4);
        DataHelper data6 = getRuleData(client10_5);
        DataHelper data7 = getRuleData(client10_6);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);
        data.crmWithdrawalEventV2.setCrypto(CrmWithdrawalEventV2.Crypto.builder()
                .walletAddress(paymentProfileKey)
                .build());

        data2.createClient(data2.getClientHelper()).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data3.createClient(data3.getClientHelper()).createWithdrawal();
        data3.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data3.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data4.createClient(data4.getClientHelper()).createWithdrawal();
        data4.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data4.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data5.createClient(data5.getClientHelper()).createWithdrawal();
        data5.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data5.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data6.createClient(data6.getClientHelper()).createWithdrawal();
        data6.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data6.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data7.createClient(data7.getClientHelper()).createWithdrawal();
        data7.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data7.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawals (6 records) with identical parameters
        data.addMultipleWithdrawalSumByCategory(9999d, 4, 6);

        Map<String, DataHelper> test10Map = new HashMap<>();
        test10Map.put("10", data);
        test10Map.put("10_1", data2);
        test10Map.put("10_2", data3);
        test10Map.put("10_3", data4);
        test10Map.put("10_4", data5);
        test10Map.put("10_5", data6);
        test10Map.put("10_6", data7);

        return test10Map;
    }

    private static Map<String, DataHelper> getTest11Data() {
        String paymentProfileKey = getRandomUuidString();
        DataHelper data = getRuleData(client11);
        DataHelper data2 = getRuleData(client11_1);
        DataHelper data3 = getRuleData(client11_2);
        DataHelper data4 = getRuleData(client11_3);
        DataHelper data5 = getRuleData(client11_4);
        DataHelper data6 = getRuleData(client11_5);
        DataHelper data7 = getRuleData(client11_6);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);
        data.crmWithdrawalEventV2.setCrypto(CrmWithdrawalEventV2.Crypto.builder()
                .walletAddress(paymentProfileKey)
                .build());

        data2.createClient(data2.getClientHelper()).createWithdrawal();
        data2.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data2.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data3.createClient(data3.getClientHelper()).createWithdrawal();
        data3.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data3.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data4.createClient(data4.getClientHelper()).createWithdrawal();
        data4.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data4.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data5.createClient(data5.getClientHelper()).createWithdrawal();
        data5.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data5.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data6.createClient(data6.getClientHelper()).createWithdrawal();
        data6.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data6.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data7.createClient(data7.getClientHelper()).createWithdrawal();
        data7.getCrmTbWithdrawalObjects().getFirst().setCryptoWalletAddress(paymentProfileKey);
        data7.getCrmTbWithdrawalObjects().getFirst().setStatus("7");

        data.crmWithdrawalEventV2.getCrypto().setWalletAddress(paymentProfileKey);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawal
        data.addWithdrawalSumByCategory(9999D, 4);

        Map<String, DataHelper> test11Map = new HashMap<>();
        test11Map.put("11", data);
        test11Map.put("11_1", data2);
        test11Map.put("11_2", data3);
        test11Map.put("11_3", data4);
        test11Map.put("11_4", data5);
        test11Map.put("11_5", data6);
        test11Map.put("11_6", data7);

        return test11Map;
    }

    public static Map<String, DataHelper> setupConnectionSearchPmAndIdSharingRuleData() throws IOException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getTest1Data());
        map.put("2", getTest2Data());
        map.put("2_1", getTest2_1Data());
        map.putAll(getTest3Data());
        map.putAll(getTest4Data());
        map.putAll(getTest5Data());
        map.put("6", getTest6Data());
        map.putAll(getTest7Data());
        map.put("8", getTest8Data());
        map.putAll(getTest9Data());
        map.putAll(getTest10Data());
        map.putAll(getTest11Data());
        return map;
    }
}
