package tests.rule_engine_service_tests.rules.trading;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.trading.MarketManipulationRuleDataFactory.setupMarketManipulationRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.DataDeleteHelper;
import helpers.data.DataHelper;
import helpers.data.enums.Rule;
import io.qameta.allure.AllureId;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

class MarketManipulationRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupMarketManipulationRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        DataDeleteHelper.deleteData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("2002")
    @DisplayName("Market manipulation rule. Exit if test client. ElementId: Event_1dfbkag")
    void MarketManipulationRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_1dfbkag", data.closeTradeMtEvent.id, Rule.MARKET_MANIPULATION_RULE.getProcessId());
    }

    @Disabled
    @Test
    @DisplayName("Market manipulation rule. Exit if test client >0.7. ElementId: Event_0k840lo")
    void MarketManipulationRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_0k840lo", data.closeTradeMtEvent.id, "marketManipulation_rule");

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Market Manipulation");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
    }
}
