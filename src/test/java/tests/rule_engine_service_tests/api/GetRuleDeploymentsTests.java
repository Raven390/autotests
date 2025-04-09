package tests.rule_engine_service_tests.api;

import business_objects.api.rule_engine_api.rule_deployments.GetRuleDeploymentResponse;
import business_objects.db.rule_engine_db.rule_deployment.RuleDeploymentObject;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static business_objects.api.rule_engine_api.rule_deployments.RuleDeploymentsRequests.getRuleDeploymentsByProcessId;
import static business_objects.api.rule_engine_api.rule_deployments.RuleDeploymentsRequests.getRuleDeploymentsNoParams;
import static business_objects.db.rule_engine_db.rule_deployment.RuleDeploymentObjectFactory.generateRuleDeploymentObject;
import static helpers.database.CleanTableHelper.cleanRuleDeploymentTableByUuId;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.DbName.RULE_ENGINE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.SUITE_RULE_ENGINE_API_TESTS;
import static utils.Utils.getRandomUuid;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_API_TESTS)
class GetRuleDeploymentsTests extends TestBaseApi {

    private static RuleDeploymentObject rule;
    private static UUID ruleUuid;

    @BeforeAll
    static void setupData() {
        ruleUuid = getRandomUuid();
        rule = generateRuleDeploymentObject(ruleUuid);
        insertObjectsToDb(RULE_ENGINE, RULE_ENGINE_RULE_DEPLOYMENT_TABLE, List.of(rule));
    }

    @AfterAll
    static void deleteData() throws SQLException {
        cleanRuleDeploymentTableByUuId(rule.getUuid().toString());
    }

    @Test
    @DisplayName("Rule engine api. Get rule deployment without parameter success")
    @AllureId("1123")
    void getRuleDeploymentTest1() throws IOException {
        Response response = getRuleDeploymentsNoParams();
        assert response.body() != null;
        List<GetRuleDeploymentResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetRuleDeploymentResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert rule uuid", mappedResponse.size(), greaterThanOrEqualTo(1));
    }

    @Test
    @DisplayName("Rule engine api. Get rule deployment with parameter success")
    @AllureId("1124")
    void getRuleDeploymentTest2() throws IOException {
        Response response = getRuleDeploymentsByProcessId(rule.getProcessId());
        assert response.body() != null;
        List<GetRuleDeploymentResponse> mappedResponse = Arrays.stream(objectMapper.readValue(response.body().string(), GetRuleDeploymentResponse[].class)).toList();
        assertThat("Assert that code is 200", response.code(), is(200));

        assertThat("Assert rule Version", mappedResponse.getFirst().getVersion(), equalTo(rule.getVersion()));
        assertThat("Assert rule uuid", mappedResponse.getFirst().getUuid(), equalTo(rule.getUuid().toString()));
        assertThat("Assert rule AuthorName", mappedResponse.getFirst().getAuthorName(), equalTo(rule.getAuthorName()));
        assertThat("Assert rule Rule name", mappedResponse.getFirst().getRuleName(), equalTo(rule.getRuleName()));
        assertThat("Assert rule ProcessId", mappedResponse.getFirst().getProcessId(), equalTo(rule.getProcessId()));
        assertThat("Assert rule ZeebeRevision", mappedResponse.getFirst().getZeebeRevision(), equalTo(rule.getZeebeRevision()));
        assertThat("Assert rule LastUpdate", mappedResponse.getFirst().getLastUpdate(), equalTo(rule.getLastUpdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        assertThat("Assert rule Status", mappedResponse.getFirst().getStatus(), equalTo(rule.getStatus().toString()));
        assertThat("Assert rule Comment", mappedResponse.getFirst().getComment(), equalTo(rule.getComment()));
    }
}
