package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getSwapFreeFees.GetSwapFreeFeesResponse;
import businessObjects.db.clickhouse.mtBalanceOrdersTable.MtBalanceOrdersObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getSwapFreeFees.GetSwapFreeFeesRequest.getSwapFreeFees;
import static businessObjects.db.clickhouse.mtBalanceOrdersTable.MtBalanceOrdersObjectFactory.generateBalanceOrders;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.CleanTableHelper.cleanMtBalanceOrdersTableByClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_SWAP_FREE_FEES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetSwapFreeFeesTests extends TestBaseApi {

    private static MtBalanceOrdersObject data1;
    private static MtBalanceOrdersObject data2;
    private static final ClientHelper client1 = getRandomVantageClient();
    public static final String dateTo = getNextYearTimestampDbFormat();
    public static final String dateFrom = getPreviousYearTimestampDbFormat();
    public static String tradeDate1 = "2024-12-10 17:59:14";
    public static String tradeDate2 = "2024-12-10 17:59:15";
    public static Integer tradeId = 123;
    public static String comment = "Administration Fee Automation test";


    @BeforeAll
    public static void setupData() throws ReflectiveOperationException, SQLException {
        data1 = generateBalanceOrders(client1, 1d, 2d, tradeDate1);
        data2 = generateBalanceOrders(client1, 3d, 4d, tradeDate2);
        insertObjectToDb(MT_BALANCE_ORDERS_TABLE_NAME, List.of(data1, data2));
    }

    @AfterAll
    public static void teardownData() throws Exception {
        cleanMtBalanceOrdersTableByClient(data1.ucid, data2.ucid);
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with required params")
    @AllureId("528")
    public void getSwapFreeFeesTest1() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        List<GetSwapFreeFeesResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetSwapFreeFeesResponse[].class)).toList();
        GetSwapFreeFeesResponse response1 = new GetSwapFreeFeesResponse(formatTimeToUtc(tradeDate1), tradeId, client1.getTradingAccount(), 1d, 2d, comment);
        GetSwapFreeFeesResponse response2 = new GetSwapFreeFeesResponse(formatTimeToUtc(tradeDate2), tradeId, client1.getTradingAccount(), 3d, 4d, comment);

        assertThat("Check response code", response.code(), is(200));
        assertThat("Check list size", mappedResponse.size(), is(2));
        assertThat("Check response", mappedResponse, containsInAnyOrder(response1, response2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with all params")
    @AllureId("529")
    public void getSwapFreeFeesTest2() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", data1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", data2.createTime.replace(" ", "T"));
        queryParams.put("orderBy", "tradeDate"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "asc"); // asc, desc
        queryParams.put("limit", "1");
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        GetSwapFreeFeesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeFeesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(1));

        assertThat("Assert tradeId", mappedResponse[0].tradeId, is(123));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(client1.getTradingAccount()));
        assertThat("Assert profit", mappedResponse[0].profit, is(1.0));
        assertThat("Assert profitUSD", mappedResponse[0].profitUsd, is(2.0));
        assertThat("Assert comment", mappedResponse[0].comment, is(comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with dateFrom")
    @AllureId("530")
    public void getSwapFreeFeesTest3() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", getPreviousYearTimestampDbFormat().replace(" ", "T"));
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        GetSwapFreeFeesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeFeesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with dateTo")
    @AllureId("531")
    public void getSwapFreeFeesTest4() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", getNextYearTimestampDbFormat().replace(" ", "T"));
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        GetSwapFreeFeesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeFeesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with orderBy=tradeDate and sortOrder=asc")
    @AllureId("532")
    public void getSwapFreeFeesTest5() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "tradeDate"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "asc"); // asc, desc
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        GetSwapFreeFeesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeFeesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert tradeDate", mappedResponse[0].tradeDate, is("2024-12-10T17:59:14Z"));
        assertThat("Assert tradeDate", mappedResponse[1].tradeDate, is("2024-12-10T17:59:15Z"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with orderBy=tradeDate and sortOrder=desc")
    @AllureId("533")
    public void getSwapFreeFeesTest6() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "tradeDate"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "desc"); // asc, desc
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        GetSwapFreeFeesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeFeesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert tradeDate", mappedResponse[0].tradeDate, is("2024-12-10T17:59:15Z"));
        assertThat("Assert tradeDate", mappedResponse[1].tradeDate, is("2024-12-10T17:59:14Z"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with orderBy=profit and sortOrder=asc")
    @AllureId("534")
    public void getSwapFreeFeesTest7() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "profit"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "asc"); // asc, desc
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        GetSwapFreeFeesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeFeesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert profit", mappedResponse[0].profit, is(1d));
        assertThat("Assert profit", mappedResponse[1].profit, is(3d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with orderBy=profit and sortOrder=desc")
    @AllureId("535")
    public void getSwapFreeFeesTest8() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "profit"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "desc"); // asc, desc
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        GetSwapFreeFeesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeFeesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert profit", mappedResponse[0].profit, is(3d));
        assertThat("Assert profit", mappedResponse[1].profit, is(1d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with orderBy=profitUsd and sortOrder=asc")
    @AllureId("536")
    public void getSwapFreeFeesTest9() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "profitUSD"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "asc"); // asc, desc
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        GetSwapFreeFeesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeFeesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert profitUSD", mappedResponse[0].profitUsd, is(2d));
        assertThat("Assert profitUSD", mappedResponse[1].profitUsd, is(4d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with orderBy=profit and sortOrder=desc")
    @AllureId("537")
    public void getSwapFreeFeesTest10() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "profitUSD"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "desc"); // asc, desc
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        GetSwapFreeFeesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeFeesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert length", mappedResponse.length, is(2));

        assertThat("Assert profitUSD", mappedResponse[0].profitUsd, is(4d));
        assertThat("Assert profitUSD", mappedResponse[1].profitUsd, is(2d));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with only tradingAccount")
    @AllureId("538")
    public void getSwapFreeFeesTest11() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert error", mappedResponse.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with only serverId")
    @AllureId("539")
    public void getSwapFreeFeesTest12() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", 1); // Required
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert error", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with wrong dateFrom")
    @AllureId("540")
    public void getSwapFreeFeesTest13() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", 1);
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert type", mappedResponse.type, is("about:blank"));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateFrom' with value: '1'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/swapFreeFees"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with wrong dateTo")
    @AllureId("541")
    public void getSwapFreeFeesTest14() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", 1);
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert type", mappedResponse.type, is("about:blank"));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateTo' with value: '1'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/swapFreeFees"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with wrong orderBy")
    @AllureId("542")
    public void getSwapFreeFeesTest15() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "1"); // tradeDate, profit, profitUsd
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert type", mappedResponse.error, containsString("The property may include only: tradeDate, profit, profitUSD"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with wrong sortOrder")
    @AllureId("543")
    public void getSwapFreeFeesTest16() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("orderBy", "tradeDate"); // tradeDate, profit, profitUsd
        queryParams.put("sortOrder", "1"); // asc, desc
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert type", mappedResponse.error, containsString("The property may include only: asc, desc"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free fees with wrong limit")
    @AllureId("544")
    public void getSwapFreeFeesTest17() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("limit", "a");
        Response response = getSwapFreeFees(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert type", mappedResponse.type, is("about:blank"));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'limit' with value: 'a'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/swapFreeFees"));
    }
}
