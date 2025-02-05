package tests.connectionSearchApiServiceTests;

import businessObjects.api.connectionSearchApi.ConnectionSearchResponseError;
import businessObjects.api.connectionSearchApi.getConnections.GetConnectionsResponse;
import businessObjects.api.connectionSearchApi.getConnections.GetConnectionsResponseError;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.connectionSearchApi.getConnections.GetConnectionsRequest.getConnectionsByClientId;
import static businessObjects.api.connectionSearchApi.getConnections.GetConnectionsResponseFactory.*;
import static businessObjects.api.connectionSearchApi.getConnections.GetConnectionsResponseFactory.getConnectionsResponseSuccess;
import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.*;
import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.getConnectionTableEntry;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_CLIENT_ID)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
public class GetConnectionsByClientTest extends TestBaseApi {

    public static final ClientHelper userFrom1 = getRandomVantageClient();
    public static final ClientHelper userTo1_1 = getRandomVantageClient();
    public static final ClientHelper userTo1_2 = getRandomVantageClient();
    public static final ClientHelper userFrom2 = getRandomVantageClient();
    public static final ClientHelper userTo2_1 = getRandomVantageClient();
    public static final ClientHelper userTo2_2 = getRandomVantageClient();
    public static final ClientHelper userFrom3 = getRandomVantageClient();
    public static final ClientHelper userTo3_1 = getRandomVantageClient();
    public static final ClientHelper userTo3_2 = getRandomVantageClient();
    public static final ClientHelper userFrom4 = getRandomVantageClient();
    public static final ClientHelper userTo4_1 = getRandomVantageClient();
    public static final ClientHelper userTo4_2 = getRandomVantageClient();
    public static final ClientHelper userTo4_3 = getRandomVantageClient();

    public final GetConnectionsResponse getConnectionsResponseSuccess = getConnectionsResponseSuccess(userFrom1, userTo1_1);
    public final GetConnectionsResponse getConnectionsLvl2ResponseSuccess = getConnectionsByClientLvl2ResponseSuccess(userTo1_1, userTo1_2);
    public final GetConnectionsResponse[] getConnectionsResponsesForFiltration = getConnectionsByClientForFiltrationByParams(userFrom2, userTo2_1, userTo2_2);
    public final GetConnectionsResponseError getConnectionsResponseErrorIncorrectConnectionAttributes = getConnectionsResponseErrorIncorrectConnectionAttributes();

    public static ConnectionTableEntry connectionTableEntry = getConnectionTableEntry(userFrom1, userTo1_1);
    public static ConnectionTableEntry connectionTableEntryLvl2 = getConnectionTableEntryLvl2(userTo1_1, userTo1_2);
    public static ConnectionTableEntry connectionTableEntryForFiltration1 = getConnectionTableEntry(userFrom2, userTo2_1);
    public static ConnectionTableEntry connectionTableEntryForFiltration2 = getConnectionTableEntryForFiltration(userTo2_1, userTo2_2);
    public static ConnectionTableEntry connectionTableEntry1And2Level1 = getConnectionTableEntry(userFrom3, userTo3_1);
    public static ConnectionTableEntry connectionTableEntry1And2Level2 = getConnectionTableEntry(userFrom3, userTo3_2);
    public static ConnectionTableEntry connectionTableEntry1And2Level3 = getConnectionTableEntryLvl2(userTo3_1, userTo3_2);
    public static ConnectionTableEntry connectionTableEntrySameLevelScore1 = getConnectionTableEntry(userFrom4, userTo4_1);
    public static ConnectionTableEntry connectionTableEntrySameLevelScore2 = getConnectionTableEntry(userFrom4, userTo4_2);
    public static ConnectionTableEntry connectionTableEntrySameLevelScore3 = getConnectionTableEntryLvl2(userTo4_1, userTo4_3);
    public static ConnectionTableEntry connectionTableEntrySameLevelScore4 = getConnectionTableEntry(userTo4_2, userTo4_3);

