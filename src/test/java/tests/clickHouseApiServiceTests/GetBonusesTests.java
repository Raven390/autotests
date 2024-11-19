package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getBonuses.GetBonusesResponse;
import businessObjects.api.clickhouseApiService.getBonuses.GetBonusesResponseError;
import businessObjects.db.crmTbBonusTable.CrmTbBonusObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getBonuses.GetBonusesRequest.getBonuses;
import static businessObjects.db.crmTbBonusTable.CrmTbBonusObjectFactory.generateBonusByClient;
import static helpers.data.ClientFactory.getRandomClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_BONUSES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Disabled
public class GetBonusesTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by all params")
    @AllureId("")
    public void getBonusesTest10() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomVantageClient();
        CrmTbBonusObject bonus1 = generateBonusByClient(client);
        CrmTbBonusObject bonus2 = generateBonusByClient(client);
        insertObjectToDb(CRM_BONUS_TABLE_NAME, bonus1);
        insertObjectToDb(CRM_BONUS_TABLE_NAME, bonus2);

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateFrom", "2024-10-19T13:14:15"); // date of bonus
        queryParams.put("dateTo", "2024-10-21T13:14:15"); // date of bonus
        //TODO partially string entry search
        queryParams.put("bonusType", "2"); // bonus type (e.g. ‘WelcomeBonus’)
        queryParams.put("orderBy", "createTime"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        queryParams.put("limit", "2"); // Limit the number of results returned
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert listSize", mappedResponse.bonusItem.size(), is(2));
        assertThat("Assert transferId", mappedResponse.bonusItem.getFirst().transferId, is(bonus1.transferId));
        assertThat("Assert createTime", mappedResponse.bonusItem.getFirst().createTime, is(bonus1.createTime));
        assertThat("Assert clientId", mappedResponse.bonusItem.getFirst().clientId, is(client.getUcid()));
        assertThat("Assert bonusType", mappedResponse.bonusItem.getFirst().bonusType, is(bonus1.type));
        assertThat("Assert actualAmountUSD", mappedResponse.bonusItem.getFirst().actualAmount, is(bonus1.amount));
        assertThat("Assert actualAmount", mappedResponse.bonusItem.getFirst().actualAmountUsd, is(bonus1.amountUsd));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by empty params")
    @AllureId("")
    public void getBonusesTest19() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateFrom", ""); // date of bonus
        queryParams.put("dateTo", ""); // date of bonus
        queryParams.put("bonusType", ""); // bonus type (e.g. ‘WelcomeBonus’)
        queryParams.put("orderBy", ""); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", ""); // Enum - asc, desc
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert listSize", mappedResponse.bonusItem.size(), is(2));
        assertThat("Assert transferId", mappedResponse.bonusItem.getFirst().transferId, is(1_000_001));
        assertThat("Assert createTime", mappedResponse.bonusItem.getFirst().createTime, is("2024-11-04T10:15:30"));
        assertThat("Assert clientId", mappedResponse.bonusItem.getFirst().clientId, is("UCID12345"));
        assertThat("Assert bonusType", mappedResponse.bonusItem.getFirst().bonusType, is("Welcome bonus"));
        assertThat("Assert actualAmountUSD", mappedResponse.bonusItem.getFirst().actualAmount, is("150.00"));
        assertThat("Assert actualAmount", mappedResponse.bonusItem.getFirst().actualAmountUsd, is("1230.00"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses only by clientId(200)")
    @AllureId("210")
    public void getBonusesTest1() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by clientId and limit")
    @AllureId("")
    public void getBonusesTest2() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by bonusType")
    @AllureId("")
    public void getBonusesTest3() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("bonusType", ""); // bonus type (e.g. ‘WelcomeBonus’)
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by orderBy=createTime")
    @AllureId("")
    public void getBonusesTest5() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "createTime"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки

    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by orderBy=actualAmount")
    @AllureId("")
    public void getBonusesTest6() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmount"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by orderBy=actualAmountUSD")
    @AllureId("")
    public void getBonusesTest7() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by dateFrom")
    @AllureId("")
    public void getBonusesTest8() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateFrom", ""); // date of bonus
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by dateTo")
    @AllureId("")
    public void getBonusesTest4() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateTo", ""); // date of bonus
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by date range")
    @AllureId("")
    public void getBonusesTest9() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateFrom", ""); // date of bonus
        queryParams.put("dateTo", ""); // date of bonus
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses without parameters - 400")
    @AllureId("")
    public void getBonusesTest11() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses not found(404)")
    @AllureId("")
    public void getBonusesTest12() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "a-1"); // Required
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses ordered by createdTime and sorted asc")
    @AllureId("")
    public void getBonusesTest13() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "createdTime"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses ordered by createdTime and sorted desc")
    @AllureId("")
    public void getBonusesTest14() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "createdTime"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses ordered by actualAmount and sorted asc")
    @AllureId("")
    public void getBonusesTest15() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmount"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses ordered by actualAmount and sorted desc")
    @AllureId("")
    public void getBonusesTest16() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmount"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses ordered by actualAmountUSD and sorted asc")
    @AllureId("")
    public void getBonusesTest17() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses ordered by actualAmountUSD and sorted desc")
    @AllureId("")
    public void getBonusesTest18() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getBonuses(queryParams);

        assert response.body() != null;
        GetBonusesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetBonusesResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by orderBy=createTime")
    @AllureId("")
    public void getBonusesTest20() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "123"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getBonuses(queryParams);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error text", response.message(), containsString("The property may include only: createTime, actualAmount, actualAmountUSD"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client bonuses by orderBy=createTime")
    @AllureId("")
    public void getBonusesTest21() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_BONUS_TABLE_NAME, generateBonusByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "createTime"); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "123"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getBonuses(queryParams);
        assertThat("Assert that code is 400", response.code(), is(400));
        assertThat("Assert error text", response.message(), containsString("The property may include only: asc, desc"));
    }

}
