package businessObjects.kafka.mtDbEvents.rafBalanceOrder;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1112440834/RAF+balance+order
 */
public class RafBalanceOrderMtDbEventMt4Data {
    @JsonProperty("OPEN_TIME")
    public String openTime;

    @JsonProperty("TICKET")
    public Integer tradeId;

    @JsonProperty("LOGIN")
    public Integer mtAccount;

    @JsonProperty("COMMENT")
    public String comment;

    @JsonProperty("CMD")
    public Integer cmd;

    @JsonProperty("ServerID")
    public Integer serverId;

    public RafBalanceOrderMtDbEventMt4Data(
                                           String openTime, Integer tradeId, Integer mtAccount, String comment, Integer cmd, Integer serverId) {
        this.openTime = openTime;
        this.tradeId = tradeId;
        this.mtAccount = mtAccount;
        this.comment = comment;
        this.cmd = cmd;
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RafBalanceOrderMtDbEventMt4Data that = (RafBalanceOrderMtDbEventMt4Data) o;
        return Objects.equals(openTime, that.openTime) && Objects.equals(tradeId, that.tradeId) && Objects.equals(mtAccount, that.mtAccount) && Objects.equals(comment, that.comment) && Objects.equals(cmd, that.cmd) && Objects.equals(serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openTime, tradeId, mtAccount, comment, cmd, serverId);
    }

    @Override
    public String toString() {
        return "RafBalanceOrderMtDbEventMt4Data{" + "openTime='" + openTime + '\'' + ", tradeId=" + tradeId + ", mtAccount=" + mtAccount + ", comment='" + comment + '\'' + ", cmd=" + cmd + ", serverId=" + serverId + '}';
    }
}
