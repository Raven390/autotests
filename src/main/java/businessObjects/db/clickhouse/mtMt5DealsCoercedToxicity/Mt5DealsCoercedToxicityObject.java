package businessObjects.db.clickhouse.mtMt5DealsCoercedToxicity;

import java.util.Objects;

public class Mt5DealsCoercedToxicityObject {

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
    public Integer deal;
    public Long order;
    public Integer action;
    public Integer entry;
    public Integer reason;
    public Double contractSize;
    public String time;
    public String timeUtc;
    public String symbol;
    public String symbolUnderlying;
    public String baseCurrency;
    public String quoteCurrency;
    public Double rateUsdBase;
    public Double rateUsdQuote;
    public Double rateUsdAcc;
    public Double price;
    public Double volume;
    public Double volumeLots;
    public Double notionalValueUsd;
    public Double profit;
    public Integer storage;
    public Double commission;
    public Double profitUsd;
    public Integer storageUsd;
    public Double commissionUsd;
    public Long expertId;
    public Long positionId;
    public String comment;
    public Double sl;
    public Double tp;
    public Double priceGateway;
    public Double marketBid;
    public Double marketAsk;
    public Double rateProfit;
    public Double toxicityUsd;
    public Integer isDeleted;
    public String lastUpdated;
    public String internalComment;

    public Mt5DealsCoercedToxicityObject() {
    }

    public Mt5DealsCoercedToxicityObject(String brand, String regulator, Integer userId, String ucid, Integer account,
            String platform, Integer serverId, String serverName, String accountType, String accountGroup,
            String accountCurrency, Integer deal, Long order, Integer action, Integer entry, Integer reason,
            Double contractSize, String time, String timeUtc, String symbol, String symbolUnderlying,
            String baseCurrency, String quoteCurrency, Double rateUsdBase, Double rateUsdQuote, Double rateUsdAcc,
            Double price, Double volume, Double volumeLots, Double notionalValueUsd, Double profit, Integer storage,
            Double commission, Double profitUsd, Integer storageUsd, Double commissionUsd, Long expertId,
            Long positionId, String comment, Double sl, Double tp, Double priceGateway, Double marketBid,
            Double marketAsk, Double rateProfit, Double toxicityUsd, Integer isDeleted,
            String lastUpdated, String internalComment) {
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
        this.deal = deal;
        this.order = order;
        this.action = action;
        this.entry = entry;
        this.reason = reason;
        this.contractSize = contractSize;
        this.time = time;
        this.timeUtc = timeUtc;
        this.symbol = symbol;
        this.symbolUnderlying = symbolUnderlying;
        this.baseCurrency = baseCurrency;
        this.quoteCurrency = quoteCurrency;
        this.rateUsdBase = rateUsdBase;
        this.rateUsdQuote = rateUsdQuote;
        this.rateUsdAcc = rateUsdAcc;
        this.price = price;
        this.volume = volume;
        this.volumeLots = volumeLots;
        this.notionalValueUsd = notionalValueUsd;
        this.profit = profit;
        this.storage = storage;
        this.commission = commission;
        this.profitUsd = profitUsd;
        this.storageUsd = storageUsd;
        this.commissionUsd = commissionUsd;
        this.expertId = expertId;
        this.positionId = positionId;
        this.comment = comment;
        this.sl = sl;
        this.tp = tp;
        this.priceGateway = priceGateway;
        this.marketBid = marketBid;
        this.marketAsk = marketAsk;
        this.rateProfit = rateProfit;
        this.toxicityUsd = toxicityUsd;
        this.isDeleted = isDeleted;
        this.lastUpdated = lastUpdated;
        this.internalComment = internalComment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Mt5DealsCoercedToxicityObject that = (Mt5DealsCoercedToxicityObject) o;
        return Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(userId, that.userId) && Objects.equals(ucid, that.ucid) && Objects.equals(account, that.account) && Objects.equals(platform, that.platform) && Objects.equals(serverId, that.serverId) && Objects.equals(serverName, that.serverName) && Objects.equals(accountType, that.accountType) && Objects.equals(accountGroup, that.accountGroup) && Objects.equals(accountCurrency, that.accountCurrency) && Objects.equals(deal, that.deal) && Objects.equals(order, that.order) && Objects.equals(action, that.action) && Objects.equals(entry, that.entry) && Objects.equals(reason, that.reason) && Objects.equals(contractSize, that.contractSize) && Objects.equals(time, that.time) && Objects.equals(timeUtc, that.timeUtc) && Objects.equals(symbol, that.symbol) && Objects.equals(symbolUnderlying, that.symbolUnderlying) && Objects.equals(baseCurrency, that.baseCurrency) && Objects.equals(quoteCurrency, that.quoteCurrency) && Objects.equals(rateUsdBase, that.rateUsdBase) && Objects.equals(rateUsdQuote, that.rateUsdQuote) && Objects.equals(rateUsdAcc, that.rateUsdAcc) && Objects.equals(price, that.price) && Objects.equals(volume, that.volume) && Objects.equals(volumeLots, that.volumeLots) && Objects.equals(notionalValueUsd, that.notionalValueUsd) && Objects.equals(profit, that.profit) && Objects.equals(storage, that.storage) && Objects.equals(commission, that.commission) && Objects.equals(profitUsd, that.profitUsd) && Objects.equals(storageUsd, that.storageUsd) && Objects.equals(commissionUsd, that.commissionUsd) && Objects.equals(expertId, that.expertId) && Objects.equals(positionId, that.positionId) && Objects.equals(comment, that.comment) && Objects.equals(sl, that.sl) && Objects.equals(tp, that.tp) && Objects.equals(priceGateway, that.priceGateway) && Objects.equals(marketBid, that.marketBid) && Objects.equals(marketAsk, that.marketAsk) && Objects.equals(rateProfit, that.rateProfit) && Objects.equals(toxicityUsd, that.toxicityUsd) && Objects.equals(isDeleted, that.isDeleted) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(internalComment, that.internalComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, regulator, userId, ucid, account, platform, serverId, serverName, accountType, accountGroup, accountCurrency, deal, order, action, entry, reason, contractSize, time, timeUtc, symbol, symbolUnderlying, baseCurrency, quoteCurrency, rateUsdBase, rateUsdQuote, rateUsdAcc, price, volume, volumeLots, notionalValueUsd, profit, storage, commission, profitUsd, storageUsd, commissionUsd, expertId, positionId, comment, sl, tp, priceGateway, marketBid, marketAsk, rateProfit, toxicityUsd, isDeleted, lastUpdated, internalComment);
    }

