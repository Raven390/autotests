package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getCreditEquityRatio.GetCreditEquityRatioResponseError;
import businessObjects.api.clickhouseApiService.getCreditEquityRatio.GetCreditEquityResponse;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getCreditEquityRatio.GetCreditEquityRequest.getCreditEquity;
import static helpers.data.ClientFactory.getRandomClient;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CREDIT_EQUITY)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Disabled
public class GetCreditEquityRatioTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get credit by date range (200)")
    @AllureId("")
    public void getCreditEquityRatioTest7() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        //TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", ""); // Required
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradingAccount", mappedResponse.tradingAccount, is(1_241_413));
        assertThat("Assert tradingIndicators size", mappedResponse.tradingIndicators.size(), is(2));
        assertThat("Assert tradingIndicators currentEquity", mappedResponse.tradingIndicators.getFirst().currentEquity, is(10_500.75));
        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.getFirst().indicatorDate, is("2024-11-04T12:13:24"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity request without params (400)")
    @AllureId("")
    public void getCreditEquityRatioTest1() throws IOException, ReflectiveOperationException, SQLException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityRatioResponseError
                mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityRatioResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity request only by tradingAccount(400)")
    @AllureId("")
    public void getCreditEquityRatioTest2() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityRatioResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityRatioResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity request only by serverId (400)")
    @AllureId("")
    public void getCreditEquityRatioTest3() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", ""); // Required
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityRatioResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityRatioResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity request (200)")
    @AllureId("")
    public void getCreditEquityRatioTest4() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        //TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", ""); // Required
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity request by dateFrom (200)")
    @AllureId("")
    public void getCreditEquityRatioTest5() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        //TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", ""); // Required
        queryParams.put("dateFrom", "");
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity request by dateTo(200)")
    @AllureId("")
    public void getCreditEquityRatioTest6() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        //TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", ""); // Required
        queryParams.put("dateTo", "");
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

}
