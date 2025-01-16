package tests.connectionSearchApiServiceTests;

import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;


import static businessObjects.db.clickhouse.connectionTable.ConnectionTableEntryFactory.*;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

@Disabled
@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_ATTRIBUTES)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_CONNECTION_SEARCH_SERVICE)
public class GetAbuseTypesByAttributesTest extends TestBaseApi {


}
