package business_objects.api.clickhouse_api_service;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ClickhouseApiErrorResponse {

    @JsonProperty("error")
    String error;

    @JsonProperty("status")
    Integer status;

    @JsonProperty("type")
    String type;

    @JsonProperty("title")
    String title;

    @JsonProperty("detail")
    String detail;

    @JsonProperty("instance")
    String instance;

    public ClickhouseApiErrorResponse() {
    }

    public ClickhouseApiErrorResponse(
            String error, Integer status, String type, String title, String detail, String instance) {
        this.error = error;
        this.status = status;
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.instance = instance;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClickhouseApiErrorResponse that = (ClickhouseApiErrorResponse) o;
        return Objects.equals(error, that.error) && Objects.equals(status, that.status) && Objects.equals(
                type, that.type) && Objects.equals(title, that.title) && Objects.equals(detail, that.detail) && Objects.equals(
                        instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, status, type, title, detail, instance);
    }

    @Override
    public String toString() {
        return "GetBonusesResponseError{" + "error='" + error + '\'' + ", status=" + status + ", type='" + type + '\'' + ", title='" + title + '\'' + ", detail='" + detail + '\'' + ", instance='" + instance + '\'' + '}';
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }
}
