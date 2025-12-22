package business_objects.db.ticks.rates_usd_current;

import java.util.Objects;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RatesUsdCurrentObject {
    private String currency;
    private String ts;
    private Double rate;

    @Override
    public String toString() {
        return "RatesUsdCurrentObject{" + "currency='" + currency + '\'' + ", ts='" + ts + '\'' + ", rate=" + rate
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RatesUsdCurrentObject that = (RatesUsdCurrentObject) o;
        return Double.compare(rate, that.rate) == 0
                && Objects.equals(currency, that.currency)
                && Objects.equals(ts, that.ts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, ts, rate);
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
