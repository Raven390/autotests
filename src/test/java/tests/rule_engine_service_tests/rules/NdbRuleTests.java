package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientsRestrictionGeneral;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.enums.Restriction;
import helpers.data.rules.RuleDataHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.disableCRMEmulator;
import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.enums.FraudType.*;
import static helpers.data.rules.ndb_rule.NdbRuleDataFactory.deleteNdbRuleData;
import static helpers.data.rules.ndb_rule.NdbRuleDataFactory.setupNdbRuleData;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

// alias clientNDBWithdrawal
@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_NDB_ABUSE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class NdbRuleTests extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws Exception {
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
        assertThat("Verify that there is no alerts", consumedMessages.size(), equalTo(0));

        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Test
    @DisplayName("NDB rule exit Event_2. User  have any ndb trades.count !<50")
    @AllureId("")
    void ndbRuleExitEventEnd2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Check number of alerts and restrictions");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is no alerts", consumedMessages.size(), equalTo(0));

        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Test
    @DisplayName("NDB rule exit Event_3.  trades.count <50, first trade more than 2 week old")
    @AllureId("")
    void ndbRuleExitEventEnd3Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Check number of alerts and restrictions");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is no alerts", consumedMessages.size(), equalTo(0));

        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("No Deposit Bonus Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(BONUS_ABUSE.getKey()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.reason, is("One or many connected clients are bonus abusers"));

        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictionGenerals.size(), equalTo(1));
        assertThat("Verify that restriction is Manual Withdrawal Review", clientsRestrictionGenerals.getFirst().restrictionId, equalTo(Restriction.MANUAL_WITHDRAWAL_REVIEW.getId().longValue()));
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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("No Deposit Bonus Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(BONUS_ABUSE.getKey()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.reason, is("One or many connected clients are bonus abusers"));

        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictionGenerals.size(), equalTo(1));
    }

    @Test
    @DisplayName("NDB rule exit Event_5. Linked fraud - other")
    @AllureId("947")
    void ndbRuleExitEventEnd5Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("5");
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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("No Deposit Bonus Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(BONUS_ABUSE.getKey()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.reason, is("One or many connected clients are non-bonus fraudsters"));
        assertThat("Verify rule attributes fraud type", alert.rule.attributes.fraudType, equalTo(data.clientFraudTypes.getFirst().getFraudTypeCode()));

        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));
    }

    @Test
    @DisplayName("NDB rule exit Event_6. Linked active accounts not with same email AND NDB from the last 1 week? = false")
    @AllureId("945")
    void ndbRuleExitEventEnd6Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("6");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Check number of alerts and restrictions");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(0));

        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Test
    @DisplayName("NDB rule exit Event_7. Any under the same IB? = true")
    @AllureId("951")
    void ndbRuleExitEventEnd7Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("7");
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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("No Deposit Bonus Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(BONUS_ABUSE.getKey()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.reason, is("Connected client with recent no deposit bonus (NDB) has same IB"));

        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );
        assertThat("Verify that there is only 1 restriction", clientsRestrictionGenerals.size(), equalTo(1));
    }

    @Test
    @DisplayName("NDB rule exit Event_8. Lexis registration score high? = false")
    @AllureId("960")
    void ndbRuleExitEventEnd8Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("8");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Check number of alerts and restrictions");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is no alerts", consumedMessages.size(), equalTo(0));

        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Test
    @DisplayName("NDB rule exit Event_9. Lexis registration score high? = true")
    @AllureId("961")
    void ndbRuleExitEventEnd9Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("9");
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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("No Deposit Bonus Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(BONUS_ABUSE.getKey()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.reason, is("High Lexis score"));

        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );
        assertThat("Verify that there is only 1 restriction", clientsRestrictionGenerals.size(), equalTo(1));
    }
}
