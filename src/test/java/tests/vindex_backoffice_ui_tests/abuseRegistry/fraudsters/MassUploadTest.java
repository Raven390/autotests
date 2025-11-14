package tests.vindex_backoffice_ui_tests.abuseRegistry.fraudsters;

import business_objects.db.abuse_registry_db.AbuserFraudType;
import business_objects.db.audit_service_db.Event;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import helpers.data.enums.*;
import helpers.database.ArHelper;
import helpers.database.DbName;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static helpers.data.enums.FraudSource.*;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.BoHelper.*;
import static helpers.database.DbHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.checkUserHaveRestrictionGeneral;
import static page_objects.backoffice_pages.investigationTool.RestrictionPage.cleanUserRestriction;
import static utils.Constants.*;
import static utils.Constants.LAYER_WEB;
import static utils.Utils.getCurrentTimestampSeconds;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Tag(ABUSE_REGISTRY)
class MassUploadTest extends TestBaseWeb {

    static ClientHelper client1 = new ClientHelper(313_101, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 313_101_001, 42);
    static ClientHelper client2 = new ClientHelper(313_102, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 313_102_001, 42);
    static ClientHelper client3 = new ClientHelper(313_103, "063cde3b-ea9d-48b5-8e2c-99f3d5f67999", Brand.VANTAGE, Regulator.VFSC2, 313_103_001, 42);

