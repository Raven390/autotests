package tests.rule_engine_service_tests;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientsRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.rules.RuleDataHelper;
import helpers.database.DbName;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.market_manipulation_rule.MarketManipulationRuleDataFactory.deleteMarketManipulationRuleData;
import static helpers.data.rules.market_manipulation_rule.MarketManipulationRuleDataFactory.setupMarketManipulationRuleData;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

// TODO CHECK WHY ALERTS COME LATE
@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_MARKET_MANIPULATION_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_SERVICE)
public class MarketManipulationRuleTest extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException, ReflectiveOperationException, SQLException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMarketManipulationRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteMarketManipulationRuleData(dbDataMap);
    }

    @Test
    @DisplayName("Market manipulation rule exit 5. Profit < 2500")
    @AllureId("1032")
    public void marketManipulationRuleExitEventEnd5Test() throws Exception {
        Allure.step("Profit <= 2500");
        RuleDataHelper data = dbDataMap.get("5");
        System.out.println(data.clientHelper.getUcid());
        System.out.println(data.clientHelper.getServerId());
        System.out.println(data.clientHelper.getTradingAccount());
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);
        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Disabled("Temorarily disabled")
    @Test
    @DisplayName("Market manipulation rule exit 1. Equity <= 2500")
    @AllureId("952")
    public void marketManipulationRuleExitEventEnd1Test() throws Exception {
        Allure.step("Equity <= 2500");
        RuleDataHelper data = dbDataMap.get("1");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

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
    @DisplayName("Market manipulation rule exit 2v1")
    @AllureId("953")
    public void marketManipulationRuleExitEventEnd2v1Test() throws Exception {
        Allure.step("Equity > 2500");
        Allure.step("Low toxicity");
        Allure.step("No PNL data");
        Allure.step("No connections");
        RuleDataHelper data = dbDataMap.get("2v1");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

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
    @DisplayName("Market manipulation rule exit 2v2")
    @AllureId("954")
    public void marketManipulationRuleExitEventEnd2v2Test() throws Exception {
        Allure.step("Equity > 2500");
        Allure.step("Low toxicity");
        Allure.step("PnL <= 2500 for all symbols for last 30 min");
        Allure.step("Connection with no PNL data");
        RuleDataHelper data = dbDataMap.get("2v2");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

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
    @DisplayName("Market manipulation rule exit 3")
    @AllureId("956")
    public void marketManipulationRuleExitEventEnd3Test() throws Exception {
        Allure.step("Equity > 2500");
        Allure.step("Low toxicity");
        Allure.step("PnL > 2500 for any symbol for last 30 min");
        Allure.step("Pnl for symbol > 80% of total pnl");
        Allure.step(">0 fast trades");
        Allure.step("Set manual withdrawal");
        Allure.step("Post alert");
        RuleDataHelper data = dbDataMap.get("3");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify amount of alerts in kafka", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo(data.closeTradeEvent.type));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("MARKET_MANIPULATION"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("HFT MM confirmed"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
//        );
//
//        assertThat("Verify amount of restrictions", clientsRestrictions.size(), equalTo(1));
//
//        ClientsRestriction restriction = clientsRestrictions.getFirst();
//        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "marketManipulation_rule_set_manual_withdrawal_2", "APPLIED");
//
//        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }

    @Test
    @DisplayName("Market manipulation rule exit 4v1")
    @AllureId("957")
    public void marketManipulationRuleExitEventEnd4v1Test() throws Exception {
        Allure.step("Equity > 2500");
        Allure.step("Low toxicity");
        Allure.step("PnL <= 2500 for any symbol for last 30 min");
        Allure.step("Connection with PnL > 2500 for any symbol for last 30 min");
        Allure.step("Pnl for symbol <= 80% of total pnl");
        Allure.step("Trade opened or closed at 20-21, 23, or 0-8");
        Allure.step("LN riskRating high for registration");
        Allure.step("Connection with type 'Same Person' is present");
        Allure.step("Min trade date > now - 2 weeks");
        Allure.step("Min diff between trade date and poi completion / first deposit time >= 1 week");
        Allure.step("First deposit actualAmountUSD < 10000");
        Allure.step("Symbol in list of risky symbols");
        Allure.step("Some of LN data is with location CN, HK, MY, VN, VI");
        Allure.step("Dummy trades true");
        Allure.step("2 deposits in a row with second at least 4x amount from first");
        Allure.step("Crypto >= 90% of deposits");
        Allure.step(">2 Ips or >2 devices in LN");
        Allure.step("Credit < 500");
        Allure.step("B-Book -> A-Book restriction");
        Allure.step("Withdrawal restriction");
        Allure.step("Alert");

        RuleDataHelper data = dbDataMap.get("4v1");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify amount of alerts in kafka", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo(data.closeTradeEvent.type));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("MARKET_MANIPULATION"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("MM confirmed"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
//        );
//
//        assertThat("Verify amount of restrictions", clientsRestrictions.size(), equalTo(1));
//
//        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 4L, "Market Manipulator middle risk", "APPLIED");
//        ClientsRestriction expectedRestriction1 = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 7L, "Market_manipulation_a-book_restriction", "APPLIED"); TODO enable when it becomes active in BO
//
//        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction));
    }

    @Test
    @DisplayName("Market manipulation rule exit 4v2")
    @AllureId("958")
    public void marketManipulationRuleExitEventEnd4v2Test() throws Exception {
        Allure.step("Equity > 2500");
        Allure.step("Low toxicity");
        Allure.step("PnL > 2500 for any symbol for last 30 min");
        Allure.step("Pnl for symbol > 80% of total pnl");
        Allure.step("0 fast trades");
        Allure.step("Trade opened or closed at 20-21, 23, or 0-8");
        Allure.step("LN riskRating low for registration");
        Allure.step("Connection with type 'Same Person' is present");
        Allure.step("Min trade date > now - 2 weeks");
        Allure.step("Min diff between trade date and poi completion / first deposit time < 1 week");
        Allure.step("First deposit actualAmountUSD > 10000");
        Allure.step("No Symbol in list of risky symbols");
        Allure.step("No LN data with location CN, HK, MY, VN, VI");
        Allure.step("Dummy trades false");
        Allure.step("No 2 deposits in a row with second at least 4x amount from first");
        Allure.step("Crypto < 90% of deposits");
        Allure.step("<=2 Ips or <=2 devices in LN");
        Allure.step("Credit >= 500");
        Allure.step("Off quotes restriction");
        Allure.step("Manual withdrawal restriction");
        Allure.step("Alert");

        RuleDataHelper data = dbDataMap.get("4v2");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify amount of alerts in kafka", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo(data.closeTradeEvent.type));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("MARKET_MANIPULATION"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("MM confirmed"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
//        );
//
//        assertThat("Verify amount of restrictions", clientsRestrictions.size(), equalTo(1));
//
//        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "marketManipulation_rule_set_manual_withdrawal_2", "APPLIED");
//        ClientsRestriction expectedRestriction1 = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 11L, "Market Manipulator Off-Quote Restriction", "APPLIED"); TODO enable when it becomes active in BO
//
//        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction));
    }

    @Test
    @DisplayName("Market manipulation rule exit 4v3")
    @AllureId("959")
    public void marketManipulationRuleExitEventEnd4v3Test() throws Exception {
        Allure.step("Equity > 2500");
        Allure.step("High toxicity");
        Allure.step("Trade opened or closed at 20-21, 23, or 0-8");
        Allure.step("LN riskRating low for registration");
        Allure.step("No Connection with type 'Same Person' is present");
        Allure.step("Min trade date < now - 2 weeks");
        Allure.step("Min diff between trade date and poi completion / first deposit time < 1 week");
        Allure.step("First deposit actualAmountUSD > 10000");
        Allure.step("No Symbol in list of risky symbols");
        Allure.step("No LN data with location CN, HK, MY, VN, VI");
        Allure.step("Dummy trades false");
        Allure.step("No 2 deposits in a row with second at least 4x amount from first");
        Allure.step("Crypto < 90% of deposits");
        Allure.step("<=2 Ips or <=2 devices in LN");
        Allure.step("Credit >= 500");
        Allure.step("Manual withdrawal restriction");
        Allure.step("Alert");

        RuleDataHelper data = dbDataMap.get("4v3");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify amount of alerts in kafka", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name not null", alert.rule.name, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo(data.closeTradeEvent.type));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("MARKET_MANIPULATION"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes stepName is correct", alert.rule.attributes.stepName, equalTo("MM confirmed"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
//        );
//
//        assertThat("Verify amount of restrictions", clientsRestrictions.size(), equalTo(1));
//
//        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 8L, "marketManipulation_rule_set_manual_withdrawal_2", "APPLIED");
//
//        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction));
    }
}
