package businessObjects.db.clickhouse.tsBySymbolDaily;

import java.util.Objects;

public class TsBySymbolDailyObject {

    public String ucid;
    public Long account;
    public String date;
    public String symbol;
    public Double totalPnl;
    public Double notionalValueUsd;
    public Long countWinDeals;
    public Long countTotalDeals;
    public Long countDealsLess10m;
    public Double totalToxicityUsd;
    public String lastUpdated;
    public Double totalNotionalValueUsd;

    public TsBySymbolDailyObject() {
    }

    public TsBySymbolDailyObject(String ucid, Long account, String date, String symbol, Double totalPnl,
            Double notionalValueUsd, Long countWinDeals, Long countTotalDeals, Long countDealsLess10m,
            Double totalToxicityUsd, String lastUpdated, Double totalNotionalValueUsd) {
        this.ucid = ucid;
        this.account = account;
        this.date = date;
        this.symbol = symbol;
        this.totalPnl = totalPnl;
        this.notionalValueUsd = notionalValueUsd;
        this.countWinDeals = countWinDeals;
        this.countTotalDeals = countTotalDeals;
        this.countDealsLess10m = countDealsLess10m;
        this.totalToxicityUsd = totalToxicityUsd;
        this.lastUpdated = lastUpdated;
        this.totalNotionalValueUsd = totalNotionalValueUsd;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TsBySymbolDailyObject that = (TsBySymbolDailyObject) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(account, that.account) && Objects.equals(date, that.date) && Objects.equals(symbol, that.symbol) && Objects.equals(totalPnl, that.totalPnl) && Objects.equals(notionalValueUsd, that.notionalValueUsd) && Objects.equals(countWinDeals, that.countWinDeals) && Objects.equals(countTotalDeals, that.countTotalDeals) && Objects.equals(countDealsLess10m, that.countDealsLess10m) && Objects.equals(totalToxicityUsd, that.totalToxicityUsd) && Objects.equals(lastUpdated, that.lastUpdated) && Objects.equals(totalNotionalValueUsd, that.totalNotionalValueUsd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, account, date, symbol, totalPnl, notionalValueUsd, countWinDeals, countTotalDeals, countDealsLess10m, totalToxicityUsd, lastUpdated, totalNotionalValueUsd);
    }

    @Override
    public String toString() {
        return "TsBySymbolDailyObject{" + "ucid='" + ucid + '\'' + ", account=" + account + ", date='" + date + '\'' + ", symbol='" + symbol + '\'' + ", totalPnl=" + totalPnl + ", notionalValueUsd=" + notionalValueUsd + ", countWinDeals=" + countWinDeals + ", countTotalDeals=" + countTotalDeals + ", countDealsLess10m=" + countDealsLess10m + ", totalToxicityUsd=" + totalToxicityUsd + ", lastUpdated='" + lastUpdated + '\'' + ", totalNotionalValueUsd=" + totalNotionalValueUsd + '}';
    }
}
