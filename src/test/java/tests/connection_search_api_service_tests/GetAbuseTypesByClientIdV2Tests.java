package tests.connection_search_api_service_tests;

import static business_objects.api.connection_search_api.get_abuse_types_v2.GetAbuseTypesRequestV2.getAbuseTypesByClientIdV2;
import static business_objects.api.connection_search_api.get_abuse_types_v2.GetAbuseTypesResponseFactoryV2.*;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClients;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry.ConnectionInfo.connectionInfoToString;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntry;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.enums.FraudTypeOld.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.api.connection_search_api.ConnectionSearchResponseError;
import business_objects.api.connection_search_api.get_abuse_types_v2.GetAbuseTypesResponseV2;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_CLIENT_ID_V2)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
@Tag("CSV-1574")
class GetAbuseTypesByClientIdV2Tests extends TestBaseApi {

    // Data 1
    private static final ClientHelper userFrom1 = getRandomVantageClient();
    private static final ClientHelper userTo1_1 = getRandomVantageClient();
    private static final ClientHelper userTo1_2 = getRandomVantageClient();
    private static final ClientHelper userTo1_3 = getRandomVantageClient();
    private static final ClientHelper userTo1_4 = getRandomVantageClient();

    private static ConnectionTableEntry connectionTableEntry11 = getConnectionTableEntry(userFrom1, userTo1_1);
    private static ConnectionTableEntry connectionTableEntry12 = getConnectionTableEntry(userFrom1, userTo1_2);
    private static ConnectionTableEntry connectionTableEntry13 = getConnectionTableEntry(userFrom1, userTo1_3);
    private static ConnectionTableEntry connectionTableEntry14 = getConnectionTableEntry(userFrom1, userTo1_4);

    private static ClientFraudTypes fraud11;
    private static ClientFraudTypes fraud12;
    private static ClientFraudTypes fraud13;
    private static ClientFraudTypes fraud14;

    private static List<ClientHelper> fraudsters = new ArrayList<>(List.of(userTo1_1, userTo1_2, userTo1_3, userTo1_4));
    private static final List<CrmTbUserObject> clientsDB = generateUserByClients(fraudsters);

