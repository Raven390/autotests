package helpers.kafka.crmEvents.eventGeneratorOutboundEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginEvent {
    @JsonProperty("uuid")
    public String uuid;

    @JsonProperty("login_time")
    public String login_time;

    @JsonProperty("user_id")
    public Integer user_id;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("ip_address")
    public String ip_address;

    @JsonProperty("cid")
    public String cid;

    @JsonProperty("cookie")
    public String cookie;

    @JsonProperty("type")
    public String type;
}
