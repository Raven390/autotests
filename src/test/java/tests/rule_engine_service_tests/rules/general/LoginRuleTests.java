package tests.rule_engine_service_tests.rules.general;

import static business_objects.api.mitigation_service.MitigationServiceRequest.*;
import static helpers.api.RestrictionHelper.addCancelledRestriction;
import static helpers.asserts.RestrictionsAssertsHelper.*;
import static helpers.data.DataDeleteHelper.deleteData;
import static helpers.data.DataSetupHelper.setupData;
import static helpers.data.rules.general.LoginRuleDataFactory.setupLoginRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_RULE_ENGINE_RULES_TESTS;

import business_objects.api.abuse_registry.GetStatusResponseBody;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import helpers.data.DataHelper;
import helpers.data.enums.FraudType;
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
@Story(STORY_RULE_ENGINE_LOGIN_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class LoginRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setup() throws IOException, InterruptedException {
        startSshTunnel();
        enableCRMEmulator();
        dbDataMap = setupLoginRuleData();
    }

    // @AfterAll
    static void teardown() throws Exception {
        deleteData(dbDataMap);
        stopSshTunnel();
    }

    @Test
    @AllureId("1462")
    @DisplayName(
            "Login rule. Connection search sub-process. Exit without restriction if no toxic connections for non VT or PU users, chargeback score <0.9. ElementId: Event.id end_cs_no_toxic")
    void loginRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");
        setupData(data);

        produceLoginMessageToKafka(data.loginEvent);

        checkElementId("end_cs_no_toxic", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1462")
    @DisplayName(
            "Login rule. Connection search sub-process. Exit without restriction if no toxic connections for non VT or PU users, chargeback score >0.9, no false positives. ElementId: Event.id end_cs_no_toxic")
    void loginRuleTest2() throws Exception {}

    @Disabled
    @Test
    @AllureId("1463")
    @DisplayName(
            "Login rule. Connection search sub-process. Exit without restriction if no toxic connections for non VT or PU users, chargeback score >0.9, false positives alerts exists. ElementId: Event.id end_cs_no_toxic")
    void loginRuleTest3() throws Exception {}

    @Disabled
    @Test
    @AllureId("1656")
    @DisplayName("Login rule. Connection search sub-process. Exit if general score < 0.7. ElementId: end_gs_low")
    void loginRuleTest16() throws Exception {
        DataHelper data = dbDataMap.get("16");
        setupData(data);

        produceLoginMessageToKafka(data.loginEvent);

        checkElementId("end_gs_low", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1655")
    @DisplayName(
            "Login rule. Connection search sub-process. Exit if has WR that 24OP removed and current <= previous average generalScore. ElementId: Event_1fdy7w1")
    void loginRuleTest17() throws Exception {
        DataHelper data = dbDataMap.get("17");
        setupData(data);

        addCancelledRestriction(data.clientHelper, "GENERAL", "13");

        produceLoginMessageToKafka(data.loginEvent);

        checkElementId("Event_1fdy7w1", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1466")
    @DisplayName(
            "Login rule. Connection search sub-process. Exit without restriction if user has model score > 0.7 and is mirror trader without strong connections. ElementId: end_no_str1_hedge")
    void loginRuleTest6() throws Exception {
        DataHelper data = dbDataMap.get("6");
        produceLoginMessageToKafka(data.loginEvent);
        setupData(data);

        checkElementId("end_no_str1_hedge", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());
    }

    @Disabled
    @Test
    @AllureId("1467")
    @DisplayName(
            "Login rule. Connection search sub-process. Exit without restriction if user has model score> 0.7 and is mirror trader with strong connections. ElementId: Event_1o2qu8z")
    void loginRuleTest7() throws Exception {
        DataHelper data = dbDataMap.get("7");
        produceLoginMessageToKafka(data.loginEvent);
        setupData(data);

        checkElementId("Event_1o2qu8z", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());
    }

    @Test
    @AllureId("1468")
    @DisplayName(
            "Login rule. Exit with restriction if user has strong connection with HEDGING fraud user and no bonus restriction. ElementId: end_cs_abuse")
    void loginRuleTest8() throws Exception {
        DataHelper data = dbDataMap.get("8");
        setupData(data);

        produceLoginMessageToKafka(data.loginEvent);

        checkElementId("end_cs_abuse", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());

        // Verify restriction
        checkManualWithdrawalRestrictionApplied(data, "Login rule. Linked Hedging Abuser");
        checkNoBonusRestrictionApplied(data, "Login rule. Linked Hedging Abuser");

        // add check for FT_HEDGE
        GetStatusResponseBody abuserStatus = getAbuserStatus(data.clientHelper);
        assertThat(abuserStatus.getUcid(), is(data.clientHelper.getUcid()));
        assertThat(abuserStatus.getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getCreatedAt(), notNullValue());
        assertThat(abuserStatus.getUpdatedAt(), notNullValue());
        assertThat(abuserStatus.getComment(), is("Linked hedging abuser"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getCode(), is("HEDGING"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getName(), is("Hedging"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getComment(), is("Linked hedging abuser"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getDescription(), is(FraudType.HEDGING.getDescription()));
        assertThat(abuserStatus.getFraudTypes().getFirst().getSubtypeCode(), nullValue());
        assertThat(abuserStatus.getFraudTypes().getFirst().getSubtypeName(), nullValue());
    }

    @Test
    @AllureId("1469")
    @DisplayName(
            "Login rule. Connection search sub-process. Exit without restriction if user has model score > 0.7 and fraud type is unknown. ElementId: end_unknown_fraud_type")
    void loginRuleTest9() throws Exception {
        DataHelper data = dbDataMap.get("9");
        setupData(data);

        produceLoginMessageToKafka(data.loginEvent);

        checkElementId("end_cs_abuse", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());

        // Verify restriction
        checkManualWithdrawalRestrictionApplied(data, "Login rule. Linked Hedging Abuser");
        checkBonusRestrictionNotExists(data, "Login rule. Linked Hedging Abuser");

        // add check for FT_HEDGE
        GetStatusResponseBody abuserStatus = getAbuserStatus(data.clientHelper);
        assertThat(abuserStatus.getUcid(), is(data.clientHelper.getUcid()));
        assertThat(abuserStatus.getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getCreatedAt(), notNullValue());
        assertThat(abuserStatus.getUpdatedAt(), notNullValue());
        assertThat(abuserStatus.getComment(), is("Linked hedging abuser"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getCode(), is("HEDGING"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getName(), is("Hedging"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getComment(), is("Linked hedging abuser"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getDescription(), is(FraudType.HEDGING.getDescription()));
        assertThat(abuserStatus.getFraudTypes().getFirst().getSubtypeCode(), nullValue());
        assertThat(abuserStatus.getFraudTypes().getFirst().getSubtypeName(), nullValue());
    }

    @Disabled
    @Test
    @AllureId("1470")
    @DisplayName(
            "Login rule. Connection search sub-process. Exit with restriction if user has model score > 0.7 and fraud type is Market manipulation. ElementId: end_cs_abuse")
    void loginRuleTest10() throws Exception {
        DataHelper data = dbDataMap.get("10");
        produceLoginMessageToKafka(data.loginEvent);
        setupData(data);

        checkElementId("end_cs_abuse", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());

        // Verify restrictions
        checkManualWithdrawalRestrictionApplied(data, "Linked MM Abuser");

        // add check for MARKET_MANIPULATION
        GetStatusResponseBody abuserStatus = getAbuserStatus(data.clientHelper);
        assertThat(abuserStatus.getUcid(), is(data.clientHelper.getUcid()));
        assertThat(abuserStatus.getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getCreatedAt(), notNullValue());
        assertThat(abuserStatus.getUpdatedAt(), notNullValue());
        assertThat(abuserStatus.getComment(), is("Linked market manipulator"));
        assertThat(abuserStatus.getPendingProcessing(), is("false"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getCode(), is("MARKET_MANIPULATION"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getName(), is("Market manipulation"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getComment(), is("Linked market manipulator"));
        assertThat(
                abuserStatus.getFraudTypes().getFirst().getDescription(),
                is(FraudType.MARKET_MANIPULATION.getDescription()));
        assertThat(abuserStatus.getFraudTypes().getFirst().getSubtypeCode(), nullValue());
        assertThat(abuserStatus.getFraudTypes().getFirst().getSubtypeName(), nullValue());
    }

    @Disabled
    @Test
    @AllureId("1532")
    @DisplayName(
            "Login rule. Connection search sub-process. General score> 0.7, fraud type is Bonus abuser and toxic account linked. ElementId: end_cs_abuse")
    void loginRuleTest15() throws Exception {
        DataHelper data = dbDataMap.get("15");
        produceLoginMessageToKafka(data.loginEvent);
        setupData(data);

        checkElementId("end_cs_abuse", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());

        // Verify restrictions
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is 2 restrictions", clientGeneralRestrictions.size(), equalTo(2));

        // Check restriction
        assertThat(
                "Verify restriction",
                clientGeneralRestrictions.getFirst().getUcid(),
                equalTo(data.clientHelper.getUcid()));
        assertThat(
                "Verify restriction",
                clientGeneralRestrictions.getFirst().getRegulator(),
                equalTo(data.clientHelper.getRegulator()));
        assertThat("Verify restriction", clientGeneralRestrictions.getFirst().getRestrictionId(), equalTo(9L));
        assertThat(
                "Verify restriction", clientGeneralRestrictions.getLast().getComment(), equalTo("Linked Bonus Abuser"));
        assertThat("Verify restriction", clientGeneralRestrictions.getFirst().getStatus(), equalTo("APPLIED"));

        // Check restriction
        checkManualWithdrawalRestrictionApplied(data, "Linked Bonus Abuser");

        // add check for bonus
        GetStatusResponseBody abuserStatus = getAbuserStatus(data.clientHelper);
        assertThat(abuserStatus.getUcid(), is(data.clientHelper.getUcid()));
        assertThat(abuserStatus.getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getCreatedAt(), notNullValue());
        assertThat(abuserStatus.getUpdatedAt(), notNullValue());
        assertThat(abuserStatus.getComment(), is("Linked bonus abuser"));
        assertThat(abuserStatus.getPendingProcessing(), is("false"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getCode(), is("BONUS_ABUSE"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getName(), is("Bonus abuse"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getComment(), is("Linked bonus abuser"));
        assertThat(
                abuserStatus.getFraudTypes().getFirst().getDescription(), is(FraudType.BONUS_ABUSE.getDescription()));
        assertThat(abuserStatus.getFraudTypes().getFirst().getSubtypeCode(), nullValue());
        assertThat(abuserStatus.getFraudTypes().getFirst().getSubtypeName(), nullValue());
    }

    @Disabled
    @Test
    @AllureId("1471")
    @DisplayName(
            "Login rule. Connection search sub-process. Exit with restriction if user has model score > 0.7 and fraud type is Chargeback. ElementId: end_cs_abuse")
    void loginRuleTest11() throws Exception {
        DataHelper data = dbDataMap.get("11");
        produceLoginMessageToKafka(data.loginEvent);
        setupData(data);

        checkElementId("end_cs_abuse", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());

        // Verify restrictions
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only 1 restriction", clientGeneralRestrictions.size(), equalTo(1));
        // Check restriction
        checkManualWithdrawalRestrictionApplied(data, "Linked Chargeback Abuser");
        // add check for CHARGEBACK
        GetStatusResponseBody abuserStatus = getAbuserStatus(data.clientHelper);
        assertThat(abuserStatus.getUcid(), is(data.clientHelper.getUcid()));
        assertThat(abuserStatus.getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getCreatedAt(), notNullValue());
        assertThat(abuserStatus.getUpdatedAt(), notNullValue());
        assertThat(abuserStatus.getComment(), is("Linked chargeback abuser"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getStatus(), is("POTENTIAL"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getCode(), is("CHARGEBACK"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getName(), is("Chargeback"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getComment(), is("Linked chargeback abuser"));
        assertThat(abuserStatus.getFraudTypes().getFirst().getDescription(), is(FraudType.CHARGEBACK.getDescription()));
        assertThat(abuserStatus.getFraudTypes().getFirst().getSubtypeCode(), nullValue());
        assertThat(abuserStatus.getFraudTypes().getFirst().getSubtypeName(), nullValue());
    }

    @Disabled
    @Test
    @AllureId("1472")
    @DisplayName(
            "Login rule. Connection search sub-process. Exit without restriction if user has model score> 0.7 and fraud type is CPA. ElementId: end_no_mitigation")
    void loginRuleTest12() throws Exception {
        DataHelper data = dbDataMap.get("12");
        setupData(data);

        produceLoginMessageToKafka(data.loginEvent);

        checkElementId("end_no_mitigation", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());

        // Verify alerts
        assertThat(
                "Verify amount of user alerts in kafka",
                getUserAlertsFromKafka(data.clientHelper).size(),
                is(0));
        assertThat(
                "Verify amount of alerts in BO DB",
                getUserAlertsFromDb(data.clientHelper).size(),
                is(0));

        // Verify restrictions
        Allure.step("Get client restrictions");
        assertThat(
                "Verify that there is only 1 restriction",
                getUserRestrictionsFromDb(data.clientHelper).size(),
                equalTo(0));
        // Check for CPA
        GetStatusResponseBody abuserStatus = getAbuserStatus(data.clientHelper);
        assertThat(abuserStatus.getUcid(), is(data.clientHelper.getUcid()));
        assertThat(abuserStatus.getStatus(), is("CLEANED"));
        assertThat(abuserStatus.getCreatedAt(), nullValue());
        assertThat(abuserStatus.getUpdatedAt(), nullValue());
        assertThat(abuserStatus.getComment(), nullValue());
        assertThat(abuserStatus.getFraudTypes().size(), is(0));
    }

    @Disabled
    @Test
    @AllureId("1473")
    @DisplayName(
            "Login rule. Exit with restriction if user has strong connection with HEDGING fraud user and has bonus restriction. ElementId: end_hedge_ald_no_bonus")
    void loginRuleTest13() throws Exception {
        DataHelper data = dbDataMap.get("13");
        setupData(data);

        // add bonus restriction
        Integer restrictionId = postRestriction(data.clientHelper, "GENERAL", "14").id;

        produceLoginMessageToKafka(data.loginEvent);

        checkElementId("end_hedge_ald_no_bonus", data.loginEvent.getId(), Rule.LOGIN_RULE.getProcessId());

        // Verify restriction is a bonus restriction with code 14
        Allure.step("Get client restrictions");
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        assertThat("Verify that there is only restriction", clientGeneralRestrictions.size(), equalTo(1));
        assertThat(
                "Verify restriction id ", clientGeneralRestrictions.getFirst().getRestrictionId(), equalTo(9L));
        assertThat(
                "Verify restriction id ",
                clientGeneralRestrictions.getFirst().getId(),
                equalTo(Long.valueOf(restrictionId)));
    }
}
