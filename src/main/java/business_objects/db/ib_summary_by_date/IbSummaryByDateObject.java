package business_objects.db.ib_summary_by_date;

import java.util.Objects;

public class IbSummaryByDateObject {
    public Integer sourceIdSt;
    public Integer userId;
    public String brand;
    public String regulator;
    public String ucid;
    public Long ibRebateAccount;
    public String date;
    public Double pnl;
    public Double rebate;
    public Double netPnl;
    public Double deposit;
    public Double withdrawal;
    public Double netDeposit;
    public Double equity;
    public Double notionalValue;

    public IbSummaryByDateObject() {
    }

    public IbSummaryByDateObject(Integer sourceIdSt, Integer userId, String brand, String regulator, String ucid,
            Long ibRebateAccount, String date, Double pnl, Double rebate, Double netPnl, Double deposit,
            Double withdrawal, Double netDeposit, Double equity, Double notionalValue) {
        this.sourceIdSt = sourceIdSt;
        this.userId = userId;
        this.brand = brand;
        this.regulator = regulator;
        this.ucid = ucid;
        this.ibRebateAccount = ibRebateAccount;
        this.date = date;
        this.pnl = pnl;
        this.rebate = rebate;
        this.netPnl = netPnl;
        this.deposit = deposit;
        this.withdrawal = withdrawal;
        this.netDeposit = netDeposit;
        this.equity = equity;
        this.notionalValue = notionalValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IbSummaryByDateObject that = (IbSummaryByDateObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt) && Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(ucid, that.ucid) && Objects.equals(ibRebateAccount, that.ibRebateAccount) && Objects.equals(date, that.date) && Objects.equals(pnl, that.pnl) && Objects.equals(rebate, that.rebate) && Objects.equals(netPnl, that.netPnl) && Objects.equals(deposit, that.deposit) && Objects.equals(withdrawal, that.withdrawal) && Objects.equals(netDeposit, that.netDeposit) && Objects.equals(equity, that.equity) && Objects.equals(notionalValue, that.notionalValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceIdSt, userId, brand, regulator, ucid, ibRebateAccount, date, pnl, rebate, netPnl, deposit, withdrawal, netDeposit, equity, notionalValue);
    }

    @Override
    public String toString() {
        return "IbSummaryByDateObject{" + "sourceIdSt=" + sourceIdSt + ", userId=" + userId + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", ucid='" + ucid + '\'' + ", ibRebateAccount=" + ibRebateAccount + ", date='" + date + '\'' + ", pnl=" + pnl + ", rebate=" + rebate + ", netPnl=" + netPnl + ", deposit=" + deposit + ", withdrawal=" + withdrawal + ", netDeposit=" + netDeposit + ", equity=" + equity + ", notionalValue=" + notionalValue + '}';
    }
}
