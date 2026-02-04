package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.api.payment_gate.payments.PaymentsRequests.postPayments;
import static helpers.api.PaymentGateHelper.sendRiskApproveDecision;
import static helpers.api.PaymentGateHelper.sendRiskRejectDecision;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.asserts.AcknowledgeAssertsHelper.assertAcknowledge;
import static helpers.asserts.AlertsAssertsHelper.assertRiskWithdrawalAlert;
import static helpers.asserts.PaymentGateAssertsHelper.*;
import static helpers.asserts.WithdrawalApprovalAssertsHelper.assertWithdrawalApproval;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.Restriction.LOGIN_CRM;
import static helpers.data.rules.payments.router_rule_crm_payment.RouterRuleCrmPaymentShadowModeFactory.setupRouterRuleShadowModeWithdrawalData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static utils.Constants.*;
import static utils.Utils.sleep;

import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.kafka.alerts.RuleAlertV2;
import business_objects.kafka.payment.acknowledgement.Acknowledge;
import business_objects.kafka.restriction_events.WithdrawalApprovalsV2;
import helpers.data.DataHelper;
import helpers.data.enums.payment_gate.Decision;
import helpers.data.enums.rule_engine.Rule;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_ROUTER_RULE_SHADOW_MODE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class CrmPaymentShadowModeWithdrawalTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupRouterRuleShadowModeWithdrawalData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dataMap);
    }

    @Test
    @AllureId("1945")
    @DisplayName("Router Rule shadow mode. Withdrawal Manual Approve")
    void routerRuleShadowModeWithdrawalTest1() throws Exception {
        DataHelper data = dataMap.get("1");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);
        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid()))
                .getPaymentId();

        checkElementId(
                "post_pending_decision",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        assertPaymentEvent(data, paymentId, "withdrawal", "PENDING");
        assertPaymentDetails(data, paymentId, "Risk Audit");
        assertRiskDecision(paymentId, 0);
        assertPaymentDecision(paymentId, 1);

        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Withdrawal Review");
        assertRiskWithdrawalAlert(data, alerts);

        List<Acknowledge> acknowledge = getPaymentAcknowledgeFromKafka(paymentId);
        assertAcknowledge(data, paymentId, acknowledge.getFirst());
        assertRuleExecutions(paymentId);

        sleep(30_000);
        sendRiskApproveDecision(paymentId);

        checkElementId(
                "Activity_197u1ti", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        List<PaymentDecisionsObject> decision =
                getRuleDecisionByWithdrawalIdFromDb((paymentId), Decision.RISK_APPROVE.getType());
        List<PaymentDecisionsObject> decision2 =
                getRuleDecisionByWithdrawalIdFromDb((paymentId), Decision.FINAL_APPROVE.getType());
        assertDecision(decision, decision2, paymentId);

        assertPutPayment(paymentId);

        List<WithdrawalApprovalsV2> withdrawalApprovals =
                getWithdrawalApprovalsV2FromKafka(String.valueOf(data.crmWithdrawalEventV2.getWithdrawalId()));
        assertWithdrawalApproval(data, paymentId, withdrawalApprovals.getFirst());
    }

    @Test
    @AllureId("1946")
    @DisplayName("Router Rule shadow mode. Withdrawal Manual Reject")
    void routerRuleShadowModeWithdrawalTest2() throws Exception {
        DataHelper data = dataMap.get("2");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "post_pending_decision",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid()))
                .getPaymentId();

        sleep(20_000);
        sendRiskRejectDecision(paymentId);

        checkElementId(
                "Activity_0jjuzkg", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1947")
    @DisplayName("Router Rule shadow mode. Withdrawal Auto approve")
    void routerRuleShadowModeWithdrawalTest3() throws Exception {
        DataHelper data = dataMap.get("3");
        setupData(data);

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), LOGIN_CRM.getCode(), "Mirror trade pattern");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "put_approve_decision", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        checkElementId(
                "Activity_197u1ti", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("2124")
    @DisplayName("Router Rule shadow mode. Exit rule for duplicate event")
    void routerRuleShadowModeWithdrawalTest4() throws Exception {
        DataHelper data = dataMap.get("4");
        setupData(data);

        postPayments(data.crmWithdrawalEventV2);
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_duplicate", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("2125")
    @DisplayName("Router Rule shadow mode. No exit for duplicate which need to be reprocessed")
    void routerRuleShadowModeWithdrawalTest5() throws Exception {
        DataHelper data = dataMap.get("5");
        setupData(data);

        postPayments(data.crmWithdrawalEventV2);
        data.crmWithdrawalEventV2.setNeedReprocessing(true);
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "send_acknowledge", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }
}
