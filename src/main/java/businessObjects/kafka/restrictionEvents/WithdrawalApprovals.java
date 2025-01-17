package businessObjects.kafka.restrictionEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawalApprovals {

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("messageId")
    public String messageId;

    @JsonProperty("transferId")
    public Long transferId;

    @JsonProperty("brand")
    public String brand;

    @JsonProperty("regulator")
    public String regulator;

    @JsonProperty("internalReason")
    public String internalReason;

    @JsonProperty("status")
    public String status;

}
