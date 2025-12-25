package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

import business_objects.kafka.crm_events.CrmWithdrawalEventV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import utils.Utils;

public class ConnectionSearchDataFactory {
    private static final ClientHelper connectionSearchRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7_6 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8 = getRandomVantageClientAllFields();

    @Description("Create data for Connection search rule")
    private static DataHelper getConnectionSearchRuleData(ClientHelper client) {
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

    private static DataHelper getConnectionSearchTest1Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient1);

        return data;
    }

    private static DataHelper getConnectionSearchTest2Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient2);

        return data;
    }

    private static DataHelper getConnectionSearchTest3Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient3);
        addDepositSumByCategory(data, 501d);
        return data;
    }

    private static DataHelper getConnectionSearchTest4Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient4);
        DataHelper data2 = getConnectionSearchRuleData(connectionSearchRuleClient4_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        return data;
    }

    private static DataHelper getConnectionSearchTest5Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient5);
        DataHelper data2 = getConnectionSearchRuleData(connectionSearchRuleClient5_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);

        addDepositSumByCategory(data, 501d);
        return data;
    }

    private static DataHelper getConnectionSearchTest6Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient6);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CREDIT_CARD);

        addDepositSumByCategory(data, 501d);

        addWithdrawalSumByCategory(data, 9000d, 5);

        return data;
    }

    private static DataHelper getConnectionSearchTest7Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient7);
        DataHelper data2 = getConnectionSearchRuleData(connectionSearchRuleClient7_1);
        DataHelper data3 = getConnectionSearchRuleData(connectionSearchRuleClient7_2);
        DataHelper data4 = getConnectionSearchRuleData(connectionSearchRuleClient7_3);
        DataHelper data5 = getConnectionSearchRuleData(connectionSearchRuleClient7_4);
        DataHelper data6 = getConnectionSearchRuleData(connectionSearchRuleClient7_5);
        DataHelper data7 = getConnectionSearchRuleData(connectionSearchRuleClient7_6);

        addConnectionByPayoutIdAttribute(data, data2.clientHelper);
        addConnectionByPayoutIdAttribute(data, data3.clientHelper);
        addConnectionByPayoutIdAttribute(data, data4.clientHelper);
        addConnectionByPayoutIdAttribute(data, data5.clientHelper);
        addConnectionByPayoutIdAttribute(data, data6.clientHelper);
        addConnectionByPayoutIdAttribute(data, data7.clientHelper);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        addDepositSumByCategory(data, 501d);

        addWithdrawalSumByCategory(data, 50_001d, 4);

        return data;
    }

    private static DataHelper getConnectionSearchTest8Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient8);

        data.crmWithdrawalEventV2.setPaymentMethodCode(PAYMENT_METHOD_CODE_CRYPTO);

        addDepositSumByCategory(data, 501d);

        addWithdrawalSumByCategory(data, 9000d, 4);

        return data;
    }

    public static Map<String, DataHelper> setupConnectionSearchRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getConnectionSearchTest1Data());
        map.put("2", getConnectionSearchTest2Data());
        map.put("3", getConnectionSearchTest3Data());
        map.put("4", getConnectionSearchTest4Data());
        map.put("5", getConnectionSearchTest5Data());
        map.put("6", getConnectionSearchTest6Data());
        map.put("7", getConnectionSearchTest7Data());
        map.put("8", getConnectionSearchTest8Data());

        setupData(map);

        return map;
    }
}
