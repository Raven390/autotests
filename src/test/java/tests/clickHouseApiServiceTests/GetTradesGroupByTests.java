package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getTradesGroupBy.GetTradesGroupByResponse;
import businessObjects.api.clickhouseApiService.getTradesGroupBy.GetTradesGroupByResponseError;
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

import static businessObjects.api.clickhouseApiService.getTradesGroupBy.GetTradesGroupByRequest.getTradesGroupBySymbol;
import static businessObjects.db.mtMt5DealsTable.Mt5DealsFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_TRADES_GROUP_BY_SYMBOL)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Disabled
public class GetTradesGroupByTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request (200)")
    @AllureId("213")
    public void getTradesTest1() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("action", "");
        queryParams.put("entry", "");
        queryParams.put("orderBy", ""); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", ""); // Enum - asc, desc
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse
                mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert list size", mappedResponse.groups.size(), is(2));
        assertThat("Assert symbol", mappedResponse.groups.getFirst(), is("EURUSD"));
        assertThat("Assert profit", mappedResponse.groups.getFirst(), is(1500.500_00));
        assertThat("Assert profitUSD", mappedResponse.groups.getFirst(), is(1500.500_00));
        assertThat("Assert symbol", mappedResponse.groups.get(1), is("EURUSD"));
        assertThat("Assert profit", mappedResponse.groups.get(1), is(1500.500_00));
        assertThat("Assert profitUSD", mappedResponse.groups.get(1), is(1500.500_00));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request with required params(200)")
    @AllureId("")
    public void getTradesTest2() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request with serverId=1(404)")
    @AllureId("")
    public void getTradesTest3() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "1");
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError
                mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request with tradingAccount=1(404)")
    @AllureId("")
    public void getTradesTest4() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "1");
        queryParams.put("serverId", "");
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request with tradingAccount(400)")
    @AllureId("")
    public void getTradesTest5() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request with serverId(400)")
    @AllureId("")
    public void getTradesTest6() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", "");
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request without parameters(400)")
    @AllureId("")
    public void getTradesTest7() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request with dateFrom(200)")
    @AllureId("")
    public void getTradesTest8() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request with dateTo(200)")
    @AllureId("")
    public void getTradesTest9() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("dateTo", "");
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request with dateRange(200)")
    @AllureId("")
    public void getTradesTest10() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by action(200)")
    @AllureId("213")
    public void getTradesTest11() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("action", "");
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by entry (200)")
    @AllureId("")
    public void getTradesTest12() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("entry", "");
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by orderBy=createTime(200)")
    @AllureId("")
    public void getTradesTest13() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "createTime"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by orderBy=actualAmount(200)")
    @AllureId("")
    public void getTradesTest14() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmount"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by orderBy=actualAmountUSD(200)")
    @AllureId("")
    public void getTradesTest15() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by orderBy=createTime and sortOrder=asc(200)")
    @AllureId("")
    public void getTradesTest16() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "createTime"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by orderBy=actualAmount and sortOrder=asc(200)")
    @AllureId("")
    public void getTradesTest17() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmount"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by orderBy=actualAmountUSD and sortOrder=asc(200)")
    @AllureId("")
    public void getTradesTest18() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by orderBy=createTime and sortOrder=desc(200)")
    @AllureId("")
    public void getTradesTest19() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "createTime"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by orderBy=actualAmount and sortOrder=desc(200)")
    @AllureId("")
    public void getTradesTest20() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmount"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by orderBy=actualAmountUSD and sortOrder=desc(200)")
    @AllureId("")
    public void getTradesTest21() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get trades grouped request by limit(200)")
    @AllureId("")
    public void getTradesTest22() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_TRADES_TABLE_NAME, generateTradeByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getTradesGroupBySymbol(queryParams);

        assert response.body() != null;
        GetTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }
}
