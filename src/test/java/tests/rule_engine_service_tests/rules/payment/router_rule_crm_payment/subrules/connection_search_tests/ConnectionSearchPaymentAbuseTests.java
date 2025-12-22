package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.subrules.connection_search_tests;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.payments.router_rule_crm_payment.connection_search.ConnectionSearchPaymentAbuseDataFactory.setupConnectionSearchPaymentRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataDeleteHelper;
import helpers.data.DataHelper;
import helpers.data.enums.Rule;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

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
        DataDeleteHelper.deleteData(dataMap);
    }

    @Test
    @AllureId("1844")
    @DisplayName(
            "Connection Search in router rule. Exit if no matching connections. ElementId: end_connections_not_found2")
    void connectionSearchPaymentAbuseTest1() throws Exception {
        DataHelper data = dataMap.get("1");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_connections_not_found2",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1845")
    @DisplayName("Connection Search in router rule. Exit if no toxic account linked. ElementId: end_cs_no_abuse")
    void connectionSearchPaymentAbuseTest2() throws Exception {
        DataHelper data = dataMap.get("2");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_cs_no_abuse",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1846")
    @DisplayName("Connection Search in router rule. Exit if unknown fraud type. ElementId: end_unknown_FT")
    void connectionSearchPaymentAbuseTest3() throws Exception {
        DataHelper data = dataMap.get("3");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "end_unknown_FT",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1847")
    @DisplayName("Connection Search in router rule. Exit without alert. ElementId: Event_1sc8b2t")
    void connectionSearchPaymentAbuseTest4() throws Exception {
        DataHelper data = dataMap.get("4");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_1sc8b2t",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1848")
    @DisplayName("Connection Search in router rule. Exit with alert with strong + confirmed. ElementId: Event_0yh59iy")
    void connectionSearchPaymentAbuseTest5() throws Exception {
        DataHelper data = dataMap.get("5");

        data.crmWithdrawalEventV2.setPaymentMethodCode("CRYPTO2");
        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_0yh59iy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(
                data.clientHelper, "Connection search with known fraudster", "Used by known EXCHANGER");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert name", alerts.getFirst().getAlertId(), is(data.crmWithdrawalEventV2.getId()));
        assertThat("Verify alert", alerts.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getType(), is("PAYMENT"));
        assertThat("Verify alert", alerts.getFirst().getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().getTrigger(), is("Withdrawal"));
        assertThat("Verify alert", alerts.getFirst().getReason(), is("Used by known EXCHANGER"));
        assertThat("Verify alert", alerts.getFirst().getAccount(), is(data.clientHelper.getTradingAccount()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getPaymentMethod(),
                is(data.crmWithdrawalEventV2.getPaymentMethodCode()));
        assertThat("Verify alert", alerts.getFirst().getAmount(), is(data.crmWithdrawalEventV2.getWithdrawalAmount()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getAmountUsd(),
                is(data.crmWithdrawalEventV2.getWithdrawalAmountUSD()));
        assertThat(
                "Verify alert", alerts.getFirst().getCurrency(), is(data.crmWithdrawalEventV2.getWithdrawalCurrency()));
        assertThat("Verify alert", alerts.getFirst().getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getMerchantOrderId(),
                is(data.crmWithdrawalEventV2.getMerchantOrderId()));
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

    @Test
    @AllureId("1982")
    @DisplayName(
            "Connection Search in router rule. Exit with alert with medium confirmed + 0.6 connect(pnl > 2000 or sumWithdrawalsStrPotConnections > 4000). ElementId: Event_0yh59iy")
    void connectionSearchPaymentAbuseTest6() throws Exception {
        DataHelper data = dataMap.get("6");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_0yh59iy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1983")
    @DisplayName(
            "Connection Search in router rule. Exit with alert with strong potential + 0.8 connect(pnl < 500 or sumWithdrawalsStrPotConnections < 1000). ElementId: Event_0yh59iy")
    void connectionSearchPaymentAbuseTest7() throws Exception {
        DataHelper data = dataMap.get("7");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_0yh59iy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1984")
    @DisplayName(
            "Connection Search in router rule. Exit without alert with medium confirmed + 0.6 connect(pnl < 2000 or sumWithdrawalsStrPotConnections < 4000). ElementId: Event_1sc8b2t")
    void connectionSearchPaymentAbuseTest8() throws Exception {
        DataHelper data = dataMap.get("8");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_1sc8b2t",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1985")
    @DisplayName(
            "Connection Search in router rule. Exit without alert with strong potential + 0.8 connect(pnl < 500 or sumWithdrawalsStrPotConnections < 1000). ElementId: Event_1sc8b2t")
    void connectionSearchPaymentAbuseTest9() throws Exception {
        DataHelper data = dataMap.get("9");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_1sc8b2t",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1986")
    @DisplayName("Connection Search in router rule. Exit without alert with medium potential. ElementId: Event_1sc8b2t")
    void connectionSearchPaymentAbuseTest10() throws Exception {
        DataHelper data = dataMap.get("10");

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_1sc8b2t",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_154345n",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }
}
