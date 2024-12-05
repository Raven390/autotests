package businessObjects.db.clickhouse.mtTbCreditsTable;


import java.util.Objects;

public class MtTbCreditsObject {

    public Integer account;
    public Double amount;
    public Double amountUsd;
    public String brand;
    public String comment;
    public String createTime;
    public String currency;
    public String regulator;
    public Integer serverId;
    public String serverName;
    public Integer ticket;
    public String ucid;
    public String uid;
    public Integer userId;

    public MtTbCreditsObject() {
    }

    public MtTbCreditsObject(Integer account, Double amount, Double amountUsd, String brand, String comment,
            String createTime, String currency, String regulator, Integer serverId, String serverName,
            Integer ticket, String ucid, String uid, Integer userId) {
        this.account = account;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.brand = brand;
        this.comment = comment;
        this.createTime = createTime;
        this.currency = currency;
        this.regulator = regulator;
        this.serverId = serverId;
        this.serverName = serverName;
        this.ticket = ticket;
        this.ucid = ucid;
        this.uid = uid;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MtTbCreditsObject that = (MtTbCreditsObject) o;
        return Objects.equals(account, that.account) && Objects.equals(amount, that.amount) && Objects.equals(amountUsd, that.amountUsd) && Objects.equals(brand, that.brand) && Objects.equals(comment, that.comment) && Objects.equals(createTime, that.createTime) && Objects.equals(currency, that.currency) && Objects.equals(regulator, that.regulator) && Objects.equals(serverId, that.serverId) && Objects.equals(serverName, that.serverName) && Objects.equals(ticket, that.ticket) && Objects.equals(ucid, that.ucid) && Objects.equals(uid, that.uid) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, amount, amountUsd, brand, comment, createTime, currency, regulator, serverId, serverName, ticket, ucid, uid, userId);
    }

    @Override
    public String toString() {
        return "MtTbCreditsObject{" + "account=" + account + ", amount=" + amount + ", amountUsd=" + amountUsd + ", brand='" + brand + '\'' + ", comment='" + comment + '\'' + ", createTime='" + createTime + '\'' + ", currency='" + currency + '\'' + ", regulator='" + regulator + '\'' + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", ticket=" + ticket + ", ucid='" + ucid + '\'' + ", uid='" + uid + '\'' + ", userId=" + userId + '}';
    }
}