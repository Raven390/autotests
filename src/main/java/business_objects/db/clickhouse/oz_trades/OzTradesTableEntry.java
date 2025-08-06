package businessObjects.db.clickhouse.ozTrades;


public class OzTradesTableEntry {

    private Integer serverId;
    private String serverName;
    private Integer account;
    private String ucid;
    private Double slippage;
    private Integer tradeId;
    private Integer orderId;
    private String makerTradeId;
    private String tradeTime;
    private String valueRollTimeUtc;
    private Integer makerSide;
    private Double takerRequestedVolume;
    private Double makerFilledVolume;
    private String coreSymbol;
    private Double takerRequestedPrice;
    private Double makerExecutedPrice;
    private Double takerExecutedPrice;
    private String makerSymbol;
    private String takerOrderId;
    private Integer takerMtOrderType;
    private String takerSymbol;
    private Double takerAvgExecutedPrice;
    private Integer coreOrderSide;
    private Integer coreOrderType;
    private String coreCreatedTime;
    private Integer orderResult;
    private String takerConnectionName;
    private String makerStreamName;
    private Integer tradeStatus;
    private String takerLogin;
    private String tradeDate;
    private String takerMtGroup;
    private Integer requestType;
    private Double coreVolume;
    private Double matchedVolume;
    private String makerProvider;
    private Integer requestNewId;
    private Integer takerOrderType;
    private Double takerSpread;
    private Double coreSpread;
    private Double coreTotalSpread;
    private String makerComment;
    private String makerExecutionDuration;
    private String makerValueDate;
    private Double usdAmount;
    private Integer annotationFlags;
    private Double coreMakerSpread;
    private Double requestedAppliedMinSpread;
    private Double requestedAppliedMaxSpread;
    private Double makerCcy2Amount;
    private Double takerCcy2Amount;
    private String lastModified;
    private String quoteMakerStreamName;
    private String coreComment;
    private Integer executionRejectReason;
    private Integer orderRejectReason;
    private Double baseRefConversionRatio;
    private String refCurrency;
    private Double termRefConversionRatio;
    private String termCurrency;
    private Double matchedPrice;
    private Integer systematicHedgeBucketId;
    private Integer systematicHedgeCcy2BucketId;
    private Double systematicHedgeRefCurrencyAmount;
    private String providerVolumes;
    private String timeSentToMaker;
    private String orderAnnotationFlags;
    private Integer linkedOrderId;
    private String makerAdapter;
    private String ozBaseName;

