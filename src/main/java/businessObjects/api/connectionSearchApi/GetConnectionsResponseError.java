package businessObjects.api.connectionSearchApi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetConnectionsResponseError {

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("status")
    public Integer status;

    @JsonProperty("error")
    public String error;

    @JsonProperty("path")
    public String path;

    public GetConnectionsResponseError() {
    }

    public GetConnectionsResponseError(String timestamp, Integer status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetConnectionsResponseError that = (GetConnectionsResponseError) o;
        return Objects.equals(status, that.status) && Objects.equals(error, that.error) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, error, path);
    }

    @Override
    public String toString() {
        return "GetConnectionsResponseError{" +
                "timestamp='" + timestamp + '\'' +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
