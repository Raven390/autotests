package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_trades_group_by.GetTradesGroupByResponse;
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

import static business_objects.api.clickhouse_api_service.get_trades_group_by.GetTradesGroupByRequest.getTradesGroupBy;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.mt_mt5_deals_coerced.Mt5DealsCoercedFactory.*;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.CleanTableHelper.cleanMt5CoercedTableByUcid;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Disabled("Covered because of missing table on env")
@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_TRADES_GROUP_BY_SYMBOL)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetTradesGroupByTests extends TestBaseApi {

    private static ClientHelper client;
    private static ClientHelper client2;
    private static ClientHelper client3;

    private static Mt5DealsCoercedObject trade1;
    private static Mt5DealsCoercedObject trade2;
    private static Mt5DealsCoercedObject trade3;
    private static Mt5DealsCoercedObject trade4;
    private static Mt5DealsCoercedObject trade5;
    private static Mt5DealsCoercedObject trade6;
    private static Mt5DealsCoercedObject trade7;

    @BeforeAll
    static void setup() {
        client = getRandomVantageClient();
        client2 = getRandomVantageClient();
        client3 = getRandomVantageClient();
        trade1 = generateTradeByClient(client);
        trade1.setAction(1);
        trade1.setEntry(1);
        trade1.setTime("2024-01-01 00:00:00");
        trade1.setTimeUtc("2024-01-01 00:00:00");
        trade1.setProfitUsd(2d);
        trade1.setProfit(2d);
        trade2 = generateTradeByClient(client);
        trade2.setAction(0);
        trade2.setTime("2030-01-01 00:00:00");
        trade2.setTimeUtc("2030-01-01 00:00:00");
        trade3 = generateTradeByClient(client);
        trade3.setEntry(1);
        trade3.setAction(1);
        trade3.setSymbol("GBPJPY");
        trade3.setTime("2024-01-01 00:00:00");
        trade3.setTimeUtc("2024-01-01 00:00:00");
        trade4 = generateTradeByClient(client);
        trade4.setTime("2030-01-01 00:00:00");
        trade4.setTimeUtc("2030-01-01 00:00:00");
        trade4.setSymbol("GBPJPY");
        trade5 = generateTradeByClient(client);
        trade5.setSymbol("GBPJPY");
        trade5.setProfitUsd(5d);
        trade5.setProfit(5d);
        trade5.setServerId(trade5.getServerId() + 1);
        trade6 = generateTradeByClient(client2);
        trade6.setSymbol("BTCUSD");
        trade7 = generateTradeByClient(client3);
        trade6.setSymbol("BTCEUR");

        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(generateCrmTbAccountData(client), generateCrmTbAccountData(client2), generateCrmTbAccountData(client3)));
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(trade1, trade2, trade3, trade4, trade5, trade6, trade7));
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanMt5CoercedTableByUcid(client.getUcid(), client2.getUcid(), client3.getUcid());
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with mandatory params (200)")
    @AllureId("213")
    void getTradesGroupByTest1() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade1.getSymbol(), trade1.getProfit() + trade2.getProfit(), trade1.getProfit() + trade2.getProfit(), trade1.getVolumeLots() + trade2.getVolumeLots(), trade1.getProfitUsd() + trade1.getCommissionUsd() + trade1.getStorageUsd() + trade2.getProfitUsd() + trade2.getCommissionUsd() + trade2.getStorageUsd(), trade1.getStorageUsd() + trade2.getStorageUsd(), trade1.getCommissionUsd() + trade2.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade3.getSymbol(), trade3.getProfit() + trade4.getProfit(), trade3.getProfit() + trade4.getProfit(), trade3.getVolumeLots() + trade4.getVolumeLots(), trade3.getProfitUsd() + trade3.getCommissionUsd() + trade3.getStorageUsd() + trade4.getProfitUsd() + trade4.getCommissionUsd() + trade4.getStorageUsd(), trade3.getStorageUsd() + trade4.getStorageUsd(), trade3.getCommissionUsd() + trade4.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with action (200)")
    @AllureId("485")
    void getTradesGroupByTest2() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("action", trade1.getAction());
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade1.getSymbol(), trade1.getProfit(), trade1.getProfit(), trade1.getVolumeLots(), trade1.getProfitUsd() + trade1.getCommissionUsd() + trade1.getStorageUsd(), trade1.getStorageUsd(), trade1.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade3.getSymbol(), trade3.getProfit(), trade3.getProfit(), trade3.getVolumeLots(), trade3.getProfitUsd() + trade3.getCommissionUsd() + trade3.getStorageUsd(), trade3.getStorageUsd(), trade3.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with entry (200)")
    @AllureId("486")
    void getTradesGroupByTest3() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("entry", trade2.getEntry());
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade2.getSymbol(), trade2.getProfit(), trade2.getProfit(), trade2.getVolumeLots(), trade2.getProfitUsd() + trade2.getCommissionUsd() + trade2.getStorageUsd(), trade2.getStorageUsd(), trade2.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade4.getSymbol(), trade4.getProfit(), trade4.getProfit(), trade4.getVolumeLots(), trade4.getProfitUsd() + trade4.getCommissionUsd() + trade4.getStorageUsd(), trade4.getStorageUsd(), trade4.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with dateFrom (200)")
    @AllureId("487")
    void getTradesGroupByTest4() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateFrom", "2029-01-02T00:00:00");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade2.getSymbol(), trade2.getProfit(), trade2.getProfit(), trade2.getVolumeLots(), trade2.getProfitUsd() + trade2.getCommissionUsd() + trade2.getStorageUsd(), trade2.getStorageUsd(), trade2.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade4.getSymbol(), trade4.getProfit(), trade4.getProfit(), trade4.getVolumeLots(), trade4.getProfitUsd() + trade4.getCommissionUsd() + trade4.getStorageUsd(), trade4.getStorageUsd(), trade4.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with dateTo (200)")
    @AllureId("488")
    void getTradesGroupByTest5() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateTo", "2024-01-02T00:00:00");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade1.getSymbol(), trade1.getProfit(), trade1.getProfit(), trade1.getVolumeLots(), trade1.getProfitUsd() + trade1.getCommissionUsd() + trade1.getStorageUsd(), trade1.getStorageUsd(), trade1.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade3.getSymbol(), trade3.getProfit(), trade3.getProfit(), trade3.getVolumeLots(), trade3.getProfitUsd() + trade3.getCommissionUsd() + trade3.getStorageUsd(), trade3.getStorageUsd(), trade3.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy symbol asc (200)")
    @AllureId("489")
    void getTradesGroupByTest6() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("orderBy", "symbol");
        queryParams.put("sortOrder", "asc");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade1.getSymbol(), trade1.getProfit() + trade2.getProfit(), trade1.getProfit() + trade2.getProfit(), trade1.getVolumeLots() + trade2.getVolumeLots(), trade1.getProfitUsd() + trade1.getCommissionUsd() + trade1.getStorageUsd() + trade2.getProfitUsd() + trade2.getCommissionUsd() + trade2.getStorageUsd(), trade1.getStorageUsd() + trade2.getStorageUsd(), trade1.getCommissionUsd() + trade2.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade3.getSymbol(), trade3.getProfit() + trade4.getProfit(), trade3.getProfit() + trade4.getProfit(), trade3.getVolumeLots() + trade4.getVolumeLots(), trade3.getProfitUsd() + trade3.getCommissionUsd() + trade3.getStorageUsd() + trade4.getProfitUsd() + trade4.getCommissionUsd() + trade4.getStorageUsd(), trade3.getStorageUsd() + trade4.getStorageUsd(), trade3.getCommissionUsd() + trade4.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInRelativeOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy profit desc (200)")
    @AllureId("490")
    void getTradesGroupByTest7() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("orderBy", "profit");
        queryParams.put("sortOrder", "desc");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade1.getSymbol(), trade1.getProfit() + trade2.getProfit(), trade1.getProfit() + trade2.getProfit(), trade1.getVolumeLots() + trade2.getVolumeLots(), trade1.getProfitUsd() + trade1.getCommissionUsd() + trade1.getStorageUsd() + trade2.getProfitUsd() + trade2.getCommissionUsd() + trade2.getStorageUsd(), trade1.getStorageUsd() + trade2.getStorageUsd(), trade1.getCommissionUsd() + trade2.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade3.getSymbol(), trade3.getProfit() + trade4.getProfit(), trade3.getProfit() + trade4.getProfit(), trade3.getVolumeLots() + trade4.getVolumeLots(), trade3.getProfitUsd() + trade3.getCommissionUsd() + trade3.getStorageUsd() + trade4.getProfitUsd() + trade4.getCommissionUsd() + trade4.getStorageUsd(), trade3.getStorageUsd() + trade4.getStorageUsd(), trade3.getCommissionUsd() + trade4.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInRelativeOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy symbol default (200)")
    @AllureId("491")
    void getTradesGroupByTest8() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("orderBy", "symbol");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade1.getSymbol(), trade1.getProfit() + trade2.getProfit(), trade1.getProfit() + trade2.getProfit(), trade1.getVolumeLots() + trade2.getVolumeLots(), trade1.getProfitUsd() + trade1.getCommissionUsd() + trade1.getStorageUsd() + trade2.getProfitUsd() + trade2.getCommissionUsd() + trade2.getStorageUsd(), trade1.getStorageUsd() + trade2.getStorageUsd(), trade1.getCommissionUsd() + trade2.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade3.getSymbol(), trade3.getProfit() + trade4.getProfit(), trade3.getProfit() + trade4.getProfit(), trade3.getVolumeLots() + trade4.getVolumeLots(), trade3.getProfitUsd() + trade3.getCommissionUsd() + trade3.getStorageUsd() + trade4.getProfitUsd() + trade4.getCommissionUsd() + trade4.getStorageUsd(), trade3.getStorageUsd() + trade4.getStorageUsd(), trade3.getCommissionUsd() + trade4.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInRelativeOrder(responseGroup1, responseGroup2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with orderBy profit default (200)")
    @AllureId("492")
    void getTradesGroupByTest9() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("orderBy", "profit");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade1.getSymbol(), trade1.getProfit() + trade2.getProfit(), trade1.getProfit() + trade2.getProfit(), trade1.getVolumeLots() + trade2.getVolumeLots(), trade1.getProfitUsd() + trade1.getCommissionUsd() + trade1.getStorageUsd() + trade2.getProfitUsd() + trade2.getCommissionUsd() + trade2.getStorageUsd(), trade1.getStorageUsd() + trade2.getStorageUsd(), trade1.getCommissionUsd() + trade2.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade3.getSymbol(), trade3.getProfit() + trade4.getProfit(), trade3.getProfit() + trade4.getProfit(), trade3.getVolumeLots() + trade4.getVolumeLots(), trade3.getProfitUsd() + trade3.getCommissionUsd() + trade3.getStorageUsd() + trade4.getProfitUsd() + trade4.getCommissionUsd() + trade4.getStorageUsd(), trade3.getStorageUsd() + trade4.getStorageUsd(), trade3.getCommissionUsd() + trade4.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup2, responseGroup1));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with limit (200)")
    @AllureId("493")
    void getTradesGroupByTest10() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("limit", 1);
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(1));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), null, trade1.getSymbol(), trade1.getProfit() + trade2.getProfit(), trade1.getProfit() + trade2.getProfit(), trade1.getVolumeLots() + trade2.getVolumeLots(), trade1.getProfitUsd() + trade1.getCommissionUsd() + trade1.getStorageUsd() + trade2.getProfitUsd() + trade2.getCommissionUsd() + trade2.getStorageUsd(), trade1.getStorageUsd() + trade2.getStorageUsd(), trade1.getCommissionUsd() + trade2.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, anyOf(hasItem(responseGroup1)));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request without tradingAccount (400)")
    @AllureId("494")
    void getTradesGroupByTest11() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", client.getServerId());
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), equalTo(400));
        assertThat("Assert error", mappedResponse.getError(), equalTo("Either clientId or tradingAccount and serverId must be provided."));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request without serverId (400)")
    @AllureId("495")
    void getTradesGroupByTest12() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), equalTo(400));
        assertThat("Assert error", mappedResponse.getError(), equalTo("Either clientId or tradingAccount and serverId must be provided."));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request tradingAccount not int (400)")
    @AllureId("496")
    void getTradesGroupByTest13() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "test");
        queryParams.put("serverId", client.getServerId());
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), equalTo(400));
        assertThat("Assert error", mappedResponse.getError(), equalTo("Invalid tradingAccount format: tradingAccount must be a string that can be parsed into a long"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request serverId not int (400)")
    @AllureId("497")
    void getTradesGroupByTest14() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "test");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), equalTo(400));
        assertThat("Assert error", mappedResponse.getError(), equalTo("Invalid serverId format: serverId must be a string that can be parsed into an integer"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request action not int (400)")
    @AllureId("498")
    void getTradesGroupByTest15() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("action", "test");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), equalTo(400));
        assertThat("Assert title", mappedResponse.getTitle(), equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), equalTo("Failed to convert 'action' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request entry not int (400)")
    @AllureId("499")
    void getTradesGroupByTest16() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("entry", "test");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), equalTo(400));
        assertThat("Assert title", mappedResponse.getTitle(), equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), equalTo("Failed to convert 'entry' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect dateFrom (400)")
    @AllureId("500")
    void getTradesGroupByTest17() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateFrom", "test");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), equalTo(400));
        assertThat("Assert title", mappedResponse.getTitle(), equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), equalTo("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect dateTo (400)")
    @AllureId("501")
    void getTradesGroupByTest18() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("dateTo", "test");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), equalTo(400));
        assertThat("Assert title", mappedResponse.getTitle(), equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), equalTo("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect orderBy (400)")
    @AllureId("502")
    void getTradesGroupByTest19() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("orderBy", "test");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), equalTo(400));
        assertThat("Assert error", mappedResponse.getError(), equalTo("Invalid &quot;orderBy&quot; property format. The property may include only: symbol, profit, profitUSD"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request incorrect sortOrder (400)")
    @AllureId("503")
    void getTradesGroupByTest20() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("sortOrder", "test");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), equalTo(400));
        assertThat("Assert error", mappedResponse.getError(), equalTo("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request limit not int (400)")
    @AllureId("504")
    void getTradesGroupByTest21() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        queryParams.put("limit", "test");
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), equalTo(400));
        assertThat("Assert title", mappedResponse.getTitle(), equalTo("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), equalTo("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), equalTo("/v1/tradesGroupBy"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades groupBy request with ucid (200)")
    @AllureId("1076")
    void getTradesGroupByTest22() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(client.getUcid()));
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), client.getUcid(), trade1.getSymbol(), trade1.getProfit() + trade2.getProfit(), trade1.getProfit() + trade2.getProfit(), trade1.getVolumeLots() + trade2.getVolumeLots(), trade1.getProfitUsd() + trade1.getCommissionUsd() + trade1.getStorageUsd() + trade2.getProfitUsd() + trade2.getCommissionUsd() + trade2.getStorageUsd(), trade1.getStorageUsd() + trade2.getStorageUsd(), trade1.getCommissionUsd() + trade2.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client.getTradingAccount().toString(), client.getServerId().toString(), client.getUcid(), trade3.getSymbol(), trade3.getProfit() + trade4.getProfit(), trade3.getProfit() + trade4.getProfit(), trade3.getVolumeLots() + trade4.getVolumeLots(), trade3.getProfitUsd() + trade3.getCommissionUsd() + trade3.getStorageUsd() + trade4.getProfitUsd() + trade4.getCommissionUsd() + trade4.getStorageUsd(), trade3.getStorageUsd() + trade4.getStorageUsd(), trade3.getCommissionUsd() + trade4.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }

    @Test
    @Tag("CSV-1287")
    @AllureId("1208")
    @DisplayName("Clickhouse Api. Get trades groupBy request by list of users (200)")
    void getTradesGroupByTest23() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", List.of(client2.getTradingAccount(), client3.getTradingAccount()));
        queryParams.put("serverId", List.of(client2.getServerId(), client3.getServerId()));
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client2.getTradingAccount().toString(), client2.getServerId().toString(), null, trade6.getSymbol(), trade6.getProfit(), trade6.getProfit(), trade6.getVolumeLots(), trade6.getProfitUsd() + trade6.getCommissionUsd() + trade6.getStorageUsd(), trade6.getStorageUsd(), trade6.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client3.getTradingAccount().toString(), client3.getServerId().toString(), null, trade7.getSymbol(), trade7.getProfit(), trade7.getProfit(), trade7.getVolumeLots(), trade7.getProfitUsd() + trade7.getCommissionUsd() + trade7.getStorageUsd(), trade7.getStorageUsd(), trade7.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));

    }

    @Test
    @Tag("CSV-1287")
    @AllureId("1209")
    @DisplayName("Clickhouse Api. Get trades groupBy request by list of users in concatenated string (200)")
    void getTradesGroupByTest24() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client2.getTradingAccount() + "," + client3.getTradingAccount());
        queryParams.put("serverId", client2.getServerId() + "," + client3.getServerId());
        Response response = getTradesGroupBy(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetTradesGroupByResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetTradesGroupByResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.size(), is(2));

        GetTradesGroupByResponse responseGroup1 = new GetTradesGroupByResponse(client2.getTradingAccount().toString(), client2.getServerId().toString(), null, trade6.getSymbol(), trade6.getProfit(), trade6.getProfit(), trade6.getVolumeLots(), trade6.getProfitUsd() + trade6.getCommissionUsd() + trade6.getStorageUsd(), trade6.getStorageUsd(), trade6.getCommissionUsd());
        GetTradesGroupByResponse responseGroup2 = new GetTradesGroupByResponse(client3.getTradingAccount().toString(), client3.getServerId().toString(), null, trade7.getSymbol(), trade7.getProfit(), trade7.getProfit(), trade7.getVolumeLots(), trade7.getProfitUsd() + trade7.getCommissionUsd() + trade7.getStorageUsd(), trade7.getStorageUsd(), trade7.getCommissionUsd());
        assertThat("Assert response body", mappedResponse, containsInAnyOrder(responseGroup1, responseGroup2));
    }
}
