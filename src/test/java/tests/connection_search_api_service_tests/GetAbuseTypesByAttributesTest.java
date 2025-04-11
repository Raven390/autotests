package tests.connection_search_api_service_tests;

import business_objects.api.connection_search_api.ConnectionSearchResponseError;
import business_objects.api.connection_search_api.get_abuse_types.GetAbuseTypesResponse;
import business_objects.db.clickhouse.bo_client_fraud_types.ClientFraudTypesObject;
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

import static business_objects.api.connection_search_api.get_abuse_types.GetAbuseTypesRequest.getAbuseTypesByAttributes;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.getConnectionTableEntry;
import static business_objects.db.clickhouse.device_id_table.DeviceIdTableEntryFactory.deviceIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.digital_id_table.DigitalIdTableEntryFactory.digitalIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.document_table.DocumentTableEntryFactory.documentTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.email_table.EmailTableEntryFactory.*;
import static business_objects.db.clickhouse.ip_table.IpTableEntryFactory.ipTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.name_birth.NameBirthTableEntryFactory.nameBirthTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.payout.PayoutTableEntryFactory.payoutTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.phone.PhoneTableEntryFactory.phoneTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.session_id.SessionIdTableEntryFactory.sessionIdTableEntryForConnectionSearch;
import static business_objects.db.clickhouse.web_session.WebSessionTableEntryFactory.webSessionTableEntryForConnectionSearch;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.*;
import static helpers.database.CleanTableHelper.cleanBoFraudTypesTableByUcid;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_ATTRIBUTES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
class GetAbuseTypesByAttributesTest extends TestBaseApi {

    // Users
    static final ClientHelper userFromDocument = getRandomVantageClient();
    static final ClientHelper userToDocument = getRandomVantageClient();

    static final ClientHelper userFromEmail = getRandomVantageClientAllFields();
    static final ClientHelper userToEmail = getRandomVantageClientAllFields();

    static final ClientHelper userFromIp = getRandomVantageClient();
    static final ClientHelper userToIp = getRandomVantageClient();

    static final ClientHelper userFromPhone = getRandomVantageClientAllFields();
    static final ClientHelper userToPhone = getRandomVantageClientAllFields();

    static final ClientHelper userFromPayout = getRandomVantageClient();
    static final ClientHelper userToPayout = getRandomVantageClient();

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

    static final ClientHelper userFromDepth = getRandomVantageClientAllFields();
    static final ClientHelper userToDepth = getRandomVantageClientAllFields();
    static final ClientHelper userTo2Depth = getRandomVantageClientAllFields();
    static final ClientHelper userTo3Depth = getRandomVantageClientAllFields();

    // Objects to insert to attributes tables
    static final DocumentTableEntry documentTableEntry = documentTableEntryForConnectionSearch(userFromDocument);
    static final DocumentTableEntry documentTableEntry2 = documentTableEntryForConnectionSearch(userToDocument);

    static final EmailTableEntry emailTableEntry = getEmailTableEntryByClient(userFromEmail);
    static final EmailTableEntry emailTableEntry2 = emailTableEntryForConnectionSearch(userToEmail, userFromEmail.getEmail());

    static final EmailTableEntry emailTableEntryForDepth1 = emailTableEntryForConnectionSearch(userFromDepth);

    static final IpTableEntry ipTableEntry = ipTableEntryForConnectionSearch(userFromIp);
    static final IpTableEntry ipTableEntry2 = ipTableEntryForConnectionSearch(userToIp);

    static final PhoneTableEntry phoneTableEntry = phoneTableEntryForConnectionSearch(userFromPhone);
    static final PhoneTableEntry phoneTableEntry2 = phoneTableEntryForConnectionSearch(userToPhone);

    static final PayoutTableEntry payoutTableEntry = payoutTableEntryForConnectionSearch(userFromPayout);
    static final PayoutTableEntry payoutTableEntry2 = payoutTableEntryForConnectionSearch(userToPayout);

