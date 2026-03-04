package tests.rule_engine_service_tests.rules.trading;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.asserts.AlertsAssertsHelper.assertThatAlertNotFailed;
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.enums.FraudType.NEWS_TRADER;
import static helpers.data.rules.trading.NewsTraderRuleDataFactory.setupNewsTraderCloseTradeRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.kafka.alerts.RuleAlertV2;
import helpers.data.DataHelper;
import helpers.data.enums.rule_engine.Rule;
import io.qameta.allure.Allure;
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
@Story(STORY_RULE_ENGINE_NEWS_TRADER_CLOSE_TRADE_EVENT_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class NewsTraderRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupNewsTraderCloseTradeRuleData();
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("2309")
    @DisplayName("News trader on close trade. Exit without alert if user is test or social trader user")
    void newsTradingCloseTradeEventRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");
        setupData(data);

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_1", data.closeTradeMtEvent.id, Rule.NEWS_TRADE_RULE.getProcessId());
    }

    @Test
    @AllureId("2310")
    @DisplayName(
            "News trader on close trade. Exit without alert if user have news trade ratio <0.7 profitTotal/profitNews=0.5 Event_end_5")
    void newsTradingCloseTradeEventRuleTest2() throws Exception {
        Allure.step("generate test data where ...");
        DataHelper data = dbDataMap.get("2");
        setupData(data);

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_5", data.closeTradeMtEvent.id, Rule.NEWS_TRADE_RULE.getProcessId());
    }

    @Test
    @AllureId("2311")
    @DisplayName(
            "News trader on close trade. Exit without alert if user have news trade ratio <0.7 profitTotal/profitNews< 0.6 leverage < 50. Event_end_6")
    void newsTradingCloseTradeEventRuleTest21() throws Exception {
        Allure.step("generate test data where ...");
        DataHelper data = dbDataMap.get("21");
        setupData(data);

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_6", data.closeTradeMtEvent.id, Rule.NEWS_TRADE_RULE.getProcessId());
    }

    @Test
    @AllureId("2312")
    @DisplayName(
            "News trader on close trade. Exit without alert if user have news trade ratio <0.7 profitTotal/profitNews< 0.6 leverage > 50. ucidScore < 0.7. Event_end_7")
    void newsTradingCloseTradeEventRuleTest22() throws Exception {
        Allure.step("generate test data where ...");
        DataHelper data = dbDataMap.get("22");
        setupData(data);

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_7", data.closeTradeMtEvent.id, Rule.NEWS_TRADE_RULE.getProcessId());
    }

    @Test
    @AllureId("2313")
    @DisplayName(
            "News trader on close trade. Exit without alert if user have news trade ratio <0.7 profitTotal/profitNews< 0.6 leverage > 50. ucidScore > 0.7. get_trades_gr_by")
    void newsTradingCloseTradeEventRuleTest23() throws Exception {
        Allure.step("generate test data where ...");
        DataHelper data = dbDataMap.get("23");
        setupData(data);

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("get_general_score", data.closeTradeMtEvent.id, Rule.NEWS_TRADE_RULE.getProcessId());
        checkElementId("get_trades_gr_by", data.closeTradeMtEvent.id, Rule.NEWS_TRADE_RULE.getProcessId());
    }

    @Test
    @AllureId("2314")
    @DisplayName("News trader on close trade. Exit without alert if user have profit USD <350")
    void newsTradingCloseTradeEventRuleTest3() throws Exception {
        Allure.step("generate test data where ...");
        DataHelper data = dbDataMap.get("3");
        setupData(data);

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_3", data.closeTradeMtEvent.id, Rule.NEWS_TRADE_RULE.getProcessId());
    }

    @Test
    @AllureId("2315")
    @DisplayName("News Trader. Exit without alert if profit/deposit < 0.5. Event_end_4")
    void newsTradingCloseTradeEventRuleTest4() throws Exception {
        Allure.step("generate test data where News Trader. Exit without alert if profit/deposit < 0.5. Event_end_4");
        DataHelper data = dbDataMap.get("4");
        setupData(data);

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_4", data.closeTradeMtEvent.id, Rule.NEWS_TRADE_RULE.getProcessId());
    }

    @Test
    @AllureId("2316")
    @DisplayName("News Trader. Exit with alert if profit/deposit > 0.5. End_nt_alert")
    void newsTradingCloseTradeEventRuleTest5() throws Exception {
        Allure.step("generate test data where News Trader. Exit without alert if profit/deposit < 0.5. Event_end_4");
        DataHelper data = dbDataMap.get("5");
        setupData(data);

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("End_nt_alert", data.closeTradeMtEvent.id, Rule.NEWS_TRADE_RULE.getProcessId());

        // Verify alert kafka
        Allure.step("Get alerts kafka messages");
        List<RuleAlertV2> alerts = getUserAlertsV2FromKafka(data, "News Trading");
        assertThat("Verify that there is only 1 alert", alerts.size(), equalTo(1));
        assertThat("Verify alert id not null", alerts.getFirst().getAlertId(), notNullValue());
        assertThat("Verify timestamp not null", alerts.getFirst().getTimestamp(), notNullValue());
        assertThat("Verify ucid is correct", alerts.getFirst().getUcid(), equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alerts.getFirst().getRule(), notNullValue());
        assertThat("Verify rule ver not null", alerts.getFirst().getRule().getVer(), notNullValue());
        assertThat("Verify rule name is correct", alerts.getFirst().getRule().getName(), equalTo("News Trading"));
        assertThat("Verify rule trigger is correct", alerts.getFirst().getTrigger(), equalTo("Close Trade"));
        assertThat(
                "Verify rule fraud type is correct", alerts.getFirst().getFraudType(), equalTo(NEWS_TRADER.getCode()));
        assertThat("Verify rule version not null, alert.rule.ver", notNullValue());
        assertThat("Verify rule attributes not null", alerts.getFirst().getAttributes(), notNullValue());

        assertThatAlertNotFailed(data.clientHelper.getUcid(), "News Trading");

        // Verify restriction
        checkManualWithdrawalRestrictionApplied(data, "News trading pattern");
        // TODO enable after implementig for rule
        //        List<AiAlert> aiAlerts = getUserAiAlertFromKafka(data, "News Trading", "News trading pattern
        // detected");
        //        assertAiAlert(data, aiAlerts, "News Trading", NEWS_TRADER.getCode(), "News trading pattern detected");
    }
}
