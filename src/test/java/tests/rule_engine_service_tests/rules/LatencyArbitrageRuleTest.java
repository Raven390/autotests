package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.DataHelper;
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
import static helpers.data.rules.latency_arbitrage_rule.LatencyArbitrageRuleDataFactory.*;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_LATENCY_ARBITRAGE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class LatencyArbitrageRuleTest extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
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
    @DisplayName("Latency arbitrage rule rule. Exit without alert if platform is not MT5. ElementId: Event_0qxj50n")
    void latencyArbitrageRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Latency Arbitrage");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1373")
    @DisplayName("Latency arbitrage rule. Exit without alert if user is test or social trader user. ElementId: Event_end_1")
    void latencyArbitrageRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Latency Arbitrage");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1373")
    @DisplayName("Latency arbitrage rule. Exit without alert if user has less that 10 trading days. ElementId: Event_088xwgg")
    void latencyArbitrageRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Latency Arbitrage");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1374")
    @DisplayName("Latency arbitrage rule. Exit without alert if user has less than 100 trades. ElementId: Event_002l07f")
    void latencyArbitrageRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Latency Arbitrage");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1375")
    @DisplayName("Latency arbitrage rule. Exit without alert if netProfit + rebatesAmount not >= 500. ElementId: Event_1mf0xpo")
    void latencyArbitrageRuleTest5() throws Exception {
        DataHelper data = dbDataMap.get("5");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Latency Arbitrage");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1375")
    @DisplayName("Latency arbitrage rule. Exit without alert if Total Profit / Cumulative deposit not >= 0.3. ElementId: Event_10836lw")
    void latencyArbitrageRuleTest6() throws Exception {
        DataHelper data = dbDataMap.get("6");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Latency Arbitrage");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Disabled("Not implemented")
    @Test
    @AllureId("1376")
    @DisplayName("Latency arbitrage rule. Exit without alert if shortToxicity / ((netProfit + rebatesAmount) * 100) not >= 80. ElementId: Event_0l87tr0")
    void latencyArbitrageRuleTest7() throws Exception {
        DataHelper data = dbDataMap.get("7");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Latency Arbitrage");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }
}
