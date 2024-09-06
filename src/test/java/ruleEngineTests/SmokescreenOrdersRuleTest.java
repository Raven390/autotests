package ruleEngineTests;

import static utils.Constants.*;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class SmokescreenOrdersRuleTest {

    @Test
    @AllureId("40")
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(LAYER_API)
    @Tag(STATUS_MANUAL)
    @Tag(SUITE_REGRESSION)
    @Tag(TEAM_CORE)
    @DisplayName("Check that user can be processed by the 'Smokescreen orders rule'")
    @Description(
            """
                    RULE DESCRIPTION:
                    \s
                    Number of trades before withdrawal attempt less than 10,\s
                    they have one or two trades whose lot size is at least 50x the average,\s
                    at least 2 other accounts linked by SmartID""")
    public void smokescreenOrdersTest() {
        Allure.step("Prepare test data:");
        Allure.step("");
        Allure.step("Execute test steps");
        Allure.step("Put in kafka incoming message with user1 to check this user");
        Allure.step("Make assertions:");
        Allure.step("Assert that we've got a correct message in kafka output topic");
    }
}
