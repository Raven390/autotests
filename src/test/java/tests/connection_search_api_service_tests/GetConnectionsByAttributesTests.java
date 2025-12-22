package tests.connection_search_api_service_tests;

import static business_objects.api.connection_search_api.get_connections.GetConnectionsRequest.getConnectionsByAttributes;
import static business_objects.api.connection_search_api.get_connections.GetConnectionsResponseFactory.*;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.*;
import static business_objects.db.clickhouse.data_science_test.device_id_table.DeviceIdTableEntryFactory.deviceIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.data_science_test.digital_id_table.DigitalIdTableEntryFactory.digitalIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.data_science_test.digital_id_table.DigitalIdTableEntryFactory.digitalIdTableEntryForConnectionSearchFiltration;
import static business_objects.db.clickhouse.data_science_test.document_table.DocumentTableEntryFactory.documentTableEntryForConnectionSearchRandomized;
import static business_objects.db.clickhouse.data_science_test.email_table.EmailTableEntryFactory.*;
import static business_objects.db.clickhouse.data_science_test.ip_table.IpTableEntryFactory.ipTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.data_science_test.mt_cid.MtCidEntryFactory.mtCidTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.data_science_test.payout.PayoutTableEntryFactory.payoutTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.data_science_test.phone.PhoneTableEntryFactory.phoneTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.data_science_test.session_id.SessionIdTableEntryFactory.sessionIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.data_science_test.web_session.WebSessionTableEntryFactory.webSessionTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.name_birth.NameBirthTableEntryFactory.nameBirthTableEntryForConnectionSearch;
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

