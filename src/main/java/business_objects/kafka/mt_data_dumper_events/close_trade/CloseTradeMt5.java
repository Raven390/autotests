package business_objects.kafka.mt_data_dumper_events.close_trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class CloseTradeMt5 {

    @JsonProperty("Header")
    public Header header;

    @JsonProperty("Payload")
    public Payload payload;

    public CloseTradeMt5(Header header, Payload payload) {
        this.header = header;
        this.payload = payload;
    }

    public static class Header {
        @JsonProperty("MsgId")
        public String msgId;

        @JsonProperty("MsgType")
        public String msgType;

        @JsonProperty("Operation")
        public int operation;

        @JsonProperty("ServerId")
        public int serverId;

        @JsonProperty("Timestamp")
        public long timestamp;

        public Header(String msgId, String msgType, int operation, int serverId, long timestamp) {
            this.msgId = msgId;
            this.msgType = msgType;
            this.operation = operation;
            this.serverId = serverId;
            this.timestamp = timestamp;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
            this.msgType = msgType;
        }

        public int getOperation() {
            return operation;
        }

        public void setOperation(int operation) {
            this.operation = operation;
        }

        public int getServerId() {
            return serverId;
        }

        public void setServerId(int serverId) {
            this.serverId = serverId;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Header header)) return false;
            return operation == header.operation && serverId == header.serverId && timestamp == header.timestamp && Objects.equals(
                    msgId, header.msgId) && Objects.equals(msgType, header.msgType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(msgId, msgType, operation, serverId, timestamp);
        }

        @Override
        public String toString() {
            return "Header{" + "msgId='" + msgId + '\'' + ", msgType='" + msgType + '\'' + ", operation=" + operation + ", serverId=" + serverId + ", timestamp=" + timestamp + '}';
        }
    }

    public static class Payload {
        @JsonProperty("AccountRate")
        public double accountRate;

        @JsonProperty("Action")
        public int action;

        @JsonProperty("Balance")
        public double balance;

        @JsonProperty("Comment")
        public String comment;

        @JsonProperty("Commission")
        public double commission;

        @JsonProperty("ContractSize")
        public double contractSize;

        @JsonProperty("Deal")
        public long deal;

        @JsonProperty("Dealer")
        public long dealer;

        @JsonProperty("Digits")
        public int digits;

        @JsonProperty("DigitsCurrency")
        public int digitsCurrency;

        @JsonProperty("Entry")
        public int entry;

        @JsonProperty("Equity")
        public double equity;

        @JsonProperty("ExpertID")
        public long expertId;

        @JsonProperty("ExternalID")
        public String externalId;

        @JsonProperty("Fee")
        public double fee;

        @JsonProperty("Flags")
        public int flags;

        @JsonProperty("FreeMargin")
        public double freeMargin;

        @JsonProperty("Gateway")
        public String gateway;

        @JsonProperty("Leverage")
        public int leverage;

        @JsonProperty("Login")
        public long login;

        @JsonProperty("Margin")
        public double margin;

        @JsonProperty("MarketAsk")
        public double marketAsk;

        @JsonProperty("MarketBid")
        public double marketBid;

        @JsonProperty("MarketLast")
        public double marketLast;

        @JsonProperty("ModificationFlags")
        public int modificationFlags;

        @JsonProperty("Order")
        public long order;

        @JsonProperty("PositionID")
        public long positionId;

        @JsonProperty("Price")
        public double price;

        @JsonProperty("PriceGateway")
        public double priceGateway;

        @JsonProperty("PricePosition")
        public double pricePosition;

        @JsonProperty("PriceSL")
        public double priceSL;

        @JsonProperty("PriceTP")
        public double priceTP;

        @JsonProperty("Profit")
        public double profit;

        @JsonProperty("ProfitRaw")
        public double profitRaw;

        @JsonProperty("RateMargin")
        public double rateMargin;

        @JsonProperty("RateProfit")
        public double rateProfit;

        @JsonProperty("RawApiData")
        public List<RawApiData> rawApiData;

        @JsonProperty("Reason")
        public int reason;

        @JsonProperty("Storage")
        public double storage;

        @JsonProperty("Symbol")
        public String symbol;

        @JsonProperty("TickSize")
        public double tickSize;

        @JsonProperty("TickValue")
        public double tickValue;

        @JsonProperty("Time")
        public long time;

        @JsonProperty("TimeUtc")
        public long timeUtc;

        @JsonProperty("Value")
        public double value;

        @JsonProperty("Volume")
        public long volume;

        @JsonProperty("VolumeClosed")
        public long volumeClosed;

        @JsonProperty("VolumeClosedExt")
        public long volumeClosedExt;

        @JsonProperty("VolumeExt")
        public long volumeExt;

        public Payload(
                double accountRate, int action, double balance, String comment, double commission, double contractSize,
                long deal, long dealer, int digits, int digitsCurrency, int entry, double equity, long expertId,
                String externalId, double fee, int flags, double freeMargin, String gateway, int leverage, long login,
                double margin, double marketAsk, double marketBid, double marketLast, int modificationFlags, long order,
                long positionId, double price, double priceGateway, double pricePosition, double priceSL,
                double priceTP,
                double profit, double profitRaw, double rateMargin, double rateProfit, List<RawApiData> rawApiData,
                int reason,
                double storage, String symbol, double tickSize, double tickValue, long time, long timeUtc, double value,
                long volume, long volumeClosed, long volumeClosedExt, long volumeExt) {
            this.accountRate = accountRate;
            this.action = action;
            this.balance = balance;
            this.comment = comment;
            this.commission = commission;
            this.contractSize = contractSize;
            this.deal = deal;
            this.dealer = dealer;
            this.digits = digits;
            this.digitsCurrency = digitsCurrency;
            this.entry = entry;
            this.equity = equity;
            this.expertId = expertId;
            this.externalId = externalId;
            this.fee = fee;
            this.flags = flags;
            this.freeMargin = freeMargin;
            this.gateway = gateway;
            this.leverage = leverage;
            this.login = login;
            this.margin = margin;
            this.marketAsk = marketAsk;
            this.marketBid = marketBid;
            this.marketLast = marketLast;
            this.modificationFlags = modificationFlags;
            this.order = order;
            this.positionId = positionId;
            this.price = price;
            this.priceGateway = priceGateway;
            this.pricePosition = pricePosition;
            this.priceSL = priceSL;
            this.priceTP = priceTP;
            this.profit = profit;
            this.profitRaw = profitRaw;
            this.rateMargin = rateMargin;
            this.rateProfit = rateProfit;
            this.rawApiData = rawApiData;
            this.reason = reason;
            this.storage = storage;
            this.symbol = symbol;
            this.tickSize = tickSize;
            this.tickValue = tickValue;
            this.time = time;
            this.timeUtc = timeUtc;
            this.value = value;
            this.volume = volume;
            this.volumeClosed = volumeClosed;
            this.volumeClosedExt = volumeClosedExt;
            this.volumeExt = volumeExt;
        }

        public double getAccountRate() {
            return accountRate;
        }

        public void setAccountRate(double accountRate) {
            this.accountRate = accountRate;
        }

        public int getAction() {
            return action;
        }

        public void setAction(int action) {
            this.action = action;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public double getCommission() {
            return commission;
        }

        public void setCommission(double commission) {
            this.commission = commission;
        }

        public double getContractSize() {
            return contractSize;
        }

        public void setContractSize(double contractSize) {
            this.contractSize = contractSize;
        }

        public long getDeal() {
            return deal;
        }

        public void setDeal(long deal) {
            this.deal = deal;
        }

        public long getDealer() {
            return dealer;
        }

        public void setDealer(long dealer) {
            this.dealer = dealer;
        }

        public int getDigits() {
            return digits;
        }

        public void setDigits(int digits) {
            this.digits = digits;
        }

        public int getDigitsCurrency() {
            return digitsCurrency;
        }

        public void setDigitsCurrency(int digitsCurrency) {
            this.digitsCurrency = digitsCurrency;
        }

        public int getEntry() {
            return entry;
        }

        public void setEntry(int entry) {
            this.entry = entry;
        }

        public double getEquity() {
            return equity;
        }

        public void setEquity(double equity) {
            this.equity = equity;
        }

        public long getExpertId() {
            return expertId;
        }

        public void setExpertId(long expertId) {
            this.expertId = expertId;
        }

        public String getExternalId() {
            return externalId;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public int getFlags() {
            return flags;
        }

        public void setFlags(int flags) {
            this.flags = flags;
        }

        public double getFreeMargin() {
            return freeMargin;
        }

        public void setFreeMargin(double freeMargin) {
            this.freeMargin = freeMargin;
        }

        public String getGateway() {
            return gateway;
        }

        public void setGateway(String gateway) {
            this.gateway = gateway;
        }

        public int getLeverage() {
            return leverage;
        }

        public void setLeverage(int leverage) {
            this.leverage = leverage;
        }

        public long getLogin() {
            return login;
        }

        public void setLogin(long login) {
            this.login = login;
        }

        public double getMargin() {
            return margin;
        }

        public void setMargin(double margin) {
            this.margin = margin;
        }

        public double getMarketAsk() {
            return marketAsk;
        }

        public void setMarketAsk(double marketAsk) {
            this.marketAsk = marketAsk;
        }

        public double getMarketBid() {
            return marketBid;
        }

        public void setMarketBid(double marketBid) {
            this.marketBid = marketBid;
        }

        public double getMarketLast() {
            return marketLast;
        }

        public void setMarketLast(double marketLast) {
            this.marketLast = marketLast;
        }

        public int getModificationFlags() {
            return modificationFlags;
        }

        public void setModificationFlags(int modificationFlags) {
            this.modificationFlags = modificationFlags;
        }

        public long getOrder() {
            return order;
        }

        public void setOrder(long order) {
            this.order = order;
        }

        public long getPositionId() {
            return positionId;
        }

        public void setPositionId(long positionId) {
            this.positionId = positionId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getPriceGateway() {
            return priceGateway;
        }

        public void setPriceGateway(double priceGateway) {
            this.priceGateway = priceGateway;
        }

        public double getPricePosition() {
            return pricePosition;
        }

        public void setPricePosition(double pricePosition) {
            this.pricePosition = pricePosition;
        }

        public double getPriceSL() {
            return priceSL;
        }

        public void setPriceSL(double priceSL) {
            this.priceSL = priceSL;
        }

        public double getPriceTP() {
            return priceTP;
        }

        public void setPriceTP(double priceTP) {
            this.priceTP = priceTP;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public double getProfitRaw() {
            return profitRaw;
        }

        public void setProfitRaw(double profitRaw) {
            this.profitRaw = profitRaw;
        }

        public double getRateMargin() {
            return rateMargin;
        }

        public void setRateMargin(double rateMargin) {
            this.rateMargin = rateMargin;
        }

        public double getRateProfit() {
            return rateProfit;
        }

        public void setRateProfit(double rateProfit) {
            this.rateProfit = rateProfit;
        }

        public List<RawApiData> getRawApiData() {
            return rawApiData;
        }

        public void setRawApiData(
                List<RawApiData> rawApiData) {
            this.rawApiData = rawApiData;
        }

        public int getReason() {
            return reason;
        }

        public void setReason(int reason) {
            this.reason = reason;
        }

        public double getStorage() {
            return storage;
        }

        public void setStorage(double storage) {
            this.storage = storage;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public double getTickSize() {
            return tickSize;
        }

        public void setTickSize(double tickSize) {
            this.tickSize = tickSize;
        }

        public double getTickValue() {
            return tickValue;
        }

        public void setTickValue(double tickValue) {
            this.tickValue = tickValue;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public long getTimeUtc() {
            return timeUtc;
        }

        public void setTimeUtc(long timeUtc) {
            this.timeUtc = timeUtc;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public long getVolume() {
            return volume;
        }

        public void setVolume(long volume) {
            this.volume = volume;
        }

        public long getVolumeClosed() {
            return volumeClosed;
        }

        public void setVolumeClosed(long volumeClosed) {
            this.volumeClosed = volumeClosed;
        }

        public long getVolumeClosedExt() {
            return volumeClosedExt;
        }

        public void setVolumeClosedExt(long volumeClosedExt) {
            this.volumeClosedExt = volumeClosedExt;
        }

        public long getVolumeExt() {
            return volumeExt;
        }

        public void setVolumeExt(long volumeExt) {
            this.volumeExt = volumeExt;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Payload payload)) return false;
            return Double.compare(accountRate, payload.accountRate) == 0 && action == payload.action && Double.compare(
                    balance, payload.balance) == 0 && Double.compare(commission, payload.commission) == 0 && Double.compare(
                            contractSize, payload.contractSize) == 0 && deal == payload.deal && dealer == payload.dealer && digits == payload.digits && digitsCurrency == payload.digitsCurrency && entry == payload.entry && Double.compare(
                                    equity, payload.equity) == 0 && expertId == payload.expertId && Double.compare(fee, payload.fee) == 0 && flags == payload.flags && Double.compare(
                                            freeMargin, payload.freeMargin) == 0 && leverage == payload.leverage && login == payload.login && Double.compare(
                                                    margin, payload.margin) == 0 && Double.compare(marketAsk, payload.marketAsk) == 0 && Double.compare(
                                                            marketBid, payload.marketBid) == 0 && Double.compare(marketLast, payload.marketLast) == 0 && modificationFlags == payload.modificationFlags && order == payload.order && positionId == payload.positionId && Double.compare(
                                                                    price, payload.price) == 0 && Double.compare(priceGateway, payload.priceGateway) == 0 && Double.compare(
                                                                            pricePosition, payload.pricePosition) == 0 && Double.compare(priceSL, payload.priceSL) == 0 && Double.compare(
                                                                                    priceTP, payload.priceTP) == 0 && Double.compare(profit, payload.profit) == 0 && Double.compare(
                                                                                            profitRaw, payload.profitRaw) == 0 && Double.compare(rateMargin, payload.rateMargin) == 0 && Double.compare(
                                                                                                    rateProfit, payload.rateProfit) == 0 && reason == payload.reason && Double.compare(storage, payload.storage) == 0 && Double.compare(
                                                                                                            tickSize, payload.tickSize) == 0 && Double.compare(tickValue, payload.tickValue) == 0 && time == payload.time && timeUtc == payload.timeUtc && Double.compare(
                                                                                                                    value, payload.value) == 0 && volume == payload.volume && volumeClosed == payload.volumeClosed && volumeClosedExt == payload.volumeClosedExt && volumeExt == payload.volumeExt && Objects.equals(
                                                                                                                            comment, payload.comment) && Objects.equals(externalId, payload.externalId) && Objects.equals(
                                                                                                                                    gateway, payload.gateway) && Objects.equals(rawApiData, payload.rawApiData) && Objects.equals(
                                                                                                                                            symbol, payload.symbol);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountRate, action, balance, comment, commission, contractSize, deal, dealer, digits, digitsCurrency, entry, equity, expertId, externalId, fee, flags, freeMargin, gateway, leverage, login, margin, marketAsk, marketBid, marketLast, modificationFlags, order, positionId, price, priceGateway, pricePosition, priceSL, priceTP, profit, profitRaw, rateMargin, rateProfit, rawApiData, reason, storage, symbol, tickSize, tickValue, time, timeUtc, value, volume, volumeClosed, volumeClosedExt, volumeExt);
        }

        @Override
        public String toString() {
            return "Payload{" + "accountRate=" + accountRate + ", action=" + action + ", balance=" + balance + ", comment='" + comment + '\'' + ", commission=" + commission + ", contractSize=" + contractSize + ", deal=" + deal + ", dealer=" + dealer + ", digits=" + digits + ", digitsCurrency=" + digitsCurrency + ", entry=" + entry + ", equity=" + equity + ", expertId=" + expertId + ", externalId='" + externalId + '\'' + ", fee=" + fee + ", flags=" + flags + ", freeMargin=" + freeMargin + ", gateway='" + gateway + '\'' + ", leverage=" + leverage + ", login=" + login + ", margin=" + margin + ", marketAsk=" + marketAsk + ", marketBid=" + marketBid + ", marketLast=" + marketLast + ", modificationFlags=" + modificationFlags + ", order=" + order + ", positionId=" + positionId + ", price=" + price + ", priceGateway=" + priceGateway + ", pricePosition=" + pricePosition + ", priceSL=" + priceSL + ", priceTP=" + priceTP + ", profit=" + profit + ", profitRaw=" + profitRaw + ", rateMargin=" + rateMargin + ", rateProfit=" + rateProfit + ", rawApiData=" + rawApiData + ", reason=" + reason + ", storage=" + storage + ", symbol='" + symbol + '\'' + ", tickSize=" + tickSize + ", tickValue=" + tickValue + ", time=" + time + ", timeUtc=" + timeUtc + ", value=" + value + ", volume=" + volume + ", volumeClosed=" + volumeClosed + ", volumeClosedExt=" + volumeClosedExt + ", volumeExt=" + volumeExt + '}';
        }
    }

    public static class RawApiData {
        @JsonProperty("AppId")
        public int appId;

        @JsonProperty("Id")
        public int id;

        @JsonProperty("Value")
        public int value;

        public RawApiData(int appId, int id, int value) {
            this.appId = appId;
            this.id = id;
            this.value = value;
        }


        public int getAppId() {
            return appId;
        }

        public void setAppId(int appId) {
            this.appId = appId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof RawApiData that)) return false;
            return appId == that.appId && id == that.id && value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(appId, id, value);
        }

        @Override
        public String toString() {
            return "RawApiData{" + "appId=" + appId + ", id=" + id + ", value=" + value + '}';
        }
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CloseTradeMt5 that)) return false;
        return Objects.equals(header, that.header) && Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, payload);
    }

    @Override
    public String toString() {
        return "CloseTradeMt5{" + "header=" + header + ", payload=" + payload + '}';
    }
}