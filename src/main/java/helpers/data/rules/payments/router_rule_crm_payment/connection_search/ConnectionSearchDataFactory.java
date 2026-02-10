package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Event;
import io.qameta.allure.Description;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

public class ConnectionSearchDataFactory {
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client4 = getRandomVantageClientAllFields();
    private static final ClientHelper client5 = getRandomVantageClientAllFields();
    private static final ClientHelper client6 = getRandomVantageClientAllFields();
    private static final ClientHelper client7 = getRandomVantageClientAllFields();
    private static final ClientHelper client7_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client8 = getRandomVantageClientAllFields();
    private static final ClientHelper client8_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client9 = getRandomVantageClientAllFields();
    private static final ClientHelper client9_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client10 = getRandomVantageClientAllFields();
    private static final ClientHelper client11 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_1 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_2 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_3 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_4 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_5 = getRandomVantageClientAllFields();
    private static final ClientHelper client11_6 = getRandomVantageClientAllFields();
    private static final ClientHelper client12 = getRandomVantageClientAllFields();

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

        data.crmWithdrawalEventV2.setLocalBankTransfer(CrmWithdrawalEventV2.LocalBankTransfer.builder()
                .accountNumber("Cashh")
                .build());
        return data;
    }

    private static DataHelper getTest2Data() {
        DataHelper data = getRuleData(client2);
        data.crmWithdrawalEventV2.setLocalBankTransfer(CrmWithdrawalEventV2.LocalBankTransfer.builder()
                .accountNumber("0")
                .build());
        return data;
    }

    private static DataHelper getTest3Data() {
        DataHelper data = getRuleData(client3);
        data.crmWithdrawalEventV2.setLocalBankTransfer(CrmWithdrawalEventV2.LocalBankTransfer.builder()
                .accountNumber("00")
                .build());
        return data;
    }

    private static DataHelper getTest4Data() {
        DataHelper data = getRuleData(client4);

        return data;
    }

    private static DataHelper getTest5Data() {
        DataHelper data = getRuleData(client5);

        return data;
    }

    private static DataHelper getTest6Data() {
        DataHelper data = getRuleData(client6);
        data.addDepositSumByCategory(501d);
        return data;
    }

    private static DataHelper getTest7Data() {
        DataHelper data = getRuleData(client7);
        DataHelper data2 = getRuleData(client7_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        return data;
    }

    private static DataHelper getTest8Data() {
        DataHelper data = getRuleData(client8);
        DataHelper data2 = getRuleData(client8_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);

        data.addDepositSumByCategory(501d);
        return data;
    }

    private static DataHelper getTest9Data() {
        DataHelper data = getRuleData(client9);
        DataHelper data2 = getRuleData(client9_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(9000d, 5);

        return data;
    }

    private static DataHelper getTest10Data() {
        DataHelper data = getRuleData(client10);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(9000d, 5);

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

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(50_001d, 4);

        return data;
    }

    private static DataHelper getTest12Data() {
        DataHelper data = getRuleData(client12);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        data.addDepositSumByCategory(501d);

        data.addWithdrawalSumByCategory(9000d, 4);

        return data;
    }

    public static Map<String, DataHelper> setupConnectionSearchRuleData() {
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
        map.put("12", getTest12Data());
        return map;
    }
}
