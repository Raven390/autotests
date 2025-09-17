package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.rules.RuleDataHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.market_manipulation_rule.MarketManipulationRuleDataFactory.setupMarketManipulationRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MarketManipulationRuleTests extends TestBaseRule {
    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupMarketManipulationRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteRuleData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @DisplayName("Market manipulation rule. Exit with alert if ucidScore >0.7. ElementId: Event_0k840lo")
    void MarketManipulationRuleTest1() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "Market Manipulation");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        checkElementId("Event_0k840lo", data.closeTradeMtEvent.id, "marketManipulation_rule");
    }
}
