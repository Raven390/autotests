package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_mirror_trade_on_last_withdrawal.GetMirrorTradeOnLastWithdrawalRequest.getMirrorTradesOnLastWithdrawal;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAccountByClient;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.rules.MirrorTradeOnLastWithdrawalDataInserter.deleteData;
import static helpers.data.rules.MirrorTradeOnLastWithdrawalDataInserter.insertMirrorTradeOnLastWithdrawalData;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.Constants.*;
import static utils.Constants.LAYER_API;
import static utils.Constants.SUITE_CLICKHOUSE_API_SERVICE;

import business_objects.api.clickhouse_api_service.ClickhouseApiErrorResponse;
import business_objects.api.clickhouse_api_service.get_mirror_trade_on_last_withdrawal.GetMirrorTradeOnLastWithdrawalResponse;
import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import java.io.IOException;
import java.util.List;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_CLICKHOUSE_API_SERVICE)
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_MIRROR_TRADE_ON_LAST_WITHDRAWAL)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetMirrorTradeOnLastWithdrawalTests extends TestBaseApi {

    private static ClientHelper client1;
    private static ClientHelper client2;
    private static ClientHelper client3;
    private static CrmTbUserObject crmTbUserObject1;
    private static CrmTbUserObject crmTbUserObject2;
    private static CrmTbUserObject crmTbUserObject3;
    private static CrmTbAccountObject crmTbAccountObject2;

    @BeforeAll
    static void setup() {
        client1 = getRandomVantageClientAllFields();
        client2 = getRandomVantageClientAllFields();
        client3 = getRandomVantageClientAllFields();
        insertMirrorTradeOnLastWithdrawalData(client1);
        crmTbUserObject1 = generateUserByClient(client1);
        crmTbUserObject2 = generateUserByClient(client2);
        crmTbUserObject3 = generateUserByClient(client3);
        crmTbAccountObject2 = generateAccountByClient(client2, false);

        insertObjectsToDb(
                DbName.CLICKHOUSE, CRM_USER_TABLE_NAME, List.of(crmTbUserObject1, crmTbUserObject2, crmTbUserObject3));
        insertObjectsToDb(DbName.CLICKHOUSE, CRM_TB_ACCOUNT_TABLE_NAME, List.of(crmTbAccountObject2));
    }

    @AfterAll
    static void teardown() {
        deleteData(client1);
        deleteData(client2);
    }

    @Test
    @AllureId("1897")
    @DisplayName("Clickhouse Api. Get mirror trade on last withdrawal(200)")
    void getMirrorTradeOnLastWithdrawalTest1() throws IOException {
        Response response = getMirrorTradesOnLastWithdrawal(client1.getUcid());
        assertThat(response.body(), is(notNullValue()));
        GetMirrorTradeOnLastWithdrawalResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetMirrorTradeOnLastWithdrawalResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.getSuspiciousFlag(), is(true));
        assertThat("Assert response length", mappedResponse.getSuspiciousFlag(), is(notNullValue()));
    }

    @Test
    @AllureId("1896")
    @DisplayName("Clickhouse Api. Get mirror trade on last withdrawal. Empty response(200)")
    void getMirrorTradeOnLastWithdrawalTest2() throws IOException {
        Response response = getMirrorTradesOnLastWithdrawal(client2.getUcid());
        assertThat(response.body(), is(notNullValue()));
        GetMirrorTradeOnLastWithdrawalResponse mappedResponse =
                objectMapper.readValue(response.body().string(), GetMirrorTradeOnLastWithdrawalResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response length", mappedResponse.getSuspiciousFlag(), is(false));
        assertThat("Assert response length", mappedResponse.getSuspiciousFlag(), is(notNullValue()));
    }

    @Test
    @AllureId("1895")
    @DisplayName("Clickhouse Api. Get mirror trade on last withdrawal(404)")
    void getMirrorTradeOnLastWithdrawalTest3() throws IOException {
        Response response = getMirrorTradesOnLastWithdrawal("ucid-1234");
        assertThat(response.body(), is(notNullValue()));
        ClickhouseApiErrorResponse mappedResponse =
                objectMapper.readValue(response.body().string(), ClickhouseApiErrorResponse.class);
        assertThat("Assert that code is 200", response.code(), is(404));
        assertThat(
                "Assert response error",
                mappedResponse.getError(),
                is("TradingAccount or serverId not found for client ucid-1234."));
        assertThat("Assert response length", mappedResponse.getStatus(), is(404));
    }
}
