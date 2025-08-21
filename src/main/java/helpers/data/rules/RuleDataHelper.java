package helpers.data.rules;

import business_objects.db.clickhouse.aggr_credit_equity_rate.AggrCreditEquityRateObject;
import business_objects.db.clickhouse.aggr_floating_trades_group_by.AggrFloatingTradesGroupBy;
import business_objects.db.clickhouse.aggr_mirror_accounts_by_trades.MirrorLoginObject;
import business_objects.db.clickhouse.bo_alerts.BoAlertsObject;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObject;
import business_objects.db.clickhouse.crm_tb_bonus_table.CrmTbBonusObject;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import business_objects.db.clickhouse.device_id_table.DeviceIdTableEntry;
import business_objects.db.clickhouse.dict_account_to_ucid.DictAccountToUcidObject;
import business_objects.db.clickhouse.dict_active_trading_days_by_ucid.dict_is_test.DictActiveTradingDaysByUcidObject;
import business_objects.db.clickhouse.dict_is_test.DictIsTestObject;
import business_objects.db.clickhouse.email_table.EmailTableEntry;
import business_objects.db.clickhouse.ip_table.IpTableEntry;
import business_objects.db.clickhouse.ln_session_parsed.LnSessionParsedObject;
import business_objects.db.clickhouse.loyalties_redemption.LoyaltiesRedemptionObject;
import business_objects.db.clickhouse.mirror_ucid_table.MirrorUcidObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObject;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import business_objects.db.clickhouse.mt_mt5_positions.MtMt5PositionsObject;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
import business_objects.db.clickhouse.phone.PhoneTableEntry;
import business_objects.db.clickhouse.s3_fact_ib_sales_commissions.S3FactIbSalesCommissionsObject;
import business_objects.db.clickhouse.session_id.SessionIdTableEntry;
import business_objects.db.data_science.ucid_general_score.UcidGeneralScore;
import business_objects.kafka.alerts.RuleAlert;
import business_objects.kafka.crm_events.EgWithdrawalEvent;
import business_objects.kafka.crm_events.LoginEvent;
import business_objects.kafka.crm_events.RegistrationEvent;
import business_objects.kafka.mt_events.CloseTradeMtEvent;
import business_objects.db.data_science.ucid_mirror_score.UcidMirrorScore;
import business_objects.kafka.mt_events.TradeEvent;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudTypeOld;
import helpers.data.enums.FraudTypeStatus;
import helpers.database.DbName;
import businessObjects.db.clickhouse.ozTrades.OzTradesTableEntry;
import net.datafaker.Faker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static business_objects.db.clickhouse.client_fraud_types.ClientFraudTypesFactory.createClientFraudTypeCh;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntry.ConnectionInfo.connectionInfoToString;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.getConnection;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.device_id_table.DeviceIdTableEntryFactory.deviceIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.email_table.EmailTableEntryFactory.emailTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.phone.PhoneTableEntryFactory.phoneTableEntryForConnectionSearch;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.enums.FraudType.HEDGING;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.CleanTableHelper.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.waitForConnectionSearchToUpdate;

public class RuleDataHelper {

    static Faker faker = new Faker();

    public ClientHelper clientHelper;
    public CrmTbUserObject crmTbUserObject;
    public DictAccountToUcidObject dictAccountToUcidObject;
    public DictIsTestObject dictIsTestObject;
    public List<DictActiveTradingDaysByUcidObject> dictActiveTradingDaysByUcidObject;
    public LnSessionParsedObject lnSessionParsedObjectRegistration;
    public LnSessionParsedObject lnSessionParsedObjectLogin;
    public List<ConnectionTableEntry> connections;
    public List<CrmTbUserObject> connectedUsers;
    public EgWithdrawalEvent withdrawalEvent;
    public CloseTradeMtEvent closeTradeEvent;
    public TradeEvent tradeEvent;
    public List<ClientFraudTypes> clientFraudTypes;
    public CrmTbAccountObject crmTbAccountObject;
    public CrmTbAccountForMtObject crmTbAccountForMtObject;
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
    public RegistrationEvent registrationEvent;
    public LoginEvent loginEvent;
    public List<SessionIdTableEntry> sessionIdTableEntries;
    public List<EmailTableEntry> emailTableEntries;
    public List<PhoneTableEntry> phoneTableEntries;
    public List<IpTableEntry> ipTableEntries;
    public List<DeviceIdTableEntry> deviceIdTableEntries;
    public CloseTradeMtEvent closeTradeMtEvent;
    public List<Mt5DealsCoercedObject> mt5DealsObjects;
    public List<S3FactIbSalesCommissionsObject> s3FactIbSalesCommissionsObject;
    public UcidMirrorScore ucidMirrorScore;
    public List<RuleAlert> ruleAlerts;
    public List<BoAlertsObject> boAlertsObjects;
    public List<OzTradesTableEntry> ozTradesTableObjects;
    public UcidGeneralScore ucidGeneralScore;

