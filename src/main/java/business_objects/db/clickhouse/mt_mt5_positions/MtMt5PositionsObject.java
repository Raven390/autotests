package business_objects.db.clickhouse.mt_mt5_positions;

import java.util.Objects;

public class MtMt5PositionsObject {
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
    private Long positionId;
    private Integer action;
    private Integer reason;
    private Double contractSize;
    private String timeCreate;
    private String timeCreateUtc;
    private String timeUpdate;
    private String timeUpdateUtc;
    private String symbol;
    private String symbolUnderlying;
    private String baseCurrency;
    private String quoteCurrency;
    private Double rateUsdBase;
    private Double rateUsdQuote;
    private Double rateUsdAcc;
    private Double priceOpen;
    private Double priceCurrent;
    private Double volume;
    private Double volumeLots;
    private Double notionalValueUsd;
    private Double profit;
    private Double storage;
    private Double profitUsd;
    private Double storageUsd;
    private String comment;
    private Double sl;
    private Double tp;
    private Double expertId;
    private Double expertPositionId;
    private Integer dealer;
    private Integer rateProfit;
    private Integer rateMargin;
    private String op;
    private Integer isDeleted;
    private String lastUpdated;
    private String internalComment;

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
            Double volume, Double volumeLots, Double notionalValueUsd, Double profit, Double storage, Double profitUsd,
            Double storageUsd, String comment, Double sl, Double tp, Double expertId, Double expertPositionId,
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

    public Long getPositionId() {
        return positionId;
    }

    public void setPositionId(Long positionId) {
        this.positionId = positionId;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
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

    public String getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(String timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getTimeCreateUtc() {
        return timeCreateUtc;
    }

    public void setTimeCreateUtc(String timeCreateUtc) {
        this.timeCreateUtc = timeCreateUtc;
    }

    public String getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(String timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    public String getTimeUpdateUtc() {
        return timeUpdateUtc;
    }

    public void setTimeUpdateUtc(String timeUpdateUtc) {
        this.timeUpdateUtc = timeUpdateUtc;
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

    public Double getPriceOpen() {
        return priceOpen;
    }

    public void setPriceOpen(Double priceOpen) {
        this.priceOpen = priceOpen;
    }

    public Double getPriceCurrent() {
        return priceCurrent;
    }

    public void setPriceCurrent(Double priceCurrent) {
        this.priceCurrent = priceCurrent;
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

    public Double getStorage() {
        return storage;
    }

    public void setStorage(Double storage) {
        this.storage = storage;
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

    public Double getExpertId() {
        return expertId;
    }

    public void setExpertId(Double expertId) {
        this.expertId = expertId;
    }

    public Double getExpertPositionId() {
        return expertPositionId;
    }

    public void setExpertPositionId(Double expertPositionId) {
        this.expertPositionId = expertPositionId;
    }

    public Integer getDealer() {
        return dealer;
    }

    public void setDealer(Integer dealer) {
        this.dealer = dealer;
    }

    public Integer getRateProfit() {
        return rateProfit;
    }

    public void setRateProfit(Integer rateProfit) {
        this.rateProfit = rateProfit;
    }

    public Integer getRateMargin() {
        return rateMargin;
    }

    public void setRateMargin(Integer rateMargin) {
        this.rateMargin = rateMargin;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
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
