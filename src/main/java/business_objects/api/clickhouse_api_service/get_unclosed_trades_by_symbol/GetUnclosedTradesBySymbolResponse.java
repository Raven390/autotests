package business_objects.api.clickhouse_api_service.get_unclosed_trades_by_symbol;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetUnclosedTradesBySymbolResponse {

    @JsonProperty("symbol")
    public String symbol;

    @JsonProperty("tradeCount")
    public Integer tradeCount;

    @JsonProperty("percentage")
    public Double percentage;
}
