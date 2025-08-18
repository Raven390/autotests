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

    @JsonProperty("fraudTypes")
    private List<FraudType> fraudTypes;

    public String getUcid() {
        return ucid;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getComment() {
        return comment;
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

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getComment() {
            return comment;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getDescription() {
            return description;
        }

        public String getSubtypeCode() {
            return subtypeCode;
        }

        public String getSubtypeName() {
            return subtypeName;
        }
    }
}
