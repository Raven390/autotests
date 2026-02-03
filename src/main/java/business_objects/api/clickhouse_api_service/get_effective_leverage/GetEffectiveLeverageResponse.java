package business_objects.api.clickhouse_api_service.get_effective_leverage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetEffectiveLeverageResponse {

    @JsonProperty("dealId")
    String dealId;

    @JsonProperty("effectiveLeverage")
    Double effectiveLeverage;
}
