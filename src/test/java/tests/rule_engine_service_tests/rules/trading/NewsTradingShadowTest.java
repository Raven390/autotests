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
            List<String> normalized = new java.util.ArrayList<>(camundaPath);

            // Camunda execution does not append the rule_name as the final exit node,
            // but the new engine does ("news_trade"). To match them, we append the rule_name
            // if the path completed successfully.
            if (!normalized.isEmpty() && normalized.get(normalized.size() - 1).startsWith("End_nt_alert")) {
                normalized.add("news_trade");
            }
            return normalized;
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
        // Use "5" data to hit End_nt_alert in both engines.
        // Note: Earlier it seems we might be hitting Event_end_1 due to default data/test flags.
        // I will ensure data 5 is properly fetched and setup.
        DataHelper data = dbDataMap.get("5");
        setupData(data);

        String correlationId = data.closeTradeMtEvent.id;

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        Allure.step("Assert shadow execution path");
        assertShadowExecutionPath(correlationId, "news_trade", "End_nt_alert", NORMALIZER);
    }
}