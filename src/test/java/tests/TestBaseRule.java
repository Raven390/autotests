package tests;

import business_objects.api.abuse_registry.GetStatusResponseBody;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.clickhouse.reporting_test.ZeebeRulesElements;
import business_objects.db.clickhouse.reporting_test.ZeebeRulesStarted;
import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import business_objects.kafka.crm_events.LoginEvent;
import business_objects.kafka.crm_events.RegistrationEvent;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.kafka.mt_events.TradeEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Step;
import okhttp3.Response;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestResultWatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static helpers.api.AbuseRegistryHelper.getClientStatus;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;


@ExtendWith(TestResultWatcher.class)
public class TestBaseRule {
    public static KafkaHelper kafka = new KafkaHelper();
    public static ObjectMapper objectMapper = new ObjectMapper();

    @Step("Produce withdrawal event to crm-events topic")
    public static void produceWithdrawalMessageToKafka(CrmWithdrawalEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_CRM_EVENTS);
    }

    @Step("Produce close trade event to mt-events topic")
    public static void produceCloseTradeMessageToKafka(CloseTradeMtEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_MT_EVENTS);
    }

    @Step("Produce open trade event to mt-events topic")
    public static void produceTradeMessageToKafka(TradeEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_MT_EVENTS);
    }

    @Step("Produce registration event to crm-events topic")
    public static void produceRegistrationEventToKafka(RegistrationEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_CRM_EVENTS);
    }

    @Step("Produce login event to crm-events topic")
    public static void produceLoginMessageToKafka(LoginEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_CRM_EVENTS);
    }

    @Step("Get User Alerts from Kafka topic 'alerts'")
    public static List<RuleAlert> getUserAlertsFromKafka(ClientHelper client) throws InterruptedException,
            JsonProcessingException {
        return Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlert[].class)).toList();
    }

    @Step("Get User Alerts from Kafka topic 'alerts'")
    public static List<RuleAlert> getUserAlertsFromKafka(ClientHelper client, String ruleName)
            throws InterruptedException,
            JsonProcessingException {
        return Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlert[].class)).filter(alert -> alert.rule.name.equals(ruleName)).toList();
    }

    @Step("Get User Alerts from postgres.bo.alert table with retries")
    public static List<Alert> getUserAlertsFromDb(ClientHelper client) throws Exception {
        List<Alert> result = getObjectsFromDB(DbName.BACKOFFICE, BO_ALERT_TABLE_NAME, String.format("client_ucid = '%s' AND status = 'OPEN'", client.getUcid()), Alert.class, 60);
        return result != null ? result : List.of(); // empty if no alerts found after retries
    }

    @Step("Get User restrictions from mitigation DB")
    public static List<ClientGeneralRestriction> getUserRestrictionsFromDb(ClientHelper client) throws Exception {
        List<ClientGeneralRestriction> result = getObjectsFromDB(
                DbName.MITIGATION_POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, String.format("ucid = '%s'", client.getUcid()), ClientGeneralRestriction.class, 30);
        return result != null ? result : List.of(); // empty if no alerts found after retries
    }

    @Step("Get abuser status by ucid")
    public static GetStatusResponseBody getAbuserStatus(ClientHelper client) throws Exception {
        Response response = getClientStatus(client);

        assert response.body() != null;
        GetStatusResponseBody mappedResponse = objectMapper.readValue(response.body().string(), GetStatusResponseBody.class);
        assertThat("Assert that code is 200", response.code(), is(200));

        return mappedResponse;
    }

    @Step("Get abuser status by ucid")
    public static void checkElementId(String elementId, String event_id, String ruleName) throws Exception {
        List<ZeebeRulesStarted> startedList = null;
        for (int i = 0; i < 60; i++) {
            startedList = getObjectsFromDB(
                    DbName.CLICKHOUSE, ZEEBE_RULES_STARTED, String.format("SELECT run_id FROM %s WHERE event_id = '%s' and rule_name = '%s'", ZEEBE_RULES_STARTED, event_id, ruleName), ZeebeRulesStarted.class);
            if (startedList != null && !startedList.isEmpty()) {
                break;
            }
            Thread.sleep(1000);
        }
        System.out.println("List" + startedList);
        assert !startedList.isEmpty();
        String runId = startedList.getFirst().getRunId();

        List<ZeebeRulesElements> elementsList = null;
        for (int i = 0; i < 60; i++) {
            elementsList = getObjectsFromDB(
                    DbName.CLICKHOUSE, ZEEBE_RULE_ELEMENTS, String.format("run_id = '%s'", runId), ZeebeRulesElements.class);
            if (elementsList != null && !elementsList.isEmpty()) {
                break;
            }
            Thread.sleep(1000);
        }
        System.out.println("Elements List: " + elementsList);

        assert !elementsList.isEmpty();
        String elementIdsDb = elementsList.stream().map(ZeebeRulesElements::getElementId).filter(Objects::nonNull).filter(s -> !s.isBlank()).collect(Collectors.joining(","));
        System.out.println("Element IDs: " + elementIdsDb);

        assertThat(elementIdsDb, containsString(elementId));
    }

}
