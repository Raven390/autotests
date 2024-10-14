package helpers.kafka.crmEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CrmLoginEvent {
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

    @JsonProperty("loginId")
    public Integer loginId;

    @JsonProperty("clientId")
    public Integer clientId;

    @JsonProperty("ipAddress")
    public String ipAddress;

    @JsonProperty("eventDate")
    public String eventDate;

    @JsonProperty("loginType")
    public String loginType;

    public static CrmLoginEvent getLoginEvent(
            String schemaVersion,
            String brand,
            String regulator,
            String type,
            String id,
            Integer loginId,
            Integer clientId,
            String ipAddress,
            String eventDate,
            String loginType) {
        CrmLoginEvent event = new CrmLoginEvent();
        event.schemaVersion = schemaVersion;
        event.brand = brand;
        event.regulator = regulator;
        event.type = type;
        event.id = id;
        event.loginId = loginId;
        event.clientId = clientId;
        event.ipAddress = ipAddress;
        event.eventDate = eventDate;
        event.loginType = loginType;
        return event;
    }
}
