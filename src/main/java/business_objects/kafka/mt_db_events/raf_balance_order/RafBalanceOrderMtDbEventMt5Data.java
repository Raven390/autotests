package business_objects.kafka.mt_db_events.raf_balance_order;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1112440834/RAF+balance+order
 */
public class RafBalanceOrderMtDbEventMt5Data {
    @JsonProperty("Time")
    String openTime;

    @JsonProperty("Deal")
    Integer tradeId;

    @JsonProperty("Login")
    Integer mtAccount;

    @JsonProperty("Comment")
    String comment;

    @JsonProperty("Action")
    Integer action;

    @JsonProperty("ServerID")
    Integer serverId;

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

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
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

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }
}
