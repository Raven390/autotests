package helpers.data;

import static helpers.data.rules.DepositTypeInserter.insertDepositTypeData;
import static helpers.data.rules.WithdrawalTypeInserter.insertWithrawalTypeData;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.writeLog;

import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import helpers.database.DbName;
import java.util.Map;

public class DataSetupHelper {

    public static void setupData(DataHelper data) {
        setupData(Map.of("data", data));
    }

    public static void setupData(Map<String, DataHelper> map) {
        startSshTunnel();
        for (DataHelper data : map.values()) {
            writeLog("WE ARE IN SETUP");
            if (data.connections != null && (!data.connections.isEmpty()))
                try {
                    for (ConnectionTableEntry i : data.connections) {
                        i.datetime = getCurrentTimestampDbFormat();
                        writeLog("WE ARE INSERTING connections");

                        executeQueryToDb(
                                DbName.CLICKHOUSE,
                                " INSERT INTO " + CONNECTIONS_TABLE_NAME
                                        + " (user_from, user_to, degree_connection, connection_score, connection_info, `datetime`, ver, status) VALUES('"
                                        + i.userFrom + "','" + i.userTo + "','" + i.degreeConnection + "','"
                                        + i.connectionScore + "','" + i.connectionInfo + "', NOW(), '1','new');");
                    }
                    waitForConnectionSearchToUpdate(data.connections.getFirst().userFrom);
                } catch (Exception e) {
                    writeLog("Error while inserting connections into table: " + e.getMessage());
                }
            if (data.crmTbUserObject != null) {
                insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            }
            if (data.dictAccountToUcidObject != null) {
                insertObjectToDb(DICT_ACCOUNT_TO_UCID, data.dictAccountToUcidObject);
            }
            if (data.dictActiveTradingDaysByUcidObject != null) {
                data.dictActiveTradingDaysByUcidObject.forEach(
                        tradingDays -> insertObjectToDb(DICT_ACTIVE_TRADE_DAYS_BY_UCID, tradingDays));
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
            if (data.dictIsTestObject != null) {
                deleteEntryFromDb(DICT_IS_TEST, "account =" + data.dictIsTestObject.account);
                insertObjectToDb(DICT_IS_TEST, data.dictIsTestObject);
            }
            if (data.clientFraudTypes != null) {
                data.clientFraudTypes.forEach(fraud -> insertObjectToDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, fraud));
            }
            if (data.clientCards != null) {
                data.clientCards.forEach(card -> insertObjectToDb(CLIENT_CARDS_TABLE_NAME, card));
            }
            if (data.crmTbAccountObject != null) {
                insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, data.crmTbAccountObject);
            }
            if (data.crmTbAccountForMtObject != null) {
                insertObjectToDb(CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, data.crmTbAccountForMtObject);
            }
            if (data.MtMt4TradesObjects != null) {
                insertObjectsToDb(MT4_TRADES_TABLE_NAME, data.MtMt4TradesObjects);
            }
            if (data.crmTbAccountObjectConnections != null) {
                data.crmTbAccountObjectConnections.forEach(
                        credit -> insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, credit));
            }
            if (data.mtTbCreditsObjects != null) {
                data.mtTbCreditsObjects.forEach(credit -> insertObjectToDb(MT_CREDITS_TABLE_NAME, credit));
            }
            if (data.sessionIdTableEntries != null) {
                data.sessionIdTableEntries.forEach(
                        sessionIdTableEntry -> insertObjectToDb(SESSION_ID_TABLE_NAME, sessionIdTableEntry));
            }
            if (data.callbacksObjects != null) {
                data.callbacksObjects.forEach(
                        callbacksObject -> insertObjectToDb(CALLBACKS_TABLE_NAME, callbacksObject));
            }
            if (data.emailTableEntries != null) {
                data.emailTableEntries.forEach(emailTableEntry -> insertObjectToDb(EMAIL_TABLE_NAME, emailTableEntry));
            }
            if (data.segmentObjects != null) {
                data.segmentObjects.forEach(s -> insertObjectToDb(SEGMENTATION_TABLE_NAME, s));
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
                insertWithrawalTypeData();
                data.crmTbWithdrawalObjects.forEach(
                        withdrawal -> insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, withdrawal));
            }
            if (data.costPaymentFees != null) {
                data.costPaymentFees.forEach(cf -> {
                    deleteObjectFromDb(
                            COST_PAYMENT_FEE_TABLE_NAME,
                            String.format("category = '%s' and country = '%s'", cf.getCategory(), cf.getCountry()));
                    insertObjectToDb(COST_PAYMENT_FEE_TABLE_NAME, cf);
                });
            }

            if (data.crmTbWithdrawalTypeObjects != null) {
                data.crmTbWithdrawalTypeObjects.forEach(
                        withdrawalType -> insertObjectToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL_TYPE, withdrawalType));
            }
            if (data.crmTbDepositObjects != null) {
                insertDepositTypeData();
                data.crmTbDepositObjects.forEach(deposit -> insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit));
            }
            if (data.crmTbDepositTypeObjects != null) {
                data.crmTbDepositTypeObjects.forEach(type -> insertObjectToDb(CRM_DEPOSIT_TYPE_TABLE_NAME, type));
            }
            if (data.crmTbDepositChannelObjects != null) {
                data.crmTbDepositChannelObjects.forEach(
                        channel -> insertObjectToDb(CRM_DEPOSIT_CHANNEL_TABLE_NAME, channel));
            }
            if (data.crmTbBonusObjects != null) {
                data.crmTbBonusObjects.forEach(bonus -> insertObjectToDb(CRM_BONUS_TABLE_NAME, bonus));
            }
            if (data.mt5DealsCoercedObjects != null && !data.mt5DealsCoercedObjects.isEmpty()) {
                insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, data.mt5DealsCoercedObjects);
            }
            if (data.S3FactLoginMetricsObjects != null && !data.S3FactLoginMetricsObjects.isEmpty()) {
                insertObjectsToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, data.S3FactLoginMetricsObjects);
            }
            if (data.mtMt5PositionsObjects != null) {
                data.mtMt5PositionsObjects.forEach(position -> insertObjectToDb(MT5_POSITIONS_TABLE_NAME, position));
            }
            if (data.mtBalanceOrdersObjects != null) {
                data.mtBalanceOrdersObjects.forEach(deal -> insertObjectToDb(MT_BALANCE_ORDERS_TABLE_NAME, deal));
            }
            if (data.documentTableEntries != null) {
                data.documentTableEntries.forEach(document -> insertObjectToDb(DOCUMENT_TABLE_NAME, document));
            }
            if (data.payoutTableEntries != null) {
                data.payoutTableEntries.forEach(document -> insertObjectToDb(PAYOUT_TABLE_NAME, document));
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
                data.mirrorUcidObjects.forEach(
                        mirrorUcidObject -> insertObjectToDb(MIRROR_UCID_TABLE_NAME, mirrorUcidObject));
            }
            if (data.loyaltyObjects != null) {
                data.loyaltyObjects.forEach(
                        loyaltyObjects -> insertObjectToDb(CRM_TB_LOYALTY_REDEMPTION, loyaltyObjects));
            }
            if (data.s3FactIbSalesCommissionsObject != null) {
                data.s3FactIbSalesCommissionsObject.forEach(
                        salesComm -> insertObjectToDb(S3_FACT_IB_SALES_COMMISSIONS, salesComm));
            }
            if (data.ucidMirrorScore != null) {
                insertObjectToDb(DATA_SCIENCE_UCID_MIRROR_SCORE_PYTHON, data.ucidMirrorScore);
            }
            if (data.boAlertsObjects != null) {
                data.boAlertsObjects.forEach(alerts -> insertObjectToDb(CLICKHOUSE_BO_ALERTS_TABLE_NAME, alerts));
            }
            if (data.ozTradesTableObjects != null) {
                data.ozTradesTableObjects.forEach(
                        ozTrade -> insertObjectToDb(CLICKHOUSE_OZ_TRADES_TABLE_NAME, ozTrade));
            }
            if (data.ratesUsdCurrentObjects != null) {
                data.ratesUsdCurrentObjects.forEach(rate -> insertObjectToDb(RATES_USD_CURRENT, rate));
            }
            if (data.ucidGeneralScore != null) {
                insertObjectToDb(DATA_SCIENCE_UCID_GENERAL_SCORE_TABLE_NAME, data.ucidGeneralScore);
            }
            if (data.ucidGeneralScores != null) {
                data.ucidGeneralScores.forEach(
                        score -> insertObjectToDb(DATA_SCIENCE_UCID_GENERAL_SCORE_TABLE_NAME, score));
            }
            if (data.AppTbFinindexData != null) {
                insertObjectsToDb(APP_TB_FININDEX_DATA, data.AppTbFinindexData);
            }
        }
    }
}
