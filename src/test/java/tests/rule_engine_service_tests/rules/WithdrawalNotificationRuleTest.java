package tests.rule_engine_service_tests.rules;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;
import helpers.data.enums.Restriction;
import helpers.database.DbName;
import io.qameta.allure.*;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.api.mitigation_service.MitigationServiceRequest.postRestriction;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static helpers.database.CleanTableHelper.*;
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
    private static final ClientHelper client3 = getRandomVantageClientAllFields();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException {
        // Enable emulator to set restrictions to status APPLIED
        startSshTunnel();
        enableCRMEmulator();
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client1));
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client2));
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client3));
        Thread.sleep(1000);//pause for sync DB and services
        Response response1 = postRestriction(new PostRestrictionRequestBody(
                client1.getUcid(), Restriction.MANUAL_WITHDRAWAL_REVIEW.getCode(), Restriction.MANUAL_WITHDRAWAL_REVIEW.getType(), null, null, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        Logger ln = Logger.getAnonymousLogger();
        ln.info("responce body is " + response1.body().string());
        assertThat("Assert that restriction has been set successfully", response1.code(), equalTo(200));
        Response response2 = postRestriction(new PostRestrictionRequestBody(
                client2.getUcid(), Restriction.WITHDRAWALS.getCode(), Restriction.WITHDRAWALS.getType(), null, null, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        assertThat("Assert that restriction has been set successfully", response2.code(), equalTo(200));
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanCrmUserTableByClient(client1.getUcid(), client2.getUcid(), client3.getUcid());
        cleanUserRestrictionGeneral(client1.getUcid());
        cleanUserRestrictionGeneral(client2.getUcid());
        closeAlert(client1.getUcid());
        stopSshTunnel();
    }

    @Test
    @DisplayName("Withdrawal notification rule. Send alert for not empty check name")
    @AllureId("962")
    void withdrawalNotificationRule1Test() throws Exception {
        Allure.step("Produce withdrawal event to crm-events topic");
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client1.getBrand().toLowerCase(),    // brand
                "checkname",                         // checkName
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
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client1.getUcid()).toString(), RuleAlert[].class)).toList();
        assertThat("Verify amount of alerts in kafka", alerts.size(), greaterThan(0));

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "Withdrawal Review") && Objects.equals(alert.ucid, client1.getUcid())) {
                isAlertPresent = true;
                break;
            }
        }

        assertThat("Check that withdrawal notification alert is present", isAlertPresent, is(true));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, client1.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), greaterThan(0));
    }

    @Test
    @AllureId("963")
    @DisplayName("Withdrawal notification rule. Send alert for not empty check name")
    void withdrawalNotificationRule2Test() throws Exception {
        Allure.step("Produce withdrawal event to crm-events topic");
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client2.getBrand().toLowerCase(),    // brand
                "",                                  // checkName
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
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client2.getUcid()).toString(), RuleAlert[].class)).toList();
        assertThat("Verify amount of alerts in kafka", alerts.size(), is(0));

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "Withdrawal Review") && Objects.equals(alert.ucid, client2.getUcid())) {
                isAlertPresent = true;
                break;
            }
        }

        assertThat("Check that withdrawal notification alert is present", isAlertPresent, is(false));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, client2.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("963")
    @DisplayName("Withdrawal notification rule. Send alert for not empty check name")
    void withdrawalNotificationRule3Test() throws Exception {
        Allure.step("Produce withdrawal event to crm-events topic");
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client3.getBrand().toLowerCase(),    // brand
                null,                                // checkName
                client3.getUserId(),                 // clientId
                "2025-06-03T16:30:07+03:00",         // eventDate (you can format if you need +03:00)
                "4",                                 // expMonth
                "2030",                              // expYear
                "1",                                 // fullName
                getRandomUuidString(),               // id
                "VTSG" + client3.getTradingAccount() + "20250603144826", // merchantOrderId (example)
                client3.getTradingAccount(),         // mt4Account
                PAYMENT_PROVIDER_FASAPAY,            // paymentChannelCode
                "-",                                 // paymentChannelName
                "CREDIT_CARD",                       // paymentMethodCode
                "WEB",                               // platform
                client3.getRegulator(),              // regulator
                "1.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1,                                   // withdrawalAmount
                "2025-06-03T16:30:07",               // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client3.getUcid()).toString(), RuleAlert[].class)).toList();
        assertThat("Verify amount of alerts in kafka", alerts.size(), is(0));

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "Withdrawal Review") && Objects.equals(alert.ucid, client3.getUcid())) {
                isAlertPresent = true;
                break;
            }
        }

        assertThat("Check that withdrawal notification alert is present", isAlertPresent, is(false));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, client3.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }
}
