package business_objects.api.rule_engine_api.get_events;

import static utils.ConfigFactory.*;

import helpers.http_helper.HttpHelper;
import io.qameta.allure.Step;
import java.io.IOException;
import okhttp3.Response;

public class GetEventsRequest {
    @Step("Get events")
    public static Response getEvents() throws IOException {
        return new HttpHelper().sendGetRequest(RULE_ENGINE_PATH_TEST_ENV + RULE_ENGINE_GET_EVENTS, null, null);
    }
}
