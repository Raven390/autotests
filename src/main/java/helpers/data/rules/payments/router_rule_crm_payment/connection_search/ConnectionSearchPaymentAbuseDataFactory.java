package helpers.data.rules.payments.router_rule_crm_payment.connection_search;

import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.FraudTypeStatus;
import io.qameta.allure.Description;
import utils.Utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalEntityFactory.generateCrmTbWithdrawalEntityByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.*;
import static helpers.data.enums.FraudType.EXCHANGER;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.CRM_WITHDRAWAL_EVENT;
import static utils.Constants.PAYMENT_PROVIDER_FASAPAY;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

public class ConnectionSearchPaymentAbuseDataFactory {
    private static final ClientHelper connectionSearchRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient4 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5 = getRandomVantageClientAllFields();
    private static final ClientHelper connectionSearchRuleClient5_1 = getRandomVantageClientAllFields();

    @Description("Create data for Connection search rule")
    private static DataHelper getConnectionSearchPaymentAbuseRuleData(ClientHelper client) {
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
                1,                                   // withdrawalAmount
                Instant.now().toString(),               // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );
        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest1Data() {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient1);

        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest2Data() {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient2);

        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest3Data() {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient3);

        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest4Data() {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient4);

        return data;
    }

    private static DataHelper getConnectionSearchPaymentAbuseTest5Data() throws IOException, InterruptedException {
        DataHelper data = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient5);
        DataHelper data2 = getConnectionSearchPaymentAbuseRuleData(connectionSearchRuleClient5_1);

        addConnectionByEmailPhoneAttribute(data, data2.clientHelper, 1d);
        addFraudTypeToConnectedUser(data, FraudTypeStatus.CONFIRMED, EXCHANGER);

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

    public static Map<String, DataHelper> setupConnectionSearchPaymentRuleData()
            throws IOException, InterruptedException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
//        map.put("1", getConnectionSearchPaymentAbuseTest1Data());
//        map.put("2", getConnectionSearchPaymentAbuseTest2Data());
//        map.put("3", getConnectionSearchPaymentAbuseTest3Data());
//        map.put("4", getConnectionSearchPaymentAbuseTest4Data());
        map.put("5", getConnectionSearchPaymentAbuseTest5Data());

        setupData(map);

        return map;
    }
}
