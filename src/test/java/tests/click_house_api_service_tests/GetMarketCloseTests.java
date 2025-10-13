package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_market_close.GetMarketCloseResponse;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static business_objects.api.clickhouse_api_service.get_market_close.GetMarketCloseRequest.getMarketClose;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_MARKET_CLOSE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetMarketCloseTests extends TestBaseApi {

    @Test
    @AllureId("1679")
    @DisplayName("Clickhouse Api. Get Market close with time= 2025-10-13T08:07:09.002338Z (200)")
    void getMarketCloseTest1() throws IOException {
        Response response = getMarketClose("AAPL", "1", "2025-10-13T08:07:09.002338Z");
        GetMarketCloseResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMarketCloseResponse.class);
        assertThat("Check close time", mappedResponse.getMarketCloseTime(), is("2025-10-13T23:00:00Z"));
    }

    @Test
    @AllureId("1680")
    @DisplayName("Clickhouse Api. Get Market close with time 2025-10-13T08:07:09Z (200)")
    void getMarketCloseTest2() throws IOException {
        Response response = getMarketClose("AAPL", "1", "2025-10-13T08:07:09Z");
        GetMarketCloseResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMarketCloseResponse.class);
        assertThat("Check close time", mappedResponse.getMarketCloseTime(), is("2025-10-13T23:00:00Z"));
    }

    @Test
    @AllureId("1681")
    @DisplayName("Clickhouse Api. Get Market close no symbol (200)")
    void getMarketCloseTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", "1");
        queryParams.put("date", "2025-10-13T08:07:09Z");// Required
        Response response = getMarketClose(queryParams);
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Check close time", mappedResponse.getError(), is("Required request parameter 'symbol' for method parameter type String is not present"));
        assertThat("Check close time", mappedResponse.getStatus(), is(400));
    }

    @Test
    @AllureId("1682")
    @DisplayName("Clickhouse Api. Get Market close  no server id (400)")
    void getMarketCloseTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("symbol", "AAPL");
        queryParams.put("date", "2025-10-13T08:07:09Z");// Required
        Response response = getMarketClose(queryParams);
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Check close time", mappedResponse.getError(), is("Required request parameter 'serverId' for method parameter type String is not present"));
        assertThat("Check close time", mappedResponse.getStatus(), is(400));
    }

    @Test
    @AllureId("1683")
    @DisplayName("Clickhouse Api. Get Market close no date (200)")
    void getMarketCloseTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("symbol", "AAPL");
        queryParams.put("serverId", "1");
        Response response = getMarketClose(queryParams);
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Check close time", mappedResponse.getError(), is("Required request parameter 'date' for method parameter type LocalDateTime is not present"));
        assertThat("Check close time", mappedResponse.getStatus(), is(400));
    }

    @Test
    @AllureId("1684")
    @DisplayName("Clickhouse Api. Get Market close with wrong Symbol (404)")
    void getMarketCloseTest6() throws IOException {
        Response response = getMarketClose("123", "1", "2025-10-13T08:07:09.002338Z");
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Check close time", mappedResponse.getError(), is("Server or symbol not found"));
        assertThat("Check close time", mappedResponse.getStatus(), is(404));
    }

    @Test
    @AllureId("1685")
    @DisplayName("Clickhouse Api. Get Market close with wrong serverId (404)")
    void getMarketCloseTest7() throws IOException {
        Response response = getMarketClose("AAPL", "123", "2025-10-13T08:07:09.002338Z");
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Check close time", mappedResponse.getError(), is("Server or symbol not found"));
        assertThat("Check close time", mappedResponse.getStatus(), is(404));
    }

    @Test
    @AllureId("1686")
    @DisplayName("Clickhouse Api. Get Market close with wrong date (400)")
    void getMarketCloseTest8() throws IOException {
        Response response = getMarketClose("AAPL", "1", "2025");
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Check close time", mappedResponse.getDetail(), is("Failed to convert 'date' with value: '2025'"));
        assertThat("Check close time", mappedResponse.getStatus(), is(400));
    }

}
