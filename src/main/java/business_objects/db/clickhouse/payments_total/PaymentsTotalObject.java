package business_objects.db.clickhouse.payments_total;

import java.util.Objects;

public class PaymentsTotalObject {
    public String ucid;
    public String brand;
    public String regulator;
    public long userId;
    public long account;
    public String date;
    public double totalDepositUsd;
    public long totalDepositCount;
    public double totalWithdrawalUsd;
    public long totalWithdrawalCount;
    public double netDepositsUsd;
    public long totalBalanceopsCount;
    public double totalCreditsUsd;
    public long totalCreditsCount;
    public double totalTransfersUsd;
    public long totalTransfersCount;
    public String lastUpdated;

    @Override
    public String toString() {
        return "PaymentsTotalObject{" + "ucid='" + ucid + '\'' + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", account=" + account + ", date='" + date + '\'' + ", totalDepositUsd=" + totalDepositUsd + ", totalDepositCount=" + totalDepositCount + ", totalWithdrawalUsd=" + totalWithdrawalUsd + ", totalWithdrawalCount=" + totalWithdrawalCount + ", netDepositUsd=" + netDepositsUsd + ", totalBalanceopsCount=" + totalBalanceopsCount + ", totalCreditsUsd=" + totalCreditsUsd + ", totalCreditsCount=" + totalCreditsCount + ", totalTransfersUsd=" + totalTransfersUsd + ", totalTransfersCount=" + totalTransfersCount + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentsTotalObject that = (PaymentsTotalObject) o;
        return userId == that.userId && account == that.account && Double.compare(totalDepositUsd, that.totalDepositUsd) == 0 && totalDepositCount == that.totalDepositCount && Double.compare(totalWithdrawalUsd, that.totalWithdrawalUsd) == 0 && totalWithdrawalCount == that.totalWithdrawalCount && Double.compare(netDepositsUsd, that.netDepositsUsd) == 0 && totalBalanceopsCount == that.totalBalanceopsCount && Double.compare(totalCreditsUsd, that.totalCreditsUsd) == 0 && totalCreditsCount == that.totalCreditsCount && Double.compare(totalTransfersUsd, that.totalTransfersUsd) == 0 && Double.compare(totalTransfersCount, that.totalTransfersCount) == 0 && Objects.equals(ucid, that.ucid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(date, that.date) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ucid, brand, regulator, userId, account, date, totalDepositUsd, totalDepositCount, totalWithdrawalUsd, totalWithdrawalCount, netDepositsUsd, totalBalanceopsCount, totalCreditsUsd, totalCreditsCount, totalTransfersUsd, totalTransfersCount, lastUpdated);
    }

    public PaymentsTotalObject() {
    };

    public PaymentsTotalObject(String ucid, String brand, String regulator, long userId, long account, String date,
            double totalDepositUsd, long totalDepositCount, double totalWithdrawalUsd, long totalWithdrawalCount,
            double netDepositsUsd, long totalBalanceopsCount, double totalCreditsUsd, long totalCreditsCount,
            double totalTransfersUsd, long totalTransfersCount, String lastUpdated) {
        this.ucid = ucid;
        this.brand = brand;
        this.regulator = regulator;
        this.userId = userId;
        this.account = account;
        this.date = date;
        this.totalDepositUsd = totalDepositUsd;
        this.totalDepositCount = totalDepositCount;
        this.totalWithdrawalUsd = totalWithdrawalUsd;
        this.totalWithdrawalCount = totalWithdrawalCount;
        this.netDepositsUsd = netDepositsUsd;
        this.totalBalanceopsCount = totalBalanceopsCount;
        this.totalCreditsUsd = totalCreditsUsd;
        this.totalCreditsCount = totalCreditsCount;
        this.totalTransfersUsd = totalTransfersUsd;
        this.totalTransfersCount = totalTransfersCount;
        this.lastUpdated = lastUpdated;
    }
}
