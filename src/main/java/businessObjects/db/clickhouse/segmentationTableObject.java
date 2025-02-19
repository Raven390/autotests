package businessObjects.db.clickhouse;

import java.util.Objects;

public class segmentationTableObject {
    private String ucid;
    private String currency;
    private String deposit;
    private Double withdraw;
    private Double netClosedPnl;
    private Double floating;
    private Double tradingVolIn;
    private Double tradingVolOut;
    private Double swap;
    private Double commission;
    private Double spread;
    private Double revenue;
    private String segment;
    private String date;

    @Override
    public String toString() {
        return "segmentationTableObject{" + "ucid='" + ucid + '\'' + ", currency='" + currency + '\'' + ", deposit='" + deposit + '\'' + ", withdraw=" + withdraw + ", netClosedPnl=" + netClosedPnl + ", floating=" + floating + ", tradingVolIn=" + tradingVolIn + ", tradingVolOut=" + tradingVolOut + ", swap=" + swap + ", commission=" + commission + ", spread=" + spread + ", revenue=" + revenue + ", segment='" + segment + '\'' + ", date='" + date + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        segmentationTableObject that = (segmentationTableObject) o;
        return Objects.equals(ucid, that.ucid) && Objects.equals(currency, that.currency) && Objects.equals(deposit, that.deposit) && Objects.equals(withdraw, that.withdraw) && Objects.equals(netClosedPnl, that.netClosedPnl) && Objects.equals(floating, that.floating) && Objects.equals(tradingVolIn, that.tradingVolIn) && Objects.equals(tradingVolOut, that.tradingVolOut) && Objects.equals(swap, that.swap) && Objects.equals(commission, that.commission) && Objects.equals(spread, that.spread) && Objects.equals(revenue, that.revenue) && Objects.equals(segment, that.segment) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, currency, deposit, withdraw, netClosedPnl, floating, tradingVolIn, tradingVolOut, swap, commission, spread, revenue, segment, date);
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public Double getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(Double withdraw) {
        this.withdraw = withdraw;
    }

    public Double getNetClosedPnl() {
        return netClosedPnl;
    }

    public void setNetClosedPnl(Double netClosedPnl) {
        this.netClosedPnl = netClosedPnl;
    }

    public Double getFloating() {
        return floating;
    }

    public void setFloating(Double floating) {
        this.floating = floating;
    }

    public Double getTradingVolIn() {
        return tradingVolIn;
    }

    public void setTradingVolIn(Double tradingVolIn) {
        this.tradingVolIn = tradingVolIn;
    }

    public Double getTradingVolOut() {
        return tradingVolOut;
    }

    public void setTradingVolOut(Double tradingVolOut) {
        this.tradingVolOut = tradingVolOut;
    }

    public Double getSwap() {
        return swap;
    }

    public void setSwap(Double swap) {
        this.swap = swap;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getSpread() {
        return spread;
    }

    public void setSpread(Double spread) {
        this.spread = spread;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