    @BeforeAll
    static void setup() throws Exception {
        CrmTbUserObject crmClient1 = generateStaticUserByClient(client1);
        CrmTbUserObject crmClient2 = generateStaticUserByClient(client2);
        CrmTbUserObject crmClient3 = generateStaticUserByClient(client3);

        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmClient1, crmClient2, crmClient3));
    }

    @Test
    @AllureId("1289")
    @DisplayName("Abuse registry full flow simple test")
    void abuseRegistryMassUploadSimpleFullFlowTest() throws Exception {

        cleanClientAudit(client1.getUcid(), client2.getUcid());
        deleteUserBO(client1.getUcid());
        deleteUserBO(client2.getUcid());
        deleteUserBO(client3.getUcid());
        cleanUserRestriction(client1.getUcid());
        cleanUserRestriction(client2.getUcid());
        cleanUserRestriction(client3.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client1.getUcid(), client2.getUcid(), client3.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.clickUploadByClientId();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString(), client2.getUserId().toString(), client3.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudTypeOld fraudTypeOld = FraudTypeOld.BONUS_ABUSE;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getDisplayName(), "Confirmed");
        String source = getRandomFraudSource().getDisplayName();
        fraudstersPage.selectFraudSource(source);
        fraudstersPage.clickAddRestrictionButton();
        Restriction restriction = Restriction.DEPOSITS;
        fraudstersPage.selectRestriction(restriction.getName());
        fraudstersPage.clickApplyselectedRestrictions();
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client1.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals("CONFIRMED", fraud.getStatus());
        Allure.step("Assert that record in ar.abuser_fraud_type have right fraud");
        assertEquals(fraudTypeOld.getKey(), fraud.getFraudTypeCode());
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(commentary, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());


        List<Event> events = getObjectsFromDB(DbName.POSTGRES, AUDIT_EVENT, "ucid='" + client1.getUcid() + "' and type ='FRAUD_REPORTED'", Event.class);

        Event event = events.getFirst();
        assertEquals("Batch operation. " + commentary, event.getComment());

        checkUserHaveRestrictionGeneral(client1.getUcid(), restriction.getId(), "APPLIED");

    }

    @Test
    @AllureId("1799")
    @Feature("BMS-2614 Fraud source select")
    @DisplayName("Mass upload source test Vindex")
    void sourceFieldTestVindex() throws Exception {

        cleanClientAudit(client1.getUcid());
        deleteUserBO(client1.getUcid());
        cleanUserRestriction(client1.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client1.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.clickUploadByClientId();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudTypeOld fraudTypeOld = FraudTypeOld.BONUS_ABUSE;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getDisplayName(), "Confirmed");
        String source = VINDEX.getDisplayName();
        fraudstersPage.selectFraudSource(source);
        fraudstersPage.clickAddRestrictionButton();
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client1.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals("CONFIRMED", fraud.getStatus());
        Allure.step("Assert that record in ar.abuser_fraud_type have right fraud");
        assertEquals(fraudTypeOld.getKey(), fraud.getFraudTypeCode());
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(commentary, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @Test
    @AllureId("1800")
    @Feature("BMS-2614 Fraud source select")
    @DisplayName("Mass upload source test RA Raise")
    void sourceFieldTestRaRaise() throws Exception {

        cleanClientAudit(client1.getUcid());
        deleteUserBO(client1.getUcid());
        cleanUserRestriction(client1.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client1.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.clickUploadByClientId();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudTypeOld fraudTypeOld = FraudTypeOld.BONUS_ABUSE;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getDisplayName(), "Confirmed");
        String source = RA_RAISE.getDisplayName();
        fraudstersPage.selectFraudSource(source);
        fraudstersPage.clickAddRestrictionButton();
        Restriction restriction = Restriction.DEPOSITS;
        fraudstersPage.selectRestriction(restriction.getName());
        fraudstersPage.clickApplyselectedRestrictions();
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client1.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals("CONFIRMED", fraud.getStatus());
        Allure.step("Assert that record in ar.abuser_fraud_type have right fraud");
        assertEquals(fraudTypeOld.getKey(), fraud.getFraudTypeCode());
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(commentary, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @Test
    @AllureId("1801")
    @Feature("BMS-2614 Fraud source select")
    @DisplayName("Mass upload source test Additional Review")
    void sourceFieldTestAdditionalReview() throws Exception {

        cleanClientAudit(client1.getUcid());
        deleteUserBO(client1.getUcid());
        cleanUserRestriction(client1.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client1.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.clickUploadByClientId();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudTypeOld fraudTypeOld = FraudTypeOld.BONUS_ABUSE;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getDisplayName(), "Confirmed");
        String source = ADDITIONAL_REVIEW.getDisplayName();
        fraudstersPage.selectFraudSource(source);
        fraudstersPage.clickAddRestrictionButton();
        Restriction restriction = Restriction.DEPOSITS;
        fraudstersPage.selectRestriction(restriction.getName());
        fraudstersPage.clickApplyselectedRestrictions();
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client1.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals("CONFIRMED", fraud.getStatus());
        Allure.step("Assert that record in ar.abuser_fraud_type have right fraud");
        assertEquals(fraudTypeOld.getKey(), fraud.getFraudTypeCode());
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(commentary, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @Test
    @AllureId("1802")
    @Feature("BMS-2614 Fraud source select")
    @DisplayName("Mass upload source test Insight")
    void sourceFieldTestInsight() throws Exception {

        cleanClientAudit(client1.getUcid());
        deleteUserBO(client1.getUcid());
        cleanUserRestriction(client1.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client1.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.clickUploadByClientId();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudTypeOld fraudTypeOld = FraudTypeOld.BONUS_ABUSE;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getDisplayName(), "Confirmed");
        String source = INSIGHT.getDisplayName();
        fraudstersPage.selectFraudSource(source);
        fraudstersPage.clickAddRestrictionButton();
        Restriction restriction = Restriction.DEPOSITS;
        fraudstersPage.selectRestriction(restriction.getName());
        fraudstersPage.clickApplyselectedRestrictions();
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client1.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals("CONFIRMED", fraud.getStatus());
        Allure.step("Assert that record in ar.abuser_fraud_type have right fraud");
        assertEquals(fraudTypeOld.getKey(), fraud.getFraudTypeCode());
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(commentary, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }

    @Test
    @AllureId("1803")
    @Feature("BMS-2614 Fraud source select")
    @DisplayName("Mass upload source test Frontend")
    void sourceFieldTestFrontend() throws Exception {

        cleanClientAudit(client1.getUcid());
        deleteUserBO(client1.getUcid());
        cleanUserRestriction(client1.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client1.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.clickUploadByClientId();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        fraudstersPage.clickAddFraudButton();
        FraudTypeOld fraudTypeOld = FraudTypeOld.BONUS_ABUSE;
        fraudstersPage.addSelectedFraudAdd(fraudTypeOld.getDisplayName(), "Confirmed");
        String source = FRONTEND.getDisplayName();
        fraudstersPage.selectFraudSource(source);
        fraudstersPage.clickAddRestrictionButton();
        Restriction restriction = Restriction.DEPOSITS;
        fraudstersPage.selectRestriction(restriction.getName());
        fraudstersPage.clickApplyselectedRestrictions();
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client1.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals("CONFIRMED", fraud.getStatus());
        Allure.step("Assert that record in ar.abuser_fraud_type have right fraud");
        assertEquals(fraudTypeOld.getKey(), fraud.getFraudTypeCode());
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(commentary, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }


    @Disabled("Deprecated. source now is mandatory")
    @Deprecated//source now is mandatory
    @Test
    @AllureId("1499")
    @Feature("BMS-1499 Add source to bulk uploading of fraudsters")
    @DisplayName("Abuse registry on adding fraud user can select source")
    void sourceFieldTest() throws Exception {

        cleanClientAudit(client1.getUcid(), client2.getUcid());
        deleteUserBO(client1.getUcid());
        deleteUserBO(client2.getUcid());
        deleteUserBO(client3.getUcid());
        cleanUserRestriction(client1.getUcid());
        cleanUserRestriction(client2.getUcid());
        cleanUserRestriction(client3.getUcid());
        ArHelper.deleteUserFromAbuseRegistry(client1.getUcid(), client2.getUcid(), client3.getUcid());

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.clickUploadByClientId();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(client1.getUserId().toString());
        String source = getRandomFraudSource().getDisplayName();
        fraudstersPage.selectFraudSource(source);
        fraudstersPage.clickAddFraudButton();
        FraudTypeOld fraudTypeOld = FraudTypeOld.BONUS_ABUSE;
        fraudstersPage.addSelectedFraudAddWithSource(fraudTypeOld.getDisplayName(), "Confirmed", source);
        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        fraudstersPage.verifySuccessMessageUpload();

        page.waitForTimeout(1000);

        List<AbuserFraudType> frauds = getObjectsFromDB(DbName.POSTGRES, AR_ABUSER_FRAUD_TYPE_TABLE_NAME, "ucid='" + client1.getUcid() + "'", AbuserFraudType.class);
        Allure.step("Assert that there only one record in ar.abuser_fraud_type");
        assertEquals(1, frauds.size());
        AbuserFraudType fraud = frauds.getFirst();
        Allure.step("Assert that record in ar.abuser_fraud_type have right status");
        assertEquals("CONFIRMED", fraud.getStatus());
        Allure.step("Assert that record in ar.abuser_fraud_type have right fraud");
        assertEquals(fraudTypeOld.getKey(), fraud.getFraudTypeCode());
        Allure.step("Assert that record in ar.abuser_fraud_type have commentary that you used in upload form");
        assertEquals(commentary, fraud.getComment());
        Allure.step("Assert that source in ar.abuser_fraud_type have source that you used in upload form");
        assertEquals(source, fraud.getFraudSource());
    }


}
