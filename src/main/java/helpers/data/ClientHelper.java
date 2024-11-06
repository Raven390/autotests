package helpers.data;

import java.util.Objects;

public class ClientHelper {

    private Integer userId;
    private String uuid;
    private Brand brand;
    private Integer tradingAccount;
    private Integer serverId;
    private String email;
    private String phoneNumber;
    private String ipAddress;

    public ClientHelper() {
    }

    public ClientHelper(Integer userId, String uuid, Brand brand, Integer tradingAccount, Integer serverId) {
        this.userId = userId;
        this.uuid = uuid;
        this.brand = brand;
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
    }

    public ClientHelper(Integer userId, String uuid, Brand brand, Integer tradingAccount, Integer serverId, String email, String phoneNumber, String ipAddress) {
        this.userId = userId;
        this.uuid = uuid;
        this.brand = brand;
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.ipAddress = ipAddress;
    }

    public String getUcid() {
        return String.format("%s-%s", brand.getDisplayName().toLowerCase().replace(" ", "_"), userId);
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

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Integer getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(Integer tradingAccount) {
        this.tradingAccount = tradingAccount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientHelper that = (ClientHelper) o;
        return Objects.equals(userId, that.userId) && Objects.equals(uuid, that.uuid) && brand == that.brand && Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(serverId, that.serverId) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(ipAddress, that.ipAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, uuid, brand, tradingAccount, serverId, email, phoneNumber, ipAddress);
    }

    @Override
    public String toString() {
        return "ClientHelper{" +
                "userId=" + userId +
                ", uuid='" + uuid + '\'' +
                ", brand=" + brand +
                ", tradingAccount=" + tradingAccount +
                ", serverId=" + serverId +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
