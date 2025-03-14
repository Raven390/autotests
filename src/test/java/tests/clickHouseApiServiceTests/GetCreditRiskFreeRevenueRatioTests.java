package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getWithdrawals.GetCreditRiskFreeRevenueRatioResponse;
import businessObjects.api.clickhouseApiService.getWithdrawals.GetCreditRiskFreeRevenueRatioResponseError;
import businessObjects.db.clickhouse.aggrCreditRiskFreeRevenueRatio.AggrCreditRiskFreeRevenueRatioObject;
import businessObjects.db.clickhouse.mtTbCredits.MtTbCreditsObject;
import businessObjects.db.clickhouse.s3FactLoginMetrics.S3FactLoginMetricsObject;
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

import static businessObjects.api.clickhouseApiService.getCreditRiskFreeRevenueRatio.GetCreditRiskFreeRevenueRatioRequest.getCreditRiskFreeRevenueRatio;
import static businessObjects.db.clickhouse.aggrCreditRiskFreeRevenueRatio.AggrCreditRiskFreeRevenueRatioObjectFactory.generateAggrCreditRiskFreeRevenueRatioObject;
import static businessObjects.db.clickhouse.mtTbCredits.MtTbCreditsObjectFactory.generateCreditsByClient;
import static businessObjects.db.clickhouse.s3FactLoginMetrics.S3FactLoginMetricsFactory.generateS3FactLoginMetricsClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CREDIT_RISK_FREE_REVENUE_RATIO)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetCreditRiskFreeRevenueRatioTests extends TestBaseApi {

    private static final String date = formatTimeToUtc("2026-12-31 00:00:00");
    //public static final String date = "2024-12-31 00:00:00".replace(" ", "T");

    public static AggrCreditRiskFreeRevenueRatioObject data1;
    public static S3FactLoginMetricsObject s3Metrics;
    public static MtTbCreditsObject credit;

    public static final ClientHelper client1 = getRandomVantageClient();
    public static final String dateTo = getCurrentTimestampDbFormat();
    public static final String dateFrom = getTomorrowTimestampDbFormat();

    @BeforeAll
    public static void setupData() {
        data1 = generateAggrCreditRiskFreeRevenueRatioObject(client1);
        s3Metrics = generateS3FactLoginMetricsClient(client1);
        s3Metrics.setDailyCoreSpreadRevenuePe(6d);
        credit = generateCreditsByClient(client1);
        credit.amount = 2d;
        credit.amountUsd = 2d;
        insertObjectToDb(AGGR_CREDIT_RISK_FREE_REVENUE_RATIO, data1);
        insertObjectToDb(S3_FACT_LOGIN_METRICS_TABLE_NAME, s3Metrics);
        insertObjectToDb(MT_CREDITS_TABLE_NAME, credit);
    }

    @AfterAll
    public static void teardownData() {
        //deleteEntryFromDb(AGGR_CREDIT_RISK_FREE_REVENUE_RATIO, String.format("trading_account = '%s'", data1.tradingAccount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio required params (200)")
    @AllureId("566")
    public void getCreditRiskFreeRevenueRatioTest1() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", date); // Required
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        GetCreditRiskFreeRevenueRatioResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditRiskFreeRevenueRatioResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradingAccount", mappedResponse.tradingAccount, is(client1.getTradingAccount()));
        assertThat("Assert tradingIndicators size", mappedResponse.tradingIndicators.size(), is(3));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.get(0).indicatorDate, is(date));
        assertThat("Assert tradingIndicators currentRiskFreeRevenue", mappedResponse.tradingIndicators.get(0).currentRiskFreeRevenue, is(6));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.get(1).indicatorDate, is(date));
        assertThat("Assert tradingIndicators sumCreditOrder", mappedResponse.tradingIndicators.get(1).sumCreditOrder, is(2));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.get(2).indicatorDate, is(date));
        assertThat("Assert tradingIndicators creditRiskFreeRevenueRatio", mappedResponse.tradingIndicators.get(2).creditRiskFreeRevenueRatio, is(3));

    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio with only tradingAccount parameter(400)")
    @AllureId("568")
    public void getCreditRiskFreeRevenueRatioTest3() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        GetCreditRiskFreeRevenueRatioResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetCreditRiskFreeRevenueRatioResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is("400"));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio with only serverId parameter(400)")
    @AllureId("569")
    public void getCreditRiskFreeRevenueRatioTest4() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", client1.getServerId()); // Required
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        GetCreditRiskFreeRevenueRatioResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetCreditRiskFreeRevenueRatioResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is("400"));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio wrong date format(200)")
    @AllureId("570")
    public void getCreditRiskFreeRevenueRatioTest5() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", "1");
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert type", mappedResponse.type, is("about:blank"));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateTo' with value: '1'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/creditRiskFreeRevenueRatio"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio empty response for non existing data(200)")
    @AllureId("571")
    public void getCreditRiskFreeRevenueRatioTest6() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", 1); // Required
        queryParams.put("dateTo", date); // Required
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response body", response.body().string(), is("{}"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio empty response ,filtered by date(200)")
    @AllureId("572")
    public void getCreditRiskFreeRevenueRatioTest8() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", formatTimeToUtc("2024-12-30 00:00:00"));
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response body", response.body().string(), is("{}"));
    }
}
