package tests.production_tests;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.ConfigFactory.*;
import static utils.ConfigFactory.CONNECTION_SEARCH_GET_CONNECTIONS_BY_ATTRIBUTES;
import static utils.Constants.*;

@Tag(LAYER_API)
@Tag(SUITE_SMOKE_PROD)
@Story(STORY_PRODUCTION_TESTS)
@Feature(FEATURE_PRODUCTION_TESTS_CONNECTION_SEARCH)
class ConnectionSearchProductionSmokeTests {

    @Test
    @DisplayName("Connections smoke test. getAbuseTypes by attributes success (200)")
    @AllureId("795")
    void testConnectionSearchProd1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", "test@test.com");
        Response response = new HttpHelper().sendGetRequest(CONNECTION_SEARCH_BASE_PATH_PROD + CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_ATTRIBUTES, null, queryParams);
        assertThat("Check the response code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Connections smoke test. getAbuseTypes by client id success (200)")
    @AllureId("796")
    void testConnectionSearchProd2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "vantage-123");
        Response response = new HttpHelper().sendGetRequest(CONNECTION_SEARCH_BASE_PATH_PROD + CONNECTION_SEARCH_GET_ABUSE_TYPES_BY_CLIENT_V1, null, queryParams);
        assertThat("Check the response code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Connections smoke test. getConnections by client id success (200)")
    @AllureId("797")
    void testConnectionSearchProd3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "vantage-123");
        Response response = new HttpHelper().sendGetRequest(CONNECTION_SEARCH_BASE_PATH_PROD + CONNECTION_SEARCH_GET_CONNECTIONS_BY_CLIENT, null, queryParams);
        assertThat("Check the response code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Connections smoke test. getConnections by attributes success (200)")
    @AllureId("798")
    void testConnectionSearchProd4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("emailAddress", "test@test.com");
        Response response = new HttpHelper().sendGetRequest(CONNECTION_SEARCH_BASE_PATH_PROD + CONNECTION_SEARCH_GET_CONNECTIONS_BY_ATTRIBUTES, null, queryParams);
        assertThat("Check the response code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Connections smoke test. Check connected ib (200)")
    @AllureId("898")
    void testConnectionSearchProd5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "vantage-123");
        queryParams.put("emailAddress", "mail@mail.com");
        Response response = new HttpHelper().sendGetRequest(CONNECTION_SEARCH_BASE_PATH_PROD + CONNECTION_SEARCH_GET_CHECK_CONNECTED_IB, null, queryParams);
        assertThat("Check the response code is 200", response.code(), is(200));
    }
}
