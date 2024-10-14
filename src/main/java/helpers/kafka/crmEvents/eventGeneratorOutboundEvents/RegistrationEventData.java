package helpers.kafka.crmEvents.eventGeneratorOutboundEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistrationEventData {
    @JsonProperty("UUID")
    public String UUID;

    @JsonProperty("create_time")
    public String create_time;

    @JsonProperty("user_id")
    public Integer user_id;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("mt_account")
    public Integer mt_account;
}
