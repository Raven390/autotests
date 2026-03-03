package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.subrules;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.api.RestrictionHelper.setRestrictionAPIGeneral;
import static helpers.api.RestrictionHelper.setRestrictionAPITrade;
import static helpers.asserts.AlertsAssertsHelper.assertThatAlertNotFailed;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.Restriction.*;
import static helpers.data.rules.payments.router_rule_crm_payment.EnoughTradesDataFactory.setupEnoughTradesRuleData;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static helpers.database.PaymentGateHelper.getPaymentRuleExecution;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static tests.TestBaseRule.*;
import static utils.Constants.*;

import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataDeleteHelper;
import helpers.data.DataHelper;
import helpers.data.enums.FraudTypeStatus;
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
    @AllureId("2153")
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
    @AllureId("2154")
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
    @AllureId("2155")
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
    @AllureId("2156")
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
    @AllureId("2413")
    @DisplayName(
            "Enough trades rule. Client have Deposits amd Close Only restrictions. fraud EXCHANGER. Element id: Event_1o3t1d2")
    void enoughTradesTest401() throws Exception {
        DataHelper data = dbDataMap.get("401");

        addFraudForClient(data.clientHelper, EXCHANGER, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());
        setRestrictionAPITrade(
                data.clientHelper.getUcid(),
                data.crmTbAccountForMtObject.getAccount(),
                data.crmTbAccountForMtObject.getServerIdSt(),
                CLOSE_ONLY_MODE.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementIdSubrule(
                "Event_1o3t1d2",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementIdSubrule(
                "Event_0sg27lc",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");

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
    @AllureId("2414")
    @DisplayName(
            "Enough trades rule. Client have Deposits amd Close Only restrictions. fraud UPGRADER . Element id: Event_1o3t1d2")
    void enoughTradesTest402() throws Exception {
        DataHelper data = dbDataMap.get("402");

        addFraudForClient(data.clientHelper, UPGRADER, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());
        setRestrictionAPITrade(
                data.clientHelper.getUcid(),
                data.crmTbAccountForMtObject.getAccount(),
                data.crmTbAccountForMtObject.getServerIdSt(),
                CLOSE_ONLY_MODE.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementIdSubrule(
                "Event_1o3t1d2",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementIdSubrule(
                "Event_0sg27lc",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");

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
    @AllureId("2415")
    @DisplayName(
            "Enough trades rule. Client have Deposits amd Close Only restrictions. fraud CHARGEBACK . Element id: Event_1o3t1d2")
    void enoughTradesTest403() throws Exception {
        DataHelper data = dbDataMap.get("403");

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());
        setRestrictionAPITrade(
                data.clientHelper.getUcid(),
                data.crmTbAccountForMtObject.getAccount(),
                data.crmTbAccountForMtObject.getServerIdSt(),
                CLOSE_ONLY_MODE.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementIdSubrule(
                "Event_1o3t1d2",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementIdSubrule(
                "Event_0sg27lc",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");

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
    @AllureId("2416")
    @DisplayName(
            "Enough trades rule. Client have Deposits amd Close Only restrictions. fraud MONEY_LAUNDRY . Element id: Event_1o3t1d2")
    void enoughTradesTest404() throws Exception {
        DataHelper data = dbDataMap.get("404");

        addFraudForClient(data.clientHelper, MONEY_LAUNDRY, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());
        setRestrictionAPITrade(
                data.clientHelper.getUcid(),
                data.crmTbAccountForMtObject.getAccount(),
                data.crmTbAccountForMtObject.getServerIdSt(),
                CLOSE_ONLY_MODE.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementIdSubrule(
                "Event_1o3t1d2",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementIdSubrule(
                "Event_0sg27lc",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");

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
    @AllureId("2417")
    @DisplayName(
            "Enough trades rule. Client have Deposits amd Close Only restrictions. fraud CLAIMER . Element id: Event_1o3t1d2")
    void enoughTradesTest405() throws Exception {
        DataHelper data = dbDataMap.get("405");

        addFraudForClient(data.clientHelper, CLAIMER, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());
        setRestrictionAPITrade(
                data.clientHelper.getUcid(),
                data.crmTbAccountForMtObject.getAccount(),
                data.crmTbAccountForMtObject.getServerIdSt(),
                CLOSE_ONLY_MODE.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementIdSubrule(
                "Event_1o3t1d2",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementIdSubrule(
                "Event_0sg27lc",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");

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
    @AllureId("2418")
    @DisplayName(
            "Enough trades rule. Client have Deposits amd Close Only restrictions. fraud ATO . Element id: Event_1o3t1d2")
    void enoughTradesTest406() throws Exception {
        DataHelper data = dbDataMap.get("406");

        addFraudForClient(data.clientHelper, ATO, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);

        setRestrictionAPIGeneral(data.clientHelper.getUcid(), DEPOSITS.getCode());
        setRestrictionAPITrade(
                data.clientHelper.getUcid(),
                data.crmTbAccountForMtObject.getAccount(),
                data.crmTbAccountForMtObject.getServerIdSt(),
                CLOSE_ONLY_MODE.getCode());

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementIdSubrule(
                "Event_1o3t1d2",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementIdSubrule(
                "Event_0sg27lc",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");

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
    @AllureId("2158")
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
    @AllureId("2159")
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
    @AllureId("2160")
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
    @AllureId("2161")
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
    @AllureId("2162")
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
    @AllureId("2163")
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
    @AllureId("2164")
    @DisplayName("Enough trades rule. Clients withdrawals <=500, yes connections. Connected withdrawals > 500 . ")
    void enoughTradesTest10() throws Exception {
        DataHelper data = dbDataMap.get("10");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_conn_sum_withdrawals_by_categories", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("set_date_from_lastNdays", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2165")
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
    @AllureId("2166")
    @DisplayName("Enough trades rule. Last N days: withdrawals - credits > trading profit.")
    void enoughTradesTest12() throws Exception {
        DataHelper data = dbDataMap.get("12");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_profit", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_rfr", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2167")
    @DisplayName("Enough trades rule. Last N days: SR / Withdrawal <= 0.02")
    void enoughTradesTest13() throws Exception {
        DataHelper data = dbDataMap.get("13");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_payment_fees", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_floating_trades", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2168")
    @DisplayName("Enough trades rule. Last N days: SR / Withdrawal > 0.02")
    void enoughTradesTest14() throws Exception {
        DataHelper data = dbDataMap.get("14");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_payment_fees", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_lastNdays_sum_deposits_by_categories", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2169")
    @DisplayName("Enough trades rule.Last N days: SR >= cost * 0.8O. paymentMethodCode = CRYPTO")
    void enoughTradesTest15() throws Exception {
        DataHelper data = dbDataMap.get("15");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("set_dateFrom_last24h", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2170")
    @DisplayName("Enough trades rule.Last N days: SR < cost * 0.8O. paymentMethodCode = CRYPTO")
    void enoughTradesTest16() throws Exception {
        DataHelper data = dbDataMap.get("16");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_floating_trades", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2171")
    @DisplayName("Enough trades rule.Last N days: cost < 10. paymentMethodCode != CRYPTO")
    void enoughTradesTest17() throws Exception {
        DataHelper data = dbDataMap.get("17");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("set_dateFrom_last24h", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2172")
    @DisplayName("Enough trades rule.Last N days: cost > 10. paymentMethodCode != CRYPTO")
    void enoughTradesTest18() throws Exception {
        DataHelper data = dbDataMap.get("18");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_floating_trades", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2173")
    @DisplayName("Enough trades rule. Last 24h: turnovers < 4.")
    void enoughTradesTest19() throws Exception {
        DataHelper data = dbDataMap.get("19");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_turnovers", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_last24h_sum_withdrawals_by_categories", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2174")
    @DisplayName("Enough trades rule. Last 24h: turnovers !< 4.")
    void enoughTradesTest20() throws Exception {
        DataHelper data = dbDataMap.get("20");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("set_cost", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_last24h_rfr", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2175")
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
    @AllureId("2176")
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
    @AllureId("2177")
    @DisplayName("Enough trades rule. Last 24h: PM in [E_WALLET] and event.paymentType is Neteller.")
    void enoughTradesTest23() throws Exception {
        DataHelper data = dbDataMap.get("23");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_last24h_sum_deposits_by_categories", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("get_last24h_rfr", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2178")
    @DisplayName("Enough trades rule. Last 24h: SR >= cost*0.8. end_107")
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
    @AllureId("2179")
    @DisplayName("Enough trades rule. Last 24h: SR < cost*0.8.")
    void enoughTradesTest25() throws Exception {
        DataHelper data = dbDataMap.get("25");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("get_floating_trades", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");
    }

    @Test
    @AllureId("2180")
    @DisplayName("Enough trades rule. Lifetime: 'cost' < SR*0.7. end 113")
    void enoughTradesTest26() throws Exception {
        DataHelper data = dbDataMap.get("26");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Activity_1ve369z", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("end_113", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(113));
    }

    @Test
    @AllureId("2181")
    @DisplayName("Enough trades rule. Two last month: 'cost' >= SR*0.7. end 113")
    void enoughTradesTest27() throws Exception {
        DataHelper data = dbDataMap.get("27");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId("Activity_0sstcik", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_16krycn", data.crmWithdrawalEventV2.getId(), "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(113));
    }

    @Test
    @AllureId("2470")
    @DisplayName("Enough trades rule.No Alert. Previous end = 202, decision 2 . Element id: Event_1en3mz7")
    void enoughTradesTest28() throws Exception {
        DataHelper data = dbDataMap.get("28");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementIdSubrule(
                "Event_1en3mz7",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementIdSubrule(
                "put_rule_execution",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
    }

    @Test
    @AllureId("2182")
    @DisplayName("Enough trades rule. Alert 1  . Element id: Event_1gmc8xt")
    void enoughTradesTestAlert1() throws Exception {
        DataHelper data = dbDataMap.get("770");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementIdSubrule(
                "Event_1gmc8xt",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementIdSubrule(
                "Event_0sg27lc",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));

        // Verify alert kafka
        Allure.step("Get alerts kafka messages");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlertV2 alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlertV2.class);
        assertThat("Verify alert id not null", alert.getAlertId(), notNullValue());
        assertThat("Verify timestamp not null", alert.getTimestamp(), notNullValue());
        assertThat("Verify ucid is correct", alert.getUcid(), equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.getRule(), notNullValue());
        assertThat("Verify rule ver not null", alert.getRule().getVer(), notNullValue());
        assertThat("Verify rule name is correct", alert.getRule().getName(), equalTo("Enough Trades"));
        assertThat("Verify rule trigger is correct", alert.getTrigger(), equalTo("Withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.getFraudType(), equalTo(EXCHANGER.getCode()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.getAttributes(), notNullValue());
        assertThat("Verify rule attributes not null", alert.getAttributes().getStage(), is("1"));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Enough Trades");
    }

    @Test
    @AllureId("2183")
    @DisplayName("Enough trades rule. Alert2 . Element id: Event_1mzlm7b")
    void enoughTradesTestAlert2() throws Exception {
        DataHelper data = dbDataMap.get("771");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementIdSubrule(
                "Event_1mzlm7b",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementIdSubrule(
                "createETAlertVariable",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementIdSubrule(
                "Event_0sg27lc",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(203));

        // Verify alert kafka
        Allure.step("Get alerts kafka messages");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlertV2 alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlertV2.class);
        assertThat("Verify alert id not null", alert.getAlertId(), notNullValue());
        assertThat("Verify timestamp not null", alert.getTimestamp(), notNullValue());
        assertThat("Verify ucid is correct", alert.getUcid(), equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.getRule(), notNullValue());
        assertThat("Verify rule ver not null", alert.getRule().getVer(), notNullValue());
        assertThat("Verify rule name is correct", alert.getRule().getName(), equalTo("Enough Trades"));
        assertThat("Verify rule trigger is correct", alert.getTrigger(), equalTo("Withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.getFraudType(), equalTo(EXCHANGER.getCode()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.getAttributes(), notNullValue());
        assertThat("Verify rule attributes not null", alert.getAttributes().getStage(), is("3"));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Enough Trades");
    }

    @Test
    @AllureId("2184")
    @DisplayName("Enough trades rule. Alert 3  . Element id: Event_1gmc8xt. 202 in story with 4 outcomes to reset")
    void enoughTradesTestAlert3() throws Exception {
        DataHelper data = dbDataMap.get("772");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementIdSubrule(
                "Event_1gmc8xt",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementId("Event_0sg27lc", data.crmWithdrawalEventV2.getId(), "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));

        // Verify alert kafka
        Allure.step("Get alerts kafka messages");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlertV2 alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlertV2.class);
        assertThat("Verify alert id not null", alert.getAlertId(), notNullValue());
        assertThat("Verify timestamp not null", alert.getTimestamp(), notNullValue());
        assertThat("Verify ucid is correct", alert.getUcid(), equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.getRule(), notNullValue());
        assertThat("Verify rule ver not null", alert.getRule().getVer(), notNullValue());
        assertThat("Verify rule name is correct", alert.getRule().getName(), equalTo("Enough Trades"));
        assertThat("Verify rule trigger is correct", alert.getTrigger(), equalTo("Withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.getFraudType(), equalTo(EXCHANGER.getCode()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.getAttributes(), notNullValue());

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Enough Trades");
    }

    @Test
    @AllureId("2471")
    @DisplayName("Enough trades rule. Alert4. Previous end = 202, decision !=2 . Element id: Event_1gmc8xt")
    void enoughTradesTestAlert4() throws Exception {
        DataHelper data = dbDataMap.get("773");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementIdSubrule(
                "Event_1gmc8xt",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");
        checkElementIdSubrule(
                "Event_0sg27lc",
                data.crmWithdrawalEventV2.getId(),
                "router_rule_crm_payment_shadow_mode",
                "enough_trades");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString(), "3");
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(3));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));

        // Verify alert kafka
        Allure.step("Get alerts kafka messages");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlertV2 alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlertV2.class);
        assertThat("Verify alert id not null", alert.getAlertId(), notNullValue());
        assertThat("Verify timestamp not null", alert.getTimestamp(), notNullValue());
        assertThat("Verify ucid is correct", alert.getUcid(), equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.getRule(), notNullValue());
        assertThat("Verify rule ver not null", alert.getRule().getVer(), notNullValue());
        assertThat("Verify rule name is correct", alert.getRule().getName(), equalTo("Enough Trades"));
        assertThat("Verify rule trigger is correct", alert.getTrigger(), equalTo("Withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.getFraudType(), equalTo(EXCHANGER.getCode()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alert.getAttributes(), notNullValue());
        assertThat("Verify rule attributes not null", alert.getAttributes().getStage(), is("1"));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Enough Trades");
    }
}
