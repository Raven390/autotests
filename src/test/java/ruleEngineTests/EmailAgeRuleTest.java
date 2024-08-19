package ruleEngineTests;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class EmailAgeRuleTest {

    @Test
    @Tag("Api")
    @Tag("Manual")
    @Tag("Regression")
    @Tag("Core")
    @DisplayName("Check that user can be processed by the 'Email age rule'")
    @Description("""
            RULE DESCRIPTION:
            
            Email age: less than 1 or 3 months for two accounts\s
            that are connected by SmartID\s
            OR the first 3 octets of the IP address,\s
            both using a deposit bonus with crypto FTD,\s
            both trading the same symbol,\s
            opening and closing times are in pairs with less than 30 sec difference in both.
            \s""")
    @AllureId("2")
    @Owner("Nikolai Koriagin")
    public void emailAgeTest() {
        Allure.step("Prepare test data:");
        Allure.step("Create user1 with SmartID=1");
        Allure.step("Create user2 with SmartID=1");
        Allure.step("Put IP for user1 = 127.0.0.X");
        Allure.step("Put IP for user2 = 127.0.0.X");
        Allure.step("Create deposit with crypto FSD for user1");
        Allure.step("Make deposit with same parameters for user2");
        Allure.step("Create trade WITH Z pair AND opening time = X AND closing time = Y for user1");
        Allure.step("Create trade WITH Z pair AND opening time = X+5sec AND closing time = Y+5sec for user2");
        Allure.step("Execute test steps");
        Allure.step("Put in kafka incoming message with user1 to check this user");
        Allure.step("Make assertions:");
        Allure.step("Assert that we've got a correct message in kafka output topic");
    }
}
