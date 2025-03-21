package tests.connection_search_api_service_tests;

import business_objects.api.connection_search_api.ConnectionSearchResponseError;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.device_id_table.DeviceIdTableEntry;
import business_objects.db.clickhouse.digital_id_table.DigitalIdTableEntry;
import business_objects.db.clickhouse.email_table.EmailTableEntry;
import business_objects.db.clickhouse.ip_table.IpTableEntry;
import business_objects.db.clickhouse.name_birth.NameBirthTableEntry;
import business_objects.db.clickhouse.phone.PhoneTableEntry;
import business_objects.db.clickhouse.session_id.SessionIdTableEntry;
import business_objects.db.clickhouse.web_session.WebSessionTableEntry;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.connection_search_api.check_connected_ib.CheckConnectedIbRequest.getCheckConnectedIb;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.getConnectionTableEntry;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.device_id_table.DeviceIdTableEntryFactory.deviceIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.digital_id_table.DigitalIdTableEntryFactory.digitalIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.email_table.EmailTableEntryFactory.emailTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.email_table.EmailTableEntryFactory.getEmailTableEntryByClient;
import static business_objects.db.clickhouse.ip_table.IpTableEntryFactory.ipTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.name_birth.NameBirthTableEntryFactory.nameBirthTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.phone.PhoneTableEntryFactory.phoneTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.session_id.SessionIdTableEntryFactory.sessionIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.web_session.WebSessionTableEntryFactory.webSessionTableEntryForConnectionSearch;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.ClientFactory.getRandomVantageClientNoCpaIbRef;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.waitForConnectionSearchToUpdate;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CHECK_CONNECTED_IB)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
public class GetCheckConnectedIbTests extends TestBaseApi {

    public static final ClientHelper userFrom1 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo1 = getRandomVantageClientAllFields();
    public static final ClientHelper userFrom2 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo2 = getRandomVantageClientAllFields();
    public static final ClientHelper userFrom3 = getRandomVantageClientNoCpaIbRef();
    public static final ClientHelper userTo3 = getRandomVantageClientNoCpaIbRef();
    public static final ClientHelper userFrom4 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo4 = getRandomVantageClientAllFields();
    public static final ClientHelper userFrom5 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo5 = getRandomVantageClientAllFields();
    public static final ClientHelper userFrom6 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo6 = getRandomVantageClientAllFields();
    public static final ClientHelper userFrom7 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo71 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo72 = getRandomVantageClientAllFields();
    public static final ClientHelper userFrom8 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo81 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo82 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo83 = getRandomVantageClientAllFields();

    public static CrmTbUserObject user1;
    public static CrmTbUserObject user2;
    public static CrmTbUserObject user3;
    public static CrmTbUserObject user4;
    public static CrmTbUserObject user5;
    public static CrmTbUserObject user6;
    public static CrmTbUserObject user7;
    public static CrmTbUserObject user8;
    public static CrmTbUserObject user9;
    public static CrmTbUserObject user91;
    public static CrmTbUserObject user92;
    public static CrmTbUserObject user10;
    public static CrmTbUserObject user101;
    public static CrmTbUserObject user102;
    public static CrmTbUserObject user103;

    public static ConnectionTableEntry connectionTableEntry1 = getConnectionTableEntry(userFrom1, userTo1);
    public static ConnectionTableEntry connectionTableEntry2 = getConnectionTableEntry(userFrom2, userTo2);
    public static ConnectionTableEntry connectionTableEntry3 = getConnectionTableEntry(userFrom3, userTo3);
    public static ConnectionTableEntry connectionTableEntry4 = getConnectionTableEntry(userFrom7, userTo71);
    public static ConnectionTableEntry connectionTableEntry5 = getConnectionTableEntry(userTo71, userTo72);
    public static ConnectionTableEntry connectionTableEntry6 = getConnectionTableEntry(userFrom8, userTo81);
    public static ConnectionTableEntry connectionTableEntry7 = getConnectionTableEntry(userTo81, userTo82);
    public static ConnectionTableEntry connectionTableEntry8 = getConnectionTableEntry(userTo82, userTo83);

