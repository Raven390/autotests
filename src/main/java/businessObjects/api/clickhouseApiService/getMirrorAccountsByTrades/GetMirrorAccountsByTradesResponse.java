package businessObjects.api.clickhouseApiService.getMirrorAccountsByTrades;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetMirrorAccountsByTradesResponse {

    @JsonProperty("originalAccount")
    public originalAccount originalAccount;

    public static class originalAccount{
        @JsonProperty("tradingAccount")
        public String requestTradingAccount;

        @JsonProperty("serverId")
        public String requestServerId;

        @JsonProperty("volumeInLots")
        public String requestVolumeInLots;
    }

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
