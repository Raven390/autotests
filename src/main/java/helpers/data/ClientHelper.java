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
    private Integer ibId;
    private Integer referrerId;
    private String deviceId;
    private String webSessionId;
    private String sessionId;
    private String digitalId;
    private String dateOfBirth;
    private String firstName;
    private String lastName;
    private String country;

    public ClientHelper() {
    }

    public ClientHelper(
            Integer userId, String uid, Brand brand, Regulator regulator, Integer tradingAccount,
            Integer tradingAccount2,
            Integer serverId, String email, String phoneNumber, String ipAddress, String countryCode, Integer cpaId,
            Integer ibId, Integer referrerId, String deviceId, String webSessionId, String sessionId, String digitalId,
            String dateOfBirth, String firstName, String lastName, String country) {
        this.userId = userId;
        this.uid = uid;
        this.brand = brand;
        this.regulator = regulator;
        this.tradingAccount = tradingAccount;
        this.tradingAccount2 = tradingAccount2;
        this.serverId = serverId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ipAddress = ipAddress;
        this.countryCode = countryCode;
        this.cpaId = cpaId;
        this.ibId = ibId;
        this.referrerId = referrerId;
        this.deviceId = deviceId;
        this.webSessionId = webSessionId;
        this.sessionId = sessionId;
        this.digitalId = digitalId;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
    }

    public ClientHelper(Integer userId, String uid, Brand brand, Integer tradingAccount, Integer serverId) {
        this.userId = userId;
        this.uid = uid;
        this.brand = brand;
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
    }

    public ClientHelper(Integer userId, String uid, Brand brand, Regulator regulator, Integer tradingAccount,
            Integer serverId) {
        this.userId = userId;
        this.uid = uid;
        this.brand = brand;
        this.regulator = regulator;
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
    }

    public ClientHelper(Integer userId, String uid, Brand brand, Regulator regulator, Integer tradingAccount,
            Integer tradingAccount2,
            Integer serverId) {
        this.userId = userId;
        this.uid = uid;
        this.brand = brand;
        this.regulator = regulator;
        this.tradingAccount = tradingAccount;
        this.tradingAccount2 = tradingAccount2;
        this.serverId = serverId;
    }

    public ClientHelper(Integer userId, Brand brand, Regulator regulator) {
        this.userId = userId;
        this.brand = brand;
        this.regulator = regulator;
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

    @Deprecated(forRemoval = true)
    // Use constructor with all fields
    public ClientHelper(
            Integer userId, String uid, Regulator regulator, Brand brand, Integer tradingAccount,
            Integer tradingAccount2,
            Integer serverId, String email, String phoneNumber, String ipAddress, String countryCode, Integer cpaId,
            Integer ibId, Integer referrerId,
            String deviceId, String webSessionId, String sessionId, String digitalId, String dateOfBirth,
            String firstName, String lastName) {
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
        this.ibId = ibId;
        this.referrerId = referrerId;
        this.cpaId = cpaId;
        this.deviceId = deviceId;
        this.webSessionId = webSessionId;
        this.sessionId = sessionId;
        this.digitalId = digitalId;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public Integer getReferrerId() {
        return referrerId;
    }

    public Integer getIbId() {
        return ibId;
    }

    public void setCpaId(Integer cpaId) {
        this.cpaId = cpaId;
    }

    public void setReferrerId(Integer referrerId) {
        this.referrerId = referrerId;
    }

    public void setIbId(Integer ibId) {
        this.ibId = ibId;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNameDateOfBirth() {
        return firstName + " " + lastName + " " + dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientHelper that = (ClientHelper) o;
        return Objects.equals(userId, that.userId) && Objects.equals(uid, that.uid) && brand == that.brand && regulator == that.regulator && Objects.equals(
                tradingAccount, that.tradingAccount) && Objects.equals(tradingAccount2, that.tradingAccount2) && Objects.equals(
                        serverId, that.serverId) && Objects.equals(email, that.email) && Objects.equals(
                                phoneNumber, that.phoneNumber) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(
                                        countryCode, that.countryCode) && Objects.equals(cpaId, that.cpaId) && Objects.equals(
                                                ibId, that.ibId) && Objects.equals(referrerId, that.referrerId) && Objects.equals(
                                                        deviceId, that.deviceId) && Objects.equals(webSessionId, that.webSessionId) && Objects.equals(
                                                                sessionId, that.sessionId) && Objects.equals(digitalId, that.digitalId) && Objects.equals(
                                                                        dateOfBirth, that.dateOfBirth) && Objects.equals(firstName, that.firstName) && Objects.equals(
                                                                                lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, uid, brand, regulator, tradingAccount, tradingAccount2, serverId, email, phoneNumber, ipAddress, countryCode, cpaId, ibId, referrerId, deviceId, webSessionId, sessionId, digitalId, dateOfBirth, firstName, lastName);
    }

    @Override
    public String toString() {
        return "ClientHelper{" + "userId=" + userId + ", uid='" + uid + '\'' + ", brand=" + brand + ", regulator=" + regulator + ", tradingAccount=" + tradingAccount + ", tradingAccount2=" + tradingAccount2 + ", serverId=" + serverId + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", ipAddress='" + ipAddress + '\'' + ", countryCode='" + countryCode + '\'' + ", cpaId=" + cpaId + ", ibId=" + ibId + ", referrerId=" + referrerId + ", deviceId='" + deviceId + '\'' + ", webSessionId='" + webSessionId + '\'' + ", sessionId='" + sessionId + '\'' + ", digitalId='" + digitalId + '\'' + ", dateOfBirth='" + dateOfBirth + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + '}';
    }
}
