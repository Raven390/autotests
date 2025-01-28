package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.kafka.alerts.RuleAlert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.kafka.KafkaHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.sql.SQLException;

import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.kafka.alerts.RuleAlertFactory.generateRuleAlertByUcid;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static utils.Constants.*;

public class GeneralInfoTabTest extends TestBaseWeb {

    private static final KafkaHelper kafka = new KafkaHelper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(getRandomVantageClientAllFields());

    @BeforeAll
    public static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
        RuleAlert alert = generateRuleAlertByUcid(crmTbUser.ucid);
        kafka.produceMessage(alert.alertId, objectMapper.writeValueAsString(alert), KAFKA_TOPIC_ALERTS);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("559")
    @DisplayName("Verify all data is present in general tab")
    public void verifyGeneralInfoTabTest() {
        investigationPage.navigateToClient(crmTbUser.ucid);
        keycloackPage.loginAsCoreUser();
        alertsPage.waitForPageToLoad();
        generalPage.clickGeneralTabButton();
        assertThat("Assert that full name is correct", generalPage.getFullName(), equalTo(String.format("%s %s", crmTbUser.firstName, crmTbUser.lastName)));
        String registrationDateAgoPattern = "^(?:\\d+ year(?:s)? )?\\d+ month(?:s)?$";
        assertThat("Assert that registration date ago is correct", generalPage.getRegistrationDateAgo(), matchesPattern(registrationDateAgoPattern));
        assertThat("Assert that client id is correct", generalPage.getClientId(), equalTo(String.valueOf(crmTbUser.userId)));
        assertThat("Assert that registration date is correct", generalPage.getRegistrationDate(), equalTo(crmTbUser.createTime.split(" ")[0]));
        assertThat("Assert that regulator is correct", generalPage.getRegulator(), equalTo(crmTbUser.regulator));
        assertThat("Assert that gender is correct", generalPage.getGender(), equalTo(crmTbUser.gender));
        assertThat("Assert that date of birth is correct", generalPage.getDateOfBirth(), equalTo(crmTbUser.birthday));
        assertThat("Assert that country is correct", generalPage.getCountry(), equalTo(crmTbUser.country));
        assertThat("Assert that nationality is correct", generalPage.getNationality(), equalTo(crmTbUser.nationality));
        assertThat("Assert that encoded email is correct", generalPage.getEmailAddress(), equalTo("t***4@example.com"));
        assertThat("Assert that encoded phone number is correct", generalPage.getPhoneNumber(), equalTo("+1*********3"));
        assertThat("Assert that 2 factor auth is correct", generalPage.get2FactorAuth(), equalTo("Yes"));
        generalPage.verifyKycSectionIsVisible();
        assertThat("Assert that registration source ib is correct", generalPage.getRegistrationSourceIb(), equalTo(crmTbUser.ibId.toString()));
        assertThat("Assert that registration source cpa is correct", generalPage.getRegistrationSourceCpa(), equalTo(crmTbUser.cpaId.toString()));
        generalPage.clickShowHiddenDataButton();
        assertThat("Assert that email is correct", generalPage.getEmailAddress(), equalTo("test14@example.com"));
        assertThat("Assert that phone number is correct", generalPage.getPhoneNumber(), equalTo("+1810347493"));
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
    }
}
