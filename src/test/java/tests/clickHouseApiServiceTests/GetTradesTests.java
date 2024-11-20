package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getTrades.GetTradesResponse;
import businessObjects.api.clickhouseApiService.getTrades.GetTradesResponseError;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Muted;
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

import static businessObjects.api.clickhouseApiService.getTrades.GetTradesRequest.getTrades;
import static businessObjects.db.clickhouse.mtMt5DealsTable.Mt5DealsFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_TRADES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Disabled
@Muted
public class GetTradesTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get trades request by all params")
    @AllureId("")
    public void getTradesTest14() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("action", "");
        queryParams.put("entry", "");
        queryParams.put("orderBy", ""); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", ""); // Enum - asc, desc
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert list size", mappedResponse.tradesItems.size(), is(2));
        assertThat("Assert tradeDate", mappedResponse.tradesItems.getFirst().tradeDate, is("2024-10-14 14:42:35"));
        assertThat("Assert tradeId", mappedResponse.tradesItems.getFirst().tradeId, is(16_252_379));
        assertThat("Assert tradingAccount", mappedResponse.tradesItems.getFirst().tradingAccount, is(51_605_455));
        assertThat("Assert action", mappedResponse.tradesItems.getFirst().action, is(0));
        assertThat("Assert entry", mappedResponse.tradesItems.getFirst().entry, is(0));
        assertThat("Assert symbol", mappedResponse.tradesItems.getFirst().symbol, is("BTCUSD"));
        assertThat("Assert profit", mappedResponse.tradesItems.getFirst().profit, is(0.000_00));
        assertThat("Assert profitUSD", mappedResponse.tradesItems.getFirst().profitUsd, is( 0.000_00));
        assertThat("Assert comment", mappedResponse.tradesItems.getFirst().comment, is(""));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request by tradingAccount and serverId (200)")
    @AllureId("")
    public void getTradesTest1() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request by without required params (400)")
    @AllureId("")
    public void getTradesTest2() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request by DateFrom (200)")
    @AllureId("")
    public void getTradesTest3() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request by dateTo (200)")
    @AllureId("")
    public void getTradesTest4() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("dateTo", "");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request by date range (200)")
    @AllureId("")
    public void getTradesTest5() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request by action (200)")
    @AllureId("")
    public void getTradesTest6() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("action", "");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request by entry (200)")
    @AllureId("")
    public void getTradesTest7() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("entry", "");
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request by orderBy=time (200)")
    @AllureId("")
    public void getTradesTest8() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "time"); // Enum - time, trading_account
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request by orderBy=trading_account (200)")
    @AllureId("")
    public void getTradesTest9() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "trading_account"); // Enum - time, trading_account
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request by limit (200)")
    @AllureId("")
    public void getTradesTest12() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request (404)")
    @AllureId("")
    public void getTradesTest13() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1);
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request sortOrder=asc and orderBy=createdTime(200)")
    @AllureId("")
    public void getTradesTest10() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "createdTime"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request sortOrder=desc and orderBy=createdTime")
    @AllureId("")
    public void getTradesTest11() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "createdTime"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request sortOrder=asc and orderBy=actualAmount")
    @AllureId("")
    public void getTradesTest15() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmount"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request sortOrder=desc and orderBy=actualAmount")
    @AllureId("")
    public void getTradesTest16() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmount"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request sortOrder=asc and orderBy=actualAmountUSD")
    @AllureId("")
    public void getTradesTest17() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades request sortOrder=desc and orderBy=actualAmountUSD")
    @AllureId("")
    public void getTradesTest18() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getTrades(queryParams);

        assert response.body() != null;
        GetTradesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

}
