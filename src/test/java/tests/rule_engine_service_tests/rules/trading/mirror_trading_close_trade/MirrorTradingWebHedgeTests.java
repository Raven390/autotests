package tests.rule_engine_service_tests.rules.trading.mirror_trading_close_trade;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.trading.mirror_trading_close_trade.MirrorTradingWebHedgeDataFactory.setupMirrorTradingWebHedgeRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
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
class MirrorTradingWebHedgeTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException, InterruptedException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingWebHedgeRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dbDataMap);
    }

    @Disabled
    @Test
    @AllureId("1524")
    @DisplayName("Mirror trading. Web hedge. Exit without alert if user geo is not vietnam. ElementId: Event_10k041u")
    void mirrorTradeRuleTest18() throws Exception {
        DataHelper data = dbDataMap.get("18");
        setupData(data);

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_10k041u", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Disabled
    @Test
    @AllureId("1525")
    @DisplayName(
            "Mirror trading. Web hedge. Exit without alert if user has no crypto deposits. ElementId: Event_06qi81c")
    void mirrorTradeRuleTest19() throws Exception {
        DataHelper data = dbDataMap.get("19");
        setupData(data);

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_06qi81c", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Disabled
    @Test
    @AllureId("1526")
    @DisplayName(
            "Mirror trading. Web hedge. Exit without alert if user has country != vietnam. ElementId: Event_1ya7o9a")
    void mirrorTradeRuleTest20() throws Exception {
        DataHelper data = dbDataMap.get("20");
        setupData(data);

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_1ya7o9a", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Disabled
    @Test
    @AllureId("1527")
    @DisplayName(
            "Mirror trading. Web hedge. Exit without alert if user has not all trades from web trader. ElementId: Event_1q3hzii")
    void mirrorTradeRuleTest21() throws Exception {
        DataHelper data = dbDataMap.get("21");
        setupData(data);

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_1q3hzii", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Disabled
    @Test
    @AllureId("1528")
    @DisplayName("Mirror trading. Web hedge. Exit without alert if user has resolved alerts. ElementId: Event_1ss67m1")
    void mirrorTradeRuleTest22() throws Exception {
        DataHelper data = dbDataMap.get("22");
        setupData(data);

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_1ss67m1", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Disabled
    @Test
    @AllureId("1529")
    @DisplayName(
            "Mirror trading. Web hedge. Exit with restriction alert if user doesn't has resolved alerts. ElementId: Event_1ss67m1")
    void mirrorTradeRuleTest23() throws Exception {
        DataHelper data = dbDataMap.get("23");
        setupData(data);

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_1ss67m1", data.closeTradeMtEvent.id, "mirror_trade");

        // Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        // Verify restriction is bonus restriction with code 13
        checkManualWithdrawalRestrictionApplied(data, "Mirror trade pattern");
    }
}
