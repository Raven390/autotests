package business_objects.api.clickhouse_api_service.get_bonuses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class GetBonusesResponse {

    @JsonProperty("transferId")
    Integer transferId;

    @JsonProperty("createTime")
    String createTime;

    @JsonProperty("clientId")
    String clientId;

    @JsonProperty("actualAmountUSD")
    Double actualAmountUsd;

    @JsonProperty("actualAmount")
    Double actualAmount;

    @JsonProperty("bonusType")
    String bonusType;

    @JsonProperty("bonusGroup")
    String bonusGroup;

    public GetBonusesResponse() {}

    public GetBonusesResponse(
            Integer transferId,
            String createTime,
            String clientId,
            Double actualAmountUsd,
            Double actualAmount,
            String bonusType,
            String bonusGroup) {
        this.transferId = transferId;
        this.createTime = createTime;
        this.clientId = clientId;
        this.actualAmountUsd = actualAmountUsd;
        this.actualAmount = actualAmount;
        this.bonusType = bonusType;
        this.bonusGroup = bonusGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetBonusesResponse that = (GetBonusesResponse) o;
        return Objects.equals(transferId, that.transferId)
                && Objects.equals(createTime, that.createTime)
                && Objects.equals(clientId, that.clientId)
                && Objects.equals(actualAmountUsd, that.actualAmountUsd)
                && Objects.equals(actualAmount, that.actualAmount)
                && Objects.equals(bonusType, that.bonusType)
                && Objects.equals(bonusGroup, that.bonusGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, createTime, clientId, actualAmountUsd, actualAmount, bonusType, bonusGroup);
    }

    @Override
    public String toString() {
        return "GetBonusesResponse{" + "transferId=" + transferId + ", createTime='" + createTime + '\''
                + ", clientId='" + clientId + '\'' + ", actualAmountUsd=" + actualAmountUsd + ", actualAmount="
                + actualAmount + ", bonusType='" + bonusType + '\'' + ", bonusGroup='" + bonusGroup + '\'' + '}';
    }

    public Integer getTransferId() {
        return transferId;
    }

    public void setTransferId(Integer transferId) {
        this.transferId = transferId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Double getActualAmountUsd() {
        return actualAmountUsd;
    }

    public void setActualAmountUsd(Double actualAmountUsd) {
        this.actualAmountUsd = actualAmountUsd;
    }

    public Double getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Double actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
    }

    public String getBonusGroup() {
        return bonusGroup;
    }

    public void setBonusGroup(String bonusGroup) {
        this.bonusGroup = bonusGroup;
    }
}
