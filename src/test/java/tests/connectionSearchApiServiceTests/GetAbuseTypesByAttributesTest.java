package tests.connectionSearchApiServiceTests;

import businessObjects.api.connectionSearchApi.ConnectionSearchResponseError;
import businessObjects.api.connectionSearchApi.getAbuseTypes.GetAbuseTypesResponse;
import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
import businessObjects.db.clickhouse.deviceIdTable.DeviceIdTableEntry;
import businessObjects.db.clickhouse.digitalIdTable.DigitalIdTableEntry;
import businessObjects.db.clickhouse.documentTable.DocumentTableEntry;
import businessObjects.db.clickhouse.emailTable.EmailTableEntry;
import businessObjects.db.clickhouse.ipTable.IpTableEntry;
import businessObjects.db.clickhouse.nameBirth.NameBirthTableEntry;
import businessObjects.db.clickhouse.payout.PayoutTableEntry;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.connectionSearchApi.getAbuseTypes.GetAbuseTypesRequest.getAbuseTypesByAttributes;
import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.*;
import static businessObjects.db.clickhouse.deviceIdTable.DeviceIdTableEntryFactory.deviceIdTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.digitalIdTable.DigitalIdTableEntryFactory.digitalIdTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.documentTable.DocumentTableEntryFactory.documentTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.emailTable.EmailTableEntryFactory.*;
import static businessObjects.db.clickhouse.ipTable.IpTableEntryFactory.ipTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.nameBirth.NameBirthTableEntryFactory.nameBirthTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.payout.PayoutTableEntryFactory.payoutTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.phone.PhoneTableEntryFactory.phoneTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.sessionId.SessionIdTableEntryFactory.sessionIdTableEntryForConnectionSearch;
import static businessObjects.db.clickhouse.webSession.WebSessionTableEntryFactory.webSessionTableEntryForConnectionSearch;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudType.*;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_ATTRIBUTES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
public class GetAbuseTypesByAttributesTest extends TestBaseApi {

    // Users
    public static final ClientHelper userFromDocument = getRandomVantageClient();
    public static final ClientHelper userToDocument = getRandomVantageClient();

    public static final ClientHelper userFromEmail = getRandomVantageClientAllFields();
    public static final ClientHelper userToEmail = getRandomVantageClientAllFields();

    public static final ClientHelper userFromIp = getRandomVantageClient();
    public static final ClientHelper userToIp = getRandomVantageClient();

    public static final ClientHelper userFromPhone = getRandomVantageClientAllFields();
    public static final ClientHelper userToPhone = getRandomVantageClientAllFields();

    public static final ClientHelper userFromPayout = getRandomVantageClient();
    public static final ClientHelper userToPayout = getRandomVantageClient();

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

    public static final ClientHelper userFromDepth = getRandomVantageClientAllFields();
    public static final ClientHelper userToDepth = getRandomVantageClientAllFields();
    public static final ClientHelper userTo2Depth = getRandomVantageClientAllFields();
    public static final ClientHelper userTo3Depth = getRandomVantageClientAllFields();

    // Objects to insert to attributes tables
    public static final DocumentTableEntry documentTableEntry = documentTableEntryForConnectionSearch(userFromDocument);
    public static final DocumentTableEntry documentTableEntry2 = documentTableEntryForConnectionSearch(userToDocument);

    public static final EmailTableEntry emailTableEntry = getEmailTableEntryByClient(userFromEmail);
    public static final EmailTableEntry emailTableEntry2 = emailTableEntryForConnectionSearch(userToEmail, userFromEmail.getEmail());

    public static final EmailTableEntry emailTableEntryForDepth1 = emailTableEntryForConnectionSearch(userFromDepth);

    public static final IpTableEntry ipTableEntry = ipTableEntryForConnectionSearch(userFromIp);
    public static final IpTableEntry ipTableEntry2 = ipTableEntryForConnectionSearch(userToIp);

    public static final PhoneTableEntry phoneTableEntry = phoneTableEntryForConnectionSearch(userFromPhone);
    public static final PhoneTableEntry phoneTableEntry2 = phoneTableEntryForConnectionSearch(userToPhone);

    public static final PayoutTableEntry payoutTableEntry = payoutTableEntryForConnectionSearch(userFromPayout);
    public static final PayoutTableEntry payoutTableEntry2 = payoutTableEntryForConnectionSearch(userToPayout);

    public static final DeviceIdTableEntry deviceIdTableEntry = deviceIdTableEntryForConnectionSearch(userFromDeviceId);
    public static final DeviceIdTableEntry deviceIdTableEntry2 = deviceIdTableEntryForConnectionSearch(userToDeviceId, userFromDeviceId.getDeviceId());

    public static final DigitalIdTableEntry digitalIdTableEntry = digitalIdTableEntryForConnectionSearch(userFromDigitalId);
    public static final DigitalIdTableEntry digitalIdTableEntry2 = digitalIdTableEntryForConnectionSearch(userToDigitalId, userFromDigitalId.getDigitalId());

    public static final NameBirthTableEntry nameBirthTableEntry = nameBirthTableEntryForConnectionSearch(userFromNameBirth);
    public static final NameBirthTableEntry nameBirthTableEntry2 = nameBirthTableEntryForConnectionSearch(userToNameBirth, userFromNameBirth.getNamedateofbirth());

    public static final SessionIdTableEntry sessionIdTableEntry = sessionIdTableEntryForConnectionSearch(userFromSessionId);
    public static final SessionIdTableEntry sessionIdTableEntry2 = sessionIdTableEntryForConnectionSearch(userToSessionId, userFromSessionId.getSessionId());

