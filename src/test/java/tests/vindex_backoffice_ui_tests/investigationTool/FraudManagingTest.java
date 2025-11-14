package tests.vindex_backoffice_ui_tests.investigationTool;

import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.database.ArHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.io.IOException;
import java.util.List;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.api.AbuseRegistryHelper.addFraudForClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.FraudSource.*;
import static helpers.data.enums.FraudType.*;
import static helpers.data.enums.FraudTypeStatus.CONFIRMED;
import static helpers.data.enums.FraudTypeStatus.POTENTIAL;
import static helpers.database.ArHelper.deleteUserFromAbuseRegistry;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.deleteUserBO;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.cleanUserRestriction;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampSeconds;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class FraudManagingTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client);


    @BeforeAll
    static void setup() throws IOException {
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser);
    }

    @AfterAll
    static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteUserFromAbuseRegistry(client.getUcid());
        deleteUserBO(client.getUcid());
        cleanUserRestriction(client.getUcid());
    }

    @AllureId("1504")
    @DisplayName("Fraud types that was separately added as potential and confirmed must be shown only as confirmed on FE")
    @Test
    void overridedPotentialNotShown() throws IOException {

        FraudType fraudType = getRandomFraudType();

        Allure.step("report one fraud type to the client two times, first as potential, second as confirmed");
        addFraudForClient(client, fraudType, POTENTIAL, List.of());
        page.waitForTimeout(500);
        addFraudForClient(client, fraudType, CONFIRMED, List.of());
        page.waitForTimeout(500);

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        List<List<String>> frauds = resolvePage.getPreviouslyReportedFraudItems();
        Allure.step("check that there only one fraud type displayed");
        assertEquals(1, frauds.size());
        Allure.step("check that name of the fraud is that that we reported on client");
        assertEquals(fraudType.getName(), frauds.getFirst().getFirst());
    }

    @DisplayName("fraud management source test Vindex")
    @Test
    @AllureId("1804")
    void sourceVindexTest() throws Exception {

        cleanClientAudit(client.getUcid());
        deleteUserBO(client.getUcid());
        cleanUserRestriction(client.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraudManagement(CPA_ABUSE, POTENTIAL);
        String source = VINDEX.getDisplayName();
        resolvePage.selectFraudSourceManage(source);
        String comment = String.valueOf(getCurrentTimestampSeconds());
        resolvePage.applyFraudManagement(comment);

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(comment, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @DisplayName("fraud management source test RA Raise")
    @Test
    @AllureId("1805")
    void sourceRaRaiseTest() throws Exception {

        cleanClientAudit(client.getUcid());
        deleteUserBO(client.getUcid());
        cleanUserRestriction(client.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraudManagement(CPA_ABUSE, POTENTIAL);
        String source = RA_RAISE.getDisplayName();
        resolvePage.selectFraudSourceManage(source);
        String comment = String.valueOf(getCurrentTimestampSeconds());
        resolvePage.applyFraudManagement(comment);

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(comment, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @DisplayName("fraud management source test Additional Review")
    @Test
    @AllureId("1806")
    void sourceAdditionalReviewTest() throws Exception {

        cleanClientAudit(client.getUcid());
        deleteUserBO(client.getUcid());
        cleanUserRestriction(client.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraudManagement(CPA_ABUSE, POTENTIAL);
        String source = ADDITIONAL_REVIEW.getDisplayName();
        resolvePage.selectFraudSourceManage(source);
        String comment = String.valueOf(getCurrentTimestampSeconds());
        resolvePage.applyFraudManagement(comment);

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(comment, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @DisplayName("fraud management source test Insight")
    @Test
    @AllureId("1807")
    void sourceInsightTest() throws Exception {

        cleanClientAudit(client.getUcid());
        deleteUserBO(client.getUcid());
        cleanUserRestriction(client.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraudManagement(CPA_ABUSE, POTENTIAL);
        String source = INSIGHT.getDisplayName();
        resolvePage.selectFraudSourceManage(source);
        String comment = String.valueOf(getCurrentTimestampSeconds());
        resolvePage.applyFraudManagement(comment);

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(comment, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @DisplayName("fraud management source test Frontend")
    @Test
    @AllureId("1808")
    void sourceFrontendTest() throws Exception {

        cleanClientAudit(client.getUcid());
        deleteUserBO(client.getUcid());
        cleanUserRestriction(client.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraudManagement(CPA_ABUSE, POTENTIAL);
        String source = FRONTEND.getDisplayName();
        resolvePage.selectFraudSourceManage(source);
        String comment = String.valueOf(getCurrentTimestampSeconds());
        resolvePage.applyFraudManagement(comment);

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(comment, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }
}
