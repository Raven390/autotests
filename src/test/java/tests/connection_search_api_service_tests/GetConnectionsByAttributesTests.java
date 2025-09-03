package tests.connection_search_api_service_tests;

import business_objects.api.connection_search_api.get_connections.GetConnectionsResponse;
import business_objects.api.connection_search_api.get_connections.GetConnectionsResponseError;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.device_id_table.DeviceIdTableEntry;
import business_objects.db.clickhouse.digital_id_table.DigitalIdTableEntry;
import business_objects.db.clickhouse.document_table.DocumentTableEntry;
import business_objects.db.clickhouse.email_table.EmailTableEntry;
import business_objects.db.clickhouse.ip_table.IpTableEntry;
import business_objects.db.clickhouse.name_birth.NameBirthTableEntry;
import business_objects.db.clickhouse.payout.PayoutTableEntry;
import business_objects.db.clickhouse.phone.PhoneTableEntry;
import business_objects.db.clickhouse.session_id.SessionIdTableEntry;
import business_objects.db.clickhouse.web_session.WebSessionTableEntry;
import helpers.data.ClientHelper;
import helpers.data.enums.ConnectionAttributes;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.*;

import static business_objects.api.connection_search_api.get_connections.GetConnectionsRequest.getConnectionsByAttributes;
import static business_objects.api.connection_search_api.get_connections.GetConnectionsResponseFactory.*;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.*;
import static business_objects.db.clickhouse.device_id_table.DeviceIdTableEntryFactory.deviceIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.digital_id_table.DigitalIdTableEntryFactory.digitalIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.digital_id_table.DigitalIdTableEntryFactory.digitalIdTableEntryForConnectionSearchFiltration;
import static business_objects.db.clickhouse.document_table.DocumentTableEntryFactory.documentTableEntryForConnectionSearchRandomized;
import static business_objects.db.clickhouse.email_table.EmailTableEntryFactory.emailTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.email_table.EmailTableEntryFactory.emailTableEntryForConnectionSearchFiltration;
import static business_objects.db.clickhouse.ip_table.IpTableEntryFactory.ipTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.name_birth.NameBirthTableEntryFactory.nameBirthTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.payout.PayoutTableEntryFactory.payoutTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.phone.PhoneTableEntryFactory.phoneTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.session_id.SessionIdTableEntryFactory.sessionIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.web_session.WebSessionTableEntryFactory.webSessionTableEntryForConnectionSearch;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.ConnectionAttributes.*;
import static helpers.database.CleanTableHelper.cleanConnectionsTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Constants.*;
import static utils.Utils.insertConnectionToDb;
import static utils.Utils.waitForConnectionSearchToUpdate;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_ATTRIBUTES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
class GetConnectionsByAttributesTests extends TestBaseApi {
    static final ClientHelper userFromDocument = getRandomVantageClientAllFields();
    static final ClientHelper userToDocument = getRandomVantageClientAllFields();
    static final ClientHelper userFromEmail = getRandomVantageClientAllFields();
    static final ClientHelper userToEmail = getRandomVantageClientAllFields();
    static final ClientHelper userFromIp = getRandomVantageClientAllFields();
    static final ClientHelper userFromIp2 = getRandomVantageClientAllFields();
    static final ClientHelper userToIp = getRandomVantageClientAllFields();
    static final ClientHelper userToIp2 = getRandomVantageClientAllFields();
    static final ClientHelper userToIp3 = getRandomVantageClientAllFields();
    static final ClientHelper userFromPhone = getRandomVantageClientAllFields();
    static final ClientHelper userToPhone = getRandomVantageClientAllFields();
    static final ClientHelper userFromPayout = getRandomVantageClientAllFields();
    static final ClientHelper userToPayout = getRandomVantageClientAllFields();
    static final ClientHelper userFromDepth = getRandomVantageClientAllFields();
    static final ClientHelper userToDepth1 = getRandomVantageClientAllFields();
    static final ClientHelper userToDepth2 = getRandomVantageClientAllFields();
    static final ClientHelper userFromFiltration = getRandomVantageClientAllFields();
    static final ClientHelper userToFiltration1 = getRandomVantageClientAllFields();
    static final ClientHelper userToFiltration2 = getRandomVantageClientAllFields();
    static final ClientHelper userFromDeviceId = getRandomVantageClientAllFields();
    static final ClientHelper userToDeviceId = getRandomVantageClientAllFields();
    static final ClientHelper userFromDigitalId = getRandomVantageClientAllFields();
    static final ClientHelper userToDigitalId = getRandomVantageClientAllFields();
    static final ClientHelper userFromNameBirth = getRandomVantageClientAllFields();
    static final ClientHelper userToNameBirth = getRandomVantageClientAllFields();
    static final ClientHelper userFromSessionId = getRandomVantageClientAllFields();
    static final ClientHelper userToSessionId = getRandomVantageClientAllFields();
    static final ClientHelper userFromWebSessionId = getRandomVantageClientAllFields();
    static final ClientHelper userToWebSessionId = getRandomVantageClientAllFields();
    // Expected responses
    static GetConnectionsResponse getConnectionsByAttributesDocumentResponseSuccessInitial = getConnectionsByAttributesResponseSuccessDocumentInitial(userFromDocument);
    static GetConnectionsResponse getConnectionsByAttributesDocumentResponseSuccess = getConnectionsByAttributesResponseSuccessDocumentLvl2(userFromDocument, userToDocument);
    static GetConnectionsResponse getConnectionsByAttributesEmailResponseSuccessInitial = getConnectionsByAttributesResponseSuccessEmailInitial(userFromEmail);
    final GetConnectionsResponse getConnectionsByAttributesEmailResponseSuccess = getConnectionsByAttributesResponseSuccessEmailLvl2(userFromEmail, userToEmail);
    static GetConnectionsResponse getConnectionsByAttributesIpResponseSuccessInitial = getConnectionsByAttributesResponseSuccessIpInitial(userFromIp);
    final GetConnectionsResponse getConnectionsByAttributesIpResponseSuccess = getConnectionsByAttributesResponseSuccessIpLvl2(userFromIp, userToIp);
    static GetConnectionsResponse getConnectionsByAttributesPhoneResponseSuccessInitial = getConnectionsByAttributesResponseSuccessPhoneInitial(userFromPhone);
    final GetConnectionsResponse getConnectionsByAttributesPhoneResponseSuccess = getConnectionsByAttributesResponseSuccessPhoneLvl2(userFromPhone, userToPhone);
    final GetConnectionsResponse getConnectionsByAttributesPayoutResponseSuccessInitial = getConnectionsByAttributesResponseSuccessPayoutInitial(userFromPayout);
    final GetConnectionsResponse getConnectionsByAttributesPayoutResponseSuccess = getConnectionsByAttributesResponseSuccessPayoutLvl2(userFromPayout, userToPayout);
    final GetConnectionsResponse[] getConnectionsByAttributesFiltrationResponseSuccess = getConnectionsForFiltrationByParams(userFromFiltration, userToFiltration1, userToFiltration2);
    final GetConnectionsResponse getConnectionsByAttributesDeviceIdResponseSuccessInitial = getConnectionsByAttributesResponseSuccessDeviceIdInitial(userFromDeviceId);
    static GetConnectionsResponse getConnectionsByAttributesDeviceIdResponseSuccess = getConnectionsByAttributesResponseSuccessDeviceIdLvl2(userFromDeviceId, userToDeviceId);
    static final GetConnectionsResponse getConnectionsByAttributesDigitalIdResponseSuccessInitial = getConnectionsByAttributesResponseSuccessDigitalIdInitial(userFromDigitalId);
    static GetConnectionsResponse getConnectionsByAttributesDigitalIdResponseSuccess = getConnectionsByAttributesResponseSuccessDigitalIdLvl2(userFromDigitalId, userToDigitalId);
    static final GetConnectionsResponse getConnectionsByAttributesNameBirthResponseSuccessInitial = getConnectionsByAttributesResponseSuccessNameBirthInitial(userFromNameBirth);
    static GetConnectionsResponse getConnectionsByAttributesNameBirthResponseSuccess = getConnectionsByAttributesResponseSuccessNameBirthLvl2(userFromNameBirth, userToNameBirth);
    final GetConnectionsResponse getConnectionsByAttributesSessionIdResponseSuccessInitial = getConnectionsByAttributesResponseSuccessSessionIdInitial(userFromSessionId);
    static GetConnectionsResponse getConnectionsByAttributesSessionIdResponseSuccess = getConnectionsByAttributesResponseSuccessSessionIdLvl2(userFromSessionId, userToSessionId);
    final GetConnectionsResponse getConnectionsByAttributesWebSessionIdResponseSuccessInitial = getConnectionsByAttributesResponseSuccessWebSessionIdInitial(userFromWebSessionId);
    static GetConnectionsResponse getConnectionsByAttributesWebSessionIdResponseSuccess = getConnectionsByAttributesResponseSuccessWebSessionIdLvl2(userFromWebSessionId, userToWebSessionId);

