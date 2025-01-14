package businessObjects.db.clickhouse.mtMt4TradesCoerced;

import java.util.Objects;

public class MtMt4TradesCoercedObject {

    public String brand;
    public String regulator;
    public Long userId;
    public String ucid;
    public Long account;
    public String platform;
    public Long serverId;
    public String serverName;
    public String accountType;
    public String accountGroup;
    public String accountCurrency;
    public Long ticket;
    public Long cmd;
    public String ticketType;
    public Long reason;
    public String reasonName;
    public Long contractSize;
    public String openTime;
    public String openTimeUtc;
    public Double openPrice;
    public Double stopLoss;
    public Double takeProfit;
    public String symbol;
    public String symbolUnderlying;
    public String baseCurrency;
    public String quoteCurrency;
    public Double openRateUsdBase;
    public Double openRateUsdQuote;
    public Double openRateUsdAcc;
    public Double closeRateUsdBase;
    public Double closeRateUsdQuote;
    public Double closeRateUsdAcc;
    public Long volume;
    public Double volumeLots;
    public Double notionalValueUsd;
    public Double openNotionalValueUsd;
    public Double closeNotionalValueUsd;
    public Double profit;
    public Double storage;
    public Double commission;
    public Double profitUsd;
    public Double storageUsd;
    public Double commissionUsd;
    public String closeTime;
    public String closeTimeUtc;
    public Double closePrice;
    public Long positionId;
    public String comment;
    public Double spreadRevenueUsd;
    public Double taxesUsd;
    public Double feeUsd;
    public Long isDeleted;
    public String lastUpdated;
    public String internalComment;

    public MtMt4TradesCoercedObject() {
    }

    public MtMt4TradesCoercedObject(String brand, String regulator, Long userId, String ucid, Long account,
            String platform, Long serverId, String serverName, String accountType, String accountGroup,
            String accountCurrency, Long ticket, Long cmd, String ticketType, Long reason, String reasonName,
            Long contractSize, String openTime, String openTimeUtc, Double openPrice, Double stopLoss,
            Double takeProfit, String symbol, String symbolUnderlying, String baseCurrency, String quoteCurrency,
            Double openRateUsdBase, Double openRateUsdQuote, Double openRateUsdAcc, Double closeRateUsdBase,
            Double closeRateUsdQuote, Double closeRateUsdAcc, Long volume, Double volumeLots, Double notionalValueUsd,
            Double openNotionalValueUsd, Double closeNotionalValueUsd, Double profit, Double storage, Double commission,
            Double profitUsd, Double storageUsd, Double commissionUsd, String closeTime, String closeTimeUtc,
            Double closePrice, Long positionId, String comment, Double spreadRevenueUsd, Double taxesUsd, Double feeUsd,
            Long isDeleted, String lastUpdated, String internalComment) {
        this.brand = brand;
        this.regulator = regulator;
        this.userId = userId;
        this.ucid = ucid;
        this.account = account;
        this.platform = platform;
        this.serverId = serverId;
        this.serverName = serverName;
        this.accountType = accountType;
        this.accountGroup = accountGroup;
        this.accountCurrency = accountCurrency;
        this.ticket = ticket;
        this.cmd = cmd;
        this.ticketType = ticketType;
        this.reason = reason;
        this.reasonName = reasonName;
        this.contractSize = contractSize;
        this.openTime = openTime;
        this.openTimeUtc = openTimeUtc;
        this.openPrice = openPrice;
        this.stopLoss = stopLoss;
        this.takeProfit = takeProfit;
        this.symbol = symbol;
        this.symbolUnderlying = symbolUnderlying;
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.openRateUsdBase = openRateUsdBase;
        this.openRateUsdQuote = openRateUsdQuote;
        this.openRateUsdAcc = openRateUsdAcc;
        this.closeRateUsdBase = closeRateUsdBase;
        this.closeRateUsdQuote = closeRateUsdQuote;
        this.closeRateUsdAcc = closeRateUsdAcc;
        this.volume = volume;
        this.volumeLots = volumeLots;
        this.notionalValueUsd = notionalValueUsd;
        this.openNotionalValueUsd = openNotionalValueUsd;
        this.closeNotionalValueUsd = closeNotionalValueUsd;
        this.profit = profit;
        this.storage = storage;
        this.commission = commission;
        this.profitUsd = profitUsd;
        this.storageUsd = storageUsd;
        this.commissionUsd = commissionUsd;
        this.closeTime = closeTime;
        this.closeTimeUtc = closeTimeUtc;
        this.closePrice = closePrice;
        this.positionId = positionId;
        this.comment = comment;
        this.spreadRevenueUsd = spreadRevenueUsd;
        this.taxesUsd = taxesUsd;
        this.feeUsd = feeUsd;
        this.isDeleted = isDeleted;
        this.lastUpdated = lastUpdated;
        this.internalComment = internalComment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MtMt4TradesCoercedObject that = (MtMt4TradesCoercedObject) o;
        return Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(userId, that.userId) && Objects.equals(ucid, that.ucid) && Objects.equals(account, that.account) && Objects.equals(platform, that.platform) && Objects.equals(serverId, that.serverId) && Objects.equals(serverName, that.serverName) && Objects.equals(accountType, that.accountType) && Objects.equals(accountGroup, that.accountGroup) && Objects.equals(accountCurrency, that.accountCurrency) && Objects.equals(ticket, that.ticket) && Objects.equals(cmd, that.cmd) && Objects.equals(ticketType, that.ticketType) && Objects.equals(reason, that.reason) && Objects.equals(reasonName, that.reasonName) && Objects.equals(contractSize, that.contractSize) && Objects.equals(openTime, that.openTime) && Objects.equals(openTimeUtc, that.openTimeUtc) && Objects.equals(openPrice, that.openPrice) && Objects.equals(stopLoss, that.stopLoss) && Objects.equals(takeProfit, that.takeProfit) && Objects.equals(symbol, that.symbol) && Objects.equals(symbolUnderlying, that.symbolUnderlying) && Objects.equals(baseCurrency, that.baseCurrency) && Objects.equals(quoteCurrency, that.quoteCurrency) && Objects.equals(openRateUsdBase, that.openRateUsdBase) && Objects.equals(openRateUsdQuote, that.openRateUsdQuote) && Objects.equals(openRateUsdAcc, that.openRateUsdAcc) && Objects.equals(closeRateUsdBase, that.closeRateUsdBase) && Objects.equals(closeRateUsdQuote, that.closeRateUsdQuote) && Objects.equals(closeRateUsdAcc, that.closeRateUsdAcc) && Objects.equals(volume, that.volume) && Objects.equals(volumeLots, that.volumeLots) && Objects.equals(notionalValueUsd, that.notionalValueUsd) && Objects.equals(openNotionalValueUsd, that.openNotionalValueUsd) && Objects.equals(closeNotionalValueUsd, that.closeNotionalValueUsd) && Objects.equals(profit, that.profit) && Objects.equals(storage, that.storage) && Objects.equals(commission, that.commission) && Objects.equals(profitUsd, that.profitUsd) && Objects.equals(storageUsd, that.storageUsd) && Objects.equals(commissionUsd, that.commissionUsd) && Objects.equals(closeTime, that.closeTime) && Objects.equals(closeTimeUtc, that.closeTimeUtc) && Objects.equals(closePrice, that.closePrice) && Objects.equals(positionId, that.positionId) && Objects.equals(comment, that.comment) && Objects.equals(spreadRevenueUsd, that.spreadRevenueUsd) && Objects.equals(taxesUsd, that.taxesUsd) && Objects.equals(feeUsd, that.feeUsd) && Objects.equals(isDeleted, that.isDeleted) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(internalComment, that.internalComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, regulator, userId, ucid, account, platform, serverId, serverName, accountType, accountGroup, accountCurrency, ticket, cmd, ticketType, reason, reasonName, contractSize, openTime, openTimeUtc, openPrice, stopLoss, takeProfit, symbol, symbolUnderlying, baseCurrency, quoteCurrency, openRateUsdBase, openRateUsdQuote, openRateUsdAcc, closeRateUsdBase, closeRateUsdQuote, closeRateUsdAcc, volume, volumeLots, notionalValueUsd, openNotionalValueUsd, closeNotionalValueUsd, profit, storage, commission, profitUsd, storageUsd, commissionUsd, closeTime, closeTimeUtc, closePrice, positionId, comment, spreadRevenueUsd, taxesUsd, feeUsd, isDeleted, lastUpdated, internalComment);
    }

