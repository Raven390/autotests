package businessObjects.api.mitigationService;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PostRestrictionResponse {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("ucid")
    public String ucid;

    @JsonProperty("code")
    public String code;

    public PostRestrictionResponse() {
    }

    public PostRestrictionResponse(Integer id, String ucid, String code) {
        this.id = id;
        this.ucid = ucid;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PostRestrictionResponse that = (PostRestrictionResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(ucid, that.ucid) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ucid, code);
    }

    @Override
    public String toString() {
        return "PostRestrictionResponse{" + "id=" + id + ", ucid='" + ucid + '\'' + ", code='" + code + '\'' + '}';
    }
}
