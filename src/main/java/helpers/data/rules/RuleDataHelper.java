package helpers.data.rules;

import businessObjects.db.clickhouse.aggrCreditEquityRate.AggrCreditEquityRateObject;
import businessObjects.db.clickhouse.aggrFloatingTradesGroupBy.AggrFloatingTradesGroupBy;
import businessObjects.db.clickhouse.aggrMirrorAccountsByTrades.MirrorLoginObject;
import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbBonusTable.CrmTbBonusObject;
import businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.crmTbWithdrawal.CrmTbWithdrawalObject;
import businessObjects.db.clickhouse.lnSessionParsed.LnSessionParsedObject;
import businessObjects.db.clickhouse.mirrorUcidTable.MirrorUcidObject;
import businessObjects.db.clickhouse.mtBalanceOrdersTable.MtBalanceOrdersObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import businessObjects.db.clickhouse.mtTbCredits.MtTbCreditsObject;
import businessObjects.kafka.crmEvents.WithdrawalEvent;
import businessObjects.kafka.mtEvents.CloseTradeMtEvent;
import helpers.data.ClientHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static businessObjects.db.clickhouse.emailTable.EmailTableEntryFactory.getEmailTableEntryByClient;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.MitigationHelper.cleanUserRestriction;
import static utils.Constants.*;

public class RuleDataHelper {
    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public LnSessionParsedObject lnSessionParsedObjectRegistration;
    public LnSessionParsedObject lnSessionParsedObjectLogin;
    public List<ConnectionTableEntry> connections;
    public List<CrmTbUserObject> connectedUsers;
    public WithdrawalEvent withdrawalEvent;
    public CloseTradeMtEvent closeTradeEvent;
    public List<BoClientFraudTypesObject> clientFraudTypes;
    public CrmTbAccountObject crmTbAccountObject;
    public List<CrmTbAccountObject> crmTbAccountObjectConnections;
    public List<MtTbCreditsObject> mtTbCreditsObjects;
    public List<CrmTbWithdrawalObject> crmTbWithdrawalObjects;
    public List<CrmTbDepositObject> crmTbDepositObjects;
    public List<CrmTbBonusObject> crmTbBonusObjects;
    public List<Mt5DealsCoercedObject> mt5DealsObjects;
    public AggrCreditEquityRateObject aggrCreditEquityRate;
    public MirrorLoginObject aggrMirrorAccountsByTrades;
    public List<MtBalanceOrdersObject> mtBalanceOrdersObjects;
    public List<MirrorLoginObject> mirrorLoginObjects;
    public List<AggrFloatingTradesGroupBy> floatingTrades;
    public List<ClientHelper> connectedClientHelpers;
    public List<MirrorUcidObject> mirrorUcidObjects;

    public RuleDataHelper() {
        this.connections = new ArrayList<>();
        this.connectedUsers = new ArrayList<>();
        this.clientFraudTypes = new ArrayList<>();
        this.crmTbAccountObjectConnections = new ArrayList<>();
        this.mtTbCreditsObjects = new ArrayList<>();
        this.crmTbWithdrawalObjects = new ArrayList<>();
        this.crmTbDepositObjects = new ArrayList<>();
        this.crmTbBonusObjects = new ArrayList<>();
        this.mt5DealsObjects = new ArrayList<>();
        this.mtBalanceOrdersObjects = new ArrayList<>();
        this.mirrorLoginObjects = new ArrayList<>();
        this.floatingTrades = new ArrayList<>();
        this.connectedClientHelpers = new ArrayList<>();
        this.mirrorUcidObjects = new ArrayList<>();
    }

