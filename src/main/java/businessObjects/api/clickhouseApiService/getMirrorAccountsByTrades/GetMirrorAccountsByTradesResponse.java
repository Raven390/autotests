package businessObjects.api.clickhouseApiService.getMirrorAccountsByTrades;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetMirrorAccountsByTradesResponse {

    @JsonProperty("requestTradingAccount")
    public String requestTradingAccount;

    @JsonProperty("requestServerId")
    public String requestServerId;

    @JsonProperty("requestVolumeInLots")
    public String requestVolumeInLots;

    @JsonProperty("Symbol")
    public String Symbol;

    @JsonProperty("mirrorAccounts")
    public List<MirrorAccounts> mirrorAccounts;

    public static class MirrorAccounts{

        @JsonProperty("tradingAccount")
        public String tradingAccount;

        @JsonProperty("serverId")
        public String serverId;

        @JsonProperty("volumeInLots")
        public String volumeInLots;

    }

}
