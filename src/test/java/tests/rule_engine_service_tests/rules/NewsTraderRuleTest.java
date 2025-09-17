package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
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
import static helpers.data.rules.news_trader_rule.NewsTraderRuleDataFactory.setupNewsTraderCloseTradeRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_NEWS_TRADER_OPEN_TRADE_EVENT_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class NewsTraderRuleTest extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupNewsTraderCloseTradeRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteRuleData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("1533")
    @DisplayName("News trader on close trade. Exit without alert if user is test or social trader user")
    void mirrorTradingOpenTradeEventRuleTest1() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        Allure.step("Verify amount of user alerts in kafka is 0");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        Allure.step("Verify  amount of alerts in BO DB is 0");
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1534")
    @DisplayName("News trader on close trade. Exit without alert if user have news trade ratio <0.7")
    void mirrorTradingOpenTradeEventRuleTest2() throws Exception {
        Allure.step("generate test data where ...");
        RuleDataHelper data = dbDataMap.get("2");

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        Allure.step("Verify amount of user alerts in kafka is 0");
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        Allure.step("Verify  amount of alerts in BO DB is 0");
        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1535")
    @DisplayName("News trader on close trade. Exit without alert if user have profit USD <350")
    void mirrorTradingOpenTradeEventRuleTest3() throws Exception {
        Allure.step("generate test data where ...");
        RuleDataHelper data = dbDataMap.get("3");

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        Allure.step("Verify amount of user alerts in kafka is 0");
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        Allure.step("Verify  amount of alerts in BO DB is 0");
        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1536")
    @DisplayName("News Trader. Exit without alert if profit/deposit < 0.5. Event_end_4")
    void mirrorTradingOpenTradeEventRuleTest4() throws Exception {
        Allure.step("generate test data where News Trader. Exit without alert if profit/deposit < 0.5. Event_end_4");
        RuleDataHelper data = dbDataMap.get("4");

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        Allure.step("Verify amount of user alerts in kafka is 0");
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        Allure.step("Verify  amount of alerts in BO DB is 0");
        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1537")
    @DisplayName("News Trader. Exit with alert if profit/deposit > 0.5. Event_end_5")
    void mirrorTradingOpenTradeEventRuleTest5() throws Exception {
        Allure.step("generate test data where News Trader. Exit without alert if profit/deposit < 0.5. Event_end_4");
        RuleDataHelper data = dbDataMap.get("5");

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        Allure.step("Verify there is alert in kafka");
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "News Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));

        Allure.step("Verify there is alert in BO DB");
        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
    }
}
