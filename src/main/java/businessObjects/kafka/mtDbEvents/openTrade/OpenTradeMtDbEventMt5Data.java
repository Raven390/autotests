package businessObjects.kafka.mtDbEvents.openTrade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1104118020/Open+trade
 */
public class OpenTradeMtDbEventMt5Data {
    @JsonProperty("Time")
    public String openTime;

    @JsonProperty("Deal")
    public Integer tradeId;

    @JsonProperty("Login")
    public Integer mtAccount;

    @JsonProperty("Volume")
    public Double volume;

    @JsonProperty("Symbol")
    public String symbol;

    @JsonProperty("Entry")
    public Integer entry;

    @JsonProperty("Action")
    public Integer action;

    @JsonProperty("ServerID")
    public Integer serverId;

    public OpenTradeMtDbEventMt5Data(
            String openTime, Integer tradeId, Integer mtAccount, Double volume, String symbol, Integer entry,
            Integer action, Integer serverId) {
        this.openTime = openTime;
        this.tradeId = tradeId;
        this.mtAccount = mtAccount;
        this.volume = volume;
        this.symbol = symbol;
        this.entry = entry;
        this.action = action;
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenTradeMtDbEventMt5Data that = (OpenTradeMtDbEventMt5Data) o;
        return Objects.equals(openTime, that.openTime) && Objects.equals(tradeId, that.tradeId) && Objects.equals(mtAccount, that.mtAccount) && Objects.equals(volume, that.volume) && Objects.equals(symbol, that.symbol) && Objects.equals(entry, that.entry) && Objects.equals(action, that.action) && Objects.equals(serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openTime, tradeId, mtAccount, volume, symbol, entry, action, serverId);
    }

    @Override
    public String toString() {
        return "OpenTradeMtDbEventMt5Data{" + "openTime='" + openTime + '\'' + ", tradeId=" + tradeId + ", mtAccount=" + mtAccount + ", volume=" + volume + ", symbol='" + symbol + '\'' + ", entry=" + entry + ", action=" + action + ", serverId=" + serverId + '}';
    }
}
