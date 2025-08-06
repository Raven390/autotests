package tests.rule_engine_service_tests.rules.old_rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.enums.Restriction;
import helpers.data.rules.RuleDataHelper;
import helpers.database.DbName;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.*;
import java.util.*;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.registration_rule.RegistrationRuleDataFactory.deleteRegistrationRuleData;
import static helpers.data.rules.registration_rule.RegistrationRuleDataFactory.setupRegistrationRuleData;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Disabled
@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_REGISTRATION_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class RegistrationRuleTest extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupRegistrationRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteRegistrationRuleData(dbDataMap);
    }

    @Test
    @DisplayName("Registration rule exit Event1")
    @AllureId("155")
    void registrationRuleExitEventEnd1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");

        Allure.step("No toxic accounts linked");
        Allure.step("LN score != high");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientGeneralRestrictions, empty());
    }

    @Test
    @DisplayName("Registration rule exit Event2")
    @AllureId("156")
    void registrationRuleExitEventEnd2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");
        System.out.println(data.clientHelper.getUcid());

        Allure.step("No toxic accounts linked");
        Allure.step("LN score == high");
        Allure.step("Set manual withdrawal restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("POTENTIAL_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(data.lnSessionParsedObject.getRiskRating()));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.reason, equalTo("High Lexis score"));
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(data.lnSessionParsedObject.getRiskRating()));
        assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo(data.lnSessionParsedObject.getPolicyScore().toString()));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_set_manual_withdrawal_restriction_2", "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event3p1")
    @AllureId("158")
    void registrationRuleExitEventEnd3p1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p1");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is CPA abuser = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked CPA abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));

    }

    @Test
    @DisplayName("Registration rule exit Event3p2")
    @AllureId("1135")
    void registrationRuleExitEventEnd3p2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p2");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is CPA abuser = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked CPA abuser"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);

        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));

        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event3p3")
    @AllureId("1136")
    void registrationRuleExitEventEnd3p3Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p3");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is Mirror Trader (Hedger) abuser = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked hedging abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));
    }

    @Test
    @DisplayName("Registration rule exit Event3p4")
    @AllureId("1137")
    void registrationRuleExitEventEnd3p4Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p4");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is Mirror Trader (Hedger) abuser = true");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked hedging abuser"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);

        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));

        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event3p5")
    @AllureId("159")
    void registrationRuleExitEventEnd3p5Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p5");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is  voucher abuser (Loss Voucher Abuse)  = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOSS_VOUCHER_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked loss voucher abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));
    }

    @Test
    @DisplayName("Registration rule exit Event3p6")
    @AllureId("160")
    void registrationRuleExitEventEnd3p6Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p6");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is  voucher abuser (Loss Voucher Abuse)  = true");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOSS_VOUCHER_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked loss voucher abuser"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify restrictions in Mitigation Service db

        assertThat("Verify that there is only restriction in Mitigation Service db", dbAlerts.size(), equalTo(1));
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit EventEnd3p7")
    @AllureId("161")
    void registrationRuleExitEventEnd3p7Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p7");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is  News Trader = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NEWS_ABUSER"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked news trader"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p8")
    @AllureId("162")
    void registrationRuleExitEventEnd3p8Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p8");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is News Trader = true");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NEWS_ABUSER"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked news trader"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify restrictions in Mitigation Service db
        assertThat("Verify that there is only restriction in Mitigation Service db", dbAlerts.size(), equalTo(1));
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p9")
    @AllureId("164")
    void registrationRuleExitEventEnd3p9Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p9");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is  Liquidity Scalper = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("TLS_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked TLS abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p10")
    @AllureId("165")
    void registrationRuleExitEventEnd3p10Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p10");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is Liquidity Scalper = true");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("TLS_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked TLS abuser"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify restrictions in Mitigation Service db
        assertThat("Verify that there is only restriction in Mitigation Service db", dbAlerts.size(), equalTo(1));
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p11")
    @AllureId("167")
    void registrationRuleExitEventEnd7Version7Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p11");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is SWAP abuser = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("SWAP_ARBITRAGE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked SWAP abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p12")
    @AllureId("169")
    void registrationRuleExitEventEnd3p12Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p12");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is SWAP abuser = true");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("SWAP_ARBITRAGE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked SWAP abuser"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify restrictions in Mitigation Service db
        assertThat("Verify that there is only restriction in Mitigation Service db", dbAlerts.size(), equalTo(1));
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p13")
    @AllureId("170")
    void registrationRuleExitEventEnd3p13Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p13");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is GAP abuser = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("GAP_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked GAP abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p14")
    @AllureId("171")
    void registrationRuleExitEventEnd3p14Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p14");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is SWAP abuser = true");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("GAP_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked GAP abuser"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify restrictions in Mitigation Service db
        assertThat("Verify that there is only restriction in Mitigation Service db", dbAlerts.size(), equalTo(1));
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p15")
    @AllureId("209")
    void registrationRuleExitEventEnd3p15Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p15");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is  latency abuser = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LATENCY_ARBITRAGE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked latency abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p16")
    @AllureId("172")
    void registrationRuleExitEventEnd3p16Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p16");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is  latency abuser = true");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LATENCY_ARBITRAGE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked latency abuser"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify restrictions in Mitigation Service db
        assertThat("Verify that there is only restriction in Mitigation Service db", dbAlerts.size(), equalTo(1));
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End3p17")
    @AllureId("484")
    void registrationRuleExitEventEnd3p17Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p17");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is  pricing error abuser = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("PRICING_ERROR_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked pricing error abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p18")
    @AllureId("689")
    void registrationRuleExitEventEnd3p18Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p18");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is  latency abuser = true");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("PRICING_ERROR_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked pricing error abuser"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify restrictions in Mitigation Service db
        assertThat("Verify that there is only restriction in Mitigation Service db", dbAlerts.size(), equalTo(1));
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p19")
    @AllureId("690")
    void registrationRuleExitEventEnd3p19Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p19");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is NBP abuser = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NBP_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked NBP abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p20")
    @AllureId("691")
    void registrationRuleExitEventEnd3p20Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p20");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is  latency abuser = true");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NBP_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked NBP abuser"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify restrictions in Mitigation Service db
        assertThat("Verify that there is only restriction in Mitigation Service db", dbAlerts.size(), equalTo(1));
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p21")
    @AllureId("692")
    void registrationRuleExitEventEnd3p21Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p21");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is HFT abuser = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HFT_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked HFT abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p22")
    @AllureId("693")
    void registrationRuleExitEventEnd3p22() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p22");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is HFT abuser = true");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HFT_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked HFT abuser"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify restrictions in Mitigation Service db
        assertThat("Verify that there is only restriction in Mitigation Service db", dbAlerts.size(), equalTo(1));
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p23")
    @AllureId("694")
    void registrationRuleExitEventEnd3p23Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p23");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is Loophole abuser = true");
        Allure.step("Connection score by attributes >=0.75");
        Allure.step("Set internal transfer restriction");
        Allure.step("Set no bonus restriction");
        Allure.step("Set open new account restriction");
        Allure.step("Set deposit restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOOPHOLE_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked loophole abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat("Verify that there is 4 restrictions", clientGeneralRestrictions.size(), equalTo(4));

        ClientGeneralRestriction expectedRestrictionInternalTransfer = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.INTERNAL_TRANSFER.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionBonus = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.CREDIT_AND_BONUS.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionNewAccount = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.ACCOUNT_CREATION.getIdLong(), "APPLIED");
        ClientGeneralRestriction expectedRestrictionDeposit = new ClientGeneralRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.DEPOSITS.getIdLong(), "APPLIED");

        Allure.step("Check that expected restrictions applied on client is exists in DB");
        assertThat(clientGeneralRestrictions, containsInAnyOrder(expectedRestrictionInternalTransfer, expectedRestrictionBonus, expectedRestrictionNewAccount, expectedRestrictionDeposit));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p24")
    @AllureId("946")
    void registrationRuleExitEventEnd3p24Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p24");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user is Loophole abuser = true");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOOPHOLE_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked loophole abuser"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify restrictions in Mitigation Service db
        assertThat("Verify that there is only restriction in Mitigation Service db", dbAlerts.size(), equalTo(1));
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        ClientGeneralRestriction restriction = clientGeneralRestrictions.getFirst();
        ClientGeneralRestriction expectedRestriction = new ClientGeneralRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, Restriction.MANUAL_WITHDRAWAL_REVIEW.getIdLong(), "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3p25")
    @AllureId("1178")
    void registrationRuleExitEventEnd3p25Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3p25");
        Allure.step("toxic accounts linked = true");
        Allure.step("connected user fraud is none of listed in other cases");
        Allure.step("Connection score by attributes < 0.75");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Registration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("POTENTIAL_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("Linked unknown abuser"));

        // Verify restrictions in Mitigation Service db

        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientGeneralRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientGeneralRestrictions, empty());
    }
}
