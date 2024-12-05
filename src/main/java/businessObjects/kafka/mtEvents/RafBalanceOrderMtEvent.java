package businessObjects.kafka.mtEvents;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1112440834/RAF+balance+order
 */
public class RafBalanceOrderMtEvent {
    @JsonProperty("id")
    public String id;

    @JsonProperty("openTime")
    public String openTime;

    @JsonProperty("tradeId")
    public Integer tradeId;

    @JsonProperty("tradingAccount")
    public Integer tradingAccount;

    @JsonProperty("comment")
    public String comment;

    @JsonProperty("serverId")
    public Integer serverId;

    @JsonProperty("type")
    public String type;

    public RafBalanceOrderMtEvent() {
    }

    public RafBalanceOrderMtEvent(
            String id, String openTime, Integer tradeId, Integer tradingAccount, String comment, Integer serverId) {
        this.id = id;
        this.openTime = openTime;
        this.tradeId = tradeId;
        this.tradingAccount = tradingAccount;
        this.comment = comment;
        this.serverId = serverId;
    }

    public RafBalanceOrderMtEvent(
            String openTime, Integer tradeId, Integer tradingAccount, String comment, Integer serverId, String type) {
        this.openTime = openTime;
        this.tradeId = tradeId;
        this.tradingAccount = tradingAccount;
        this.comment = comment;
        this.serverId = serverId;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RafBalanceOrderMtEvent that = (RafBalanceOrderMtEvent) o;
        return Objects.equals(openTime, that.openTime) && Objects.equals(tradeId, that.tradeId) && Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(comment, that.comment) && Objects.equals(serverId, that.serverId) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openTime, tradeId, tradingAccount, comment, serverId, type);
    }

    @Override
    public String toString() {
        return "RafBalanceOrderMtEvent{" + "id='" + id + '\'' + ", openTime='" + openTime + '\'' + ", tradeId=" + tradeId + ", tradingAccount=" + tradingAccount + ", comment='" + comment + '\'' + ", serverId=" + serverId + ", type='" + type + '\'' + '}';
    }
}
