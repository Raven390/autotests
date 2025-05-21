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
        public int operation;

        @JsonProperty("server_id")
        public int serverId;

        @JsonProperty("timestamp")
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
        @JsonProperty("account_rate")
        public double accountRate;

        @JsonProperty("api_data")
        public List<Integer> apiData;

        @JsonProperty("balance")
        public double balance;

        @JsonProperty("close_price")
        public double closePrice;

        @JsonProperty("close_time")
        public long closeTime;

        @JsonProperty("close_time_utc")
        public long closeTimeUtc;

        @JsonProperty("cmd")
        public int cmd;

        @JsonProperty("comment")
        public String comment;

        @JsonProperty("commission")
        public double commission;

        @JsonProperty("commission_agent")
        public double commissionAgent;

        @JsonProperty("conv_rates")
        public List<Double> convRates;

        @JsonProperty("conv_reserv")
        public List<Integer> convReserv;

        @JsonProperty("current_ask")
        public double currentAsk;

        @JsonProperty("current_bid")
        public double currentBid;

        @JsonProperty("digits")
        public int digits;

        @JsonProperty("equity")
        public double equity;

        @JsonProperty("expiration")
        public long expiration;

        @JsonProperty("expiration_utc")
        public long expirationUtc;

        @JsonProperty("free_margin")
        public double freeMargin;

        @JsonProperty("gw_close_price")
        public double gwClosePrice;

        @JsonProperty("gw_open_price")
        public double gwOpenPrice;

        @JsonProperty("gw_order")
        public long gwOrder;

        @JsonProperty("gw_volume")
        public double gwVolume;

        @JsonProperty("leverage")
        public double leverage;

        @JsonProperty("login")
        public long login;

        @JsonProperty("magic")
        public long magic;

        @JsonProperty("margin")
        public double margin;

        @JsonProperty("margin_rate")
        public double marginRate;

        @JsonProperty("mode")
        public int mode;

        @JsonProperty("open_price")
        public double openPrice;

        @JsonProperty("open_time")
        public long openTime;

        @JsonProperty("open_time_utc")
        public long openTimeUtc;

        @JsonProperty("order")
        public long order;

        @JsonProperty("profit")
        public double profit;

        @JsonProperty("reason")
        public int reason;

        @JsonProperty("sl")
        public double sl;

        @JsonProperty("state")
        public int state;

        @JsonProperty("storage")
        public double storage;

        @JsonProperty("symbol")
        public String symbol;

        @JsonProperty("taxes")
        public double taxes;

        @JsonProperty("timestamp")
        public long timestamp;

        @JsonProperty("timestamp_utc")
        public long timestampUtc;

        @JsonProperty("tp")
        public double tp;

        @JsonProperty("volume")
        public double volume;

        public Payload(
                double accountRate, List<Integer> apiData, double balance, double closePrice, long closeTime,
                long closeTimeUtc,
                int cmd, String comment, double commission, double commissionAgent, List<Double> convRates,
                List<Integer> convReserv, double currentAsk, double currentBid, int digits, double equity,
                long expiration,
                long expirationUtc, double freeMargin, double gwClosePrice, double gwOpenPrice, long gwOrder,
                double gwVolume,
                double leverage, long login, long magic, double margin, double marginRate, int mode, double openPrice,
                long openTime, long openTimeUtc, long order, double profit, int reason, double sl, int state,
                double storage,
                String symbol, double taxes, long timestamp, long timestampUtc, double tp, double volume) {
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

        public double getAccountRate() {
            return accountRate;
        }

        public void setAccountRate(double accountRate) {
            this.accountRate = accountRate;
        }

        public List<Integer> getApiData() {
            return apiData;
        }

        public void setApiData(List<Integer> apiData) {
            this.apiData = apiData;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public double getClosePrice() {
            return closePrice;
        }

        public void setClosePrice(double closePrice) {
            this.closePrice = closePrice;
        }

        public long getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(long closeTime) {
            this.closeTime = closeTime;
        }

        public long getCloseTimeUtc() {
            return closeTimeUtc;
        }

        public void setCloseTimeUtc(long closeTimeUtc) {
            this.closeTimeUtc = closeTimeUtc;
        }

        public int getCmd() {
            return cmd;
        }

        public void setCmd(int cmd) {
            this.cmd = cmd;
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

        public double getCommissionAgent() {
            return commissionAgent;
        }

        public void setCommissionAgent(double commissionAgent) {
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

        public double getCurrentAsk() {
            return currentAsk;
        }

        public void setCurrentAsk(double currentAsk) {
            this.currentAsk = currentAsk;
        }

        public double getCurrentBid() {
            return currentBid;
        }

        public void setCurrentBid(double currentBid) {
            this.currentBid = currentBid;
        }

        public int getDigits() {
            return digits;
        }

        public void setDigits(int digits) {
            this.digits = digits;
        }

        public double getEquity() {
            return equity;
        }

        public void setEquity(double equity) {
            this.equity = equity;
        }

        public long getExpiration() {
            return expiration;
        }

        public void setExpiration(long expiration) {
            this.expiration = expiration;
        }

        public long getExpirationUtc() {
            return expirationUtc;
        }

        public void setExpirationUtc(long expirationUtc) {
            this.expirationUtc = expirationUtc;
        }

        public double getFreeMargin() {
            return freeMargin;
        }

        public void setFreeMargin(double freeMargin) {
            this.freeMargin = freeMargin;
        }

        public double getGwClosePrice() {
            return gwClosePrice;
        }

        public void setGwClosePrice(double gwClosePrice) {
            this.gwClosePrice = gwClosePrice;
        }

        public double getGwOpenPrice() {
            return gwOpenPrice;
        }

        public void setGwOpenPrice(double gwOpenPrice) {
            this.gwOpenPrice = gwOpenPrice;
        }

        public long getGwOrder() {
            return gwOrder;
        }

        public void setGwOrder(long gwOrder) {
            this.gwOrder = gwOrder;
        }

        public double getGwVolume() {
            return gwVolume;
        }

        public void setGwVolume(double gwVolume) {
            this.gwVolume = gwVolume;
        }

        public double getLeverage() {
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

        public long getMagic() {
            return magic;
        }

        public void setMagic(long magic) {
            this.magic = magic;
        }

        public double getMargin() {
            return margin;
        }

        public void setMargin(double margin) {
            this.margin = margin;
        }

        public double getMarginRate() {
            return marginRate;
        }

        public void setMarginRate(double marginRate) {
            this.marginRate = marginRate;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public double getOpenPrice() {
            return openPrice;
        }

        public void setOpenPrice(double openPrice) {
            this.openPrice = openPrice;
        }

        public long getOpenTime() {
            return openTime;
        }

        public void setOpenTime(long openTime) {
            this.openTime = openTime;
        }

        public long getOpenTimeUtc() {
            return openTimeUtc;
        }

        public void setOpenTimeUtc(long openTimeUtc) {
            this.openTimeUtc = openTimeUtc;
        }

        public long getOrder() {
            return order;
        }

        public void setOrder(long order) {
            this.order = order;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public int getReason() {
            return reason;
        }

        public void setReason(int reason) {
            this.reason = reason;
        }

        public double getSl() {
            return sl;
        }

        public void setSl(double sl) {
            this.sl = sl;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
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

        public double getTaxes() {
            return taxes;
        }

        public void setTaxes(double taxes) {
            this.taxes = taxes;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public long getTimestampUtc() {
            return timestampUtc;
        }

        public void setTimestampUtc(long timestampUtc) {
            this.timestampUtc = timestampUtc;
        }

        public double getTp() {
            return tp;
        }

        public void setTp(double tp) {
            this.tp = tp;
        }

        public double getVolume() {
            return volume;
        }

        public void setVolume(double volume) {
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
