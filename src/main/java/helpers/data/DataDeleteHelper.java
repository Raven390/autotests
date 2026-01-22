package helpers.data;

import static helpers.database.BoHelper.closeAlert;
import static helpers.database.CleanTableHelper.cleanUserRestrictionGeneral;
import static helpers.database.DbHelper.*;
import static utils.Constants.*;
import static utils.Utils.writeLog;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import java.util.Map;

public class DataDeleteHelper {
    private static final String DELETE_BY_UCID = "ucid = '%s'";
    private static final String DELETE_BY_USER_ID = "user_id = %s";
    private static final String DELETE_BY_ID = "id = '%s'";
    private static final String DELETE_BY_ALERT_ID = "alert_id = '%s'";
    private static final String DELETE_BY_DEVICE_ID = "device_id = '%s'";
    private static final String DELETE_BY_USER_FROM = "user_from = '%s'";

    public static void deleteData(Map<String, DataHelper> map) throws Exception {
        for (DataHelper data : map.values()) {
            if (data.getMtSymbolSessions() != null) {
                data.getMtSymbolSessions()
                        .forEach(session -> deleteObjectFromDb(
                                MT_SYMBOL_SESSION_TABLE_NAME,
                                String.format(
                                        "symbol = '%s' and source_id_st = %s",
                                        session.getSymbol(), session.getSourceIdSt())));
            }
            if (data.crmTbUserObject != null) {
                deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format(DELETE_BY_USER_ID, data.crmTbUserObject.userId));
            }
            if (data.dictAccountToUcidObject != null) {
                deleteObjectFromDb(
                        DICT_ACCOUNT_TO_UCID, String.format(DELETE_BY_UCID, data.dictAccountToUcidObject.ucid));
            }
            if (data.dictActiveTradingDaysByUcidObject != null) {
                data.dictActiveTradingDaysByUcidObject.forEach(tradingDays -> deleteObjectFromDb(
                        DICT_ACTIVE_TRADE_DAYS_BY_UCID, String.format(DELETE_BY_UCID, tradingDays.ucid)));
            }
            if (data.connections != null) {
                data.connections.forEach(connection -> deleteObjectFromDb(
                        CONNECTIONS_TABLE_NAME, String.format(DELETE_BY_USER_FROM, connection.userFrom)));
            }
            if (data.lnSessionParsedObjectRegistration != null) {
                deleteObjectFromDb(
                        LEXIS_NEXIS_TABLE_NAME,
                        String.format(DELETE_BY_USER_ID, data.lnSessionParsedObjectRegistration.getUserId()));
            }
            if (data.lnSessionParsedObjectLogin != null) {
                deleteObjectFromDb(
                        LEXIS_NEXIS_TABLE_NAME,
                        String.format(DELETE_BY_USER_ID, data.lnSessionParsedObjectLogin.getUserId()));
            }
            if (data.callbacksObjects != null) {
                data.callbacksObjects.forEach(callbacksObject -> deleteObjectFromDb(
                        CALLBACKS_TABLE_NAME, String.format(DELETE_BY_UCID, callbacksObject.getUcid())));
            }
            if (data.lnSessionParsedObject != null) {
                deleteObjectFromDb(
                        LEXIS_NEXIS_TABLE_NAME,
                        String.format(DELETE_BY_USER_ID, data.lnSessionParsedObject.getUserId()));
            }
            if (data.clientFraudTypes != null) {
                data.clientFraudTypes.forEach(fraud -> deleteObjectFromDb(
                        BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format(DELETE_BY_UCID, fraud.getUcid())));
            }
            if (data.mtTbCreditsObjects != null) {
                data.mtTbCreditsObjects.forEach(credit ->
                        deleteObjectFromDb(MT_CREDITS_TABLE_NAME, String.format(DELETE_BY_UCID, credit.ucid)));
            }
            if (data.crmTbWithdrawalObjects != null) {
                data.crmTbWithdrawalObjects.forEach(withdrawal -> deleteObjectFromDb(
                        CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format(DELETE_BY_UCID, withdrawal.getUcid())));
            }
            if (data.crmTbWithdrawalTypeObjects != null)
                try {
                    data.crmTbWithdrawalTypeObjects.forEach(withdrawalType -> deleteObjectFromDb(
                            CLICKHOUSE_CRM_TB_WITHDRAWAL, String.format(DELETE_BY_ID, withdrawalType.getId())));
                } catch (Exception e) {
                    writeLog("Exception in deleteData: " + e.getMessage());
                }
            if (data.crmTbDepositObjects != null) {
                data.crmTbDepositObjects.forEach(deposit ->
                        deleteObjectFromDb(CRM_DEPOSIT_TABLE_NAME, String.format(DELETE_BY_UCID, deposit.getUcid())));
            }
            if (data.crmTbDepositTypeObjects != null) {
                data.crmTbDepositTypeObjects.forEach(depositType -> deleteObjectFromDb(
                        CRM_DEPOSIT_TYPE_TABLE_NAME, String.format(DELETE_BY_ID, depositType.getId())));
            }
            if (data.crmTbDepositChannelObjects != null) {
                data.crmTbDepositChannelObjects.forEach(depositChannel -> deleteObjectFromDb(
                        CRM_DEPOSIT_CHANNEL_TABLE_NAME, String.format(DELETE_BY_ID, depositChannel.getId())));
                data.crmTbDepositObjects.forEach(deposit ->
                        deleteObjectFromDb(CRM_DEPOSIT_TABLE_NAME, String.format(DELETE_BY_UCID, deposit.getUcid())));
            }
            if (data.crmTbBonusObjects != null) {
                data.crmTbBonusObjects.forEach(
                        bonus -> deleteObjectFromDb(CRM_BONUS_TABLE_NAME, String.format(DELETE_BY_UCID, bonus.ucid)));
            }
            if (data.mtBalanceOrdersObjects != null) {
                data.mtBalanceOrdersObjects.forEach(bonus ->
                        deleteObjectFromDb(MT_BALANCE_ORDERS_TABLE_NAME, String.format(DELETE_BY_UCID, bonus.ucid)));
            }
            if (data.mt5DealsCoercedObjects != null) {
                data.mt5DealsCoercedObjects.forEach(deal -> deleteObjectFromDb(
                        MT5_DEALS_COERCED_TABLE_NAME,
                        String.format("server_id = %s and account = %s", deal.getServerId(), deal.getAccount())));
            }
            if (data.mt5DealsCoercedDdObjects != null) {
                try {
                    data.mt5DealsCoercedDdObjects.forEach(deal -> deleteObjectFromDb(
                            MT5_DEALS_COERCED_DD_TABLE_NAME,
                            String.format("server_id = %s and account = %s", deal.getServerId(), deal.getAccount())));
                } catch (Exception e) {
                    writeLog("Exception in deleteData: " + e.getMessage());
                }
            }
            if (data.mt5DealsCoercedDdObjects != null) {
                data.mt5DealsCoercedDdObjects.forEach(deal -> deleteObjectFromDb(
                        MT5_DEALS_COERCED_DD_TABLE_NAME,
                        String.format("server_id = %s and account = %s", deal.getServerId(), deal.getAccount())));
            }
            if (data.mtMt5PositionsObjects != null) {
                data.mtMt5PositionsObjects.forEach(position -> deleteObjectFromDb(
                        MT5_POSITIONS_TABLE_NAME,
                        String.format(
                                "server_id = %s and account = %s", position.getServerId(), position.getAccount())));
            }
            if (data.mirrorLoginObjects != null) {
                data.mirrorLoginObjects.forEach(mirrorLoginObject -> deleteObjectFromDb(
                        MIRROR_LOGIN_TABLE_NAME, String.format("login_1 = %s", mirrorLoginObject.login_1)));
            }
            if (data.mirrorUcidObjects != null) {
                data.mirrorUcidObjects.forEach(mirrorUcidObject -> deleteObjectFromDb(
                        MIRROR_UCID_TABLE_NAME, String.format("ucid_1 = '%s'", mirrorUcidObject.ucid_1)));
            }
            if (data.sessionIdTableEntries != null) {
                data.sessionIdTableEntries.forEach(sessionIdTableEntry -> deleteObjectFromDb(
                        SESSION_ID_TABLE_NAME, String.format("session_id = '%s'", sessionIdTableEntry.sessionId)));
            }
            if (data.emailTableEntries != null) {
                data.emailTableEntries.forEach(emailTableEntry ->
                        deleteObjectFromDb(EMAIL_TABLE_NAME, String.format("email = '%s'", emailTableEntry.email)));
            }
            if (data.ipTableEntries != null) {
                data.ipTableEntries.forEach(
                        ipTableEntry -> deleteObjectFromDb(IP_TABLE_NAME, String.format("ip = '%s'", ipTableEntry.ip)));
            }
            if (data.phoneTableEntries != null) {
                data.phoneTableEntries.forEach(phoneTableEntry -> deleteObjectFromDb(
                        PHONE_TABLE_NAME, String.format("phone_num = '%s'", phoneTableEntry.phoneNum)));
            }
            if (data.deviceIdTableEntries != null) {
                data.deviceIdTableEntries.forEach(deviceIdTableEntry -> deleteObjectFromDb(
                        DEVICE_ID_TABLE_NAME, String.format(DELETE_BY_DEVICE_ID, deviceIdTableEntry.deviceId)));
            }
            if (data.aggrCreditEquityRate != null) {
                deleteObjectFromDb(
                        AGGR_CREDIT_EQUITY_RATE,
                        String.format("trading_account = %s", data.clientHelper.getTradingAccount()));
            }
            if (data.aggrCreditEquityRate != null) {
                data.loyaltyObjects.forEach(loyaltyObjects -> deleteObjectFromDb(
                        CRM_TB_LOYALTY_REDEMPTION, String.format(DELETE_BY_UCID, loyaltyObjects.ucid)));
            }
            if (data.AppTbFinindexData != null) {
                data.AppTbFinindexData.forEach(AppTbFinindexData -> deleteObjectFromDb(
                        APP_TB_FININDEX_DATA, String.format(DELETE_BY_ID, AppTbFinindexData.getId())));
            }
            if (data.s3FactIbSalesCommissionsObject != null) {
                data.s3FactIbSalesCommissionsObject.forEach(salesComm -> deleteObjectFromDb(
                        S3_FACT_IB_SALES_COMMISSIONS, String.format(DELETE_BY_UCID, salesComm.getUcid())));
            }
            if (data.S3FactLoginMetricsObjects != null) {
                data.S3FactLoginMetricsObjects.forEach(mertic -> deleteObjectFromDb(
                        S3_FACT_LOGIN_METRICS_TABLE_NAME, String.format(DELETE_BY_UCID, mertic.getUcid())));
            }
            if (data.ucidMirrorScore != null) {
                deleteObjectFromDb(
                        DATA_SCIENCE_UCID_MIRROR_SCORE_TABLE_NAME,
                        String.format(DELETE_BY_UCID, data.clientHelper.getUcid()));
            }
            if (data.boAlertsObjects != null) {
                data.boAlertsObjects.forEach(alert -> deleteObjectFromDb(
                        CLICKHOUSE_BO_ALERTS_TABLE_NAME, String.format(DELETE_BY_ALERT_ID, alert.getAlertId())));
            }
            if (data.clientCards != null) {
                deleteObjectFromDb(CLIENT_CARDS_TABLE_NAME, String.format(DELETE_BY_UCID, data.clientHelper.getUcid()));
            }
            if (data.ozTradesTableObjects != null) {
                data.ozTradesTableObjects.forEach(ozTrade -> deleteObjectFromDb(
                        CLICKHOUSE_OZ_TRADES_TABLE_NAME, String.format(DELETE_BY_UCID, ozTrade.getUcid())));
            }
            if (data.MtMt4TradesObjects != null) {
                data.MtMt4TradesObjects.forEach(trade ->
                        deleteObjectFromDb(MT4_TRADES_TABLE_NAME, String.format(DELETE_BY_UCID, trade.getUcid())));
            }
            if (data.ucidGeneralScore != null) {
                deleteObjectFromDb(
                        DATA_SCIENCE_UCID_GENERAL_SCORE_TABLE_NAME,
                        String.format(DELETE_BY_UCID, data.ucidGeneralScore.getUcid()));
            }
            if (data.ucidGeneralScores != null) {
                data.ucidGeneralScores.forEach(score -> deleteObjectFromDb(
                        DATA_SCIENCE_UCID_GENERAL_SCORE_TABLE_NAME, String.format(DELETE_BY_UCID, score.getUcid())));
            }
            cleanUserRestrictionGeneral(data.clientHelper.getUcid());
            closeAlert(data.clientHelper.getUcid());
            if ((data.connectedUsers != null) && (!data.connectedUsers.isEmpty())) {
                int size = data.connectedUsers.size();
                var sb = new StringBuilder();
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
                deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid in %s", sb));
            }
        }
        stopSshTunnel();
    }
}
