package tests.rule_engine_service_tests.rules;

import business_objects.api.mitigation_service.PostRestrictionRequestBody;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.crm_events.WithdrawalEvent;
import helpers.data.ClientHelper;
import helpers.data.enums.Restriction;
import helpers.database.DbName;
import io.qameta.allure.*;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.time.Instant;
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
        cleanUserRestriction(client1.getUcid());
        cleanUserRestriction(client2.getUcid());
        closeAlert(client1.getUcid());
        stopSshTunnel();
    }

    @Test
    @DisplayName("Withdrawal notification rule manual withdrawal review restriction")
    @AllureId("962")
    void withdrawalNotificationRule1Test() throws Exception {
        ClientHelper client = client1;

        Allure.step("Produce withdrawal event to crm-events topic");
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), client.getRegulator(), "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "egWithdrawal");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlert[].class)).toList();
        assertThat("Verify amount of alerts in kafka", alerts.size(), greaterThan(0));

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "Withdrawal Review") && Objects.equals(alert.ucid, client1.getUcid())) {
                isAlertPresent = true;
                break;
            }
        }

        assertThat("Check that withdrawal notification is present", isAlertPresent, is(true));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, client.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), greaterThan(0));
    }

    @Test
    @DisplayName("Withdrawal notification rule other restriction")
    @AllureId("963")
    void withdrawalNotificationRule2Test() throws Exception {
        ClientHelper client = client2;

        Allure.step("Produce withdrawal event to crm-events topic");
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), client.getRegulator(), "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "egWithdrawal");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlert[].class)).toList();

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "Withdrawal Review") && Objects.equals(alert.ucid, client1.getUcid())) {
                isAlertPresent = true;
                break;
            }
        }

        assertThat("Check that withdrawal notification is not present", isAlertPresent, is(false));
    }

    @Test
    @DisplayName("Withdrawal notification rule no restrictions")
    @AllureId("964")
    void withdrawalNotificationRule3Test() throws Exception {
        ClientHelper client = client3;

        Allure.step("Produce withdrawal event to crm-events topic");
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), client.getRegulator(), "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "egWithdrawal");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlert[].class)).toList();

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "Withdrawal Review") && Objects.equals(alert.ucid, client1.getUcid())) {
                isAlertPresent = true;
                break;
            }
        }

        assertThat("Check that withdrawal notification is not present", isAlertPresent, is(false));
    }
}
