package tests.clickHouseApiServiceTests;

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
public class GetCreditEquityTests extends TestBaseApi {

    @Disabled
    @Test
    @DisplayName("Clickhouse Api. Get credit equity request (200)")
    @AllureId("216")
    public void getCreditEquityTest1() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        //TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", ""); // Required
        queryParams.put("dateIndicator", "");
        queryParams.put("filterDateType", "");
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityResponse getCreditsResponse = objectMapper.readValue(response.body().string(), GetCreditEquityResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

}
