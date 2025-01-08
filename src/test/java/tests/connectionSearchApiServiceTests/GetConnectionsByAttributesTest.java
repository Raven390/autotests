package tests.connectionSearchApiServiceTests;

import businessObjects.api.connectionSearchApi.GetConnectionsResponse;
import businessObjects.api.connectionSearchApi.GetConnectionsResponseError;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.deviceIdTable.DeviceIdTableEntry;
import businessObjects.db.clickhouse.digitalIdTable.DigitalIdTableEntry;
import businessObjects.db.clickhouse.documentTable.DocumentTableEntry;
import businessObjects.db.clickhouse.emailTable.EmailTableEntry;
import businessObjects.db.clickhouse.ipTable.IpTableEntry;
import businessObjects.db.clickhouse.nameBirthTable.NameBirthTableEntry;
import businessObjects.db.clickhouse.payoutTable.PayoutTableEntry;
import businessObjects.db.clickhouse.phoneTable.PhoneTableEntry;
import businessObjects.db.clickhouse.sessionIdTable.SessionIdTableEntry;
import businessObjects.db.clickhouse.webSessionTable.WebSessionTableEntry;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.connectionSearchApi.GetConnectionsRequest.getConnectionsByAttributes;
import static businessObjects.api.connectionSearchApi.GetConnectionsResponseFactory.*;
import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.*;
import static businessObjects.db.clickhouse.deviceIdTable.DeviceIdTableEntryFactory.deviceIdTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.digitalIdTable.DigitalIdTableEntryFactory.digitalIdTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.documentTable.DocumentTableEntryFactory.documentTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.emailTable.EmailTableEntryFactory.emailTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.emailTable.EmailTableEntryFactory.emailTableEntryForConnectionSearchFiltration;
import static businessObjects.db.clickhouse.ipTable.IpTableEntryFactory.ipTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.nameBirthTable.NameBirthTableEntryFactory.nameBirthTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.payoutTable.PayoutTableEntryFactory.payoutTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.phoneTable.PhoneTableEntryFactory.phoneTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.sessionIdTable.SessionIdTableEntryFactory.sessionIdTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.webSessionTable.WebSessionTableEntryFactory.webSessionTableEntryForConnectionSearch;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_ATTRIBUTES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
public class GetConnectionsByAttributesTest extends TestBaseApi {
    public static final ClientHelper userFromDocument = getRandomVantageClient();
    public static final ClientHelper userToDocument = getRandomVantageClient();
    public static final ClientHelper userFromEmail = getRandomVantageClient();
    public static final ClientHelper userToEmail = getRandomVantageClient();
    public static final ClientHelper userFromIp = getRandomVantageClient();
    public static final ClientHelper userToIp = getRandomVantageClient();
    public static final ClientHelper userFromPhone = getRandomVantageClient();
    public static final ClientHelper userToPhone = getRandomVantageClient();
    public static final ClientHelper userFromPayout = getRandomVantageClient();
    public static final ClientHelper userToPayout = getRandomVantageClient();
    public static final ClientHelper userFromDepth = getRandomVantageClient();
    public static final ClientHelper userToDepth1 = getRandomVantageClient();
    public static final ClientHelper userToDepth2 = getRandomVantageClient();
    public static final ClientHelper userFromFiltration = getRandomVantageClient();
    public static final ClientHelper userToFiltration1 = getRandomVantageClient();
    public static final ClientHelper userToFiltration2 = getRandomVantageClient();
    public static final ClientHelper userFromDeviceId = getRandomVantageClientAllFields();
    public static final ClientHelper userToDeviceId = getRandomVantageClientAllFields();
    public static final ClientHelper userFromDigitalId = getRandomVantageClientAllFields();
    public static final ClientHelper userToDigitalId = getRandomVantageClientAllFields();
    public static final ClientHelper userFromNameBirth = getRandomVantageClientAllFields();
    public static final ClientHelper userToNameBirth = getRandomVantageClientAllFields();
    public static final ClientHelper userFromSessionId = getRandomVantageClientAllFields();
    public static final ClientHelper userToSessionId = getRandomVantageClientAllFields();
    public static final ClientHelper userFromWebSessionId = getRandomVantageClientAllFields();
    public static final ClientHelper userToWebSessionId = getRandomVantageClientAllFields();
    // Expected responses
    public static GetConnectionsResponse getConnectionsByAttributesDocumentResponseSuccess = getConnectionsResponseSuccess(userFromDocument, userToDocument);
    public final GetConnectionsResponse getConnectionsByAttributesEmailResponseSuccess = getConnectionsResponseSuccess(userFromEmail, userToEmail);
    public final GetConnectionsResponse getConnectionsByAttributesIpResponseSuccess = getConnectionsResponseSuccess(userFromIp, userToIp);
    public final GetConnectionsResponse getConnectionsByAttributesPhoneResponseSuccess = getConnectionsResponseSuccess(userFromPhone, userToPhone);
    public final GetConnectionsResponse getConnectionsByAttributesPayoutResponseSuccess = getConnectionsResponseSuccess(userFromPayout, userToPayout);
    public final GetConnectionsResponse getConnectionsByAttributesConnDepthResponseSuccess = getConnectionsByAttributesForDepth(userToDepth1, userToDepth2);
    public final GetConnectionsResponse[] getConnectionsByAttributesFiltrationResponseSuccess = getConnectionsForFiltrationByParams(userFromFiltration, userToFiltration1, userToFiltration2);
    public final GetConnectionsResponseError getConnectionsResponseError = getConnectionsByAttributesResponseErrorBadRequest();
    public static GetConnectionsResponse getConnectionsByAttributesDeviceIdResponseSuccess = getConnectionsResponseSuccess(userFromDeviceId, userToDeviceId);
    public static GetConnectionsResponse getConnectionsByAttributesDigitalIdResponseSuccess = getConnectionsResponseSuccess(userFromDigitalId, userToDigitalId);
    public static GetConnectionsResponse getConnectionsByAttributesNameBirthResponseSuccess = getConnectionsResponseSuccess(userFromNameBirth, userToNameBirth);
    public static GetConnectionsResponse getConnectionsByAttributesSessionIdResponseSuccess = getConnectionsResponseSuccess(userFromSessionId, userToSessionId);
    public static GetConnectionsResponse getConnectionsByAttributesWebSessionIdResponseSuccess = getConnectionsResponseSuccess(userFromWebSessionId, userToWebSessionId);

