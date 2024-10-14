package helpers.kafka.crmEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrmReferralEvent {
    @JsonProperty("schemaVersion")
    public String schemaVersion;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("type")
    public String type;

    @JsonProperty("id")
    public String id;

    @JsonProperty("clientId")
    public Integer clientId;

    @JsonProperty("bonusAmount")
    public Float bonusAmount;

    @JsonProperty("currency")
    public String currency;

    @JsonProperty("bonusType")
    public String bonusType;

    @JsonProperty("eventDate")
    public String eventDate;

    public static CrmReferralEvent getReferralEvent(
            String schemaVersion,
            String brand,
            String regulator,
            String type,
            String id,
            Integer clientId,
            Float bonusAmount,
            String currency,
            String bonusType,
            String eventDate) {
        CrmReferralEvent event = new CrmReferralEvent();
        event.schemaVersion = schemaVersion;
        event.brand = brand;
        event.regulator = regulator;
        event.type = type;
        event.id = id;
        event.clientId = clientId;
        event.bonusAmount = bonusAmount;
        event.currency = currency;
        event.bonusType = bonusType;
        event.eventDate = eventDate;
        return event;
    }
}
