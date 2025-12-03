package tests;

import static org.awaitility.Awaitility.await;
import business_objects.api.abuse_registry.GetStatusResponseBody;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.clickhouse.reporting_test.ZeebeRulesElements;
import business_objects.db.clickhouse.reporting_test.ZeebeRulesStarted;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.db.payment_gate.payment_decisions.PaymentDecisionsObject;
import business_objects.db.payment_gate.payment_events.PaymentEventsObject;
import business_objects.kafka.CustomEvent;
import business_objects.kafka.InternalHedgeEvent;
import business_objects.kafka.MirrorScoreEvent;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.alerts.RuleAlertV2;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import business_objects.kafka.crm_events.LoginEvent;
import business_objects.kafka.crm_events.RegistrationEvent;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEvent;
import business_objects.kafka.payment.acknowledgement.Acknowledgement;
import business_objects.kafka.restriction_events.WithdrawalApprovals;
import business_objects.kafka.restriction_events.WithdrawalApprovalsV2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Step;
import okhttp3.Response;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestResultWatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static helpers.api.AbuseRegistryHelper.getClientStatus;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;


@ExtendWith(TestResultWatcher.class)
public class TestBaseRule {
    public static KafkaHelper kafka = new KafkaHelper();
    public static ObjectMapper objectMapper = new ObjectMapper();

