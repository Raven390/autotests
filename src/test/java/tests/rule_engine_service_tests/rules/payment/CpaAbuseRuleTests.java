package tests.rule_engine_service_tests.rules.payment;

import business_objects.kafka.alerts.RuleAlert;
import helpers.data.DataHelper;
import helpers.data.enums.FraudTypeOld;

import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.payments.CpaAbuseRuleDataFactory.setupCpaAbuseRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CPA_ABUSE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class CpaAbuseRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    String ruleEventType = "Withdrawal";

    @BeforeAll
    static void setupData() throws Exception {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupCpaAbuseRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dbDataMap);
    }

    @Test
    @DisplayName("CPA abuse rule. User don't have cpaId. Element id: Event1")
    @AllureId("916")
    void cpaAbuseRuleExitEventEnd1Test() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event1", data.crmWithdrawalEvent.getId(), "clientCPAWithdrawal");
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event_2. User have cpaId first deal  less than 60 d ago")
    @AllureId("1125")
    void cpaAbuseRuleExitEvent2Test() throws Exception {
        DataHelper data = dbDataMap.get("2");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("User do not have cpaId number");

        Allure.step("Produce withdrawal event to crm-events topic");
        //TODO add event

        Allure.step("Check that there is no alerts on client");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Check that there is no restrictions on client");
        //List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
        //assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event_3. first deal more than 60 d ago,500 user same cpa")
    @AllureId("1126")
    void cpaAbuseRuleExitEvent3Test() throws Exception {
        DataHelper data = dbDataMap.get("3");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("User do not have cpaId number");

        Allure.step("Produce withdrawal event to crm-events topic");
        //TODO add event

        Allure.step("Check that there is no alerts on client");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Check that there is no restrictions on client");
//        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
//        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event_4. User connected to known abuser")
    @AllureId("917")
    void cpaAbuseRuleExitEventEnd4Test() throws Exception {
        DataHelper data = dbDataMap.get("4");
        Allure.step("Produce withdrawal event to crm-events topic");
        //TODO add event

        Allure.step("Verify alert in Kafka");

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 250);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);
        // Verify alert kafka
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("CPA Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo(ruleEventType));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.CPA_ABUSE.getKey()));
        assertThat("Verify rule fraud name is correct", alert.rule.name, notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, is("One or many connected clients are CPA abusers"));

        // Verify alert in BO db
        Allure.step("Verify client alert in BO DB");
//        List<Alert> dbAlerts = getObjectsFromDB(
//                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
//        );
//        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Verify client restrictions");
//        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
//        );
//        assertThat("Verify amount of restrictions", clientsRestrictionGenerals.size(), equalTo(1));
//        ClientsRestrictionGeneral expectedRestriction = new ClientsRestrictionGeneral(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, (Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong()), "CPA abuse", "APPLIED");
//        assertThat("Verify that the restriction is as expected", clientsRestrictionGenerals, containsInAnyOrder(expectedRestriction));
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event_5p1 profit>2.5k")
    @AllureId("1126")
    void cpaAbuseRuleExitEvent5p1Test() throws Exception {
        DataHelper data = dbDataMap.get("5");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("User do not have cpaId number");

        Allure.step("Produce withdrawal event to crm-events topic");
        //TODO add event

        Allure.step("Check that there is no alerts on client");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Check that there is no restrictions on client");
//        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
//        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event_5p2 profit<-700")
    @AllureId("1127")
    void cpaAbuseRuleExitEvent5p2Test() throws Exception {
        DataHelper data = dbDataMap.get("6");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("User do not have cpaId number");

        Allure.step("Produce withdrawal event to crm-events topic");
        //TODO add event

        Allure.step("Check that there is no alerts on client");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Check that there is no restrictions on client");
//        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
//        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event_6. Among connected clients for the same brand there are more than 3 clients, 99% of them have the same CPA value as the initial client")
    @AllureId("921")
    void cpaAbuseRuleExitEventEnd6Test() throws Exception {
        DataHelper data = dbDataMap.get("7");
        Allure.step("Produce withdrawal event to crm-events topic");
        //TODO add event

        Allure.step("Verify alert in Kafka");

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 250);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);
        // Verify alert kafka
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("CPA Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo(ruleEventType));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.CPA_ABUSE.getKey()));
        assertThat("Verify rule fraud name is correct", alert.rule.name, notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes creason is correct", alert.rule.attributes.reason, is("At least 65% of connected clients have CPA Id value"));

        // Verify alert in BO db
//        Allure.step("Verify client alert in BO DB");
//        List<Alert> dbAlerts = getObjectsFromDB(
//                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
//        );
//        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));
//
//        Allure.step("Verify client restrictions");
//        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
//        );
//        assertThat("Verify amount of restrictions", clientsRestrictionGenerals.size(), equalTo(1));
//        ClientsRestrictionGeneral expectedRestriction = new ClientsRestrictionGeneral(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, (Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong()), "CPA abuse", "APPLIED");
//        assertThat("Verify that the restriction is as expected", clientsRestrictionGenerals, containsInAnyOrder(expectedRestriction));
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event_7p1. First deposit are less than 60 days ago. Among connected clients for the same brand there are  3 clients, 100% of them have the same CPA value as the initial client")
    @AllureId("921")
    void cpaAbuseRuleExitEventEnd7p1Test() throws Exception {
        DataHelper data = dbDataMap.get("8");
        Allure.step("Produce withdrawal event to crm-events topic");
        //TODO add event


        Allure.step("Check that there is no alerts on client");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Check that there is no restrictions on client");
//        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
//        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event_7p2. First deposit are less than 60 days ago. Among connected clients for the same brand there are  4 clients, 50% of them have the same CPA value as the initial client")
    @AllureId("1128")
    void cpaAbuseRuleExitEventEnd7p2Test() throws Exception {
        DataHelper data = dbDataMap.get("9");
        Allure.step("Produce withdrawal event to crm-events topic");
        //TODO add event


        Allure.step("Check that there is no alerts on client");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Check that there is no restrictions on client");
//        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
//        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event 8p1. CPA score 0 not crypto,deposit more than 550, 1 trade, 0.1 lots no mirror, no hft  Allow payout")
    @AllureId("929")
    void cpaAbuseRuleExitEventEnd8Test() throws Exception {
        DataHelper data = dbDataMap.get("10");
        Allure.step("Produce withdrawal event to crm-events topic");
        //TODO add event


        Allure.step("Check that there is no alerts on client");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Check that there is no restrictions on client");
//        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
//        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event 8p2. CPA score 2  crypto,deposit less than 450,  8.1 lots, 1 mirror, 1 hft  Allow payout")
    @AllureId("1130")
    void cpaAbuseRuleExitEventEnd8p2Test() throws Exception {
        DataHelper data = dbDataMap.get("11");
        Allure.step("Produce withdrawal event to crm-events topic");
        //TODO add event


        Allure.step("Check that there is no alerts on client");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Check that there is no restrictions on client");
//        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class);
//        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event 9p1. Connected to clients with Same CPA, CPA score 3  not crypto,deposit 500,  4 lots, 0 mirror, 0 hft")
    @AllureId("929")
    void cpaAbuseRuleExitEventEnd9p1Test() throws Exception {
        DataHelper data = dbDataMap.get("12");
        Allure.step("Produce registration event to crm-events topic");
        //TODO add event

        Allure.step("Verify alert in Kafka");

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 250);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);
        // Verify alert kafka
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("CPA Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo(ruleEventType));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.CPA_ABUSE.getKey()));
        assertThat("Verify rule fraud name is correct", alert.rule.name, notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes creason is correct", alert.rule.attributes.reason, is("CPA abuse found by rule engine"));

        // Verify alert in BO db
        Allure.step("Verify client alert in BO DB");
//        List<Alert> dbAlerts = getObjectsFromDB(
//                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
//        );
//        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));
//
//        Allure.step("Verify client restrictions");
//        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
//        );
//        assertThat("Verify amount of restrictions", clientsRestrictionGenerals.size(), equalTo(1));
//        ClientsRestrictionGeneral expectedRestriction = new ClientsRestrictionGeneral(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, (Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong()), "CPA abuse", "APPLIED");
//        assertThat("Verify that the restriction is as expected", clientsRestrictionGenerals, containsInAnyOrder(expectedRestriction));
    }

    @Disabled
    @Test
    @DisplayName("CPA abuse rule exit Event 9p2. No connections with same CPA")
    @AllureId("1131")
    void cpaAbuseRuleExitEventEnd9p2Test() throws Exception {
        DataHelper data = dbDataMap.get("13");
        Allure.step("Produce registration event to crm-events topic");
        //TODO add event

        Allure.step("Verify alert in Kafka");

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 250);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);
        // Verify alert kafka
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("CPA Abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo(ruleEventType));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.CPA_ABUSE.getKey()));
        assertThat("Verify rule fraud name is correct", alert.rule.name, notNullValue());
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes creason is correct", alert.rule.attributes.reason, is("CPA abuse found by rule engine"));

        // Verify alert in BO db
        Allure.step("Verify client alert in BO DB");
//        List<Alert> dbAlerts = getObjectsFromDB(
//                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
//        );
//        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));
//
//        Allure.step("Verify client restrictions");
//        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
//        );
//        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }
}
