package tests.rule_engine_service_tests.rules.trading.mirror_trading_close_trade;

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
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.rules.trading.mirror_trading_close_trade.MirrorTradingScotlandDataFactory.setupMirrorTradingScotlandRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_MIRROR_TRADING_CLOSE_TRADE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MirrorTradingScotlandTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingScotlandRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dbDataMap);
    }

    @Test
    @AllureId("1432")
    @DisplayName("Mirror trading. Scotland. Exit without alert if trades count > 5. ElementId: Event_end_8")
    void mirrorTradeRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_8", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Test
    @AllureId("1433")
    @DisplayName("Mirror trading. Scotland. Exit without alert if profit/(deposit+credit) < 0.6. ElementId: Event_end_8")
    void mirrorTradeRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_8", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Test
    @AllureId("1434")
    @DisplayName("Mirror trading. Scotland. Exit without alert if Leverage < 200. ElementId: Event_12inxex")
    void mirrorTradeRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_12inxex", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Test
    @AllureId("1435")
    @DisplayName("Mirror trading. Scotland. Exit with alert and restriction if Leverage > 200. ElementId: Event_1k86ppo")
    void mirrorTradeRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_1k86ppo", data.closeTradeMtEvent.id, "mirror_trade");

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().timestamp, matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().alertId, is(data.closeTradeMtEvent.id));
        assertThat("Verify alert", alerts.getFirst().type, is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().ucid, is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(data.closeTradeMtEvent.eventDate));

        assertThat("Verify alert", alerts.getFirst().rule.name, is("Mirror Trading"));
        assertThat("Verify alert", alerts.getFirst().rule.fraudType, is("HEDGING"));
        assertThat("Verify alert", alerts.getFirst().rule.trigger, is("Close Trade"));
        assertThat("Verify alert", alerts.getFirst().rule.ver, notNullValue());

        assertThat("Verify alert", alerts.getFirst().rule.attributes.reason, is("Mirror trade pattern"));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.symbolTraded, is(data.closeTradeMtEvent.symbol));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.serverId, is(data.closeTradeMtEvent.serverId));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.ticketId, is(String.valueOf(data.closeTradeMtEvent.tradeId)));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.account, is(String.valueOf(data.closeTradeMtEvent.tradingAccount)));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        // Verify restriction
        checkManualWithdrawalRestrictionApplied(data.clientHelper, "Mirror trade pattern");
    }

}