    public static final EmailTableEntry emailTableEntry11 = emailTableEntryForConnectionSearch(userFrom1);
    public static final EmailTableEntry emailTableEntry12 = emailTableEntryForConnectionSearch(userTo1, userFrom1.getEmail());
    public static final EmailTableEntry emailTableEntry41 = emailTableEntryForConnectionSearch(userFrom4);
    public static final EmailTableEntry emailTableEntry42 = emailTableEntryForConnectionSearch(userTo4, userFrom4.getEmail());
    public static final EmailTableEntry emailTableEntry51 = emailTableEntryForConnectionSearch(userFrom5);
    public static final EmailTableEntry emailTableEntry52 = emailTableEntryForConnectionSearch(userTo5, userFrom5.getEmail());
    public static final EmailTableEntry emailTableEntry61 = emailTableEntryForConnectionSearch(userFrom6);
    public static final EmailTableEntry emailTableEntry62 = emailTableEntryForConnectionSearch(userTo6, userFrom6.getEmail());
    public static final EmailTableEntry emailTableEntry71 = emailTableEntryForConnectionSearch(userFrom7);
    public static final EmailTableEntry emailTableEntry72 = emailTableEntryForConnectionSearch(userTo71, userFrom7.getEmail());
    public static final EmailTableEntry emailTableEntry73 = emailTableEntryForConnectionSearch(userTo72, userFrom7.getEmail());
    public static final EmailTableEntry emailTableEntry8 = emailTableEntryForConnectionSearch(userFrom8);
    public static final EmailTableEntry emailTableEntry81 = emailTableEntryForConnectionSearch(userTo81, userFrom8.getEmail());
    public static final EmailTableEntry emailTableEntry82 = emailTableEntryForConnectionSearch(userTo81, userFrom8.getEmail());
    public static final EmailTableEntry emailTableEntry83 = emailTableEntryForConnectionSearch(userTo82, userFrom8.getEmail());

    public static final DigitalIdTableEntry digitalTableEntry11 = digitalIdTableEntryForConnectionSearch(userFrom1);
    public static final DigitalIdTableEntry digitalTableEntry12 = digitalIdTableEntryForConnectionSearch(userTo1, userFrom1.getDigitalId());

    public static final DeviceIdTableEntry deviceTableEntry11 = deviceIdTableEntryForConnectionSearch(userFrom1);
    public static final DeviceIdTableEntry deviceTableEntry12 = deviceIdTableEntryForConnectionSearch(userTo1, userFrom1.getDeviceId());

    public static final SessionIdTableEntry sessionTableEntry11 = sessionIdTableEntryForConnectionSearch(userFrom1);
    public static final SessionIdTableEntry sessionTableEntry12 = sessionIdTableEntryForConnectionSearch(userTo1, userFrom1.getSessionId());

    public static NameBirthTableEntry nameTableEntry11 = nameBirthTableEntryForConnectionSearch(userFrom1);
    public static NameBirthTableEntry nameTableEntry12 = nameBirthTableEntryForConnectionSearch(userTo1, userFrom1.getFirstName(), userFrom1.getLastName(), userFrom1.getDateOfBirth());

    public static final WebSessionTableEntry webSessionTableEntry11 = webSessionTableEntryForConnectionSearch(userFrom1);
    public static final WebSessionTableEntry webSessionTableEntry12 = webSessionTableEntryForConnectionSearch(userTo1, userFrom1.getWebSessionId());

    public static final PhoneTableEntry phoneTableEntry11 = phoneTableEntryForConnectionSearch(userFrom1);
    public static final PhoneTableEntry phoneTableEntry12 = phoneTableEntryForConnectionSearch(userTo1, userFrom1.getPhoneNumber());

    public static final IpTableEntry ipTableEntry11 = ipTableEntryForConnectionSearch(userFrom1);
    public static final IpTableEntry ipTableEntry12 = ipTableEntryForConnectionSearch(userTo1, userFrom1.getIpAddress());

    public static final EmailTableEntry emailTableEntry21 = getEmailTableEntryByClient(userFrom2);
    public static final EmailTableEntry emailTableEntry22 = getEmailTableEntryByClient(userTo2);

