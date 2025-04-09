package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientsRestriction;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.disableCRMEmulator;
import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.enums.FraudType.GAP_TRADING;
import static helpers.data.enums.FraudType.POTENTIAL_ABUSE;
import static helpers.data.rules.ndb_rule.NdbRuleDataFactory.deleteNdbRuleData;
import static helpers.data.rules.ndb_rule.NdbRuleDataFactory.setupNdbRuleData;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_NDB_ABUSE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class NdbRuleTests extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws ReflectiveOperationException, SQLException, IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupNdbRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteNdbRuleData(dbDataMap);
        disableCRMEmulator();
    }

    @Test
    @DisplayName("NDB rule exit Event_1. User don't have any ndb")
    @AllureId("945")
    void ndbRuleExitEventEnd1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Check number of alerts and restrictions");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(0));

        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class);
        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("NDB rule exit Event_2. Linked fraud - other")
    @AllureId("947")
    void ndbRuleExitEventEnd2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("No Deposit Bonuse Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(POTENTIAL_ABUSE.getKey()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.stepName, is("Linked unknown abuser"));
        assertThat("Verify rule attributes fraud type", alert.rule.attributes.fraudType, equalTo(GAP_TRADING.getKey()));

        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));
    }

    @Test
    @DisplayName("NDB rule exit Event_3. Linked active accounts not with same email AND NDB from the last 1 week? = false")
    @AllureId("945")
    void ndbRuleExitEventEnd3Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Check number of alerts and restrictions");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(0));

        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class);
        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("NDB rule exit Event_4_1. Linked fraud - LVA")
    @AllureId("948")
    void ndbRuleExitEventEnd4_1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("41");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("No Deposit Bonuse Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(POTENTIAL_ABUSE.getKey()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.stepName, is("Connected with same IB or fraudster"));

        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));
    }

    @Test
    @DisplayName("NDB rule exit Event_4_2. Linked fraud - Hedging")
    @AllureId("949")
    void ndbRuleExitEventEnd4_2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("42");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("No Deposit Bonuse Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(POTENTIAL_ABUSE.getKey()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.stepName, is("Connected with same IB or fraudster"));

        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));
    }

    @Test
    @DisplayName("NDB rule exit Event_4_3. Any under the same IB? = true")
    @AllureId("951")
    void ndbRuleExitEventEnd4_3Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("43");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("No Deposit Bonuse Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(POTENTIAL_ABUSE.getKey()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.stepName, is("Connected with same IB or fraudster"));

        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );
        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));
    }

    @Test
    @DisplayName("NDB rule exit Event_5. Lexis registration score high? = false")
    @AllureId("960")
    void ndbRuleExitEventEnd5Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("5");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Check number of alerts and restrictions");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(0));

        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class);
        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("NDB rule exit Event_6. Lexis registration score high? = true")
    @AllureId("961")
    void ndbRuleExitEventEnd6Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("6");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("No Deposit Bonuse Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(POTENTIAL_ABUSE.getKey()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.stepName, is("High Lexis score"));

        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );
        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));
    }
}
