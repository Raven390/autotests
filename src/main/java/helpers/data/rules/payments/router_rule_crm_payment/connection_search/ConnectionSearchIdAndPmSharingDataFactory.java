package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import static helpers.data.ClientFactory.getRandomClientByBrandAndCountry;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Country;
import io.qameta.allure.Description;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

public class ConnectionSearchIdAndPmSharingDataFactory {
    private static final ClientHelper connectionSearchRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_6 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_7 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2_8 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_6 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_7 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_8 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_9 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_10 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_11 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_12 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_13 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_14 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_15 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_16 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_17 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_18 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_19 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_20 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6_2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_6 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8_2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8_3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8_4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8_5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8_6 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient9 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient9_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient9_2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient9_3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient9_4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient9_5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient9_6 = getRandomVantageClientAllFields();

    @Description("Create data for Connection search rule")
    private static DataHelper getConnectionSearchPmAndIdSharingRuleData(ClientHelper client) {
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
                .eWallet(CrmWithdrawalEventV2.EWallet.builder().build())
                .build();
        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest1Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient1);

        data.crmWithdrawalEventV2.getEWallet().setAccountName(data.clientHelper.getEmail());
        data.crmWithdrawalEventV2.getEWallet().setAccountNumber("test14@example.com");
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_EWALLET);

        // add deposit
        addDepositSumByCategory(data, 501D);

        // add CRYPTO withdrawal
        addWithdrawalSumByCategory(data, 9999D, 4);
        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest2Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2);
        DataHelper data2 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_1);
        DataHelper data3 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_2);
        DataHelper data4 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_3);
        DataHelper data5 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_4);
        DataHelper data6 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_5);
        DataHelper data7 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_6);
        DataHelper data8 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_7);
        DataHelper data9 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2_8);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        addConnectionByPayoutAndNameBirthAttribute(data, data2.clientHelper);
        addConnectionByPayoutAndNameBirthAttribute(data, data3.clientHelper);
        addConnectionByPayoutAndNameBirthAttribute(data, data4.clientHelper);
        addConnectionByPayoutAndNameBirthAttribute(data, data5.clientHelper);
        addConnectionByPayoutAndNameBirthAttribute(data, data6.clientHelper);
        addConnectionByPayoutAndNameBirthAttribute(data, data7.clientHelper);
        addConnectionByPayoutAndNameBirthAttribute(data, data8.clientHelper);
        addConnectionByPayoutAndNameBirthAttribute(data, data9.clientHelper);

        // add deposit
        addDepositSumByCategory(data, 501D);

        // add CRYPTO withdrawal
        addWithdrawalSumByCategory(data, 10_001D, 5);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest3Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient3);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        for (int i = 0; i < 7; i++) {
            ClientHelper connectedClient =
                    getRandomClientByBrandAndCountry(Brand.VANTAGE, Country.getCountryNameByCodeUppercase("CN"));
            addConnectionByPayoutIdAttribute(data, connectedClient);
        }
        addConnectionByPayoutIdAttribute(data, connectionSearchRuleClient3_1);

        // add deposit
        addDepositSumByCategory(data, 501D);

        // add CRYPTO withdrawal
        addWithdrawalSumByCategory(data, 9999D, 4);

        // add CRYPTO withdrawals (6 records) with identical parameters
        data.crmTbWithdrawalObjects = new java.util.ArrayList<>();
        for (int i = 0; i < 6; i++) {
            addWithdrawalSumByCategory(data, 9999D, 4);
        }

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest4Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient4);
        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        // add deposit
        addDepositSumByCategory(data, 501D);

        // add withdrawal
        addWithdrawalSumByCategory(data, 10_001D, 5);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest5Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5);
        DataHelper data2 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_1);
        DataHelper data3 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_2);
        DataHelper data4 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_3);
        DataHelper data5 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_4);
        DataHelper data6 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_5);
        DataHelper data7 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_6);
        DataHelper data8 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_7);
        DataHelper data9 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_8);
        DataHelper data10 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_9);
        DataHelper data11 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_10);
        DataHelper data12 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_11);
        DataHelper data13 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_12);
        DataHelper data14 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_13);
        DataHelper data15 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_14);
        DataHelper data16 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_15);
        DataHelper data17 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_16);
        DataHelper data18 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_17);
        DataHelper data19 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_18);
        DataHelper data20 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_19);
        DataHelper data21 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5_20);

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
        addDepositSumByCategory(data, 501D);

        // add CRYPTO withdrawal
        addWithdrawalSumByCategory(data, 10_001D, 5);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest6Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient6);
        DataHelper data2 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient6_1);
        DataHelper data3 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient6_2);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);

        // add deposit
        addDepositSumByCategory(data, 501D);

        // add withdrawal
        addWithdrawalSumByCategory(data, 10_001D, 5);
        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest7Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient7);
        DataHelper data2 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient7_1);
        DataHelper data3 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient7_2);
        DataHelper data4 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient7_3);
        DataHelper data5 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient7_4);
        DataHelper data6 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient7_5);
        DataHelper data7 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient7_6);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);
        addConnectionByPayoutIdAttribute(data, data4.clientHelper);
        addConnectionByPayoutIdAttribute(data, data5.clientHelper);
        addConnectionByPayoutIdAttribute(data, data6.clientHelper);
        addConnectionByPayoutIdAttribute(data, data7.clientHelper);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        // add deposit
        addDepositSumByCategory(data, 501D);

        // add CRYPTO withdrawal
        addWithdrawalSumByCategory(data, 10_001D, 4);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest8Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient8);
        DataHelper data2 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient8_1);
        DataHelper data3 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient8_2);
        DataHelper data4 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient8_3);
        DataHelper data5 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient8_4);
        DataHelper data6 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient8_5);
        DataHelper data7 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient8_6);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);
        addConnectionByPayoutIdAttribute(data, data4.clientHelper);
        addConnectionByPayoutIdAttribute(data, data5.clientHelper);
        addConnectionByPayoutIdAttribute(data, data6.clientHelper);
        addConnectionByPayoutIdAttribute(data, data7.clientHelper);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        // add deposit
        addDepositSumByCategory(data, 501D);

        // add CRYPTO withdrawals (6 records) with identical parameters
        addMultipleWithdrawalSumByCategory(data, 9999d, 4, 6);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest9Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient9);
        DataHelper data2 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient9_1);
        DataHelper data3 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient9_2);
        DataHelper data4 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient9_3);
        DataHelper data5 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient9_4);
        DataHelper data6 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient9_5);
        DataHelper data7 = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient9_6);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);
        addConnectionByPayoutIdAttribute(data, data4.clientHelper);
        addConnectionByPayoutIdAttribute(data, data5.clientHelper);
        addConnectionByPayoutIdAttribute(data, data6.clientHelper);
        addConnectionByPayoutIdAttribute(data, data7.clientHelper);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        // add deposit
        addDepositSumByCategory(data, 501D);

        // add CRYPTO withdrawal
        addWithdrawalSumByCategory(data, 9999D, 4);

        return data;
    }

    public static Map<String, DataHelper> setupConnectionSearchPmAndIdSharingRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getConnectionSearchPmAndIdSharingTest1Data());
        map.put("2", getConnectionSearchPmAndIdSharingTest2Data());
        map.put("3", getConnectionSearchPmAndIdSharingTest3Data());
        map.put("4", getConnectionSearchPmAndIdSharingTest4Data());
        map.put("5", getConnectionSearchPmAndIdSharingTest5Data());
        map.put("6", getConnectionSearchPmAndIdSharingTest6Data());
        map.put("7", getConnectionSearchPmAndIdSharingTest7Data());
        map.put("8", getConnectionSearchPmAndIdSharingTest8Data());
        map.put("9", getConnectionSearchPmAndIdSharingTest9Data());

        setupData(map);

        return map;
    }
}
