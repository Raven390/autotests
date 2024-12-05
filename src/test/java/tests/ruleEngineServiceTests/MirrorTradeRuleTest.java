package tests.ruleEngineServiceTests;

import businessObjects.db.backofficeDb.alert.Alert;
import businessObjects.db.mitigationServiceDb.ClientsRestriction;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.rules.mirrorTradingRule.MirrorTradingRuleData;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.mitigationService.MitigationServiceRequest.disableCRMEmulator;
import static businessObjects.api.mitigationService.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.mirrorTradingRule.MirrorTradingRuleDataFactory.*;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_MIRROR_TRADING_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_SERVICE)
@Tag(TAG_MANUAL)
@Disabled
@Muted
public class MirrorTradeRuleTest {

    public static Map<String, MirrorTradingRuleData> dbDataMap = new HashMap<>();

    @BeforeAll
    public static void setupDbData() throws ReflectiveOperationException, SQLException, IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingRuleData();
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_2")
    @AllureId("187")
    public void mirrorTradeRuleExitEventEnd2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("2");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
    @DisplayName("Mirror trading rule exit Event_End_3_1")
    @AllureId("185")
    public void mirrorTradeRuleExitEventEnd3_1Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("3_1");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.clones, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestrictionClose = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 6L, "Doppelganger is a mirrorAbuser", "APPLIED");

        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Doppelganger isn't a mirrorAbuser but with bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestrictionClose, expectedRestrictionWithdrawal));
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_3_2")
    @AllureId("186")
    public void mirrorTradeRuleExitEventEnd3_2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("3_2");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.clones, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));

        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Doppelganger isn't a mirrorAbuser but with bonus", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_4_1")
    @AllureId("183")
    public void mirrorTradeRuleExitEventEnd4_1Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("4_1");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
    @DisplayName("Mirror trading rule exit Event_End_4_2")
    @AllureId("184")
    public void mirrorTradeRuleExitEventEnd4_2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("4_2");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
    @DisplayName("Mirror trading rule exit Event_End_5_1")
    @AllureId("181")
    public void mirrorTradeRuleExitEventEnd5_1Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("5_1");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
    @DisplayName("Mirror trading rule exit Event_End_5_2")
    @AllureId("182")
    public void mirrorTradeRuleExitEventEnd5_2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("5_2");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
    @DisplayName("Mirror trading rule exit Event_End_6")
    @AllureId("173")
    public void mirrorTradeRuleExitEventEnd6Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("6");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
    @DisplayName("Mirror trading rule exit Event_End_1_1")
    @AllureId("179")
    public void mirrorTradeRuleExitEventEnd1_1Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("1_1");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.clones, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestrictionClose = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 6L, "Trading account is a mirror abuser (full rule)", "APPLIED");

        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Doppelganger is a mirrorAbuser", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestrictionClose, expectedRestrictionWithdrawal));
    }


    @Test
    @DisplayName("Mirror trading rule exit Event_End_1_2")
    @AllureId("220")
    public void mirrorTradeRuleExitEventEnd1_2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("1_2");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.clones, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there are 2 restrictions", clientsRestrictions.size(), equalTo(2));

        ClientsRestriction expectedRestrictionClose = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 6L, "Trading account is a mirror abuser (full rule)", "APPLIED");

        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Doppelganger is a mirrorAbuser", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestrictionClose, expectedRestrictionWithdrawal));
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_1")
    @AllureId("178")
    public void mirrorTradeRuleExitEventEnd7_1Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("7_1");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.clones, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is 1 restriction", clientsRestrictions.size(), equalTo(1));


        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Trading account is a mirror abuser (full rule) but without existing mirror trades", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_2")
    @AllureId("177")
    public void mirrorTradeRuleExitEventEnd7_2Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("7_2");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.clones, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is 1 restriction", clientsRestrictions.size(), equalTo(1));


        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Trading account is a mirror abuser (full rule) but without existing mirror trades", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_3")
    @AllureId("176")
    public void mirrorTradeRuleExitEventEnd7_3Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("7_3");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.clones, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is 1 restriction", clientsRestrictions.size(), equalTo(1));


        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Trading account is a mirror abuser (full rule) but without existing mirror trades", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_4")
    @AllureId("175")
    public void mirrorTradeRuleExitEventEnd7_4Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("7_4");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.clones, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is 1 restriction", clientsRestrictions.size(), equalTo(1));


        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Trading account is a mirror abuser (full rule) but without existing mirror trades", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }

    @Test
    @DisplayName("Mirror trading rule exit Event_End_7_5")
    @AllureId("174")
    public void mirrorTradeRuleExitEventEnd7_5Test() throws Exception {
        MirrorTradingRuleData data = dbDataMap.get("7_5");
        Allure.step("Produce close trade event to crm-events topic");
        KafkaHelper kafka = new KafkaHelper();
        ObjectMapper objectMapper = new ObjectMapper();
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.closeTradeMtEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("HEDGING"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.clones, notNullValue());

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify that there is 1 restriction", clientsRestrictions.size(), equalTo(1));


        ClientsRestriction expectedRestrictionWithdrawal = new ClientsRestriction(
                data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Trading account is a mirror abuser (full rule) but without existing mirror trades", "APPLIED");

        assertThat("Verify that the restriction is as expected", clientsRestrictions.getFirst(), equalTo(expectedRestrictionWithdrawal));
    }

    @AfterAll
    public static void deleteDbData() throws Exception {
        deleteMirrorTradingRuleData(dbDataMap);
        disableCRMEmulator();
    }
}
