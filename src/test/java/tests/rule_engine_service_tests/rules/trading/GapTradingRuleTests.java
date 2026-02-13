package tests.rule_engine_service_tests.rules.trading;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.AlertsAssertsHelper.assertThatAlertNotFailed;
import static helpers.asserts.AlertsAssertsHelper.checkTradingAlert;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.trading.GapTradingRuleDataFactory.setupGapTradingRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataDeleteHelper;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Rule;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_GAP_TRADING_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class GapTradingRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupGapTradingRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataDeleteHelper.deleteData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("2018")
    @DisplayName("Gap trading rule. Exit without alert if user is test or social trader user. ElementId: endEvent1")
    void gapTradingOpenTradeEventRuleTest1() throws Exception {
        runGapTradingRuleTest("1", "endEvent1");
    }

    @Test
    @AllureId("2017")
    @DisplayName("Gap trading rule. Exit without alert if market does not close in 30 min. ElementId: endEvent2")
    void gapTradingOpenTradeEventRuleTest2() throws Exception {
        runGapTradingRuleTest("2", "endEvent2");
    }

    @Test
    @AllureId("2019")
    @DisplayName("Gap trading rule. Exit without alert if user has no open trades. ElementId: endEvent3")
    void gapTradingOpenTradeEventRuleTest3() throws Exception {
        runGapTradingRuleTest("3", "endEvent3");
    }

    @Test
    @AllureId("2020")
    @DisplayName("Gap trading rule. Exit without alert if leverage is low. ElementId: endEvent4")
    void gapTradingOpenTradeEventRuleTest4() throws Exception {
        runGapTradingRuleTest("4", "endEvent4");
    }

    @Test
    @AllureId("2036")
    @DisplayName("Gap trading rule. Exit without alert if growth is low. ElementId: endEvent5")
    void gapTradingOpenTradeEventRuleTest5() throws Exception {
        runGapTradingRuleTest("5", "endEvent5");
    }

    @Test
    @AllureId("2037")
    @DisplayName("Gap trading rule. Exit without alert if equity is not enough. ElementId: endEvent6")
    void gapTradingOpenTradeEventRuleTest6() throws Exception {
        runGapTradingRuleTest("6", "endEvent6");
    }

    @Test
    @AllureId("2038")
    @DisplayName(
            "Gap trading rule. Exit without alert if account isn't running near its daily peak exposure. ElementId: endEvent7")
    void gapTradingOpenTradeEventRuleTest7() throws Exception {
        runGapTradingRuleTest("7", "endEvent7");
    }

    @Test
    @AllureId("2039")
    @DisplayName(
            "Gap trading rule. Exit without alert if trades aren't represented by one symbol for >80%. ElementId: endEvent8")
    void gapTradingOpenTradeEventRuleTest8() throws Exception {
        runGapTradingRuleTest("8", "endEvent8");
    }

    @Test
    @AllureId("2037")
    @DisplayName("Gap trading rule. Exit with alert. ElementId: endEvent")
    void gapTradingOpenTradeEventRuleTestAlert() throws Exception {
        runGapTradingRuleTest("alert", "endEvent");

        DataHelper data = dbDataMap.get("alert");
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.getClientHelper(), "Gap trading");
        checkTradingAlert(
                data,
                alerts,
                "Client has open trades with " + data.getTradeEvent().symbol
                        + " and has open positions after market close",
                "GAP_TRADING",
                "Open trade",
                "Gap trading");

        RuleAlertV2 alert = alerts.getFirst();
        assertThat(
                "Attributes.tradeId should match tradeEvent.tradeId",
                alert.getAttributes().getTicketId(),
                is(String.valueOf(data.getTradeEvent().getTradeId())));
        assertThatAlertNotFailed(alert.getUcid(), "Gap trading");
    }

    private void runGapTradingRuleTest(String dataKey, String expectedElementId) throws Exception {

        DataHelper data = dbDataMap.get(dataKey);
        setupData(data);

        produceTradeMessageToKafka(data.getTradeEvent());

        checkElementId(expectedElementId, data.getTradeEvent().getId(), Rule.GAP_TRADING.getProcessId(), 2);
    }
}
