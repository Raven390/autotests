package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CancelTradingRestriction.class, name = "TRADING"),
    @JsonSubTypes.Type(value = CancelGeneralRestriction.class, name = "GENERAL")
})
public class CancelRestrictionRequest {
    @JsonProperty("ucid")
    private String ucid = null;

    @JsonProperty("restrictionId")
    private Long restrictionId = null;

    @JsonProperty("type")
    private RestrictionType type = null;

    @JsonProperty("cancelReason")
    private String cancelReason = null;

    @JsonProperty("correlationType")
    private CorrelationType correlationType = null;

    @JsonProperty("correlationId")
    private String correlationId = null;

    @JsonProperty("updatedBy")
    private UpdatedBy updatedBy = null;

    public CancelRestrictionRequest ucid(String ucid) {
        this.ucid = ucid;
        return this;
    }

    /**
     * Unique identifier of the client
     * @return ucid
     **/
    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public CancelRestrictionRequest restrictionId(Long restrictionId) {
        this.restrictionId = restrictionId;
        return this;
    }

    /**
     * identifier of the restriction
     * @return restrictionId
     **/
    public Long getRestrictionId() {
        return restrictionId;
    }

    public void setRestrictionId(Long restrictionId) {
        this.restrictionId = restrictionId;
    }

    public CancelRestrictionRequest type(RestrictionType type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     * @return type
     **/
    public RestrictionType getType() {
        return type;
    }

    public void setType(RestrictionType type) {
        this.type = type;
    }

    public CancelRestrictionRequest cancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
        return this;
    }

    /**
     * The reason for canceling the restriction. Use only for system that can cancel restriction.
     * @return cancelReason
     **/
    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public CancelRestrictionRequest correlationType(CorrelationType correlationType) {
        this.correlationType = correlationType;
        return this;
    }

    /**
     * Get correlationType
     * @return correlationType
     **/
    public CorrelationType getCorrelationType() {
        return correlationType;
    }

    public void setCorrelationType(CorrelationType correlationType) {
        this.correlationType = correlationType;
    }

    public CancelRestrictionRequest correlationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    /**
     * Get correlationId
     * @return correlationId
     **/
    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public CancelRestrictionRequest updatedBy(UpdatedBy updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    /**
     * Get updatedBy
     * @return updatedBy
     **/
    public UpdatedBy getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UpdatedBy updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CancelRestrictionRequest cancelRestrictionRequestV3 = (CancelRestrictionRequest) o;
        return Objects.equals(this.ucid, cancelRestrictionRequestV3.ucid)
                && Objects.equals(this.restrictionId, cancelRestrictionRequestV3.restrictionId)
                && Objects.equals(this.type, cancelRestrictionRequestV3.type)
                && Objects.equals(this.cancelReason, cancelRestrictionRequestV3.cancelReason)
                && Objects.equals(this.correlationType, cancelRestrictionRequestV3.correlationType)
                && Objects.equals(this.correlationId, cancelRestrictionRequestV3.correlationId)
                && Objects.equals(this.updatedBy, cancelRestrictionRequestV3.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, restrictionId, type, cancelReason, correlationType, correlationId, updatedBy);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CancelRestrictionRequestV3 {\n");

        sb.append("    ucid: ").append(toIndentedString(ucid)).append("\n");
        sb.append("    restrictionId: ").append(toIndentedString(restrictionId)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    cancelReason: ").append(toIndentedString(cancelReason)).append("\n");
        sb.append("    correlationType: ")
                .append(toIndentedString(correlationType))
                .append("\n");
        sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
        sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
