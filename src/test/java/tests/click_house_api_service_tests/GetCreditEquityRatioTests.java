package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_credit_equity_ratio.GetCreditEquityResponse;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
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

import static business_objects.api.clickhouse_api_service.get_credit_equity_ratio.GetCreditEquityRequest.getCreditEquity;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CREDIT_EQUITY)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetCreditEquityRatioTests extends TestBaseApi {

    static final String DATE_TIME = TIME_2024_12_31_00_00_00.replace(" ", "T");

    static MtAccountObject data1;
    static final ClientHelper client1 = getRandomVantageClient();

    @BeforeAll
    static void setupData() {
        data1 = generateMtAccountByClient(client1);
        data1.equityUsd = 1d;//currentEquity
        data1.creditUsd = 2d;//sumCreditOrder
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, data1);
    }

    @AfterAll
    static void teardownData() {
        deleteEntryFromDb(MT_ACCOUNT_TABLE_NAME, String.format("account = '%s'", data1.account));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio by dateTo (200)")
    @AllureId("216")
    void getCreditEquityRatioTest1() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", DATE_TIME);
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        GetCreditEquityResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditEquityResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradingAccount", mappedResponse.tradingAccount, is(client1.getTradingAccount()));
        assertThat("Assert tradingIndicators size", mappedResponse.tradingIndicators.size(), is(3));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.getFirst().indicatorDate, is("2024-12-31T00:00"));
        assertThat("Assert tradingIndicators currentEquity", mappedResponse.tradingIndicators.getFirst().currentEquity, is("0"));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.get(1).indicatorDate, is("2024-12-31T00:00"));
        assertThat("Assert tradingIndicators sumCreditOrder", mappedResponse.tradingIndicators.get(1).sumCreditOrder, is("0"));

        assertThat("Assert tradingIndicators indicatorDate", mappedResponse.tradingIndicators.get(2).indicatorDate, is("2024-12-31T00:00"));
        assertThat("Assert tradingIndicators creditEquityRatio", mappedResponse.tradingIndicators.get(2).creditEquityRatio, is("0"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio request only by tradingAccount (400)")
    @AllureId("445")
    void getCreditEquityRatioTest2() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.getStatus(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Required request parameter 'serverId' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio request only by serverId (400)")
    @AllureId("446")
    void getCreditEquityRatioTest3() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", client1.getServerId()); // Required
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert that code is 400", mappedResponse.getStatus(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credit equity ratio request without date parameters (200)")
    @AllureId("447")
    void getCreditEquityRatioTest4() throws IOException {
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
    @DisplayName("Clickhouse Api. Get credit equity ratio with wrong date format(200)")
    @AllureId("565")
    void getCreditEquityRatioTest6() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", "1");
        Response response = getCreditEquity(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert status", mappedResponse.getStatus(), is(400));
        assertThat("Assert type", mappedResponse.getType(), is("about:blank"));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateTo' with value: '1'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/creditEquityRatio"));
    }
}
