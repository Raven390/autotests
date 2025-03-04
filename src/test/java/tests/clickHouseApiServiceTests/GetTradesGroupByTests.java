package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getTradesGroupBy.GetTradesGroupByResponse;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getTradesGroupBy.GetTradesGroupByRequest.getTradesGroupBy;
import static businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedFactory.*;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_TRADES_GROUP_BY_SYMBOL)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetTradesGroupByTests extends TestBaseApi {

    private static ClientHelper client;

    private static Mt5DealsCoercedObject trade1;
    private static Mt5DealsCoercedObject trade2;
    private static Mt5DealsCoercedObject trade3;
    private static Mt5DealsCoercedObject trade4;
    private static Mt5DealsCoercedObject trade5;

    @BeforeAll
    public static void setupTradesGroupBy() {
        client = getRandomVantageClientAllFields();
        trade1 = generateTradeByClient(client);
        trade1.action = 1;
        trade1.entry = 1;
        trade1.time = "2024-01-01 00:00:00";
        trade1.timeUtc = "2024-01-01 00:00:00";
        trade2 = generateTradeByClient(client);
        trade2.action = 0;
        trade2.time = "2030-01-01 00:00:00";
        trade2.timeUtc = "2030-01-01 00:00:00";
        trade3 = generateTradeByClient(client);
        trade3.entry = 1;
        trade3.action = 1;
        trade3.symbol = "GBPJPY";
        trade3.time = "2024-01-01 00:00:00";
        trade3.timeUtc = "2024-01-01 00:00:00";
        trade4 = generateTradeByClient(client);
        trade4.time = "2030-01-01 00:00:00";
        trade4.timeUtc = "2030-01-01 00:00:00";
        trade4.symbol = "GBPJPY";
        trade5 = generateTradeByClient(client);
        trade4.symbol = "GBPJPY";
        trade5.serverId = trade5.serverId + 1;
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5));
    }

    @AfterAll
    public static void teardownTradesGroupBy() {
        //deleteEntryFromDb(MT5_DEALS_COERCED_TABLE_NAME, String.format("account = %s", client.getTradingAccount()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with mandatory params (200)")
    @AllureId("213")
    public void getTradesGroupByWithMandatoryParamsTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit, trade1.volumeLots + trade2.volumeLots);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit, trade3.volumeLots + trade4.volumeLots);
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with action (200)")
    @AllureId("485")
    public void getTradesGroupByWithActionTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("action", trade1.action);
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit, trade1.profit, trade1.volumeLots);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit, trade3.profit, trade3.volumeLots);
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with entry (200)")
    @AllureId("486")
    public void getTradesGroupByWithEntryTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("entry", trade2.entry);
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade2.symbol, trade2.profit, trade2.profit, trade2.volumeLots);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade4.symbol, trade4.profit, trade4.profit, trade4.volumeLots);
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with dateFrom (200)")
    @AllureId("487")
    public void getTradesGroupByWithDateFromTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateFrom", "2029-01-02T00:00:00");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade2.symbol, trade2.profit, trade2.profit, trade2.volumeLots);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade4.symbol, trade4.profit, trade4.profit, trade4.volumeLots);
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with dateTo (200)")
    @AllureId("488")
    public void getTradesGroupByWithDateToTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateTo", "2024-01-02T00:00:00");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit, trade1.profitUsd, trade1.volumeLots);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit, trade3.profitUsd, trade3.volumeLots);
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy symbol asc (200)")
    @AllureId("489")
    public void getTradesGroupByOrderBySymbolAscTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("orderBy", "symbol");
        queryParams.put("sortOrder", "asc");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit, trade1.volumeLots + trade2.volumeLots);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit, trade3.volumeLots + trade4.volumeLots);
        assertThat("Assert response body", mappedResponse, containsInRelativeOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy profit desc (200)")
    @AllureId("490")
    public void getTradesGroupByOrderByProfitDescTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("orderBy", "profit");
        queryParams.put("sortOrder", "desc");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit, trade1.volumeLots + trade2.volumeLots);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit, trade3.volumeLots + trade4.volumeLots);
        assertThat("Assert response body", mappedResponse, containsInRelativeOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy symbol default (200)")
    @AllureId("491")
    public void getTradesGroupByOrderBySymbolDefaultTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("orderBy", "symbol");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit, trade1.volumeLots + trade2.volumeLots);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit, trade3.volumeLots + trade4.volumeLots);
        assertThat("Assert response body", mappedResponse, containsInRelativeOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy profit default (200)")
    @AllureId("492")
    public void getTradesGroupByOrderByProfitDefaultTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("orderBy", "profit");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit, trade1.volumeLots + trade2.volumeLots);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit, trade3.volumeLots + trade4.volumeLots);
        assertThat("Assert response body", mappedResponse, containsInRelativeOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with limit (200)")
    @AllureId("493")
    public void getTradesGroupByWithLimitTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("limit", 1);
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(1));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit, trade1.volumeLots + trade2.volumeLots);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit, trade3.volumeLots + trade4.volumeLots);
        assertThat("Assert response body", mappedResponse, anyOf(hasItem(responseGroup1), hasItem(responseGroup2)));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request without tradingAccount (400)")
    @AllureId("494")
    public void getTradesGroupByNoTradingAccountTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", client.getServerId());
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo(400));
        assertThat("Assert error", mappedResponse.error, equalTo("Required request parameter 'tradingAccount' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request without serverId (400)")
    @AllureId("495")
    public void getTradesGroupByNoServerIdTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo(400));
        assertThat("Assert error", mappedResponse.error, equalTo("Required request parameter 'serverId' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request tradingAccount not int (400)")
    @AllureId("496")
    public void getTradesGroupByTradingAccountNotIntTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "test");
        queryParams.put("serverId", client.getServerId());
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo(400));
        assertThat("Assert error", mappedResponse.error, equalTo("Invalid tradingAccount format: tradingAccount must be a string that can be parsed into a long"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request serverId not int (400)")
    @AllureId("497")
    public void getTradesGroupByServerIdNotIntTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo(400));
        assertThat("Assert error", mappedResponse.error, equalTo("Invalid serverId format: serverId must be a string that can be parsed into an integer"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request action not int (400)")
    @AllureId("498")
    public void getTradesGroupByActionNotIntTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("action", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo(400));
        assertThat("Assert title", mappedResponse.title, equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, equalTo("Failed to convert 'action' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request entry not int (400)")
    @AllureId("499")
    public void getTradesGroupByEntryNotIntTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("entry", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo(400));
        assertThat("Assert title", mappedResponse.title, equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, equalTo("Failed to convert 'entry' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect dateFrom (400)")
    @AllureId("500")
    public void getTradesGroupByIncorrectDateFromTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateFrom", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo(400));
        assertThat("Assert title", mappedResponse.title, equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, equalTo("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect dateTo (400)")
    @AllureId("501")
    public void getTradesGroupByIncorrectDateToTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateTo", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo(400));
        assertThat("Assert title", mappedResponse.title, equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, equalTo("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect orderBy (400)")
    @AllureId("502")
    public void getTradesGroupByIncorrectOrderByTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("orderBy", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo(400));
        assertThat("Assert error", mappedResponse.error, equalTo("Invalid &quot;orderBy&quot; property format. The property may include only: symbol, profit, profitUSD"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect sortOrder (400)")
    @AllureId("503")
    public void getTradesGroupByIncorrectSortOrderTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("sortOrder", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo(400));
        assertThat("Assert error", mappedResponse.error, equalTo("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request limit not int (400)")
    @AllureId("504")
    public void getTradesGroupByLimitNotIntTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("limit", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo(400));
        assertThat("Assert title", mappedResponse.title, equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, equalTo("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, equalTo("/v1/tradesGroupBy"));
    }
}
