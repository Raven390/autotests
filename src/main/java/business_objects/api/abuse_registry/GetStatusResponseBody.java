package business_objects.api.abuse_registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GetStatusResponseBody {

    @JsonProperty("ucid")
    private String ucid;

    @JsonProperty("status")
    private String status;

    @JsonProperty("createdAt")
    private String createdAt;

    @JsonProperty("updatedAt")
    private String updatedAt;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("pendingProcessing")
    private String pendingProcessing;

    @JsonProperty("fraudTypes")
    private List<FraudType> fraudTypes;

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPendingProcessing() {
        return pendingProcessing;
    }

    public void setPendingProcessing(String pendingProcessing) {
        this.pendingProcessing = pendingProcessing;
    }

    public void setFraudTypes(List<FraudType> fraudTypes) {
        this.fraudTypes = fraudTypes;
    }

    public List<FraudType> getFraudTypes() {
        return fraudTypes;
    }

    public static class FraudType {
        @JsonProperty("status")
        private String status;

        @JsonProperty("code")
        private String code;

        @JsonProperty("name")
        private String name;

        @JsonProperty("comment")
        private String comment;

        @JsonProperty("updatedAt")
        private String updatedAt;

        @JsonProperty("description")
        private String description;

        @JsonProperty("subtypeCode")
        private String subtypeCode;

        @JsonProperty("subtypeName")
        private String subtypeName;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSubtypeCode() {
            return subtypeCode;
        }

        public void setSubtypeCode(String subtypeCode) {
            this.subtypeCode = subtypeCode;
        }

        public String getSubtypeName() {
            return subtypeName;
        }

        public void setSubtypeName(String subtypeName) {
            this.subtypeName = subtypeName;
        }
    }
}
