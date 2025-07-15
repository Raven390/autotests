package tests.vindex_backoffice_ui_tests.investigationTool.connectionSearch;

import business_objects.db.clickhouse.connection_table.ConnectionTableEntry;
import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;
import helpers.data.ClientHelper;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.*;
import tests.TestBaseWeb;

import java.util.List;

import static business_objects.db.clickhouse.connection_table.ConnectionTableEntryFactory.*;
import static business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObjectFactory.generateUserByClient;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.database.DbHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.Constants.*;
import static utils.Utils.waitForConnectionSearchToUpdate;

public class ConnectionSearchPreserveSettingsTest extends TestBaseWeb {

    private static final ClientHelper client = getRandomVantageClientAllFields();
    private static final ClientHelper connectedClient = getRandomVantageClientAllFields();

    @BeforeAll
    public static void setup() throws Exception {
        CrmTbUserObject crmTbUser = generateUserByClient(client);
        CrmTbUserObject connectedCrmTbUser = generateUserByClient(connectedClient);
        insertObjectsToDb(CRM_USER_TABLE_NAME, List.of(crmTbUser, connectedCrmTbUser));
        ConnectionTableEntry connectionTableEntry = getConnectionTableEntry(client, connectedClient);
        insertObjectToDb(CONNECTIONS_TABLE_NAME, connectionTableEntry);
        waitForConnectionSearchToUpdate(client);
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1028")
    @DisplayName("Verify connection search preservation of filters")
    public void verifyConnectionSearchPreserveSettingsTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        connectionPage.clickConnectionTabButton();
        connectionPage.clickFilterButton();
        connectionPage.selectBehaviorFilterOption("Normal");
        connectionPage.clickApplyFiltersButton();
        assertThat("Verify applied filters list", connectionPage.getAppliedFiltersList(), contains("Behavior"));
        connectionPage.openConnectionTable();
        generalTab.clickGeneralTabButton();
        generalTab.waitForPageToLoad();
        connectionPage.clickConnectionTabButton();
        connectionPage.verifyConnectionTableIsRendered();
        connectionPage.openConnectionGraph();
        assertThat("Verify applied filters are saved", connectionPage.getAppliedFiltersList(), contains("Behavior"));
    }

    @Test
    @Tag(TEAM_BACKOFFICE)
    @Tag(LAYER_WEB)
    @AllureId("1060")
    @DisplayName("Verify connection search preservation of zoom")
    public void verifyConnectionSearchPreserveZoomTest() {
        investigationPage.navigateEnterPage();
        keycloackPage.loginAsAutotestUser();
        investigationPage.navigateToClient(client.getUcid());
        alertsPage.waitForPageToLoad();
        connectionPage.clickConnectionTabButton();
        connectionPage.clickZoomInButton();
        assertThat("Verify zoom has changed", connectionPage.getZoomValue(), is("110%"));
        generalTab.clickGeneralTabButton();
        generalTab.waitForPageToLoad();
        connectionPage.clickConnectionTabButton();
        assertThat("Verify zoom is saved", connectionPage.getZoomValue(), is("110%"));
    }

    @AfterAll
    public static void teardown() throws Exception {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("ucid = '%s'", client.getUcid()));
        deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", client.getUcid()));
    }
}
