package helpers.data.rules;

import business_objects.db.clickhouse.aggr_credit_equity_rate.AggrCreditEquityRateObject;
import business_objects.db.clickhouse.aggr_floating_trades_group_by.AggrFloatingTradesGroupBy;
import business_objects.db.clickhouse.aggr_mirror_accounts_by_trades.MirrorLoginObject;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_bonus_table.CrmTbBonusObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.device_id_table.DeviceIdTableEntry;
import business_objects.db.clickhouse.dict_account_to_ucid.DictAccountToUcidObject;
import business_objects.db.clickhouse.email_table.EmailTableEntry;
import business_objects.db.clickhouse.ip_table.IpTableEntry;
import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import business_objects.db.clickhouse.loyalties_redemption.LoyaltiesRedemptionObject;
import business_objects.db.clickhouse.mirror_ucid_table.MirrorUcidObject;
import business_objects.db.clickhouse.mtAccount.MtAccountObject;
import business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.db.clickhouse.phone.PhoneTableEntry;
import business_objects.db.clickhouse.session_id.SessionIdTableEntry;
import business_objects.kafka.crm_events.EgRegistrationEvent;
import business_objects.kafka.crm_events.EgWithdrawalEvent;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import helpers.data.ClientHelper;
import helpers.database.DbName;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.CleanTableHelper.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.waitForConnectionSearchToUpdate;

public class RuleDataHelper {
    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public DictAccountToUcidObject dictAccountToUcidObject;
    public LnSessionParsedObject lnSessionParsedObjectRegistration;
    public LnSessionParsedObject lnSessionParsedObjectLogin;
    public List<ConnectionTableEntry> connections;
    public List<CrmTbUserObject> connectedUsers;
    public EgWithdrawalEvent withdrawalEvent;
    public CloseTradeMtEvent closeTradeEvent;
    public List<ClientFraudTypes> clientFraudTypes;
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
    public LnSessionParsedObject lnSessionParsedObject;
    public EgRegistrationEvent registrationEvent;
    public List<SessionIdTableEntry> sessionIdTableEntries;
    public List<EmailTableEntry> emailTableEntries;
    public List<PhoneTableEntry> phoneTableEntries;
    public List<IpTableEntry> ipTableEntries;
    public List<DeviceIdTableEntry> deviceIdTableEntries;
    public CloseTradeMtEvent closeTradeMtEvent;
    public List<Mt5DealsCoercedObject> mt5DealsObjects;


    public RuleDataHelper() {
    }

