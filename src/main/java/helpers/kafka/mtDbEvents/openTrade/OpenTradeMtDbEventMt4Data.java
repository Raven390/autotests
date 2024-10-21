package helpers.kafka.mtDbEvents.openTrade;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1104118020/Open+trade
 */
public class OpenTradeMtDbEventMt4Data {
    @JsonProperty("OPEN_TIME")
    public String openTime;

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

    @JsonProperty("CLOSE_TIME")
    public String closeTime;

    @JsonProperty("ServerID")
    public Integer serverId;

    public OpenTradeMtDbEventMt4Data(
                                     String openTime, Integer tradeId, Integer mtAccount, Double volume, String symbol, Integer cmd, String closeTime, Integer serverId) {
        this.openTime = openTime;
        this.tradeId = tradeId;
        this.mtAccount = mtAccount;
        this.volume = volume;
        this.symbol = symbol;
        this.cmd = cmd;
        this.closeTime = closeTime;
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenTradeMtDbEventMt4Data that = (OpenTradeMtDbEventMt4Data) o;
        return Objects.equals(openTime, that.openTime) && Objects.equals(tradeId, that.tradeId) && Objects.equals(mtAccount, that.mtAccount) && Objects.equals(volume, that.volume) && Objects.equals(symbol, that.symbol) && Objects.equals(cmd, that.cmd) && Objects.equals(closeTime, that.closeTime) && Objects.equals(serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openTime, tradeId, mtAccount, volume, symbol, cmd, closeTime, serverId);
    }

    @Override
    public String toString() {
        return "OpenTradeMtDbEventMt4Data{" + "openTime='" + openTime + '\'' + ", tradeId=" + tradeId + ", mtAccount=" + mtAccount + ", volume=" + volume + ", symbol='" + symbol + '\'' + ", cmd=" + cmd + ", closeTime='" + closeTime + '\'' + ", serverId=" + serverId + '}';
    }
}