    // Objects to insert to attributes tables
    static final DocumentTableEntry documentTableEntry = documentTableEntryForConnectionSearchRandomized(userFromDocument);
    static final EmailTableEntry emailTableEntry = emailTableEntryForConnectionSearch(userFromEmail, userFromEmail.getEmail());
    static final IpTableEntry ipTableEntry = ipTableEntryForConnectionSearch(userFromIp, true);
    static final IpTableEntry ipTableEntry2 = ipTableEntryForConnectionSearch(userFromIp2, true);
    static final IpTableEntry ipTableEntryFiltration = ipTableEntryForConnectionSearch(userFromFiltration, true);
    static final DigitalIdTableEntry digitalIdTableEntryIp = digitalIdTableEntryForConnectionSearch(userFromIp2);
    static final PhoneTableEntry phoneTableEntry = phoneTableEntryForConnectionSearch(userFromPhone);
    static final PhoneTableEntry phoneTableEntry2 = phoneTableEntryForConnectionSearch(userToIp3);
    static final PayoutTableEntry payoutTableEntry = payoutTableEntryForConnectionSearch(userFromPayout);
    static final EmailTableEntry emailTableEntryFiltration = emailTableEntryForConnectionSearchFiltration(userFromFiltration);
    static final DeviceIdTableEntry deviceIdTableEntry = deviceIdTableEntryForConnectionSearch(userFromDeviceId);
    static final DigitalIdTableEntry digitalIdTableEntry = digitalIdTableEntryForConnectionSearch(userFromDigitalId);
    static final DigitalIdTableEntry digitalIdTableEntryFiltration = digitalIdTableEntryForConnectionSearchFiltration(userFromFiltration);
    static final NameBirthTableEntry nameBirthTableEntry = nameBirthTableEntryForConnectionSearch(userFromNameBirth);
    static final SessionIdTableEntry sessionIdTableEntry = sessionIdTableEntryForConnectionSearch(userFromSessionId);
    static final WebSessionTableEntry webSessionTableEntryFrom = webSessionTableEntryForConnectionSearch(userFromWebSessionId);
    static final WebSessionTableEntry webSessionTableEntryTo = webSessionTableEntryForConnectionSearch(userToWebSessionId);

