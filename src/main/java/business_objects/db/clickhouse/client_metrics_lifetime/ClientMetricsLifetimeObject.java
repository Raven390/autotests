package business_objects.db.clickhouse.client_metrics_lifetime;

import java.util.Objects;

public class ClientMetricsLifetimeObject {

    public String ucid;
    public String brand;
    public String regulator;
    public Long userId;
    public Double totalPnl;
    public Double totalDeposit;
    public Double totalWithdrawal;
    public Long closedDealsCount;
    public String clLastActionDate;
    public String ts;

    public ClientMetricsLifetimeObject() {}

    public ClientMetricsLifetimeObject(
            String ucid,
            String brand,
            String regulator,
            Long userId,
            Double totalPnl,
            Double totalDeposit,
            Double totalWithdrawal,
            Long closedDealsCount,
            String clLastActionDate,
            String ts) {
        this.ucid = ucid;
        this.brand = brand;
        this.regulator = regulator;
        this.userId = userId;
        this.totalPnl = totalPnl;
        this.totalDeposit = totalDeposit;
        this.totalWithdrawal = totalWithdrawal;
        this.closedDealsCount = closedDealsCount;
        this.clLastActionDate = clLastActionDate;
        this.ts = ts;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ClientMetricsLifetimeObject that = (ClientMetricsLifetimeObject) o;
        return Objects.equals(ucid, that.ucid)
                && Objects.equals(brand, that.brand)
                && Objects.equals(regulator, that.regulator)
                && Objects.equals(userId, that.userId)
                && Objects.equals(totalPnl, that.totalPnl)
                && Objects.equals(totalDeposit, that.totalDeposit)
                && Objects.equals(totalWithdrawal, that.totalWithdrawal)
                && Objects.equals(closedDealsCount, that.closedDealsCount)
                && Objects.equals(clLastActionDate, that.clLastActionDate)
                && Objects.equals(ts, that.ts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                ucid,
                brand,
                regulator,
                userId,
                totalPnl,
                totalDeposit,
                totalWithdrawal,
                closedDealsCount,
                clLastActionDate,
                ts);
    }

    @Override
    public String toString() {
        return "ClientMetricsLifetimeObject{" + "ucid='" + ucid + '\'' + ", brand='" + brand + '\'' + ", regulator='"
                + regulator + '\'' + ", userId=" + userId + ", totalPnl=" + totalPnl + ", totalDeposit=" + totalDeposit
                + ", totalWithdrawal=" + totalWithdrawal + ", totalDealsCount=" + closedDealsCount
                + ", clLastActionDate='" + clLastActionDate + '\'' + ", ts='" + ts + '\'' + '}';
    }
}