import business_objects.api.connection_search_api.get_connections.GetConnectionsResponse;
import business_objects.api.connection_search_api.get_connections.GetConnectionsResponseError;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.data_science_test.device_id_table.DeviceIdTableEntry;
import business_objects.db.clickhouse.data_science_test.digital_id_table.DigitalIdTableEntry;
import business_objects.db.clickhouse.data_science_test.document_table.DocumentTableEntry;
import business_objects.db.clickhouse.data_science_test.email_table.EmailTableEntry;
import business_objects.db.clickhouse.data_science_test.ip_table.IpTableEntry;
import business_objects.db.clickhouse.data_science_test.mt_cid.MtCidTableEntry;
import business_objects.db.clickhouse.data_science_test.payout.PayoutTableEntry;
import business_objects.db.clickhouse.data_science_test.phone.PhoneTableEntry;
import business_objects.db.clickhouse.data_science_test.session_id.SessionIdTableEntry;
import business_objects.db.clickhouse.data_science_test.web_session.WebSessionTableEntry;
import business_objects.db.clickhouse.name_birth.NameBirthTableEntry;
import helpers.data.ClientHelper;
import helpers.data.enums.ConnectionAttributes;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.*;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_ATTRIBUTES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
class GetConnectionsByAttributesTests extends TestBaseApi {
    private static final ClientHelper userFromDocument = getRandomVantageClientAllFields();
    private static final ClientHelper userToDocument = getRandomVantageClientAllFields();
    private static final ClientHelper userFromEmail = getRandomVantageClientAllFields();
    private static final ClientHelper userToEmail = getRandomVantageClientAllFields();
    private static final ClientHelper userFromIp = getRandomVantageClientAllFields();
    private static final ClientHelper userFromIp2 = getRandomVantageClientAllFields();
    private static final ClientHelper userToIp = getRandomVantageClientAllFields();
    private static final ClientHelper userToIp2 = getRandomVantageClientAllFields();
    private static final ClientHelper userToIp3 = getRandomVantageClientAllFields();
    private static final ClientHelper userFromPhone = getRandomVantageClientAllFields();
    private static final ClientHelper userToPhone = getRandomVantageClientAllFields();
    private static final ClientHelper userFromPayout = getRandomVantageClientAllFields();
    private static final ClientHelper userToPayout = getRandomVantageClientAllFields();
    private static final ClientHelper userFromDepth = getRandomVantageClientAllFields();
    private static final ClientHelper userToDepth1 = getRandomVantageClientAllFields();
    private static final ClientHelper userToDepth2 = getRandomVantageClientAllFields();
    private static final ClientHelper userFromFiltration = getRandomVantageClientAllFields();
    private static final ClientHelper userToFiltration1 = getRandomVantageClientAllFields();
    private static final ClientHelper userToFiltration2 = getRandomVantageClientAllFields();
    private static final ClientHelper userFromDeviceId = getRandomVantageClientAllFields();
    private static final ClientHelper userToDeviceId = getRandomVantageClientAllFields();
    private static final ClientHelper userFromDigitalId = getRandomVantageClientAllFields();
    private static final ClientHelper userToDigitalId = getRandomVantageClientAllFields();
    private static final ClientHelper userFromNameBirth = getRandomVantageClientAllFields();
    private static final ClientHelper userToNameBirth = getRandomVantageClientAllFields();
    private static final ClientHelper userFromSessionId = getRandomVantageClientAllFields();
    private static final ClientHelper userToSessionId = getRandomVantageClientAllFields();
    private static final ClientHelper userFromWebSessionId = getRandomVantageClientAllFields();
    private static final ClientHelper userToWebSessionId = getRandomVantageClientAllFields();
    private static final ClientHelper userFromMtCid = getRandomVantageClientAllFields();
    private static final ClientHelper userFromMtCid2 = getRandomVantageClientAllFields();
    private static final ClientHelper userToMtCid = getRandomVantageClientAllFields();
    private static final ClientHelper userToMtCid2 = getRandomVantageClientAllFields();
    // Expected responses
    private static GetConnectionsResponse getConnectionsByAttributesDocumentResponseSuccessInitial =
            getConnectionsByAttributesResponseSuccessDocumentInitial(userFromDocument);
    private static GetConnectionsResponse getConnectionsByAttributesDocumentResponseSuccess =
            getConnectionsByAttributesResponseSuccessDocumentLvl2(userFromDocument, userToDocument);
    private static GetConnectionsResponse getConnectionsByAttributesEmailResponseSuccessInitial =
            getConnectionsByAttributesResponseSuccessEmailInitial(userFromEmail);
    private final GetConnectionsResponse getConnectionsByAttributesEmailResponseSuccess =
            getConnectionsByAttributesResponseSuccessEmailLvl2(userFromEmail, userToEmail);
    private static GetConnectionsResponse getConnectionsByAttributesPhoneResponseSuccessInitial =
            getConnectionsByAttributesResponseSuccessPhoneInitial(userFromPhone);
    private final GetConnectionsResponse getConnectionsByAttributesPhoneResponseSuccess =
            getConnectionsByAttributesResponseSuccessPhoneLvl2(userFromPhone, userToPhone);
    private final GetConnectionsResponse getConnectionsByAttributesPayoutResponseSuccessInitial =
            getConnectionsByAttributesResponseSuccessPayoutInitial(userFromPayout);
    private final GetConnectionsResponse getConnectionsByAttributesPayoutResponseSuccess =
            getConnectionsByAttributesResponseSuccessPayoutLvl2(userFromPayout, userToPayout);
    private final GetConnectionsResponse[] getConnectionsByAttributesFiltrationResponseSuccess =
            getConnectionsForFiltrationByParams(userFromFiltration, userToFiltration1, userToFiltration2);
    private final GetConnectionsResponse getConnectionsByAttributesDeviceIdResponseSuccessInitial =
            getConnectionsByAttributesResponseSuccessDeviceIdInitial(userFromDeviceId);
    private static GetConnectionsResponse getConnectionsByAttributesDeviceIdResponseSuccess =
            getConnectionsByAttributesResponseSuccessDeviceIdLvl2(userFromDeviceId, userToDeviceId);
    private static final GetConnectionsResponse getConnectionsByAttributesDigitalIdResponseSuccessInitial =
            getConnectionsByAttributesResponseSuccessDigitalIdInitial(userFromDigitalId);
    private static GetConnectionsResponse getConnectionsByAttributesDigitalIdResponseSuccess =
            getConnectionsByAttributesResponseSuccessDigitalIdLvl2(userFromDigitalId, userToDigitalId);
    private static final GetConnectionsResponse getConnectionsByAttributesNameBirthResponseSuccessInitial =
            getConnectionsByAttributesResponseSuccessNameBirthInitial(userFromNameBirth);
    private static GetConnectionsResponse getConnectionsByAttributesNameBirthResponseSuccess =
            getConnectionsByAttributesResponseSuccessNameBirthLvl2(userFromNameBirth, userToNameBirth);
    private final GetConnectionsResponse getConnectionsByAttributesSessionIdResponseSuccessInitial =
            getConnectionsByAttributesResponseSuccessSessionIdInitial(userFromSessionId);
    private static GetConnectionsResponse getConnectionsByAttributesSessionIdResponseSuccess =
            getConnectionsByAttributesResponseSuccessSessionIdLvl2(userFromSessionId, userToSessionId);
    private final GetConnectionsResponse getConnectionsByAttributesWebSessionIdResponseSuccessInitial =
            getConnectionsByAttributesResponseSuccessWebSessionIdInitial(userFromWebSessionId);
    private static GetConnectionsResponse getConnectionsByAttributesWebSessionIdResponseSuccess =
            getConnectionsByAttributesResponseSuccessWebSessionIdLvl2(userFromWebSessionId, userToWebSessionId);

