package businessObjects.db.clickhouse.s3FactLoginMetrics;


import java.util.Objects;

public class S3FactLoginMetricsObject {
    private String date;
    private Integer brandUid;
    private String brand;
    private String regulator;
    private Integer userId;
    private String ucid;
    private Integer account;
    private Integer serverId;
    private String server;
    private String accountGroup;
    private String platform;
    private String currency;
    private Double dailyDeposit;
    private Double dailyWithdraw;
    private Double dailyNetDeposit;
    private Double balance;
    private Double credit;
    private Double equity;
    private Double floatingPnl;
    private Double dailyNetClosedPnl;
    private Double dailyTradingVolIn;
    private Double dailyTradingVolOut;
    private Double dailyDividend;
    private Double dailyCommissionRevenue;
    private Double dailyCashAdjDebtWo;
    private Double dailyNegativeAdj;
    private Double dailyAdminFee;
    private Double dailyRollover;
    private Double dailyPnlAdj;
    private Double dailySwapsRevenue;
    private Double dailyGrossClientPnl;
    private Double dailyRebateToTrade;
    private Double dailySwapsCalc;
    private Double dailyProfitsCalc;
    private Double balanceLocal;
    private Double creditLocal;
    private Double equityLocal;
    private Double deltaPnl;
    private Double deltaSwaps;
    private Double dailyCoreSpreadRevenueOz;
    private Double dailyTakerSpreadRevenueOz;
    private Double dailyClientSlippageRevenueOz;
    private Double dailyLpSpreadRevenueOz;
    private Double dailyCoreSpreadRevenuePe;
    private Double dailyTakerSpreadRevenuePe;
    private Double dailyLpSpreadRevenuePe;
    private Double dailyClientSlippageRevenuePe;
    private Double dailyTotalSpreadRevenue;
    private Double dailyDepositCount;
    private Double dailyDepositReversalCount;
    private Double dailyWithdrawCount;
    private Double dailyWithdrawReversalCount;
    private Double dailyVbSpreadRevenueOz;
    private Double dailyAppliedMinSpreadRevenueOz;
    private Double dailyAppliedMaxSpreadRevenueOz;
    private Double dailyStpOz;
    private Double dailySlpOz;
    private Double dailyVbSpreadRevenuePe;
    private Double dailyAppliedMinSpreadRevenuePe;
    private Double dailyAppliedMaxSpreadRevenuePe;
    private String dlInsertTs;
    private String dlUpdateTs;

