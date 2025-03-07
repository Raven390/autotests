package tests.vindexBackofficeUiTests;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import java.sql.SQLException;

import static helpers.database.BoHelper.cleanUserFraudsDb;
import static helpers.database.BoHelper.createUserFraudsDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static helpers.kafka.alerts.CreateSimpleAlert.createSimpleAlert;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.Constants.LAYER_WEB;
import static utils.Constants.TEAM_BACKOFFICE;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomUuidString;

public class ConnectionSearchTest extends TestBaseWeb {

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("315")
    @DisplayName("Check line width")
    void csPageConnectionLinesStileTest() {
        connectionPage.navigateMain();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkLineStyle("infinox-424201", "infinox-424202", 1.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424203", 16.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424204", 17.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424205", 33.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424206", 34.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424207", 49.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424208", 50.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424209", 66.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424210", 67.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424211", 83.0);
        connectionPage.checkLineStyle("infinox-424201", "infinox-424212", 84.0);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("311")
    @DisplayName("Check that connection node have right client name")
    void csPageConnectionNodesHaveRightClientNamesTest() {
        connectionPage.navigateMain();
        keycloackPage.loginAsAutotestUser();
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
        createSimpleAlert("infinox-424201", "HEDGING");
        connectionPage.navigateMain();
        keycloackPage.loginAsAutotestUser();
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
        keycloackPage.loginAsAutotestUser();
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
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionTable();
        connectionPage.verifyTableSorting();
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("520")
    @DisplayName("Connection Search connection search tab can switches between graph and table mode")
    void csPageConnectionTableSwitchesBackToGraphTest() {
        connectionPage.navigateMain();
        keycloackPage.loginAsAutotestUser();
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
        keycloackPage.loginAsAutotestUser();
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
        keycloackPage.loginAsAutotestUser();
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
        keycloackPage.loginAsAutotestUser();
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
        keycloackPage.loginAsAutotestUser();
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
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionCard("infinox-424213");
        connectionPage.ccCheckDirectConnectionRows("Connect Tenthman", "Type", "Same Identity");
        connectionPage.ccCheckDirectConnectionRows("Connect Tenthman", "Score", "0.84");
        connectionPage.ccCheckDirectConnectionRows("Connect Tenthman", "ipAddress", "connectionAttributeValue10-13");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("554")
    @DisplayName("Test connection card content General info")
    void csPageConnectionCardGeneralInfoContentTest() {
        connectionPage.navigateMain();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionCard("infinox-424213");
        connectionPage.ccCheckGeneralInfoRows("Brand", "Infinox");
        connectionPage.ccCheckGeneralInfoRows("Country", "Cyprus");
        connectionPage.ccCheckGeneralInfoRows("Email", "t***4@example.com");
//        connectionPage.ccCheckGeneralInfoRows("Phone", "***********");
        connectionPage.ccCheckGeneralInfoRows("IB", "1");
        connectionPage.ccCheckGeneralInfoRows("CPA", "2");
        connectionPage.ccCheckGeneralInfoRows("Registered", "2024-10-22");
        connectionPage.ccCheckGeneralInfoRows("Last login", "2025-01-03 13:58");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("547")
    @DisplayName("Test connection card content Summary")
    void csPageConnectionCardSummaryContentTest() {
        connectionPage.navigateMain();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionCard("infinox-424213");
        connectionPage.ccCheckSummaryRows("Trading", "26 closed deals");
        connectionPage.ccCheckSummaryRows("Total PNL", "35.5 USD");
        connectionPage.ccCheckSummaryRows("Deposit", "22.4 USD");
        connectionPage.ccCheckSummaryRows("Withdrawal", "56.1 USD");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("557")
    @DisplayName("Test connection card content Header")
    void csPageConnectionCardHeaderContentTest() {
        connectionPage.navigateMain();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionCard("infinox-424213");
        connectionPage.ccCheckHeaderClientName("Connect Threerteenhman");
        connectionPage.ccCheckHeaderClientId("424213");
        connectionPage.ccCheckHeaderConnectionLevel("2");
        connectionPage.ccCheckHeaderConnectionPoints("0.67");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("312")
    @DisplayName("Connection Search clients frauds must be taken from DB")
    void csGraphPageConnectionHaveFraudsFromDBTest() throws Exception {
        connectionPage.navigateMain();
        keycloackPage.loginAsAutotestUser();
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
        connectionPage.checkClientStatus("infinox-424204", "CPA abuse");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 15);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Bonus abuse");
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
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 16);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Loophole abuse");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 17);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "HFT abuse");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 18);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "News trader");
        cleanUserFraudsDb("infinox-424204");
        createUserFraudsDb("infinox-424204", 19);
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientStatus("infinox-424204", "Anomalous profit");
        cleanUserFraudsDb("infinox-424204");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("316")
    @DisplayName("Connection Search Name of clients must be taken from the DB")
    void csPageConnectionNodeHasNameFromDbTest() throws ReflectiveOperationException, SQLException {
        CrmTbUserObject testUser = new CrmTbUserObject(424_204, "infinox-424204", "Infinox", "FCA", "2024-10-23", "2024-10-23", "Connect", "Fourthman", "male", "1975-05-11", "Cyprus", "CY", "CY", "en", "RUS", "DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS", "BjrbbdAHkwhBFLnPclfvbg==", "996", "1", "2FA", "2", "1", "1", 1, 2, 3, "APPROVED", getCurrentTimestampDbFormat(), "2024-10-23 14:56:59", "2024-10-23 14:56:59", getRandomUuidString(), "nationalityId", getCurrentTimestampDbFormat());
        insertObjectToDb("vindex_test.crm___tb_user", testUser);
        connectionPage.navigateMain();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.checkClientName("infinox-424204", "Connect Fourthman");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("724")
    @DisplayName("Connection Search Connection Graph user can see data on Attribute Card")
    void csAttributeCardHaveDataFromDbTest() {

        connectionPage.navigateMain();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.ccClickAttributeChevron("infinox-424213");
        Allure.step("list of user connection attributes must unfolds");
        connectionPage.ccClickAttributeCardButton("infinox-424213", "ipAddress");
        connectionPage.checkAttributeCardTitle("ipAddress");
        connectionPage.checkAttributeCardSourceName("Connect Threerteenhman");
        connectionPage.checkAttributeCardConnectedName("connectionAttributeValue10-13", "Connect Tenthman");
        connectionPage.checkAttributeCardFieldsValues("connectionAttributeValue10-13", "Match", "exact");
        connectionPage.checkAttributeCardFieldsValues("connectionAttributeValue10-13", "Value", "sourceAttributeValue10-13");
        connectionPage.checkAttributeCardConnectedName("sourceAttributeValue13-15", "Connect Fifthteenhman");
        connectionPage.checkAttributeCardFieldsValues("sourceAttributeValue13-15", "Match", "exact");
        connectionPage.checkAttributeCardFieldsValues("sourceAttributeValue13-15", "Value", "connectionAttributeValue13-15");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("725")
    @DisplayName("Connection Search Connection Graph user can unmask data on Attribute Card")
    void csAttributeCardCanBeUnmaskedTest() {
        connectionPage.navigateMain();
        keycloackPage.loginAsAutotestUser();
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.ccClickAttributeChevron("infinox-424208");
        Allure.step("list of user connection attributes must unfolds");
        connectionPage.ccClickAttributeCardButton("infinox-424201", "phoneNumber");
        connectionPage.checkAttributeCardTitle("phoneNumber");
        connectionPage.checkThatMaskedTextIsVisible();
        connectionPage.toggleAttributeCardMask();
        connectionPage.checkThatMaskedTextIsNotVisible();
        connectionPage.toggleAttributeCardMask();
        connectionPage.checkThatMaskedTextIsVisible();
    }
}
