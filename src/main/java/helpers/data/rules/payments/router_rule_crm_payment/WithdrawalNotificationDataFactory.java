package helpers.data.rules.payments.router_rule_crm_payment;

import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import utils.Utils;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.CRM_WITHDRAWAL_EVENT;
import static utils.Constants.PAYMENT_PROVIDER_FASAPAY;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

public class WithdrawalNotificationDataFactory {
    private static final ClientHelper withdrawalNotificationRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient3 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalNotificationRuleClient4 = getRandomVantageClientAllFields();

    @Description("Create data for Withdrawal Notification rule")
    private static DataHelper getWithdrawalNotificationRuleData(ClientHelper client) {
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

    private static DataHelper getWithdrawalNotificationTest1Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient1);
        data.crmWithdrawalEvent.setCheckName("");
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest2Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient2);
        data.crmWithdrawalEvent.setCheckName(null);
        return data;
    }

    private static DataHelper getWithdrawalNotificationTest3Data() {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient3);
        data.crmWithdrawalEvent.setCheckName("Crypto_Risk");

        return data;
    }

    private static DataHelper getWithdrawalNotificationTest4Data() throws IOException {
        DataHelper data = getWithdrawalNotificationRuleData(withdrawalNotificationRuleClient4);
        data.crmWithdrawalEvent.setCheckName("");
        return data;
    }

    public static Map<String, DataHelper> setupWithdrawalNotificationRuleData() throws IOException {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getWithdrawalNotificationTest1Data());
        map.put("2", getWithdrawalNotificationTest2Data());
        map.put("3", getWithdrawalNotificationTest3Data());
        map.put("4", getWithdrawalNotificationTest4Data());

        setupData(map);

        return map;
    }
}
