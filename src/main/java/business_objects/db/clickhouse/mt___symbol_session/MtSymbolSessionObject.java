package business_objects.db.clickhouse.mt___symbol_session;

import java.util.Objects;

public class MtSymbolSessionObject {

    private String symbol;
    private Integer sourceIdSt;
    private Integer day;
    private String trade;

    public MtSymbolSessionObject(String symbol, Integer sourceIdSt, Integer day, String trade) {
        this.symbol = symbol;
        this.sourceIdSt = sourceIdSt;
        this.day = day;
        this.trade = trade;
    }

    public String getSymbol() {
        return symbol;
    }

    public Integer getSourceIdSt() {
        return sourceIdSt;
    }

    public Integer getDay() {
        return day;
    }

    public String getTrade() {
        return trade;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MtSymbolSessionObject that = (MtSymbolSessionObject) o;
        return Objects.equals(symbol, that.symbol)
                && Objects.equals(sourceIdSt, that.sourceIdSt)
                && Objects.equals(day, that.day)
                && Objects.equals(trade, that.trade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, sourceIdSt, day, trade);
    }
}