    // Objects to insert to connections table
    public static final ConnectionTableEntry connectionTableEntryByDocument = getConnectionTableEntry(userFromDocument, userToDocument);
    public static final ConnectionTableEntry connectionTableEntryByEmail = getConnectionTableEntry(userFromEmail, userToEmail);
    public static final ConnectionTableEntry connectionTableEntryByIp = getConnectionTableEntry(userFromIp, userToIp);
    public static final ConnectionTableEntry connectionTableEntryByPhone = getConnectionTableEntry(userFromPhone, userToPhone);
    public static final ConnectionTableEntry connectionTableEntryByPayout = getConnectionTableEntry(userFromPayout, userToPayout);
    public static final ConnectionTableEntry connectionTableEntryForDepth1 = getConnectionTableEntry(userFromDepth, userToDepth1);
    public static final ConnectionTableEntry connectionTableEntryForDepth2 = getConnectionTableEntryLvl2(userToDepth1, userToDepth2);
    public static final ConnectionTableEntry connectionTableEntryFiltration1 = getConnectionTableEntry(userFromFiltration, userToFiltration1);
    public static final ConnectionTableEntry connectionTableEntryFiltration2 = getConnectionTableEntryForFiltration(userToFiltration1, userToFiltration2);
    public static final ConnectionTableEntry connectionTableEntryByDeviceId = getConnectionTableEntry(userFromDeviceId, userToDeviceId);
    public static final ConnectionTableEntry connectionTableEntryByDigitalId = getConnectionTableEntry(userFromDigitalId, userToDigitalId);
    public static final ConnectionTableEntry connectionTableEntryByNameBirth = getConnectionTableEntry(userFromNameBirth, userToNameBirth);
    public static final ConnectionTableEntry connectionTableEntryBySessionId = getConnectionTableEntry(userFromSessionId, userToSessionId);
    public static final ConnectionTableEntry connectionTableEntryByWebSessionId = getConnectionTableEntry(userFromWebSessionId, userToWebSessionId);
    // Objects to insert to attributes tables
    public static final DocumentTableEntry documentTableEntry = documentTableEntryForConnectionSearch(userFromDocument);
    public static final EmailTableEntry emailTableEntry = emailTableEntryForConnectionSearch(userFromEmail);
    public static final IpTableEntry ipTableEntry = ipTableEntryForConnectionSearch(userFromIp);
    public static final PhoneTableEntry phoneTableEntry = phoneTableEntryForConnectionSearch(userFromPhone);
    public static final PayoutTableEntry payoutTableEntry = payoutTableEntryForConnectionSearch(userFromPayout);
    public static final EmailTableEntry emailTableEntryFiltration = emailTableEntryForConnectionSearchFiltration(userFromFiltration);
    public static final DeviceIdTableEntry deviceIdTableEntry = deviceIdTableEntryForConnectionSearch(userFromDeviceId);
    public static final DigitalIdTableEntry digitalIdTableEntry = digitalIdTableEntryForConnectionSearch(userFromDigitalId);
    public static final NameBirthTableEntry nameBirthTableEntry = nameBirthTableEntryForConnectionSearch(userFromNameBirth);
    public static final SessionIdTableEntry sessionIdTableEntry = sessionIdTableEntryForConnectionSearch(userFromSessionId);
    public static final WebSessionTableEntry webSessionTableEntry = webSessionTableEntryForConnectionSearch(userFromWebSessionId);

