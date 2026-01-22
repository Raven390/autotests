package tests.rule_engine_service_tests.rules.trading;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.AlertType.TRADING;
import static helpers.data.rules.trading.MirrorTradingOpenTradeEventDataFactory.setupMirrorTradingOpenTradeEventRuleData;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.DataHelper;
import helpers.data.enums.Rule;
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
@Story(STORY_RULE_ENGINE_MIRROR_TRADE_OPEN_TRADE_EVENT_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MirrorTradingOpenTradeRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingOpenTradeEventRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("1385")
    @DisplayName(
            "Mirror trading rule with open trade event. Exit without alert if user is test or social trader user. ElementId: endEvent1TestOrSocialTrader")
    void mirrorTradingOpenTradeEventRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId(
                "endEvent1TestOrSocialTrader", data.tradeEvent.id, Rule.MIRROR_TRADE_OPEN_TRADE.getProcessId(), 2);
    }

    @Disabled
    @Test
    @DisplayName(
            "Mirror trading rule with open trade event. Exit without alert if user doesn't have a credit. ElementId: endEvent2DoesNotHaveCredits")
    void mirrorTradingOpenTradeEventRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("endEvent2DoesNotHaveCredits", data.tradeEvent.id, Rule.MIRROR_TRADE_OPEN_TRADE.getProcessId());
    }

    @Disabled
    @Test
    @DisplayName(
            "Mirror trading rule with open trade event. Exit without alert if trades count < 5. ElementId: endEvent3DoesNotHaveEnoughTrades")
    void mirrorTradingOpenTradeEventRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId(
                "endEvent3DoesNotHaveEnoughTrades", data.tradeEvent.id, Rule.MIRROR_TRADE_OPEN_TRADE.getProcessId());
    }

    @Disabled
    @Test
    @DisplayName(
            "Mirror trading rule with open trade event. Exit without alert if trades count > 200. ElementId: endEvent3DoesNotHaveEnoughTrades")
    void mirrorTradingOpenTradeEventRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId(
                "endEvent3DoesNotHaveEnoughTrades", data.tradeEvent.id, Rule.MIRROR_TRADE_OPEN_TRADE.getProcessId());
    }

    @Disabled
    @Test
    @DisplayName(
            "Mirror trading rule with open trade event. Exit without alert if ucidScore < 0.9. ElementId: endEvent5MirrorScoreIsNotHigh")
    void mirrorTradingOpenTradeEventRuleTest5() throws Exception {
        DataHelper data = dbDataMap.get("5");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId(
                "endEvent5MirrorScoreIsNotHigh", data.tradeEvent.id, Rule.MIRROR_TRADE_OPEN_TRADE.getProcessId());
    }

    @Disabled
    @Test
    @DisplayName(
            "Mirror trading rule with open trade event. Exit without alert user have at least 1 resolved alerts. ElementId: endEventFinishWithoutAlert")
    void mirrorTradingOpenTradeEventRuleTest6() throws Exception {
        DataHelper data = dbDataMap.get("6");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("endEventFinishWithoutAlert", data.tradeEvent.id, Rule.MIRROR_TRADE_OPEN_TRADE.getProcessId());
    }

    @Disabled
    @Test
    @DisplayName(
            "Mirror trading rule with open trade event. Exit with alert and restriction if there was no previous alerts. ElementId: endEvent4SuspectsByMLModel")
    void mirrorTradingOpenTradeEventRuleTest7() throws Exception {
        DataHelper data = dbDataMap.get("7");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("wrModel", data.tradeEvent.id, Rule.MIRROR_TRADE_OPEN_TRADE.getProcessId());
        checkElementId("endEvent4SuspectsByMLModel", data.tradeEvent.id, Rule.MIRROR_TRADE_OPEN_TRADE.getProcessId());

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify rule name in alert", alerts.getFirst().rule.name, is("Mirror Trading"));
        assertThat(
                "Verify rule reason in alert",
                alerts.getFirst().rule.attributes.reason,
                is("ML Model suspects the client of Mirror Trading"));
        assertThat("Verify symbol traded in alert", alerts.getFirst().rule.attributes.symbolTraded, is(EURUSD));
        assertThat(
                "Verify serverId in alert",
                alerts.getFirst().rule.attributes.serverId,
                is(data.clientHelper.getServerId()));
        assertThat(
                "Verify ticker id in alert",
                alerts.getFirst().rule.attributes.ticketId,
                is(String.valueOf(data.tradeEvent.tradeId)));
        assertThat(
                "Verify trading account in alert",
                alerts.getFirst().rule.attributes.account,
                is(String.valueOf(data.clientHelper.getTradingAccount())));
        assertThat("Verify ucid in alert", alerts.getFirst().ucid, is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().type, is(TRADING.getDisplayName()));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(data.tradeEvent.eventDate));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        // Verify restriction
        checkManualWithdrawalRestrictionApplied(data, "ML Model suspects the client of Mirror Trading");
    }
}
