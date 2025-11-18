package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_unclosed_trades.GetUnclosedTradesResponse;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;
import utils.Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.clickhouse_api_service.get_unclosed_trades.GetUnclosedTradesRequest.getUnclosedTrades;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanMt5CoercedTableByAccount;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_UNCLOSED_TRADES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetUnclosedTradesTests extends TestBaseApi {

    private static Mt5DealsCoercedObject trade1;
    private static Mt5DealsCoercedObject trade2;
    private static Mt5DealsCoercedObject trade3;
    private static Mt5DealsCoercedObject trade4;
    private static Mt5DealsCoercedObject trade5;
    private static Mt5DealsCoercedObject trade6;
    private static Mt5DealsCoercedObject trade7;
    private static Mt5DealsCoercedObject trade8;

    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client4 = getRandomVantageClientAllFields();
    private static final ClientHelper client5 = getRandomVantageClientAllFields();

    @BeforeAll
    static void setup() {
        trade1 = generateTradeByClient(client1, 0, 0, 0, Utils.getRandomLongPositive());
        trade2 = generateTradeByClient(client2, 0, 0, 0, Utils.getRandomLongPositive());
        trade3 = generateTradeByClient(client3, 0, 0, 0, Utils.getRandomLongPositive());
        trade3.setComment(null);
        trade4 = generateTradeByClient(client4, 0, 0, 0, Utils.getRandomLongPositive());
        trade4.setTime("2025-01-01 00:00:00");
        trade4.setProfit(1D);
        trade4.setProfitUsd(1D);
        trade5 = generateTradeByClient(client4, 0, 0, 0, Utils.getRandomLongPositive());
        trade5.setTime("2025-01-02 00:00:00");
        trade6 = generateTradeByClient(client5, 0, 0, 0, Utils.getRandomLongPositive());
        trade7 = generateTradeByClient(client5, 0, 0, 0, Utils.getRandomLongPositive());
        trade8 = generateTradeByClient(client5, 0, 0, 0, Utils.getRandomLongPositive());
        trade5.setProfit(2D);
        trade5.setProfitUsd(2D);
        trade6.setServerId(10);
        trade7.setServerId(10);
        trade8.setServerId(11);
        trade6.setEntry(0);
        trade7.setEntry(1);
        trade8.setEntry(0);
        trade6.setPositionId(1000L);
        trade7.setPositionId(1000L);
        trade8.setPositionId(1000L);
        trade6.setDeal(1L);
        trade7.setDeal(2L);
        trade8.setDeal(3L);
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8));
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanMt5CoercedTableByAccount(client1.getTradingAccount(), client2.getTradingAccount(), client3.getTradingAccount(), client4.getTradingAccount(), client5.getTradingAccount());
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by required params + limit")
    @AllureId("703")
    void getUnclosedTradesTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount());
        queryParams.put("serverId", client1.getServerId());
        queryParams.put("limit", 1);
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(1));
        assertThat("Assert tradeId", mappedResponse.getFirst().tradeId, is(trade1.getDeal()));
        assertThat("Assert tradeDate", mappedResponse.getFirst().tradeDate, is(formatTimeToUtc(trade1.getTime())));
        assertThat("Assert tradingAccount", mappedResponse.getFirst().tradingAccount, is(trade1.getAccount()));
        assertThat("Assert profitUSD", mappedResponse.getFirst().profitUsd, is(trade1.getProfit()));
        assertThat("Assert profit", mappedResponse.getFirst().profit, is(trade1.getProfit()));
        assertThat("Assert comment", mappedResponse.getFirst().comment, is(trade1.getComment()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by all params")
    @AllureId("704")
    void getUnclosedTradesTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount());
        queryParams.put("serverId", client1.getServerId());
        queryParams.put("dateFrom", "2023-12-27T00:00:00");
        queryParams.put("dateTo", "2025-12-27T00:00:00");
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "asc");
        queryParams.put("limit", 1);
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(1));
        assertThat("Assert tradeId", mappedResponse.getFirst().tradeId, is(trade1.getDeal()));
        assertThat("Assert tradeDate", mappedResponse.getFirst().tradeDate, is(formatTimeToUtc(trade1.getTime())));
        assertThat("Assert tradingAccount", mappedResponse.getFirst().tradingAccount, is(trade1.getAccount()));
        assertThat("Assert profitUSD", mappedResponse.getFirst().profitUsd, is(trade1.getProfit()));
        assertThat("Assert profit", mappedResponse.getFirst().profit, is(trade1.getProfit()));
        assertThat("Assert comment", mappedResponse.getFirst().comment, is(trade1.getComment()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong limit")
    @AllureId("705")
    void getUnclosedTradesTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("limit", "a");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response type", mappedResponse.getType(), is("about:blank"));
        assertThat("Assert response title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert response status", mappedResponse.getStatus(), is(400));
        assertThat("Assert response details", mappedResponse.getDetail(), is("Failed to convert 'limit' with value: 'a'"));
        assertThat("Assert response instance", mappedResponse.getInstance(), is("/v1/unclosedTrades"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades no serverId")
    @AllureId("706")
    void getUnclosedTradesTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.getError(), is("Required request parameter 'serverId' for method parameter type String is not present"));
        assertThat("Assert response status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades no tradingAccount")
    @AllureId("707")
    void getUnclosedTradesTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", client1.getServerId());
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.getError(), is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Assert response status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong dateFrom")
    @AllureId("708")
    void getUnclosedTradesTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("dateFrom", "a");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response type", mappedResponse.getType(), is("about:blank"));
        assertThat("Assert response title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert response status", mappedResponse.getStatus(), is(400));
        assertThat("Assert response details", mappedResponse.getDetail(), is("Failed to convert 'dateFrom' with value: 'a'"));
        assertThat("Assert response instance", mappedResponse.getInstance(), is("/v1/unclosedTrades"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong dateTo")
    @AllureId("709")
    void getUnclosedTradesTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("dateTo", "a");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response type", mappedResponse.getType(), is("about:blank"));
        assertThat("Assert response title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert response status", mappedResponse.getStatus(), is(400));
        assertThat("Assert response details", mappedResponse.getDetail(), is("Failed to convert 'dateTo' with value: 'a'"));
        assertThat("Assert response instance", mappedResponse.getInstance(), is("/v1/unclosedTrades"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong sortOrder")
    @AllureId("710")
    void getUnclosedTradesTest8() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("sortOrder", "a");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.getError(), is("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
        assertThat("Assert response status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong orderBy")
    @AllureId("711")
    void getUnclosedTradesTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("orderBy", "a");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.getError(), is("Invalid &quot;orderBy&quot; property format. The property may include only: createTime, actualAmount, actualAmountUSD"));
        assertThat("Assert response status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades no comment")
    @AllureId("712")
    void getUnclosedTradesTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client3.getTradingAccount());
        queryParams.put("serverId", client3.getServerId());
        queryParams.put("limit", 1);
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert comment", mappedResponse.getFirst().comment, is(""));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=createTime sortOrder=desc")
    @AllureId("713")
    void getUnclosedTradesTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradeId", mappedResponse.getFirst().tradeDate, is("2025-01-02T00:00:00Z"));
        assertThat("Assert tradeId", mappedResponse.getLast().tradeDate, is("2025-01-01T00:00:00Z"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=createTime sortOrder=asc")
    @AllureId("714")
    void getUnclosedTradesTest18() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "asc");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradeId", mappedResponse.getFirst().tradeDate, is("2025-01-01T00:00:00Z"));
        assertThat("Assert tradeId", mappedResponse.getLast().tradeDate, is("2025-01-02T00:00:00Z"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=actualAmount and sortOrder=asc")
    @AllureId("715")
    void getUnclosedTradesTest12() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "actualAmount");
        queryParams.put("sortOrder", "asc");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradeId", mappedResponse.getFirst().profit, is(1d));
        assertThat("Assert tradeId", mappedResponse.getLast().profit, is(2d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=actualAmount and sortOrder=asc")
    @AllureId("716")
    void getUnclosedTradesTest19() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "actualAmountUSD");
        queryParams.put("sortOrder", "asc");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert profit", mappedResponse.getFirst().profitUsd, is(1d));
        assertThat("Assert profit", mappedResponse.getLast().profitUsd, is(2d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=actualAmount and sortOrder=desc")
    @AllureId("717")
    void getUnclosedTradesTest13() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "actualAmount");
        queryParams.put("sortOrder", "desc");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert profitUsd", mappedResponse.getFirst().profit, is(2d));
        assertThat("Assert profitUsd", mappedResponse.getLast().profit, is(1d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=actualAmountUSD and sortOrder=desc")
    @AllureId("718")
    void getUnclosedTradesTest20() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "actualAmountUSD");
        queryParams.put("sortOrder", "desc");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradeId", mappedResponse.getFirst().profitUsd, is(2d));
        assertThat("Assert tradeId", mappedResponse.getLast().profitUsd, is(1d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong serverId")
    @AllureId("719")
    void getUnclosedTradesTest14() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", "trade1.serverId");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.getError(), is("Invalid serverId format: serverId must be a string that can be parsed into an integer"));
        assertThat("Assert response status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong trading account")
    @AllureId("720")
    void getUnclosedTradesTest15() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "trade1.login");
        queryParams.put("serverId", trade1.getServerId());
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.getError(), is("Invalid tradingAccount format: tradingAccount must be a string that can be parsed into a long"));
        assertThat("Assert response status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades empty with dateFrom")
    @AllureId("721")
    void getUnclosedTradesTest16() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount());
        queryParams.put("serverId", client1.getServerId());
        queryParams.put("dateFrom", "2030-01-01T00:00:00");
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(0));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades empty with dateTo")
    @AllureId("722")
    void getUnclosedTradesTest17() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount());
        queryParams.put("serverId", client1.getServerId());
        queryParams.put("dateTo", "2020-01-01T00:00:00");

        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(0));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades with same positionId from different servers")
    @AllureId("723")
    void getUnclosedTradesTest21() throws IOException {
        client5.setServerId(11);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client5.getTradingAccount());
        queryParams.put("serverId", client5.getServerId());
        queryParams.put("limit", 1);
        Response response = getUnclosedTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(1));
        assertThat("Assert response length", mappedResponse.getFirst().tradeId, is(3L));
    }
}
