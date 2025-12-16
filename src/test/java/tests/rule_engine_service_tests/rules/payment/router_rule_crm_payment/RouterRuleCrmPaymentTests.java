package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.alerts.RuleAlertV2;
import business_objects.kafka.payment.acknowledgement.Acknowledge;
import business_objects.kafka.restriction_events.WithdrawalApprovals;
import business_objects.kafka.restriction_events.WithdrawalApprovalsV2;
import helpers.data.DataHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Rule;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.*;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.api.PaymentGateHelper.*;
import static helpers.data.rules.payments.router_rule_crm_payment.RouterRuleCrmPaymentDataFactory.setupRouterRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.PaymentGateHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_ROUTER_RULE_CRM_PAYMENT)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class RouterRuleCrmPaymentTests extends TestBaseRule {

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

    @Disabled
    @Test
    @AllureId("1621")
    @DisplayName("Router Rule. No alerts/rejects. Approve withdrawal. elementId: Event_0t14mt3")
    void routerRuleTest1() throws Exception {
        DataHelper data = dataMap.get("1");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_end_2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("Event_0t14mt3", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();

        PaymentDetailsObject paymentDetailsObject = getPaymentDetails(data.clientHelper.getUserId());
        assertThat("Assert payment details", paymentDetailsObject.getPaymentId(), is(paymentId));
        assertThat("Assert payment details", paymentDetailsObject.getPaymentId(), is(paymentId));
        assertThat("Assert payment details", paymentDetailsObject.getPayload(), containsString("\"withdrawalAmountUSD\": 1.1"));

        List<Acknowledge> acknowledgement = getPaymentAcknowledgementFromKafka(data.crmWithdrawalEvent.getId());
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getPaymentId(), is(paymentId.toString()));

        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(notNullValue()));

        List<PaymentDecisionsObject> paymentDecisionsObject = getPaymentDecisionsByPaymentId(paymentId);
        assertThat("Verify amount of decisions in DB", paymentDecisionsObject.size(), is(1));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionType(), is("risk"));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionCode(), is(1));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getRejectionCode(), is(nullValue()));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getActor(), is("Rule engine"));

        assertThat("Assert final_decision_id", paymentEventsObject.getFinalDecisionId(), is(2));

        List<WithdrawalApprovals> withdrawalApprovals = getWithdrawalApprovalsFromKafka(String.valueOf(data.crmWithdrawalEvent.getWithdrawalId()));
        writeLog(withdrawalApprovals);
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getMessageId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTransferId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getBrand(), is(data.crmWithdrawalEvent.getBrand().replace("v", "V")));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getInternalReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getStatus(), is("Approve"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getOrderNumber(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getCheckName(), is(""));
    }

    @Disabled
    @Test
    @AllureId("1621")
    @DisplayName("Router Rule. Alert, no rejects. elementId: Event_11azia8")
    void routerRuleTest2() throws Exception {
        DataHelper data = dataMap.get("2");

        data.crmWithdrawalEvent.setCheckName("Checkname");
        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        Allure.step("Retrieve payment id");
        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid())).getPaymentId();

        sendRiskApproveDecision(paymentId);

        checkElementId("Event_1gdl5i3", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("send_alert", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        List<PaymentDecisionsObject> paymentDecisionsObject = getPaymentDecisionsByPaymentId(paymentId);
        assertThat("Verify amount of decisions in DB", paymentDecisionsObject.size(), is(1));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionType(), is("risk"));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionCode(), is(1));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getRejectionCode(), is(0));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getActor(), is("Auto qa"));

        List<WithdrawalApprovals> withdrawalApprovals = getWithdrawalApprovalsFromKafka(String.valueOf(data.crmWithdrawalEvent.getWithdrawalId()));
        writeLog(withdrawalApprovals);
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getMessageId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTransferId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getBrand(), is(data.crmWithdrawalEvent.getBrand().replace("v", "V")));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getInternalReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getStatus(), is("Approve"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getOrderNumber(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getCheckName(), is(data.crmWithdrawalEvent.getCheckName()));
    }

    @Disabled
    @Test
    @AllureId("1622")
    @DisplayName("Router Rule. Alert, no rejects, Risk rejection = true. elementId: Event_1kdk048")
    void routerRuleTest3() throws Exception {
        DataHelper data = dataMap.get("3");

        data.crmWithdrawalEvent.setCheckName("Checkname");
        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        Allure.step("Retrieve payment id");
        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid())).getPaymentId();

        sendRiskRejectDecision(paymentId);

        checkElementId("Event_1gdl5i3", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("send_alert", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        List<PaymentDecisionsObject> paymentDecisionsObject = getPaymentDecisionsByPaymentId(paymentId);
        assertThat("Verify amount of decisions in DB", paymentDecisionsObject.size(), is(1));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionType(), is("risk"));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionCode(), is(2));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getRejectionCode(), is(0));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getActor(), is("Auto qa"));

        List<WithdrawalApprovals> withdrawalApprovals = getWithdrawalApprovalsFromKafka(String.valueOf(data.crmWithdrawalEvent.getWithdrawalId()));
        writeLog(withdrawalApprovals);
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getMessageId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTransferId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getBrand(), is(data.crmWithdrawalEvent.getBrand().replace("v", "V")));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getInternalReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getStatus(), is("Refuse"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getOrderNumber(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getCheckName(), is(data.crmWithdrawalEvent.getCheckName()));
    }

    @Disabled
    @Test
    @AllureId("1624")
    @DisplayName("Router rule. Exit with alert if withdrawal has not empty check name and 'Crypto_Risk' mirror flag = true. ElementId:Event_0dvxfab")
    void routerRuleTest4() throws Exception {
        DataHelper data = dataMap.get("4");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        List<PaymentEventsObject> events = getUserPaymentEventsFromDb(data.clientHelper);
        UUID paymentId = events.getFirst().getPaymentId();

        sendRiskRejectDecision(paymentId);

        checkElementId("Event_1gdl5i3", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("Event_1kdk048", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        assertThat("Verify amount of payments events in DB", events.size(), is(1));
        assertEquals(data.crmWithdrawalEvent.getWithdrawalId(), Long.valueOf(events.getFirst().getCrmId()));

        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Withdrawal Review");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert rule", alerts.getFirst().getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alerts.getFirst().getRule().getName(), is("Withdrawal Review"));

        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getPlatform(), is(data.crmWithdrawalEvent.getPlatform()));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getPaymentChannel(), is(data.crmWithdrawalEvent.getPaymentChannelName()));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getCreateTime(), is(notNullValue()));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getCheck(), is(notNullValue()));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getDate(), is(notNullValue()));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getWithdrawalId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getBrand(), is(data.crmWithdrawalEvent.getBrand()));

        assertThat("Verify alert ", alerts.getFirst().getAmountUsd(), is(instanceOf(Double.class)));
        assertThat("Verify alert ", alerts.getFirst().getAmount(), is(instanceOf(Double.class)));
        assertThat("Verify alert ", alerts.getFirst().getPaymentMethod(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getAlertId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Verify alert ", alerts.getFirst().getMerchantOrderId(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getReason(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getFraudType(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getCurrency(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getTrigger(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getUcid(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getType(), is(notNullValue()));

        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb((events.getFirst().getPaymentId()));
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("risk"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(2));
        assertThat("Verify decisions have right decision ", decision.getFirst().getRejectionCode(), is(0));
        assertThat("Verify decisions have right decision ", decision.getFirst().getActor(), is("Auto qa"));
    }

    @Disabled
    @Test
    @AllureId("1623")
    @DisplayName("Router rule. Exit with alert if withdrawal has not empty check name and 'Crypto_Risk' mirror flag = false. ElementId:Event_1gdl5i3")
    void routerRuleTest5() throws Exception {
        DataHelper data = dataMap.get("5");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_end_2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("Event_0t14mt3", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "withdrawalNotification");

        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<PaymentEventsObject> events = getUserPaymentEventsFromDb(data.clientHelper);
        assertThat("Verify amount of payments events in DB", events.size(), is(1));
        assertEquals(data.crmWithdrawalEvent.getWithdrawalId(), Long.valueOf(events.getFirst().getCrmId()));
        UUID paymentId = events.getFirst().getPaymentId();

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper, "withdrawalNotification");
        assertThat("Verify amount of alerts in DB", dbAlerts.size(), is(0));

        List<PaymentDecisionsObject> paymentDecisionsObject = getPaymentDecisionsByPaymentId(paymentId);
        assertThat("Verify amount of decisions in DB", paymentDecisionsObject.size(), is(1));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionType(), is("risk"));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionCode(), is(1));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getRejectionCode(), is(nullValue()));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getActor(), is("Rule engine"));

        List<WithdrawalApprovals> approval = getWithdrawalApprovalsFromKafka(String.valueOf(data.crmWithdrawalEvent.getWithdrawalId()));

        assertThat("Verify amount of approvals in Kafka topic", approval.size(), is(1));
        assertThat("Verify status of approval in Kafka topic", approval.getFirst().getStatus(), is("Approve"));
        assertThat("Verify checkName in Kafka topic", approval.getFirst().getCheckName(), is(""));
        assertThat("Verify brand in Kafka topic", approval.getFirst().getBrand(), is("Vantage"));
        assertThat("Verify orderNumber in Kafka topic", approval.getFirst().getOrderNumber(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
    }

    @Disabled
    @Test
    @AllureId("1859")
    @DisplayName("Router Rule tests. Manual approve withdrawal after CS rule ElementId: end_102")
    void routerRuleTest6() throws Exception {
        DataHelper data = dataMap.get("6");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("end_102", data.crmWithdrawalEvent.getId(), "cs_on_withdrawal");
        checkElementId("Activity_06e7z5l", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
        checkElementId("Activity_04gzpdk", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid())).getPaymentId();

        sendRiskApproveDecision(paymentId);

        List<WithdrawalApprovalsV2> withdrawalApprovals = getWithdrawalApprovalsV2FromKafka(String.valueOf(data.crmWithdrawalEvent.getWithdrawalId()));
        writeLog(withdrawalApprovals);
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getSchemaVersion(), is(data.crmWithdrawalEvent.getSchemaVersion()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTransferId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getBrand(), is(Brand.VANTAGE.getDisplayName()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getClientId(), is(data.crmWithdrawalEvent.getClientId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getType(), is(data.crmWithdrawalEvent.getType()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getInternalReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getStatus(), is("Approve"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getMerchantOrderId(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getCheckName(), is(data.crmWithdrawalEvent.getCheckName()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRuleName(), is("Router rule"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReasonCode(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReasonRecommend(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getUnderManualReview(), is(1));
    }

    @Disabled
    @Test
    @AllureId("1859")
    @DisplayName("Router Rule tests. Manual reject withdrawal after CS rule ElementId: end_102")
    void routerRuleTest7() throws Exception {
        DataHelper data = dataMap.get("7");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("end_102", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_06e7z5l", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
        checkElementId("Activity_016fbcu", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid())).getPaymentId();

        sendRiskRejectDecision(paymentId);

        List<WithdrawalApprovalsV2> withdrawalApprovals = getWithdrawalApprovalsV2FromKafka(String.valueOf(data.crmWithdrawalEvent.getWithdrawalId()));
        writeLog(withdrawalApprovals);
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getSchemaVersion(), is(data.crmWithdrawalEvent.getSchemaVersion()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTransferId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getBrand(), is(Brand.VANTAGE.getDisplayName()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getClientId(), is(data.crmWithdrawalEvent.getClientId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getType(), is(data.crmWithdrawalEvent.getType()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getInternalReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getStatus(), is("Approve"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getMerchantOrderId(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getCheckName(), is(data.crmWithdrawalEvent.getCheckName()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRuleName(), is("Router rule"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReasonCode(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReasonRecommend(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getUnderManualReview(), is(1));
    }

    @Disabled
    @Test
    @AllureId("1865")
    @DisplayName("Router Rule tests. Auto reject after CS rule. ElementId: ")
    void routerRuleTest8() throws Exception {

    }

    @Disabled
    @Test
    @AllureId("1866")
    @DisplayName("Router Rule tests. Auto approve after CS rule. ElementId: ")
    void routerRuleTest9() throws Exception {

    }

    @Disabled
    @Test
    @AllureId("")
    @DisplayName("Router Rule tests. Two payment rules generates alert")
    void routerRuleTest10() throws Exception {

    }

    @Disabled
    @Test
    @AllureId("")
    @DisplayName("Router Rule tests. 1 Payment alert and 1 trading alert waits for manual decisions")
    void routerRuleTest11() throws Exception {

    }

    /// Router rule with trading + payment branches

    @Disabled
    @Test
    @AllureId("1878")
    @DisplayName("FULL Router Rule tests. Risk approve + payment approve = approve")
    void routerRuleTest12() throws Exception {
        DataHelper data = dataMap.get("12");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("post_pending_decision", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
        checkElementId("post_payment_pending_decision", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid())).getPaymentId();

        sendRiskApproveDecision(paymentId);
        Thread.sleep(10_000);
        sendPaymentApproveDecision(paymentId);

        checkElementId("Activity_04gzpdk", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1877")
    @DisplayName("FULL Router Rule tests. Risk reject + payment approve = reject")
    void routerRuleTest13() throws Exception {
        DataHelper data = dataMap.get("13");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("post_pending_decision", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
        checkElementId("post_payment_pending_decision", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid())).getPaymentId();

        sendRiskRejectDecision(paymentId);
        Thread.sleep(10_000);
        sendPaymentApproveDecision(paymentId);

        checkElementId("Activity_05p28in", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1876")
    @DisplayName("FULL Router Rule tests. trading approve + payment reject = reject")
    void routerRuleTest14() throws Exception {
        DataHelper data = dataMap.get("14");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("post_pending_decision", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
        checkElementId("post_payment_pending_decision", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid())).getPaymentId();

        sendRiskApproveDecision(paymentId);
        Thread.sleep(10_000);
        sendPaymentRejectDecision(paymentId);

        checkElementId("Activity_0hnglsq", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1875")
    @DisplayName("FULL Router Rule tests. trading reject + payment reject = reject")
    void routerRuleTest15() throws Exception {
        DataHelper data = dataMap.get("15");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("post_pending_decision", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
        checkElementId("post_payment_pending_decision", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid())).getPaymentId();

        sendRiskRejectDecision(paymentId);
        Thread.sleep(10_000);
        sendPaymentRejectDecision(paymentId);

        checkElementId("Activity_05p28in", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
    }

    /// router rule without payment branch

    @Test
    @AllureId("")
    @DisplayName("Router Rule V0. Manual Approve")
    void routerRuleTest16() throws Exception {
        DataHelper data = dataMap.get("16");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("post_pending_decision", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        // Check payment event in PGS DB
        PaymentEventsObject paymentEvent = getPaymentEvent(data.clientHelper.getUcid());
        UUID paymentId = Objects.requireNonNull(paymentEvent).getPaymentId();
        assertThat("Assert payment event", paymentEvent.getPaymentId(), is(paymentId));
        assertThat("Assert payment event", paymentEvent.getCrmId(), is(data.crmWithdrawalEvent.getWithdrawalId().toString()));
        assertThat("Assert payment event", paymentEvent.getType(), is("withdrawal"));
        assertThat("Assert payment event", paymentEvent.getFinalDecisionId(), is(nullValue()));
        assertThat("Assert payment event", paymentEvent.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Assert payment event", paymentEvent.getDateCreated(), is(notNullValue()));
        assertThat("Assert payment event", paymentEvent.getDateUpdated(), is(notNullValue()));
        assertThat("Assert payment event", paymentEvent.getDateDecided(), is(nullValue()));
        assertThat("Assert payment event", paymentEvent.getDeliveryStatus(), is("PENDING"));

        //check payment details in PGS DB
        PaymentDetailsObject paymentDetailsObject = getPaymentDetails(data.clientHelper.getUserId());
        assertThat("Assert payment details", paymentDetailsObject.getPaymentId(), is(paymentId));
        assertThat("Assert payment details", paymentDetailsObject.getBrand(), is(data.crmWithdrawalEvent.getBrand()));
        assertThat("Assert payment details", paymentDetailsObject.getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Assert payment details", paymentDetailsObject.getType(), is(data.crmWithdrawalEvent.getType()));
        assertThat("Assert payment details", paymentDetailsObject.getClientId(), is(data.crmWithdrawalEvent.getClientId().toString()));
        assertThat("Assert payment details", paymentDetailsObject.getMerchantOrderId(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Assert payment details", paymentDetailsObject.getEventDate(), is(notNullValue()));
        assertThat("Assert payment details", paymentDetailsObject.getStatus(), is(nullValue()));
        //assertThat("Assert payment details", paymentDetailsObject.getPlatform(), is(notNullValue()));
        assertThat("Assert payment details", paymentDetailsObject.getSourceSystem(), is(notNullValue()));
        assertThat("Assert payment details", paymentDetailsObject.getSourceEnv(), is(notNullValue()));
        assertThat("Assert payment details", paymentDetailsObject.getDateCreated(), is(notNullValue()));
        assertThat("Assert payment details", paymentDetailsObject.getAmountUsd().toString(), containsString("1."));
        assertThat("Assert payment details", paymentDetailsObject.getPayload(), containsString("\"withdrawalAmountUSD\": 1."));

        // check pending decision
        List<PaymentDecisionsObject> paymentDecisionsObject = getPaymentDecisionsByPaymentId(paymentId).stream().filter(decision -> "risk".equals(decision.getDecisionType())).toList();
        assertThat("Verify amount of decisions in DB", paymentDecisionsObject.size(), is(1));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionType(), is("risk"));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getDecisionCode(), is(0));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getRejectionCode(), is(nullValue()));
        assertThat("Verify decisions have right decision ", paymentDecisionsObject.getFirst().getActor(), is("Rule engine"));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Withdrawal Review");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert rule", alerts.getFirst().getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alerts.getFirst().getRule().getName(), is("Withdrawal Review"));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getWithdrawalId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getPaymentChannel(), is(data.crmWithdrawalEvent.getPaymentChannelName()));
        assertThat("Verify alert attributes", alerts.getFirst().getAttributes().getPlatform(), is(data.crmWithdrawalEvent.getPlatform()));
        assertThat("Verify alert ", alerts.getFirst().getAmountUsd(), is(instanceOf(Double.class)));
        assertThat("Verify alert ", alerts.getFirst().getAmount(), is(instanceOf(Double.class)));
        assertThat("Verify alert ", alerts.getFirst().getPaymentMethod(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getAlertId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Verify alert ", alerts.getFirst().getMerchantOrderId(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getReason(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getFraudType(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getCurrency(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getTrigger(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getUcid(), is(notNullValue()));
        assertThat("Verify alert ", alerts.getFirst().getType(), is(notNullValue()));

        // check payment.acknowledge
        List<Acknowledge> acknowledgement = getPaymentAcknowledgementFromKafka(data.crmWithdrawalEvent.getId());
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getSubtype(), is("acknowledge"));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getTransferId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getMerchantOrderId(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getBrand(), is(data.crmWithdrawalEvent.getBrand()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getStatus(), is("RECEIVED"));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getCorrelationId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getType(), is(data.crmWithdrawalEvent.getType()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getSchemaVersion(), is("2.0"));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getClientId(), is(data.crmWithdrawalEvent.getClientId()));
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getPaymentId(), is(paymentId));

        // check rule executions
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRunId(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleVersion(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getDateCreated(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getDateUpdated(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getDateStarted(), is(notNullValue()));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getDateCompleted(), is(notNullValue()));

        Thread.sleep(20_000);
        sendRiskApproveDecision(paymentId);

        checkElementId("Activity_04gzpdk", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        // check final decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb((paymentId), "risk");
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("risk"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getRejectionCode(), is(0));
        assertThat("Verify decisions have right decision ", decision.getFirst().getActor(), is("Auto qa"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getReasonString(), is("Default risk rejection."));
        List<PaymentDecisionsObject> decision2 = getRuleDecisionByWithdrawalIdFromDb((paymentId), "final");
        assertThat("Verify decisions have right decision ", decision2.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision2.getFirst().getDecisionType(), is("final"));
        assertThat("Verify decisions have right decision ", decision2.getFirst().getDecisionCode(), is(1));
        assertThat("Verify decisions have right decision ", decision2.getFirst().getRejectionCode(), is(nullValue()));
        assertThat("Verify decisions have right decision ", decision2.getFirst().getActor(), is("Vindex BO"));
        assertThat("Verify decisions have right decision ", decision2.getFirst().getReasonString(), is(nullValue()));

        // check put payments

        // check withdrawal approval
        List<WithdrawalApprovalsV2> withdrawalApprovals = getWithdrawalApprovalsV2FromKafka(String.valueOf(data.crmWithdrawalEvent.getWithdrawalId()));
        writeLog(withdrawalApprovals);
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getSchemaVersion(), is(data.crmWithdrawalEvent.getSchemaVersion()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getPaymentId(), is(paymentId));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTransferId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getBrand(), is(Brand.VANTAGE.getDisplayName()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getClientId(), is(data.crmWithdrawalEvent.getClientId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getType(), is(data.crmWithdrawalEvent.getType()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getInternalReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getStatus(), is("Approve"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getMerchantOrderId(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getCheckName(), is(data.crmWithdrawalEvent.getCheckName()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRuleName(), is("Router rule"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReasonCode(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRejectionReasonRecommend(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getUnderManualReview(), is(1));
    }

    @Test
    @AllureId("")
    @DisplayName("Router Rule V0. Manual Reject")
    void routerRuleTest17() throws Exception {
        DataHelper data = dataMap.get("17");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("post_pending_decision", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        UUID paymentId = Objects.requireNonNull(getPaymentEvent(data.clientHelper.getUcid())).getPaymentId();

        Thread.sleep(20_000);
        sendRiskRejectDecision(paymentId);

        checkElementId("Activity_05p28in", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
    }

    @Test
    @AllureId("")
    @DisplayName("Router Rule V0. Auto approve")
    void routerRuleTest18() throws Exception {
        DataHelper data = dataMap.get("18");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("put_approve_decision", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());

        checkElementId("Activity_04gzpdk", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_TRANSFER_TO_WA.getProcessId());
    }
}
