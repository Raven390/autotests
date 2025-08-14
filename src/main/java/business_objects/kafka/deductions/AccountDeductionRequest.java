package business_objects.kafka.deductions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AccountDeductionRequest {

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("messageId")
    private String messageId;

    @JsonProperty("serverId")
    private Integer serverId;

    @JsonProperty("login")
    private Integer login;

    @JsonProperty("updateType")
    private String updateType;

    @JsonProperty("uploadCurrency")
    private String uploadCurrency;

    @JsonProperty("uploadAmount")
    private Double uploadAmount;

    @JsonProperty("comment")
    private String comment;

    public AccountDeductionRequest() {
    }

    public AccountDeductionRequest(String timestamp, String messageId, Integer serverId, Integer login,
            String updateType, String uploadCurrency, Double uploadAmount, String comment) {
        this.timestamp = timestamp;
        this.messageId = messageId;
        this.serverId = serverId;
        this.login = login;
        this.updateType = updateType;
        this.uploadCurrency = uploadCurrency;
        this.uploadAmount = uploadAmount;
        this.comment = comment;
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

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Integer getLogin() {
        return login;
    }

    public void setLogin(Integer login) {
        this.login = login;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getUploadCurrency() {
        return uploadCurrency;
    }

    public void setUploadCurrency(String uploadCurrency) {
        this.uploadCurrency = uploadCurrency;
    }

    public Double getUploadAmount() {
        return uploadAmount;
    }

    public void setUploadAmount(Double uploadAmount) {
        this.uploadAmount = uploadAmount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AccountDeductionRequest that = (AccountDeductionRequest) o;
        return Objects.equals(serverId, that.serverId) && Objects.equals(login, that.login) && Objects.equals(updateType, that.updateType) && Objects.equals(uploadCurrency, that.uploadCurrency) && Objects.equals(uploadAmount, that.uploadAmount) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, login, updateType, uploadCurrency, uploadAmount, comment);
    }

    @Override
    public String toString() {
        return "AccountDeductionRequest{" + "timestamp='" + timestamp + '\'' + ", messageId='" + messageId + '\'' + ", serverId=" + serverId + ", login=" + login + ", updateType='" + updateType + '\'' + ", uploadCurrency='" + uploadCurrency + '\'' + ", uploadAmount=" + uploadAmount + ", comment='" + comment + '\'' + '}';
    }
}
