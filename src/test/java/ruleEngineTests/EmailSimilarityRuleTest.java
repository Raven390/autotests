package ruleEngineTests;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;

public class EmailSimilarityRuleTest {

    @Disabled
    @Tag("Api")
    @Tag("Manual")
    @Tag("Regression")
    @Tag("Core")
    @DisplayName("Check that user can be processed by the 'Email similarity rule'")
    @Description(
            """
                    RULE DESCRIPTION:
                    \s
                    There are at least 5 accounts whose email addresses are less than a month old and similar to one another,
                    all of these are registrations.
                    IP addresses: either linked by IP or ISP, and they are all proxy IPs.
                    Crypto or bridger deposit, bonus claimed.""")
    @AllureId("3")
    @Owner("Nikolai Koriagin")
    public void emailSimilarityTest() {
        Allure.step("");
        Allure.step("Execute test steps");
        Allure.step("Put in kafka incoming message with user1 to check this user");
        Allure.step("Make assertions:");
        Allure.step("Assert that we've got a correct message in kafka output topic");
    }
}
