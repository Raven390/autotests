package tests.connection_search_api_service_tests;

import business_objects.api.connection_search_api.ConnectionSearchResponseError;
import business_objects.api.connection_search_api.get_abuse_types.GetAbuseTypesResponse;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
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
import java.util.*;

import static business_objects.api.connection_search_api.get_abuse_types.GetAbuseTypesRequest.getAbuseTypesByAttributes;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.getConnectionTableEntry;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClients;
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
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudTypeOld.*;
import static helpers.database.BoHelper.deleteUserAR;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.waitForConnectionSearchToUpdate;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_ATTRIBUTES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
class GetAbuseTypesByAttributesTest extends TestBaseApi {

    // Users
    static final ClientHelper userFromDocument = getRandomVantageClientAllFields();
    static final ClientHelper userToDocument = getRandomVantageClientAllFields();

    static final ClientHelper userFromEmail = getRandomVantageClientAllFields();
    static final ClientHelper userToEmail = getRandomVantageClientAllFields();
    static final ClientHelper userToEmail2 = getRandomVantageClientAllFields();

    static final ClientHelper userFromIp = getRandomVantageClientAllFields();
    static final ClientHelper userToIp = getRandomVantageClientAllFields();

    static final ClientHelper userFromIp2 = getRandomVantageClientAllFields();
    static final ClientHelper userToIp2 = getRandomVantageClientAllFields();

    static final ClientHelper userFrom3 = getRandomVantageClientAllFields();
    static final ClientHelper userTo31 = getRandomVantageClientAllFields();
    static final ClientHelper userTo32 = getRandomVantageClientAllFields();

    static final ClientHelper userFromPhone = getRandomVantageClientAllFields();
    static final ClientHelper userToPhone = getRandomVantageClientAllFields();

    static final ClientHelper userFromPayout = getRandomVantageClientAllFields();
    static final ClientHelper userToPayout = getRandomVantageClientAllFields();

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

    static final IpTableEntry ipTableEntry3 = ipTableEntryForConnectionSearch(userFromIp2);
    static final IpTableEntry ipTableEntry4 = ipTableEntryForConnectionSearch(userToIp2);
    static final IpTableEntry ipTableEntry5 = ipTableEntryForConnectionSearch(userTo31);
    static final IpTableEntry ipTableEntry6 = ipTableEntryForConnectionSearch(userToEmail2);

    static final PhoneTableEntry phoneTableEntry = phoneTableEntryForConnectionSearch(userFromPhone);
    static final PhoneTableEntry phoneTableEntry2 = phoneTableEntryForConnectionSearch(userToPhone);

    static final PayoutTableEntry payoutTableEntry = payoutTableEntryForConnectionSearch(userFromPayout);
    static final PayoutTableEntry payoutTableEntry2 = payoutTableEntryForConnectionSearch(userToPayout);

    static final DeviceIdTableEntry deviceIdTableEntry = deviceIdTableEntryForConnectionSearch(userFromDeviceId);
    static final DeviceIdTableEntry deviceIdTableEntry2 = deviceIdTableEntryForConnectionSearch(userToDeviceId, userFromDeviceId.getDeviceId());
    static final DeviceIdTableEntry deviceIdTableEntry3 = deviceIdTableEntryForConnectionSearch(userToEmail);

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
    static final ConnectionTableEntry connectionTableEntryByEmail4 = getConnectionTableEntry(userFromDepth, userToEmail2);
    static final ConnectionTableEntry connectionTableEntryByIp1 = getConnectionTableEntry(userFromIp2, userToIp2);
    static final ConnectionTableEntry connectionTableEntryByIp2 = getConnectionTableEntry(userFrom3, userTo31);
    static final ConnectionTableEntry connectionTableEntryByIp3 = getConnectionTableEntry(userFrom3, userTo32);

