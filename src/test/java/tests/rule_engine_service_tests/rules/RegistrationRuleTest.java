package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientsRestriction;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.rules.registration_rule.RegistrationRuleData;
import helpers.database.DbName;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.*;
import java.util.*;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.registration_rule.RegistrationRuleDataFactory.*;
import static helpers.database.DbHelper.*;
import static helpers.kafka.KafkaHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Disabled("disabled till finalization of the rule")
@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_REGISTRATION_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class RegistrationRuleTest extends TestBaseRule {

    static Map<String, RegistrationRuleData> dbDataMap = new HashMap<>();

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
        RegistrationRuleData data = dbDataMap.get("1");

        Allure.step("No toxic accounts linked");
        Allure.step("Connections in the same brand with different identity?");
        Allure.step("LN score != high");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
    @DisplayName("Registration rule exit Event2")
    @AllureId("156")
    void registrationRuleExitEventEnd2Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("2");
        System.out.println(data.clientHelper.getUcid());

        Allure.step("No toxic accounts linked");
        Allure.step("No different identity connections");
        Allure.step("IP country == address country");
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
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo("high"));
        assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo("-50"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );
        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));
        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_set_manual_withdrawal_restriction_2", "APPLIED");
        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event3p1")
    @AllureId("158")
    void registrationRuleExitEventEnd3p1Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("3p1");
        Allure.step("No toxic accounts linked");
        Allure.step("Different identity and same brand connections");
        Allure.step("Linked to same raf");
        Allure.step("Set manual withdrawal restriction");
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
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(data.lnSessionParsedObject.getRiskRating()));
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("IB or referrer connection"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db
        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_set_manual_withdrawal_restriction_3", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event3p2")
    @AllureId("1135")
    void registrationRuleExitEventEnd3p2Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("3p2");
        Allure.step("No toxic accounts linked");
        Allure.step("Different identity and same brand connections");
        Allure.step("Linked to same IB account");
        Allure.step("Set manual withdrawal restriction");
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
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(data.lnSessionParsedObject.getRiskRating()));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.reason, equalTo("IB or referrer connection"));
        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify alert in BO db

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class);

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_set_manual_withdrawal_restriction_3", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event4p1")
    @AllureId("1136")
    void registrationRuleExitEventEnd4p1Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("4p1");

        Allure.step("No toxic accounts linked");
        Allure.step("Different identity and same brand connections");
        Allure.step("Not linked to IB account OR Same referrer");
        Allure.step("LN score == medium");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
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
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("High or medium Lexis score with connected clients"));
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo("medium"));
        assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo("-49"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("Registration rule exit Event4p2")
    @AllureId("1137")
    void registrationRuleExitEventEnd4p2Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("4p2");

        Allure.step("No toxic accounts linked");
        Allure.step("Different identity and same brand connections");
        Allure.step("Not linked to IB account OR Same referrer");
        Allure.step("LN score == high");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 100);
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
        assertThat("Verify rule attributes reason is correct", alert.rule.attributes.reason, equalTo("High Lexis score"));
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo("high"));
        assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo("-49"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );
        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_5")
    @AllureId("159")
    void registrationRuleExitEventEnd5Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("5");

        Allure.step("No toxic accounts linked");
        Allure.step("Different identity and same brand connections");
        Allure.step("Not linked to IB account OR Same referrer");
        Allure.step("LN score != Medium or High");
        Allure.step("Empty exit");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
    @DisplayName("Registration rule exit Event_End_6")
    @AllureId("160")
    void registrationRuleExitEventEnd6Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("6");

        Allure.step("No toxic accounts linked");
        Allure.step("Different identity connections");
        Allure.step("Not linked to IB account OR Same referrer");
        Allure.step("LN score != Medium or High");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("POTENTIAL_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(
                data.lnSessionParsedObject.getRiskRating()));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("High Lexis score, same Identity"));
        assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo("-49"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());

        // TODO delete check that there are no restrictions and add restriction id and check for the restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v1 all available alerts/restrictions")
    @AllureId("161")
    void registrationRuleExitEventEnd7Version1Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v1");

        Allure.step("Toxic accounts linked");
        Allure.step("Any of the connected users is a CPA abuser");
        Allure.step("Set no rebates");
        Allure.step("LN == High");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("Connected user is a bonus abuser");
        Allure.step("Set no bonuses, promotions");
        Allure.step("Not Vjp");
        Allure.step("Set Login CRM Restriction");
        Allure.step("Generate alert");
        Allure.step("Is a voucher abuser");
        Allure.step("Set no vouchers");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("Set bad trading environment");
        Allure.step("News trader");
        Allure.step("Not Vjp");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("Is TLS");
        Allure.step("Block user");
        Allure.step("Generate alert");
        Allure.step("Swap abuse");
        Allure.step("Set no swap free option");
        Allure.step("Generate alert");
        Allure.step("Market manipulation");
        Allure.step("A-book the new account");
        Allure.step("Set no bonuses, promotions");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Set<String> expectedSteps = new HashSet<>();
        expectedSteps.add("Linked CPA_ABUSE abuser");
        expectedSteps.add("Linked bonus abuser");
        expectedSteps.add("Linked voucher abuser");
        expectedSteps.add("Linked news trading abuser");
        expectedSteps.add("Linked TLS abuser");
        expectedSteps.add("Linked SWAP abuser");
        expectedSteps.add("Linked market manipulator abuser");
        expectedSteps.add("Linked unknown abuser");
        expectedSteps.add("Linked GAP abuser");
        expectedSteps.add("Linked latency abuser");
        expectedSteps.add("Linked Pricing ERROR abuser");
        expectedSteps.add("Linked NBP abuser");
        expectedSteps.add("Linked HFT abuser");
        expectedSteps.add("Linked loophole abuser");

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that amount of alerts is correct", consumedMessages.size(), equalTo(expectedSteps.size()));
        System.out.println(consumedMessages);

        // Verify alerts
        List<RuleAlert> alerts = consumedMessages.stream().map(message -> {
            try {
                return objectMapper.readValue(message, RuleAlert.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.attributes.stepName, "Linked CPA_ABUSE abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(
                        data.lnSessionParsedObject.getRiskRating()));
                expectedSteps.remove("Linked CPA_ABUSE abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked bonus abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked bonus abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked voucher abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOSS_VOUCHER_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked voucher abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked news trading abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NEWS_ABUSER"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked news trading abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked TLS abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("TLS_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked TLS abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked SWAP abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("SWAP_ARBITRAGE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked SWAP abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked market manipulator abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("MARKET_MANIPULATION"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked market manipulator abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked unknown abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("POTENTIAL_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                assertThat("Verify rule attributes fraudType is correct", alert.rule.attributes.fraudType, equalTo("UNKNOWN"));
                expectedSteps.remove("Linked unknown abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked GAP abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("GAP_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked GAP abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked latency abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LATENCY_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked latency abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked Pricing ERROR abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("PRICING_ERROR_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked Pricing ERROR abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked NBP abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NBP_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked NBP abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked HFT abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HFT_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked HFT abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked loophole abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOOPHOLE_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked loophole abuser");
            }
        }

        assertThat("Verify that all the expected abuse types were found", expectedSteps, empty());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify alert in BO DB", dbAlerts.size(), equalTo(14));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are restriction", clientsRestrictions.size(), equalTo(3));

        ClientsRestriction expectedRestriction1 = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_SetRestriction_2", "APPLIED");
        ClientsRestriction expectedRestriction3 = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 3L, "Registration_block_user_restriction_bonus", "APPLIED");
        ClientsRestriction expectedRestriction4 = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 1L, "Registration_block_user_restriction_bonus", "APPLIED");
        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction1, expectedRestriction3, expectedRestriction4));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v2 CPA + low Lexis score")
    @AllureId("162")
    void registrationRuleExitEventEnd7Version2Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v2");

        Allure.step("Toxic accounts linked");
        Allure.step("Any of the connected users is a CPA abuser");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("LN == Low");
        Allure.step("Generate alert");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(
                data.lnSessionParsedObject.getRiskRating()));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked CPA_ABUSE abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_SetRestriction_2", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v4 mirror trader abuser, Startrader")
    @AllureId("164")
    void registrationRuleExitEventEnd7Version4Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v4");

        Allure.step("Toxic accounts linked");
        Allure.step("Connected user is a mirror trading abuser");
        Allure.step("Connection score > 0.75");
        Allure.step("Set Deposits restriction");
        Allure.step("Set Open new account restriction"); // Disabled for now
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked bonus abuser"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
        // TODO add check for a restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v5 Bonus abuser, not Vjp")
    @AllureId("165")
    void registrationRuleExitEventEnd7Version5Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v5");

        Allure.step("Toxic accounts linked");
        Allure.step("Connected user is a bonus abuser");
        Allure.step("Connection score > 0.75");
        Allure.step("Set Deposits restriction");
        Allure.step("Set Open new account restriction"); // Disabled for now
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked bonus abuser"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
        // TODO add check for a restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v7 Voucher abuser, low Lexis score")
    @AllureId("167")
    void registrationRuleExitEventEnd7Version7Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v7");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("Is a voucher abuser");
        Allure.step("Set no vouchers");
        Allure.step("Low LN score");
        Allure.step("Generate alert");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOSS_VOUCHER_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked voucher abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_SetRestriction_7", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v9 News trader, Vjp")
    @AllureId("169")
    void registrationRuleExitEventEnd7Version9Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v9");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("Is a News trader");
        Allure.step("Is Vjp");
        Allure.step("Wipeout blacklist");
        Allure.step("Generate alert");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NEWS_ABUSER"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked news trading abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_SetRestriction_9", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v10 News trader, not Vjp")
    @AllureId("170")
    void registrationRuleExitEventEnd7Version10Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v10");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("Is a News trader");
        Allure.step("NOT Vjp");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NEWS_ABUSER"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked news trading abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("VVerify alert in db", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_SetRestriction_9", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v11 TLS abuser")
    @AllureId("171")
    void registrationRuleExitEventEnd7Version11Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v11");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("Is TLS");
        Allure.step("Block user");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("TLS_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked TLS abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify alert in db", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestriction = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 3L, "Registration_block_user_restriction_bonus", "APPLIED");
        ClientsRestriction expectedRestriction2 = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 1L, "Registration_block_user_restriction_bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction, expectedRestriction2));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v12 Swap abuser")
    @AllureId("209")
    void registrationRuleExitEventEnd7Version12Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v12");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("Is not a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("Swap abuse");
        Allure.step("Set Manual Withdrawal Review restriction");
        Allure.step("Generate alert");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("SWAP_ARBITRAGE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked SWAP abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify alert in db", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_SetRestriction_12", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v13 Market manipulator")
    @AllureId("172")
    void registrationRuleExitEventEnd7Version13Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v13");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("Market manipulation");
        Allure.step("A-book the new account");
        Allure.step("Set no bonuses, promotions");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("MARKET_MANIPULATION"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked market manipulator abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify alert in db", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 1L, "Registration_block_user_restriction_bonus", "APPLIED");
        ClientsRestriction expectedRestriction2 = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 3L, "Registration_block_user_restriction_bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction, expectedRestriction2));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v14 unknown fraud type")
    @AllureId("484")
    void registrationRuleExitEventEnd7Version14Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v14");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Market manipulation");
        Allure.step("Unknown fraud type");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("POTENTIAL_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked unknown abuser"));
        assertThat("Verify rule attributes fraudType is correct", alert.rule.attributes.fraudType, allOf(containsString(data.clientFraudTypes.getFirst().getFraudTypeCode()), containsString(data.clientFraudTypes.getLast().getFraudTypeCode()))
        );

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v15 GAP abuser")
    @AllureId("689")
    void registrationRuleExitEventEnd7Version15Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v15");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Market manipulation");
        Allure.step("GAP Abuser");
        Allure.step("Set login CRM restriction");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("GAP_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked GAP abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 1L, "Registration_block_user_restriction_bonus", "APPLIED");
        ClientsRestriction expectedRestriction2 = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 3L, "Registration_block_user_restriction_bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction, expectedRestriction2));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v16 Latency abuser")
    @AllureId("690")
    void registrationRuleExitEventEnd7Version16Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v16");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Market manipulation");
        Allure.step("NOT GAP Abuser");
        Allure.step("Latency Abuser");
        Allure.step("Set login CRM restriction");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LATENCY_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked latency abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 1L, "Registration_block_user_restriction_bonus", "APPLIED");
        ClientsRestriction expectedRestriction2 = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 3L, "Registration_block_user_restriction_bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction, expectedRestriction2));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v17 Pricing error abuser")
    @AllureId("691")
    void registrationRuleExitEventEnd7Version17Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v17");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Market manipulation");
        Allure.step("NOT GAP Abuser");
        Allure.step("NOT Latency Abuser");
        Allure.step("Pricing Error Abuser");
        Allure.step("Set login CRM restriction");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("PRICING_ERROR_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked Pricing ERROR abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 1L, "Registration_block_user_restriction_bonus", "APPLIED");
        ClientsRestriction expectedRestriction2 = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 3L, "Registration_block_user_restriction_bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction, expectedRestriction2));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v18 NBP abuser")
    @AllureId("692")
    void registrationRuleExitEventEnd7Version18Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v18");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Market manipulation");
        Allure.step("NOT GAP Abuser");
        Allure.step("NOT Latency Abuser");
        Allure.step("NOT Pricing Error Abuser");
        Allure.step("NBP Abuser");
        Allure.step("Set login CRM restriction");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NBP_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked NBP abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 1L, "Registration_block_user_restriction_bonus", "APPLIED");
        ClientsRestriction expectedRestriction2 = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 3L, "Registration_block_user_restriction_bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction, expectedRestriction2));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v19 HFT abuser")
    @AllureId("693")
    void registrationRuleExitEventEnd7Version19Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v19");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Market manipulation");
        Allure.step("NOT GAP Abuser");
        Allure.step("NOT Latency Abuser");
        Allure.step("NOT Pricing Error Abuser");
        Allure.step("NOT NBP Abuser");
        Allure.step("HFT Abuser");
        Allure.step("Set login CRM restriction");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HFT_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked HFT abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 1L, "Registration_block_user_restriction_bonus", "APPLIED");
        ClientsRestriction expectedRestriction2 = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 3L, "Registration_block_user_restriction_bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction, expectedRestriction2));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v20 Loophole abuser")
    @AllureId("694")
    void registrationRuleExitEventEnd7Version20Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v20");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Market manipulation");
        Allure.step("NOT GAP Abuser");
        Allure.step("NOT Latency Abuser");
        Allure.step("NOT Pricing Error Abuser");
        Allure.step("NOT NBP Abuser");
        Allure.step("NOT HFT Abuser");
        Allure.step("Loophole Abuser");
        Allure.step("Set login CRM restriction");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOOPHOLE_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked loophole abuser"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 1L, "Registration_block_user_restriction_bonus", "APPLIED");
        ClientsRestriction expectedRestriction2 = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 3L, "Registration_block_user_restriction_bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction, expectedRestriction2));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7v23 all available alerts/restrictions with scores < 0.75")
    @AllureId("946")
    void registrationRuleExitEventEnd7Version23Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v23");

        Allure.step("Toxic accounts linked");
        Allure.step("Any of the connected users is a CPA abuser");
        Allure.step("Set no rebates");
        Allure.step("LN == High");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("Connected user is a bonus abuser");
        Allure.step("Set no bonuses, promotions");
        Allure.step("Not Vjp");
        Allure.step("Set Login CRM Restriction");
        Allure.step("Generate alert");
        Allure.step("Is a voucher abuser");
        Allure.step("Set no vouchers");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("Set bad trading environment");
        Allure.step("News trader");
        Allure.step("Not Vjp");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("Is TLS");
        Allure.step("Block user");
        Allure.step("Generate alert");
        Allure.step("Swap abuse");
        Allure.step("Set no swap free option");
        Allure.step("Generate alert");
        Allure.step("Market manipulation");
        Allure.step("A-book the new account");
        Allure.step("Set no bonuses, promotions");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Set<String> expectedSteps = new HashSet<>();
        expectedSteps.add("Linked CPA_ABUSE abuser");
        expectedSteps.add("Linked bonus abuser");
        expectedSteps.add("Linked voucher abuser");
        expectedSteps.add("Linked news trading abuser");
        expectedSteps.add("Linked TLS abuser");
        expectedSteps.add("Linked SWAP abuser");
        expectedSteps.add("Linked market manipulator abuser");
        expectedSteps.add("Linked GAP abuser");
        expectedSteps.add("Linked latency abuser");
        expectedSteps.add("Linked Pricing ERROR abuser");
        expectedSteps.add("Linked NBP abuser");
        expectedSteps.add("Linked HFT abuser");
        expectedSteps.add("Linked loophole abuser");

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that amount of alerts is correct", consumedMessages.size(), equalTo(expectedSteps.size()));
        System.out.println(consumedMessages);

        // Verify alerts
        List<RuleAlert> alerts = consumedMessages.stream().map(message -> {
            try {
                return objectMapper.readValue(message, RuleAlert.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.attributes.stepName, "Linked CPA_ABUSE abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(
                        data.lnSessionParsedObject.getRiskRating()));
                expectedSteps.remove("Linked CPA_ABUSE abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked bonus abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked bonus abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked voucher abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOSS_VOUCHER_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked voucher abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked news trading abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NEWS_ABUSER"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked news trading abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked TLS abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("TLS_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked TLS abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked SWAP abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("SWAP_ARBITRAGE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked SWAP abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked market manipulator abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("MARKET_MANIPULATION"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked market manipulator abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked GAP abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("GAP_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked GAP abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked latency abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LATENCY_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked latency abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked Pricing ERROR abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("PRICING_ERROR_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked Pricing ERROR abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked NBP abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NBP_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked NBP abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked HFT abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HFT_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked HFT abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked loophole abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOOPHOLE_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked loophole abuser");
            }
        }

        assertThat("Verify that all the expected abuse types were found", expectedSteps, empty());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify alert in BO DB", dbAlerts.size(), equalTo(13));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction expectedRestriction1 = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_SetRestriction_7", "APPLIED");
        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction1));
    }
}
