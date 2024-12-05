package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getTradesGroupBy.GetTradesGroupByResponse;
import businessObjects.api.clickhouseApiService.getTradesGroupBy.GetTradesGroupByResponseError;
import businessObjects.db.clickhouse.mtMt5DealsTable.Mt5DealsObject;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getTradesGroupBy.GetTradesGroupByRequest.getTradesGroupBy;
import static businessObjects.db.clickhouse.mtMt5DealsTable.Mt5DealsFactory.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_TRADES_GROUP_BY_SYMBOL)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetTradesGroupByTests extends TestBaseApi {

    private static final Integer ACCOUNT_ID = getRandomIntPositive();
    private static final Integer SERVER_ID = 188;
    private static final Mt5DealsObject trade1 = generateTradeForGroupBy1(ACCOUNT_ID, SERVER_ID);
    private static final Mt5DealsObject trade2 = generateTradeForGroupBy2(ACCOUNT_ID, SERVER_ID);
    private static final Mt5DealsObject trade3 = generateTradeForGroupBy3(ACCOUNT_ID, SERVER_ID);
    private static final Mt5DealsObject trade4 = generateTradeForGroupBy4(ACCOUNT_ID, SERVER_ID);

    @BeforeAll
    public static void setupTradesGroupBy() throws ReflectiveOperationException, SQLException {
        insertObjectToDb(MT5_DEALS_TABLE_NAME, trade1);
        insertObjectToDb(MT5_DEALS_TABLE_NAME, trade2);
        insertObjectToDb(MT5_DEALS_TABLE_NAME, trade3);
        insertObjectToDb(MT5_DEALS_TABLE_NAME, trade4);
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with mandatory params (200)")
    @AllureId("213")
    public void getTradesGroupByWithMandatoryParamsTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit);
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with action (200)")
    @AllureId("485")
    public void getTradesGroupByWithActionTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("action", trade1.action);
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit, trade1.profit);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit, trade3.profit);
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with entry (200)")
    @AllureId("486")
    public void getTradesGroupByWithEntryTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("entry", trade2.entry);
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade2.symbol, trade2.profit, trade2.profit);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade4.symbol, trade4.profit, trade4.profit);
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with dateFrom (200)")
    @AllureId("487")
    public void getTradesGroupByWithDateFromTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("dateFrom", trade2.time.replace(" ", "T"));
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade2.symbol, trade2.profit, trade2.profit);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade4.symbol, trade4.profit, trade4.profit);
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with dateTo (200)")
    @AllureId("488")
    public void getTradesGroupByWithDateToTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("dateTo", trade1.time.replace(" ", "T"));
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit, trade1.profit);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit, trade3.profit);
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy symbol asc (200)")
    @AllureId("489")
    public void getTradesGroupByOrderBySymbolAscTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("orderBy", "symbol");
        queryParams.put("sortOrder", "asc");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit);
        assertThat("Assert response body", mappedResponse, containsInRelativeOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy profit desc (200)")
    @AllureId("490")
    public void getTradesGroupByOrderByProfitDescTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("orderBy", "profit");
        queryParams.put("sortOrder", "desc");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit);
        assertThat("Assert response body", mappedResponse, containsInRelativeOrder(responseGroup2, responseGroup1));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy symbol default (200)")
    @AllureId("491")
    public void getTradesGroupByOrderBySymbolDefaultTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("orderBy", "symbol");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit);
        assertThat("Assert response body", mappedResponse, containsInRelativeOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy profit default (200)")
    @AllureId("492")
    public void getTradesGroupByOrderByProfitDefaultTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("orderBy", "profit");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit);
        assertThat("Assert response body", mappedResponse, containsInRelativeOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with limit (200)")
    @AllureId("493")
    public void getTradesGroupByWithLimitTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("limit", 1);
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(1));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(trade1.symbol, trade1.profit + trade2.profit, trade1.profit + trade2.profit);
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(trade3.symbol, trade3.profit + trade4.profit, trade3.profit + trade4.profit);
        assertThat("Assert response body", mappedResponse, anyOf(hasItem(responseGroup1), hasItem(responseGroup2)));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request without tradingAccount (400)")
    @AllureId("494")
    public void getTradesGroupByNoTradingAccountTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", SERVER_ID);
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo("400"));
        assertThat("Assert error", mappedResponse.error, equalTo("Required request parameter 'tradingAccount' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request without serverId (400)")
    @AllureId("495")
    public void getTradesGroupByNoServerIdTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo("400"));
        assertThat("Assert error", mappedResponse.error, equalTo("Required request parameter 'serverId' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request tradingAccount not int (400)")
    @AllureId("496")
    public void getTradesGroupByTradingAccountNotIntTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "test");
        queryParams.put("serverId", SERVER_ID);
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo("400"));
        assertThat("Assert error", mappedResponse.error, equalTo("Invalid tradingAccount format: tradingAccount must be a string that can be parsed into a long"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request serverId not int (400)")
    @AllureId("497")
    public void getTradesGroupByServerIdNotIntTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo("400"));
        assertThat("Assert error", mappedResponse.error, equalTo("Invalid serverId format: serverId must be a string that can be parsed into an integer"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request action not int (400)")
    @AllureId("498")
    public void getTradesGroupByActionNotIntTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("action", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo("400"));
        assertThat("Assert title", mappedResponse.title, equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, equalTo("Failed to convert 'action' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request entry not int (400)")
    @AllureId("499")
    public void getTradesGroupByEntryNotIntTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("entry", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo("400"));
        assertThat("Assert title", mappedResponse.title, equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, equalTo("Failed to convert 'entry' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect dateFrom (400)")
    @AllureId("500")
    public void getTradesGroupByIncorrectDateFromTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("dateFrom", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo("400"));
        assertThat("Assert title", mappedResponse.title, equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, equalTo("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect dateTo (400)")
    @AllureId("501")
    public void getTradesGroupByIncorrectDateToTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("dateTo", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo("400"));
        assertThat("Assert title", mappedResponse.title, equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, equalTo("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect orderBy (400)")
    @AllureId("502")
    public void getTradesGroupByIncorrectOrderByTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("orderBy", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo("400"));
        assertThat("Assert error", mappedResponse.error, equalTo("Invalid &quot;orderBy&quot; property format. The property may include only: symbol, profit, profitUSD"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect sortOrder (400)")
    @AllureId("503")
    public void getTradesGroupByIncorrectSortOrderTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("sortOrder", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo("400"));
        assertThat("Assert error", mappedResponse.error, equalTo("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request limit not int (400)")
    @AllureId("504")
    public void getTradesGroupByLimitNotIntTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", ACCOUNT_ID);
        queryParams.put("serverId", SERVER_ID);
        queryParams.put("limit", "test");
        Response response = getTradesGroupBy(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, equalTo("400"));
        assertThat("Assert title", mappedResponse.title, equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, equalTo("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, equalTo("/v1/tradesGroupBy"));
    }

    @AfterAll
    public static void teardownTradesGroupBy() throws SQLException {
        deleteEntryFromDb(MT5_DEALS_TABLE_NAME, String.format("login = %s", ACCOUNT_ID));
    }
}
