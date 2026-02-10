package business_objects.api.abuse_registry;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Objects;

public class PostFraudTypesV2RequestBody {

    @JsonProperty("actor")
    private String actor;

    @JsonProperty("system")
    private String system;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("canDeleteTradingConfirmed")
    private Boolean canDeleteTradingConfirmed;

    @JsonProperty("canDeletePaymentConfirmed")
    private Boolean canDeletePaymentConfirmed;

    @JsonProperty("fraudTypes")
    private List<FraudType> fraudTypes;

    public PostFraudTypesV2RequestBody() {}

    public PostFraudTypesV2RequestBody(String actor, String system, String comment, List<FraudType> fraudTypes) {
        this.actor = actor;
        this.system = system;
        this.comment = comment;
        this.fraudTypes = fraudTypes;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<FraudType> getFraudTypes() {
        return fraudTypes;
    }

    public void setFraudTypes(List<FraudType> fraudTypes) {
        this.fraudTypes = fraudTypes;
    }

    public Boolean getCanDeleteTradingConfirmed() {
        return canDeleteTradingConfirmed;
    }

    public void setCanDeleteTradingConfirmed(Boolean canDeleteTradingConfirmed) {
        this.canDeleteTradingConfirmed = canDeleteTradingConfirmed;
    }

    public Boolean getCanDeletePaymentConfirmed() {
        return canDeletePaymentConfirmed;
    }

    public void setCanDeletePaymentConfirmed(Boolean canDeletePaymentConfirmed) {
        this.canDeletePaymentConfirmed = canDeletePaymentConfirmed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PostFraudTypesV2RequestBody that = (PostFraudTypesV2RequestBody) o;
        return Objects.equals(actor, that.actor)
                && Objects.equals(system, that.system)
                && Objects.equals(comment, that.comment)
                && Objects.equals(fraudTypes, that.fraudTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actor, system, comment, fraudTypes);
    }

    @Override
    public String toString() {
        return "PostFraudTypesV2RequestBody{" + "actor='" + actor + '\'' + ", system='" + system + '\'' + ", comment='"
                + comment + '\'' + ", fraudTypes=" + fraudTypes + '}';
    }

    public static class FraudType {

        @JsonProperty("code")
        String code;

        @JsonProperty("status")
        String status;

        @JsonProperty("comment")
        String comment;

        @JsonProperty("subtypeCode")
        String subtypeCode;

        @JsonProperty("symbols")
        List<String> symbols;

        public FraudType(String code, String status, String comment, String subtypeCode, List<String> symbols) {
            this.code = code;
            this.status = status;
            this.comment = comment;
            this.subtypeCode = subtypeCode;
            this.symbols = symbols;
        }
    }
}
