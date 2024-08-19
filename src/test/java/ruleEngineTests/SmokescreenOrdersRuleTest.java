package ruleEngineTests;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

public class SmokescreenOrdersRuleTest {
    @Tag("Api")
    @Tag("Manual")
    @Tag("Regression")
    @Tag("Core")
    @DisplayName("Check that user can be processed by the 'Smokescreen orders rule'")
    @Description("""
            RULE DESCRIPTION:
            \s
            Number of trades before withdrawal attempt less than 10,\s
            they have one or two trades whose lot size is at least 50x the average,\s
            at least 2 other accounts linked by SmartID""")
    @AllureId("4")
    @Owner("Nikolai Koriagin")
    public void smokescreenOrdersTest() {
        Allure.step("Prepare test data:");
        Allure.step("");
        Allure.step("Execute test steps");
        Allure.step("Put in kafka incoming message with user1 to check this user");
        Allure.step("Make assertions:");
        Allure.step("Assert that we've got a correct message in kafka output topic");
    }
}