    @BeforeAll
    static void setupConnectionTableEntry() throws Exception {
        insertObjectsToDb(CRM_USER_TABLE_NAME, clientsDB);
        connectionTableEntry11.connectionInfo = connectionInfoToString(List.of(new ConnectionTableEntry.ConnectionInfo(
                CONNECTION_ATTRIBUTE_NAME_DIGITAL,
                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                CONNECTION_SEARCH_DATA_CARD_NUMBER,
                CONNECTION_TYPE_RELATION_TYPE_EXACT)));
        connectionTableEntry11.connectionScore = 0.5d;
        connectionTableEntry12.connectionScore = 0.8d;
        connectionTableEntry13.connectionScore = 0.2d;
        connectionTableEntry14.connectionScore = 0.4d;

        fraud11 = new ClientFraudTypes(
                userTo1_1.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
        fraud12 = new ClientFraudTypes(
                userTo1_2.getUcid(), CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
        fraud13 = new ClientFraudTypes(
                userTo1_3.getUcid(), CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
        fraud14 = new ClientFraudTypes(
                userTo1_4.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());

        insertObjectsToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, List.of(fraud11, fraud12, fraud13, fraud14));

        addFraudForClient(fraud11, "CONFIRMED");
        addFraudForClient(fraud12, "POTENTIAL");
        addFraudForClient(fraud13, "CONFIRMED");
        addFraudForClient(fraud14, "CONFIRMED");

        insertConnectionToDb(
                connectionTableEntry11, connectionTableEntry12, connectionTableEntry13, connectionTableEntry14);
        waitForConnectionSearchToUpdate();
        // pause for async services like CS and AR always set up connections last and use
        // waitForConnectionSearchToUpdate() before this wait.
        Thread.sleep(5000);
    }

    // @AfterAll
    static void deleteConnectionTableEntry() throws Exception {
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry11.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry12.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry13.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry14.userFrom));

        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud11.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud12.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud13.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud14.getUcid()));

        List<String> clientUcids = new ArrayList<>(List.of());
        for (CrmTbUserObject client : clientsDB) {
            clientUcids.add(client.ucid);
        }
        cleanCrmUserTableByClient(String.valueOf(clientUcids));
        deleteUserFromAbuseRegistry(String.valueOf(clientUcids));
    }

    GetAbuseTypesResponseV2 responseFraud11 = getAbuseTypesResponseByFraud(fraud11, 0.5, "CONFIRMED");
    GetAbuseTypesResponseV2 responseFraud12 = getAbuseTypesResponseByFraud(fraud12, 0.800_000_011_920_929, "POTENTIAL");
    GetAbuseTypesResponseV2 responseFraud13 =
            getAbuseTypesResponseByFraud(fraud13, 0.200_000_002_980_232_24, "CONFIRMED");
    GetAbuseTypesResponseV2 responseFraud14 = getAbuseTypesResponseByFraud(fraud14, 0.4, "CONFIRMED");

    @Test
    @AllureId("1509")
    @DisplayName("Connection search. Get abuse types v2. By clientId success (200)")
    void getAbuseTypesByClientIdTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponseV2[] responseBody =
                (objectMapper.readValue(response.body().string(), GetAbuseTypesResponseV2[].class));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(3));
        assertThat("Check the response body", responseBody, hasItemInArray(responseFraud11));
        assertThat("Check the response body", responseBody, hasItemInArray(responseFraud12));
        assertThat("Check the response body", responseBody, hasItemInArray(responseFraud13));
    }

    @Test
    @AllureId("1510")
    @DisplayName("Connection search. Get abuse types by clientId v2. Required param is missing (200)")
    void getAbuseTypesByClientIdTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId1", userFrom1.getUcid());

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        ConnectionSearchResponseError responseBody =
                (objectMapper.readValue(response.body().string(), ConnectionSearchResponseError.class));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat("Check the response body type", responseBody.type, equalTo("about:blank"));
        assertThat("Check the response body title", responseBody.title, equalTo("Bad Request"));
        assertThat("Check the response body status", responseBody.status, equalTo(400));
        assertThat(
                "Check the response body detail",
                responseBody.detail,
                equalTo("Required parameter 'clientId' is not present."));
        assertThat("Check the response body instance", responseBody.instance, equalTo("/v2/abuseTypes/byClientId"));
    }

    @Test
    @AllureId("1511")
    @DisplayName("Connection search. Get abuse types v2. By clientId and abuseTypes (200)")
    void getAbuseTypesByClientIdTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("abuseTypes", List.of("HEDGING"));

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponseV2[] responseBody =
                (objectMapper.readValue(response.body().string(), GetAbuseTypesResponseV2[].class));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body length", responseBody.length, equalTo(1));
        assertThat("Check the response body abuse type", responseBody, hasItemInArray(responseFraud11));
    }

    @Test
    @AllureId("1512")
    @DisplayName("Connection search. Get abuse types v2. By clientId and connectionDepth (200)")
    void getAbuseTypesByClientIdTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionDepth", 2);

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponseV2[] responseBody =
                (objectMapper.readValue(response.body().string(), GetAbuseTypesResponseV2[].class));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(3));
        assertThat("Check the response body abuse type", responseBody, hasItemInArray(responseFraud11));
        assertThat("Check the response body abuse type", responseBody, hasItemInArray(responseFraud12));
    }

    @Test
    @AllureId("1513")
    @DisplayName("Connection search. Get abuse types v2. By clientId and connectionScoreFrom (200)")
    void getAbuseTypesByClientIdTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreFrom", 0.9);

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponseV2[] responseBody =
                (objectMapper.readValue(response.body().string(), GetAbuseTypesResponseV2[].class));

        assertThat("Check the response body is empty", responseBody.length, equalTo(0));
    }

    @Test
    @AllureId("1514")
    @DisplayName("Connection search. Get abuse types v2. By clientId and connectionScoreFrom (200)")
    void getAbuseTypesByClientIdTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreFrom", 0.7);

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponseV2[] responseBody =
                (objectMapper.readValue(response.body().string(), GetAbuseTypesResponseV2[].class));

        assertThat("Check the response body is empty", responseBody.length, equalTo(1));
        assertThat("Check the response body abuse type", responseBody, hasItemInArray(responseFraud12));
    }

    @Test
    @AllureId("1515")
    @DisplayName("Connection search. Get abuse types v2. By clientId and connectionScoreFrom (200)")
    void getAbuseTypesByClientIdTest12() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreFrom", 1.1);

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        ConnectionSearchResponseError responseBody =
                (objectMapper.readValue(response.body().string(), ConnectionSearchResponseError.class));

        assertThat(
                "Check the response body error",
                responseBody.error,
                equalTo("{jakarta.validation.constraints.DecimalMax.message}"));
        assertThat("Check the response body status", responseBody.status, equalTo(400));
    }

    @Test
    @AllureId("1516")
    @DisplayName("Connection search. Get abuse types v2. By clientId and connectionScoreTo (200)")
    void getAbuseTypesByClientIdTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreTo", 0.1);

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponseV2[] responseBody =
                (objectMapper.readValue(response.body().string(), GetAbuseTypesResponseV2[].class));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(0));
    }

    @Test
    @AllureId("1518")
    @DisplayName("Connection search. Get abuse types v2. By clientId and connectionAttributes (200)")
    void getAbuseTypesByClientIdTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionAttributes", List.of("digital"));

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponseV2[] responseBody =
                (objectMapper.readValue(response.body().string(), GetAbuseTypesResponseV2[].class));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));
        assertThat("Check the response body abuse type", responseBody, hasItemInArray(responseFraud11));
    }

    @Test
    @AllureId("1519")
    @DisplayName("Connection search. Get abuse types v2. By clientId and abuseTypeStatus (200)")
    void getAbuseTypesByClientIdTest8() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("abuseTypeStatus", "CONFIRMED");

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponseV2[] responseBody =
                (objectMapper.readValue(response.body().string(), GetAbuseTypesResponseV2[].class));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody, hasItemInArray(responseFraud11));
    }

    @Test
    @AllureId("1521")
    @DisplayName("Connection search. Get abuse types v2. By clientId and abuseTypeStatus (200)")
    void getAbuseTypesByClientIdTest13() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("abuseTypeStatus", "POTENTIAL");

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponseV2[] responseBody =
                (objectMapper.readValue(response.body().string(), GetAbuseTypesResponseV2[].class));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));
        assertThat("Check the response body is not empty", responseBody, hasItemInArray(responseFraud12));
    }

    @Test
    @AllureId("1520")
    @DisplayName("Connection search. Get abuse types v2. By all params (200)")
    void getAbuseTypesByClientIdTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("abuseTypes", List.of("HEDGING", "CPA_ABUSE"));
        queryParams.put("abuseTypesStatus", List.of("CONFIRMED", "POTENTIAL"));
        queryParams.put("connectionDepth", 1);
        queryParams.put("connectionScoreTo", 0.9);
        queryParams.put("connectionScoreFrom", 0.1);
        queryParams.put("connectionAttributes", List.of("digital", "payoutId"));

        Response response = getAbuseTypesByClientIdV2(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetAbuseTypesResponseV2[] responseBody =
                (objectMapper.readValue(response.body().string(), GetAbuseTypesResponseV2[].class));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(3));
        assertThat("Check the response body abuse type", responseBody, hasItemInArray(responseFraud11));
        assertThat("Check the response body abuse type", responseBody, hasItemInArray(responseFraud12));
        assertThat("Check the response body abuse type", responseBody, hasItemInArray(responseFraud13));
    }
}
