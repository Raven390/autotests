package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.connection_search_tests;

import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import helpers.data.enums.Rule;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.payments.router_rule_crm_payment.connection_search.ConnectionSearchPaymentAbuseDataFactory.setupConnectionSearchPaymentRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CONNECTION_SEARCH_IN_ROUTER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class ConnectionSearchPaymentAbuseTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupConnectionSearchPaymentRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dataMap);
    }

    @Disabled
    @Test
    @AllureId("1844")
    @DisplayName("Connection Search in router rule. Exit if no matching connections. ElementId: end_connections_not_found2")
    void connectionSearchPaymentAbuseTest1() throws Exception {
        DataHelper data = dataMap.get("1");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("end_connections_not_found2", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_154345n", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_1kf15qu", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1845")
    @DisplayName("Connection Search in router rule. Exit if no toxic account linked. ElementId: end_cs_no_abuse")
    void connectionSearchPaymentAbuseTest2() throws Exception {
        DataHelper data = dataMap.get("2");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("end_cs_no_abuse", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_154345n", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_1kf15qu", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1846")
    @DisplayName("Connection Search in router rule. Exit if unknown fraud type. ElementId: end_unknown_FT")
    void connectionSearchPaymentAbuseTest3() throws Exception {
        DataHelper data = dataMap.get("3");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("end_unknown_FT", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_154345n", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_1kf15qu", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1847")
    @DisplayName("Connection Search in router rule. Exit without alert. ElementId: Event_1sc8b2t")
    void connectionSearchPaymentAbuseTest4() throws Exception {
        DataHelper data = dataMap.get("4");

        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_1sc8b2t", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_154345n", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_1kf15qu", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());
    }

    @Test
    @AllureId("1848")
    @DisplayName("Connection Search in router rule. Exit with alert. ElementId: Event_0yh59iy")
    void connectionSearchPaymentAbuseTest5() throws Exception {
        DataHelper data = dataMap.get("5");

        data.crmWithdrawalEvent.setPaymentMethodCode("CRYPTO2");
        produceWithdrawalMessageToCrmPaymentTopic(data.crmWithdrawalEvent);

        checkElementId("Event_0yh59iy", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Gateway_154345n", data.crmWithdrawalEvent.getId(), Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId("Activity_1kf15qu", data.crmWithdrawalEvent.getId(), Rule.ROUTER_RULE.getProcessId());

        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Connection search with known fraudster", "Used by known EXCHANGER");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert name", alerts.getFirst().getAlertId(), is(data.crmWithdrawalEvent.getId()));
        assertThat("Verify alert", alerts.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getType(), is("PAYMENT"));
        assertThat("Verify alert", alerts.getFirst().getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().getTrigger(), is("Withdrawal"));
        assertThat("Verify alert", alerts.getFirst().getReason(), is("Used by known EXCHANGER"));
        assertThat("Verify alert", alerts.getFirst().getAccount(), is(data.clientHelper.getTradingAccount()));
        assertThat("Verify alert", alerts.getFirst().getPaymentMethod(), is(data.crmWithdrawalEvent.getPaymentMethodCode()));
        assertThat("Verify alert", alerts.getFirst().getAmount(), is(data.crmWithdrawalEvent.getWithdrawalAmount()));
        assertThat("Verify alert", alerts.getFirst().getAmountUsd(), is(data.crmWithdrawalEvent.getWithdrawalAmountUSD()));
        assertThat("Verify alert", alerts.getFirst().getCurrency(), is(data.crmWithdrawalEvent.getWithdrawalCurrency()));
        assertThat("Verify alert", alerts.getFirst().getPaymentEventId(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getMerchantOrderId(), is(data.crmWithdrawalEvent.getMerchantOrderId()));
        assertThat("Verify alert", alerts.getFirst().getFraudType(), is("EXCHANGER"));
        assertThat("Verify alert", alerts.getFirst().getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getRule().getName(), is("Connection search with known fraudster"));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getProfileDeposits(), is(nullValue()));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getProfileWithdrawals(), is(nullValue()));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getCryptoWithdrawal10k(), is(true));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getCryptoDeposit(), is("0 USD"));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getCryptoWithdrawal(), is("10001 USD"));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getSameDataEwalletKyc(), is(nullValue()));
    }
}
