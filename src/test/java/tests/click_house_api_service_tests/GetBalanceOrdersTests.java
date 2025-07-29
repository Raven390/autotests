package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_balance_orders.GetBalanceOrdersResponse;
import business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObject;
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

import static business_objects.api.clickhouse_api_service.get_balance_orders.GetBalanceOrdersRequest.getBalanceOrders;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrders;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.CleanTableHelper.cleanMtBalanceOrdersTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_BALANCE_ORDERS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetBalanceOrdersTests extends TestBaseApi {

    private static MtBalanceOrdersObject data1;
    private static MtBalanceOrdersObject data2;
    private static final ClientHelper client1 = getRandomVantageClient();
    static final String dateTo = formatTimeToUtc(getNextYearTimestampDbFormat());
    static final String dateFrom = formatTimeToUtc(getPreviousYearTimestampDbFormat());
    static String tradeDate1 = "2024-12-10 17:59:14";
    static String tradeDate2 = "2024-12-10 17:59:15";
    static String comment = "Administration Fee Automation test";

    @BeforeAll
    static void setupData() {
        data1 = generateMtBalanceOrders(client1, 1d, 2d, tradeDate1);
        data2 = generateMtBalanceOrders(client1, 3d, 4d, tradeDate2);
        insertObjectsToDb(MT_BALANCE_ORDERS_TABLE_NAME, List.of(data1, data2));
    }

    @AfterAll
    static void teardownData() throws Exception {
        cleanMtBalanceOrdersTableByClient(data1.ucid, data2.ucid);
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with required params")
    @AllureId("659")
    void getBalanceOrderTest1() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        List<GetBalanceOrdersResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetBalanceOrdersResponse[].class)).toList();
        GetBalanceOrdersResponse object1 = new GetBalanceOrdersResponse(formatTimeToUtc(tradeDate1), data1.ticket, client1.getTradingAccount(), 1d, 2d, comment);
        GetBalanceOrdersResponse object2 = new GetBalanceOrdersResponse(formatTimeToUtc(tradeDate2), data2.ticket, client1.getTradingAccount(), 3d, 4d, comment);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check list size", mappedResponse.size(), is(2));
        assertThat("Check response", mappedResponse, containsInAnyOrder(object1, object2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with all params")
    @AllureId("660")
    void getBalanceOrderTest2() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", dateFrom);
        queryParams.put("dateTo", dateTo);
        queryParams.put("orderBy", "tradeDate");
        queryParams.put("sortOrder", "asc");
        queryParams.put("limit", "1");
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        GetBalanceOrdersResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBalanceOrdersResponse[].class);

        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(1));

        assertThat("Assert tradeId", mappedResponse[0].getTradeId(), is(data1.ticket));
        assertThat("Assert tradingAccount", mappedResponse[0].getTradingAccount(), is(client1.getTradingAccount()));
        assertThat("Assert profit", mappedResponse[0].getProfit(), is(1.0));
        assertThat("Assert profitUSD", mappedResponse[0].getProfitUsd(), is(2.0));
        assertThat("Assert comment", mappedResponse[0].getComment(), is(comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with dateFrom")
    @AllureId("661")
    void getBalanceOrdersTest3() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", getPreviousYearTimestampDbFormat().replace(" ", "T"));
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        GetBalanceOrdersResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBalanceOrdersResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with dateTo")
    @AllureId("662")
    void getBalanceOrdersTest4() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", getNextYearTimestampDbFormat().replace(" ", "T"));
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        GetBalanceOrdersResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBalanceOrdersResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with orderBy=tradeDate and sortOrder=asc")
    @AllureId("663")
    void getBalanceOrdersTest5() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "tradeDate"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "asc"); // asc, desc
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        GetBalanceOrdersResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBalanceOrdersResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert tradeDate", mappedResponse[0].getTradeDate(), is(formatTimeToUtc(tradeDate1)));
        assertThat("Assert tradeDate", mappedResponse[1].getTradeDate(), is(formatTimeToUtc(tradeDate2)));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with orderBy=tradeDate and sortOrder=desc")
    @AllureId("664")
    void getBalanceOrdersTest6() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "tradeDate"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "desc"); // asc, desc
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        GetBalanceOrdersResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBalanceOrdersResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert tradeDate", mappedResponse[0].getTradeDate(), is(formatTimeToUtc(tradeDate2)));
        assertThat("Assert tradeDate", mappedResponse[1].getTradeDate(), is(formatTimeToUtc(tradeDate1)));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with orderBy=profit and sortOrder=asc")
    @AllureId("665")
    void getBalanceOrdersTest7() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "profit"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "asc"); // asc, desc
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        GetBalanceOrdersResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBalanceOrdersResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert profit", mappedResponse[0].getProfit(), is(1d));
        assertThat("Assert profit", mappedResponse[1].getProfit(), is(3d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with orderBy=profit and sortOrder=desc")
    @AllureId("666")
    void getBalanceOrdersTest8() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "profit"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "desc"); // asc, desc
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        GetBalanceOrdersResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBalanceOrdersResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert profit", mappedResponse[0].getProfit(), is(3d));
        assertThat("Assert profit", mappedResponse[1].getProfit(), is(1d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with orderBy=profitUsd and sortOrder=asc")
    @AllureId("667")
    void getBalanceOrdersTest9() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "profitUSD"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "asc"); // asc, desc
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        GetBalanceOrdersResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBalanceOrdersResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert profitUSD", mappedResponse[0].getProfitUsd(), is(2d));
        assertThat("Assert profitUSD", mappedResponse[1].getProfitUsd(), is(4d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with orderBy=profit and sortOrder=desc")
    @AllureId("668")
    void getBalanceOrdersTest10() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "profitUSD"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "desc"); // asc, desc
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        GetBalanceOrdersResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBalanceOrdersResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert profitUSD", mappedResponse[0].getProfitUsd(), is(4d));
        assertThat("Assert profitUSD", mappedResponse[1].getProfitUsd(), is(2d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with only tradingAccount")
    @AllureId("669")
    void getBalanceOrdersTest11() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), is(400));
        assertThat("Assert error", mappedResponse.getError(), is("Required request parameter 'serverId' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with only serverId")
    @AllureId("670")
    void getBalanceOrdersTest12() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", 1); // Required
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), is(400));
        assertThat("Assert error", mappedResponse.getError(), is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with wrong dateFrom")
    @AllureId("671")
    void getBalanceOrdersTest13() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", 1);
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), is(400));
        assertThat("Assert type", mappedResponse.getType(), is("about:blank"));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateFrom' with value: '1'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/balanceOrders"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with wrong dateTo")
    @AllureId("672")
    void getBalanceOrdersTest14() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", 1);
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), is(400));
        assertThat("Assert type", mappedResponse.getType(), is("about:blank"));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateTo' with value: '1'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/balanceOrders"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with wrong orderBy")
    @AllureId("673")
    void getBalanceOrdersTest15() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "1"); // tradeDate, profit, profitUsd
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), is(400));
        assertThat("Assert type", mappedResponse.getError(), containsString("The property may include only: tradeDate, profit, profitUSD"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with wrong sortOrder")
    @AllureId("674")
    void getBalanceOrdersTest16() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "tradeDate"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "1"); // asc, desc
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), is(400));
        assertThat("Assert type", mappedResponse.getError(), containsString("The property may include only: asc, desc"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get balance orders with wrong limit")
    @AllureId("675")
    void getBalanceOrdersTest17() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("limit", "a");
        Response response = getBalanceOrders(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.getStatus(), is(400));
        assertThat("Assert type", mappedResponse.getType(), is("about:blank"));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'limit' with value: 'a'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/balanceOrders"));
    }
}
