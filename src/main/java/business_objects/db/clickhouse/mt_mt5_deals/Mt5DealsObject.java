package business_objects.db.clickhouse.mt_mt5_deals;

import java.util.Objects;

public class Mt5DealsObject {

    private String brand;
    private String regulator;
    private Long userId;
    private String ucid;
    private Long account;
    private String platform;
    private Integer serverId;
    private String serverName;
    private String accountType;
    private String accountGroup;
    private String accountCurrency;

    private Long deal;
    private Long dealer;
    private Long order;

    private Integer action;
    private Integer entry;
    private Integer reason;

    private Long contractSize;

    private String time;
    private String timeUtc;

    private String symbol;
    private String symbolUnderlying;
    private String baseCurrency;
    private String quoteCurrency;

    private Double rateUsdBase;
    private Double rateUsdQuote;
    private Double rateUsdAcc;

    private Double price;
    private Long volume;
    private Double volumeLots;

    private Double notionalValueUsd;
    private Double profit;
    private Double storage;
    private Double commission;

    private Double profitUsd;
    private Double storageUsd;
    private Double commissionUsd;

    private Long expertId;
    private Long positionId;
    private String comment;

    private Double sl;
    private Double tp;

    private Long volumeClosed;

    private Double priceGateway;
    private Double marketBid;
    private Double marketAsk;
    private Double rateProfit;

    private Integer isDeleted;
    private String lastUpdated;
    private String internalComment;

    public Mt5DealsObject() {}

