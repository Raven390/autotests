package tests.rule_engine_service_tests.rules.trading;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.trading.GapTradingRuleDataFactory.setupGapTradingRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static utils.Constants.*;

import helpers.data.DataDeleteHelper;
import helpers.data.DataHelper;
import helpers.data.enums.Rule;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
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
    static void setupData() throws IOException {
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
        DataHelper data = dbDataMap.get("1");

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("endEvent1", data.tradeEvent.id, Rule.GAP_TRADING.getProcessId(), 2);
    }

    @Test
    @AllureId("2017")
    @DisplayName("Gap trading rule. Exit without alert if market does not close in 30 min. ElementId: endEvent2")
    void gapTradingOpenTradeEventRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("endEvent2", data.tradeEvent.id, Rule.GAP_TRADING.getProcessId(), 2);
    }

    @Test
    @AllureId("2019")
    @DisplayName("Gap trading rule. Exit without alert if user has no open trades. ElementId: endEvent3")
    void gapTradingOpenTradeEventRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("endEvent3", data.tradeEvent.id, Rule.GAP_TRADING.getProcessId(), 2);
    }

    @Test
    @AllureId("2020")
    @DisplayName("Gap trading rule. Exit without alert if leverage is low. ElementId: endEvent4")
    void gapTradingOpenTradeEventRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");

        produceTradeMessageToKafka(data.tradeEvent);

        checkElementId("endEvent4", data.tradeEvent.id, Rule.GAP_TRADING.getProcessId(), 2);
    }
}
