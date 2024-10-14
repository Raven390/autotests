package helpers.kafka.crmEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrmPasswordChangeEvent {
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

    @JsonProperty("ipAddress")
    public String ipAddress;

    @JsonProperty("eventDate")
    public String eventDate;

    public static CrmPasswordChangeEvent getPasswordChangeEvent(
            String schemaVersion,
            String brand,
            String regulator,
            String type,
            String id,
            Integer clientId,
            String ipAddress,
            String eventDate) {
        CrmPasswordChangeEvent event = new CrmPasswordChangeEvent();
        event.schemaVersion = schemaVersion;
        event.brand = brand;
        event.regulator = regulator;
        event.type = type;
        event.id = id;
        event.clientId = clientId;
        event.ipAddress = ipAddress;
        event.eventDate = eventDate;
        return event;
    }
}
