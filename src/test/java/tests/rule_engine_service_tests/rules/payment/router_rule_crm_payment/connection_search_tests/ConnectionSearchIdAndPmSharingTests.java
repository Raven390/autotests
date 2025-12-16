package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.connection_search_tests;

import business_objects.api.payment_gate.payments_decisions.PutDecisionsRequestBody;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import helpers.data.enums.Rule;
import helpers.data.enums.payment_gate.Decision;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.api.payment_gate.payments_decisions.DecisionsRequests.putDecisions;
import static helpers.data.rules.payments.router_rule_crm_payment.connection_search.ConnectionSearchIdAndPmSharingDataFactory.setupConnectionSearchPmAndIdSharingRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.PaymentGateHelper.getPaymentEvent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomDateTimeIsoUtc;


@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CONNECTION_SEARCH_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class ConnectionSearchIdAndPmSharingTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupConnectionSearchPmAndIdSharingRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dataMap);
    }

    @Disabled
    @Test
    @AllureId("1835")
    @DisplayName("Connection Search in router rule. Alert if Email / Phone compare = true. ElementId: Event_1i416tj")
    void connectionSearchRuleTest1() throws Exception {
        DataHelper data = dataMap.get("1");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1i416tj", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_1xwthxg", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_0tbmvdy", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_0prj79b", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1836")
    @DisplayName("Connection Search in router rule. All connected by payout are connected by NAME_BIRTH or DOCUMENT = true. ElementId: Event_1i416tj")
    void connectionSearchIdAndPmSharingTest2() throws Exception {
        DataHelper data = dataMap.get("2");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1ypjv6r", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_1xwthxg", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_0tbmvdy", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_0prj79b", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Test
    @AllureId("1837")
    @DisplayName("Connection Search in router rule. All connected by payout are connected by NAME_BIRTH or DOCUMENT = false. ElementId: Event_1i416tj")
    void connectionSearchIdAndPmSharingTest3() throws Exception {
        DataHelper data = dataMap.get("3");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1sjvssv", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_0tbmvdy", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_0prj79b", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Test
    @AllureId("1838")
    @DisplayName("Connection Search in router rule. Is Crypto withdrawal = false. ElementId: Event_1w0lx0h")
    void connectionSearchIdAndPmSharingTest4() throws Exception {
        DataHelper data = dataMap.get("4");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1w0lx0h", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_0tbmvdy", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_0prj79b", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1839")
    @DisplayName("Connection Search in router rule. payoutConnectionsCount > 4 = true. ElementId: Event_1d2pl82")
    void connectionSearchIdAndPmSharingTest5() throws Exception {
        DataHelper data = dataMap.get("5");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1d2pl82", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_1xwthxg", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_0tbmvdy", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_0prj79b", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1840")
    @DisplayName("Connection Search in router rule. payoutConnectionsCount > 1 = true. ElementId: Event_10q9rcg")
    void connectionSearchIdAndPmSharingTest6() throws Exception {
        DataHelper data = dataMap.get("6");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_10q9rcg", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_1xwthxg", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_0tbmvdy", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_0prj79b", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Test
    @AllureId("1841")
    @DisplayName("Connection Search in router rule. Send alert if Single transaction of >= 10K USD = true. ElementId: Event_13t7cj9")
    void connectionSearchIdAndPmSharingTest7() throws Exception {
        DataHelper data = dataMap.get("7");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        PaymentEventsObject paymentEventsObject = getPaymentEvent(data.clientHelper.getUcid());
        Assertions.assertNotNull(paymentEventsObject);
        UUID paymentId = paymentEventsObject.getPaymentId();

        Allure.step("Send payment approval");
        PutDecisionsRequestBody putPaymentDecisionBody1 = new PutDecisionsRequestBody();
        putPaymentDecisionBody1.setDecisionType(Decision.RISK_REJECT.getType());
        putPaymentDecisionBody1.setDecisionCode(Decision.RISK_REJECT.getCode());
        putPaymentDecisionBody1.setRejectionCode(1);
        putPaymentDecisionBody1.setDecidedAt(getRandomDateTimeIsoUtc());
        putPaymentDecisionBody1.setActor("Auto qa");

        putDecisions(paymentId.toString(), List.of(putPaymentDecisionBody1));

        checkElementId("Event_13t7cj9", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_1xwthxg", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_0tbmvdy", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_0prj79b", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());

        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Connection search", "Crypto withdrawal > 10k");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert name", alerts.getFirst().getAlertId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Verify alert", alerts.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getType(), is("PAYMENT"));
        assertThat("Verify alert", alerts.getFirst().getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().getTrigger(), is("Withdrawal"));
        assertThat("Verify alert", alerts.getFirst().getReason(), is("Crypto withdrawal > 10k"));
        assertThat("Verify alert", alerts.getFirst().getAccount(), is(data.clientHelper.getTradingAccount()));
        assertThat("Verify alert", alerts.getFirst().getPaymentMethod(), is(data.crmWithdrawalEvent.getPaymentMethodCode()));
        assertThat("Verify alert", alerts.getFirst().getAmount(), is(data.crmWithdrawalEvent.getWithdrawalAmount()));
        assertThat("Verify alert", alerts.getFirst().getAmountUsd(), is(data.crmWithdrawalEvent.getWithdrawalAmountUSD()));
        assertThat("Verify alert", alerts.getFirst().getCurrency(), is(data.crmWithdrawalEvent.getWithdrawalCurrency()));
        assertThat("Verify alert", alerts.getFirst().getPaymentEventId(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getMerchantOrderId(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Verify alert", alerts.getFirst().getFraudType(), is("POTENTIAL_ABUSE"));
        assertThat("Verify alert", alerts.getFirst().getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getRule().getName(), is("Connection search"));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getProfileDeposits(), is(nullValue()));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getProfileWithdrawals(), is(nullValue()));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getCryptoWithdrawal10k(), is(true));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getCryptoDeposit(), is("0 USD"));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getCryptoWithdrawal(), is("10001 USD"));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getSameDataEwalletKyc(), is(nullValue()));
    }

    @Test
    @AllureId("1842")
    @DisplayName("Connection Search in router rule. Sum Deposit OR Withdrawal >= 50K USD = true. ElementId: Event_0sscypz")
    void connectionSearchIdAndPmSharingTest8() throws Exception {
        DataHelper data = dataMap.get("8");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_0sscypz", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_1xwthxg", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_0tbmvdy", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_0prj79b", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1843")
    @DisplayName("Connection Search in router rule. Sum Deposit OR Withdrawal >= 50K USD = false. ElementId: Event_136ttoj")
    void connectionSearchIdAndPmSharingTest9() throws Exception {
        DataHelper data = dataMap.get("9");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_136ttoj", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_0tbmvdy", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_0prj79b", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }
}
