package tests.connection_search_api_service_tests;

import business_objects.api.connection_search_api.get_abuse_types.GetAbuseTypesResponse;
import business_objects.api.connection_search_api.ConnectionSearchResponseError;
import business_objects.db.clickhouse.bo_client_fraud_types.ClientFraudTypesObject;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
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

import static business_objects.api.connection_search_api.get_abuse_types.GetAbuseTypesRequest.getAbuseTypesByClientId;
import static business_objects.api.connection_search_api.get_abuse_types.GetAbuseTypesResponseFactory.*;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntry.connectionInfoToString;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.*;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.enums.FraudType.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.waitForConnectionSearchToUpdate;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_CLIENT_ID)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
class GetAbuseTypesByClientTest extends TestBaseApi {

    //Data 1
    static final ClientHelper userFrom1 = getRandomVantageClient();
    static final ClientHelper userTo1_1 = getRandomVantageClient();
    static final ClientHelper userTo1_2 = getRandomVantageClient();
    static final ClientHelper userTo1_3 = getRandomVantageClient();

    static ConnectionTableEntry connectionTableEntry11 = getConnectionTableEntry(userFrom1, userTo1_1);
    static ConnectionTableEntry connectionTableEntry12 = getConnectionTableEntry(userFrom1, userTo1_2);
    static ConnectionTableEntry connectionTableEntry13 = getConnectionTableEntry(userTo1_1, userTo1_3);

    private static ClientFraudTypesObject fraud11;
    private static ClientFraudTypesObject fraud12;

    //Data 2
    static final ClientHelper userFrom2 = getRandomVantageClient();
    static final ClientHelper userTo2_1 = getRandomVantageClient();
    static final ClientHelper userTo2_2 = getRandomVantageClient();
    static final ClientHelper userTo2_3 = getRandomVantageClient();

    static ConnectionTableEntry connectionTableEntry21 = getConnectionTableEntry(userFrom2, userTo2_1);
    static ConnectionTableEntry connectionTableEntry22 = getConnectionTableEntry(userFrom2, userTo2_2);
    static ConnectionTableEntry connectionTableEntry23 = getConnectionTableEntryLvl2(userTo2_2, userTo2_3);

    private static ClientFraudTypesObject fraud2_2;

    //Data 3
    static final ClientHelper userFrom3 = getRandomVantageClient();
    static final ClientHelper userTo3 = getRandomVantageClient();
    static ConnectionTableEntry connectionTableEntry3 = getConnectionTableEntry(userFrom3, userTo3, userTo3.getIpAddress());

    private static ClientFraudTypesObject fraud3;


    @BeforeAll
    static void setupConnectionTableEntry() throws Exception {
        connectionTableEntry11.connectionInfo = connectionInfoToString(List.of(new ConnectionTableEntry.ConnectionInfo(CONNECTION_ATTRIBUTE_NAME_DIGITAL, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_SEARCH_DATA_CARD_NUMBER, CONNECTION_TYPE_RELATION_TYPE_EXACT)));

        fraud11 = new ClientFraudTypesObject(userTo1_1.getUcid(), HEDGING.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
        fraud12 = new ClientFraudTypesObject(userTo1_2.getUcid(), CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
        fraud2_2 = new ClientFraudTypesObject(userTo2_3.getUcid(), CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());
        fraud3 = new ClientFraudTypesObject(userTo3.getUcid(), CPA_ABUSE.getKey(), FRAUD_TYPE_SOURCE_VINDEX, 0, getCurrentTimestampDbFormat());

        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud11);
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud12);
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud2_2);
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud3);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry11);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry12);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry13);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry21);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry22);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry23);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry3);
        waitForConnectionSearchToUpdate();
    }

    @AfterAll
    static void deleteConnectionTableEntry() {
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry11.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry12.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry13.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry21.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry22.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry23.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry3.userFrom));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud11.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud12.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud2_2.getUcid()));
        deleteEntryFromDb(BO_CLIENT_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud3.getUcid()));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId success(200)")
    @AllureId("745")
    void getAbuseTypesByClientTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));
        assertThat("Check the response body", responseBody[1].abuseType, is(CPA_ABUSE.getKey()));
        assertThat("Check the response body", responseBody[0].abuseType, is(HEDGING.getKey()));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types for non existing client(200)")
    @AllureId("746")
    void getAbuseTypesByClientTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "vantage-168443934111");

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId without params(400)")
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
    @DisplayName("Connection search by clientId. Get abuse types by clientId with abuseTypes success(200)")
    @AllureId("748")
    void getAbuseTypesByClientTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("abuseTypes", CPA_ABUSE.getKey());

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));
        assertThat("Check the response body", responseBody[0].abuseType, is(CPA_ABUSE.getKey()));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId with abuseTypes empty response(200)")
    @AllureId("749")
    void getAbuseTypesByClientTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("abuseTypes", LOSS_VOUCHER_ABUSE);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId with connectionDepth=-99(200)")
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
    @DisplayName("Connection search by clientId. Get abuse types by clientId with connectionDepth=2(200)")
    @AllureId("751")
    void getAbuseTypesByClientTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom2.getUcid());
        queryParams.put("connectionDepth", 2);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(1));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId with connectionScoreFrom=0.9(200)")
    @AllureId("752")
    void getAbuseTypesByClientTest8() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreFrom", 0.9);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(2));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId with connectionScoreFrom=1.1(200)")
    @AllureId("753")
    void getAbuseTypesByClientTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreFrom", 1.1);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId with connectionScoreTo=0.9(200)")
    @AllureId("754")
    void getAbuseTypesByClientTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreTo", 0.9);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId with connectionScoreFrom=1.1(200)")
    @AllureId("755")
    void getAbuseTypesByClientTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreTo", 1.1);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(2));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId with unknown connection attribute(400)")
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

        assertThat("Check the response code is 200", response.code(), is(400));
        assertThat("Check the response body is empty", responseBody, equalTo(getAbuseTypesResponseErrorUnknownAttributeBadRequest()));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId with connection attribute(200)")
    @AllureId("757")
    void getAbuseTypesByClientTest13() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionAttributes", List.of("payoutId", "payoutId"));

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(1));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId with connectionScoreTo wrong value(400)")
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
    @DisplayName("Connection search by clientId. Get abuse types by clientId with connectionScoreFrom wrong value(400)")
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
    @DisplayName("Connection search by clientId. Get abuse types by wrong clientId (400)")
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
    @DisplayName("Connection search by clientId. Get abuse types by all params(200)")
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
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types only by ip, empty response(200)")
    @AllureId("1145")
    void getAbuseTypesByClientTest18() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom3.getUcid());

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(0));
    }
}