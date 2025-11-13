package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment;

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
import static helpers.data.rules.payments.router_rule_crm_payment.MirrorTradeDataFactory.setupMirrorTradeRuleData;

import static helpers.database.DbHelper.*;

import static utils.Constants.*;


@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_MIRROR_TRADE_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MirrorTradeTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupMirrorTradeRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dataMap);
    }

    @Test
    @AllureId("1773")
    @DisplayName("Mirror trade in router rule. Exit without alert if mirror trade flag is false. ElementId: Event_1gdl5i3")
    void mirrorTradeRule1Test() throws Exception {
        DataHelper data = dataMap.get("1");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1gdl5i3", data.crmWithdrawalEvent.getId(), "mirror_trade_rr");
        checkElementId("Activity_1kf15qu", data.crmWithdrawalEvent.getId(), "router_rule_crm_payment");

    }

    @Test
    @AllureId("1772")
    @DisplayName("Mirror trade in router rule. Exit with alert if mirror trade flag is true. ElementId: Event_1waht3m")
    void mirrorTradeRule2Test() throws Exception {
        DataHelper data = dataMap.get("2");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1waht3m", data.crmWithdrawalEvent.getId(), "mirror_trade_rr");
        checkElementId("Activity_1kf15qu", data.crmWithdrawalEvent.getId(), "router_rule_crm_payment");
    }
}
