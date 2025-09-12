package tests.rule_engine_service_tests.rules;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.payment_gate.payment_details.PaymentDetailsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import helpers.data.ClientHelper;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import tests.TestBaseRule;

import java.io.IOException;
import java.util.List;

import static business_objects.api.mitigation_service.MitigationServiceRequest.enableCRMEmulator;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static helpers.database.PaymentGateHelper.getPaymentDetailsByClientId;
import static helpers.database.PaymentGateHelper.getPaymentEventByUcid;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

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
    private static final ClientHelper client5 = getRandomVantageClientAllFields();

    @BeforeAll
    static void setupData() throws IOException {
        // Enable emulator to set restrictions to status APPLIED
        startSshTunnel();
        enableCRMEmulator();
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(generateUserByClient(client1), generateUserByClient(client2), generateUserByClient(client3), generateUserByClient(client4), generateUserByClient(client5)));
    }

    @AfterAll
    static void deleteData() throws Exception {
        cleanCrmUserTableByClient(client1.getUcid(), client2.getUcid());
        closeAlert(client2.getUcid());
        closeAlert(client3.getUcid());
        closeAlert(client4.getUcid());
        closeAlert(client5.getUcid());
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
    @AllureId("963")
    @DisplayName("Withdrawal notification rule. Exit with alert if withdrawal has not empty check name, withdrawalApplicationTime = 2025-06-03T16:30:07. ElementId:Event_1waht3m")
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

        List<RuleAlert> alerts = getUserAlertsFromKafka(client2);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert name", alerts.getFirst().rule.name, is("Withdrawal Review"));
        assertThat("Verify alert ucid", alerts.getFirst().ucid, is(client2.getUcid()));

        List<Alert> dbAlerts = getUserAlertsFromDb(client2);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));

        //Check that payment is written to payment gate tables
        Allure.step("Validate Data in paymentEvent table");
        PaymentEventsObject paymentEventsObject = getPaymentEventByUcid(client2.getUcid());
        assertThat("Assert paymentId", paymentEventsObject.getCrmId(), is(String.valueOf(withdrawalEvent.getWithdrawalId())));
        Allure.step("Validate Data in paymentDetails table");
        PaymentDetailsObject paymentDetailsObject = getPaymentDetailsByClientId(client2.getUserId());
        assertThat("Assert brand", paymentDetailsObject.getClientId(), is(client2.getUserId().toString()));
    }

    @Test
    @AllureId("964")
    @DisplayName("Withdrawal notification rule. Exit with alert if withdrawal has not empty check name, withdrawalApplicationTime = 2025-07-29 10:54:48 ElementId: Event_1waht3m")
    void withdrawalNotificationRule3Test() throws Exception {
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client3.getBrand().toLowerCase(),    // brand
                "Checkname",                                  // checkName
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
                "2025-07-29 10:54:48",           // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );
        produceWithdrawalMessageToKafka(withdrawalEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(client3);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert name", alerts.getFirst().rule.name, is("Withdrawal Review"));
        assertThat("Verify alert ucid", alerts.getFirst().ucid, is(client3.getUcid()));

        List<Alert> dbAlerts = getUserAlertsFromDb(client3);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
    }

    @Test
    @AllureId("1378")
    @DisplayName("Withdrawal notification rule. Exit with alert if withdrawal has not empty check name, withdrawalApplicationTime = 2025-07-29T10:54:48Z. ElementId: Event_1waht3m")
    void withdrawalNotificationRule4Test() throws Exception {
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client4.getBrand().toLowerCase(),    // brand
                "Checkname",                                  // checkName
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
                "2025-07-29T10:54:48Z",              // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );
        produceWithdrawalMessageToKafka(withdrawalEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(client4);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert name", alerts.getFirst().rule.name, is("Withdrawal Review"));
        assertThat("Verify alert ucid", alerts.getFirst().ucid, is(client4.getUcid()));

        List<Alert> dbAlerts = getUserAlertsFromDb(client4);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
    }

    @Test
    @AllureId("1379")
    @DisplayName("Withdrawal notification rule. Exit with alert if withdrawal has not empty check name, withdrawalApplicationTime = 2024-07-29 12:34:56Z. ElementId: Event_1waht3m")
    void withdrawalNotificationRule5Test() throws Exception {
        CrmWithdrawalEvent withdrawalEvent = new CrmWithdrawalEvent(
                "MT4",                   // accountType
                "486951",                            // binNumber
                client5.getBrand().toLowerCase(),    // brand
                "Checkname",                                  // checkName
                client5.getUserId(),                 // clientId
                "2025-06-03T16:30:07+03:00",         // eventDate (you can format if you need +03:00)
                "4",                                 // expMonth
                "2030",                              // expYear
                "1",                                 // fullName
                getRandomUuidString(),               // id
                "VTSG" + client5.getTradingAccount() + "20250603144826", // merchantOrderId (example)
                client5.getTradingAccount(),         // mt4Account
                PAYMENT_PROVIDER_FASAPAY,            // paymentChannelCode
                "-",                                 // paymentChannelName
                "CREDIT_CARD",                       // paymentMethodCode
                "MT4",                               // platform
                client5.getRegulator(),              // regulator
                "1.0",                               // schemaVersion
                CRM_WITHDRAWAL_EVENT,                // type
                1,                                   // withdrawalAmount
                "2024-07-29T12:34:56Z",              // withdrawalApplicationTime
                "EUR",                               // withdrawalCurrency
                getRandomIntPositive()               // withdrawalId
        );
        produceWithdrawalMessageToKafka(withdrawalEvent);

        List<RuleAlert> alerts = getUserAlertsFromKafka(client5);
        assertThat("Verify amount of user alerts in kafka", alerts.size(), is(1));
        assertThat("Verify alert", alerts.getFirst().timestamp, matchesPattern("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?(?:Z|[+-]\\d{2}:\\d{2})$"));
        assertThat("Verify alert", alerts.getFirst().alertId, is(withdrawalEvent.id));
        assertThat("Verify alert", alerts.getFirst().type, is("TRADING"));
        assertThat("Verify alert", alerts.getFirst().ucid, is(client5.getUcid()));
        assertThat("Verify alert", alerts.getFirst().triggerCreatedTime, is(withdrawalEvent.eventDate));

        assertThat("Verify alert", alerts.getFirst().rule.name, is("Withdrawal Review"));
        assertThat("Verify alert", alerts.getFirst().rule.fraudType, is("POTENTIAL_ABUSE"));
        assertThat("Verify alert", alerts.getFirst().rule.trigger, is("Withdrawal"));
        assertThat("Verify alert", alerts.getFirst().rule.ver, notNullValue());

        assertThat("Verify alert", alerts.getFirst().rule.attributes.platform, is(withdrawalEvent.platform));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.currency, is(withdrawalEvent.withdrawalCurrency));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.createTime, is(withdrawalEvent.withdrawalApplicationTime));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.paymentType, is(withdrawalEvent.paymentMethodCode));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.check, is(withdrawalEvent.checkName));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.account, is(String.valueOf(withdrawalEvent.mt4Account)));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.paymentChannel, is(withdrawalEvent.paymentChannelName));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.amount, is("1"));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.date, is(withdrawalEvent.eventDate));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.withdrawalId, is(String.valueOf(withdrawalEvent.withdrawalId)));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.orderId, is(withdrawalEvent.merchantOrderId));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.regulator, is(withdrawalEvent.regulator));
        assertThat("Verify alert", alerts.getFirst().rule.attributes.brand, is(withdrawalEvent.brand));

        List<Alert> dbAlerts = getUserAlertsFromDb(client5);
        assertThat("Verify amount of alerts in BO DB", dbAlerts.size(), is(1));
    }
}
