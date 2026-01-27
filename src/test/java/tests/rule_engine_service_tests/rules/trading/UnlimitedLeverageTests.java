package tests.rule_engine_service_tests.rules.trading;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.AlertsAssertsHelper.assertUnlimitedLeverageAlert;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.trading.UnlimitedLeverageRuleDataFactory.setupUnlimitedLeverageRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;

import business_objects.kafka.alerts.RuleAlertV2;
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
@Story(STORY_RULE_ENGINE_UNLIMITED_LEVERAGE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class UnlimitedLeverageTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupUnlimitedLeverageRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dataMap);
    }

    @Test
    @AllureId("2050")
    @DisplayName("Unlimited leverage rule. Exit without alert if user is test user. Element Id: Event_0lxjn09")
    void unlimitedLeverageRuleTest1() throws Exception {
        DataHelper data = dataMap.get("1");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("Event_0lxjn09", data.tradeEvent.getId(), Rule.UNLIMITED_LEVERAGE.getProcessId());
    }

    @Test
    @AllureId("2051")
    @DisplayName(
            "Unlimited leverage rule. Exit without alert if unlimited leverage is false. Element Id: Event_0lxjn09")
    void unlimitedLeverageRuleTest2() throws Exception {
        DataHelper data = dataMap.get("2");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("Event_end_1", data.tradeEvent.getId(), Rule.UNLIMITED_LEVERAGE.getProcessId());
    }

    @Test
    @AllureId("2093")
    @DisplayName("Unlimited leverage rule. Exit without alert if unlimited leverage is true. Element Id: Event_07awlre")
    void unlimitedLeverageRuleTest3() throws Exception {
        DataHelper data = dataMap.get("3");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("Event_07awlre", data.tradeEvent.getId(), Rule.UNLIMITED_LEVERAGE.getProcessId());
    }

    @Test
    @AllureId("2053")
    @DisplayName("Unlimited leverage rule. Exit without alert if profit < 300usd. Element Id: Event_1tzckgk")
    void unlimitedLeverageRuleTest4() throws Exception {
        DataHelper data = dataMap.get("4");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("Event_1tzckgk", data.tradeEvent.getId(), Rule.UNLIMITED_LEVERAGE.getProcessId());
    }

    @Test
    @AllureId("2054")
    @DisplayName("Unlimited leverage rule. Exit without alert if profit/deposit < 1. Element Id: Event_0urh1rn")
    void unlimitedLeverageRuleTest5() throws Exception {
        DataHelper data = dataMap.get("5");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("Event_0urh1rn", data.tradeEvent.getId(), Rule.UNLIMITED_LEVERAGE.getProcessId());
    }

    @Test
    @AllureId("2055")
    @DisplayName("Unlimited leverage rule. Exit without alert if news trade ration < 0.5. Element Id: Event_1fmnax8")
    void unlimitedLeverageRuleTest6() throws Exception {
        DataHelper data = dataMap.get("6");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("Event_1fmnax8", data.tradeEvent.getId(), Rule.UNLIMITED_LEVERAGE.getProcessId());
    }

    @Test
    @AllureId("2056")
    @DisplayName(
            "Unlimited leverage rule. Exit without alert if DailyNotional Value / NotionalValue < 0.6. Element Id: Event_1antcml")
    void unlimitedLeverageRuleTest7() throws Exception {
        DataHelper data = dataMap.get("7");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("Event_1antcml", data.tradeEvent.getId(), Rule.UNLIMITED_LEVERAGE.getProcessId());
    }

    @Test
    @AllureId("2057")
    @DisplayName("Unlimited leverage rule. Exit without alert if leverage < 200. Element Id: Event_0zb1zw6")
    void unlimitedLeverageRuleTest8() throws Exception {
        DataHelper data = dataMap.get("8");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("Event_0zb1zw6", data.tradeEvent.getId(), Rule.UNLIMITED_LEVERAGE.getProcessId());
    }

    @Test
    @AllureId("2058")
    @DisplayName(
            "Unlimited leverage rule. Exit without alert if Client have not been detected as Fraud News Abuse. Element Id: Event_0v7avtz")
    void unlimitedLeverageRuleTest9() throws Exception {
        DataHelper data = dataMap.get("9");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("Event_0v7avtz", data.tradeEvent.getId(), Rule.UNLIMITED_LEVERAGE.getProcessId());
    }

    @Test
    @AllureId("2059")
    @DisplayName(
            "Unlimited leverage rule. Exit with alert and restriction if Client have not been detected as Fraud News Abuse. Element Id: Event_0tv1x8x")
    void unlimitedLeverageRuleTest10() throws Exception {
        DataHelper data = dataMap.get("10");
        setupData(data);

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("Event_0tv1x8x", data.tradeEvent.getId(), Rule.UNLIMITED_LEVERAGE.getProcessId());

        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(
                data.clientHelper, "News Trading", "News trading pattern with Unlimited Leverage");
        assertUnlimitedLeverageAlert(data, alerts);

        checkManualWithdrawalRestrictionApplied(data, "News trading pattern");
    }
}
