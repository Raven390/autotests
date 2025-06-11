package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientsRestrictionGeneral;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.enums.FraudTypeOld;
import helpers.data.rules.mirror_trading_rule.MirrorTradingRuleData;
import helpers.database.DbName;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.mirror_trading_rule.MirrorTradingRuleDataFactory.*;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Disabled("disabled till finalization of the rule")
@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_MIRROR_TRADING_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MirrorTradeRuleTest extends TestBaseRule {

    static Map<String, MirrorTradingRuleData> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteMirrorTradingRuleData(dbDataMap);
    }

    @Disabled("Disabled on production")
    @Test
    @DisplayName("Mirror trading rule exit Event_End_2")
    @AllureId("187")
    void mirrorTradeRuleExitEventEnd2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("2");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_31. Clone is a mirror abuser")
    @AllureId("185")
    void mirrorTradeRuleExitEventEnd31Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("3_1");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Mirror Trading"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Close Trade"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.HEDGING.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.hedgingClone, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // TODO enable restrictions check after enabling them on production
//        // Verify restriction
//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
//        );
//
//        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));
//
//        ClientsRestriction expectedRestrictionClose = new ClientsRestriction(
//                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 6L, "Doppelganger is a mirrorAbuser", "APPLIED");
//
//        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
//                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Doppelganger isn't a mirrorAbuser but with bonus", "APPLIED");
//
//        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestrictionClose, expectedRestrictionWithdrawal));
    }

    @Disabled("Disabled on production")
    @Test
    @DisplayName("Mirror trading rule exit Event_End_32. Clone is not a mirror abuser and has credits")
    @AllureId("186")
    void mirrorTradeRuleExitEventEnd3_2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("32");
        System.out.println(data.clientHelper.getUcid());
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Mirror Trading"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Close Trade"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.HEDGING.getDisplayName()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.hedgingClone, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictionGenerals.size(), equalTo(1));

        ClientsRestrictionGeneral expectedRestrictionWithdrawal = new ClientsRestrictionGeneral(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Doppelganger isn't a mirrorAbuser but with bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictionGenerals.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_4_1. Client has no connections and no credits")
    @AllureId("183")
    void mirrorTradeRuleExitEventEnd41Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("41");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Disabled("Disabled on production")
    @Test
    @DisplayName("Mirror trading rule exit Event_End_42. CreditEquityRatio > 0.7 is False")
    @AllureId("184")
    void mirrorTradeRuleExitEventEnd4_2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("42");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_43. Νοn abuser connection. User and connections have no credits")
    @AllureId("895")
    void mirrorTradeRuleExitEventEnd4_3Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("43");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_44. Νοn abuser connection. User has no credits and connections have credits")
    @AllureId("896")
    void mirrorTradeRuleExitEventEnd4_4Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("44");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_51. No Alert if abuse points < 4")
    @AllureId("181")
    void mirrorTradeRuleExitEventEnd5_1Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("51");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Disabled("Disabled on production")
    @Test
    @DisplayName("Mirror trading rule exit Event_End_52")
    @AllureId("182")
    void mirrorTradeRuleExitEventEnd5_2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("52");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Disabled("Disabled on production")
    @Test
    @DisplayName("Mirror trading rule exit Event_End_6")
    @AllureId("173")
    void mirrorTradeRuleExitEventEnd6Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("6");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictionGenerals, empty());
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_1_1")
    @AllureId("179")
    void mirrorTradeRuleExitEventEnd1_1Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("11");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Mirror Trading"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Close Trade"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.HEDGING.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
