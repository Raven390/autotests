package businessObjects.db.clickhouse.aggrCreditEquityRate;


import java.util.Objects;

public class AggrCreditEquityRateObject {
    public String serverId;
    public String tradingAccount;
    public String currentEquityDate;
    public Double currentEquity;
    public String sumCreditOrderDate;
    public Double sumCreditOrder;
    public String creditEquityRatioDate;
    public Double creditEquityRatio;

    public AggrCreditEquityRateObject() {
    }

    public AggrCreditEquityRateObject(String serverId, String tradingAccount, String currentEquityDate,
                                      Double currentEquity, String sumCreditOrderDate, Double sumCreditOrder, String creditEquityRatioDate, Double creditEquityRatio) {
        this.serverId = serverId;
        this.tradingAccount = tradingAccount;
        this.currentEquityDate = currentEquityDate;
        this.currentEquity = currentEquity;
        this.sumCreditOrderDate = sumCreditOrderDate;
        this.sumCreditOrder = sumCreditOrder;
        this.creditEquityRatioDate = creditEquityRatioDate;
        this.creditEquityRatio = creditEquityRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggrCreditEquityRateObject that = (AggrCreditEquityRateObject) o;
        return Objects.equals(serverId, that.serverId) && Objects.equals(tradingAccount, that.tradingAccount)
                && Objects.equals(currentEquityDate, that.currentEquityDate) && Objects.equals(currentEquity, that.currentEquity)
            && Objects.equals(sumCreditOrderDate, that.sumCreditOrderDate) && Objects.equals(sumCreditOrder, that.sumCreditOrder)
            && Objects.equals(creditEquityRatioDate, that.creditEquityRatioDate) && Objects.equals(creditEquityRatio, that.creditEquityRatio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, tradingAccount, currentEquityDate, currentEquity, sumCreditOrderDate, sumCreditOrder,creditEquityRatioDate,creditEquityRatio);
    }

    @Override
    public String toString() {
        return "aggrsumCreditOrderByTradesObject{" +
                "serverId='" + serverId + '\'' +
                ", tradingAccount=" + tradingAccount +
                ", currentEquityDate='" + currentEquityDate + '\'' +
                ", currentEquity='" + currentEquity + '\'' +
                ", sumCreditOrderDate='" + sumCreditOrderDate + '\'' +
                ", sumCreditOrder='" + sumCreditOrder + '\'' +
                ", creditEquityRatioDate='" + creditEquityRatioDate + '\'' +
                ", creditEquityRatio='" + creditEquityRatio + '\'' +
                '}';
    }
}
