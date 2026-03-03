package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = GetGeneralRestrictionResponseBody.class, name = "GENERAL"),
    @JsonSubTypes.Type(value = GetTradingRestrictionResponseBody.class, name = "TRADING")
})
public class GetRestrictionResponseBody {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("code")
    public String code;

    @JsonProperty("type")
    public String type;

    @JsonProperty("comment")
    public String comment;

    @JsonProperty("cancelReason")
    public String cancelReason;

    @JsonProperty("updatedAt")
    public String updatedAt;

    @JsonProperty("createdAt")
    public String createdAt;

    @JsonProperty("updatedBy")
    public UpdatedBy updatedBy;

    @JsonProperty("accountId")
    public Integer accountId;

    @JsonProperty("account")
    public String account;

    @JsonProperty("serverId")
    public String serverId;

    @JsonProperty("editable")
    public String editable;
}
