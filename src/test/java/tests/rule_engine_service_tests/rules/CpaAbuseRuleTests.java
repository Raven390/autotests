package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientsRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.enums.FraudType;
import helpers.data.rules.RuleDataHelper;
import helpers.database.DbName;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.disableCRMEmulator;
import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.cpa_abuse_rule.CpaAbuseRuleDataFactory.deleteCpaAbuseRuleData;
import static helpers.data.rules.cpa_abuse_rule.CpaAbuseRuleDataFactory.setupCpaAbuseRuleData;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CPA_ABUSE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class CpaAbuseRuleTests extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws Exception {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupCpaAbuseRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteCpaAbuseRuleData(dbDataMap);
        disableCRMEmulator();
    }

    @Test
    @DisplayName("CPA abuse rule exit Event_1. User don't have cpaId")
    @AllureId("916")
    void mirrorTradeRuleExitEventEnd1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("User do not have cpaId number");

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class);

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("CPA abuse rule exit Event_2. User have cpaId first deal  less than 60 d ago")
    @AllureId("916")
    void mirrorTradeRuleExitEvent2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("User do not have cpaId number");

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class);

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("CPA abuse rule exit Event_21. User connected to known abuser")
    @AllureId("917")
    void mirrorTradeRuleExitEventEnd2_1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("21");
        Allure.step("Produce withdrawal event to crm-events topic");
        data.withdrawalEvent.type = "egWithdrawal";
        System.out.println(data.withdrawalEvent.type);
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);


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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("CPA Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudType.CPA_ABUSE.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        //assertThat("Verify rule attributes clones not null", alert.rule.attributes.stepName, is("Linked abuser"));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.reason, is("One or many connected clients are CPA abusers"));
//        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
//        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
//        assertThat("Verify rule attributes clones not null", alert.rule.attributes.hedgingClone, notNullValue());


        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class);
        //assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("CPA abuse rule exit Event_22. Among connected clients for the same brand, if there are more than 3 clients, do at least 65% of them have the same CPA value as the initial client?")
    @AllureId("921")
    void mirrorTradeRuleExitEventEnd2_2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("22");
        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("CPA Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudType.CPA_ABUSE.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.stepName, is("At least 70% have any CPA value"));
//        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
//        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
//        assertThat("Verify rule attributes clones not null", alert.rule.attributes.hedgingClone, notNullValue());


        // Verify alert in BO db
        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class);
        //assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("CPA abuse rule exit Event 3. Allow withdrawal")
    @AllureId("929")
    void mirrorTradeRuleExitEventEnd3Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3");
        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());
    }

    @Test
    @DisplayName("CPA abuse rule exit Event 4_1. Score > 3. Connected to other account with same CPA - false")
    @AllureId("929")
    void mirrorTradeRuleExitEventEnd4_1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("4_1");
        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("CPA Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudType.CPA_ABUSE.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.stepName, is("Confirmed abuser"));
    }

    @Test
    @DisplayName("CPA abuse rule exit Event 4_2. Score > 3. Connected to other account with same CPA - true")
    @AllureId("929")
    void mirrorTradeRuleExitEventEnd4_2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("4_2");
        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("CPA Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudType.CPA_ABUSE.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.stepName, is("Confirmed abuser"));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

//        ClientsRestriction restriction = clientsRestrictions.getFirst();
//        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_set_manual_withdrawal_restriction_2", "APPLIED");
//
//        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("CPA abuse rule exit Event 4_3. Score < 3. Connected to other account with same CPA - true")
    @AllureId("929")
    void mirrorTradeRuleExitEventEnd4_3Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("4_3");
        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("CPA Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudType.CPA_ABUSE.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.stepName, is("Confirmed abuser"));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

//        ClientsRestriction restriction = clientsRestrictions.getFirst();
//        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_set_manual_withdrawal_restriction_2", "APPLIED");
//
//        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }
}
