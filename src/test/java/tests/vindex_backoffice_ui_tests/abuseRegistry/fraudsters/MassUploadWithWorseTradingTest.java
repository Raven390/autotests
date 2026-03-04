package tests.vindex_backoffice_ui_tests.abuseRegistry.fraudsters;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_account_for_mt.crm_tb_account.CrmTbAccountForMtObjectFactory.generateAccountForMtByAccount;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Currency.USD;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampSeconds;
import static utils.Utils.getRandomIntPositive;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Restriction;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
@Tag(ABUSE_REGISTRY)
@Feature("BMS-2981-batch-worse-trading")
class MassUploadWithWorseTradingTest extends TestBaseWeb {

    private static final ClientHelper client3 = getRandomVantageClientAllFields();
    private static final ClientHelper client2 = getRandomVantageClientAllFields();
    private static final ClientHelper client1 = getRandomVantageClientAllFields();
    private static final CrmTbUserObject crmTbUser = generateUserByClient(client1);
    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client1);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client2);
    private static final CrmTbAccountObject account3 = generateCrmTbAccountDataForUi(client2);
    private static final CrmTbAccountObject account4 = generateCrmTbAccountDataForUi(client3);

    @BeforeAll
    static void setup() {
        account1.currency = USD.getIsoCode();
        account2.account = getRandomIntPositive();
        account3.account = getRandomIntPositive();
        account4.currency = USD.getIsoCode();
        account4.accountStatus = "Inactive";
        MtAccountObject mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        var mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        var mtAccount3 = generateMtAccountByCrmTbAccount(account3);
        var mtAccount4 = generateMtAccountByCrmTbAccount(account4);

        CrmTbUserObject crmClient2 = generateUserByClient(client2);
        CrmTbUserObject crmClient3 = generateUserByClient(client3);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, crmClient2, crmClient3));
        insertObjectsToDb(CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2, account3, account4));
        insertObjectsToDb(
                CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME,
                List.of(
                        generateAccountForMtByAccount(account1),
                        generateAccountForMtByAccount(account2),
                        generateAccountForMtByAccount(account3),
                        generateAccountForMtByAccount(account4)));
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2, mtAccount3, mtAccount4));
    }

    @AfterAll
    static void teardown() {
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client1.getUcid()));
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client2.getUcid()));
        deleteObjectFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client3.getUcid()));
    }

    @Test
    @AllureId("2086")
    @DisplayName("Batch upload by serverName and acc Worse trading test")
    void accServerNameTest() {

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.typeServerNameAcc(
                account1.serverName, account1.account.toString(), account2.serverName, account2.account.toString());

        fraudstersPage.clickAddRestrictionButton();
        Restriction restriction = Restriction.WORSE_TRADING;
        fraudstersPage.selectRestriction(restriction.getName());
        fraudstersPage.selectRestrictionWorseTradingLevel("Medium");
        fraudstersPage.clickApplySelectedRestrictions();

        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        assertTrue(
                fraudstersPage.isUploadSuccessMessageDisplayed(),
                "The upload success message was not displayed or the text is incorrect!");

        restrictionPage.navigate(client1.getUcid());
        restrictionPage.isPageLoaded();
        restrictionPage.openWorseTradingAppliedRestrictionsTab();
        restrictionPage.isPageLoaded();
        var level1 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account1.account));

        restrictionPage.navigate(client2.getUcid());
        restrictionPage.isPageLoaded();
        restrictionPage.openWorseTradingAppliedRestrictionsTab();
        restrictionPage.isPageLoaded();
        var level2 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account2.account));

        assertThat("Worse trading restriction should be set", level1, is("Medium"));
        assertThat("Worse trading restriction should be set", level2, is("Medium"));
    }

    @Test
    @AllureId("2087")
    @DisplayName("Batch upload by ucidList Worse trading test")
    void ucidListTest() {

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.clickUploadByClientId();
        fraudstersPage.selectBrandToUpload(Brand.VANTAGE.getDisplayName());
        fraudstersPage.typeClientsID(
                client1.getUserId().toString(), client2.getUserId().toString());

        fraudstersPage.clickAddRestrictionButton();
        Restriction restriction = Restriction.WORSE_TRADING;
        fraudstersPage.selectRestriction(restriction.getName());
        fraudstersPage.selectRestrictionWorseTradingLevel("Medium");
        fraudstersPage.clickApplySelectedRestrictions();

        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        assertTrue(
                fraudstersPage.isUploadSuccessMessageDisplayed(),
                "The upload success message was not displayed or the text is incorrect!");

        restrictionPage.navigate(client1.getUcid());
        restrictionPage.isPageLoaded();
        restrictionPage.openWorseTradingAppliedRestrictionsTab();
        restrictionPage.isPageLoaded();
        var level1 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account1.account));

        restrictionPage.navigate(client2.getUcid());
        restrictionPage.isPageLoaded();
        restrictionPage.openWorseTradingAppliedRestrictionsTab();
        restrictionPage.isPageLoaded();
        var level2 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account2.account));
        var level3 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account3.account));

        assertThat("Worse trading restriction should be set", level1, is("Medium"));
        assertThat("Worse trading restriction should be set", level2, is("Medium"));
        assertThat("Worse trading restriction should be set", level3, is("Medium"));
    }

    @Test
    @AllureId("2088")
    @DisplayName("Batch upload by serverName and acc INACTIVE ACCOUNT Worse trading test")
    void accServerNameInactiveTest() {

        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        fraudstersPage.navigateAbuseRegistryFraudsters();
        fraudstersPage.openUploadDrawer();
        fraudstersPage.typeServerNameAcc(
                account4.serverName, account4.account.toString(), account2.serverName, account2.account.toString());

        fraudstersPage.clickAddRestrictionButton();
        Restriction restriction = Restriction.WORSE_TRADING;
        fraudstersPage.selectRestriction(restriction.getName());
        fraudstersPage.selectRestrictionWorseTradingLevel("High");
        fraudstersPage.clickApplySelectedRestrictions();

        String commentary = "test" + getCurrentTimestampSeconds();
        fraudstersPage.fillCommentary(commentary);
        fraudstersPage.clickApplyUpload();
        assertTrue(
                fraudstersPage.isUploadSuccessMessageDisplayed(),
                "The upload success message was not displayed or the text is incorrect!");

        restrictionPage.navigate(client3.getUcid());
        restrictionPage.isPageLoaded();
        boolean hasWt = restrictionPage.waitForWorseTradingAppearRestrictionInList(100);

        restrictionPage.navigate(client2.getUcid());
        restrictionPage.isPageLoaded();
        restrictionPage.openWorseTradingAppliedRestrictionsTab();
        restrictionPage.isPageLoaded();
        var level2 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account2.account));

        assertThat("Worse trading restriction should not be set", hasWt, is(false));
        assertThat("Worse trading restriction should be set", level2, is("High"));
    }
}
