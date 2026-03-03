package business_objects.api.abuse_registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostFraudTypesRequestBody {

    @JsonProperty("actor")
    private String actor;

    @JsonProperty("system")
    private String system;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("fraudTypes")
    private List<FraudTypeWithStatus> fraudTypes;

    public PostFraudTypesRequestBody() {}

    public PostFraudTypesRequestBody(
            String actor, String system, String comment, List<FraudTypeWithStatus> fraudTypes) {
        this.actor = actor;
        this.system = system;
        this.comment = comment;
        this.fraudTypes = fraudTypes;
    }

    @Override
    public String toString() {
        return "PostFraudTypesRequestBody{" + "actor='" + actor + '\'' + ", system='" + system + '\'' + ", comment='"
                + comment + '\'' + ", fraudTypes=" + fraudTypes + '}';
    }

    public static class FraudTypeWithStatus {

        @JsonProperty("status")
        String status;

        @JsonProperty("code")
        String code;

        public FraudTypeWithStatus(String status, String code) {
            this.status = status;
            this.code = code;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            FraudTypeWithStatus that = (FraudTypeWithStatus) o;
            return Objects.equals(status, that.status) && Objects.equals(code, that.code);
        }

        @Override
        public int hashCode() {
            return Objects.hash(status, code);
        }

        @Override
        public String toString() {
            return "FraudTypeWithStatus{" + "status='" + status + '\'' + ", code='" + code + '\'' + '}';
        }
    }
}
