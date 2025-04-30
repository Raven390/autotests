package business_objects.kafka.mt_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1112440834/RAF+balance+order
 */
public class RafBalanceOrderMtEvent {
    @JsonProperty("id")
    String id;

    @JsonProperty("openTime")
    String openTime;

    @JsonProperty("tradeId")
    Integer tradeId;

    @JsonProperty("tradingAccount")
    Integer tradingAccount;

    @JsonProperty("comment")
    String comment;

    @JsonProperty("serverId")
    Integer serverId;

    @JsonProperty("type")
    String type;

    @JsonProperty("eventDate")
    String eventDate;

    @JsonProperty("metadata")
    TradeEventMetadata metadata;

    @JsonProperty("initialEventTime")
    String initialEventTime;

    public RafBalanceOrderMtEvent() {
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
        if (!(o instanceof RafBalanceOrderMtEvent that)) return false;
        return Objects.equals(openTime, that.openTime) && Objects.equals(tradeId, that.tradeId) && Objects.equals(
                tradingAccount, that.tradingAccount) && Objects.equals(comment, that.comment) && Objects.equals(
                        serverId, that.serverId) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openTime, tradeId, tradingAccount, comment, serverId, type);
    }

    @Override
    public String toString() {
        return "RafBalanceOrderMtEvent{" + "id='" + id + '\'' + ", openTime='" + openTime + '\'' + ", tradeId=" + tradeId + ", tradingAccount=" + tradingAccount + ", comment='" + comment + '\'' + ", serverId=" + serverId + ", type='" + type + '\'' + ", eventDate='" + eventDate + '\'' + ", metadata=" + metadata + ", initialEventTime='" + initialEventTime + '\'' + '}';
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getTradingAccount() {
        return tradingAccount;
    }

    public void setTradingAccount(Integer tradingAccount) {
        this.tradingAccount = tradingAccount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getInitialEventTime() {
        return initialEventTime;
    }

    public void setInitialEventTime(String initialEventTime) {
        this.initialEventTime = initialEventTime;
    }
}
