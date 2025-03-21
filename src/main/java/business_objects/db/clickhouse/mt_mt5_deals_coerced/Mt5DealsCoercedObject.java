package business_objects.db.clickhouse.mt_mt5_deals_coerced;

import java.util.Objects;

public class Mt5DealsCoercedObject {

    private String brand;
    private String regulator;
    private Integer userId;
    private String ucid;
    private Integer account;
    private String platform;
    private Integer serverId;
    private String serverName;
    private String accountType;
    private String accountGroup;
    private String accountCurrency;
    private Long deal;
    private Long order;
    private Integer action;
    private Integer entry;
    private Integer reason;
    private Double contractSize;
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
    private Double volume;
    private Double volumeLots;
    private Double notionalValueUsd;
    private Double profit;
    private Integer storage;
    private Double commission;
    private Double profitUsd;
    private Integer storageUsd;
    private Double commissionUsd;
    private Long expertId;
    private Long positionId;
    private String comment;
    private Double sl;
    private Double tp;
    private Double priceGateway;
    private Double marketBid;
    private Double marketAsk;
    private Double rateProfit;
    private Integer isDeleted;
    private String lastUpdated;
    private String internalComment;

    public Mt5DealsCoercedObject() {
    }

    public Mt5DealsCoercedObject(
            String brand, String regulator, Integer userId, String ucid, Integer account, String platform,
            Integer serverId,
            String serverName, String accountType, String accountGroup, String accountCurrency, Long deal,
            Long order,
            Integer action, Integer entry, Integer reason, Double contractSize, String time, String timeUtc,
            String symbol,
            String symbolUnderlying, String baseCurrency, String quoteCurrency, Double rateUsdBase, Double rateUsdQuote,
            Double rateUsdAcc, Double price, Double volume, Double volumeLots, Double notionalValueUsd, Double profit,
            Integer storage, Double commission, Double profitUsd, Integer storageUsd, Double commissionUsd,
            Long expertId,
            Long positionId, String comment, Double sl, Double tp, Double priceGateway, Double marketBid,
            Double marketAsk,
            Double rateProfit, Integer isDeleted, String lastUpdated, String internalComment) {
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
        this.isDeleted = isDeleted;
        this.lastUpdated = lastUpdated;
        this.internalComment = internalComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mt5DealsCoercedObject that = (Mt5DealsCoercedObject) o;
        return Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(
                userId, that.userId) && Objects.equals(ucid, that.ucid) && Objects.equals(account, that.account) && Objects.equals(
                        platform, that.platform) && Objects.equals(serverId, that.serverId) && Objects.equals(
                                serverName, that.serverName) && Objects.equals(accountType, that.accountType) && Objects.equals(
                                        accountGroup, that.accountGroup) && Objects.equals(accountCurrency, that.accountCurrency) && Objects.equals(
                                                deal, that.deal) && Objects.equals(order, that.order) && Objects.equals(action, that.action) && Objects.equals(
                                                        entry, that.entry) && Objects.equals(reason, that.reason) && Objects.equals(
                                                                contractSize, that.contractSize) && Objects.equals(time, that.time) && Objects.equals(
                                                                        timeUtc, that.timeUtc) && Objects.equals(symbol, that.symbol) && Objects.equals(
                                                                                symbolUnderlying, that.symbolUnderlying) && Objects.equals(baseCurrency, that.baseCurrency) && Objects.equals(
                                                                                        quoteCurrency, that.quoteCurrency) && Objects.equals(rateUsdBase, that.rateUsdBase) && Objects.equals(
                                                                                                rateUsdQuote, that.rateUsdQuote) && Objects.equals(rateUsdAcc, that.rateUsdAcc) && Objects.equals(
                                                                                                        price, that.price) && Objects.equals(volume, that.volume) && Objects.equals(volumeLots, that.volumeLots) && Objects.equals(
                                                                                                                notionalValueUsd, that.notionalValueUsd) && Objects.equals(profit, that.profit) && Objects.equals(
                                                                                                                        storage, that.storage) && Objects.equals(commission, that.commission) && Objects.equals(
                                                                                                                                profitUsd, that.profitUsd) && Objects.equals(storageUsd, that.storageUsd) && Objects.equals(
                                                                                                                                        commissionUsd, that.commissionUsd) && Objects.equals(expertId, that.expertId) && Objects.equals(
                                                                                                                                                positionId, that.positionId) && Objects.equals(comment, that.comment) && Objects.equals(
                                                                                                                                                        sl, that.sl) && Objects.equals(tp, that.tp) && Objects.equals(priceGateway, that.priceGateway) && Objects.equals(
                                                                                                                                                                marketBid, that.marketBid) && Objects.equals(marketAsk, that.marketAsk) && Objects.equals(
                                                                                                                                                                        rateProfit, that.rateProfit) && Objects.equals(isDeleted, that.isDeleted) && Objects.equals(
                                                                                                                                                                                lastUpdated, that.lastUpdated) && Objects.equals(internalComment, that.internalComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, regulator, userId, ucid, account, platform, serverId, serverName, accountType, accountGroup, accountCurrency, deal, order, action, entry, reason, contractSize, time, timeUtc, symbol, symbolUnderlying, baseCurrency, quoteCurrency, rateUsdBase, rateUsdQuote, rateUsdAcc, price, volume, volumeLots, notionalValueUsd, profit, storage, commission, profitUsd, storageUsd, commissionUsd, expertId, positionId, comment, sl, tp, priceGateway, marketBid, marketAsk, rateProfit, isDeleted, lastUpdated, internalComment);
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

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
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

    public Double getContractSize() {
        return contractSize;
    }

    public void setContractSize(Double contractSize) {
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

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
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

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
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

    public Integer getStorageUsd() {
        return storageUsd;
    }

    public void setStorageUsd(Integer storageUsd) {
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