//        );
//
//        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));
//
//        ClientsRestriction expectedRestrictionClose = new ClientsRestriction(
//                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 6L, "Trading account is a mirror abuser (full rule)", "APPLIED");
//
//        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
//                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Doppelganger is a mirrorAbuser", "APPLIED");
//
//        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestrictionClose, expectedRestrictionWithdrawal));
    }

    @Disabled("Disabled on production")
    @Test
    @DisplayName("Mirror trading rule exit Event_End_1_2")
    @AllureId("220")
    void mirrorTradeRuleExitEventEnd1_2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("12");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Mirror Trading"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Close Trade"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.HEDGING.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.hedgingClone, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat("Verify that there are 2 restrictions", clientsRestrictionGenerals.size(), equalTo(2));

        ClientsRestrictionGeneral expectedRestrictionClose = new ClientsRestrictionGeneral(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 6L, "Trading account is a mirror abuser (full rule)", "APPLIED");

        ClientsRestrictionGeneral expectedRestrictionWithdrawal = new ClientsRestrictionGeneral(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Doppelganger is a mirrorAbuser", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictionGenerals, containsInAnyOrder(expectedRestrictionClose, expectedRestrictionWithdrawal));
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_1")
    @AllureId("178")
    void mirrorTradeRuleExitEventEnd7_1Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("71");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Mirror Trading"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Close Trade"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.HEDGING.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes mirrorTradeScore not null", alert.rule.attributes.mirrorTradeScore, notNullValue());
        assertThat("Verify rule attributes mirrorTrades not null", alert.rule.attributes.mirrorTrades, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
//        );
//
//        assertThat("Verify that there is 1 restriction", clientsRestrictions.size(), equalTo(1));
//
//
//        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
//                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Trading account is a mirror abuser (full rule) but without existing mirror trades", "APPLIED");
//
//        assertThat("Verify that the restriction is as expected", clientsRestrictions.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_2")
    @AllureId("177")
    void mirrorTradeRuleExitEventEnd7_2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("72");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Mirror Trading"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Close Trade"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.HEDGING.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes mirrorTradeScore not null", alert.rule.attributes.mirrorTradeScore, notNullValue());
        assertThat("Verify rule attributes mirrorTrades not null", alert.rule.attributes.mirrorTrades, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
//        );
//
//        assertThat("Verify that there is 1 restriction", clientsRestrictions.size(), equalTo(1));
//
//
//        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
//                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Trading account is a mirror abuser (full rule) but without existing mirror trades", "APPLIED");
//
//        assertThat("Verify that the restriction is as expected", clientsRestrictions.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_3")
    @AllureId("176")
    void mirrorTradeRuleExitEventEnd7_3Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("73");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Mirror Trading"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Close Trade"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.HEDGING.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
//        );
//
//        assertThat("Verify that there is 1 restriction", clientsRestrictions.size(), equalTo(1));
//
//
//        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
//                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Trading account is a mirror abuser (full rule) but without existing mirror trades", "APPLIED");
//
//        assertThat("Verify that the restriction is as expected", clientsRestrictions.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }

    @Disabled("Disabled on production")
    @Test
    @DisplayName("Mirror trading rule exit Event_End_74")
    @AllureId("175")
    void mirrorTradeRuleExitEventEnd7_4Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("7_4");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Mirror Trading"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Close Trade"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.HEDGING.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.hedgingClone, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat("Verify that there is 1 restriction", clientsRestrictionGenerals.size(), equalTo(1));


        ClientsRestrictionGeneral expectedRestrictionWithdrawal = new ClientsRestrictionGeneral(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Trading account is a mirror abuser (full rule) but without existing mirror trades", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictionGenerals.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }

    @Disabled("Disabled on production")
    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_5")
    @AllureId("174")
    void mirrorTradeRuleExitEventEnd7_5Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("75");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Mirror Trading"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Close Trade"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.HEDGING.getKey()));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.hedgingClone, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat("Verify that there is 1 restriction", clientsRestrictionGenerals.size(), equalTo(1));


        ClientsRestrictionGeneral expectedRestrictionWithdrawal = new ClientsRestrictionGeneral(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Trading account is a mirror abuser (full rule) but without existing mirror trades", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictionGenerals.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }
}
