package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_client_trading_accounts.GetClientTradingAccountsResponse;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static business_objects.api.clickhouse_api_service.get_client_trading_accounts.GetClientTradingAccountsRequest.getClientTradingAccounts;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CLIENT_TRADING_ACCOUNTS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetClientTradingAccountTests extends TestBaseApi {

    private static final ClientHelper client = getRandomVantageClientAllFields();

    @BeforeAll
    static void setup() {
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(generateCrmTbAccountData(client), generateAdditionalCrmTbAccountData(client)));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client trading accounts success (200)")
    @AllureId("450")
    void getClientTradingAccountsTest1() throws IOException {
        // Execute request
        Response response = getClientTradingAccounts(client.getUcid());

        // Assert response
        assertThat(response.body(), is(notNullValue()));
        List<GetClientTradingAccountsResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetClientTradingAccountsResponse[].class)).toList();
        GetClientTradingAccountsResponse response1 = new GetClientTradingAccountsResponse(
                "clientAccounts", client.getUcid(), client.getTradingAccount().toString(), client.getServerId().toString(), "USD"
        );
        GetClientTradingAccountsResponse response2 = new GetClientTradingAccountsResponse(
                "clientAccounts", client.getUcid(), client.getTradingAccount2().toString(), client.getServerId().toString(), "USD"
        );
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check list size", mappedResponse.size(), is(2));
        assertThat("Check list data", mappedResponse, containsInAnyOrder(response1, response2));
    }

    @Disabled("Covered in clickhouse-api repoe")
    @Test
    @DisplayName("Clickhouse Api. Get client trading accounts wrong ucid (400)")
    @AllureId("459")
    void getClientTradingAccountsTest2() throws IOException {
        // Execute request
        Response response = getClientTradingAccounts("1");

        // Assert response
        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response code", mappedResponse.getStatus(), is(400));
        assertThat("Check response code", mappedResponse.getError(), is("Invalid clientId format: clientId must contain brand and userId divided by a dash e.g., vantage-2068746030"));
    }
}
