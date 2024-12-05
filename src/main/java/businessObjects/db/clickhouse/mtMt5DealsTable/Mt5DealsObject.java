package businessObjects.db.clickhouse.mtMt5DealsTable;

import java.util.Objects;

public class Mt5DealsObject {

    public Integer action;
    public String apidata;
    public String comment;
    public Double commission;
    public Double contractsize;
    public Integer deal;
    public Integer dealer;
    public Integer digits;
    public Integer digitscurrency;
    public Integer entry;
    public Long expertid;
    public String externalid;
    public Double fee;
    public Integer flags;
    public String gateway;
    public String lastUpdated;
    public Integer login;
    public Double marketask;
    public Double marketbid;
    public Double marketlast;
    public Integer modifyflags;
    public String op;
    public Long order;
    public Long positionid;
    public Double price;
    public Double pricegateway;
    public Double priceposition;
    public Double pricesl;
    public Double pricetp;
    public Double profit;
    public Double profitraw;
    public Double ratemargin;
    public Double rateprofit;
    public String reason;
    public Integer serverId;
    public Integer serverIdOld;
    public String serverName;
    public String serverNameOld;
    public Integer storage;
    public String symbol;
    public Double ticksize;
    public Double tickvalue;
    public String time;
    public String timemsc;
    public Long timestamp;
    public Double value;
    public Double volume;
    public Double volumeLots;
    public Double volumeclosed;
    public Double volumeclosedext;
    public Double volumeext;

    public Mt5DealsObject() {
    }

    public Mt5DealsObject(Integer action, String apidata, String comment, Double commission, Double contractsize,
            Integer deal, Integer dealer, Integer digits, Integer digitscurrency, Integer entry, Long expertid,
            String externalid, Double fee, Integer flags, String gateway, String lastUpdated, Integer login,
            Double marketask, Double marketbid, Double marketlast, Integer modifyflags, String op,
            Long order, Long positionid, Double price, Double pricegateway, Double priceposition,
            Double pricesl, Double pricetp, Double profit, Double profitraw, Double ratemargin,
            Double rateprofit, String reason, Integer serverId, Integer serverIdOld, String serverName,
            String serverNameOld, Integer storage, String symbol, Double ticksize, Double tickvalue,
            String time, String timemsc, Long timestamp, Double value, Double volume, Double volumeLots,
            Double volumeclosed, Double volumeclosedext, Double volumeext) {
        this.action = action;
        this.apidata = apidata;
        this.comment = comment;
        this.commission = commission;
        this.contractsize = contractsize;
        this.deal = deal;
        this.dealer = dealer;
        this.digits = digits;
        this.digitscurrency = digitscurrency;
        this.entry = entry;
        this.expertid = expertid;
        this.externalid = externalid;
        this.fee = fee;
        this.flags = flags;
        this.gateway = gateway;
        this.lastUpdated = lastUpdated;
        this.login = login;
        this.marketask = marketask;
        this.marketbid = marketbid;
        this.marketlast = marketlast;
        this.modifyflags = modifyflags;
        this.op = op;
        this.order = order;
        this.positionid = positionid;
        this.price = price;
        this.pricegateway = pricegateway;
        this.priceposition = priceposition;
        this.pricesl = pricesl;
        this.pricetp = pricetp;
        this.profit = profit;
        this.profitraw = profitraw;
        this.ratemargin = ratemargin;
        this.rateprofit = rateprofit;
        this.reason = reason;
        this.serverId = serverId;
        this.serverIdOld = serverIdOld;
        this.serverName = serverName;
        this.serverNameOld = serverNameOld;
        this.storage = storage;
        this.symbol = symbol;
        this.ticksize = ticksize;
        this.tickvalue = tickvalue;
        this.time = time;
        this.timemsc = timemsc;
        this.timestamp = timestamp;
        this.value = value;
        this.volume = volume;
        this.volumeLots = volumeLots;
        this.volumeclosed = volumeclosed;
        this.volumeclosedext = volumeclosedext;
        this.volumeext = volumeext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mt5DealsObject that = (Mt5DealsObject) o;
        return Objects.equals(action, that.action) && Objects.equals(apidata, that.apidata) && Objects.equals(comment, that.comment) && Objects.equals(commission, that.commission) && Objects.equals(contractsize, that.contractsize) && Objects.equals(deal, that.deal) && Objects.equals(dealer, that.dealer) && Objects.equals(digits, that.digits) && Objects.equals(digitscurrency, that.digitscurrency) && Objects.equals(entry, that.entry) && Objects.equals(expertid, that.expertid) && Objects.equals(externalid, that.externalid) && Objects.equals(fee, that.fee) && Objects.equals(flags, that.flags) && Objects.equals(gateway, that.gateway) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(login, that.login) && Objects.equals(marketask, that.marketask) && Objects.equals(marketbid, that.marketbid) && Objects.equals(marketlast, that.marketlast) && Objects.equals(modifyflags, that.modifyflags) && Objects.equals(op, that.op) && Objects.equals(order, that.order) && Objects.equals(positionid, that.positionid) && Objects.equals(price, that.price) && Objects.equals(pricegateway, that.pricegateway) && Objects.equals(priceposition, that.priceposition) && Objects.equals(pricesl, that.pricesl) && Objects.equals(pricetp, that.pricetp) && Objects.equals(profit, that.profit) && Objects.equals(profitraw, that.profitraw) && Objects.equals(ratemargin, that.ratemargin) && Objects.equals(rateprofit, that.rateprofit) && Objects.equals(reason, that.reason) && Objects.equals(serverId, that.serverId) && Objects.equals(serverIdOld, that.serverIdOld) && Objects.equals(serverName, that.serverName) && Objects.equals(serverNameOld, that.serverNameOld) && Objects.equals(storage, that.storage) && Objects.equals(symbol, that.symbol) && Objects.equals(ticksize, that.ticksize) && Objects.equals(tickvalue, that.tickvalue) && Objects.equals(time, that.time) && Objects.equals(timemsc, that.timemsc) && Objects.equals(timestamp, that.timestamp) && Objects.equals(value, that.value) && Objects.equals(volume, that.volume) && Objects.equals(volumeLots, that.volumeLots) && Objects.equals(volumeclosed, that.volumeclosed) && Objects.equals(volumeclosedext, that.volumeclosedext) && Objects.equals(volumeext, that.volumeext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, apidata, comment, commission, contractsize, deal, dealer, digits, digitscurrency, entry, expertid, externalid, fee, flags, gateway, lastUpdated, login, marketask, marketbid, marketlast, modifyflags, op, order, positionid, price, pricegateway, priceposition, pricesl, pricetp, profit, profitraw, ratemargin, rateprofit, reason, serverId, serverIdOld, serverName, serverNameOld, storage, symbol, ticksize, tickvalue, time, timemsc, timestamp, value, volume, volumeLots, volumeclosed, volumeclosedext, volumeext);
    }

