package tests.connectionSearchApiServiceTests;

import businessObjects.api.connectionSearchApi.GetConnectionsResponse;
import businessObjects.api.connectionSearchApi.GetConnectionsResponseError;
import businessObjects.db.clickhouse.csTbConnectionTableV3.ConnectionTableEntryV3;
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
import java.util.Map;

import static businessObjects.api.connectionSearchApi.GetConnectionsRequest.getConnectionsByClientId;
import static businessObjects.api.connectionSearchApi.GetConnectionsResponseFactory.*;
import static businessObjects.db.clickhouse.csTbConnectionTableV3.ConnectionTableEntryV3Factory.getConnectionTableEntryByClientLvl2V3;
import static businessObjects.db.clickhouse.csTbConnectionTableV3.ConnectionTableEntryV3Factory.getConnectionTableEntryByClientV3;
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
public class GetConnectionsByClientTest extends TestBaseApi {

    public final GetConnectionsResponse getConnectionsResponseSuccess = getConnectionsByClientResponseSuccess();
    public final GetConnectionsResponse getConnectionsLvl2ResponseSuccess = getConnectionsByClientLvl2ResponseSuccess();
    public final GetConnectionsResponseError getConnectionsResponseError = getConnectionsResponseErrorBadRequest();
    public static ConnectionTableEntryV3 connectionTableEntryV3 = getConnectionTableEntryByClientV3();
    public static ConnectionTableEntryV3 connectionTableEntryV3Lvl2 = getConnectionTableEntryByClientLvl2V3();

    @BeforeAll
    public static void setupConnectionTableEntry() throws ReflectiveOperationException, SQLException {
        insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connectionTableEntryV3);
        insertObjectToDb(CONNECTIONS_V3_TABLE_NAME, connectionTableEntryV3Lvl2);
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by clientId success(200)")
    @AllureId("145")
    public void getConnectionsByClientSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntryV3.userFrom);

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check the response body", Arrays.stream(responseBody).toList(), containsInAnyOrder(getConnectionsResponseSuccess, getConnectionsLvl2ResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by clientId and connectionDepth success(200)")
    @AllureId("146")
    public void getConnectionsByClientAndConnectionDepthSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntryV3.userFrom);
        queryParams.put("connectionDepth", 1);

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));
        GetConnectionsResponse firstResponse = responseBody[0];

        assertThat("Check the response body", firstResponse, equalTo(getConnectionsResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by not existing clientId success(200)")
    @AllureId("147")
    public void getConnectionsNoSuchClientIdSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "test-12345");

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length == 0, equalTo(true));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by not existing connectionDepth for the client success(200)")
    @AllureId("148")
    public void getConnectionsNoSuchConnectionDepthSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntryV3.userFrom);
        queryParams.put("connectionDepth", -99);

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length == 0, equalTo(true));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection without clientId Bad Request(400)")
    @AllureId("149")
    public void getConnectionsNoClientIdBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the timestamp field", responseBody.timestamp, notNullValue());

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseError));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by clientId with incorrect format success(400)")
    @AllureId("180")
    public void getConnectionsIncorrectClientIdFormatBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "incorrect_client_format");
        queryParams.put("connectionDepth", 1);

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorClientIdBadRequest()));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection with connectionDepth not int Bad Request(400)")
    @AllureId("150")
    public void getConnectionsConnectionDepthNotIntBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntryV3.userFrom);
        queryParams.put("connectionDepth", "test");

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(),
                GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the timestamp field", responseBody.timestamp, notNullValue());

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseError));
    }

    @AfterAll
    public static void deleteConnectionTableEntry() throws SQLException {
        deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryV3.userFrom));
        deleteEntryFromDb(CONNECTIONS_V3_TABLE_NAME, String.format("user_from = '%s'", connectionTableEntryV3Lvl2.userFrom));
    }
}
