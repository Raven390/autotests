package tests.rule_engine_service_tests.rules.trading;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.DataHelper;
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
import static helpers.asserts.RestrictionsAssertsHelper.checkManualWithdrawalRestrictionApplied;
import static helpers.data.rules.trading.NoSlippageRuleDataFactory.setupNoSlippageRuleData;
import static helpers.database.DbHelper.startSshTunnel;
import static helpers.database.DbHelper.stopSshTunnel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.writeLog;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_NO_SLIPPAGE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class NoSlippageRuleTests extends TestBaseRule {

    private static Map<String, DataHelper> dbDataMap = new HashMap<>();

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
        DataHelper.deleteData(dbDataMap);
    }

    @Test
    @AllureId("1419")
    @DisplayName("No slippage rule. If User is test user -> Exit without alert. ElementId: Event_end_1")
    void noSlippageRuleTest1() throws Exception {
        DataHelper data = dbDataMap.get("1");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_1", data.closeTradeMtEvent.id, "no_slippage");
    }

    @Test
    @AllureId("1420")
    @DisplayName("No slippage rule. If account currency is USC -> Exit without alert. ElementId: Event_197txjh")
    void noSlippageRuleTest2() throws Exception {
        DataHelper data = dbDataMap.get("2");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_197txjh", data.closeTradeMtEvent.id, "no_slippage");
    }

    @Test
    @AllureId("1421")
    @DisplayName("No slippage rule. Exit without alert if at least 1 resolved alert for user. ElementId: Event_end_12")
    void noSlippageRuleTest3() throws Exception {
        DataHelper data = dbDataMap.get("3");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_end_12", data.closeTradeMtEvent.id, "no_slippage");
    }

    @Test
    @AllureId("1422")
    @DisplayName("No slippage rule. Exit with alert and restriction if user has 0 resolved alerts. ElementId: Event_0oa6zyc")
    void noSlippageRuleTest4() throws Exception {
        DataHelper data = dbDataMap.get("4");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_0oa6zyc", data.closeTradeMtEvent.id, "no_slippage");

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "No Slippage");
        writeLog("client ucid: " + data.clientHelper.getUcid());
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().type, is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(data.closeTradeMtEvent.eventDate));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        // Verify restriction
        checkManualWithdrawalRestrictionApplied(data.clientHelper, "No slippage pattern");

    }

    @Test
    @AllureId("1423")
    @DisplayName("No slippage rule. Exit without alert if traded symbol not in the list. ElementId: Event_1u9lc7r")
    void noSlippageRuleTest5() throws Exception {
        DataHelper data = dbDataMap.get("5");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_1u9lc7r", data.closeTradeMtEvent.id, "no_slippage");
    }

    @Test
    @AllureId("1424")
    @DisplayName("No slippage rule. Exit without alert if deals/fast deals ratio < 0.7. ElementId: Event_034y6nl")
    void noSlippageRuleTest6() throws Exception {
        DataHelper data = dbDataMap.get("6");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_034y6nl", data.closeTradeMtEvent.id, "no_slippage");
    }

    @Test
    @AllureId("1425")
    @DisplayName("No slippage rule. Exit without alert if stopout ratio < 0.75. ElementId: Event_12inxex")
    void noSlippageRuleTest7() throws Exception {
        DataHelper data = dbDataMap.get("7");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_12inxex", data.closeTradeMtEvent.id, "no_slippage");
    }

    @Test
    @AllureId("1426")
    @DisplayName("No slippage rule. Exit without alert if notional value < 3mln. ElementId: Event_0n07x4l")
    void noSlippageRuleTest8() throws Exception {
        DataHelper data = dbDataMap.get("8");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_0n07x4l", data.closeTradeMtEvent.id, "no_slippage");
    }

    @Test
    @AllureId("1427")
    @DisplayName("No slippage rule. Exit without alert if count trades < 30. ElementId: Event_13p6x81")
    void noSlippageRuleTest9() throws Exception {
        DataHelper data = dbDataMap.get("9");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_13p6x81", data.closeTradeMtEvent.id, "no_slippage");
    }

    @Test
    @AllureId("1428")
    @DisplayName("No slippage rule. Exit without alert if profit(acc) + rebates(acc) < -10 000$?. ElementId: Event_0jy5i8k")
    void noSlippageRuleTest10() throws Exception {
        DataHelper data = dbDataMap.get("10");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_0jy5i8k", data.closeTradeMtEvent.id, "no_slippage");
    }

    @Test
    @AllureId("1429")
    @DisplayName("No slippage rule. Exit without alert if resolved alerts amount > 0. ElementId: Event_1rm136r")
    void noSlippageRuleTest11() throws Exception {
        DataHelper data = dbDataMap.get("11");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_1rm136r", data.closeTradeMtEvent.id, "no_slippage");
    }

    @Test
    @AllureId("1430")
    @DisplayName("No slippage rule. Exit with alert and restriction if resolved alerts amount = 0. ElementId: Event_1k86ppo")
    void noSlippageRuleTest12() throws Exception {
        DataHelper data = dbDataMap.get("12");

        produceCloseTradeMessageToKafka(data.closeTradeMtEvent);

        checkElementId("Event_1k86ppo", data.closeTradeMtEvent.id, "no_slippage");

        //Verify alerts
        List<RuleAlert> alerts = getUserAlertsFromKafka(data.clientHelper, "No Slippage");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));

        List<Alert> dbAlerts = getUserAlertsFromDb(data.clientHelper);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        // Verify restriction
        List<ClientGeneralRestriction> clientGeneralRestrictions = getUserRestrictionsFromDb(data.clientHelper);
        checkManualWithdrawalRestrictionApplied(data.clientHelper, "No slippage pattern");

    }
}
