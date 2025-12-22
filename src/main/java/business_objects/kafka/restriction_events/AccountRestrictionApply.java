package business_objects.kafka.restriction_events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRestrictionApply {

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("messageId")
    public String messageId;

    @JsonProperty("accountId")
    public Integer accountId;

    @JsonProperty("clientId")
    public Integer clientId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("serverId")
    public Integer serverId;

    @JsonProperty("initialBanDurationInMinutes")
    public Integer initialBanDurationInMinutes;

    @JsonProperty("modifier")
    public String modifier;

    @JsonProperty("restriction")
    public Restriction restriction;

    public static class Restriction {

        @JsonProperty("restrictionId")
        public Integer restrictionId;

        @JsonProperty("restrictionCode")
        public String restrictionCode;

        @JsonProperty("sites")
        public String[] sites;
    }
}
