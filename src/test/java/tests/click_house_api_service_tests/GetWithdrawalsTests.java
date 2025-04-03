package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_withdrawals.GetWithdrawalsResponse;
import business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static business_objects.api.clickhouse_api_service.get_withdrawals.GetWithdrawalsRequest.getWithdrawals;
import static business_objects.db.clickhouse.crm_tb_withdrawal.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.CleanTableHelper.cleanWithdrawalsTableByUcid;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.formatTimeToUtc;
import static utils.Utils.getTomorrowTimestampDbFormat;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_WITHDRAWALS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetWithdrawalsTests extends TestBaseApi {

    private static CrmTbWithdrawalObject withdrawal1;
    private static CrmTbWithdrawalObject withdrawal2;

    @BeforeAll
    static void setupWithdrawals() {
        ClientHelper client = getRandomVantageClient();
        withdrawal1 = generateWithdrawalByClient(client);
        withdrawal2 = generateWithdrawalByClient(client);
        withdrawal2.createTime = getTomorrowTimestampDbFormat();
        withdrawal2.amountUsd = 3.0;
        insertObjectsToDb(CRM_WITHDRAWAL_TABLE_NAME, List.of(withdrawal1, withdrawal2));
    }

    @AfterAll
    static void teardownWithdrawals() throws Exception {
        cleanWithdrawalsTableByUcid(withdrawal1.ucid, withdrawal2.ucid);
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals by all params")
    @AllureId("378")
    void getWithdrawalsAllParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", withdrawal1.ucid);
        queryParams.put("dateFrom", withdrawal1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", withdrawal2.createTime.replace(" ", "T"));
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert transferId", mappedResponse[0].transferId, is(withdrawal2.transferId));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(formatTimeToUtc(withdrawal2.createTime)));
        assertThat("Assert clientId", mappedResponse[0].clientId, is(withdrawal2.ucid));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(withdrawal2.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].actualAmount, is(withdrawal2.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals by empty params")
    @AllureId("379")
    void getWithdrawalsEmptyParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", withdrawal1.ucid);
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("bonusType", "");
        queryParams.put("orderBy", "");
        queryParams.put("sortOrder", "");
        queryParams.put("limit", "");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals only by clientId(200)")
    @AllureId("215")
    void getWithdrawalsClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", withdrawal1.ucid);
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals by clientId and limit")
    @AllureId("380")
    void getWithdrawalsLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", withdrawal1.ucid);
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "1");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(1));
        assertThat("Assert transferId", mappedResponse[0].transferId, is(withdrawal2.transferId));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(formatTimeToUtc(withdrawal2.createTime)));
        assertThat("Assert clientId", mappedResponse[0].clientId, is(withdrawal2.ucid));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(withdrawal2.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].actualAmount, is(withdrawal2.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals order by create time default order")
    @AllureId("381")
    void getWithdrawalsDefaultSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", withdrawal1.ucid);
        queryParams.put("orderBy", "createTime");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert transferId", mappedResponse[0].transferId, is(withdrawal1.transferId));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(formatTimeToUtc(withdrawal1.createTime)));
        assertThat("Assert clientId", mappedResponse[0].clientId, is(withdrawal1.ucid));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(withdrawal1.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].actualAmount, is(withdrawal1.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals order by actualAmountUSD")
    @AllureId("382")
    void getWithdrawalsOrderByAmountUsdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", withdrawal1.ucid);
        queryParams.put("orderBy", "actualAmountUSD");
        queryParams.put("sortOrder", "desc");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(withdrawal1.amountUsd));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals no params")
    @AllureId("383")
    void getWithdrawalsNoParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Required request parameter 'clientId' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals no clientId")
    @AllureId("384")
    void getWithdrawalsNoClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("dateFrom", withdrawal1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", withdrawal2.createTime.replace(" ", "T"));
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Required request parameter 'clientId' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals incorrect dateFrom")
    @AllureId("385")
    void getWithdrawalsIncorrectDateFromTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", withdrawal1.ucid);
        queryParams.put("dateFrom", "test");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/withdrawals"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals incorrect dateTo")
    @AllureId("386")
    void getWithdrawalsIncorrectDateToTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", withdrawal1.ucid);
        queryParams.put("dateTo", "test");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/withdrawals"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals incorrect orderBy")
    @AllureId("387")
    void getWithdrawalsIncorrectOrderByTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", withdrawal1.ucid);
        queryParams.put("orderBy", "test");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.getError(), is("Invalid &quot;orderBy&quot; property format. The property may include only: createTime, actualAmount, actualAmountUSD"));
        assertThat("Assert status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals incorrect sortOrder")
    @AllureId("388")
    void getWithdrawalsIncorrectSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", withdrawal1.ucid);
        queryParams.put("sortOrder", "test");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.getError(), is("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
        assertThat("Assert status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals incorrect limit")
    @AllureId("389")
    void getWithdrawalsIncorrectLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", withdrawal1.ucid);
        queryParams.put("limit", "test");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/withdrawals"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }
}