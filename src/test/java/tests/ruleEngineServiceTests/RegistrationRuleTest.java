package tests.ruleEngineServiceTests;

import businessObjects.db.mitigationServiceDB.ClientsRestriction;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.rules.registrationRule.RegistrationRuleData;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.*;

import static businessObjects.db.clickhouse.csTbEmailTable.EmailTableEntryFactory.getEmailTableEntryByCrmUser;
import static helpers.data.rules.registrationRule.RegistrationRuleDataFactory.*;
import static helpers.database.DbHelper.*;
import static helpers.database.MitigationHelper.cleanUserRestriction;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_REGISTRATION_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_SERVICE)
@Disabled
@Muted
public class RegistrationRuleTest {

    public static RegistrationRuleData registrationRuleData1 = getRegistrationRuleExitEventEnd1Data();
    public static RegistrationRuleData registrationRuleData2 = getRegistrationRuleExitEventEnd2Data();
    public static RegistrationRuleData registrationRuleData3 = getRegistrationRuleExitEventEnd3Data();
    public static RegistrationRuleData registrationRuleData4 = getRegistrationRuleExitEventEnd4Data();
    public static RegistrationRuleData registrationRuleData5 = getRegistrationRuleExitEventEnd5Data();
    public static RegistrationRuleData registrationRuleData6 = getRegistrationRuleExitEventEnd6Data();
    public static RegistrationRuleData registrationRuleData7v1 = getRegistrationRuleExitEventEnd7Version1Data();
    public static RegistrationRuleData registrationRuleData7v2 = getRegistrationRuleExitEventEnd7Version2Data();
    public static RegistrationRuleData registrationRuleData7v3 = getRegistrationRuleExitEventEnd7Version3Data();
    public static RegistrationRuleData registrationRuleData7v4 = getRegistrationRuleExitEventEnd7Version4Data();
    public static RegistrationRuleData registrationRuleData7v5 = getRegistrationRuleExitEventEnd7Version5Data();
    public static RegistrationRuleData registrationRuleData7v6 = getRegistrationRuleExitEventEnd7Version6Data();
    public static RegistrationRuleData registrationRuleData7v7 = getRegistrationRuleExitEventEnd7Version7Data();
    public static RegistrationRuleData registrationRuleData7v8 = getRegistrationRuleExitEventEnd7Version8Data();
    public static RegistrationRuleData registrationRuleData7v9 = getRegistrationRuleExitEventEnd7Version9Data();
    public static RegistrationRuleData registrationRuleData7v10 = getRegistrationRuleExitEventEnd7Version10Data();
    public static RegistrationRuleData registrationRuleData7v11 = getRegistrationRuleExitEventEnd7Version11Data();
    public static RegistrationRuleData registrationRuleData7v12 = getRegistrationRuleExitEventEnd7Version12Data();
    public static RegistrationRuleData registrationRuleData7v13 = getRegistrationRuleExitEventEnd7Version13Data();
    public static RegistrationRuleData registrationRuleData7v14 = getRegistrationRuleExitEventEnd7Version14Data();
    public static List<RegistrationRuleData> dbDataList = new ArrayList<>();

