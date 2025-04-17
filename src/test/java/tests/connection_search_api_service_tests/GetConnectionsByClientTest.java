package tests.connection_search_api_service_tests;

import business_objects.api.connection_search_api.ConnectionSearchResponseError;
import business_objects.api.connection_search_api.get_connections.GetConnectionsResponse;
import business_objects.api.connection_search_api.get_connections.GetConnectionsResponseError;
import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.connection_search_api.get_connections.GetConnectionsRequest.getConnectionsByClientId;
import static business_objects.api.connection_search_api.get_connections.GetConnectionsResponseFactory.*;
import static business_objects.api.connection_search_api.get_connections.GetConnectionsResponseFactory.getConnectionsResponseSuccess;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.*;
import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.getConnectionTableEntry;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanConnectionsTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.waitForConnectionSearchToUpdate;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_CLIENT_ID)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
class GetConnectionsByClientTest extends TestBaseApi {

    static final ClientHelper userFrom1 = getRandomVantageClient();
    static final ClientHelper userTo1_1 = getRandomVantageClient();
    static final ClientHelper userTo1_2 = getRandomVantageClient();
    static final ClientHelper userFrom2 = getRandomVantageClient();
    static final ClientHelper userTo2_1 = getRandomVantageClient();
    static final ClientHelper userTo2_2 = getRandomVantageClient();
    static final ClientHelper userFrom3 = getRandomVantageClient();
    static final ClientHelper userTo3_1 = getRandomVantageClient();
    static final ClientHelper userTo3_2 = getRandomVantageClient();
    static final ClientHelper userFrom4 = getRandomVantageClient();
    static final ClientHelper userTo4_1 = getRandomVantageClient();
    static final ClientHelper userTo4_2 = getRandomVantageClient();
    static final ClientHelper userTo4_3 = getRandomVantageClient();
    static final ClientHelper userFrom5 = getRandomVantageClientAllFields();
    static final ClientHelper userTo5 = getRandomVantageClientAllFields();

    final GetConnectionsResponse getConnectionsResponseSuccess = getConnectionsResponseSuccess(userFrom1, userTo1_1);
    final GetConnectionsResponse getConnectionsLvl2ResponseSuccess = getConnectionsByClientLvl2ResponseSuccess(userTo1_1, userTo1_2);
    final GetConnectionsResponse[] getConnectionsResponsesForFiltration = getConnectionsByClientForFiltrationByParams(userFrom2, userTo2_1, userTo2_2);
    final GetConnectionsResponseError getConnectionsResponseErrorIncorrectConnectionAttributes = getConnectionsResponseErrorIncorrectConnectionAttributes();

    static ConnectionTableEntry connectionTableEntry = getConnectionTableEntry(userFrom1, userTo1_1);
    static ConnectionTableEntry connectionTableEntry2 = getConnectionTableEntry(userFrom5, userTo5, userTo5.getIpAddress());
    static ConnectionTableEntry connectionTableEntryLvl2 = getConnectionTableEntryLvl2(userTo1_1, userTo1_2);
    static ConnectionTableEntry connectionTableEntryForFiltration1 = getConnectionTableEntry(userFrom2, userTo2_1);
    //static ConnectionTableEntry connectionTableEntryForFiltration2 = getConnectionTableEntryForFiltration(userTo2_1, userTo2_2);
    static ConnectionTableEntry connectionTableEntry1And2Level1 = getConnectionTableEntry(userFrom3, userTo3_1);
    static ConnectionTableEntry connectionTableEntry1And2Level2 = getConnectionTableEntry(userFrom3, userTo3_2);
    static ConnectionTableEntry connectionTableEntry1And2Level3 = getConnectionTableEntryLvl2(userTo3_1, userTo3_2);
    static ConnectionTableEntry connectionTableEntrySameLevelScore1 = getConnectionTableEntry(userFrom4, userTo4_1);
    static ConnectionTableEntry connectionTableEntrySameLevelScore2 = getConnectionTableEntry(userFrom4, userTo4_2);
    static ConnectionTableEntry connectionTableEntrySameLevelScore3 = getConnectionTableEntryLvl2(userTo4_1, userTo4_3);
    static ConnectionTableEntry connectionTableEntrySameLevelScore4 = getConnectionTableEntry(userTo4_2, userTo4_3);

