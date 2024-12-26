package tests.ruleEngineServiceTests;

import businessObjects.kafka.alerts.RuleAlert;
import helpers.data.rules.cpaAbuseRule.CpaAbuseRuleData;
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

    public static Map<String, CpaAbuseRuleData> dbDataMap = new HashMap<>();

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
    @DisplayName("CPA abuse rule exit Event_End_1_1")
    @Description("User connected to known abuser")
    @AllureId("")
    public void mirrorTradeRuleExitEventEnd__Test() throws Exception {
        Allure.step("Create data");
        CpaAbuseRuleData cpaAbuseRuleData = dbDataMap.get("1_1");

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(cpaAbuseRuleData.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, cpaAbuseRuleData.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        Allure.step("Check alert");
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(cpaAbuseRuleData.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Cpa abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(cpaAbuseRuleData.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(cpaAbuseRuleData.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.clones, notNullValue());

    }

    @Test
    @DisplayName("CPA abuse rule exit Event_End_1_2 (User connected to know abuser)")
    @Description("User NOT connected to known abuser + at least 70% connected clients has any CPA value")
    @AllureId("")
    public void mirrorTradeRuleExitEventEnd___Test() throws Exception {
        Allure.step("Create data");
        CpaAbuseRuleData data = dbDataMap.get("1_2");

        Allure.step("Produce withdrawal event to crm-events topic");
        kafka.produceMessage("13", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_CRM_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        Allure.step("Check alert");
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Cpa abuse"));
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Withdrawal"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo("CPA"));
        assertThat("Verify rule attributes not null", alert.rule.attributes, notNullValue());
        assertThat("Verify rule attributes tradingAccount is correct", alert.rule.attributes.tradingAccount, equalTo(data.clientHelper.getTradingAccount()));
        assertThat("Verify rule attributes serverId is correct", alert.rule.attributes.serverId, equalTo(data.clientHelper.getServerId()));
        assertThat("Verify rule attributes clones not null", alert.rule.attributes.clones, notNullValue());
    }
}
