package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.Objects;

public class PostRestrictionRequestBody {

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("code")
    public String code;

    @JsonProperty("type")
    public String type;

    @JsonProperty("accountId")
    public Integer accountId;

    @JsonProperty("serverId")
    public Integer serverId;

    @JsonProperty("comment")
    public String comment;

    @JsonProperty("updatedBy")
    public UpdatedBy updatedBy;

    @JsonProperty("additionalProperties")
    public AdditionalProperty[] additionalProperties;

    public PostRestrictionRequestBody() {
    }

    public PostRestrictionRequestBody(String ucid, String code, String type, Integer accountId, Integer serverId,
            String comment, UpdatedBy updatedBy) {
        this.ucid = ucid;
        this.code = code;
        this.type = type;
        this.accountId = accountId;
        this.serverId = serverId;
        this.comment = comment;
        this.updatedBy = updatedBy;
    }

    public PostRestrictionRequestBody(String ucid, String code, String type, Integer accountId, Integer serverId,
            String comment, UpdatedBy updatedBy, AdditionalProperty[] additionalProperties) {
        this.ucid = ucid;
        this.code = code;
        this.type = type;
        this.accountId = accountId;
        this.serverId = serverId;
        this.comment = comment;
        this.updatedBy = updatedBy;
        this.additionalProperties = additionalProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PostRestrictionRequestBody that = (PostRestrictionRequestBody) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(code, that.code) && Objects.equals(type, that.type) && Objects.equals(accountId, that.accountId) && Objects.equals(serverId, that.serverId) && Objects.equals(comment, that.comment) && Objects.equals(updatedBy, that.updatedBy) && Objects.deepEquals(additionalProperties, that.additionalProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, code, type, accountId, serverId, comment, updatedBy, Arrays.hashCode(additionalProperties));
    }

    @Override
    public String toString() {
        return "PostRestrictionRequestBody{" + "ucid='" + ucid + '\'' + ", code='" + code + '\'' + ", type='" + type + '\'' + ", accountId=" + accountId + ", serverId=" + serverId + ", comment='" + comment + '\'' + ", updatedBy=" + updatedBy + ", additionalProperties=" + Arrays.toString(additionalProperties) + '}';
    }

    public static class UpdatedBy {

        @JsonProperty("system")
        String system;

        @JsonProperty("user")
        String user;

        public UpdatedBy() {
        }

        public UpdatedBy(String system, String user) {
            this.system = system;
            this.user = user;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UpdatedBy updatedBy = (UpdatedBy) o;
            return Objects.equals(system, updatedBy.system) && Objects.equals(user, updatedBy.user);
        }

        @Override
        public int hashCode() {
            return Objects.hash(system, user);
        }

        @Override
        public String toString() {
            return "UpdatedBy{" + "system='" + system + '\'' + ", user='" + user + '\'' + '}';
        }
    }

    public static class AdditionalProperty {

        @JsonProperty("name")
        String name;

        @JsonProperty("type")
        String type;

        @JsonProperty("value")
        Object value;

        public AdditionalProperty() {
        }

        public AdditionalProperty(String name, String type, Object value) {
            this.name = name;
            this.type = type;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            AdditionalProperty that = (AdditionalProperty) o;
            return Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, type, value);
        }

        @Override
        public String toString() {
            return "AdditionalProperty{" + "name='" + name + '\'' + ", type='" + type + '\'' + ", value='" + value + '\'' + '}';
        }
    }
}
