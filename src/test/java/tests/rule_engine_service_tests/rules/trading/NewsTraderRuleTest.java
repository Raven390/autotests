package tests.rule_engine_service_tests.rules.trading;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.DataHelper;
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
import static helpers.data.rules.trading.NewsTraderRuleDataFactory.setupNewsTraderCloseTradeRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_NEWS_TRADER_OPEN_TRADE_EVENT_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class NewsTraderRuleTest extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupNewsTraderCloseTradeRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataHelper.deleteData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("1533")
    @DisplayName("News trader on close trade. Exit without alert if user is test or social trader user")
    void mirrorTradingOpenTradeEventRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_1", data.closeTradeMtEvent.id, "news_trade");
    }

    @Test
    @AllureId("1534")
    @DisplayName("News trader on close trade. Exit without alert if user have news trade ratio <0.7")
    void mirrorTradingOpenTradeEventRuleTest2() throws Exception {
        Allure.step("generate test data where ...");
        DataHelper data = dbDataMap.get("2");

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_2", data.closeTradeMtEvent.id, "news_trade");
    }

    @Test
    @AllureId("1535")
    @DisplayName("News trader on close trade. Exit without alert if user have profit USD <350")
    void mirrorTradingOpenTradeEventRuleTest3() throws Exception {
        Allure.step("generate test data where ...");
        DataHelper data = dbDataMap.get("3");

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_3", data.closeTradeMtEvent.id, "news_trade");

    }

    @Test
    @AllureId("1536")
    @DisplayName("News Trader. Exit without alert if profit/deposit < 0.5. Event_end_4")
    void mirrorTradingOpenTradeEventRuleTest4() throws Exception {
        Allure.step("generate test data where News Trader. Exit without alert if profit/deposit < 0.5. Event_end_4");
        DataHelper data = dbDataMap.get("4");

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_4", data.closeTradeMtEvent.id, "news_trade");

    }

    @Test
    @AllureId("1537")
    @DisplayName("News Trader. Exit with alert if profit/deposit > 0.5. End_nt_alert")
    void mirrorTradingOpenTradeEventRuleTest5() throws Exception {
        Allure.step("generate test data where News Trader. Exit without alert if profit/deposit < 0.5. Event_end_4");
        DataHelper data = dbDataMap.get("5");

        Allure.step("send test event to kafka");
        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("End_nt_alert", data.closeTradeMtEvent.id, "news_trade");

        Allure.step("Verify there is alert in kafka");
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "News Trading");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().timestamp, matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().alertId, is(data.closeTradeMtEvent.id));
        assertThat("Verify alert", alerts.getFirst().type, is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().ucid, is(data.clientHelper.getUcid()));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(data.closeTradeMtEvent.eventDate));

        assertThat("Verify alert", alerts.getFirst().rule.name, is("News Trading"));
        assertThat("Verify alert", alerts.getFirst().rule.fraudType, is("NEWS_TRADER"));
        assertThat("Verify alert", alerts.getFirst().rule.trigger, is("Close Trade"));
        assertThat("Verify alert", alerts.getFirst().rule.ver, notNullValue());

        assertThat("Verify alert", alerts.getFirst().rule.attributes.reason, is("News trading pattern"));
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
        assertThat("Check comment", clientGeneralRestrictions.getFirst().getComment(), is("News trading pattern"));
        assertThat("Check status", clientGeneralRestrictions.getFirst().getStatus(), is("APPLIED"));
    }
}
