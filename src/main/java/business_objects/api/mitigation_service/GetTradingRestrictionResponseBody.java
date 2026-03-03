package business_objects.api.mitigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetTradingRestrictionResponseBody extends GetRestrictionResponseBody {

    @JsonProperty("status")
    public List<TradingStatus> status;

    @Data
    public static class TradingStatus {

        @JsonProperty("site")
        public Site site;

        @JsonProperty("status")
        public String status;
    }

    @Data
    public static class Site {
        @JsonProperty("name")
        public String name;

        @JsonProperty("cancellable")
        public Boolean cancellable;
    }
}