    static final DeviceIdTableEntry deviceIdTableEntry = deviceIdTableEntryForConnectionSearch(userFromDeviceId);
    static final DeviceIdTableEntry deviceIdTableEntry2 = deviceIdTableEntryForConnectionSearch(userToDeviceId, userFromDeviceId.getDeviceId());

    static final DigitalIdTableEntry digitalIdTableEntry = digitalIdTableEntryForConnectionSearch(userFromDigitalId);
    static final DigitalIdTableEntry digitalIdTableEntry2 = digitalIdTableEntryForConnectionSearch(userToDigitalId, userFromDigitalId.getDigitalId());

    static final NameBirthTableEntry nameBirthTableEntry = nameBirthTableEntryForConnectionSearch(userFromNameBirth);
    static final NameBirthTableEntry nameBirthTableEntry2 = nameBirthTableEntryForConnectionSearch(userToNameBirth, userFromNameBirth.getFirstName(), userFromNameBirth.getLastName(), userFromNameBirth.getDateOfBirth());

    static final SessionIdTableEntry sessionIdTableEntry = sessionIdTableEntryForConnectionSearch(userFromSessionId);
    static final SessionIdTableEntry sessionIdTableEntry2 = sessionIdTableEntryForConnectionSearch(userToSessionId, userFromSessionId.getSessionId());

    static final WebSessionTableEntry webSessionTableEntry = webSessionTableEntryForConnectionSearch(userFromWebSessionId);
    static final WebSessionTableEntry webSessionTableEntry2 = webSessionTableEntryForConnectionSearch(userToWebSessionId, userFromWebSessionId.getWebSessionId());

    // Objects to insert to connections table
    static final ConnectionTableEntry connectionTableEntryByEmail1 = getConnectionTableEntry(userFromDepth, userToDepth);
    static final ConnectionTableEntry connectionTableEntryByEmail2 = getConnectionTableEntry(userToDepth, userTo2Depth);
    static final ConnectionTableEntry connectionTableEntryByEmail3 = getConnectionTableEntry(userTo2Depth, userTo3Depth);

