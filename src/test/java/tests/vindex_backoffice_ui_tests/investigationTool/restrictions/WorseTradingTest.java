package tests.vindex_backoffice_ui_tests.investigationTool.restrictions;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateCrmTbAccountDataForUi;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.enums.Restriction.*;
import static helpers.database.AuHelper.cleanClientAudit;
import static helpers.database.CleanTableHelper.*;
import static helpers.database.DbHelper.*;
import static helpers.database.DbName.CLICKHOUSE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.*;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import helpers.data.ClientHelper;
import helpers.database.DbName;
import io.qameta.allure.AllureId;
import java.util.List;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

@Tag(TEAM_BACKOFFICE)
@Tag(LAYER_WEB)
class WorseTradingTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final CrmTbUserObject clientUser = generateUserByClient(client);
    private static final CrmTbAccountObject account1 = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account2 = generateCrmTbAccountDataForUi(client);
    private static final CrmTbAccountObject account3 = generateCrmTbAccountDataForUi(client);
    private static MtAccountObject mtAccount1;
    private static MtAccountObject mtAccount2;
    private static MtAccountObject mtAccount3;

    @BeforeAll
    static void setup() {
        account2.setAccount(client.getTradingAccount2());
        account3.setAccount(getRandomIntPositive());
        mtAccount1 = generateMtAccountByCrmTbAccount(account1);
        mtAccount2 = generateMtAccountByCrmTbAccount(account2);
        mtAccount3 = generateMtAccountByCrmTbAccount(account3);

        insertObjectsToDb(DbName.CLICKHOUSE, CRM_USER_TABLE_NAME, List.of(clientUser));
        insertObjectsToDb(DbName.CLICKHOUSE, CRM_TB_ACCOUNT_FOR_MT_TABLE_NAME, List.of(account1, account2, account3));
        insertObjectsToDb(DbName.CLICKHOUSE, CRM_TB_ACCOUNT_TABLE_NAME, List.of(account1, account2, account3));
        insertObjectsToDb(DbName.CLICKHOUSE, MT_ACCOUNT_TABLE_NAME, List.of(mtAccount1, mtAccount2, mtAccount3));
    }

    @AfterAll
    static void teardown() throws Exception {
        var ucid = client.getUcid();
        cleanUserRestrictionTradingEnv(ucid);
        cleanClientAudit(client.getUcid());
        deleteObjectsFromDb(CLICKHOUSE, CRM_USER_TABLE_NAME, "ucid", List.of(client.getUcid()));
    }

    @Test
    @AllureId("2034")
    @DisplayName("Worse trading: set levels per account, verify in drawer, then remove")
    void worseTradingLevelsSetAndRemoveTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(client.getUcid());
        restrictionPage.openWorseTradingEmptyRestrictionsTab();
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account1.account), "Low");
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account2.account), "Medium");
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account3.account), "High");
        restrictionPage.applyWorseTrading();
        boolean shown = restrictionPage.waitForWorseTradingAppearRestrictionInList(20_000);
        assertThat("Worse trading restriction should appear", shown, is(true));
        restrictionPage.openWorseTradingAppliedRestrictionsTab();
        restrictionPage.isPageLoaded();
        var level1 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account1.account));
        var level2 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account2.account));
        var level3 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account3.account));
        assertThat("Worse trading restriction should be set", level1, is("Low"));
        assertThat("Worse trading restriction should be set", level2, is("Medium"));
        assertThat("Worse trading restriction should be set", level3, is("High"));
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account1.account), "Not applied");
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account2.account), "Not applied");
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account3.account), "Not applied");
        restrictionPage.applyWorseTrading("Autotest comment remove");
        boolean hidden = restrictionPage.waitForWorseTradingDisappearRestrictionInList(20_000);
        assertThat("Worse trading restriction should disappear", hidden, is(true));
    }

    @Test
    @AllureId("2035")
    @DisplayName("Worse trading: set levels, change levels, verify updates in drawer, then remove")
    void worseTradingLevelsSetChangeAndRemoveTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        restrictionPage.navigate(client.getUcid());
        restrictionPage.openWorseTradingEmptyRestrictionsTab();
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account1.account), "Low");
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account3.account), "High");
        restrictionPage.applyWorseTrading();
        boolean shown = restrictionPage.waitForWorseTradingAppearRestrictionInList(20_000);
        assertThat("Worse trading restriction should appear", shown, is(true));
        restrictionPage.openWorseTradingAppliedRestrictionsTab();
        restrictionPage.isPageLoaded();
        var level1 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account1.account));
        var level3 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account3.account));
        assertThat("Worse trading restriction should be set", level1, is("Low"));
        assertThat("Worse trading restriction should be set", level3, is("High"));
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account1.account), "High");
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account3.account), "Low");
        restrictionPage.applyWorseTrading("Autotest comment change");
        shown = restrictionPage.waitForWorseTradingAppearRestrictionInList(20_000);
        assertThat("Worse trading restriction should appear", shown, is(true));
        restrictionPage.openWorseTradingAppliedRestrictionsTab();
        int i = 0;
        while ("Low".equals(level1)) {
            restrictionPage.isPageLoaded();
            level1 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account1.account));
            level3 = restrictionPage.getWorseTradingLevelForAccount(String.valueOf(account3.account));
            i++;
            if (i > 2) {
                break;
            }
        }
        assertThat("Worse trading restriction should be set", level1, is("High"));
        assertThat("Worse trading restriction should be set", level3, is("Low"));
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account1.account), "Not applied");
        restrictionPage.setWorseTradingLevelForAccount(String.valueOf(account3.account), "Not applied");
        restrictionPage.applyWorseTrading("Autotest comment remove");
        boolean hidden = restrictionPage.waitForWorseTradingDisappearRestrictionInList(20_000);
        assertThat("Worse trading restriction should disappear", hidden, is(true));
    }
}
