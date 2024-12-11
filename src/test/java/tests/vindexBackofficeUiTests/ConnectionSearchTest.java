package tests.vindexBackofficeUiTests;

import io.qameta.allure.AllureId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tests.TestBaseWeb;

import static utils.Constants.LAYER_WEB;
import static utils.Constants.TEAM_BACKOFFICE;

public class ConnectionSearchTest extends TestBaseWeb {

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("Positive login test")
    void CSPageOpensTest() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("")
    @DisplayName("Check line width")
    void CSPageConnectionLinesStileTest() {
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
    void CSPageConnectionNodesHaveRightClientNamesTest() {
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
    void CSPageConnectionNodesHaveRightClientStatusTest() {
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
    void CSPageConnectionTableOpens() {
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
    void CSPageConnectionTableSort() {
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
    void CSPageConnectionTableSwitchesBackToGraph() {
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
    void CSPageConnectionSelectionSwitchView() {
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
    @DisplayName("Test the same user save highlighted state between different modes of the connection search when you click link button ")
    void CSPageConnectionSelectionLinkButton() {
        connectionPage.navigateMain();
        keycloackPage.loginWeb("dev", "123");
        connectionPage.navigateConnectionTab("infinox-424201");
        connectionPage.openConnectionTable();
        connectionPage.checkSelection("424202", "infinox-424202");
    }

}
