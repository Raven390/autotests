package business_objects.kafka.ai_alerts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface AiAlert {
    String getId();

    String getEventId();

    String getProducedAtUtc();

    String getAlertDate();

    String getEventDate();

    String getAlertType();

    String getUcid();

    Integer getTradingAccount();

    Integer getServerId();

    String getRule();

    String getFraudType();

    String getReason();
}
