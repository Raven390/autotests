package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostRestrictionByBitRequest {

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("code")
    public String code;

    @JsonProperty("account")
    public String account;

    @JsonProperty("comment")
    public String comment;

    @JsonProperty("updatedBy")
    public UpdatedBy updatedBy;

    public PostRestrictionByBitRequest(String ucid, String code, String account, String comment, UpdatedBy updatedBy) {
        this.ucid = ucid;
        this.code = code;
        this.account = account;
        this.comment = comment;
        this.updatedBy = updatedBy;
    }

    public PostRestrictionByBitRequest() {}

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UpdatedBy getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UpdatedBy updatedBy) {
        this.updatedBy = updatedBy;
    }

    public static class UpdatedBy {

        @JsonProperty("system")
        String system;

        @JsonProperty("user")
        String user;

        public String getSystem() {
            return system;
        }

        public void setSystem(String system) {
            this.system = system;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        @Override
        public String toString() {
            return "UpdatedBy{" + "system='" + system + '\'' + ", user='" + user + '\'' + '}';
        }

        public UpdatedBy() {}

        public UpdatedBy(String system, String user) {
            this.system = system;
            this.user = user;
        }
    }

    @Override
    public String toString() {
        return "PostRestrictionByBitRequest{" + "ucid='" + ucid + '\'' + ", code='" + code + '\'' + ", account='"
                + account + '\'' + ", comment='" + comment + '\'' + ", updatedBy=" + updatedBy + '}';
    }
}
