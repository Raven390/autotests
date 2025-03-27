package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.mitigation_service.MitigationServiceRequest.disableCRMEmulator;
import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;

import static helpers.data.rules.loss_voucher_rule.LossVoucherRuleDataFactory.deleteLossVoucherRuleData;
import static helpers.data.rules.loss_voucher_rule.LossVoucherRuleDataFactory.setupLossVoucherRuleData;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_LOSS_VOUCHER_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class LossVoucherRuleTests extends TestBaseRule {
    static Map<String, RuleDataHelper> dbDataMap = new HashMap<>();

    @BeforeAll
    static void setupData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        enableCRMEmulator();
        dbDataMap = setupLossVoucherRuleData();
    }

    @AfterAll
    static void deleteData() throws Exception {
        deleteLossVoucherRuleData(dbDataMap);
        disableCRMEmulator();
    }

    @Test
    @DisplayName("Abnormal profit rule exit 1. Has user used loss vouchers before? = false")
    @AllureId("979")
    void lossVoucherRuleExitEventEnd11Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("11");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(0));
    }

    @Test
    @DisplayName("Abnormal profit rule exit 1. -200 < Lifetime PnL < 200 USD = false")
    @AllureId("980")
    void lossVoucherRuleExitEventEnd12Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("12");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(0));
    }

    @Test
    @DisplayName("Abnormal profit rule exit 2. Sum LV amount = +/-20% |PnL| = false")
    @AllureId("981")
    void lossVoucherRuleExitEventEnd2Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("2");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

        List<String> consumedMessages = kafka.consumeMessages(KAFKA_TOPIC_ALERTS, data.clientHelper.getUcid());
        assertThat("Verify that there is only 1 alert", consumedMessages.size(), equalTo(0));
    }

    @Test
    @DisplayName("Abnormal profit rule exit 3. Alert + restriction")
    @AllureId("982")
    void lossVoucherRuleExitEventEnd3Test() throws Exception {
        RuleDataHelper data = dbDataMap.get("3");
        Allure.step("Produce close trade event to crm-events topic");
        kafka.produceMessage("QA", objectMapper.writeValueAsString(data.withdrawalEvent), KAFKA_TOPIC_MT_EVENTS);

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
        assertThat("Verify rule trigger is correct", alert.rule.trigger, equalTo("withdrawal"));
        assertThat("Verify rule name is correct", alert.rule.name, equalTo("Loss Voucher Abuse"));
        assertThat("Verify rule fraud type is correct", alert.rule.fraudType, equalTo(FraudType.LOSS_VOUCHER_ABUSE.getKey()));
        assertThat("Verify rule attributes step name", alert.rule.attributes.reason, equalTo("Confirmed loss voucher abuser"));
        assertThat("Verify rule attributes lossVoucherAmount", alert.rule.attributes.lossVoucherAmount, equalTo("100"));

        List<Alert> dbAlerts = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, data.clientHelper.getUcid()), Alert.class);

        // Verify alert in BO db
        assertThat("Verify that there is only 1 alert in BO DB", dbAlerts.size(), equalTo(1));

        // Verify restriction
//        Allure.step("Get client restrictions");
//        List<ClientsRestriction> clientsRestrictions = getObjectsFromDB(
//                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, String.format("ucid = '%s'", data.clientHelper.getUcid()), ClientsRestriction.class
//        );
//
//        assertThat("Verify that there is only 1 restriction", clientsRestrictions.size(), equalTo(1));
//
//        ClientsRestriction restriction = clientsRestrictions.getFirst();
//        ClientsRestriction expectedRestriction = new ClientsRestriction(data.clientHelper.getUcid(), data.crmTbUserObject.regulator, 14L, "NBP_set_restriction", "APPLIED");
//
//        assertThat("Verify that the restriction is as expected", restriction, equalTo(expectedRestriction));
    }
}
