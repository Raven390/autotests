package helpers.kafka.alerts;

import helpers.kafka.KafkaHelper;

import java.util.UUID;

public class CreateSimpleAlert {


    public static void createSimpleAlert(String ucid, String ruleName) {
        String uuid = UUID.randomUUID().toString();
        String key = UUID.randomUUID().toString();

        String alert = "{\n" + "  \"alertId\": \"" + uuid + "\",\n" + "  \"timestamp\": \"2024-08-21T13:45:56Z\",\n" + "  \"ucid\": \"" + ucid + "\",\n" + "  \"rule\": {\n" + "    \"code\": 558,\n" + "    \"ver\": \"12\",\n" + "    \"name\": \"" + ruleName + "\",\n" + "    \"trigger\": \"LOGIN\",\n" + "    \"fraudType\": \"CPA\",\n" + "    \"attributes\": {\n" + "      \"first\": \"firstValue\",\n" + "      \"second\": \"secondValue\"\n" + "    }\n" + "  }\n" + "}";
        KafkaHelper kafkaHelper = new KafkaHelper();
        kafkaHelper.produceMessage(key, alert, "alerts");
        System.out.println("Alert was sent successfully");
    }
}
