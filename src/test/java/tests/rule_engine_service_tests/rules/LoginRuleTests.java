package tests.rule_engine_service_tests.rules;

import business_objects.api.abuse_registry.GetStatusResponseBody;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.enums.FraudType;
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

import static business_objects.api.mitigation_service.MitigationServiceRequest.*;
import static helpers.data.rules.RuleDataHelper.deleteRuleData;
import static helpers.data.rules.login_rule.LoginRuleDataFactory.setupLoginRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_RULE_ENGINE_RULES_TESTS;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_LOGIN_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class LoginRuleTests extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException, InterruptedException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupLoginRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteRuleData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("1461")
    @DisplayName("Login rule. Connection search sub-process. Exit without alert if user has no connections. end_connections_not_found")
    void loginRuleTest1() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");

        produceLoginMessageToKafka(data.loginEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1462")
    @DisplayName("Login rule. Connection search sub-process. No toxic connections for non VT or PU users. Event.id end_cs_no_toxic")
    void loginRuleTest2() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");

        produceLoginMessageToKafka(data.loginEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1463")
    @DisplayName("Login rule. Connection search sub-process. No connections for VT or PU users. Event.id end_connections_not_found2")
    void loginRuleTest3() throws Exception {
        RuleDataHelper data = dbDataMap.get("3");

        produceLoginMessageToKafka(data.loginEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1464")
    @DisplayName("Login rule. Connection search sub-process. No toxic connections for VT or PU users. Event.id end_cs_no_toxic")
    void loginRuleTest4() throws Exception {
        RuleDataHelper data = dbDataMap.get("4");
        produceLoginMessageToKafka(data.loginEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1465")
    @DisplayName("Login rule. Connection search sub-process. Exists toxic connections for VT or PU users, general score<0.7. Event.id Event_0qy8dcr")
    void loginRuleTest5() throws Exception {
        RuleDataHelper data = dbDataMap.get("5");
        produceLoginMessageToKafka(data.loginEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1466")
    @DisplayName("Login rule. Connection search sub-process. General score>0.7, user is mirror trader without strong connections. Event.id end_no_str1_hedge")
    void loginRuleTest6() throws Exception {
        RuleDataHelper data = dbDataMap.get("6");
        produceLoginMessageToKafka(data.loginEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1467")
    @DisplayName("Login rule. Connection search sub-process. General score> 0.7, user is mirror trader with strong connections. Event.id Event_1o2qu8z")
    void loginRuleTest7() throws Exception {
        RuleDataHelper data = dbDataMap.get("7");
        produceLoginMessageToKafka(data.loginEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1468")
    @DisplayName("Login rule. Strong connection with HEDGING fraud and no bonus restriction. Event.id end_cs_abuse")
    void loginRuleTest8() throws Exception {
        RuleDataHelper data = dbDataMap.get("8");
        produceLoginMessageToKafka(data.loginEvent);

        // Verify restriction
        Allure.step("Get client restrictions");
        Thread.sleep(30_000);
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        //Check restriction
        assertThat("Check ucid", clientGeneralRestrictions.get(0).getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Check regulator", clientGeneralRestrictions.get(0).getRegulator(), is(data.clientHelper.getRegulator()));
        assertThat("Check restrictionId", clientGeneralRestrictions.get(0).getRestrictionId(), is(8L));
        assertThat("Check comment", clientGeneralRestrictions.get(0).getComment(), is("Linked abuser"));
        assertThat("Check status", clientGeneralRestrictions.get(0).getStatus(), is("APPLIED"));
        //add check for FT_HEDGE
        GetStatusResponseBody abuserStatus = getAbuserStatus(data.clientHelper);
        assertThat(abuserStatus.getUcid(), is(data.clientHelper.getUcid()));
        assertThat(abuserStatus.getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getCreatedAt(), notNullValue());
        assertThat(abuserStatus.getUpdatedAt(), notNullValue());
        assertThat(abuserStatus.getComment(), is("Linked hedging abuser"));
        assertThat(abuserStatus.getFraudTypes().get(0).getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getFraudTypes().get(0).getCode(), is("HEDGING"));
        assertThat(abuserStatus.getFraudTypes().get(0).getName(), is("Hedging"));
        assertThat(abuserStatus.getFraudTypes().get(0).getComment(), is("Linked hedging abuser"));
        assertThat(abuserStatus.getFraudTypes().get(0).getDescription(), is(FraudType.HEDGING.getDescription()));
        assertThat(abuserStatus.getFraudTypes().get(0).getSubtypeCode(), nullValue());
        assertThat(abuserStatus.getFraudTypes().get(0).getSubtypeName(), nullValue());
    }

    @Test
    @AllureId("1469")
    @DisplayName("Login rule. Connection search sub-process. General score> 0.7, fraud type is uknown. Event.id end_unknown_fraud_type")
    void loginRuleTest9() throws Exception {
        RuleDataHelper data = dbDataMap.get("9");
        produceLoginMessageToKafka(data.loginEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1470")
    @DisplayName("Login rule. Connection search sub-process. General score> 0.7, fraud type is Market manipulation. Event.id end_cs_abuse")
    void loginRuleTest10() throws Exception {
        RuleDataHelper data = dbDataMap.get("10");
        produceLoginMessageToKafka(data.loginEvent);

        Thread.sleep(30_000);
        // Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));

        // Verify restrictions
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        //Check restriction
        assertThat("Check ucid", clientGeneralRestrictions.get(0).getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Check regulator", clientGeneralRestrictions.get(0).getRegulator(), is(data.clientHelper.getRegulator()));
        assertThat("Check restrictionId", clientGeneralRestrictions.get(0).getRestrictionId(), is(8L));
        assertThat("Check comment", clientGeneralRestrictions.get(0).getComment(), is("Linked abuser"));
        assertThat("Check status", clientGeneralRestrictions.get(0).getStatus(), is("APPLIED"));
        //add check for MARKET_MANIPULATION
        GetStatusResponseBody abuserStatus = getAbuserStatus(data.clientHelper);
        assertThat(abuserStatus.getUcid(), is(data.clientHelper.getUcid()));
        assertThat(abuserStatus.getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getCreatedAt(), notNullValue());
        assertThat(abuserStatus.getUpdatedAt(), notNullValue());
        assertThat(abuserStatus.getComment(), is("Linked market manipulator"));
        assertThat(abuserStatus.getFraudTypes().get(0).getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getFraudTypes().get(0).getCode(), is("MARKET_MANIPULATION"));
        assertThat(abuserStatus.getFraudTypes().get(0).getName(), is("Market manipulation"));
        assertThat(abuserStatus.getFraudTypes().get(0).getComment(), is("Linked market manipulator"));
        assertThat(abuserStatus.getFraudTypes().get(0).getDescription(), is(FraudType.MARKET_MANIPULATION.getDescription()));
        assertThat(abuserStatus.getFraudTypes().get(0).getSubtypeCode(), nullValue());
        assertThat(abuserStatus.getFraudTypes().get(0).getSubtypeName(), nullValue());
    }

    @Test
    @AllureId("1471")
    @DisplayName("Login rule. Connection search sub-process. General score> 0.7, fraud type is Chargeback. Event.id end_cs_abuse")
    void loginRuleTest11() throws Exception {
        RuleDataHelper data = dbDataMap.get("11");
        produceLoginMessageToKafka(data.loginEvent);

        Thread.sleep(30_000);
        // Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));

        // Verify restrictions
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        //Check restriction
        assertThat("Check ucid", clientGeneralRestrictions.get(0).getUcid(), is(data.clientHelper.getUcid()));
        assertThat("Check regulator", clientGeneralRestrictions.get(0).getRegulator(), is(data.clientHelper.getRegulator()));
        assertThat("Check restrictionId", clientGeneralRestrictions.get(0).getRestrictionId(), is(8L));
        assertThat("Check comment", clientGeneralRestrictions.get(0).getComment(), is("Linked abuser"));
        assertThat("Check status", clientGeneralRestrictions.get(0).getStatus(), is("APPLIED"));
        //add check for CHARGEBACK
        GetStatusResponseBody abuserStatus = getAbuserStatus(data.clientHelper);
        assertThat(abuserStatus.getUcid(), is(data.clientHelper.getUcid()));
        assertThat(abuserStatus.getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getCreatedAt(), notNullValue());
        assertThat(abuserStatus.getUpdatedAt(), notNullValue());
        assertThat(abuserStatus.getComment(), is("Linked chargeback abuser"));
        assertThat(abuserStatus.getFraudTypes().get(0).getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getFraudTypes().get(0).getCode(), is("CHARGEBACK"));
        assertThat(abuserStatus.getFraudTypes().get(0).getName(), is("Chargeback"));
        assertThat(abuserStatus.getFraudTypes().get(0).getComment(), is("Linked chargeback abuser"));
        assertThat(abuserStatus.getFraudTypes().get(0).getDescription(), is(FraudType.CHARGEBACK.getDescription()));
        assertThat(abuserStatus.getFraudTypes().get(0).getSubtypeCode(), nullValue());
        assertThat(abuserStatus.getFraudTypes().get(0).getSubtypeName(), nullValue());
    }

    @Test
    @AllureId("1472")
    @DisplayName("Login rule. Connection search sub-process. General score> 0.7, fraud type is CPA. Event.id end_no_mitigation")
    void loginRuleTest12() throws Exception {
        RuleDataHelper data = dbDataMap.get("12");
        produceLoginMessageToKafka(data.loginEvent);

        Thread.sleep(30_000);
        // Verify alerts
        assertThat("Verify amount of user alerts in kafka", getUserAlertsFromKafka(data.clientHelper).size(), is(0));
        assertThat("Verify amount of alerts in BO DB", getUserAlertsFromDb(data.clientHelper).size(), is(0));

        // Verify restrictions
        Allure.step("Get client restrictions");
        assertThat("Verify that there is only 1 restriction", getUserRestrictionsFromDb(data.clientHelper).size(), equalTo(0));
        //Check for CPA
        GetStatusResponseBody abuserStatus = getAbuserStatus(data.clientHelper);
        assertThat(abuserStatus.getUcid(), is(data.clientHelper.getUcid()));
        assertThat(abuserStatus.getStatus(), is("CLEANED"));
        assertThat(abuserStatus.getCreatedAt(), nullValue());
        assertThat(abuserStatus.getUpdatedAt(), nullValue());
        assertThat(abuserStatus.getComment(), nullValue());
        assertThat(abuserStatus.getFraudTypes().size(), is(0));
    }

    @Test
    @AllureId("1473")
    @DisplayName("Login rule. Strong connection with HEDGING fraud and has bonus restriction. Event.id end_hedge_ald_no_bonus")
    void loginRuleTest13() throws Exception {
        RuleDataHelper data = dbDataMap.get("13");

        //add  bonus restriction
        Integer restrictionId = postRestriction(data.clientHelper, "GENERAL", "14").id;
        //

        produceLoginMessageToKafka(data.loginEvent);

        // Verify restriction is bonus restriction with code 14
        Allure.step("Get client restrictions");
        Thread.sleep(30_000);
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only restriction", clientGeneralRestrictions.size(), equalTo(1));
        assertThat("Verify restriction id ", clientGeneralRestrictions.getFirst().getRestrictionId(), equalTo(9L));
        assertThat("Verify restriction id ", clientGeneralRestrictions.getFirst().getId(), equalTo(Long.valueOf(restrictionId)));
    }

    @Test
    @AllureId("1474")
    @DisplayName("Login rule. Connection search sub-process. Exit without alert if user recently cancelled WD restriction. ElementId: end_wr_cooldown")
    void loginRuleTest14() throws Exception {
        RuleDataHelper data = dbDataMap.get("14");

        //add bonus restriction
        Thread.sleep(10_000);
        Integer restrictionId = postRestriction(data.clientHelper, "GENERAL", "13").id;
        //cancel bonus restriction
        assertThat("Assert response code", cancelRestriction(restrictionId).code(), is(204));
        //

        produceLoginMessageToKafka(data.loginEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

}
