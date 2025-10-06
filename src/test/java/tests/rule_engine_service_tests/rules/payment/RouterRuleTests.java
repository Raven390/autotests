package tests.rule_engine_service_tests.rules.payment;

import business_objects.api.clickhouse_api_service.get_rates_usd.GetRatesUsdResponse;
import business_objects.api.payment_gate.decisions.PutDecisionsRequestBody;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.db.payment_gate.tmp_rule_decisions.TmpRuleDecisionsObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.payment.acknowledgement.Acknowledgement;
import business_objects.kafka.restriction_events.WithdrawalApprovals;
import helpers.data.DataHelper;
import helpers.data.enums.payment_gate.Decision;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.*;

import static business_objects.api.clickhouse_api_service.get_rates_usd.GetRatesUsdRequest.getRateUsd;
import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.api.payment_gate.decisions.DecisionsRequests.putDecisions;
import static helpers.data.rules.payments.RouterRuleDataFactory.setupRouterRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.PaymentGateHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class RouterRuleTests extends TestBaseRule {

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

        produceWithdrawalMessageToKafka(data.crmWithdrawalEvent);

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();

        PaymentDetailsObject paymentDetailsObject = getPaymentDetails(data.clientHelper.getUserId());
        assertThat("Assert payment details", paymentDetailsObject.getPaymentId(), is(paymentId));
        assertThat("Assert payment details", paymentDetailsObject.getPaymentId(), is(paymentId));

        List<Acknowledgement> acknowledgement = getPaymentAcknowledgementFromKafka(data.crmWithdrawalEvent.getId());
        assertThat("Assert acknowledgement", acknowledgement.getFirst().getPaymentId(), is(paymentId.toString()));

        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));

        List<PaymentDecisionsObject> paymentDecisionsObject = getPaymentDecisionsByPaymentId(paymentId);
        assertThat("Assert decisionType", paymentDecisionsObject.getFirst().getDecisionType(), is("risk"));
        assertThat("Assert decisionId", paymentDecisionsObject.getFirst().getDecisionCode(), is(1));

        assertThat("Assert final_decision_id", paymentEventsObject.getFinalDecisionId(), is(2));

        List<WithdrawalApprovals> withdrawalApprovals = getWithdrawalApprovalsFromKafka(String.valueOf(data.crmWithdrawalEvent.getWithdrawalId()));
        System.out.println(withdrawalApprovals);
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getMessageId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTransferId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getBrand(), is(data.crmWithdrawalEvent.getBrand()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getInternalReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getStatus(), is("Approve"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getOrderNumber(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getCheckName(), is(""));

        checkElementId("Event_end_2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0t14mt3", data.crmWithdrawalEvent.getId(), "router_rule");
    }

    @Test
    @AllureId("1621")
    @DisplayName("Router Rule. Alert, no rejects, payment rejection = true. elementId: Event_11azia8")
    void routerRuleTest2() throws Exception {
        DataHelper data = dataMap.get("2");

        data.crmWithdrawalEvent.setCheckName("Checkname");
        produceWithdrawalMessageToKafka(data.crmWithdrawalEvent);

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();

        List<PaymentDecisionsObject> paymentDecisionsObject = getPaymentDecisionsByPaymentId(paymentId);
        assertThat("Assert decisionType", paymentDecisionsObject.getFirst().getDecisionType(), is("risk"));
        assertThat("Assert decisionId", paymentDecisionsObject.getFirst().getDecisionCode(), is(0));

        Allure.step("Send payment rejection");
        PutDecisionsRequestBody putPaymentDecisionBody1 = new PutDecisionsRequestBody();
        putPaymentDecisionBody1.setDecisionType(Decision.PAYMENT_REJECT.getType());
        putPaymentDecisionBody1.setDecisionCode(Decision.PAYMENT_REJECT.getCode());
        putPaymentDecisionBody1.setRejectionCode(0);
        putPaymentDecisionBody1.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody1.setActor("Auto qa");

        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody1));

        List<WithdrawalApprovals> withdrawalApprovals = getWithdrawalApprovalsFromKafka(String.valueOf(data.crmWithdrawalEvent.getWithdrawalId()));
        System.out.println(withdrawalApprovals);
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getMessageId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTransferId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getBrand(), is(data.crmWithdrawalEvent.getBrand()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getInternalReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getStatus(), is("Refuse"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getOrderNumber(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getCheckName(), is(data.crmWithdrawalEvent.getCheckName()));

        checkElementId("Event_1waht3m", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("send_alert", data.crmWithdrawalEvent.getId(), "router_rule");
    }

    @Test
    @AllureId("1622")
    @DisplayName("Router Rule. Alert, no rejects, Risk rejection = true. elementId: Event_1kdk048")
    void routerRuleTest3() throws Exception {
        DataHelper data = dataMap.get("3");

        data.crmWithdrawalEvent.setCheckName("Checkname");
        produceWithdrawalMessageToKafka(data.crmWithdrawalEvent);

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();

        List<PaymentDecisionsObject> paymentDecisionsObject = getPaymentDecisionsByPaymentId(paymentId);
        assertThat("Assert decisionType", paymentDecisionsObject.getFirst().getDecisionType(), is("risk"));
        assertThat("Assert decisionId", paymentDecisionsObject.getFirst().getDecisionCode(), is(0));

        Allure.step("Send payment rejection");
        PutDecisionsRequestBody putPaymentDecisionBody1 = new PutDecisionsRequestBody();
        putPaymentDecisionBody1.setDecisionType(Decision.RISK_REJECT.getType());
        putPaymentDecisionBody1.setDecisionCode(Decision.RISK_REJECT.getCode());
        putPaymentDecisionBody1.setRejectionCode(0);
        putPaymentDecisionBody1.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody1.setActor("Auto qa");

        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody1));

        List<WithdrawalApprovals> withdrawalApprovals = getWithdrawalApprovalsFromKafka(String.valueOf(data.crmWithdrawalEvent.getWithdrawalId()));
        System.out.println(withdrawalApprovals);
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getMessageId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getTransferId(), is(data.crmWithdrawalEvent.getWithdrawalId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getBrand(), is(data.crmWithdrawalEvent.getBrand()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getRegulator(), is(data.crmWithdrawalEvent.getRegulator()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getInternalReason(), is(""));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getStatus(), is("Refuse"));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getOrderNumber(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Assert withdrawal.approval message", withdrawalApprovals.getFirst().getCheckName(), is(data.crmWithdrawalEvent.getCheckName()));

        checkElementId("Event_1waht3m", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("send_alert", data.crmWithdrawalEvent.getId(), "router_rule");
    }

    @Test
    @AllureId("1624")
    @DisplayName("Withdrawal notification rule. Exit with alert if withdrawal has not empty check name and 'Crypto_Risk' mirror flag = true. ElementId:Event_0dvxfab")
    void routerRuleTest4() throws Exception {
        DataHelper data = dataMap.get("4");

        produceWithdrawalMessageToKafka(data.crmWithdrawalEvent);

        List<PaymentEventsObject> events = getUserPaymentEventsFromDb(data.clientHelper);
        UUID paymentId = events.getFirst().getPaymentId();
        assertThat("Verify amount of payments events in DB", events.size(), is(1));
        assertEquals(data.crmWithdrawalEvent.getWithdrawalId(), Long.valueOf(events.getFirst().getCrmId()));

        Allure.step("Send payment rejection");
        PutDecisionsRequestBody putPaymentDecisionBody1 = new PutDecisionsRequestBody();
        putPaymentDecisionBody1.setDecisionType(Decision.RISK_REJECT.getType());
        putPaymentDecisionBody1.setDecisionCode(Decision.RISK_REJECT.getCode());
        putPaymentDecisionBody1.setRejectionCode(0);
        putPaymentDecisionBody1.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody1.setActor("Auto qa");

        Thread.sleep(5000);
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Withdrawal Review");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert name", alerts.getFirst().rule.name, is("Withdrawal Review"));
        assertThat("Verify alert ucid", alerts.getFirst().ucid, is(data.clientHelper.getUcid()));

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("currency", data.crmWithdrawalEvent.getWithdrawalCurrency());
        Response response = getRateUsd(queryParams);
        Double rateUsd = Arrays.stream(objectMapper.readValue(response.body().string(), GetRatesUsdResponse[].class)).toList().getFirst().getRateUsd();

        assertThat("Verify alert attributes", alerts.getFirst().rule.attributes.rateUSD, is(rateUsd.toString()));
        assertThat("Verify alert attributes", alerts.getFirst().rule.attributes.amountUSD, is(String.valueOf(data.crmWithdrawalEvent.getWithdrawalAmount() * rateUsd)));
        assertThat("Verify alert attributes", alerts.getFirst().rule.attributes.paymentId, is(paymentId.toString()));

        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody1));

        List<TmpRuleDecisionsObject> decision = getTempRuleDecisionByWithdrawalIdFromDb((events.getFirst().getPaymentId()));
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().decision, is("ALERT"));

        checkElementId("Event_0dvxfab", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_1kdk048", data.crmWithdrawalEvent.getId(), "router_rule");
    }

    @Test
    @AllureId("1623")
    @DisplayName("Withdrawal notification rule. Exit with alert if withdrawal has not empty check name and 'Crypto_Risk' mirror flag = false. ElementId:Event_1gdl5i3")
    void routerRuleTest5() throws Exception {
        DataHelper data = dataMap.get("5");

        produceWithdrawalMessageToKafka(data.crmWithdrawalEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "withdrawalNotification");

        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<PaymentEventsObject> events = getUserPaymentEventsFromDb(data.clientHelper);
        assertThat("Verify amount of payments events in DB", events.size(), is(1));
        assertEquals(data.crmWithdrawalEvent.getWithdrawalId(), Long.valueOf(events.getFirst().getCrmId()));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper, "withdrawalNotification");
        assertThat("Verify amount of alerts in DB", dbAlerts.size(), is(0));

        List<TmpRuleDecisionsObject> decision = getTempRuleDecisionByWithdrawalIdFromDb((events.getFirst().getPaymentId()));
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().decision, is("AUTO_APPROVE"));

        List<WithdrawalApprovals> approval = getWithdrawalApprovalsFromKafka(String.valueOf(data.crmWithdrawalEvent.getWithdrawalId()));

        assertThat("Verify amount of approvals in Kafka topic", approval.size(), is(1));
        assertThat("Verify status of approval in Kafka topic", approval.getFirst().getStatus(), is("Approve"));
        assertThat("Verify status of checkName in Kafka topic", approval.getFirst().getCheckName(), is("Crypto_Risk"));
        assertThat("Verify checkName in Kafka topic", approval.getFirst().getCheckName(), is("Crypto_Risk"));
        assertThat("Verify orderNumber in Kafka topic", approval.getFirst().getOrderNumber(), is(data.crmWithdrawalEvent.getMerchantOrderId()));

        checkElementId("Event_1gdl5i3", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0t14mt3", data.crmWithdrawalEvent.getId(), "router_rule");
    }
}