    // Objects to insert to connections table
    static final ConnectionTableEntry connectionTableEntryByDocument = getConnectionTableEntry(userFromDocument, userToDocument, DOCUMENT);
    static final ConnectionTableEntry connectionTableEntryByEmail = getConnectionTableEntry(userFromEmail, userToEmail, EMAIL);
    static final ConnectionTableEntry connectionTableEntryByIp = getConnectionTableEntry(userFromIp, userToIp, IP);
    static final ConnectionTableEntry connectionTableEntryByIp2 = getConnectionTableEntry(userFromIp2, userToIp2, userFromIp2.getIpAddress());
    static final ConnectionTableEntry connectionTableEntryByIp3 = getConnectionTableEntry(userFromIp2, userToIp3, IP);
    static final ConnectionTableEntry connectionTableEntryNotByIp3 = getConnectionTableEntry(userFromIp2, userToIp3, PAYOUT);
    static final ConnectionTableEntry connectionTableEntryByPhone = getConnectionTableEntry(userFromPhone, userToPhone, PHONE);
    static final ConnectionTableEntry connectionTableEntryByPayout = getConnectionTableEntry(userFromPayout, userToPayout, PAYOUT);
    static final ConnectionTableEntry connectionTableEntryForDepth1 = getConnectionTableEntry(userFromDepth, userToDepth1);
    static final ConnectionTableEntry connectionTableEntryForDepth2 = getConnectionTableEntryLvl2(userToDepth1, userToDepth2);
    static final ConnectionTableEntry connectionTableEntryFiltration1 = getConnectionTableEntry(userFromFiltration, userToFiltration1);
    static final ConnectionTableEntry connectionTableEntryFiltration2 = getConnectionTableEntryForFiltration(userToFiltration1, userToFiltration2);
    static final ConnectionTableEntry connectionTableEntryByDeviceId = getConnectionTableEntry(userFromDeviceId, userToDeviceId, ConnectionAttributes.DEVICE);
    static final ConnectionTableEntry connectionTableEntryByDigitalId = getConnectionTableEntry(userFromDigitalId, userToDigitalId, ConnectionAttributes.DIGITAL);
    static final ConnectionTableEntry connectionTableEntryByNameBirth = getConnectionTableEntry(userFromNameBirth, userToNameBirth, NAME_BIRTH);
    static final ConnectionTableEntry connectionTableEntryBySessionId = getConnectionTableEntry(userFromSessionId, userToSessionId, SESSION);
    static final ConnectionTableEntry connectionTableEntryByWebSessionId = getConnectionTableEntry(userFromWebSessionId, userToWebSessionId, WEB_SESSION);

