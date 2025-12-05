package tests.rule_engine_service_tests.rules.trading.router_rule_crm_events;

import business_objects.api.mitigation_service.GetRestrictionResponseBody;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import helpers.data.DataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.*;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.api.mitigation_service.MitigationServiceRequest.getRestrictionsByUcid;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.data.enums.Restriction.DEPOSITS;
import static helpers.data.enums.Restriction.MANUAL_WITHDRAWAL_REVIEW;
import static helpers.data.rules.payments.router_rule_crm_payment.RrEventsWithdrawalNotificationDataFactory.setupRrEventsWithdrawalNotificationData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static helpers.database.PaymentGateHelper.getPaymentRuleExecution;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_NOTIFICATION_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class WithdrawalNotificationRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupRrEventsWithdrawalNotificationData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dataMap);
    }

    @Test
    @AllureId("1913")
    @DisplayName("Withdrawal notification check in Router rule. Exit without alert if check name is empty and no restriction. ElementId: Event_end_2")
    void withdrawalNotificationRule1Test() throws Exception {
        DataHelper data = dataMap.get("1");

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId("Event_end_2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(101));
    }

    @Test
    @AllureId("1914")
    @DisplayName("Withdrawal notification check in Router rule. Exit without alert if check name is null and no restriction. ElementId: Event_end_2")
    void withdrawalNotificationRule2Test() throws Exception {
        DataHelper data = dataMap.get("2");

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId("Event_end_2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(101));
    }


    @Test
    @AllureId("1915")
    @DisplayName("Withdrawal notification rule. Scotland . Exit with alert if check name is not 'Crypto_Risk' and Scotland WR restriction not exists. ElementId: Event_1gdl5i3")
    void withdrawalNotificationRule5Test() throws Exception {
        DataHelper data = dataMap.get("5");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId("alert", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));
    }

    @Test
    @AllureId("1916")
    @DisplayName("Withdrawal notification rule. Scotland . Exit with alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restrictions >1 . ElementId: Event_1gdl5i3")
    void withdrawalNotificationRule6Test() throws Exception {
        DataHelper data = dataMap.get("6");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId("alert", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));
    }

    @Test
    @AllureId("1917")
    @DisplayName("Withdrawal notification rule. Scotland . Exit with alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restrictions >1 . ElementId: Event_1gdl5i3")
    void withdrawalNotificationRule7Test() throws Exception {
        DataHelper data = dataMap.get("7");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId("alert_after_profit_check", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));
    }

    @Test
    @AllureId("1918")
    @DisplayName("Withdrawal notification rule. Scotland . Exit without alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restrictions 1 . ElementId: Event_1gdl5i3")
    void withdrawalNotificationRule8Test() throws Exception {
        DataHelper data = dataMap.get("8");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId("Event_0hvwbs7", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(103));

        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(
                objectMapper.readValue(getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(), GetRestrictionResponseBody[].class));
        assertEquals(1, clientRestrictions.size());

        GetRestrictionResponseBody restriction = clientRestrictions.getFirst();
        assertEquals(MANUAL_WITHDRAWAL_REVIEW.getCode(), restriction.getCode());
        assertEquals("CANCELLED", restriction.getStatus());

    }

    @Test
    @AllureId("1919")
    @DisplayName("Withdrawal notification rule. Mirror Trading Mirror Flag = False")
    void withdrawalNotificationRule9Test() throws Exception {
        DataHelper data = dataMap.get("9");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1gdl5i3", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_10opua2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(103));
    }

    @Test
    @AllureId("1920")
    @DisplayName("Withdrawal notification rule. Mirror Trading Mirror Trading Mirror Flag = True, Ucid score not rose")
    void withdrawalNotificationRule10Test() throws Exception {
        DataHelper data = dataMap.get("10");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId("Gateway_10fzxzw", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_10opua2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(103));
    }

    @Test
    @AllureId("1921")
    @DisplayName("Withdrawal notification rule. Mirror Trading Mirror Trading Mirror Flag = True, Ucid score rise")
    void withdrawalNotificationRule11Test() throws Exception {
        DataHelper data = dataMap.get("11");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId("Event_0dvxfab", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(204));
    }

//    @Disabled//reserved for old flow tests
    @Test
    @AllureId("1922")
    @DisplayName("OLD FLOW Withdrawal notification rule. Mirror Trading Mirror Trading Mirror Trade on last WD = False")
    void withdrawalNotificationRule12Test() throws Exception {
        DataHelper data = dataMap.get("12");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId("Event_10opua2", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(103));
    }

    //    @Disabled//reserved for old flow tests
    @Test
    @AllureId("1923")
    @DisplayName("OLD FLOW Withdrawal notification rule. Mirror Trading Mirror Trading Mirror Trade on last WD = True")
    void withdrawalNotificationRule13Test() throws Exception {
        DataHelper data = dataMap.get("13");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());

        produceWithdrawalMessageToCrmEventsTopic(data.crmWithdrawalEvent);

        checkElementId("Event_0dvxfab", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "withdrawal_notification_rr");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(5));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(204));
    }
}
