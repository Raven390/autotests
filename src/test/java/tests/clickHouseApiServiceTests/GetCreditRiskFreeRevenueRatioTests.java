package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getCreditRiskFreeRevenueRatio.getWithdrawals.GetCreditRiskFreeRevenueRatioResponse;
import businessObjects.api.clickhouseApiService.getCreditRiskFreeRevenueRatio.getWithdrawals.GetCreditRiskFreeRevenueRatioResponseError;
import businessObjects.db.clickhouse.aggrCreditRiskFreeRevenueRatio.AggrCreditRiskFreeRevenueRatioObject;
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

import static businessObjects.api.clickhouseApiService.getCreditRiskFreeRevenueRatio.GetCreditRiskFreeRevenueRatioRequest.getCreditRiskFreeRevenueRatio;
import static businessObjects.db.clickhouse.aggrCreditRiskFreeRevenueRatio.AggrCreditRiskFreeRevenueRatioObjectFactory.generateAggrCreditRiskFreeRevenueRatioObject;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getTomorrowTimestampDbFormat;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CREDIT_RISK_FREE_REVENUE_RATIO)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Disabled
public class GetCreditRiskFreeRevenueRatioTests extends TestBaseApi {

    private static AggrCreditRiskFreeRevenueRatioObject data1;
    private static final ClientHelper client1 = getRandomVantageClient();
    public static final String dateTo = getCurrentTimestampDbFormat();
    public static final String dateFrom = getTomorrowTimestampDbFormat();

    @BeforeAll
    public static void setupData() throws ReflectiveOperationException, SQLException {
        data1 = generateAggrCreditRiskFreeRevenueRatioObject(client1);
        insertObjectToDb(AGGR_CREDIT_RISK_FREE_REVENUE_RATIO, data1);
    }

    @AfterAll
    public static void teardownData() throws SQLException {
        deleteEntryFromDb(AGGR_CREDIT_RISK_FREE_REVENUE_RATIO, String.format("trading_account = '%s'", data1.tradingAccount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio required params(200)")
    @AllureId("")
    public void getCreditRiskFreeRevenueRatioTest1() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        GetCreditRiskFreeRevenueRatioResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditRiskFreeRevenueRatioResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradingAccount", mappedResponse.tradingAccount, is(client1.getTradingAccount()));
        assertThat("Assert tradingIndicators size", mappedResponse.tradingIndicators.size(), is(3));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.getFirst().indicatorDate, is(("2024-12-31 00:00:00").replace(" ", "T")));
        assertThat("Assert tradingIndicators creditRiskFreeRevenueRatio", mappedResponse.tradingIndicators.getFirst().creditRiskFreeRevenueRatio, is("1"));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.get(1).indicatorDate, is(("2024-12-31 00:00:00").replace(" ", "T")));
        assertThat("Assert tradingIndicators sumCreditOrder", mappedResponse.tradingIndicators.get(1).sumCreditOrder, is("2"));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.get(2).indicatorDate, is(("2024-12-31 00:00:00").replace(" ", "T")));
        assertThat("Assert tradingIndicators currentRiskFreeRevenue", mappedResponse.tradingIndicators.get(2).currentRiskFreeRevenue, is("3"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio all params(200)")
    @AllureId("")
    public void getCreditRiskFreeRevenueRatioTest2() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", dateTo.replace(" ", "T"));
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        GetCreditRiskFreeRevenueRatioResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditRiskFreeRevenueRatioResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradingAccount", mappedResponse.tradingAccount, is(client1.getTradingAccount()));
        assertThat("Assert tradingIndicators size", mappedResponse.tradingIndicators.size(), is(3));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.getFirst().indicatorDate, is(("2024-12-31 00:00:00").replace(" ", "T")));
        assertThat("Assert tradingIndicators creditRiskFreeRevenueRatio", mappedResponse.tradingIndicators.getFirst().creditRiskFreeRevenueRatio, is("1"));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.get(1).indicatorDate, is(("2024-12-31 00:00:00").replace(" ", "T")));
        assertThat("Assert tradingIndicators sumCreditOrder", mappedResponse.tradingIndicators.get(1).sumCreditOrder, is("2"));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.get(2).indicatorDate, is(("2024-12-31 00:00:00").replace(" ", "T")));
        assertThat("Assert tradingIndicators currentRiskFreeRevenue", mappedResponse.tradingIndicators.get(2).currentRiskFreeRevenue, is("3"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio with only tradingAccount parameter(400)")
    @AllureId("")
    public void getCreditRiskFreeRevenueRatioTest3() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        GetCreditRiskFreeRevenueRatioResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetCreditRiskFreeRevenueRatioResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is("400"));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio with only serverId parameter(400)")
    @AllureId("")
    public void getCreditRiskFreeRevenueRatioTest4() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", client1.getServerId()); // Required
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        GetCreditRiskFreeRevenueRatioResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetCreditRiskFreeRevenueRatioResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is("400"));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio wrong date format(200)")
    @AllureId("")
    public void getCreditRiskFreeRevenueRatioTest5() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", "1");
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        GetCreditRiskFreeRevenueRatioResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetCreditRiskFreeRevenueRatioResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is("400"));
        assertThat("Assert error message", mappedResponse.error, is("Failed to convert 'dateTo' with value"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit risk free equity ratio all params(200)")
    @AllureId("")
    public void getCreditRiskFreeRevenueRatioTest6() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        queryParams.put("serverId", 1); // Required
        Response response = getCreditRiskFreeRevenueRatio(queryParams);

        assert response.body() != null;
        GetCreditRiskFreeRevenueRatioResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditRiskFreeRevenueRatioResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradingAccount", mappedResponse.tradingAccount, is(client1.getTradingAccount()));
        assertThat("Assert tradingIndicators size", mappedResponse.tradingIndicators.size(), is(0));
    }
}