    @BeforeAll
    public static void setupConnectionTableEntry() throws ReflectiveOperationException, SQLException {
        // Insert data to connections table
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryByDocument);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryByEmail);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryByIp);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryByPhone);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryByPayout);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryForDepth1);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryForDepth2);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryFiltration1);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryFiltration2);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryByDeviceId);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryByDigitalId);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryByNameBirth);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryBySessionId);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntryByWebSessionId);
        // Insert data to attributes tables
        insertObjectToDb(DOCUMENT_TABLE_NAME, documentTableEntry);
        insertObjectToDb(EMAIL_TABLE_NAME, emailTableEntry);
        insertObjectToDb(IP_TABLE_NAME, ipTableEntry);
        insertObjectToDb(PHONE_TABLE_NAME, phoneTableEntry);
        insertObjectToDb(PAYOUT_TABLE_NAME, payoutTableEntry);
        insertObjectToDb(EMAIL_TABLE_NAME, emailTableEntryFiltration);
        insertObjectToDb(DIGITAL_ID_TABLE_NAME, digitalIdTableEntry);
        insertObjectToDb(DEVICE_ID_TABLE_NAME, deviceIdTableEntry);
        insertObjectToDb(SESSION_ID_TABLE_NAME, sessionIdTableEntry);
        insertObjectToDb(NAME_BIRTH_TABLE_NAME, nameBirthTableEntry);
        insertObjectToDb(WEB_SESSION_TABLE_NAME, webSessionTableEntry);
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by document success(200)")
    @AllureId("188")
    public void getConnectionsTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check the response body", Arrays.stream(responseBody).toList(), containsInAnyOrder(getConnectionsByAttributesDocumentResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by document with connection depth success(200)")
    @AllureId("189")
    public void getConnectionsTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);
        queryParams.put("connectionDepth", 1);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));
        GetConnectionsResponse firstResponse = responseBody[0];

        assertThat("Check the response body", firstResponse, equalTo(getConnectionsByAttributesDocumentResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by email success(200)")
    @AllureId("190")
    public void getConnectionsTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntry.email);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));
        GetConnectionsResponse firstResponse = responseBody[0];

        assertThat("Check the response body", firstResponse, equalTo(getConnectionsByAttributesEmailResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by ip success(200)")
    @AllureId("191")
    public void getConnectionsTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ipAddress", ipTableEntry.ip);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));
        GetConnectionsResponse firstResponse = responseBody[0];

        assertThat("Check the response body", firstResponse, equalTo(getConnectionsByAttributesIpResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by phone success(200)")
    @AllureId("192")
    public void getConnectionsTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("phoneNumber", phoneTableEntry.phoneNum);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));
        GetConnectionsResponse firstResponse = responseBody[0];

        assertThat("Check the response body", firstResponse, equalTo(getConnectionsByAttributesPhoneResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by payout success(200)")
    @AllureId("193")
    public void getConnectionsTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("payoutId", payoutTableEntry.payout);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));
        GetConnectionsResponse firstResponse = responseBody[0];

        assertThat("Check the response body", firstResponse, equalTo(getConnectionsByAttributesPayoutResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by all params success(200)")
    @AllureId("194")
    public void getConnectionsTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);
        queryParams.put("emailAddress", emailTableEntry.email);
        queryParams.put("ipAddress", ipTableEntry.ip);
        queryParams.put("phoneNumber", phoneTableEntry.phoneNum);
        queryParams.put("payoutId", payoutTableEntry.payout);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check that response body has object found by document", responseBody, hasItemInArray(getConnectionsByAttributesDocumentResponseSuccess));
        assertThat("Check that response body has object found by emailAddress", responseBody, hasItemInArray(getConnectionsByAttributesEmailResponseSuccess));
        assertThat("Check that response body has object found by ipAddress", responseBody, hasItemInArray(getConnectionsByAttributesIpResponseSuccess));
        assertThat("Check that response body has object found by phoneNumber", responseBody, hasItemInArray(getConnectionsByAttributesPhoneResponseSuccess));
        assertThat("Check that response body has object found by payoutId", responseBody, hasItemInArray(getConnectionsByAttributesPayoutResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreFrom success(200)")
    @AllureId("467")
    public void getConnectionsTest8() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreFrom", 1);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));

        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsByAttributesFiltrationResponseSuccess[0]));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreTo success(200)")
    @AllureId("468")
    public void getConnectionsTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreTo", 0.9);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));

        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsByAttributesFiltrationResponseSuccess[1]));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionType success(200)")
    @AllureId("469")
    public void getConnectionsTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionType", List.of("Same Network"));

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));

        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsByAttributesFiltrationResponseSuccess[1]));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentCountryId not int bad request (400)")
    @AllureId("195")
    public void getConnectionsTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", "test");

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorDocumentCountryIdNotInt()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentType missing bad request (400)")
    @AllureId("196")
    public void getConnectionsTest12() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorDocumentTypeBadRequest()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentNumber missing bad request (400)")
    @AllureId("197")
    public void getConnectionsTest13() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorDocumentNumberBadRequest()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentCountryId missing bad request (400)")
    @AllureId("198")
    public void getConnectionsTest14() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorDocumentCountryIdBadRequest()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. No params bad request (400)")
    @AllureId("199")
    public void getConnectionsTest15() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorNoSearchParameters()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreFrom not int Bad Request(400)")
    @AllureId("470")
    public void getConnectionsTest16() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreFrom", "test");

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsByAttributesResponseErrorConnectionScoreFromBadRequest()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreTo not int Bad Request(400)")
    @AllureId("471")
    public void getConnectionsTest17() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreTo", "test");

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsByAttributesResponseErrorConnectionScoreToBadRequest()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by device id success(200)")
    @AllureId("695")
    public void getConnectionsTest18() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("device", deviceIdTableEntry.deviceId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check the response body", Arrays.stream(responseBody).toList(), containsInAnyOrder(getConnectionsByAttributesDeviceIdResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by digital id success(200)")
    @AllureId("696")
    public void getConnectionsTest19() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("digital", digitalIdTableEntry.digitalId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check the response body", Arrays.stream(responseBody).toList(), containsInAnyOrder(getConnectionsByAttributesDigitalIdResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by name birth success(200)")
    @AllureId("697")
    public void getConnectionsTest20() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("nameBirth", nameBirthTableEntry.nameDateofbirth);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check the response body", Arrays.stream(responseBody).toList(), containsInAnyOrder(getConnectionsByAttributesNameBirthResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by session id success(200)")
    @AllureId("698")
    public void getConnectionsTest21() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("session", sessionIdTableEntry.sessionId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check the response body", Arrays.stream(responseBody).toList(), containsInAnyOrder(getConnectionsByAttributesSessionIdResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by web session id success(200)")
    @AllureId("699")
    public void getConnectionsTest22() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("webSession", webSessionTableEntry.webSessionId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check the response body", Arrays.stream(responseBody).toList(), containsInAnyOrder(getConnectionsByAttributesWebSessionIdResponseSuccess));
    }

    @AfterAll
    public static void deleteConnectionTableEntry() throws SQLException {
        // Delete data from connections table
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByDocument.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByEmail.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByIp.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByPhone.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByPayout.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryForDepth1.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryForDepth2.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryFiltration1.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryFiltration2.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByDeviceId.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByDigitalId.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByNameBirth.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryBySessionId.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByWebSessionId.userFrom));
        // Delete data from attributes tables
        deleteEntryFromDb(DOCUMENT_TABLE_NAME, String.format("acc_id_num = '%s'", documentTableEntry.accIdNum));
        deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("email = '%s'", emailTableEntry.email));
        deleteEntryFromDb(IP_TABLE_NAME, String.format("ip = '%s'", ipTableEntry.ip));
        deleteEntryFromDb(PHONE_TABLE_NAME, String.format("phone_num = '%s'", phoneTableEntry.phoneNum));
        deleteEntryFromDb(PAYOUT_TABLE_NAME, String.format("payout = '%s'", payoutTableEntry.payout));
        deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("email = '%s'", emailTableEntryFiltration.email));
        deleteEntryFromDb(DEVICE_ID_TABLE_NAME, String.format("device_id = '%s'", deviceIdTableEntry.deviceId));
        deleteEntryFromDb(DIGITAL_ID_TABLE_NAME, String.format("digital_id = '%s'", digitalIdTableEntry.digitalId));
        deleteEntryFromDb(NAME_BIRTH_TABLE_NAME, String.format("name_dateofbirth = '%s'", nameBirthTableEntry.nameDateofbirth));
        deleteEntryFromDb(SESSION_ID_TABLE_NAME, String.format("session_id = '%s'", sessionIdTableEntry.sessionId));
        deleteEntryFromDb(WEB_SESSION_TABLE_NAME, String.format("web_session_id = '%s'", webSessionTableEntry.webSessionId));
    }
}
