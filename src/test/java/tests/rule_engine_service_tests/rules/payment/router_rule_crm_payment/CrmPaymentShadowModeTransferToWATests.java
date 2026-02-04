package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment;

import static business_objects.api.payment_gate.payments.PaymentsRequests.postPayments;
import static helpers.api.PaymentGateHelper.*;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.asserts.AcknowledgeAssertsHelper.assertAcknowledge;
import static helpers.asserts.AlertsAssertsHelper.assertRiskTransferToWaAlert;
import static helpers.asserts.PaymentGateAssertsHelper.*;
import static helpers.asserts.WithdrawalApprovalAssertsHelper.assertWithdrawalApproval;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.Restriction.*;
import static helpers.data.rules.payments.router_rule_crm_payment.RouterRuleCrmPaymentTransferToWaDataFactory.setupRouterRuleShadowModeTransferToWaData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.PaymentGateHelper.*;
import static utils.Constants.*;
import static utils.Utils.sleep;

import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.kafka.alerts.RuleAlertV2;
import business_objects.kafka.payment.acknowledgement.Acknowledge;
import business_objects.kafka.restriction_events.WithdrawalApprovalsV2;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Rule;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_ROUTER_RULE_TRANSFER_TO_WA)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class CrmPaymentShadowModeTransferToWATests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setup() {
        startSshTunnel();
        dataMap = setupRouterRuleShadowModeTransferToWaData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dataMap);
    }

    @Test
    @AllureId("1942")
    @DisplayName("Router Rule transfer to wallet. Transfer manual Approve")
    void routerRuleShadowModeTransferTest1() throws Exception {
        DataHelper data = dataMap.get("1");
        setupData(data);

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);
        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid()))
                .getPaymentId();

        checkElementId(
                "post_pending_decision",
                data.transferToWaEvent.getId().toString(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
        checkElementId(
                "exit_from_payment_branch_for_transfer_to_wa",
                data.transferToWaEvent.getId().toString(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        assertPaymentEvent(data, paymentId, "transferToWA", "PENDING");
        assertPaymentDetails(data, paymentId, "Risk Audit");
        assertRiskDecision(paymentId, 0);

        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Withdrawal Review");
        assertRiskTransferToWaAlert(data, alerts);

        List<Acknowledge> acknowledge = getPaymentAcknowledgeFromKafka(paymentId);
        assertAcknowledge(data, paymentId, acknowledge.getFirst());
        assertRuleExecutions(paymentId);

        Thread.sleep(30_000);
        sendRiskApproveDecision(paymentId);

        checkElementId(
                "Activity_197u1ti",
                data.transferToWaEvent.getId().toString(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb((paymentId), "risk");
        List<PaymentDecisionsObject> decision2 = getRuleDecisionByWithdrawalIdFromDb((paymentId), "final");
        assertDecision(decision, decision2, paymentId);

        assertPutPayment(paymentId);

        List<WithdrawalApprovalsV2> withdrawalApprovals =
                getWithdrawalApprovalsV2FromKafka(String.valueOf(data.transferToWaEvent.getTransferId()));
        assertWithdrawalApproval(data, paymentId, withdrawalApprovals.getFirst());
    }

    @Test
    @AllureId("1943")
    @DisplayName("Router Rule transfer to wallet. Transfer manual Reject")
    void routerRuleShadowModeTransferTest2() throws Exception {
        DataHelper data = dataMap.get("2");
        setupData(data);

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId(
                "post_pending_decision",
                data.transferToWaEvent.getId().toString(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid()))
                .getPaymentId();

        Thread.sleep(30_000);
        sendRiskRejectDecision(paymentId);

        checkElementId(
                "Activity_0jjuzkg",
                data.transferToWaEvent.getId().toString(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1944")
    @DisplayName("Router Rule transfer to wallet. Transfer Auto approve")
    void routerRuleShadowModeTransferTest3() throws Exception {
        DataHelper data = dataMap.get("3");
        setupData(data);

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), LOGIN_CRM.getCode(), "Mirror trade pattern");

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId(
                "put_approve_decision",
                data.transferToWaEvent.getId().toString(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        checkElementId(
                "exit_from_payment_branch_for_transfer_to_wa",
                data.transferToWaEvent.getId().toString(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("2126")
    @DisplayName("Router Rule transfer to wallet. Exit rule for duplicate event")
    void routerRuleShadowModeTransferTest4() throws Exception {
        DataHelper data = dataMap.get("4");
        setupData(data);

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);
        checkElementId(
                "send_acknowledge",
                data.transferToWaEvent.getId().toString(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);
        sleep(180_000);
        checkElementId(
                "end_duplicate",
                data.transferToWaEvent.getId().toString(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("2127")
    @DisplayName("Router Rule transfer to wallet. No exit for duplicate which need to be reprocessed")
    void routerRuleShadowModeTransferTest5() throws Exception {
        DataHelper data = dataMap.get("5");
        setupData(data);

        postPayments(data.transferToWaEvent);
        data.getTransferToWaEvent().setNeedReprocessing(true);
        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId(
                "send_acknowledge",
                data.transferToWaEvent.getId().toString(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }
}
