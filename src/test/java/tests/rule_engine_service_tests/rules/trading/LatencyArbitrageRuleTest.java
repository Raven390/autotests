package tests.rule_engine_service_tests.rules.trading;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
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
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        assertThat("Check ucid", clientGeneralRestrictions.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Check regulator", clientGeneralRestrictions.getFirst().getRegulator(), is(data.clientHelper.getRegulator()));
        assertThat("Check restrictionId", clientGeneralRestrictions.getFirst().getRestrictionId(), is(8L));
        assertThat("Check comment", clientGeneralRestrictions.getFirst().getComment(), is("Lattency arbitrage pattern"));
        assertThat("Check status", clientGeneralRestrictions.getFirst().getStatus(), is("APPLIED"));
    }

    @Disabled
    @Test
    @AllureId("1374")
    @DisplayName("")
    void latencyArbitrageRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);
    }

    @Disabled
    @Test
    @AllureId("1375")
    @DisplayName("")
    void latencyArbitrageRuleTest5() throws Exception {
        DataHelper data = dbDataMap.get("5");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

    }

    @Disabled
    @Test
    @AllureId("1375")
    @DisplayName("")
    void latencyArbitrageRuleTest6() throws Exception {
        DataHelper data = dbDataMap.get("6");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

    }

    @Disabled("Not implemented")
    @Test
    @AllureId("1376")
    @DisplayName("")
    void latencyArbitrageRuleTest7() throws Exception {
        DataHelper data = dbDataMap.get("7");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

    }
}
