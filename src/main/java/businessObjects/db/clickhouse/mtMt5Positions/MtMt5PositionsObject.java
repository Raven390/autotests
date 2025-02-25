package businessObjects.db.clickhouse.mtMt5Positions;

import java.util.Objects;

public class MtMt5PositionsObject {
    public String brand;
    public String regulator;
    public Integer userId;
    public String ucid;
    public Integer account;
    public String platform;
    public Integer serverId;
    public String serverName;
    public String accountType;
    public String accountGroup;
    public String accountCurrency;
    public Long positionId;
    public Integer action;
    public Integer reason;
    public Double contractSize;
    public String timeCreate;
    public String timeCreateUtc;
    public String timeUpdate;
    public String timeUpdateUtc;
    public String symbol;
    public String symbolUnderlying;
    public String baseCurrency;
    public String quoteCurrency;
    public Double rateUsdBase;
    public Double rateUsdQuote;
    public Double rateUsdAcc;
    public Double priceOpen;
    public Double priceCurrent;
    public Double volume;
    public Double volumeLots;
    public Double notionalValueUsd;
    public Double profit;
    public Integer storage;
    public Double profitUsd;
    public Integer storageUsd;
    public String comment;
    public Double sl;
    public Double tp;
    public Double expertId;
    public Double expertPositionId;
    public Integer dealer;
    public Integer rateProfit;
    public Integer rateMargin;
    public String op;
    public Integer isDeleted;
    public String lastUpdated;
    public String internalComment;

    public MtMt5PositionsObject() {
    }

    public MtMt5PositionsObject(
            String brand, String regulator, Integer userId, String ucid, Integer account, String platform,
            Integer serverId,
            String serverName, String accountType, String accountGroup, String accountCurrency, Long positionId,
            Integer action, Integer reason, Double contractSize, String timeCreate, String timeCreateUtc,
            String timeUpdate,
            String timeUpdateUtc, String symbol, String symbolUnderlying, String baseCurrency, String quoteCurrency,
            Double rateUsdBase, Double rateUsdQuote, Double rateUsdAcc, Double priceOpen, Double priceCurrent,
            Double volume, Double volumeLots, Double notionalValueUsd, Double profit, Integer storage, Double profitUsd,
            Integer storageUsd, String comment, Double sl, Double tp, Double expertId, Double expertPositionId,
            Integer dealer, Integer rateProfit, Integer rateMargin, String op, Integer isDeleted, String lastUpdated,
            String internalComment) {
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
        this.positionId = positionId;
        this.action = action;
        this.reason = reason;
        this.contractSize = contractSize;
        this.timeCreate = timeCreate;
        this.timeCreateUtc = timeCreateUtc;
        this.timeUpdate = timeUpdate;
        this.timeUpdateUtc = timeUpdateUtc;
        this.symbol = symbol;
        this.symbolUnderlying = symbolUnderlying;
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.rateUsdBase = rateUsdBase;
        this.rateUsdQuote = rateUsdQuote;
        this.rateUsdAcc = rateUsdAcc;
        this.priceOpen = priceOpen;
        this.priceCurrent = priceCurrent;
        this.volume = volume;
        this.volumeLots = volumeLots;
        this.notionalValueUsd = notionalValueUsd;
        this.profit = profit;
        this.storage = storage;
        this.profitUsd = profitUsd;
        this.storageUsd = storageUsd;
        this.comment = comment;
        this.sl = sl;
        this.tp = tp;
        this.expertId = expertId;
        this.expertPositionId = expertPositionId;
        this.dealer = dealer;
        this.rateProfit = rateProfit;
        this.rateMargin = rateMargin;
        this.op = op;
        this.isDeleted = isDeleted;
        this.lastUpdated = lastUpdated;
        this.internalComment = internalComment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MtMt5PositionsObject that = (MtMt5PositionsObject) o;
        return Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(
                userId, that.userId) && Objects.equals(ucid, that.ucid) && Objects.equals(account, that.account) && Objects.equals(
                        platform, that.platform) && Objects.equals(serverId, that.serverId) && Objects.equals(
                                serverName, that.serverName) && Objects.equals(accountType, that.accountType) && Objects.equals(
                                        accountGroup, that.accountGroup) && Objects.equals(accountCurrency, that.accountCurrency) && Objects.equals(
                                                positionId, that.positionId) && Objects.equals(action, that.action) && Objects.equals(
                                                        reason, that.reason) && Objects.equals(contractSize, that.contractSize) && Objects.equals(timeCreate, that.timeCreate) && Objects.equals(
                                                                timeCreateUtc, that.timeCreateUtc) && Objects.equals(timeUpdate, that.timeUpdate) && Objects.equals(
                                                                        timeUpdateUtc, that.timeUpdateUtc) && Objects.equals(symbol, that.symbol) && Objects.equals(
                                                                                symbolUnderlying, that.symbolUnderlying) && Objects.equals(baseCurrency, that.baseCurrency) && Objects.equals(
                                                                                        quoteCurrency, that.quoteCurrency) && Objects.equals(rateUsdBase, that.rateUsdBase) && Objects.equals(
                                                                                                rateUsdQuote, that.rateUsdQuote) && Objects.equals(rateUsdAcc, that.rateUsdAcc) && Objects.equals(
                                                                                                        priceOpen, that.priceOpen) && Objects.equals(priceCurrent, that.priceCurrent) && Objects.equals(
                                                                                                                volume, that.volume) && Objects.equals(volumeLots, that.volumeLots) && Objects.equals(
                                                                                                                        notionalValueUsd, that.notionalValueUsd) && Objects.equals(profit, that.profit) && Objects.equals(
                                                                                                                                storage, that.storage) && Objects.equals(profitUsd, that.profitUsd) && Objects.equals(
                                                                                                                                        storageUsd, that.storageUsd) && Objects.equals(comment, that.comment) && Objects.equals(
                                                                                                                                                sl, that.sl) && Objects.equals(tp, that.tp) && Objects.equals(expertId, that.expertId) && Objects.equals(
                                                                                                                                                        expertPositionId, that.expertPositionId) && Objects.equals(dealer, that.dealer) && Objects.equals(
                                                                                                                                                                rateProfit, that.rateProfit) && Objects.equals(rateMargin, that.rateMargin) && Objects.equals(op, that.op) && Objects.equals(
                                                                                                                                                                        isDeleted, that.isDeleted) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(
                                                                                                                                                                                internalComment, that.internalComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, regulator, userId, ucid, account, platform, serverId, serverName, accountType, accountGroup, accountCurrency, positionId, action, reason, contractSize, timeCreate, timeCreateUtc, timeUpdate, timeUpdateUtc, symbol, symbolUnderlying, baseCurrency, quoteCurrency, rateUsdBase, rateUsdQuote, rateUsdAcc, priceOpen, priceCurrent, volume, volumeLots, notionalValueUsd, profit, storage, profitUsd, storageUsd, comment, sl, tp, expertId, expertPositionId, dealer, rateProfit, rateMargin, op, isDeleted, lastUpdated, internalComment);
    }

