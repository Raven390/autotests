package business_objects.api.clickhouse_api_service.get_mirror_trade_on_withdrawal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMirrorTradeOnWithdrawalResponse {

    @JsonProperty("suspiciousFlag")
    private Boolean suspiciousFlag;

    @JsonProperty("withdrawalApplicationTime")
    private String withdrawalApplicationTime;
}