    // Objects to insert to attributes tables
    private static final DocumentTableEntry documentTableEntry =
            documentTableEntryForConnectionSearchRandomized(userFromDocument);
    private static final EmailTableEntry emailTableEntry =
            emailTableEntryForConnectionSearch(userFromEmail, userFromEmail.getEmail());
    private static final IpTableEntry ipTableEntry = ipTableEntryForConnectionSearch(userFromIp, true);
    private static final IpTableEntry ipTableEntry2 = ipTableEntryForConnectionSearch(userFromIp2, true);
    private static final IpTableEntry ipTableEntryFiltration =
            ipTableEntryForConnectionSearch(userFromFiltration, true);
    private static final DigitalIdTableEntry digitalIdTableEntryIp =
            digitalIdTableEntryForConnectionSearch(userFromIp2);
    private static final PhoneTableEntry phoneTableEntry = phoneTableEntryForConnectionSearch(userFromPhone);
    private static final PhoneTableEntry phoneTableEntry2 = phoneTableEntryForConnectionSearch(userToIp3);
    private static final PayoutTableEntry payoutTableEntry = payoutTableEntryForConnectionSearch(userFromPayout);
    private static final EmailTableEntry emailTableEntryFiltration =
            emailTableEntryForConnectionSearchFiltration(userFromFiltration);
    private static final IpTableEntry ipTableEntry3 = ipTableEntryForConnectionSearch(userFromMtCid);
    private static final IpTableEntry ipTableEntry4 =
            ipTableEntryForConnectionSearch(userToMtCid, userFromMtCid.getIpAddress());

    private static final EmailTableEntry emailTableEntry2 = getEmailTableEntryByClient(userFromMtCid2);
    private static final EmailTableEntry emailTableEntry3 =
            emailTableEntryForConnectionSearch(userToMtCid2, userFromMtCid2.getEmail());

    private static final DeviceIdTableEntry deviceIdTableEntry =
            deviceIdTableEntryForConnectionSearch(userFromDeviceId);
    private static final DigitalIdTableEntry digitalIdTableEntry =
            digitalIdTableEntryForConnectionSearch(userFromDigitalId);
    private static final DigitalIdTableEntry digitalIdTableEntryFiltration =
            digitalIdTableEntryForConnectionSearchFiltration(userFromFiltration);
    private static final NameBirthTableEntry nameBirthTableEntry =
            nameBirthTableEntryForConnectionSearch(userFromNameBirth);
    private static final SessionIdTableEntry sessionIdTableEntry =
            sessionIdTableEntryForConnectionSearch(userFromSessionId);
    private static final WebSessionTableEntry webSessionTableEntryFrom =
            webSessionTableEntryForConnectionSearch(userFromWebSessionId);
    private static final WebSessionTableEntry webSessionTableEntryTo =
            webSessionTableEntryForConnectionSearch(userToWebSessionId);
    private static final MtCidTableEntry mtCidTableEntryFrom = mtCidTableEntryForConnectionSearch(userFromMtCid);
    private static final MtCidTableEntry mtCidTableEntryTo =
            mtCidTableEntryForConnectionSearch(userToMtCid, userFromMtCid.getMtCid());

