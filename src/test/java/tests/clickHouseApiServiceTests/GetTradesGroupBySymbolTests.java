package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getTrades.GetTradesResponse;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Muted;
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

import static businessObjects.api.clickhouseApiService.getTradesGroupBy.GetTradesGroupByRequest.getTradesGroupBySymbol;
import static helpers.data.ClientFactory.getRandomClient;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_TRADES_GROUP_BY_SYMBOL)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Tag(TAG_MANUAL)
@Disabled
@Muted
public class GetTradesGroupBySymbolTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request (200)")
    @AllureId("213")
    public void getTradesTest1() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("tradeDate", "");
        queryParams.put("action", "");
        queryParams.put("entry", "");
        queryParams.put("filterDateType", ""); // Enum: period, before_date
        queryParams.put("orderBy", ""); // Enum - bonusDate, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", ""); // Enum - asc, desc
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesResponse getCreditsResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

}
