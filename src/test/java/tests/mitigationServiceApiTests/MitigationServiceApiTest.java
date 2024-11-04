package tests.mitigationServiceApiTests;

import businessObjects.api.mitigationService.GetRestrictionResponseBody;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.util.Arrays;

import static businessObjects.api.mitigationService.MitigationServiceRequest.*;
import static utils.Constants.*;

@Disabled
@Feature(FEATURE_CONNECTION_SEARCH_API_SERVICE)
@Story(STORY_CONNECTION_SEARCH_BY_CLIENT_ID)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_MITIGATION_SERVICE)
public class MitigationServiceApiTest extends TestBaseApi {

    @Test
    @DisplayName("Mitigation service tests")
    @AllureId("")
    public void getRestrictionsByUcidTest() throws IOException{

        Response response = getRestrictionsByUcid("vantage-10079856");
        GetRestrictionResponseBody[] restrictionBody = objectMapper.readValue(
                response.body().string(),
                GetRestrictionResponseBody[].class
        );
        System.out.println(Arrays.toString(restrictionBody));
        System.out.println(response.code());
    }
}
