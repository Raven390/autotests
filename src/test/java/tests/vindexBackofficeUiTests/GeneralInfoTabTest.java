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
        generalTab.clickGeneralTabButton();
        assertThat("Assert that full name is correct", generalTab.getFullName(), equalTo(String.format("%s %s", crmTbUser.firstName, crmTbUser.lastName)));
        String registrationDateAgoPattern = "^(?:\\d+ year(?:s)? )?\\d+ month(?:s)?$";
        assertThat("Assert that registration date ago is correct", generalTab.getRegistrationDateAgo(), matchesPattern(registrationDateAgoPattern));
        assertThat("Assert that client id is correct", generalTab.getClientId(), equalTo(String.valueOf(crmTbUser.userId)));
        assertThat("Assert that registration date is correct", generalTab.getRegistrationDate(), equalTo(crmTbUser.createTime.split(" ")[0]));
        assertThat("Assert that regulator is correct", generalTab.getRegulator(), equalTo(crmTbUser.regulator));
        assertThat("Assert that gender is correct", generalTab.getGender(), equalTo(crmTbUser.gender));
        assertThat("Assert that date of birth is correct", generalTab.getDateOfBirth(), equalTo(crmTbUser.birthday));
        assertThat("Assert that country is correct", generalTab.getCountry(), equalTo(crmTbUser.country));
        assertThat("Assert that nationality is correct", generalTab.getNationality(), equalTo(crmTbUser.nationality));
        assertThat("Assert that encoded email is correct", generalTab.getEmailAddress(), equalTo("t***4@example.com"));
        assertThat("Assert that encoded phone number is correct", generalTab.getPhoneNumber(), equalTo("+1*********3"));
        assertThat("Assert that 2 factor auth is correct", generalTab.get2FactorAuth(), equalTo("Yes"));
        generalTab.verifyKycSectionIsVisible();
        assertThat("Assert that registration source ib is correct", generalTab.getRegistrationSourceIb(), equalTo(crmTbUser.ibId.toString()));
        assertThat("Assert that registration source cpa is correct", generalTab.getRegistrationSourceCpa(), equalTo(crmTbUser.cpaId.toString()));
        generalTab.clickShowHiddenDataButton();
        assertThat("Assert that email is correct", generalTab.getEmailAddress(), equalTo("test14@example.com"));
        assertThat("Assert that phone number is correct", generalTab.getPhoneNumber(), equalTo("+1810347493"));
    }

    @AfterAll
    public static void teardown() throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", crmTbUser.ucid));
        closeAlert(crmTbUser.ucid);
    }
}