    public RuleDataHelper() {
    }

    public RuleDataHelper(ClientHelper clientHelper, CrmTbUserObject crmTbUserObject,
            DictAccountToUcidObject dictAccountToUcidObject, DictIsTestObject dictIsTestObject,
            List<DictActiveTradingDaysByUcidObject> dictActiveTradingDaysByUcidObjects,
            LnSessionParsedObject lnSessionParsedObjectRegistration, LnSessionParsedObject lnSessionParsedObjectLogin,
            List<ConnectionTableEntry> connections, List<CrmTbUserObject> connectedUsers,
            EgWithdrawalEvent withdrawalEvent, CloseTradeMtEvent closeTradeEvent, TradeEvent tradeEvent,
            List<ClientFraudTypes> clientFraudTypes,
            List<CrmTbAccountObject> crmTbAccountObjectConnections, List<MtTbCreditsObject> mtTbCreditsObjects,
            CrmTbAccountObject crmTbAccountObject, CrmTbAccountForMtObject crmTbAccountForMtObject,
            List<CrmTbWithdrawalObject> crmTbWithdrawalObjects, List<CrmTbDepositObject> crmTbDepositObjects,
            List<CrmTbBonusObject> crmTbBonusObjects, List<Mt5DealsCoercedObject> mt5DealsCoercedObjects,
            AggrCreditEquityRateObject aggrCreditEquityRate, MirrorLoginObject aggrMirrorAccountsByTrades,
            List<MtBalanceOrdersObject> mtBalanceOrdersObjects, List<MirrorLoginObject> mirrorLoginObjects,
            List<AggrFloatingTradesGroupBy> floatingTrades, List<ClientHelper> connectedClientHelpers,
            List<MirrorUcidObject> mirrorUcidObjects, MtAccountObject mtAccountObject,
            List<LoyaltiesRedemptionObject> loyaltyObjects, List<MtMt5PositionsObject> mtMt5PositionsObjects,
            List<S3FactIbSalesCommissionsObject> s3FactIbSalesCommissionsObject, UcidMirrorScore ucidMirrorScore,
            List<RuleAlert> ruleAlerts, List<BoAlertsObject> boAlertsObjects,
            List<OzTradesTableEntry> ozTradesTableObjects, RegistrationEvent registrationEvent, LoginEvent loginEvent,
            UcidGeneralScore ucidGeneralScore) {
        this.clientHelper = clientHelper;
        this.crmTbUserObject = crmTbUserObject;
        this.dictAccountToUcidObject = dictAccountToUcidObject;
        this.dictIsTestObject = dictIsTestObject;
        this.dictActiveTradingDaysByUcidObject = dictActiveTradingDaysByUcidObjects;
        this.lnSessionParsedObjectRegistration = lnSessionParsedObjectRegistration;
        this.lnSessionParsedObjectLogin = lnSessionParsedObjectLogin;
        this.connections = connections;
        this.connectedUsers = connectedUsers;
        this.withdrawalEvent = withdrawalEvent;
        this.closeTradeEvent = closeTradeEvent;
        this.tradeEvent = tradeEvent;
        this.clientFraudTypes = clientFraudTypes;
        this.crmTbAccountObject = crmTbAccountObject;
        this.crmTbAccountForMtObject = crmTbAccountForMtObject;
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
        this.s3FactIbSalesCommissionsObject = s3FactIbSalesCommissionsObject;
        this.ucidMirrorScore = ucidMirrorScore;
        this.ruleAlerts = ruleAlerts;
        this.boAlertsObjects = boAlertsObjects;
        this.ozTradesTableObjects = ozTradesTableObjects;
        this.registrationEvent = registrationEvent;
        this.loginEvent = loginEvent;
        this.ucidGeneralScore = ucidGeneralScore;
    }

    static Logger logger = Logger.getLogger(RuleDataHelper.class.getName());

