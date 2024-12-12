package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getClientTradingAccounts.GetClientTradingAccountsResponse;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static businessObjects.api.clickhouseApiService.getClientTradingAccounts.GetClientTradingAccountsRequest.getClientTradingAccounts;
import static businessObjects.db.clickhouse.mtTbUserTable.MtTbUserObjectFactory.generateMtTbUserData;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CLIENT_TRADING_ACCOUNTS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetClientTradingAccountTests extends TestBaseApi {

    private static final ClientHelper client = getRandomVantageClientAllFields();

    @BeforeAll
    public static void setupMirrorTrades() throws ReflectiveOperationException, SQLException {
        insertObjectToDb(MT_USER_TABLE_NAME, generateMtTbUserData(client.getUcid(), client.getTradingAccount(), client.getServerId()));
        insertObjectToDb(MT_USER_TABLE_NAME, generateMtTbUserData(client.getUcid(), client.getTradingAccount2(), client.getServerId()));
    }

    @AfterAll
    public static void teardownMirrorTrades() throws SQLException {
        deleteEntryFromDb(MT_USER_TABLE_NAME, String.format("account = '%s'", client.getTradingAccount()));
        deleteEntryFromDb(MT_USER_TABLE_NAME, String.format("account = '%s'", client.getTradingAccount2()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client trading accounts success (200)")
    @AllureId("450")
    public void getClientTradingAccountsTest1() throws IOException {
        // Execute request
        Response response = getClientTradingAccounts(client.getUcid());

        // Assert response
        assert response.body() != null;
        List<GetClientTradingAccountsResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetClientTradingAccountsResponse[].class)).toList();
        GetClientTradingAccountsResponse response1 = new GetClientTradingAccountsResponse(
                client.getTradingAccount().toString(), client.getServerId().toString()
        );
        GetClientTradingAccountsResponse response2 = new GetClientTradingAccountsResponse(
                client.getTradingAccount2().toString(), client.getServerId().toString()
        );
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check list size", mappedResponse.size(), is(2));
        assertThat("Check list size", mappedResponse, containsInAnyOrder(response1, response2));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client trading accounts not found (404)")
    @AllureId("458")
    public void getClientTradingAccountsTest2() throws IOException {
        // Execute request
        Response response = getClientTradingAccounts("vantage-1234");

        // Assert response
        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Check response code", response.code(), is(404));
        assertThat("Check response code", mappedResponse.status, is(404));
        assertThat("Check response code", mappedResponse.error, is("Client with clientId=vantage-1234 not found."));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client trading accounts wrong ucid (400)")
    @AllureId("459")
    public void getClientTradingAccountsTest3() throws IOException {
        // Execute request
        Response response = getClientTradingAccounts("1");

        // Assert response
        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response code", mappedResponse.status, is(400));
        assertThat("Check response code", mappedResponse.error, is("Invalid clientId format: clientId must contain brand and userId divided by a dash e.g., vantage-2068746030"));
    }
}
