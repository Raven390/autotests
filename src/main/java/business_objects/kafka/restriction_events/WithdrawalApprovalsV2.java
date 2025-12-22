package business_objects.kafka.restriction_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawalApprovalsV2 {

    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("id")
    private String id;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("transferId")
    private Long transferId;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("clientId")
    private Long clientId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("regulator")
    private String regulator;

    @JsonProperty("internalReason")
    private String internalReason;

    @JsonProperty("status")
    private String status;

    @JsonProperty("merchantOrderId")
    private String merchantOrderId;

    @JsonProperty("checkName")
    private String checkName;

    @JsonProperty("ruleName")
    private String ruleName;

    @JsonProperty("rejectionReasonCode")
    private String rejectionReasonCode;

    @JsonProperty("rejectionReason")
    private String rejectionReason;

    @JsonProperty("rejectionReasonRecommend")
    private String rejectionReasonRecommend;

    @JsonProperty("underManualReview")
    private Integer underManualReview;

    @JsonProperty("paymentId")
    private UUID paymentId;

    public WithdrawalApprovalsV2() {}

    public WithdrawalApprovalsV2(
            String schemaVersion,
            String id,
            String timestamp,
            Long transferId,
            String brand,
            Long clientId,
            String type,
            String regulator,
            String internalReason,
            String status,
            String merchantOrderId,
            String checkName,
            String ruleName,
            String rejectionReasonCode,
            String rejectionReason,
            String rejectionReasonRecommend,
            Integer underManualReview) {
        this.schemaVersion = schemaVersion;
        this.id = id;
        this.timestamp = timestamp;
        this.transferId = transferId;
        this.brand = brand;
        this.clientId = clientId;
        this.type = type;
        this.regulator = regulator;
        this.internalReason = internalReason;
        this.status = status;
        this.merchantOrderId = merchantOrderId;
        this.checkName = checkName;
        this.ruleName = ruleName;
        this.rejectionReasonCode = rejectionReasonCode;
        this.rejectionReason = rejectionReason;
        this.rejectionReasonRecommend = rejectionReasonRecommend;
        this.underManualReview = underManualReview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawalApprovalsV2 that = (WithdrawalApprovalsV2) o;
        return Objects.equals(schemaVersion, that.schemaVersion)
                && Objects.equals(id, that.id)
                && Objects.equals(timestamp, that.timestamp)
                && Objects.equals(transferId, that.transferId)
                && Objects.equals(brand, that.brand)
                && Objects.equals(clientId, that.clientId)
                && Objects.equals(type, that.type)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(internalReason, that.internalReason)
                && Objects.equals(status, that.status)
                && Objects.equals(merchantOrderId, that.merchantOrderId)
                && Objects.equals(checkName, that.checkName)
                && Objects.equals(ruleName, that.ruleName)
                && Objects.equals(rejectionReasonCode, that.rejectionReasonCode)
                && Objects.equals(rejectionReason, that.rejectionReason)
                && Objects.equals(rejectionReasonRecommend, that.rejectionReasonRecommend)
                && Objects.equals(underManualReview, that.underManualReview);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                schemaVersion,
                id,
                timestamp,
                transferId,
                brand,
                clientId,
                type,
                regulator,
                internalReason,
                status,
                merchantOrderId,
                checkName,
                ruleName,
                rejectionReasonCode,
                rejectionReason,
                rejectionReasonRecommend,
                underManualReview);
    }

    @Override
    public String toString() {
        return "WithdrawalApprovalsV2{" + "schemaVersion=" + schemaVersion + ", id='" + id + '\'' + ", timestamp='"
                + timestamp + '\'' + ", transferId=" + transferId + ", brand='" + brand + '\'' + ", clientId="
                + clientId + ", type='" + type + '\'' + ", regulator='" + regulator + '\'' + ", internalReason='"
                + internalReason + '\'' + ", status='" + status + '\'' + ", merchantOrderId='" + merchantOrderId + '\''
                + ", checkName='" + checkName + '\'' + ", ruleName='" + ruleName + '\'' + ", rejectionReasonCode='"
                + rejectionReasonCode + '\'' + ", rejectionReason='" + rejectionReason + '\''
                + ", rejectionReasonRecommend='" + rejectionReasonRecommend + '\'' + ", underManualReview="
                + underManualReview + '}';
    }
}
