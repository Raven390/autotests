package business_objects.api.rule_engine_api.get_brands;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;

import static utils.ConfigFactory.*;

public class GetBrandsRequest {
    @Step("Get brands")
    public static Response getBrands() throws IOException {
        String url = RULE_ENGINE_PATH_TEST_ENV + RULE_ENGINE_GET_BRANDS;
        System.out.println(url);
        return new HttpHelper().sendGetRequest(url, null, null);
    }
}
