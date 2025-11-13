package helpers.data.rules.payments.router_rule_crm_payment;

import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Description;
import utils.Utils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static business_objects.db.data_science.ucid_general_score.UcidGeneralScoreFactory.generateUcidGeneralScoreObject;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.DataHelper.createClient;
import static helpers.data.DataHelper.setupData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.CRM_WITHDRAWAL_EVENT;
import static utils.Constants.PAYMENT_PROVIDER_FASAPAY;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

public class WithdrawalIntegrityDataFactory {
    private static final ClientHelper withdrawalIntegrityRuleClient1 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalIntegrityRuleClient2 = getRandomVantageClientAllFields();
    private static final ClientHelper withdrawalIntegrityRuleClient3 = getRandomVantageClientAllFields();

    @Description("Create data for Withdrawal Integrity check rule")
    private static DataHelper getWithdrawalIntegrityCheckRuleData(ClientHelper client) {
        DataHelper data = new DataHelper();
        createClient(data, client);

        data.crmWithdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                            // accountType
                Utils.getRandomIntPositive().toString(),      // binNumber
                data.clientHelper.getBrand().toLowerCase(),   // brand
                "",                                           // checkName
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

    private static DataHelper getWithdrawalIntegrityCheckTest1Data() {
        DataHelper data = getWithdrawalIntegrityCheckRuleData(withdrawalIntegrityRuleClient1);
        data.crmWithdrawalEvent.setWithdrawalAmount(50_000);
        return data;
    }

    private static DataHelper getWithdrawalIntegrityCheckTest2Data() {
        DataHelper data = getWithdrawalIntegrityCheckRuleData(withdrawalIntegrityRuleClient2);
        data.crmWithdrawalEvent.setWithdrawalAmount(100);
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.91, 0.91);

        return data;
    }

    private static DataHelper getWithdrawalIntegrityCheckTest3Data() {
        DataHelper data = getWithdrawalIntegrityCheckRuleData(withdrawalIntegrityRuleClient3);
        data.crmWithdrawalEvent.setWithdrawalAmount(2000);
        data.ucidGeneralScore = generateUcidGeneralScoreObject(data.clientHelper, 0.8, 0.8);
        return data;
    }

    public static Map<String, DataHelper> setupWithdrawalIntegrityCheckRuleData() {
        startSshTunnel();
        Map<String, DataHelper> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1", getWithdrawalIntegrityCheckTest1Data());
        map.put("2", getWithdrawalIntegrityCheckTest2Data());
        map.put("3", getWithdrawalIntegrityCheckTest3Data());

        setupData(map);

        return map;
    }
}