    public RuleDataHelper(ClientHelper clientHelper, CrmTbUserObject crmTbUserObject,
            LnSessionParsedObject lnSessionParsedObjectRegistration, LnSessionParsedObject lnSessionParsedObjectLogin,
            List<ConnectionTableEntry> connections, List<CrmTbUserObject> connectedUsers,
            WithdrawalEvent withdrawalEvent, CloseTradeMtEvent closeTradeEvent,
            List<BoClientFraudTypesObject> clientFraudTypes,
            List<CrmTbAccountObject> crmTbAccountObjectConnections, List<MtTbCreditsObject> mtTbCreditsObjects,
            CrmTbAccountObject crmTbAccountObject,
            List<CrmTbWithdrawalObject> crmTbWithdrawalObjects, List<CrmTbDepositObject> crmTbDepositObjects,
            List<CrmTbBonusObject> crmTbBonusObjects, List<Mt5DealsCoercedObject> mt5DealsObjects,
            AggrCreditEquityRateObject aggrCreditEquityRate, MirrorLoginObject aggrMirrorAccountsByTrades,
            List<MtBalanceOrdersObject> mtBalanceOrdersObjects, List<MirrorLoginObject> mirrorLoginObjects,
            List<AggrFloatingTradesGroupBy> floatingTrades, List<ClientHelper> connectedClientHelpers,
            List<MirrorUcidObject> mirrorUcidObjects) {
        this.clientHelper = clientHelper;
        this.crmTbUserObject = crmTbUserObject;
        this.lnSessionParsedObjectRegistration = lnSessionParsedObjectRegistration;
        this.lnSessionParsedObjectLogin = lnSessionParsedObjectLogin;
        this.connections = connections;
        this.connectedUsers = connectedUsers;
        this.withdrawalEvent = withdrawalEvent;
        this.closeTradeEvent = closeTradeEvent;
        this.clientFraudTypes = clientFraudTypes;
        this.crmTbAccountObject = crmTbAccountObject;
        this.crmTbAccountObjectConnections = crmTbAccountObjectConnections;
        this.mtTbCreditsObjects = mtTbCreditsObjects;
        this.crmTbWithdrawalObjects = crmTbWithdrawalObjects;
        this.crmTbDepositObjects = crmTbDepositObjects;
        this.crmTbBonusObjects = crmTbBonusObjects;
        this.mt5DealsObjects = mt5DealsObjects;
        this.aggrCreditEquityRate = aggrCreditEquityRate;
        this.aggrMirrorAccountsByTrades = aggrMirrorAccountsByTrades;
        this.mtBalanceOrdersObjects = mtBalanceOrdersObjects;
        this.mirrorLoginObjects = mirrorLoginObjects;
        this.floatingTrades = floatingTrades;
        this.connectedClientHelpers = connectedClientHelpers;
        this.mirrorUcidObjects = mirrorUcidObjects;
    }

    @Override
    public String toString() {
        return "RuleDataHelper{" + "clientHelper=" + clientHelper + ", crmTbUserObject=" + crmTbUserObject + ", lnSessionParsedObjectRegistration=" + lnSessionParsedObjectRegistration + ", lnSessionParsedObjectLogin=" + lnSessionParsedObjectLogin + ", connections=" + connections + ", connectedUsers=" + connectedUsers + ", withdrawalEvent=" + withdrawalEvent + ", closeTradeEvent=" + closeTradeEvent + ", clientFraudTypes=" + clientFraudTypes + ", crmTbAccountObject=" + crmTbAccountObject + ", crmTbAccountObjectConnections=" + crmTbAccountObjectConnections + ", mtTbCreditsObjects=" + mtTbCreditsObjects + ", crmTbWithdrawalObjects=" + crmTbWithdrawalObjects + ", crmTbDepositObjects=" + crmTbDepositObjects + ", crmTbBonusObjects=" + crmTbBonusObjects + ", mt5DealsObjects=" + mt5DealsObjects + ", aggrCreditEquityRate=" + aggrCreditEquityRate + ", aggrMirrorAccountsByTrades=" + aggrMirrorAccountsByTrades + ", mtBalanceOrdersObjects=" + mtBalanceOrdersObjects + ", mirrorLoginObjects=" + mirrorLoginObjects + ", floatingTrades=" + floatingTrades + ", connectedClientHelpers=" + connectedClientHelpers + '}';
    }

