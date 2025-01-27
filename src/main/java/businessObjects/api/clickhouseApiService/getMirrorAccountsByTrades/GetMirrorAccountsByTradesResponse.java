package businessObjects.api.clickhouseApiService.getMirrorAccountsByTrades;

import com.fasterxml.jackson.annotation.JsonProperty;


public class GetMirrorAccountsByTradesResponse {

    @JsonProperty("originalTradingAccount")
    public String originalTradingAccount;

    @JsonProperty("originalServerId")
    public String originalServerId;

    @JsonProperty("originalVolumeInLots")
    public String originalVolumeInLots;

    @JsonProperty("mirrorTradingAccount")
    public String mirrorTradingAccount;

    @JsonProperty("mirrorServerId")
    public String mirrorServerId;

    @JsonProperty("mirrorVolumeInLots")
    public String mirrorVolumeInLots;

}
