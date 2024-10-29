package businessObjects.kafka.mtDbEvents.rafBalanceOrder;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1112440834/RAF+balance+order
 */
public class RafBalanceOrderMtDbEventMt5Data {
    @JsonProperty("Time")
    public String openTime;

    @JsonProperty("Deal")
    public Integer tradeId;

    @JsonProperty("Login")
    public Integer mtAccount;

    @JsonProperty("Comment")
    public String comment;

    @JsonProperty("Action")
    public Integer action;

    @JsonProperty("ServerID")
    public Integer serverId;

    public RafBalanceOrderMtDbEventMt5Data(
                                           String openTime, Integer tradeId, Integer mtAccount, String comment, Integer action, Integer serverId) {
        this.openTime = openTime;
        this.tradeId = tradeId;
        this.mtAccount = mtAccount;
        this.comment = comment;
        this.action = action;
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RafBalanceOrderMtDbEventMt5Data that = (RafBalanceOrderMtDbEventMt5Data) o;
        return Objects.equals(openTime, that.openTime) && Objects.equals(tradeId, that.tradeId) && Objects.equals(mtAccount, that.mtAccount) && Objects.equals(comment, that.comment) && Objects.equals(action, that.action) && Objects.equals(serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openTime, tradeId, mtAccount, comment, action, serverId);
    }

    @Override
    public String toString() {
        return "RafBalanceOrderMtDbEventMt5Data{" + "openTime='" + openTime + '\'' + ", tradeId=" + tradeId + ", mtAccount=" + mtAccount + ", comment='" + comment + '\'' + ", action=" + action + ", serverId=" + serverId + '}';
    }
}
