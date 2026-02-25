package tests.click_house_api_service_tests;

import static business_objects.api.clickhouse_api_service.get_crypto_deposits.GetCryptoDepositsRequest.getCryptoDeposits;
import static business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObjectFactory.generateMtBalanceOrderByAccount;
import static helpers.data.ClientFactory.getRandomVantageClient;
import static helpers.data.DataSetupHelper.setupData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.api.clickhouse_api_service.get_crypto_deposits.GetCryptoDepositsResponse;
import business_objects.db.clickhouse.mt_balance_orders_table.MtBalanceOrdersObject;
import com.fasterxml.jackson.core.type.TypeReference;
import helpers.data.DataHelper;
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
@Story(STORY_CLICKHOUSE_API_SERVICE_GET_CRYPTO_DEPOSITS)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CLICKHOUSE_API_SERVICE)
class GetCryptoDepositsTests extends TestBaseApi {

    @Test
    @DisplayName("Clickhouse Api. Get crypto deposits success check")
    @AllureId("2425")
    void getCryptoDepositTest1() throws IOException {
        DataHelper data = new DataHelper();
        data.createClient(getRandomVantageClient());

        MtBalanceOrdersObject mtBalanceOrdersObject1 = generateMtBalanceOrderByAccount(data.crmTbAccountObject);
        mtBalanceOrdersObject1.setComment("crypto");
        MtBalanceOrdersObject mtBalanceOrdersObject2 = generateMtBalanceOrderByAccount(data.crmTbAccountObject);
        mtBalanceOrdersObject2.setComment("usdt");
        MtBalanceOrdersObject mtBalanceOrdersObject3 = generateMtBalanceOrderByAccount(data.crmTbAccountObject);
        mtBalanceOrdersObject3.setComment("trc20");
        MtBalanceOrdersObject mtBalanceOrdersObject4 = generateMtBalanceOrderByAccount(data.crmTbAccountObject);
        mtBalanceOrdersObject4.setComment("deposit-wt-");
        MtBalanceOrdersObject mtBalanceOrdersObject5 = generateMtBalanceOrderByAccount(data.crmTbAccountObject);
        mtBalanceOrdersObject5.setComment("bybitpay");
        MtBalanceOrdersObject mtBalanceOrdersObject6 = generateMtBalanceOrderByAccount(data.crmTbAccountObject);
        mtBalanceOrdersObject6.setComment("binancepay");
        data.setMtBalanceOrdersObjects(List.of(
                mtBalanceOrdersObject1,
                mtBalanceOrdersObject2,
                mtBalanceOrdersObject3,
                mtBalanceOrdersObject4,
                mtBalanceOrdersObject5,
                mtBalanceOrdersObject6));

        setupData(data);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("clientIds", List.of(data.clientHelper.getUcid()));

        Response response = getCryptoDeposits(queryParams);

        assertThat(response.body(), is(notNullValue()));
        List<GetCryptoDepositsResponse> mappedResponse =
                objectMapper.readValue(response.body().string(), new TypeReference<>() {});

        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response size", mappedResponse.size(), is(6));
        for (GetCryptoDepositsResponse deposit : mappedResponse) {
            assertThat(
                    "Assert trading account",
                    deposit.getTradingAccount(),
                    is(data.clientHelper.getTradingAccount().toString()));
            assertThat(
                    "Assert server id",
                    deposit.getServerId(),
                    is(data.clientHelper.getServerId().toString()));
        }
    }
}
