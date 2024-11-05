package helpers.data;

import java.util.Objects;

public class ClientHelper {

    private Integer userId;
    private String uuid;
    private Brand brand;
    private Integer tradingAccount;
    private Integer serverId;

    public ClientHelper() {
    }

    public ClientHelper(Integer userId, String uuid, Brand brand, Integer tradingAccount, Integer serverId) {
        this.userId = userId;
        this.uuid = uuid;
        this.brand = brand;
        this.tradingAccount = tradingAccount;
        this.serverId = serverId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUuid() {
        return uuid;
    }

    public Brand getBrand() {
        return brand;
    }

    public Integer getTradingAccount() {
        return tradingAccount;
    }

    public Integer getServerId() {
        return serverId;
    }

    public String getUcid() {
        return String.format("%s+%s", brand.getDisplayName().toLowerCase(), userId);
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setTradingAccount(Integer tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientHelper that = (ClientHelper) o;
        return Objects.equals(userId, that.userId) && Objects.equals(uuid, that.uuid) && brand == that.brand && Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, uuid, brand, tradingAccount, serverId);
    }

    @Override
    public String toString() {
        return "ClientHelper{" +
                "userId=" + userId +
                ", uuid='" + uuid + '\'' +
                ", brand=" + brand +
                ", tradingAccount=" + tradingAccount +
                ", serverId=" + serverId +
                '}';
    }
}
