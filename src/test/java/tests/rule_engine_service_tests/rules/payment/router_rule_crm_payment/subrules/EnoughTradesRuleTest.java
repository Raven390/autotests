package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.subrules;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.api.RestrictionHelper.setRestrictionAPITrade;
import static helpers.asserts.AlertsAssertsHelper.assertThatAlertNotFailed;
import static helpers.data.enums.Restriction.*;
import static helpers.data.rules.payments.router_rule_crm_payment.EnoughTradesDataFactory.setupEnoughTradesRuleData;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static helpers.database.PaymentGateHelper.getPaymentRuleExecution;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static tests.TestBaseRule.*;
import static utils.Constants.*;

import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataDeleteHelper;
import helpers.data.DataHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_ENOUGH_TRADES_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class EnoughTradesRuleTest {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws Exception {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupEnoughTradesRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataDeleteHelper.deleteData(dbDataMap);
    }

    @Test
    @AllureId("1826")
    @DisplayName("Enough trades rule. Account type in event 'IB'. Element id: Event_1f8kqnh")
    void enoughTradesTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_1f8kqnh", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(101));
    }

    @Test
    @AllureId("1828")
    @DisplayName("Enough trades rule. Fund type in event 'PAMM'. Element id: Event_1f8kqnh")
    void enoughTradesTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_1f8kqnh", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(101));
    }

    @Test
    @AllureId("1827")
    @DisplayName("Enough trades rule. Fund type in event 'MAM'. Element id: Event_1f8kqnh")
    void enoughTradesTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_1f8kqnh", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(101));
    }

    @Test
    @AllureId("2004")
    @DisplayName("Enough trades rule. Account type in event 'MTS'. Element id: Event_1f8kqnh")
    void enoughTradMTSesTest31() throws Exception {
        DataHelper data = dbDataMap.get("31");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_1f8kqnh", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(101));
    }

    @Test
    @AllureId("1827")
    @DisplayName("Enough trades rule. Client have Deposits amd Close Only restrictions . Element id: Event_1o3t1d2")
    void enoughTradesTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());
        setRestrictionAPITrade(
                data.clientHelper.getUcid(),
                data.crmTbAccountForMtObject.getAccount(),
                data.crmTbAccountForMtObject.getServerIdSt(),
                CLOSE_ONLY_MODE.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_1o3t1d2", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(109));
    }

    @Test
    @AllureId("2007")
    @DisplayName("Enough trades rule. Client have only Close Only restrictions . Element id: Event_0p0h812")
    void enoughTradesTest41() throws Exception {
        DataHelper data = dbDataMap.get("41");

        setRestrictionAPITrade(
                data.clientHelper.getUcid(),
                data.crmTbAccountForMtObject.getAccount(),
                data.crmTbAccountForMtObject.getServerIdSt(),
                CLOSE_ONLY_MODE.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_0p0h812", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(205));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Enough Trades");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat("Verify alert ", alert.getReason(), is("Close only restriction"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("EXCHANGER"));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Enough Trades");
    }

    @Test
    @AllureId("2003")
    @DisplayName("Enough trades rule. Client not have any deposits . Element id: Event_1mskm4k")
    void enoughTradesTest5() throws Exception {
        DataHelper data = dbDataMap.get("5");

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_1mskm4k", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(109));
    }

    @Test
    @AllureId("2021")
    @DisplayName("Enough trades rule. Clients deposits <=500, no connections . Element id: end_102")
    void enoughTradesTest6() throws Exception {
        DataHelper data = dbDataMap.get("6");

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), MANUAL_WITHDRAWAL_REVIEW.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_102", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(102));
    }

    @Test
    @AllureId("2022")
    @DisplayName("Enough trades rule. Clients withdrawals <=500, no connections . Element id: end_102")
    void enoughTradesTest7() throws Exception {
        DataHelper data = dbDataMap.get("7");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_102", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(102));
    }

    @Test
    @AllureId("2023")
    @DisplayName(
            "Enough trades rule. Clients withdrawals <=500, yes connections. Connected deposits <=500 . Element id: end_103")
    void enoughTradesTest8() throws Exception {
        DataHelper data = dbDataMap.get("8");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_103", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(103));
    }

    @Test
    @AllureId("2024")
    @DisplayName(
            "Enough trades rule. Clients withdrawals <=500, yes connections. Connected withdrawals <=500 . Element id: end_104")
    void enoughTradesTest9() throws Exception {
        DataHelper data = dbDataMap.get("9");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_104", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(104));
    }

    @Test
    @AllureId("2025")
    @DisplayName("Enough trades rule. Clients withdrawals <=500, yes connections. Connected withdrawals > 500 . ")
    void enoughTradesTest10() throws Exception {
        DataHelper data = dbDataMap.get("10");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_conn_sum_withdrawals_by_categories", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("set_date_from_lastNdays", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2005")
    @DisplayName("Enough trades rule. Last N days: withdrawals - credits <= trading profit. end_105")
    void enoughTradesTest11() throws Exception {
        DataHelper data = dbDataMap.get("11");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_105", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(105));
    }

    @Test
    @AllureId("2006")
    @DisplayName("Enough trades rule. Last N days: withdrawals - credits > trading profit.")
    void enoughTradesTest12() throws Exception {
        DataHelper data = dbDataMap.get("12");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_profit", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_rfr", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2013")
    @DisplayName("Enough trades rule. Last N days: RFR / Withdrawal <= 0.02")
    void enoughTradesTest13() throws Exception {
        DataHelper data = dbDataMap.get("13");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_payment_fees", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_floating_trades", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2012")
    @DisplayName("Enough trades rule. Last N days: RFR / Withdrawal > 0.02")
    void enoughTradesTest14() throws Exception {
        DataHelper data = dbDataMap.get("14");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_payment_fees", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_lastNdays_sum_deposits_by_categories", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2011")
    @DisplayName("Enough trades rule.Last N days: RFR >= cost * 0.8O. paymentMethodCode = CRYPTO")
    void enoughTradesTest15() throws Exception {
        DataHelper data = dbDataMap.get("15");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("set_dateFrom_last24h", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2010")
    @DisplayName("Enough trades rule.Last N days: RFR < cost * 0.8O. paymentMethodCode = CRYPTO")
    void enoughTradesTest16() throws Exception {
        DataHelper data = dbDataMap.get("16");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_floating_trades", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2009")
    @DisplayName("Enough trades rule.Last N days: cost < 10. paymentMethodCode != CRYPTO")
    void enoughTradesTest17() throws Exception {
        DataHelper data = dbDataMap.get("17");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("set_dateFrom_last24h", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2014")
    @DisplayName("Enough trades rule.Last N days: cost > 10. paymentMethodCode != CRYPTO")
    void enoughTradesTest18() throws Exception {
        DataHelper data = dbDataMap.get("18");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_floating_trades", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2026")
    @DisplayName("Enough trades rule. Last 24h: turnovers < 4.")
    void enoughTradesTest19() throws Exception {
        DataHelper data = dbDataMap.get("19");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_turnovers", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_last24h_sum_withdrawals_by_categories", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2027")
    @DisplayName("Enough trades rule. Last 24h: turnovers !< 4.")
    void enoughTradesTest20() throws Exception {
        DataHelper data = dbDataMap.get("20");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_last24h_rfr", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2028")
    @DisplayName("Enough trades rule. Last 24h: PM in [card] and event.paymentType not Neteller. end_111")
    void enoughTradesTest21() throws Exception {
        DataHelper data = dbDataMap.get("21");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_111", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(111));
    }

    @Test
    @AllureId("2029")
    @DisplayName("Enough trades rule. Last 24h: PM in [E_WALLET] and event.paymentType not Neteller. end_111")
    void enoughTradesTest22() throws Exception {
        DataHelper data = dbDataMap.get("22");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_111", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(111));
    }

    @Test
    @AllureId("2030")
    @DisplayName("Enough trades rule. Last 24h: PM in [E_WALLET] and event.paymentType is Neteller.")
    void enoughTradesTest23() throws Exception {
        DataHelper data = dbDataMap.get("23");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_last24h_sum_deposits_by_categories", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_last24h_rfr", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2031")
    @DisplayName("Enough trades rule. Last 24h: RFR >= cost*0.8. end_107")
    void enoughTradesTest24() throws Exception {
        DataHelper data = dbDataMap.get("24");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("end_107", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(107));
    }

    @Test
    @AllureId("2033")
    @DisplayName("Enough trades rule. Last 24h: RFR < cost*0.8.")
    void enoughTradesTest25() throws Exception {
        DataHelper data = dbDataMap.get("25");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_floating_trades", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("1886")
    @DisplayName("Enough trades rule. Alert 1  . Element id: Event_1gmc8xt")
    void enoughTradesTestAlert1() throws Exception {
        DataHelper data = dbDataMap.get("770");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_1gmc8xt", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));
    }

    @Test
    @AllureId("1887")
    @DisplayName("Enough trades rule. Alert2 . Element id: Event_1mzlm7b")
    void enoughTradesTestAlert2() throws Exception {
        DataHelper data = dbDataMap.get("771");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_1mzlm7b", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("createETAlertVariable", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(203));
    }

    @Test
    @AllureId("2032")
    @DisplayName("Enough trades rule. Alert 3  . Element id: Event_1gmc8xt. 202 in story with 4 outcomes to reset")
    void enoughTradesTestAlert3() throws Exception {
        DataHelper data = dbDataMap.get("772");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Event_1gmc8xt", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));
    }
}
