package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_trades.GetTradesResponse;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
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
import java.util.stream.Collectors;

import static business_objects.api.clickhouse_api_service.get_trades.GetTradesRequest.getTrades;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.CleanTableHelper.cleanMt5CoercedTableByUcid;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_TRADES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetTradesTests extends TestBaseApi {

    private static ClientHelper clientHelper = getRandomVantageClient();
    private static CrmTbUserObject client = generateUserByClient(clientHelper);
    private static CrmTbAccountObject client1Account = generateAccountByClient(clientHelper, false);
    private static ClientHelper clientHelper2 = getRandomVantageClient();
    private static CrmTbUserObject client2 = generateUserByClient(clientHelper2);
    private static CrmTbAccountObject client2Account = generateAccountByClient(clientHelper2, false);
    private static ClientHelper clientHelper3 = getRandomVantageClient();
    private static CrmTbUserObject client3 = generateUserByClient(clientHelper3);
    private static CrmTbAccountObject client3Account = generateAccountByClient(clientHelper3, false);
    private static Mt5DealsCoercedObject trade1;
    private static Mt5DealsCoercedObject trade2;
    private static Mt5DealsCoercedObject trade3;
    private static Mt5DealsCoercedObject trade4;
    private static Mt5DealsCoercedObject trade5;

    @BeforeAll
    static void setup() {
        trade1 = generateTradeByClient(clientHelper);
        trade2 = generateTradeByClient(clientHelper);
        trade3 = generateTradeByClient(clientHelper);
        trade4 = generateTradeByClient(clientHelper2);
        trade5 = generateTradeByClient(clientHelper3);
        trade2.setTime(getTomorrowTimestampDbFormat());
        trade2.setProfit(2.0);
        trade2.setProfitUsd(3.0);
        trade2.setAction(2);
        trade2.setEntry(2);
        trade3.setServerId(1000);
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5));
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(client, client2, client3));
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(client1Account, client2Account, client3Account));
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanMt5CoercedTableByUcid(clientHelper.getUcid(), clientHelper2.getUcid(), clientHelper3.getUcid());
    }

    @Tag("CSV-1228")
    @Test
    @DisplayName("Clickhouse Api. Get Trades by all params")
    @AllureId("214")
    void getTradesTest1() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("dateFrom", trade1.getTime().replace(" ", "T"));
        queryParams.put("dateTo", trade2.getTime().replace(" ", "T"));
        queryParams.put("action", trade1.getAction());
        queryParams.put("entry", trade1.getEntry());
        queryParams.put("orderBy", "tradeDate");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(1));
        assertThat("Assert tradeId", mappedResponse[0].tradeId, is(trade1.getDeal()));
        assertThat("Assert tradeDate", mappedResponse[0].tradeDate, is(formatTimeToUtc(trade1.getTime())));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(trade1.getAccount()));
        assertThat("Assert action", mappedResponse[0].action, is(trade1.getAction()));
        assertThat("Assert entry", mappedResponse[0].entry, is(trade1.getEntry()));
        assertThat("Assert symbol", mappedResponse[0].symbol, is(trade1.getSymbol()));
        assertThat("Assert profitUSD", mappedResponse[0].profitUsd, is(trade1.getProfit()));
        assertThat("Assert profit", mappedResponse[0].profit, is(trade1.getProfit()));
        assertThat("Assert volumeInLots", mappedResponse[0].volumeInLots, is(trade1.getVolumeLots()));
        assertThat("Assert comment", mappedResponse[0].comment, is(trade1.getComment()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades by empty params")
    @AllureId("428")
    void getTradesTest2() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("action", "");
        queryParams.put("entry", "");
        queryParams.put("orderBy", "tradeDate");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades only by clientId(200)")
    @AllureId("210")
    void getTradesTest3() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades by clientId and limit")
    @AllureId("367")
    void getTradesTest4() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade2.getAccount());
        queryParams.put("serverId", trade2.getServerId());
        queryParams.put("orderBy", "tradeDate");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "1");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(1));
        assertThat("Assert tradeId", mappedResponse[0].tradeId, is(trade2.getDeal()));
        assertThat("Assert tradeDate", mappedResponse[0].tradeDate, is(formatTimeToUtc(trade2.getTime())));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(trade2.getAccount()));
        assertThat("Assert action", mappedResponse[0].action, is(trade2.getAction()));
        assertThat("Assert entry", mappedResponse[0].entry, is(trade2.getEntry()));
        assertThat("Assert symbol", mappedResponse[0].symbol, is(trade2.getSymbol()));
        assertThat("Assert profitUSD", mappedResponse[0].profitUsd, is(trade2.getProfitUsd()));
        assertThat("Assert profit", mappedResponse[0].profit, is(trade2.getProfit()));
        assertThat("Assert comment", mappedResponse[0].comment, is(trade2.getComment()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades order by create time default order")
    @AllureId("368")
    void getTradesTest5() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("orderBy", "tradeDate");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert tradeId", mappedResponse[0].tradeId, is(trade1.getDeal()));
        assertThat("Assert tradeDate", mappedResponse[0].tradeDate, is(formatTimeToUtc(trade1.getTime())));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(trade1.getAccount()));
        assertThat("Assert action", mappedResponse[0].action, is(trade1.getAction()));
        assertThat("Assert entry", mappedResponse[0].entry, is(trade1.getEntry()));
        assertThat("Assert symbol", mappedResponse[0].symbol, is(trade1.getSymbol()));
        assertThat("Assert profitUSD", mappedResponse[0].profitUsd, is(trade1.getProfit()));
        assertThat("Assert profit", mappedResponse[0].profit, is(trade1.getProfit()));
        assertThat("Assert comment", mappedResponse[0].comment, is(trade1.getComment()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades order by actualAmountUSD")
    @AllureId("369")
    void getTradesTest6() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade2.getAccount());
        queryParams.put("serverId", trade2.getServerId());
        queryParams.put("orderBy", "profitUSD");
        queryParams.put("sortOrder", "desc");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert profitUSD", mappedResponse[0].profitUsd, is(trade2.getProfitUsd()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades no params")
    @AllureId("370")
    void getTradesTest7() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Either clientId or tradingAccount and serverId must be provided."));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades no tradingAccount")
    @AllureId("371")
    void getTradesTest8() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("dateFrom", trade1.getTime().replace(" ", "T"));
        queryParams.put("dateTo", trade2.getTime().replace(" ", "T"));
        queryParams.put("action", trade1.getServerId());
        queryParams.put("entry", trade1.getServerId());
        queryParams.put("orderBy", "tradeDate");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Either clientId or tradingAccount and serverId must be provided."));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades no serverId")
    @AllureId("372")
    void getTradesTest9() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("dateFrom", trade1.getTime().replace(" ", "T"));
        queryParams.put("dateTo", trade2.getTime().replace(" ", "T"));
        queryParams.put("action", trade1.getServerId());
        queryParams.put("entry", trade1.getServerId());
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Invalid &quot;orderBy&quot; property format. The property may include only: tradeDate, symbol, profit, profitUSD"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades incorrect dateFrom")
    @AllureId("373")
    void getTradesTest10() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("dateFrom", "test");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/trades"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades incorrect dateTo")
    @AllureId("374")
    void getTradesTest11() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("dateTo", "test");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/trades"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades incorrect orderBy")
    @AllureId("375")
    void getTradesTest12() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("orderBy", "test");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.getError(), is("Invalid &quot;orderBy&quot; property format. The property may include only: tradeDate, symbol, profit, profitUSD"));
        assertThat("Assert status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades incorrect sortOrder")
    @AllureId("376")
    void getTradesTest13() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("sortOrder", "test");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.getError(), is("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
        assertThat("Assert status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Trades incorrect limit")
    @AllureId("377")
    void getTradesTest14() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade1.getAccount());
        queryParams.put("serverId", trade1.getServerId());
        queryParams.put("limit", "test");
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/trades"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @AllureId("1206")
    @Tag("CSV-1286")
    @DisplayName("Clickhouse Api. Get Trades by multiple accounts and server ids as string")
    void getTradesTest15() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", trade4.getAccount() + "," + trade5.getAccount());
        queryParams.put("serverId", trade4.getServerId() + "," + trade5.getServerId());
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);

        // Convert to a list of trade IDs
        List<Long> actualTradeIds = Arrays.stream(mappedResponse).map(r -> r.tradeId).collect(Collectors.toList()).reversed();

        // Expected trade IDs
        List<Long> expectedTradeIds = Arrays.asList(trade4.getDeal(), trade5.getDeal());

        // Assertions
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert all expected tradeIds are present", actualTradeIds, containsInAnyOrder(expectedTradeIds.toArray()));

    }

    @Test
    @AllureId("1207")
    @Tag("CSV-1286")
    @DisplayName("Clickhouse Api. Get Trades by multiple accounts and server ids as list")
    void getTradesTest16() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", List.of(trade4.getAccount(), trade5.getAccount()));
        queryParams.put("serverId", List.of(trade4.getServerId(), trade5.getServerId()));
        Response response = getTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse[].class);

        // Convert to a list of trade IDs
        List<Long> actualTradeIds = Arrays.stream(mappedResponse).map(r -> r.tradeId).collect(Collectors.toList()).reversed();

        // Expected trade IDs
        List<Long> expectedTradeIds = Arrays.asList(trade4.getDeal(), trade5.getDeal());

        // Assertions
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert all expected tradeIds are present", actualTradeIds, containsInAnyOrder(expectedTradeIds.toArray()));
    }
}
