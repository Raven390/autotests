package tests.rule_engine_service_tests.rules.trading;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.DataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.rules.trading.LatencyArbitrageRuleDataFactory.setupLatencyArbitrageData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_LATENCY_ARBITRAGE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class LatencyArbitrageRuleTest extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws Exception {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupLatencyArbitrageData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("1372")
    @DisplayName("Latency arbitrage rule rule. Exit without alert if user is a test/st user")
    void latencyArbitrageRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_1", data.closeTradeMtEvent.id, "latency_arbitrage");
    }

    @Test
    @AllureId("1373")
    @DisplayName("Latency arbitrage rule. Exit without alert if user has resolved alerts. ElementId: Event_end_12")
    void latencyArbitrageRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_12", data.closeTradeMtEvent.id, "latency_arbitrage");
    }

    @Test
    @AllureId("1373")
    @DisplayName("Latency arbitrage rule. Exit without alert if user has less that 10 trading days. ElementId: Event_088xwgg")
    void latencyArbitrageRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_0782vj1", data.closeTradeMtEvent.id, "latency_arbitrage");

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Latency Arbitrage");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().timestamp, matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().alertId, is(data.closeTradeMtEvent.id));
        assertThat("Verify alert", alerts.getFirst().type, is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().ucid, is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(data.closeTradeMtEvent.eventDate));

        assertThat("Verify alert", alerts.getFirst().rule.name, is("Latency Arbitrage"));
        assertThat("Verify alert", alerts.getFirst().rule.fraudType, is("LATENCY_ARBITRAGE"));
        assertThat("Verify alert", alerts.getFirst().rule.trigger, is("Close Trade"));
        assertThat("Verify alert", alerts.getFirst().rule.ver, notNullValue());

        assertThat("Verify alert", alerts.getFirst().rule.attributes.reason, is("Detected suspicious Latency Arbitrage pattern"));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.symbolTraded, is(data.closeTradeMtEvent.symbol));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.serverId, is(data.closeTradeMtEvent.serverId));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.ticketId, is(String.valueOf(data.closeTradeMtEvent.tradeId)));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.account, is(String.valueOf(data.closeTradeMtEvent.tradingAccount)));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        // Verify restriction
        checkManualWithdrawalRestrictionApplied(data.clientHelper, "Lattency arbitrage pattern");
    }

    @Test
    @AllureId("1374")
    @DisplayName("Latency arbitrage rule. Rebate Latency Branch. Total Profit / Cumulative deposit =< 0.2. ElementId: Event_1jau96v")
    void latencyArbitrageRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_1jau96v", data.closeTradeMtEvent.id, "latency_arbitrage");
    }

    @Test
    @AllureId("1375")
    @DisplayName("Latency arbitrage rule. Rebate Latency Branch. rebates(client) / profit(client) < 0.3?. ElementId: Event_0cyuekk")
    void latencyArbitrageRuleTest5() throws Exception {
        DataHelper data = dbDataMap.get("5");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_0cyuekk", data.closeTradeMtEvent.id, "latency_arbitrage");
    }

    @Test
    @DisplayName("Latency arbitrage rule. notionalValue(ucid) < 10 000 000. ElementId: Event_04k2uu4")
    void latencyArbitrageRuleTest6() throws Exception {
        DataHelper data = dbDataMap.get("6");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_04k2uu4", data.closeTradeMtEvent.id, "latency_arbitrage");
    }

    @Test
    @AllureId("1376")
    @DisplayName("Latency arbitrage rule. max(maxNotionalValue.maxDailyNotionalValueUSD) / sum(notionalValue.notionalValueAmountUSD) > 0.6. ElementId: Event_0byoyft")
    void latencyArbitrageRuleTest7() throws Exception {
        DataHelper data = dbDataMap.get("7");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_0byoyft", data.closeTradeMtEvent.id, "latency_arbitrage");
    }

    @Test
    @AllureId("1657")
    @DisplayName("Latency arbitrage rule. Rebate Latency Branch. At least 1 resolved Alert. ElementId: Event_0u8x3op")
    void latencyArbitrageRuleTest8() throws Exception {
        DataHelper data = dbDataMap.get("8");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_0u8x3op", data.closeTradeMtEvent.id, "latency_arbitrage");
    }

    @Test
    @AllureId("1658")
    @DisplayName("Latency arbitrage rule. Rebate Latency Branch. No resolved Alerts. ElementId: Event_04a1zpc")
    void latencyArbitrageRuleTest9() throws Exception {
        DataHelper data = dbDataMap.get("9");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_04a1zpc", data.closeTradeMtEvent.id, "latency_arbitrage");

        Allure.step("Verify there is alert in kafka");
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Latency Arbitrage");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().timestamp, matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().alertId, is(data.closeTradeMtEvent.id));
        assertThat("Verify alert", alerts.getFirst().type, is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().ucid, is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(data.closeTradeMtEvent.eventDate));

        assertThat("Verify alert", alerts.getFirst().rule.name, is("Latency Arbitrage"));
        assertThat("Verify alert", alerts.getFirst().rule.fraudType, is("LATENCY_ARBITRAGE"));
        assertThat("Verify alert", alerts.getFirst().rule.trigger, is("Close Trade"));
        assertThat("Verify alert", alerts.getFirst().rule.ver, notNullValue());

        assertThat("Verify alert", alerts.getFirst().rule.attributes.reason, is("Detected suspicious Latency Arbitrage pattern + Rebate abuse"));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.symbolTraded, is(data.closeTradeMtEvent.symbol));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.serverId, is(data.closeTradeMtEvent.serverId));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.ticketId, is(String.valueOf(data.closeTradeMtEvent.tradeId)));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.account, is(String.valueOf(data.closeTradeMtEvent.tradingAccount)));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        // Verify restriction
        checkManualWithdrawalRestrictionApplied(data.clientHelper, "Lattency arbitrage pattern");

    }
}
