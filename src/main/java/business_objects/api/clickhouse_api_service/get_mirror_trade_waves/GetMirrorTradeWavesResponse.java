package business_objects.api.clickhouse_api_service.get_mirror_trade_waves;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetMirrorTradeWavesResponse {

    @JsonProperty("suspiciousFlag")
    public Boolean suspiciousFlag;
}