    @Override
    public String toString() {
        return "Mt5DealsObject{" + "brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId
                + ", ucid='" + ucid + '\'' + ", account=" + account + ", platform='" + platform + '\'' + ", serverId="
                + serverId + ", serverName='" + serverName + '\'' + ", accountType='" + accountType + '\''
                + ", accountGroup='" + accountGroup + '\'' + ", accountCurrency='" + accountCurrency + '\'' + ", deal="
                + deal + ", dealer=" + dealer + ", order=" + order + ", action=" + action + ", entry=" + entry
                + ", reason=" + reason + ", contractSize=" + contractSize + ", time='" + time + '\'' + ", timeUtc='"
                + timeUtc + '\'' + ", symbol='" + symbol + '\'' + ", symbolUnderlying='" + symbolUnderlying + '\''
                + ", baseCurrency='" + baseCurrency + '\'' + ", quoteCurrency='" + quoteCurrency + '\''
                + ", rateUsdBase=" + rateUsdBase + ", rateUsdQuote=" + rateUsdQuote + ", rateUsdAcc=" + rateUsdAcc
                + ", price=" + price + ", volume=" + volume + ", volumeLots=" + volumeLots + ", notionalValueUsd="
                + notionalValueUsd + ", profit=" + profit + ", storage=" + storage + ", commission=" + commission
                + ", profitUsd=" + profitUsd + ", storageUsd=" + storageUsd + ", commissionUsd=" + commissionUsd
                + ", expertId=" + expertId + ", positionId=" + positionId + ", comment='" + comment + '\'' + ", sl="
                + sl + ", tp=" + tp + ", volumeClosed=" + volumeClosed + ", priceGateway=" + priceGateway
                + ", marketBid=" + marketBid + ", marketAsk=" + marketAsk + ", rateProfit=" + rateProfit
                + ", isDeleted=" + isDeleted + ", lastUpdated='" + lastUpdated + '\'' + ", internalComment='"
                + internalComment + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Mt5DealsObject that = (Mt5DealsObject) o;
        return Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(userId, that.userId)
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(account, that.account)
                && Objects.equals(platform, that.platform)
                && Objects.equals(serverId, that.serverId)
                && Objects.equals(serverName, that.serverName)
                && Objects.equals(accountType, that.accountType)
                && Objects.equals(accountGroup, that.accountGroup)
                && Objects.equals(accountCurrency, that.accountCurrency)
                && Objects.equals(deal, that.deal)
                && Objects.equals(dealer, that.dealer)
                && Objects.equals(order, that.order)
                && Objects.equals(action, that.action)
                && Objects.equals(entry, that.entry)
                && Objects.equals(reason, that.reason)
                && Objects.equals(contractSize, that.contractSize)
                && Objects.equals(time, that.time)
                && Objects.equals(timeUtc, that.timeUtc)
                && Objects.equals(symbol, that.symbol)
                && Objects.equals(symbolUnderlying, that.symbolUnderlying)
                && Objects.equals(baseCurrency, that.baseCurrency)
                && Objects.equals(quoteCurrency, that.quoteCurrency)
                && Objects.equals(rateUsdBase, that.rateUsdBase)
                && Objects.equals(rateUsdQuote, that.rateUsdQuote)
                && Objects.equals(rateUsdAcc, that.rateUsdAcc)
                && Objects.equals(price, that.price)
                && Objects.equals(volume, that.volume)
                && Objects.equals(volumeLots, that.volumeLots)
                && Objects.equals(notionalValueUsd, that.notionalValueUsd)
                && Objects.equals(profit, that.profit)
                && Objects.equals(storage, that.storage)
                && Objects.equals(commission, that.commission)
                && Objects.equals(profitUsd, that.profitUsd)
                && Objects.equals(storageUsd, that.storageUsd)
                && Objects.equals(commissionUsd, that.commissionUsd)
                && Objects.equals(expertId, that.expertId)
                && Objects.equals(positionId, that.positionId)
                && Objects.equals(comment, that.comment)
                && Objects.equals(sl, that.sl)
                && Objects.equals(tp, that.tp)
                && Objects.equals(volumeClosed, that.volumeClosed)
                && Objects.equals(priceGateway, that.priceGateway)
                && Objects.equals(marketBid, that.marketBid)
                && Objects.equals(marketAsk, that.marketAsk)
                && Objects.equals(rateProfit, that.rateProfit)
                && Objects.equals(isDeleted, that.isDeleted)
                && Objects.equals(lastUpdated, that.lastUpdated)
                && Objects.equals(internalComment, that.internalComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                brand,
                regulator,
                userId,
                ucid,
                account,
                platform,
                serverId,
                serverName,
                accountType,
                accountGroup,
                accountCurrency,
                deal,
                dealer,
                order,
                action,
                entry,
                reason,
                contractSize,
                time,
                timeUtc,
                symbol,
                symbolUnderlying,
                baseCurrency,
                quoteCurrency,
                rateUsdBase,
                rateUsdQuote,
                rateUsdAcc,
                price,
                volume,
                volumeLots,
                notionalValueUsd,
                profit,
                storage,
                commission,
                profitUsd,
                storageUsd,
                commissionUsd,
                expertId,
                positionId,
                comment,
                sl,
                tp,
                volumeClosed,
                priceGateway,
                marketBid,
                marketAsk,
                rateProfit,
                isDeleted,
                lastUpdated,
                internalComment);
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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    public Long getDeal() {
        return deal;
    }

    public void setDeal(Long deal) {
        this.deal = deal;
    }

    public Long getDealer() {
        return dealer;
    }

    public void setDealer(Long dealer) {
        this.dealer = dealer;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getEntry() {
        return entry;
    }

    public void setEntry(Integer entry) {
        this.entry = entry;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public Long getContractSize() {
        return contractSize;
    }

    public void setContractSize(Long contractSize) {
        this.contractSize = contractSize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeUtc() {
        return timeUtc;
    }

    public void setTimeUtc(String timeUtc) {
        this.timeUtc = timeUtc;
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

    public Double getRateUsdBase() {
        return rateUsdBase;
    }

    public void setRateUsdBase(Double rateUsdBase) {
        this.rateUsdBase = rateUsdBase;
    }

    public Double getRateUsdQuote() {
        return rateUsdQuote;
    }

    public void setRateUsdQuote(Double rateUsdQuote) {
        this.rateUsdQuote = rateUsdQuote;
    }

    public Double getRateUsdAcc() {
        return rateUsdAcc;
    }

    public void setRateUsdAcc(Double rateUsdAcc) {
        this.rateUsdAcc = rateUsdAcc;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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

    public Double getNotionalValueUsd() {
        return notionalValueUsd;
    }

    public void setNotionalValueUsd(Double notionalValueUsd) {
        this.notionalValueUsd = notionalValueUsd;
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

    public Long getExpertId() {
        return expertId;
    }

    public void setExpertId(Long expertId) {
        this.expertId = expertId;
    }

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Long getVolumeClosed() {
        return volumeClosed;
    }

    public void setVolumeClosed(Long volumeClosed) {
        this.volumeClosed = volumeClosed;
    }

    public Double getPriceGateway() {
        return priceGateway;
    }

    public void setPriceGateway(Double priceGateway) {
        this.priceGateway = priceGateway;
    }

    public Double getMarketBid() {
        return marketBid;
    }

    public void setMarketBid(Double marketBid) {
        this.marketBid = marketBid;
    }

    public Double getMarketAsk() {
        return marketAsk;
    }

    public void setMarketAsk(Double marketAsk) {
        this.marketAsk = marketAsk;
    }

    public Double getRateProfit() {
        return rateProfit;
    }

    public void setRateProfit(Double rateProfit) {
        this.rateProfit = rateProfit;
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
}