    @Override
    public String toString() {
        return "MtMt5PositionsObject{" + "brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", ucid='" + ucid + '\'' + ", account=" + account + ", platform='" + platform + '\'' + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", accountType='" + accountType + '\'' + ", accountGroup='" + accountGroup + '\'' + ", accountCurrency='" + accountCurrency + '\'' + ", positionId=" + positionId + ", action=" + action + ", reason=" + reason + ", contractSize=" + contractSize + ", timeCreate='" + timeCreate + '\'' + ", timeCreateUtc='" + timeCreateUtc + '\'' + ", timeUpdate='" + timeUpdate + '\'' + ", timeUpdateUtc='" + timeUpdateUtc + '\'' + ", symbol='" + symbol + '\'' + ", symbolUnderlying='" + symbolUnderlying + '\'' + ", baseCurrency='" + baseCurrency + '\'' + ", quoteCurrency='" + quoteCurrency + '\'' + ", rateUsdBase=" + rateUsdBase + ", rateUsdQuote=" + rateUsdQuote + ", rateUsdAcc=" + rateUsdAcc + ", priceOpen=" + priceOpen + ", priceCurrent=" + priceCurrent + ", volume=" + volume + ", volumeLots=" + volumeLots + ", notionalValueUsd=" + notionalValueUsd + ", profit=" + profit + ", storage=" + storage + ", profitUsd=" + profitUsd + ", storageUsd=" + storageUsd + ", comment='" + comment + '\'' + ", sl=" + sl + ", tp=" + tp + ", expertId=" + expertId + ", expertPositionId=" + expertPositionId + ", dealer=" + dealer + ", rateProfit=" + rateProfit + ", rateMargin=" + rateMargin + ", op='" + op + '\'' + ", isDeleted=" + isDeleted + ", lastUpdated='" + lastUpdated + '\'' + ", internalComment='" + internalComment + '\'' + '}';
    }
}
