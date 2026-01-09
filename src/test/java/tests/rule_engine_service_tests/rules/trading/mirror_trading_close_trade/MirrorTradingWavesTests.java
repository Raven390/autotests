package tests.rule_engine_service_tests.rules.trading.mirror_trading_close_trade;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.WaveFlagInserter.deleteWaveFlagData;
import static helpers.data.rules.trading.mirror_trading_close_trade.MirrorTradingWavesDataFactory.setupMirrorTradingWavesRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.DataHelper;
import io.qameta.allure.Allure;
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
@Story(STORY_RULE_ENGINE_MIRROR_TRADING_CLOSE_TRADE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MirrorTradingWavesTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException, InterruptedException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingWavesRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dbDataMap);
        deleteWaveFlagData(dbDataMap.get("2").clientHelper);
        deleteWaveFlagData(dbDataMap.get("3").clientHelper);
    }

    @Disabled
    @Test
    @AllureId("1620")
    @DisplayName("Mirror trading. Waves. Exit without alerts if pattern not matched. ElementId: Event_end_9")
    void mirrorTradeRuleTest14() throws Exception {
        DataHelper data = dbDataMap.get("1");
        setupData(data);

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_9", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Disabled
    @Test
    @AllureId("1619")
    @DisplayName(
            "Mirror trading. Waves. Exit without alerts if previously at least 1 resolved alert. ElementId: Event_end_5")
    void mirrorTradeRuleTest15() throws Exception {
        DataHelper data = dbDataMap.get("2");
        setupData(data);

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_5", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Disabled
    @Test
    @AllureId("1618")
    @DisplayName("Mirror trading. Waves. Exit with alerts if previously 0 resolved alerts. ElementId: Event_end_5")
    void mirrorTradeRuleTest16() throws Exception {
        DataHelper data = dbDataMap.get("3");
        setupData(data);

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_5", data.closeTradeMtEvent.id, "mirror_trade");

        // Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat(
                "Verify alert",
                alerts.getFirst().timestamp,
                matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().alertId, is(data.closeTradeMtEvent.id));
        assertThat("Verify alert", alerts.getFirst().type, is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().ucid, is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(data.closeTradeMtEvent.eventDate));

        assertThat("Verify alert", alerts.getFirst().rule.name, is("Mirror Trading"));
        assertThat("Verify alert", alerts.getFirst().rule.fraudType, is("HEDGING"));
        assertThat("Verify alert", alerts.getFirst().rule.trigger, is("Close Trade"));
        assertThat("Verify alert", alerts.getFirst().rule.ver, notNullValue());

        assertThat(
                "Verify alert",
                alerts.getFirst().rule.attributes.reason,
                is("The client hides the fraud inside several waves"));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.symbolTraded, is(data.closeTradeMtEvent.symbol));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.serverId, is(data.closeTradeMtEvent.serverId));
        assertThat(
                "Verify alert",
                alerts.getFirst().rule.attributes.ticketId,
                is(String.valueOf(data.closeTradeMtEvent.tradeId)));
        assertThat(
                "Verify alert",
                alerts.getFirst().rule.attributes.account,
                is(String.valueOf(data.closeTradeMtEvent.tradingAccount)));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        checkManualWithdrawalRestrictionApplied(data.clientHelper, "The client hides the fraud inside several waves");
    }
}
