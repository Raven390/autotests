package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.List;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_NOTIFICATION_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class WithdrawalNotificationRuleTest extends TestBaseRule {

    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException {
        // Enable emulator to set restrictions to status APPLIED
        startSshTunnel();
        enableCRMEmulator();
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client1));
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client2));
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanCrmUserTableByClient(client1.getUcid(), client2.getUcid());
        closeAlert(client2.getUcid());
        stopSshTunnel();
    }

    @Test
    @DisplayName("Withdrawal notification rule. Exit with empty check name")
    @AllureId("962")
    void withdrawalNotificationRule1Test() throws Exception {
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client1.getBrand().toLowerCase(),    // brand
                "",                                  // checkName
                client1.getUserId(),                 // clientId
                "2025-06-03T16:30:07+03:00",         // eventDate (you can format if you need +03:00)
                "4",                                 // expMonth
                "2030",                              // expYear
                "1",                                 // fullName
                getRandomUuidString(),               // id
                "VTSG" + client1.getTradingAccount() + "20250603144826", // merchantOrderId (example)
                client1.getTradingAccount(),         // mt4Account
                PAYMENT_PROVIDER_FASAPAY,            // paymentChannelCode
                "-",                                 // paymentChannelName
                "CREDIT_CARD",                       // paymentMethodCode
                "WEB",                               // platform
                client1.getRegulator(),              // regulator
                "1.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1,                                   // withdrawalAmount
                "2025-06-03T16:30:07",               // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );
        produceWithdrawalMessageToKafka(withdrawalEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(client1);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(client1);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("963")
    @DisplayName("Withdrawal notification rule. Exit with alert if withdrawal has not empty check name")
    void withdrawalNotificationRule2Test() throws Exception {
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client2.getBrand().toLowerCase(),    // brand
                "Checkname",                                  // checkName
                client2.getUserId(),                 // clientId
                "2025-06-03T16:30:07+03:00",         // eventDate (you can format if you need +03:00)
                "4",                                 // expMonth
                "2030",                              // expYear
                "1",                                 // fullName
                getRandomUuidString(),               // id
                "VTSG" + client2.getTradingAccount() + "20250603144826", // merchantOrderId (example)
                client2.getTradingAccount(),         // mt4Account
                PAYMENT_PROVIDER_FASAPAY,            // paymentChannelCode
                "-",                                 // paymentChannelName
                "CREDIT_CARD",                       // paymentMethodCode
                "WEB",                               // platform
                client2.getRegulator(),              // regulator
                "1.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1,                                   // withdrawalAmount
                "2025-06-03T16:30:07",               // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );
        produceWithdrawalMessageToKafka(withdrawalEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(client2);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert name", alerts.getFirst().rule.name, is("Withdrawal Review"));
        assertThat("Verify alert ucid", alerts.getFirst().ucid, is(client2.getUcid()));

        List<Alert> dbAlerts = getUserAlertsFromDb(client2);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
    }

}
