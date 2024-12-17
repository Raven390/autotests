package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getDeposits.GetDepositsResponse;
import businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObject;
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

import static businessObjects.api.clickhouseApiService.getDeposits.GetDepositsRequest.getDeposits;
import static businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObjectFactory.generateDepositByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.getTomorrowTimestampDbFormat;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_DEPOSITS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetDepositsTests extends TestBaseApi {

    private static CrmTbDepositObject deposit1;
    private static CrmTbDepositObject deposit2;

    @BeforeAll
    public static void setupDeposits() throws ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomVantageClient();
        deposit1 = generateDepositByClient(client);
        deposit2 = generateDepositByClient(client);
        deposit2.createTime = getTomorrowTimestampDbFormat();
        deposit2.amountUsd = 3.0;
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit1);
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, deposit2);
    }

    @AfterAll
    public static void teardownDeposits() throws SQLException {
        deleteEntryFromDb(CRM_DEPOSIT_TABLE_NAME, String.format("ucid = '%s'", deposit1.ucid));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits by all params")
    @AllureId("390")
    public void getDepositsAllParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.ucid);
        queryParams.put("dateFrom", deposit1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", deposit2.createTime.replace(" ", "T"));
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert transferId", mappedResponse[0].transferId, is(deposit2.transferId));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(deposit2.createTime.replace(" ", "T")));
        assertThat("Assert clientId", mappedResponse[0].clientId, is(deposit2.ucid));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(deposit2.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].actualAmount, is(deposit2.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits by empty params")
    @AllureId("391")
    public void getDepositsEmptyParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.ucid);
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("bonusType", "");
        queryParams.put("orderBy", "");
        queryParams.put("sortOrder", "");
        queryParams.put("limit", "");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits only by clientId(200)")
    @AllureId("212")
    public void getDepositsClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.ucid);
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits by clientId and limit")
    @AllureId("392")
    public void getDepositsLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.ucid);
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "1");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(1));
        assertThat("Assert transferId", mappedResponse[0].transferId, is(deposit2.transferId));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(deposit2.createTime.replace(" ", "T")));
        assertThat("Assert clientId", mappedResponse[0].clientId, is(deposit2.ucid));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(deposit2.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].actualAmount, is(deposit2.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits order by create time default order")
    @AllureId("393")
    public void getDepositsDefaultSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.ucid);
        queryParams.put("orderBy", "createTime");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert transferId", mappedResponse[0].transferId, is(deposit1.transferId));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(deposit1.createTime.replace(" ", "T")));
        assertThat("Assert clientId", mappedResponse[0].clientId, is(deposit1.ucid));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(deposit1.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].actualAmount, is(deposit1.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits order by actualAmountUSD")
    @AllureId("394")
    public void getDepositsOrderByAmountUsdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.ucid);
        queryParams.put("orderBy", "actualAmountUSD");
        queryParams.put("sortOrder", "desc");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(deposit2.amountUsd));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits no params")
    @AllureId("395")
    public void getDepositsNoParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'clientId' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits no clientId")
    @AllureId("396")
    public void getDepositsNoClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("dateFrom", deposit1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", deposit2.createTime.replace(" ", "T"));
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'clientId' for method parameter type String is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits incorrect dateFrom")
    @AllureId("397")
    public void getDepositsIncorrectDateFromTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.ucid);
        queryParams.put("dateFrom", "test");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/deposits"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits incorrect dateTo")
    @AllureId("398")
    public void getDepositsIncorrectDateToTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.ucid);
        queryParams.put("dateTo", "test");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/deposits"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits incorrect orderBy")
    @AllureId("399")
    public void getDepositsIncorrectOrderByTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.ucid);
        queryParams.put("orderBy", "test");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid &quot;orderBy&quot; property format. The property may include only: createTime, actualAmount, actualAmountUSD"));
        assertThat("Assert status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits incorrect sortOrder")
    @AllureId("400")
    public void getDepositsIncorrectSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.ucid);
        queryParams.put("sortOrder", "test");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
        assertThat("Assert status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits incorrect limit")
    @AllureId("401")
    public void getDepositsIncorrectLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.ucid);
        queryParams.put("limit", "test");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/deposits"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }
}