package tests.connectionSearchApiServiceTests;

import businessObjects.api.connectionSearchApi.GetAbuseTypesResponse;
import businessObjects.api.connectionSearchApi.ConnectionSearchResponseError;
import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;
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

import static businessObjects.api.connectionSearchApi.GetAbuseTypesRequest.getAbuseTypesByClientId;
import static businessObjects.api.connectionSearchApi.GetAbuseTypesResponseFactory.*;
import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.*;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.enums.FraudType.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_CLIENT_ID)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
public class GetAbuseTypesByClientTest extends TestBaseApi {

    //Data 1
    public static final ClientHelper userFrom1 = getRandomVantageClient();
    public static final ClientHelper userTo1_1 = getRandomVantageClient();
    public static final ClientHelper userTo1_2 = getRandomVantageClient();
    public static final ClientHelper userTo1_3 = getRandomVantageClient();

    public static ConnectionTableEntry connectionTableEntry1_1 = getConnectionTableEntry(userFrom1, userTo1_1);
    public static ConnectionTableEntry connectionTableEntry1_2 = getConnectionTableEntry(userFrom1, userTo1_2);
    public static ConnectionTableEntry connectionTableEntry1_3 = getConnectionTableEntry(userTo1_1, userTo1_3);

    private static BoClientFraudTypesObject fraud1_1;
    private static BoClientFraudTypesObject fraud1_2;

    //Data 2
    public static final ClientHelper userFrom2 = getRandomVantageClient();
    public static final ClientHelper userTo2_1 = getRandomVantageClient();
    public static final ClientHelper userTo2_2 = getRandomVantageClient();
    public static final ClientHelper userTo2_3 = getRandomVantageClient();

    public static ConnectionTableEntry connectionTableEntry2_1 = getConnectionTableEntry(userFrom2, userTo2_1);
    public static ConnectionTableEntry connectionTableEntry2_2 = getConnectionTableEntry(userFrom2, userTo2_2);
    public static ConnectionTableEntry connectionTableEntry2_3 = getConnectionTableEntryLvl2(userTo2_2, userTo2_3);

    private static BoClientFraudTypesObject fraud2_1;
    private static BoClientFraudTypesObject fraud2_2;

    @BeforeAll
    public static void setupConnectionTableEntry() throws ReflectiveOperationException, SQLException {
        connectionTableEntry1_1.connectionInfo = "[{\"connectionAttributeName\": \"digital\", \"connectionAttributeValue\": \"535456**** **0344\", \"sourceAttributeValue\": \"535456**** **0344\", \"relationType\": \"exact\"}]";
        fraud1_1 = new BoClientFraudTypesObject(userTo1_1.getUcid(), HEDGING.getFraudTypeId(), HEDGING.getDisplayName());
        fraud1_2 = new BoClientFraudTypesObject(userTo1_2.getUcid(), CPA.getFraudTypeId(), CPA.getDisplayName());
        fraud2_2 = new BoClientFraudTypesObject(userTo2_3.getUcid(), CPA.getFraudTypeId(), CPA.getDisplayName());
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud1_1);
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud1_2);
        insertObjectToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, fraud2_2);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry1_1);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry1_2);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry1_3);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry2_1);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry2_2);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry2_3);
    }

    @AfterAll
    public static void deleteConnectionTableEntry() throws SQLException {
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry1_1.userFrom));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry1_2.userFrom));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId success(200)")
    @AllureId("745")
    public void getAbuseTypesByClientTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));
        assertThat("Check the response body", responseBody[0].abuseType, is(CPA.getDisplayName()));
        assertThat("Check the response body", responseBody[1].abuseType, is(HEDGING.getDisplayName()));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types for non existing client(200)")
    @AllureId("746")
    public void getAbuseTypesByClientTest2() throws IOException {
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
    public void getAbuseTypesByClientTest3() throws IOException {
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
    public void getAbuseTypesByClientTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("abuseTypes", CPA.getDisplayName());

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(1));
        assertThat("Check the response body", responseBody[0].abuseType, is(CPA.getDisplayName()));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId with abuseTypes empty response(200)")
    @AllureId("749")
    public void getAbuseTypesByClientTest5() throws IOException {
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
    public void getAbuseTypesByClientTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionDepth", -99);

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search by clientId. Get abuse types by clientId with connectionDepth=2(200)")
    @AllureId("751")
    public void getAbuseTypesByClientTest7() throws IOException {
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
    public void getAbuseTypesByClientTest8() throws IOException {
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
    public void getAbuseTypesByClientTest9() throws IOException {
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
    public void getAbuseTypesByClientTest10() throws IOException {
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
    public void getAbuseTypesByClientTest11() throws IOException {
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
    public void getAbuseTypesByClientTest12() throws IOException {
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
    @DisplayName("Connection search by clientId. Get abuse types by clientId with with  connection attribute(200)")
    @AllureId("757")
    public void getAbuseTypesByClientTest13() throws IOException {
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
    public void getAbuseTypesByClientTest14() throws IOException {
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
    public void getAbuseTypesByClientTest15() throws IOException {
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
    public void getAbuseTypesByClientTest16() throws IOException {
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
    public void getAbuseTypesByClientTest17() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom1.getUcid());
        queryParams.put("connectionScoreFrom", 0);
        queryParams.put("connectionScoreTo", 2);
        queryParams.put("connectionDepth", 1);
        queryParams.put("abuseTypes", List.of(CPA.getDisplayName(), HEDGING.getDisplayName()));
        queryParams.put("connectionAttributes", List.of("digital", "payoutId"));

        Response response = getAbuseTypesByClientId(queryParams);
        assert response.body() != null;
        GetAbuseTypesResponse[] responseBody = (objectMapper.readValue(
                response.body().string(), GetAbuseTypesResponse[].class
        ));

        assertThat("Check the response code is 200", response.code(), is(200));
        assertThat("Check the response body is not empty", responseBody.length, equalTo(2));

    }
}