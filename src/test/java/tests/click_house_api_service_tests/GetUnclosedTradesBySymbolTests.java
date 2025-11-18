package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.get_unclosed_trades_by_symbol.GetUnclosedTradesBySymbolResponse;
import business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedObject;
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

import static business_objects.api.clickhouse_api_service.get_unclosed_trades_by_symbol.GetUnclosedTradesBySymbolRequest.getUnclosedTradesBySymbol;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanMt5CoercedTableByAccount;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_CLICKHOUSE_API_SERVICE;
import static utils.Utils.getRandomLongPositive;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_UNCLOSED_TRADES_BY_SYMBOL)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetUnclosedTradesBySymbolTests extends TestBaseApi {

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

    @BeforeAll
    static void setup() {
        // client1: 1 unclosed trade (EURUSD)
        trade1 = generateTradeByClient(client1, 0, 0, 0, getRandomLongPositive());
        trade1.setPositionId(getRandomLongPositive());
        trade1.setEntry(0);
        trade1.setSymbol("EURUSD");

        // client3: one closed position (should be excluded) and 4 unclosed trades grouped by symbols
        // closed pair (position 5000): entry=0 and entry=1
        trade2 = generateTradeByClient(client3, 0, 0, 0, getRandomLongPositive());
        trade2.setPositionId(getRandomLongPositive());
        trade2.setDeal(getRandomLongPositive());
        trade2.setSymbol("EURUSD");

        trade3 = generateTradeByClient(client3, 0, 1, 0, getRandomLongPositive());
        trade3.setPositionId(trade2.getPositionId());
        trade3.setDeal(getRandomLongPositive());
        trade3.setSymbol("EURUSD");

        // unclosed trades for grouping
        trade4 = generateTradeByClient(client3, 0, 0, 0, getRandomLongPositive());
        trade4.setPositionId(getRandomLongPositive());
        trade4.setSymbol("EURUSD");
        trade4.setTime("2025-01-01 00:00:00");

        trade5 = generateTradeByClient(client3, 0, 0, 0, getRandomLongPositive());
        trade5.setPositionId(getRandomLongPositive());
        trade5.setSymbol("EURUSD");
        trade5.setTime("2025-01-02 00:00:00");

        trade6 = generateTradeByClient(client3, 0, 0, 0, getRandomLongPositive());
        trade6.setPositionId(getRandomLongPositive());
        trade6.setSymbol("EURUSD");
        trade6.setTime("2025-01-03 00:00:00");

        trade7 = generateTradeByClient(client3, 0, 0, 0, getRandomLongPositive());
        trade7.setPositionId(getRandomLongPositive());
        trade7.setSymbol("GBPUSD");
        trade7.setTime("2025-01-03 00:00:00");

        // unrelated client2 trade (to test empty response for that client)
        trade8 = generateTradeByClient(client2, 0, 1, 0, getRandomLongPositive());
        trade8.setPositionId(getRandomLongPositive());
        trade8.setSymbol("USDJPY");

        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7, trade8));
    }

    @AfterAll
    static void delete() throws Exception {
        cleanMt5CoercedTableByAccount(client1.getTradingAccount(), client2.getTradingAccount(), client3.getTradingAccount());
    }

    @Test
    @AllureId("1816")
    @DisplayName("Clickhouse Api. Get unclosed trades by symbol - tradingAccount+serverId")
    void getUnclosedTradesBySymbolTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount());
        queryParams.put("serverId", client1.getServerId());
        Response response = getUnclosedTradesBySymbol(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesBySymbolResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesBySymbolResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(1));
        assertThat("Assert symbol", mappedResponse.getFirst().symbol, is("EURUSD"));
        assertThat("Assert tradeCount", mappedResponse.getFirst().tradeCount, is(1));
        assertThat("Assert percentage", mappedResponse.getFirst().percentage, is(100.0));
    }

    @Test
    @AllureId("1817")
    @DisplayName("Clickhouse Api. Get unclosed trades by symbol - ucid only (no CRM link => empty)")
    void getUnclosedTradesBySymbolTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ucid", client1.getUcid());
        Response response = getUnclosedTradesBySymbol(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesBySymbolResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesBySymbolResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(0));
    }

    @Test
    @AllureId("1818")
    @DisplayName("Clickhouse Api. Get unclosed trades by symbol - grouping and percentages")
    void getUnclosedTradesBySymbolTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client3.getTradingAccount());
        queryParams.put("serverId", client3.getServerId());
        Response response = getUnclosedTradesBySymbol(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesBySymbolResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesBySymbolResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        // Expect EURUSD first with 3 trades (75%) and GBPUSD second with 1 trade (25%)
        assertThat("Assert first symbol", mappedResponse.get(0).symbol, is("EURUSD"));
        assertThat("Assert first tradeCount", mappedResponse.get(0).tradeCount, is(3));
        assertThat("Assert first percentage", mappedResponse.get(0).percentage, is(75.0));
        assertThat("Assert second symbol", mappedResponse.get(1).symbol, is("GBPUSD"));
        assertThat("Assert second tradeCount", mappedResponse.get(1).tradeCount, is(1));
        assertThat("Assert second percentage", mappedResponse.get(1).percentage, is(25.0));
    }

    @Test
    @AllureId("1819")
    @DisplayName("Clickhouse Api. Get unclosed trades by symbol - date range filters")
    void getUnclosedTradesBySymbolTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client3.getTradingAccount());
        queryParams.put("serverId", client3.getServerId());
        queryParams.put("dateFrom", "2025-01-02T00:00:00");
        queryParams.put("dateTo", "2025-01-03T23:59:59");
        Response response = getUnclosedTradesBySymbol(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesBySymbolResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesBySymbolResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));
        assertThat("Assert EURUSD count in range", mappedResponse.get(0).tradeCount + mappedResponse.get(1).tradeCount, is(3));
    }

    @Test
    @AllureId("1820")
    @DisplayName("Clickhouse Api. Get unclosed trades by symbol - empty when client has no unclosed")
    void getUnclosedTradesBySymbolTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client4.getTradingAccount());
        queryParams.put("serverId", client4.getServerId());
        Response response = getUnclosedTradesBySymbol(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetUnclosedTradesBySymbolResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetUnclosedTradesBySymbolResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(0));
    }
}
