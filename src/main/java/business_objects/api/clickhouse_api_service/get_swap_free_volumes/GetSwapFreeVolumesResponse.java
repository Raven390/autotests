package business_objects.api.clickhouse_api_service.get_swap_free_volumes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class GetSwapFreeVolumesResponse {

    @JsonProperty("indicatorDate")
    public String indicatorDate;

    @JsonProperty("tradingIndicators")
    public List<TradingIndicators> tradingIndicators;

    public static class TradingIndicators {

        @JsonProperty("volumeInitial")
        public String volumeInitial;

        @JsonProperty("volumeOpened")
        public String volumeOpened;

        @JsonProperty("volumeClosed")
        public String volumeClosed;

        @JsonProperty("volumeEndOfDay")
        public String volumeEndOfDay;
    }


}