    @Step("Produce withdrawal event to crm-events topic")
    public static void produceWithdrawalMessageToCrmEventsTopic(CrmWithdrawalEvent event)
            throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_CRM_EVENTS);
    }

    @Step("Produce withdrawal event to crm-events topic")
    public static void produceWithdrawalMessageToCrmPaymentTopic(CrmWithdrawalEvent event)
            throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_CRM_PAYMENTS);
    }

    @Step("Produce close trade event to mt-events topic")
    public static void produceCloseTradeMessageToKafka(CloseTradeMtEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_MT_EVENTS);
    }

    @Step("Produce open trade event to mt-events topic")
    public static void produceTradeMessageToKafka(TradeEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_MT_EVENTS);
    }

    @Step("Produce Internal hedge event trade event to mt-events topic")
    public static void produceInternalHedgeMessageToKafka(InternalHedgeEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_ML_MIRROR_TRADE_EVENTS);
    }

    @Step("Produce registration event to crm-events topic")
    public static void produceRegistrationEventToKafka(RegistrationEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_CRM_EVENTS);
    }

    @Step("Produce login event to crm-events topic")
    public static void produceLoginMessageToKafka(LoginEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_CRM_EVENTS);
    }

    @Step("Produce message to custom-event topic")
    public static void produceCustomMessageToKafka(CustomEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_CUSTOM_EVENTS);
    }

    @Step("Produce mirrorScore message to ucid_mirror_score topic")
    public static void produceMirrorScoreMessageToKafka(MirrorScoreEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_UCID_MIRROR_SCORE);
    }

    @Step("Get User Alerts from Kafka topic 'alerts'")
    public static List<RuleAlert> getUserAlertsFromKafka(ClientHelper client) throws InterruptedException,
            JsonProcessingException {
        return Arrays.stream(
                objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlert[].class)).toList();
    }

    @Step("Get User Alerts from Kafka topic 'alerts'")
    public static List<RuleAlert> getUserAlertsFromKafka(ClientHelper client, String ruleName)
            throws InterruptedException,
            JsonProcessingException {
        return Arrays.stream(
                objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlert[].class)).filter(alert -> alert.rule.name.equals(ruleName)).toList();
    }

    @Step("Get User Alerts from Kafka topic 'alerts' by rule")
    public static List<RuleAlertV2> getUserAlertsV2FromKafka(ClientHelper client, String ruleName)
            throws InterruptedException,
            JsonProcessingException {
        return Arrays.stream(
                objectMapper.readValue(
                        kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlertV2[].class)).filter(alert -> {
                            RuleAlertV2.Rule rule = alert.getRule();
                            String actual;
                            if (rule != null) {
                                actual = rule.getName();
                            } else {
                                actual = null;
                            }
                            return actual != null && actual.trim().equalsIgnoreCase(ruleName.trim());
                        }).toList();
    }

    @Step("Get User Alerts from Kafka topic 'alerts' by Rule and reason")
    public static List<RuleAlertV2> getUserAlertsV2FromKafka(ClientHelper client, String ruleName, String reason)
            throws InterruptedException,
            JsonProcessingException {
        return Arrays.stream(
                objectMapper.readValue(
                        kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlertV2[].class)).filter(alert -> {
                            // Match rule name first
                            RuleAlertV2.Rule rule = alert.getRule();
                            String actualRuleName;
                            if (rule != null) {
                                actualRuleName = rule.getName();
                            } else {
                                actualRuleName = null;
                            }
                            boolean ruleMatches = actualRuleName != null && actualRuleName.trim().equalsIgnoreCase(ruleName.trim());
                            if (!ruleMatches) return false;

                            // If reason filter is provided, match it too (case-insensitive, trimmed)
                            if (reason == null || reason.isBlank()) return true;
                            String actualReason = alert.getReason();
                            return actualReason != null && actualReason.trim().equalsIgnoreCase(reason.trim());
                        }).toList();
    }

    @Deprecated
    @Step("Get Withdrawal from Kafka topic 'approvals'")
    public static List<WithdrawalApprovals> getWithdrawalApprovalsFromKafka(String withdrawalId)
            throws InterruptedException,
            JsonProcessingException {
        return Arrays.stream(
                objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_WITHDRAWAL_APPROVALS, withdrawalId).toString(), WithdrawalApprovals[].class)).toList();
    }

    @Step("Get Withdrawal from Kafka topic 'approvals'")
    public static List<WithdrawalApprovalsV2> getWithdrawalApprovalsV2FromKafka(String withdrawalId)
            throws InterruptedException,
            JsonProcessingException {
        return Arrays.stream(
                objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_WITHDRAWAL_APPROVALS, withdrawalId).toString(), WithdrawalApprovalsV2[].class)).toList();
    }

    @Step("Get User Alerts from postgres.bo.alert table")
    public static List<Alert> getUserAlertsFromDb(ClientHelper client) throws Exception {
        List<Alert> result = getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format("client_ucid = '%s' AND status = 'OPEN'", client.getUcid()), Alert.class, 60);
        return result != null ? result : List.of(); // empty if no alerts found after retries
    }

    @Step("Get User Alerts from postgres.bo.alert table")
    public static List<Alert> getUserAlertsFromDb(ClientHelper client, String ruleName) throws Exception {
        return getObjectsFromDB(DbName.POSTGRES, BO_ALERT_TABLE_NAME, String.format("client_ucid = '%s' AND status = 'OPEN'", client.getUcid()), Alert.class).stream().filter(alert -> alert.getRule().equals(ruleName)).toList();
    }

    @Step("Get Rule Decision from payment gate db")
    public static List<PaymentDecisionsObject> getRuleDecisionByWithdrawalIdFromDb(UUID paymentId)
            throws Exception {
        return getObjectsFromDB(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id='%s'", paymentId), PaymentDecisionsObject.class);
    }

    @Step("Get User Payment event from postgres.paymentgate.payment_events table")
    public static List<PaymentEventsObject> getUserPaymentEventsFromDb(ClientHelper client) throws Exception {
        return getObjectsFromDB(DbName.POSTGRES, PAYMENT_EVENT_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()), PaymentEventsObject.class);
    }

    @Step("Get User restrictions from mitigation DB")
    public static List<ClientGeneralRestriction> getUserRestrictionsFromDb(ClientHelper client) throws Exception {
        List<ClientGeneralRestriction> result = getObjectsFromDB(
                DbName.POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", client.getUcid()), ClientGeneralRestriction.class, 30);
        return result != null ? result : List.of(); // empty if no alerts found after retries
    }

    @Step("Get abuser status by ucid")
    public static GetStatusResponseBody getAbuserStatus(ClientHelper client) throws Exception {
        Response response = getClientStatus(client);

        assertThat(response.body(), is(notNullValue()));
        GetStatusResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetStatusResponseBody.class);
        assertThat("Assert that code is 200", response.code(), is(200));

        return mappedResponse;
    }

    @Step("Check {elementId} presented in rule path")
    public static void checkElementId(String elementId, String eventId, String bpmnProcessId) {

        // Wait until runId appears in zeebe_rules_started
        ZeebeRulesStarted started = await().atMost(120, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> {
            List<ZeebeRulesStarted> startedList = getObjectsFromDB(
                    DbName.CLICKHOUSE, REPORTING_DB_ZEEBE_RULES_STARTED, String.format("SELECT run_id FROM %s WHERE event_id = '%s' and rule_name = '%s'", REPORTING_DB_ZEEBE_RULES_STARTED, eventId, bpmnProcessId), ZeebeRulesStarted.class);

            if (startedList != null && !startedList.isEmpty()) {
                return startedList.getFirst();
            } else {
                return null;
            }
        }, Objects::nonNull);

        String runId = started.getRunId();

        // Wait until the elementId appears in the elements string for the runId
        String elementIdsDb = await().atMost(120, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> {
            List<ZeebeRulesElements> list = getObjectsFromDB(
                    DbName.CLICKHOUSE, REPORTING_DB_ZEEBE_RULE_ELEMENTS, String.format("run_id = '%s'", runId), ZeebeRulesElements.class);
            String joined;
            if (list == null || list.isEmpty()) {
                joined = "";
            } else {
                joined = list.stream().map(ZeebeRulesElements::getElementId).filter(Objects::nonNull).filter(s -> !s.isBlank()).collect(Collectors.joining(","));
            }
            System.out.println("Looking for Element ID: " + elementId + " in list: " + joined);
            return joined;
        }, ids -> ids != null && ids.contains(elementId));

        assertThat(elementIdsDb, containsString(elementId));
    }

    public static List<Acknowledgement> getPaymentAcknowledgementFromKafka(String paymentId) throws Exception {
        return Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_PAYMENT_ACKNOWLEDGE, paymentId).toString(), Acknowledgement[].class)).toList();
    }

    @Step("Check {elementId} NOT presented in rule path")
    public static void checkElementIdNotPresent(String elementId, String eventId, String bpmnProcessId)
            throws Exception {
        // Wait until runId appears in zeebe_rules_started
        ZeebeRulesStarted started = await().atMost(30, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> {
            List<ZeebeRulesStarted> startedList = getObjectsFromDB(
                    DbName.CLICKHOUSE, REPORTING_DB_ZEEBE_RULES_STARTED, String.format("SELECT run_id FROM %s WHERE event_id = '%s' and rule_name = '%s'", REPORTING_DB_ZEEBE_RULES_STARTED, eventId, bpmnProcessId), ZeebeRulesStarted.class);

            if (startedList != null && !startedList.isEmpty()) {
                return startedList.getFirst();
            } else {
                return null;
            }
        }, Objects::nonNull);

        String runId = started.getRunId();

        // Try to wait until the elementId appears and expect a timeout (meaning it never appeared)
        try {
            await().atMost(30, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> {
                List<ZeebeRulesElements> list = getObjectsFromDB(
                        DbName.CLICKHOUSE, REPORTING_DB_ZEEBE_RULE_ELEMENTS, String.format("run_id = '%s'", runId), ZeebeRulesElements.class);
                String joined;
                if (list == null || list.isEmpty()) {
                    joined = "";
                } else {
                    joined = list.stream().map(ZeebeRulesElements::getElementId).filter(Objects::nonNull).filter(s -> !s.isBlank()).collect(Collectors.joining(","));
                }
                System.out.println("Ensuring Element ID is absent: " + elementId + " current list: " + joined);
                return joined.contains(elementId);
            });

            // If we reach here, the element appeared within the timeout, which is a failure for this check
            throw new AssertionError("Element ID '" + elementId + "' unexpectedly appeared in rule path for runId=" + runId);
        } catch (ConditionTimeoutException ignored) {
            // Expected: element never appeared during the waiting period. Verify once more and assert not present.
            List<ZeebeRulesElements> list = getObjectsFromDB(
                    DbName.CLICKHOUSE, REPORTING_DB_ZEEBE_RULE_ELEMENTS, String.format("run_id = '%s'", runId), ZeebeRulesElements.class);
            String joined;
            if (list == null || list.isEmpty()) {
                joined = "";
            } else {
                joined = list.stream().map(ZeebeRulesElements::getElementId).filter(Objects::nonNull).filter(s -> !s.isBlank()).collect(Collectors.joining(","));
            }
            assertThat(joined, not(containsString(elementId)));
        }
    }
}
