package businessObjects.api.connectionSearchApi.getConnections;

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

    @JsonProperty("type")
    public String type;

    @JsonProperty("title")
    public String title;

    @JsonProperty("detail")
    public String detail;

    @JsonProperty("instance")
    public String instance;

    public GetConnectionsResponseError() {
    }

    public GetConnectionsResponseError(String timestamp, Integer status, String error, String path, String type,
            String title, String detail, String instance) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.instance = instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetConnectionsResponseError that = (GetConnectionsResponseError) o;
        return Objects.equals(timestamp, that.timestamp) && Objects.equals(status, that.status) && Objects.equals(error, that.error) && Objects.equals(path, that.path) && Objects.equals(type, that.type) && Objects.equals(title, that.title) && Objects.equals(detail, that.detail) && Objects.equals(instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, status, error, path, type, title, detail, instance);
    }

    @Override
    public String toString() {
        return "GetConnectionsResponseError{" + "timestamp='" + timestamp + '\'' + ", status=" + status + ", error='" + error + '\'' + ", path='" + path + '\'' + ", type='" + type + '\'' + ", title='" + title + '\'' + ", detail='" + detail + '\'' + ", instance='" + instance + '\'' + '}';
    }
}
