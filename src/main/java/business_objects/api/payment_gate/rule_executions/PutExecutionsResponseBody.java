package business_objects.api.payment_gate.rule_executions;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class PutExecutionsResponseBody {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("error")
    private String error;

    @JsonProperty("message")
    private String message;

    public PutExecutionsResponseBody() {
    }

    public PutExecutionsResponseBody(String Integer, String error, String message) {
        this.id = id;
        this.error = error;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PutExecutionsResponseBody that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(error, that.error) && Objects.equals(
                message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, error, message);
    }

    @Override
    public String toString() {
        return "RuleExecutionsResponseBodyFactory{" + "id='" + id + '\'' + ", error='" + error + '\'' + ", message='" + message + '\'' + '}';
    }
}
