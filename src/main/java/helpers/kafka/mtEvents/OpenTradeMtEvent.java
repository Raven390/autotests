package helpers.kafka.mtEvents;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1104118020/Open+trade
 */
public class OpenTradeMtEvent {
    @JsonProperty("open_time")
    public Date openTime;

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

    public static OpenTradeMtEvent openTrade(
            Date openTime, int tradeId, int mtAccount, int volume, String symbol, int serverId) {
        OpenTradeMtEvent event = new OpenTradeMtEvent();
        event.openTime = openTime;
        event.tradeId = tradeId;
        event.mtAccount = mtAccount;
        event.volume = volume;
        event.symbol = symbol;
        event.serverId = serverId;
        return event;
    }
}
