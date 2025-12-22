package business_objects.api.clickhouse_api_service.get_withdrawals;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetCreditRiskFreeRevenueRatioResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
