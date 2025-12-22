package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * ClientRestriction
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ClientGeneralRestriction.class, name = "GENERAL"),
    @JsonSubTypes.Type(value = ClientTradingRestriction.class, name = "TRADING"),
    @JsonSubTypes.Type(value = ClientTradingEnvironmentRestriction.class, name = "TRADING_ENVIRONMENT"),
    @JsonSubTypes.Type(value = ClientBybitRestriction.class, name = "BYBIT"),
})
public class ClientRestriction {
    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("code")
    private String code = null;

    @JsonTypeId
    private RestrictionType type = null;

    @JsonProperty("comment")
    private String comment = null;

    @JsonProperty("cancelReason")
    private String cancelReason = null;

    @JsonProperty("updatedAt")
    private OffsetDateTime updatedAt = null;

    @JsonProperty("createdAt")
    private OffsetDateTime createdAt = null;

    @JsonProperty("updatedBy")
    private UpdatedBy updatedBy = null;

    @JsonProperty("editable")
    private Boolean editable = null;

    public ClientRestriction id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Applied restriction identifier
     *
     * @return id
     **/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientRestriction code(String code) {
        this.code = code;
        return this;
    }

    /**
     * The unique code of specific restriction
     *
     * @return code
     **/
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ClientRestriction type(RestrictionType type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     **/
    public RestrictionType getType() {
        return type;
    }

    public void setType(RestrictionType type) {
        this.type = type;
    }

    public ClientRestriction comment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * The reason for applying the restriction: - For restrictions from RE, this should be the name of the triggered
     * rule. - For other systems, it should be a comment.
     *
     * @return comment
     **/
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ClientRestriction cancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
        return this;
    }

    /**
     * The reason for canceling the restriction. Use only for system that can cancel restriction.
     *
     * @return cancelReason
     **/
    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public ClientRestriction updatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Timestamp when were last updates of the restriction status.
     *
     * @return updatedAt
     **/
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ClientRestriction createdAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Timestamp of restriction creating.
     *
     * @return createdAt
     **/
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ClientRestriction updatedBy(UpdatedBy updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    /**
     * Get updatedBy
     *
     * @return updatedBy
     **/
    public UpdatedBy getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UpdatedBy updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ClientRestriction editable(Boolean editable) {
        this.editable = editable;
        return this;
    }

    /**
     * Get editable
     *
     * @return editable
     **/
    public Boolean isEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientRestriction clientRestriction = (ClientRestriction) o;
        return Objects.equals(this.id, clientRestriction.id)
                && Objects.equals(this.code, clientRestriction.code)
                && Objects.equals(this.type, clientRestriction.type)
                && Objects.equals(this.comment, clientRestriction.comment)
                && Objects.equals(this.cancelReason, clientRestriction.cancelReason)
                && Objects.equals(this.updatedAt, clientRestriction.updatedAt)
                && Objects.equals(this.createdAt, clientRestriction.createdAt)
                && Objects.equals(this.updatedBy, clientRestriction.updatedBy)
                && Objects.equals(this.editable, clientRestriction.editable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, type, comment, cancelReason, updatedAt, createdAt, updatedBy, editable);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ClientRestriction {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
        sb.append("    cancelReason: ").append(toIndentedString(cancelReason)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
        sb.append("    editable: ").append(toIndentedString(editable)).append("\n");
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
