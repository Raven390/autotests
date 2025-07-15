package business_objects.api.abuse_registry;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PostAbuserStatusRequestBody {

    @JsonProperty("actor")
    private String actor;

    @JsonProperty("system")
    private String system;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("status")
    private String status;

    public PostAbuserStatusRequestBody() {
    }

    public PostAbuserStatusRequestBody(String actor, String system, String comment, String status) {
        this.actor = actor;
        this.system = system;
        this.comment = comment;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PostAbuserStatusRequestBody that = (PostAbuserStatusRequestBody) o;
        return Objects.equals(actor, that.actor) && Objects.equals(system, that.system) && Objects.equals(comment, that.comment) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actor, system, comment, status);
    }

    @Override
    public String toString() {
        return "PostAbuserStatusRequestBody{" + "actor='" + actor + '\'' + ", system='" + system + '\'' + ", comment='" + comment + '\'' + ", status='" + status + '\'' + '}';
    }
}
