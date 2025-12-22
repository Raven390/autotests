package tests.rule_engine_service_tests.api;

import static business_objects.api.rule_engine_api.delete_rules.DeleteRulesRequest.deleteRules;
import static business_objects.api.rule_engine_api.post_rules.RuleObjectFactory.generateRule;
import static business_objects.db.rule_engine_db.rule.RuleDbObjectFactory.generateRuleDbObjectByRulePgArray;
import static helpers.database.CleanTableHelper.cleanRuleTableByRuleId;
import static helpers.database.DbHelper.getObjectsFromDB;
import static helpers.database.DbHelper.insertObjectsToDb;
import static helpers.database.DbName.POSTGRES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.SUITE_RULE_ENGINE_API_TESTS;

import business_objects.api.rule_engine_api.post_rules.RuleObject;
import business_objects.db.rule_engine_db.rule.RuleDbObjectPgArray;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import java.util.stream.Collectors;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import tests.TestBaseApi;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_API_TESTS)
class DeleteRulesTests extends TestBaseApi {

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
    @DisplayName("Rule engine api. Delete rules request success")
    @AllureId("1106")
    void deleteRulesTest1() throws Exception {
        Response response = deleteRules(rule.getId());

        assertThat(response.body(), is(notNullValue()));
        List<RuleDbObjectPgArray> ruleFromDb = getObjectsFromDB(
                POSTGRES, RULE_ENGINE_RULE_TABLE, "id = '" + rule.getId() + "'", RuleDbObjectPgArray.class);
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat(
                "Check that no element in the list has the specific id",
                ruleFromDb.stream() // Convert the list to a stream
                        .map(RuleDbObjectPgArray::getId) // Extract the 'id' field from each element
                        .collect(Collectors.toList()), // Collect the ids into a list
                not(hasItem(rule.getId())));
    }
}
