package business_objects.api.clickhouse_api_service.get_cumulative_notional_value;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetCumulativeNotionalValueResponse {

    @JsonProperty("cumulativeNotionalValue")
    Double cumulativeNotionalValue;
}
