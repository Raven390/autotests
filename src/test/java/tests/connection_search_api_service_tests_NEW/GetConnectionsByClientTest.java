package tests.connection_search_api_service_tests_NEW;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;


import static business_objects.api.connection_search_api.get_connections.GetConnectionsResponseFactory.*;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_CLIENT_ID)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)

@Disabled
@Tag(TAG_MANUAL)
class GetConnectionsByClientTest extends TestBaseApi {


}
