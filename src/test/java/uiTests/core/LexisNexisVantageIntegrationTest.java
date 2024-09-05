package uiTests.core;

import static helpers.database.MySqlHelper.makeQuery;
import static utils.ConfigFactory.*;
import static utils.Constants.*;
import static utils.Utils.getRandomEmail;
import static utils.Utils.getRandomInt;

import io.qameta.allure.AllureId;
import io.qameta.allure.Owner;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.*;
import pageObjects.vantageUserAccountPages.StageRegistrationHelperPage;
import pageObjects.vantageUserAccountPages.VantageUserAccountPage;
import uiTests.TestBaseE2E;

public class LexisNexisVantageIntegrationTest extends TestBaseE2E {

    // TODO Enable after resolving issue with access from gitlab runner
    @Disabled("Disabled due to lack of DB access from gitlab runner")
    @Test
    @Owner(OWNER_NIKOLAI_KORIAGIN)
    @Tag(TEAM_CORE)
    @Tag(STATUS_AUTOMATED)
    @Tag(FEATURE_LEXIS_NEXIS)
    @Tag(LAYER_API)
    @AllureId("")
    @DisplayName("Register and wait till LexisNexis event")
    public void LexisNexisRegistrationEventTest() throws InterruptedException, SQLException, ClassNotFoundException {
        StageRegistrationHelperPage stageRegistrationHelperPage = new StageRegistrationHelperPage(page);
        VantageUserAccountPage vantageUserAccountPage = new VantageUserAccountPage(page);
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
        stageRegistrationHelperPage.chooseRegulator(REGISTRATION_HELPER_REGULATOR);
        stageRegistrationHelperPage.fillWid(REGISTRATION_HELPER_WID);
        stageRegistrationHelperPage.chooseRegisterInterface(REGISTRATION_HELPER_REG_INTERFACE);
        stageRegistrationHelperPage.clickSendFormButton();

        // Pass form after account creation
        vantageUserAccountPage.checkPagePresent();
        vantageUserAccountPage.closeAlertWindow();
        vantageUserAccountPage.waitPersonalDetailsWindowPresented();
        vantageUserAccountPage.chooseGender();
        vantageUserAccountPage.selectDateOfBirth("1", "1", "1990");
        vantageUserAccountPage.fillPhoneNumber(getRandomInt().toString());
        vantageUserAccountPage.clickNextButton();
        vantageUserAccountPage.chooseCurrency();
        vantageUserAccountPage.acceptTerms();
        vantageUserAccountPage.clickNextButton();
        // Make db query
        ResultSet result = makeQuery(
                "SELECT raw_result FROM dev_m_regulator_global.tb_tmx_session_query WHERE raw_result LIKE '%"
                        + email
                        + "%'",
                30);
        String raw_result = result.getString("raw_result");
        System.out.println("raw_result: " + raw_result);

        // Assertions
        Assertions.assertTrue(raw_result.contains("\"account_email\":\"" + email + "\""));
        Assertions.assertTrue(raw_result.contains("\"account_date_of_birth\":\"19900101\""));
        Assertions.assertTrue(raw_result.contains("\"account_first_name\":\"" + firstName + "\""));
        Assertions.assertTrue(raw_result.contains("\"account_gender\":\"male\""));
        Assertions.assertTrue(raw_result.contains("\"account_last_name\":\"" + secondName + "\""));
        Assertions.assertTrue(raw_result.contains("\"event_type\":\"account_creation\""));
    }
}
