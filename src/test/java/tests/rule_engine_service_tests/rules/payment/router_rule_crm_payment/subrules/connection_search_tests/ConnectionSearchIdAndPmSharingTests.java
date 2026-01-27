package tests.rule_engine_service_tests.rules.payment.router_rule_crm_payment.subrules.connection_search_tests;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.payments.router_rule_crm_payment.connection_search.ConnectionSearchIdAndPmSharingDataFactory.setupConnectionSearchPmAndIdSharingRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Rule;
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
class ConnectionSearchIdAndPmSharingTests extends TestBaseRule {

    private static Map<String, DataHelper> dataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dataMap = setupConnectionSearchPmAndIdSharingRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dataMap);
    }

    @Test
    @AllureId("1835")
    @DisplayName("Connection Search in router rule. No alert if Email / Phone compare = true. ElementId: Event_1i416tj")
    void connectionSearchRuleTest1() throws Exception {
        DataHelper data = dataMap.get("1");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_1i416tj",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_0tbmvdy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("2015")
    @DisplayName(
            "Connection Search in router rule. Exit without alert if payment profile verified for another user and name_birth=false. ElementId: Event_0jxlf93")
    void connectionSearchRuleTest2() throws Exception {
        DataHelper data = dataMap.get("2");
        DataHelper data2 = dataMap.get("2_1");
        setupData(data);
        setupData(data2);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_0jxlf93",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("2016")
    @DisplayName(
            "Connection Search in router rule. Exit with alert if payment profile verified for another user and name_birth=true. ElementId: Event_0sdekqs")
    void connectionSearchRuleTest3() throws Exception {
        DataHelper data = dataMap.get("3");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_0sdekqs",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        List<RuleAlertV2> alerts =
                getUserAlertsV2FromKafka(data.clientHelper, "Connection search", "Verified by other client");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify attribute", alerts.getFirst().getAttributes().getSharedUniqueIdentifier(), is("No"));
        assertThat("Verify attribute", alerts.getFirst().getAttributes().getVerifiedByOtherClient(), is("2 clients"));
        assertThat("Verify attribute", alerts.getFirst().getAttributes().getSharedPaymentProfile(), is("1 clients"));
        assertThat(
                "Verify attribute",
                alerts.getFirst().getAttributes().getProfileWithdrawals(),
                is("19998 USD for 2 clients"));
        assertThat(
                "Verify attribute",
                alerts.getFirst().getAttributes().getProfileDeposits(),
                is("1000 USD for 2 clients"));
    }

    @Test
    @AllureId("1836")
    @DisplayName(
            "Connection Search in router rule. All connected by payout are connected by NAME_BIRTH or DOCUMENT = true. ElementId: Event_1ypjv6r")
    void connectionSearchIdAndPmSharingTest4() throws Exception {
        DataHelper data = dataMap.get("4");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_1ypjv6r",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_0tbmvdy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1837")
    @DisplayName(
            "Connection Search in router rule. All connected by payout are connected by NAME_BIRTH or DOCUMENT = false. ElementId: Event_1sjvssv")
    void connectionSearchIdAndPmSharingTest5() throws Exception {
        DataHelper data = dataMap.get("5");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_1sjvssv",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_0tbmvdy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1838")
    @DisplayName("Connection Search in router rule. Is Crypto withdrawal = false. ElementId: Event_1w0lx0h")
    void connectionSearchIdAndPmSharingTest6() throws Exception {
        DataHelper data = dataMap.get("6");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_1w0lx0h",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_0tbmvdy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1839")
    @DisplayName("Connection Search in router rule. payoutConnectionsCount > 19 = true. ElementId: Event_1d2pl82")
    void connectionSearchIdAndPmSharingTest7() throws Exception {
        DataHelper data = dataMap.get("7");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_1d2pl82",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Activity_0w14j8s",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_0tbmvdy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1840")
    @DisplayName("Connection Search in router rule. payoutConnectionsCount > 5 = false. ElementId: Event_10q9rcg")
    void connectionSearchIdAndPmSharingTest8() throws Exception {
        DataHelper data = dataMap.get("8");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_10q9rcg",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_0tbmvdy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1841")
    @DisplayName(
            "Connection Search in router rule. Send alert if Single transaction of >= 10K USD = true. ElementId: Event_13t7cj9")
    void connectionSearchIdAndPmSharingTest9() throws Exception {
        DataHelper data = dataMap.get("9");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_13t7cj9",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Activity_0w14j8s",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_0tbmvdy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());

        List<RuleAlertV2> alerts =
                getUserAlertsV2FromKafka(data.clientHelper, "Connection search", "Crypto withdrawal > 10k");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert name", alerts.getFirst().getAlertId(), is(data.crmWithdrawalEventV2.getId()));
        assertThat("Verify alert", alerts.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getType(), is("PAYMENT"));
        assertThat("Verify alert", alerts.getFirst().getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().getTrigger(), is("Withdrawal"));
        assertThat("Verify alert", alerts.getFirst().getReason(), is("Crypto withdrawal > 10k"));
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
        assertThat("Verify alert", alerts.getFirst().getFraudType(), is("POTENTIAL_ABUSE"));
        assertThat("Verify alert", alerts.getFirst().getRule().getVer(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getRule().getName(), is("Connection search"));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getProfileDeposits(), is(nullValue()));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getProfileWithdrawals(), is(nullValue()));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getCryptoWithdrawal10k(), is(true));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getCryptoDeposit(), is("0 USD"));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getCryptoWithdrawal(), is("10001 USD"));
        assertThat("Verify alert", alerts.getFirst().getAttributes().getSharedUniqueIdentifier(), is(nullValue()));
    }

    @Test
    @AllureId("1842")
    @DisplayName(
            "Connection Search in router rule. Sum Deposit OR Withdrawal >= 50K USD = true. ElementId: Event_0sscypz")
    void connectionSearchIdAndPmSharingTest10() throws Exception {
        DataHelper data = dataMap.get("10");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_0sscypz",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Activity_0w14j8s",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_0tbmvdy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }

    @Test
    @AllureId("1843")
    @DisplayName(
            "Connection Search in router rule. Sum Deposit OR Withdrawal >= 50K USD = false. ElementId: Event_136ttoj")
    void connectionSearchIdAndPmSharingTest11() throws Exception {
        DataHelper data = dataMap.get("11");
        setupData(data);

        produceWithdrawalMessageV2ToCrmPaymentTopic(data.crmWithdrawalEventV2);

        checkElementId(
                "Event_136ttoj",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "Gateway_0tbmvdy",
                data.crmWithdrawalEventV2.getId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementId(
                "payment_branch_end_for_withdrawal",
                data.crmWithdrawalEventV2.getId(),
                Rule.ROUTER_RULE_SHADOW_MODE.getProcessId());
    }
}
