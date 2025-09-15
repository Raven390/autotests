package tests.connection_search_api_service_tests;

import business_objects.api.connection_search_api.get_abuse_types_v1.GetAbuseTypesResponseV1;
import business_objects.api.connection_search_api.ConnectionSearchResponseError;
import business_objects.db.clickhouse.client_fraud_types.ClientFraudTypes;
import business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.connection_search_api.get_abuse_types_v1.GetAbuseTypesRequestV1.getAbuseTypesByClientId;
import static business_objects.api.connection_search_api.get_abuse_types_v1.GetAbuseTypesResponseFactoryV1.*;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntry.ConnectionInfo.connectionInfoToString;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClients;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntry;
import static business_objects.db.clickhouse.data_science_test.connection_table.ConnectionTableEntryFactory.getConnectionTableEntryLvl2;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.api.AbuseRegistryHelper.addFraudsForClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.enums.FraudTypeOld.*;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_CLIENT_ID_V1)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
class GetAbuseTypesByClientIdV1Tests extends TestBaseApi {

    //Data 1
    static final ClientHelper userFrom1 = getRandomVantageClient();
    static final ClientHelper userTo1_1 = getRandomVantageClient();
    static final ClientHelper userTo1_2 = getRandomVantageClient();
    static final ClientHelper userTo1_3 = getRandomVantageClient();
    static final ClientHelper userPotential = getRandomVantageClient();
    static final ClientHelper userConfirmed = getRandomVantageClient();

    static ConnectionTableEntry connectionTableEntry11 = getConnectionTableEntry(userFrom1, userTo1_1);
    static ConnectionTableEntry connectionTableEntry12 = getConnectionTableEntry(userFrom1, userTo1_2);
    static ConnectionTableEntry connectionTableEntry13 = getConnectionTableEntry(userTo1_1, userTo1_3);

    private static ClientFraudTypes fraud11;
    private static ClientFraudTypes fraud12;

    //Data 2
    static final ClientHelper userFrom2 = getRandomVantageClient();
    static final ClientHelper userTo2_1 = getRandomVantageClient();
    static final ClientHelper userTo2_2 = getRandomVantageClient();
    static final ClientHelper userTo2_3 = getRandomVantageClient();

    static ConnectionTableEntry connectionTableEntry21 = getConnectionTableEntry(userFrom2, userTo2_1);
    static ConnectionTableEntry connectionTableEntry22 = getConnectionTableEntry(userFrom2, userTo2_2);
    static ConnectionTableEntry connectionTableEntry23 = getConnectionTableEntryLvl2(userTo2_2, userTo2_3);
    static ConnectionTableEntry connectionTableEntryStatus1 = getConnectionTableEntryLvl2(userConfirmed, userPotential);
    static ConnectionTableEntry connectionTableEntryStatus2 = getConnectionTableEntryLvl2(userPotential, userConfirmed);

    private static ClientFraudTypes fraud2_2;

    //Data 3
    static final ClientHelper userFrom3 = getRandomVantageClient();
    static final ClientHelper userTo3 = getRandomVantageClient();
    static ConnectionTableEntry connectionTableEntry3 = getConnectionTableEntry(userFrom3, userTo3, userTo3.getIpAddress());

    private static ClientFraudTypes fraud3;
    private static ClientFraudTypes fraudPotential;
    private static ClientFraudTypes fraudConfirmed;

    static List<ClientHelper> fraudsters = new ArrayList<>(List.of(userTo1_1, userTo1_2, userTo1_3, userTo2_1, userTo2_2, userTo2_3, userTo3, userConfirmed, userPotential));
    static final List<CrmTbUserObject> clientsDB = generateUserByClients(fraudsters);


    @BeforeAll
    static void setupConnectionTableEntry() throws Exception {
        insertObjectsToDb(CRM_USER_TABLE_NAME, clientsDB);
        connectionTableEntry11.connectionInfo = connectionInfoToString(List.of(new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DIGITAL, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT)));

        fraud11 = new ClientFraudTypes(userTo1_1.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
        fraud12 = new ClientFraudTypes(userTo1_2.getUcid(), CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
        fraud2_2 = new ClientFraudTypes(userTo2_3.getUcid(), CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
        fraud3 = new ClientFraudTypes(userTo3.getUcid(), CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
        fraudPotential = new ClientFraudTypes(userPotential.getUcid(), CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
        fraudConfirmed = new ClientFraudTypes(userConfirmed.getUcid(), CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());

        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud11);
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud12);
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud2_2);
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud3);
        addFraudsForClient(fraud11, fraud12, fraud2_2, fraud3);
        addFraudForClient(fraudConfirmed, "CONFIRMED");
        addFraudForClient(fraudPotential, "POTENTIAL");
        insertConnectionToDb(connectionTableEntry11, connectionTableEntry12, connectionTableEntry13, connectionTableEntry21, connectionTableEntry22, connectionTableEntry23, connectionTableEntry3, connectionTableEntryStatus1, connectionTableEntryStatus2);
        waitForConnectionSearchToUpdate();
        Thread.sleep(5000);//pause for asinc services like CS and AR alvays set up connections last and use waitForConnectionSearchToUpdate() before this wait.
    }

