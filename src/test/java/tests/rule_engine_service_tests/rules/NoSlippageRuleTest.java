package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.rules.RuleDataHelper;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.no_slippage_rule.NoSlippageRuleDataFactory.setupNoSlippageRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class NoSlippageRuleTest extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException {
        // Enable emulator to set restrictions to status APPLIED
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupNoSlippageRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        stopSshTunnel();
        deleteRuleData(dbDataMap);
    }

    @Test
    @DisplayName("No slippage rule. If User is test user -> Exit with no alert in Event_end_1")
    void noSlippageRuleTest1() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @DisplayName("No slippage rule. If Slippage amount<396 -> Exit with no alert in Event_0yv2mku")
    void noSlippageRuleTest2() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @DisplayName("No slippage rule. If profit(acc) / slippage(acc) > 0.61? -> exiting the rule without alert Event_0tlx8d8")
    void noSlippageRuleTest3() throws Exception {
        RuleDataHelper data = dbDataMap.get("3");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Disabled
    @Test
    @DisplayName("No slippage rule Event_0tlx8d8")
    void noSlippageRuleTest4() throws IOException, InterruptedException {

    }

    @Disabled
    @Test
    @DisplayName("No slippage rule Event_0n3wjq9")
    void noSlippageRuleTest5() throws IOException, InterruptedException {

    }

    @Disabled
    @Test
    @DisplayName("No slippage rule Event_0m5x0nm")
    void noSlippageRuleTest6() throws IOException, InterruptedException {

    }

    @Disabled
    @Test
    @DisplayName("No slippage rule Event_05pqy7v")
    void noSlippageRuleTest7() throws IOException, InterruptedException {

    }

    @Disabled
    @Test
    @DisplayName("No slippage rule Event_0b9or7o")
    void noSlippageRuleTest8() throws IOException, InterruptedException {

    }

    @Disabled
    @Test
    @DisplayName("No slippage rule Event_0505ghq")
    void noSlippageRuleTest9() throws IOException, InterruptedException {

    }

    @Disabled
    @Test
    @DisplayName("No slippage rule Event_0aw1lki")
    void noSlippageRuleTest10() throws IOException, InterruptedException {

    }

    @Disabled
    @Test
    @DisplayName("No slippage rule Event_0oa6zyc")
    void noSlippageRuleTest11() throws IOException, InterruptedException {

    }


}
