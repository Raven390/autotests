package business_objects.api.clickhouse_api_service.get_free_margin;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class GetFreeMarginResponse {

    @JsonProperty
    private List<FreeMarginItem> freeMarginItems;

    @Data
    public static class FreeMarginItem {
        @JsonProperty("freeMarginUSD")
        private Double freeMarginUSD;

        @JsonProperty("equityUSD")
        private Double equityUSD;
    }
}
