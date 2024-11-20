package businessObjects.db.clickhouse.mtMt5DealsTable;

import java.util.Objects;

public class Mt5DealsObject {

    public String action;
    public String apiData;
    public String comment;
    public Double commission;
    public Double contractSize;
    public Long deal;
    public String dealer;
    public Integer digits;
    public Integer digitsCurrency;
    public String entry;
    public Long expertId;
    public String externalId;
    public Double fee;
    public Integer flags;
    public String gateway;
    public String lastUpdated;
    public Long login;
    public Double marketAsk;
    public Double marketBid;
    public Double marketLast;
    public Integer modifyFlags;
    public String op;
    public Long order;
    public Long positionId;
    public Double price;
    public Double priceGateway;
    public Double pricePosition;
    public Double priceSl;
    public Double priceTp;
    public Double profit;
    public Double profitRaw;
    public Double rateMargin;
    public Double rateProfit;
    public String reason;
    public Integer serverId;
    public Integer serverIdOld;
    public String serverName;
    public String serverNameOld;
    public String storage;
    public String symbol;
    public Double tickSize;
    public Double tickValue;
    public String time;
    public Long timeMsc;
    public String timestamp;
    public Double value;
    public Double volume;
    public Double volumeLots;
    public Double volumeClosed;
    public Double volumeClosedExt;
    public Double volumeExt;

    public Mt5DealsObject() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mt5DealsObject that = (Mt5DealsObject) o;
        return Objects.equals(action, that.action) && Objects.equals(apiData, that.apiData) && Objects.equals(comment, that.comment) && Objects.equals(commission, that.commission) && Objects.equals(contractSize, that.contractSize) && Objects.equals(deal, that.deal) && Objects.equals(dealer, that.dealer) && Objects.equals(digits, that.digits) && Objects.equals(digitsCurrency, that.digitsCurrency) && Objects.equals(entry, that.entry) && Objects.equals(expertId, that.expertId) && Objects.equals(externalId, that.externalId) && Objects.equals(fee, that.fee) && Objects.equals(flags, that.flags) && Objects.equals(gateway, that.gateway) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(login, that.login) && Objects.equals(marketAsk, that.marketAsk) && Objects.equals(marketBid, that.marketBid) && Objects.equals(marketLast, that.marketLast) && Objects.equals(modifyFlags, that.modifyFlags) && Objects.equals(op, that.op) && Objects.equals(order, that.order) && Objects.equals(positionId, that.positionId) && Objects.equals(price, that.price) && Objects.equals(priceGateway, that.priceGateway) && Objects.equals(pricePosition, that.pricePosition) && Objects.equals(priceSl, that.priceSl) && Objects.equals(priceTp, that.priceTp) && Objects.equals(profit, that.profit) && Objects.equals(profitRaw, that.profitRaw) && Objects.equals(rateMargin, that.rateMargin) && Objects.equals(rateProfit, that.rateProfit) && Objects.equals(reason, that.reason) && Objects.equals(serverId, that.serverId) && Objects.equals(serverIdOld, that.serverIdOld) && Objects.equals(serverName, that.serverName) && Objects.equals(serverNameOld, that.serverNameOld) && Objects.equals(storage, that.storage) && Objects.equals(symbol, that.symbol) && Objects.equals(tickSize, that.tickSize) && Objects.equals(tickValue, that.tickValue) && Objects.equals(time, that.time) && Objects.equals(timeMsc, that.timeMsc) && Objects.equals(timestamp, that.timestamp) && Objects.equals(value, that.value) && Objects.equals(volume, that.volume) && Objects.equals(volumeLots, that.volumeLots) && Objects.equals(volumeClosed, that.volumeClosed) && Objects.equals(volumeClosedExt, that.volumeClosedExt) && Objects.equals(volumeExt, that.volumeExt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, apiData, comment, commission, contractSize, deal, dealer, digits, digitsCurrency, entry, expertId, externalId, fee, flags, gateway, lastUpdated, login, marketAsk, marketBid, marketLast, modifyFlags, op, order, positionId, price, priceGateway, pricePosition, priceSl, priceTp, profit, profitRaw, rateMargin, rateProfit, reason, serverId, serverIdOld, serverName, serverNameOld, storage, symbol, tickSize, tickValue, time, timeMsc, timestamp, value, volume, volumeLots, volumeClosed, volumeClosedExt, volumeExt);
    }

    @Override
    public String toString() {
        return "Mt5DealsObject{" +
                "action='" + action + '\'' +
                ", apiData='" + apiData + '\'' +
                ", comment='" + comment + '\'' +
                ", commission=" + commission +
                ", contractSize=" + contractSize +
                ", deal=" + deal +
                ", dealer='" + dealer + '\'' +
                ", digits=" + digits +
                ", digitsCurrency=" + digitsCurrency +
                ", entry='" + entry + '\'' +
                ", expertId=" + expertId +
                ", externalId='" + externalId + '\'' +
                ", fee=" + fee +
                ", flags=" + flags +
                ", gateway='" + gateway + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", login=" + login +
                ", marketAsk=" + marketAsk +
                ", marketBid=" + marketBid +
                ", marketLast=" + marketLast +
                ", modifyFlags=" + modifyFlags +
                ", op='" + op + '\'' +
                ", order=" + order +
                ", positionId=" + positionId +
                ", price=" + price +
                ", priceGateway=" + priceGateway +
                ", pricePosition=" + pricePosition +
                ", priceSl=" + priceSl +
                ", priceTp=" + priceTp +
                ", profit=" + profit +
                ", profitRaw=" + profitRaw +
                ", rateMargin=" + rateMargin +
                ", rateProfit=" + rateProfit +
                ", reason='" + reason + '\'' +
                ", serverId=" + serverId +
                ", serverIdOld=" + serverIdOld +
                ", serverName='" + serverName + '\'' +
                ", serverNameOld='" + serverNameOld + '\'' +
                ", storage='" + storage + '\'' +
                ", symbol='" + symbol + '\'' +
                ", tickSize=" + tickSize +
                ", tickValue=" + tickValue +
                ", time='" + time + '\'' +
                ", timeMsc=" + timeMsc +
                ", timestamp='" + timestamp + '\'' +
                ", value=" + value +
                ", volume=" + volume +
                ", volumeLots=" + volumeLots +
                ", volumeClosed=" + volumeClosed +
                ", volumeClosedExt=" + volumeClosedExt +
                ", volumeExt=" + volumeExt +
                '}';
    }
}