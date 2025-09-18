package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.payment_gate.tmp_rule_decisions.TmpRuleDecisionsObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.kafka.restriction_events.WithdrawalApprovals;
import helpers.data.ClientHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.List;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.MirrorFlagDataInserter.deleteMirrorFlagData;
import static helpers.data.rules.MirrorFlagDataInserter.insertMirrorFlagData;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;
import static utils.Utils.getRandomUuidString;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_WITHDRAWAL_NOTIFICATION_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_RULES_TESTS)
class WithdrawalNotificationRuleTest extends TestBaseRule {

    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client4 = getRandomVantageClientAllFields();
    private static final CrmTbAccountObject account4 = generateAccountByClient(client4, false);

    @BeforeAll
    static void setupData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        startSshTunnel();
        enableCRMEmulator();
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(generateUserByClient(client1), generateUserByClient(client2), generateUserByClient(client3), generateUserByClient(client4)));
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account4);
        insertMirrorFlagData(client3);
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanCrmUserTableByClient(client1.getUcid(), client2.getUcid());
        try {
            deleteEntryFromDb(CRM_TB_ACCOUNT_TABLE_NAME, "ucid = '" + account4.ucid + "'");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        closeAlert(client2.getUcid());
        closeAlert(client3.getUcid());
        closeAlert(client4.getUcid());
        deleteMirrorFlagData(client3);
        stopSshTunnel();
    }

    @Test
    @DisplayName("Withdrawal notification rule. Exit without alert if check name is empty. ElementId: Event_end_2")
    @AllureId("962")
    void withdrawalNotificationRule1Test() throws Exception {
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client1.getBrand().toLowerCase(),    // brand
                "",                                  // checkName
                client1.getUserId(),                 // clientId
                "2025-06-03T16:30:07+03:00",         // eventDate (you can format if you need +03:00)
                "4",                                 // expMonth
                "2030",                              // expYear
                "1",                                 // fullName
                getRandomUuidString(),               // id
                "VTSG" + client1.getTradingAccount() + "20250603144826", // merchantOrderId (example)
                client1.getTradingAccount(),         // mt4Account
                PAYMENT_PROVIDER_FASAPAY,            // paymentChannelCode
                "-",                                 // paymentChannelName
                "CREDIT_CARD",                       // paymentMethodCode
                "WEB",                               // platform
                client1.getRegulator(),              // regulator
                "1.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1,                                   // withdrawalAmount
                "2025-06-03T16:30:07",               // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );
        produceWithdrawalMessageToKafka(withdrawalEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(client1);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<Alert> dbAlerts = getUserAlertsFromDb(client1);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(0));
    }

    @Test
    @AllureId("1551")
    @DisplayName("Withdrawal notification rule. Exit with alert if withdrawal has not empty check name and no 'Crypto'. ElementId:Event_1waht3m")
    void withdrawalNotificationRule2Test() throws Exception {
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client2.getBrand().toLowerCase(),    // brand
                "Checkname",                                  // checkName
                client2.getUserId(),                 // clientId
                "2025-06-03T16:30:07+03:00",         // eventDate (you can format if you need +03:00)
                "4",                                 // expMonth
                "2030",                              // expYear
                "1",                                 // fullName
                getRandomUuidString(),               // id
                "VTSG" + client2.getTradingAccount() + "20250603144826", // merchantOrderId (example)
                client2.getTradingAccount(),         // mt4Account
                PAYMENT_PROVIDER_FASAPAY,            // paymentChannelCode
                "-",                                 // paymentChannelName
                "CREDIT_CARD",                       // paymentMethodCode
                "WEB",                               // platform
                client2.getRegulator(),              // regulator
                "1.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1,                                   // withdrawalAmount
                "2025-06-03T16:30:07",           // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );
        produceWithdrawalMessageToKafka(withdrawalEvent);


        List<RuleAlert> alerts = getUserAlertsFromKafka(client2, "Withdrawal Review");
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert name", alerts.getFirst().rule.name, is("Withdrawal Review"));
        assertThat("Verify alert ucid", alerts.getFirst().ucid, is(client2.getUcid()));

        List<PaymentEventsObject> events = getUserPaymentEventsFromDb(client2);
        assertThat("Verify amount of payments events in DB", events.size(), is(1));
        assertEquals(withdrawalEvent.withdrawalId, Long.valueOf(events.getFirst().getCrmId()));


        List<Alert> dbAlerts = getUserAlertsFromDb(client2);
        assertThat("Verify amount of alerts in DB", dbAlerts.size(), is(1));

        List<TmpRuleDecisionsObject> decision = getTempRuleDecisionByWithdrawalIdFromDb((events.getFirst().getPaymentId()));
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().decision, is("ALERT"));
    }

    @Test
    @AllureId("")
    @DisplayName("Withdrawal notification rule. Exit with alert if withdrawal has not empty check name and 'Crypto_Risk' mirror flag = true. ElementId:Event_0dvxfab")
    void withdrawalNotificationRule3Test() throws Exception {
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client3.getBrand().toLowerCase(),    // brand
                "Crypto_Risk",                                  // checkName
                client3.getUserId(),                 // clientId
                "2025-06-03T16:30:07+03:00",         // eventDate (you can format if you need +03:00)
                "4",                                 // expMonth
                "2030",                              // expYear
                "1",                                 // fullName
                getRandomUuidString(),               // id
                "VTSG" + client3.getTradingAccount() + "20250603144826", // merchantOrderId (example)
                client3.getTradingAccount(),         // mt4Account
                PAYMENT_PROVIDER_FASAPAY,            // paymentChannelCode
                "-",                                 // paymentChannelName
                "CREDIT_CARD",                       // paymentMethodCode
                "WEB",                               // platform
                client3.getRegulator(),              // regulator
                "1.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1,                                   // withdrawalAmount
                "2025-06-03T16:30:07",           // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );

        produceWithdrawalMessageToKafka(withdrawalEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(client3, "Withdrawal Review");

        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert name", alerts.getFirst().rule.name, is("Withdrawal Review"));
        assertThat("Verify alert ucid", alerts.getFirst().ucid, is(client3.getUcid()));

        List<PaymentEventsObject> events = getUserPaymentEventsFromDb(client3);
        assertThat("Verify amount of payments events in DB", events.size(), is(1));
        assertEquals(withdrawalEvent.withdrawalId, Long.valueOf(events.getFirst().getCrmId()));


        List<Alert> dbAlerts = getUserAlertsFromDb(client3, "Withdrawal Review");
        assertThat("Verify amount of alerts in DB", dbAlerts.size(), is(1));


        List<TmpRuleDecisionsObject> decision = getTempRuleDecisionByWithdrawalIdFromDb((events.getFirst().getPaymentId()));
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().decision, is("ALERT"));

    }

    @Test
    @AllureId("")
    @DisplayName("Withdrawal notification rule. Exit with alert if withdrawal has not empty check name and 'Crypto_Risk' mirror flag = false. ElementId:Event_1gdl5i3")
    void withdrawalNotificationRule4Test() throws Exception {
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client4.getBrand().toLowerCase(),    // brand
                "Crypto_Risk",                                  // checkName
                client4.getUserId(),                 // clientId
                "2025-06-03T16:30:07+03:00",         // eventDate (you can format if you need +03:00)
                "4",                                 // expMonth
                "2030",                              // expYear
                "1",                                 // fullName
                getRandomUuidString(),               // id
                "VTSG" + client4.getTradingAccount() + "20250603144826", // merchantOrderId (example)
                client4.getTradingAccount(),         // mt4Account
                PAYMENT_PROVIDER_FASAPAY,            // paymentChannelCode
                "-",                                 // paymentChannelName
                "CREDIT_CARD",                       // paymentMethodCode
                "WEB",                               // platform
                client4.getRegulator(),              // regulator
                "1.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1,                                   // withdrawalAmount
                "2025-06-03T16:30:07",           // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );

        produceWithdrawalMessageToKafka(withdrawalEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(client4, "Withdrawal Review");

        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(0));

        List<PaymentEventsObject> events = getUserPaymentEventsFromDb(client4);
        assertThat("Verify amount of payments events in DB", events.size(), is(1));
        assertEquals(withdrawalEvent.withdrawalId, Long.valueOf(events.getFirst().getCrmId()));

        Thread.sleep(1000);

        List<Alert> dbAlerts = getUserAlertsFromDb(client4, "Withdrawal Review");
        assertThat("Verify amount of alerts in DB", dbAlerts.size(), is(0));


        List<TmpRuleDecisionsObject> decision = getTempRuleDecisionByWithdrawalIdFromDb((events.getFirst().getPaymentId()));
        assertThat("Verify amount of decisions in DB", decision.size(), is(1));
        assertThat("Verify decisions have right decision ", decision.getFirst().decision, is("AUTO_APPROVE"));

        List<WithdrawalApprovals> approval = getWithdrawalApprovalsFromKafka(String.valueOf(withdrawalEvent.getWithdrawalId()));

        assertThat("Verify amount of aproovals in Kafka topic", approval.size(), is(1));
        assertThat("Verify status of aprooval in Kafka topic", approval.getFirst().getStatus(), is("Approve"));
        assertThat("Verify status of checkName in Kafka topic", approval.getFirst().getCheckName(), is("Crypto_Risk"));
        assertThat("Verify checkName in Kafka topic", approval.getFirst().getCheckName(), is("Crypto_Risk"));
        assertThat("Verify orderNumber in Kafka topic", approval.getFirst().getOrderNumber(), is(withdrawalEvent.merchantOrderId));

    }

}
