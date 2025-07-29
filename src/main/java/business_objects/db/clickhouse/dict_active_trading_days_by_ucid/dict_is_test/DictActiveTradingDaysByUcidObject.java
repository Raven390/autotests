package business_objects.db.clickhouse.dict_active_trading_days_by_ucid.dict_is_test;

import java.util.Objects;


public class DictActiveTradingDaysByUcidObject {
    // Declare variables
    public String ucid;
    public String tradeDate;

    public DictActiveTradingDaysByUcidObject() {
    }

    public DictActiveTradingDaysByUcidObject(String ucid, String tradeDate) {
        this.ucid = ucid;
        this.tradeDate = tradeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DictActiveTradingDaysByUcidObject that)) return false;
        return Objects.equals(ucid, that.ucid) && Objects.equals(tradeDate, that.tradeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, tradeDate);
    }

    @Override
    public String toString() {
        return "DictActiveTradingDaysByUcidObject{" + "ucid='" + ucid + '\'' + ", tradeDate='" + tradeDate + '\'' + '}';
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
}