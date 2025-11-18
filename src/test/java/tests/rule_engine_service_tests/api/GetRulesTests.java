package tests.rule_engine_service_tests.api;

import business_objects.api.rule_engine_api.get_rules.GetRulesResponse;
import business_objects.api.rule_engine_api.post_rules.RuleObject;
import business_objects.db.rule_engine_db.rule.RuleDbObjectPgArray;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static business_objects.api.rule_engine_api.get_rules.GetRulesRequest.getRules;
import static business_objects.api.rule_engine_api.post_rules.RuleObjectFactory.generateRule;
import static business_objects.db.rule_engine_db.rule.RuleDbObjectFactory.generateRuleDbObjectByRulePgArray;
import static helpers.database.CleanTableHelper.cleanRuleTableByRuleId;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.DbName.POSTGRES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.SUITE_RULE_ENGINE_API_TESTS;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_API_TESTS)
class GetRulesTests extends TestBaseApi {

    private static RuleDbObjectPgArray ruleDbPgArray;
    private static RuleObject rule;

    @BeforeAll
    static void setupData() throws Exception {
        rule = generateRule();
        ruleDbPgArray = generateRuleDbObjectByRulePgArray(rule);
        insertObjectsToDb(POSTGRES, RULE_ENGINE_RULE_TABLE, List.of(ruleDbPgArray));
    }

    @AfterAll
    static void tearDownData() throws Exception {
        cleanRuleTableByRuleId(rule.getId());
    }

    @Test
    @DisplayName("Rule engine api. Get rules request success")
    @AllureId("1105")
    void getRulesTest1() throws Exception {
        Response response = getRules();

        assertThat(response.body(), is(notNullValue()));
        GetRulesResponse[] mappedResponse = objectMapper.readValue(response.body().string(), GetRulesResponse[].class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Check the response body", Arrays.stream(mappedResponse)  // Convert the array to a stream
                .map(GetRulesResponse::getId)  // Extract the 'id' field from each element
                .collect(Collectors.toList()),  // Collect the results into a list
                hasItem(rule.getId()));
    }
}
