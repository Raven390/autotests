package business_objects.api.lark.chatHistory;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class ChatHistoryRequest {
    @JsonProperty("container_it_type") // Container type Options：chat：including p2p chat and group chat. thread: thread
    // Example value: "chat
    String containerItType;

    @JsonProperty(
            "container_id") // Container ID, which can be filled in: chat_id, refer to Group ID Description thread_id,
    // refer to Thread Introduction
    String containerId;

    @JsonProperty(
            "start_time") // Start time of historical information. Notice:The thread container type currently does not
    // support obtaining messages within a specified time range. Example value: "1608594809"
    String startTime;

    @JsonProperty("end_time") // End time of historical information. Notice:The thread container type currently does not
    // support obtaining messages within a specified time range. Example value: "1608594809"
    String endTime;

    @JsonProperty("sort_type") // message sorting type Example value: "ByCreateTimeAsc" Optional values are:
    // ByCreateTimeAsc：Sort by message creation time ascending ByCreateTimeDesc：Sort by message
    // creation time descending Default value: ByCreateTimeAsc
    String sortType;

    @JsonProperty("page_size") // Example value: 20 Default value: 20 Data validation rules: Value range: 1 ～ 50
    Integer pageSize;

    @JsonProperty("page_token") // Page identifier. It is not filled in the first request, indicating traversal from the
    // beginning; when there will be more groups, the new page_token will be returned at the same
    // time, and the next traversal can use the page_token to get more groups Example value:
    // "GxmvlNRvP0NdQZpa7yIqf_Lv_QuBwTQ8tXkX7w-irAghVD_TvuYd1aoJ1LQph86O-XImC4X9j9FhUPhXQDvtrQ"
    String pageToken;

    public String getContainerItType() {
        return containerItType;
    }

    public void setContainerItType(String containerItType) {
        this.containerItType = containerItType;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageToken() {
        return pageToken;
    }

    public void setPageToken(String pageToken) {
        this.pageToken = pageToken;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChatHistoryRequest that = (ChatHistoryRequest) o;
        return Objects.equals(containerItType, that.containerItType)
                && Objects.equals(containerId, that.containerId)
                && Objects.equals(startTime, that.startTime)
                && Objects.equals(endTime, that.endTime)
                && Objects.equals(sortType, that.sortType)
                && Objects.equals(pageSize, that.pageSize)
                && Objects.equals(pageToken, that.pageToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(containerItType, containerId, startTime, endTime, sortType, pageSize, pageToken);
    }

    @Override
    public String toString() {
        return "ChatHistoryRequest{" + "containerItType='" + containerItType + '\'' + ", containerId='" + containerId
                + '\'' + ", start_time='" + startTime + '\'' + ", endTime='" + endTime + '\'' + ", sortType='"
                + sortType + '\'' + ", pageSize=" + pageSize + ", pageToken='" + pageToken + '\'' + '}';
    }
}
