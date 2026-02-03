package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_count_trading_days.GetCountTradingDaysRequest.getCountTradingDays;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.DataSetupHelper.setupData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_count_trading_days.GetCountTradingDaysResponse;
import helpers.data.DataHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_COUNT_TRADING_DAYS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetCountTradingDays extends TestBaseApi {

    @BeforeAll
    static void setup() {}

    @AfterAll
    static void teardown() {}

    @Test
    @AllureId("2117")
    @DisplayName("Clickhouse Api. Get count trading days (200)")
    void getCountTradingDaysTest1() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of("vantage-81094980"));
        Response response = getCountTradingDays(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetCountTradingDaysResponse> mappedResponse = Arrays.stream(
                        objectMapper.readValue(response.body().string(), GetCountTradingDaysResponse[].class))
                .toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response", mappedResponse.getFirst().getCount(), is(2));
    }

    @Test
    @AllureId("2118")
    @DisplayName("Clickhouse Api. Get count trading days for >1 users(200)")
    void getCountTradingDaysTest2() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of("vantage-81094980", "vantage-76985032"));
        Response response = getCountTradingDays(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetCountTradingDaysResponse> mappedResponse = Arrays.stream(
                        objectMapper.readValue(response.body().string(), GetCountTradingDaysResponse[].class))
                .toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response", mappedResponse.getFirst().getCount(), is(6));
    }

    @Test
    @AllureId("2119")
    @DisplayName("Clickhouse Api. Get count trading days for 0 users(400)")
    void getCountTradingDaysTest3() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());
        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(""));
        Response response = getCountTradingDays(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));
        assertThat("Assert response", mappedResponse.getError(), is("{jakarta.validation.constraints.Size.message}"));
    }

    @Test
    @AllureId("2120")
    @DisplayName("Clickhouse Api. Get count trading days, no param(400)")
    void getCountTradingDaysTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getCountTradingDays(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));
        assertThat(
                "Assert response",
                mappedResponse.getError(),
                is(
                        "Invalid \"clientId\" property format. The property clientId must contain brand and userId divided by a dash e.g., vantage-2068746030"));
    }

    @Test
    @AllureId("2121")
    @DisplayName("Clickhouse Api. Get count trading days for 0 users(400)")
    void getCountTradingDaysTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of("123"));
        Response response = getCountTradingDays(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));
        assertThat(
                "Assert response", mappedResponse.getError(), is("{jakarta.validation.constraints.Pattern.message}"));
    }
}
