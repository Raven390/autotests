package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_free_margin.GetFreeMarginRequest.getFreeMargin;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByClient;
import static business_objects.db.clickhouse.mt___mt5_deals_coerced_dd.Mt5DealsCoercedDdFactory.generateTradeByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByClient;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_CLICKHOUSE_API_SERVICE;

import business_objects.api.clickhouse_api_service.get_free_margin.GetFreeMarginResponse;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import com.fasterxml.jackson.core.type.TypeReference;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_FREE_MARGIN)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetFreeMarginTests extends TestBaseApi {

    private static final String DATE_TIME = "2024-12-29 14:59:30.084000000";

    private static MtAccountObject data1;
    private static ClientHelper client1;

    @BeforeAll
    static void setup() {
        client1 = getRandomVantageClient();
        data1 = generateMtAccountByClient(client1);
        insertObjectToDb(MT_ACCOUNT_TABLE_NAME, data1);
        CrmTbAccountForMtObject accountForMtObject = generateAccountForMtByClient(client1, false);
        insertObjectToDb(CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, accountForMtObject);
        CrmTbAccountObject account = generateStaticCrmTbAccountActive(client1);
        insertObjectToDb(CRM_TB_ACCOUNT_TABLE_NAME, account);

        var mt5DealsCoercedDdObjects = List.of(
                generateTradeByClient(client1, 200d, 50d, 12d, "2024-12-29 14:59:31.084000000"),
                generateTradeByClient(client1, 300d, 60d, 15d, DATE_TIME),
                generateTradeByClient(client1, 400d, 70d, 20d, "2024-12-29 14:59:29.084000000"));

        insertObjectsToDb(MT5_DEALS_COERCED_DD_TABLE_NAME, mt5DealsCoercedDdObjects);
    }

    @AfterAll
    static void teardown() {
        deleteObjectFromDb(MT_ACCOUNT_TABLE_NAME, String.format("account = '%s'", data1.account));
        deleteObjectFromDb(CRM_TB_ACCOUNT_TABLE_NAME, String.format("account = '%s'", data1.account));
        deleteObjectFromDb(CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, String.format("account = '%s'", data1.account));
    }

    @Test
    @AllureId("2052")
    @DisplayName("Clickhouse Api. Get free margin by account")
    void getFreeMarginByAccountTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("tradingAccount", client1.getTradingAccount()); // Required
        queryParams.put("serverId", client1.getServerId()); // Required
        queryParams.put("dateTo", DATE_TIME.replace(" ", "T"));
        Response response = getFreeMargin(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetFreeMarginResponse.FreeMarginItem> items =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert listSize", items.size(), is(1));
        assertThat("Assert freeMarginUsd", items.getFirst().getFreeMarginUSD(), is(15d));
        assertThat("Assert equityUsd", items.getFirst().getEquityUSD(), is(60d));
    }

    @Test
    @AllureId("2060")
    @DisplayName("Clickhouse Api. Get free margin by ucid")
    void getFreeMarginByUcidTest() throws IOException {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("ucid", client1.getUcid());
        queryParams.put("dateTo", DATE_TIME.replace(" ", "T"));
        Response response = getFreeMargin(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetFreeMarginResponse.FreeMarginItem> items =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert listSize", items.size(), is(1));
        assertThat("Assert freeMarginUsd", items.getFirst().getFreeMarginUSD(), is(15d));
        assertThat("Assert equityUsd", items.getFirst().getEquityUSD(), is(60d));
    }
}
