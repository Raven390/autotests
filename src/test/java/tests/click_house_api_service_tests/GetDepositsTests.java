package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_deposits.GetDepositsRequest.getDeposits;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.CleanTableHelper.cleanDepositsTableByUcid;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getRandomIntPositive;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_deposits.GetDepositsResponse;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntity;
import business_objects.db.clickhouse.crm_tb_deposit_table.CrmTbDepositEntityFactory;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_DEPOSITS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetDepositsTests extends TestBaseApi {

    private static CrmTbDepositEntity deposit1;
    private static CrmTbDepositEntity deposit2;
    private static ClientHelper client;

    @BeforeAll
    static void setup() {
        client = getRandomVantageClient();
        deposit1 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit1.setPaymentProfileKey(getRandomIntPositive().toString());
        deposit2 = CrmTbDepositEntityFactory.generateCrmTbDepositEntityByClient(client);
        deposit2.setCreateTime(OffsetDateTime.now().plusDays(1));
        deposit2.setAmountUsd(BigDecimal.valueOf(3.0));
        deposit2.setPaymentChannelId(getRandomIntPositive());
        deposit2.setPaymentProfileKey(getRandomIntPositive().toString());
        insertObjectsToDb(CRM_DEPOSIT_TABLE_NAME, List.of(deposit1, deposit2));
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanDepositsTableByUcid(deposit1.getUcid(), deposit2.getUcid());
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits by all params")
    @AllureId("390")
    void getDepositsAllParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.getUcid());
        queryParams.put("dateFrom", deposit1.getCreateTime().format(DateTimeFormatter.ISO_DATE_TIME));
        queryParams.put("dateTo", deposit2.getCreateTime().format(DateTimeFormatter.ISO_DATE_TIME));
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getDeposits(queryParams);

        GetDepositsResponse[] mappedResponse = verifySuccessResponse(response, 2);
        verifyDeposit(mappedResponse[0], deposit2);
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits by empty params")
    @AllureId("391")
    void getDepositsEmptyParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.getUcid());
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("bonusType", "");
        queryParams.put("orderBy", "");
        queryParams.put("sortOrder", "");
        queryParams.put("limit", "");
        Response response = getDeposits(queryParams);

        verifySuccessResponse(response, 2);
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits only by clientId(200)")
    @AllureId("212")
    void getDepositsClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.getUcid());
        Response response = getDeposits(queryParams);

        verifySuccessResponse(response, 2);
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits by clientId and limit")
    @AllureId("392")
    void getDepositsLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.getUcid());
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "1");
        Response response = getDeposits(queryParams);

        GetDepositsResponse[] mappedResponse = verifySuccessResponse(response, 1);
        verifyDeposit(mappedResponse[0], deposit2);
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits order by create time default order")
    @AllureId("393")
    void getDepositsDefaultSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.getUcid());
        queryParams.put("orderBy", "createTime");
        Response response = getDeposits(queryParams);

        GetDepositsResponse[] mappedResponse = verifySuccessResponse(response, 2);
        verifyDeposit(mappedResponse[0], deposit1);
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits order by actualAmountUSD")
    @AllureId("394")
    void getDepositsOrderByAmountUsdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.getUcid());
        queryParams.put("orderBy", "actualAmountUSD");
        queryParams.put("sortOrder", "desc");
        Response response = getDeposits(queryParams);

        GetDepositsResponse[] mappedResponse = verifySuccessResponse(response, 2);
        assertThat(
                "Assert actualAmountUSD",
                BigDecimal.valueOf(mappedResponse[0].actualAmountUsd),
                comparesEqualTo(deposit1.getAmountUsd()));
    }

    @Test
    @AllureId("1894")
    @DisplayName("Clickhouse Api. Get Deposits order by paymentProfileKey")
    void getDepositsOrderByPaymentProfileKeyTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid());
        queryParams.put("paymentProfileKey", deposit1.getPaymentProfileKey());
        Response response = getDeposits(queryParams);

        GetDepositsResponse[] mappedResponse = verifySuccessResponse(response, 1);
        assertThat(
                "Assert actualAmountUSD",
                BigDecimal.valueOf(mappedResponse[0].actualAmountUsd),
                comparesEqualTo(deposit1.getAmountUsd()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits no params")
    @AllureId("395")
    void getDepositsNoParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        Response response = getDeposits(queryParams);

        verifyErrorResponse(
                response, 400, "Required request parameter 'clientId' for method parameter type String is not present");
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits no clientId")
    @AllureId("396")
    void getDepositsNoClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("dateFrom", deposit1.getCreateTime().format(DateTimeFormatter.ISO_DATE_TIME));
        queryParams.put("dateTo", deposit2.getCreateTime().format(DateTimeFormatter.ISO_DATE_TIME));
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getDeposits(queryParams);

        verifyErrorResponse(
                response, 400, "Required request parameter 'clientId' for method parameter type String is not present");
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits incorrect dateFrom")
    @AllureId("397")
    void getDepositsIncorrectDateFromTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.getUcid());
        queryParams.put("dateFrom", "test");
        Response response = getDeposits(queryParams);

        ClickhouseApiErrorResponse mappedResponse = verifyErrorResponse(response, 400, null);
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateFrom' with value: 'test'"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits incorrect dateTo")
    @AllureId("398")
    void getDepositsIncorrectDateToTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.getUcid());
        queryParams.put("dateTo", "test");
        Response response = getDeposits(queryParams);

        ClickhouseApiErrorResponse mappedResponse = verifyErrorResponse(response, 400, null);
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateTo' with value: 'test'"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits incorrect orderBy")
    @AllureId("399")
    void getDepositsIncorrectOrderByTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.getUcid());
        queryParams.put("orderBy", "test");
        Response response = getDeposits(queryParams);

        verifyErrorResponse(
                response,
                400,
                "Invalid &quot;orderBy&quot; property format. The property may include only: createTime, actualAmount, actualAmountUSD");
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits incorrect sortOrder")
    @AllureId("400")
    void getDepositsIncorrectSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.getUcid());
        queryParams.put("sortOrder", "test");
        Response response = getDeposits(queryParams);

        verifyErrorResponse(
                response,
                400,
                "Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc");
    }

    @Test
    @DisplayName("Clickhouse Api. Get Deposits incorrect limit")
    @AllureId("401")
    void getDepositsIncorrectLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", deposit1.getUcid());
        queryParams.put("limit", "test");
        Response response = getDeposits(queryParams);

        ClickhouseApiErrorResponse mappedResponse = verifyErrorResponse(response, 400, null);
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'limit' with value: 'test'"));
    }

    private GetDepositsResponse[] verifySuccessResponse(Response response, int expectedLength) throws IOException {
        assertThat(response.body(), is(notNullValue()));
        assertThat("Assert that code is 200", response.code(), is(200));
        GetDepositsResponse[] mappedResponse =
                objectMapper.readValue(response.body().string(), GetDepositsResponse[].class);
        assertThat("Assert response length", mappedResponse.length, is(expectedLength));
        return mappedResponse;
    }

    private ClickhouseApiErrorResponse verifyErrorResponse(
            Response response, int expectedCode, String expectedErrorMessage) throws IOException {
        assertThat(response.body(), is(notNullValue()));
        assertThat("Assert that code is " + expectedCode, response.code(), is(expectedCode));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        if (expectedErrorMessage != null) {
            assertThat("Assert error message", mappedResponse.getError(), is(expectedErrorMessage));
        }
        assertThat("Assert error status", mappedResponse.getStatus(), is(expectedCode));
        return mappedResponse;
    }

    private void verifyDeposit(GetDepositsResponse actual, CrmTbDepositEntity expected) {
        assertThat(
                "Assert tradingAccount",
                actual.tradingAccount,
                is(client.getTradingAccount().toString()));
        assertThat(
                "Assert transferId",
                actual.transferId,
                is(expected.getTransferId().toString()));
        assertThat(
                "Assert createTime",
                OffsetDateTime.parse(actual.createTime).toInstant(),
                is(expected.getCreateTime()
                        .atZoneSameInstant(ZoneOffset.UTC)
                        .toInstant()
                        .truncatedTo(ChronoUnit.MILLIS)));
        assertThat("Assert clientId", actual.clientId, is(expected.getUcid()));
        assertThat(
                "Assert actualAmountUSD",
                BigDecimal.valueOf(actual.actualAmountUsd),
                comparesEqualTo(expected.getAmountUsd()));
        assertThat(
                "Assert actualAmount", BigDecimal.valueOf(actual.actualAmount), comparesEqualTo(expected.getAmount()));
    }
}
