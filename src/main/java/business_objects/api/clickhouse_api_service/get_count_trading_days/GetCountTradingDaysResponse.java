package business_objects.api.clickhouse_api_service.get_count_trading_days;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetCountTradingDaysResponse {

    @JsonProperty("count")
    Integer count;
}
