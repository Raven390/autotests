package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment;

import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.kafka.alerts.RuleAlertV2;
import business_objects.kafka.payment.acknowledgement.Acknowledge;
import business_objects.kafka.restriction_events.WithdrawalApprovalsV2;
import helpers.data.DataHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Rule;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.*;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.api.PaymentGateHelper.*;
import static helpers.asserts.PaymentGateAssertsHelper.*;
import static helpers.data.rules.payments.router_rule_crm_payment.RouterRuleCrmPaymentTransferToWaDataFactory.setupRouterRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.PaymentGateHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.writeLog;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_ROUTER_RULE_TRANSFER_TO_WA)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class RouterRuleCrmPaymentTransferToWaTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupRouterRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dataMap);
    }

    @Test
    @AllureId("")
    @DisplayName("Router Rule transfer to wallet. Manual Approve")
    void routerRuleTest1() throws Exception {
        DataHelper data = dataMap.get("1");

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId("post_pending_decision", data.transferToWaEvent.getId().toString(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid())).getPaymentId();

        assertPaymentEvent(data, paymentId, "transferToWA", "PENDING");
        assertPaymentDetails(data, paymentId, "Risk Audit");
        assertPendingDecision(paymentId);

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Withdrawal Review");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert rule", alerts.getFirst().getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alerts.getFirst().getRule().getName(), is("Withdrawal Review"));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getWithdrawalId(), is(data.transferToWaEvent.getTransferId()));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getPaymentChannel(), is("Wallet-Transfer"));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getPlatform(), is(data.transferToWaEvent.getAccountType()));
        assertThat("Verify alert ", alerts.getFirst().getAmountUsd(), is(instanceOf(Double.class)));
        assertThat("Verify alert ", alerts.getFirst().getAmount(), is(instanceOf(Double.class)));
        assertThat("Verify alert ", alerts.getFirst().getPaymentMethod(), is("CRYPTO"));
        assertThat("Verify alert ", alerts.getFirst().getAlertId(), is(data.transferToWaEvent.getId().toString()));
        assertThat("Verify alert ", alerts.getFirst().getMerchantOrderId(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getReason(), is("Potential fraud detected"));
        assertThat("Verify alert ", alerts.getFirst().getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getFraudType(), is("POTENTIAL_ABUSE"));
        assertThat("Verify alert ", alerts.getFirst().getCurrency(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getTrigger(), is("transferToWA"));
        assertThat("Verify alert ", alerts.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alerts.getFirst().getType(), is("TRADING"));

        // check payment.acknowledge
        List<Acknowledge> acknowledgement = getPaymentAcknowledgementFromKafka(data.transferToWaEvent.getId().toString());
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getSubtype(), is("acknowledge"));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getTransferId(), is(data.transferToWaEvent.getTransferId()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getMerchantOrderId(), is(data.transferToWaEvent.getMerchantOrderId()));
        //TODO FIX
        //assertThat("Assert acknowledgement", acknowledgement.getFirst().getBrand(), is(data.transferToWaEvent.getBrand()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getId(), is(data.transferToWaEvent.getId().toString()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getStatus(), is("RECEIVED"));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getCorrelationId(), is(data.transferToWaEvent.getId().toString()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getType(), is(data.transferToWaEvent.getType()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getSchemaVersion(), is("1.0"));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getRegulator(), is(data.transferToWaEvent.getRegulator()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getClientId(), is(data.transferToWaEvent.getClientId()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getPaymentId(), is(paymentId));

        assertRuleExecutions(paymentId);

        Thread.sleep(20_000);
        sendRiskApproveDecision(paymentId);

        checkElementId("Activity_197u1ti", data.transferToWaEvent.getId().toString(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        // check final decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb((paymentId), "risk");
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("risk"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getRejectionCode(), is(0));
        assertThat("Verify decisions have right decision ", decision.getFirst().getActor(), is("Auto qa"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getReasonString(), is("Account activity review."));
        List<PaymentDecisionsObject> decision2 = getRuleDecisionByWithdrawalIdFromDb((paymentId), "final");
        assertThat("Verify decisions have right decision ", decision2.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision2.getFirst().getDecisionType(), is("final"));
        assertThat("Verify decisions have right decision ", decision2.getFirst().getDecisionCode(), is(1));
        assertThat("Verify decisions have right decision ", decision2.getFirst().getRejectionCode(), is(nullValue()));
        assertThat("Verify decisions have right decision ", decision2.getFirst().getActor(), is("Vindex BO"));
        assertThat("Verify decisions have right decision ", decision2.getFirst().getReasonString(), is(nullValue()));

        // check put payments

        // check withdrawal approval
        List<WithdrawalApprovalsV2> withdrawalApprovals = getWithdrawalApprovalsV2FromKafka(String.valueOf(data.transferToWaEvent.getTransferId()));
        writeLog(withdrawalApprovals);
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getSchemaVersion(), is(data.transferToWaEvent.getSchemaVersion()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getPaymentId(), is(paymentId));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getId(), is(data.transferToWaEvent.getId().toString()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTransferId(), is(data.transferToWaEvent.getTransferId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getBrand(), is(Brand.VANTAGE.getDisplayName()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getClientId().toString(), is(data.transferToWaEvent.getClientId().toString()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getType(), is(data.transferToWaEvent.getType()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRegulator(), is(data.transferToWaEvent.getRegulator()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getInternalReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getStatus(), is("Approve"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getMerchantOrderId(), is(data.transferToWaEvent.getMerchantOrderId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getCheckName(), is(data.transferToWaEvent.getCheckName()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRuleName(), is("Router rule"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReasonCode(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReasonRecommend(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getUnderManualReview(), is(1));
    }

    @Test
    @AllureId("")
    @DisplayName("Router Rule transfer to wallet. Manual Reject")
    void routerRuleTest2() throws Exception {
        DataHelper data = dataMap.get("2");

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId("post_pending_decision", data.transferToWaEvent.getId().toString(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid())).getPaymentId();

        Thread.sleep(20_000);
        sendRiskRejectDecision(paymentId);

        checkElementId("Activity_0jjuzkg", data.transferToWaEvent.getId().toString(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
    }

    @Test
    @AllureId("")
    @DisplayName("Router Rule transfer to wallet. Auto approve")
    void routerRuleTest3() throws Exception {
        DataHelper data = dataMap.get("3");

        produceTransferToWaMessageToCrmPaymentTopic(data.transferToWaEvent);

        checkElementId("put_approve_decision", data.transferToWaEvent.getId().toString(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        checkElementId("Activity_197u1ti", data.transferToWaEvent.getId().toString(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
    }
}
