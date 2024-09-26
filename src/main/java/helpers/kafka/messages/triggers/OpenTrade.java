package helpers.kafka.messages.triggers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * Confluence link - https://vantagefx-hytechs.atlassian.net/wiki/spaces/AntiFraud/pages/1104118020/Open+trade
 */
public class OpenTrade {
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

    public static OpenTrade openTrade(
            Date openTime, int tradeId, int mtAccount, int volume, String symbol, int serverId) {
        OpenTrade openTrade = new OpenTrade();
        openTrade.openTime = openTime;
        openTrade.tradeId = tradeId;
        openTrade.mtAccount = mtAccount;
        openTrade.volume = volume;
        openTrade.symbol = symbol;
        openTrade.serverId = serverId;
        return openTrade;
    }
}