    @BeforeAll
    public static void setupConnectionTableEntry() throws ReflectiveOperationException, SQLException {
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntry, connectionTableEntryLvl2, connectionTableEntryForFiltration1, connectionTableEntryForFiltration2, connectionTableEntry1And2Level1, connectionTableEntry1And2Level2, connectionTableEntry1And2Level3, connectionTableEntrySameLevelScore1, connectionTableEntrySameLevelScore2, connectionTableEntrySameLevelScore3, connectionTableEntrySameLevelScore4));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by clientId success(200)")
    @AllureId("145")
    public void getConnectionsByClientSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntry.userFrom);

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check the response body", Arrays.stream(responseBody).toList(), containsInAnyOrder(getConnectionsResponseSuccess, getConnectionsLvl2ResponseSuccess));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by clientId with 1 and 2 level connections to the same client success(200)")
    @AllureId("939")
    public void getConnectionsByClient1And2LevelSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom3.getUcid());

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check the response body", Arrays.stream(responseBody).toList(), containsInAnyOrder(getConnectionsResponseSuccess(userFrom3, userTo3_1), getConnectionsResponseSuccess(userFrom3, userTo3_2)));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by clientId with same level connections different score to the same client success(200)")
    @AllureId("940")
    public void getConnectionsByClientSameLevelScoreSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", userFrom4.getUcid());

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length > 0, equalTo(true));

        assertThat("Check the response body", Arrays.stream(responseBody).toList(), containsInAnyOrder(getConnectionsResponseSuccess(userFrom4, userTo4_1), getConnectionsResponseSuccess(userFrom4, userTo4_2), getConnectionsResponseSuccessWithLevel(userTo4_2, userTo4_3, 2)));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by clientId and connectionDepth success(200)")
    @AllureId("146")
    public void getConnectionsByClientAndConnectionDepthSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntry.userFrom);
        queryParams.put("connectionDepth", 1);

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
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
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length == 0, equalTo(true));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by not existing connectionDepth for the client success(200)")
    @AllureId("148")
    public void getConnectionsNoSuchConnectionDepthSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntry.userFrom);
        queryParams.put("connectionDepth", -99);

        Response response = getConnectionsByClientId(queryParams);
        ConnectionSearchResponseError responseBody = objectMapper.readValue(
                response.body().string(), ConnectionSearchResponseError.class
        );

        assertThat("Check the response code is 200", response.code(), is(400));

        assertThat("Check the response body is not empty", responseBody.error, equalTo("Depth must be positive"));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection with connectionScoreFrom filtration success(200)")
    @AllureId("460")
    public void getConnectionsConnectionScoreFromFiltrationSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntryForFiltration1.userFrom);
        queryParams.put("connectionScoreFrom", 1);

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is has 1 element", responseBody.length, equalTo(1));

        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsResponsesForFiltration[0]));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection with connectionScoreTo filtration success(200)")
    @AllureId("461")
    public void getConnectionsConnectionScoreToFiltrationSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntryForFiltration1.userFrom);
        queryParams.put("connectionScoreTo", 0.9);

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is has 1 element", responseBody.length, equalTo(1));

        getConnectionsResponsesForFiltration[1].connectionDepth = 2;
        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsResponsesForFiltration[1]));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection with connectionType filtration success(200)")
    @AllureId("462")
    public void getConnectionsConnectionTypeFiltrationSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntryForFiltration1.userFrom);
        queryParams.put("connectionType", List.of("Same Network"));

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is has 1 element", responseBody.length, equalTo(1));

        getConnectionsResponsesForFiltration[1].connectionDepth = 2;
        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsResponsesForFiltration[1]));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection with connectionAttributes filtration success(200)")
    @AllureId("463")
    public void getConnectionsConnectionAttributesFiltrationSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntryForFiltration1.userFrom);
        queryParams.put("connectionAttributes", List.of("emailAddress"));

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is has 1 element", responseBody.length, equalTo(1));

        getConnectionsResponsesForFiltration[1].connectionDepth = 2;
        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsResponsesForFiltration[1]));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection without clientId Bad Request(400)")
    @AllureId("149")
    public void getConnectionsNoClientIdBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorClientIdMissingBadRequest()));
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
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorClientIdBadRequest()));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection with connectionDepth not int Bad Request(400)")
    @AllureId("150")
    public void getConnectionsConnectionDepthNotIntBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntry.userFrom);
        queryParams.put("connectionDepth", "test");

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorConnectionDepthBadRequest()));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection with connectionScoreFrom not int Bad Request(400)")
    @AllureId("464")
    public void getConnectionsConnectionScoreFromNotIntBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntry.userFrom);
        queryParams.put("connectionScoreFrom", "test");

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorConnectionScoreFromBadRequest()));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection with connectionScoreTo not int Bad Request(400)")
    @AllureId("465")
    public void getConnectionsConnectionScoreToNotIntBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntry.userFrom);
        queryParams.put("connectionScoreTo", "test");

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorConnectionScoreToBadRequest()));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection with incorrect connectionAttributes Bad Request(400)")
    @AllureId("466")
    public void getConnectionsConnectionAttributesIncorrectBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntry.userFrom);
        queryParams.put("connectionAttributes", List.of("test"));

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponseError responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponseError.class
        );

        assertThat("Check the response code is 400", response.code(), is(400));

        assertThat("Check the response body", responseBody, equalTo(getConnectionsResponseErrorIncorrectConnectionAttributes));
    }

    @AfterAll
    public static void deleteConnectionTableEntry() throws SQLException {
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from IN ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')", connectionTableEntry.userFrom, connectionTableEntryLvl2.userFrom, connectionTableEntryForFiltration1.userFrom, connectionTableEntryForFiltration2.userFrom, connectionTableEntry1And2Level1.userFrom, connectionTableEntry1And2Level2.userFrom, connectionTableEntry1And2Level3.userFrom, connectionTableEntrySameLevelScore1.userFrom, connectionTableEntrySameLevelScore2.userFrom, connectionTableEntrySameLevelScore3.userFrom, connectionTableEntrySameLevelScore4.userFrom));
    }
}
