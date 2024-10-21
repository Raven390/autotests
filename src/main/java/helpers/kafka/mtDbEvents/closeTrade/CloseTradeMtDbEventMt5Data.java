package helpers.kafka.mtDbEvents.closeTrade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1096351889/Close+trade
 */
public class CloseTradeMtDbEventMt5Data {
    @JsonProperty("Time")
    public String closeTime;

    @JsonProperty("Deal")
    public Integer tradeId;

    @JsonProperty("Login")
    public Integer mtAccount;

    @JsonProperty("VolumeClosedExt")
    public Double volume;

    @JsonProperty("Symbol")
    public String symbol;

    @JsonProperty("Entry")
    public Integer entry;

    @JsonProperty("Action")
    public Integer action;

    @JsonProperty("ServerID")
    public Integer serverId;

    public CloseTradeMtDbEventMt5Data(
            String closeTime,
            int tradeId,
            int mtAccount,
            double volume,
            String symbol,
            int entry,
            int action,
            int serverId) {
        this.closeTime = closeTime;
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
        CloseTradeMtDbEventMt5Data that = (CloseTradeMtDbEventMt5Data) o;
        return Objects.equals(closeTime, that.closeTime)
                && Objects.equals(tradeId, that.tradeId)
                && Objects.equals(mtAccount, that.mtAccount)
                && Objects.equals(volume, that.volume)
                && Objects.equals(symbol, that.symbol)
                && Objects.equals(entry, that.entry)
                && Objects.equals(action, that.action)
                && Objects.equals(serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(closeTime, tradeId, mtAccount, volume, symbol, entry, action, serverId);
    }

    @Override
    public String toString() {
        return "CloseTradeMtDbEventMt5Data{" + "closeTime='"
                + closeTime + '\'' + ", tradeId="
                + tradeId + ", mtAccount="
                + mtAccount + ", volume="
                + volume + ", symbol='"
                + symbol + '\'' + ", entry="
                + entry + ", action="
                + action + ", serverId="
                + serverId + '}';
    }
}