    // Frauds
    private static final ClientFraudTypes fraudEmailTo = new ClientFraudTypes(userToEmail.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudEmail1 = new ClientFraudTypes(userFromDepth.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudEmail2 = new ClientFraudTypes(userTo2Depth.getUcid(), CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudEmail3 = new ClientFraudTypes(userTo3Depth.getUcid(), LOSS_VOUCHER_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudEmail4 = new ClientFraudTypes(userToEmail2.getUcid(), LOSS_VOUCHER_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudDocumentTo = new ClientFraudTypes(userToDocument.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudIpTo = new ClientFraudTypes(userToIp.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudIp2To = new ClientFraudTypes(userToIp2.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudPhoneTo = new ClientFraudTypes(userToPhone.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudPhoneFrom = new ClientFraudTypes(userFromPhone.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudPayoutTo = new ClientFraudTypes(userToPayout.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudDeviceIdTo = new ClientFraudTypes(userToDeviceId.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudDigitalIdTo = new ClientFraudTypes(userToDigitalId.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudNameBirthTo = new ClientFraudTypes(userToNameBirth.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudSessionIdTo = new ClientFraudTypes(userToSessionId.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraudWebSessionIdTo = new ClientFraudTypes(userToWebSessionId.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraud1 = new ClientFraudTypes(userTo31.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
    private static final ClientFraudTypes fraud2 = new ClientFraudTypes(userTo32.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());

    static List<ClientHelper> fraudsters = new ArrayList<>(List.of(userFromDocument, userToDocument, userToEmail, userFromDepth, userTo2Depth, userTo3Depth, userToEmail2, userToIp, userToIp2, userToPhone, userFromPhone, userToPayout, userToDeviceId, userToDigitalId, userToNameBirth, userToSessionId, userToWebSessionId, userTo31, userTo32));
    static final List<CrmTbUserObject> clientsDB = generateUserByClients(fraudsters);


    @BeforeAll
    static void setupConnectionTableEntry() throws Exception {
        // Insert data to connections table
        insertObjectsToDb(CRM_USER_TABLE_NAME, clientsDB);
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntryByEmail1, connectionTableEntryByEmail2, connectionTableEntryByEmail3, connectionTableEntryByEmail4, connectionTableEntryByIp1, connectionTableEntryByIp2, connectionTableEntryByIp3));
        // Insert data to attributes tables
        insertObjectsToDb(DOCUMENT_TABLE_NAME, List.of(documentTableEntry, documentTableEntry2));
        insertObjectsToDb(EMAIL_TABLE_NAME, List.of(emailTableEntry, emailTableEntryForDepth1, emailTableEntry2));
        insertObjectsToDb(IP_TABLE_NAME, List.of(ipTableEntry, ipTableEntry2, ipTableEntry3, ipTableEntry4, ipTableEntry5, ipTableEntry6));
        insertObjectsToDb(PHONE_TABLE_NAME, List.of(phoneTableEntry, phoneTableEntry2));
        insertObjectsToDb(PAYOUT_TABLE_NAME, List.of(payoutTableEntry, payoutTableEntry2));
        insertObjectsToDb(DIGITAL_ID_TABLE_NAME, List.of(digitalIdTableEntry, digitalIdTableEntry2));
        insertObjectsToDb(DEVICE_ID_TABLE_NAME, List.of(deviceIdTableEntry, deviceIdTableEntry2, deviceIdTableEntry3));
        insertObjectsToDb(SESSION_ID_TABLE_NAME, List.of(sessionIdTableEntry, sessionIdTableEntry2));
        insertObjectsToDb(NAME_BIRTH_TABLE_NAME, List.of(nameBirthTableEntry, nameBirthTableEntry2));
        insertObjectsToDb(WEB_SESSION_TABLE_NAME, List.of(webSessionTableEntry, webSessionTableEntry2));
        //insert data to fraud table
        insertObjectsToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, List.of(fraudPhoneFrom, fraudEmail1, fraudEmail2, fraudEmail3, fraudEmail4, fraudDocumentTo, fraudEmailTo, fraudIpTo, fraudIp2To, fraudPhoneTo, fraudPayoutTo, fraudDeviceIdTo, fraudDigitalIdTo, fraudNameBirthTo, fraudSessionIdTo, fraudWebSessionIdTo, fraud1, fraud2));
        waitForConnectionSearchToUpdate();
        addFraudsForClient(fraudPhoneFrom, fraudEmail1, fraudEmail2, fraudEmail3, fraudEmail4, fraudDocumentTo, fraudEmailTo, fraudIpTo, fraudIp2To, fraudPhoneTo, fraudPayoutTo, fraudDeviceIdTo, fraudDigitalIdTo, fraudNameBirthTo, fraudSessionIdTo, fraudWebSessionIdTo, fraud1, fraud2);
        Thread.sleep(15_000);//pause for asink services like CS and AR
    }

    @AfterAll
    static void deleteConnectionTableEntry() throws Exception {
        // Delete data from connections table
        //deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryByDocument.userFrom));

        //Delete data from attributes tables
        cleanEmailTableByClient(userFromEmail.getUcid(), userToEmail.getUcid());
        cleanIpTableByClient(userFromIp.getIpAddress(), userToIp.getIpAddress(), userFromIp2.getIpAddress(), userToIp2.getIpAddress());
        cleanPhoneTableByClient(userToPhone.getPhoneNumber(), userFromPhone.getPhoneNumber());
        cleanDigitalIdTableByClient(userFromDigitalId.getDigitalId(), userToDigitalId.getDigitalId());
        cleanNameTableByClient(userFromNameBirth.getUcid(), userToNameBirth.getUcid());
        cleanSessionIdTableByClient(userFromSessionId.getUcid(), userToSessionId.getUcid());
        cleanWebSessionIdTableByClient(userFromWebSessionId.getWebSessionId(), userToWebSessionId.getWebSessionId());
        cleanDepositsTableByUcid(userFromDeviceId.getDeviceId(), userToDeviceId.getDeviceId());
        deleteEntryFromDb(PAYOUT_TABLE_NAME, String.format("payout = '%s'", payoutTableEntry.payout));
        deleteEntryFromDb(PAYOUT_TABLE_NAME, String.format("payout = '%s'", payoutTableEntry2.payout));
        deleteEntryFromDb(DOCUMENT_TABLE_NAME, String.format("acc_id_num = '%s'", documentTableEntry.accIdNum));
        deleteEntryFromDb(DOCUMENT_TABLE_NAME, String.format("acc_id_num = '%s'", documentTableEntry2.accIdNum));
        //Delete data from fraud type table
        cleanBoFraudTypesTableByUcid(userFromEmail.getUcid(), userToEmail.getUcid(), userFromDocument.getUcid(), userToDocument.getUcid(), userFromIp.getUcid(), userToIp.getUcid(), userFromPayout.getUcid(), userToPayout.getUcid(), userFromPhone.getUcid(), userToPhone.getUcid(), userFromNameBirth.getUcid(), userToNameBirth.getUcid(), userToDeviceId.getUcid(), userFromDeviceId.getUcid(), userToSessionId.getUcid(), userFromSessionId.getUcid(), userToWebSessionId.getUcid(), userFromWebSessionId.getUcid());
        //delete client objects from DB
        List<String> clientUcids = new java.util.ArrayList<>(List.of());
        for (CrmTbUserObject client : clientsDB) {
            clientUcids.add(client.ucid);
        }
        cleanCrmUserTableByClient(String.valueOf(clientUcids));
        deleteUserAR(String.valueOf(clientUcids));
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
        assertThat("Check abuse type", responseBody[0].abuseType, is(HEDGING.getKey()));
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
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getKey()));
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
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getKey()));
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
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getKey()));
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
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getKey()));
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
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getKey()));
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
        assertThat("Check abuseTYpe", responseBody[0].abuseType, is(HEDGING.getKey()));
        assertThat("Check maxScoreToInitial", responseBody[0].maxScoreToInitial, is(1.0));

    }

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
        assertThat("Check the response body element", responseBody[0].abuseType, is(HEDGING.getKey()));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get empty response for ip connection")
    @AllureId("1143")
    void getAbuseTypesByAttributesTest22() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ipAddress", userToIp2.getIpAddress());

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response length", responseBody.length, is(0));
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
        assertThat("Check abuseType", responseBody[0].abuseType, is(HEDGING.getKey()));
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

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by emailAddress and multiple connectionType success(200)")
    @AllureId("789")
    void getAbuseTypesByAttributesTest21() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", userFromDepth.getEmail());
        queryParams.put("connectionType", List.of("", ""));

        Response response = getAbuseTypesByAttributes(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat("Check the response length", responseBody.status, is(400));
        assertThat("Check the response length", responseBody.error, is("Unknown connection type provided: . Valid values are: [Same Identity, Same Person, Same Network, UNKNOWN]"));
    }
}
