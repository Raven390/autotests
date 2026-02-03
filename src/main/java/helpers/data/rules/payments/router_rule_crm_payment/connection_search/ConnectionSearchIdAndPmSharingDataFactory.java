package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import static helpers.api.VerificationServiceHelper.putProfileStatus;
import static helpers.data.ClientFactory.getRandomClientByBrandAndCountry;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.data.enums.VerificationStatus.VERIFIED;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.DataSetupHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Country;
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
    private static final ClientHelper client3_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client3_2 = getRandomVantageClientAllFields();
    private static final ClientHelper client3_3 = getRandomVantageClientAllFields();
    private static final ClientHelper client4 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_2 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_3 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_4 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_5 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_6 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_7 = getRandomVantageClientAllFields();
    private static final ClientHelper client4_8 = getRandomVantageClientAllFields();
    private static final ClientHelper client5 = getRandomVantageClientAllFields();
    private static final ClientHelper client5_1 = getRandomVantageClientAllFields();
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

    private static final String accountNumberTest2 = Utils.getRandomUuidString();

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

    private static final String accountNumberTest3 = Utils.getRandomUuidString();

    private static DataHelper getTest3Data() throws IOException {
        DataHelper data = getRuleData(client3);
        DataHelper data2 = getRuleData(client3_2);
        DataHelper data3 = getRuleData(client3_3);

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
        DataSetupHelper.setupData(data2);

        data.crmTbDepositObjects.getFirst().setPaymentProfileKey(accountNumberTest3);
        data.crmTbDepositObjects.getFirst().setPaymentProfileMasked(accountNumberTest3);
        putProfileStatus(
                VERIFIED,
                data.crmTbDepositObjects.getFirst().getPaymentProfileKey(),
                data.crmTbDepositObjects.getFirst().getPaymentProfileMasked(),
                data.clientHelper);

        return data;
    }

    private static DataHelper getTest3_1Data() throws IOException {
        DataHelper data = getRuleData(client3_1);

        data.crmWithdrawalEventV2.getEWallet().setAccountName(data.clientHelper.getEmail());
        data.crmWithdrawalEventV2.getEWallet().setAccountNumber(accountNumberTest3);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        data.addDepositSumByCategory(501D);

        data.crmTbDepositObjects.getFirst().setPaymentProfileKey(accountNumberTest3);
        data.crmTbDepositObjects.getFirst().setPaymentProfileMasked(accountNumberTest3);
        putProfileStatus(
                VERIFIED,
                data.crmTbDepositObjects.getFirst().getPaymentProfileKey(),
                data.crmTbDepositObjects.getFirst().getPaymentProfileMasked(),
                data.clientHelper);

        return data;
    }

    private static DataHelper getTest4Data() {
        DataHelper data = getRuleData(client4);
        DataHelper data2 = getRuleData(client4_1);
        DataHelper data3 = getRuleData(client4_2);
        DataHelper data4 = getRuleData(client4_3);
        DataHelper data5 = getRuleData(client4_4);
        DataHelper data6 = getRuleData(client4_5);
        DataHelper data7 = getRuleData(client4_6);
        DataHelper data8 = getRuleData(client4_7);
        DataHelper data9 = getRuleData(client4_8);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);
        addConnectionByPayoutIdAttribute(data, data4.clientHelper);
        addConnectionByPayoutIdAttribute(data, data5.clientHelper);
        addConnectionByPayoutIdAttribute(data, data6.clientHelper);
        addConnectionByPayoutIdAttribute(data, data7.clientHelper);
        addConnectionByPayoutIdAttribute(data, data8.clientHelper);
        addConnectionByPayoutIdAttribute(data, data9.clientHelper);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawal
        data.addWithdrawalSumByCategory(10_001D, 5);

        return data;
    }

    private static DataHelper getTest5Data() {
        DataHelper data = getRuleData(client5);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        for (int i = 0; i < 7; i++) {
            ClientHelper connectedClient =
                    getRandomClientByBrandAndCountry(Brand.VANTAGE, Country.getCountryNameByCodeUppercase("CN"));
            addConnectionByPayoutAndNameBirthAttribute(data, connectedClient);
        }
        addConnectionByPayoutAndNameBirthAttribute(data, client5_1);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawal
        data.addWithdrawalSumByCategory(9999D, 4);

        // add CRYPTO withdrawals (6 records) with identical parameters
        data.crmTbWithdrawalObjects = new java.util.ArrayList<>();
        for (int i = 0; i < 6; i++) {
            data.addWithdrawalSumByCategory(9999D, 4);
        }

        return data;
    }

    private static DataHelper getTest6Data() {
        DataHelper data = getRuleData(client6);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add withdrawal
        data.addWithdrawalSumByCategory(10_001D, 5);

        return data;
    }

    private static DataHelper getTest7Data() {
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

        data.crmWithdrawalEventV2.getCrypto().setWalletAddress(Utils.getRandomUuidString());
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);
        addConnectionByPayoutIdAttribute(data, data4.clientHelper);
        addConnectionByPayoutIdAttribute(data, data5.clientHelper);
        addConnectionByPayoutIdAttribute(data, data6.clientHelper);
        addConnectionByPayoutIdAttribute(data, data7.clientHelper);
        addConnectionByPayoutIdAttribute(data, data8.clientHelper);
        addConnectionByPayoutIdAttribute(data, data9.clientHelper);
        addConnectionByPayoutIdAttribute(data, data10.clientHelper);
        addConnectionByPayoutIdAttribute(data, data11.clientHelper);
        addConnectionByPayoutIdAttribute(data, data12.clientHelper);
        addConnectionByPayoutIdAttribute(data, data13.clientHelper);
        addConnectionByPayoutIdAttribute(data, data14.clientHelper);
        addConnectionByPayoutIdAttribute(data, data15.clientHelper);
        addConnectionByPayoutIdAttribute(data, data16.clientHelper);
        addConnectionByPayoutIdAttribute(data, data17.clientHelper);
        addConnectionByPayoutIdAttribute(data, data18.clientHelper);
        addConnectionByPayoutIdAttribute(data, data19.clientHelper);
        addConnectionByPayoutIdAttribute(data, data20.clientHelper);
        addConnectionByPayoutIdAttribute(data, data21.clientHelper);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawal
        data.addWithdrawalSumByCategory(10_001D, 5);

        return data;
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

    private static DataHelper getTest9Data() {
        DataHelper data = getRuleData(client9);
        DataHelper data2 = getRuleData(client9_1);
        DataHelper data3 = getRuleData(client9_2);
        DataHelper data4 = getRuleData(client9_3);
        DataHelper data5 = getRuleData(client9_4);
        DataHelper data6 = getRuleData(client9_5);
        DataHelper data7 = getRuleData(client9_6);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);
        addConnectionByPayoutIdAttribute(data, data4.clientHelper);
        addConnectionByPayoutIdAttribute(data, data5.clientHelper);
        addConnectionByPayoutIdAttribute(data, data6.clientHelper);
        addConnectionByPayoutIdAttribute(data, data7.clientHelper);

        data.crmWithdrawalEventV2.getCrypto().setWalletAddress(Utils.getRandomUuidString());
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawal
        data.addWithdrawalSumByCategory(10_001D, 4);

        return data;
    }

    private static DataHelper getTest10Data() {
        DataHelper data = getRuleData(client10);
        DataHelper data2 = getRuleData(client10_1);
        DataHelper data3 = getRuleData(client10_2);
        DataHelper data4 = getRuleData(client10_3);
        DataHelper data5 = getRuleData(client10_4);
        DataHelper data6 = getRuleData(client10_5);
        DataHelper data7 = getRuleData(client10_6);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);
        addConnectionByPayoutIdAttribute(data, data4.clientHelper);
        addConnectionByPayoutIdAttribute(data, data5.clientHelper);
        addConnectionByPayoutIdAttribute(data, data6.clientHelper);
        addConnectionByPayoutIdAttribute(data, data7.clientHelper);

        data.crmWithdrawalEventV2.getCrypto().setWalletAddress(Utils.getRandomUuidString());
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawals (6 records) with identical parameters
        data.addMultipleWithdrawalSumByCategory(9999d, 4, 6);

        return data;
    }

    private static DataHelper getTest11Data() {
        DataHelper data = getRuleData(client11);
        DataHelper data2 = getRuleData(client11_1);
        DataHelper data3 = getRuleData(client11_2);
        DataHelper data4 = getRuleData(client11_3);
        DataHelper data5 = getRuleData(client11_4);
        DataHelper data6 = getRuleData(client11_5);
        DataHelper data7 = getRuleData(client11_6);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);
        addConnectionByPayoutIdAttribute(data, data4.clientHelper);
        addConnectionByPayoutIdAttribute(data, data5.clientHelper);
        addConnectionByPayoutIdAttribute(data, data6.clientHelper);
        addConnectionByPayoutIdAttribute(data, data7.clientHelper);

        data.crmWithdrawalEventV2.getCrypto().setWalletAddress(Utils.getRandomUuidString());
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        // add deposit
        data.addDepositSumByCategory(501D);

        // add CRYPTO withdrawal
        data.addWithdrawalSumByCategory(9999D, 4);

        return data;
    }

    public static Map<String, DataHelper> setupConnectionSearchPmAndIdSharingRuleData() throws IOException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getTest1Data());
        map.put("2", getTest2Data());
        map.put("2_1", getTest2_1Data());
        map.put("3", getTest3Data());
        map.put("3_1", getTest3_1Data());
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
