package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getBonuses.GetBonusesResponse;
import businessObjects.db.clickhouse.crmTbBonusTable.CrmTbBonusObject;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getBonuses.GetBonusesRequest.getBonuses;
import static businessObjects.db.clickhouse.crmTbBonusTable.CrmTbBonusObjectFactory.generateBonusByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.getTomorrowTimestampDbFormat;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_BONUSES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetBonusesTests extends TestBaseApi {

    private static CrmTbBonusObject bonus1;
    private static CrmTbBonusObject bonus2;

    @BeforeAll
    public static void setupBonuses() throws ReflectiveOperationException, SQLException {
        bonus1 = generateBonusByClient(getRandomVantageClient());
        bonus2 = generateBonusByClient(getRandomVantageClient());
        bonus2.createTime = getTomorrowTimestampDbFormat();
        bonus2.amountUsd = 3.0;
        insertObjectToDb(CRM_BONUS_TABLE_NAME, bonus1);
        insertObjectToDb(CRM_BONUS_TABLE_NAME, bonus2);
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses by all params")
    @AllureId("415")
    public void getBonusesAllParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("dateFrom", bonus1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", bonus2.createTime.replace(" ", "T"));
        queryParams.put("bonusType", bonus1.typeRemark);
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert transferId", mappedResponse[0].transferId, is(bonus2.transferId));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(bonus2.createTime.replace(" ", "T")));
        assertThat("Assert clientId", mappedResponse[0].clientId, is(bonus2.ucid));
        assertThat("Assert bonusType", mappedResponse[0].bonusType, is(bonus2.typeRemark));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(bonus2.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].actualAmount, is(bonus2.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses by empty params")
    @AllureId("416")
    public void getBonusesEmptyParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("bonusType", "");
        queryParams.put("orderBy", "");
        queryParams.put("sortOrder", "");
        queryParams.put("limit", "");
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses only by clientId(200)")
    @AllureId("417")
    public void getBonusesClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses by clientId and limit")
    @AllureId("418")
    public void getBonusesLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("bonusType", bonus1.typeRemark);
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "1");
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(1));
        assertThat("Assert transferId", mappedResponse[0].transferId, is(bonus2.transferId));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(bonus2.createTime.replace(" ", "T")));
        assertThat("Assert clientId", mappedResponse[0].clientId, is(bonus2.ucid));
        assertThat("Assert bonusType", mappedResponse[0].bonusType, is(bonus2.typeRemark));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(bonus2.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].actualAmount, is(bonus2.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses order by create time default order")
    @AllureId("419")
    public void getBonusesDefaultSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("bonusType", bonus1.typeRemark);
        queryParams.put("orderBy", "createTime");
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert transferId", mappedResponse[0].transferId, is(bonus1.transferId));
        assertThat("Assert createTime", mappedResponse[0].createTime, is(bonus1.createTime.replace(" ", "T")));
        assertThat("Assert clientId", mappedResponse[0].clientId, is(bonus1.ucid));
        assertThat("Assert bonusType", mappedResponse[0].bonusType, is(bonus1.typeRemark));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(bonus1.amountUsd));
        assertThat("Assert actualAmount", mappedResponse[0].actualAmount, is(bonus1.amount));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses order by actualAmountUSD")
    @AllureId("420")
    public void getBonusesOrderByAmountUsdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("orderBy", "actualAmountUSD");
        queryParams.put("sortOrder", "desc");
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.length, is(2));
        assertThat("Assert actualAmountUSD", mappedResponse[0].actualAmountUsd, is(bonus2.amountUsd));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses no params")
    @AllureId("421")
    public void getBonusesNoParamsTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'clientIds' for method parameter type List is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses no clientId")
    @AllureId("422")
    public void getBonusesNoClientIdTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("dateFrom", bonus1.createTime.replace(" ", "T"));
        queryParams.put("dateTo", bonus2.createTime.replace(" ", "T"));
        queryParams.put("bonusType", bonus1.typeRemark);
        queryParams.put("orderBy", "createTime");
        queryParams.put("sortOrder", "desc");
        queryParams.put("limit", "2");
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error message", mappedResponse.error, is("Required request parameter 'clientIds' for method parameter type List is not present"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses incorrect dateFrom")
    @AllureId("423")
    public void getBonusesIncorrectDateFromTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("dateFrom", "test");
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateFrom' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/bonuses"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses incorrect dateTo")
    @AllureId("424")
    public void getBonusesIncorrectDateToTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("dateTo", "test");
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateTo' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/bonuses"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses incorrect orderBy")
    @AllureId("425")
    public void getBonusesIncorrectOrderByTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("orderBy", "test");
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid &quot;orderBy&quot; property format. The property may include only: clientId, createTime, actualAmount, actualAmountUSD"));
        assertThat("Assert status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses incorrect sortOrder")
    @AllureId("426")
    public void getBonusesIncorrectSortOrderTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("sortOrder", "test");
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid &quot;sortOrder&quot; property format. The property may include only: asc, desc"));
        assertThat("Assert status", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get bonuses incorrect limit")
    @AllureId("427")
    public void getBonusesIncorrectLimitTest() throws IOException {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(bonus1.ucid, bonus2.ucid));
        queryParams.put("limit", "test");
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'limit' with value: 'test'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/bonuses"));
        assertThat("Assert error status", mappedResponse.status, is(400));
    }

    @AfterAll
    public static void teardownBonuses() throws SQLException {
        deleteEntryFromDb(CRM_BONUS_TABLE_NAME, String.format("ucid = '%s'", bonus1.ucid));
    }
}
