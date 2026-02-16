package tests.rule_engine_service_tests.rules.payment.router_rule_crm_events;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.api.payment_gate.payments.PaymentsRequests.postPayments;
import static helpers.api.PaymentGateHelper.*;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.asserts.PaymentGateAssertsHelper.*;
import static helpers.asserts.WithdrawalApprovalAssertsHelper.assertWithdrawalApprovalV1;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.Restriction.DEPOSITS;
import static helpers.data.enums.Restriction.MANUAL_WITHDRAWAL_REVIEW;
import static helpers.data.rules.payments.router_rule_crm_events.RouterRuleCrmEventsDataFactory.setupRouterRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.PaymentGateHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.kafka.restriction_events.WithdrawalApprovals;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Rule;
import io.qameta.allure.*;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_ROUTER_RULE_CRM_EVENTS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class RouterRuleCrmEventsTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupRouterRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dataMap);
    }

    @Test
    @AllureId("2195")
    @DisplayName("Router Rule. No alerts/rejects. Approve withdrawal. elementId: end_approve")
    void routerRuleCrmEventsTest1() throws Exception {
        DataHelper data = dataMap.get("1");
        setupData(data);
        setRestrictionAPIGeneral(
                data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());

        produceWithdrawalMessageV2ToCrmEventsTopic(data.crmWithdrawalEventV2);

        checkElementId("send_alert", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();

        sendRiskApproveDecision(paymentId);
        checkElementId("end_approve", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());

        assertPaymentDetails(data, paymentId, "Risk Audit");
        assertRiskDecision(paymentId, 1, 0, "Auto qa");

        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), 13);
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));

        PaymentRuleExecutionsObject paymentRuleExecutionsObject2 = getPaymentRuleExecution(paymentId.toString(), 5);
        assertThat("Assert rule execution", paymentRuleExecutionsObject2.getPaymentId(), is(paymentId));

        List<WithdrawalApprovals> withdrawalApprovals =
                getWithdrawalApprovalsFromKafka(String.valueOf(data.crmWithdrawalEventV2.getWithdrawalId()));
        writeLog(withdrawalApprovals);
        assertWithdrawalApprovalV1(data, paymentId, withdrawalApprovals.getFirst());
    }

    @Test
    @AllureId("2196")
    @DisplayName("Router Rule. Alert, no rejects, Risk rejection = true. elementId: end_risk_reject")
    void routerRuleCrmEventsTest3() throws Exception {
        DataHelper data = dataMap.get("3");
        setupData(data);

        data.crmWithdrawalEventV2.setCheckName("Checkname");
        produceWithdrawalMessageV2ToCrmEventsTopic(data.crmWithdrawalEventV2);

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();

        checkElementId("send_alert", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());

        sendRiskRejectDecision(paymentId);

        checkElementId(
                "end_risk_reject", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());

        List<PaymentDecisionsObject> paymentDecisionsObject = getPaymentDecisionsByPaymentId(paymentId);
        assertThat("Verify amount of decisions in DB", paymentDecisionsObject.size(), is(1));
        assertThat(
                "Verify decisions have right decision ",
                paymentDecisionsObject.getFirst().getPaymentId(),
                is(paymentId));
        assertThat(
                "Verify decisions have right decision ",
                paymentDecisionsObject.getFirst().getDecisionType(),
                is("risk"));
        assertThat(
                "Verify decisions have right decision ",
                paymentDecisionsObject.getFirst().getDecisionCode(),
                is(2));
        assertThat(
                "Verify decisions have right decision ",
                paymentDecisionsObject.getFirst().getRejectionCode(),
                is(1));
        assertThat(
                "Verify decisions have right decision ",
                paymentDecisionsObject.getFirst().getActor(),
                is("Auto qa"));

        List<WithdrawalApprovals> withdrawalApprovals =
                getWithdrawalApprovalsFromKafka(String.valueOf(data.crmWithdrawalEventV2.getWithdrawalId()));
        writeLog(withdrawalApprovals);
        assertWithdrawalApprovalV1(data, paymentId, withdrawalApprovals.getFirst());
    }

    @Test
    @AllureId("2197")
    @DisplayName("Withdrawal notification rule. Auto approve if outcome is 400. ElementId:end_approve")
    void routerRuleCrmEventsTest8() throws Exception {
        DataHelper data = dataMap.get("8");
        setupData(data);

        produceWithdrawalMessageV2ToCrmEventsTopic(data.crmWithdrawalEventV2);

        checkElementId("end_event_402", data.crmWithdrawalEventV2.getId(), Rule.CLEARANCE_RULE.getProcessId());
        checkElementId(
                "put_approve_decision", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());
        checkElementId("end_approve", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();

        assertThat("Assert final_decision_id", paymentEventsObject.getFinalDecisionId(), is(2));

        List<WithdrawalApprovals> withdrawalApprovals =
                getWithdrawalApprovalsFromKafka(String.valueOf(data.crmWithdrawalEventV2.getWithdrawalId()));
        writeLog(withdrawalApprovals);
        assertWithdrawalApprovalV1(data, paymentId, withdrawalApprovals.getFirst());
    }

    @Test
    @AllureId("2198")
    @DisplayName("Router Rule. Exit rule for duplicate event")
    void routerRuleCrmEventsTest6() throws Exception {
        DataHelper data = dataMap.get("6");
        setupData(data);

        postPayments(data.crmWithdrawalEventV2);
        produceWithdrawalMessageV2ToCrmEventsTopic(data.crmWithdrawalEventV2);

        checkElementId("end_duplicate", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());
    }

    @Test
    @AllureId("2199")
    @DisplayName("Router Rule. No exit for duplicate which need to be reprocessed")
    void routerRuleCrmEventsTest7() throws Exception {
        DataHelper data = dataMap.get("7");
        setupData(data);

        postPayments(data.crmWithdrawalEventV2);
        data.crmWithdrawalEventV2.setNeedReprocessing(true);
        produceWithdrawalMessageV2ToCrmEventsTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "get_rule_executions", data.crmWithdrawalEventV2.getId(), Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());
    }
}
