package business_objects.kafka.crm_events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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

    @JsonProperty("metadata")
    public CrmEventMetadata metadata;

    @JsonProperty("initialEventTime")
    public String initialEventTime;

    public LoginEvent() {
    }

    public LoginEvent(String eventDate, Integer clientId, String brand, String ipAddress, String cid, String cookie,
            String loginType, String type) {
        this.eventDate = eventDate;
        this.clientId = clientId;
        this.brand = brand;
        this.ipAddress = ipAddress;
        this.cid = cid;
        this.cookie = cookie;
        this.loginType = loginType;
        this.type = type;
    }

    public LoginEvent(String id, String eventDate, Integer clientId, String brand, String ipAddress, String cid,
            String cookie, String loginType, String type) {
        this.id = id;
        this.eventDate = eventDate;
        this.clientId = clientId;
        this.brand = brand;
        this.ipAddress = ipAddress;
        this.cid = cid;
        this.cookie = cookie;
        this.loginType = loginType;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginEvent that = (LoginEvent) o;
        return Objects.equals(eventDate, that.eventDate) && Objects.equals(clientId, that.clientId) && Objects.equals(brand, that.brand) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(cid, that.cid) && Objects.equals(cookie, that.cookie) && Objects.equals(loginType, that.loginType) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventDate, clientId, brand, ipAddress, cid, cookie, loginType, type);
    }

    @Override
    public String toString() {
        return "LoginEvent{" + "id='" + id + '\'' + ", eventDate='" + eventDate + '\'' + ", clientId=" + clientId + ", brand='" + brand + '\'' + ", ipAddress='" + ipAddress + '\'' + ", cid='" + cid + '\'' + ", cookie='" + cookie + '\'' + ", loginType='" + loginType + '\'' + ", type='" + type + '\'' + '}';
    }
}