    private static final MtCidTableEntry mtCidTableEntryFrom2 = mtCidTableEntryForConnectionSearch(userFromMtCid2);
    private static final MtCidTableEntry mtCidTableEntryTo2 =
            mtCidTableEntryForConnectionSearch(userToMtCid2, userFromMtCid2.getMtCid());

    // Objects to insert to connections table
    private static final ConnectionTableEntry connectionTableEntryByDocument =
            getConnectionTableEntry(userFromDocument, userToDocument, DOCUMENT);
    private static final ConnectionTableEntry connectionTableEntryByEmail =
            getConnectionTableEntry(userFromEmail, userToEmail, EMAIL);
    private static final ConnectionTableEntry connectionTableEntryByIp =
            getConnectionTableEntry(userFromIp, userToIp, IP);
    private static final ConnectionTableEntry connectionTableEntryByIp2 =
            getConnectionTableEntry(userFromIp2, userToIp2, userFromIp2.getIpAddress());
    private static final ConnectionTableEntry connectionTableEntryByIp3 =
            getConnectionTableEntry(userFromIp2, userToIp3, IP);
    private static final ConnectionTableEntry connectionTableEntryByPhone =
            getConnectionTableEntry(userFromPhone, userToPhone, PHONE);
    private static final ConnectionTableEntry connectionTableEntryByPayout =
            getConnectionTableEntry(userFromPayout, userToPayout, PAYOUT);
    private static final ConnectionTableEntry connectionTableEntryForDepth1 =
            getConnectionTableEntry(userFromDepth, userToDepth1);
    private static final ConnectionTableEntry connectionTableEntryForDepth2 =
            getConnectionTableEntryLvl2(userToDepth1, userToDepth2);
    private static final ConnectionTableEntry connectionTableEntryFiltration1 =
            getConnectionTableEntry(userFromFiltration, userToFiltration1);
    private static final ConnectionTableEntry connectionTableEntryFiltration2 =
            getConnectionTableEntryForFiltration(userToFiltration1, userToFiltration2);
    private static final ConnectionTableEntry connectionTableEntryByDeviceId =
            getConnectionTableEntry(userFromDeviceId, userToDeviceId, ConnectionAttributes.DEVICE);
    private static final ConnectionTableEntry connectionTableEntryByDigitalId =
            getConnectionTableEntry(userFromDigitalId, userToDigitalId, ConnectionAttributes.DIGITAL);
    private static final ConnectionTableEntry connectionTableEntryByNameBirth =
            getConnectionTableEntry(userFromNameBirth, userToNameBirth, NAME_BIRTH);
    private static final ConnectionTableEntry connectionTableEntryBySessionId =
            getConnectionTableEntry(userFromSessionId, userToSessionId, SESSION);
    private static final ConnectionTableEntry connectionTableEntryByWebSessionId =
            getConnectionTableEntry(userFromWebSessionId, userToWebSessionId, WEB_SESSION);

