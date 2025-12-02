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
import static helpers.data.DataHelper.*;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.CRM_WITHDRAWAL_EVENT;
import static utils.Constants.PAYMENT_PROVIDER_FASAPAY;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

public class ConnectionSearchDataFactory {
    private static final ClientHelper connectionSearchRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4_1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient6 = getRandomVantageClientAllFields();

    @Description("Create data for Connection search rule")
    private static DataHelper getConnectionSearchRuleData(ClientHelper client) {
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
                "2.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1d,                                   // withdrawalAmount
                1d,                                   // withdrawalAmountUSD
                Instant.now().toString(),               // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive().longValue()              // withdrawalId
        );
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
        DataHelper data2 = getConnectionSearchRuleData(connectionSearchRuleClient3_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        return data;
    }

    private static DataHelper getConnectionSearchTest4Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient4);
        DataHelper data2 = getConnectionSearchRuleData(connectionSearchRuleClient4_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);

        //add deposit
        data.crmTbDepositObjects = List.of(generateCrmTbDepositEntityByClient(data2.clientHelper));
        data.crmTbDepositObjects.getFirst().setAmountUsd(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setAmount(BigDecimal.valueOf(501));
        data.crmTbDepositObjects.getFirst().setPaymentTypeId(43);
        data.crmTbDepositObjects.getFirst().setPaymentChannelId(44);
        data.crmTbDepositObjects.getFirst().setSourceIdSt(7);
        data.crmTbDepositObjects.getFirst().setBrandUid(3);
        data.crmTbDepositObjects.getFirst().setStatusId(5);

        return data;
    }

    private static DataHelper getConnectionSearchTest5Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient5);

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

    private static DataHelper getConnectionSearchTest6Data() {
        DataHelper data = getConnectionSearchRuleData(connectionSearchRuleClient6);

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
        data.crmTbWithdrawalObjects.getFirst().setSourceIdSt(8);
        data.crmTbWithdrawalObjects.getFirst().setBrandUid(3);
        data.crmTbWithdrawalObjects.getFirst().setStatusId(5);

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

        setupData(map);

        return map;
    }
}
