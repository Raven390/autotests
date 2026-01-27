package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.subrules;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.payments.router_rule_crm_payment.WithdrawalIntegrityDataFactory.setupWithdrawalIntegrityCheckRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static utils.Constants.*;

import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Rule;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_INTEGRITY_CHECK_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class WithdrawalIntegrityCheckTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupWithdrawalIntegrityCheckRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dataMap);
    }

    @Test
    @AllureId("")
    @DisplayName("Withdrawal Integrity check in Router rule. Withdrawal amount <= 100. elementId: end_102")
    void WithdrawalIntegrityCheckRuleTest1() throws Exception {
        DataHelper data = dataMap.get("1");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_102", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_WITHDRAWAL_INTEGRITY_CHECK.getProcessId());
        checkElementId(
                "put_rule_execution",
                data.crmWithdrawalEvent.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_INTEGRITY_CHECK.getProcessId());
    }

    @Test
    @AllureId("1753")
    @DisplayName("Withdrawal Integrity check in Router rule. Withdrawal amount <= 50000. elementId: end_202")
    void WithdrawalIntegrityCheckRuleTest2() throws Exception {
        DataHelper data = dataMap.get("2");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_202", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_WITHDRAWAL_INTEGRITY_CHECK.getProcessId());
        checkElementId(
                "put_rule_execution",
                data.crmWithdrawalEvent.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_INTEGRITY_CHECK.getProcessId());
    }

    @Test
    @AllureId("1752")
    @DisplayName("Withdrawal Integrity check in Router rule. general score > 0.9. elementId: end_202")
    void WithdrawalIntegrityCheckRuleTest3() throws Exception {
        DataHelper data = dataMap.get("3");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_203", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_WITHDRAWAL_INTEGRITY_CHECK.getProcessId());
        checkElementId(
                "put_rule_execution",
                data.crmWithdrawalEvent.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_INTEGRITY_CHECK.getProcessId());
    }

    @Test
    @AllureId("1751")
    @DisplayName(
            "Withdrawal Integrity check in Router rule. Withdrawal Integrity check. general score < 0.9. elementId: end_202")
    void WithdrawalIntegrityCheckRuleTest4() throws Exception {
        DataHelper data = dataMap.get("4");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_101", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_WITHDRAWAL_INTEGRITY_CHECK.getProcessId());
        checkElementId(
                "put_rule_execution",
                data.crmWithdrawalEvent.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_INTEGRITY_CHECK.getProcessId());
    }
}
