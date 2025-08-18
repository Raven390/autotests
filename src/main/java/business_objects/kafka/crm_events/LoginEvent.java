package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class LoginEvent {

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("clientId")
    private Integer clientId;

    @JsonProperty("eventDate")
    private String eventDate;

    @JsonProperty("id")
    private String id;

    @JsonProperty("ipAddress")
    private String ipAddress;

    @JsonProperty("loginId")
    private String loginId;

    @JsonProperty("loginType")
    private String loginType;

    @JsonProperty("regulator")
    private String regulator;

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("type")
    private String type;

    public LoginEvent() {
    }

    public LoginEvent(
            String brand, Integer clientId, String eventDate, String id, String ipAddress, String loginId,
            String loginType,
            String regulator, String schemaVersion, String type) {
        this.brand = brand;
        this.clientId = clientId;
        this.eventDate = eventDate;
        this.id = id;
        this.ipAddress = ipAddress;
        this.loginId = loginId;
        this.loginType = loginType;
        this.regulator = regulator;
        this.schemaVersion = schemaVersion;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LoginEvent that)) return false;
        return Objects.equals(brand, that.brand) && Objects.equals(clientId, that.clientId) && Objects.equals(
                eventDate, that.eventDate) && Objects.equals(id, that.id) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(
                        loginId, that.loginId) && Objects.equals(loginType, that.loginType) && Objects.equals(
                                regulator, that.regulator) && Objects.equals(schemaVersion, that.schemaVersion) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, clientId, eventDate, id, ipAddress, loginId, loginType, regulator, schemaVersion, type);
    }

    @Override
    public String toString() {
        return "LoginEvent{" + "brand='" + brand + '\'' + ", clientId=" + clientId + ", eventDate='" + eventDate + '\'' + ", id='" + id + '\'' + ", ipAddress='" + ipAddress + '\'' + ", loginId='" + loginId + '\'' + ", loginType='" + loginType + '\'' + ", regulator='" + regulator + '\'' + ", schemaVersion='" + schemaVersion + '\'' + ", type='" + type + '\'' + '}';
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
