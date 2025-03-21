package business_objects.kafka.mt_db_events.close_trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1096351889/Close+trade
 */
public class CloseTradeMtDbEventMt4Data {
    @JsonProperty("CLOSE_TIME")
    public String closeTime;

    @JsonProperty("MODIFY_TIME")
    public String modifyTime;

    @JsonProperty("TICKET")
    public Integer tradeId;

    @JsonProperty("LOGIN")
    public Integer mtAccount;

    @JsonProperty("VOLUME")
    public Double volume;

    @JsonProperty("SYMBOL")
    public String symbol;

    @JsonProperty("CMD")
    public Integer cmd;

    @JsonProperty("ServerID")
    public Integer serverId;

    public CloseTradeMtDbEventMt4Data(
            String closeTime, String modifyTime, int tradeId, int mtAccount, double volume, String symbol, int cmd,
            int serverId) {
        this.closeTime = closeTime;
        this.modifyTime = modifyTime;
        this.tradeId = tradeId;
        this.mtAccount = mtAccount;
        this.volume = volume;
        this.symbol = symbol;
        this.cmd = cmd;
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CloseTradeMtDbEventMt4Data that = (CloseTradeMtDbEventMt4Data) o;
        return Objects.equals(closeTime, that.closeTime) && Objects.equals(modifyTime, that.modifyTime) && Objects.equals(tradeId, that.tradeId) && Objects.equals(mtAccount, that.mtAccount) && Objects.equals(volume, that.volume) && Objects.equals(symbol, that.symbol) && Objects.equals(cmd, that.cmd) && Objects.equals(serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(closeTime, modifyTime, tradeId, mtAccount, volume, symbol, cmd, serverId);
    }

    @Override
    public String toString() {
        return "CloseTradeMtDbEventMt4Data{" + "closeTime='" + closeTime + '\'' + ", modifyTime='" + modifyTime + '\'' + ", tradeId=" + tradeId + ", mtAccount=" + mtAccount + ", volume=" + volume + ", symbol='" + symbol + '\'' + ", cmd=" + cmd + ", serverId=" + serverId + '}';
    }
}
