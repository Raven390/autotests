package tests.lexis_nexis_integration_tests;

import static utils.ConfigFactory.*;
import static utils.Constants.*;
import static utils.Utils.*;

import helpers.database.MySqlHelper;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Feature(FEATURE_LEXIS_NEXIS)
@Tag(TEAM_CORE)
class LexisNexisVantageIntegrationTest extends TestBaseWeb {

    @Disabled
    @Test
    @Tag(TAG_AUTOMATED)
    @Tag(LAYER_API)
    @DisplayName("Vantage. Registration Lexis Nexis event")
    @AllureId("57")
    void LexisNexisRegistrationEventTest() throws InterruptedException, SQLException, ClassNotFoundException {
        // Test data
        String email = getRandomEmail();
        String country = COUNTRY_MALAYSIA;
        String firstName = REGISTRATION_HELPER_FIRST_NAME;
        String secondName = REGISTRATION_HELPER_SECOND_NAME;
        String password = "Test1234!";
        String emailVerificationCode = "1234!";

        // Pass helper form
        stageRegistrationHelperPage.navigate(REGISTRATION_HELPER_LOGIN, REGISTRATION_HELPER_PASSWORD);
        stageRegistrationHelperPage.updateAction(BASE_URL_VANTAGE_ACCOUNT);
        stageRegistrationHelperPage.fillName(firstName);
        stageRegistrationHelperPage.fillSecondName(secondName);
        stageRegistrationHelperPage.chooseCountry(country);
        stageRegistrationHelperPage.fillEmail(email);
        stageRegistrationHelperPage.fillEmailVerificationCode(emailVerificationCode);
        stageRegistrationHelperPage.fillPassword(password);
        stageRegistrationHelperPage.chooseRegulator(REGISTRATION_HELPER_REGULATOR_VFSC2);
        stageRegistrationHelperPage.fillWid(REGISTRATION_HELPER_WID_VANTAGE);
        stageRegistrationHelperPage.chooseRegisterInterface(REGISTRATION_HELPER_INTERFACE);
        stageRegistrationHelperPage.clickSendFormButton();

        // Pass form after account creation
        vantageUserAccountPage.checkPagePresent();
        vantageUserAccountPage.closeAlertWindow();
        vantageUserAccountPage.waitPersonalDetailsWindowPresented();
        vantageUserAccountPage.chooseGender();
        vantageUserAccountPage.selectDateOfBirth("1", "1", "1990");
        vantageUserAccountPage.fillPhoneNumber(getRandomIntPositive().toString());
        vantageUserAccountPage.clickNextButton();
        vantageUserAccountPage.chooseCurrency();
        vantageUserAccountPage.acceptTerms();
        vantageUserAccountPage.clickNextButton();

        // Make db query
        ResultSet result = MySqlHelper.makeQuery("SELECT raw_result FROM dev_m_regulator_global.tb_tmx_session_query WHERE raw_result LIKE '%" + email + "%'", 30);
        String raw_result = result.getString("raw_result");
        System.out.println("raw_result: " + raw_result);
        System.out.println("user_id: " + result.getString("user_id"));
        System.out.println("ucid: " + result.getString("ucid"));

        // Assertions
        Assertions.assertTrue(raw_result.contains("\"account_email\":\"" + email + "\""));
        Assertions.assertTrue(raw_result.contains("\"account_date_of_birth\":\"19900101\""));
        Assertions.assertTrue(raw_result.contains("\"account_first_name\":\"" + firstName.toLowerCase() + "\""));
        Assertions.assertTrue(raw_result.contains("\"account_gender\":\"male\""));
        Assertions.assertTrue(raw_result.contains("\"account_last_name\":\"" + secondName.toLowerCase() + "\""));
        Assertions.assertTrue(raw_result.contains("\"event_type\":\"account_creation\""));
    }
}
