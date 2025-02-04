package tests.ruleEngineServiceTests;

import businessObjects.db.mitigationServiceDb.ClientsRestriction;
import helpers.data.rules.RuleDataHelper;
import helpers.database.DbName;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.mitigationService.MitigationServiceRequest.disableCRMEmulator;
import static businessObjects.api.mitigationService.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.cpaAbuseRule.CpaAbuseRuleDataFactory.deleteCpaAbuseRuleData;
import static helpers.data.rules.cpaAbuseRule.CpaAbuseRuleDataFactory.setupCpaAbuseRuleData;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_CPA_ABUSE_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_SERVICE)
@Disabled
public class CpaAbuseRuleTests extends TestBaseRule {

    public static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    public static void setupDbData() throws ReflectiveOperationException, SQLException, IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupCpaAbuseRuleData();
    }

    @AfterAll
    public static void deleteDbData() throws Exception {
        deleteCpaAbuseRuleData(dbDataMap);
        disableCRMEmulator();
    }

    @Test
    @DisplayName("CPA abuse rule exit Event_1. User don't have cpaId")
    @AllureId("916")
    public void mirrorTradeRuleExitEventEnd_1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");
        System.out.println("User cpaId: " + data.clientHelper.getCpaId());

        Allure.step("User do not have cpaId number");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class);

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }

    @Test
    @DisplayName("CPA abuse rule exit Event_2_1. User connected to known abuser")
    @AllureId("917")
    public void mirrorTradeRuleExitEventEnd_2_1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("2_1");

        Allure.step("User do not have cpaId number");

        Allure.step("Produce registration event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat(String.format("Check that there are no alerts for ucid %s", data.clientHelper.getUcid()), consumedMessages, empty());

        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class);

        assertThat(String.format("Check that there are no restrictions for ucid %s", data.clientHelper.getUcid()), clientsRestrictions, empty());
    }
}
