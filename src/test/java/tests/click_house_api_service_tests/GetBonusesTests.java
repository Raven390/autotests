package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_bonuses.GetBonusesResponse;
import business_objects.db.clickhouse.crm_tb_bonus_table.CrmTbBonusObject;
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

import static business_objects.api.clickhouse_api_service.get_bonuses.GetBonusesRequest.getBonuses;
import static business_objects.db.clickhouse.crm_tb_bonus_table.CrmTbBonusObjectFactory.generateBonusByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.CleanTableHelper.cleanBonusesTableByClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;
import static utils.Utils.formatTimeToUtc;
import static utils.Utils.getTomorrowTimestampDbFormat;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_BONUSES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetBonusesTests extends TestBaseApi {

    private static CrmTbBonusObject bonus1;
    private static CrmTbBonusObject bonus2;

    @BeforeAll
    static void setup() {
        bonus1 = generateBonusByClient(getRandomVantageClient());
        bonus2 = generateBonusByClient(getRandomVantageClient());
        bonus2.createTime = getTomorrowTimestampDbFormat();
        bonus2.amountUsd = 3.0;
        insertObjectsToDb(CRM_BONUS_TABLE_NAME, List.of(bonus1, bonus2));
    }

    @AfterAll
    static void teardown() throws Exception {
        cleanBonusesTableByClient(bonus1.ucid, bonus2.ucid);
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses by all params")
    @AllureId("415")
    void getBonusesAllParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("dateFrom", bonus1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", bonus2.createTime.replace(" ", "T"));
        queryParams.put("bonusType", bonus1.typeRemark);
        queryParams.put("bonusGroup", bonus1.type);
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getBonuses(queryParams);
        assertThat(response.body(), is(notNullValue()));
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert transferId", mappedResponse[0].getTransferId(), is(bonus2.transferId));
        assertThat("Assert createTime", mappedResponse[0].getCreateTime(), is(formatTimeToUtc(bonus2.createTime)));
        assertThat("Assert clientId", mappedResponse[0].getClientId(), is(bonus2.ucid));
        assertThat("Assert bonusType", mappedResponse[0].getBonusType(), is(bonus2.typeRemark));
        assertThat("Assert bonusGroup", mappedResponse[0].getBonusGroup(), is(bonus2.type));
        assertThat("Assert actualAmountUSD", mappedResponse[0].getActualAmountUsd(), is(bonus2.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].getActualAmount(), is(bonus2.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses by empty params")
    @AllureId("416")
    void getBonusesEmptyParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("bonusType", "");
        queryParams.put("orderBy", "");
        queryParams.put("sortOrder", "");
        queryParams.put("limit", "");
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses only by clientId(200)")
    @AllureId("417")
    void getBonusesClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses by clientId and limit")
    @AllureId("418")
    void getBonusesLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("bonusType", bonus1.typeRemark);
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "1");
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(1));
        assertThat("Assert transferId", mappedResponse[0].getTransferId(), is(bonus2.transferId));
        assertThat("Assert createTime", mappedResponse[0].getCreateTime(), is(formatTimeToUtc(bonus2.createTime)));
        assertThat("Assert clientId", mappedResponse[0].getClientId(), is(bonus2.ucid));
        assertThat("Assert bonusType", mappedResponse[0].getBonusType(), is(bonus2.typeRemark));
        assertThat("Assert actualAmountUSD", mappedResponse[0].getActualAmountUsd(), is(bonus2.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].getActualAmount(), is(bonus2.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses order by create time default order")
    @AllureId("419")
    void getBonusesDefaultSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("bonusType", bonus1.typeRemark);
        queryParams.put("orderBy", "createTime");
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert transferId", mappedResponse[0].getTransferId(), is(bonus1.transferId));
        assertThat("Assert createTime", mappedResponse[0].getCreateTime(), is(formatTimeToUtc(bonus1.createTime)));
        assertThat("Assert clientId", mappedResponse[0].getClientId(), is(bonus1.ucid));
        assertThat("Assert bonusType", mappedResponse[0].getBonusType(), is(bonus1.typeRemark));
        assertThat("Assert actualAmountUSD", mappedResponse[0].getActualAmountUsd(), is(bonus1.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].getActualAmount(), is(bonus1.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses order by actualAmountUSD")
    @AllureId("420")
    void getBonusesOrderByAmountUsdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("orderBy", "actualAmountUSD");
        queryParams.put("sortOrder", "desc");
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert actualAmountUSD", mappedResponse[0].getActualAmountUsd(), is(bonus2.amountUsd));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses no params")
    @AllureId("421")
    void getBonusesNoParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Required request parameter 'clientIds' for method parameter type List is not present"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses no clientId")
    @AllureId("422")
    void getBonusesNoClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("dateFrom", bonus1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", bonus2.createTime.replace(" ", "T"));
        queryParams.put("bonusType", bonus1.typeRemark);
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.getError(), is("Required request parameter 'clientIds' for method parameter type List is not present"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses incorrect dateFrom")
    @AllureId("423")
    void getBonusesIncorrectDateFromTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("dateFrom", "test");
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/bonuses"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses incorrect dateTo")
    @AllureId("424")
    void getBonusesIncorrectDateToTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("dateTo", "test");
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/bonuses"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses incorrect orderBy")
    @AllureId("425")
    void getBonusesIncorrectOrderByTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("orderBy", "test");
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.getError(), is("Invalid &quot;orderBy&quot; property format. The property may include only: clientId, createTime, actualAmount, actualAmountUSD"));
        assertThat("Assert status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses incorrect sortOrder")
    @AllureId("426")
    void getBonusesIncorrectSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("sortOrder", "test");
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.getError(), is("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
        assertThat("Assert status", mappedResponse.getStatus(), is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses incorrect limit")
    @AllureId("427")
    void getBonusesIncorrectLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("limit", "test");
        Response response = getBonuses(queryParams);

        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.getTitle(), is("Bad Request"));
        assertThat("Assert detail", mappedResponse.getDetail(), is("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.getInstance(), is("/v1/bonuses"));
        assertThat("Assert error status", mappedResponse.getStatus(), is(400));
    }
}
