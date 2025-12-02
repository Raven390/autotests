package business_objects.api.clickhouse_api_service.get_mirror_trade_on_last_withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GetMirrorTradeOnLastWithdrawalResponse {

    @JsonProperty("suspiciousFlag")
    private Boolean suspiciousFlag;

    @JsonProperty("withdrawalApplicationTime")
    private String withdrawalApplicationTime;

    public GetMirrorTradeOnLastWithdrawalResponse() {
    }
}