    public static final WebSessionTableEntry webSessionTableEntry = webSessionTableEntryForConnectionSearch(userFromWebSessionId);
    public static final WebSessionTableEntry webSessionTableEntry2 = webSessionTableEntryForConnectionSearch(userToWebSessionId, userFromWebSessionId.getWebSessionId());

    // Objects to insert to connections table
    public static final ConnectionTableEntry connectionTableEntryByEmail1 = getConnectionTableEntry(userFromDepth, userToDepth);
    public static final ConnectionTableEntry connectionTableEntryByEmail2 = getConnectionTableEntry(userToDepth, userTo2Depth);
    public static final ConnectionTableEntry connectionTableEntryByEmail3 = getConnectionTableEntry(userTo2Depth, userTo3Depth);

    // Frauds
    private static final BoClientFraudTypesObject fraudEmailTo = new BoClientFraudTypesObject(userToEmail.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
    private static final BoClientFraudTypesObject fraudEmail1 = new BoClientFraudTypesObject(userFromDepth.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
    private static final BoClientFraudTypesObject fraudEmail2 = new BoClientFraudTypesObject(userTo2Depth.getUcid(), CPA_ABUSE.getFraudTypeId(), CPA_ABUSE.getDisplayName());
    private static final BoClientFraudTypesObject fraudEmail3 = new BoClientFraudTypesObject(userTo3Depth.getUcid(), LOSS_VOUCHER_ABUSE.getFraudTypeId(), LOSS_VOUCHER_ABUSE.getDisplayName());
    private static final BoClientFraudTypesObject fraudDocumentTo = new BoClientFraudTypesObject(userToDocument.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
    private static final BoClientFraudTypesObject fraudIpTo = new BoClientFraudTypesObject(userToIp.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
    private static final BoClientFraudTypesObject fraudPhoneTo = new BoClientFraudTypesObject(userToPhone.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
    private static final BoClientFraudTypesObject fraudPhoneFrom = new BoClientFraudTypesObject(userFromPhone.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
    private static final BoClientFraudTypesObject fraudPayoutTo = new BoClientFraudTypesObject(userToPayout.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
    private static final BoClientFraudTypesObject fraudDeviceIdTo = new BoClientFraudTypesObject(userToDeviceId.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
    private static final BoClientFraudTypesObject fraudDigitalIdTo = new BoClientFraudTypesObject(userToDigitalId.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
    private static final BoClientFraudTypesObject fraudNameBirthTo = new BoClientFraudTypesObject(userToNameBirth.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
    private static final BoClientFraudTypesObject fraudSessionIdTo = new BoClientFraudTypesObject(userToSessionId.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
    private static final BoClientFraudTypesObject fraudWebSessionIdTo = new BoClientFraudTypesObject(userToWebSessionId.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());

    @BeforeAll
    public static void setupConnectionTableEntry() {
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
        insertObjectsToDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, List.of(fraudPhoneFrom, fraudEmail1, fraudEmail2, fraudEmail3, fraudDocumentTo, fraudEmailTo, fraudIpTo, fraudPhoneTo, fraudPayoutTo, fraudDeviceIdTo, fraudDigitalIdTo, fraudNameBirthTo, fraudSessionIdTo, fraudWebSessionIdTo));
    }

    @AfterAll
    public static void deleteConnectionTableEntry() {
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
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userFromEmail.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userToEmail.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userFromDocument.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userToDocument.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userFromIp.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userToIp.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userFromPayout.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userToPayout.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userFromPhone.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userToPhone.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userFromNameBirth.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userToNameBirth.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userToDeviceId.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userFromDeviceId.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userToSessionId.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userFromSessionId.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userToWebSessionId.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", userFromWebSessionId.getUcid()));
    }

    @Test
    @DisplayName("Connection search get abuse types. Get abuse types by emailAddress success(200)")
    @AllureId("769")
    public void getAbuseTypesByAttributesTest1() throws IOException {
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
    public void getAbuseTypesByAttributesTest2() throws IOException {
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
    public void getAbuseTypesByAttributesTest3() throws IOException {
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
    public void getAbuseTypesByAttributesTest4() throws IOException {
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
    public void getAbuseTypesByAttributesTest5() throws IOException {
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
    public void getAbuseTypesByAttributesTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("nameBirth", nameBirthTableEntry.dateOfBirth);

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
    public void getAbuseTypesByAttributesTest7() throws IOException {
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
    public void getAbuseTypesByAttributesTest8() throws IOException {
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
    public void getAbuseTypesByAttributesTest9() throws IOException {
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
    public void getAbuseTypesByAttributesTest10() throws IOException {
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
    public void getAbuseTypesByAttributesTest11() throws IOException {
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
    public void getAbuseTypesByAttributesTest12() throws IOException {
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
    public void getAbuseTypesByAttributesTest13() throws IOException {
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
    public void getAbuseTypesByAttributesTest14() throws IOException {
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
    public void getAbuseTypesByAttributesTest15() throws IOException {
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
    public void getAbuseTypesByAttributesTest16() throws IOException {
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
    public void getAbuseTypesByAttributesTest17() throws IOException {
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
    public void getAbuseTypesByAttributesTest18() throws IOException {
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
    public void getAbuseTypesByAttributesTest19() throws IOException {
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
    public void getAbuseTypesByAttributesTest20() throws IOException {
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
    public void getAbuseTypesByAttributesTest21() throws IOException {
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
