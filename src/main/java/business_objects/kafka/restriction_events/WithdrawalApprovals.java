package business_objects.kafka.restriction_events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class WithdrawalApprovals {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("transferId")
    private Long transferId;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("regulator")
    private String regulator;

    @JsonProperty("internalReason")
    private String internalReason;

    @JsonProperty("status")
    private String status;

    @JsonProperty("orderNumber")
    private String orderNumber;

    @JsonProperty("checkName")
    private String checkName;

    public WithdrawalApprovals() {
    }

    public WithdrawalApprovals(String timestamp, String messageId, Long transferId, String brand, String regulator,
            String internalReason, String status, String orderNumber, String checkName) {
        this.setTimestamp(timestamp);
        this.setMessageId(messageId);
        this.setTransferId(transferId);
        this.setBrand(brand);
        this.setRegulator(regulator);
        this.setInternalReason(internalReason);
        this.setStatus(status);
        this.setOrderNumber(orderNumber);
        this.setCheckName(checkName);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public String getInternalReason() {
        return internalReason;
    }

    public void setInternalReason(String internalReason) {
        this.internalReason = internalReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WithdrawalApprovals that = (WithdrawalApprovals) o;
        return Objects.equals(transferId, that.transferId) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(internalReason, that.internalReason) && Objects.equals(status, that.status) && Objects.equals(orderNumber, that.orderNumber) && Objects.equals(checkName, that.checkName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, brand, regulator, internalReason, status, orderNumber, checkName);
    }

    @Override
    public String toString() {
        return "WithdrawalApprovals{" + "timestamp='" + timestamp + '\'' + ", messageId='" + messageId + '\'' + ", transferId=" + transferId + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", internalReason='" + internalReason + '\'' + ", status='" + status + '\'' + ", orderNumber='" + orderNumber + '\'' + ", checkName='" + checkName + '\'' + '}';
    }
}
