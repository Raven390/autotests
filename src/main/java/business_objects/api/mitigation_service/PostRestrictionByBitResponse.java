package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class PostRestrictionByBitResponse {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("status")
    public String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        PostRestrictionByBitResponse that = (PostRestrictionByBitResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }

    @Override
    public String toString() {
        return "PostRestrictionByBitResponse{" + "id=" + id + ", status='" + status + '\'' + '}';
    }
}
