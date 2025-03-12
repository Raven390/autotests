package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getCreditEquityRatio.GetCreditEquityResponse;
import businessObjects.db.clickhouse.mtAccount.MtAccountObject;
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

import static businessObjects.api.clickhouseApiService.getCreditEquityRatio.GetCreditEquityRequest.getCreditEquity;
import static businessObjects.db.clickhouse.mtAccount.MtAccountObjectFactory.generateMtAccountByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.formatTimeToUtc;
import static utils.Utils.getTomorrowTimestampDbFormat;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CREDIT_EQUITY)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetCreditEquityRatioTests extends TestBaseApi {

    public static final String date = "2024-12-31 00:00:00".replace(" ", "T");

    public static MtAccountObject data1;
    public static final ClientHelper client1 = getRandomVantageClient();
    public static final String dateFrom = getTomorrowTimestampDbFormat();

    @BeforeAll
    public static void setupData() {
        data1 = generateMtAccountByClient(client1);
        data1.equityUsd = 1d;//currentEquity
        data1.creditUsd = 2d;//sumCreditOrder
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, data1);
    }

    @AfterAll
    public static void teardownData() {
        deleteEntryFromDb(MT_ACCOUNT_TABLE_NAME, String.format("account = '%s'", data1.account));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio by dateTo (200)")
    @AllureId("216")
    public void getCreditEquityRatioTest1() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", date);
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradingAccount", mappedResponse.tradingAccount, is(client1.getTradingAccount()));
        assertThat("Assert tradingIndicators size", mappedResponse.tradingIndicators.size(), is(3));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.getFirst().indicatorDate, is(formatTimeToUtc(date)));
        assertThat("Assert tradingIndicators currentEquity", mappedResponse.tradingIndicators.getFirst().currentEquity, is("1"));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.get(1).indicatorDate, is(formatTimeToUtc(date)));
        assertThat("Assert tradingIndicators sumCreditOrder", mappedResponse.tradingIndicators.get(1).sumCreditOrder, is("2"));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.get(2).indicatorDate, is(formatTimeToUtc(date)));
        assertThat("Assert tradingIndicators creditEquityRatio", mappedResponse.tradingIndicators.get(2).creditEquityRatio, is("-2"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio request only by tradingAccount (400)")
    @AllureId("445")
    public void getCreditEquityRatioTest2() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio request only by serverId (400)")
    @AllureId("446")
    public void getCreditEquityRatioTest3() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", client1.getServerId()); // Required
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio request without date parameters (200)")
    @AllureId("447")
    public void getCreditEquityRatioTest4() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        assertThat("Assert that code is 200", response.code(), is(400));
        assertThat("Assert that code is 200", response.body().string(), containsString("Required request parameter 'dateTo' for method parameter type LocalDateTime is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio by dateTo, empty response (200)")
    @AllureId("564")
    public void getCreditEquityRatioTest5() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", "2020-12-30 00:00:01".replace(" ", "T"));
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response body", response.body().string(), is("{}"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio with wrong date format(200)")
    @AllureId("565")
    public void getCreditEquityRatioTest6() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", "1");
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert type", mappedResponse.type, is("about:blank"));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateTo' with value: '1'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/creditEquityRatio"));
    }
}