    @BeforeAll
    public static void setupDbData() throws ReflectiveOperationException, SQLException {

        // Put all the db data for setup in a list
        dbDataList.add(registrationRuleData1);
        dbDataList.add(registrationRuleData2);
        dbDataList.add(registrationRuleData3);
        dbDataList.add(registrationRuleData4);
        dbDataList.add(registrationRuleData5);
        dbDataList.add(registrationRuleData6);
        dbDataList.add(registrationRuleData7v1);
        dbDataList.add(registrationRuleData7v2);
        dbDataList.add(registrationRuleData7v3);
        dbDataList.add(registrationRuleData7v4);
        dbDataList.add(registrationRuleData7v5);
        dbDataList.add(registrationRuleData7v6);
        dbDataList.add(registrationRuleData7v7);
        dbDataList.add(registrationRuleData7v8);
        dbDataList.add(registrationRuleData7v9);
        dbDataList.add(registrationRuleData7v10);
        dbDataList.add(registrationRuleData7v11);
        dbDataList.add(registrationRuleData7v12);
        dbDataList.add(registrationRuleData7v13);
        dbDataList.add(registrationRuleData7v14);

        // Loop through the list with data and insert all the data into the according tables
        for (RegistrationRuleData data : dbDataList) {
            insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            insertObjectToDb(EMAIL_TABLE_NAME, getEmailTableEntryByCrmUser(data.crmTbUserObject));
            data.connectedUsers.forEach(user -> {
                try {
                    insertObjectToDb(CRM_USER_TABLE_NAME, user);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.connectedUsers.forEach(user -> {
                try {
                    insertObjectToDb(EMAIL_TABLE_NAME, getEmailTableEntryByCrmUser(user));
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.connections.forEach(connection -> {
                try {
                    insertObjectToDb(CONNECTIONS_TABLE_NAME, connection);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObject);
        }
    }

    @Test
    @DisplayName("Registration rule exit Event_End_1")
    @AllureId("155")
    public void registrationRuleExitEventEnd1Test() throws Exception {
        Allure.step("No toxic accounts linked");
        Allure.step("No different identity connections");
        Allure.step("IP country == address country");
        Allure.step("LN score != high");

        Allure.step("Produce registration event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData1.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData1.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", registrationRuleData1.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData1.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", registrationRuleData1.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("Registration rule exit Event_End_2")
    @AllureId("156")
    public void registrationRuleExitEventEnd2Test() throws Exception {
        Allure.step("No toxic accounts linked");
        Allure.step("No different identity connections");
        Allure.step("IP country == address country");
        Allure.step("LN score == high");
        Allure.step("Set manual withdrawal restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData2.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData2.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData2.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("POTENTIAL_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo(registrationRuleData2.lnSessionParsedObject.policyScore));
        assertThat("Verify alert id not null", alert.rule.attributes.stepName, equalTo("High Lexis score"));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData2.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData2.clientHelper.getUcid(),
                registrationRuleData2.crmTbUserObject.regulator,
                12L,
                "Registration_set_manual_withdrawal_restriction_2",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3")
    @AllureId("157")
    public void registrationRuleExitEventEnd3Test() throws Exception {
        Allure.step("No toxic accounts linked");
        Allure.step("No different identity connections");
        Allure.step("IP country != address country");
        Allure.step("Set manual withdrawal restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData3.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData3.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData3.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("POTENTIAL_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo(registrationRuleData3.lnSessionParsedObject.policyScore));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Ip country does not equal address country"));
        assertThat("Verify rule attributes ipAddress is correct", alert.rule.attributes.ipAddress, equalTo(registrationRuleData3.lnSessionParsedObject.trueIp));
        assertThat("Verify rule attributes country is correct", alert.rule.attributes.country, equalTo(registrationRuleData3.crmTbUserObject.countryCode));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData3.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData3.clientHelper.getUcid(),
                registrationRuleData3.crmTbUserObject.regulator,
                12L,
                "Registration_set_manual_withdrawal_restriction_1",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_4")
    @AllureId("158")
    public void registrationRuleExitEventEnd4Test() throws Exception {
        Allure.step("No toxic accounts linked");
        Allure.step("Different identity connections");
        Allure.step("Linked to IB account OR Same referrer");
        Allure.step("Set manual withdrawal restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData4.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData4.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData4.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("POTENTIAL_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo(registrationRuleData4.lnSessionParsedObject.policyScore));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("IB or referrer connection"));
        assertThat("Verify rule attributes refferalId is correct", alert.rule.attributes.refferalId, equalTo(registrationRuleData4.crmTbUserObject.rafReferrerId));
        assertThat("Verify rule attributes ibId is correct", alert.rule.attributes.ibId, equalTo(registrationRuleData4.crmTbUserObject.ibId));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData4.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData4.clientHelper.getUcid(),
                registrationRuleData4.crmTbUserObject.regulator,
                12L,
                "Registration_set_manual_withdrawal_restriction_3",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_5")
    @AllureId("159")
    public void registrationRuleExitEventEnd5Test() throws Exception {
        Allure.step("No toxic accounts linked");
        Allure.step("Different identity connections");
        Allure.step("Not linked to IB account OR Same referrer");
        Allure.step("LN score == Low");

        Allure.step("Produce registration event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData5.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData5.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", registrationRuleData5.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData5.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", registrationRuleData5.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("Registration rule exit Event_End_6")
    @AllureId("160")
    public void registrationRuleExitEventEnd6Test() throws Exception {
        Allure.step("No toxic accounts linked");
        Allure.step("Different identity connections");
        Allure.step("Not linked to IB account OR Same referrer");
        Allure.step("LN score != Low");
        Allure.step("Set no bonus restriction");
        Allure.step("Generate alert");

        Allure.step("Produce registration event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData6.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData6.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData6.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("POTENTIAL_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo(registrationRuleData6.lnSessionParsedObject.policyScore));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("High Lexis Score, same Identity"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData6.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", registrationRuleData6.clientHelper.getUcid()), clientsRestrictions, empty());

        // TODO delete check that there are no restrictions and add restriction id and check for the restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 all available alerts/restrictions")
    @AllureId("161")
    public void registrationRuleExitEventEnd7Version1Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v1.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

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
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v1.clientHelper.getUcid());
        assertThat("Verify that amount of alerts is correct", consumedMessages.size(), equalTo(expectedSteps.size()));

        // Verify alerts
        List<RuleAlert> alerts = consumedMessages.stream()
                .map(message -> {
                    try {
                        return objectMapper.readValue(message, RuleAlert.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        for (RuleAlert alert: alerts) {
            if (Objects.equals(alert.rule.attributes.stepName, "Linked CPA abuser")) {
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v2.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo(registrationRuleData7v2.lnSessionParsedObject.policyScore));
                expectedSteps.remove("Linked CPA abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked bonus abuser")){
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v5.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked bonus abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked voucher abuse")){
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v7.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOSS_VOUCHER_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked voucher abuse");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked news trading abuser")){
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v9.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NEWS_ABUSER"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked trading abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked TLS abuser")){
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v11.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("TLS_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked TLS abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked SWAP abuser")){
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v12.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("SWAP_ARBITRAGE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked SWAP abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked market manipulator abuser")){
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v13.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("MARKET_MANIPULATION"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                expectedSteps.remove("Linked manipulator abuser");
            } else if (Objects.equals(alert.rule.attributes.stepName, "Linked unknown abuser")){
                assertThat("Verify alert id not null", alert.alertId, notNullValue());
                assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
                assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v14.clientHelper.getUcid()));
                assertThat("Verify rule not null", alert.rule, notNullValue());
                assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
                assertThat("Verify rule name not null", alert.rule.name, notNullValue());
                assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
                assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("POTENTIAL_ABUSE"));
                assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
                assertThat("Verify rule attributes fraudType is correct", alert.rule.attributes.fraudType, equalTo("unknown"));
                expectedSteps.remove("Linked unknown abuser");
            }
        }

        assertThat("Verify that all the expected abuse types were found", expectedSteps, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v1.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there are restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction expectedRestriction1 = new ClientsRestriction(
                registrationRuleData7v1.clientHelper.getUcid(),
                registrationRuleData7v1.crmTbUserObject.regulator,
                12L,
                "Registration_SetRestriction_2",
                "APPLY_REQUESTED");
        // TODO add check for a restriction when it's implemented (bad trading env)
        // TODO add check for a restriction when it's implemented (unknown)
        ClientsRestriction expectedRestriction4 = new ClientsRestriction(
                registrationRuleData7v1.clientHelper.getUcid(),
                registrationRuleData7v1.crmTbUserObject.regulator,
                5L,
                "Registration_block_user_restriction_bonus",
                "APPLY_REQUESTED");
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v2.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v2.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v2.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo(registrationRuleData7v2.lnSessionParsedObject.policyScore));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked CPA abuser"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v2.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData7v2.clientHelper.getUcid(),
                registrationRuleData7v2.crmTbUserObject.regulator,
                12L,
                "Registration_SetRestriction_2",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no rebates + Set bad trading environment")
    @AllureId("163")
    public void registrationRuleExitEventEnd7Version3Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v3.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v3.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v3.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes policyScore is correct", alert.rule.attributes.policyScore, equalTo(registrationRuleData7v3.lnSessionParsedObject.policyScore));
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked CPA abuser"));


        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v3.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData7v3.clientHelper.getUcid(),
                registrationRuleData7v3.crmTbUserObject.regulator,
                12L,
                "Registration_SetRestriction_2",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));

        // TODO add check for one more restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions (Bonus abuser)")
    @AllureId("164")
    public void registrationRuleExitEventEnd7Version4Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v4.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v4.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", registrationRuleData7v4.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v4.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", registrationRuleData7v4.clientHelper.getUcid()), clientsRestrictions, empty());
        // TODO add check for a restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions + Set bad trading environment")
    @AllureId("165")
    public void registrationRuleExitEventEnd7Version5Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v5.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v5.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v5.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked bonus abuser"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v5.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData7v5.clientHelper.getUcid(),
                registrationRuleData7v5.crmTbUserObject.regulator,
                12L,
                "Registration_SetRestriction_6",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
        // TODO add check for one more restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions + Block user")
    @AllureId("166")
    public void registrationRuleExitEventEnd7Version6Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v6.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v6.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v6.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked bonus abuser"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v6.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData7v6.clientHelper.getUcid(),
                registrationRuleData7v6.crmTbUserObject.regulator,
                5L,
                "Registration_block_user_restriction_bonus",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
        // TODO add check for one more restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers")
    @AllureId("167")
    public void registrationRuleExitEventEnd7Version7Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v7.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v7.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v7.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOSS_VOUCHER_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked voucher abuse"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v7.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData7v7.clientHelper.getUcid(),
                registrationRuleData7v7.crmTbUserObject.regulator,
                12L,
                "Registration_SetRestriction_7",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers + Set bad trading environment")
    @AllureId("168")
    public void registrationRuleExitEventEnd7Version8Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v8.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v8.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v8.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("LOSS_VOUCHER_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked voucher abuse"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v8.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData7v8.clientHelper.getUcid(),
                registrationRuleData7v8.crmTbUserObject.regulator,
                12L,
                "Registration_SetRestriction_7",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));

        // TODO add check for one more restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers + Set bad trading environment")
    @AllureId("169")
    public void registrationRuleExitEventEnd7Version9Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v9.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v9.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v9.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NEWS_ABUSER"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked news trading abuser"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v9.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData7v9.clientHelper.getUcid(),
                registrationRuleData7v9.crmTbUserObject.regulator,
                12L,
                "Registration_SetRestriction_9",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set bad trading environment (News trader)")
    @AllureId("170")
    public void registrationRuleExitEventEnd7Version10Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v10.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v10.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v10.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("NEWS_ABUSER"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked news trading abuser"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v10.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", registrationRuleData7v10.clientHelper.getUcid()), clientsRestrictions, empty());
        // TODO add check for a restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Block user (TLS)")
    @AllureId("171")
    public void registrationRuleExitEventEnd7Version11Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v11.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v11.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v11.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("TLS_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked TLS abuser"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v11.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData7v11.clientHelper.getUcid(),
                registrationRuleData7v11.crmTbUserObject.regulator,
                5L,
                "Registration_block_user_restriction_tls",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Manual withdrawal review (Swap abuse)")
    @AllureId("209")
    public void registrationRuleExitEventEnd7Version12Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v12.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v12.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v12.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("SWAP_ARBITRAGE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked SWAP abuser"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v12.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction restriction = clientsRestrictions.getFirst();
        ClientsRestriction expectedRestriction = new ClientsRestriction(
                registrationRuleData7v12.clientHelper.getUcid(),
                registrationRuleData7v12.crmTbUserObject.regulator,
                12L,
                "Registration_SetRestriction_12",
                "APPLY_REQUESTED");

        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions (Market manipulation)")
    @AllureId("172")
    public void registrationRuleExitEventEnd7Version13Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v13.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v13.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v13.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("MARKET_MANIPULATION"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked market manipulator abuser"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v13.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        // TODO add check for a restriction when it's implemented
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 unknown fraud type")
    @AllureId("")
    public void registrationRuleExitEventEnd7Version14Test() throws Exception {
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
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(registrationRuleData7v14.registrationEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, registrationRuleData7v14.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(registrationRuleData7v14.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("clientRegistration"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("POTENTIAL_ABUSE"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("Linked unknown abuser"));
        assertThat("Verify rule attributes fraudType is correct", alert.rule.attributes.fraudType, equalTo("unknown"));

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES,
                MITIGATION_CLIENTS_RESTRICTION,
                String.format("ucid = '%s'", registrationRuleData7v14.clientHelper.getUcid()),
                ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", registrationRuleData1.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @AfterAll
    public static void deleteDbData() throws Exception {

        // Loop through the list with data and delete all the previously created data into the according tables
        for (RegistrationRuleData data : dbDataList) {
            deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            data.connectedUsers.forEach(user -> {
                try {
                    deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", user.userId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.connectedUsers.forEach(user -> {
                try {
                    deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("user_id = %s", user.userId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.connections.forEach(connection -> {
                try {
                    deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = %s", connection.userFrom));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObject.userId));
            cleanUserRestriction(data.clientHelper.getUcid());
        }
    }
}
