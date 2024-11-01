package businessObjects.api.mitigationService;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PostRestrictionResponse {

    @JsonProperty("id")
    public Integer id;

    public PostRestrictionResponse() {
    }

    public PostRestrictionResponse(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostRestrictionResponse that = (PostRestrictionResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PostRestrictionResponse{" +
                "id=" + id +
                '}';
    }
}
