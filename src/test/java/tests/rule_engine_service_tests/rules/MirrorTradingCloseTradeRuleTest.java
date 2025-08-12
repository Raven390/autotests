package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.rules.RuleDataHelper;
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

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.mirror_trading_close_trade_event_rule.MirrorTradingOpenTradeEventRuleDataFactory.setupMirrorTradingCloseTradeRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_MIRROR_TRADING_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class MirrorTradingCloseTradeRuleTest extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupMirrorTradingCloseTradeRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteRuleData(dbDataMap);
    }

    @Test
    @AllureId("1431")
    @DisplayName("Mirror trading. Exit without alert if user is test account. Event_end_1")
    void mirrorTradeRuleTest1() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Exit without alert if user has no credits. Event_end_3")
    void mirrorTradeRuleTest2() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Exit without alert if tades < 5. Event_0vlh2iw")
    void mirrorTradeRuleTest3() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Exit without alert if tades > 200. Event_0vlh2iw")
    void mirrorTradeRuleTest4() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Exit without alert if ucid score < 0.9. Event_1n666vd")
    void mirrorTradeRuleTest5() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Post alert and restriction if no previously resolved alerts. Event_1m3mqdr")
    void mirrorTradeRuleTest6() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Post alert and restriction if no previously resolved alerts. Event_1m3mqdr")
    void mirrorTradeRuleTest7() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Exit without alerts if deposits > 5000. Event_end_2")
    void mirrorTradeRuleTest8() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Exit without alerts if trades > 300. Event_end_2")
    void mirrorTradeRuleTest9() throws Exception {

    }

    @Test
    @AllureId("1432")
    @DisplayName("Mirror trading. Scotland. Exit without alert if trades count > 5. Event_end_8")
    void mirrorTradeRuleTest10() throws Exception {
        RuleDataHelper data = dbDataMap.get("10");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1433")
    @DisplayName("Mirror trading. Scotland. Exit without alert if profit/(deposit+credit) < 0.6. Event_end_8")
    void mirrorTradeRuleTest11() throws Exception {
        RuleDataHelper data = dbDataMap.get("11");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1434")
    @DisplayName("Mirror trading. Scotland. Exit without alert if Leverage < 200. Event_12inxex")
    void mirrorTradeRuleTest12() throws Exception {
        RuleDataHelper data = dbDataMap.get("12");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1435")
    @DisplayName("Mirror trading. Scotland. Exit with alert and restriction if Leverage > 200. Event_end_4")
    void mirrorTradeRuleTest13() throws Exception {
        RuleDataHelper data = dbDataMap.get("13");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        Thread.sleep(5000);
        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        assertThat("Check ucid", clientGeneralRestrictions.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Check regulator", clientGeneralRestrictions.getFirst().getRegulator(), is(data.clientHelper.getRegulator()));
        assertThat("Check restrictionId", clientGeneralRestrictions.getFirst().getRestrictionId(), is(8L));
        assertThat("Check comment", clientGeneralRestrictions.getFirst().getComment(), is("Mirror trade pattern"));
        assertThat("Check status", clientGeneralRestrictions.getFirst().getStatus(), is("APPLIED"));
    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Waves. Exit without alerts if pattern not matched. Event_end_9")
    void mirrorTradeRuleTest14() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Waves. Exit without alerts if previously at least 1 resolved alert. Event_end_5")
    void mirrorTradeRuleTest15() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Waves. Exit with alerts if previously 0 resolved alerts. Event_end_5")
    void mirrorTradeRuleTest16() throws Exception {

    }
}
