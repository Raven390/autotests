package tests.rule_engine_service_tests.rules.payment;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.api.mitigation_service.MitigationServiceRequest.getRestrictionsByUcid;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObjectFactory.generatePaymentRuleExecutionsObject;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.asserts.AlertsAssertsHelper.assertChargebackRuleAlert;
import static helpers.asserts.AlertsAssertsHelper.assertThatAlertNotFailed;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.Restriction.*;
import static helpers.data.rules.payments.ChargebackRuleDataFactory.setupChargebackData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static helpers.database.PaymentGateHelper.getPaymentRuleExecution;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static tests.TestBaseRule.*;
import static utils.Constants.*;

import business_objects.api.mitigation_service.GetGeneralRestrictionResponseBody;
import business_objects.api.mitigation_service.GetRestrictionResponseBody;
import business_objects.api.mitigation_service.GetTradingRestrictionResponseBody;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.ClientHelper;
import helpers.data.DataHelper;
import helpers.data.enums.FraudTypeStatus;
import helpers.data.enums.rule_engine.Rule;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.*;
import org.junit.jupiter.api.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CHARGEBACK_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class ChargebackRuleTest {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws Exception {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupChargebackData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dbDataMap);
    }

    @Test
    @AllureId("2270")
    @DisplayName(
            "Chargeback rule test. client have rule end ALERT for rule 4. callback.data.charge.attributes.status == 'approved' No Chargeback fraud. Event_1tc5so6")
    void chargeback1Test() throws Exception {
        DataHelper data = dbDataMap.get("1");
        setupData(data);

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1en3mz7", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(101));
    }

    @Disabled("Disabled till the new version of rule")
    @Test
    @AllureId("2271")
    @DisplayName(
            "Chargeback rule test. client have no deposits for card(profile) in event. callback.data.charge.attributes.status != 'approved' No Chargeback fraud. Event_1en3mz7")
    void chargeback2Test() throws Exception {
        DataHelper data = dbDataMap.get("2");
        setupData(data);

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1en3mz7", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(101));
    }

    @Disabled("Disabled till the new version of rule")
    @Test
    @AllureId("2272")
    @DisplayName(
            "Chargeback rule test. client have deposits. callback.data.charge.attributes.status == 'approved' No Chargeback fraud. Event_1en3mz7")
    void chargeback3Test() throws Exception {
        DataHelper data = dbDataMap.get("3");
        setupData(data);

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1en3mz7", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
    }

    @Disabled("Disabled till the new version of rule")
    @Test
    @AllureId("2273")
    @DisplayName(
            "Chargeback rule test. client have no deposits for card(profile) in event. callback.data.charge.attributes.status =! 'approved' Chargeback fraud. event Event_1en3mz7")
    void chargeback4Test() throws Exception {
        DataHelper data = dbDataMap.get("4");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1en3mz7", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
    }

    @Test
    @AllureId("2274")
    @DisplayName(
            "Chargeback rule test. client have no deposits for card(profile) in event. callback.data.charge.attributes.status == 'approved' No Chargeback fraud. ")
    void chargeback5Test() throws Exception {
        DataHelper data = dbDataMap.get("5");
        setupData(data);

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("get_conn_by_nb_document", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
    }

    @Test
    @AllureId("2275")
    @DisplayName(
            "Chargeback rule test. client sum deposits >5000, 4 cards, no connections. Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback6Test() throws Exception {
        DataHelper data = dbDataMap.get("6");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("set_score_1", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("Activity_04u6iqa", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end_209", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(209));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Multi-condition match"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getOpenTrades(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFirstDeposit(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(4));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Test
    @AllureId("2342")
    @DisplayName(
            "Chargeback rule test. client sum deposits >5000, 4 cards, one older than month, no connections. Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback61Test() throws Exception {
        DataHelper data = dbDataMap.get("61");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Activity_04u6iqa", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end_209", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(209));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Multi-condition match"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getFirstDeposit(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), emptyOrNullString());
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(3));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Test
    @AllureId("2276")
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 5 cards, no connections. Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback7Test() throws Exception {
        DataHelper data = dbDataMap.get("7");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("set_score_1", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("Activity_04u6iqa", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end_209", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(209));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("High-value multi-card"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(2));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Test
    @AllureId("2277")
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 5 cards between connections, 2 connections. Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback8Test() throws Exception {
        DataHelper data = dbDataMap.get("8");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("set_score_1", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("Activity_04u6iqa", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end_209", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(209));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("High-value multi-card"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(2));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Test
    @AllureId("2278")
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 5 cards between connections, 2 connections. 3 by payout/card Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback9Test() throws Exception {
        DataHelper data = dbDataMap.get("9");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("incr_score", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("Activity_04u6iqa", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end_209", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(209));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Multi-condition match"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getSharedCardAcrossUids(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(3));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Test
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 5 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback10Test() throws Exception {
        DataHelper data = dbDataMap.get("10");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("incr_score2", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("Activity_04u6iqa", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end_209", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(209));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Multi-condition match"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getSharedCardAcrossUids(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getMultipleUniqueCards(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(4));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Test
    @AllureId("2279")
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback11Test() throws Exception {
        DataHelper data = dbDataMap.get("11");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("incr_score3", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("Activity_04u6iqa", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end_209", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(209));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Multi-condition match"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getSharedCardAcrossUids(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFailedAttempts(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(4));
        assertThat(
                "Verify alert attributes",
                alert.getAttributes().getPaymentProfile(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCardMaskedNumber()));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Test
    @AllureId("2280")
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 5 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback12Test() throws Exception {
        DataHelper data = dbDataMap.get("12");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("incr_score4", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("Activity_04u6iqa", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end_209", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(209));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Multi-condition match"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getSharedCardAcrossUids(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFailedAttempts(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getIsThereAFraudDecline(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(5));
        assertThat(
                "Verify alert attributes",
                alert.getAttributes().getPaymentProfile(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCardMaskedNumber()));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Disabled("Disabled till the new version of rule")
    @Test
    @AllureId("2281")
    @DisplayName(
            "Chargeback rule test.cardholder/KYC name similarity is == 1. client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster. Is 3d. No not FTD and no Open Trades.")
    void chargeback13Test() throws Exception {
        DataHelper data = dbDataMap.get("13");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_104", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(104));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(0));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Disabled("Disabled till the new version of rule")
    @Test
    @AllureId("2282")
    @DisplayName(
            "card is is 3dS. Chargeback rule test.cardholder/KYC name similarity is != 1 . client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster. Is 3d. No not FTD and no Open Trades.")
    void chargeback14Test() throws Exception {
        DataHelper data = dbDataMap.get("14");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_105", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(105));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(0));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("2283")
    @DisplayName(
            "Score 0. card not 3dS. Chargeback rule test.cardholder/KYC name similarity is = 1 . client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster.  not FTD and no Open Trades.")
    void chargeback15Test() throws Exception {
        DataHelper data = dbDataMap.get("15");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1tc5so6", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(107));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(0));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("2284")
    @DisplayName(
            "Score 0. FTD with Open Trades. card not 3dS. Chargeback rule test.cardholder/KYC name similarity is != 1 . client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster.")
    void chargeback16Test() throws Exception {
        DataHelper data = dbDataMap.get("16");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1tc5so6", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(107));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(0));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @DisplayName(
            "Score 0. FTD. no Open Trades. card not 3dS. Chargeback rule test.cardholder/KYC name similarity is = 1 . client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster.")
    void chargeback17Test() throws Exception {
        DataHelper data = dbDataMap.get("17");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_106", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(106));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(0));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("2285")
    @DisplayName(
            "Score 1. FTD. no Open Trades. card not 3dS. Chargeback rule test.cardholder/KYC name similarity is != 1. Chargeback fraud Card not used by known fraudster. event.regulator != ASIC or FCA")
    void chargeback18Test() throws Exception {
        DataHelper data = dbDataMap.get("18");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1z0wqcj", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(208));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Same name: Card & KYC"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), nullValue());
        assertThat("Verify alert attributes", alert.getAttributes().getThreeDs(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(1));
        assertThat(
                "Verify alert attributes",
                alert.getAttributes().getPaymentProfile(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCardMaskedNumber()));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");

        // validate restrictions
        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(objectMapper.readValue(
                getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(),
                GetRestrictionResponseBody[].class));
        assertEquals(3, clientRestrictions.size());

        GetTradingRestrictionResponseBody restriction1 = (GetTradingRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(CLOSE_ONLY_MODE.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction1);
        for (GetTradingRestrictionResponseBody.TradingStatus status : restriction1.getStatus()) {
            assertEquals("APPLIED", status.getStatus());
        }

        GetGeneralRestrictionResponseBody restriction2 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(DEPOSITS.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction2);
        assertEquals("APPLIED", restriction2.getStatus());

        GetGeneralRestrictionResponseBody restriction3 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody ->
                        getRestrictionResponseBody.code.equals(INTERNAL_TRANSFER.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction3);
        assertEquals("APPLIED", restriction3.getStatus());
    }

    @Test
    @AllureId("2443")
    @DisplayName(
            "Score 1. FTD. no Open Trades. card not 3dS. Chargeback rule test.cardholder/KYC name similarity is != 1. Chargeback fraud Card not used by known fraudster. event.regulator = ASIC")
    void chargeback181Test() throws Exception {
        DataHelper data = dbDataMap.get("181");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1z0wqcj", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(208));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Same name: Card & KYC"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), nullValue());
        assertThat("Verify alert attributes", alert.getAttributes().getThreeDs(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(1));
        assertThat(
                "Verify alert attributes",
                alert.getAttributes().getPaymentProfile(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCardMaskedNumber()));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");

        // validate restrictions
        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(objectMapper.readValue(
                getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(),
                GetRestrictionResponseBody[].class));
        assertEquals(2, clientRestrictions.size());

        GetGeneralRestrictionResponseBody restriction2 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(DEPOSITS.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction2);
        assertEquals("APPLIED", restriction2.getStatus());

        GetGeneralRestrictionResponseBody restriction3 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody ->
                        getRestrictionResponseBody.code.equals(INTERNAL_TRANSFER.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction3);
        assertEquals("APPLIED", restriction3.getStatus());
    }

    @Test
    @AllureId("2438")
    @DisplayName(
            "Score 1. FTD. no Open Trades. card not 3dS. Chargeback rule test.cardholder/KYC name similarity is != 1. Chargeback fraud Card not used by known fraudster. event.regulator = FCA")
    void chargeback182Test() throws Exception {
        DataHelper data = dbDataMap.get("182");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1z0wqcj", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(208));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Same name: Card & KYC"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), nullValue());
        assertThat("Verify alert attributes", alert.getAttributes().getThreeDs(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(1));
        assertThat(
                "Verify alert attributes",
                alert.getAttributes().getPaymentProfile(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCardMaskedNumber()));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");

        // validate restrictions
        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(objectMapper.readValue(
                getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(),
                GetRestrictionResponseBody[].class));
        assertEquals(2, clientRestrictions.size());

        GetGeneralRestrictionResponseBody restriction2 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(DEPOSITS.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction2);
        assertEquals("APPLIED", restriction2.getStatus());

        GetGeneralRestrictionResponseBody restriction3 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody ->
                        getRestrictionResponseBody.code.equals(INTERNAL_TRANSFER.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction3);
        assertEquals("APPLIED", restriction3.getStatus());
    }

    @Test
    @AllureId("2286")
    @DisplayName(
            "Chargeback fraud Card was used by known fraudster. No segment. no Open Trades. card not 3dS. event.regulator != ASIC or FCA")
    void chargeback19Test() throws Exception {
        DataHelper data = dbDataMap.get("19");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        for (ClientHelper i : data.connectedClientHelpers) {
            addFraudForClient(i, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        }
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_203", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(203));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Card used by known fraudster"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getCardUsedByKnownFraudster(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(1));
        assertThat(
                "Verify alert attributes",
                alert.getAttributes().getPaymentProfile(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCardMaskedNumber()));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");

        // validate restrictions
        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(objectMapper.readValue(
                getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(),
                GetRestrictionResponseBody[].class));
        assertEquals(3, clientRestrictions.size());

        GetTradingRestrictionResponseBody restriction1 = (GetTradingRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(CLOSE_ONLY_MODE.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction1);
        for (GetTradingRestrictionResponseBody.TradingStatus status : restriction1.getStatus()) {
            assertEquals("APPLIED", status.getStatus());
        }

        GetGeneralRestrictionResponseBody restriction2 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(DEPOSITS.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction2);
        assertEquals("APPLIED", restriction2.getStatus());

        GetGeneralRestrictionResponseBody restriction3 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody ->
                        getRestrictionResponseBody.code.equals(INTERNAL_TRANSFER.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction3);
        assertEquals("APPLIED", restriction3.getStatus());
    }

    @Test
    @AllureId("2439")
    @DisplayName(
            "Chargeback fraud Card was used by known fraudster. No segment. no Open Trades. card not 3dS. event.regulator = ASIC")
    void chargeback191Test() throws Exception {
        DataHelper data = dbDataMap.get("191");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        for (ClientHelper i : data.connectedClientHelpers) {
            addFraudForClient(i, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        }
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_203", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(203));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Card used by known fraudster"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getCardUsedByKnownFraudster(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(1));
        assertThat(
                "Verify alert attributes",
                alert.getAttributes().getPaymentProfile(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCardMaskedNumber()));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");

        // validate restrictions
        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(objectMapper.readValue(
                getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(),
                GetRestrictionResponseBody[].class));
        assertEquals(2, clientRestrictions.size());

        GetGeneralRestrictionResponseBody restriction2 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(DEPOSITS.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction2);
        assertEquals("APPLIED", restriction2.getStatus());

        GetGeneralRestrictionResponseBody restriction3 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody ->
                        getRestrictionResponseBody.code.equals(INTERNAL_TRANSFER.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction3);
        assertEquals("APPLIED", restriction3.getStatus());
    }

    @Test
    @AllureId("2440")
    @DisplayName(
            "Chargeback fraud Card was used by known fraudster. No segment. no Open Trades. card not 3dS. event.regulator = FCA")
    void chargeback192Test() throws Exception {
        DataHelper data = dbDataMap.get("192");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        for (ClientHelper i : data.connectedClientHelpers) {
            addFraudForClient(i, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        }
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_203", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(203));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Card used by known fraudster"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getCardUsedByKnownFraudster(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(1));
        assertThat(
                "Verify alert attributes",
                alert.getAttributes().getPaymentProfile(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCardMaskedNumber()));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");

        // validate restrictions
        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(objectMapper.readValue(
                getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(),
                GetRestrictionResponseBody[].class));
        assertEquals(2, clientRestrictions.size());

        GetGeneralRestrictionResponseBody restriction2 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(DEPOSITS.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction2);
        assertEquals("APPLIED", restriction2.getStatus());

        GetGeneralRestrictionResponseBody restriction3 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody ->
                        getRestrictionResponseBody.code.equals(INTERNAL_TRANSFER.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction3);
        assertEquals("APPLIED", restriction3.getStatus());
    }

    @Test
    @AllureId("2287")
    @DisplayName("Chargeback fraud Card was used by known fraudster. No segment. Open Trades. card not 3dS. end 202")
    void chargeback20Test() throws Exception {
        DataHelper data = dbDataMap.get("20");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        addFraudForClient(data.connectedClientHelpers.getFirst(), CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_202", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(202));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("Card used by known fraudster"));
        assertThat("Verify alert ", alert.getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert ", alert.getFraudType(), is("CHARGEBACK"));
        assertThat("Verify alert ", alert.getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert ",
                alert.getCurrency(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCurrency()));
        assertThat("Verify alert ", alert.getAccount(), is(notNullValue()));
        assertThat("Verify alert ", alert.getTrigger(), is("Deposit"));
        assertThat(
                "Verify alert ",
                alert.getAmount(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getAmount()));
        assertThat("Verify alert ", alert.getAmountUsd(), is(notNullValue()));
        assertThat("Verify alert ", alert.getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert ", alert.getType(), is("PAYMENT"));

        // alert/rule
        assertThat("Verify alert rule", alert.getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert rule", alert.getRule().getName(), is("Chargeback"));

        // alert/attribute
        assertThat("Verify alert attributes", alert.getAttributes().getHighValueMultiCard(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getCardUsedByKnownFraudster(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(2));
        assertThat(
                "Verify alert attributes",
                alert.getAttributes().getPaymentProfile(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCardMaskedNumber()));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Test
    @AllureId("2288")
    @DisplayName(
            "Chargeback Segment 'Very High'. Score > 0. fraud Card was used by known fraudster. No segment. Open Trades. card not 3dS. 204")
    void chargeback21Test() throws Exception {
        DataHelper data = dbDataMap.get("21");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        addFraudForClient(data.connectedClientHelpers.getFirst(), CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_204", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("send_alert", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(204));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().getPaymentId(), is(paymentId));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionType(), is("payment"));
        assertThat("Verify decisions have right decision ", decision.getFirst().getDecisionCode(), is(3));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertChargebackRuleAlert(data, alerts, 1);

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Test
    @AllureId("2289")
    @DisplayName(
            "Chargeback Segment 'Ultra'. Score = 0. fraud Card was used by known fraudster. No segment. Open Trades. card not 3dS. 102")
    void chargeback22Test() throws Exception {
        DataHelper data = dbDataMap.get("22");
        setupData(data);

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        addFraudForClient(data.connectedClientHelpers.getFirst(), CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_102", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(102));

        // validate payment decision
        List<PaymentDecisionsObject> decision = getRuleDecisionByWithdrawalIdFromDb(paymentId);
        assertThat("Verify amount of decisions in DB", decision.size(), is(0));

        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("2290")
    @DisplayName("Chargeback Chargeback. Client have previous alerts from the rule")
    void chargeback23Test() throws Exception {
        DataHelper data = dbDataMap.get("23");
        setupData(data);

        // create previous alert
        ClientHelper client1 = data.clientHelper;
        PaymentEventsObject paymentEventsObject1 = generatePaymentEventsObject(client1);
        PaymentDetailsObject paymentDetailsObject1 = generatePaymentDetailsObject(paymentEventsObject1, client1);
        PaymentRuleExecutionsObject paymentRuleExecutionsObject1 =
                generatePaymentRuleExecutionsObject(paymentEventsObject1);
        paymentRuleExecutionsObject1.setRuleEndId(202);
        paymentRuleExecutionsObject1.setRuleId(4);

        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, List.of(paymentEventsObject1));
        insertObjectsToDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, List.of(paymentDetailsObject1));
        insertObjectsToDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE, List.of(paymentRuleExecutionsObject1));

        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1en3mz7", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("2291")
    @DisplayName("Chargeback Chargeback. Branch 10, segment ultra or very high, score>0. Element_id: end_207")
    void chargeback24Test() throws Exception {
        DataHelper data = dbDataMap.get("24");
        setupData(data);

        addFraudForClient(data.clientHelper, HFT_ABUSE, FraudTypeStatus.POTENTIAL, List.of(""));
        addFraudForClient(data.connectedClientHelpers.getFirst(), HFT_ABUSE, FraudTypeStatus.POTENTIAL, List.of(""));

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_207", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert ", alerts.getFirst().getReason(), is("Card verified by other client"));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Test
    @AllureId("2292")
    @DisplayName("Chargeback Chargeback. Branch 10, segment not ultra or very high, score0. Element_id: end_103")
    void chargeback25Test() throws Exception {
        DataHelper data = dbDataMap.get("25");
        setupData(data);

        addFraudForClient(data.clientHelper, HFT_ABUSE, FraudTypeStatus.POTENTIAL, List.of(""));

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_103", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("2293")
    @DisplayName(
            "Chargeback Chargeback. Branch 10, segment is not ultra or very high, unclosed trades >0. Element_id: end_205")
    void chargeback26Test() throws Exception {
        DataHelper data = dbDataMap.get("26");
        setupData(data);

        addFraudForClient(data.clientHelper, HFT_ABUSE, FraudTypeStatus.POTENTIAL, List.of(""));
        addFraudForClient(data.connectedClientHelpers.getFirst(), HFT_ABUSE, FraudTypeStatus.POTENTIAL, List.of(""));

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_205", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert ", alerts.getFirst().getReason(), is("Card verified by other client"));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");
    }

    @Test
    @AllureId("2294")
    @DisplayName(
            "Chargeback Chargeback. Branch 10, segment is not ultra or very high, unclosed trades =0.regulator != ASIC or FCA. Element_id: end_206regulator != ASIC or FCA")
    void chargeback27Test() throws Exception {
        DataHelper data = dbDataMap.get("27");
        setupData(data);

        addFraudForClient(data.clientHelper, HFT_ABUSE, FraudTypeStatus.POTENTIAL, List.of(""));
        addFraudForClient(data.connectedClientHelpers.getFirst(), HFT_ABUSE, FraudTypeStatus.POTENTIAL, List.of(""));

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_206", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert ", alerts.getFirst().getReason(), is("Card verified by other client"));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");

        // validate restrictions
        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(objectMapper.readValue(
                getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(),
                GetRestrictionResponseBody[].class));
        assertEquals(3, clientRestrictions.size());

        GetTradingRestrictionResponseBody restriction1 = (GetTradingRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(CLOSE_ONLY_MODE.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction1);
        for (GetTradingRestrictionResponseBody.TradingStatus status : restriction1.getStatus()) {
            assertEquals("APPLIED", status.getStatus());
        }

        GetGeneralRestrictionResponseBody restriction2 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(DEPOSITS.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction2);
        assertEquals("APPLIED", restriction2.getStatus());

        GetGeneralRestrictionResponseBody restriction3 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody ->
                        getRestrictionResponseBody.code.equals(INTERNAL_TRANSFER.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction3);
        assertEquals("APPLIED", restriction3.getStatus());
    }

    @Test
    @AllureId("2441")
    @DisplayName(
            "Chargeback Chargeback. Branch 10, segment is not ultra or very high, unclosed trades =0.regulator = ASIC. Element_id: end_206")
    void chargeback271Test() throws Exception {
        DataHelper data = dbDataMap.get("271");
        setupData(data);

        addFraudForClient(data.clientHelper, HFT_ABUSE, FraudTypeStatus.POTENTIAL, List.of(""));
        addFraudForClient(data.connectedClientHelpers.getFirst(), HFT_ABUSE, FraudTypeStatus.POTENTIAL, List.of(""));

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_206", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert ", alerts.getFirst().getReason(), is("Card verified by other client"));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");

        // validate restrictions
        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(objectMapper.readValue(
                getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(),
                GetRestrictionResponseBody[].class));
        assertEquals(2, clientRestrictions.size());

        GetGeneralRestrictionResponseBody restriction2 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(DEPOSITS.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction2);
        assertEquals("APPLIED", restriction2.getStatus());

        GetGeneralRestrictionResponseBody restriction3 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody ->
                        getRestrictionResponseBody.code.equals(INTERNAL_TRANSFER.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction3);
        assertEquals("APPLIED", restriction3.getStatus());
    }

    @Test
    @AllureId("2442")
    @DisplayName(
            "Chargeback Chargeback. Branch 10, segment is not ultra or very high, unclosed trades =0.regulator = FCA. Element_id: end_206")
    void chargeback272Test() throws Exception {
        DataHelper data = dbDataMap.get("272");
        setupData(data);

        addFraudForClient(data.clientHelper, HFT_ABUSE, FraudTypeStatus.POTENTIAL, List.of(""));
        addFraudForClient(data.connectedClientHelpers.getFirst(), HFT_ABUSE, FraudTypeStatus.POTENTIAL, List.of(""));

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_206", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("process_instance_key", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("put_rule_execution", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        checkElementId("end", data.callbackEvent.getId(), Rule.CHARGEBACK.getProcessId());
        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, Rule.CHARGEBACK.getName());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert ", alerts.getFirst().getReason(), is("Card verified by other client"));

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "Chargeback");

        // validate restrictions
        List<GetRestrictionResponseBody> clientRestrictions = Arrays.asList(objectMapper.readValue(
                getRestrictionsByUcid(data.clientHelper.getUcid()).body().string(),
                GetRestrictionResponseBody[].class));
        assertEquals(2, clientRestrictions.size());

        GetGeneralRestrictionResponseBody restriction2 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody -> getRestrictionResponseBody.code.equals(DEPOSITS.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction2);
        assertEquals("APPLIED", restriction2.getStatus());

        GetGeneralRestrictionResponseBody restriction3 = (GetGeneralRestrictionResponseBody) clientRestrictions.stream()
                .filter(getRestrictionResponseBody ->
                        getRestrictionResponseBody.code.equals(INTERNAL_TRANSFER.getCode()))
                .findFirst()
                .orElse(null);
        Assertions.assertNotNull(restriction3);
        assertEquals("APPLIED", restriction3.getStatus());
    }
}
