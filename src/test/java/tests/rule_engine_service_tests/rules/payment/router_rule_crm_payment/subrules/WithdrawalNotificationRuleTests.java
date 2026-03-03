package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.subrules;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.api.mitigation_service.MitigationServiceRequest.getRestrictionsByUcid;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.asserts.AlertsAssertsHelper.assertThatAlertNotFailed;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.Restriction.DEPOSITS;
import static helpers.data.enums.Restriction.MANUAL_WITHDRAWAL_REVIEW;
import static helpers.data.rules.payments.router_rule_crm_payment.WithdrawalNotificationDataFactory.setupWithdrawalNotificationRuleData;
import static helpers.database.DbHelper.*;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static helpers.database.PaymentGateHelper.getPaymentRuleExecution;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;

import business_objects.api.mitigation_service.GetGeneralRestrictionResponseBody;
import business_objects.api.mitigation_service.GetRestrictionResponseBody;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Rule;
import io.qameta.allure.*;
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

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.getCrmWithdrawalEventV2());

        String eventId = data.getCrmWithdrawalEventV2().getId();
        checkElementIdSubrule(
                expectedEnd,
                eventId,
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_PAYMENT.getProcessId());
        checkElementId("Activity_0n9s6ck", eventId, Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("2253")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit without alert if check name is empty and no restriction. ElementId: end_101")
    void withdrawalNotificationRule1Test() throws Exception {
        runWithdrawalNotificationTest("1", "end_101", false);
    }

    @Test
    @AllureId("2254")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit without alert if check name is null and no restriction. ElementId: end_101")
    void withdrawalNotificationRule2Test() throws Exception {
        runWithdrawalNotificationTest("2", "end_101", false);
    }

    @Test
    @AllureId("2255")
    @DisplayName("Withdrawal notification rule. Exit with alert if check name is not empty. ElementId: end_alert")
    void withdrawalNotificationRule3Test() throws Exception {
        runWithdrawalNotificationTest("3", "end_alert", false);
    }

    @Test
    @AllureId("2256")
    @DisplayName(
            "Withdrawal notification rule. Exit with alert if check name is empty and WR restriction exists. ElementId: end_alert")
    void withdrawalNotificationRule4Test() throws Exception {
        runWithdrawalNotificationTest("4", "end_alert", true);
    }

    @Test
    @AllureId("2257")
    @DisplayName(
            "Withdrawal notification rule. Exit with alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restrictions >1 . ElementId: end_alert")
    void withdrawalNotificationRuleScotlandPattern5Test() throws Exception {
        DataHelper data = dataMap.get("5");
        setupData(data);

        setRestrictionAPIGeneral(
                data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        String eventId = data.getCrmWithdrawalEventV2().getId();
        checkElementId("end_alert", eventId, Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_PAYMENT.getProcessId());
        checkElementId("Activity_0n9s6ck", eventId, Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

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
    @AllureId("2258")
    @DisplayName(
            "Withdrawal notification rule. Exit with alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restriction count = 1, profit exceeds 60% of total funding (deposit plus credit). ElementId: end_alert")
    void withdrawalNotificationRuleScotlandPattern6Test() throws Exception {
        DataHelper data = dataMap.get("6");
        setupData(data);

        setRestrictionAPIGeneral(
                data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "profit_check",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_PAYMENT.getProcessId());
        checkElementId(
                "end_alert",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_PAYMENT.getProcessId());
        checkElementId(
                "end",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_PAYMENT.getProcessId());

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
    @AllureId("2259")
    @DisplayName(
            "Withdrawal notification rule. Exit without alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restrictions count = 1, profit does not exceed 60% of total funding. ElementId: end_103")
    void withdrawalNotificationRuleScotlandPattern7Test() throws Exception {
        DataHelper data = dataMap.get("7");
        setupData(data);

        setRestrictionAPIGeneral(
                data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "profit_check",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_PAYMENT.getProcessId());
        checkElementId(
                "end_103",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_PAYMENT.getProcessId());
        checkElementId(
                "end",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_PAYMENT.getProcessId());

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
    @AllureId("2411")
    @DisplayName(
            "Withdrawal notification check in Router rule. Exit with end 206 ai flag = true. ElementId: Event_1g3nnu4")
    void withdrawalNotificationRule8Test() throws Exception {

        DataHelper data = dataMap.get("8");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);
        checkElementIdSubrule(
                "Event_1g3nnu4",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_PAYMENT.getProcessId());
        checkElementIdSubrule(
                "process_instance_key",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_PAYMENT.getProcessId());
        checkElementIdSubrule(
                "put_rule_execution",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId(),
                Rule.ROUTER_RULE_WITHDRAWAL_NOTIFICATION_CRM_PAYMENT.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "5");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(206));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Withdrawal Review");
        assertThat("Verify alert count", alerts.size(), is(1));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Withdrawal Review");
    }
}
