package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment;

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
import static helpers.data.rules.payments.router_rule_crm_payment.WithdrawalNotificationDataFactory.setupWithdrawalNotificationRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static helpers.database.PaymentGateHelper.getPaymentRuleExecution;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_RULE_ENGINE_RULES_TESTS;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_NOTIFICATION_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)

class Scotland4Test extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupWithdrawalNotificationRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dataMap);
    }

    @Test
    @AllureId("1924")
    @DisplayName("Scotland . Exit with alert if check name is not 'Crypto_Risk' and Scotland WR restriction not exists. ElementId: Event_1gdl12i3")
    void withdrawalNotificationRule1Test() throws Exception {
        DataHelper data = dataMap.get("1");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("alert", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "scotland4");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "12");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(12));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));
    }

    @Test
    @AllureId("1925")
    @DisplayName("Scotland . Exit with alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restrictions >1 . ElementId: Event_1gdl12i3")
    void withdrawalNotificationRule2Test() throws Exception {
        DataHelper data = dataMap.get("2");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("alert", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "scotland4");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "12");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(12));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));
    }

    @Test
    @AllureId("1926")
    @DisplayName("Scotland . Exit with alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restrictions >1 . ElementId: Event_1gdl12i3")
    void withdrawalNotificationRule3Test() throws Exception {
        DataHelper data = dataMap.get("3");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("alert_after_profit_check", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "scotland4");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "12");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(12));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));
    }

    @Test
    @AllureId("1927")
    @DisplayName("Scotland . Exit without alert if check name is not 'Crypto_Risk' and Scotland WR restriction exists, restrictions count == 1 . ElementId: Event_1gdl12i3")
    void withdrawalNotificationRule4Test() throws Exception {
        DataHelper data = dataMap.get("4");
        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode(), "Mirror trade pattern");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_0hvwbs7", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("process_instance_key", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("put_rule_execution", data.crmWithdrawalEvent.getId(), "scotland4");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEvent.getId(), "scotland4");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "12");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(12));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(103));

        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(
                objectMapper.readValue(getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(), GetRestrictionResponseBody[].class));
        assertEquals(1, clientRestrictions.size());

        GetRestrictionResponseBody restriction = clientRestrictions.getFirst();
        assertEquals(MANUAL_WITHDRAWAL_REVIEW.getCode(), restriction.getCode());
        assertEquals("CANCELLED", restriction.getStatus());

    }
}