    public RuleDataHelper(ClientHelper clientHelper, CrmTbUserObject crmTbUserObject,
            LnSessionParsedObject lnSessionParsedObjectRegistration, LnSessionParsedObject lnSessionParsedObjectLogin,
            List<ConnectionTableEntry> connections, List<CrmTbUserObject> connectedUsers,
            EgWithdrawalEvent withdrawalEvent, CloseTradeMtEvent closeTradeEvent,
            List<ClientFraudTypes> clientFraudTypes,
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

    static Logger logger = Logger.getLogger(RuleDataHelper.class.getName());

    @Override
    public String toString() {
        return "RuleDataHelper{" + "clientHelper=" + clientHelper + ", crmTbUserObject=" + crmTbUserObject + ", lnSessionParsedObjectRegistration=" + lnSessionParsedObjectRegistration + ", lnSessionParsedObjectLogin=" + lnSessionParsedObjectLogin + ", connections=" + connections + ", connectedUsers=" + connectedUsers + ", withdrawalEvent=" + withdrawalEvent + ", closeTradeEvent=" + closeTradeEvent + ", clientFraudTypes=" + clientFraudTypes + ", crmTbAccountObject=" + crmTbAccountObject + ", crmTbAccountObjectConnections=" + crmTbAccountObjectConnections + ", mtTbCreditsObjects=" + mtTbCreditsObjects + ", crmTbWithdrawalObjects=" + crmTbWithdrawalObjects + ", crmTbDepositObjects=" + crmTbDepositObjects + ", crmTbBonusObjects=" + crmTbBonusObjects + ", mt5DealsObjects=" + mt5DealsCoercedObjects + ", aggrCreditEquityRate=" + aggrCreditEquityRate + ", aggrMirrorAccountsByTrades=" + aggrMirrorAccountsByTrades + ", mtBalanceOrdersObjects=" + mtBalanceOrdersObjects + ", mirrorLoginObjects=" + mirrorLoginObjects + ", floatingTrades=" + floatingTrades + ", connectedClientHelpers=" + connectedClientHelpers + '}';
    }

    public static void setupRuleData(Map<String, RuleDataHelper> map) {
        startSshTunnel();
        for (RuleDataHelper data : map.values()) {
            logger.info("WE ARE IN SETUP");
            if (data.connections != null && (!data.connections.isEmpty())) try {
                for (ConnectionTableEntry i : data.connections) {
                    i.datetime = getCurrentTimestampDbFormat();
//                    insertObjectToDb(CONNECTIONS_TABLE_NAME, i);
                    logger.info("WE ARE INSERTING connections");

                    executeQueryToDb(DbName.CLICKHOUSE, " INSERT INTO " + CONNECTIONS_TABLE_NAME + " (user_from, user_to, degree_connection, connection_score, connection_info, `datetime`, ver, status) VALUES('" + i.userFrom + "','" + i.userTo + "','" + i.degreeConnection + "','" + i.connectionScore + "','" + i.connectionInfo + "', NOW(), '1','new');");
                }
                waitForConnectionSearchToUpdate(data.connections.getFirst().userFrom);
            } catch (Exception e) {
                logger.info("Error while inserting connections into table: " + e.getMessage());
            }
            if (data.lnSessionParsedObjectRegistration != null) {
                logger.info("WE ARE INSERTING LN");
                insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObjectRegistration);
            }
            if (data.lnSessionParsedObject != null) {
                logger.info("WE ARE INSERTING LN");
                insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObject);
            }
            if (data.crmTbUserObject != null) {
                insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            }
            if (data.dictAccountToUcidObject != null) {
                insertObjectToDb(DICT_ACCOUNT_TO_UCID, data.dictAccountToUcidObject);
            }

            insertObjectsToDb(CRM_USER_TABLE_NAME, data.connectedUsers);
            if (data.clientFraudTypes != null) {
                data.clientFraudTypes.forEach(fraud -> insertObjectToDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, fraud));
            }
            if (data.crmTbAccountObject != null) {
                insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, data.crmTbAccountObject);
            }
            if (data.crmTbAccountObjectConnections != null) {
                data.crmTbAccountObjectConnections.forEach(credit -> insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, credit));
            }
            if (data.mtTbCreditsObjects != null) {
                data.mtTbCreditsObjects.forEach(credit -> insertObjectToDb(MT_CREDITS_TABLE_NAME, credit));
            }
            if (data.sessionIdTableEntries != null) {
                data.sessionIdTableEntries.forEach(sessionIdTableEntry -> insertObjectToDb(SESSION_ID_TABLE_NAME, sessionIdTableEntry));
            }
            if (data.emailTableEntries != null) {
                data.emailTableEntries.forEach(emailTableEntry -> insertObjectToDb(EMAIL_TABLE_NAME, emailTableEntry));
            }
            if (data.ipTableEntries != null) {
                data.ipTableEntries.forEach(ipTableEntry -> insertObjectToDb(IP_TABLE_NAME, ipTableEntry));
            }
            if (data.phoneTableEntries != null) {
                data.phoneTableEntries.forEach(phoneTableEntry -> insertObjectToDb(PHONE_TABLE_NAME, phoneTableEntry));
            }
            if (data.deviceIdTableEntries != null) {
                data.deviceIdTableEntries.forEach(payout -> insertObjectToDb(DEVICE_ID_TABLE_NAME, payout));
            }
            if (data.crmTbWithdrawalObjects != null) {
                data.crmTbWithdrawalObjects.forEach(withdrawal -> insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, withdrawal));
            }
            if (data.crmTbDepositObjects != null) {
                data.crmTbDepositObjects.forEach(deposit -> insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit));
            }
            if (data.crmTbBonusObjects != null) {
                data.crmTbBonusObjects.forEach(bonus -> insertObjectToDb(CRM_BONUS_TABLE_NAME, bonus));
            }
            if (data.mt5DealsCoercedObjects != null) {
                data.mt5DealsCoercedObjects.forEach(deal -> insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, deal));
            }
            if (data.mtMt5PositionsObjects != null) {
                data.mtMt5PositionsObjects.forEach(position -> insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position));
            }
            if (data.mtBalanceOrdersObjects != null) {
                data.mtBalanceOrdersObjects.forEach(deal -> insertObjectToDb(MT_BALANCE_ORDERS_TABLE_NAME, deal));
            }
            if (data.mirrorLoginObjects != null) {
                data.mirrorLoginObjects.forEach(deal -> insertObjectToDb(MIRROR_LOGIN_TABLE_NAME, deal));
            }
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
            if (data.mirrorLoginObjects != null) {
                data.mirrorUcidObjects.forEach(mirrorUcidObject -> insertObjectToDb(MIRROR_UCID_TABLE_NAME, mirrorUcidObject));
            }
            if (data.mirrorLoginObjects != null) {
                data.loyaltyObjects.forEach(loyaltyObjects -> insertObjectToDb(CRM_TB_LOYALTY_REDEMPTION, loyaltyObjects));
            }
        }
    }

    public static void deleteRuleData(Map<String, RuleDataHelper> map) throws Exception {
        for (RuleDataHelper data : map.values()) {
            if (data.crmTbUserObject != null) {
                deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            }
            if (data.dictAccountToUcidObject != null) {
                deleteEntryFromDb(DICT_ACCOUNT_TO_UCID, String.format("ucid = '%s'", data.dictAccountToUcidObject.ucid));
            }
            if (data.connections != null) {
                data.connections.forEach(connection -> deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connection.userFrom)));
            }
            if (data.lnSessionParsedObjectRegistration != null) {
                deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectRegistration.getUserId()));
            }
            if (data.lnSessionParsedObjectLogin != null) {
                deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObjectLogin.getUserId()));
            }
            if (data.lnSessionParsedObject != null) {
                deleteEntryFromDb(LEXIS_NEXIS_TABLE_NAME, String.format("user_id = %s", data.lnSessionParsedObject.getUserId()));
            }
            if (data.clientFraudTypes != null) {
                data.clientFraudTypes.forEach(fraud -> deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud.getUcid())));
            }
            if (data.mtTbCreditsObjects != null) {
                data.mtTbCreditsObjects.forEach(credit -> deleteEntryFromDb(MT_CREDITS_TABLE_NAME, String.format("ucid = '%s'", credit.ucid)));
            }
            if (data.crmTbWithdrawalObjects != null) {
                data.crmTbWithdrawalObjects.forEach(withdrawal -> deleteEntryFromDb(CRM_WITHDRAWAL_TABLE_NAME, String.format("ucid = '%s'", withdrawal.ucid)));
            }
            if (data.crmTbDepositObjects != null) {
                data.crmTbDepositObjects.forEach(deposit -> deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", deposit.ucid)));
            }
            if (data.crmTbBonusObjects != null) {
                data.crmTbBonusObjects.forEach(bonus -> deleteEntryFromDb(CRM_BONUS_TABLE_NAME, String.format("ucid = '%s'", bonus.ucid)));
            }
            if (data.mtBalanceOrdersObjects != null) {
                data.mtBalanceOrdersObjects.forEach(bonus -> deleteEntryFromDb(MT_BALANCE_ORDERS_TABLE_NAME, String.format("ucid = '%s'", bonus.ucid)));
            }
            if (data.mt5DealsCoercedObjects != null) {
                data.mt5DealsCoercedObjects.forEach(deal -> deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("server_id = %s and account = %s", deal.getServerId(), deal.getAccount())));
            }
            if (data.mtMt5PositionsObjects != null) {
                data.mtMt5PositionsObjects.forEach(position -> deleteEntryFromDb(MT5_POSITIONS_TABLE_NAME, String.format("server_id = %s and account = %s", position.getServerId(), position.getAccount())));
            }
            if (data.mirrorLoginObjects != null) {
                data.mirrorLoginObjects.forEach(mirrorLoginObject -> deleteEntryFromDb(MIRROR_LOGIN_TABLE_NAME, String.format("login_1 = %s", mirrorLoginObject.login_1)));
            }
            if (data.mirrorUcidObjects != null) {
                data.mirrorUcidObjects.forEach(mirrorUcidObject -> deleteEntryFromDb(MIRROR_UCID_TABLE_NAME, String.format("ucid_1 = '%s'", mirrorUcidObject.ucid_1)));
            }
            if (data.sessionIdTableEntries != null) {
                data.sessionIdTableEntries.forEach(sessionIdTableEntry -> deleteEntryFromDb(SESSION_ID_TABLE_NAME, String.format("session_id = '%s'", sessionIdTableEntry.sessionId)));
            }
            if (data.emailTableEntries != null) {
                data.emailTableEntries.forEach(emailTableEntry -> deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("email = '%s'", emailTableEntry.email)));
            }
            if (data.ipTableEntries != null) {
                data.ipTableEntries.forEach(ipTableEntry -> deleteEntryFromDb(IP_TABLE_NAME, String.format("ip = '%s'", ipTableEntry.ip)));
            }
            if (data.phoneTableEntries != null) {
                data.phoneTableEntries.forEach(phoneTableEntry -> deleteEntryFromDb(PHONE_TABLE_NAME, String.format("phone_num = '%s'", phoneTableEntry.phoneNum)));
            }
            if (data.deviceIdTableEntries != null) {
                data.deviceIdTableEntries.forEach(deviceIdTableEntry -> deleteEntryFromDb(DEVICE_ID_TABLE_NAME, String.format("device_id = '%s'", deviceIdTableEntry.deviceId)));
            }
            if (data.aggrCreditEquityRate != null) {
                deleteEntryFromDb(AGGR_CREDIT_EQUITY_RATE, String.format("trading_account = %s", data.clientHelper.getTradingAccount()));
            }
            if (data.aggrCreditEquityRate != null) {
                data.loyaltyObjects.forEach(loyaltyObjects -> deleteEntryFromDb(CRM_TB_LOYALTY_REDEMPTION, String.format("ucid = '%s'", loyaltyObjects.ucid)));
            }
            cleanUserRestrictionGeneral(data.clientHelper.getUcid());
            closeAlert(data.clientHelper.getUcid());
            if ((data.connectedUsers != null) && (!data.connectedUsers.isEmpty())) {
                int size = data.connectedUsers.size();
                StringBuilder sb = new StringBuilder();
                sb.append("(");
                for (CrmTbUserObject user : data.connectedUsers) {
                    sb.append("'");
                    sb.append(user.ucid);
                    sb.append("'");
                    if (size > 1) {
                        sb.append(",");
                        size -= 1;
                    }
                }
                sb.append(")");
                deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid in %s", sb.toString()));
            }
        }
        stopSshTunnel();
    }
}