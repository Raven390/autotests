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
        deleteObjectsFromDb(CLICKHOUSE, NAME_BIRTH_TABLE_NAME, "name_dateofbirth", List.of(Arrays.toString(values)));
    }

    @Step("Clean phone table by client")
    public static void cleanPhoneTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, PHONE_TABLE_NAME, "phone_num", List.of(Arrays.toString(values)));
    }

    @Step("Clean ip table by client")
    public static void cleanIpTableByClient(String... values) throws Exception {
        deleteObjectsFromDb(CLICKHOUSE, IP_TABLE_NAME, "ip", List.of(Arrays.toString(values)));
    }
}
