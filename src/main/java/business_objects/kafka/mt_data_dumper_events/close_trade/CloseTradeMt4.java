package business_objects.kafka.mt_data_dumper_events.close_trade;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class CloseTradeMt4 {

    @JsonProperty("header")
    public Header header;

    @JsonProperty("payload")
    public Payload payload;

    public CloseTradeMt4(Header header, Payload payload) {
        this.header = header;
        this.payload = payload;
    }

    public static class Header {
        @JsonProperty("msg_id")
        public String msgId;

        @JsonProperty("msg_type")
        public String msgType;

        @JsonProperty("operation")
        public Integer operation;

        @JsonProperty("server_id")
        public Integer serverId;

        @JsonProperty("timestamp")
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

        @JsonProperty("account_rate")
        public Double accountRate;

        @JsonProperty("api_data")
        public List<Integer> apiData;

        @JsonProperty("balance")
        public Double balance;

        @JsonProperty("close_price")
        public Double closePrice;

        @JsonProperty("close_time")
        public Long closeTime;

        @JsonProperty("close_time_utc")
        public Long closeTimeUtc;

        @JsonProperty("cmd")
        public Integer cmd;

        @JsonProperty("comment")
        public String comment;

        @JsonProperty("commission")
        public Double commission;

        @JsonProperty("commission_agent")
        public Double commissionAgent;

        @JsonProperty("conv_rates")
        public List<Double> convRates;

        @JsonProperty("conv_reserv")
        public List<Integer> convReserv;

        @JsonProperty("current_ask")
        public Double currentAsk;

        @JsonProperty("current_bid")
        public Double currentBid;

        @JsonProperty("digits")
        public Integer digits;

        @JsonProperty("equity")
        public Double equity;

        @JsonProperty("expiration")
        public Long expiration;

        @JsonProperty("expiration_utc")
        public Long expirationUtc;

        @JsonProperty("free_margin")
        public Double freeMargin;

        @JsonProperty("gw_close_price")
        public Double gwClosePrice;

        @JsonProperty("gw_open_price")
        public Double gwOpenPrice;

        @JsonProperty("gw_order")
        public Long gwOrder;

        @JsonProperty("gw_volume")
        public Double gwVolume;

        @JsonProperty("leverage")
        public Double leverage;

        @JsonProperty("login")
        public Long login;

        @JsonProperty("magic")
        public Long magic;

        @JsonProperty("margin")
        public Double margin;

        @JsonProperty("margin_rate")
        public Double marginRate;

        @JsonProperty("mode")
        public Integer mode;

        @JsonProperty("open_price")
        public Double openPrice;

        @JsonProperty("open_time")
        public Long openTime;

        @JsonProperty("open_time_utc")
        public Long openTimeUtc;

        @JsonProperty("order")
        public Long order;

        @JsonProperty("profit")
        public Double profit;

        @JsonProperty("reason")
        public Integer reason;

        @JsonProperty("sl")
        public Double sl;

        @JsonProperty("state")
        public Integer state;

        @JsonProperty("storage")
        public Double storage;

        @JsonProperty("symbol")
        public String symbol;

        @JsonProperty("taxes")
        public Double taxes;

        @JsonProperty("timestamp")
        public Long timestamp;

        @JsonProperty("timestamp_utc")
        public Long timestampUtc;

        @JsonProperty("tp")
        public Double tp;

        @JsonProperty("volume")
        public Double volume;

        public Payload(
                Double accountRate, List<Integer> apiData, Double balance, Double closePrice, Long closeTime,
                Long closeTimeUtc,
                Integer cmd, String comment, Double commission, Double commissionAgent, List<Double> convRates,
                List<Integer> convReserv, Double currentAsk, Double currentBid, Integer digits, Double equity,
                Long expiration,
                Long expirationUtc, Double freeMargin, Double gwClosePrice, Double gwOpenPrice, Long gwOrder,
                Double gwVolume,
                Double leverage, Long login, Long magic, Double margin, Double marginRate, Integer mode,
                Double openPrice,
                Long openTime, Long openTimeUtc, Long order, Double profit, Integer reason, Double sl, Integer state,
                Double storage,
                String symbol, Double taxes, Long timestamp, Long timestampUtc, Double tp, Double volume) {
            this.accountRate = accountRate;
            this.apiData = apiData;
            this.balance = balance;
            this.closePrice = closePrice;
            this.closeTime = closeTime;
            this.closeTimeUtc = closeTimeUtc;
            this.cmd = cmd;
            this.comment = comment;
            this.commission = commission;
            this.commissionAgent = commissionAgent;
            this.convRates = convRates;
            this.convReserv = convReserv;
            this.currentAsk = currentAsk;
            this.currentBid = currentBid;
            this.digits = digits;
            this.equity = equity;
            this.expiration = expiration;
            this.expirationUtc = expirationUtc;
            this.freeMargin = freeMargin;
            this.gwClosePrice = gwClosePrice;
            this.gwOpenPrice = gwOpenPrice;
            this.gwOrder = gwOrder;
            this.gwVolume = gwVolume;
            this.leverage = leverage;
            this.login = login;
            this.magic = magic;
            this.margin = margin;
            this.marginRate = marginRate;
            this.mode = mode;
            this.openPrice = openPrice;
            this.openTime = openTime;
            this.openTimeUtc = openTimeUtc;
            this.order = order;
            this.profit = profit;
            this.reason = reason;
            this.sl = sl;
            this.state = state;
            this.storage = storage;
            this.symbol = symbol;
            this.taxes = taxes;
            this.timestamp = timestamp;
            this.timestampUtc = timestampUtc;
            this.tp = tp;
            this.volume = volume;
        }

        public Double getAccountRate() {
            return accountRate;
        }

        public void setAccountRate(Double accountRate) {
            this.accountRate = accountRate;
        }

        public List<Integer> getApiData() {
            return apiData;
        }

        public void setApiData(List<Integer> apiData) {
            this.apiData = apiData;
        }

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }

        public Double getClosePrice() {
            return closePrice;
        }

        public void setClosePrice(Double closePrice) {
            this.closePrice = closePrice;
        }

        public Long getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(Long closeTime) {
            this.closeTime = closeTime;
        }

        public Long getCloseTimeUtc() {
            return closeTimeUtc;
        }

        public void setCloseTimeUtc(Long closeTimeUtc) {
            this.closeTimeUtc = closeTimeUtc;
        }

        public Integer getCmd() {
            return cmd;
        }

        public void setCmd(Integer cmd) {
            this.cmd = cmd;
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

        public Double getCommissionAgent() {
            return commissionAgent;
        }

        public void setCommissionAgent(Double commissionAgent) {
            this.commissionAgent = commissionAgent;
        }

        public List<Double> getConvRates() {
            return convRates;
        }

        public void setConvRates(List<Double> convRates) {
            this.convRates = convRates;
        }

        public List<Integer> getConvReserv() {
            return convReserv;
        }

        public void setConvReserv(List<Integer> convReserv) {
            this.convReserv = convReserv;
        }

        public Double getCurrentAsk() {
            return currentAsk;
        }

        public void setCurrentAsk(Double currentAsk) {
            this.currentAsk = currentAsk;
        }

        public Double getCurrentBid() {
            return currentBid;
        }

        public void setCurrentBid(Double currentBid) {
            this.currentBid = currentBid;
        }

        public Integer getDigits() {
            return digits;
        }

        public void setDigits(Integer digits) {
            this.digits = digits;
        }

        public Double getEquity() {
            return equity;
        }

        public void setEquity(Double equity) {
            this.equity = equity;
        }

        public Long getExpiration() {
            return expiration;
        }

        public void setExpiration(Long expiration) {
            this.expiration = expiration;
        }

        public Long getExpirationUtc() {
            return expirationUtc;
        }

        public void setExpirationUtc(Long expirationUtc) {
            this.expirationUtc = expirationUtc;
        }

        public Double getFreeMargin() {
            return freeMargin;
        }

        public void setFreeMargin(Double freeMargin) {
            this.freeMargin = freeMargin;
        }

        public Double getGwClosePrice() {
            return gwClosePrice;
        }

        public void setGwClosePrice(Double gwClosePrice) {
            this.gwClosePrice = gwClosePrice;
        }

        public Double getGwOpenPrice() {
            return gwOpenPrice;
        }

        public void setGwOpenPrice(Double gwOpenPrice) {
            this.gwOpenPrice = gwOpenPrice;
        }

        public Long getGwOrder() {
            return gwOrder;
        }

        public void setGwOrder(Long gwOrder) {
            this.gwOrder = gwOrder;
        }

        public Double getGwVolume() {
            return gwVolume;
        }

        public void setGwVolume(Double gwVolume) {
            this.gwVolume = gwVolume;
        }

        public Double getLeverage() {
            return leverage;
        }

        public void setLeverage(Double leverage) {
            this.leverage = leverage;
        }

        public Long getLogin() {
            return login;
        }

        public void setLogin(Long login) {
            this.login = login;
        }

        public Long getMagic() {
            return magic;
        }

        public void setMagic(Long magic) {
            this.magic = magic;
        }

        public Double getMargin() {
            return margin;
        }

        public void setMargin(Double margin) {
            this.margin = margin;
        }

        public Double getMarginRate() {
            return marginRate;
        }

        public void setMarginRate(Double marginRate) {
            this.marginRate = marginRate;
        }

        public Integer getMode() {
            return mode;
        }

        public void setMode(Integer mode) {
            this.mode = mode;
        }

        public Double getOpenPrice() {
            return openPrice;
        }

        public void setOpenPrice(Double openPrice) {
            this.openPrice = openPrice;
        }

        public Long getOpenTime() {
            return openTime;
        }

        public void setOpenTime(Long openTime) {
            this.openTime = openTime;
        }

        public Long getOpenTimeUtc() {
            return openTimeUtc;
        }

        public void setOpenTimeUtc(Long openTimeUtc) {
            this.openTimeUtc = openTimeUtc;
        }

        public Long getOrder() {
            return order;
        }

        public void setOrder(Long order) {
            this.order = order;
        }

        public Double getProfit() {
            return profit;
        }

        public void setProfit(Double profit) {
            this.profit = profit;
        }

        public Integer getReason() {
            return reason;
        }

        public void setReason(Integer reason) {
            this.reason = reason;
        }

        public Double getSl() {
            return sl;
        }

        public void setSl(Double sl) {
            this.sl = sl;
        }

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
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

        public Double getTaxes() {
            return taxes;
        }

        public void setTaxes(Double taxes) {
            this.taxes = taxes;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public Long getTimestampUtc() {
            return timestampUtc;
        }

        public void setTimestampUtc(Long timestampUtc) {
            this.timestampUtc = timestampUtc;
        }

        public Double getTp() {
            return tp;
        }

        public void setTp(Double tp) {
            this.tp = tp;
        }

        public Double getVolume() {
            return volume;
        }

        public void setVolume(Double volume) {
            this.volume = volume;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Payload payload)) return false;
            return Double.compare(accountRate, payload.accountRate) == 0 && Double.compare(balance, payload.balance) == 0 && Double.compare(
                    closePrice, payload.closePrice) == 0 && closeTime == payload.closeTime && closeTimeUtc == payload.closeTimeUtc && cmd == payload.cmd && Double.compare(
                            commission, payload.commission) == 0 && Double.compare(commissionAgent, payload.commissionAgent) == 0 && Double.compare(
                                    currentAsk, payload.currentAsk) == 0 && Double.compare(currentBid, payload.currentBid) == 0 && digits == payload.digits && Double.compare(
                                            equity, payload.equity) == 0 && expiration == payload.expiration && expirationUtc == payload.expirationUtc && Double.compare(
                                                    freeMargin, payload.freeMargin) == 0 && Double.compare(gwClosePrice, payload.gwClosePrice) == 0 && Double.compare(
                                                            gwOpenPrice, payload.gwOpenPrice) == 0 && gwOrder == payload.gwOrder && Double.compare(gwVolume, payload.gwVolume) == 0 && leverage == payload.leverage && login == payload.login && magic == payload.magic && Double.compare(
                                                                    margin, payload.margin) == 0 && Double.compare(marginRate, payload.marginRate) == 0 && mode == payload.mode && Double.compare(
                                                                            openPrice, payload.openPrice) == 0 && openTime == payload.openTime && openTimeUtc == payload.openTimeUtc && order == payload.order && Double.compare(
                                                                                    profit, payload.profit) == 0 && reason == payload.reason && Double.compare(sl, payload.sl) == 0 && state == payload.state && Double.compare(
                                                                                            storage, payload.storage) == 0 && Double.compare(taxes, payload.taxes) == 0 && timestamp == payload.timestamp && timestampUtc == payload.timestampUtc && Double.compare(
                                                                                                    tp, payload.tp) == 0 && Double.compare(volume, payload.volume) == 0 && Objects.equals(
                                                                                                            apiData, payload.apiData) && Objects.equals(comment, payload.comment) && Objects.equals(
                                                                                                                    convRates, payload.convRates) && Objects.equals(convReserv, payload.convReserv) && Objects.equals(
                                                                                                                            symbol, payload.symbol);
        }

        @Override
        public int hashCode() {
            return Objects.hash(accountRate, apiData, balance, closePrice, closeTime, closeTimeUtc, cmd, comment, commission, commissionAgent, convRates, convReserv, currentAsk, currentBid, digits, equity, expiration, expirationUtc, freeMargin, gwClosePrice, gwOpenPrice, gwOrder, gwVolume, leverage, login, magic, margin, marginRate, mode, openPrice, openTime, openTimeUtc, order, profit, reason, sl, state, storage, symbol, taxes, timestamp, timestampUtc, tp, volume);
        }

        @Override
        public String toString() {
            return "Payload{" + "accountRate=" + accountRate + ", apiData=" + apiData + ", balance=" + balance + ", closePrice=" + closePrice + ", closeTime=" + closeTime + ", closeTimeUtc=" + closeTimeUtc + ", cmd=" + cmd + ", comment='" + comment + '\'' + ", commission=" + commission + ", commissionAgent=" + commissionAgent + ", convRates=" + convRates + ", convReserv=" + convReserv + ", currentAsk=" + currentAsk + ", currentBid=" + currentBid + ", digits=" + digits + ", equity=" + equity + ", expiration=" + expiration + ", expirationUtc=" + expirationUtc + ", freeMargin=" + freeMargin + ", gwClosePrice=" + gwClosePrice + ", gwOpenPrice=" + gwOpenPrice + ", gwOrder=" + gwOrder + ", gwVolume=" + gwVolume + ", leverage=" + leverage + ", login=" + login + ", magic=" + magic + ", margin=" + margin + ", marginRate=" + marginRate + ", mode=" + mode + ", openPrice=" + openPrice + ", openTime=" + openTime + ", openTimeUtc=" + openTimeUtc + ", order=" + order + ", profit=" + profit + ", reason=" + reason + ", sl=" + sl + ", state=" + state + ", storage=" + storage + ", symbol='" + symbol + '\'' + ", taxes=" + taxes + ", timestamp=" + timestamp + ", timestampUtc=" + timestampUtc + ", tp=" + tp + ", volume=" + volume + '}';
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

    public void setPayload(
            Payload payload) {
        this.payload = payload;
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, payload);
    }

    @Override
    public String toString() {
        return "CloseTradeMtEvent{" + "header=" + header + ", payload=" + payload + '}';
    }
}
