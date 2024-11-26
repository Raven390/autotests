package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getFloatingTradesGroupBy.GetFloatingTradesGroupByResponse;
import businessObjects.api.clickhouseApiService.getFloatingTradesGroupBy.GetFloatingTradesGroupByResponseError;
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

import static businessObjects.api.clickhouseApiService.getFloatingTradesGroupBy.GetFloatingTradesGroupByRequest.getFloatingTradesGroupBy;
import static helpers.data.ClientFactory.getRandomClient;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_FLOATING_TRADES_GROUP_BY)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Tag(TAG_MANUAL)
@Disabled
@Muted
public class GetFloatingTradesGroupBy extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by with all params (200)")
    @AllureId("218")
    public void getMirrorTradeAccountsByTradesTest1() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("entity", "");
        queryParams.put("action", "");
        queryParams.put("orderBy", ""); // Enum - bonusDate, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", ""); // Enum - asc, desc
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert listSize", mappedResponse.floatingTradesItems.size(), is(2));
        assertThat("Assert symbol", mappedResponse.floatingTradesItems.getFirst().symbol, is("EURUSD"));
        assertThat("Assert floatingProfit", mappedResponse.floatingTradesItems.getFirst().floatingProfit, is(1500.500_00));
        assertThat("Assert floatingProfitUSD", mappedResponse.floatingTradesItems.getFirst().floatingProfitUSD, is(1600.500_00));
        assertThat("Assert totalMargin", mappedResponse.floatingTradesItems.getFirst().totalMargin, is(12_432.500_00));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by with no params (400)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest2() throws IOException, ReflectiveOperationException, SQLException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponseError
                mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by with required params only")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest3() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group only by tradingAccount (400)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest4() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group only by serverId (400)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest5() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", "");
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by dateFrom (200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest6() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by dateTo (200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest7() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("dateTo", "");
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by date range (200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest8() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by entity (200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest9() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("entity", "");
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by action (200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest10() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("action", "");
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by orderBy=createdTime (200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest11() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "createTime"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by orderBy=actualAmount (200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest12() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmount"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by orderBy=actualAmount (200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest13() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by orderBy=createdTime and sortOrder=asc(200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest14() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "createTime"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by orderBy=actualAmount and sortOrder=asc(200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest15() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmount"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by orderBy=actualAmount and sortOrder=asc (200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest16() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by orderBy=createdTime and sortOrder=desc(200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest17() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "createTime"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by orderBy=actualAmount and sortOrder=desc(200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest18() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmount"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by orderBy=actualAmount and sortOrder=desc (200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTest19() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get floating trades group by limit (200)")
    @AllureId("")
    public void getMirrorTradeAccountsByTradesTes20() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        // TODO prepare test data

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", "");
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getFloatingTradesGroupBy(queryParams);

        assert response.body() != null;
        GetFloatingTradesGroupByResponse mappedResponse = objectMapper.readValue(response.body().string(), GetFloatingTradesGroupByResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

}
