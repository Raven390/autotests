package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment;

import business_objects.api.payment_gate.payments_decisions.PutDecisionsRequestBody;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.alerts.RuleAlertV2;
import business_objects.kafka.payment.acknowledgement.Acknowledgement;
import business_objects.kafka.restriction_events.WithdrawalApprovals;
import helpers.data.DataHelper;
import helpers.data.enums.payment_gate.Decision;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.*;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.api.payment_gate.payments_decisions.DecisionsRequests.putDecisions;
import static helpers.data.rules.payments.router_rule_crm_payment.RouterRuleCrmPaymentDataFactory.setupRouterRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.PaymentGateHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_ROUTER_RULE)
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

    @Test
    @AllureId("1621")
    @DisplayName("Router Rule. No alerts/rejects. Approve withdrawal. elementId: Event_0t14mt3")
    void routerRuleTest1() throws Exception {
        DataHelper data = dataMap.get("1");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_end_2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("Event_0t14mt3", data.crmWithdrawalEvent.getId(), "router_rule_crm_payment");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();

        PaymentDetailsObject paymentDetailsObject = getPaymentDetails(data.clientHelper.getUserId());
        assertThat("Assert payment details", paymentDetailsObject.getPaymentId(), is(paymentId));
        assertThat("Assert payment details", paymentDetailsObject.getPaymentId(), is(paymentId));
        assertThat("Assert payment details", paymentDetailsObject.getPayload(), containsString("\"withdrawalAmountUSD\": 1.1"));

        List<Acknowledgement> acknowledgement = getPaymentAcknowledgementFromKafka(data.crmWithdrawalEvent.getId());
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

    @Test
    @AllureId("1621")
    @DisplayName("Router Rule. Alert, no rejects. elementId: Event_11azia8")
    void routerRuleTest2() throws Exception {
        DataHelper data = dataMap.get("2");

        data.crmWithdrawalEvent.setCheckName("Checkname");
        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();

        Allure.step("Send payment rejection");
        PutDecisionsRequestBody putPaymentDecisionBody1 = new PutDecisionsRequestBody();
        putPaymentDecisionBody1.setDecisionType(Decision.RISK_APPROVE.getType());
        putPaymentDecisionBody1.setDecisionCode(Decision.RISK_APPROVE.getCode());
        putPaymentDecisionBody1.setRejectionCode(0);
        putPaymentDecisionBody1.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody1.setActor("Auto qa");
        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody1));

        checkElementId("Event_1gdl5i3", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("send_alert", data.crmWithdrawalEvent.getId(), "router_rule_crm_payment");

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

    @Test
    @AllureId("1622")
    @DisplayName("Router Rule. Alert, no rejects, Risk rejection = true. elementId: Event_1kdk048")
    void routerRuleTest3() throws Exception {
        DataHelper data = dataMap.get("3");

        data.crmWithdrawalEvent.setCheckName("Checkname");
        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();

        Allure.step("Send payment rejection");
        PutDecisionsRequestBody putPaymentDecisionBody1 = new PutDecisionsRequestBody();
        putPaymentDecisionBody1.setDecisionType(Decision.RISK_REJECT.getType());
        putPaymentDecisionBody1.setDecisionCode(Decision.RISK_REJECT.getCode());
        putPaymentDecisionBody1.setRejectionCode(0);
        putPaymentDecisionBody1.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody1.setActor("Auto qa");

        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody1));

        checkElementId("Event_1gdl5i3", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("send_alert", data.crmWithdrawalEvent.getId(), "router_rule_crm_payment");

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

    @Test
    @AllureId("1624")
    @DisplayName("Router rule. Exit with alert if withdrawal has not empty check name and 'Crypto_Risk' mirror flag = true. ElementId:Event_0dvxfab")
    void routerRuleTest4() throws Exception {
        DataHelper data = dataMap.get("4");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        List<PaymentEventsObject> events = getUserPaymentEventsFromDb(data.clientHelper);
        UUID paymentId = events.getFirst().getPaymentId();

        Allure.step("Send payment rejection");
        PutDecisionsRequestBody putPaymentDecisionBody1 = new PutDecisionsRequestBody();
        putPaymentDecisionBody1.setDecisionType(Decision.RISK_REJECT.getType());
        putPaymentDecisionBody1.setDecisionCode(Decision.RISK_REJECT.getCode());
        putPaymentDecisionBody1.setRejectionCode(0);
        putPaymentDecisionBody1.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody1.setActor("Auto qa");

        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody1));

        checkElementId("Event_1gdl5i3", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("Event_1kdk048", data.crmWithdrawalEvent.getId(), "router_rule_crm_payment");

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

    @Test
    @AllureId("1623")
    @DisplayName("Router rule. Exit with alert if withdrawal has not empty check name and 'Crypto_Risk' mirror flag = false. ElementId:Event_1gdl5i3")
    void routerRuleTest5() throws Exception {
        DataHelper data = dataMap.get("5");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_end_2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr_payment");
        checkElementId("Event_0t14mt3", data.crmWithdrawalEvent.getId(), "router_rule_crm_payment");

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
}