    @Override
    public String toString() {
        return "Mt5DealsCoercedToxicityObject{" + "brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", ucid='" + ucid + '\'' + ", account=" + account + ", platform='" + platform + '\'' + ", serverId=" + serverId + ", serverName='" + serverName + '\'' + ", accountType='" + accountType + '\'' + ", accountGroup='" + accountGroup + '\'' + ", accountCurrency='" + accountCurrency + '\'' + ", deal=" + deal + ", order=" + order + ", action=" + action + ", entry=" + entry + ", reason=" + reason + ", contractSize=" + contractSize + ", time='" + time + '\'' + ", timeUtc='" + timeUtc + '\'' + ", symbol='" + symbol + '\'' + ", symbolUnderlying='" + symbolUnderlying + '\'' + ", baseCurrency='" + baseCurrency + '\'' + ", quoteCurrency='" + quoteCurrency + '\'' + ", rateUsdBase=" + rateUsdBase + ", rateUsdQuote=" + rateUsdQuote + ", rateUsdAcc=" + rateUsdAcc + ", price=" + price + ", volume=" + volume + ", volumeLots=" + volumeLots + ", notionalValueUsd=" + notionalValueUsd + ", profit=" + profit + ", storage=" + storage + ", commission=" + commission + ", profitUsd=" + profitUsd + ", storageUsd=" + storageUsd + ", commissionUsd=" + commissionUsd + ", expertId=" + expertId + ", positionId=" + positionId + ", comment='" + comment + '\'' + ", sl=" + sl + ", tp=" + tp + ", priceGateway=" + priceGateway + ", marketBid=" + marketBid + ", marketAsk=" + marketAsk + ", rateProfit=" + rateProfit + ", toxicityUsd=" + toxicityUsd + ", isDeleted=" + isDeleted + ", lastUpdated='" + lastUpdated + '\'' + ", internalComment='" + internalComment + '\'' + '}';
    }
}