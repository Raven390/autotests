package businessObjects.db.clickhouse.aggrCreditEquityRate;


import java.util.Objects;

public class AggrCreditEquityRateObject {
    public String serverId;
    public String tradingAccount;
    public String currentRiskFreeRevenueDate;
    public Double currentRiskFreeRevenue;
    public String sumCreditOrderDate;
    public Double sumCreditOrder;
    public String creditRiskFreeRevenueRatioDate;
    public Double creditRiskFreeRevenueRatio;

    public AggrCreditEquityRateObject() {
    }

    public AggrCreditEquityRateObject(String serverId, String tradingAccount, String currentRiskFreeRevenueDate,
            Double currentRiskFreeRevenue, String sumCreditOrderDate, Double sumCreditOrder,
            String creditRiskFreeRevenueRatioDate,
            Double creditEquityRatio) {
        this.serverId = serverId;
        this.tradingAccount = tradingAccount;
        this.currentRiskFreeRevenueDate = currentRiskFreeRevenueDate;
        this.currentRiskFreeRevenue = currentRiskFreeRevenue;
        this.sumCreditOrderDate = sumCreditOrderDate;
        this.sumCreditOrder = sumCreditOrder;
        this.creditRiskFreeRevenueRatioDate = creditRiskFreeRevenueRatioDate;
        this.creditRiskFreeRevenueRatio = creditEquityRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggrCreditEquityRateObject that = (AggrCreditEquityRateObject) o;
        return Objects.equals(serverId, that.serverId) && Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(
                currentRiskFreeRevenueDate, that.currentRiskFreeRevenueDate) && Objects.equals(currentRiskFreeRevenue, that.currentRiskFreeRevenue) && Objects.equals(sumCreditOrderDate, that.sumCreditOrderDate) && Objects.equals(sumCreditOrder, that.sumCreditOrder) && Objects.equals(
                        creditRiskFreeRevenueRatioDate, that.creditRiskFreeRevenueRatioDate) && Objects.equals(
                                creditRiskFreeRevenueRatio, that.creditRiskFreeRevenueRatio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, tradingAccount, currentRiskFreeRevenueDate, currentRiskFreeRevenue, sumCreditOrderDate, sumCreditOrder, creditRiskFreeRevenueRatioDate, creditRiskFreeRevenueRatio);
    }

    @Override
    public String toString() {
        return "aggrsumCreditOrderByTradesObject{" + "serverId='" + serverId + '\'' + ", tradingAccount=" + tradingAccount + ", currentEquityDate='" + currentRiskFreeRevenueDate + '\'' + ", currentEquity='" + currentRiskFreeRevenue + '\'' + ", sumCreditOrderDate='" + sumCreditOrderDate + '\'' + ", sumCreditOrder='" + sumCreditOrder + '\'' + ", creditEquityRatioDate='" + creditRiskFreeRevenueRatioDate + '\'' + ", creditEquityRatio='" + creditRiskFreeRevenueRatio + '\'' + '}';
    }
}
