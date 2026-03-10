package tests.rule_engine_service_tests.rules.trading;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.trading.NewsTraderRuleDataFactory.setupNewsTraderCloseTradeRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;

import helpers.data.DataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import tests.rule_engine_service_tests.ShadowTestBase;

import static utils.Constants.FEATURE_RULE_ENGINE_SERVICE;
import static utils.Constants.LAYER_API;
import static utils.Constants.STORY_RULE_ENGINE_NEWS_TRADER_CLOSE_TRADE_EVENT_RULE;
import static utils.Constants.SUITE_RULE_ENGINE_RULES_TESTS;
import static utils.Constants.TEAM_CORE;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_NEWS_TRADER_CLOSE_TRADE_EVENT_RULE + " Shadow Test")
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class NewsTradingShadowTest extends ShadowTestBase {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupNewsTraderCloseTradeRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dbDataMap);
        stopSshTunnel();
    }

    private static final PathNormalizer NORMALIZER = new PathNormalizer() {
        @Override
        public List<String> normalizeCamundaPath(List<String> camundaPath) {
            // Filter out system events/gateways specific to Camunda that the new engine doesn't emit.
            // In a real scenario, this would remove specific IDs like gateway forks, intermediate events, etc.
            // For now, we return it as is, minus standard BPMN wrapper noise if any (which we assume isn't present unless found in trace).
            return camundaPath.stream()
                .filter(id -> !id.startsWith("Gateway_")) // Example filter
                .collect(Collectors.toList());
        }

        @Override
        public List<String> normalizeNewEnginePath(List<String> newEnginePath) {
            return newEnginePath;
        }
    };

    @Test
    @AllureId("SHADOW-2316")
    @DisplayName("Shadow Test: News Trader. Exit with alert if profit/deposit > 0.5.")
    void newsTradingShadowTest5() throws Exception {
        Allure.step("generate test data where News Trader. Exit with alert if profit/deposit > 0.5.");
        DataHelper data = dbDataMap.get("5");
        setupData(data);

        String correlationId = data.closeTradeMtEvent.id;

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        Allure.step("Assert shadow execution path");
        assertShadowExecutionPath(correlationId, "End_nt_alert", NORMALIZER);
    }
}