    @AfterAll
    static void deleteConnectionTableEntry() throws Exception {
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry11.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry12.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry13.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry21.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry22.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry23.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry3.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryStatus1.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryStatus2.userFrom));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud11.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud12.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud2_2.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud3.getUcid()));

        List<String> clientUcids = new java.util.ArrayList<>(List.of());
        for (CrmTbUserObject client : clientsDB) {
            clientUcids.add(client.ucid);
        }
        cleanCrmUserTableByClient(String.valueOf(clientUcids));
        deleteUserFromAbuseRegistry(String.valueOf(clientUcids));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId success(200)")
    @AllureId("745")
    void getAbuseTypesByClientTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));
        assertThat("Check the response body", responseBody, hasItemInArray(getAbuseTypesResponseByFraud(fraud11, "CONFIRMED")));
        assertThat("Check the response body", responseBody, hasItemInArray(getAbuseTypesResponseByFraud(fraud12, "CONFIRMED")));
    }

    @Test
    @DisplayName("Connection search. Get abuse types for non existing client(200)")
    @AllureId("746")
    void getAbuseTypesByClientTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "vantage-168443934111");

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId without params(400)")
    @AllureId("747")
    void getAbuseTypesByClientTest3() throws IOException {
        Response response = getAbuseTypesByClientId(new HashMap<>());
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 400", response.code(), is(400));
        assertThat(responseBody, is(getAbuseTypesResponseErrorClientIdMissingBadRequest()));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with abuseTypes success(200)")
    @AllureId("748")
    void getAbuseTypesByClientTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("abuseTypes", CPA_ABUSE.getKey());

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));
        assertThat("Check the response body", responseBody[0].abuseType, is(CPA_ABUSE.getKey()));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with abuseTypes empty response(200)")
    @AllureId("749")
    void getAbuseTypesByClientTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("abuseTypes", LOSS_VOUCHER_ABUSE);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with connectionDepth=-99(200)")
    @AllureId("750")
    void getAbuseTypesByClientTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionDepth", -99);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat("Check the response body is empty", responseBody.error, equalTo("Depth must be positive"));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with connectionDepth=2(200)")
    @AllureId("751")
    void getAbuseTypesByClientTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom2.getUcid());
        queryParams.put("connectionDepth", 2);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(1));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with connectionScoreFrom=0.9(200)")
    @AllureId("752")
    void getAbuseTypesByClientTest8() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreFrom", 0.9);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(2));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with connectionScoreFrom=1.1(200)")
    @AllureId("753")
    void getAbuseTypesByClientTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreFrom", 1.1);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with connectionScoreTo=0.9(200)")
    @AllureId("754")
    void getAbuseTypesByClientTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreTo", 0.9);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with connectionScoreFrom=1.1(200)")
    @AllureId("755")
    void getAbuseTypesByClientTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreTo", 1.1);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(2));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with unknown connection attribute(400)")
    @AllureId("756")
    void getAbuseTypesByClientTest12() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionAttributes", "payout");

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 400", response.code(), is(400));
        assertThat("Check the response body is empty", responseBody, equalTo(getAbuseTypesResponseErrorUnknownAttributeBadRequest()));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with connection attribute(200)")
    @AllureId("757")
    void getAbuseTypesByClientTest13() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionAttributes", List.of("payoutId", "payoutId"));

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(1));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with connectionScoreTo wrong value(400)")
    @AllureId("758")
    void getAbuseTypesByClientTest14() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreTo", "test");

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat(responseBody, equalTo(getConnectionsResponseErrorConnectionScoreToBadRequest()));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId with connectionScoreFrom wrong value(400)")
    @AllureId("759")
    void getAbuseTypesByClientTest15() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreFrom", "test");

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat(responseBody, equalTo(getConnectionsResponseErrorConnectionScoreFromBadRequest()));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by wrong clientId (400)")
    @AllureId("760")
    void getAbuseTypesByClientTest16() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "123");

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        ConnectionSearchResponseError responseBody = (objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        ));

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat(responseBody, equalTo(getConnectionsResponseErrorClientIdBadRequest()));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by all params(200)")
    @AllureId("761")
    void getAbuseTypesByClientTest17() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreFrom", 0);
        queryParams.put("connectionScoreTo", 2);
        queryParams.put("connectionDepth", 1);
        queryParams.put("abuseTypes", List.of(CPA_ABUSE.getKey(), HEDGING.getKey()));
        queryParams.put("connectionAttributes", List.of("digital", "payoutId"));

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));
    }

    @Test
    @DisplayName("Connection search. Get abuse types only by ip, empty response(200)")
    @AllureId("1145")
    void getAbuseTypesByClientTest18() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom3.getUcid());

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search. Get abuse types by clientId contains abuse status")
    @AllureId("1364")
    void getAbuseTypesByClientAbuseStatusTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userPotential.getUcid());

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponseV1[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));
        assertThat("Check the response body", responseBody[0].fraudTypeStatus, is("CONFIRMED"));

        Map<String, Object> queryParams2 = new HashMap<>();
        queryParams2.put("clientId", userConfirmed.getUcid());

        Response response2 = getAbuseTypesByClientId(queryParams2);
        assert response2.body() != null;
        GetAbuseTypesResponseV1[] responseBody2 = (objectMapper.readValue(
                response2.body().string(), GetAbuseTypesResponseV1[].class
        ));

        assertThat("Check the response code is 200", response2.code(), is(200));
        assertThat("Check the response body is not empty", responseBody2.length, equalTo(1));
        assertThat("Check the response body", responseBody2[0].fraudTypeStatus, is("POTENTIAL"));
    }
}