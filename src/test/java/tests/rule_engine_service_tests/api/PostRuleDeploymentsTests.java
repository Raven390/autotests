package tests.rule_engine_service_tests.api;

import static business_objects.api.rule_engine_api.rule_deployments.RuleDeploymentsRequests.postRuleDeployment;
import static helpers.database.CleanTableHelper.cleanRuleDeploymentTableByUuId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;

import business_objects.api.rule_engine_api.rule_deployments.PostRuleDeploymentResponse;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.io.IOException;
import java.sql.SQLException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;
import utils.Utils;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_API_TESTS)
class PostRuleDeploymentsTests extends TestBaseApi {

    private static String processId = "Process" + Utils.getRandomIntPositive();
    private static String xmlData = EMPTY_RULE_XML.replace("process_id", processId);

    @BeforeAll
    static void setupData() {}

    @AfterAll
    static void deleteData() throws SQLException {
        cleanRuleDeploymentTableByUuId(processId);
    }

    @Test
    @DisplayName("Rule engine api. Post deploy rule request success")
    @AllureId("1113")
    void postRuleDeploymentTest() throws IOException {
        RequestBody requestBody = RequestBody.create(xmlData, MediaType.parse("text/xml"));
        Response response = postRuleDeployment(requestBody, "author_name", "rule_name");

        assertThat(response.body(), is(notNullValue()));
        PostRuleDeploymentResponse mappedResponse =
                objectMapper.readValue(response.body().string(), PostRuleDeploymentResponse.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert rule uuid", mappedResponse.getUuid(), is(notNullValue()));
        assertThat("Assert rule authorName", mappedResponse.getAuthorName(), is("author_name"));
        assertThat("Assert rule processId", mappedResponse.getProcessId(), is(processId));
        assertThat("Assert rule ruleName", mappedResponse.getRuleName(), is("rule_name"));
        assertThat("Assert rule zeebeRevision", mappedResponse.getZeebeRevision(), is(1));
        assertThat("Assert rule lastUpdate", mappedResponse.getLastUpdate(), is(equalTo(Utils.getCurrentDate())));
        assertThat("Assert rule status", mappedResponse.getStatus(), is("DEPLOYED"));
        assertThat("Assert rule comment", mappedResponse.getComment(), is(nullValue()));
    }
}
