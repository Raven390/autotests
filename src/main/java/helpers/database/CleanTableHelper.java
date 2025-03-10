package helpers.database;

import io.qameta.allure.Step;

import java.util.Arrays;
import java.util.List;

import static helpers.database.DbHelper.*;
import static helpers.database.DbName.CLICKHOUSE;
import static utils.Constants.*;

public class CleanTableHelper {

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
    public static void cleanWithdrawalsTableByUcid(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, CRM_WITHDRAWAL_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
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
        deleteObjectsFromDb(CLICKHOUSE, MT5_DEALS_COERCED_TOXICITY_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }

    @Step("Clean bo fraud types table by ucid")
    public static void cleanBoFraudTypesTableByUcid(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, BO_CLIENT_FRAUD_TYPES_TABLE_NAME, "ucid", List.of(Arrays.toString(values)));
    }
}
