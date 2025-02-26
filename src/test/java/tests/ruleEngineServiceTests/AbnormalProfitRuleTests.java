package tests.ruleEngineServiceTests;


import businessObjects.db.backofficeDb.alert.Alert;
import businessObjects.db.mitigationServiceDb.ClientsRestriction;
import businessObjects.kafka.alerts.RuleAlert;
import helpers.data.enums.FraudType;
import helpers.data.rules.RuleDataHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.mitigationService.MitigationServiceRequest.disableCRMEmulator;
import static businessObjects.api.mitigationService.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.RulesAssertHelper.asserAlertsAmountByClient;
import static helpers.data.rules.abnormalProfitRule.AbnormalProfitRuleDataFactory.deleteAbnormalProfitRuleData;
import static helpers.data.rules.abnormalProfitRule.AbnormalProfitRuleDataFactory.setupAbnormalProfitRuleData;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_ABNORMAL_PROFIT_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_SERVICE)
public class AbnormalProfitRuleTests extends TestBaseRule {

    public static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    public static void setupDbData() throws ReflectiveOperationException, SQLException, IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupAbnormalProfitRuleData();
    }

    @AfterAll
    public static void deleteDbData() throws Exception {
        deleteAbnormalProfitRuleData(dbDataMap);
        disableCRMEmulator();
    }

    @Test
    @DisplayName("Abnormal profit rule exit 1. Post alert")
    @AllureId("968")
    public void mirrorTradeRuleExitEventEnd1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        Allure.step("Get alerts");
        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 130);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(1));
        RuleAlert alert = objectMapper.readValue(consumedMessages.getFirst(), RuleAlert.class);

        // Verify alert
        assertThat("Verify alert id not null", alert.alertId, notNullValue());
        assertThat("Verify timestamp not null", alert.timestamp, notNullValue());
        assertThat("Verify ucid is correct", alert.ucid, equalTo(data.clientHelper.getUcid()));
        assertThat("Verify rule not null", alert.rule, notNullValue());
        assertThat("Verify rule ver not null", alert.rule.ver, notNullValue());
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("Close Trade"));
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Abnormal profit"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudType.POTENTIAL_ABUSE.getKey()));
        assertThat("Verify rule attributes step name", alert.rule.attributes.stepName, equalTo("Abnormal profit detected"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
        );

        assertThat("Verify amount of restrictions", clientsRestrictions.size(), equalTo(1));
        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 12L, "Loss voucher", "APPLIED");
        assertThat("Verify that the restriction is as expected", clientsRestrictions, containsInAnyOrder(expectedRestriction));
    }

    @Test
    @DisplayName("Abnormal profit rule exit 2. No alert")
    @AllureId("969")
    public void mirrorTradeRuleExitEventEnd2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        asserAlertsAmountByClient(data.clientHelper, 0, 130);
    }
}
