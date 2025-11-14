package business_objects.db.clickhouse.mt_tb_credits;


import java.util.Objects;

public class MtTbCreditsObject {

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setAmountUsd(Double amountUsd) {
        this.amountUsd = amountUsd;
    }

    public Integer account;
    public Double amount;
    public Double amountUsd;
    public String brand;
    public String comment;
    public String internalComment;
    public String createTime;
    public String currency;
    public String regulator;
    public Integer serverId;
    public String serverName;
    public Integer ticket;
    public String ucid;
    //public String uid;
    public Integer userId;
    public String createTimeUtc;
    public String lastUpdated;

    public MtTbCreditsObject() {
    }

    public MtTbCreditsObject(Integer account, Double amount, Double amountUsd, String brand, String comment,
            String createTime, String currency, String regulator, Integer serverId, String serverName,
            Integer ticket, String ucid, String uid, Integer userId, String internalComment, String createTimeUtc,
            String lastUpdated) {
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
        //this.uid = uid;
        this.userId = userId;
        this.internalComment = internalComment;
        this.createTimeUtc = createTimeUtc;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MtTbCreditsObject that = (MtTbCreditsObject) o;
        return Objects.equals(account, that.account) && Objects.equals(amount, that.amount) && Objects.equals(
                amountUsd, that.amountUsd) && Objects.equals(brand, that.brand) && Objects.equals(
                        comment, that.comment) && Objects.equals(internalComment, that.internalComment) && Objects.equals(
                                createTime, that.createTime) && Objects.equals(currency, that.currency) && Objects.equals(
                                        regulator, that.regulator) && Objects.equals(serverId, that.serverId) && Objects.equals(
                                                serverName, that.serverName) && Objects.equals(ticket, that.ticket) && Objects.equals(
                                                        ucid, that.ucid) && Objects.equals(userId, that.userId) && Objects.equals(createTimeUtc, that.createTimeUtc) && Objects.equals(
                                                                lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, amount, amountUsd, brand, comment, internalComment, createTime, currency, regulator, serverId, serverName, ticket, ucid, userId, createTimeUtc, lastUpdated);
    }

    @Override
    public String toString() {
        return "MtTbCreditsObject{" + "account=" + account + ", amount=" + amount + ", amountUsd=" + amountUsd + ", brand='" + brand + '\'' + ", comment='" + comment + '\'' + ", internalComment='" + internalComment + '\'' + ", createTime='" + createTime + '\'' + ", currency='" + currency + '\'' + ", regulator='" + regulator + '\'' + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", ticket=" + ticket + ", ucid='" + ucid + '\'' + ", userId=" + userId + ", createTimeUtc='" + createTimeUtc + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}