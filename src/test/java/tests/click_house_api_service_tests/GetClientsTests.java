package tests.click_house_api_service_tests;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_clients.GetClientsResponse;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.dict_account_to_ucid.DictAccountToUcidObject;
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
import java.util.HashMap;
import java.util.Map;

import static business_objects.api.clickhouse_api_service.get_clients.GetClientsRequest.getClientsIdByTradingAccountServerId;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountData;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.dict_account_to_ucid.DictAccountToUcidObjectFactory.generateDictByClient;
import static helpers.data.ClientFactory.getRandomClient;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
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
    public void getClientTest1() throws IOException {
        // Create an instance of ClientHelper
        ClientHelper client = getRandomClient();

        // Insert in dict table
        DictAccountToUcidObject dictAccountToUcidObject = generateDictByClient(client);
        insertObjectToDb(DICT_ACCOUNT_TO_UCID, dictAccountToUcidObject);
        // Insert in crm user table
        CrmTbUserObject crmObject = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmObject);
        // Insert object in mt user table
        CrmTbAccountObject accountObject = generateCrmTbAccountData(client);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, accountObject);

        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client.getTradingAccount());
        queryParams.put("serverId", client.getServerId());
        Response response = getClientsIdByTradingAccountServerId(queryParams);

        GetClientsResponse clients = objectMapper.readValue(response.body().string(), GetClientsResponse.class);

        // Assert response
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check client ucid", clients.clientId, is(client.getUcid()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client by userId + brand")
    @AllureId("600")
    public void getClientTest2() throws IOException {
        // Create an instance of ClientHelper
        ClientHelper client = getRandomClient();

        // Insert in dict table
        DictAccountToUcidObject dictAccountToUcidObject = generateDictByClient(client);
        insertObjectToDb(DICT_ACCOUNT_TO_UCID, dictAccountToUcidObject);
        // Insert in crm user table
        CrmTbUserObject crmObject = generateUserByClient(client);
        insertObjectToDb(CRM_USER_TABLE_NAME, crmObject);
        // Insert object in mt user table
        CrmTbAccountObject accountObject = generateCrmTbAccountData(client);
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, accountObject);

        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", client.getUserId());
        queryParams.put("brand", client.getBrand());
        Response response = getClientsIdByTradingAccountServerId(queryParams);

        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        System.out.println(response);

        // Assert response
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error text", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Check response code", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client by server ID=null (400 error)")
    @AllureId("201")
    public void getClientTest3() throws IOException {
        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1);
        Response response = getClientsIdByTradingAccountServerId(queryParams);
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);

        // Assert response
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error text", mappedResponse.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
        assertThat("Check response code", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client by trading account=null (400 error)")
    @AllureId("202")
    public void getClientTest4() throws IOException {
        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", 1);
        Response response = getClientsIdByTradingAccountServerId(queryParams);
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        System.out.println(response);

        // Assert response
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error text", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Check response code", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client by userID=null (400 error)")
    @AllureId("599")
    public void getClientTest5() throws IOException {
        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("brand", "1");
        Response response = getClientsIdByTradingAccountServerId(queryParams);
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        System.out.println(response);

        // Assert response
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error text", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Check response code", mappedResponse.status, is(400));
    }

    @Test
    @DisplayName("Clickhouse Api. Get client by brand=null (400 error)")
    @AllureId("598")
    public void getClientTest6() throws IOException {
        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("userId", 1);
        Response response = getClientsIdByTradingAccountServerId(queryParams);
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        System.out.println(response);

        // Assert response
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error text", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
        assertThat("Check response code", mappedResponse.status, is(400));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "qwerty"})
    @DisplayName("Clickhouse Api. Get client by trading account='' & server ID")
    @AllureId("204")
    public void getClientTest7() throws IOException {
        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "");
        queryParams.put("serverId", 1);
        Response response = getClientsIdByTradingAccountServerId(queryParams);
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);

        // Assert response
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error text", mappedResponse.error, is("Invalid tradingAccount format: tradingAccount must be a string that can be parsed into a long"));
        assertThat("Check response code", mappedResponse.status, is(400));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "qwerty"})
    @DisplayName("Clickhouse Api. Get client by trading account='' & server ID")
    @AllureId("205")
    public void getClientTest8(String serverId) throws IOException {
        // getClient request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1);
        queryParams.put("serverId", serverId);
        Response response = getClientsIdByTradingAccountServerId(queryParams);
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);

        // Assert response
        assertThat("Check response code", response.code(), is(400));
        assertThat("Check response error text", mappedResponse.error, is("Invalid serverId format: serverId must be a string that can be parsed into an integer"));
        assertThat("Check response code", mappedResponse.status, is(400));
    }
}
