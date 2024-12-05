package businessObjects.kafka.crmDbEvents.login;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class LoginDbEventData {

    @JsonProperty("login_datetime")
    public String loginDatetime;

    @JsonProperty("user_id")
    public Integer userId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("ip_address")
    public String ipAddress;

    @JsonProperty("ua_string")
    public String uaString;

    @JsonProperty("cookie")
    public String cookie;

    public LoginDbEventData() {
    }

    public LoginDbEventData(String loginDatetime, Integer userId, String brand, String ipAddress, String uaString,
            String cookie) {
        this.loginDatetime = loginDatetime;
        this.userId = userId;
        this.brand = brand;
        this.ipAddress = ipAddress;
        this.uaString = uaString;
        this.cookie = cookie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginDbEventData that = (LoginDbEventData) o;
        return Objects.equals(loginDatetime, that.loginDatetime) && Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(uaString, that.uaString) && Objects.equals(cookie, that.cookie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loginDatetime, userId, brand, ipAddress, uaString, cookie);
    }

    @Override
    public String toString() {
        return "LoginDbEventData{" + "loginDatetime='" + loginDatetime + '\'' + ", userId=" + userId + ", brand='" + brand + '\'' + ", ipAddress='" + ipAddress + '\'' + ", uaString='" + uaString + '\'' + ", cookie='" + cookie + '\'' + '}';
    }
}
