package helpers.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import helpers.data.enums.Brands;

import java.util.Objects;

import static utils.Utils.getUcidByUserIdAndBrand;

public class ClientHelper {

    private Integer userId;
    private String uuid;
    private Brands brand;
    private Integer tradingAccount;
    private Integer tradingAccount2;
    private Integer serverId;
    private String email;
    private String phoneNumber;
    private String ipAddress;
    private String countryCode;

    public ClientHelper() {
    }

    public ClientHelper(Integer userId, String uuid, Brands brand, Integer tradingAccount, Integer serverId) {
        this.userId = userId;
        this.uuid = uuid;
        this.brand = brand;
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
    }

    public ClientHelper(Integer userId, String uuid, Brands brand, Integer tradingAccount, Integer tradingAccount2,
            Integer serverId) {
        this.userId = userId;
        this.uuid = uuid;
        this.brand = brand;
        this.tradingAccount = tradingAccount;
        this.tradingAccount2 = tradingAccount2;
        this.serverId = serverId;
    }

    public ClientHelper(Integer userId, String uuid, Brands brand, Integer tradingAccount, Integer tradingAccount2,
            Integer serverId, String email, String phoneNumber, String ipAddress, String countryCode) {
        this.userId = userId;
        this.uuid = uuid;
        this.brand = brand;
        this.tradingAccount = tradingAccount;
        this.tradingAccount2 = tradingAccount2;
        this.serverId = serverId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ipAddress = ipAddress;
        this.countryCode = countryCode;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBrand() {
        return brand.getDisplayName();
    }

    public void setBrand(Brands brand) {
        this.brand = brand;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientHelper that = (ClientHelper) o;
        return Objects.equals(userId, that.userId) && Objects.equals(uuid, that.uuid) && brand == that.brand && Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(serverId, that.serverId) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(countryCode, that.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, uuid, brand, tradingAccount, serverId, email, phoneNumber, ipAddress, countryCode);
    }

    @Override
    public String toString() {
        return "ClientHelper{" + "userId=" + userId + ", uuid='" + uuid + '\'' + ", brand=" + brand + ", tradingAccount=" + tradingAccount + ", tradingAccount2=" + tradingAccount2 + ", serverId=" + serverId + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", ipAddress='" + ipAddress + '\'' + ", countryCode='" + countryCode + '\'' + '}';
    }
}
