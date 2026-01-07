package tests.rule_engine_service_tests.rules.trading.mirror_trading_close_trade;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.rules.trading.mirror_trading_close_trade.MirrorTradingMlModelDataFactory.setupMirrorTradingMLModelRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.DataDeleteHelper;
import helpers.data.DataHelper;
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
class MirrorTradingMlModelTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingMLModelRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataDeleteHelper.deleteData(dbDataMap);
    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Exit without alert if tades < 5. ElementId: Event_0vlh2iw")
    void mirrorTradeRuleTest1() throws Exception {}

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Exit without alert if tades > 200. ElementId: Event_0vlh2iw")
    void mirrorTradeRuleTest2() throws Exception {}

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Exit without alert if ucid score < 0.9. ElementId: Event_1n666vd")
    void mirrorTradeRuleTest3() throws Exception {}

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Exit if at lease 1 resolved alert. ElementId: Event_1m3mqdr")
    void mirrorTradeRuleTest4() throws Exception {}

    @Disabled
    @Test
    @AllureId("1494")
    @DisplayName(
            "Mirror trading. Ml model. Post alert and restriction if no previously resolved alerts. ElementId: Event_1m3mqdr")
    void mirrorTradeRuleTest5() throws Exception {
        DataHelper data = dbDataMap.get("5");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_1m3mqdr", data.closeTradeMtEvent.id, "mirror_trade");

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
                is("ML Model suspects the client of Mirror Trading"));
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

        // Verify restriction
        checkManualWithdrawalRestrictionApplied(data.clientHelper, "ML Model suspects the client of Mirror Trading");

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
    }
}
