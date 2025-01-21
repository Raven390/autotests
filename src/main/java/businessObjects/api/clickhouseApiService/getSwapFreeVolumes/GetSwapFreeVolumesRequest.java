package businessObjects.api.clickhouseApiService.getSwapFreeVolumes;

import helpers.httpHelper.HttpHelper;
import io.qameta.allure.Step;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

import static utils.ConfigFactory.*;

public class GetSwapFreeVolumesRequest {

    @Step("Get swap free volumes")
    public static Response getSwapFreeVolumes(Map<String, Object> paramsMap) throws IOException {
        return new HttpHelper().sendGetRequest(CLICKHOUSE_API_BASE_TEST + CLICKHOUSE_API_GET_SWAP_FREE_VOLUMES, null, paramsMap);
    }
}
