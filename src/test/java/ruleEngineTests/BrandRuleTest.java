package ruleEngineTests;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class BrandRuleTest {

    @Disabled
    @Test
    @Tag("Api")
    @Tag("Manual")
    @Tag("Regression")
    @Tag("Core")
    @DisplayName("Check that user can be processed by the 'Brand rule'")
    @Description(
            """
                    RULE DESCRIPTION:
                    \s
                    VJP,\s
                    Deposit: Crypto,\s
                    Users linked by device >5,\s
                    outlook/gmail emails,\s
                    email age >3months for all device linked accounts.\s
                    4 numbers in emails is a sub-pattern for this as well.""")
    @AllureId("2")
    @Owner("Nikolai Koriagin")
    public void brandRuleTest() {
        Allure.step("");
        Allure.step("Execute test steps");
        Allure.step("Put in kafka incoming message with user1 to check this user");
        Allure.step("Make assertions:");
        Allure.step("Assert that we've got a correct message in kafka output topic");
    }
}
