package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import com.microsoft.playwright.Page;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;

import static helpers.database.BoHelper.cleanUserFraudsDb;
import static helpers.database.BoHelper.createUserFraudsDb;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.LAYER_WEB;
import static utils.Constants.TEAM_BACKOFFICE;
import static utils.Utils.getCurrentTimestampDbFormat;

public class ConnectionSearchTest extends TestBaseWeb {

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("Positive login test")
    void csPageOpensTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("315")
    @DisplayName("Check line width")
    void csPageConnectionLinesStileTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkLineStyle("infinox-424201", "infinox-424202", "payoutId", 1.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424203", "payoutId", 16.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424204", "payoutId", 17.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424205", "payoutId", 33.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424206", "payoutId", 34.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424207", "payoutId", 49.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424208", "payoutId", 50.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424209", "payoutId", 66.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424210", "payoutId", 67.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424211", "payoutId", 83.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424212", "payoutId", 84.0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("311")
    @DisplayName("Check that connection node have right client name")
    void csPageConnectionNodesHaveRightClientNamesTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientNodeText("infinox-424201", "Connect Firstman");
        connectionPage.checkClientNodeText("infinox-424202", "Connect Secondman");
        connectionPage.checkClientNodeText("infinox-424203", "Connect Thrirdman");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("311")
    @DisplayName("Check that connection node have right client status")
    void csPageConnectionNodesHaveRightClientStatusTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientNodeText("infinox-424201", "Suspicious");
        connectionPage.checkClientNodeText("infinox-424202", "Normal");
        connectionPage.checkClientNodeText("infinox-424203", "Gap trading, Latency arbitrage");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("518")
    @DisplayName("Check that connection table opens")
    void csPageConnectionTableOpensTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionTable();
        connectionPage.connectionTableIsRendered();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("526")
    @DisplayName("Check that sorting works")
    void csPageConnectionTableSortTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionTable();
        connectionPage.testSortingLevel();
        connectionPage.testSortingConnection();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("520")
    @DisplayName("Connection Search connection search tab can switches between graph and table mode")
    void csPageConnectionTableSwitchesBackToGraphTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionTable();
        connectionPage.openConnectionGraph();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("521")
    @DisplayName("Test the same user save highlighted state between different modes of the connection search when you switches view mode")
    void csPageConnectionSelectionSwitchViewTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionTable();
        connectionPage.checkSelection("424202", "infinox-424202");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("527")
    @DisplayName("Test the same user save highlighted state between different modes of the connection search when you click link button")
    void csPageConnectionSelectionLinkButtonTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionTable();
        connectionPage.checkSelectionTransitByLinkButton("424202", "infinox-424202");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("523")
    @DisplayName("Connection Search. User can go to clients card from connection table")
    void csPageGoToClientCardTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionTable();
        Page newPage = context.waitForPage(() -> {
            connectionPage.linkToCard("424202");
        });
        int count = 0;
        while (newPage.url() == null && count < 50) {
            newPage.waitForTimeout(500);
            count += 1;
        }
        System.out.println("page URL is: " + newPage.url().toString());
        assertTrue(newPage.url().contains("infinox-424202"));
        newPage.close();

    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("556")
    @DisplayName("Connection search Connection Card user can open clients page from the connection card")
    void csPageGoToClientCardFromConnectionCardTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionCard("infinox-424202");
        Page newPage = context.waitForPage(() -> {
            connectionPage.clickConnectionLinkCc();
        });
        int count = 0;
        while (newPage.url() == null && count < 50) {
            newPage.waitForTimeout(500);
            count += 1;
        }
        System.out.println("page URL is: " + newPage.url().toString());
        assertTrue(newPage.url().contains("infinox-424202"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("555")
    @DisplayName("Test connection card content Direct Connections")
    void csPageConnectionCardDirectConnectionContentTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionCard("infinox-424213");
        connectionPage.ccCheckDirectConnectionRows("Connect Tenthman", "Type", "Same Identity");
        connectionPage.ccCheckDirectConnectionRows("Connect Tenthman", "Score", "0.8399999737739563");
        connectionPage.ccCheckDirectConnectionRows("Connect Tenthman", "documentNumber", "12121212");
        connectionPage.ccCheckDirectConnectionRows("Connect Fourteenhman", "phoneNumber", "131313");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("554")
    @DisplayName("Test connection card content General info")
    void csPageConnectionCardGeneralInfoContentTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionCard("infinox-424213");
        connectionPage.ccCheckGeneralInfoRows("Brand", "Infinox");
        connectionPage.ccCheckGeneralInfoRows("Country", "Cyprus");
        connectionPage.ccCheckGeneralInfoRows("Email", "t****4@example.com");
        connectionPage.ccCheckGeneralInfoRows("Phone", "***********");
        connectionPage.ccCheckGeneralInfoRows("IB", "1");
        connectionPage.ccCheckGeneralInfoRows("CPA", "2");
        connectionPage.ccCheckGeneralInfoRows("Registered", "2024-10-22 21:00:00");
        connectionPage.ccCheckGeneralInfoRows("Last active", "1970-01-01 00:00:00");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("547")
    @DisplayName("Test connection card content Summary")
    void csPageConnectionCardSummaryContentTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionCard("infinox-424213");
        connectionPage.ccCheckSummaryRows("Trading", "0 closed deal");
        connectionPage.ccCheckSummaryRows("Total PNL", "-");
        connectionPage.ccCheckSummaryRows("Deposit", "-");
        connectionPage.ccCheckSummaryRows("Withdrawal", "-");
        connectionPage.ccCheckSummaryRows("Fraud", "-");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("557")
    @DisplayName("Test connection card content Header")
    void csPageConnectionCardHeaderContentTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionCard("infinox-424213");
        connectionPage.ccCheckHeaderClientName("Connect Threerteenhman");
        connectionPage.ccCheckHeaderClientId("424213");
        connectionPage.ccCheckHeaderConnectionLevel("2");
        connectionPage.ccCheckHeaderConnectionPoints("0.6700000166893005");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("312")
    @DisplayName("Connection Search clients frauds must be taken from DB")
    void csGraphPageConnectionHaveFraudsFromDBTest() throws Exception {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        cleanUserFraudsDb("infinox-424204");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Normal");
        createUserFraudsDb("infinox-424204", 1);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Hedging");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 2);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Latency arbitrage");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 3);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Market manipulation");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 4);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Pricing errors");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 5);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Gap trading");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 6);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Swap arbitrage");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 7);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "CPA");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 8);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Affiliate abuse");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 9);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "RAF abuse");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 10);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Rebate churning");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 11);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Loss voucher abuse");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 12);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "NBP abuse");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 13);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "TLS abuse");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 14);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Potential abuse");
    }


    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("316")
    @DisplayName("Connection Search Name of clients must be taken from the DB")
    void csPageConnectionNodeHasNameFromDbTest() throws ReflectiveOperationException, SQLException {
        CrmTbUserObject testUser = new CrmTbUserObject(424_204, "infinox-424204", "Infinox", "FCA", "2024-10-23 14:56:59", "Connect", "Fourthman", "male", "1975-05-11", "Cyprus", "CY", "CY", "en", "RUS", "DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS", "BjrbbdAHkwhBFLnPclfvbg==", "996", "1", "2FA", "2", "1", "1", 1, 2, 3, "APPROVED", getCurrentTimestampDbFormat());
        deleteEntryFromDb("vindex_test.crm__tb_user", "user_id=424204");
        insertObjectToDb("vindex_test.crm__tb_user", testUser);
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientName("infinox-424204", "Connect Fourthman");
    }

}
