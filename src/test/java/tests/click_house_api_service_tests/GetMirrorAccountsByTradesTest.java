package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_mirror_accounts_by_trades.GetMirrorAccountsByTradesResponse;
import business_objects.db.clickhouse.aggr_mirror_accounts_by_trades.MirrorLoginObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static business_objects.api.clickhouse_api_service.get_mirror_accounts_by_trades.GetMirrorAccountsByTradesRequest.getMirrorAccountsByTrades;
import static business_objects.db.clickhouse.aggr_mirror_accounts_by_trades.MirrorLoginObjectFactory.generateMirrorTradesByAccount;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getTomorrowTimestampDbFormat;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_MIRROR_ACCOUNTS_BY_TRADES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetMirrorAccountsByTradesTest extends TestBaseApi {

    private static MirrorLoginObject data1;
    private static final ClientHelper client1 = getRandomVantageClient();
    private static final String symbol = "EURUSD";
    private static final String dateTo = getTomorrowTimestampDbFormat().replace(" ", "T");
    private static final String dateFrom = getCurrentTimestampDbFormat().replace(" ", "T");

    @BeforeAll
    static void setup() {
        data1 = generateMirrorTradesByAccount(client1);
        insertObjectToDb(MIRROR_LOGIN_TABLE_NAME, data1);
    }

    @AfterAll
    static void teardown() {
        deleteEntryFromDb(MIRROR_LOGIN_TABLE_NAME, String.format("login_1 = '%s'", data1.login_1));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades (200)")
    @AllureId("217")
    void getMirrorTradeAccountsByTradesTest1() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", dateFrom);
        queryParams.put("dateTo", dateTo);
        queryParams.put("symbol", symbol); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetMirrorAccountsByTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert requestTradingAccount", mappedResponse[0].originalTradingAccount, is(data1.login_1));
        assertThat("Assert requestServerId", mappedResponse[0].originalServerId, is(data1.server_id_1));
        assertThat("Assert requestVolumeInLots", mappedResponse[0].originalVolumeInLots, is(data1.lots_1.toString()));
        assertThat("Assert list size", mappedResponse.length, is(1));
        assertThat("Assert tradingAccount", mappedResponse[0].mirrorTradingAccount, is(data1.login_2));
        assertThat("Assert serverId", mappedResponse[0].mirrorServerId, is(data1.server_id_2));
        assertThat("Assert volumeInLots", mappedResponse[0].mirrorVolumeInLots, is(data1.lots_2.toString()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with required params (200)")
    @AllureId("436")
    void getMirrorTradeAccountsByTradesTest2() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("symbol", symbol); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with account and serverId (400)")
    @AllureId("437")
    void getMirrorTradeAccountsByTradesTest3() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "1"); // Required
        queryParams.put("serverId", "1"); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Required request parameter 'symbol' for method parameter type String is not present"));
        assertThat("Assert that code is 400", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with serverId and symbol(400)")
    @AllureId("438")
    void getMirrorTradeAccountsByTradesTest4() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", "1"); // Required
        queryParams.put("symbol", "2"); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.getStatus(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with tradingAccount and symbol (400)")
    @AllureId("439")
    void getMirrorTradeAccountsByTradesTest5() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "1"); // Required
        queryParams.put("symbol", "1"); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.getStatus(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Required request parameter 'serverId' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades dateFrom (200)")
    @AllureId("440")
    void getMirrorTradeAccountsByTradesTest6() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", dateFrom.replace(" ", "T"));
        queryParams.put("symbol", symbol); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades dateTo (200)")
    @AllureId("441")
    void getMirrorTradeAccountsByTradesTest7() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", dateTo.replace(" ", "T"));
        queryParams.put("symbol", symbol); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades not found by account (200)")
    @AllureId("442")
    void getMirrorTradeAccountsByTradesTest9() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "1"); // Required
        queryParams.put("serverId", "1"); // Required
        queryParams.put("symbol", "1"); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetMirrorAccountsByTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert that mirror accounts list is empty", mappedResponse.length, is(0));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades not found by serverId (200)")
    @AllureId("443")
    void getMirrorTradeAccountsByTradesTest10() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", "1"); // Required
        queryParams.put("symbol", symbol); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetMirrorAccountsByTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert that mirror accounts list is empty", mappedResponse.length, is(0));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades not found by symbol (200)")
    @AllureId("444")
    void getMirrorTradeAccountsByTradesTest11() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("symbol", "1"); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetMirrorAccountsByTradesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert that mirror accounts list is empty", mappedResponse.length, is(0));
    }
}
