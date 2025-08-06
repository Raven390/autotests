package tests.rule_engine_service_tests.rules.old_rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.rules.RuleDataHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.*;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.nbp_winning_leg_rule.NbpWinningLegRuleDataFactory.deleteNbpWinningLegRuleData;
import static helpers.data.rules.nbp_winning_leg_rule.NbpWinningLegRuleDataFactory.setupNbpWinningLegRuleData;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Disabled("Temporarily disabling this test class because rue is in development")
@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_NPB_WINNING_LEG_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class NbpWinningLegRuleTest extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupNbpWinningLegRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteNbpWinningLegRuleData(dbDataMap);
    }

    @Test
    @DisplayName("NBP winning leg rule exit 1")
    @AllureId("983")
    void nbpWinningLegEnd1Test() throws Exception {
        Allure.step("Deposits amount < 200 USD");
        RuleDataHelper data = dbDataMap.get("1");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        List<String> reasons = clientGeneralRestrictions.stream().map(ClientGeneralRestriction::getComment).toList();
        assertThat("Verify there is no restriction sent from the current rule", reasons, not(hasItem("NBP_set_restriction")));
    }

    @Test
    @DisplayName("NBP winning leg rule exit 2")
    @AllureId("984")
    void nbpWinningLegEnd2Test() throws Exception {
        Allure.step("Deposits amount >= 200 USD");
        Allure.step("No abnormal profit");
        RuleDataHelper data = dbDataMap.get("2");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        List<String> reasons = clientGeneralRestrictions.stream().map(ClientGeneralRestriction::getComment).toList();
        assertThat("Verify there is no restriction sent from the current rule", reasons, not(hasItem("NBP_set_restriction")));
    }

    @Test
    @DisplayName("NBP winning leg rule exit 3")
    @AllureId("985")
    void nbpWinningLegEnd3Test() throws Exception {
        Allure.step("Deposits amount >= 200 USD");
        Allure.step("Abnormal profit");
        RuleDataHelper data = dbDataMap.get("3");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid()).toString(), RuleAlert[].class)).toList();
        assertThat("Verify amount of alerts in kafka", alerts.size(), greaterThan(0));

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "NBP Abuse")) {
                // Verify alert
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, is("01"));
                assertThat("Verify rule trigger is correct", alert.rule.trigger, is(data.withdrawalEvent.type));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, is("NBP_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Confirmed NBP abuse winner"));
                isAlertPresent = true;
                break;
            }
        }

        assertThat("Check that rule alert present", isAlertPresent, is(true));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), greaterThan(0));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "NBP_set_restriction", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientGeneralRestrictions, hasItem(expectedRestriction));
    }
}
