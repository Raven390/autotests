package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.get_trade_by_id.GetTradeResponse;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.clickhouse_api_service.get_trade_by_id.GetTradeRequest.getTrade;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.CleanTableHelper.cleanMt5CoercedTableByUcid;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;
import static utils.Utils.formatTimeToUtc;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_TRADES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetTradeByTradeIdTests extends TestBaseApi {

    private static ClientHelper clientHelper = getRandomVantageClient();
    private static Mt5DealsCoercedObject trade1;


    @BeforeAll
    static void setup() {
        trade1 = generateTradeByClient(clientHelper);
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(trade1));
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanMt5CoercedTableByUcid(clientHelper.getUcid());
    }

    @Test
    @AllureId("1503")
    @DisplayName("Clickhouse Api. Get Trade data by id")
    void getTradeByIdTest1() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", clientHelper.getTradingAccount());
        queryParams.put("serverId", clientHelper.getServerId());
        queryParams.put("tradeId", trade1.getDeal());
        Response response = getTrade(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetTradeResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradeResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(1));
        assertThat("Assert tradeId", mappedResponse[0].getTradeId(), is(trade1.getDeal()));
        assertThat("Assert tradingAccount", mappedResponse[0].getTradingAccount(), is(trade1.getAccount()));
        assertThat("Assert tradeDate", mappedResponse[0].getTradeDate(), is(formatTimeToUtc(trade1.getTime())));
        assertThat("Assert action", mappedResponse[0].getAction(), is(trade1.getAction()));
        assertThat("Assert entry", mappedResponse[0].getEntry(), is(trade1.getEntry()));
        assertThat("Assert symbol", mappedResponse[0].getSymbol(), is(trade1.getSymbol()));
        assertThat("Assert profitUSD", mappedResponse[0].getProfitUSD(), is(trade1.getProfitUsd()));
        assertThat("Assert profit", mappedResponse[0].getProfit(), is(trade1.getProfit()));
        assertThat("Assert volumeInLots", mappedResponse[0].getVolumeInLots(), is(trade1.getVolumeLots()));
        assertThat("Assert comment", mappedResponse[0].getComment(), is(trade1.getComment()));
        assertThat("Assert symbolUnderlying", mappedResponse[0].getSymbolUnderlying(), is("EURUSD"));
    }
}
