package tests.ruleEngineServiceTests;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static utils.Constants.*;

@Feature(FEATURE_RULE_ENGINE_SERVICE)
@Story(STORY_RULE_ENGINE_REGISTRATION_RULE)
@Tag(TEAM_CORE)
@Tag(LAYER_API)
@Tag(SUITE_RULE_ENGINE_SERVICE)
@Tag(TAG_MANUAL)
public class RegistrationRuleTest {

//    @Test
//    @DisplayName("Debug")
//    public void sometest() throws ReflectiveOperationException, SQLException {
//        LnSessionParsedObject object = registrationRuleExitEventEnd1LNDbObject();
//        insertObjectToDb("vindex_test.ln__session_parsed", object);
//        deleteEntryFromDb("vindex_test.ln__session_parsed", String.format("user_id=%s", object.userId));
//    }

    @Test
    @DisplayName("Registration rule exit Event_End_1")
    @AllureId("155")
    public void registrationRuleExitEventEnd1Test() {
        Allure.step("No toxic accounts linked");
        Allure.step("No different identity connections");
        Allure.step("IP country == address country");
        Allure.step("LN score != high");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_2")
    @AllureId("156")
    public void registrationRuleExitEventEnd2Test() {
        Allure.step("No toxic accounts linked");
        Allure.step("No different identity connections");
        Allure.step("IP country == address country");
        Allure.step("LN score == high");
        Allure.step("Generate alert");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_3")
    @AllureId("157")
    public void registrationRuleExitEventEnd3Test() {
        Allure.step("No toxic accounts linked");
        Allure.step("No different identity connections");
        Allure.step("IP country != address country");
        Allure.step("Generate alert");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_4")
    @AllureId("158")
    public void registrationRuleExitEventEnd4Test() {
        Allure.step("No toxic accounts linked");
        Allure.step("Different identity connections");
        Allure.step("Linked to IB account OR Same referrer");
        Allure.step("Generate alert");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_5")
    @AllureId("159")
    public void registrationRuleExitEventEnd5Test() {
        Allure.step("No toxic accounts linked");
        Allure.step("Different identity connections");
        Allure.step("Not linked to IB account OR Same referrer");
        Allure.step("LN score == Low");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_6")
    @AllureId("160")
    public void registrationRuleExitEventEnd6Test() {
        Allure.step("No toxic accounts linked");
        Allure.step("Different identity connections");
        Allure.step("Not linked to IB account OR Same referrer");
        Allure.step("LN score != Low");
        Allure.step("Set no bonus, promotions, CPA");
        Allure.step("Generate alert");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 all available alerts/restrictions")
    @AllureId("161")
    public void registrationRuleExitEventEnd7Version1Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("Any of the connected users is a CPA abuser");
        Allure.step("Set no rebates");
        Allure.step("LN == High");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("Connected user is a bonus abuser");
        Allure.step("Set no bonuses, promotions");
        Allure.step("Not Vjp");
        Allure.step("Block user");
        Allure.step("Generate alert");
        Allure.step("Is a voucher abuser");
        Allure.step("Set no vouchers");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("Set bad trading environment");
        Allure.step("News trader");
        Allure.step("Not Vjp");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("Is TLS");
        Allure.step("Block user");
        Allure.step("Generate alert");
        Allure.step("Swap abuse");
        Allure.step("Set no swap free option");
        Allure.step("Generate alert");
        Allure.step("Market manipulation");
        Allure.step("A-book the new account");
        Allure.step("Set no bonuses, promotions");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no rebates")
    @AllureId("162")
    public void registrationRuleExitEventEnd7Version2Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("Any of the connected users is a CPA abuser");
        Allure.step("Set no rebates");
        Allure.step("LN == Low");
        Allure.step("Generate alert");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no rebates + Set bad trading environment")
    @AllureId("163")
    public void registrationRuleExitEventEnd7Version3Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("Any of the connected users is a CPA abuser");
        Allure.step("Set no rebates");
        Allure.step("LN == High");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions (Bonus abuser)")
    @AllureId("164")
    public void registrationRuleExitEventEnd7Version4Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is a bonus abuser");
        Allure.step("Set no bonuses, promotions");
        Allure.step("Is Vjp");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions + Set bad trading environment")
    @AllureId("165")
    public void registrationRuleExitEventEnd7Version5Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is a bonus abuser");
        Allure.step("Set no bonuses, promotions");
        Allure.step("NOT Vjp");
        Allure.step("Low LN score");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions + Block user")
    @AllureId("166")
    public void registrationRuleExitEventEnd7Version6Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is a bonus abuser");
        Allure.step("Set no bonuses, promotions");
        Allure.step("NOT Vjp");
        Allure.step("High LN score");
        Allure.step("Block user");
        Allure.step("Generate alert");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers")
    @AllureId("167")
    public void registrationRuleExitEventEnd7Version7Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("Is a voucher abuser");
        Allure.step("Set no vouchers");
        Allure.step("Low LN score");
        Allure.step("Generate alert");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers + Set bad trading environment")
    @AllureId("168")
    public void registrationRuleExitEventEnd7Version8Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("Is a voucher abuser");
        Allure.step("Set no vouchers");
        Allure.step("High LN score");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Wipeout blacklist")
    @AllureId("169")
    public void registrationRuleExitEventEnd7Version9Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("Is a voucher abuser");
        Allure.step("Set no vouchers");
        Allure.step("High LN score");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no vouchers + Set bad trading environment")
    @AllureId("169")
    public void registrationRuleExitEventEnd7Version10Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("Is a News trader");
        Allure.step("Is Vjp");
        Allure.step("Wipeout blacklist");
        Allure.step("Generate alert");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set bad trading environment (News trader)")
    @AllureId("170")
    public void registrationRuleExitEventEnd7Version11Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("Is a News trader");
        Allure.step("NOT Vjp");
        Allure.step("Set bad trading environment");
        Allure.step("Generate alert");
        Allure.step("NOT TLS");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Block user (TLS)")
    @AllureId("171")
    public void registrationRuleExitEventEnd7Version12Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("Is TLS");
        Allure.step("Block user");
        Allure.step("NOT Swap abuse");
        Allure.step("NOT Market manipulation");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }

    @Test
    @DisplayName("Registration rule exit Event_End_7 only Set no bonuses, promotions (Market manipulation)")
    @AllureId("172")
    public void registrationRuleExitEventEnd7Version13Test() {
        Allure.step("Toxic accounts linked");
        Allure.step("All of the connected users are NOT CPA abusers");
        Allure.step("Connected user is NOT a bonus abuser");
        Allure.step("NOT a voucher abuser");
        Allure.step("NOT a News trader");
        Allure.step("NOT TLS");
        Allure.step("Market manipulation");
        Allure.step("A-book the new account");
        Allure.step("Set no bonuses, promotions");
        Allure.step("Generate alert");
        Allure.step("Fraud");
    }
}
