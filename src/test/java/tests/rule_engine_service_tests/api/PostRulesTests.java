package tests.rule_engine_service_tests.api;

import business_objects.api.rule_engine_api.post_rules.RuleObject;
import business_objects.db.rule_engine_db.rule.RuleDbObjectPgArray;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import okhttp3.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseApi;

import java.util.*;

import static business_objects.api.rule_engine_api.post_rules.PostRulesRequest.postRules;
import static business_objects.api.rule_engine_api.post_rules.RuleObjectFactory.generateRule;
import static helpers.database.CleanTableHelper.cleanRuleTableByRuleId;
import static helpers.database.DbHelper.getObjectsFromDB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Constants.SUITE_RULE_ENGINE_API_TESTS;
import static utils.Utils.writeLog;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_API_TESTS)
class PostRulesTests extends TestBaseApi {

    private static RuleObject rule;

    @BeforeAll
    static void setupData() {
        rule = generateRule();
    }

    @AfterAll
    static void tearDownData() throws Exception {
        cleanRuleTableByRuleId(rule.getId());
    }

    @Test
    @DisplayName("Rule engine api. Post rules request success")
    @AllureId("1106")
    void postRulesTest1() throws Exception {
        Response response = postRules(rule);

        assertThat(response.body(), is(notNullValue()));
        assertThat("Assert that code is 200", response.code(), is(200));
        assertThat("Assert response body", response.body().string(), is("{\"id\":\"" + rule.getId() + "\"}"));

        List<RuleDbObjectPgArray> ruleFromDb = getObjectsFromDB(DbName.POSTGRES, RULE_ENGINE_RULE_TABLE, "id = '" + rule.getId() + "'", RuleDbObjectPgArray.class);
        assertThat("Check id", rule.getId(), is(equalTo(ruleFromDb.getFirst().getId())));
        assertThat("Check event_type", rule.getEventType(), is(equalTo(ruleFromDb.getFirst().getEventType())));
        assertThat("Check rule_name", rule.getValue().getName(), is(equalTo(ruleFromDb.getFirst().getRuleName())));
        assertThat("Check brands", ruleFromDb.getFirst().getBrands().toString(), containsString(rule.getValue().getBrands().getLast()));
        assertThat("Check brands", ruleFromDb.getFirst().getBrands().toString(), containsString(rule.getValue().getBrands().getFirst()));
        writeLog(ruleFromDb);
    }
}