    public OzTradesTableEntry() {
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

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Double getSlippage() {
        return slippage;
    }

    public void setSlippage(Double slippage) {
        this.slippage = slippage;
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getMakerTradeId() {
        return makerTradeId;
    }

    public void setMakerTradeId(String makerTradeId) {
        this.makerTradeId = makerTradeId;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getValueRollTimeUtc() {
        return valueRollTimeUtc;
    }

    public void setValueRollTimeUtc(String valueRollTimeUtc) {
        this.valueRollTimeUtc = valueRollTimeUtc;
    }

    public Integer getMakerSide() {
        return makerSide;
    }

    public void setMakerSide(Integer makerSide) {
        this.makerSide = makerSide;
    }

    public Double getTakerRequestedVolume() {
        return takerRequestedVolume;
    }

    public void setTakerRequestedVolume(Double takerRequestedVolume) {
        this.takerRequestedVolume = takerRequestedVolume;
    }

    public Double getMakerFilledVolume() {
        return makerFilledVolume;
    }

    public void setMakerFilledVolume(Double makerFilledVolume) {
        this.makerFilledVolume = makerFilledVolume;
    }

    public String getCoreSymbol() {
        return coreSymbol;
    }

    public void setCoreSymbol(String coreSymbol) {
        this.coreSymbol = coreSymbol;
    }

    public Double getTakerRequestedPrice() {
        return takerRequestedPrice;
    }

    public void setTakerRequestedPrice(Double takerRequestedPrice) {
        this.takerRequestedPrice = takerRequestedPrice;
    }

    public Double getMakerExecutedPrice() {
        return makerExecutedPrice;
    }

    public void setMakerExecutedPrice(Double makerExecutedPrice) {
        this.makerExecutedPrice = makerExecutedPrice;
    }

    public Double getTakerExecutedPrice() {
        return takerExecutedPrice;
    }

    public void setTakerExecutedPrice(Double takerExecutedPrice) {
        this.takerExecutedPrice = takerExecutedPrice;
    }

    public String getMakerSymbol() {
        return makerSymbol;
    }

    public void setMakerSymbol(String makerSymbol) {
        this.makerSymbol = makerSymbol;
    }

    public String getTakerOrderId() {
        return takerOrderId;
    }

    public void setTakerOrderId(String takerOrderId) {
        this.takerOrderId = takerOrderId;
    }

    public Integer getTakerMtOrderType() {
        return takerMtOrderType;
    }

    public void setTakerMtOrderType(Integer takerMtOrderType) {
        this.takerMtOrderType = takerMtOrderType;
    }

    public String getTakerSymbol() {
        return takerSymbol;
    }

    public void setTakerSymbol(String takerSymbol) {
        this.takerSymbol = takerSymbol;
    }

    public Double getTakerAvgExecutedPrice() {
        return takerAvgExecutedPrice;
    }

    public void setTakerAvgExecutedPrice(Double takerAvgExecutedPrice) {
        this.takerAvgExecutedPrice = takerAvgExecutedPrice;
    }

    public Integer getCoreOrderSide() {
        return coreOrderSide;
    }

    public void setCoreOrderSide(Integer coreOrderSide) {
        this.coreOrderSide = coreOrderSide;
    }

    public Integer getCoreOrderType() {
        return coreOrderType;
    }

    public void setCoreOrderType(Integer coreOrderType) {
        this.coreOrderType = coreOrderType;
    }

    public String getCoreCreatedTime() {
        return coreCreatedTime;
    }

    public void setCoreCreatedTime(String coreCreatedTime) {
        this.coreCreatedTime = coreCreatedTime;
    }

    public Integer getOrderResult() {
        return orderResult;
    }

    public void setOrderResult(Integer orderResult) {
        this.orderResult = orderResult;
    }

    public String getTakerConnectionName() {
        return takerConnectionName;
    }

    public void setTakerConnectionName(String takerConnectionName) {
        this.takerConnectionName = takerConnectionName;
    }

    public String getMakerStreamName() {
        return makerStreamName;
    }

    public void setMakerStreamName(String makerStreamName) {
        this.makerStreamName = makerStreamName;
    }

    public Integer getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(Integer tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTakerLogin() {
        return takerLogin;
    }

    public void setTakerLogin(String takerLogin) {
        this.takerLogin = takerLogin;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTakerMtGroup() {
        return takerMtGroup;
    }

    public void setTakerMtGroup(String takerMtGroup) {
        this.takerMtGroup = takerMtGroup;
    }

    public Integer getRequestType() {
        return requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    public Double getCoreVolume() {
        return coreVolume;
    }

    public void setCoreVolume(Double coreVolume) {
        this.coreVolume = coreVolume;
    }

    public Double getMatchedVolume() {
        return matchedVolume;
    }

    public void setMatchedVolume(Double matchedVolume) {
        this.matchedVolume = matchedVolume;
    }

    public String getMakerProvider() {
        return makerProvider;
    }

    public void setMakerProvider(String makerProvider) {
        this.makerProvider = makerProvider;
    }

    public Integer getRequestNewId() {
        return requestNewId;
    }

    public void setRequestNewId(Integer requestNewId) {
        this.requestNewId = requestNewId;
    }

    public Integer getTakerOrderType() {
        return takerOrderType;
    }

    public void setTakerOrderType(Integer takerOrderType) {
        this.takerOrderType = takerOrderType;
    }

    public Double getTakerSpread() {
        return takerSpread;
    }

    public void setTakerSpread(Double takerSpread) {
        this.takerSpread = takerSpread;
    }

    public Double getCoreSpread() {
        return coreSpread;
    }

    public void setCoreSpread(Double coreSpread) {
        this.coreSpread = coreSpread;
    }

    public Double getCoreTotalSpread() {
        return coreTotalSpread;
    }

    public void setCoreTotalSpread(Double coreTotalSpread) {
        this.coreTotalSpread = coreTotalSpread;
    }

    public String getMakerComment() {
        return makerComment;
    }

    public void setMakerComment(String makerComment) {
        this.makerComment = makerComment;
    }

    public String getMakerExecutionDuration() {
        return makerExecutionDuration;
    }

    public void setMakerExecutionDuration(String makerExecutionDuration) {
        this.makerExecutionDuration = makerExecutionDuration;
    }

    public String getMakerValueDate() {
        return makerValueDate;
    }

    public void setMakerValueDate(String makerValueDate) {
        this.makerValueDate = makerValueDate;
    }

    public Double getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(Double usdAmount) {
        this.usdAmount = usdAmount;
    }

    public Integer getAnnotationFlags() {
        return annotationFlags;
    }

    public void setAnnotationFlags(Integer annotationFlags) {
        this.annotationFlags = annotationFlags;
    }

    public Double getCoreMakerSpread() {
        return coreMakerSpread;
    }

    public void setCoreMakerSpread(Double coreMakerSpread) {
        this.coreMakerSpread = coreMakerSpread;
    }

    public Double getRequestedAppliedMinSpread() {
        return requestedAppliedMinSpread;
    }

    public void setRequestedAppliedMinSpread(Double requestedAppliedMinSpread) {
        this.requestedAppliedMinSpread = requestedAppliedMinSpread;
    }

    public Double getRequestedAppliedMaxSpread() {
        return requestedAppliedMaxSpread;
    }

    public void setRequestedAppliedMaxSpread(Double requestedAppliedMaxSpread) {
        this.requestedAppliedMaxSpread = requestedAppliedMaxSpread;
    }

    public Double getMakerCcy2Amount() {
        return makerCcy2Amount;
    }

    public void setMakerCcy2Amount(Double makerCcy2Amount) {
        this.makerCcy2Amount = makerCcy2Amount;
    }

    public Double getTakerCcy2Amount() {
        return takerCcy2Amount;
    }

    public void setTakerCcy2Amount(Double takerCcy2Amount) {
        this.takerCcy2Amount = takerCcy2Amount;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getQuoteMakerStreamName() {
        return quoteMakerStreamName;
    }

    public void setQuoteMakerStreamName(String quoteMakerStreamName) {
        this.quoteMakerStreamName = quoteMakerStreamName;
    }

    public String getCoreComment() {
        return coreComment;
    }

    public void setCoreComment(String coreComment) {
        this.coreComment = coreComment;
    }

    public Integer getExecutionRejectReason() {
        return executionRejectReason;
    }

    public void setExecutionRejectReason(Integer executionRejectReason) {
        this.executionRejectReason = executionRejectReason;
    }

    public Integer getOrderRejectReason() {
        return orderRejectReason;
    }

    public void setOrderRejectReason(Integer orderRejectReason) {
        this.orderRejectReason = orderRejectReason;
    }

    public Double getBaseRefConversionRatio() {
        return baseRefConversionRatio;
    }

    public void setBaseRefConversionRatio(Double baseRefConversionRatio) {
        this.baseRefConversionRatio = baseRefConversionRatio;
    }

    public String getRefCurrency() {
        return refCurrency;
    }

    public void setRefCurrency(String refCurrency) {
        this.refCurrency = refCurrency;
    }

    public Double getTermRefConversionRatio() {
        return termRefConversionRatio;
    }

    public void setTermRefConversionRatio(Double termRefConversionRatio) {
        this.termRefConversionRatio = termRefConversionRatio;
    }

    public String getTermCurrency() {
        return termCurrency;
    }

    public void setTermCurrency(String termCurrency) {
        this.termCurrency = termCurrency;
    }

    public Double getMatchedPrice() {
        return matchedPrice;
    }

    public void setMatchedPrice(Double matchedPrice) {
        this.matchedPrice = matchedPrice;
    }

    public Integer getSystematicHedgeBucketId() {
        return systematicHedgeBucketId;
    }

    public void setSystematicHedgeBucketId(Integer systematicHedgeBucketId) {
        this.systematicHedgeBucketId = systematicHedgeBucketId;
    }

    public Integer getSystematicHedgeCcy2BucketId() {
        return systematicHedgeCcy2BucketId;
    }

    public void setSystematicHedgeCcy2BucketId(Integer systematicHedgeCcy2BucketId) {
        this.systematicHedgeCcy2BucketId = systematicHedgeCcy2BucketId;
    }

    public Double getSystematicHedgeRefCurrencyAmount() {
        return systematicHedgeRefCurrencyAmount;
    }

    public void setSystematicHedgeRefCurrencyAmount(Double systematicHedgeRefCurrencyAmount) {
        this.systematicHedgeRefCurrencyAmount = systematicHedgeRefCurrencyAmount;
    }

    public String getProviderVolumes() {
        return providerVolumes;
    }

    public void setProviderVolumes(String providerVolumes) {
        this.providerVolumes = providerVolumes;
    }

    public String getTimeSentToMaker() {
        return timeSentToMaker;
    }

    public void setTimeSentToMaker(String timeSentToMaker) {
        this.timeSentToMaker = timeSentToMaker;
    }

    public String getOrderAnnotationFlags() {
        return orderAnnotationFlags;
    }

    public void setOrderAnnotationFlags(String orderAnnotationFlags) {
        this.orderAnnotationFlags = orderAnnotationFlags;
    }

    public Integer getLinkedOrderId() {
        return linkedOrderId;
    }

    public void setLinkedOrderId(Integer linkedOrderId) {
        this.linkedOrderId = linkedOrderId;
    }

    public String getMakerAdapter() {
        return makerAdapter;
    }

    public void setMakerAdapter(String makerAdapter) {
        this.makerAdapter = makerAdapter;
    }

    public String getOzBaseName() {
        return ozBaseName;
    }

    public void setOzBaseName(String ozBaseName) {
        this.ozBaseName = ozBaseName;
    }
}
