package businessObjects.kafka.mtEvents;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1104118020/Open+trade
 */
public class OpenTradeMtEvent {
    @JsonProperty("id")
    public String id;

    @JsonProperty("openTime")
    public String openTime;

    @JsonProperty("tradeId")
    public Integer tradeId;

    @JsonProperty("tradingAccount")
    public Integer tradingAccount;

    @JsonProperty("volume")
    public Double volume;

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("serverId")
    public Integer serverId;

    @JsonProperty("type")
    public String type;

    public OpenTradeMtEvent() {
    }

    public OpenTradeMtEvent(
            String id, String openTime, Integer tradeId, Integer tradingAccount, Double volume, String symbol,
            Integer serverId) {
        this.id = id;
        this.openTime = openTime;
        this.tradeId = tradeId;
        this.tradingAccount = tradingAccount;
        this.volume = volume;
        this.symbol = symbol;
        this.serverId = serverId;
    }

    public OpenTradeMtEvent(
            String openTime, Integer tradeId, Integer tradingAccount, Double volume, String symbol, Integer serverId,
            String type) {
        this.openTime = openTime;
        this.tradeId = tradeId;
        this.tradingAccount = tradingAccount;
        this.volume = volume;
        this.symbol = symbol;
        this.serverId = serverId;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenTradeMtEvent that = (OpenTradeMtEvent) o;
        return Objects.equals(openTime, that.openTime) && Objects.equals(tradeId, that.tradeId) && Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(volume, that.volume) && Objects.equals(symbol, that.symbol) && Objects.equals(serverId, that.serverId) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openTime, tradeId, tradingAccount, volume, symbol, serverId, type);
    }

    @Override
    public String toString() {
        return "OpenTradeMtEvent{" + "id='" + id + '\'' + ", openTime='" + openTime + '\'' + ", tradeId=" + tradeId + ", tradingAccount=" + tradingAccount + ", volume=" + volume + ", symbol='" + symbol + '\'' + ", serverId=" + serverId + ", type='" + type + '\'' + '}';
    }
}
