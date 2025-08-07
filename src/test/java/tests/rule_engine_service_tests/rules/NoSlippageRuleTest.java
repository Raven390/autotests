package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.rules.RuleDataHelper;
import io.qameta.allure.Allure;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class NoSlippageRuleTest extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
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

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @DisplayName("No slippage rule. If account currency is USC -> Exit with no alert in Event_197txjh")
    void noSlippageRuleTest2() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @DisplayName("No slippage rule. Exit without alert if at least 1 resolved alert for user -> Event_end_12")
    void noSlippageRuleTest3() throws Exception {
        RuleDataHelper data = dbDataMap.get("3");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @DisplayName("No slippage rule. Exit with alert and restriction if 0 resolved alerts for user -> Event_0oa6zyc")
    void noSlippageRuleTest4() throws Exception {
        RuleDataHelper data = dbDataMap.get("4");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

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
        assertThat("Check comment", clientGeneralRestrictions.getFirst().getComment(), is("No slippage pattern"));
        assertThat("Check status", clientGeneralRestrictions.getFirst().getStatus(), is("APPLIED"));
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
