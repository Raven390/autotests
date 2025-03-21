package business_objects.db.clickhouse.mt_tb_user;

import java.util.Objects;

import static utils.Utils.getCurrentTimestampDbFormat;

public class MtTbUserObject {

    public Integer userId;
    public String ucid;
    public Integer account;
    public String serverName;
    public String platform;
    public String type;
    public Integer serverId;
    public String createdDate;
    public String status;
    public Double balance;
    public String currency;
    public Double balanceUsd;
    public Double equity;
    public Double credit;
    public Integer leverage;
    public String accountGroup;
    public Double marginFree;
    public Double pnl;
    public String lastActionDate;
    public String lastUpdated;

    public MtTbUserObject() {
    }

    public MtTbUserObject(Integer userId, String ucid, Integer account, String serverName, String platform, String type,
            Integer serverId, String createdDate, String status, Double balance, String currency, Double balanceUsd,
            Double equity, Double credit, Integer leverage, String accountGroup, Double marginFree, Double pnl,
            String lastActionDate, String lastUpdated) {
        this.userId = userId;
        this.ucid = ucid;
        this.account = account;
        this.serverName = serverName;
        this.platform = platform;
        this.type = type;
        this.serverId = serverId;
        this.createdDate = createdDate;
        this.status = status;
        this.balance = balance;
        this.currency = currency;
        this.balanceUsd = balanceUsd;
        this.equity = equity;
        this.credit = credit;
        this.leverage = leverage;
        this.accountGroup = accountGroup;
        this.marginFree = marginFree;
        this.pnl = pnl;
        this.lastActionDate = lastActionDate;
        this.lastUpdated = lastUpdated;
    }

    public MtTbUserObject(Integer userId, String ucid, Integer account, String serverName, String platform,
            Integer serverId) {
        this.userId = userId;
        this.ucid = ucid;
        this.account = account;
        this.serverName = serverName;
        this.platform = platform;
        this.type = "Standart";
        this.serverId = serverId;
        this.createdDate = "2020-11-04 06:40:58.305000000";
        this.status = "Active";
        this.balance = 24.00;
        this.currency = "USD";
        this.balanceUsd = 24.00;
        this.equity = 32.0;
        this.credit = 42.0;
        this.leverage = 52;
        this.accountGroup = "S_VFX_EUR";
        this.marginFree = 62.00;
        this.pnl = 72.00;
        this.lastActionDate = "2024-10-04 06:40:58.305000000";
        this.lastUpdated = getCurrentTimestampDbFormat();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MtTbUserObject that = (MtTbUserObject) o;
        return Objects.equals(userId, that.userId) && Objects.equals(ucid, that.ucid) && Objects.equals(account, that.account) && Objects.equals(serverName, that.serverName) && Objects.equals(platform, that.platform) && Objects.equals(type, that.type) && Objects.equals(serverId, that.serverId) && Objects.equals(createdDate, that.createdDate) && Objects.equals(status, that.status) && Objects.equals(balance, that.balance) && Objects.equals(currency, that.currency) && Objects.equals(balanceUsd, that.balanceUsd) && Objects.equals(equity, that.equity) && Objects.equals(credit, that.credit) && Objects.equals(leverage, that.leverage) && Objects.equals(accountGroup, that.accountGroup) && Objects.equals(marginFree, that.marginFree) && Objects.equals(pnl, that.pnl) && Objects.equals(lastActionDate, that.lastActionDate) && Objects.equals(lastUpdated, that.lastUpdated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, ucid, account, serverName, platform, type, serverId, createdDate, status, balance, currency, balanceUsd, equity, credit, leverage, accountGroup, marginFree, pnl, lastActionDate, lastUpdated);
    }

    @Override
    public String toString() {
        return "MtTbUserObject{" + "userId=" + userId + ", ucid='" + ucid + '\'' + ", account=" + account + ", serverName='" + serverName + '\'' + ", platform='" + platform + '\'' + ", type='" + type + '\'' + ", serverId=" + serverId + ", createdDate='" + createdDate + '\'' + ", status='" + status + '\'' + ", balance=" + balance + ", currency='" + currency + '\'' + ", balanceUsd=" + balanceUsd + ", equity=" + equity + ", credit=" + credit + ", leverage=" + leverage + ", accountGroup='" + accountGroup + '\'' + ", marginFree=" + marginFree + ", pnl=" + pnl + ", lastActionDate='" + lastActionDate + '\'' + ", lastUpdated='" + lastUpdated + '\'' + '}';
    }
}