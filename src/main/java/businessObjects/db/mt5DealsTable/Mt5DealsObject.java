package businessObjects.db.mt5DealsTable;

public class Mt5DealsObject {

    private String action;
    private String apiData;
    private String comment;
    private Double commission;
    private Double contractSize;
    private Long deal;
    private String dealer;
    private Integer digits;
    private Integer digitsCurrency;
    private String entry;
    private Long expertId;
    private String externalId;
    private Double fee;
    private Integer flags;
    private String gateway;
    private String lastUpdated;
    private Long login;
    private Double marketAsk;
    private Double marketBid;
    private Double marketLast;
    private Integer modifyFlags;
    private String op;
    private Long order;
    private Long positionId;
    private Double price;
    private Double priceGateway;
    private Double pricePosition;
    private Double priceSl;
    private Double priceTp;
    private Double profit;
    private Double profitRaw;
    private Double rateMargin;
    private Double rateProfit;
    private String reason;
    private Integer serverId;
    private Integer serverIdOld;
    private String serverName;
    private String serverNameOld;
    private String storage;
    private String symbol;
    private Double tickSize;
    private Double tickValue;
    private String time;
    private Long timeMsc;
    private String timestamp;
    private Double value;
    private Double volume;
    private Double volumeLots;
    private Double volumeClosed;
    private Double volumeClosedExt;
    private Double volumeExt;

    // Constructor with all fields
    public Mt5DealsObject(String action, String apiData, String comment, Double commission, Double contractSize,
                       Long deal, String dealer, Integer digits, Integer digitsCurrency, String entry, Long expertId,
                       String externalId, Double fee, Integer flags, String gateway, String lastUpdated, Long login,
                       Double marketAsk, Double marketBid, Double marketLast, Integer modifyFlags, String op,
                       Long order, Long positionId, Double price, Double priceGateway, Double pricePosition,
                       Double priceSl, Double priceTp, Double profit, Double profitRaw, Double rateMargin,
                       Double rateProfit, String reason, Integer serverId, Integer serverIdOld, String serverName,
                       String serverNameOld, String storage, String symbol, Double tickSize, Double tickValue,
                       String time, Long timeMsc, String timestamp, Double value, Double volume, Double volumeLots,
                       Double volumeClosed, Double volumeClosedExt, Double volumeExt) {
        this.action = action;
        this.apiData = apiData;
        this.comment = comment;
        this.commission = commission;
        this.contractSize = contractSize;
        this.deal = deal;
        this.dealer = dealer;
        this.digits = digits;
        this.digitsCurrency = digitsCurrency;
        this.entry = entry;
        this.expertId = expertId;
        this.externalId = externalId;
        this.fee = fee;
        this.flags = flags;
        this.gateway = gateway;
        this.lastUpdated = lastUpdated;
        this.login = login;
        this.marketAsk = marketAsk;
        this.marketBid = marketBid;
        this.marketLast = marketLast;
        this.modifyFlags = modifyFlags;
        this.op = op;
        this.order = order;
        this.positionId = positionId;
        this.price = price;
        this.priceGateway = priceGateway;
        this.pricePosition = pricePosition;
        this.priceSl = priceSl;
        this.priceTp = priceTp;
        this.profit = profit;
        this.profitRaw = profitRaw;
        this.rateMargin = rateMargin;
        this.rateProfit = rateProfit;
        this.reason = reason;
        this.serverId = serverId;
        this.serverIdOld = serverIdOld;
        this.serverName = serverName;
        this.serverNameOld = serverNameOld;
        this.storage = storage;
        this.symbol = symbol;
        this.tickSize = tickSize;
        this.tickValue = tickValue;
        this.time = time;
        this.timeMsc = timeMsc;
        this.timestamp = timestamp;
        this.value = value;
        this.volume = volume;
        this.volumeLots = volumeLots;
        this.volumeClosed = volumeClosed;
        this.volumeClosedExt = volumeClosedExt;
        this.volumeExt = volumeExt;
    }
}