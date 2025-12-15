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
    public String platform;
    public Integer account;
    public String currency;
    public String createTime;
    public String createTimeUtc;
    public Double amount;
    public Double rateToUsd;
    public Double amountUsd;
    public String orderType;
    public String comment;
    public Integer isDeleted;
    public String lastUpdated;
    public String internalComment;

    public Integer getTicket() {
        return ticket;
    }

    public void setTicket(Integer ticket) {
        this.ticket = ticket;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeUtc() {
        return createTimeUtc;
    }

    public void setCreateTimeUtc(String createTimeUtc) {
        this.createTimeUtc = createTimeUtc;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRateToUsd() {
        return rateToUsd;
    }

    public void setRateToUsd(Double rateToUsd) {
        this.rateToUsd = rateToUsd;
    }

    public Double getAmountUsd() {
        return amountUsd;
    }

    public void setAmountUsd(Double amountUsd) {
        this.amountUsd = amountUsd;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getInternalComment() {
        return internalComment;
    }

    public void setInternalComment(String internalComment) {
        this.internalComment = internalComment;
    }

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
        return Objects.equals(ticket, that.ticket) && Objects.equals(serverId, that.serverId) && Objects.equals(serverName, that.serverName) && Objects.equals(ucid, that.ucid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(userId, that.userId) && Objects.equals(platform, that.platform) && Objects.equals(account, that.account) && Objects.equals(currency, that.currency) && Objects.equals(createTime, that.createTime) && Objects.equals(createTimeUtc, that.createTimeUtc) && Objects.equals(amount, that.amount) && Objects.equals(rateToUsd, that.rateToUsd) && Objects.equals(amountUsd, that.amountUsd) && Objects.equals(orderType, that.orderType) && Objects.equals(comment, that.comment) && Objects.equals(isDeleted, that.isDeleted) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(internalComment, that.internalComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticket, serverId, serverName, ucid, brand, regulator, userId, platform, account, currency, createTime, createTimeUtc, amount, rateToUsd, amountUsd, orderType, comment, isDeleted, lastUpdated, internalComment);
    }

    @Override
    public String toString() {
        return "MtBalanceOrdersObject{" + "ticket=" + ticket + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", ucid='" + ucid + '\'' + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", platform='" + platform + '\'' + ", account=" + account + ", currency='" + currency + '\'' + ", createTime='" + createTime + '\'' + ", createTimeUtc='" + createTimeUtc + '\'' + ", amount=" + amount + ", rateToUsd=" + rateToUsd + ", amountUsd=" + amountUsd + ", orderType='" + orderType + '\'' + ", comment='" + comment + '\'' + ", isDeleted=" + isDeleted + ", lastUpdated='" + lastUpdated + '\'' + ", internalComment='" + internalComment + '\'' + '}';
    }
}
