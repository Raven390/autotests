package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getCredits.GetCreditsResponse;
import businessObjects.db.clickhouse.mtTbCreditsTable.MtTbCreditsObject;
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

import static businessObjects.api.clickhouseApiService.getCredits.GetCreditsRequest.getCredits;
import static businessObjects.db.clickhouse.mtTbCreditsTable.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.formatTimeToUtc;
import static utils.Utils.getTomorrowTimestampDbFormat;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CREDITS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetCreditsTests extends TestBaseApi {

    private static MtTbCreditsObject credit1;
    private static MtTbCreditsObject credit2;

    @BeforeAll
    public static void setupCredits() throws ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomVantageClient();
        credit1 = generateCreditsByClient(client);
        credit2 = generateCreditsByClient(client);
        credit2.account = credit1.account;
        credit2.createTime = getTomorrowTimestampDbFormat();
        credit2.amountUsd = 2.0;
        insertObjectToDb(MT_CREDITS_TABLE_NAME, credit1);
        insertObjectToDb(MT_CREDITS_TABLE_NAME, credit2);
    }

    @AfterAll
    public static void teardownCredits() throws SQLException {
        deleteEntryFromDb(MT_CREDITS_TABLE_NAME, String.format("ucid = '%s'", credit1.ucid));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client credits by all params")
    @AllureId("402")
    public void getCreditsAllParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("dateFrom", credit1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", credit2.createTime.replace(" ", "T"));
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert tradeId", mappedResponse[0].tradeId, is(credit2.ticket));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(formatTimeToUtc(credit2.createTime)));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(credit2.account.toString()));
        assertThat("Assert profitUSD", mappedResponse[0].profitUSD, is(credit2.amountUsd));
        assertThat("Assert profit", mappedResponse[0].profit, is(credit2.amount));
        assertThat("Assert comment", mappedResponse[0].comment, is(credit2.comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client credits by empty params")
    @AllureId("403")
    public void getCreditsEmptyParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("orderBy", "");
        queryParams.put("sortOrder", "");
        queryParams.put("limit", "");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client credits mandatory parameters(200)")
    @AllureId("211")
    public void getCreditsClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client credits by mandatory params and limit")
    @AllureId("404")
    public void getCreditsLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "1");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(1));
        assertThat("Assert tradeId", mappedResponse[0].tradeId, is(credit2.ticket));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(formatTimeToUtc(credit2.createTime)));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(credit2.account.toString()));
        assertThat("Assert profitUSD", mappedResponse[0].profitUSD, is(credit2.amountUsd));
        assertThat("Assert profit", mappedResponse[0].profit, is(credit2.amount));
        assertThat("Assert comment", mappedResponse[0].comment, is(credit2.comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client credits order by create time default order")
    @AllureId("405")
    public void getCreditsDefaultSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("orderBy", "createTime");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert tradeId", mappedResponse[0].tradeId, is(credit1.ticket));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(formatTimeToUtc(credit1.createTime)));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(credit1.account.toString()));
        assertThat("Assert profitUSD", mappedResponse[0].profitUSD, is(credit1.amountUsd));
        assertThat("Assert profit", mappedResponse[0].profit, is(credit1.amount));
        assertThat("Assert comment", mappedResponse[0].comment, is(credit1.comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses order by profitUSD")
    @AllureId("406")
    public void getCreditsOrderByAmountUsdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("orderBy", "profitUSD");
        queryParams.put("sortOrder", "desc");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert profitUSD", mappedResponse[0].profitUSD, is(credit2.amountUsd));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits no params")
    @AllureId("407")
    public void getCreditsNoParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits no tradingAccount")
    @AllureId("408")
    public void getCreditsNoTradingAccountTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("dateFrom", credit1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", credit2.createTime.replace(" ", "T"));
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits no serverId")
    @AllureId("409")
    public void getCreditsNoServerIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("dateFrom", credit1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", credit2.createTime.replace(" ", "T"));
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits incorrect dateFrom")
    @AllureId("410")
    public void getCreditsIncorrectDateFromTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("dateFrom", "test");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/credits"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits incorrect dateTo")
    @AllureId("411")
    public void getCreditsIncorrectDateToTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("dateTo", "test");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/credits"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits incorrect orderBy")
    @AllureId("412")
    public void getCreditsIncorrectOrderByTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("orderBy", "test");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid &quot;orderBy&quot; property format. The property may include only: createTime, profit, profitUSD"));
        assertThat("Assert status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits incorrect sortOrder")
    @AllureId("413")
    public void getCreditsIncorrectSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("sortOrder", "test");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
        assertThat("Assert status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits incorrect limit")
    @AllureId("414")
    public void getCreditsIncorrectLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("limit", "test");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/credits"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }
}
