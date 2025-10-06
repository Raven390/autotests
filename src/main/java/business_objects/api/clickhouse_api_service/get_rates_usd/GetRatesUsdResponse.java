package business_objects.api.clickhouse_api_service.get_rates_usd;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GetRatesUsdResponse {

    @JsonProperty("rateUSD")
    private Double rateUsd;

    @JsonProperty("currency")
    private String currency;

    public Double getRateUsd() {
        return rateUsd;
    }

    public void setRateUsd(Double rateUsd) {
        this.rateUsd = rateUsd;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GetRatesUsdResponse that)) return false;
        return Objects.equals(rateUsd, that.rateUsd) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rateUsd, currency);
    }

    @Override
    public String toString() {
        return "GetRaterUsdResponse{" + "rateUsd='" + rateUsd + '\'' + ", currency='" + currency + '\'' + '}';
    }
}
