package businessObjects.db.clickhouse.aggrCreditEquityRate;


import java.util.Objects;

public class AggrCreditEquityRateObject {
    public String serverId;
    public String tradingAccount;
    public String indicatorDate;
    public Double currentEquity;
    public Double sumCreditOrder;
    public Double creditEquityRatio;

    public AggrCreditEquityRateObject() {
    }

    public AggrCreditEquityRateObject(String serverId, String tradingAccount, String indicatorDate,
                                      Double currentEquity, Double sumCreditOrder, Double creditEquityRatio) {
        this.serverId = serverId;
        this.tradingAccount = tradingAccount;
        this.indicatorDate = indicatorDate;
        this.currentEquity = currentEquity;
        this.sumCreditOrder = sumCreditOrder;
        this.creditEquityRatio = creditEquityRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggrCreditEquityRateObject that = (AggrCreditEquityRateObject) o;
        return Objects.equals(serverId, that.serverId) && Objects.equals(tradingAccount, that.tradingAccount)
                && Objects.equals(indicatorDate, that.indicatorDate) && Objects.equals(currentEquity, that.currentEquity)
                && Objects.equals(sumCreditOrder, that.sumCreditOrder) && Objects.equals(creditEquityRatio, that.creditEquityRatio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, tradingAccount, indicatorDate,currentEquity,sumCreditOrder,creditEquityRatio);
    }

    @Override
    public String toString() {
        return "aggrsumCreditOrderByTradesObject{" +
                "serverId='" + serverId + '\'' +
                ", tradingAccount=" + tradingAccount +
                ", indicatorDate='" + indicatorDate + '\'' +
                ", currentEquity='" + currentEquity + '\'' +
                ", sumCreditOrder='" + sumCreditOrder + '\'' +
                ", creditEquityRatio='" + creditEquityRatio + '\'' +
                '}';
    }
}
