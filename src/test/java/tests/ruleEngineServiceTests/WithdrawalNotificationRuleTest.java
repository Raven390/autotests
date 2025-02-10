package tests.ruleEngineServiceTests;

import businessObjects.api.mitigationService.PostRestrictionRequestBody;
import businessObjects.db.backofficeDb.alert.Alert;
import businessObjects.kafka.alerts.RuleAlert;
import businessObjects.kafka.crmEvents.WithdrawalEvent;
import helpers.data.ClientHelper;
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

import static businessObjects.api.mitigationService.MitigationServiceRequest.enableCRMEmulator;
import static businessObjects.api.mitigationService.MitigationServiceRequest.postRestriction;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static helpers.database.MitigationHelper.cleanUserRestriction;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_NOTIFICATION_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_SERVICE)
public class WithdrawalNotificationRuleTest extends TestBaseRule {

    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();


    @BeforeAll
    public static void setupDbData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        startSshTunnel();
        enableCRMEmulator();
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client1));
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client2));
        insertObjectToDb(CRM_USER_TABLE_NAME, generateUserByClient(client3));
        Response response1 = postRestriction(new PostRestrictionRequestBody(
                client1.getUcid(), "13", "GENERAL", null, null, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        assertThat("Assert that restriction has been set successfully", response1.code(), equalTo(200));
        Response response2 = postRestriction(new PostRestrictionRequestBody(
                client2.getUcid(), "04", "GENERAL", null, null, "Automation test", new PostRestrictionRequestBody.UpdatedBy("Auto", "Test")
        ));
        assertThat("Assert that restriction has been set successfully", response2.code(), equalTo(200));
    }

    @AfterAll
    public static void deleteDbData() throws Exception {
        cleanCrmUserTableByClient(client1.getUcid(), client2.getUcid(), client3.getUcid());
        cleanUserRestriction(client1.getUcid());
        cleanUserRestriction(client2.getUcid());
        closeAlert(client1.getUcid());
        stopSshTunnel();
    }

    @Test
    @DisplayName("Withdrawal notification rule manual withdrawal review restriction")
    @AllureId("962")
    public void withdrawalNotificationRule1Test() throws Exception {
        ClientHelper client = client1;

        Allure.step("Produce withdrawal event to crm-events topic");
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), client.getRegulator(), "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "withdrawal");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlert[].class)).toList();
        assertThat("Verify amount of alerts in kafka", alerts.size(), greaterThan(0));

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "Manual withdrawal restriction")) {
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
    public void withdrawalNotificationRule2Test() throws Exception {
        ClientHelper client = client2;

        Allure.step("Produce withdrawal event to crm-events topic");
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), client.getRegulator(), "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "withdrawal");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlert[].class)).toList();

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "Manual withdrawal restriction")) {
                isAlertPresent = true;
                break;
            }
        }

        assertThat("Check that withdrawal notification is not present", isAlertPresent, is(false));
    }

    @Test
    @DisplayName("Withdrawal notification rule no restrictions")
    @AllureId("964")
    public void withdrawalNotificationRule3Test() throws Exception {
        ClientHelper client = client3;

        Allure.step("Produce withdrawal event to crm-events topic");
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), client.getRegulator(), "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "withdrawal");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlert[].class)).toList();

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "Manual withdrawal restriction")) {
                isAlertPresent = true;
                break;
            }
        }

        assertThat("Check that withdrawal notification is not present", isAlertPresent, is(false));
    }
}