    @Override
    public String toString() {
        return "Mt5DealsObject{" + "action='" + action + '\'' + ", apiData='" + apidata + '\'' + ", comment='" + comment + '\'' + ", commission=" + commission + ", contractSize=" + contractsize + ", deal=" + deal + ", dealer='" + dealer + '\'' + ", digits=" + digits + ", digitsCurrency=" + digitscurrency + ", entry='" + entry + '\'' + ", expertId=" + expertid + ", externalId='" + externalid + '\'' + ", fee=" + fee + ", flags=" + flags + ", gateway='" + gateway + '\'' + ", lastUpdated='" + lastUpdated + '\'' + ", login=" + login + ", marketAsk=" + marketask + ", marketBid=" + marketbid + ", marketLast=" + marketlast + ", modifyFlags=" + modifyflags + ", op='" + op + '\'' + ", order=" + order + ", positionId=" + positionid + ", price=" + price + ", priceGateway=" + pricegateway + ", pricePosition=" + priceposition + ", priceSl=" + pricesl + ", priceTp=" + pricetp + ", profit=" + profit + ", profitRaw=" + profitraw + ", rateMargin=" + ratemargin + ", rateProfit=" + rateprofit + ", reason='" + reason + '\'' + ", serverId=" + serverId + ", serverIdOld=" + serverIdOld + ", serverName='" + serverName + '\'' + ", serverNameOld='" + serverNameOld + '\'' + ", storage='" + storage + '\'' + ", symbol='" + symbol + '\'' + ", tickSize=" + ticksize + ", tickValue=" + tickvalue + ", time='" + time + '\'' + ", timeMsc=" + timemsc + ", timestamp='" + timestamp + '\'' + ", value=" + value + ", volume=" + volume + ", volumeLots=" + volumeLots + ", volumeClosed=" + volumeclosed + ", volumeClosedExt=" + volumeclosedext + ", volumeExt=" + volumeext + '}';
    }
}