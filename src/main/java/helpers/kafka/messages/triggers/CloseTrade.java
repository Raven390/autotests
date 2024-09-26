package helpers.kafka.messages.triggers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1096351889/Close+trade
 */
public class CloseTrade {
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

    public static CloseTrade closeTrade(
            Date closeTime, int tradeId, int mtAccount, int volume, String symbol, int serverId) {
        CloseTrade closeTrade = new CloseTrade();
        closeTrade.closeTime = closeTime;
        closeTrade.tradeId = tradeId;
        closeTrade.mtAccount = mtAccount;
        closeTrade.volume = volume;
        closeTrade.symbol = symbol;
        closeTrade.serverId = serverId;
        return closeTrade;
    }
}
