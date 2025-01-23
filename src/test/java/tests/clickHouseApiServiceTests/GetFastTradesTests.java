package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getFastTrades.GetFastTradesResponse;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
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
import java.util.List;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getFastTrades.GetFastTradesRequest.getFastTrades;
import static businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.enums.DateTimeFormat.DATE_AND_TIME;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_FAST_TRADES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetFastTradesTests extends TestBaseApi {

    private static final ClientHelper client = getRandomVantageClient();
    private static Mt5DealsCoercedObject trade1Open;
    private static Mt5DealsCoercedObject trade1Close;
    private static Mt5DealsCoercedObject trade2Open;
    private static Mt5DealsCoercedObject trade2Close;
    private static GetFastTradesResponse responseTrade1;
    private static GetFastTradesResponse responseTrade2;

    @BeforeAll
    public static void setupTrades() throws ReflectiveOperationException, SQLException {
        trade1Open = generateTradeByClient(client);
        trade1Close = generateTradeByClient(client);
        trade1Open.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 2);
        trade1Open.timeUtc = trade1Open.time;
        trade1Open.entry = 0;
        trade1Close.positionId = trade1Open.positionId;
        trade1Close.comment = "trade 1 close";
        trade1Close.time = getCurrentTimestampDbFormat();
        trade1Close.timeUtc = trade1Close.time;
        trade1Close.entry = 1;
        trade1Close.symbol = "USDEUR";
        trade1Close.profit = 111.11;
        trade1Close.profitUsd = 123.12;
        responseTrade1 = new GetFastTradesResponse(timestampFromDbToIso(trade1Open.timeUtc), timestampFromDbToIso(trade1Close.timeUtc), trade1Close.deal, trade1Close.account, trade1Close.serverId, trade1Close.symbol, trade1Close.profit, trade1Close.profitUsd, trade1Close.comment);

        trade2Open = generateTradeByClient(client);
        trade2Close = generateTradeByClient(client);
        trade2Open.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 1);
        trade2Open.timeUtc = trade2Open.time;
        trade2Open.entry = 0;
        trade2Close.positionId = trade2Open.positionId;
        trade2Close.comment = "trade 2 close";
        trade2Close.time = getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0);
        trade2Close.timeUtc = trade2Close.time;
        trade2Close.entry = 1;
        trade2Close.symbol = "GBPJPY";
        trade2Close.profit = 222.22;
        trade2Close.profitUsd = 234.15;
        responseTrade2 = new GetFastTradesResponse(timestampFromDbToIso(trade2Open.timeUtc), timestampFromDbToIso(trade2Close.timeUtc), trade2Close.deal, trade2Close.account, trade2Close.serverId, trade2Close.symbol, trade2Close.profit, trade2Close.profitUsd, trade2Close.comment);

        Mt5DealsCoercedObject trade3 = generateTradeByClient(client);
        trade3.entry = 0;

        Mt5DealsCoercedObject trade4 = generateTradeByClient(client);
        trade4.entry = 1;

        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(trade1Open, trade1Close, trade2Open, trade2Close, trade3, trade4));
    }

    @AfterAll
    public static void teardownTrades() throws SQLException {
        deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("account = %s", client.getTradingAccount()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades by all params")
    @AllureId("844")
    public void getFastTradesAllParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateFrom", trade2Open.time.replace(" ", "T"));
        queryParams.put("dateTo", trade1Open.time.replace(" ", "T"));
        queryParams.put("tradeDurationSeconds", 130);
        queryParams.put("orderBy", "symbol");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        GetFastTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetFastTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response contains expected trades in order", mappedResponse, arrayContaining(responseTrade1, responseTrade2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades by empty optional params")
    @AllureId("845")
    public void getFastTradesEmptyOptionalParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("tradeDurationSeconds", 130);
        queryParams.put("orderBy", "");
        queryParams.put("sortOrder", "");
        queryParams.put("limit", "");
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        GetFastTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetFastTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response contains expected trades in order", mappedResponse, arrayContainingInAnyOrder(responseTrade1, responseTrade2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades only by mandatory params")
    @AllureId("846")
    public void getFastTradesMandatoryParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("tradeDurationSeconds", 130);
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        GetFastTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetFastTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response contains expected trades in order", mappedResponse, arrayContainingInAnyOrder(responseTrade1, responseTrade2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades by dateFrom")
    @AllureId("847")
    public void getFastTradesDateFromTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateFrom", trade1Open.time.replace(" ", "T"));
        queryParams.put("tradeDurationSeconds", 130);
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        GetFastTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetFastTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response contains expected trades in order", mappedResponse, arrayContaining(responseTrade1));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades by dateTo")
    @AllureId("848")
    public void getFastTradesDateToTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateTo", trade2Open.time.replace(" ", "T"));
        queryParams.put("tradeDurationSeconds", 130);
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        GetFastTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetFastTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response contains expected trades in order", mappedResponse, arrayContainingInAnyOrder(responseTrade2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades by duration")
    @AllureId("849")
    public void getFastTradesDurationTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("tradeDurationSeconds", 70);
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        GetFastTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetFastTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response contains expected trades in order", mappedResponse, arrayContaining(responseTrade2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades orderBy profit")
    @AllureId("850")
    public void getFastTradesOrderByTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("tradeDurationSeconds", 130);
        queryParams.put("orderBy", "profit");
        queryParams.put("sortOrder", "desc");
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        GetFastTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetFastTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response contains expected trades in order", mappedResponse, arrayContaining(responseTrade2, responseTrade1));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades limit")
    @AllureId("851")
    public void getFastTradesLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("tradeDurationSeconds", 130);
        queryParams.put("orderBy", "symbol");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "1");
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        GetFastTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetFastTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response contains expected trades in order", mappedResponse, arrayContaining(responseTrade1));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades no params")
    @AllureId("852")
    public void getFastTradesNoParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades no tradingAccount")
    @AllureId("853")
    public void getFastTradesNoTradingAccountTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", client.getServerId());
        queryParams.put("tradeDurationSeconds", 130);
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades no serverId")
    @AllureId("854")
    public void getFastTradesNoServerIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("tradeDurationSeconds", 130);
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades no tradeDurationSeconds")
    @AllureId("855")
    public void getFastTradesNoDurationTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'tradeDurationSeconds' for method parameter type Integer is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades incorrect tradingAccount")
    @AllureId("856")
    public void getFastTradesIncorrectAccountTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "test");
        queryParams.put("serverId", client.getServerId());
        queryParams.put("tradeDurationSeconds", 130);
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.error, is("Invalid tradingAccount format: tradingAccount must be a string that can be parsed into a long"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades incorrect serverId")
    @AllureId("857")
    public void getFastTradesIncorrectServerIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "test");
        queryParams.put("tradeDurationSeconds", 130);
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.error, is("Invalid serverId format: serverId must be a string that can be parsed into an integer"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades incorrect dateFrom")
    @AllureId("858")
    public void getFastTradesIncorrectDateFromTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateFrom", "test");
        queryParams.put("tradeDurationSeconds", 130);
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/fastTrades"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades incorrect dateTo")
    @AllureId("859")
    public void getFastTradesIncorrectDateToTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateTo", "test");
        queryParams.put("tradeDurationSeconds", 130);
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/fastTrades"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades incorrect orderBy")
    @AllureId("860")
    public void getFastTradesIncorrectOrderByTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("tradeDurationSeconds", 130);
        queryParams.put("orderBy", "test");
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid &quot;orderBy&quot; property format. The property may include only: tradeDateOpen, tradeDateClose, symbol, profit, profitUSD"));
        assertThat("Assert status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades incorrect sortOrder")
    @AllureId("861")
    public void getFastTradesIncorrectSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("tradeDurationSeconds", 130);
        queryParams.put("sortOrder", "test");
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
        assertThat("Assert status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades incorrect limit")
    @AllureId("862")
    public void getFastTradesIncorrectLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("tradeDurationSeconds", 130);
        queryParams.put("limit", "test");
        Response response = getFastTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/fastTrades"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }
}
