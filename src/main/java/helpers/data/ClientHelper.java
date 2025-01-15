package helpers.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;

import java.util.Objects;

import static utils.Utils.getUcidByUserIdAndBrand;

public class ClientHelper {

    private Integer userId;
    private String uid;
    private Brand brand;
    private Regulator regulator;
    private Integer tradingAccount;
    private Integer tradingAccount2;
    private Integer serverId;
    private String email;
    private String phoneNumber;
    private String ipAddress;
    private String countryCode;
    private Integer cpaId;
    private String deviceId;
    private String webSessionId;
    private String sessionId;
    private String digitalId;
    private String namedateofbirth;

    public ClientHelper() {
    }

    public ClientHelper(Integer userId, String uid, Brand brand, Integer tradingAccount, Integer serverId) {
        this.userId = userId;
        this.uid = uid;
        this.brand = brand;
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
    }

    public ClientHelper(
            Integer userId, String uid, Brand brand, Integer tradingAccount, Integer tradingAccount2,
            Integer serverId) {
        this.userId = userId;
        this.uid = uid;
        this.brand = brand;
        this.tradingAccount = tradingAccount;
        this.tradingAccount2 = tradingAccount2;
        this.serverId = serverId;
    }

    public ClientHelper(Integer userId, String uid, Regulator regulator, Brand brand, Integer tradingAccount,
            Integer tradingAccount2,
            Integer serverId, String email, String phoneNumber, String ipAddress, String countryCode) {
        this.userId = userId;
        this.uid = uid;
        this.regulator = regulator;
        this.brand = brand;
        this.tradingAccount = tradingAccount;
        this.tradingAccount2 = tradingAccount2;
        this.serverId = serverId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ipAddress = ipAddress;
        this.countryCode = countryCode;
    }

    public ClientHelper(
            Integer userId, String uid, Regulator regulator, Brand brand, Integer tradingAccount,
            Integer tradingAccount2,
            Integer serverId, String email, String phoneNumber, String ipAddress, String countryCode, Integer cpaId,
            String deviceId, String webSessionId, String sessionId, String digitalId, String namedateofbirth) {
        this.userId = userId;
        this.uid = uid;
        this.regulator = regulator;
        this.brand = brand;
        this.tradingAccount = tradingAccount;
        this.tradingAccount2 = tradingAccount2;
        this.serverId = serverId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ipAddress = ipAddress;
        this.countryCode = countryCode;
        this.cpaId = cpaId;
        this.deviceId = deviceId;
        this.webSessionId = webSessionId;
        this.sessionId = sessionId;
        this.digitalId = digitalId;
        this.namedateofbirth = namedateofbirth;
    }

    @JsonIgnore
    public String getUcid() {
        return getUcidByUserIdAndBrand(userId, brand);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBrand() {
        return brand.getDisplayName();
    }

    public String getRegulator() {
        return regulator.getDisplayName();
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setRegulator(Regulator regulator) {
        this.regulator = regulator;
    }

    public Integer getTradingAccount() {
        return tradingAccount;
    }

    public Integer getTradingAccount2() {
        return tradingAccount2;
    }

    public void setTradingAccount(Integer tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public void setTradingAccount2(Integer tradingAccount2) {
        this.tradingAccount2 = tradingAccount2;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getCpaId() {
        return cpaId;
    }

    public void setCpaId(Integer cpaId) {
        this.cpaId = cpaId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getWebSessionId() {
        return webSessionId;
    }

    public void setWebSessionId(String webSessionId) {
        this.webSessionId = webSessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getDigitalId() {
        return digitalId;
    }

    public void setDigitalId(String digitalId) {
        this.digitalId = digitalId;
    }

    public String getNamedateofbirth() {
        return namedateofbirth;
    }

    public void setNamedateofbirth(String namedateofbirth) {
        this.namedateofbirth = namedateofbirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientHelper that = (ClientHelper) o;
        return Objects.equals(userId, that.userId) && Objects.equals(uid, that.uid) && regulator == that.regulator && brand == that.brand && Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(serverId, that.serverId) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(countryCode, that.countryCode) && Objects.equals(deviceId, that.deviceId) && Objects.equals(cpaId, that.cpaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, uid, regulator, brand, tradingAccount, serverId, email, phoneNumber, ipAddress, countryCode, cpaId, deviceId);
    }

    @Override
    public String toString() {
        return "ClientHelper{" + "userId=" + userId + ", uid='" + uid + '\'' + ", brand=" + brand + ", regulator=" + regulator + ", tradingAccount=" + tradingAccount + ", tradingAccount2=" + tradingAccount2 + ", serverId=" + serverId + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", ipAddress='" + ipAddress + '\'' + ", countryCode='" + countryCode + '\'' + ", cpaId=" + cpaId + ", deviceId='" + deviceId + '\'' + '}';
    }
}
