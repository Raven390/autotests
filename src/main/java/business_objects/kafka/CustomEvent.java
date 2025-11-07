package business_objects.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CustomEvent {

    // JSON example for reference:
    // {
    //   "id": "fc82acbf-a1fb-46a1-af24-aa818d6fb756",
    //   "type": "custom_rule",
    //   "timestamp": "2025-10-21T09:33:12Z",
    //   "serverId": "112",
    //   "tradingAccount": "18885551",
    //   "brand": "vt",
    //   "clientId": "760338",
    //   "alert": "Client repeatedly opens opposite-direction trades using known hedging EA comments ('vef', 'My Order').",
    //   "fraudType": "HEDGING",
    //   "source": "ivan-script-hedge-ea",
    //   "message": "Raised automatically by Hedge EA detection script for manual review."
    // }

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("serverId")
    private String serverId;

    @JsonProperty("tradingAccount")
    private String tradingAccount;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("alert")
    private String alert;

    @JsonProperty("fraudType")
    private String fraudType;

    @JsonProperty("restriction")
    private String restriction;

    @JsonProperty("source")
    private String source;

    @JsonProperty("message")
    private String message;

    public CustomEvent(
            String id, String type, String timestamp, String serverId, String tradingAccount, String brand,
            String clientId,
            String alert, String fraudType, String restriction, String source, String message) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.serverId = serverId;
        this.tradingAccount = tradingAccount;
        this.brand = brand;
        this.clientId = clientId;
        this.alert = alert;
        this.fraudType = fraudType;
        this.restriction = restriction;
        this.source = source;
        this.message = message;
    }

    public CustomEvent() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(String tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public String getFraudType() {
        return fraudType;
    }

    public void setFraudType(String fraudType) {
        this.fraudType = fraudType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
