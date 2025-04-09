package business_objects.kafka.mt_db_events.raf_balance_order;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1112440834/RAF+balance+order
 */
public class RafBalanceOrderMtDbEventMt4Data {
    @JsonProperty("OPEN_TIME")
    String openTime;

    @JsonProperty("TICKET")
    Integer tradeId;

    @JsonProperty("LOGIN")
    Integer mtAccount;

    @JsonProperty("COMMENT")
    String comment;

    @JsonProperty("CMD")
    Integer cmd;

    @JsonProperty("ServerID")
    Integer serverId;

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

    public String getOpenTime() {
        return openTime;
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getMtAccount() {
        return mtAccount;
    }

    public void setMtAccount(Integer mtAccount) {
        this.mtAccount = mtAccount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCmd() {
        return cmd;
    }

    public void setCmd(Integer cmd) {
        this.cmd = cmd;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }
}