    // Frauds
    private static final ClientFraudTypesObject fraudEmailTo = new ClientFraudTypesObject(userToEmail.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudEmail1 = new ClientFraudTypesObject(userFromDepth.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudEmail2 = new ClientFraudTypesObject(userTo2Depth.getUcid(), CPA_ABUSE.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudEmail3 = new ClientFraudTypesObject(userTo3Depth.getUcid(), LOSS_VOUCHER_ABUSE.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudDocumentTo = new ClientFraudTypesObject(userToDocument.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudIpTo = new ClientFraudTypesObject(userToIp.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudPhoneTo = new ClientFraudTypesObject(userToPhone.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudPhoneFrom = new ClientFraudTypesObject(userFromPhone.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudPayoutTo = new ClientFraudTypesObject(userToPayout.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudDeviceIdTo = new ClientFraudTypesObject(userToDeviceId.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudDigitalIdTo = new ClientFraudTypesObject(userToDigitalId.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudNameBirthTo = new ClientFraudTypesObject(userToNameBirth.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudSessionIdTo = new ClientFraudTypesObject(userToSessionId.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypesObject fraudWebSessionIdTo = new ClientFraudTypesObject(userToWebSessionId.getUcid(), HEDGING.getDisplayName(), "VINDEX", 0, getCurrentTimestampDbFormat());

    @BeforeAll
    static void setupConnectionTableEntry() throws InterruptedException {
        // Insert data to connections table
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntryByEmail1, connectionTableEntryByEmail2, connectionTableEntryByEmail3));
        // Insert data to attributes tables
        insertObjectsToDb(DOCUMENT_TABLE_NAME, List.of(documentTableEntry, documentTableEntry2));
        insertObjectsToDb(EMAIL_TABLE_NAME, List.of(emailTableEntry, emailTableEntryForDepth1, emailTableEntry2));
        insertObjectsToDb(IP_TABLE_NAME, List.of(ipTableEntry, ipTableEntry2));
        insertObjectsToDb(PHONE_TABLE_NAME, List.of(phoneTableEntry, phoneTableEntry2));
        insertObjectsToDb(PAYOUT_TABLE_NAME, List.of(payoutTableEntry, payoutTableEntry2));
        insertObjectsToDb(DIGITAL_ID_TABLE_NAME, List.of(digitalIdTableEntry, digitalIdTableEntry2));
        insertObjectsToDb(DEVICE_ID_TABLE_NAME, List.of(deviceIdTableEntry, deviceIdTableEntry2));
        insertObjectsToDb(SESSION_ID_TABLE_NAME, List.of(sessionIdTableEntry, sessionIdTableEntry2));
        insertObjectsToDb(NAME_BIRTH_TABLE_NAME, List.of(nameBirthTableEntry, nameBirthTableEntry2));
        insertObjectsToDb(WEB_SESSION_TABLE_NAME, List.of(webSessionTableEntry, webSessionTableEntry2));
        //insert data to fraud table
        insertObjectsToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, List.of(fraudPhoneFrom, fraudEmail1, fraudEmail2, fraudEmail3, fraudDocumentTo, fraudEmailTo, fraudIpTo, fraudPhoneTo, fraudPayoutTo, fraudDeviceIdTo, fraudDigitalIdTo, fraudNameBirthTo, fraudSessionIdTo, fraudWebSessionIdTo));
    }

    @AfterAll
    static void deleteConnectionTableEntry() throws Exception {
        // Delete data from connections table
        //deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByDocument.userFrom));

        //Delete data from attributes tables
        deleteEntryFromDb(DOCUMENT_TABLE_NAME, String.format("acc_id_num = '%s'", documentTableEntry.accIdNum));
        deleteEntryFromDb(DOCUMENT_TABLE_NAME, String.format("acc_id_num = '%s'", documentTableEntry2.accIdNum));
        deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("email = '%s'", emailTableEntry.email));
        deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("email = '%s'", emailTableEntry2.email));
        deleteEntryFromDb(IP_TABLE_NAME, String.format("ip = '%s'", ipTableEntry.ip));
        deleteEntryFromDb(IP_TABLE_NAME, String.format("ip = '%s'", ipTableEntry2.ip));
        deleteEntryFromDb(PHONE_TABLE_NAME, String.format("phone_num = '%s'", phoneTableEntry.phoneNum));
        deleteEntryFromDb(PHONE_TABLE_NAME, String.format("phone_num = '%s'", phoneTableEntry2.phoneNum));
        deleteEntryFromDb(PAYOUT_TABLE_NAME, String.format("payout = '%s'", payoutTableEntry.payout));
        deleteEntryFromDb(PAYOUT_TABLE_NAME, String.format("payout = '%s'", payoutTableEntry2.payout));
        deleteEntryFromDb(DEVICE_ID_TABLE_NAME, String.format("device_id = '%s'", deviceIdTableEntry.deviceId));
        deleteEntryFromDb(DEVICE_ID_TABLE_NAME, String.format("device_id = '%s'", deviceIdTableEntry2.deviceId));
        deleteEntryFromDb(DIGITAL_ID_TABLE_NAME, String.format("digital_id = '%s'", digitalIdTableEntry.digitalId));
        deleteEntryFromDb(DIGITAL_ID_TABLE_NAME, String.format("digital_id = '%s'", digitalIdTableEntry2.digitalId));
        deleteEntryFromDb(NAME_BIRTH_TABLE_NAME, String.format("ucid = '%s'", nameBirthTableEntry.ucid));
        deleteEntryFromDb(NAME_BIRTH_TABLE_NAME, String.format("ucid = '%s'", nameBirthTableEntry2.ucid));
        deleteEntryFromDb(SESSION_ID_TABLE_NAME, String.format("session_id = '%s'", sessionIdTableEntry.sessionId));
        deleteEntryFromDb(SESSION_ID_TABLE_NAME, String.format("session_id = '%s'", sessionIdTableEntry2.sessionId));
        deleteEntryFromDb(WEB_SESSION_TABLE_NAME, String.format("web_session_id = '%s'", webSessionTableEntry.webSessionId));
        deleteEntryFromDb(WEB_SESSION_TABLE_NAME, String.format("web_session_id = '%s'", webSessionTableEntry2.webSessionId));
        //Delete data from fraud type table
        cleanBoFraudTypesTableByUcid(userFromEmail.getUcid(), userToEmail.getUcid(), userFromDocument.getUcid(), userToDocument.getUcid(), userFromIp.getUcid(), userToIp.getUcid(), userFromPayout.getUcid(), userToPayout.getUcid(), userFromPhone.getUcid(), userToPhone.getUcid(), userFromNameBirth.getUcid(), userToNameBirth.getUcid(), userToDeviceId.getUcid(), userFromDeviceId.getUcid(), userToSessionId.getUcid(), userFromSessionId.getUcid(), userToWebSessionId.getUcid(), userFromWebSessionId.getUcid());
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by emailAddress success(200)")
    @AllureId("769")
    void getAbuseTypesByAttributesTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", userFromEmail.getEmail());

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response length", responseBody.length, is(1));
        assertThat("Check abuse type", responseBody[0].abuseType, is(HEDGING.getDisplayName()));
        assertThat("Check maxScoreToInitial", responseBody[0].maxScoreToInitial, is(0.5));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by payoutId success(200)")
    @AllureId("770")
    void getAbuseTypesByAttributesTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("payoutId", payoutTableEntry.payout);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response length", responseBody.length, is(1));
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getDisplayName()));
        assertThat("Check maxScoreToInitial", responseBody[0].maxScoreToInitial, is(1.0));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by digital success(200)")
    @AllureId("771")
    void getAbuseTypesByAttributesTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("digital", digitalIdTableEntry.digitalId);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response length", responseBody.length, is(1));
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getDisplayName()));
        assertThat("Check maxScoreToInitial", responseBody[0].maxScoreToInitial, is(0.699_999_988_079_071));

    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by device success(200)")
    @AllureId("772")
    void getAbuseTypesByAttributesTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("device", deviceIdTableEntry.deviceId);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response length", responseBody.length, is(1));
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getDisplayName()));
        assertThat("Check maxScoreToInitial", responseBody[0].maxScoreToInitial, is(0.699_999_988_079_071));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by session success(200)")
    @AllureId("773")
    void getAbuseTypesByAttributesTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("session", sessionIdTableEntry.sessionId);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response length", responseBody.length, is(1));
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getDisplayName()));
        assertThat("Check maxScoreToInitial", responseBody[0].maxScoreToInitial, is(1.0));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by nameBirth success(200)")
    @AllureId("774")
    void getAbuseTypesByAttributesTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("nameBirth", userFromNameBirth.getNameDateOfBirth());

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response length", responseBody.length, is(1));
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getDisplayName()));
        assertThat("Check maxScoreToInitial", responseBody[0].maxScoreToInitial, is(0.800_000_011_920_929));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by webSession success(200)")
    @AllureId("775")
    void getAbuseTypesByAttributesTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("webSession", webSessionTableEntry.webSessionId);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response length", responseBody.length, is(1));
        assertThat("Check abuseTYpe", responseBody[0].abuseType, is(HEDGING.getDisplayName()));
        assertThat("Check maxScoreToInitial", responseBody[0].maxScoreToInitial, is(1.0));

    }

    //TODO Fix after https://vantagefx-hytechs.atlassian.net/browse/CSV-788
    @Disabled
    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by phoneNumber success(200)")
    @AllureId("776")
    void getAbuseTypesByAttributesTest8() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("phoneNumber", phoneTableEntry.phoneNum);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response length", responseBody.length, is(1));
        assertThat("Check the response body element", responseBody[0].abuseType, is(HEDGING.getDisplayName()));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by ipAddress success(200)")
    @AllureId("777")
    void getAbuseTypesByAttributesTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ipAddress", ipTableEntry.ip);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response length", responseBody.length, is(1));
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getDisplayName()));
        assertThat("Check maxScoreToInitial", responseBody[0].maxScoreToInitial, is(0.200_000_002_980_232_24));

    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by documentType/documentNumber/documentCountryId success(200)")
    @AllureId("778")
    void getAbuseTypesByAttributesTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response length", responseBody.length, is(1));
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getDisplayName()));
        assertThat("Check maxScoreToInitial", responseBody[0].maxScoreToInitial, is(1.0));

    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by documentCountryId success(200)")
    @AllureId("779")
    void getAbuseTypesByAttributesTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();

        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat("Check the response length", responseBody.status, is(400));
        assertThat("Check the response length", responseBody.error, is("DocumentType must be specified once DocumentNumber or DocumentCountryId provided"));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by documentNumber success(200)")
    @AllureId("780")
    void getAbuseTypesByAttributesTest12() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentNumber", documentTableEntry.accIdNum);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat("Check the response length", responseBody.status, is(400));
        assertThat("Check the response length", responseBody.error, is("DocumentType must be specified once DocumentNumber or DocumentCountryId provided"));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by documentNumber and documentCountryId success(200)")
    @AllureId("781")
    void getAbuseTypesByAttributesTest13() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentNumber", documentTableEntry.accIdNum);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat("Check the response length", responseBody.status, is(400));
        assertThat("Check the response length", responseBody.error, is("DocumentType must be specified once DocumentNumber or DocumentCountryId provided"));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by documentType success(200)")
    @AllureId("782")
    void getAbuseTypesByAttributesTest14() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat("Check the response length", responseBody.status, is(400));
        assertThat("Check the response length", responseBody.error, is("DocumentNumber must be specified once DocumentType or DocumentCountryId provided"));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by documentType and documentCountryId success(200)")
    @AllureId("783")
    void getAbuseTypesByAttributesTest15() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentCountryId", documentTableEntry.nationalityId);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat("Check the response length", responseBody.status, is(400));
        assertThat("Check the response length", responseBody.error, is("DocumentNumber must be specified once DocumentType or DocumentCountryId provided"));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by documentType and documentNumber success(200)")
    @AllureId("784")
    void getAbuseTypesByAttributesTest16() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("documentType", documentTableEntry.accIdType);
        queryParams.put("documentNumber", documentTableEntry.accIdNum);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat("Check the response length", responseBody.status, is(400));
        assertThat("Check the response length", responseBody.error, is("DocumentCountryId must be specified once DocumentType or DocumentNumber provided"));
    }

    @Disabled("Fix after CSV-707")
    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by emailAddress and connectionDepth success(200)")
    @AllureId("785")
    void getAbuseTypesByAttributesTest17() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        System.out.println(userFromDepth.getUcid());

        queryParams.put("emailAddress", userFromDepth.getEmail());
        queryParams.put("connectionDepth", 1);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
    }

    @Disabled("Fix after CSV-707")
    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by emailAddress and connectionScoreTo success(200)")
    @AllureId("786")
    void getAbuseTypesByAttributesTest18() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", userFromDepth.getEmail());
        queryParams.put("connectionScoreTo", 1);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
    }

    @Disabled("Fix after CSV-707")
    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by emailAddress and connectionScoreFrom success(200)")
    @AllureId("787")
    void getAbuseTypesByAttributesTest19() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        System.out.println(userFromDepth.getUcid());

        queryParams.put("emailAddress", userFromDepth.getEmail());
        queryParams.put("connectionScoreFrom", 1);

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
    }

    @Disabled("Fix after CSV-707")
    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by emailAddress and connectionType success(200)")
    @AllureId("788")
    void getAbuseTypesByAttributesTest20() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", userFromDepth.getEmail());
        queryParams.put("connectionType", List.of("Same Network"));

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
    }

    @Disabled("Fix after CSV-707")
    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by emailAddress and multiple connectionType success(200)")
    @AllureId("789")
    void getAbuseTypesByAttributesTest21() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", userFromDepth.getEmail());
        queryParams.put("connectionType", List.of("", ""));

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
    }

}
