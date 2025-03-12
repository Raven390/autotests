package tests.clickHouseApiServiceTests;

import businessObjects.api.clickhouseApiService.ClickhouseApiErrorResponse;
import businessObjects.api.clickhouseApiService.getSwapFreeVolumes.GetSwapFreeVolumesResponse;
import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static businessObjects.api.clickhouseApiService.getSwapFreeVolumes.GetSwapFreeVolumesRequest.getSwapFreeVolumes;
import static businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObjectFactory.generateAccountByClient;
import static businessObjects.db.clickhouse.mtMt5DealsCoerced.Mt5DealsCoercedFactory.generateTradeByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.CleanTableHelper.cleanCrmUserTableByClient;
import static helpers.database.CleanTableHelper.cleanMt5CoercedTableByComment;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_SWAP_FREE_VOLUME)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
public class GetSwapFreeVolumesTests extends TestBaseApi {

    //Client 1 data
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final CrmTbAccountObject account1 = generateAccountByClient(client1, true);

    private static final Mt5DealsCoercedObject deal1 = generateTradeByClient(client1, 0, 0, 1, 1111L);
    private static final Mt5DealsCoercedObject deal2 = generateTradeByClient(client1, 1, 1, 1, 1111L);
    private static final Mt5DealsCoercedObject deal3 = generateTradeByClient(client1, 1, 1, 3, 1112L);

    //Client 2 data
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final CrmTbAccountObject account2 = generateAccountByClient(client2, false);
    private static final Mt5DealsCoercedObject deal5 = generateTradeByClient(client2, 0, 0, 0, 1113L);
    private static final Mt5DealsCoercedObject deal6 = generateTradeByClient(client2, 1, 1, 0, 1114L);

    //Client 3 data
    private static final ClientHelper client3 = getRandomVantageClientAllFields();

    public static final String dateTo = getCurrentTimestampDbFormat();

    @BeforeAll
    public static void setupData() {
        insertObjectsToDb(CRM_ACCOUNT_TABLE_NAME, List.of(account1, account2));
        insertObjectsToDb(MT5_DEALS_COERCED_TABLE_NAME, List.of(deal1, deal2, deal3, deal5, deal6));
    }

