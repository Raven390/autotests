package helpers.kafka.mtEvents;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1096351889/Close+trade
 */
public class CloseTradeMtEvent {
    @JsonProperty("close_time")
    public Date closeTime;

    @JsonProperty("trade_id")
    public int tradeId;

    @JsonProperty("mt_account")
    public int mtAccount;

    @JsonProperty("volume")
    public int volume;

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("server_id")
    public int serverId;

    public static CloseTradeMtEvent closeTrade(
            Date closeTime, int tradeId, int mtAccount, int volume, String symbol, int serverId) {
        CloseTradeMtEvent event = new CloseTradeMtEvent();
        event.closeTime = closeTime;
        event.tradeId = tradeId;
        event.mtAccount = mtAccount;
        event.volume = volume;
        event.symbol = symbol;
        event.serverId = serverId;
        return event;
    }
}