    @Override
    public String toString() {
        return "RuleDataHelper{" + "clientHelper=" + clientHelper + ", crmTbUserObject=" + crmTbUserObject + ", dictAccountToUcidObject=" + dictAccountToUcidObject + ", dictIsTestObject=" + dictIsTestObject + ", lnSessionParsedObjectRegistration=" + lnSessionParsedObjectRegistration + ", lnSessionParsedObjectLogin=" + lnSessionParsedObjectLogin + ", connections=" + connections + ", connectedUsers=" + connectedUsers + ", withdrawalEvent=" + withdrawalEvent + ", closeTradeEvent=" + closeTradeEvent + ", clientFraudTypes=" + clientFraudTypes + ", crmTbAccountObject=" + crmTbAccountObject + ", crmTbAccountObjectConnections=" + crmTbAccountObjectConnections + ", mtTbCreditsObjects=" + mtTbCreditsObjects + ", crmTbWithdrawalObjects=" + crmTbWithdrawalObjects + ", crmTbDepositObjects=" + crmTbDepositObjects + ", crmTbBonusObjects=" + crmTbBonusObjects + ", mt5DealsCoercedObjects=" + mt5DealsCoercedObjects + ", aggrCreditEquityRate=" + aggrCreditEquityRate + ", aggrMirrorAccountsByTrades=" + aggrMirrorAccountsByTrades + ", mtBalanceOrdersObjects=" + mtBalanceOrdersObjects + ", mirrorLoginObjects=" + mirrorLoginObjects + ", floatingTrades=" + floatingTrades + ", connectedClientHelpers=" + connectedClientHelpers + ", mirrorUcidObjects=" + mirrorUcidObjects + ", mtAccountObject=" + mtAccountObject + ", loyaltyObjects=" + loyaltyObjects + ", mtMt5PositionsObjects=" + mtMt5PositionsObjects + ", lnSessionParsedObject=" + lnSessionParsedObject + ", registrationEvent=" + registrationEvent + ", sessionIdTableEntries=" + sessionIdTableEntries + ", emailTableEntries=" + emailTableEntries + ", phoneTableEntries=" + phoneTableEntries + ", ipTableEntries=" + ipTableEntries + ", deviceIdTableEntries=" + deviceIdTableEntries + ", closeTradeMtEvent=" + closeTradeMtEvent + ", mt5DealsObjects=" + mt5DealsObjects + '}';
    }

