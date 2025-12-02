package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import utils.Utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.CRM_WITHDRAWAL_EVENT;
import static utils.Constants.PAYMENT_PROVIDER_FASAPAY;
import static utils.Utils.*;

public class ConnectionSearchIdAndPmSharingDataFactory {
    private static final ClientHelper connectionSearchRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient7 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient8 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient9 = getRandomVantageClientAllFields();

    @Description("Create data for Connection search rule")
    private static DataHelper getConnectionSearchPmAndIdSharingRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);

        data.crmWithdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                            // accountType
                Utils.getRandomIntPositive().toString(),      // binNumber
                data.clientHelper.getBrand().toLowerCase(),   // brand
                "",                                           // Name
                data.clientHelper.getUserId(),                // clientId
                Instant.now().toString(),                  // eventDate (you can format if you need +03:00)
                "4",                                          // expMonth
                "2030",                                       // expYear
                data.clientHelper.getFirstName(),             // fullName
                getRandomUuidString(),                        // id
                "VTSG" + getRandomIntPositive(),              // merchantOrderId (example)
                data.clientHelper.getTradingAccount(),        // mt4Account
                PAYMENT_PROVIDER_FASAPAY,            // paymentChannelCode
                "-",                                 // paymentChannelName
                "CREDIT_CARD",                       // paymentMethodCode
                "WEB",                               // platform
                data.clientHelper.getRegulator(),    // regulator
                "1.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1.1,                                   // withdrawalAmount
                1.1,                                     // withdrawalAmountUSD
                Instant.now().toString(),               // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomLongPositive()             // withdrawalId
        );
        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest1Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient1);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest2Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient2);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest3Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient3);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest4Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient4);
        data.crmWithdrawalEvent.setPaymentMethodCode("CREDIT_CARD");

        //add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(43);
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(44);
        data.crmTbDepositObjects.getFirst().setSourceIdSt(7);
        data.crmTbDepositObjects.getFirst().setBrandUid(3);
        data.crmTbDepositObjects.getFirst().setStatusId(5);

        //add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(43);
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannelId(4);
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(8);
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(8);
        data.crmTbWithdrawalObjects.getFirst().setBrandUid(3);
        data.crmTbWithdrawalObjects.getFirst().setStatusId(5);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest5Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient5);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest6Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient6);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest7Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient7);
        data.crmWithdrawalEvent.setPaymentMethodCode("CRYPTO");

        //add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(43);
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(44);
        data.crmTbDepositObjects.getFirst().setSourceIdSt(7);
        data.crmTbDepositObjects.getFirst().setBrandUid(3);
        data.crmTbDepositObjects.getFirst().setStatusId(5);

        //add CRYPTO withdrawal
        data.crmTbWithdrawalObjects = List.of(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
        data.crmTbWithdrawalObjects.getFirst().setAmountUsd(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setAmount(BigDecimal.valueOf(10_001));
        data.crmTbWithdrawalObjects.getFirst().setPaymentTypeId(43);
        data.crmTbWithdrawalObjects.getFirst().setPaymentChannelId(4);
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(8);
        data.crmTbWithdrawalObjects.getFirst().setBrandUid(3);
        data.crmTbWithdrawalObjects.getFirst().setStatusId(5);

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest8Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient8);

        data.crmWithdrawalEvent.setPaymentMethodCode("CRYPTO");

        //add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(100_501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(100_501));
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(43);
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(44);
        data.crmTbDepositObjects.getFirst().setSourceIdSt(7);
        data.crmTbDepositObjects.getFirst().setBrandUid(3);
        data.crmTbDepositObjects.getFirst().setStatusId(5);

        // add CRYPTO withdrawals (6 records) with identical parameters
        data.crmTbWithdrawalObjects = new java.util.ArrayList<>();
        for (int i = 0; i < 6; i++) {
            data.crmTbWithdrawalObjects.add(generateCrmTbWithdrawalEntityByClient(data.clientHelper));
            data.crmTbWithdrawalObjects.get(i).setAmountUsd(BigDecimal.valueOf(9999));
            data.crmTbWithdrawalObjects.get(i).setAmount(BigDecimal.valueOf(9999));
            data.crmTbWithdrawalObjects.get(i).setPaymentTypeId(43);
            data.crmTbWithdrawalObjects.get(i).setPaymentChannelId(4);
            data.crmTbWithdrawalObjects.get(i).setSourceIdSt(8);
            data.crmTbWithdrawalObjects.get(i).setBrandUid(3);
            data.crmTbWithdrawalObjects.get(i).setStatusId(5);
        }

        return data;
    }

    private static DataHelper getConnectionSearchPmAndIdSharingTest9Data() {
        DataHelper data = getConnectionSearchPmAndIdSharingRuleData(connectionSearchRuleClient9);

        return data;
    }

    public static Map<String, DataHelper> setupConnectionSearchPmAndIdSharingRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
//        map.put("1", getConnectionSearchPmAndIdSharingTest1Data());
//        map.put("2", getConnectionSearchPmAndIdSharingTest2Data());
//        map.put("3", getConnectionSearchPmAndIdSharingTest3Data());
        map.put("4", getConnectionSearchPmAndIdSharingTest4Data());
//        map.put("5", getConnectionSearchPmAndIdSharingTest5Data());
//        map.put("6", getConnectionSearchPmAndIdSharingTest6Data());
        map.put("7", getConnectionSearchPmAndIdSharingTest7Data());
        map.put("8", getConnectionSearchPmAndIdSharingTest8Data());
//        map.put("9", getConnectionSearchPmAndIdSharingTest9Data());

        setupData(map);

        return map;
    }
}
