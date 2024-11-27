package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.getClients.GetClientsResponse;
import businessObjects.api.clickhouseApiService.getClients.GetClientsResponseError;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.mtTbUserTable.MtTbUserObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getClients.GetClientsRequest.getClientsIdByTradingAccountServerId;
import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.mtTbUserTable.MtTbUserObjectFactory.generateMtTbUserData;
import static helpers.data.ClientFactory.getRandomClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CLIENTS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetClientsTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get client by trading account & server ID")
    @AllureId("200")
    public void getClientSuccessTest() throws IOException, ReflectiveOperationException, SQLException {
        // Create an instance of ClientHelper
        ClientHelper client = getRandomClient();

        // Insert in crm user table
        CrmTbUserObject crmObject = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmObject);
        // Insert object in mt user table
        MtTbUserObject mtObject = generateMtTbUserData(client.getUcid(),client.getTradingAccount(), client.getServerId());
        insertObjectToDb(MT_USER_TABLE_NAME, mtObject);

        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        Response response = getClientsIdByTradingAccountServerId(queryParams);

        GetClientsResponse clients = objectMapper.readValue(response.body().string(), GetClientsResponse.class);

        // Assert response
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check response code", clients.clientId, is(client.getUcid()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client by trading account & server ID=null (400 error)")
    @AllureId("201")
    public void getClientSuccessTest2() throws IOException, ReflectiveOperationException, SQLException {
        // Create an instance of ClientHelper
        ClientHelper client = getRandomClient();

        // Insert in crm user table
        CrmTbUserObject crmObject = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmObject);
        // Insert object in mt user table
        MtTbUserObject mtObject = generateMtTbUserData(client.getUcid(),client.getTradingAccount(), client.getServerId());
        insertObjectToDb(MT_USER_TABLE_NAME, mtObject);

        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        Response response = getClientsIdByTradingAccountServerId(queryParams);
        GetClientsResponseError error = objectMapper.readValue(response.body().string(), GetClientsResponseError.class);

        // Assert response
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response code", error.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
        assertThat("Check response code", error.status, is("400"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client by trading account=null & server ID (400 error)")
    @AllureId("202")
    public void getClientSuccessTest3() throws IOException, ReflectiveOperationException, SQLException {
        // Create an instance of ClientHelper
        ClientHelper client = getRandomClient();

        // Insert in crm user table
        CrmTbUserObject crmObject = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmObject);
        // Insert object in mt user table
        MtTbUserObject mtObject = generateMtTbUserData(client.getUcid(),client.getTradingAccount(), client.getServerId());
        insertObjectToDb(MT_USER_TABLE_NAME, mtObject);

        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", client.getServerId());
        Response response = getClientsIdByTradingAccountServerId(queryParams);
        GetClientsResponseError error = objectMapper.readValue(response.body().string(), GetClientsResponseError.class);
        System.out.println(response);

        // Assert response
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response code", error.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Check response code", error.status, is("400"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client by trading account=null & server ID (404 error)")
    @AllureId("203")
    public void getClientSuccessTest4() throws IOException {
        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", 1);
        queryParams.put("tradingAccount", 1);
        Response response = getClientsIdByTradingAccountServerId(queryParams);
        GetClientsResponseError error = objectMapper.readValue(response.body().string(), GetClientsResponseError.class);

        // Assert response
        assertThat("Check response code", response.code(), is(404));
        assertThat("Check response code", error.error, containsString("Client not found"));
        assertThat("Check response code", error.status, is("404"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"","qwerty"})
    @DisplayName("Clickhouse Api. Get client by trading account='' & server ID")
    @AllureId("204")
    public void getClientSuccessTest5() throws IOException {
        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", 1);
        Response response = getClientsIdByTradingAccountServerId(queryParams);
        GetClientsResponseError error = objectMapper.readValue(response.body().string(), GetClientsResponseError.class);

        // Assert response
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response code", error.error, is("Invalid tradingAccount format: tradingAccount must be a string that can be parsed into a long"));
        assertThat("Check response code", error.status, is("400"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"","qwerty"})
    @DisplayName("Clickhouse Api. Get client by trading account='' & server ID")
    @AllureId("205")
    public void getClientSuccessTest6(String serverId) throws IOException {
        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1);
        queryParams.put("serverId", serverId);
        Response response = getClientsIdByTradingAccountServerId(queryParams);
        GetClientsResponseError error = objectMapper.readValue(response.body().string(), GetClientsResponseError.class);

        // Assert response
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response code", error.error, is("Invalid serverId format: serverId must be a string that can be parsed into an integer"));
        assertThat("Check response code", error.status, is("400"));
    }
}
