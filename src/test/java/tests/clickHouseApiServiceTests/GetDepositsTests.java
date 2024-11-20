package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getDeposits.GetDepositsResponse;
import businessObjects.api.clickhouseApiService.getDeposits.GetDepositsResponseError;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Muted;
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

import static businessObjects.api.clickhouseApiService.getDeposits.GetDepositsRequest.getDeposits;
import static businessObjects.db.clickhouse.crmTbDepositTable.CrmTbDepositObjectFactory.generateDepositByClient;
import static helpers.data.ClientFactory.getRandomClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_DEPOSITS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Disabled
@Muted
public class GetDepositsTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get deposits request by all params")
    @AllureId("")
    public void getDepositsTest10() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("orderBy", ""); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", ""); // Enum - asc, desc
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert depositItems size", mappedResponse.depositItems.size(), is(2));
        assertThat("Assert transferId ", mappedResponse.depositItems.getFirst().transferId, is(123_456));
        assertThat("Assert createTime ", mappedResponse.depositItems.getFirst().createTime, is("2024-11-05T12:34:56Z"));
        assertThat("Assert clientId ", mappedResponse.depositItems.getFirst().clientId, is("moneta-78910"));
        assertThat("Assert actualAmountUSD ", mappedResponse.depositItems.getFirst().actualAmountUsd, is(1500.75));
        assertThat("Assert actualAmount ", mappedResponse.depositItems.getFirst().actualAmount, is(1500.75));
        assertThat("Assert paymentChannel ", mappedResponse.depositItems.getFirst().paymentChannel, is("BankTransfer"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get deposits request by client Id (200)")
    @AllureId("212")
    public void getDepositsTest1() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get deposits request without required params (400)")
    @AllureId("")
    public void getDepositsTest2() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get deposits request by dateFrom")
    @AllureId("")
    public void getDepositsTest3() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateFrom", "");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get deposits request by dateTo")
    @AllureId("")
    public void getDepositsTest4() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateTo", "");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get deposits request by date range")
    @AllureId("")
    public void getDepositsTest5() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateTo", "");
        queryParams.put("dateFrom", "");
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get deposits request by orderBy=createTime")
    @AllureId("")
    public void getDepositsTest6() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "createTime"); // Enum - createTime, actualAmount, actualAmountUSD

        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get deposits request by orderBy=actualAmount")
    @AllureId("")
    public void getDepositsTest7() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmount"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get deposits request by orderBy=actualAmountUSD")
    @AllureId("")
    public void getDepositsTest8() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createTime, actualAmount, actualAmount
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get deposits request by limit")
    @AllureId("")
    public void getDepositsTest9() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", client.getUcid()); // Required
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("orderBy", ""); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", ""); // Enum - asc, desc
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get deposits request not found(404)")
    @AllureId("")
    public void getDepositsTest11() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(CRM_DEPOSIT_TABLE_NAME, generateDepositByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientId", 1); // Required
        Response response = getDeposits(queryParams);

        assert response.body() != null;
        GetDepositsResponseError
                mappedResponse = objectMapper.readValue(response.body().string(), GetDepositsResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
    }
}