    public static final EmailTableEntry emailTableEntry31 = getEmailTableEntryByClient(userFrom3);
    public static final EmailTableEntry emailTableEntry32 = getEmailTableEntryByClient(userTo3);

    @BeforeAll
    static void setupConnectionTableEntry() throws Exception {
        userTo1.setCpaId(userFrom1.getCpaId());
        userTo1.setIbId(userFrom1.getIbId());
        userTo1.setReferrerId(userFrom1.getReferrerId());
        user1 = generateUserByClient(userFrom1);
        user2 = generateUserByClient(userTo1);
        userTo4.setCpaId(userFrom4.getCpaId());
        user3 = generateUserByClient(userFrom4);
        user4 = generateUserByClient(userTo4);
        userTo5.setReferrerId(userFrom5.getReferrerId());
        user5 = generateUserByClient(userFrom5);
        user6 = generateUserByClient(userTo5);
        userTo6.setIbId(userFrom6.getIbId());
        user7 = generateUserByClient(userFrom6);
        user8 = generateUserByClient(userTo6);
        user9 = generateUserByClient(userFrom7);
        user91 = generateUserByClient(userTo71);
        userTo72.setCpaId(userFrom7.getCpaId());
        user92 = generateUserByClient(userTo72);
        user10 = generateUserByClient(userFrom8);
        user101 = generateUserByClient(userTo81);
        user102 = generateUserByClient(userTo82);
        userTo83.setCpaId(userFrom8.getCpaId());
        user103 = generateUserByClient(userTo83);
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntry1, connectionTableEntry2, connectionTableEntry3, connectionTableEntry4, connectionTableEntry5, connectionTableEntry6, connectionTableEntry7, connectionTableEntry8));
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user91, user92, user10, user101, user102, user103));
        insertObjectsToDb(EMAIL_TABLE_NAME, List.of(emailTableEntry11, emailTableEntry12, emailTableEntry21, emailTableEntry22, emailTableEntry31, emailTableEntry32, emailTableEntry41, emailTableEntry42, emailTableEntry51, emailTableEntry52, emailTableEntry61, emailTableEntry62, emailTableEntry71, emailTableEntry72, emailTableEntry73, emailTableEntry8, emailTableEntry81, emailTableEntry82, emailTableEntry83));
        insertObjectsToDb(DEVICE_ID_TABLE_NAME, List.of(deviceTableEntry11, deviceTableEntry12));
        insertObjectsToDb(DIGITAL_ID_TABLE_NAME, List.of(digitalTableEntry11, digitalTableEntry12));
        insertObjectsToDb(SESSION_ID_TABLE_NAME, List.of(sessionTableEntry11, sessionTableEntry12));
        insertObjectsToDb(WEB_SESSION_TABLE_NAME, List.of(webSessionTableEntry11, webSessionTableEntry12));
        insertObjectsToDb(PHONE_TABLE_NAME, List.of(phoneTableEntry11, phoneTableEntry12));
        insertObjectsToDb(IP_TABLE_NAME, List.of(ipTableEntry11, ipTableEntry12));
        insertObjectsToDb(NAME_BIRTH_TABLE_NAME, List.of(nameTableEntry11, nameTableEntry12));
        waitForConnectionSearchToUpdate(userFrom1);
    }

    @AfterAll
    static void deleteConnectionTableEntry() throws Exception {
        cleanConnectionsTableByClient(connectionTableEntry1.userFrom, connectionTableEntry2.userFrom, connectionTableEntry3.userFrom, connectionTableEntry4.userFrom, connectionTableEntry5.userFrom, connectionTableEntry6.userFrom, connectionTableEntry7.userFrom, connectionTableEntry8.userFrom);
        cleanEmailTableByClient(emailTableEntry11.email, emailTableEntry12.email, emailTableEntry21.email, emailTableEntry22.email, emailTableEntry31.email, emailTableEntry32.email);
        cleanDeviceIdTableByClient(deviceTableEntry11.deviceId, deviceTableEntry12.deviceId);
        cleanDigitalIdTableByClient(digitalTableEntry11.digitalId, digitalTableEntry12.digitalId);
        cleanSessionIdTableByClient(sessionTableEntry11.sessionId, sessionTableEntry12.sessionId);
        cleanWebSessionIdTableByClient(webSessionTableEntry11.webSessionId, webSessionTableEntry12.webSessionId);
        cleanPhoneTableByClient(phoneTableEntry11.phoneNum, phoneTableEntry12.phoneNum);
        cleanIpTableByClient(ipTableEntry11.ip, ipTableEntry12.ip);
        cleanNameTableByClient(nameTableEntry11.ucid, nameTableEntry12.ucid);
        cleanCrmUserTableByClient(user1.ucid, user2.ucid, user3.ucid, user4.ucid, user5.ucid, user6.ucid, user7.ucid, user8.ucid);
    }

    @Test
    @DisplayName("Connection search. Get check connected id, not enough parameters (400)")
    @AllureId("823")
    public void getCheckConnectedIbTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 400", response.code(), is(400));
        assertThat("Check the response code is 400", responseBody.status, is(400));
        assertThat("Check the response code is 400", responseBody.error, is("No search parameters specified"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + emailAddress, response true by all params (200)")
    @AllureId("824")
    public void getCheckConnectedIbTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("emailAddress", userFrom1.getEmail());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + emailAddress, response true by cpaId match (200)")
    @AllureId("837")
    public void getCheckConnectedIbTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom4.getUcid());
        queryParams.put("emailAddress", userFrom4.getEmail());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + emailAddress, response true by refId match (200)")
    @AllureId("838")
    public void getCheckConnectedIbTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom5.getUcid());
        queryParams.put("emailAddress", userFrom5.getEmail());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + emailAddress, response true by ibId match (200)")
    @AllureId("839")
    public void getCheckConnectedIbTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom6.getUcid());
        queryParams.put("emailAddress", userFrom6.getEmail());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + emailAddress, response false - cpa/ib/ref empty (200)")
    @AllureId("835")
    public void getCheckConnectedIbTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom3.getUcid());
        queryParams.put("emailAddress", userFrom3.getEmail());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("false"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + emailAddress, response false (200)")
    @AllureId("824")
    public void getCheckConnectedIbTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom2.getUcid());
        queryParams.put("emailAddress", userFrom2.getEmail());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("false"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + digital (200)")
    @AllureId("825")
    public void getCheckConnectedIbTest8() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("digital", userFrom1.getDigitalId());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + device (200)")
    @AllureId("826")
    public void getCheckConnectedIbTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("device", userFrom1.getDeviceId());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + session (200)")
    @AllureId("827")
    public void getCheckConnectedIbTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("session", userFrom1.getSessionId());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + nameBirth (200)")
    @AllureId("828")
    public void getCheckConnectedIbTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("nameBirth", userFrom1.getNameDateOfBirth());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + webSession (200)")
    @AllureId("829")
    public void getCheckConnectedIbTest12() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("webSession", userFrom1.getWebSessionId());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + phoneNumber (200)")
    @AllureId("830")
    public void getCheckConnectedIbTest13() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("phoneNumber", userFrom1.getPhoneNumber());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + ipAddress (200)")
    @AllureId("831")
    public void getCheckConnectedIbTest14() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("ipAddress", userFrom1.getIpAddress());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id, no parameters (400)")
    @AllureId("832")
    public void getCheckConnectedIbTest15() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 400", response.code(), is(400));
        assertThat("Check the response code is 400", responseBody.type, is("about:blank"));
        assertThat("Check the response code is 400", responseBody.title, is("Bad Request"));
        assertThat("Check the response code is 400", responseBody.status, is(400));
        assertThat("Check the response code is 400", responseBody.detail, is("Required parameter 'clientId' is not present."));
        assertThat("Check the response code is 400", responseBody.instance, is("/v1/connections/checkConnectedIb"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + emailAddress, response true for connection level 2 (200)")
    @AllureId("840")
    public void getCheckConnectedIbTest16() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom7.getUcid());
        queryParams.put("emailAddress", userFrom7.getEmail());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + emailAddress, response true for connection level 3 (200)")
    @AllureId("831")
    public void getCheckConnectedIbTest17() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom8.getUcid());
        queryParams.put("emailAddress", userFrom8.getEmail());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }
}
