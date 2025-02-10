package businessObjects.db.clickhouse.mtAccount;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import helpers.data.ClientHelper;

import static utils.Utils.getCurrentTimestampDbFormat;

public class mtAccountObjectFactory {

    public static mtAccountObject generateMtAccountByClient(ClientHelper client) {
        mtAccountObject mtAccount = new mtAccountObject();
        mtAccount.sourceIdSt = client.getServerId();
        mtAccount.account = client.getTradingAccount();
        mtAccount.server = "MT5-UK";
        mtAccount.accountGroup = "Social_Trading\\VU_Hedge\\ARCHIVE_SUB_USD";
        mtAccount.platform = "MT5";
        mtAccount.currency = "EUR";
        mtAccount.createTime = getCurrentTimestampDbFormat();
        mtAccount.createTimeUtc = getCurrentTimestampDbFormat();
        mtAccount.lastLogin = getCurrentTimestampDbFormat();
        mtAccount.lastLoginUtc = getCurrentTimestampDbFormat();
        mtAccount.leverage = 500;
        mtAccount.agentAccount = 0;
        mtAccount.balance = 0.0;
        mtAccount.balanceUsd = 0.0;
        mtAccount.credit = 0.0;
        mtAccount.creditUsd = 0.0;
        mtAccount.equity = 0.0;
        mtAccount.equityUsd = 0.0;
        mtAccount.floatingPnl = 0.0;
        mtAccount.floatingPnlUsd = 0.0;
        mtAccount.margin = 0.0;
        mtAccount.marginUsd = 0.0;
        mtAccount.marginFree = 0.0;
        mtAccount.marginFreeUsd = 0.0;
        mtAccount.lastUpdated = getCurrentTimestampDbFormat();
        return mtAccount;
    }

    public static mtAccountObject generateMtAccountByCrmTbAccount(CrmTbAccountObject account) {
        mtAccountObject mtAccount = new mtAccountObject();
        mtAccount.sourceIdSt = account.serverIdSt;
        mtAccount.account = account.account;
        mtAccount.server = account.serverName;
        mtAccount.accountGroup = account.accountGroup;
        mtAccount.platform = account.platform;
        mtAccount.currency = account.currency;
        mtAccount.createTime = account.createTime;
        mtAccount.createTimeUtc = account.createTimeUtc;
        mtAccount.lastLogin = account.lastLogin;
        mtAccount.lastLoginUtc = account.lastLoginUtc;
        mtAccount.leverage = account.leverage;
        mtAccount.agentAccount = 0;
        mtAccount.balance = account.balance;
        mtAccount.balanceUsd = account.balanceUsd;
        mtAccount.credit = account.credit;
        mtAccount.creditUsd = account.credit;
        mtAccount.equity = account.equity;
        mtAccount.equityUsd = account.equity;
        mtAccount.floatingPnl = account.pnl;
        mtAccount.floatingPnlUsd = account.pnl;
        mtAccount.margin = account.marginFree + account.equity;
        mtAccount.marginUsd = account.marginFree + account.equity;
        mtAccount.marginFree = account.marginFree;
        mtAccount.marginFreeUsd = account.marginFree;
        mtAccount.lastUpdated = account.lastUpdated;
        return mtAccount;
    }
}
