package tests.connectionSearchApiServiceTests;

import helpers.connectionSearchApi.GetConnectionsResponse;
import helpers.connectionSearchApi.GetConnectionsResponseError;
import helpers.database.dbObjects.csTbConnectionTableV2.ConnectionTableEntry;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static helpers.connectionSearchApi.GetConnectionsRequest.getConnectionsByClientId;
import static helpers.connectionSearchApi.GetConnectionsResponseFactory.getConnectionsResponseErrorBadRequest;
import static helpers.connectionSearchApi.GetConnectionsResponseFactory.getConnectionsResponseSuccess;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.dbObjects.csTbConnectionTableV2.ConnectionTableEntryFactory.getConnectionTableEntry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_CLIENT_ID)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
public class GetConnectionsByClientTest extends TestBaseApi {

    public final GetConnectionsResponse getConnectionsResponseSuccess = getConnectionsResponseSuccess();
    public final GetConnectionsResponseError getConnectionsResponseError = getConnectionsResponseErrorBadRequest();
    public static final String TABLE_NAME = "vindex_test.cs__tb_connection_table_v2";
    public static ConnectionTableEntry connectionTableEntry = getConnectionTableEntry();

    @BeforeAll
    public static void setupConnectionTableEntry() throws ReflectiveOperationException, SQLException {
        insertObjectToDb(TABLE_NAME, connectionTableEntry);
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by clientId success(200)")
    @AllureId("145")
    public void getConnectionsByClientSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", getConnectionsResponseSuccess.clientIdFrom);

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
    @DisplayName("Connection search by client Api. Get connection by clientId and connectionDepth success(200)")
    @AllureId("146")
    public void getConnectionsByClientAndConnectionDepthSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", getConnectionsResponseSuccess.clientIdFrom);
        queryParams.put("connectionDepth", getConnectionsResponseSuccess.connectionDepth);

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
        queryParams.put("clientId", "test_not_existing_client");
        queryParams.put("connectionDepth", getConnectionsResponseSuccess.connectionDepth);

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
        queryParams.put("clientId", getConnectionsResponseSuccess.clientIdFrom);
        queryParams.put("connectionDepth", 99);

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
        queryParams.put("connectionDepth", getConnectionsResponseSuccess.connectionDepth);

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
    @DisplayName("Connection search by client Api. Get connection with connectionDepth not int Bad Request(400)")
    @AllureId("150")
    public void getConnectionsConnectionDepthNotIntBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", getConnectionsResponseSuccess.clientIdFrom);
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
        deleteEntryFromDb(TABLE_NAME, String.format("user_from = '%s'", connectionTableEntry.userFrom));
    }
}
