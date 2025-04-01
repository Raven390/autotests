package business_objects.db.clickhouse.mt_mt4_trades;

import java.util.Objects;

public class MtMt4TradesObject {
    protected String brand;
    protected String regulator;
    protected Long userId;
    protected String ucid;
    protected Long account;
    protected String platform;
    protected Long serverId;
    protected String serverName;
    protected String accountType;
    protected String accountGroup;
    protected String accountCurrency;
    protected Long ticket;
    protected Integer cmd;
    protected Long reason;
    protected Long contractSize;
    protected String openTime;
    protected String openTimeUtc;
    protected Double openPrice;
    protected Double sl;
    protected Double tp;
    protected String symbol;
    protected String symbolUnderlying;
    protected String ticketType;
    protected String reasonName;
    protected String baseCurrency;
    protected String quoteCurrency;
    protected Double openRateUsdBase;
    protected Double openRateUsdQuote;
    protected Double openRateUsdAcc;
    protected Long volume;
    protected Double volumeLots;
    protected Double openNotionalValueUsd;
    protected Double closeNotionalValueUsd;
    protected Double profit;
    protected Double storage;
    protected Double commission;
    protected Double profitUsd;
    protected Double storageUsd;
    protected Double commissionUsd;
    protected String closeTime;
    protected String closeTimeUtc;
    protected Double closePrice;
    protected Double closeRateUsdBase;
    protected Double closeRateUsdQuote;
    protected Double closeRateUsdAcc;
    protected Double convRate1;
    protected Double convRate2;
    protected String comment;
    protected Integer isDeleted;
    protected String lastUpdated;
    protected String internalComment;

