package tests.vindex_backoffice_ui_tests.search;

import business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObject;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import business_objects.db.clickhouse.mt_account.MtAccountObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsoft.playwright.Page;
import helpers.data.ClientHelper;
import helpers.data.enums.Brand;
import helpers.data.enums.Regulator;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.*;
import page_objects.backoffice_pages.search.GeneralSearchElements;
import tests.TestBaseWeb;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateAdditionalStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_account.CrmTbAccountObjectFactory.generateStaticCrmTbAccountActive;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateStaticUserByClient;
import static business_objects.db.clickhouse.mt_account.MtAccountObjectFactory.generateMtAccountByCrmTbAccount;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.database.DbHelper.insertObjectsToDb;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static utils.Constants.*;
import static utils.Utils.insertCrmAccountsToDb;

class GeneralSearchTest extends TestBaseWeb {
    private static final ClientHelper client1;
    static {
        client1 = ClientHelper.builder().userId(161_605).uid("e5880ca5-8578-4a1e-969d-7a64716ca41f").brand(Brand.VANTAGE).regulator(Regulator.VFSC2).tradingAccount(161_605_001).tradingAccount2(161_605_002).serverId(42).build();
    }
    private static CrmTbUserObject crmTbUser1 = generateStaticUserByClient(client1);
    private static CrmTbAccountObject account11 = generateStaticCrmTbAccountActive(client1);
    private static CrmTbAccountObject account12 = generateAdditionalStaticCrmTbAccountActive(client1);
    private static MtAccountObject mtAccount11 = generateMtAccountByCrmTbAccount(account11);
    private static MtAccountObject mtAccount12 = generateMtAccountByCrmTbAccount(account12);

    private static final ClientHelper client2;
    static {
        client2 = ClientHelper.builder().userId(161_605).uid("e5880ca5-8578-4a1e-969d-7a64716ca41f").brand(Brand.VJP).regulator(Regulator.VFSC2).tradingAccount(161_605_001).tradingAccount2(161_605_002).serverId(24).build();
    }
    private static CrmTbUserObject crmTbUser2 = generateStaticUserByClient(client2);
    private static CrmTbAccountObject account21 = generateStaticCrmTbAccountActive(client2);
    private static CrmTbAccountObject account22 = generateAdditionalStaticCrmTbAccountActive(client2);
    private static MtAccountObject mtAccount21 = generateMtAccountByCrmTbAccount(account21);
    private static MtAccountObject mtAccount22 = generateMtAccountByCrmTbAccount(account22);

    public static final String CLIENT_FULL_NAME_FORMAT = "%s %s";
    public static final String ENCRYPTED_EMAIL_FIRST = "dUWiXVMG5K9niVudIef4Tff7jNArTmj6n9ZoGIdI89o=";
    private static final String PLAIN_EMAIL_FIRST = "test-search-email-1@test.com";