    @BeforeAll
    static void setupConnectionTableEntry() throws Exception {
        getConnectionsByAttributesDigitalIdResponseSuccessInitial.connectionStrengthToInitial = 0.699_999_988_079_071;
        getConnectionsByAttributesDigitalIdResponseSuccessInitial.connectionScoreToInitial =
                getConnectionsByAttributesDigitalIdResponseSuccessInitial.connectionStrengthToInitial;
        getConnectionsByAttributesNameBirthResponseSuccessInitial.connectionStrengthToInitial = 0.800_000_011_920_929;
        getConnectionsByAttributesNameBirthResponseSuccessInitial.connectionScoreToInitial =
                getConnectionsByAttributesNameBirthResponseSuccessInitial.connectionStrengthToInitial;
        // Insert data to attributes tables
        insertObjectToDb(DOCUMENT_TABLE_NAME, documentTableEntry);
        insertObjectsToDb(EMAIL_TABLE_NAME, List.of(emailTableEntry, emailTableEntry3, emailTableEntry2));
        insertObjectsToDb(IP_TABLE_NAME, List.of(ipTableEntry, ipTableEntry2, ipTableEntry3, ipTableEntry4));
        insertObjectsToDb(PHONE_TABLE_NAME, List.of(phoneTableEntry, phoneTableEntry2));
        insertObjectToDb(PAYOUT_TABLE_NAME, payoutTableEntry);
        insertObjectToDb(EMAIL_TABLE_NAME, emailTableEntryFiltration);
        insertObjectsToDb(
                DIGITAL_ID_TABLE_NAME,
                List.of(digitalIdTableEntry, digitalIdTableEntryIp, digitalIdTableEntryFiltration));
        insertObjectToDb(DEVICE_ID_TABLE_NAME, deviceIdTableEntry);
        insertObjectToDb(SESSION_ID_TABLE_NAME, sessionIdTableEntry);
        insertObjectToDb(NAME_BIRTH_TABLE_NAME, nameBirthTableEntry);
        insertObjectsToDb(WEB_SESSION_TABLE_NAME, List.of(webSessionTableEntryFrom, webSessionTableEntryTo));
        insertObjectsToDb(
                MT_CID_TABLE_NAME,
                List.of(mtCidTableEntryFrom, mtCidTableEntryTo, mtCidTableEntryFrom2, mtCidTableEntryTo2));
        // Insert data to connections table
        insertConnectionToDb(
                connectionTableEntryByDocument,
                connectionTableEntryByEmail,
                connectionTableEntryByIp,
                connectionTableEntryByIp2,
                connectionTableEntryByIp3,
                connectionTableEntryByPhone,
                connectionTableEntryByPayout,
                connectionTableEntryForDepth1,
                connectionTableEntryForDepth2,
                connectionTableEntryFiltration1,
                connectionTableEntryFiltration2,
                connectionTableEntryByDeviceId,
                connectionTableEntryByDigitalId,
                connectionTableEntryByNameBirth,
                connectionTableEntryBySessionId,
                connectionTableEntryByWebSessionId);
        waitForConnectionSearchToUpdate();
        // pause for async services like CS and AR always set up connections last and use
        // waitForConnectionSearchToUpdate() before this wait.
        Thread.sleep(60_000);
    }

