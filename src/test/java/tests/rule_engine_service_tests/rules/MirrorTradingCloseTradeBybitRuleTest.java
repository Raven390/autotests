package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.mirror_trading_close_trade_event_bybit_rule.MirrorTradingCloseTradeEventBybitRuleDataFactory.setupMirrorTradingCloseTradeBybitRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_MIRROR_TRADING_CLOSE_TRADE_BYBIT_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MirrorTradingCloseTradeBybitRuleTest extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingCloseTradeBybitRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteRuleData(dbDataMap);
    }

    @Test
    @AllureId("1445")
    @DisplayName("Mirror trading Bybit. Exit without alert if user has no credits. Element id Event_end_3")
    void mirrorTradeBybitRuleTest1() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1446")
    @DisplayName("Mirror trading Bybit. ML model. Exit without alert trades amount < 10. Element id Event_0vlh2iw")
    void mirrorTradeBybitRuleTest2() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1447")
    @DisplayName("Mirror trading Bybit. ML model. Exit without alert trades amount > 200. Element id Event_0vlh2iw")
    void mirrorTradeBybitRuleTest3() throws Exception {
        RuleDataHelper data = dbDataMap.get("3");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1448")
    @DisplayName("Mirror trading Bybit. ML model. Exit without alert if score < 0.9. Element id Event_1n666vd")
    void mirrorTradeBybitRuleTest4() throws Exception {
        RuleDataHelper data = dbDataMap.get("4");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1449")
    @DisplayName("Mirror trading Bybit. ML model. Exit without alert no resolved alerts. Element id Event_1he561b")
    void mirrorTradeBybitRuleTest5() throws Exception {
        RuleDataHelper data = dbDataMap.get("5");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1450")
    @DisplayName("Mirror trading Bybit. ML model. Exit with alert if  resolved alerts>0. Element id Event_1he561b")
    void mirrorTradeBybitRuleTest6() throws Exception {
        RuleDataHelper data = dbDataMap.get("6");

        produceTradeMessageToKafka(data.tradeEvent);
        Thread.sleep(10_000);

        //Verify alerts
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
    }

    @Test
    @AllureId("1451")
    @DisplayName("Mirror trading Bybit. Exit without alerts if deposits > 5000. Element id Event_end_2")
    void mirrorTradeBybitRuleTest7() throws Exception {
        RuleDataHelper data = dbDataMap.get("7");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1452")
    @DisplayName("Mirror trading Bybit. Exit without alerts if count trades > 300. Element id Event_end_2")
    void mirrorTradeBybitRuleTest8() throws Exception {
        RuleDataHelper data = dbDataMap.get("8");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1453")
    @DisplayName("Mirror trading Bybit. Scotland. No alerts if trades count > 5. ElementId : Event_1cl2uhs")
    void mirrorTradeBybitRuleTest9() throws Exception {
        RuleDataHelper data = dbDataMap.get("9");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1454")
    @DisplayName("Mirror trading Bybit. Scotland. No alerts if profit/(deposit+credit) < 0.6. ElementId: Event_1cl2uhs")
    void mirrorTradeBybitRuleTest10() throws Exception {
        RuleDataHelper data = dbDataMap.get("10");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1455")
    @DisplayName("Mirror trading Bybit. Scotland. No alerts if leverage < 200")
    void mirrorTradeBybitRuleTest11() throws Exception {
        RuleDataHelper data = dbDataMap.get("11");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1456")
    @DisplayName("Mirror trading Bybit. Scotland. Exit with alert if leverage > 200 and no resolved alerts. Element id : Event_end_4")
    void mirrorTradeBybitRuleTest12() throws Exception {
        RuleDataHelper data = dbDataMap.get("12");

        produceTradeMessageToKafka(data.tradeEvent);
        Thread.sleep(30_000);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify rule name in alert", alerts.getFirst().rule.name, is("Mirror Trading"));
        assertThat("Verify rule reason in alert", alerts.getFirst().rule.attributes.reason, is("Mirror trade pattern"));
        assertThat("Verify symbol traded in alert", alerts.getFirst().rule.attributes.symbolTraded, is(EURUSD));
        assertThat("Verify serverId in alert", alerts.getFirst().rule.attributes.serverId, is(data.clientHelper.getServerId()));
        assertThat("Verify ticker id in alert", alerts.getFirst().rule.attributes.ticketId, is(String.valueOf(data.tradeEvent.tradeId)));
        assertThat("Verify trading account in alert", alerts.getFirst().rule.attributes.account, is(String.valueOf(data.clientHelper.getTradingAccount())));
        assertThat("Verify ucid in alert", alerts.getFirst().ucid, is(data.clientHelper.getUcid()));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
    }

    @Test
    @AllureId("1498")
    @DisplayName("Mirror trading Bybit. Scotland. Exit without alert if leverage > 200 and >0 resolved alerts. Element id : Event_end_4")
    void mirrorTradeBybitRuleTest13() throws Exception {
        RuleDataHelper data = dbDataMap.get("13");

        produceTradeMessageToKafka(data.tradeEvent);
        Thread.sleep(30_000);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Disabled("Not implemented")
    @Test
    @AllureId("1457")
    @DisplayName("Mirror trading Bybit. Waves. Exit without alert if there is no pattern. Element id Event_end_9")
    void mirrorTradeBybitRuleTest14() throws Exception {
        RuleDataHelper data = dbDataMap.get("14");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Disabled("Not implemented")
    @Test
    @AllureId("1458")
    @DisplayName("Mirror trading Bybit. Waves. Exit without alert if there is pattern and resolved alerts>0. Element id Event_0w3j9pm")
    void mirrorTradeBybitRuleTest15() throws Exception {
        RuleDataHelper data = dbDataMap.get("15");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Disabled("Not implemented")
    @Test
    @AllureId("1459")
    @DisplayName("Mirror trading Bybit. Waves. Exit with alert if there is pattern and resolved alerts=0. Element id Event_0w3j9pm")
    void mirrorTradeBybitRuleTest16() throws Exception {
        RuleDataHelper data = dbDataMap.get("16");

        produceTradeMessageToKafka(data.tradeEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
    }

}
