package helpers.data;

import static helpers.data.rules.DepositTypeInserter.insertDepositTypeData;
import static helpers.data.rules.WithdrawalTypeInserter.insertWithrawalTypeData;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteObjectFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.DbName.POSTGRES;
import static utils.Constants.*;
import static utils.Utils.*;
import static utils.Utils.writeLog;

import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import helpers.database.DbName;
import java.util.List;
import java.util.Map;

public class DataSetupHelper {

    public static void setupData(DataHelper data) {
        setupData(Map.of("data", data));
    }

    public static void setupData(Map<String, DataHelper> map) {
        startSshTunnel();

        for (DataHelper data : map.values()) {
            writeLog("WE ARE IN SETUP");
            insertConnections(data.connections);

            if (data.dictIsTestObject != null) {
                deleteObjectFromDb(DICT_IS_TEST, "account =" + data.dictIsTestObject.account);
                insertObjectToDb(DICT_IS_TEST, data.dictIsTestObject);
            }

            if (data.crmTbWithdrawalObjects != null) {
                insertWithrawalTypeData();
                insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL, data.crmTbWithdrawalObjects);
            }
            if (data.costPaymentFees != null) {
                data.costPaymentFees.forEach(cf -> {
                    deleteObjectFromDb(
                            COST_PAYMENT_FEE_TABLE_NAME,
                            String.format("category = '%s' and country = '%s'", cf.getCategory(), cf.getCountry()));
                    insertObjectToDb(COST_PAYMENT_FEE_TABLE_NAME, cf);
                });
            }
            insertObjectsToDb(CLICKHOUSE_CRM_TB_WITHDRAWAL_TYPE, data.crmTbWithdrawalTypeObjects);
            if (data.crmTbDepositObjects != null) {
                insertDepositTypeData();
                insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, data.crmTbDepositObjects);
            }

            insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            insertObjectToDb(DICT_ACCOUNT_TO_UCID, data.dictAccountToUcidObject);
            insertObjectsToDb(DICT_ACTIVE_TRADE_DAYS_BY_UCID, data.dictActiveTradingDaysByUcidObject);
            insertObjectsToDb(CRM_USER_TABLE_NAME, data.connectedUsers);
            insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObjectRegistration);
            insertObjectToDb(LEXIS_NEXIS_TABLE_NAME, data.lnSessionParsedObject);
            insertObjectsToDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, data.clientFraudTypes);
            insertObjectsToDb(CLIENT_CARDS_TABLE_NAME, data.clientCards);
            insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, data.crmTbAccountObject);
            insertObjectToDb(CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, data.crmTbAccountForMtObject);
            insertObjectsToDb(MT4_TRADES_TABLE_NAME, data.MtMt4TradesObjects);
            insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, data.crmTbAccountObjectConnections);
            insertObjectsToDb(MT_CREDITS_TABLE_NAME, data.mtTbCreditsObjects);
            insertObjectsToDb(SESSION_ID_TABLE_NAME, data.sessionIdTableEntries);
            insertObjectsToDb(CALLBACKS_TABLE_NAME, data.callbacksObjects);
            insertObjectsToDb(EMAIL_TABLE_NAME, data.emailTableEntries);
            insertObjectsToDb(SEGMENTATION_TABLE_NAME, data.segmentObjects);
            insertObjectsToDb(IP_TABLE_NAME, data.ipTableEntries);
            insertObjectsToDb(PHONE_TABLE_NAME, data.phoneTableEntries);
            insertObjectsToDb(DEVICE_ID_TABLE_NAME, data.deviceIdTableEntries);
            insertObjectsToDb(MT_SYMBOL_SESSION_TABLE_NAME, data.getMtSymbolSessions());
            insertObjectsToDb(CRM_DEPOSIT_TYPE_TABLE_NAME, data.crmTbDepositTypeObjects);
            insertObjectsToDb(CRM_DEPOSIT_CHANNEL_TABLE_NAME, data.crmTbDepositChannelObjects);
            insertObjectsToDb(CRM_BONUS_TABLE_NAME, data.crmTbBonusObjects);
            insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, data.mt5DealsCoercedObjects);
            insertObjectsToDb(MT5_DEALS_COERCED_DD_TABLE_NAME, data.getMt5DealsCoercedDdObjects());
            insertObjectsToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, data.S3FactLoginMetricsObjects);
            insertObjectsToDb(MT5_POSITIONS_TABLE_NAME, data.mtMt5PositionsObjects);
            insertObjectsToDb(MT_BALANCE_ORDERS_TABLE_NAME, data.mtBalanceOrdersObjects);
            insertObjectsToDb(DOCUMENT_TABLE_NAME, data.documentTableEntries);
            insertObjectsToDb(PAYOUT_TABLE_NAME, data.payoutTableEntries);
            insertObjectsToDb(MIRROR_LOGIN_TABLE_NAME, data.mirrorLoginObjects);
            insertObjectToDb(AGGR_CREDIT_EQUITY_RATE, data.aggrCreditEquityRate);
            insertObjectToDb(MIRROR_LOGIN_TABLE_NAME, data.aggrMirrorAccountsByTrades);
            insertObjectToDb(MT_ACCOUNT_TABLE_NAME, data.mtAccountObject);
            insertObjectsToDb(MIRROR_UCID_TABLE_NAME, data.mirrorUcidObjects);
            insertObjectsToDb(CRM_TB_LOYALTY_REDEMPTION, data.loyaltyObjects);
            insertObjectsToDb(S3_FACT_IB_SALES_COMMISSIONS, data.s3FactIbSalesCommissionsObject);
            insertObjectToDb(DATA_SCIENCE_UCID_MIRROR_SCORE_PYTHON, data.ucidMirrorScore);
            insertObjectsToDb(CLICKHOUSE_BO_ALERTS_TABLE_NAME, data.boAlertsObjects);
            insertObjectsToDb(CLICKHOUSE_OZ_TRADES_TABLE_NAME, data.ozTradesTableObjects);
            insertObjectsToDb(RATES_USD_CURRENT, data.ratesUsdCurrentObjects);
            insertObjectToDb(DATA_SCIENCE_UCID_GENERAL_SCORE_TABLE_NAME, data.ucidGeneralScore);
            insertObjectsToDb(DATA_SCIENCE_UCID_GENERAL_SCORE_TABLE_NAME, data.ucidGeneralScores);
            insertObjectsToDb(APP_TB_FININDEX_DATA, data.AppTbFinindexData);

            if (data.paymentEventsObjects != null) {
                data.paymentEventsObjects.forEach(
                        pEvent -> insertObjectToDb(POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, pEvent));
            }
            if (data.paymentDetailsObjects != null) {
                data.paymentDetailsObjects.forEach(
                        pDetail -> insertObjectToDb(POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, pDetail));
            }
            if (data.paymentRuleExecutionsObjects != null) {
                data.paymentRuleExecutionsObjects.forEach(pExecution ->
                        insertObjectToDb(POSTGRES, PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE, pExecution));
            }
        }
    }

    private static void insertConnections(List<ConnectionTableEntry> connections) {
        if (connections == null || connections.isEmpty()) return;

        try {
            for (ConnectionTableEntry i : connections) {
                i.datetime = getCurrentTimestampDbFormat();
                writeLog("WE ARE INSERTING connections");
                executeQueryToDb(
                        DbName.CLICKHOUSE,
                        "INSERT INTO " + CONNECTIONS_TABLE_NAME
                                + " (user_from, user_to, degree_connection, connection_score, connection_info, `datetime`, ver, status) VALUES('"
                                + i.userFrom + "','" + i.userTo + "','" + i.degreeConnection + "','"
                                + i.connectionScore + "','" + i.connectionInfo + "', NOW(), '1','new');");
            }
            waitForConnectionSearchToUpdate(connections.getFirst().userFrom);
        } catch (Exception e) {
            writeLog("Error while inserting connections into table: " + e.getMessage());
        }
    }
}
