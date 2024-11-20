package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getCredits.GetCreditsResponse;
import businessObjects.api.clickhouseApiService.getCredits.GetCreditsResponseError;
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

import static businessObjects.api.clickhouseApiService.getCredits.GetCreditsRequest.getCredits;
import static businessObjects.db.clickhouse.mtTbCreditsTable.MtTbCreditsObjectFactory.generateCreditsByClient;
import static helpers.data.ClientFactory.getRandomClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CREDITS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
@Disabled
@Muted
public class GetCreditsTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get credits request by all params (200)")
    @AllureId("")
    public void getCreditsTest10() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        queryParams.put("orderBy", ""); // Enum - createTime, actualAmount, actualAmountUSD
        queryParams.put("sortOrder", ""); // Enum - asc, desc
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert tradingAccount", mappedResponse.creditItem.size(), is(2));
        assertThat("Assert openTime", mappedResponse.creditItem.getFirst(), is("2024-11-04T10:15:30"));
        assertThat("Assert tradeId", mappedResponse.creditItem.getFirst(), is(1_000_001));
        assertThat("Assert tradingAccount", mappedResponse.creditItem.getFirst(), is(2_001_001));
        assertThat("Assert profitUSD", mappedResponse.creditItem.getFirst(), is("150.12345"));
        assertThat("Assert profit", mappedResponse.creditItem.getFirst(), is("150.12345"));
        assertThat("Assert comment", mappedResponse.creditItem.getFirst(), is("Credit for promotional offer"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits request by tradingAccount and serverId(200)")
    @AllureId("211")
    public void getCreditsTest1() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits request no params(400)")
    @AllureId("")
    public void getCreditsTest2() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits request by dateFrom(200)")
    @AllureId("")
    public void getCreditsTest3() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits request by dateTo(200)")
    @AllureId("")
    public void getCreditsTest4() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("dateTo", "");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits request by date range(200)")
    @AllureId("")
    public void getCreditsTest5() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("dateFrom", "");
        queryParams.put("dateTo", "");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits request by orderBy=createTime(200)")
    @AllureId("")
    public void getCreditsTest6() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "createTime"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits request by orderBy=actualAmount(200)")
    @AllureId("")
    public void getCreditsTest7() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmount"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits request by orderBy=actualAmountUSD(200)")
    @AllureId("")
    public void getCreditsTest8() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("orderBy", "actualAmountUSD"); // Enum - createTime, actualAmount, actualAmountUSD
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        // TODO проверить сортировку если не отправлен параметр сортировки
    }


    @Test
    @DisplayName("Clickhouse Api. Get credits request by limit (200)")
    @AllureId("")
    public void getCreditsTest9() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", "");
        queryParams.put("limit", ""); // Limit the number of results returned
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponse mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
    }

    @Test
    @DisplayName("Clickhouse Api. Get credits request not found(404)")
    @AllureId("")
    public void getCreditsTest11() throws IOException, ReflectiveOperationException, SQLException {
        ClientHelper client = getRandomClient();
        insertObjectToDb(MT_CREDITS_TABLE_NAME, generateCreditsByClient(client));

        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "1");
        queryParams.put("serverId", "1");
        Response response = getCredits(queryParams);

        assert response.body() != null;
        GetCreditsResponseError mappedResponse = objectMapper.readValue(response.body().string(), GetCreditsResponseError.class);
        assertThat("Assert that code is 404", response.code(), is(404));
    }

}