    @BeforeAll
    static void setup() throws ReflectiveOperationException, SQLException, JsonProcessingException {
        crmTbUser1.firstName = "General";
        crmTbUser1.lastName = "Searchman";
        crmTbUser1.registrationDate = "2025-02-21";
        crmTbUser1.email = ENCRYPTED_EMAIL_FIRST;
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser1);
        crmTbUser1.email = PLAIN_EMAIL_FIRST;
        insertCrmAccountsToDb(account11, account12);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount11, mtAccount12));

        crmTbUser2.firstName = "General";
        crmTbUser2.lastName = "Secondman";
        crmTbUser2.registrationDate = "2024-03-19";
        insertObjectToDb(CRM_USER_TABLE_NAME, crmTbUser2);
        insertCrmAccountsToDb(account21, account22);
        insertObjectsToDb(MT_ACCOUNT_TABLE_NAME, List.of(mtAccount21, mtAccount22));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Feature("BMS-938 Global search")
    @DisplayName("General Search error screen")
    @AllureId("1109")
    void errorScreenTest() {
        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        alertsPage.navigateToMain();
        generalSearch.openSearch();
        generalSearch.inputSearchText("12");
        generalSearch.inputSearchPressEnter();
        generalSearch.checkErrorScreen();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Feature("BMS-938 Global search")
    @DisplayName("General Search find client by id two clients with same id")
    @AllureId("1094")
    void findClientByIdTwoClientsTest() {
        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        alertsPage.navigateToMain();
        generalSearch.openSearch();
        generalSearch.inputSearchText(client1.getUserId());
        generalSearch.inputSearchPressEnter();
        generalSearch.findClientsCard(client1.getUserId(), client1.getBrand());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Feature("BMS-938 Global search")
    @DisplayName("General Search , search by clientId renders all elements")
    @AllureId("1097")
    void clientSearchCardHaveAllElementsTest() {
        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        alertsPage.navigateToMain();
        generalSearch.openSearch();
        generalSearch.inputSearchText(client1.getUserId());
        generalSearch.inputSearchPressEnter();
        generalSearch.checkClientsCard(client1.getUserId(), client1.getBrand(), crmTbUser1.firstName, crmTbUser1.lastName, crmTbUser1.country, crmTbUser1.registrationDate);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Feature("BMS-938 Global search")
    @DisplayName("General Search  Find account by account id two accounts with same id")
    @AllureId("1096")
    void findClientByAccountTwoClientsTest() {
        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        alertsPage.navigateToMain();
        generalSearch.openSearch();
        generalSearch.inputSearchText(client1.getTradingAccount());
        generalSearch.inputSearchPressEnter();
        generalSearch.findClientsCard(client1.getTradingAccount(), client1.getBrand());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Feature("BMS-938 Global search")
    @DisplayName("General Search , search by account renders all elements")
    @AllureId("1097")
    void clientSearchCardHaveAllElementsAccountTest() {
        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        alertsPage.navigateToMain();
        generalSearch.openSearch();
        generalSearch.inputSearchText(client1.getTradingAccount());
        generalSearch.inputSearchPressEnter();
        generalSearch.checkClientsCardAccount(client1.getTradingAccount(), client1.getBrand(), crmTbUser1.firstName, crmTbUser1.lastName, crmTbUser1.country, crmTbUser1.registrationDate, account11.platform);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Feature("BMS-938 Global search")
    @DisplayName("General Search , click by trading button in card open Trading tab in a new tab")
    @AllureId("1100")
    void openTradingTest() {
        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        alertsPage.navigateToMain();
        generalSearch.openSearch();
        generalSearch.inputSearchText(client1.getUserId());
        generalSearch.inputSearchPressEnter();
        Page newPage = context.waitForPage(() -> {
            generalSearch.clickTradingClientsCard(client1.getUserId(), client1.getBrand());
        });
        GeneralSearchElements newTab = new GeneralSearchElements(newPage);
        String expectedUrl = client1.getBrand().toLowerCase(Locale.ROOT) + "-" + client1.getUserId() + "/trading/operations";
        newTab.checkPageUrl(expectedUrl);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Feature("BMS-938 Global search")
    @DisplayName("General Search , click by connection search button in card open connection search tab in a new tab")
    @AllureId("1101")
    void openConnectionSearchTest() {
        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        alertsPage.navigateToMain();
        generalSearch.openSearch();
        generalSearch.inputSearchText(client1.getUserId());
        generalSearch.inputSearchPressEnter();
        Page newPage = context.waitForPage(() -> {
            generalSearch.clickConnectionSearchClientsCard(client1.getUserId(), client1.getBrand());
        });
        GeneralSearchElements newTab = new GeneralSearchElements(newPage);
        String expectedUrl = client1.getBrand().toLowerCase(Locale.ROOT) + "-" + client1.getUserId() + "/connections";
        newTab.checkPageUrl(expectedUrl);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Feature("BMS-938 Global search")
    @DisplayName("General Search , click by copy link button in card saves link to client page in buffer")
    @AllureId("1102")
    void copyLinkTest() {
        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        alertsPage.navigateToMain();
        generalSearch.openSearch();
        generalSearch.inputSearchText(client1.getUserId());
        generalSearch.inputSearchPressEnter();
        generalSearch.clickCopyButtonClientsCard(client1.getUserId(), client1.getBrand());
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @Feature("BMS-938 Global search")
    @DisplayName("General Search , click on card open clients base tab in a new tab")
    @AllureId("1099")
    void openClientPageTest() {
        sessionsTab.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        alertsPage.navigateToMain();
        generalSearch.openSearch();
        generalSearch.inputSearchText(client1.getUserId());
        generalSearch.inputSearchPressEnter();
        Page newPage = context.waitForPage(() -> {
            generalSearch.clickClientsCard(client1.getUserId(), client1.getBrand());
        });
        GeneralSearchElements newTab = new GeneralSearchElements(newPage);
        String expectedUrl = client1.getBrand().toLowerCase(Locale.ROOT) + "-" + client1.getUserId() + "/audit";
        newTab.checkPageUrl(expectedUrl);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1767")
    @DisplayName("Search client by exact email")
    void searchClientByExactEmail() {
        generalSearch.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalSearch.navigateToMain();
        generalSearch.waitForPageToLoad();
        generalSearch.openSearch();
        // Clear any previous search
        generalSearch.clearSearch();

        // Perform search by exact email
        generalSearch.inputSearchText(crmTbUser1.email);
        generalSearch.inputSearchPressEnter();
        // Wait until the client card appears
        generalSearch.waitForClientCard(crmTbUser1.ucid);

        // Verify the client card displays the correct name
        String displayedName = generalSearch.getClientFullName(crmTbUser1.ucid);
        assertThat(displayedName, is(CLIENT_FULL_NAME_FORMAT.formatted(crmTbUser1.firstName, crmTbUser1.lastName)));

        // Optionally click operations button to verify it's clickable

        // Click on the first result
        try (Page clientPage = generalSearch.clickCard(crmTbUser1.ucid)) {
            var url = clientPage.url();
            assertThat("Verify URL contains client UCID", url, containsString(crmTbUser1.ucid));
        }
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1768")
    @DisplayName("Verify no results for non-existent email")
    void searchClientByNonExistentEmail() {
        generalSearch.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        generalSearch.navigateToMain();
        generalSearch.waitForPageToLoad();
        generalSearch.openSearch();
        // Clear previous search
        generalSearch.clearSearch();

        // Search with a random non-existent email
        String randomEmail = "nonexistent_" + System.currentTimeMillis() + "@example.com";
        generalSearch.inputSearchText(randomEmail);
        generalSearch.inputSearchPressEnter();

        // Verify that no client card appears
        Assertions.assertFalse(
                generalSearch.isCardPresent(client1.getUcid()), "No client card should appear for non-existent email"
        );
    }
}
