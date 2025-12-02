package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.connection_search_tests;

import helpers.data.DataHelper;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.payments.router_rule_crm_payment.connection_search.ConnectionSearchTradingAbuseDataFactory.setupConnectionSearchTradingAbuseRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;

@Disabled
@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CONNECTION_SEARCH_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class ConnectionSearchTradingAbuseTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupConnectionSearchTradingAbuseRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dataMap);
    }

    @Test
    @DisplayName("")
    void connectionSearchTradingAbuseTest1() throws Exception {
    }
}
