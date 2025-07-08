package business_objects.kafka.mt_events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class TradeEvent {

    @JsonProperty("tradeId")
    public long tradeId;

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("id")
    public String id;

    @JsonProperty("serverId")
    public int serverId;

    @JsonProperty("tradingAccount")
    public long tradingAccount;

    @JsonProperty("volume")
    public double volume;

    @JsonProperty("openTime")
    public String openTime;

    @JsonProperty("openTimeUtc")
    public String openTimeUtc;

    @JsonProperty("closeTime")
    public String closeTime;

    @JsonProperty("closeTimeUtc")
    public String closeTimeUtc;

    @JsonProperty("equity")
    public double equity;

    @JsonProperty("balance")
    public double balance;

    @JsonProperty("leverage")
    public double leverage;

    @JsonProperty("margin")
    public double margin;

    @JsonProperty("freeMargin")
    public double freeMargin;

    @JsonProperty("type")
    public String type;

    @JsonProperty("eventDate")
    public String eventDate;

    @JsonProperty("metadata")
    public TradeEventMetadata metadata;

    @JsonProperty("initialEventTime")
    public String initialEventTime;

    public long getTradeId() {
        return tradeId;
    }

    public void setTradeId(long tradeId) {
        this.tradeId = tradeId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public long getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(long tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getCloseTimeUtc() {
        return closeTimeUtc;
    }

    public void setCloseTimeUtc(String closeTimeUtc) {
        this.closeTimeUtc = closeTimeUtc;
    }

    public double getEquity() {
        return equity;
    }

    public void setEquity(double equity) {
        this.equity = equity;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getLeverage() {
        return leverage;
    }

    public void setLeverage(double leverage) {
        this.leverage = leverage;
    }

    public double getMargin() {
        return margin;
    }

    public void setMargin(double margin) {
        this.margin = margin;
    }

    public double getFreeMargin() {
        return freeMargin;
    }

    public void setFreeMargin(double freeMargin) {
        this.freeMargin = freeMargin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public TradeEventMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(TradeEventMetadata metadata) {
        this.metadata = metadata;
    }

    public String getInitialEventTime() {
        return initialEventTime;
    }

    public void setInitialEventTime(String initialEventTime) {
        this.initialEventTime = initialEventTime;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TradeEvent that)) return false;
        return tradeId == that.tradeId && serverId == that.serverId && tradingAccount == that.tradingAccount && Double.compare(
                volume, that.volume) == 0 && Double.compare(equity, that.equity) == 0 && Double.compare(
                        balance, that.balance) == 0 && Double.compare(leverage, that.leverage) == 0 && Double.compare(
                                margin, that.margin) == 0 && Double.compare(freeMargin, that.freeMargin) == 0 && Objects.equals(
                                        symbol, that.symbol) && Objects.equals(id, that.id) && Objects.equals(closeTime, that.closeTime) && Objects.equals(
                                                closeTimeUtc, that.closeTimeUtc) && Objects.equals(type, that.type) && Objects.equals(
                                                        eventDate, that.eventDate) && Objects.equals(metadata, that.metadata) && Objects.equals(
                                                                initialEventTime, that.initialEventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, symbol, id, serverId, tradingAccount, volume, closeTime, closeTimeUtc, equity, balance, leverage, margin, freeMargin, type, eventDate, metadata, initialEventTime);
    }

    @Override
    public String toString() {
        return "CloseTradeMtEvent_NEW{" + "tradeId=" + tradeId + ", symbol='" + symbol + '\'' + ", id='" + id + '\'' + ", serverId=" + serverId + ", tradingAccount=" + tradingAccount + ", volume=" + volume + ", closeTime='" + closeTime + '\'' + ", closeTimeUtc='" + closeTimeUtc + '\'' + ", equity=" + equity + ", balance=" + balance + ", leverage=" + leverage + ", margin=" + margin + ", freeMargin=" + freeMargin + ", type='" + type + '\'' + ", eventDate='" + eventDate + '\'' + ", metadata=" + metadata + ", initialEventTime='" + initialEventTime + '\'' + '}';
    }
}