package helpers.database;

import static helpers.database.DbHelper.*;
import static helpers.database.DbName.CLICKHOUSE;
import static helpers.database.DbName.POSTGRES;
import static utils.Constants.*;

import business_objects.db.mitigation_service_db.ClientGeneralRestriction;
import business_objects.db.mitigation_service_db.ClientTradingRestriction;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CleanTableHelper {

    private static final String WHERE_STATEMENT_BY_UCID = "ucid = '%s'";
    private static final String WHERE_STATEMENT_BY_RESTRICTION_ID = "client_restriction_id %s";

    @Step("Clean connections table by client")
    public static void cleanConnectionsTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, CONNECTIONS_TABLE_NAME, "user_from", List.of(Arrays.toString(values)));
    }

    @Step("Clean crm user table by client")
    public static void cleanCrmUserTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, CRM_USER_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean email table by client")
    public static void cleanEmailTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, EMAIL_TABLE_NAME, "email", List.of(Arrays.toString(values)));
    }

    @Step("Clean device id table by client")
    public static void cleanDeviceIdTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, DEVICE_ID_TABLE_NAME, "device_id", List.of(Arrays.toString(values)));
    }

    @Step("Clean digital id table by client")
    public static void cleanDigitalIdTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, DIGITAL_ID_TABLE_NAME, "digital_id", List.of(Arrays.toString(values)));
    }

    @Step("Clean session id table by client")
    public static void cleanSessionIdTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, SESSION_ID_TABLE_NAME, "session_id", List.of(Arrays.toString(values)));
    }

    @Step("Clean webSession table by client")
    public static void cleanWebSessionIdTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, WEB_SESSION_TABLE_NAME, "web_session_id", List.of(Arrays.toString(values)));
    }

    @Step("Clean nameBirth id table by client")
    public static void cleanNameTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, NAME_BIRTH_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean phone table by client")
    public static void cleanPhoneTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, PHONE_TABLE_NAME, "phone_num", List.of(Arrays.toString(values)));
    }

    @Step("Clean ip table by client")
    public static void cleanIpTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, IP_TABLE_NAME, "ip", List.of(Arrays.toString(values)));
    }

    @Step("Clean fraud type table by client")
    public static void cleanFraudTypeTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, BO_CLIENT_FRAUD_TYPES_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean mt balance orders table by client")
    public static void cleanMtBalanceOrdersTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, MT_BALANCE_ORDERS_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean bonus table by client")
    public static void cleanBonusesTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, CRM_BONUS_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean mt credits table by ucid")
    public static void cleanMtCreditsTableByUcid(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, MT_CREDITS_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean deposits table by ucid")
    public static void cleanDepositsTableByUcid(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, CRM_DEPOSIT_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean mt5 coerced table by comment")
    public static void cleanMt5CoercedTableByComment(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, MT5_DEALS_COERCED_TABLE_NAME, "comment", List.of(Arrays.toString(values)));
    }

    @Step("Clean mt5 coerced table by account")
    public static void cleanMt5CoercedTableByAccount(Integer... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, MT5_DEALS_COERCED_TABLE_NAME, "account", List.of(Arrays.toString(values)));
    }

    @Step("Clean mt5 coerced table by ucid")
    public static void cleanMt5CoercedTableByUcid(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, MT5_DEALS_COERCED_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean withdrawals table by account")
    public static void cleanCrmTbWithdrawalTableByUcid(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, CLICKHOUSE_CRM_TB_WITHDRAWAL, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean lexisNexis table by ucid")
    public static void cleanLexisNexisTableByUcid(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, LEXIS_NEXIS_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean mt4 trades coerced table by ucid")
    public static void cleanMt4CoercedTableByUcid(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, MT4_TRADES_COERCED_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean mt5 coerced toxicity table by ucid")
    public static void cleanMt5CoercedToxicityTableByUcid(String... values) throws Exception {
        deleteObjectsFromDb(
                CLICKHOUSE, MT5_DEALS_COERCED_TOXICITY_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean bo fraud types table by ucid")
    public static void cleanBoFraudTypesTableByUcid(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, BO_CLIENT_FRAUD_TYPES_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    // Rule engine db

    @Step("Clean rule table by rule id")
    public static void cleanRuleTableByRuleId(String... values) throws SQLException {
        deleteObjectsFromDb(POSTGRES, RULE_ENGINE_RULE_TABLE, "id", List.of(Arrays.toString(values)));
    }

    @Step("Clean rule_deployment table by uuid")
    public static void cleanRuleDeploymentTableByUuId(String... values) throws SQLException {
        deleteObjectsFromDb(
                POSTGRES, RULE_ENGINE_RULE_DEPLOYMENT_TABLE, "process_id", List.of(Arrays.toString(values)));
    }

    // Mitigation db

    @Step("Clean users general restriction history for ucid '{ucid}'")
    public static void cleanUserRestrictionGeneral(String ucid) throws Exception {
        List<ClientGeneralRestriction> restrictionList = getObjectsFromDB(
                DbName.POSTGRES,
                MITIGATION_CLIENT_GENERAL_RESTRICTION,
                String.format(WHERE_STATEMENT_BY_UCID, ucid),
                ClientGeneralRestriction.class);
        if (!restrictionList.isEmpty()) {
            List<String> restrictionIdList = restrictionList.stream()
                    .map(restriction -> restriction.getId().toString())
                    .toList();
            String inClause = "IN ("
                    + restrictionIdList.stream().map(id -> "'" + id + "'").collect(Collectors.joining(", ")) + ")";
            deleteObjectFromDb(
                    DbName.POSTGRES,
                    MITIGATION_CLIENT_GENERAL_RESTRICTION_ACTION,
                    String.format(WHERE_STATEMENT_BY_RESTRICTION_ID, inClause));
            deleteObjectFromDb(
                    DbName.POSTGRES,
                    MITIGATION_KAFKA_REQUEST_GENERAL,
                    String.format(WHERE_STATEMENT_BY_RESTRICTION_ID, inClause));
            deleteObjectFromDb(
                    DbName.POSTGRES,
                    MITIGATION_KAFKA_RESPONSE_GENERAL,
                    String.format(WHERE_STATEMENT_BY_RESTRICTION_ID, inClause));
            deleteObjectFromDb(DbName.POSTGRES, MITIGATION_CLIENT_GENERAL_RESTRICTION, "id " + inClause);
        }
    }

    @Step("Clean users trading restriction history for ucid '{ucid}'")
    public static void cleanUserRestrictionTrading(String ucid) throws Exception {
        List<ClientTradingRestriction> restrictionList = getObjectsFromDB(
                DbName.POSTGRES,
                MITIGATION_CLIENT_TRADING_RESTRICTION,
                String.format(WHERE_STATEMENT_BY_UCID, ucid),
                ClientTradingRestriction.class);
        if (!restrictionList.isEmpty()) {
            List<String> restrictionIdList = restrictionList.stream()
                    .map(restriction -> restriction.getId().toString())
                    .toList();
            String inClause = "IN ("
                    + restrictionIdList.stream().map(id -> "'" + id + "'").collect(Collectors.joining(", ")) + ")";
            deleteObjectFromDb(
                    DbName.POSTGRES,
                    MITIGATION_CLIENT_TRADING_RESTRICTION_ACTION,
                    String.format(WHERE_STATEMENT_BY_RESTRICTION_ID, inClause));
            deleteObjectFromDb(
                    DbName.POSTGRES,
                    MITIGATION_KAFKA_REQUEST_TRADING,
                    String.format(WHERE_STATEMENT_BY_RESTRICTION_ID, inClause));
            deleteObjectFromDb(
                    DbName.POSTGRES,
                    MITIGATION_KAFKA_RESPONSE_TRADING,
                    String.format(WHERE_STATEMENT_BY_RESTRICTION_ID, inClause));
            deleteObjectFromDb(
                    DbName.POSTGRES,
                    MITIGATION_CLIENT_TRADING_RESTRICTION_STATUS_BY_SITE,
                    String.format(WHERE_STATEMENT_BY_RESTRICTION_ID, inClause));
            deleteObjectFromDb(DbName.POSTGRES, MITIGATION_CLIENT_TRADING_RESTRICTION, "id " + inClause);
        }
    }

    @Step("Clean client's trading env restriction for ucid '{ucid}'")
    public static void cleanUserRestrictionTradingEnv(String ucid) throws Exception {
        var sqlClientTradingEnvRestrictionDelete =
                """
                with deleted_client_trading_env_restriction_ids as (
                    delete from postgres.mi.client_trading_environment_restriction
                    where ucid = '%s'
                    returning id
                ),
                deleted_actions as (
                    delete from postgres.mi.client_trading_environment_restriction_action
                    where client_restriction_id in (select id from deleted_client_trading_env_restriction_ids)
                ),
                deleted_kafka_requests as (
                    delete from postgres.mi.client_trading_environment_restriction_kafka_request
                    where client_restriction_id in (select id from deleted_client_trading_env_restriction_ids)
                )
                delete from postgres.mi.client_trading_environment_restriction_kafka_response
                where client_restriction_id in (select id from deleted_client_trading_env_restriction_ids)
                """;
        var sqlQueueEventDelete =
                """
                delete from postgres.mi.client_trading_environment_restriction_queue
                where ucid = '%s'
                """;
        DbHelper.executeQueryToDb(POSTGRES, String.format(sqlClientTradingEnvRestrictionDelete, ucid));
        DbHelper.executeQueryToDb(POSTGRES, String.format(sqlQueueEventDelete, ucid));
    }

    // Audit db

    @Step("Clean users audit history")
    public static void cleanUserAudit(String ucid) throws Exception {
        deleteObjectFromDb(DbName.POSTGRES, AUDIT_EVENT_TABLE, String.format(WHERE_STATEMENT_BY_UCID, ucid));
    }

    // Data science db

    public static void cleanUserMirrorScoreDataDb(String ucid) throws Exception {
        Allure.step("delete user's mirror score data from DB");

        deleteObjectFromDb(DATA_SCIENCE_FEATURE_STORE_SERVICE_TABLE_NAME, "ucid = '" + ucid + "'");
        // deleteObjectFromDb(DATA_SCIENCE_FEATURE_STORE_SERVICE_V2_TABLE_NAME, "ucid = '" + ucid + "'");
    }

    // Payment gate db
    @Step("Clean payment data (details then events) by ucid '{ucid}' and client_id '{clientId}'")
    public static void cleanPaymentGateData(String ucid, Integer clientId) throws Exception {
        // Delete child records first to avoid FK violations
        deleteObjectFromDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE,
                "payment_id IN (SELECT payment_id FROM " + PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE + " WHERE ucid = '"
                        + ucid + "')");
        deleteObjectFromDb(DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, "client_id = '" + clientId + "'");
        deleteObjectFromDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, String.format(WHERE_STATEMENT_BY_UCID, ucid));
    }

    @Step("Clean payment data (details then events) by ucid '{ucid}' and client_id '{clientId}'")
    public static void cleanPaymentGateData(String ucid, Integer clientId, String paymentId) throws Exception {
        // Delete child records first to avoid FK violations
        deleteObjectFromDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_TMP_RULE_DECISIONS_TABLE, String.format("payment_id='%s'", paymentId));
        deleteObjectFromDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_REJECTION_ATTRIBUTES_TABLE,
                String.format("payment_id='%s'", paymentId));
        deleteObjectFromDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DECISIONS_TABLE, String.format("payment_id='%s'", paymentId));
        deleteObjectFromDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_PAYMENT_RULE_EXECUTIONS_TABLE,
                String.format("payment_id='%s'", paymentId));
        deleteObjectFromDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_DETAILS_TABLE, String.format("client_id = '%s'", clientId));
        deleteObjectFromDb(
                DbName.POSTGRES,
                PAYMENT_GATEWAY_TMP_RULE_DECISIONS_SENT_TABLE,
                String.format("payment_id='%s'", paymentId));
        deleteObjectFromDb(
                DbName.POSTGRES, PAYMENT_GATEWAY_PAYMENT_EVENTS_TABLE, String.format(WHERE_STATEMENT_BY_UCID, ucid));
    }
}
