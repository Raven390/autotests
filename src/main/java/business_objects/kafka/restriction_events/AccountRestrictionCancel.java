package business_objects.kafka.restriction_events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRestrictionCancel {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("accountId")
    private Integer accountId;

    @JsonProperty("clientId")
    private Integer clientId;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("serverId")
    private Integer serverId;

    @JsonProperty("modifier")
    private String modifier;

    @JsonProperty("restriction")
    private Restriction restriction;

    public static class Restriction {

        @JsonProperty("restrictionId")
        private Integer restrictionId;

        @JsonProperty("sites")
        private String[] sites;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Restriction getRestrictions() {
        return restriction;
    }

    public void setRestrictions(Restriction restrictions) {
        this.restriction = restrictions;
    }
}
