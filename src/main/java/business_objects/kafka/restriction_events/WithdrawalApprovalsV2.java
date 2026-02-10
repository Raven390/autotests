package business_objects.kafka.restriction_events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import lombok.Data;

@Data
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

    @JsonProperty("rejectionReasonAttr")
    private String rejectionReasonAttr;

    @JsonProperty("underManualReview")
    private Integer underManualReview;

    @JsonProperty("paymentId")
    private UUID paymentId;
}
