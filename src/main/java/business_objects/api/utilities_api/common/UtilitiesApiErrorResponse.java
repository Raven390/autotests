package business_objects.api.utilities_api.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class UtilitiesApiErrorResponse {

    @JsonProperty("error")
    String error;

    @JsonProperty("status")
    Integer status;

    public UtilitiesApiErrorResponse() {}

    public UtilitiesApiErrorResponse(String error, Integer status) {
        this.error = error;
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtilitiesApiErrorResponse that = (UtilitiesApiErrorResponse) o;
        return Objects.equals(error, that.error) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, status);
    }

    @Override
    public String toString() {
        return "UtilitiesApiErrorResponse{" + "error='" + error + '\'' + ", status=" + status + '}';
    }
}
