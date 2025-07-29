package tests;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.crm_events.CrmWithdrawalEvent;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.TestResultWatcher;

import java.util.Arrays;
import java.util.List;

import static helpers.database.DbHelper.getObjectsFromDB;
import static utils.Constants.*;


@ExtendWith(TestResultWatcher.class)
public class TestBaseRule {
    public static KafkaHelper kafka = new KafkaHelper();
    public static ObjectMapper objectMapper = new ObjectMapper();

    @Step("Produce withdrawal event to crm-events topic")
    public static void produceWithdrawalMessageToKafka(CrmWithdrawalEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_MT_EVENTS);
    }

    @Step("Produce close trade event to mt-events topic")
    public static void produceCloseTradeMessageToKafka(CloseTradeMtEvent event) throws JsonProcessingException {
        kafka.produceMessage(KAFKA_MESSAGE_KEY, objectMapper.writeValueAsString(event), KAFKA_TOPIC_MT_EVENTS);
    }

    @Step("Get User Alerts from Kafka topic 'alerts'")
    public static List<RuleAlert> getUserAlertsFromKafka(ClientHelper client) throws InterruptedException,
            JsonProcessingException {
        return Arrays.stream(objectMapper.readValue(kafka.consumeMessages(KAFKA_TOPIC_ALERTS, client.getUcid()).toString(), RuleAlert[].class)).toList();
    }

    @Step("Get User Alerts from postgres.bo.alert table")
    public static List<Alert> getUserAlertsFromDb(ClientHelper client) throws Exception {
        return getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, String.format("client_id = (select id from %s where ucid = '%s') AND status = 'OPEN'", BO_CLIENT_TABLE_NAME, client.getUcid()), Alert.class);
    }
}
