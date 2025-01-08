package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getTrades.GetTradesResponse;
import businessObjects.db.clickhouse.mtMt5DealsCoercedTable.Mt5DealsCoercedObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getTrades.GetTradesRequest.getTrades;
import static businessObjects.db.clickhouse.mtMt5DealsCoercedTable.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_TRADES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetTradesTests extends TestBaseApi {

    private static ClientHelper client1 = getRandomVantageClient();
    private static Mt5DealsCoercedObject trade1;
    private static Mt5DealsCoercedObject trade2;
    private static Integer account;
    private static final Integer serverId = 24;

    @BeforeAll
    public static void setupTrades() throws ReflectiveOperationException, SQLException {
        account = getRandomIntPositive();
        trade1 = generateTradeByClient(client1);
        trade2 = generateTradeByClient(client1);
        trade2.time = getTomorrowTimestampDbFormat();
        trade2.profit = 2.0;
        trade2.action = 2;
        trade2.entry = 2;
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, trade1);
        insertObjectToDb(MT5_DEALS_COERCED_TABLE_NAME, trade2);
    }

//    @AfterAll
//    public static void teardownTrades() throws SQLException {
//        deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("account = %s", account));
//    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades by all params")
    @AllureId("214")
    public void getTradesAllParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("dateFrom", trade1.time.replace(" ", "T"));
        queryParams.put("dateTo", trade2.time.replace(" ", "T"));
        queryParams.put("action", trade1.action);
        queryParams.put("entry", trade1.entry);
        queryParams.put("orderBy", "tradeDate");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(1));
        assertThat("Assert tradeId", mappedResponse[0].tradeId, is(trade1.deal));
        assertThat("Assert tradeDate", formatTimeToUtc(mappedResponse[0].tradeDate), is(formatTimeToUtc(trade1.time)));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(trade1.account));
        assertThat("Assert action", mappedResponse[0].action, is(trade1.action));
        assertThat("Assert entry", mappedResponse[0].entry, is(trade1.entry));
        assertThat("Assert symbol", mappedResponse[0].symbol, is(trade1.symbol));
        assertThat("Assert profitUSD", mappedResponse[0].profitUsd, is(trade1.profit));
        assertThat("Assert profit", mappedResponse[0].profit, is(trade1.profit));
        assertThat("Assert comment", mappedResponse[0].comment, is(trade1.comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades by empty params")
    @AllureId("428")
    public void getTradesEmptyParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("action", "");
        queryParams.put("entry", "");
        queryParams.put("orderBy", "tradeDate");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades only by clientId(200)")
    @AllureId("210")
    public void getTradesClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades by clientId and limit")
    @AllureId("367")
    public void getTradesLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("orderBy", "tradeDate");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "1");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(1));
        assertThat("Assert tradeId", mappedResponse[0].tradeId, is(trade2.deal));
        assertThat("Assert tradeDate", mappedResponse[0].tradeDate, is(formatTimeToUtc(trade2.time)));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(trade2.account));
        assertThat("Assert action", mappedResponse[0].action, is(trade2.action));
        assertThat("Assert entry", mappedResponse[0].entry, is(trade2.entry));
        assertThat("Assert symbol", mappedResponse[0].symbol, is(trade2.symbol));
        assertThat("Assert profitUSD", mappedResponse[0].profitUsd, is(trade2.profit));
        assertThat("Assert profit", mappedResponse[0].profit, is(trade2.profit));
        assertThat("Assert comment", mappedResponse[0].comment, is(trade2.comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades order by create time default order")
    @AllureId("368")
    public void getTradesDefaultSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("orderBy", "tradeDate");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert tradeId", mappedResponse[0].tradeId, is(trade1.deal));
        assertThat("Assert tradeDate", mappedResponse[0].tradeDate, is(formatTimeToUtcWithMs(trade1.time)));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(trade1.account));
        assertThat("Assert action", mappedResponse[0].action, is(trade1.action));
        assertThat("Assert entry", mappedResponse[0].entry, is(trade1.entry));
        assertThat("Assert symbol", mappedResponse[0].symbol, is(trade1.symbol));
        assertThat("Assert profitUSD", mappedResponse[0].profitUsd, is(trade1.profit));
        assertThat("Assert profit", mappedResponse[0].profit, is(trade1.profit));
        assertThat("Assert comment", mappedResponse[0].comment, is(trade1.comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades order by actualAmountUSD")
    @AllureId("369")
    public void getTradesOrderByAmountUsdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("orderBy", "profitUSD");
        queryParams.put("sortOrder", "desc");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert profitUSD", mappedResponse[0].profitUsd, is(trade2.profit));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades no params")
    @AllureId("370")
    public void getTradesNoParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        Response response = getTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades no tradingAccount")
    @AllureId("371")
    public void getTradesNoTradingAccountTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("dateFrom", trade1.time.replace(" ", "T"));
        queryParams.put("dateTo", trade2.time.replace(" ", "T"));
        queryParams.put("action", trade1.serverId);
        queryParams.put("entry", trade1.serverId);
        queryParams.put("orderBy", "tradeDate");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades no serverId")
    @AllureId("372")
    public void getTradesNoServerIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("dateFrom", trade1.time.replace(" ", "T"));
        queryParams.put("dateTo", trade2.time.replace(" ", "T"));
        queryParams.put("action", trade1.serverId);
        queryParams.put("entry", trade1.serverId);
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades incorrect dateFrom")
    @AllureId("373")
    public void getTradesIncorrectDateFromTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("dateFrom", "test");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/trades"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades incorrect dateTo")
    @AllureId("374")
    public void getTradesIncorrectDateToTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("dateTo", "test");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/trades"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades incorrect orderBy")
    @AllureId("375")
    public void getTradesIncorrectOrderByTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("orderBy", "test");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid &quot;orderBy&quot; property format. The property may include only: tradeDate, symbol, profit, profitUSD"));
        assertThat("Assert status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades incorrect sortOrder")
    @AllureId("376")
    public void getTradesIncorrectSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("sortOrder", "test");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
        assertThat("Assert status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades incorrect limit")
    @AllureId("377")
    public void getTradesIncorrectLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.account);
        queryParams.put("serverId", trade1.serverId);
        queryParams.put("limit", "test");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/trades"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }
}
