package tests.rule_engine_service_tests.rules.trading.mirror_trading_close_trade;

import helpers.data.DataHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.trading.mirror_trading_close_trade.MirrorTradingMainBranchDataFactory.setupMirrorTradingMainBranchRuleData;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_MIRROR_TRADING_CLOSE_TRADE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MirrorTradingMainBranchTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingMainBranchRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dbDataMap);
    }

    @Test
    @AllureId("1431")
    @DisplayName("Mirror trading. Exit without alert if user is test account. ElementId: Event_end_1")
    void mirrorTradeRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_1", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Test
    @DisplayName("Mirror trading. Exit without alert if user has no credits. ElementId: Event_end_3")
    void mirrorTradeRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_3", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Test
    @DisplayName("Mirror trading. Exit without alert if user has no mirrorMatch trades. ElementId: get_matching_opposite_trades_exit")
    void mirrorTradeRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("get_matching_opposite_trades_exit", data.closeTradeMtEvent.id, "mirror_trade");
    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Exit without alerts if deposits > 5000. ElementId:  Event_end_2")
    void mirrorTradeRuleTest4() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Exit without alerts if trades > 300. ElementId: Event_end_2")
    void mirrorTradeRuleTest5() throws Exception {

    }
}
