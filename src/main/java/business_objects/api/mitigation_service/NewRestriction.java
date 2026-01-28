package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * NewRestriction
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = NewGeneralRestriction.class, name = "GENERAL"),
    @JsonSubTypes.Type(value = NewTradingRestriction.class, name = "TRADING")
})
public class NewRestriction {
    @JsonTypeId
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

    public NewRestriction type(RestrictionType type) {
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

    public NewRestriction ucid(String ucid) {
        this.ucid = ucid;
        return this;
    }

    /**
     * The unique identifier of the client.
     *
     * @return ucid
     **/
    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public NewRestriction code(String code) {
        this.code = code;
        return this;
    }

    /**
     * Code of restriction to apply.
     *
     * @return code
     **/
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public NewRestriction comment(String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * (optional): Explanation or trigger for the restriction.
     *
     * @return comment
     **/
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public NewRestriction correlationType(CorrelationType correlationType) {
        this.correlationType = correlationType;
        return this;
    }

    /**
     * Get correlationType
     *
     * @return correlationType
     **/
    public CorrelationType getCorrelationType() {
        return correlationType;
    }

    public void setCorrelationType(CorrelationType correlationType) {
        this.correlationType = correlationType;
    }

    public NewRestriction correlationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    /**
     * Get correlationId
     *
     * @return correlationId
     **/
    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public NewRestriction updatedBy(UpdatedBy updatedBy) {
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

    public NewRestriction additionalParams(List<AdditionalParamEntry> additionalParams) {
        this.additionalParams = additionalParams;
        return this;
    }

    public NewRestriction addAdditionalParamsItem(AdditionalParamEntry additionalParamsItem) {
        if (this.additionalParams == null) {
            this.additionalParams = new ArrayList<>();
        }
        this.additionalParams.add(additionalParamsItem);
        return this;
    }

    /**
     * Additional params
     *
     * @return additionalParams
     **/
    public List<AdditionalParamEntry> getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(List<AdditionalParamEntry> additionalParams) {
        this.additionalParams = additionalParams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewRestriction newRestriction = (NewRestriction) o;
        return Objects.equals(this.type, newRestriction.type)
                && Objects.equals(this.ucid, newRestriction.ucid)
                && Objects.equals(this.code, newRestriction.code)
                && Objects.equals(this.comment, newRestriction.comment)
                && Objects.equals(this.correlationType, newRestriction.correlationType)
                && Objects.equals(this.correlationId, newRestriction.correlationId)
                && Objects.equals(this.updatedBy, newRestriction.updatedBy)
                && Objects.equals(this.additionalParams, newRestriction.additionalParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, ucid, code, comment, correlationType, correlationId, updatedBy, additionalParams);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NewRestriction {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    ucid: ").append(toIndentedString(ucid)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
        sb.append("    correlationType: ")
                .append(toIndentedString(correlationType))
                .append("\n");
        sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
        sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
        sb.append("    additionalParams: ")
                .append(toIndentedString(additionalParams))
                .append("\n");
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
