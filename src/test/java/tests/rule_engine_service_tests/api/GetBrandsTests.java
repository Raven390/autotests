package tests.rule_engine_service_tests.api;

import business_objects.api.rule_engine_api.get_brands.GetBrandsResponse;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

import static business_objects.api.rule_engine_api.get_brands.GetBrandsRequest.getBrands;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static tests.TestBaseApi.objectMapper;
import static utils.Constants.*;
import static utils.Constants.SUITE_RULE_ENGINE_API_TESTS;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_API_TESTS)
class GetBrandsTests {

    @Test
    @DisplayName("Rule engine api. Get brands request success")
    @AllureId("1104")
    void getBrandsTest1() throws IOException {

        GetBrandsResponse vantage = new GetBrandsResponse();
        vantage.setName("Vantage");
        vantage.setCode("vantage");

        GetBrandsResponse alphaTick = new GetBrandsResponse();
        alphaTick.setName("AlphaTick");
        alphaTick.setCode("alphatick");

        GetBrandsResponse infinox = new GetBrandsResponse();
        infinox.setName("Infinox");
        infinox.setCode("infinox");

        GetBrandsResponse moneta = new GetBrandsResponse();
        moneta.setName("Moneta");
        moneta.setCode("moneta");

        GetBrandsResponse puPrime = new GetBrandsResponse();
        puPrime.setName("PU Prime");
        puPrime.setCode("puprime");

        GetBrandsResponse rockglobal = new GetBrandsResponse();
        rockglobal.setName("RockGlobal");
        rockglobal.setCode("rockglobal");

        GetBrandsResponse startrader = new GetBrandsResponse();
        startrader.setName("StarTrader");
        startrader.setCode("startrader");

        GetBrandsResponse ultimamarkets = new GetBrandsResponse();
        ultimamarkets.setName("Ultima Markets");
        ultimamarkets.setCode("ultimamarkets");

        GetBrandsResponse vjp = new GetBrandsResponse();
        vjp.setName("VJP");
        vjp.setCode("vjp");

        GetBrandsResponse vt = new GetBrandsResponse();
        vt.setName("VT");
        vt.setCode("vt");

        Response response = getBrands();

        assertThat(response.body(), is(notNullValue()));
        GetBrandsResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetBrandsResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Check the response body", Arrays.stream(mappedResponse).toList(), containsInAnyOrder(vantage, alphaTick, infinox, moneta, puPrime, rockglobal, startrader, ultimamarkets, vjp, vt));

    }
}
