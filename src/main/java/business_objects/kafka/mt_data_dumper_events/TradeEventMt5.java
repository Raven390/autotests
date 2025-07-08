package business_objects.kafka.mt_data_dumper_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class TradeEventMt5 {

    @JsonProperty("Header")
    public Header header;

    @JsonProperty("Payload")
    public Payload payload;

    public TradeEventMt5(Header header, Payload payload) {
        this.header = header;
        this.payload = payload;
    }

    public static class Header {
        @JsonProperty("MsgId")
        public String msgId;

        @JsonProperty("MsgType")
        public String msgType;

        @JsonProperty("Operation")
        public Integer operation;

        @JsonProperty("ServerId")
        public Integer serverId;

        @JsonProperty("Timestamp")
        public Long timestamp;

        public Header(String msgId, String msgType, Integer operation, Integer serverId, Long timestamp) {
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

        public Integer getOperation() {
            return operation;
        }

        public void setOperation(Integer operation) {
            this.operation = operation;
        }

        public Integer getServerId() {
            return serverId;
        }

        public void setServerId(Integer serverId) {
            this.serverId = serverId;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
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
        public Double accountRate;

        @JsonProperty("Action")
        public Integer action;

        @JsonProperty("Balance")
        public Double balance;

        @JsonProperty("Comment")
        public String comment;

        @JsonProperty("Commission")
        public Double commission;

        @JsonProperty("ContractSize")
        public Double contractSize;

        @JsonProperty("Deal")
        public Long deal;

        @JsonProperty("Dealer")
        public Long dealer;

        @JsonProperty("Digits")
        public Integer digits;

        @JsonProperty("DigitsCurrency")
        public Integer digitsCurrency;

        @JsonProperty("Entry")
        public Integer entry;

        @JsonProperty("Equity")
        public Double equity;

        @JsonProperty("ExpertID")
        public Long expertId;

        @JsonProperty("ExternalID")
        public String externalId;

        @JsonProperty("Fee")
        public Double fee;

        @JsonProperty("Flags")
        public Integer flags;

        @JsonProperty("FreeMargin")
        public Double freeMargin;

        @JsonProperty("Gateway")
        public String gateway;

        @JsonProperty("Leverage")
        public Integer leverage;

        @JsonProperty("Login")
        public Long login;

        @JsonProperty("Margin")
        public Double margin;

        @JsonProperty("MarketAsk")
        public Double marketAsk;

        @JsonProperty("MarketBid")
        public Double marketBid;

        @JsonProperty("MarketLast")
        public Double marketLast;

        @JsonProperty("ModificationFlags")
        public Integer modificationFlags;

        @JsonProperty("Order")
        public Long order;

        @JsonProperty("PositionID")
        public Long positionId;

        @JsonProperty("Price")
        public Double price;

        @JsonProperty("PriceGateway")
        public Double priceGateway;

        @JsonProperty("PricePosition")
        public Double pricePosition;

        @JsonProperty("PriceSL")
        public Double priceSL;

        @JsonProperty("PriceTP")
        public Double priceTP;

        @JsonProperty("Profit")
        public Double profit;

        @JsonProperty("ProfitRaw")
        public Double profitRaw;

        @JsonProperty("RateMargin")
        public Double rateMargin;

        @JsonProperty("RateProfit")
        public Double rateProfit;

        @JsonProperty("RawApiData")
        public List<RawApiData> rawApiData;

        @JsonProperty("Reason")
        public Integer reason;

        @JsonProperty("Storage")
        public Double storage;

        @JsonProperty("Symbol")
        public String symbol;

        @JsonProperty("TickSize")
        public Double tickSize;

        @JsonProperty("TickValue")
        public Double tickValue;

        @JsonProperty("Time")
        public Long time;

        @JsonProperty("TimeUtc")
        public Long timeUtc;

        @JsonProperty("Value")
        public Double value;

        @JsonProperty("Volume")
        public Long volume;

        @JsonProperty("VolumeClosed")
        public Long volumeClosed;

        @JsonProperty("VolumeClosedExt")
        public Long volumeClosedExt;

        @JsonProperty("VolumeExt")
        public Long volumeExt;

        public Payload(
                Double accountRate, Integer action, Double balance, String comment, Double commission,
                Double contractSize,
                Long deal, Long dealer, Integer digits, Integer digitsCurrency, Integer entry, Double equity,
                Long expertId,
                String externalId, Double fee, Integer flags, Double freeMargin, String gateway, Integer leverage,
                Long login,
                Double margin, Double marketAsk, Double marketBid, Double marketLast, Integer modificationFlags,
                Long order,
                Long positionId, Double price, Double priceGateway, Double pricePosition, Double priceSL,
                Double priceTP,
                Double profit, Double profitRaw, Double rateMargin, Double rateProfit, List<RawApiData> rawApiData,
                Integer reason,
                Double storage, String symbol, Double tickSize, Double tickValue, Long time, Long timeUtc, Double value,
                Long volume, Long volumeClosed, Long volumeClosedExt, Long volumeExt) {
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

        public Double getAccountRate() {
            return accountRate;
        }

        public void setAccountRate(Double accountRate) {
            this.accountRate = accountRate;
        }

        public Integer getAction() {
            return action;
        }

        public void setAction(Integer action) {
            this.action = action;
        }

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Double getCommission() {
            return commission;
        }

        public void setCommission(Double commission) {
            this.commission = commission;
        }

        public Double getContractSize() {
            return contractSize;
        }

        public void setContractSize(Double contractSize) {
            this.contractSize = contractSize;
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

        public Integer getDigits() {
            return digits;
        }

        public void setDigits(Integer digits) {
            this.digits = digits;
        }

        public Integer getDigitsCurrency() {
            return digitsCurrency;
        }

        public void setDigitsCurrency(Integer digitsCurrency) {
            this.digitsCurrency = digitsCurrency;
        }

        public Integer getEntry() {
            return entry;
        }

        public void setEntry(Integer entry) {
            this.entry = entry;
        }

        public Double getEquity() {
            return equity;
        }

        public void setEquity(Double equity) {
            this.equity = equity;
        }

        public Long getExpertId() {
            return expertId;
        }

        public void setExpertId(Long expertId) {
            this.expertId = expertId;
        }

        public String getExternalId() {
            return externalId;
        }

        public void setExternalId(String externalId) {
            this.externalId = externalId;
        }

        public Double getFee() {
            return fee;
        }

        public void setFee(Double fee) {
            this.fee = fee;
        }

        public Integer getFlags() {
            return flags;
        }

        public void setFlags(Integer flags) {
            this.flags = flags;
        }

        public Double getFreeMargin() {
            return freeMargin;
        }

        public void setFreeMargin(Double freeMargin) {
            this.freeMargin = freeMargin;
        }

        public String getGateway() {
            return gateway;
        }

        public void setGateway(String gateway) {
            this.gateway = gateway;
        }

        public Integer getLeverage() {
            return leverage;
        }

        public void setLeverage(Integer leverage) {
            this.leverage = leverage;
        }

        public Long getLogin() {
            return login;
        }

        public void setLogin(Long login) {
            this.login = login;
        }

        public Double getMargin() {
            return margin;
        }

        public void setMargin(Double margin) {
            this.margin = margin;
        }

        public Double getMarketAsk() {
            return marketAsk;
        }

        public void setMarketAsk(Double marketAsk) {
            this.marketAsk = marketAsk;
        }

        public Double getMarketBid() {
            return marketBid;
        }

        public void setMarketBid(Double marketBid) {
            this.marketBid = marketBid;
        }

        public Double getMarketLast() {
            return marketLast;
        }

        public void setMarketLast(Double marketLast) {
            this.marketLast = marketLast;
        }

        public Integer getModificationFlags() {
            return modificationFlags;
        }

        public void setModificationFlags(Integer modificationFlags) {
            this.modificationFlags = modificationFlags;
        }

        public Long getOrder() {
            return order;
        }

        public void setOrder(Long order) {
            this.order = order;
        }

        public Long getPositionId() {
            return positionId;
        }

        public void setPositionId(Long positionId) {
            this.positionId = positionId;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getPriceGateway() {
            return priceGateway;
        }

        public void setPriceGateway(Double priceGateway) {
            this.priceGateway = priceGateway;
        }

        public Double getPricePosition() {
            return pricePosition;
        }

        public void setPricePosition(Double pricePosition) {
            this.pricePosition = pricePosition;
        }

        public Double getPriceSL() {
            return priceSL;
        }

        public void setPriceSL(Double priceSL) {
            this.priceSL = priceSL;
        }

        public Double getPriceTP() {
            return priceTP;
        }

        public void setPriceTP(Double priceTP) {
            this.priceTP = priceTP;
        }

        public Double getProfit() {
            return profit;
        }

        public void setProfit(Double profit) {
            this.profit = profit;
        }

        public Double getProfitRaw() {
            return profitRaw;
        }

        public void setProfitRaw(Double profitRaw) {
            this.profitRaw = profitRaw;
        }

        public Double getRateMargin() {
            return rateMargin;
        }

        public void setRateMargin(Double rateMargin) {
            this.rateMargin = rateMargin;
        }

        public Double getRateProfit() {
            return rateProfit;
        }

        public void setRateProfit(Double rateProfit) {
            this.rateProfit = rateProfit;
        }

        public List<RawApiData> getRawApiData() {
            return rawApiData;
        }

        public void setRawApiData(
                List<RawApiData> rawApiData) {
            this.rawApiData = rawApiData;
        }

        public Integer getReason() {
            return reason;
        }

        public void setReason(Integer reason) {
            this.reason = reason;
        }

        public Double getStorage() {
            return storage;
        }

        public void setStorage(Double storage) {
            this.storage = storage;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public Double getTickSize() {
            return tickSize;
        }

        public void setTickSize(Double tickSize) {
            this.tickSize = tickSize;
        }

        public Double getTickValue() {
            return tickValue;
        }

        public void setTickValue(Double tickValue) {
            this.tickValue = tickValue;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public Long getTimeUtc() {
            return timeUtc;
        }

        public void setTimeUtc(Long timeUtc) {
            this.timeUtc = timeUtc;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }

        public Long getVolume() {
            return volume;
        }

        public void setVolume(Long volume) {
            this.volume = volume;
        }

        public Long getVolumeClosed() {
            return volumeClosed;
        }

        public void setVolumeClosed(Long volumeClosed) {
            this.volumeClosed = volumeClosed;
        }

        public Long getVolumeClosedExt() {
            return volumeClosedExt;
        }

        public void setVolumeClosedExt(Long volumeClosedExt) {
            this.volumeClosedExt = volumeClosedExt;
        }

        public Long getVolumeExt() {
            return volumeExt;
        }

        public void setVolumeExt(Long volumeExt) {
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
        if (!(o instanceof TradeEventMt5 that)) return false;
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