    @Override
    public String toString() {
        return "MtMt4TradesCoercedObject{" + "brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", ucid='" + ucid + '\'' + ", account=" + account + ", platform='" + platform + '\'' + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", accountType='" + accountType + '\'' + ", accountGroup='" + accountGroup + '\'' + ", accountCurrency='" + accountCurrency + '\'' + ", ticket=" + ticket + ", cmd=" + cmd + ", ticketType='" + ticketType + '\'' + ", reason=" + reason + ", reasonName='" + reasonName + '\'' + ", contractSize=" + contractSize + ", openTime='" + openTime + '\'' + ", openTimeUtc='" + openTimeUtc + '\'' + ", openPrice=" + openPrice + ", stopLoss=" + stopLoss + ", takeProfit=" + takeProfit + ", symbol='" + symbol + '\'' + ", symbolUnderlying='" + symbolUnderlying + '\'' + ", baseCurrency='" + baseCurrency + '\'' + ", quoteCurrency='" + quoteCurrency + '\'' + ", openRateUsdBase=" + openRateUsdBase + ", openRateUsdQuote=" + openRateUsdQuote + ", openRateUsdAcc=" + openRateUsdAcc + ", closeRateUsdBase=" + closeRateUsdBase + ", closeRateUsdQuote=" + closeRateUsdQuote + ", closeRateUsdAcc=" + closeRateUsdAcc + ", volume=" + volume + ", volumeLots=" + volumeLots + ", notionalValueUsd=" + notionalValueUsd + ", openNotionalValueUsd=" + openNotionalValueUsd + ", closeNotionalValueUsd=" + closeNotionalValueUsd + ", profit=" + profit + ", storage=" + storage + ", commission=" + commission + ", profitUsd=" + profitUsd + ", storageUsd=" + storageUsd + ", commissionUsd=" + commissionUsd + ", closeTime='" + closeTime + '\'' + ", closeTimeUtc='" + closeTimeUtc + '\'' + ", closePrice=" + closePrice + ", positionId=" + positionId + ", comment='" + comment + '\'' + ", spreadRevenueUsd=" + spreadRevenueUsd + ", taxesUsd=" + taxesUsd + ", feeUsd=" + feeUsd + ", isDeleted=" + isDeleted + ", lastUpdated='" + lastUpdated + '\'' + ", internalComment='" + internalComment + '\'' + '}';
    }
}
