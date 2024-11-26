package businessObjects.api.clickhouseApiService.getDeposits;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetDepositsResponse {

    @JsonProperty("transferId")
    public Integer transferId;

    @JsonProperty("createTime")
    public String createTime;

    @JsonProperty("clientId")
    public String clientId;

    @JsonProperty("actualAmountUSD")
    public Double actualAmountUsd;

    @JsonProperty("actualAmount")
    public Double actualAmount;

    @JsonProperty("paymentChannel")
    public String paymentChannel;

    public GetDepositsResponse() {
    }

    public GetDepositsResponse(Integer transferId, String createTime, String clientId, Double actualAmountUsd, Double actualAmount, String paymentChannel) {
        this.transferId = transferId;
        this.createTime = createTime;
        this.clientId = clientId;
        this.actualAmountUsd = actualAmountUsd;
        this.actualAmount = actualAmount;
        this.paymentChannel = paymentChannel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetDepositsResponse that = (GetDepositsResponse) o;
        return Objects.equals(transferId, that.transferId) && Objects.equals(createTime, that.createTime) && Objects.equals(clientId, that.clientId) && Objects.equals(actualAmountUsd, that.actualAmountUsd) && Objects.equals(actualAmount, that.actualAmount) && Objects.equals(paymentChannel, that.paymentChannel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, createTime, clientId, actualAmountUsd, actualAmount, paymentChannel);
    }

    @Override
    public String toString() {
        return "GetDepositsResponse{" +
                "transferId=" + transferId +
                ", createTime='" + createTime + '\'' +
                ", clientId='" + clientId + '\'' +
                ", actualAmountUsd=" + actualAmountUsd +
                ", actualAmount=" + actualAmount +
                ", paymentChannel='" + paymentChannel + '\'' +
                '}';
    }
}