    @Override
    public String toString() {
        return "MtMt4TradesObject{" + "brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", ucid='" + ucid + '\'' + ", account=" + account + ", platform='" + platform + '\'' + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", accountType='" + accountType + '\'' + ", accountGroup='" + accountGroup + '\'' + ", accountCurrency='" + accountCurrency + '\'' + ", ticket=" + ticket + ", cmd=" + cmd + ", reason=" + reason + ", contractSize=" + contractSize + ", openTime='" + openTime + '\'' + ", openTimeUtc='" + openTimeUtc + '\'' + ", openPrice=" + openPrice + ", sl=" + sl + ", tp=" + tp + ", symbol='" + symbol + '\'' + ", symbolUnderlying='" + symbolUnderlying + '\'' + ", ticketType='" + ticketType + '\'' + ", reasonName='" + reasonName + '\'' + ", symbolUnderlying='" + symbolUnderlying + '\'' + ", baseCurrency='" + baseCurrency + '\'' + ", quoteCurrency='" + quoteCurrency + '\'' + ", openRateUsdBase=" + openRateUsdBase + ", openRateUsdQuote=" + openRateUsdQuote + ", openRateUsdAcc=" + openRateUsdAcc + ", volume=" + volume + ", volumeLots=" + volumeLots + ", openNotionalValueUsd=" + openNotionalValueUsd + ", closeNotionalValueUsd=" + closeNotionalValueUsd + ", profit=" + profit + ", storage=" + storage + ", commission=" + commission + ", profitUsd=" + profitUsd + ", storageUsd=" + storageUsd + ", commissionUsd=" + commissionUsd + ", closeTime='" + closeTime + '\'' + ", closeTimeUtc='" + closeTimeUtc + '\'' + ", closePrice=" + closePrice + ", closeRateUsdBase=" + closeRateUsdBase + ", closeRateUsdQuote=" + closeRateUsdQuote + ", closeRateUsdAcc=" + closeRateUsdAcc + ", convRate1=" + convRate1 + ", convRate2=" + convRate2 + ", comment='" + comment + '\'' + ", isDeleted=" + isDeleted + ", lastUpdated='" + lastUpdated + '\'' + ", internalComment='" + internalComment + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MtMt4TradesObject that = (MtMt4TradesObject) o;
        return Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(userId, that.userId) && Objects.equals(ucid, that.ucid) && Objects.equals(account, that.account) && Objects.equals(platform, that.platform) && Objects.equals(serverId, that.serverId) && Objects.equals(serverName, that.serverName) && Objects.equals(accountType, that.accountType) && Objects.equals(accountGroup, that.accountGroup) && Objects.equals(accountCurrency, that.accountCurrency) && Objects.equals(ticket, that.ticket) && Objects.equals(cmd, that.cmd) && Objects.equals(reason, that.reason) && Objects.equals(contractSize, that.contractSize) && Objects.equals(openTime, that.openTime) && Objects.equals(openTimeUtc, that.openTimeUtc) && Objects.equals(openPrice, that.openPrice) && Objects.equals(sl, that.sl) && Objects.equals(tp, that.tp) && Objects.equals(symbol, that.symbol) && Objects.equals(symbolUnderlying, that.symbolUnderlying) && Objects.equals(ticketType, that.ticketType) && Objects.equals(reasonName, that.reasonName) && Objects.equals(symbolUnderlying, that.symbolUnderlying) && Objects.equals(baseCurrency, that.baseCurrency) && Objects.equals(quoteCurrency, that.quoteCurrency) && Objects.equals(openRateUsdBase, that.openRateUsdBase) && Objects.equals(openRateUsdQuote, that.openRateUsdQuote) && Objects.equals(openRateUsdAcc, that.openRateUsdAcc) && Objects.equals(volume, that.volume) && Objects.equals(volumeLots, that.volumeLots) && Objects.equals(openNotionalValueUsd, that.openNotionalValueUsd) && Objects.equals(closeNotionalValueUsd, that.closeNotionalValueUsd) && Objects.equals(profit, that.profit) && Objects.equals(storage, that.storage) && Objects.equals(commission, that.commission) && Objects.equals(profitUsd, that.profitUsd) && Objects.equals(storageUsd, that.storageUsd) && Objects.equals(commissionUsd, that.commissionUsd) && Objects.equals(closeTime, that.closeTime) && Objects.equals(closeTimeUtc, that.closeTimeUtc) && Objects.equals(closePrice, that.closePrice) && Objects.equals(closeRateUsdBase, that.closeRateUsdBase) && Objects.equals(closeRateUsdQuote, that.closeRateUsdQuote) && Objects.equals(closeRateUsdAcc, that.closeRateUsdAcc) && Objects.equals(convRate1, that.convRate1) && Objects.equals(convRate2, that.convRate2) && Objects.equals(comment, that.comment) && Objects.equals(isDeleted, that.isDeleted) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(internalComment, that.internalComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, regulator, userId, ucid, account, platform, serverId, serverName, accountType, accountGroup, accountCurrency, ticket, cmd, reason, contractSize, openTime, openTimeUtc, openPrice, sl, tp, symbol, symbolUnderlying, ticketType, reasonName, symbolUnderlying, baseCurrency, quoteCurrency, openRateUsdBase, openRateUsdQuote, openRateUsdAcc, volume, volumeLots, openNotionalValueUsd, closeNotionalValueUsd, profit, storage, commission, profitUsd, storageUsd, commissionUsd, closeTime, closeTimeUtc, closePrice, closeRateUsdBase, closeRateUsdQuote, closeRateUsdAcc, convRate1, convRate2, comment, isDeleted, lastUpdated, internalComment);
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public void setAccount(Integer account) {
        this.account = Long.valueOf(String.valueOf(account));
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = Long.valueOf(serverId);
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    public Long getTicket() {
        return ticket;
    }

    public void setTicket(Long ticket) {
        this.ticket = ticket;
    }

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public Long getReason() {
        return reason;
    }

    public void setReason(Long reason) {
        this.reason = reason;
    }

    public Long getContractSize() {
        return contractSize;
    }

    public void setContractSize(Long contractSize) {
        this.contractSize = contractSize;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getOpenTimeUtc() {
        return openTimeUtc;
    }

    public void setOpenTimeUtc(String openTimeUtc) {
        this.openTimeUtc = openTimeUtc;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getSl() {
        return sl;
    }

    public void setSl(Double sl) {
        this.sl = sl;
    }

    public Double getTp() {
        return tp;
    }

    public void setTp(Double tp) {
        this.tp = tp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbolUnderlying() {
        return symbolUnderlying;
    }

    public void setSymbolUnderlying(String symbolUnderlying) {
        this.symbolUnderlying = symbolUnderlying;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public Double getOpenRateUsdBase() {
        return openRateUsdBase;
    }

    public void setOpenRateUsdBase(Double openRateUsdBase) {
        this.openRateUsdBase = openRateUsdBase;
    }

    public Double getOpenRateUsdQuote() {
        return openRateUsdQuote;
    }

    public void setOpenRateUsdQuote(Double openRateUsdQuote) {
        this.openRateUsdQuote = openRateUsdQuote;
    }

    public Double getOpenRateUsdAcc() {
        return openRateUsdAcc;
    }

    public void setOpenRateUsdAcc(Double openRateUsdAcc) {
        this.openRateUsdAcc = openRateUsdAcc;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Double getVolumeLots() {
        return volumeLots;
    }

    public void setVolumeLots(Double volumeLots) {
        this.volumeLots = volumeLots;
    }

    public Double getOpenNotionalValueUsd() {
        return openNotionalValueUsd;
    }

    public void setOpenNotionalValueUsd(Double openNotionalValueUsd) {
        this.openNotionalValueUsd = openNotionalValueUsd;
    }

    public Double getCloseNotionalValueUsd() {
        return closeNotionalValueUsd;
    }

    public void setCloseNotionalValueUsd(Double closeNotionalValueUsd) {
        this.closeNotionalValueUsd = closeNotionalValueUsd;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getStorage() {
        return storage;
    }

    public void setStorage(Double storage) {
        this.storage = storage;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getProfitUsd() {
        return profitUsd;
    }

    public void setProfitUsd(Double profitUsd) {
        this.profitUsd = profitUsd;
    }

    public Double getStorageUsd() {
        return storageUsd;
    }

    public void setStorageUsd(Double storageUsd) {
        this.storageUsd = storageUsd;
    }

    public Double getCommissionUsd() {
        return commissionUsd;
    }

    public void setCommissionUsd(Double commissionUsd) {
        this.commissionUsd = commissionUsd;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getCloseTimeUtc() {
        return closeTimeUtc;
    }

    public void setCloseTimeUtc(String closeTimeUtc) {
        this.closeTimeUtc = closeTimeUtc;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getCloseRateUsdBase() {
        return closeRateUsdBase;
    }

    public void setCloseRateUsdBase(Double closeRateUsdBase) {
        this.closeRateUsdBase = closeRateUsdBase;
    }

    public Double getCloseRateUsdQuote() {
        return closeRateUsdQuote;
    }

    public void setCloseRateUsdQuote(Double closeRateUsdQuote) {
        this.closeRateUsdQuote = closeRateUsdQuote;
    }

    public Double getCloseRateUsdAcc() {
        return closeRateUsdAcc;
    }

    public void setCloseRateUsdAcc(Double closeRateUsdAcc) {
        this.closeRateUsdAcc = closeRateUsdAcc;
    }

    public Double getConvRate1() {
        return convRate1;
    }

    public void setConvRate1(Double convRate1) {
        this.convRate1 = convRate1;
    }

    public Double getConvRate2() {
        return convRate2;
    }

    public void setConvRate2(Double convRate2) {
        this.convRate2 = convRate2;
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

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }
}
