package business_objects.db.clickhouse.ib_summary_lifetime;

import java.util.Objects;

public class IbSummaryLifetimeObject {
    public Integer sourceIdSt;
    public Integer userId;
    public String brand;
    public String regulator;
    public String ucid;
    public String serverName;
    public Integer ibLevel;
    public Long masterIbRebateAccount;
    public Long ibRebateAccount;
    public Integer directUsersCount;
    public Integer fraudstersCount;
    public Integer subIbsCount;
    public Double pnl;
    public Double rebate;
    public Double netPnl;
    public Double deposit;
    public Double withdrawal;
    public Double netDeposit;
    public Double equity;
    public Double notionalValue;
    public String lastUpdated;

    public IbSummaryLifetimeObject() {
    }

    public IbSummaryLifetimeObject(Integer sourceIdSt, Integer userId, String brand, String regulator, String ucid,
            String serverName, Integer ibLevel, Long masterIbRebateAccount, Long ibRebateAccount,
            Integer directUsersCount, Integer fraudstersCount, Integer subIbsCount, Double pnl, Double rebate,
            Double netPnl, Double deposit, Double withdrawal, Double netDeposit, Double equity, Double notionalValue,
            String lastUpdated) {
        this.sourceIdSt = sourceIdSt;
        this.userId = userId;
        this.brand = brand;
        this.regulator = regulator;
        this.ucid = ucid;
        this.serverName = serverName;
        this.ibLevel = ibLevel;
        this.masterIbRebateAccount = masterIbRebateAccount;
        this.ibRebateAccount = ibRebateAccount;
        this.directUsersCount = directUsersCount;
        this.fraudstersCount = fraudstersCount;
        this.subIbsCount = subIbsCount;
        this.pnl = pnl;
        this.rebate = rebate;
        this.netPnl = netPnl;
        this.deposit = deposit;
        this.withdrawal = withdrawal;
        this.netDeposit = netDeposit;
        this.equity = equity;
        this.notionalValue = notionalValue;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IbSummaryLifetimeObject that = (IbSummaryLifetimeObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt) && Objects.equals(userId, that.userId) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(ucid, that.ucid) && Objects.equals(serverName, that.serverName) && Objects.equals(ibLevel, that.ibLevel) && Objects.equals(masterIbRebateAccount, that.masterIbRebateAccount) && Objects.equals(ibRebateAccount, that.ibRebateAccount) && Objects.equals(directUsersCount, that.directUsersCount) && Objects.equals(fraudstersCount, that.fraudstersCount) && Objects.equals(subIbsCount, that.subIbsCount) && Objects.equals(pnl, that.pnl) && Objects.equals(rebate, that.rebate) && Objects.equals(netPnl, that.netPnl) && Objects.equals(deposit, that.deposit) && Objects.equals(withdrawal, that.withdrawal) && Objects.equals(netDeposit, that.netDeposit) && Objects.equals(equity, that.equity) && Objects.equals(notionalValue, that.notionalValue) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceIdSt, userId, brand, regulator, ucid, serverName, ibLevel, masterIbRebateAccount, ibRebateAccount, directUsersCount, fraudstersCount, subIbsCount, pnl, rebate, netPnl, deposit, withdrawal, netDeposit, equity, notionalValue, lastUpdated);
    }

    @Override
    public String toString() {
        return "IbSummaryLifetimeObject{" + "sourceIdSt=" + sourceIdSt + ", userId=" + userId + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", ucid='" + ucid + '\'' + ", serverName='" + serverName + '\'' + ", ibLevel=" + ibLevel + ", masterIbRebateAccount=" + masterIbRebateAccount + ", ibRebateAccount=" + ibRebateAccount + ", directUsersCount=" + directUsersCount + ", fraudstersCount=" + fraudstersCount + ", subIbsCount=" + subIbsCount + ", pnl=" + pnl + ", rebate=" + rebate + ", netPnl=" + netPnl + ", deposit=" + deposit + ", withdrawal=" + withdrawal + ", netDeposit=" + netDeposit + ", equity=" + equity + ", notionalValue=" + notionalValue + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}