    @BeforeAll
    static void setupConnectionTableEntry() throws Exception {
        getConnectionsByAttributesDigitalIdResponseSuccessInitial.connectionStrengthToInitial = 0.699_999_988_079_071;
        getConnectionsByAttributesDigitalIdResponseSuccessInitial.connectionScoreToInitial = getConnectionsByAttributesDigitalIdResponseSuccessInitial.connectionStrengthToInitial;
        getConnectionsByAttributesNameBirthResponseSuccessInitial.connectionStrengthToInitial = 0.800_000_011_920_929;
        getConnectionsByAttributesNameBirthResponseSuccessInitial.connectionScoreToInitial = getConnectionsByAttributesNameBirthResponseSuccessInitial.connectionStrengthToInitial;
        // Insert data to attributes tables
        insertObjectToDb(DOCUMENT_TABLE_NAME, documentTableEntry);
        insertObjectToDb(EMAIL_TABLE_NAME, emailTableEntry);
        insertObjectToDb(IP_TABLE_NAME, ipTableEntry);
        insertObjectToDb(IP_TABLE_NAME, ipTableEntry2);
        insertObjectsToDb(PHONE_TABLE_NAME, List.of(phoneTableEntry, phoneTableEntry2));
        insertObjectToDb(PAYOUT_TABLE_NAME, payoutTableEntry);
        insertObjectToDb(EMAIL_TABLE_NAME, emailTableEntryFiltration);
        insertObjectsToDb(DIGITAL_ID_TABLE_NAME, List.of(digitalIdTableEntry, digitalIdTableEntryIp, digitalIdTableEntryFiltration));
        insertObjectToDb(DEVICE_ID_TABLE_NAME, deviceIdTableEntry);
        insertObjectToDb(SESSION_ID_TABLE_NAME, sessionIdTableEntry);
        insertObjectToDb(NAME_BIRTH_TABLE_NAME, nameBirthTableEntry);
        insertObjectToDb(WEB_SESSION_TABLE_NAME, webSessionTableEntryFrom);
        insertObjectToDb(WEB_SESSION_TABLE_NAME, webSessionTableEntryTo);
        // Insert data to connections table
        insertConnectionToDb(connectionTableEntryByDocument, connectionTableEntryByEmail, connectionTableEntryByIp, connectionTableEntryByIp2, connectionTableEntryByIp3, connectionTableEntryByPhone, connectionTableEntryByPayout, connectionTableEntryForDepth1, connectionTableEntryForDepth2, connectionTableEntryFiltration1, connectionTableEntryFiltration2, connectionTableEntryByDeviceId, connectionTableEntryByDigitalId, connectionTableEntryByNameBirth, connectionTableEntryBySessionId, connectionTableEntryByWebSessionId);
        waitForConnectionSearchToUpdate();
        Thread.sleep(15_000);//pause for asinc services like CS and AR alvays set up connections last and use waitForConnectionSearchToUpdate() before this wait.
    }

