package business_objects.api.clickhouse_api_service.get_symbol_groups;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class GetSymbolGroupsResponse {

    @JsonProperty("group")
    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GetSymbolGroupsResponse that)) return false;
        return Objects.equals(group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(group);
    }

    @Override
    public String toString() {
        return "GetSymbolGroupsResponse{" + "group=" + group + '}';
    }
}
