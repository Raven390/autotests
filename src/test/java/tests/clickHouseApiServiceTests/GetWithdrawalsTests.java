package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getWithdrawals.GetWithdrawalsResponse;
import businessObjects.api.clickhouseApiService.getWithdrawals.GetWithdrawalsResponseError;
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

import static businessObjects.api.clickhouseApiService.getWithdrawals.GetWithdrawalsRequest.getWithdrawals;
import static businessObjects.db.crmTbWithdrawalTable.CrmTbWithdrawalObjectFactory.generateWithdrawalByClient;
import static helpers.data.ClientFactory.getRandomClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_WITHDRAWALS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Disabled
public class GetWithdrawalsTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request by all params (200)")
    @AllureId("")
    public void getWithdrawalsTest10() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("orderBy", ""); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", ""); // Enum - asc, desc
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request by clientId (200)")
    @AllureId("215")
    public void getWithdrawalsTest1() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request (400)")
    @AllureId("")
    public void getWithdrawalsTest2() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request by dateFrom (200)")
    @AllureId("")
    public void getWithdrawalsTest3() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateFrom", "");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request by dateTo (200)")
    @AllureId("")
    public void getWithdrawalsTest4() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("orderBy", ""); // Enum - bonusDate, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", ""); // Enum - asc, desc
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request by date range(200)")
    @AllureId("")
    public void getWithdrawalsTest5() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request orderBy=createdTime(200)")
    @AllureId("")
    public void getWithdrawalsTest6() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "createdTime"); // Enum - createdTime, actualAmount, actualAmountUSD
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request orderBy=actualAmount(200)")
    @AllureId("")
    public void getWithdrawalsTest7() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmount"); // Enum - createdTime, actualAmount, actualAmountUSD
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request orderBy=actualAmountUSD(200)")
    @AllureId("")
    public void getWithdrawalsTest8() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createdTime, actualAmount, actualAmountUSD
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request by limit(200)")
    @AllureId("")
    public void getWithdrawalsTest9() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request not found(404)")
    @AllureId("")
    public void getWithdrawalsTest11() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", "1"); // Required

        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponseError
                mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request ordered by createdTime and sorted asc")
    @AllureId("")
    public void getWithdrawalsTest12() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "createdTime"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request ordered by createdTime and sorted desc")
    @AllureId("")
    public void getWithdrawalsTest13() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "createdTime"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request ordered by actualAmount and sorted asc")
    @AllureId("")
    public void getWithdrawalsTest14() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmount"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert list size", mappedResponse.withdrawalsItem.size(), is(2));
        assertThat("Assert list size", mappedResponse.withdrawalsItem.getFirst().transferId, is(2));
        assertThat("Assert list size", mappedResponse.withdrawalsItem.getFirst().createTime, is("2024-11-05T12:34:56Z"));
        assertThat("Assert list size", mappedResponse.withdrawalsItem.getFirst().clientId, is(78_910));
        assertThat("Assert list size", mappedResponse.withdrawalsItem.getFirst().actualAmountUsd, is(1500.75));
        assertThat("Assert list size", mappedResponse.withdrawalsItem.getFirst().actualAmount, is(1500.75));
        assertThat("Assert list size", mappedResponse.withdrawalsItem.getFirst().paymentChannel, is("BankTransfer"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request ordered by actualAmount and sorted desc")
    @AllureId("")
    public void getWithdrawalsTest15() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmount"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request ordered by actualAmountUSD and sorted asc")
    @AllureId("")
    public void getWithdrawalsTest16() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "asc"); // Enum - asc, desc
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get Withdrawals request ordered by actualAmountUSD and sorted desc")
    @AllureId("")
    public void getWithdrawalsTest17() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));
        insertObjectToDb(CRM_WITHDRAWAL_TABLE_NAME, generateWithdrawalByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createdTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", "desc"); // Enum - asc, desc
        Response response = getWithdrawals(queryParams);

        assert response.body() != null;
        GetWithdrawalsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetWithdrawalsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }
}
