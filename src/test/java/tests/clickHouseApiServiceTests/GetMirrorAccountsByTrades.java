package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getMirrorAccountsByTrades.GetMirrorAccountsByTradesResponse;
import businessObjects.api.clickhouseApiService.getMirrorAccountsByTrades.GetMirrorAccountsByTradesResponseError;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getMirrorAccountsByTrades.GetMirrorAccountsByTradesRequest.getMirrorAccountsByTrades;
import static helpers.data.ClientFactory.getRandomClient;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_MIRROR_ACCOUNTS_BY_TRADES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Disabled
public class GetMirrorAccountsByTrades extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades (200)")
    @AllureId("217")
    public void getMirrorTradeAccountsByTradesTest1() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", ""); // Required
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("symbol", ""); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert requestTradingAccount", mappedResponse.requestTradingAccount, is(1_241_413));
        assertThat("Assert requestServerId", mappedResponse.requestServerId, is("23"));
        assertThat("Assert requestVolumeInLots", mappedResponse.requestVolumeInLots, is("1.50334"));
        assertThat("Assert Symbol", mappedResponse.Symbol, is("XAUEUR"));
        assertThat("Assert list size", mappedResponse.mirrorAccounts.size(), is(2));
        assertThat("Assert tradingAccount", mappedResponse.mirrorAccounts.getFirst().tradingAccount, is("849600"));
        assertThat("Assert serverId", mappedResponse.mirrorAccounts.getFirst().serverId, is("44"));
        assertThat("Assert volumeInLots", mappedResponse.mirrorAccounts.getFirst().volumeInLots, is(1.522_31));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with required params(200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest2() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", ""); // Required
        queryParams.put("symbol", ""); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with account and serverId(400)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest3() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", ""); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with serverId and symbol(400)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest4() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", ""); // Required
        queryParams.put("symbol", ""); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with tradingAccount and symbol(400)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest5() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("symbol", ""); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades dateFrom(200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest6() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", ""); // Required
        queryParams.put("dateFrom", "");
        queryParams.put("symbol", ""); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades dateTo(200)")
    @AllureId("217")
    public void getMirrorTradeAccountsByTradesTest7() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", ""); // Required
        queryParams.put("dateTo", "");
        queryParams.put("symbol", ""); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades with no params (400)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest8() throws IOException, ReflectiveOperationException, SQLException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades not found by account(404)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest9() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "1"); // Required
        queryParams.put("serverId", ""); // Required
        queryParams.put("symbol", ""); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponseError
                mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades not found by serverId(404)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest10() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", "1"); // Required
        queryParams.put("symbol", ""); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
    }

    @Test
    @DisplayName("Clickhouse Api. Get mirror trade account by trades not found by symbol(404)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest11() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount()); // Required
        queryParams.put("serverId", ""); // Required
        queryParams.put("symbol", "1"); // Required
        Response response = getMirrorAccountsByTrades(queryParams);

        assert response.body() != null;
        GetMirrorAccountsByTradesResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetMirrorAccountsByTradesResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
    }
}
