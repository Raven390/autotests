package tests.rule_engine_service_tests.rules.payment;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.payment_gate.payment_details.PaymentDetailsObjectFactory.generatePaymentDetailsObject;
import static business_objects.db.payment_gate.payment_events.PaymentEventsObjectFactory.generatePaymentEventsObject;
import static business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObjectFactory.generatePaymentRuleExecutionsObject;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.enums.FraudType.*;
import static helpers.data.rules.payments.ChargebackRuleDataFactory.setupChargebackData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static helpers.database.PaymentGateHelper.getPaymentRuleExecution;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static tests.TestBaseRule.*;
import static utils.Constants.*;

import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.db.payment_gate.payment_rule_executions.PaymentRuleExecutionsObject;
import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.ClientHelper;
import helpers.data.DataDeleteHelper;
import helpers.data.DataHelper;
import helpers.data.enums.FraudTypeStatus;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.*;

class ChargebackRuleTest {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws Exception {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupChargebackData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataDeleteHelper.deleteData(dbDataMap);
    }

    @Test
    @AllureId("1948")
    @DisplayName(
            "Chargeback rule test. client have no data. callback.data.charge.attributes.status == 'approved' No Chargeback fraud. Event_1tc5so6")
    void chargeback1Test() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_106", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

        Allure.step("Retrieve payment id");
        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();
        PaymentRuleExecutionsObject paymentRuleExecutionsObject = getPaymentRuleExecution(paymentId.toString());
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getPaymentId(), is(paymentId));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleId(), is(4));
        assertThat("Assert rule execution", paymentRuleExecutionsObject.getRuleEndId(), is(106));
    }

    @Test
    @AllureId("1949")
    @DisplayName(
            "Chargeback rule test. client have no deposits for card(profile) in event. callback.data.charge.attributes.status != 'approved' No Chargeback fraud. Event_1en3mz7")
    void chargeback2Test() throws Exception {
        DataHelper data = dbDataMap.get("2");

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1en3mz7", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");
    }

    @Test
    @AllureId("1950")
    @DisplayName(
            "Chargeback rule test. client have deposits. callback.data.charge.attributes.status == 'approved' No Chargeback fraud. Event_1en3mz7")
    void chargeback3Test() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1en3mz7", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");
    }

    @Test
    @AllureId("1951")
    @DisplayName(
            "Chargeback rule test. client have no deposits for card(profile) in event. callback.data.charge.attributes.status =! 'approved' Chargeback fraud. ")
    void chargeback4Test() throws Exception {
        DataHelper data = dbDataMap.get("4");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("get_rate", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");
    }

    @Test
    @AllureId("1952")
    @DisplayName(
            "Chargeback rule test. client have no deposits for card(profile) in event. callback.data.charge.attributes.status == 'approved' No Chargeback fraud. ")
    void chargeback5Test() throws Exception {
        DataHelper data = dbDataMap.get("5");

        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("get_rate", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");
    }

    @Test
    @AllureId("1953")
    @DisplayName(
            "Chargeback rule test. client sum deposits >5000, 3 cards, no connections. Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback6Test() throws Exception {
        DataHelper data = dbDataMap.get("6");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("set_score_1", data.callbackEvent.getId(), "chargeback");
        checkElementId("end_209", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), "chargeback");
        checkElementId("send_alert", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("1st deposit and no open trades"));
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
        assertThat("Verify alert attributes", alert.getAttributes().getFirstDepositNoOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(1));
    }

    @Test
    @AllureId("1954")
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 4 cards, no connections. Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback7Test() throws Exception {
        DataHelper data = dbDataMap.get("7");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("set_score_1", data.callbackEvent.getId(), "chargeback");
        checkElementId("end_209", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), "chargeback");
        checkElementId("send_alert", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("1st deposit and no open trades"));
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
        assertThat("Verify alert attributes", alert.getAttributes().getFirstDepositNoOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(1));
    }

    @Test
    @AllureId("1955")
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 4 cards between connections, 2 connections. Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback8Test() throws Exception {
        DataHelper data = dbDataMap.get("8");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("set_score_1", data.callbackEvent.getId(), "chargeback");
        checkElementId("end_209", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), "chargeback");
        checkElementId("send_alert", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("1st deposit and no open trades"));
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
        assertThat("Verify alert attributes", alert.getAttributes().getFirstDepositNoOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(1));
    }

    @Test
    @AllureId("1956")
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback9Test() throws Exception {
        DataHelper data = dbDataMap.get("9");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("incr_score", data.callbackEvent.getId(), "chargeback");
        checkElementId("end_209", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), "chargeback");
        checkElementId("send_alert", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("1st deposit and no open trades"));
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
        assertThat("Verify alert attributes", alert.getAttributes().getFirstDepositNoOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getSharedCardAcrossUids(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(2));
    }

    @Test
    @AllureId("1957")
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback10Test() throws Exception {
        DataHelper data = dbDataMap.get("10");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("incr_score2", data.callbackEvent.getId(), "chargeback");
        checkElementId("end_209", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), "chargeback");
        checkElementId("send_alert", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("1st deposit and no open trades"));
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
        assertThat("Verify alert attributes", alert.getAttributes().getFirstDepositNoOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getSharedCardAcrossUids(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getMultipleUniqueCards(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(3));
    }

    @Test
    @AllureId("1958")
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback11Test() throws Exception {
        DataHelper data = dbDataMap.get("11");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("incr_score3", data.callbackEvent.getId(), "chargeback");
        checkElementId("end_209", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), "chargeback");
        checkElementId("send_alert", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("1st deposit and no open trades"));
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
        assertThat("Verify alert attributes", alert.getAttributes().getFirstDepositNoOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getSharedCardAcrossUids(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFailedAttempts(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFraudScore(), is(3));
        assertThat(
                "Verify alert attributes",
                alert.getAttributes().getPaymentProfile(),
                is(data.callbackEvent
                        .getCallback()
                        .getData()
                        .getCharge()
                        .getAttributes()
                        .getCardMaskedNumber()));
    }

    @Test
    @AllureId("1959")
    @DisplayName(
            "Chargeback rule test. client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster. Client is not cardholder. Is not 3d. No not FTD and no Open Trades. end_209")
    void chargeback12Test() throws Exception {
        DataHelper data = dbDataMap.get("12");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("incr_score4", data.callbackEvent.getId(), "chargeback");
        checkElementId("end_209", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), "chargeback");
        checkElementId("send_alert", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("1st deposit and no open trades"));
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
        assertThat("Verify alert attributes", alert.getAttributes().getFirstDepositNoOpenTrades(), is("No"));
        assertThat("Verify alert attributes", alert.getAttributes().getSharedCardAcrossUids(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getFailedAttempts(), is("Yes"));
        assertThat("Verify alert attributes", alert.getAttributes().getIsThereAFraudDecline(), is("Yes"));
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
    }

    @Test
    @AllureId("1960")
    @DisplayName(
            "Chargeback rule test.cardholder/KYC name similarity is == 1. client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster. Is 3d. No not FTD and no Open Trades.")
    void chargeback13Test() throws Exception {
        DataHelper data = dbDataMap.get("13");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_104", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("1961")
    @DisplayName(
            "card is is 3dS. Chargeback rule test.cardholder/KYC name similarity is != 1 . client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster. Is 3d. No not FTD and no Open Trades.")
    void chargeback14Test() throws Exception {
        DataHelper data = dbDataMap.get("14");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_105", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("1962")
    @DisplayName(
            "Score 0. card not 3dS. Chargeback rule test.cardholder/KYC name similarity is != 1 . client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster.  not FTD and no Open Trades.")
    void chargeback15Test() throws Exception {
        DataHelper data = dbDataMap.get("15");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1tc5so6", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("1963")
    @DisplayName(
            "Score 0. FTD with Open Trades. card not 3dS. Chargeback rule test.cardholder/KYC name similarity is != 1 . client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster.")
    void chargeback16Test() throws Exception {
        DataHelper data = dbDataMap.get("16");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_1tc5so6", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("1964")
    @DisplayName(
            "Score 0. FTD. no Open Trades. card not 3dS. Chargeback rule test.cardholder/KYC name similarity is != 1 . client sum deposits <5000, 4 cards between connections, 2 connections. 3 by payout/card. 4 callbacks fo 24h. 3 failed attempts with same card. 1 fraud decline. Chargeback fraud Card not used by known fraudster.")
    void chargeback17Test() throws Exception {
        DataHelper data = dbDataMap.get("17");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_106", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("1965")
    @DisplayName(
            "Score 0. FTD. no Open Trades. card not 3dS. Chargeback rule test.cardholder/KYC name similarity is != 1. Chargeback fraud Card not used by known fraudster.")
    void chargeback18Test() throws Exception {
        DataHelper data = dbDataMap.get("18");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("Event_0nx74ci", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), "chargeback");
        checkElementId("send_alert", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        RuleAlertV2 alert = alerts.getFirst();
        // alert root
        assertThat(
                "Verify alert rule",
                alert.getMerchantOrderId(),
                is(data.callbackEvent.getCallback().getData().getOrderId()));
        assertThat("Verify alert rule", alert.getPaymentMethod(), is(data.callbackEvent.getPaymentMethodCode()));
        assertThat("Verify alert ", alert.getReason(), is("1st deposit and no open trades"));
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
    }

    @Test
    @AllureId("1966")
    @DisplayName("Chargeback fraud Card was used by known fraudster. No segment. no Open Trades. card not 3dS.")
    void chargeback19Test() throws Exception {
        DataHelper data = dbDataMap.get("19");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        addFraudForClient(data.connectedClientHelpers.getFirst(), CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_203", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), "chargeback");
        checkElementId("send_alert", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
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
    }

    @Test
    @AllureId("1967")
    @DisplayName("Chargeback fraud Card was used by known fraudster. No segment. Open Trades. card not 3dS. end 202")
    void chargeback20Test() throws Exception {
        DataHelper data = dbDataMap.get("20");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        addFraudForClient(data.connectedClientHelpers.getFirst(), CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_202", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), "chargeback");
        checkElementId("send_alert", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
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
    }

    @Test
    @AllureId("1968")
    @DisplayName(
            "Chargeback Segment 'Very High'. Score > 0. fraud Card was used by known fraudster. No segment. Open Trades. card not 3dS. 204")
    void chargeback21Test() throws Exception {
        DataHelper data = dbDataMap.get("21");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        addFraudForClient(data.connectedClientHelpers.getFirst(), CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_204", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("get_deposits_by_order_id", data.callbackEvent.getId(), "chargeback");
        checkElementId("send_alert", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_decision_not_applicable", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
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
    }

    @Test
    @AllureId("1969")
    @DisplayName(
            "Chargeback Segment 'Ultra'. Score = 0. fraud Card was used by known fraudster. No segment. Open Trades. card not 3dS. 102")
    void chargeback22Test() throws Exception {
        DataHelper data = dbDataMap.get("22");
        addFraudForClient(data.clientHelper, CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        addFraudForClient(data.connectedClientHelpers.getFirst(), CHARGEBACK, FraudTypeStatus.POTENTIAL, List.of(""));
        Thread.sleep(1000);
        produceCallbackMessageToCrmPaymentTopic(data.callbackEvent);

        checkElementId("end_102", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");

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
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }

    @Test
    @AllureId("1970")
    @DisplayName("Chargeback Chargeback. Client have previous alerts from the rule")
    void chargeback23Test() throws Exception {
        DataHelper data = dbDataMap.get("23");

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

        checkElementId("Event_1en3mz7", data.callbackEvent.getId(), "chargeback");
        checkElementId("process_instance_key", data.callbackEvent.getId(), "chargeback");
        checkElementId("put_rule_execution", data.callbackEvent.getId(), "chargeback");
        checkElementId("end", data.callbackEvent.getId(), "chargeback");
        // check alert
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Chargeback");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));
    }
}
