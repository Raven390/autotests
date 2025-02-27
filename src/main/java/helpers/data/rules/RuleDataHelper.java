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
import businessObjects.db.clickhouse.dictAccountToUcid.DictAccountToUcidObject;
import businessObjects.db.clickhouse.lnSessionParsed.LnSessionParsedObject;
import businessObjects.db.clickhouse.loyaltiesRedemption.LoyaltiesRedemptionObject;
import businessObjects.db.clickhouse.mirrorUcidTable.MirrorUcidObject;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
import businessObjects.db.clickhouse.mtBalanceOrdersTable.MtBalanceOrdersObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import businessObjects.db.clickhouse.mtMt5Positions.MtMt5PositionsObject;
import businessObjects.db.clickhouse.mtTbCredits.MtTbCreditsObject;
import businessObjects.kafka.crmEvents.WithdrawalEvent;
import businessObjects.kafka.mtEvents.CloseTradeMtEvent;
import helpers.data.ClientHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.MitigationHelper.cleanUserRestriction;
import static utils.Constants.*;

public class RuleDataHelper {
    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public DictAccountToUcidObject dictAccountToUcidObject;
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
    public List<Mt5DealsCoercedObject> mt5DealsCoercedObjects;
    public AggrCreditEquityRateObject aggrCreditEquityRate;
    public MirrorLoginObject aggrMirrorAccountsByTrades;
    public List<MtBalanceOrdersObject> mtBalanceOrdersObjects;
    public List<MirrorLoginObject> mirrorLoginObjects;
    public List<AggrFloatingTradesGroupBy> floatingTrades;
    public List<ClientHelper> connectedClientHelpers;
    public List<MirrorUcidObject> mirrorUcidObjects;
    public MtAccountObject mtAccountObject;
    public List<LoyaltiesRedemptionObject> loyaltyObjects;
    public List<MtMt5PositionsObject> mtMt5PositionsObjects;

    public RuleDataHelper() {
        this.connections = new ArrayList<>();
        this.connectedUsers = new ArrayList<>();
        this.clientFraudTypes = new ArrayList<>();
        this.crmTbAccountObjectConnections = new ArrayList<>();
        this.mtTbCreditsObjects = new ArrayList<>();
        this.crmTbWithdrawalObjects = new ArrayList<>();
        this.crmTbDepositObjects = new ArrayList<>();
        this.crmTbBonusObjects = new ArrayList<>();
        this.mt5DealsCoercedObjects = new ArrayList<>();
        this.mtBalanceOrdersObjects = new ArrayList<>();
        this.mirrorLoginObjects = new ArrayList<>();
        this.floatingTrades = new ArrayList<>();
        this.connectedClientHelpers = new ArrayList<>();
        this.mirrorUcidObjects = new ArrayList<>();
        this.mtMt5PositionsObjects = new ArrayList<>();
    }

    public RuleDataHelper(ClientHelper clientHelper, CrmTbUserObject crmTbUserObject,
            LnSessionParsedObject lnSessionParsedObjectRegistration, LnSessionParsedObject lnSessionParsedObjectLogin,
            List<ConnectionTableEntry> connections, List<CrmTbUserObject> connectedUsers,
            WithdrawalEvent withdrawalEvent, CloseTradeMtEvent closeTradeEvent,
            List<BoClientFraudTypesObject> clientFraudTypes,
            List<CrmTbAccountObject> crmTbAccountObjectConnections, List<MtTbCreditsObject> mtTbCreditsObjects,
            CrmTbAccountObject crmTbAccountObject,
            List<CrmTbWithdrawalObject> crmTbWithdrawalObjects, List<CrmTbDepositObject> crmTbDepositObjects,
            List<CrmTbBonusObject> crmTbBonusObjects, List<Mt5DealsCoercedObject> mt5DealsCoercedObjects,
            AggrCreditEquityRateObject aggrCreditEquityRate, MirrorLoginObject aggrMirrorAccountsByTrades,
            List<MtBalanceOrdersObject> mtBalanceOrdersObjects, List<MirrorLoginObject> mirrorLoginObjects,
            List<AggrFloatingTradesGroupBy> floatingTrades, List<ClientHelper> connectedClientHelpers,
            List<MirrorUcidObject> mirrorUcidObjects, MtAccountObject mtAccountObject,
            List<LoyaltiesRedemptionObject> loyaltyObjects, List<MtMt5PositionsObject> mtMt5PositionsObjects) {
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
        this.mt5DealsCoercedObjects = mt5DealsCoercedObjects;
        this.aggrCreditEquityRate = aggrCreditEquityRate;
        this.aggrMirrorAccountsByTrades = aggrMirrorAccountsByTrades;
        this.mtBalanceOrdersObjects = mtBalanceOrdersObjects;
        this.mirrorLoginObjects = mirrorLoginObjects;
        this.floatingTrades = floatingTrades;
        this.connectedClientHelpers = connectedClientHelpers;
        this.mirrorUcidObjects = mirrorUcidObjects;
        this.mtAccountObject = mtAccountObject;
        this.loyaltyObjects = loyaltyObjects;
        this.mtMt5PositionsObjects = mtMt5PositionsObjects;
    }

