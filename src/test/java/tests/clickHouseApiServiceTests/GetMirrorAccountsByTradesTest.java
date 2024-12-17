package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getMirrorAccountsByTrades.GetMirrorAccountsByTradesResponse;
import businessObjects.db.clickhouse.aggrMirrorAccountsByTrades.AggrMirrorAccountsByTradesObject;
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

import static businessObjects.api.clickhouseApiService.getMirrorAccountsByTrades.GetMirrorAccountsByTradesRequest.getMirrorAccountsByTrades;
import static businessObjects.db.clickhouse.aggrMirrorAccountsByTrades.AggrMirrorAccountsByTradesObjectFactory.generateMirrorTradesByAccount;
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
public class GetMirrorAccountsByTradesTest extends TestBaseApi {

    private static AggrMirrorAccountsByTradesObject data1;
    private static final ClientHelper client1 = getRandomVantageClient();
    private static final String symbol = "EURUSD";
    public static final String dateTo = getCurrentTimestampDbFormat();
    public static final String dateFrom = getTomorrowTimestampDbFormat();


    @BeforeAll
    public static void setupMirrorTrades() throws ReflectiveOperationException, SQLException {
        data1 = generateMirrorTradesByAccount(client1);
        insertObjectToDb(AGGR_MIRROR_ACCOUNTS_BY_TRADES, data1);
    }

    @AfterAll
    public static void teardownMirrorTrades() throws SQLException {
        deleteEntryFromDb(AGGR_MIRROR_ACCOUNTS_BY_TRADES, String.format("request_trading_account = '%s'", data1.requestTradingAccount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades (200)")
    @AllureId("217")
    public void getMirrorTradeAccountsByTradesTest1() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", dateFrom.replace(" ", "T"));
        queryParams.put("dateTo", dateTo.replace(" ", "T"));
        queryParams.put("symbol", symbol); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert requestTradingAccount", mappedResponse.originalAccount.requestTradingAccount, is(data1.requestTradingAccount));
        assertThat("Assert requestServerId", mappedResponse.originalAccount.requestServerId, is(data1.requestServerId));
        assertThat("Assert requestVolumeInLots", mappedResponse.originalAccount.requestVolumeInLots, is(data1.requestVolumeInLots.toString()));
        assertThat("Assert list size", mappedResponse.mirrorAccounts.size(), is(1));
        assertThat("Assert tradingAccount", mappedResponse.mirrorAccounts.getFirst().tradingAccount, is(data1.mirrorAccounts));
        assertThat("Assert serverId", mappedResponse.mirrorAccounts.getFirst().serverId, is(data1.mirrorServerId));
        assertThat("Assert volumeInLots", mappedResponse.mirrorAccounts.getFirst().volumeInLots, is(data1.mirrorVolumeInLots.toString()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with required params (200)")
    @AllureId("436")
    public void getMirrorTradeAccountsByTradesTest2() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("symbol", symbol); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with account and serverId (400)")
    @AllureId("437")
    public void getMirrorTradeAccountsByTradesTest3() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "1"); // Required
        queryParams.put("serverId", "1"); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'symbol' for method parameter type String is not present"));
        assertThat("Assert that code is 400", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with serverId and symbol(400)")
    @AllureId("438")
    public void getMirrorTradeAccountsByTradesTest4() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", "1"); // Required
        queryParams.put("symbol", "2"); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with tradingAccount and symbol (400)")
    @AllureId("439")
    public void getMirrorTradeAccountsByTradesTest5() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "1"); // Required
        queryParams.put("symbol", "1"); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades dateFrom (200)")
    @AllureId("440")
    public void getMirrorTradeAccountsByTradesTest6() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", dateFrom.replace(" ", "T"));
        queryParams.put("symbol", symbol); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades dateTo (200)")
    @AllureId("441")
    public void getMirrorTradeAccountsByTradesTest7() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", dateTo.replace(" ", "T"));
        queryParams.put("symbol", symbol); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades not found by account (200)")
    @AllureId("442")
    public void getMirrorTradeAccountsByTradesTest9() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "1"); // Required
        queryParams.put("serverId", "1"); // Required
        queryParams.put("symbol", "1"); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert that original account is null", mappedResponse.originalAccount, is(nullValue()));
        assertThat("Assert that mirror accounts list is empty", mappedResponse.mirrorAccounts, is(empty()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades not found by serverId (200)")
    @AllureId("443")
    public void getMirrorTradeAccountsByTradesTest10() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", "1"); // Required
        queryParams.put("symbol", symbol); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert that original account is null", mappedResponse.originalAccount, is(nullValue()));
        assertThat("Assert that mirror accounts list is empty", mappedResponse.mirrorAccounts, is(empty()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades not found by symbol (200)")
    @AllureId("444")
    public void getMirrorTradeAccountsByTradesTest11() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("symbol", "1"); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert that original account is null", mappedResponse.originalAccount, is(nullValue()));
        assertThat("Assert that mirror accounts list is empty", mappedResponse.mirrorAccounts, is(empty()));
    }
}
