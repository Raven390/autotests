package tests.connectionSearchApiServiceTests;

import businessObjects.api.connectionSearchApi.ConnectionSearchResponseError;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.deviceIdTable.DeviceIdTableEntry;
import businessObjects.db.clickhouse.digitalIdTable.DigitalIdTableEntry;
import businessObjects.db.clickhouse.emailTable.EmailTableEntry;
import businessObjects.db.clickhouse.ipTable.IpTableEntry;
import businessObjects.db.clickhouse.nameBirth.NameBirthTableEntry;
import businessObjects.db.clickhouse.phone.PhoneTableEntry;
import businessObjects.db.clickhouse.sessionId.SessionIdTableEntry;
import businessObjects.db.clickhouse.webSession.WebSessionTableEntry;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.connectionSearchApi.checkConnectedIb.CheckConnectedIbRequest.getCheckConnectedIb;
import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.getConnectionTableEntry;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.deviceIdTable.DeviceIdTableEntryFactory.deviceIdTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.digitalIdTable.DigitalIdTableEntryFactory.digitalIdTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.emailTable.EmailTableEntryFactory.emailTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.emailTable.EmailTableEntryFactory.getEmailTableEntryByClient;
import static businessObjects.db.clickhouse.ipTable.IpTableEntryFactory.ipTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.nameBirth.NameBirthTableEntryFactory.nameBirthTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.phone.PhoneTableEntryFactory.phoneTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.sessionId.SessionIdTableEntryFactory.sessionIdTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.webSession.WebSessionTableEntryFactory.webSessionTableEntryForConnectionSearch;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.ClientFactory.getRandomVantageClientNoCpaIbRef;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

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
    public static final ClientHelper userTo7_1 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo7_2 = getRandomVantageClientAllFields();
    public static final ClientHelper userFrom8 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo8_1 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo8_2 = getRandomVantageClientAllFields();
    public static final ClientHelper userTo8_3 = getRandomVantageClientAllFields();

    public static CrmTbUserObject user1;
    public static CrmTbUserObject user2;
    public static CrmTbUserObject user3;
    public static CrmTbUserObject user4;
    public static CrmTbUserObject user5;
    public static CrmTbUserObject user6;
    public static CrmTbUserObject user7;
    public static CrmTbUserObject user8;
    public static CrmTbUserObject user9;
    public static CrmTbUserObject user9_1;
    public static CrmTbUserObject user9_2;
    public static CrmTbUserObject user10;
    public static CrmTbUserObject user10_1;
    public static CrmTbUserObject user10_2;
    public static CrmTbUserObject user10_3;

    public static ConnectionTableEntry connectionTableEntry1 = getConnectionTableEntry(userFrom1, userTo1);
    public static ConnectionTableEntry connectionTableEntry2 = getConnectionTableEntry(userFrom2, userTo2);
    public static ConnectionTableEntry connectionTableEntry3 = getConnectionTableEntry(userFrom3, userTo3);
    public static ConnectionTableEntry connectionTableEntry4 = getConnectionTableEntry(userFrom7, userTo7_1);
    public static ConnectionTableEntry connectionTableEntry5 = getConnectionTableEntry(userTo7_1, userTo7_2);
    public static ConnectionTableEntry connectionTableEntry6 = getConnectionTableEntry(userFrom8, userTo8_1);
    public static ConnectionTableEntry connectionTableEntry7 = getConnectionTableEntry(userTo8_1, userTo8_2);
    public static ConnectionTableEntry connectionTableEntry8 = getConnectionTableEntry(userTo8_2, userTo8_3);

    public static final EmailTableEntry emailTableEntry1_1 = emailTableEntryForConnectionSearch(userFrom1);
    public static final EmailTableEntry emailTableEntry1_2 = emailTableEntryForConnectionSearch(userTo1, userFrom1.getEmail());
    public static final EmailTableEntry emailTableEntry4_1 = emailTableEntryForConnectionSearch(userFrom4);
    public static final EmailTableEntry emailTableEntry4_2 = emailTableEntryForConnectionSearch(userTo4, userFrom4.getEmail());
    public static final EmailTableEntry emailTableEntry5_1 = emailTableEntryForConnectionSearch(userFrom5);
    public static final EmailTableEntry emailTableEntry5_2 = emailTableEntryForConnectionSearch(userTo5, userFrom5.getEmail());
    public static final EmailTableEntry emailTableEntry6_1 = emailTableEntryForConnectionSearch(userFrom6);
    public static final EmailTableEntry emailTableEntry6_2 = emailTableEntryForConnectionSearch(userTo6, userFrom6.getEmail());
    public static final EmailTableEntry emailTableEntry7_1 = emailTableEntryForConnectionSearch(userFrom7);
    public static final EmailTableEntry emailTableEntry7_2 = emailTableEntryForConnectionSearch(userTo7_1, userFrom7.getEmail());
    public static final EmailTableEntry emailTableEntry7_3 = emailTableEntryForConnectionSearch(userTo7_2, userFrom7.getEmail());
    public static final EmailTableEntry emailTableEntry8 = emailTableEntryForConnectionSearch(userFrom8);
    public static final EmailTableEntry emailTableEntry8_1 = emailTableEntryForConnectionSearch(userTo8_1, userFrom8.getEmail());
    public static final EmailTableEntry emailTableEntry8_2 = emailTableEntryForConnectionSearch(userTo8_1, userFrom8.getEmail());
    public static final EmailTableEntry emailTableEntry8_3 = emailTableEntryForConnectionSearch(userTo8_2, userFrom8.getEmail());

    public static final DigitalIdTableEntry digitalTableEntry1_1 = digitalIdTableEntryForConnectionSearch(userFrom1);
    public static final DigitalIdTableEntry digitalTableEntry1_2 = digitalIdTableEntryForConnectionSearch(userTo1, userFrom1.getDigitalId());

    public static final DeviceIdTableEntry deviceTableEntry1_1 = deviceIdTableEntryForConnectionSearch(userFrom1);
    public static final DeviceIdTableEntry deviceTableEntry1_2 = deviceIdTableEntryForConnectionSearch(userTo1, userFrom1.getDeviceId());

    public static final SessionIdTableEntry sessionTableEntry1_1 = sessionIdTableEntryForConnectionSearch(userFrom1);
    public static final SessionIdTableEntry sessionTableEntry1_2 = sessionIdTableEntryForConnectionSearch(userTo1, userFrom1.getSessionId());

    public static NameBirthTableEntry nameTableEntry1_1 = nameBirthTableEntryForConnectionSearch(userFrom1);
    public static NameBirthTableEntry nameTableEntry1_2 = nameBirthTableEntryForConnectionSearch(userTo1, userFrom1.getNamedateofbirth());

    public static final WebSessionTableEntry webSessionTableEntry1_1 = webSessionTableEntryForConnectionSearch(userFrom1);
    public static final WebSessionTableEntry webSessionTableEntry1_2 = webSessionTableEntryForConnectionSearch(userTo1, userFrom1.getWebSessionId());

    public static final PhoneTableEntry phoneTableEntry1_1 = phoneTableEntryForConnectionSearch(userFrom1);
    public static final PhoneTableEntry phoneTableEntry1_2 = phoneTableEntryForConnectionSearch(userTo1, userFrom1.getPhoneNumber());

    public static final IpTableEntry ipTableEntry1_1 = ipTableEntryForConnectionSearch(userFrom1);
    public static final IpTableEntry ipTableEntry1_2 = ipTableEntryForConnectionSearch(userTo1, userFrom1.getIpAddress());

    public static final EmailTableEntry emailTableEntry2_1 = getEmailTableEntryByClient(userFrom2);
    public static final EmailTableEntry emailTableEntry2_2 = getEmailTableEntryByClient(userTo2);

    public static final EmailTableEntry emailTableEntry3_1 = getEmailTableEntryByClient(userFrom3);
    public static final EmailTableEntry emailTableEntry3_2 = getEmailTableEntryByClient(userTo3);

    @BeforeAll
    public static void setupConnectionTableEntry() throws ReflectiveOperationException, SQLException {
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
        user9_1 = generateUserByClient(userTo7_1);
        userTo7_2.setCpaId(userFrom7.getCpaId());
        user9_2 = generateUserByClient(userTo7_2);
        user10 = generateUserByClient(userFrom8);
        user10_1 = generateUserByClient(userTo8_1);
        user10_2 = generateUserByClient(userTo8_2);
        userTo8_3.setCpaId(userFrom8.getCpaId());
        user10_3 = generateUserByClient(userTo8_3);
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntry1, connectionTableEntry2, connectionTableEntry3, connectionTableEntry4, connectionTableEntry5, connectionTableEntry6, connectionTableEntry7, connectionTableEntry8));
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user9_1, user9_2, user10, user10_1, user10_2, user10_3));
        insertObjectsToDb(EMAIL_TABLE_NAME, List.of(emailTableEntry1_1, emailTableEntry1_2, emailTableEntry2_1, emailTableEntry2_2, emailTableEntry3_1, emailTableEntry3_2, emailTableEntry4_1, emailTableEntry4_2, emailTableEntry5_1, emailTableEntry5_2, emailTableEntry6_1, emailTableEntry6_2, emailTableEntry7_1, emailTableEntry7_2, emailTableEntry7_3, emailTableEntry8, emailTableEntry8_1, emailTableEntry8_2, emailTableEntry8_3));
        insertObjectsToDb(DEVICE_ID_TABLE_NAME, List.of(deviceTableEntry1_1, deviceTableEntry1_2));
        insertObjectsToDb(DIGITAL_ID_TABLE_NAME, List.of(digitalTableEntry1_1, digitalTableEntry1_2));
        insertObjectsToDb(SESSION_ID_TABLE_NAME, List.of(sessionTableEntry1_1, sessionTableEntry1_2));
        insertObjectsToDb(WEB_SESSION_TABLE_NAME, List.of(webSessionTableEntry1_1, webSessionTableEntry1_2));
        insertObjectsToDb(PHONE_TABLE_NAME, List.of(phoneTableEntry1_1, phoneTableEntry1_2));
        insertObjectsToDb(IP_TABLE_NAME, List.of(ipTableEntry1_1, ipTableEntry1_2));
        insertObjectsToDb(NAME_BIRTH_TABLE_NAME, List.of(nameTableEntry1_1, nameTableEntry1_2));
    }

    @AfterAll
    public static void deleteConnectionTableEntry() throws Exception {
        cleanConnectionsTableByClient(connectionTableEntry1.userFrom, connectionTableEntry2.userFrom, connectionTableEntry3.userFrom, connectionTableEntry4.userFrom, connectionTableEntry5.userFrom, connectionTableEntry6.userFrom, connectionTableEntry7.userFrom, connectionTableEntry8.userFrom);
        cleanEmailTableByClient(emailTableEntry1_1.email, emailTableEntry1_2.email, emailTableEntry2_1.email, emailTableEntry2_2.email, emailTableEntry3_1.email, emailTableEntry3_2.email);
        cleanDeviceIdTableByClient(deviceTableEntry1_1.deviceId, deviceTableEntry1_2.deviceId);
        cleanDigitalIdTableByClient(digitalTableEntry1_1.digitalId, digitalTableEntry1_2.digitalId);
        cleanSessionIdTableByClient(sessionTableEntry1_1.sessionId, sessionTableEntry1_2.sessionId);
        cleanWebSessionIdTableByClient(webSessionTableEntry1_1.webSessionId, webSessionTableEntry1_2.webSessionId);
        cleanPhoneTableByClient(phoneTableEntry1_1.phoneNum, phoneTableEntry1_2.phoneNum);
        cleanIpTableByClient(ipTableEntry1_1.ip, ipTableEntry1_2.ip);
        cleanNameTableByClient(nameTableEntry1_1.nameDateofbirth, nameTableEntry1_2.nameDateofbirth);
        cleanCrmUserTableByClient(user1.ucid, user2.ucid, user3.ucid, user4.ucid, user5.ucid, user6.ucid, user7.ucid, user8.ucid);
    }

    @Test
    @DisplayName("Connection search. Get check connected id, not enough parameters (400)")
    @AllureId("823")
    public void getACheckConnectedIbTest1() throws IOException {
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
    public void getACheckConnectedIbTest2() throws IOException {
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
    public void getACheckConnectedIbTest3() throws IOException {
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
    public void getACheckConnectedIbTest4() throws IOException {
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
    public void getACheckConnectedIbTest5() throws IOException {
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
    public void getACheckConnectedIbTest6() throws IOException {
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
    public void getACheckConnectedIbTest7() throws IOException {
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
    public void getACheckConnectedIbTest8() throws IOException {
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
    public void getACheckConnectedIbTest9() throws IOException {
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
    public void getACheckConnectedIbTest10() throws IOException {
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
    public void getACheckConnectedIbTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("nameBirth", userFrom1.getNamedateofbirth());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }

    @Test
    @DisplayName("Connection search. Get check connected id + webSession (200)")
    @AllureId("829")
    public void getACheckConnectedIbTest12() throws IOException {
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
    public void getACheckConnectedIbTest13() throws IOException {
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
    public void getACheckConnectedIbTest14() throws IOException {
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
    public void getACheckConnectedIbTest15() throws IOException {
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
    public void getACheckConnectedIbTest16() throws IOException {
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
    public void getACheckConnectedIbTest17() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom8.getUcid());
        queryParams.put("emailAddress", userFrom8.getEmail());

        Response response = getCheckConnectedIb(queryParams);
        assert response.body() != null;
        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response code is 200", response.body().string(), is("true"));
    }
}
