package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getCreditEquityRatio.GetCreditEquityRatioResponseError;
import businessObjects.api.clickhouseApiService.getCreditEquityRatio.GetCreditEquityResponse;
import businessObjects.db.clickhouse.aggrCreditEquityRate.AggrGetCreditEquityRatioObject;
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

import static businessObjects.api.clickhouseApiService.getCreditEquityRatio.GetCreditEquityRequest.getCreditEquity;
import static businessObjects.db.clickhouse.aggrCreditEquityRate.AggrGetCreditEquityRatioObjectFactory.generateCreditEquityRatioAccount;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getTomorrowTimestampDbFormat;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CREDIT_EQUITY)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetCreditEquityRatioTests extends TestBaseApi {

    private static AggrGetCreditEquityRatioObject data1;
    private static final ClientHelper client1 = getRandomVantageClient();
    public static final String dateTo = getCurrentTimestampDbFormat();
    public static final String dateFrom = getTomorrowTimestampDbFormat();

    @BeforeAll
    public static void setupMirrorTrades() throws ReflectiveOperationException, SQLException {
        data1 = generateCreditEquityRatioAccount(client1);
        insertObjectToDb(AGGR_CREDIT_EQUITY_RATE, data1);
    }

    @AfterAll
    public static void teardownMirrorTrades() throws SQLException {
        deleteEntryFromDb(AGGR_CREDIT_EQUITY_RATE, String.format("trading_account = '%s'", data1.tradingAccount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio by date range (200)")
    @AllureId("216")
    public void getCreditEquityRatioTest1() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", dateFrom.replace(" ", "T"));
        queryParams.put("dateTo", dateTo.replace(" ", "T"));
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradingAccount", mappedResponse.tradingAccount, is(client1.getTradingAccount()));
        assertThat("Assert tradingIndicators size", mappedResponse.tradingIndicators.size(), is(1));
        assertThat("Assert tradingIndicators currentEquity", mappedResponse.tradingIndicators.getFirst().currentEquity, is("1"));
        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.getFirst().indicatorDate, is(("2024-12-31 00:00:00").replace(" ", "T")));
        assertThat("Assert tradingIndicators sumCreditOrder", mappedResponse.tradingIndicators.getFirst().sumCreditOrder, is("2"));
        assertThat("Assert tradingIndicators creditEquityRatio", mappedResponse.tradingIndicators.getFirst().creditEquityRatio, is("3"));
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
        GetCreditEquityRatioResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityRatioResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is("400"));
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
        GetCreditEquityRatioResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityRatioResponseError.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.status, is("400"));
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
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio request by dateFrom (200)")
    @AllureId("448")
    public void getCreditEquityRatioTest5() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateFrom", dateFrom.replace(" ", "T"));
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradingAccount", mappedResponse.tradingAccount, is(client1.getTradingAccount()));
        assertThat("Assert tradingIndicators size", mappedResponse.tradingIndicators.size(), is(1));
        assertThat("Assert tradingIndicators currentEquity", mappedResponse.tradingIndicators.getFirst().currentEquity, is("1"));
        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.getFirst().indicatorDate, is(("2024-12-31 00:00:00").replace(" ", "T")));
        assertThat("Assert tradingIndicators sumCreditOrder", mappedResponse.tradingIndicators.getFirst().sumCreditOrder, is("2"));
        assertThat("Assert tradingIndicators creditEquityRatio", mappedResponse.tradingIndicators.getFirst().creditEquityRatio, is("3"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio request by dateTo (200)")
    @AllureId("449")
    public void getCreditEquityRatioTest6() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", dateTo.replace(" ", "T"));
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradingAccount", mappedResponse.tradingAccount, is(client1.getTradingAccount()));
        assertThat("Assert tradingIndicators size", mappedResponse.tradingIndicators.size(), is(1));
        assertThat("Assert tradingIndicators currentEquity", mappedResponse.tradingIndicators.getFirst().currentEquity, is("1"));
        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.getFirst().indicatorDate, is(("2024-12-31 00:00:00").replace(" ", "T")));
        assertThat("Assert tradingIndicators sumCreditOrder", mappedResponse.tradingIndicators.getFirst().sumCreditOrder, is("2"));
        assertThat("Assert tradingIndicators creditEquityRatio", mappedResponse.tradingIndicators.getFirst().creditEquityRatio, is("3"));
    }
}