    @Override
    public String toString() {
        return "s3FactLoginMetricsObject{" + "date='" + date + '\'' + ", brandUid=" + brandUid + ", brand='" + brand + '\'' + ", regulator='" + regulator + '\'' + ", userId=" + userId + ", ucid='" + ucid + '\'' + ", account=" + account + ", serverId=" + serverId + ", server='" + server + '\'' + ", accountGroup='" + accountGroup + '\'' + ", platform='" + platform + '\'' + ", currency='" + currency + '\'' + ", dailyDeposit=" + dailyDeposit + ", dailyWithdrawal=" + dailyWithdraw + ", dailyNetDeposit=" + dailyNetDeposit + ", balance=" + balance + ", credit=" + credit + ", equity=" + equity + ", floatingPnl=" + floatingPnl + ", dailyNetClosedPnl=" + dailyNetClosedPnl + ", dailyTradingVolumeIn=" + dailyTradingVolIn + ", dailyTradingVolumeOut=" + dailyTradingVolOut + ", dailyDividend=" + dailyDividend + ", dailyCommissionRevenue=" + dailyCommissionRevenue + ", dailyCashAdjDebtWo=" + dailyCashAdjDebtWo + ", dailyNegativeAdj=" + dailyNegativeAdj + ", dailyAdminFee=" + dailyAdminFee + ", dailyRollover=" + dailyRollover + ", dailyPnlAdj=" + dailyPnlAdj + ", dailySwapsRevenue=" + dailySwapsRevenue + ", dailyGrossClientPnl=" + dailyGrossClientPnl + ", dailyRebateToTrade=" + dailyRebateToTrade + ", dailySwapsCalc=" + dailySwapsCalc + ", dailyProfitsCalc=" + dailyProfitsCalc + ", balanceLocal=" + balanceLocal + ", creditLocal=" + creditLocal + ", equityLocal=" + equityLocal + ", deltaPnl=" + deltaPnl + ", deltaSwaps=" + deltaSwaps + ", dailyCoreSpreadRevenueOz=" + dailyCoreSpreadRevenueOz + ", dailyTakerSpreadRevenueOz=" + dailyTakerSpreadRevenueOz + ", dailyClientSlippageRevenueOz=" + dailyClientSlippageRevenueOz + ", dailyLpSpreadRevenueOz=" + dailyLpSpreadRevenueOz + ", dailyCoreSpreadRevenuePe=" + dailyCoreSpreadRevenuePe + ", dailyTakerSpreadRevenuePe=" + dailyTakerSpreadRevenuePe + ", dailyLpSpreadRevenuePe=" + dailyLpSpreadRevenuePe + ", dailyClientSlippageRevenuePe=" + dailyClientSlippageRevenuePe + ", dailyTotalSpreadRevenue=" + dailyTotalSpreadRevenue + ", dailyDepositCount=" + dailyDepositCount + ", dailyDepositReversalCount=" + dailyDepositReversalCount + ", dailyWithdrawCount=" + dailyWithdrawCount + ", dailyWithdrawReversalCount=" + dailyWithdrawReversalCount + ", dailyVbSpreadRevenueOz=" + dailyVbSpreadRevenueOz + ", dailyAppliedMinSpreadRevenueOz=" + dailyAppliedMinSpreadRevenueOz + ", dailyAppliedMaxSpreadRevenueOz=" + dailyAppliedMaxSpreadRevenueOz + ", dailyStpOz=" + dailyStpOz + ", dailySlpOz=" + dailySlpOz + ", dailyVbSpreadRevenuePe=" + dailyVbSpreadRevenuePe + ", dailyAppliedMinSpreadRevenuePe=" + dailyAppliedMinSpreadRevenuePe + ", dailyAppliedMaxSpreadRevenuePe=" + dailyAppliedMaxSpreadRevenuePe + ", dlInsertTs='" + dlInsertTs + '\'' + ", dlUpdateTs='" + dlUpdateTs + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        S3FactLoginMetricsObject that = (S3FactLoginMetricsObject) o;
        return Objects.equals(date, that.date) && Objects.equals(brandUid, that.brandUid) && Objects.equals(brand, that.brand) && Objects.equals(regulator, that.regulator) && Objects.equals(userId, that.userId) && Objects.equals(ucid, that.ucid) && Objects.equals(account, that.account) && Objects.equals(serverId, that.serverId) && Objects.equals(server, that.server) && Objects.equals(accountGroup, that.accountGroup) && Objects.equals(platform, that.platform) && Objects.equals(currency, that.currency) && Objects.equals(dailyDeposit, that.dailyDeposit) && Objects.equals(dailyWithdraw, that.dailyWithdraw) && Objects.equals(dailyNetDeposit, that.dailyNetDeposit) && Objects.equals(balance, that.balance) && Objects.equals(credit, that.credit) && Objects.equals(equity, that.equity) && Objects.equals(floatingPnl, that.floatingPnl) && Objects.equals(dailyNetClosedPnl, that.dailyNetClosedPnl) && Objects.equals(dailyTradingVolIn, that.dailyTradingVolIn) && Objects.equals(dailyTradingVolOut, that.dailyTradingVolOut) && Objects.equals(dailyDividend, that.dailyDividend) && Objects.equals(dailyCommissionRevenue, that.dailyCommissionRevenue) && Objects.equals(dailyCashAdjDebtWo, that.dailyCashAdjDebtWo) && Objects.equals(dailyNegativeAdj, that.dailyNegativeAdj) && Objects.equals(dailyAdminFee, that.dailyAdminFee) && Objects.equals(dailyRollover, that.dailyRollover) && Objects.equals(dailyPnlAdj, that.dailyPnlAdj) && Objects.equals(dailySwapsRevenue, that.dailySwapsRevenue) && Objects.equals(dailyGrossClientPnl, that.dailyGrossClientPnl) && Objects.equals(dailyRebateToTrade, that.dailyRebateToTrade) && Objects.equals(dailySwapsCalc, that.dailySwapsCalc) && Objects.equals(dailyProfitsCalc, that.dailyProfitsCalc) && Objects.equals(balanceLocal, that.balanceLocal) && Objects.equals(creditLocal, that.creditLocal) && Objects.equals(equityLocal, that.equityLocal) && Objects.equals(deltaPnl, that.deltaPnl) && Objects.equals(deltaSwaps, that.deltaSwaps) && Objects.equals(dailyCoreSpreadRevenueOz, that.dailyCoreSpreadRevenueOz) && Objects.equals(dailyTakerSpreadRevenueOz, that.dailyTakerSpreadRevenueOz) && Objects.equals(dailyClientSlippageRevenueOz, that.dailyClientSlippageRevenueOz) && Objects.equals(dailyLpSpreadRevenueOz, that.dailyLpSpreadRevenueOz) && Objects.equals(dailyCoreSpreadRevenuePe, that.dailyCoreSpreadRevenuePe) && Objects.equals(dailyTakerSpreadRevenuePe, that.dailyTakerSpreadRevenuePe) && Objects.equals(dailyLpSpreadRevenuePe, that.dailyLpSpreadRevenuePe) && Objects.equals(dailyClientSlippageRevenuePe, that.dailyClientSlippageRevenuePe) && Objects.equals(dailyTotalSpreadRevenue, that.dailyTotalSpreadRevenue) && Objects.equals(dailyDepositCount, that.dailyDepositCount) && Objects.equals(dailyDepositReversalCount, that.dailyDepositReversalCount) && Objects.equals(dailyWithdrawCount, that.dailyWithdrawCount) && Objects.equals(dailyWithdrawReversalCount, that.dailyWithdrawReversalCount) && Objects.equals(dailyVbSpreadRevenueOz, that.dailyVbSpreadRevenueOz) && Objects.equals(dailyAppliedMinSpreadRevenueOz, that.dailyAppliedMinSpreadRevenueOz) && Objects.equals(dailyAppliedMaxSpreadRevenueOz, that.dailyAppliedMaxSpreadRevenueOz) && Objects.equals(dailyStpOz, that.dailyStpOz) && Objects.equals(dailySlpOz, that.dailySlpOz) && Objects.equals(dailyVbSpreadRevenuePe, that.dailyVbSpreadRevenuePe) && Objects.equals(dailyAppliedMinSpreadRevenuePe, that.dailyAppliedMinSpreadRevenuePe) && Objects.equals(dailyAppliedMaxSpreadRevenuePe, that.dailyAppliedMaxSpreadRevenuePe) && Objects.equals(dlInsertTs, that.dlInsertTs) && Objects.equals(dlUpdateTs, that.dlUpdateTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, brandUid, brand, regulator, userId, ucid, account, serverId, server, accountGroup, platform, currency, dailyDeposit, dailyWithdraw, dailyNetDeposit, balance, credit, equity, floatingPnl, dailyNetClosedPnl, dailyTradingVolIn, dailyTradingVolOut, dailyDividend, dailyCommissionRevenue, dailyCashAdjDebtWo, dailyNegativeAdj, dailyAdminFee, dailyRollover, dailyPnlAdj, dailySwapsRevenue, dailyGrossClientPnl, dailyRebateToTrade, dailySwapsCalc, dailyProfitsCalc, balanceLocal, creditLocal, equityLocal, deltaPnl, deltaSwaps, dailyCoreSpreadRevenueOz, dailyTakerSpreadRevenueOz, dailyClientSlippageRevenueOz, dailyLpSpreadRevenueOz, dailyCoreSpreadRevenuePe, dailyTakerSpreadRevenuePe, dailyLpSpreadRevenuePe, dailyClientSlippageRevenuePe, dailyTotalSpreadRevenue, dailyDepositCount, dailyDepositReversalCount, dailyWithdrawCount, dailyWithdrawReversalCount, dailyVbSpreadRevenueOz, dailyAppliedMinSpreadRevenueOz, dailyAppliedMaxSpreadRevenueOz, dailyStpOz, dailySlpOz, dailyVbSpreadRevenuePe, dailyAppliedMinSpreadRevenuePe, dailyAppliedMaxSpreadRevenuePe, dlInsertTs, dlUpdateTs);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getBrandUid() {
        return brandUid;
    }

    public void setBrandUid(Integer brandUid) {
        this.brandUid = brandUid;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getRegulator() {
        return regulator;
    }

    public void setRegulator(String regulator) {
        this.regulator = regulator;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUcid() {
        return ucid;
    }

    public void setUcid(String ucid) {
        this.ucid = ucid;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getAccountGroup() {
        return accountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        this.accountGroup = accountGroup;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getDailyDeposit() {
        return dailyDeposit;
    }

    public void setDailyDeposit(Double dailyDeposit) {
        this.dailyDeposit = dailyDeposit;
    }

    public Double getDailyWithdraw() {
        return dailyWithdraw;
    }

    public void setDailyWithdraw(Double dailyWithdraw) {
        this.dailyWithdraw = dailyWithdraw;
    }

    public Double getDailyNetDeposit() {
        return dailyNetDeposit;
    }

    public void setDailyNetDeposit(Double dailyNetDeposit) {
        this.dailyNetDeposit = dailyNetDeposit;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getEquity() {
        return equity;
    }

    public void setEquity(Double equity) {
        this.equity = equity;
    }

    public Double getFloatingPnl() {
        return floatingPnl;
    }

    public void setFloatingPnl(Double floatingPnl) {
        this.floatingPnl = floatingPnl;
    }

    public Double getDailyNetClosedPnl() {
        return dailyNetClosedPnl;
    }

    public void setDailyNetClosedPnl(Double dailyNetClosedPnl) {
        this.dailyNetClosedPnl = dailyNetClosedPnl;
    }

    public Double getDailyTradingVolIn() {
        return dailyTradingVolIn;
    }

    public void setDailyTradingVolIn(Double dailyTradingVolIn) {
        this.dailyTradingVolIn = dailyTradingVolIn;
    }

    public Double getDailyTradingVolOut() {
        return dailyTradingVolOut;
    }

    public void setDailyTradingVolOut(Double dailyTradingVolOut) {
        this.dailyTradingVolOut = dailyTradingVolOut;
    }

    public Double getDailyDividend() {
        return dailyDividend;
    }

    public void setDailyDividend(Double dailyDividend) {
        this.dailyDividend = dailyDividend;
    }

    public Double getDailyCommissionRevenue() {
        return dailyCommissionRevenue;
    }

    public void setDailyCommissionRevenue(Double dailyCommissionRevenue) {
        this.dailyCommissionRevenue = dailyCommissionRevenue;
    }

    public Double getDailyCashAdjDebtWo() {
        return dailyCashAdjDebtWo;
    }

    public void setDailyCashAdjDebtWo(Double dailyCashAdjDebtWo) {
        this.dailyCashAdjDebtWo = dailyCashAdjDebtWo;
    }

    public Double getDailyNegativeAdj() {
        return dailyNegativeAdj;
    }

    public void setDailyNegativeAdj(Double dailyNegativeAdj) {
        this.dailyNegativeAdj = dailyNegativeAdj;
    }

    public Double getDailyAdminFee() {
        return dailyAdminFee;
    }

    public void setDailyAdminFee(Double dailyAdminFee) {
        this.dailyAdminFee = dailyAdminFee;
    }

    public Double getDailyRollover() {
        return dailyRollover;
    }

    public void setDailyRollover(Double dailyRollover) {
        this.dailyRollover = dailyRollover;
    }

    public Double getDailyPnlAdj() {
        return dailyPnlAdj;
    }

    public void setDailyPnlAdj(Double dailyPnlAdj) {
        this.dailyPnlAdj = dailyPnlAdj;
    }

    public Double getDailySwapsRevenue() {
        return dailySwapsRevenue;
    }

    public void setDailySwapsRevenue(Double dailySwapsRevenue) {
        this.dailySwapsRevenue = dailySwapsRevenue;
    }

    public Double getDailyGrossClientPnl() {
        return dailyGrossClientPnl;
    }

    public void setDailyGrossClientPnl(Double dailyGrossClientPnl) {
        this.dailyGrossClientPnl = dailyGrossClientPnl;
    }

    public Double getDailyRebateToTrade() {
        return dailyRebateToTrade;
    }

    public void setDailyRebateToTrade(Double dailyRebateToTrade) {
        this.dailyRebateToTrade = dailyRebateToTrade;
    }

    public Double getDailySwapsCalc() {
        return dailySwapsCalc;
    }

    public void setDailySwapsCalc(Double dailySwapsCalc) {
        this.dailySwapsCalc = dailySwapsCalc;
    }

    public Double getDailyProfitsCalc() {
        return dailyProfitsCalc;
    }

    public void setDailyProfitsCalc(Double dailyProfitsCalc) {
        this.dailyProfitsCalc = dailyProfitsCalc;
    }

    public Double getBalanceLocal() {
        return balanceLocal;
    }

    public void setBalanceLocal(Double balanceLocal) {
        this.balanceLocal = balanceLocal;
    }

    public Double getCreditLocal() {
        return creditLocal;
    }

    public void setCreditLocal(Double creditLocal) {
        this.creditLocal = creditLocal;
    }

    public Double getEquityLocal() {
        return equityLocal;
    }

    public void setEquityLocal(Double equityLocal) {
        this.equityLocal = equityLocal;
    }

    public Double getDeltaPnl() {
        return deltaPnl;
    }

    public void setDeltaPnl(Double deltaPnl) {
        this.deltaPnl = deltaPnl;
    }

    public Double getDeltaSwaps() {
        return deltaSwaps;
    }

    public void setDeltaSwaps(Double deltaSwaps) {
        this.deltaSwaps = deltaSwaps;
    }

    public Double getDailyCoreSpreadRevenueOz() {
        return dailyCoreSpreadRevenueOz;
    }

    public void setDailyCoreSpreadRevenueOz(Double dailyCoreSpreadRevenueOz) {
        this.dailyCoreSpreadRevenueOz = dailyCoreSpreadRevenueOz;
    }

    public Double getDailyTakerSpreadRevenueOz() {
        return dailyTakerSpreadRevenueOz;
    }

    public void setDailyTakerSpreadRevenueOz(Double dailyTakerSpreadRevenueOz) {
        this.dailyTakerSpreadRevenueOz = dailyTakerSpreadRevenueOz;
    }

    public Double getDailyClientSlippageRevenueOz() {
        return dailyClientSlippageRevenueOz;
    }

    public void setDailyClientSlippageRevenueOz(Double dailyClientSlippageRevenueOz) {
        this.dailyClientSlippageRevenueOz = dailyClientSlippageRevenueOz;
    }

    public Double getDailyLpSpreadRevenueOz() {
        return dailyLpSpreadRevenueOz;
    }

    public void setDailyLpSpreadRevenueOz(Double dailyLpSpreadRevenueOz) {
        this.dailyLpSpreadRevenueOz = dailyLpSpreadRevenueOz;
    }

    public Double getDailyCoreSpreadRevenuePe() {
        return dailyCoreSpreadRevenuePe;
    }

    public void setDailyCoreSpreadRevenuePe(Double dailyCoreSpreadRevenuePe) {
        this.dailyCoreSpreadRevenuePe = dailyCoreSpreadRevenuePe;
    }

    public Double getDailyTakerSpreadRevenuePe() {
        return dailyTakerSpreadRevenuePe;
    }

    public void setDailyTakerSpreadRevenuePe(Double dailyTakerSpreadRevenuePe) {
        this.dailyTakerSpreadRevenuePe = dailyTakerSpreadRevenuePe;
    }

    public Double getDailyLpSpreadRevenuePe() {
        return dailyLpSpreadRevenuePe;
    }

    public void setDailyLpSpreadRevenuePe(Double dailyLpSpreadRevenuePe) {
        this.dailyLpSpreadRevenuePe = dailyLpSpreadRevenuePe;
    }

    public Double getDailyClientSlippageRevenuePe() {
        return dailyClientSlippageRevenuePe;
    }

    public void setDailyClientSlippageRevenuePe(Double dailyClientSlippageRevenuePe) {
        this.dailyClientSlippageRevenuePe = dailyClientSlippageRevenuePe;
    }

    public Double getDailyTotalSpreadRevenue() {
        return dailyTotalSpreadRevenue;
    }

    public void setDailyTotalSpreadRevenue(Double dailyTotalSpreadRevenue) {
        this.dailyTotalSpreadRevenue = dailyTotalSpreadRevenue;
    }

    public Double getDailyDepositCount() {
        return dailyDepositCount;
    }

    public void setDailyDepositCount(Double dailyDepositCount) {
        this.dailyDepositCount = dailyDepositCount;
    }

    public Double getDailyDepositReversalCount() {
        return dailyDepositReversalCount;
    }

    public void setDailyDepositReversalCount(Double dailyDepositReversalCount) {
        this.dailyDepositReversalCount = dailyDepositReversalCount;
    }

    public Double getDailyWithdrawCount() {
        return dailyWithdrawCount;
    }

    public void setDailyWithdrawCount(Double dailyWithdrawCount) {
        this.dailyWithdrawCount = dailyWithdrawCount;
    }

    public Double getDailyWithdrawReversalCount() {
        return dailyWithdrawReversalCount;
    }

    public void setDailyWithdrawReversalCount(Double dailyWithdrawReversalCount) {
        this.dailyWithdrawReversalCount = dailyWithdrawReversalCount;
    }

    public Double getDailyVbSpreadRevenueOz() {
        return dailyVbSpreadRevenueOz;
    }

    public void setDailyVbSpreadRevenueOz(Double dailyVbSpreadRevenueOz) {
        this.dailyVbSpreadRevenueOz = dailyVbSpreadRevenueOz;
    }

    public Double getDailyAppliedMinSpreadRevenueOz() {
        return dailyAppliedMinSpreadRevenueOz;
    }

    public void setDailyAppliedMinSpreadRevenueOz(Double dailyAppliedMinSpreadRevenueOz) {
        this.dailyAppliedMinSpreadRevenueOz = dailyAppliedMinSpreadRevenueOz;
    }

    public Double getDailyAppliedMaxSpreadRevenueOz() {
        return dailyAppliedMaxSpreadRevenueOz;
    }

    public void setDailyAppliedMaxSpreadRevenueOz(Double dailyAppliedMaxSpreadRevenueOz) {
        this.dailyAppliedMaxSpreadRevenueOz = dailyAppliedMaxSpreadRevenueOz;
    }

    public Double getDailyStpOz() {
        return dailyStpOz;
    }

    public void setDailyStpOz(Double dailyStpOz) {
        this.dailyStpOz = dailyStpOz;
    }

    public Double getDailySlpOz() {
        return dailySlpOz;
    }

    public void setDailySlpOz(Double dailySlpOz) {
        this.dailySlpOz = dailySlpOz;
    }

    public Double getDailyVbSpreadRevenuePe() {
        return dailyVbSpreadRevenuePe;
    }

    public void setDailyVbSpreadRevenuePe(Double dailyVbSpreadRevenuePe) {
        this.dailyVbSpreadRevenuePe = dailyVbSpreadRevenuePe;
    }

    public Double getDailyAppliedMinSpreadRevenuePe() {
        return dailyAppliedMinSpreadRevenuePe;
    }

    public void setDailyAppliedMinSpreadRevenuePe(Double dailyAppliedMinSpreadRevenuePe) {
        this.dailyAppliedMinSpreadRevenuePe = dailyAppliedMinSpreadRevenuePe;
    }

    public Double getDailyAppliedMaxSpreadRevenuePe() {
        return dailyAppliedMaxSpreadRevenuePe;
    }

    public void setDailyAppliedMaxSpreadRevenuePe(Double dailyAppliedMaxSpreadRevenuePe) {
        this.dailyAppliedMaxSpreadRevenuePe = dailyAppliedMaxSpreadRevenuePe;
    }

    public String getDlInsertTs() {
        return dlInsertTs;
    }

    public void setDlInsertTs(String dlInsertTs) {
        this.dlInsertTs = dlInsertTs;
    }

    public String getDlUpdateTs() {
        return dlUpdateTs;
    }

    public void setDlUpdateTs(String dlUpdateTs) {
        this.dlUpdateTs = dlUpdateTs;
    }
}

