package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_fast_trades.GetFastTradesResponse;
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

import static business_objects.api.clickhouse_api_service.get_fast_trades.GetFastTradesRequest.getFastTrades;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
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
class GetFastTradesTests extends TestBaseApi {

    private static final ClientHelper client = getRandomVantageClient();
    private static Mt5DealsCoercedObject tradeOpen1;
    private static Mt5DealsCoercedObject tradeClose1;
    private static Mt5DealsCoercedObject tradeOpen2;
    private static Mt5DealsCoercedObject tradeClose2;
    private static GetFastTradesResponse responseTrade1;
    private static GetFastTradesResponse responseTrade2;

    @BeforeAll
    static void setupTrades() {
        tradeOpen1 = generateTradeByClient(client);
        tradeOpen1.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 0, 0, 2));
        tradeOpen1.setTimeUtc(tradeOpen1.getTime());
        tradeOpen1.setEntry(0);
        tradeClose1 = generateTradeByClient(client);
        tradeClose1.setPositionId(tradeOpen1.getPositionId());
        tradeClose1.setComment("trade 1 close");
        tradeClose1.setTime(getCurrentTimestampDbFormat());
        tradeClose1.setTimeUtc(tradeClose1.getTime());
        tradeClose1.setEntry(1);
        tradeClose1.setSymbol("USDEUR");
        tradeClose1.setProfit(111.11);
        tradeClose1.setProfitUsd(123.12);

        responseTrade1 = new GetFastTradesResponse(timestampFromDbToIso(tradeOpen1.getTimeUtc()), timestampFromDbToIso(
                tradeClose1.getTimeUtc()), tradeClose1.getDeal(), tradeClose1.getAccount(), tradeClose1.getServerId(), tradeClose1.getSymbol(), tradeClose1.getProfit(), tradeClose1.getProfitUsd(), tradeClose1.getComment());

        tradeOpen2 = generateTradeByClient(client);
        tradeOpen2.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 1));
        tradeOpen2.setTimeUtc(tradeOpen2.getTime());
        tradeOpen2.setEntry(0);

        tradeClose2 = generateTradeByClient(client);
        tradeClose2.setPositionId(tradeOpen2.getPositionId());
        tradeClose2.setComment("trade 2 close");
        tradeClose2.setTime(getCurrentTimestampMinusOffsetFormatted(DATE_AND_TIME, 0, 0, 1, 0, 0));
        tradeClose2.setTimeUtc(tradeClose2.getTime());
        tradeClose2.setEntry(1);
        tradeClose2.setSymbol("GBPJPY");
        tradeClose2.setProfit(222.22);
        tradeClose2.setProfitUsd(234.15);
        responseTrade2 = new GetFastTradesResponse(timestampFromDbToIso(tradeOpen2.getTimeUtc()), timestampFromDbToIso(
                tradeClose2.getTimeUtc()), tradeClose2.getDeal(), tradeClose2.getAccount(), tradeClose2.getServerId(), tradeClose2.getSymbol(), tradeClose2.getProfit(), tradeClose2.getProfitUsd(), tradeClose2.getComment());

        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(tradeOpen1, tradeClose1, tradeOpen2, tradeClose2));
    }

    @AfterAll
    static void teardownTrades() {
        deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("account = %s", client.getTradingAccount()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Fast Trades by all params")
    @AllureId("844")
    void getFastTradesAllParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateFrom", tradeOpen2.getTime().replace(" ", "T"));
        queryParams.put("dateTo", tradeOpen1.getTime().replace(" ", "T"));
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
    void getFastTradesEmptyOptionalParamsTest() throws IOException {

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
    void getFastTradesMandatoryParamsTest() throws IOException {

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
    void getFastTradesDateFromTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateFrom", tradeOpen1.getTime().replace(" ", "T"));
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
    void getFastTradesDateToTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateTo", tradeOpen2.getTime().replace(" ", "T"));
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
    void getFastTradesDurationTest() throws IOException {

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
    void getFastTradesOrderByTest() throws IOException {

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
    void getFastTradesLimitTest() throws IOException {

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
    void getFastTradesNoParamsTest() throws IOException {

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
    void getFastTradesNoTradingAccountTest() throws IOException {

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
    void getFastTradesNoServerIdTest() throws IOException {

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
    void getFastTradesNoDurationTest() throws IOException {

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
    void getFastTradesIncorrectAccountTest() throws IOException {

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
    void getFastTradesIncorrectServerIdTest() throws IOException {

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
    void getFastTradesIncorrectDateFromTest() throws IOException {

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
    void getFastTradesIncorrectDateToTest() throws IOException {

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
    void getFastTradesIncorrectOrderByTest() throws IOException {

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
    void getFastTradesIncorrectSortOrderTest() throws IOException {

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
    void getFastTradesIncorrectLimitTest() throws IOException {

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
