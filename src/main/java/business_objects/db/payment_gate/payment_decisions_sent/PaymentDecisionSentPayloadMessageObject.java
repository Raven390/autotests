package business_objects.db.payment_gate.payment_decisions_sent;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDecisionSentPayloadMessageObject {

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
    private Integer clientId;

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

    @JsonProperty("rejectionReasonAttr")
    private String rejectionReasonAttr;

    @JsonProperty("rejectionReasonRecommend")
    private String rejectionReasonRecommend;

    @JsonProperty("paymentId")
    private String paymentId;

    @JsonProperty("underManualReview")
    private Integer underManualReview;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentDecisionSentPayloadMessageObject that)) return false;
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
                && Objects.equals(rejectionReasonAttr, that.rejectionReasonAttr)
                && Objects.equals(rejectionReasonRecommend, that.rejectionReasonRecommend)
                && Objects.equals(paymentId, that.paymentId)
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
                rejectionReasonAttr,
                rejectionReasonRecommend,
                paymentId,
                underManualReview);
    }

    @Override
    public String toString() {
        return "PaymentDecisionSentPayloadMessageObject{" + "schemaVersion='" + schemaVersion + '\'' + ", id='" + id
                + '\'' + ", timestamp='" + timestamp + '\'' + ", transferId=" + transferId + ", brand='" + brand + '\''
                + ", clientId=" + clientId + ", type='" + type + '\'' + ", regulator='" + regulator + '\''
                + ", internalReason='" + internalReason + '\'' + ", status='" + status + '\'' + ", merchantOrderId='"
                + merchantOrderId + '\'' + ", checkName='" + checkName + '\'' + ", ruleName='" + ruleName + '\''
                + ", rejectionReasonCode='" + rejectionReasonCode + '\'' + ", rejectionReason='" + rejectionReason
                + '\'' + ", rejectionReasonAttr='" + rejectionReasonAttr + '\'' + ", rejectionReasonRecommend='"
                + rejectionReasonRecommend + '\'' + ", paymentId='" + paymentId + '\'' + ", underManualReview="
                + underManualReview + '}';
    }
}
