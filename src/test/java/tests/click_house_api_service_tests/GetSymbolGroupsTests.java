package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_symbol_groups.GetSymbolGroupsRequest.getSymbolGroups;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_CLICKHOUSE_API_SERVICE;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_symbol_groups.GetSymbolGroupsResponse;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_SYMBOL_GROUPS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetSymbolGroupsTests extends TestBaseApi {

    @BeforeAll
    static void setup() throws IOException {
        // No test data needed for these endpoint checks
    }

    @AfterAll
    static void delete() throws Exception {
        // No cleanup needed
    }

    @Test
    @AllureId("1821")
    @DisplayName("Clickhouse Api. Get symbol groups - 200 when symbol exists")
    void getSymbolGroupsTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("symbol", "EURUSDpro");
        Response response = getSymbolGroups(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetSymbolGroupsResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetSymbolGroupsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert that code is 200", mappedResponse.getGroup(), is("Forex"));
    }

    @Test
    @AllureId("1822")
    @DisplayName("Clickhouse Api. Get symbol groups - 400 when symbol is missing")
    void getSymbolGroupsTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getSymbolGroups(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat(
                "Error key should be present",
                mappedResponse.getError(),
                is("Required request parameter 'symbol' for method parameter type String is not present"));
        assertThat("Status should equal 404", mappedResponse.getStatus(), is(400));
    }

    @Test
    @AllureId("1823")
    @DisplayName("Clickhouse Api. Get symbol groups - 404 when symbol not found")
    void getSymbolGroupsTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("symbol", "ACVKSJ");
        Response response = getSymbolGroups(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 404", response.code(), is(404));

        assertThat(
                "Error key should be present",
                mappedResponse.getError(),
                is("SymbolGroup data is not found for the request with parameters: {symbol=ACVKSJ}."));
        assertThat("Status should equal 404", mappedResponse.getStatus(), is(404));
    }
}
