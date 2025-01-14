package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getUnclosedTrades.GetUnclosedTradesResponse;
import businessObjects.db.clickhouse.mtMt5DealsCoercedTable.Mt5DealsCoercedObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;
import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getUnclosedTrades.GetUnclosedTradesRequest.getUnclosedTrades;
import static businessObjects.db.clickhouse.mtMt5DealsCoercedTable.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_UNCLOSED_TRADES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetUnclosedTradesTests extends TestBaseApi {

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
    public static void setupTests() throws ReflectiveOperationException, SQLException {
        trade1 = generateTradeByClient(client1, 0, 0, 0, Utils.getRandomLongPositive());
        trade2 = generateTradeByClient(client2, 0, 0, 0, Utils.getRandomLongPositive());
        trade3 = generateTradeByClient(client3, 0, 0, 0, Utils.getRandomLongPositive());
        trade3.comment = null;
        trade4 = generateTradeByClient(client4, 0, 0, 0, Utils.getRandomLongPositive());
        trade4.time = "2025-01-01 00:00:00";
        trade4.profit = 1D;
        trade5 = generateTradeByClient(client4, 0, 0, 0, Utils.getRandomLongPositive());
        trade5.time = "2025-01-02 00:00:00";
        trade6 = generateTradeByClient(client5, 0, 0, 0, Utils.getRandomLongPositive());
        trade7 = generateTradeByClient(client5, 0, 0, 0, Utils.getRandomLongPositive());
        trade8 = generateTradeByClient(client5, 0, 0, 0, Utils.getRandomLongPositive());
        trade5.profit = 2D;
        trade6.serverId = 10;
        trade7.serverId = 10;
        trade8.serverId = 11;
        trade6.entry = 0;
        trade7.entry = 1;
        trade8.entry = 0;
        trade6.positionId = 1000L;
        trade7.positionId = 1000L;
        trade8.positionId = 1000L;
        trade6.deal = 1;
        trade7.deal = 2;
        trade8.deal = 3;
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, trade1);
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, trade2);
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, trade3);
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, trade4);
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, trade5);
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, trade6);
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, trade7);
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, trade8);
    }

    @AfterAll
    public static void teardownTests() throws SQLException {
        deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("account = %s", client1.getTradingAccount()));
        deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("account = %s", client2.getTradingAccount()));
        deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("account = %s", client3.getTradingAccount()));
        deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("account = %s", client4.getTradingAccount()));
        deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("account = %s", client5.getTradingAccount()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by required params + limit")
    @AllureId("703")
    public void getUnclosedTradesTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount());
        queryParams.put("serverId", client1.getServerId());
        queryParams.put("limit", 1);
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(1));
        assertThat("Assert tradeId", mappedResponse.getFirst().tradeId, is(trade1.deal));
        assertThat("Assert tradeDate", mappedResponse.getFirst().tradeDate, is(formatTimeToUtc(trade1.time)));
        assertThat("Assert tradingAccount", mappedResponse.getFirst().tradingAccount, is(trade1.account));
        assertThat("Assert profitUSD", mappedResponse.getFirst().profitUsd, is(trade1.profit));
        assertThat("Assert profit", mappedResponse.getFirst().profit, is(trade1.profit));
        assertThat("Assert comment", mappedResponse.getFirst().comment, is(trade1.comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by all params")
    @AllureId("704")
    public void getUnclosedTradesTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount());
        queryParams.put("serverId", client1.getServerId());
        queryParams.put("dateFrom", "2023-12-27T00:00:00");
        queryParams.put("dateTo", "2025-12-27T00:00:00");
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "asc");
        queryParams.put("limit", 1);
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(1));
        assertThat("Assert tradeId", mappedResponse.getFirst().tradeId, is(trade1.deal));
        assertThat("Assert tradeDate", mappedResponse.getFirst().tradeDate, is(formatTimeToUtc(trade1.time)));
        assertThat("Assert tradingAccount", mappedResponse.getFirst().tradingAccount, is(trade1.account));
        assertThat("Assert profitUSD", mappedResponse.getFirst().profitUsd, is(trade1.profit));
        assertThat("Assert profit", mappedResponse.getFirst().profit, is(trade1.profit));
        assertThat("Assert comment", mappedResponse.getFirst().comment, is(trade1.comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong limit")
    @AllureId("705")
    public void getUnclosedTradesTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("limit", "a");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response type", mappedResponse.type, is("about:blank"));
        assertThat("Assert response title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert response status", mappedResponse.status, is(400));
        assertThat("Assert response details", mappedResponse.detail, is("Failed to convert 'limit' with value: 'a'"));
        assertThat("Assert response instance", mappedResponse.instance, is("/v1/unclosedTrades"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades no serverId")
    @AllureId("706")
    public void getUnclosedTradesTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
        assertThat("Assert response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades no tradingAccount")
    @AllureId("707")
    public void getUnclosedTradesTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", client1.getServerId());
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Assert response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong dateFrom")
    @AllureId("708")
    public void getUnclosedTradesTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("dateFrom", "a");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response type", mappedResponse.type, is("about:blank"));
        assertThat("Assert response title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert response status", mappedResponse.status, is(400));
        assertThat("Assert response details", mappedResponse.detail, is("Failed to convert 'dateFrom' with value: 'a'"));
        assertThat("Assert response instance", mappedResponse.instance, is("/v1/unclosedTrades"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong dateTo")
    @AllureId("709")
    public void getUnclosedTradesTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("dateTo", "a");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response type", mappedResponse.type, is("about:blank"));
        assertThat("Assert response title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert response status", mappedResponse.status, is(400));
        assertThat("Assert response details", mappedResponse.detail, is("Failed to convert 'dateTo' with value: 'a'"));
        assertThat("Assert response instance", mappedResponse.instance, is("/v1/unclosedTrades"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong sortOrder")
    @AllureId("710")
    public void getUnclosedTradesTest8() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("sortOrder", "a");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.error, is("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
        assertThat("Assert response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong orderBy")
    @AllureId("711")
    public void getUnclosedTradesTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("orderBy", "a");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.error, is("Invalid &quot;orderBy&quot; property format. The property may include only: createTime, actualAmount, actualAmountUSD"));
        assertThat("Assert response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades no comment")
    @AllureId("712")
    public void getUnclosedTradesTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client3.getTradingAccount());
        queryParams.put("serverId", client3.getServerId());
        queryParams.put("limit", 1);
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert comment", mappedResponse.getFirst().comment, is(""));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=createTime sortOrder=desc")
    @AllureId("713")
    public void getUnclosedTradesTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradeId", mappedResponse.getFirst().tradeDate, is("2025-01-02T00:00:00Z"));
        assertThat("Assert tradeId", mappedResponse.getLast().tradeDate, is("2025-01-01T00:00:00Z"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=createTime sortOrder=asc")
    @AllureId("714")
    public void getUnclosedTradesTest18() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "asc");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradeId", mappedResponse.getFirst().tradeDate, is("2025-01-01T00:00:00Z"));
        assertThat("Assert tradeId", mappedResponse.getLast().tradeDate, is("2025-01-02T00:00:00Z"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=actualAmount and sortOrder=asc")
    @AllureId("715")
    public void getUnclosedTradesTest12() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "actualAmount");
        queryParams.put("sortOrder", "asc");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradeId", mappedResponse.getFirst().profit, is(1d));
        assertThat("Assert tradeId", mappedResponse.getLast().profit, is(2d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=actualAmount and sortOrder=asc")
    @AllureId("716")
    public void getUnclosedTradesTest19() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "actualAmount");
        queryParams.put("sortOrder", "asc");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert profit", mappedResponse.getFirst().profit, is(1d));
        assertThat("Assert profit", mappedResponse.getLast().profit, is(2d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=actualAmountUSD and sortOrder=desc")
    @AllureId("717")
    public void getUnclosedTradesTest13() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "actualAmountUSD");
        queryParams.put("sortOrder", "desc");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradeId", mappedResponse.getFirst().profitUsd, is(2d));
        assertThat("Assert tradeId", mappedResponse.getLast().profitUsd, is(1d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by orderBy=actualAmountUSD and sortOrder=desc")
    @AllureId("718")
    public void getUnclosedTradesTest20() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        queryParams.put("orderBy", "actualAmountUSD");
        queryParams.put("sortOrder", "desc");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradeId", mappedResponse.getFirst().profitUsd, is(2d));
        assertThat("Assert tradeId", mappedResponse.getLast().profitUsd, is(1d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong serverId")
    @AllureId("719")
    public void getUnclosedTradesTest14() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", "trade1.serverId");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.error, is("Invalid serverId format: serverId must be a string that can be parsed into an integer"));
        assertThat("Assert response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades by wrong trading account")
    @AllureId("720")
    public void getUnclosedTradesTest15() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "trade1.login");
        queryParams.put("serverId", trade1.serverId);
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert response error", mappedResponse.error, is("Invalid tradingAccount format: tradingAccount must be a string that can be parsed into a long"));
        assertThat("Assert response status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades empty with dateFrom")
    @AllureId("721")
    public void getUnclosedTradesTest16() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount());
        queryParams.put("serverId", client1.getServerId());
        queryParams.put("dateFrom", "2030-01-01T00:00:00");
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(0));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades empty with dateTo")
    @AllureId("722")
    public void getUnclosedTradesTest17() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount());
        queryParams.put("serverId", client1.getServerId());
        queryParams.put("dateTo", "2020-01-01T00:00:00");

        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(0));
    }

    @Test
    @DisplayName("Clickhouse Api. Get unclosed trades with same positionId from different servers")
    @AllureId("723")
    public void getUnclosedTradesTest21() throws IOException {
        client5.setServerId(11);
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client5.getTradingAccount());
        queryParams.put("serverId", client5.getServerId());
        queryParams.put("limit", 1);
        Response response = getUnclosedTrades(queryParams);

        assert response.body() != null;
        List<GetUnclosedTradesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(1));
        assertThat("Assert response length", mappedResponse.getFirst().tradeId, is(3));

    }
}
