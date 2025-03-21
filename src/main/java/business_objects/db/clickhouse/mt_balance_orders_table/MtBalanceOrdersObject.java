package business_objects.db.clickhouse.mt_balance_orders_table;


import java.util.Objects;

public class MtBalanceOrdersObject {
    public Integer ticket;
    public Integer serverId;
    public String serverName;
    public String ucid;
    public String brand;
    public String regulator;
    public Integer userId;
    public Integer account;
    public String createTime;
    public Double amount;
    public Double amountUsd;
    public String currency;
    public String comment;

    public MtBalanceOrdersObject() {
    }

    public MtBalanceOrdersObject(
            Integer ticket, Integer serverId, String serverName, String ucid, String brand,
            String regulator,
            Integer userId, Integer account, String createTime, Double amount, Double amountUsd, String currency,
            String comment) {
        this.ticket = ticket;
        this.serverId = serverId;
        this.serverName = serverName;
        this.ucid = ucid;
        this.brand = brand;
        this.regulator = regulator;
        this.userId = userId;
        this.account = account;
        this.createTime = createTime;
        this.amount = amount;
        this.amountUsd = amountUsd;
        this.currency = currency;
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MtBalanceOrdersObject that = (MtBalanceOrdersObject) o;
        return Objects.equals(ticket, that.ticket) && Objects.equals(serverId, that.serverId) && Objects.equals(
                serverName, that.serverName) && Objects.equals(ucid, that.ucid) && Objects.equals(brand, that.brand) && Objects.equals(
                        regulator, that.regulator) && Objects.equals(userId, that.userId) && Objects.equals(
                                account, that.account) && Objects.equals(createTime, that.createTime) && Objects.equals(
                                        amount, that.amount) && Objects.equals(amountUsd, that.amountUsd) && Objects.equals(
                                                currency, that.currency) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticket, serverId, serverName, ucid, brand, regulator, userId, account, createTime, amount, amountUsd, currency, comment);
    }

    @Override
    public String toString() {
        return "MtBalanceOrdersObject{" + "ticket=" + ticket + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", ucid='" + ucid + '\'' + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", account=" + account + ", createTime='" + createTime + '\'' + ", amount=" + amount + ", amountUsd=" + amountUsd + ", currency='" + currency + '\'' + ", comment='" + comment + '\'' + '}';
    }
}
