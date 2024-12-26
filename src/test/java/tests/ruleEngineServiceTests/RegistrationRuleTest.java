package tests.ruleEngineServiceTests;

import businessObjects.db.backofficeDb.alert.Alert;
import businessObjects.db.mitigationServiceDb.ClientsRestriction;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import helpers.data.rules.registrationRule.RegistrationRuleData;
import helpers.database.DbName;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static businessObjects.api.mitigationService.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.registrationRule.RegistrationRuleDataFactory.*;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_REGISTRATION_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_SERVICE)
public class RegistrationRuleTest extends TestBaseRule {

    public static Map<String, RegistrationRuleData> dbDataMap = new HashMap<>();

    @BeforeAll
    public static void setupDbData() throws ReflectiveOperationException, SQLException, IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupRegistrationRuleData();
    }

    @AfterAll
    public static void deleteDbData() throws Exception {
        deleteRegistrationRuleData(dbDataMap);
    }

    @Test
    @DisplayName("Registration rule exit Event_End_1")
    @AllureId("155")
    public void registrationRuleExitEventEnd1Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("1");

        Allure.step("No toxic accounts linked");
        Allure.step("No different identity connections");
        Allure.step("IP country == address country");
        Allure.step("LN score != high");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
    @DisplayName("Registration rule exit Event_End_2")
    @AllureId("156")
    public void registrationRuleExitEventEnd2Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("2");

        Allure.step("No toxic accounts linked");
        Allure.step("No different identity connections");
        Allure.step("IP country == address country");
        Allure.step("LN score == high");
        Allure.step("Set manual withdrawal restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(data.lnSessionParsedObject.riskRating));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("High Lexis score"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

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
    @DisplayName("Registration rule exit Event_End_3")
    @AllureId("157")
    public void registrationRuleExitEventEnd3Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("3");

        Allure.step("No toxic accounts linked");
        Allure.step("No different identity connections");
        Allure.step("IP country != address country");
        Allure.step("Set manual withdrawal restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(data.lnSessionParsedObject.riskRating));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Ip country does not equal address country"));
        assertThat("Verify rule attributes ipAddress is correct", alert.rule.attributes.ipAddress, equalTo(data.lnSessionParsedObject.trueIp));
        assertThat("Verify rule attributes country is correct", alert.rule.attributes.country, equalTo(data.crmTbUserObject.countryCode));

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
        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_set_manual_withdrawal_restriction_3", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_4")
    @AllureId("158")
    public void registrationRuleExitEventEnd4Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("4");
        Allure.step("No toxic accounts linked");
        Allure.step("Different identity connections");
        Allure.step("Linked to IB account OR Same referrer");
        Allure.step("Set manual withdrawal restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(data.lnSessionParsedObject.riskRating));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("IB or referrer connection"));
        assertThat("Verify rule attributes refferalId is correct", alert.rule.attributes.refferalId, equalTo(data.crmTbUserObject.rafReferrerId));
        assertThat("Verify rule attributes ibId is correct", alert.rule.attributes.ibId, equalTo(data.crmTbUserObject.ibId));

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
    @DisplayName("Registration rule exit Event_End_5")
    @AllureId("159")
    public void registrationRuleExitEventEnd5Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("5");

        Allure.step("No toxic accounts linked");
        Allure.step("Different identity connections");
        Allure.step("Not linked to IB account OR Same referrer");
        Allure.step("LN score == Low");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
    public void registrationRuleExitEventEnd6Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("6");

        Allure.step("No toxic accounts linked");
        Allure.step("Different identity connections");
        Allure.step("Not linked to IB account OR Same referrer");
        Allure.step("LN score != Low");
        Allure.step("Set no bonus restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(data.lnSessionParsedObject.riskRating));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("High Lexis score, same Identity"));

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
    @DisplayName("Registration rule exit Event_End_7 all available alerts/restrictions")
    @AllureId("161")
    @Disabled
    @Muted
    @Tag(TAG_MANUAL)
    public void registrationRuleExitEventEnd7Version1Test() throws Exception {
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
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Set<String> expectedSteps = new HashSet<>();
        expectedSteps.add("Linked CPA abuser");
        expectedSteps.add("Linked bonus abuser");
        expectedSteps.add("Linked voucher abuse");
        expectedSteps.add("Linked news trading abuser");
        expectedSteps.add("Linked TLS abuser");
        expectedSteps.add("Linked SWAP abuser");
        expectedSteps.add("Linked market manipulator abuser");
        expectedSteps.add("Linked unknown abuser");

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that amount of alerts is correct", consumedMessages.size(), equalTo(expectedSteps.size()));

        // Verify alerts
        List<RuleAlert> alerts = consumedMessages.stream().map(message -> {
            try {
                return objectMapper.readValue(message, RuleAlert.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();

        for (RuleAlert alert : alerts) {
            if (Objects.equals(alert.rule.attributes.stepName, "Linked CPA abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(data.lnSessionParsedObject.riskRating));
                expectedSteps.remove("Linked CPA abuser");
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
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked voucher abuse")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOSS_VOUCHER_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked voucher abuse");
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
                expectedSteps.remove("Linked trading abuser");
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
                expectedSteps.remove("Linked manipulator abuser");
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
            }
        }

        assertThat("Verify that all the expected abuse types were found", expectedSteps, empty());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(8));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are restriction", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestriction1 = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 12L, "Registration_SetRestriction_2", "APPLIED");
        // TODO add check for a restriction when it's implemented (bad trading env)
        // TODO add check for a restriction when it's implemented (unknown)
        ClientsRestriction expectedRestriction4 = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 5L, "Registration_block_user_restriction_bonus", "APPLIED");
        // TODO add check for a restriction when it's implemented (unknown)
        // TODO add check for a restriction when it's implemented (bad trading env)
        // TODO add check for a restriction when it's implemented (b-book -> a-book)
        // TODO add check for a restriction when it's implemented (unknown)
        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction1, expectedRestriction4));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no rebates")
    @AllureId("162")
    public void registrationRuleExitEventEnd7Version2Test() throws Exception {
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
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(data.lnSessionParsedObject.riskRating));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked CPA abuser"));

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
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_SetRestriction_2", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no rebates + Set bad trading environment")
    @AllureId("163")
    public void registrationRuleExitEventEnd7Version3Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v3");

        Allure.step("Toxic accounts linked");
        Allure.step("Any of the connected users is a CPA abuser");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("LN == High");
        Allure.step("Set bad trading environment restriction");
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
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes riskRating is correct", alert.rule.attributes.riskRating, equalTo(data.lnSessionParsedObject.riskRating));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked CPA abuser"));

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
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_SetRestriction_2", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));

        // TODO add check for one more restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions (Bonus abuser)")
    @AllureId("164")
    public void registrationRuleExitEventEnd7Version4Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v4");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is a bonus abuser");
        Allure.step("Set no bonuses, promotions");
        Allure.step("Is Vjp");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions + Set bad trading environment")
    @AllureId("165")
    public void registrationRuleExitEventEnd7Version5Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v5");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is a bonus abuser");
        Allure.step("Set no bonuses, promotions");
        Allure.step("NOT Vjp");
        Allure.step("Low LN score");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_SetRestriction_6", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
        // TODO add check for one more restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions + Block user")
    @AllureId("166")
    public void registrationRuleExitEventEnd7Version6Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v6");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is a bonus abuser");
        Allure.step("Set no bonuses, promotions");
        Allure.step("NOT Vjp");
        Allure.step("High LN score");
        Allure.step("Block user");
        Allure.step("Generate alert");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 5L, "Registration_block_user_restriction_bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
        // TODO add check for one more restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers")
    @AllureId("167")
    public void registrationRuleExitEventEnd7Version7Test() throws Exception {
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
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

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
    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers + Set bad trading environment")
    @AllureId("168")
    public void registrationRuleExitEventEnd7Version8Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v8");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("Is a voucher abuser");
        Allure.step("Set manual withdrawal review restriction");
        Allure.step("High LN score");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "Registration_SetRestriction_7", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));

        // TODO add check for one more restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers + Set bad trading environment")
    @AllureId("169")
    public void registrationRuleExitEventEnd7Version9Test() throws Exception {
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
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

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
    @DisplayName("Registration rule exit Event_End_7 only Set bad trading environment (News trader)")
    @AllureId("170")
    public void registrationRuleExitEventEnd7Version10Test() throws Exception {
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
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
        // TODO add check for a restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Block user (TLS)")
    @AllureId("171")
    public void registrationRuleExitEventEnd7Version11Test() throws Exception {
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
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 5L, "Registration_block_user_restriction_tls", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Manual withdrawal review (Swap abuse)")
    @AllureId("209")
    public void registrationRuleExitEventEnd7Version12Test() throws Exception {
        RegistrationRuleData data = dbDataMap.get("7v12");

        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("Is a voucher abuser");
        Allure.step("Set no vouchers");
        Allure.step("High LN score");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("Swap abuse");
        Allure.step("Set Manual Withdrawal Review restriction");
        Allure.step("Generate alert");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

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
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions (Market manipulation)")
    @AllureId("172")
    public void registrationRuleExitEventEnd7Version13Test() throws Exception {
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
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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

        assertThat("Verify that there is only 1 restriction in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 7L, "Registration_a-book_restriction", "APPLIED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 unknown fraud type")
    @AllureId("484")
    public void registrationRuleExitEventEnd7Version14Test() throws Exception {
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
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        assertThat("Verify rule attributes fraudType is correct", alert.rule.attributes.fraudType, allOf(containsString(data.clientFraudTypes.getFirst().fraudTypeCode), containsString(data.clientFraudTypes.getLast().fraudTypeCode))
        );

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }
}
