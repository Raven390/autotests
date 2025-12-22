package business_objects.kafka.restriction_events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientRestrictionApply {

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("messageId")
    public String messageId;

    @JsonProperty("clientId")
    public Integer clientId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("restrictions")
    public Restriction[] restrictions;

    public static class Restriction {

        @JsonProperty("restrictionId")
        public Integer restrictionId;

        @JsonProperty("internalReason")
        public String internalReason;

        @JsonProperty("restrictionCode")
        public String restrictionCode;
    }
}