    @Override
    public String toString() {
        return "RuleDataHelper{" + "clientHelper=" + clientHelper + ", crmTbUserObject=" + crmTbUserObject + ", lnSessionParsedObjectRegistration=" + lnSessionParsedObjectRegistration + ", lnSessionParsedObjectLogin=" + lnSessionParsedObjectLogin + ", connections=" + connections + ", connectedUsers=" + connectedUsers + ", withdrawalEvent=" + withdrawalEvent + ", closeTradeEvent=" + closeTradeEvent + ", clientFraudTypes=" + clientFraudTypes + ", crmTbAccountObject=" + crmTbAccountObject + ", crmTbAccountObjectConnections=" + crmTbAccountObjectConnections + ", mtTbCreditsObjects=" + mtTbCreditsObjects + ", crmTbWithdrawalObjects=" + crmTbWithdrawalObjects + ", crmTbDepositObjects=" + crmTbDepositObjects + ", crmTbBonusObjects=" + crmTbBonusObjects + ", mt5DealsObjects=" + mt5DealsCoercedObjects + ", aggrCreditEquityRate=" + aggrCreditEquityRate + ", aggrMirrorAccountsByTrades=" + aggrMirrorAccountsByTrades + ", mtBalanceOrdersObjects=" + mtBalanceOrdersObjects + ", mirrorLoginObjects=" + mirrorLoginObjects + ", floatingTrades=" + floatingTrades + ", connectedClientHelpers=" + connectedClientHelpers + '}';
    }

