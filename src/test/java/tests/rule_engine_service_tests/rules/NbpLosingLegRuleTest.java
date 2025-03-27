package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientsRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.ClientHelper;
import helpers.data.rules.RuleDataHelper;
import helpers.database.DbName;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.*;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.nbp_losing_leg_rule.NbpLosingLegRuleDataFactory.deleteNbpLosingLegRuleData;
import static helpers.data.rules.nbp_losing_leg_rule.NbpLosingLegRuleDataFactory.setupNbpLosingLegRuleData;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_NPB_LOSING_LEG_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class NbpLosingLegRuleTest extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupNbpLosingLegRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteNbpLosingLegRuleData(dbDataMap);
    }

    @Test
    @DisplayName("NBP losing leg rule exit 1")
    @AllureId("974")
    void nbpLosingLegEnd1Test() throws Exception {
        Allure.step("Deals with stopouts < 50%");
        RuleDataHelper data = dbDataMap.get("1");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("NBP losing leg rule exit 2")
    @AllureId("975")
    void nbpLosingLegEnd2Test() throws Exception {
        Allure.step("Deals with stopouts < 50%");
        Allure.step("Credit/Deposit < 80%");
        RuleDataHelper data = dbDataMap.get("2");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("NBP losing leg rule exit 3v1")
    @AllureId("976")
    void nbpLosingLegEnd3v1Test() throws Exception {
        Allure.step("Deals with stopouts < 50%");
        Allure.step("Credit/Deposit >= 80%");
        Allure.step("No mirror clients");
        RuleDataHelper data = dbDataMap.get("3v1");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid()).toString(), RuleAlert[].class)).toList();
        assertThat("Verify amount of alerts in kafka", alerts.size(), greaterThan(0));

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "NBP Abuse Losing")) {
                // Verify alert
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, is("01"));
                assertThat("Verify rule trigger is correct", alert.rule.trigger, is(data.closeTradeEvent.type));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, is("NBP_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Confirmed NBP abuse losing"));
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
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "NBP Losing", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, hasItem(expectedRestriction));
    }

    @Test
    @DisplayName("NBP losing leg rule exit 3v2")
    @AllureId("977")
    void nbpLosingLegEnd3v2Test() throws Exception {
        Allure.step("Deals with stopouts < 50%");
        Allure.step("Credit/Deposit >= 80%");
        Allure.step("Mirror client without abnormal profit");
        RuleDataHelper data = dbDataMap.get("3v2");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<RuleAlert> alerts = Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid()).toString(), RuleAlert[].class)).toList();
        assertThat("Verify amount of alerts in kafka", alerts.size(), greaterThan(0));

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "NBP Abuse Losing")) {
                // Verify alert
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, is("01"));
                assertThat("Verify rule trigger is correct", alert.rule.trigger, is(data.closeTradeEvent.type));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, is("NBP_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Confirmed NBP abuse losing"));
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
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "NBP Losing", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, hasItem(expectedRestriction));
    }

    @Test
    @DisplayName("NBP losing leg rule exit 4")
    @AllureId("978")
    void nbpLosingLegEnd4Test() throws Exception {
        Allure.step("Deals with stopouts < 50%");
        Allure.step("Credit/Deposit >= 80%");
        Allure.step("Mirror client with abnormal profit");
        RuleDataHelper data = dbDataMap.get("4");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        ClientHelper mirrorClient = data.connectedClientHelpers.getFirst();
        Map<String, List<String>> alertsMap = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), mirrorClient.getUcid());

        assertThat("Verify initial client alert was found", alertsMap.get(data.clientHelper.getUcid()), notNullValue());
        assertThat("Verify mirror client alert was found", alertsMap.get(mirrorClient.getUcid()), notNullValue());

        List<RuleAlert> alerts = alertsMap.get(data.clientHelper.getUcid()).stream().map(json -> {
            try {
                return objectMapper.readValue(json, RuleAlert.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize RuleAlert", e);
            }
        }).toList();

        assertThat("Verify amount of alerts in kafka", alerts.size(), greaterThan(0));

        // Verify alert
        boolean isAlertPresent = false;
        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.name, "NBP Abuse Losing")) {
                // Verify alert
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, is("01"));
                assertThat("Verify rule trigger is correct", alert.rule.trigger, is(data.closeTradeEvent.type));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, is("NBP_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Confirmed NBP abuse losing"));
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
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "NBP Losing", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, hasItem(expectedRestriction));

        Allure.step("Get alerts");
        List<RuleAlert> mirrorAlerts = alertsMap.get(mirrorClient.getUcid()).stream().map(json -> {
            try {
                return objectMapper.readValue(json, RuleAlert.class);
            } catch (Exception e) {
                throw new RuntimeException("Failed to deserialize RuleAlert", e);
            }
        }).toList();

        assertThat("Verify amount of alerts in kafka", mirrorAlerts.size(), equalTo(1));
        RuleAlert alert = mirrorAlerts.getFirst();

        // Verify alert for mirror client
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, is(mirrorClient.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, is("01"));
        assertThat("Verify rule name not null", alert.rule.name, is("NBP Abuse Losing"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, is(data.closeTradeEvent.type));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, is("NBP_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, is("Confirmed NBP abuse winner [Losing Rule]"));

        List<Alert> dbAlertsMirror = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, mirrorClient.getUcid()), Alert.class
        );

        // Verify mirror client alert in BO db

        assertThat("Verify amount of alerts in BO DB", dbAlertsMirror.size(), equalTo(1));

        // Verify mirror client restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictionsMirror = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", mirrorClient.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify amount of restrictions", clientsRestrictionsMirror.size(), equalTo(1));

        ClientsRestriction restrictionMirror = clientsRestrictionsMirror.getFirst();
        ClientsRestriction expectedRestrictionMirror = new ClientsRestriction(mirrorClient.getUcid(), mirrorClient.getRegulator(), 8L, "NBP_set_restriction", "APPLIED");

        assertThat("Verify that the restriction is as expected", restrictionMirror, equalTo(expectedRestrictionMirror));
    }
}
