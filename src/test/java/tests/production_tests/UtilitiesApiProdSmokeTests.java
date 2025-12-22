package tests.production_tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.AllureId;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UtilitiesApiProdSmokeTests {
    @Disabled
    @Test
    @DisplayName("Production smoke test. Get abuse types (200)")
    @AllureId("741")
    void testClickHouseApiProd1() throws IOException {
        Map<String, Object> queryParamsMap = new HashMap<>();
        queryParamsMap.put("brand", "");
        queryParamsMap.put("email", "");
        queryParamsMap.put("phoneNum", "");
        Response response = new HttpHelper()
                .sendGetRequest(UTILITIES_API_SERVICE_PROD_BASE_PATH + UTILITIES_API_GET_DECRYPT, null, queryParamsMap);
        assertThat("Assert that code is 200", response.code(), is(200));
    }
}
