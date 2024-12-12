package businessObjects.api.clickhouseApiService.getCreditRiskFreeRevenueRatio.getWithdrawals;

import com.fasterxml.jackson.annotation.JsonProperty;


public class GetCreditRiskFreeRevenueRatioResponseError {

    @JsonProperty("error")
    public String error;

    @JsonProperty("status")
    public String status;
}