    public static void setupRuleData(Map<String, RuleDataHelper> map) throws ReflectiveOperationException,
            SQLException {
        startSshTunnel();
        for (RuleDataHelper data : map.values()) {
            insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            data.connections.forEach(connection -> {
                try {
                    insertObjectToDb(CONNECTIONS_TABLE_NAME, connection);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.connectedUsers.forEach(user -> {
                try {
                    insertObjectToDb(CRM_USER_TABLE_NAME, user);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.clientFraudTypes.forEach(fraud -> {
                try {
                    insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.crmTbAccountObject != null) {
                insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, data.crmTbAccountObject);
            }
            data.crmTbAccountObjectConnections.forEach(credit -> {
                try {
                    insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, credit);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mtTbCreditsObjects.forEach(credit -> {
                try {
                    insertObjectToDb(MT_CREDITS_TABLE_NAME, credit);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbWithdrawalObjects.forEach(withdrawal -> {
                try {
                    insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawal);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbDepositObjects.forEach(deposit -> {
                try {
                    insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbBonusObjects.forEach(bonus -> {
                try {
                    insertObjectToDb(CRM_BONUS_TABLE_NAME, bonus);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mt5DealsObjects.forEach(deal -> {
                try {
                    insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, deal);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mtBalanceOrdersObjects.forEach(deal -> {
                try {
                    insertObjectToDb(MT_BALANCE_ORDERS_TABLE_NAME, deal);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mirrorLoginObjects.forEach(deal -> {
                try {
                    insertObjectToDb(MIRROR_LOGIN_TABLE_NAME, deal);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.aggrCreditEquityRate != null) {
                insertObjectToDb(AGGR_CREDIT_EQUITY_RATE, data.aggrCreditEquityRate);
            }
            if (data.aggrMirrorAccountsByTrades != null) {
                insertObjectToDb(MIRROR_LOGIN_TABLE_NAME, data.aggrMirrorAccountsByTrades);
            }
            if (data.aggrCreditEquityRate != null) {
                insertObjectToDb(AGGR_CREDIT_EQUITY_RATE, data.aggrCreditEquityRate);
            }
            data.connectedClientHelpers.forEach(user -> {
                try {
                    insertObjectToDb(EMAIL_TABLE_NAME, getEmailTableEntryByClient(user));
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mirrorUcidObjects.forEach(mirrorUcidObject -> {
                try {
                    insertObjectToDb(MIRROR_UCID_TABLE_NAME, mirrorUcidObject);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public static void deleteRuleData(Map<String, RuleDataHelper> map) throws Exception {
        for (RuleDataHelper data : map.values()) {
            deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            data.connections.forEach(connection -> {
                try {
                    deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connection.userFrom));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.lnSessionParsedObjectRegistration != null) {
                deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectRegistration.userId));

            }
            if (data.lnSessionParsedObjectLogin != null) {
                deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectLogin.userId));

            }
            data.clientFraudTypes.forEach(fraud -> {
                try {
                    deleteEntryFromDb(CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mtTbCreditsObjects.forEach(credit -> {
                try {
                    deleteEntryFromDb(MT_CREDITS_TABLE_NAME, String.format("ucid = '%s'", credit.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbWithdrawalObjects.forEach(withdrawal -> {
                try {
                    deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, String.format("ucid = '%s'", withdrawal.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbDepositObjects.forEach(deposit -> {
                try {
                    deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", deposit.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.crmTbBonusObjects.forEach(bonus -> {
                try {
                    deleteEntryFromDb(CRM_BONUS_TABLE_NAME, String.format("ucid = '%s'", bonus.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mtBalanceOrdersObjects.forEach(bonus -> {
                try {
                    deleteEntryFromDb(MT_BALANCE_ORDERS_TABLE_NAME, String.format("ucid = '%s'", bonus.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mt5DealsObjects.forEach(deal -> {
                try {
                    deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("server_id = %s and account = %s", deal.serverId, deal.account));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mirrorLoginObjects.forEach(mirrorLoginObject -> {
                try {
                    deleteEntryFromDb(MIRROR_LOGIN_TABLE_NAME, String.format("login_1 = %s", mirrorLoginObject.login_1));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.mirrorUcidObjects.forEach(mirrorUcidObject -> {
                try {
                    deleteEntryFromDb(MIRROR_UCID_TABLE_NAME, String.format("ucid_1 = '%s'", mirrorUcidObject.ucid_1));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.aggrCreditEquityRate != null) {
                deleteEntryFromDb(AGGR_CREDIT_EQUITY_RATE, String.format("trading_account = %s", data.clientHelper.getTradingAccount()));
            }
            deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            cleanUserRestriction(data.clientHelper.getUcid());
            closeAlert(data.clientHelper.getUcid());
        }
        stopSshTunnel();
    }
}