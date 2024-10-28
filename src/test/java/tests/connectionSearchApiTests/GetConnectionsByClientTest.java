package tests.connectionSearchApiTests;

import helpers.connectionSearchApi.GetConnectionsResponse;
import helpers.connectionSearchApi.GetConnectionsResponseError;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static helpers.connectionSearchApi.GetConnectionsRequest.getConnectionsByClientId;
import static helpers.connectionSearchApi.GetConnectionsResponseFactory.getConnectionsResponseErrorBadRequest;
import static helpers.connectionSearchApi.GetConnectionsResponseFactory.getConnectionsResponseSuccess;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

public class GetConnectionsByClientTest extends TestBaseApi {

    public final GetConnectionsResponse getConnectionsResponseSuccess = getConnectionsResponseSuccess();
    public final GetConnectionsResponseError getConnectionsResponseError = getConnectionsResponseErrorBadRequest();

    @Test
    @DisplayName("Connection search by client Api. Get connection by clientId success(200)")
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
    @Tag(TEAM_CORE)
    @AllureId("145")
    public void getConnectionsByClientSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "vantage-10079042");

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
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
    @Tag(TEAM_CORE)
    @AllureId("146")
    public void getConnectionsByClientAndConnectionDepthSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "vantage-10079042");
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
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
    @Tag(TEAM_CORE)
    @AllureId("147")
    public void getConnectionsNoSuchClientIdSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "test_not_existing_client");
        queryParams.put("connectionDepth", 1);

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
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
    @Tag(TEAM_CORE)
    @AllureId("148")
    public void getConnectionsNoSuchConnectionDepthSuccessTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "vantage-10079042");
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
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
    @Tag(TEAM_CORE)
    @AllureId("149")
    public void getConnectionsNoClientIdBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("connectionDepth", 1);

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
    @Owner(OWNER_FEDOR_NESTEROVICH)
    @Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
    @Tag(TEAM_CORE)
    @AllureId("150")
    public void getConnectionsConnectionDepthNotIntBadRequestTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "vantage-10079042");
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
}
