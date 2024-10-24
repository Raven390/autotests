package helpers.kafka.crmEvents.eventGeneratorOutboundEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginEvent {
    @JsonProperty("id")
    public String id;

    @JsonProperty("eventDate")
    public String eventDate;

    @JsonProperty("clientId")
    public Integer clientId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("ipAddress")
    public String ipAddress;

    @JsonProperty("cid")
    public String cid;

    @JsonProperty("cookie")
    public String cookie;

    @JsonProperty("loginType")
    public String loginType;

    @JsonProperty("type")
    public String type;
}