    @AfterAll
    static void deleteConnectionTableEntry() throws Exception {
        // Delete data from connections table
        cleanConnectionsTableByClient(connectionTableEntryByDocument.userFrom, connectionTableEntryByEmail.userFrom, connectionTableEntryByIp.userFrom, connectionTableEntryByPhone.userFrom, connectionTableEntryByPayout.userFrom, connectionTableEntryForDepth1.userFrom, connectionTableEntryForDepth2.userFrom, connectionTableEntryFiltration1.userFrom, connectionTableEntryFiltration2.userFrom, connectionTableEntryByDeviceId.userFrom, connectionTableEntryByDigitalId.userFrom, connectionTableEntryByNameBirth.userFrom, connectionTableEntryBySessionId.userFrom, connectionTableEntryByWebSessionId.userFrom);
        // Delete data from attributes tables
        deleteEntryFromDb(DOCUMENT_TABLE_NAME, String.format("acc_id_num = '%s'", documentTableEntry.accIdNum));
        deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("email = '%s'", emailTableEntry.email));
        deleteEntryFromDb(IP_TABLE_NAME, String.format("ip = '%s'", ipTableEntry.ip));
        deleteEntryFromDb(PHONE_TABLE_NAME, String.format("phone_num = '%s'", phoneTableEntry.phoneNum));
        deleteEntryFromDb(PAYOUT_TABLE_NAME, String.format("payout = '%s'", payoutTableEntry.payout));
        deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("email = '%s'", emailTableEntryFiltration.email));
        deleteEntryFromDb(DEVICE_ID_TABLE_NAME, String.format("device_id = '%s'", deviceIdTableEntry.deviceId));
        deleteEntryFromDb(DIGITAL_ID_TABLE_NAME, String.format("digital_id = '%s'", digitalIdTableEntry.digitalId));
        deleteEntryFromDb(DIGITAL_ID_TABLE_NAME, String.format("digital_id = '%s'", digitalIdTableEntryIp.digitalId));
        deleteEntryFromDb(NAME_BIRTH_TABLE_NAME, String.format("ucid = '%s'", nameBirthTableEntry.ucid));
        deleteEntryFromDb(SESSION_ID_TABLE_NAME, String.format("session_id = '%s'", sessionIdTableEntry.sessionId));
        deleteEntryFromDb(WEB_SESSION_TABLE_NAME, String.format("web_session_id = '%s'", webSessionTableEntryFrom.webSessionId));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by document success(200)")
    @AllureId("188")
    void getConnectionsTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        Allure.step("Get connections with null as client from");
        GetConnectionsResponse connection = Arrays.stream(responseBody).filter(connect -> connect.clientIdFrom == null).findFirst().orElse(null);
        assertNotNull(connection);


        GetConnectionsResponse.ConnectionDetail[] details = connection.connectionDetail;
        //get connection detail documentType
        GetConnectionsResponse.ConnectionDetail documentType = Arrays.stream(details).filter(connect -> "documentType".equals(connect.connectionAttributeName)).findFirst().orElse(null);
        assertNotNull(documentType);
        assertEquals(documentTableEntry.accIdType, documentType.connectionAttributeValue);
        assertEquals(userFromDocument.getUcid(), connection.clientIdTo);

        Allure.step("Check value of document number in connection details");
        GetConnectionsResponse.ConnectionDetail documentNumber = Arrays.stream(details).filter(connect -> "documentNumber".equals(connect.connectionAttributeName)).findFirst().orElse(null);
        assertNotNull(documentNumber);
        assertEquals(documentTableEntry.accIdNum, documentNumber.connectionAttributeValue);
        assertEquals(userFromDocument.getUcid(), connection.clientIdTo);

        Allure.step("Check value of document nation code in connection details");
        GetConnectionsResponse.ConnectionDetail natCode = Arrays.stream(details).filter(connect -> "documentCountryId".equals(connect.connectionAttributeName)).findFirst().orElse(null);
        assertNotNull(natCode);
        assertEquals(documentTableEntry.nationalityId, Integer.valueOf(natCode.connectionAttributeValue));
        assertEquals(userFromDocument.getUcid(), connection.clientIdTo);
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by document with connection depth success(200)")
    @AllureId("189")
    void getConnectionsTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);
        queryParams.put("connectionDepth", 1);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));

        Allure.step("Get connections with null as client from");
        GetConnectionsResponse connection = Arrays.stream(responseBody).filter(connect -> connect.clientIdFrom == null).findFirst().orElse(null);
        assertNotNull(connection);


        GetConnectionsResponse.ConnectionDetail[] details = connection.connectionDetail;
        //get connection detail documentType
        GetConnectionsResponse.ConnectionDetail documentType = Arrays.stream(details).filter(connect -> "documentType".equals(connect.connectionAttributeName)).findFirst().orElse(null);
        assertNotNull(documentType);
        assertEquals(documentTableEntry.accIdType, documentType.connectionAttributeValue);
        assertEquals(userFromDocument.getUcid(), connection.clientIdTo);

        Allure.step("Check value of document number in connection details");
        GetConnectionsResponse.ConnectionDetail documentNumber = Arrays.stream(details).filter(connect -> "documentNumber".equals(connect.connectionAttributeName)).findFirst().orElse(null);
        assertNotNull(documentNumber);
        assertEquals(documentTableEntry.accIdNum, documentNumber.connectionAttributeValue);
        assertEquals(userFromDocument.getUcid(), connection.clientIdTo);

        Allure.step("Check value of document nation code in connection details");
        GetConnectionsResponse.ConnectionDetail natCode = Arrays.stream(details).filter(connect -> "documentCountryId".equals(connect.connectionAttributeName)).findFirst().orElse(null);
        assertNotNull(natCode);
        assertEquals(documentTableEntry.nationalityId, Integer.valueOf(natCode.connectionAttributeValue));
        assertEquals(userFromDocument.getUcid(), connection.clientIdTo);
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by email success(200)")
    @AllureId("190")
    void getConnectionsTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntry.email);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesEmailResponseSuccess));
        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesEmailResponseSuccessInitial));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get empty response by only one ip(200)")
    @AllureId("191")
    void getConnectionsTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ipAddress", ipTableEntry2.ip);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get not empty response for connections not only with ip(200)")
    @AllureId("1141")
    void getConnectionsTest23() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ipAddress", ipTableEntry2.ip);
        queryParams.put("digital", digitalIdTableEntryIp.digitalId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by phone success(200)")
    @AllureId("192")
    void getConnectionsTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("phoneNumber", phoneTableEntry.phoneNum);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesPhoneResponseSuccess));
        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesPhoneResponseSuccessInitial));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by payout success(200)")
    @AllureId("193")
    void getConnectionsTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("payoutId", payoutTableEntry.payout);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesPayoutResponseSuccess));
        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesPayoutResponseSuccessInitial));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by all params success(200)")
    @AllureId("194")
    void getConnectionsTest7() throws IOException {
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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(8));

        assertThat("Check that response body has object found by document", responseBody, hasItemInArray(getConnectionsByAttributesDocumentResponseSuccess));
        assertThat("Check that response body has object found by document", responseBody, hasItemInArray(getConnectionsByAttributesDocumentResponseSuccessInitial));
        assertThat("Check that response body has object found by emailAddress", responseBody, hasItemInArray(getConnectionsByAttributesEmailResponseSuccess));
        assertThat("Check that response body has object found by emailAddress", responseBody, hasItemInArray(getConnectionsByAttributesEmailResponseSuccessInitial));
        assertThat("Check that response body has object found by phoneNumber", responseBody, hasItemInArray(getConnectionsByAttributesPhoneResponseSuccess));
        assertThat("Check that response body has object found by phoneNumber", responseBody, hasItemInArray(getConnectionsByAttributesPhoneResponseSuccessInitial));
        assertThat("Check that response body has object found by payoutId", responseBody, hasItemInArray(getConnectionsByAttributesPayoutResponseSuccess));
        assertThat("Check that response body has object found by payoutId", responseBody, hasItemInArray(getConnectionsByAttributesPayoutResponseSuccessInitial));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreFrom success(200)")
    @AllureId("467")
    void getConnectionsTest8() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreFrom", 0.4);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat("Check the response body", responseBody, arrayContainingInAnyOrder(getConnectionsByAttributesFiltrationResponseSuccess[0], getConnectionsByAttributesFiltrationResponseSuccess[1]));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreTo success(200)")
    @AllureId("468")
    void getConnectionsTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("digital", digitalIdTableEntryFiltration.digitalId);
        queryParams.put("ipAddress", ipTableEntryFiltration.ip);
        queryParams.put("connectionScoreTo", 0.6);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));

        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsByAttributesFiltrationResponseSuccess[2]));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionType success(200)")
    @AllureId("469")
    void getConnectionsTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionType", List.of(CONNECTION_TYPE_SAME_PERSON));

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        GetConnectionsResponse connectNull = null;
        for (GetConnectionsResponse resp : responseBody) {
            connectNull = resp;
            if (connectNull.clientIdFrom == null) {
                break;
            }
        }

        GetConnectionsResponse connectFrom = null;
        for (GetConnectionsResponse resp : responseBody) {
            connectFrom = resp;
            if (connectFrom.clientIdFrom != null && !("null".equals(connectFrom.clientIdFrom)) && connectFrom.clientIdFrom.equals(userFromFiltration.getUcid())) {
                break;
            }
        }

        assertThat("Check the response body", connectNull, equalTo(getConnectionsByAttributesFiltrationResponseSuccess[0]));
        assertThat("Check the response body", connectFrom, equalTo(getConnectionsByAttributesFiltrationResponseSuccess[1]));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentCountryId not int bad request (400)")
    @AllureId("195")
    void getConnectionsTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", "test");

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorDocumentCountryIdNotInt()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentType missing bad request (Empty response)")
    @AllureId("196")
    void getConnectionsTest12() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 400", response.code(), is(200));

        assertThat("Check the response body", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentNumber missing bad request (Empty response)")
    @AllureId("197")
    void getConnectionsTest13() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 400", response.code(), is(200));

        assertThat("Check the response body", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search by attributes Api. documentCountryId missing bad request (Empty response)")
    @AllureId("198")
    void getConnectionsTest14() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 400", response.code(), is(200));

        assertThat("Check the response body", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search by attributes Api. No params bad request (400)")
    @AllureId("199")
    void getConnectionsTest15() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorNoSearchParameters()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreFrom not int Bad Request(400)")
    @AllureId("470")
    void getConnectionsTest16() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreFrom", "test");

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsByAttributesResponseErrorConnectionScoreFromBadRequest()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection with connectionScoreTo not int Bad Request(400)")
    @AllureId("471")
    void getConnectionsTest17() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreTo", "test");

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsByAttributesResponseErrorConnectionScoreToBadRequest()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by device id success(200)")
    @AllureId("695")
    void getConnectionsTest18() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("device", deviceIdTableEntry.deviceId);

        Response response = getConnectionsByAttributes(queryParams);
        assert response.body() != null;
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesDeviceIdResponseSuccessInitial));
        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesDeviceIdResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by digital id success(200)")
    @AllureId("696")
    void getConnectionsTest19() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("digital", digitalIdTableEntry.digitalId);


        Response response = getConnectionsByAttributes(queryParams);
        assert response.body() != null;
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesDigitalIdResponseSuccessInitial));
        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesDigitalIdResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by name birth success(200)")
    @AllureId("697")
    void getConnectionsTest20() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("nameBirth", userFromNameBirth.getNameDateOfBirth());

        Response response = getConnectionsByAttributes(queryParams);
        assert response.body() != null;
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesNameBirthResponseSuccess));
        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesNameBirthResponseSuccessInitial));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by session id success(200)")
    @AllureId("698")
    void getConnectionsTest21() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("session", sessionIdTableEntry.sessionId);

        Response response = getConnectionsByAttributes(queryParams);
        assert response.body() != null;
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesSessionIdResponseSuccess));
        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesSessionIdResponseSuccessInitial));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by web session id success(200)")
    @AllureId("699")
    void getConnectionsTest22() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("webSession", webSessionTableEntryFrom.webSessionId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesWebSessionIdResponseSuccess));
        assertThat("Check the response body", responseBody, hasItemInArray(getConnectionsByAttributesWebSessionIdResponseSuccessInitial));
    }
}
