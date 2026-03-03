package tests.rule_engine_service_tests.rules.payment.router_rule_crm_events.subrules;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.api.mitigation_service.MitigationServiceRequest.getRestrictionsByUcid;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.asserts.AlertsAssertsHelper.assertThatAlertNotFailed;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.Restriction.DEPOSITS;
import static helpers.data.enums.Restriction.MANUAL_WITHDRAWAL_REVIEW;
import static helpers.data.rules.payments.router_rule_crm_events.WithdrawalNotificationDataFactory.setupWithdrawalNotificationRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static helpers.database.PaymentGateHelper.getPaymentRuleExecution;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;

import business_objects.api.mitigation_service.GetGeneralRestrictionResponseBody;
import business_objects.api.mitigation_service.GetRestrictionResponseBody;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Rule;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_NOTIFICATION_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class WithdrawalNotificationRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupWithdrawalNotificationRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dataMap);
    }

    private void runWithdrawalNotificationTest(String dataKey, String expectedEnd, boolean applyWRRestriction)
            throws Exception {

        DataHelper data = dataMap.get(dataKey);
        setupData(data);

        if (applyWRRestriction) {
            setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());
        }

        produceWithdrawalMessageToCrmEventsTopic(data.getCrmWithdrawalEvent());

        String eventId = data.getCrmWithdrawalEvent().getId();
        checkElementIdSubrule(
                expectedEnd,
                eventId,
                Rule.ROUTER_RULE_CRM_EVENTS.getProcessId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_EVENTS.getProcessId());
        checkElementId("get_rule_executions", eventId, Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());
    }

    @Test
    @AllureId("2185")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit without alert if check name is empty and no restriction. ElementId: Event_end_2")
    void withdrawalNotificationRule10Test() throws Exception {
        runWithdrawalNotificationTest("10", "Event_end_2", false);
    }

    @Test
    @AllureId("2186")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit without alert if check name is empty and no restriction. ElementId: Event_end_2")
    void withdrawalNotificationRule1Test() throws Exception {
        runWithdrawalNotificationTest("1", "Event_end_2", false);
    }

    @Test
    @AllureId("2187")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit without alert if check name is null and no restriction. ElementId: Event_end_2")
    void withdrawalNotificationRule2Test() throws Exception {
        runWithdrawalNotificationTest("2", "Event_end_2", false);
    }

    @Test
    @AllureId("2188")
    @DisplayName(
            "Withdrawal notification rule. Exit without alert if checkname is crypto risk and mirror trade flag = false ElementId: Event_1gdl5i3")
    void withdrawalNotificationRule3Test() throws Exception {
        runWithdrawalNotificationTest("3", "Event_1gdl5i3", false);
    }

    @Test
    @AllureId("2189")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit without alert if checkname is crypto risk and mirror trade flag = true and ucidScoreLatest < ucidScorePenultimate. ElementId: Event_1ywjhdr")
    void withdrawalNotificationRule4Test() throws Exception {
        runWithdrawalNotificationTest("4", "Event_1gdl5i3", false);
    }

    @Test
    @AllureId("2190")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit with alert if checkname is crypto risk and mirror trade flag = true and ucidScoreLatest > ucidScorePenultimate. ElementId: Event_0dvxfab")
    void withdrawalNotificationRule5Test() throws Exception {
        runWithdrawalNotificationTest("5", "Event_0dvxfab", false);
    }

    @Test
    @AllureId("2191")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit with alert if checkname is not crypto risk and no scotland restriction. ElementId: Event_0kmxtqw")
    void withdrawalNotificationRule6Test() throws Exception {
        DataHelper data = dataMap.get("6");
        setupData(data);

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        String eventId = data.getCrmWithdrawalEvent().getId();
        checkElementId("alert", eventId, Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_EVENTS.getProcessId());
        checkElementId("get_rule_executions", eventId, Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "5");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));
    }

    @Test
    @AllureId("2192")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit with alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restrictions >1 . ElementId: end_alert")
    void withdrawalNotificationRule7Test() throws Exception {
        DataHelper data = dataMap.get("7");
        setupData(data);

        setRestrictionAPIGeneral(
                data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        String eventId = data.getCrmWithdrawalEvent().getId();
        checkElementId("alert", eventId, Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_EVENTS.getProcessId());
        checkElementId("get_rule_executions", eventId, Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "5");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));
    }

    @Test
    @AllureId("2193")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit with alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restriction count = 1, profit exceeds 60% of total funding (deposit plus credit). ElementId: end_alert")
    void withdrawalNotificationRule8Test() throws Exception {
        DataHelper data = dataMap.get("8");
        setupData(data);

        setRestrictionAPIGeneral(
                data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId(
                "alert_after_profit_check",
                data.crmWithdrawalEvent.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_EVENTS.getProcessId());
        checkElementId(
                "Event_0kmxtqw",
                data.crmWithdrawalEvent.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_EVENTS.getProcessId());
        checkElementId(
                "get_rule_executions", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "5");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));
    }

    @Test
    @AllureId("2194")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit without alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restrictions count = 1, profit does not exceed 60% of total funding. ElementId: end_103")
    void withdrawalNotificationRule9Test() throws Exception {
        DataHelper data = dataMap.get("9");
        setupData(data);

        setRestrictionAPIGeneral(
                data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId(
                "Event_0hvwbs7",
                data.crmWithdrawalEvent.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_EVENTS.getProcessId());
        checkElementId(
                "scotland_no_alert",
                data.crmWithdrawalEvent.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_EVENTS.getProcessId());
        checkElementId(
                "get_rule_executions", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE_CRM_EVENTS.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "5");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(103));

        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(objectMapper.readValue(
                getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(),
                GetRestrictionResponseBody[].class));
        assertEquals(1, clientRestrictions.size());

        GetGeneralRestrictionResponseBody restriction =
                (GetGeneralRestrictionResponseBody) clientRestrictions.getFirst();
        assertEquals(MANUAL_WITHDRAWAL_REVIEW.getCode(), restriction.getCode());
        assertEquals("CANCELLED", restriction.getStatus());
    }

    @Test
    @AllureId("2412")
    @DisplayName("Withdrawal notification check in Router rule. end 206 if ai flag = true. ElementId: end_206")
    void withdrawalNotificationRule11Test() throws Exception {

        DataHelper data = dataMap.get("11");
        setupData(data);

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementIdSubrule(
                "end_206",
                data.crmWithdrawalEvent.getId(),
                Rule.ROUTER_RULE_CRM_EVENTS.getProcessId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_EVENTS.getProcessId());
        checkElementIdSubrule(
                "process_instance_key",
                data.crmWithdrawalEvent.getId(),
                Rule.ROUTER_RULE_CRM_EVENTS.getProcessId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_EVENTS.getProcessId());
        checkElementIdSubrule(
                "put_rule_execution",
                data.crmWithdrawalEvent.getId(),
                Rule.ROUTER_RULE_CRM_EVENTS.getProcessId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_EVENTS.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "5");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(206));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("risk"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(0));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Withdrawal Review");
        assertThat("Verify alert count", alerts.size(), is(1));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Withdrawal Review");
    }
}
