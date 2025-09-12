package business_objects.api.payment_gate.temp_decisions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostTempDecisionsResponseBody {

    // 201 Created: { "id": 223344 }
    @JsonProperty("id")
    private Long id;

    // Errors: { "error": "bad_request", "message": "decision is missing" }
    //         { "error": "not_found",  "message": "paymentId 23232 is not found" }
    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCreated() {
        return id != null && error == null && message == null;
    }

    public boolean isError() {
        return error != null && message != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostTempDecisionsResponseBody that = (PostTempDecisionsResponseBody) o;
        return Objects.equals(id, that.id) && Objects.equals(error, that.error) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, error, message);
    }

    @Override
    public String toString() {
        return "PostTempDecisionsResponseBody{" + "id=" + id + ", error='" + error + '\'' + ", message='" + message + '\'' + '}';
    }
}