    public static void setupRuleData(Map<String, RuleDataHelper> map) {
        startSshTunnel();
        for (RuleDataHelper data : map.values()) {
            logger.info("WE ARE IN SETUP");
            if (data.connections != null && (!data.connections.isEmpty())) try {
                for (ConnectionTableEntry i : data.connections) {
                    i.datetime = getCurrentTimestampDbFormat();
                    logger.info("WE ARE INSERTING connections");

                    executeQueryToDb(DbName.CLICKHOUSE, " INSERT INTO " + CONNECTIONS_TABLE_NAME + " (user_from, user_to, degree_connection, connection_score, connection_info, `datetime`, ver, status) VALUES('" + i.userFrom + "','" + i.userTo + "','" + i.degreeConnection + "','" + i.connectionScore + "','" + i.connectionInfo + "', NOW(), '1','new');");
                }
                waitForConnectionSearchToUpdate(data.connections.getFirst().userFrom);
            } catch (Exception e) {
                logger.info("Error while inserting connections into table: " + e.getMessage());
            }
            if (data.crmTbUserObject != null) {
                insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            }
            if (data.dictAccountToUcidObject != null) {
                insertObjectToDb(DICT_ACCOUNT_TO_UCID, data.dictAccountToUcidObject);
            }
            if (data.dictIsTestObject != null) {
                insertObjectToDb(DICT_IS_TEST, data.dictIsTestObject);
            }
            if (data.dictActiveTradingDaysByUcidObject != null) {
                data.dictActiveTradingDaysByUcidObject.forEach(tradingDays -> insertObjectToDb(DICT_ACTIVE_TRADE_DAYS_BY_UCID, tradingDays));
            }
            if (data.connectedUsers != null) {
                data.connectedUsers.forEach(user -> insertObjectToDb(CRM_USER_TABLE_NAME, user));
            }
            if (data.lnSessionParsedObjectRegistration != null) {
                insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObjectRegistration);
            }
            if (data.lnSessionParsedObject != null) {
                insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObject);
            }
            if (data.clientFraudTypes != null) {
                data.clientFraudTypes.forEach(fraud -> insertObjectToDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, fraud));
            }
            if (data.crmTbAccountObject != null) {
                insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, data.crmTbAccountObject);
            }
            if (data.crmTbAccountForMtObject != null) {
                insertObjectToDb(CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, data.crmTbAccountForMtObject);
            }
            if (data.crmTbAccountObjectConnections != null) {
                data.crmTbAccountObjectConnections.forEach(credit -> insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, credit));
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
            if (data.mt5DealsCoercedObjects != null && !data.mt5DealsCoercedObjects.isEmpty()) {
                insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, data.mt5DealsCoercedObjects);
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
            if (data.loyaltyObjects != null) {
                data.loyaltyObjects.forEach(loyaltyObjects -> insertObjectToDb(CRM_TB_LOYALTY_REDEMPTION, loyaltyObjects));
            }
            if (data.s3FactIbSalesCommissionsObject != null) {
                data.s3FactIbSalesCommissionsObject.forEach(salesComm -> insertObjectToDb(S3_FACT_IB_SALES_COMMISSIONS, salesComm));
            }
            if (data.ucidMirrorScore != null) {
                insertObjectToDb(DATA_SCIENCE_UCID_MIRROR_SCORE_TABLE_NAME, data.ucidMirrorScore);
            }
            if (data.boAlertsObjects != null) {
                data.boAlertsObjects.forEach(alerts -> insertObjectToDb(CLICKHOUSE_BO_ALERT_TABLE_NAME, alerts));
            }
            if (data.ozTradesTableObjects != null) {
                data.ozTradesTableObjects.forEach(ozTrade -> insertObjectToDb(CLICKHOUSE_OZ_TRADES_TABLE_NAME, ozTrade));
            }
            if (data.ucidGeneralScore != null) {
                insertObjectToDb(DATA_SCIENCE_UCID_GENERAL_SCORE_TABLE_NAME, data.ucidGeneralScore);
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
            if (data.dictActiveTradingDaysByUcidObject != null) {
                data.dictActiveTradingDaysByUcidObject.forEach(tradingDays -> deleteEntryFromDb(DICT_ACTIVE_TRADE_DAYS_BY_UCID, String.format("ucid = '%s'", tradingDays.ucid)));
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
            if (data.s3FactIbSalesCommissionsObject != null) {
                data.s3FactIbSalesCommissionsObject.forEach(salesComm -> deleteEntryFromDb(S3_FACT_IB_SALES_COMMISSIONS, String.format("ucid = '%s'", salesComm.getUcid())));
            }
            if (data.ucidMirrorScore != null) {
                deleteObjectFromDb(DATA_SCIENCE_UCID_MIRROR_SCORE_TABLE_NAME, String.format("ucid = '%s'", data.clientHelper.getUcid()));
            }
            if (data.boAlertsObjects != null) {
                data.boAlertsObjects.forEach(alert -> deleteEntryFromDb(CLICKHOUSE_BO_ALERT_TABLE_NAME, String.format("alert_id = '%s'", alert.getAlertId())));
            }
            if (data.ozTradesTableObjects != null) {
                data.ozTradesTableObjects.forEach(ozTrade -> deleteEntryFromDb(CLICKHOUSE_OZ_TRADES_TABLE_NAME, String.format("ucid = '%s'", ozTrade.getUcid())));
            }
            if (data.ucidGeneralScore != null) {
                deleteEntryFromDb(DATA_SCIENCE_UCID_GENERAL_SCORE_TABLE_NAME, String.format("ucid = '%s'", data.ucidGeneralScore.getUcid()));
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
                deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid in %s", sb));
            }
        }
        stopSshTunnel();
    }

    public static RuleDataHelper createClient(RuleDataHelper ruleDataHelper, ClientHelper clientHelper) {
        ruleDataHelper.clientHelper = clientHelper;
        ruleDataHelper.crmTbUserObject = generateUserByClient(ruleDataHelper.clientHelper);
        ruleDataHelper.crmTbAccountObject = generateAccountByClient(ruleDataHelper.clientHelper, false);
        ruleDataHelper.crmTbAccountForMtObject = generateAccountForMtByClient(ruleDataHelper.clientHelper, false);
        return ruleDataHelper;
    }

    protected static void setupAttrConnectionEmailPhoneWithCustomScore(RuleDataHelper data,
            ClientHelper connectedClient, Double score) {

        if (data.connections == null) {
            data.connections = new ArrayList<>();
        }
        if (data.phoneTableEntries == null) {
            data.phoneTableEntries = new ArrayList<>();
        }
        if (data.emailTableEntries == null) {
            data.emailTableEntries = new ArrayList<>();
        }
        connectedClient.setEmail(data.clientHelper.getEmail());
        connectedClient.setPhoneNumber(data.clientHelper.getPhoneNumber());

        //add connection with connected client
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient, score);
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "email";
        connectionInfo1.connectionAttributeValue = data.clientHelper.getEmail();
        connectionInfo1.sourceAttributeValue = data.clientHelper.getEmail();
        connectionInfo1.relationType = "exact";
        ConnectionTableEntry.ConnectionInfo connectionInfo2 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo2.connectionAttributeName = "phone";
        connectionInfo2.connectionAttributeValue = data.clientHelper.getPhoneNumber();
        connectionInfo2.sourceAttributeValue = data.clientHelper.getPhoneNumber();
        connectionInfo2.relationType = "exact";
        connection.connectionInfo = connectionInfoToString(List.of(connectionInfo1, connectionInfo2));
        connection.connectionScore = score;
        data.connections.add(connection);
        //add email to LN record
        data.lnSessionParsedObject.setEmail(data.clientHelper.getEmail());
        data.lnSessionParsedObject.setMobile(data.clientHelper.getPhoneNumber());

        //add to emails table records with same email for initial and connected clients

        data.emailTableEntries.add(emailTableEntryForConnectionSearch(data.clientHelper, data.clientHelper.getEmail()));
        data.emailTableEntries.add(emailTableEntryForConnectionSearch(connectedClient, data.clientHelper.getEmail()));

        //add to phone table records with same email for initial and connected clients
        data.phoneTableEntries.add(phoneTableEntryForConnectionSearch(data.clientHelper, data.clientHelper.getPhoneNumber()));
        data.phoneTableEntries.add(phoneTableEntryForConnectionSearch(connectedClient, data.clientHelper.getPhoneNumber()));
    }

    public static void setupAttrConnectionDevice(
            RuleDataHelper data,
            ClientHelper connectedClient) {

        if (data.connections == null) {
            data.connections = new ArrayList<>();
        }
        if (data.deviceIdTableEntries == null) {
            data.deviceIdTableEntries = new ArrayList<>();
        }
        connectedClient.setDeviceId(data.clientHelper.getDeviceId());

        //add connection with connected client
        ConnectionTableEntry connection = getConnection(data.clientHelper, connectedClient);
        ConnectionTableEntry.ConnectionInfo connectionInfo1 = new ConnectionTableEntry.ConnectionInfo();
        connectionInfo1.connectionAttributeName = "device";
        connectionInfo1.connectionAttributeValue = data.clientHelper.getDeviceId();
        connectionInfo1.sourceAttributeValue = data.clientHelper.getDeviceId();
        connectionInfo1.relationType = "exact";
        connection.connectionScore = 0.7;
        data.connections.add(connection);
        //add email to LN record
        data.lnSessionParsedObject.setDeviceId(data.clientHelper.getDeviceId());

        //add to emails table records with same email for initial and connected clients

        data.deviceIdTableEntries.add(deviceIdTableEntryForConnectionSearch(data.clientHelper, data.clientHelper.getDeviceId()));
        data.deviceIdTableEntries.add(deviceIdTableEntryForConnectionSearch(connectedClient, data.clientHelper.getDeviceId()));
    }

    public static void addConnectionByEmailPhoneAttribute(RuleDataHelper data, ClientHelper clientTo, Double score) {
        if (data.connectedUsers == null) {
            data.connectedUsers = new ArrayList<>();
        }
        if (data.connectedClientHelpers == null) {
            data.connectedClientHelpers = new ArrayList<>();
        }
        data.connectedUsers.add(generateUserByClient(clientTo));
        data.connectedClientHelpers.add(clientTo);
        setupAttrConnectionEmailPhoneWithCustomScore(data, clientTo, score);
    }

    public static void addConnectionByDeviceAttribute(RuleDataHelper data, ClientHelper clientTo) {
        if (data.connectedUsers == null) {
            data.connectedUsers = new ArrayList<>();
        }
        if (data.connectedClientHelpers == null) {
            data.connectedClientHelpers = new ArrayList<>();
        }
        data.connectedUsers.add(generateUserByClient(clientTo));
        data.connectedClientHelpers.add(clientTo);
        setupAttrConnectionDevice(data, clientTo);
    }

    public static RuleDataHelper addFraudTypeToConnectedUser(RuleDataHelper data, FraudTypeStatus status)
            throws IOException, InterruptedException {
        data.clientFraudTypes.add(createClientFraudTypeCh(data.connectedClientHelpers.getFirst().getUcid(), FraudTypeOld.HEDGING.getKey()));

        insertObjectToDb(CRM_USER_TABLE_NAME, data.connectedUsers.getFirst());
        addFraudsForClient(data.connectedClientHelpers.getFirst(), List.of(HEDGING), status);
        return data;
    }

}
