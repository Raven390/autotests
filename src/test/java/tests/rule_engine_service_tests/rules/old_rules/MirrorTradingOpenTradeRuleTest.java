package tests.rule_engine_service_tests.rules.old_rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.mirror_trading_open_trade_event_rule.mirror_trading_close_trade_event_rule.MirrorTradingOpenTradeEventDataFactory.deleteMirrorTradingOpenTradeEventRuleData;
import static helpers.data.rules.mirror_trading_open_trade_event_rule.mirror_trading_close_trade_event_rule.MirrorTradingOpenTradeEventDataFactory.setupMirrorTradingOpenTradeEventRuleData;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_MIRROR_TRADE_OPEN_TRADE_EVENT_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MirrorTradingOpenTradeRuleTest extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException, SQLException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingOpenTradeEventRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteMirrorTradingOpenTradeEventRuleData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("1385")
    @DisplayName("Mirror trading with open trade event. Exit without alert if user is test or social trader user")
    void mirrorTradingOpenTradeEventRuleTest1() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");

        produceTradeMessageToKafka(data.tradeEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @DisplayName("Mirror trading with open trade event. Exit without alert if user doesn't have a credit")
    void mirrorTradingOpenTradeEventRuleTest2() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");

        produceTradeMessageToKafka(data.tradeEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @DisplayName("Mirror trading with open trade event. Exit without alert if trades count < 5")
    void mirrorTradingOpenTradeEventRuleTest3() throws Exception {
        RuleDataHelper data = dbDataMap.get("3");

        produceTradeMessageToKafka(data.tradeEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @DisplayName("Mirror trading with open trade event. Exit without alert if trades count > 200")
    void mirrorTradingOpenTradeEventRuleTest4() throws Exception {
        RuleDataHelper data = dbDataMap.get("4");

        produceTradeMessageToKafka(data.tradeEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @DisplayName("Mirror trading with open trade event. Exit without alert if ucidScore < 0.9")
    void mirrorTradingOpenTradeEventRuleTest5() throws Exception {
        RuleDataHelper data = dbDataMap.get("5");

        produceTradeMessageToKafka(data.tradeEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @DisplayName("Mirror trading with open trade event. Exit without alert user have at least 1 resolved alerts")
    void mirrorTradingOpenTradeEventRuleTest6() throws Exception {
        RuleDataHelper data = dbDataMap.get("6");

        produceTradeMessageToKafka(data.tradeEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @DisplayName("Mirror trading with open trade event. Exit with alert and restriction if there was no previous alerts")
    void mirrorTradingOpenTradeEventRuleTest7() throws Exception {
        RuleDataHelper data = dbDataMap.get("7");

        produceTradeMessageToKafka(data.tradeEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify rule name in alert", alerts.getFirst().rule.name, is("Mirror Trading"));
        assertThat("Verify rule reason in alert", alerts.getFirst().rule.attributes.reason, is("ML Model suspects the client of Mirror Trading"));
        assertThat("Verify symbol traded in alert", alerts.getFirst().rule.attributes.symbolTraded, is(EURUSD));
        assertThat("Verify serverId in alert", alerts.getFirst().rule.attributes.serverId, is(data.clientHelper.getServerId()));
        assertThat("Verify ticker id in alert", alerts.getFirst().rule.attributes.ticketId, is(String.valueOf(data.tradeEvent.tradeId)));
        assertThat("Verify trading account in alert", alerts.getFirst().rule.attributes.account, is(String.valueOf(data.clientHelper.getTradingAccount())));
        assertThat("Verify ucid in alert", alerts.getFirst().ucid, is(data.clientHelper.getUcid()));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));


        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        assertThat("Check ucid", clientGeneralRestrictions.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Check regulator", clientGeneralRestrictions.getFirst().getRegulator(), is(data.clientHelper.getRegulator()));
        assertThat("Check restrictionId", clientGeneralRestrictions.getFirst().getRestrictionId(), is(8L));
        assertThat("Check comment", clientGeneralRestrictions.getFirst().getComment(), is("ML Model suspects the client of Mirror Trading"));
        assertThat("Check status", clientGeneralRestrictions.getFirst().getStatus(), is("APPLIED"));
    }
}
