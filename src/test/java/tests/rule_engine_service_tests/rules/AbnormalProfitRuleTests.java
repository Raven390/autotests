package tests.rule_engine_service_tests.rules;


import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.mitigation_service_db.ClientsRestrictionGeneral;
import business_objects.kafka.alerts.RuleAlert;
import helpers.data.enums.FraudTypeOld;
import helpers.data.rules.RuleDataHelper;
import helpers.database.DbName;
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

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static helpers.data.rules.abnormal_profit_rule.AbnormalProfitRuleDataFactory.deleteAbnormalProfitRuleData;
import static helpers.data.rules.abnormal_profit_rule.AbnormalProfitRuleDataFactory.setupAbnormalProfitRuleData;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static utils.Constants.*;

@Disabled("Temporarily disabling this test class because rule is in development")
@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_ABNORMAL_PROFIT_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class AbnormalProfitRuleTests extends TestBaseRule {

    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupAbnormalProfitRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteAbnormalProfitRuleData(dbDataMap);
    }

    @Test
    @DisplayName("Abnormal profit rule exit 1. Post alert")
    @AllureId("968")
    void mirrorTradeRuleExitEventEnd1Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("1");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudTypeOld.POTENTIAL_ABUSE.getKey()));
        assertThat("Verify rule attributes step name", alert.rule.attributes.stepName, equalTo("Abnormal profit detected"));

        List<Alert> dbAlerts = getObjectsFromDB(
                DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class
        );

        // Verify alert in BO db

        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
        Allure.step("Get client restrictions");
        List<ClientsRestrictionGeneral> clientsRestrictionGenerals = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_RESTRICTION_GENERAL, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestrictionGeneral.class
        );

        assertThat("Verify amount of restrictions", clientsRestrictionGenerals.size(), equalTo(1));
        ClientsRestrictionGeneral expectedRestriction = new ClientsRestrictionGeneral(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 12L, "Loss voucher", "APPLIED");
        assertThat("Verify that the restriction is as expected", clientsRestrictionGenerals, containsInAnyOrder(expectedRestriction));
    }

    @Test
    @DisplayName("Abnormal profit rule exit 2. No alert")
    @AllureId("969")
    void mirrorTradeRuleExitEventEnd2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(data.closeTradeEvent), KAFKA_TOPIC_MT_EVENTS);

        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid(), 130);
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(0));
    }
}
