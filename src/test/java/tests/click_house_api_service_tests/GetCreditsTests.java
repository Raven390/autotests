package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_credits.GetCreditsResponse;
import business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObject;
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

import static business_objects.api.clickhouse_api_service.get_credits.GetCreditsRequest.getCredits;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.mt_tb_credits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.CleanTableHelper.cleanMtCreditsTableByUcid;
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
class GetCreditsTests extends TestBaseApi {

    private static MtTbCreditsObject credit1;
    private static MtTbCreditsObject credit2;

    @BeforeAll
    static void setupCredits() {
        ClientHelper client = getRandomVantageClient();
        credit1 = generateCreditsByClient(client);
        credit2 = generateCreditsByClient(client);
        credit2.account = credit1.account;
        credit2.createTime = getTomorrowTimestampDbFormat();
        credit2.amountUsd = 2.0;
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, generateCrmTbAccountData(client));
        insertObjectToDb(MT_CREDITS_TABLE_NAME, credit1);
        insertObjectToDb(MT_CREDITS_TABLE_NAME, credit2);
    }

    @AfterAll
    static void teardownCredits() throws Exception {
        cleanMtCreditsTableByUcid(credit1.ucid, credit2.ucid);
    }

    @Test
    @DisplayName("Clickhouse Api. Get client credits by all params")
    @AllureId("402")
    void getCreditsAllParamsTest() throws IOException {
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
        assertThat("Assert clientId", mappedResponse[0].clientId, is(credit2.ucid));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(formatTimeToUtc(credit2.createTime)));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(credit2.account.toString()));
        assertThat("Assert profitUSD", mappedResponse[0].profitUSD, is(credit2.amountUsd));
        assertThat("Assert profit", mappedResponse[0].profit, is(credit2.amount));
        assertThat("Assert comment", mappedResponse[0].comment, is(credit2.comment));
        assertThat("Assert comment", mappedResponse[0].clientId, is(credit1.ucid));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client credits by all params with clientId")
    @AllureId("894")
    void getCreditsAllParamsClientIdTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", credit1.ucid);
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
        assertThat("Assert clientId", mappedResponse[0].clientId, is(credit2.ucid));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(formatTimeToUtc(credit2.createTime)));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(credit2.account.toString()));
        assertThat("Assert profitUSD", mappedResponse[0].profitUSD, is(credit2.amountUsd));
        assertThat("Assert profit", mappedResponse[0].profit, is(credit2.amount));
        assertThat("Assert comment", mappedResponse[0].comment, is(credit2.comment));
        assertThat("Assert comment", mappedResponse[0].clientId, is(credit1.ucid));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client credits by empty params")
    @AllureId("403")
    void getCreditsEmptyParamsTest() throws IOException {

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
    void getCreditsClientIdTest() throws IOException {
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
    void getCreditsLimitTest() throws IOException {
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
        assertThat("Assert clientId", mappedResponse[0].clientId, is(credit2.ucid));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(formatTimeToUtc(credit2.createTime)));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(credit2.account.toString()));
        assertThat("Assert profitUSD", mappedResponse[0].profitUSD, is(credit2.amountUsd));
        assertThat("Assert profit", mappedResponse[0].profit, is(credit2.amount));
        assertThat("Assert comment", mappedResponse[0].comment, is(credit2.comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client credits order by create time default order")
    @AllureId("405")
    void getCreditsDefaultSortOrderTest() throws IOException {
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
        assertThat("Assert clientId", mappedResponse[0].clientId, is(credit1.ucid));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(formatTimeToUtc(credit1.createTime)));
        assertThat("Assert tradingAccount", mappedResponse[0].tradingAccount, is(credit1.account.toString()));
        assertThat("Assert profitUSD", mappedResponse[0].profitUSD, is(credit1.amountUsd));
        assertThat("Assert profit", mappedResponse[0].profit, is(credit1.amount));
        assertThat("Assert comment", mappedResponse[0].comment, is(credit1.comment));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses order by profitUSD")
    @AllureId("406")
    void getCreditsOrderByAmountUsdTest() throws IOException {
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
    void getCreditsNoParamsTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Either clientId or tradingAccount and serverId must be provided."));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits no tradingAccount")
    @AllureId("408")
    void getCreditsNoTradingAccountTest() throws IOException {
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
        assertThat("Assert error message", mappedResponse.getError(), is("Either clientId or tradingAccount and serverId must be provided."));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits no serverId")
    @AllureId("409")
    void getCreditsNoServerIdTest() throws IOException {
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
        assertThat("Assert error message", mappedResponse.getError(), is("Either clientId or tradingAccount and serverId must be provided."));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits incorrect dateFrom")
    @AllureId("410")
    void getCreditsIncorrectDateFromTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("dateFrom", "test");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/credits"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits incorrect dateTo")
    @AllureId("411")
    void getCreditsIncorrectDateToTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("dateTo", "test");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/credits"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits incorrect orderBy")
    @AllureId("412")
    void getCreditsIncorrectOrderByTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("orderBy", "test");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.getError(), is("Invalid &quot;orderBy&quot; property format. The property may include only: createTime, profit, profitUSD"));
        assertThat("Assert status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits incorrect sortOrder")
    @AllureId("413")
    void getCreditsIncorrectSortOrderTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("sortOrder", "test");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.getError(), is("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
        assertThat("Assert status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits incorrect limit")
    @AllureId("414")
    void getCreditsIncorrectLimitTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", credit1.account);
        queryParams.put("serverId", credit1.serverId);
        queryParams.put("limit", "test");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/credits"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }
}
