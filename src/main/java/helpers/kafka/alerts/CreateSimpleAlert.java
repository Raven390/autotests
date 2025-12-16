package helpers.kafka.alerts;

import business_objects.kafka.alerts.PaymentAlertMessage;
import business_objects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;

import java.util.UUID;

import static business_objects.kafka.alerts.RuleAlertFactory.generatePaymentAlertByUcid;
import static business_objects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static utils.Constants.KAFKA_TOPIC_ALERTS;
import static utils.Utils.writeLog;

public class CreateSimpleAlert {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendSimpleAlert(String ucid, String ruleName) {
        String uuid = UUID.randomUUID().toString();
        String key = UUID.randomUUID().toString();

        String alert = "{\n" + "  \"alertId\": \"" + uuid + "\",\n" + "  \"timestamp\": \"2024-08-21T13:45:56Z\",\n" + "  \"ucid\": \"" + ucid + "\",\n" + "  \"rule\": {\n" + "    \"code\": 558,\n" + "    \"ver\": \"12\",\n" + "    \"name\": \"" + ruleName + "\",\n" + "    \"trigger\": \"LOGIN\",\n" + "    \"fraudType\": \"" + ruleName + "\",\n" + "    \"attributes\": {\n" + "      \"first\": \"firstValue\",\n" + "      \"second\": \"secondValue\"\n" + "    }\n" + "  }\n" + "}";
        kafka.produceMessage(key, alert, "alerts");
        writeLog("Alert was sent successfully");
    }

    public static void sendSimpleAlert(String ucid) throws JsonProcessingException {
        RuleAlert alert1 = generateRuleAlertByUcid(ucid);
        kafka.produceMessage(alert1.alertId, objectMapper.writeValueAsString(alert1), KAFKA_TOPIC_ALERTS);
    }

    public static void sendSimplePaymentAlert(String ucid) throws JsonProcessingException {
        objectMapper.findAndRegisterModules();
        PaymentAlertMessage alert = generatePaymentAlertByUcid(ucid);
        kafka.produceMessage(alert.getId().toString(), objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
        writeLog("Alert was sent successfully");
    }
}