    //TODO uncomment after solving error with delete statement
    @AfterAll
    public static void teardownData() throws Exception {
        cleanCrmUserTableByClient(client1.getUcid(), client2.getUcid(), client3.getUcid());
        cleanMt5CoercedTableByComment(deal1.comment, deal2.comment);
        // deleteEntryFromDb(CRM_ACCOUNT_TABLE_NAME, String.format("account = '%s'", client1.getTradingAccount()));
        // deleteEntryFromDb(CRM_ACCOUNT_TABLE_NAME, String.format("account = '%s'", client2.getTradingAccount()));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free volumes with all params")
    @AllureId("590")
    public void getSwapFreeVolumeTest1() throws IOException {
        //Send request
        System.out.println(client1.getTradingAccount());
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", dateTo.replace(" ", "T"));
        Response response = getSwapFreeVolumes(queryParams);

        assert response.body() != null;
        GetSwapFreeVolumesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeVolumesResponse.class);
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check list size", mappedResponse.tradingIndicators.size(), is(4));
        assertThat("Check indicatorDate", mappedResponse.indicatorDate, notNullValue());

        assertThat("Check volumeInitial", mappedResponse.tradingIndicators.getFirst().volumeInitial, is("1.0000"));
        assertThat("Check volumeOpened", mappedResponse.tradingIndicators.get(1).volumeOpened, is("1.0000"));
        assertThat("Check volumeClosed", mappedResponse.tradingIndicators.get(2).volumeClosed, is("1.0000"));
        assertThat("Check volumeEndOfDay", mappedResponse.tradingIndicators.getLast().volumeEndOfDay, is("1.0000"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free volumes with required params only")
    @AllureId("591")
    public void getSwapFreeVolumeTest2() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        Response response = getSwapFreeVolumes(queryParams);

        assert response.body() != null;
        GetSwapFreeVolumesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeVolumesResponse.class);
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check list size", mappedResponse.tradingIndicators.size(), is(4));
        assertThat("Check indicatorDate", mappedResponse.indicatorDate, notNullValue());

        assertThat("Check volumeInitial", mappedResponse.tradingIndicators.getFirst().volumeInitial, is("1.0000"));
        assertThat("Check volumeOpened", mappedResponse.tradingIndicators.get(1).volumeOpened, is("1.0000"));
        assertThat("Check volumeClosed", mappedResponse.tradingIndicators.get(2).volumeClosed, is("1.0000"));
        assertThat("Check volumeEndOfDay", mappedResponse.tradingIndicators.getLast().volumeEndOfDay, is("1.0000"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free volumes with wrong dateTo")
    @AllureId("592")
    public void getSwapFreeVolumeTest3() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", 1);
        Response response = getSwapFreeVolumes(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert type", mappedResponse.type, is("about:blank"));
        assertThat("Assert title", mappedResponse.title, is("Bad Request"));
        assertThat("Assert detail", mappedResponse.detail, is("Failed to convert 'dateTo' with value: '1'"));
        assertThat("Assert instance", mappedResponse.instance, is("/v1/swapFreeVolumes"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free volumes with tradingAccount only")
    @AllureId("593")
    public void getSwapFreeVolumeTest4() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", 1); // Required
        Response response = getSwapFreeVolumes(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert error", mappedResponse.error, is("Required request parameter 'serverId' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free volumes for not swap free account -  empty response")
    @AllureId("594")
    public void getSwapFreeVolumeTest5() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client2.getTradingAccount()); // Required
        queryParams.put("serverId", client2.getServerId()); // Required
        Response response = getSwapFreeVolumes(queryParams);

        assert response.body() != null;
        GetSwapFreeVolumesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeVolumesResponse.class);
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check list size", mappedResponse.tradingIndicators.size(), is(4));
        assertThat("Check indicatorDate", mappedResponse.indicatorDate, notNullValue());

        assertThat("Check volumeInitial", mappedResponse.tradingIndicators.getFirst().volumeInitial, is("0.0000"));
        assertThat("Check volumeOpened", mappedResponse.tradingIndicators.get(1).volumeOpened, is("0.0000"));
        assertThat("Check volumeClosed", mappedResponse.tradingIndicators.get(2).volumeClosed, is("0.0000"));
        assertThat("Check volumeEndOfDay", mappedResponse.tradingIndicators.getLast().volumeEndOfDay, is("0.0000"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free volumes with serverId only")
    @AllureId("595")
    public void getSwapFreeVolumeTest6() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("serverId", 1); // Required
        Response response = getSwapFreeVolumes(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert error", mappedResponse.error, is("Required request parameter 'tradingAccount' for method parameter type String is not present"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free volumes dateTo < order date")
    @AllureId("596")
    public void getSwapFreeVolumeTest7() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", LocalDateTime.now().minusYears(10));
        Response response = getSwapFreeVolumes(queryParams);

        assert response.body() != null;
        GetSwapFreeVolumesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeVolumesResponse.class);
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check list size", mappedResponse.tradingIndicators.size(), is(4));
        assertThat("Check indicatorDate", mappedResponse.indicatorDate, notNullValue());

        assertThat("Check volumeInitial", mappedResponse.tradingIndicators.getFirst().volumeInitial, is("0.0000"));
        assertThat("Check volumeOpened", mappedResponse.tradingIndicators.get(1).volumeOpened, is("0.0000"));
        assertThat("Check volumeClosed", mappedResponse.tradingIndicators.get(2).volumeClosed, is("0.0000"));
        assertThat("Check volumeEndOfDay", mappedResponse.tradingIndicators.getLast().volumeEndOfDay, is("0.0000"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free volumes with wrong tradingAccount format")
    @AllureId("643")
    public void getSwapFreeVolumeTest8() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", "abc"); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        Response response = getSwapFreeVolumes(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid tradingAccount format: tradingAccount must be a string that can be parsed into a long"));
    }

    @Test
    @DisplayName("Clickhouse Api. Get swap free volumes with wrong serverId format")
    @AllureId("644")
    public void getSwapFreeVolumeTest9() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", "abc"); // Required
        Response response = getSwapFreeVolumes(queryParams);

        assert response.body() != null;
        ClickhouseApiErrorResponse mappedResponse = objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 400", response.code(), is(400));

        assertThat("Assert status", mappedResponse.status, is(400));
        assertThat("Assert error", mappedResponse.error, is("Invalid serverId format: serverId must be a string that can be parsed into an integer"));
    }

    @Test
    @DisplayName("Clickhouse Api. Response for user without orders")
    @AllureId("645")
    public void getSwapFreeVolumeTest10() throws IOException {
        //Send request
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client3.getTradingAccount()); // Required
        queryParams.put("serverId", client3.getServerId()); // Required
        Response response = getSwapFreeVolumes(queryParams);

        assert response.body() != null;
        GetSwapFreeVolumesResponse mappedResponse = objectMapper.readValue(response.body().string(), GetSwapFreeVolumesResponse.class);
        assertThat("Check response code", response.code(), is(200));
        assertThat("Check list size", mappedResponse.tradingIndicators.size(), is(4));
        assertThat("Check indicatorDate", mappedResponse.indicatorDate, notNullValue());

        assertThat("Check volumeInitial", mappedResponse.tradingIndicators.getFirst().volumeInitial, is("0.0000"));
        assertThat("Check volumeOpened", mappedResponse.tradingIndicators.get(1).volumeOpened, is("0.0000"));
        assertThat("Check volumeClosed", mappedResponse.tradingIndicators.get(2).volumeClosed, is("0.0000"));
        assertThat("Check volumeEndOfDay", mappedResponse.tradingIndicators.getLast().volumeEndOfDay, is("0.0000"));
    }
}
