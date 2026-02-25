package tests.vindex_backoffice_ui_tests.investigationTool;

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
import static helpers.database.DbHelper.deleteObjectFromDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.cleanUserRestriction;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampSeconds;

import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.FraudType;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

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
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
    }

    @AfterEach
    void deleteFraudType() throws Exception {
        cleanClientAudit(client.getUcid());
        deleteUserBO(client.getUcid());
        cleanUserRestriction(client.getUcid());
        deleteUserFromAbuseRegistry(client.getUcid());
    }

    @Test
    @AllureId("1504")
    @DisplayName(
            "Fraud types that was separately added as potential and confirmed must be shown only as confirmed on FE")
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
        List<String> frauds = resolvePage.getPreviouslyReportedFraudItems();
        Allure.step("check that there only one fraud type displayed");
        assertEquals(1, frauds.size());
        Allure.step("check that name of the fraud is that that we reported on client");
        assertEquals(fraudType.getName(), frauds.getFirst());
    }

    @Test
    @AllureId("1804")
    @DisplayName("fraud management source test Vindex")
    void sourceVindexTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(CPA_ABUSE, POTENTIAL);
        String source = VINDEX.getDisplayName();
        resolvePage.selectFraudSourceManage(source);
        String comment = String.valueOf(getCurrentTimestampSeconds());
        resolvePage.applyFraudManagement(comment);

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                "ucid='" + client.getUcid() + "'",
                AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(comment, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @Test
    @AllureId("1805")
    @DisplayName("fraud management source test RA Raise")
    void sourceRaRaiseTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(CPA_ABUSE, POTENTIAL);
        String source = RA_RAISE.getDisplayName();
        resolvePage.selectFraudSourceManage(source);
        String comment = String.valueOf(getCurrentTimestampSeconds());
        resolvePage.applyFraudManagement(comment);

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                "ucid='" + client.getUcid() + "'",
                AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(comment, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @Test
    @AllureId("1806")
    @DisplayName("fraud management source test Additional Review")
    void sourceAdditionalReviewTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(CPA_ABUSE, POTENTIAL);
        String source = ADDITIONAL_REVIEW.getDisplayName();
        resolvePage.selectFraudSourceManage(source);
        String comment = String.valueOf(getCurrentTimestampSeconds());
        resolvePage.applyFraudManagement(comment);

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                "ucid='" + client.getUcid() + "'",
                AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(comment, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @Test
    @AllureId("1808")
    @DisplayName("fraud management source test Frontend")
    void sourceFrontendTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(CPA_ABUSE, POTENTIAL);
        String source = FRONTEND.getDisplayName();
        resolvePage.selectFraudSourceManage(source);
        String comment = String.valueOf(getCurrentTimestampSeconds());
        resolvePage.applyFraudManagement(comment);

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                "ucid='" + client.getUcid() + "'",
                AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(comment, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @Test
    @AllureId("1807")
    @DisplayName("fraud management source test Insight")
    void sourceInsightTest() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();
        resolvePage.addFraud(CPA_ABUSE, POTENTIAL);
        String source = INSIGHT.getDisplayName();
        resolvePage.selectFraudSourceManage(source);
        String comment = String.valueOf(getCurrentTimestampSeconds());
        resolvePage.applyFraudManagement(comment);

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                "ucid='" + client.getUcid() + "'",
                AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(comment, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @Test
    @AllureId("1885")
    @DisplayName("general role can manage Trading fraud type without active alerts - verify UI blocks")
    void generalRoleManagePaymentFraudWithoutAlerts() throws Exception {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(crmTbUser.ucid);
        investigationPage.clickSelectInvestigationType("Trading");
        alertsPage.waitForPageToLoad();
        resolvePage.openReportFraudForm();

        Allure.step("Add PAYMENT fraud type (CHARGEBACK)");
        resolvePage.reportFraud(CHARGEBACK, CONFIRMED);

        Allure.step("Verify that \"Previously reported\" section is visible");
        List<String> previouslyReportedFrauds = resolvePage.getPreviouslyReportedFraudItems();
        assertThat("Verify that previously reported section is displayed", previouslyReportedFrauds, hasSize(0));

        Allure.step("Verify that \"Detected fraud\" section is visible (selected fraud should be displayed)");
        String selectedFraud = resolvePage.getSelectedFraud();
        assertThat(
                "Verify that detected fraud section shows selected fraud",
                selectedFraud.contains("Chargeback"),
                is(true));

        Allure.step("Verify that 'Restrictions' section is visible1");
        List<String> restrictionsList = resolvePage.getRestrictionsList();
        assertThat("Verify that restrictions section is accessible", restrictionsList.isEmpty(), is(false));

        Allure.step("Verify that 'Suggested Deduction' block is displayed ");
        boolean isSuggestedDeductionVisible = resolvePage.isNoDeductionBlockVisible();
        assertThat("Verify that Suggested Deduction block is displayed", isSuggestedDeductionVisible, is(true));

        List<String> displayedSources = resolvePage.getDisplayedFraudSources();
        assertThat(
                "Verify that trading fraud sources are displayed",
                displayedSources,
                containsInAnyOrder(
                        getFraudSourceNames(getTradingFraudSourcesList()).toArray()));

        List<String> paymentOnlySources = getFraudSourceNames(getPaymentOnlyFraudSourcesList());
        for (String source : paymentOnlySources) {
            assertThat(
                    "Verify that payment-only fraud source is NOT displayed: " + source,
                    displayedSources,
                    not(hasItem(source)));
        }

        Allure.step("Verify that comment input is visible");
        resolvePage.fillCommentAndApply("Payment fraud management test");
        List<AbuserFraudType> abuserFraudTypes = getObjectsFromDB(
                DbName.POSTGRES,
                AR_ABUSER_FRAUD_TYPE_TABLE_NAME,
                "ucid = '%s'".formatted(client.getUcid()),
                AbuserFraudType.class);
        assertThat(abuserFraudTypes.size(), is(1));
        AbuserFraudType abuserFraudType = abuserFraudTypes.getFirst();
        assertThat("Fraud type inserted into AR DB", abuserFraudType.getFraudTypeCode(), is(CHARGEBACK.getCode()));
    }
}
