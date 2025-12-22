package business_objects.api.rule_engine_api.get_brands;

import static utils.ConfigFactory.*;
import static utils.Utils.writeLog;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import okhttp3.Response;

public class GetBrandsRequest {
    @Step("Get brands")
    public static Response getBrands() throws IOException {
        String url = RULE_ENGINE_PATH_TEST_ENV + RULE_ENGINE_GET_BRANDS;
        writeLog(url);
        return new HttpHelper().sendGetRequest(url, null, null);
    }
}