    public static void setupRuleData(Map<String, RuleDataHelper> map) {
        startSshTunnel();
        for (RuleDataHelper data : map.values()) {
            if (data.lnSessionParsedObjectRegistration != null) {
                insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObjectRegistration);
            }
            if (data.crmTbUserObject != null) {
                insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            }
            if (data.dictAccountToUcidObject != null) {
                insertObjectToDb(DICT_ACCOUNT_TO_UCID, data.dictAccountToUcidObject);
            }
            data.connections.forEach(connection -> {
                insertObjectToDb(CONNECTIONS_TABLE_NAME, connection);
            });
            data.connectedUsers.forEach(user -> {
                insertObjectToDb(CRM_USER_TABLE_NAME, user);
            });
            data.clientFraudTypes.forEach(fraud -> {
                insertObjectToDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, fraud);
            });
            if (data.crmTbAccountObject != null) {
                insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, data.crmTbAccountObject);
            }
            data.crmTbAccountObjectConnections.forEach(credit -> {
                insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, credit);
            });
            data.mtTbCreditsObjects.forEach(credit -> {
                insertObjectToDb(MT_CREDITS_TABLE_NAME, credit);
            });
            data.crmTbWithdrawalObjects.forEach(withdrawal -> {
                insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawal);
            });
            data.crmTbDepositObjects.forEach(deposit -> {
                insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit);
            });
            data.crmTbBonusObjects.forEach(bonus -> {
                insertObjectToDb(CRM_BONUS_TABLE_NAME, bonus);
            });
            data.mt5DealsCoercedObjects.forEach(deal -> {
                insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, deal);
            });
            data.mtMt5PositionsObjects.forEach(position -> {
                insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position);
            });
            data.mtBalanceOrdersObjects.forEach(deal -> {
                insertObjectToDb(MT_BALANCE_ORDERS_TABLE_NAME, deal);
            });
            data.mirrorLoginObjects.forEach(deal -> {
                insertObjectToDb(MIRROR_LOGIN_TABLE_NAME, deal);
            });
            if (data.aggrCreditEquityRate != null) {
                insertObjectToDb(AGGR_CREDIT_EQUITY_RATE, data.aggrCreditEquityRate);
            }
            if (data.aggrMirrorAccountsByTrades != null) {
                insertObjectToDb(MIRROR_LOGIN_TABLE_NAME, data.aggrMirrorAccountsByTrades);
            }
            if (data.mtAccountObject != null) {
                insertObjectToDb(MT_ACCOUNT_TABLE_NAME, data.mtAccountObject);
            }
            if (data.aggrCreditEquityRate != null) {
                insertObjectToDb(AGGR_CREDIT_EQUITY_RATE, data.aggrCreditEquityRate);
            }
            data.mirrorUcidObjects.forEach(mirrorUcidObject -> {
                insertObjectToDb(MIRROR_UCID_TABLE_NAME, mirrorUcidObject);
            });
            data.loyaltyObjects.forEach(loyaltyObjects -> {
                insertObjectToDb(CRM_TB_LOYALTY_REDEMPTION, loyaltyObjects);
            });
        }
    }

    public static void deleteRuleData(Map<String, RuleDataHelper> map) throws Exception {
        for (RuleDataHelper data : map.values()) {
            if (data.crmTbUserObject != null) {
                deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            }
            if (data.dictAccountToUcidObject != null) {
                deleteEntryFromDb(DICT_ACCOUNT_TO_UCID, String.format("ucid = %s", data.dictAccountToUcidObject.ucid));
            }
            data.connections.forEach(connection -> {
                deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connection.userFrom));
            });
            if (data.lnSessionParsedObjectRegistration != null) {
                deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectRegistration.userId));
            }
            if (data.lnSessionParsedObjectLogin != null) {
                deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectLogin.userId));
            }
            data.clientFraudTypes.forEach(fraud -> {
                deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud.ucid));
            });
            data.mtTbCreditsObjects.forEach(credit -> {
                deleteEntryFromDb(MT_CREDITS_TABLE_NAME, String.format("ucid = '%s'", credit.ucid));
            });
            data.crmTbWithdrawalObjects.forEach(withdrawal -> {
                deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, String.format("ucid = '%s'", withdrawal.ucid));
            });
            data.crmTbDepositObjects.forEach(deposit -> {
                deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", deposit.ucid));
            });
            data.crmTbBonusObjects.forEach(bonus -> {
                deleteEntryFromDb(CRM_BONUS_TABLE_NAME, String.format("ucid = '%s'", bonus.ucid));
            });
            data.mtBalanceOrdersObjects.forEach(bonus -> {
                deleteEntryFromDb(MT_BALANCE_ORDERS_TABLE_NAME, String.format("ucid = '%s'", bonus.ucid));
            });
            data.mt5DealsCoercedObjects.forEach(deal -> {
                deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("server_id = %s and account = %s", deal.serverId, deal.account));
            });
            data.mtMt5PositionsObjects.forEach(position -> {
                deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("server_id = %s and account = %s", position.serverId, position.account));
            });
            data.mirrorLoginObjects.forEach(mirrorLoginObject -> {
                deleteEntryFromDb(MIRROR_LOGIN_TABLE_NAME, String.format("login_1 = %s", mirrorLoginObject.login_1));
            });
            data.mirrorUcidObjects.forEach(mirrorUcidObject -> {
                deleteEntryFromDb(MIRROR_UCID_TABLE_NAME, String.format("ucid_1 = '%s'", mirrorUcidObject.ucid_1));
            });
            if (data.aggrCreditEquityRate != null) {
                deleteEntryFromDb(AGGR_CREDIT_EQUITY_RATE, String.format("trading_account = %s", data.clientHelper.getTradingAccount()));
            }
            data.loyaltyObjects.forEach(loyaltyObjects -> {
                deleteEntryFromDb(CRM_TB_LOYALTY_REDEMPTION, String.format("ucid = '%s'", loyaltyObjects.ucid));
            });
            cleanUserRestriction(data.clientHelper.getUcid());
            closeAlert(data.clientHelper.getUcid());
        }
        stopSshTunnel();
    }
}