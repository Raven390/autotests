package tests.rule_engine_service_tests.rules.payment.router_rule_withdrawal_from_wa;

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
class ConnectionSearchIdAndPmSharingWithdrawalFromWATests extends TestBaseRule {

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
    @AllureId("2445")
    @DisplayName(
            "Connection Search(id and pm sharing branch withdrawal from wa) in router rule. Send alert if Single transaction of >= 10K USD = true. ElementId: end_204")
    void connectionSearchIdAndPmSharingTest9() throws Exception {
        DataHelper data = dataMap.get("9");
        setupData(data);
        setupData(dataMap.get("9_1"));
        setupData(dataMap.get("9_2"));
        setupData(dataMap.get("9_3"));
        setupData(dataMap.get("9_4"));
        setupData(dataMap.get("9_5"));
        setupData(dataMap.get("9_6"));

        produceWithdrawalFromWaToCrmPaymentTopic(data.getCrmWithdrawalFromWaEvent());

        checkElementIdSubrule(
                "end_204",
                data.getCrmWithdrawalFromWaEvent().getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_FROM_WA.getProcessId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementIdSubrule(
                "create_alert_var_id_pm_sharing",
                data.getCrmWithdrawalFromWaEvent().getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_FROM_WA.getProcessId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());
        checkElementIdSubrule(
                "id_pm_sharing_end",
                data.getCrmWithdrawalFromWaEvent().getId(),
                Rule.ROUTER_RULE_WITHDRAWAL_FROM_WA.getProcessId(),
                Rule.CONNECTION_SEARCH_IN_ROUTER_RULE.getProcessId());

        List<RuleAlertV2> alerts =
                getUserAlertsV2FromKafka(data.clientHelper, "Connection search", "Crypto withdrawal > 10k");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat(
                "Verify alert",
                alerts.getFirst().getAlertId(),
                is(data.getCrmWithdrawalFromWaEvent().getId()));
        assertThat("Verify alert", alerts.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getType(), is("PAYMENT"));
        assertThat("Verify alert", alerts.getFirst().getTriggerCreatedTime(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().getTrigger(), is("withdrawalFromWA"));
        assertThat("Verify alert", alerts.getFirst().getReason(), is("Crypto withdrawal > 10k"));
        assertThat(
                "Verify alert",
                alerts.getFirst().getAccount(),
                is(data.getCrmWithdrawalFromWaEvent().getWalletAccount()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getPaymentMethod(),
                is(data.getCrmWithdrawalFromWaEvent().getPaymentMethodCode()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getAmount(),
                is(data.getCrmWithdrawalFromWaEvent().getWithdrawalAmount()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getAmountUsd(),
                is(data.getCrmWithdrawalFromWaEvent().getWithdrawalAmountUSD()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getCurrency(),
                is(data.getCrmWithdrawalFromWaEvent().getWithdrawalCurrency()));
        assertThat("Verify alert", alerts.getFirst().getPaymentEventId(), is(notNullValue()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getMerchantOrderId(),
                is(data.getCrmWithdrawalFromWaEvent().getMerchantOrderId()));
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
}
