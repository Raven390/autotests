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
import static helpers.data.rules.mirror_trading_close_trade_event_rule.MirrorTradingCloseTradeEventRuleDataFactory.setupMirrorTradingCloseTradeRuleData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_MIRROR_TRADING_CLOSE_TRADE_RULE)
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
    @DisplayName("Mirror trading. Exit without alert if user is test account. ElementId: Event_end_1")
    void mirrorTradeRuleTest1() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Exit without alert if user has no credits. ElementId: Event_end_3")
    void mirrorTradeRuleTest2() throws Exception {

    }

    @Test
    @AllureId("1524")
    @DisplayName("Mirror trading. Web hedge. Exit without alert if user geo is not vietnam. ElementId: Event_1t7mktu")
    void mirrorTradeRuleTest18() throws Exception {
        RuleDataHelper data = dbDataMap.get("18");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1525")
    @DisplayName("Mirror trading. Web hedge. Exit without alert if user has no crypto deposits. ElementId: Event_06qi81c")
    void mirrorTradeRuleTest19() throws Exception {
        RuleDataHelper data = dbDataMap.get("19");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1526")
    @DisplayName("Mirror trading. Web hedge. Exit without alert if user has country != vietnam. ElementId: Event_06qi81c")
    void mirrorTradeRuleTest20() throws Exception {
        RuleDataHelper data = dbDataMap.get("20");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1527")
    @DisplayName("Mirror trading. Web hedge. Exit without alert if user has not all trades from web trader. ElementId: Event_06qi81c")
    void mirrorTradeRuleTest21() throws Exception {
        RuleDataHelper data = dbDataMap.get("21");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1528")
    @DisplayName("Mirror trading. Web hedge. Exit without alert if user has resolved alerts. ElementId: Event_06qi81c")
    void mirrorTradeRuleTest22() throws Exception {
        RuleDataHelper data = dbDataMap.get("22");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1529")
    @DisplayName("Mirror trading. Web hedge. Exit with restriction alert if user doesn't has resolved alerts. ElementId: Event_06qi81c")
    void mirrorTradeRuleTest23() throws Exception {
        RuleDataHelper data = dbDataMap.get("23");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        Thread.sleep(30_000);
        // Verify restriction is bonus restriction with code 13
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only restriction", clientGeneralRestrictions.size(), equalTo(1));
        assertThat("Verify restriction id ", clientGeneralRestrictions.getFirst().getRestrictionId(), equalTo(8L));
    }


    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Exit without alert if tades < 5. ElementId: Event_0vlh2iw")
    void mirrorTradeRuleTest3() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Exit without alert if tades > 200. ElementId: Event_0vlh2iw")
    void mirrorTradeRuleTest4() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Exit without alert if ucid score < 0.9. ElementId: Event_1n666vd")
    void mirrorTradeRuleTest5() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Ml model. Exit if at lease 1 resolved alert. ElementId: Event_1m3mqdr")
    void mirrorTradeRuleTest6() throws Exception {

    }

    @Test
    @AllureId("1494")
    @DisplayName("Mirror trading. Ml model. Post alert and restriction if no previously resolved alerts. ElementId: Event_1m3mqdr")
    void mirrorTradeRuleTest7() throws Exception {
        RuleDataHelper data = dbDataMap.get("7");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().timestamp, matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().alertId, is(data.closeTradeMtEvent.id));
        assertThat("Verify alert", alerts.getFirst().type, is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().ucid, is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(data.closeTradeMtEvent.eventDate));

        assertThat("Verify alert", alerts.getFirst().rule.name, is("Mirror Trading"));
        assertThat("Verify alert", alerts.getFirst().rule.fraudType, is("HEDGING"));
        assertThat("Verify alert", alerts.getFirst().rule.trigger, is("Close Trade"));
        assertThat("Verify alert", alerts.getFirst().rule.ver, notNullValue());

        assertThat("Verify alert", alerts.getFirst().rule.attributes.reason, is("ML Model suspects the client of Mirror Trading"));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.symbolTraded, is(data.closeTradeMtEvent.symbol));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.serverId, is(data.closeTradeMtEvent.serverId));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.ticketId, is(String.valueOf(data.closeTradeMtEvent.tradeId)));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.account, is(String.valueOf(data.closeTradeMtEvent.tradingAccount)));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        assertThat("Check ucid", clientGeneralRestrictions.getFirst().getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Check regulator", clientGeneralRestrictions.getFirst().getRegulator(), is(data.clientHelper.getRegulator()));
        assertThat("Check restrictionId", clientGeneralRestrictions.getFirst().getRestrictionId(), is(8L));
        assertThat("Check comment", clientGeneralRestrictions.getFirst().getComment(), is("ML Model suspects the client of Mirror Trading"));
        assertThat("Check status", clientGeneralRestrictions.getFirst().getStatus(), is("APPLIED"));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Exit without alerts if deposits > 5000. ElementId:  Event_end_2")
    void mirrorTradeRuleTest8() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Exit without alerts if trades > 300. ElementId: Event_end_2")
    void mirrorTradeRuleTest9() throws Exception {

    }

    @Test
    @AllureId("1432")
    @DisplayName("Mirror trading. Scotland. Exit without alert if trades count > 5. ElementId: Event_end_8")
    void mirrorTradeRuleTest10() throws Exception {
        RuleDataHelper data = dbDataMap.get("10");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1433")
    @DisplayName("Mirror trading. Scotland. Exit without alert if profit/(deposit+credit) < 0.6. ElementId: Event_end_8")
    void mirrorTradeRuleTest11() throws Exception {
        RuleDataHelper data = dbDataMap.get("11");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1434")
    @DisplayName("Mirror trading. Scotland. Exit without alert if Leverage < 200. ElementId: Event_12inxex")
    void mirrorTradeRuleTest12() throws Exception {
        RuleDataHelper data = dbDataMap.get("12");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1435")
    @DisplayName("Mirror trading. Scotland. Exit with alert and restriction if Leverage > 200. ElementId: Event_end_4")
    void mirrorTradeRuleTest13() throws Exception {
        RuleDataHelper data = dbDataMap.get("13");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        Thread.sleep(30_000);
        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Mirror Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().timestamp, matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().alertId, is(data.closeTradeMtEvent.id));
        assertThat("Verify alert", alerts.getFirst().type, is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().ucid, is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(data.closeTradeMtEvent.eventDate));

        assertThat("Verify alert", alerts.getFirst().rule.name, is("Mirror Trading"));
        assertThat("Verify alert", alerts.getFirst().rule.fraudType, is("HEDGING"));
        assertThat("Verify alert", alerts.getFirst().rule.trigger, is("Close Trade"));
        assertThat("Verify alert", alerts.getFirst().rule.ver, notNullValue());

        assertThat("Verify alert", alerts.getFirst().rule.attributes.reason, is("The client hides the fraud inside several waves"));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.symbolTraded, is(data.closeTradeMtEvent.symbol));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.serverId, is(data.closeTradeMtEvent.serverId));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.ticketId, is(String.valueOf(data.closeTradeMtEvent.tradeId)));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.account, is(String.valueOf(data.closeTradeMtEvent.tradingAccount)));

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
    @DisplayName("Mirror trading. Waves. Exit without alerts if pattern not matched. ElementId: Event_end_9")
    void mirrorTradeRuleTest14() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Waves. Exit without alerts if previously at least 1 resolved alert. ElementId: Event_end_5")
    void mirrorTradeRuleTest15() throws Exception {

    }

    @Disabled
    @Test
    @DisplayName("Mirror trading. Waves. Exit with alerts if previously 0 resolved alerts. ElementId: Event_end_5")
    void mirrorTradeRuleTest16() throws Exception {

    }


}
