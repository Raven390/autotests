package tests.rule_engine_service_tests.rules.trading;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.Rule.MIRROR_TRADE_INTERNAL_HEDGE;
import static helpers.data.rules.trading.MirrorTradingInternalHedgeRuleDataFactory.setupMirrorTradingInternalHedgeRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_LATENCY_ARBITRAGE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MirrorTradingInternalHedgeRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws Exception {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingInternalHedgeRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("1898")
    @DisplayName(
            "Mirror trade internal hedge. Exit without alert if (Is deal profit of the positive leg > 50$?) = false. ElementId: EVENT_END_NO_ALERT")
    void mirrorTradeInternalHedgeTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");
        setupData(data);

        produceInternalHedgeMessageToKafka(data.internalHedgeEvent);

        checkElementId(
                "EVENT_END_NO_ALERT", data.internalHedgeEvent.getId(), MIRROR_TRADE_INTERNAL_HEDGE.getProcessId());
    }

    @Test
    @AllureId("1899")
    @DisplayName(
            "Mirror trade internal hedge. Exit with alert if (Is deal profit of the positive leg > 50$?) = true. ElementId: EVENT_END_WITH_ALERT")
    void mirrorTradeInternalHedgeTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");
        setupData(data);

        produceInternalHedgeMessageToKafka(data.internalHedgeEvent);

        checkElementId(
                "EVENT_END_WITH_ALERT", data.internalHedgeEvent.getId(), MIRROR_TRADE_INTERNAL_HEDGE.getProcessId());

        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().getAlertId(), is(data.internalHedgeEvent.getId()));
        assertThat("Verify alert", alerts.getFirst().getTimestamp(), is(notNullValue()));
        assertThat("Verify alert", alerts.getFirst().getType(), is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().getTriggerCreatedTime(), is(notNullValue()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getUcid(),
                is(data.internalHedgeEvent.getPositiveLeg().getUcid()));
        assertThat("Verify alert", alerts.getFirst().getTrigger(), is("Close Trade"));
        assertThat("Verify alert", alerts.getFirst().getFraudType(), is("HEDGING"));
        assertThat(
                "Verify alert",
                alerts.getFirst().getAccount(),
                is(data.internalHedgeEvent.getPositiveLeg().getTradingAccount()));
        assertThat("Verify alert", alerts.getFirst().getSymbol(), is(data.internalHedgeEvent.getSymbolUnderlying()));
        assertThat(
                "Verify alert",
                alerts.getFirst().getServerId(),
                is(data.internalHedgeEvent.getPositiveLeg().getServerId().toString()));
        assertThat("Verify alert", alerts.getFirst().getReason(), is("Suspicion of an Internal Hedge"));
        assertThat("Verify alert. rule", alerts.getFirst().getRule().getVer(), is("2.5.0"));
        assertThat("Verify alert. rule", alerts.getFirst().getRule().getName(), is("Mirror Trading"));
        assertThat(
                "Verify alert. attributes",
                alerts.getFirst().getAttributes().getUcidNegativeLeg(),
                is(data.internalHedgeEvent.getNegativeLeg().getUcid()));
        assertThat(
                "Verify alert. attributes",
                alerts.getFirst().getAttributes().getAccountNegativeLeg(),
                is(data.internalHedgeEvent.getNegativeLeg().getTradingAccount().toString()));
        assertThat(
                "Verify alert. attributes",
                alerts.getFirst().getAttributes().getServerIdNegativeLeg(),
                is(data.internalHedgeEvent.getNegativeLeg().getServerId().toString()));
        assertThat(
                "Verify alert. attributes",
                alerts.getFirst().getAttributes().getInternalHedgeTime(),
                is(notNullValue()));

        checkManualWithdrawalRestrictionApplied(data.clientHelper, "Suspicion of an Internal Hedge");
    }
}
