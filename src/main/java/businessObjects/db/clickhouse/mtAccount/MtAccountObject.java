package businessObjects.db.clickhouse.mtAccount;

import java.util.Objects;

public class MtAccountObject {

    public Integer sourceIdSt;
    public Integer account;
    public String server;
    public String accountGroup;
    public String platform;
    public String currency;
    public String createTime;
    public String createTimeUtc;
    public String lastLogin;
    public String lastLoginUtc;
    public Integer leverage;
    public Integer agentAccount;
    public Double balance;
    public Double balanceUsd;
    public Double credit;
    public Double creditUsd;
    public Double equity;
    public Double equityUsd;
    public Double floatingPnl;
    public Double floatingPnlUsd;
    public Double margin;
    public Double marginUsd;
    public Double marginFree;
    public Double marginFreeUsd;
    public String lastUpdated;

    public MtAccountObject() {

    }

    public MtAccountObject(Integer sourceIdSt, Integer account, String server, String accountGroup, String platform,
            String currency, String createTime, String createTimeUtc, String lastLogin, String lastLoginUtc,
            Integer leverage, Integer agentAccount, Double balance, Double balanceUsd, Double credit, Double creditUsd,
            Double equity, Double equityUsd, Double floatingPnl, Double floatingPnlUsd, Double margin, Double marginUsd,
            Double marginFree, Double marginFreeUsd, String lastUpdated) {
        this.sourceIdSt = sourceIdSt;
        this.account = account;
        this.server = server;
        this.accountGroup = accountGroup;
        this.platform = platform;
        this.currency = currency;
        this.createTime = createTime;
        this.createTimeUtc = createTimeUtc;
        this.lastLogin = lastLogin;
        this.lastLoginUtc = lastLoginUtc;
        this.leverage = leverage;
        this.agentAccount = agentAccount;
        this.balance = balance;
        this.balanceUsd = balanceUsd;
        this.credit = credit;
        this.creditUsd = creditUsd;
        this.equity = equity;
        this.equityUsd = equityUsd;
        this.floatingPnl = floatingPnl;
        this.floatingPnlUsd = floatingPnlUsd;
        this.margin = margin;
        this.marginUsd = marginUsd;
        this.marginFree = marginFree;
        this.marginFreeUsd = marginFreeUsd;
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "mtAccountObject{" + "sourceIdSt=" + sourceIdSt + ", account=" + account + ", server='" + server + '\'' + ", accuntGroup='" + accountGroup + '\'' + ", platform='" + platform + '\'' + ", currency='" + currency + '\'' + ", createTime='" + createTime + '\'' + ", createTimeUtc='" + createTimeUtc + '\'' + ", lastLogin='" + lastLogin + '\'' + ", lastLoginUtc='" + lastLoginUtc + '\'' + ", leverage=" + leverage + ", agentAccount=" + agentAccount + ", balance=" + balance + ", balanceUsd=" + balanceUsd + ", credit=" + credit + ", creditUsd=" + creditUsd + ", equity=" + equity + ", equityUsd=" + equityUsd + ", floatingPnl=" + floatingPnl + ", floatingPnlUsd=" + floatingPnlUsd + ", margin=" + margin + ", marginUsd=" + marginUsd + ", marginFree=" + marginFree + ", marginFreeUsd=" + marginFreeUsd + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MtAccountObject that = (MtAccountObject) o;
        return Objects.equals(sourceIdSt, that.sourceIdSt) && Objects.equals(account, that.account) && Objects.equals(server, that.server) && Objects.equals(accountGroup, that.accountGroup) && Objects.equals(platform, that.platform) && Objects.equals(currency, that.currency) && Objects.equals(createTime, that.createTime) && Objects.equals(createTimeUtc, that.createTimeUtc) && Objects.equals(lastLogin, that.lastLogin) && Objects.equals(lastLoginUtc, that.lastLoginUtc) && Objects.equals(leverage, that.leverage) && Objects.equals(agentAccount, that.agentAccount) && Objects.equals(balance, that.balance) && Objects.equals(balanceUsd, that.balanceUsd) && Objects.equals(credit, that.credit) && Objects.equals(creditUsd, that.creditUsd) && Objects.equals(equity, that.equity) && Objects.equals(equityUsd, that.equityUsd) && Objects.equals(floatingPnl, that.floatingPnl) && Objects.equals(floatingPnlUsd, that.floatingPnlUsd) && Objects.equals(margin, that.margin) && Objects.equals(marginUsd, that.marginUsd) && Objects.equals(marginFree, that.marginFree) && Objects.equals(marginFreeUsd, that.marginFreeUsd) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceIdSt, account, server, accountGroup, platform, currency, createTime, createTimeUtc, lastLogin, lastLoginUtc, leverage, agentAccount, balance, balanceUsd, credit, creditUsd, equity, equityUsd, floatingPnl, floatingPnlUsd, margin, marginUsd, marginFree, marginFreeUsd, lastUpdated);
    }
}
