package business_objects.api.payment_gate.aggr_by_ucid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAggrByUcidResponse {
    @JsonProperty("count")
    private Integer count;

    @JsonProperty("lastDateCreated")
    private String lastDateCreated;

    @JsonProperty("lastPaymentMethodCode")
    private String lastPaymentMethodCode;

    @JsonProperty("lastWithdrawalAmountUSD")
    private Integer lastWithdrawalAmountUSD;

    public GetAggrByUcidResponse() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getLastDateCreated() {
        return lastDateCreated;
    }

    public void setLastDateCreated(String lastDateCreated) {
        this.lastDateCreated = lastDateCreated;
    }

    public String getLastPaymentMethodCode() {
        return lastPaymentMethodCode;
    }

    public void setLastPaymentMethodCode(String lastPaymentMethodCode) {
        this.lastPaymentMethodCode = lastPaymentMethodCode;
    }

    public Integer getLastWithdrawalAmountUSD() {
        return lastWithdrawalAmountUSD;
    }

    public void setLastWithdrawalAmountUSD(Integer lastWithdrawalAmountUSD) {
        this.lastWithdrawalAmountUSD = lastWithdrawalAmountUSD;
    }
}
