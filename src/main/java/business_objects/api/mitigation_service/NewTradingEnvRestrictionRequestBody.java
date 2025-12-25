package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class NewTradingEnvRestrictionRequestBody {
    @JsonProperty("type")
    private RestrictionType type = null;

    @JsonProperty("ucid")
    private String ucid = null;

    @JsonProperty("code")
    private String code = null;

    @JsonProperty("comment")
    private String comment = null;

    @JsonProperty("correlationType")
    private CorrelationType correlationType = null;

    @JsonProperty("correlationId")
    private String correlationId = null;

    @JsonProperty("updatedBy")
    private UpdatedBy updatedBy = null;

    @JsonProperty("additionalParams")
    private List<AdditionalParamEntry> additionalParams = null;

    @JsonProperty("accountId")
    private BigInteger accountId = null;

    @JsonProperty("serverId")
    private Integer serverId = null;

    @JsonProperty("level")
    private String level = null;

    @JsonProperty("applicationReason")
    private String applicationReason = null;

    public NewTradingEnvRestrictionRequestBody() {}

    public NewTradingEnvRestrictionRequestBody(
            String applicationReason,
            String level,
            Integer serverId,
            BigInteger accountId,
            List<AdditionalParamEntry> additionalParams,
            UpdatedBy updatedBy,
            String correlationId,
            CorrelationType correlationType,
            String comment,
            String code,
            String ucid,
            RestrictionType type) {
        this.applicationReason = applicationReason;
        this.level = level;
        this.serverId = serverId;
        this.accountId = accountId;
        this.additionalParams = additionalParams;
        this.updatedBy = updatedBy;
        this.correlationId = correlationId;
        this.correlationType = correlationType;
        this.comment = comment;
        this.code = code;
        this.ucid = ucid;
        this.type = type;
    }

    public RestrictionType getType() {
        return type;
    }

    public String getUcid() {
        return ucid;
    }

    public String getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }

    public CorrelationType getCorrelationType() {
        return correlationType;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public UpdatedBy getUpdatedBy() {
        return updatedBy;
    }

    public List<AdditionalParamEntry> getAdditionalParams() {
        return additionalParams;
    }

    public BigInteger getAccountId() {
        return accountId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public String getLevel() {
        return level;
    }

    public String getApplicationReason() {
        return applicationReason;
    }

    public void setType(RestrictionType type) {
        this.type = type;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCorrelationType(CorrelationType correlationType) {
        this.correlationType = correlationType;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public void setUpdatedBy(UpdatedBy updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setAdditionalParams(List<AdditionalParamEntry> additionalParams) {
        this.additionalParams = additionalParams;
    }

    public void setAccountId(BigInteger accountId) {
        this.accountId = accountId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setApplicationReason(String applicationReason) {
        this.applicationReason = applicationReason;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        NewTradingEnvRestrictionRequestBody that = (NewTradingEnvRestrictionRequestBody) o;
        return type == that.type
                && Objects.equals(ucid, that.ucid)
                && Objects.equals(code, that.code)
                && Objects.equals(comment, that.comment)
                && correlationType == that.correlationType
                && Objects.equals(correlationId, that.correlationId)
                && Objects.equals(updatedBy, that.updatedBy)
                && Objects.equals(additionalParams, that.additionalParams)
                && Objects.equals(accountId, that.accountId)
                && Objects.equals(serverId, that.serverId)
                && Objects.equals(level, that.level)
                && Objects.equals(applicationReason, that.applicationReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                type,
                ucid,
                code,
                comment,
                correlationType,
                correlationId,
                updatedBy,
                additionalParams,
                accountId,
                serverId,
                level,
                applicationReason);
    }

    @Override
    public String toString() {
        return "NewTradingEnvRestrictionRequestBody{" + "type=" + type + ", ucid='" + ucid + '\'' + ", code='" + code
                + '\'' + ", comment='" + comment + '\'' + ", correlationType=" + correlationType + ", correlationId='"
                + correlationId + '\'' + ", updatedBy=" + updatedBy + ", additionalParams=" + additionalParams
                + ", accountId=" + accountId + ", serverId=" + serverId + ", level='" + level + '\''
                + ", applicationReason='" + applicationReason + '\'' + '}';
    }
}