    @AfterAll
    static void deleteConnectionTableEntry() throws Exception {
        // Delete data from connections table
        cleanConnectionsTableByClient(
                connectionTableEntryByDocument.userFrom,
                connectionTableEntryByEmail.userFrom,
                connectionTableEntryByIp.userFrom,
                connectionTableEntryByPhone.userFrom,
                connectionTableEntryByPayout.userFrom,
                connectionTableEntryForDepth1.userFrom,
                connectionTableEntryForDepth2.userFrom,
                connectionTableEntryFiltration1.userFrom,
                connectionTableEntryFiltration2.userFrom,
                connectionTableEntryByDeviceId.userFrom,
                connectionTableEntryByDigitalId.userFrom,
                connectionTableEntryByNameBirth.userFrom,
                connectionTableEntryBySessionId.userFrom);
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
        deleteEntryFromDb(
                WEB_SESSION_TABLE_NAME, String.format("web_session_id = '%s'", webSessionTableEntryFrom.webSessionId));
        deleteEntryFromDb(MT_CID_TABLE_NAME, String.format("mt_cid = '%s'", mtCidTableEntryFrom.getMtCid()));
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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        Allure.step("Get connections with null as client from");
        GetConnectionsResponse connection = Arrays.stream(responseBody)
                .filter(connect -> connect.clientIdFrom == null)
                .findFirst()
                .orElse(null);
        assertNotNull(connection);

        GetConnectionsResponse.ConnectionDetail[] details = connection.connectionDetail;
        // get connection detail documentType
        GetConnectionsResponse.ConnectionDetail documentType = Arrays.stream(details)
                .filter(connect -> "documentType".equals(connect.connectionAttributeName))
                .findFirst()
                .orElse(null);
        assertNotNull(documentType);
        assertEquals(documentTableEntry.accIdType, documentType.connectionAttributeValue);
        assertEquals(userFromDocument.getUcid(), connection.clientIdTo);

        Allure.step("Check value of document number in connection details");
        GetConnectionsResponse.ConnectionDetail documentNumber = Arrays.stream(details)
                .filter(connect -> "documentNumber".equals(connect.connectionAttributeName))
                .findFirst()
                .orElse(null);
        assertNotNull(documentNumber);
        assertEquals(documentTableEntry.accIdNum, documentNumber.connectionAttributeValue);
        assertEquals(userFromDocument.getUcid(), connection.clientIdTo);

        Allure.step("Check value of document nation code in connection details");
        GetConnectionsResponse.ConnectionDetail natCode = Arrays.stream(details)
                .filter(connect -> "documentCountryId".equals(connect.connectionAttributeName))
                .findFirst()
                .orElse(null);
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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));

        Allure.step("Get connections with null as client from");
        GetConnectionsResponse connection = Arrays.stream(responseBody)
                .filter(connect -> connect.clientIdFrom == null)
                .findFirst()
                .orElse(null);
        assertNotNull(connection);

        GetConnectionsResponse.ConnectionDetail[] details = connection.connectionDetail;
        // get connection detail documentType
        GetConnectionsResponse.ConnectionDetail documentType = Arrays.stream(details)
                .filter(connect -> "documentType".equals(connect.connectionAttributeName))
                .findFirst()
                .orElse(null);
        assertNotNull(documentType);
        assertEquals(documentTableEntry.accIdType, documentType.connectionAttributeValue);
        assertEquals(userFromDocument.getUcid(), connection.clientIdTo);

        Allure.step("Check value of document number in connection details");
        GetConnectionsResponse.ConnectionDetail documentNumber = Arrays.stream(details)
                .filter(connect -> "documentNumber".equals(connect.connectionAttributeName))
                .findFirst()
                .orElse(null);
        assertNotNull(documentNumber);
        assertEquals(documentTableEntry.accIdNum, documentNumber.connectionAttributeValue);
        assertEquals(userFromDocument.getUcid(), connection.clientIdTo);

        Allure.step("Check value of document nation code in connection details");
        GetConnectionsResponse.ConnectionDetail natCode = Arrays.stream(details)
                .filter(connect -> "documentCountryId".equals(connect.connectionAttributeName))
                .findFirst()
                .orElse(null);
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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesEmailResponseSuccess));
        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesEmailResponseSuccessInitial));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get empty response by only one ip(200)")
    @AllureId("191")
    void getConnectionsTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ipAddress", ipTableEntry2.ip);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesPhoneResponseSuccess));
        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesPhoneResponseSuccessInitial));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by payout success(200)")
    @AllureId("193")
    void getConnectionsTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("payoutId", payoutTableEntry.payout);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesPayoutResponseSuccess));
        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesPayoutResponseSuccessInitial));
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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(8));

        assertThat(
                "Check that response body has object found by document",
                responseBody,
                hasItemInArray(getConnectionsByAttributesDocumentResponseSuccess));
        assertThat(
                "Check that response body has object found by document",
                responseBody,
                hasItemInArray(getConnectionsByAttributesDocumentResponseSuccessInitial));
        assertThat(
                "Check that response body has object found by emailAddress",
                responseBody,
                hasItemInArray(getConnectionsByAttributesEmailResponseSuccess));
        assertThat(
                "Check that response body has object found by emailAddress",
                responseBody,
                hasItemInArray(getConnectionsByAttributesEmailResponseSuccessInitial));
        assertThat(
                "Check that response body has object found by phoneNumber",
                responseBody,
                hasItemInArray(getConnectionsByAttributesPhoneResponseSuccess));
        assertThat(
                "Check that response body has object found by phoneNumber",
                responseBody,
                hasItemInArray(getConnectionsByAttributesPhoneResponseSuccessInitial));
        assertThat(
                "Check that response body has object found by payoutId",
                responseBody,
                hasItemInArray(getConnectionsByAttributesPayoutResponseSuccess));
        assertThat(
                "Check that response body has object found by payoutId",
                responseBody,
                hasItemInArray(getConnectionsByAttributesPayoutResponseSuccessInitial));
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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat(
                "Check the response body",
                responseBody,
                arrayContainingInAnyOrder(
                        getConnectionsByAttributesFiltrationResponseSuccess[0],
                        getConnectionsByAttributesFiltrationResponseSuccess[1]));
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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));

        assertThat(
                "Check the response body",
                responseBody[0],
                equalTo(getConnectionsByAttributesFiltrationResponseSuccess[2]));
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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

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
            if (connectFrom.clientIdFrom != null
                    && !("null".equals(connectFrom.clientIdFrom))
                    && connectFrom.clientIdFrom.equals(userFromFiltration.getUcid())) {
                break;
            }
        }

        assertThat(
                "Check the response body",
                connectNull,
                equalTo(getConnectionsByAttributesFiltrationResponseSuccess[0]));
        assertThat(
                "Check the response body",
                connectFrom,
                equalTo(getConnectionsByAttributesFiltrationResponseSuccess[1]));
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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponseError.class);

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat(
                "Check the response body", responseBody, equalTo(getConnectionsResponseErrorDocumentCountryIdNotInt()));
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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponse[].class);

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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponseError.class);

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorNoSearchParameters()));
    }

    @Test
    @DisplayName(
            "Connection search by attributes Api. Get connection with connectionScoreFrom not int Bad Request(400)")
    @AllureId("470")
    void getConnectionsTest16() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", emailTableEntryFiltration.email);
        queryParams.put("connectionScoreFrom", "test");

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponseError.class);

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat(
                "Check the response body",
                responseBody,
                equalTo(getConnectionsByAttributesResponseErrorConnectionScoreFromBadRequest()));
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
                Objects.requireNonNull(response.body()).string(), GetConnectionsResponseError.class);

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat(
                "Check the response body",
                responseBody,
                equalTo(getConnectionsByAttributesResponseErrorConnectionScoreToBadRequest()));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by device id success(200)")
    @AllureId("695")
    void getConnectionsTest18() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("device", deviceIdTableEntry.deviceId);

        Response response = getConnectionsByAttributes(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetConnectionsResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesDeviceIdResponseSuccessInitial));
        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesDeviceIdResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by digital id success(200)")
    @AllureId("696")
    void getConnectionsTest19() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("digital", digitalIdTableEntry.digitalId);

        Response response = getConnectionsByAttributes(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetConnectionsResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesDigitalIdResponseSuccessInitial));
        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesDigitalIdResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by name birth success(200)")
    @AllureId("697")
    void getConnectionsTest20() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("nameBirth", userFromNameBirth.getNameDateOfBirth());

        Response response = getConnectionsByAttributes(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetConnectionsResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesNameBirthResponseSuccess));
        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesNameBirthResponseSuccessInitial));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by session id success(200)")
    @AllureId("698")
    void getConnectionsTest21() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("session", sessionIdTableEntry.sessionId);

        Response response = getConnectionsByAttributes(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetConnectionsResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesSessionIdResponseSuccess));
        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesSessionIdResponseSuccessInitial));
    }

    @Test
    @DisplayName("Connection search by attributes Api. Get connection by web session id success(200)")
    @AllureId("699")
    void getConnectionsTest22() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("webSession", webSessionTableEntryFrom.webSessionId);

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesWebSessionIdResponseSuccess));
        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesWebSessionIdResponseSuccessInitial));
    }

    @Test
    @AllureId("1552")
    @DisplayName("Connection search by attributes Api. Get connection by mt cid and ip success(200)")
    void getConnectionsTest24() throws IOException, InterruptedException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ipAddress", userFromMtCid.getIpAddress());
        queryParams.put("mtCid", userFromMtCid.getMtCid());

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));

        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(
                        getConnectionsByAttributesResponseSuccessMtCidLvl1(userFromMtCid, userToMtCid.getUcid())));
    }

    @Test
    @AllureId("1552")
    @DisplayName("Connection search by attributes Api. Get connection by mt cid and email success(200)")
    void getConnectionsTest25() throws IOException, InterruptedException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", userFromMtCid2.getEmail());
        queryParams.put("mtCid", userFromMtCid2.getMtCid());

        Response response = getConnectionsByAttributes(queryParams);
        GetConnectionsResponse[] responseBody =
                objectMapper.readValue(response.body().string(), GetConnectionsResponse[].class);

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesResponseSuccessMtCidEmailLvl1(
                        userFromMtCid2, userToMtCid2.getUcid())));
        assertThat(
                "Check the response body",
                responseBody,
                hasItemInArray(getConnectionsByAttributesResponseSuccessMtCidEmailLvl1(
                        userFromMtCid2, userFromMtCid2.getUcid())));
    }
}
