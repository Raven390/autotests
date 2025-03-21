package business_objects.db.clickhouse.aggr_credit_risk_free_revenue_ratio;


import java.util.Objects;

public class AggrCreditRiskFreeRevenueRatioObject {
    public String serverId;
    public String tradingAccount;
    public String currentRiskFreeRevenueDate;
    public Double currentRiskFreeRevenue;
    public String sumCreditOrderDate;
    public Double sumCreditOrder;
    public String creditRiskFreeRevenueRatioDate;
    public Double creditRiskFreeRevenueRatio;

    public AggrCreditRiskFreeRevenueRatioObject(
            String serverId, String tradingAccount, String currentRiskFreeRevenueDate, Double currentRiskFreeRevenue,
            String sumCreditOrderDate, Double sumCreditOrder, String creditRiskFreeRevenueRatioDate,
            Double creditRiskFreeRevenueRatio) {
        this.serverId = serverId;
        this.tradingAccount = tradingAccount;
        this.currentRiskFreeRevenueDate = currentRiskFreeRevenueDate;
        this.currentRiskFreeRevenue = currentRiskFreeRevenue;
        this.sumCreditOrderDate = sumCreditOrderDate;
        this.sumCreditOrder = sumCreditOrder;
        this.creditRiskFreeRevenueRatioDate = creditRiskFreeRevenueRatioDate;
        this.creditRiskFreeRevenueRatio = creditRiskFreeRevenueRatio;
    }

    public AggrCreditRiskFreeRevenueRatioObject() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggrCreditRiskFreeRevenueRatioObject that = (AggrCreditRiskFreeRevenueRatioObject) o;
        return Objects.equals(serverId, that.serverId) && Objects.equals(tradingAccount, that.tradingAccount) && Objects.equals(currentRiskFreeRevenue, that.currentRiskFreeRevenue) && Objects.equals(sumCreditOrderDate, that.sumCreditOrderDate) && Objects.equals(sumCreditOrder, that.sumCreditOrder) && Objects.equals(creditRiskFreeRevenueRatioDate, that.creditRiskFreeRevenueRatioDate) && Objects.equals(creditRiskFreeRevenueRatio, that.creditRiskFreeRevenueRatio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, tradingAccount, currentRiskFreeRevenueDate, currentRiskFreeRevenue, sumCreditOrderDate, sumCreditOrder, creditRiskFreeRevenueRatioDate, creditRiskFreeRevenueRatio);
    }

    @Override
    public String toString() {
        return "aggrsumCreditOrderByTradesObject{" + "serverId='" + serverId + '\'' + ", tradingAccount=" + tradingAccount + ", currentRiskFreeRevenueDate='" + currentRiskFreeRevenueDate + '\'' + ", currentRiskFreeRevenue='" + currentRiskFreeRevenue + '\'' + ", sumCreditOrderDate='" + sumCreditOrderDate + '\'' + ", sumCreditOrder='" + sumCreditOrder + '\'' + ", creditRiskFreeRevenueRatioDate='" + creditRiskFreeRevenueRatioDate + '\'' + ", creditRiskFreeRevenueRatioDate='" + creditRiskFreeRevenueRatio + '\'' + '}';
    }
}