    @BeforeAll
    static void setupConnectionTableEntry() throws Exception {
        connectionTableEntryForFiltration1.connectionScore = 0.8;
        insertObjectsToDb(CONNECTIONS_TABLE_NAME, List.of(connectionTableEntry, connectionTableEntryLvl2, connectionTableEntryForFiltration1, connectionTableEntry1And2Level1, connectionTableEntry1And2Level2, connectionTableEntry1And2Level3, connectionTableEntrySameLevelScore1, connectionTableEntrySameLevelScore2, connectionTableEntrySameLevelScore3, connectionTableEntrySameLevelScore4));
        waitForConnectionSearchToUpdate(userFrom1);
        waitForConnectionSearchToUpdate(userFrom2);
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by clientId success(200)")
    @AllureId("145")
    void getConnectionsByClientSuccessTest() throws Exception {
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
    @DisplayName("Connection search by client Api. Get no connection for connection only by ip (200)")
    @AllureId("1140")
    void getConnectionsByClientTest17() throws Exception {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntry2.userFrom);

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is not empty", responseBody.length, equalTo(0));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection by clientId with 1 and 2 level connections to the same client success(200)")
    @AllureId("939")
    void getConnectionsByClient1And2LevelSuccessTest() throws IOException {
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
    void getConnectionsByClientSameLevelScoreSuccessTest() throws IOException {
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
    void getConnectionsByClientAndConnectionDepthSuccessTest() throws IOException {
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
    void getConnectionsNoSuchClientIdSuccessTest() throws IOException {
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
    void getConnectionsNoSuchConnectionDepthSuccessTest() throws IOException {
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
    void getConnectionsConnectionScoreFromFiltrationSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntryForFiltration1.userFrom);
        queryParams.put("connectionScoreFrom", 0.7);

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
    void getConnectionsConnectionScoreToFiltrationSuccessTest() throws IOException {
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
        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsResponsesForFiltration[0]));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection with connectionType filtration success(200)")
    @AllureId("462")
    void getConnectionsConnectionTypeFiltrationSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntryForFiltration1.userFrom);
        queryParams.put("connectionType", List.of("Same Person"));

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is has 1 element", responseBody.length, equalTo(1));

        getConnectionsResponsesForFiltration[1].connectionDepth = 2;
        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsResponsesForFiltration[0]));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection with connectionAttributes filtration success(200)")
    @AllureId("463")
    void getConnectionsConnectionAttributesFiltrationSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", connectionTableEntryForFiltration1.userFrom);
        queryParams.put("connectionAttributes", List.of("payoutId"));

        Response response = getConnectionsByClientId(queryParams);
        GetConnectionsResponse[] responseBody = objectMapper.readValue(
                response.body().string(), GetConnectionsResponse[].class
        );

        assertThat("Check the response code is 200", response.code(), is(200));

        assertThat("Check the response body is has 1 element", responseBody.length, equalTo(1));

        getConnectionsResponsesForFiltration[1].connectionDepth = 2;
        assertThat("Check the response body", responseBody[0], equalTo(getConnectionsResponsesForFiltration[0]));
    }

    @Test
    @DisplayName("Connection search by client Api. Get connection without clientId Bad Request(400)")
    @AllureId("149")
    void getConnectionsNoClientIdBadRequestTest() throws IOException {
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
    void getConnectionsIncorrectClientIdFormatBadRequestTest() throws IOException {
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
    void getConnectionsConnectionDepthNotIntBadRequestTest() throws IOException {
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
    void getConnectionsConnectionScoreFromNotIntBadRequestTest() throws IOException {
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
    void getConnectionsConnectionScoreToNotIntBadRequestTest() throws IOException {
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
    void getConnectionsConnectionAttributesIncorrectBadRequestTest() throws IOException {
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
    static void deleteConnectionTableEntry() throws Exception {
        cleanConnectionsTableByClient(connectionTableEntry.userFrom, connectionTableEntryLvl2.userFrom, connectionTableEntryForFiltration1.userFrom, connectionTableEntry1And2Level1.userFrom, connectionTableEntry1And2Level2.userFrom, connectionTableEntry1And2Level3.userFrom, connectionTableEntrySameLevelScore1.userFrom, connectionTableEntrySameLevelScore2.userFrom, connectionTableEntrySameLevelScore3.userFrom, connectionTableEntrySameLevelScore4.userFrom);
    }
}
