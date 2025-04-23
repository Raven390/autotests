package business_objects.api.clickhouse_api_service.get_deposits;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetDepositsResponse {

    @JsonProperty("tradingAccount")
    public String tradingAccount;

    @JsonProperty("transferId")
    public String transferId;

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

    @JsonProperty("paymentType")
    public String paymentType;

    public GetDepositsResponse() {
    }

    public GetDepositsResponse(
            String tradingAccount, String transferId, String createTime, String clientId, Double actualAmountUsd,
            Double actualAmount, String paymentChannel, String paymentType) {
        this.tradingAccount = tradingAccount;
        this.transferId = transferId;
        this.createTime = createTime;
        this.clientId = clientId;
        this.actualAmountUsd = actualAmountUsd;
        this.actualAmount = actualAmount;
        this.paymentChannel = paymentChannel;
        this.paymentType = paymentType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetDepositsResponse that = (GetDepositsResponse) o;
        return Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(transferId, that.transferId) && Objects.equals(
                createTime, that.createTime) && Objects.equals(clientId, that.clientId) && Objects.equals(
                        actualAmountUsd, that.actualAmountUsd) && Objects.equals(actualAmount, that.actualAmount) && Objects.equals(
                                paymentChannel, that.paymentChannel) && Objects.equals(paymentType, that.paymentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradingAccount, transferId, createTime, clientId, actualAmountUsd, actualAmount, paymentChannel, paymentType);
    }

    @Override
    public String toString() {
        return "GetDepositsResponse{" + "tradingAccount=" + tradingAccount + ", transferId=" + transferId + ", createTime='" + createTime + '\'' + ", clientId='" + clientId + '\'' + ", actualAmountUsd=" + actualAmountUsd + ", actualAmount=" + actualAmount + ", paymentChannel='" + paymentChannel + '\'' + ", paymentType='" + paymentType + '\'' + '}';
    }
}
